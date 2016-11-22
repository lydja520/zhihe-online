package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.RedEnvelop;
import cn.com.zhihetech.online.bean.RedEnvelopItem;
import cn.com.zhihetech.online.service.IActivityService;
import cn.com.zhihetech.online.service.IAdminService;
import cn.com.zhihetech.online.service.IRedEnvelopItemService;
import cn.com.zhihetech.online.service.IRedEnvelopService;
import cn.com.zhihetech.online.util.DateUtils;
import cn.com.zhihetech.online.util.ExportExcelUtil;
import cn.com.zhihetech.online.vo.ExportRedEnvelopStatistics;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2016/6/15.
 */
@Controller
public class RedEnvelopStatisticsController extends SupportController {
    @Resource(name = "redEnvelopService")
    private IRedEnvelopService redEnvelopService;
    @Resource(name = "adminService")
    private IAdminService adminService;
    @Resource(name = "activityService")
    private IActivityService activityService;
    @Resource(name = "redEnvelopItemService")
    private IRedEnvelopItemService redEnvelopItemService;

    @RequestMapping(value = "admin/redEnvelopStatistics")
    public String getredEnvelopStatisticsPage() {
        return "admin/redEnvelopStatistics";
    }

    @RequestMapping(value = "admin/userRedEnvelop")
    public String userRedEnvelopPage() {
        return "admin/userRedEnvelop";
    }


    /**
     * 根据查询条件得到所有的红包
     * @param request
     * @param merchName
     * @param activitName
     * @param initTime
     * @param endTime
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/redEnvelopStatistics/list")
    public PageData<RedEnvelop> getRedEnvelopAll(HttpServletRequest request, String merchName, String activitName, String initTime, String endTime) {
        IQueryParams queryParams = createQueryParams(request);
        if(!StringUtils.isEmpty(this.getCurrentMerchatId(request))){
            queryParams.andEqual("merchant.merchantId", this.getCurrentMerchatId(request));
        }
        if (StringUtils.isEmpty(request.getParameter("sort"))) {
            queryParams.sort("createDate", Order.DESC);
        }
        if (!StringUtils.isEmpty(merchName)) {
            queryParams.andAllLike("merchant.merchName", merchName);
        }
        if (!StringUtils.isEmpty(activitName)) {
            queryParams.andAllLike("activity.activitName", activitName);
        }
        if (!StringUtils.isEmpty(initTime)) {
            queryParams.andMoreAndEq("createDate", DateUtils.getStartDateTimeWithDate(DateUtils.String2Date(initTime)));
        }
        if (!StringUtils.isEmpty(endTime)) {
            queryParams.andLessAndEq("createDate", DateUtils.getEndDateTimeWithDate(DateUtils.String2Date(endTime)));
        }
        return this.redEnvelopService.getPageData(createPager(request), queryParams);

    }

    /**
     * 通过红包Id得到红包对象，抢红包用户信息，统计
     * @param request
     * @param envelopId
     * @return
     */
    @RequestMapping(value = "admin/api/userRedEnvelop/{id}")
    public ModelAndView getUserRedEnvelop(HttpServletRequest request, @PathVariable(value = "id") String envelopId) {
        ModelAndView modelAndView = new ModelAndView("admin/userRedEnvelop");

        IQueryParams queryParams = createQueryParams(request);
        queryParams.andEqual("redEnvelop.envelopId", envelopId);
        queryParams.andEqual("received", true);

        List<RedEnvelopItem> redEnvelopItemData = this.redEnvelopItemService.getAllByParams(null, queryParams);
        RedEnvelop currentRedEnvelop = this.redEnvelopService.getById(envelopId);
        float receivedTotalMoney = 0;
        int receivedTotal = 0;
        float surplusMoney = 0;

        if (redEnvelopItemData.size()!=0) {
            receivedTotal = redEnvelopItemData.size();
            for (RedEnvelopItem redEnvelopItem : redEnvelopItemData) {
                BigDecimal b1 = new BigDecimal(Float.toString(receivedTotalMoney));
                BigDecimal b2 = new BigDecimal(Float.toString(redEnvelopItem.getAmountOfMoney()));
                receivedTotalMoney = b1.add(b2).floatValue();
            }
        }
        BigDecimal b3 = new BigDecimal(Float.toString(currentRedEnvelop.getTotalMoney()));
        BigDecimal b4 = new BigDecimal(Float.toString(receivedTotalMoney));
        surplusMoney = b3.subtract(b4).floatValue();
        surplusMoney = new BigDecimal(surplusMoney).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();

        modelAndView.addObject("rows", redEnvelopItemData);
        modelAndView.addObject("currentRedEnvelop", currentRedEnvelop);
        modelAndView.addObject("receivedTotalMoney", receivedTotalMoney);
        modelAndView.addObject("receivedTotal", receivedTotal);
        modelAndView.addObject("surplusMoney", surplusMoney);
        return modelAndView;
    }

    /**
     * 根据查询参数得到所有的红包统计excel对象
     * @param request
     * @param response
     * @param merchName
     * @param activitName
     * @param initTime
     * @param endTime
     */
    @RequestMapping(value = "admin/api/redEnvelopStatistics/export")
    public void exportRedEnvelopStatisticsToExcel(HttpServletRequest request,HttpServletResponse response,
                                                                                        String merchName, String activitName, String initTime, String endTime){
        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + System.currentTimeMillis() + ".xls");

        ExportExcelUtil<ExportRedEnvelopStatistics> exp = new ExportExcelUtil<>();
        String[] headers = {"商家名称","活动名称","红包创建时间",
                "红包是否充值","是否发出","红包金额","红包裂变个数","红包已领取金额","已领取个数","红包剩余金额","红包剩余个数"};

        IQueryParams  queryParams = new GeneralQueryParams();
        if(!StringUtils.isEmpty(this.getCurrentMerchatId(request))){
            queryParams.andEqual("merchant.merchantId", this.getCurrentMerchatId(request));
        }
        if (StringUtils.isEmpty(request.getParameter("sort"))) {
            queryParams.sort("createDate", Order.DESC);
        }
        if (!StringUtils.isEmpty(merchName)) {
            queryParams.andAllLike("merchant.merchName", merchName);
        }
        if (!StringUtils.isEmpty(activitName)) {
            queryParams.andAllLike("activity.activitName", activitName);
        }
        if (!StringUtils.isEmpty(initTime)) {
            queryParams.andMoreAndEq("createDate", DateUtils.getStartDateTimeWithDate(DateUtils.String2Date(initTime)));
        }
        if (!StringUtils.isEmpty(endTime)) {
            queryParams.andLessAndEq("createDate", DateUtils.getEndDateTimeWithDate(DateUtils.String2Date(endTime)));
        }

        List<ExportRedEnvelopStatistics> exportRedEnvelopStatisticses = this.redEnvelopService.getExportRedEnvelopList(queryParams);
        String sheetName = null;
        if (StringUtils.isEmpty(this.getCurrentMerchatId(request))) {
            sheetName = "红包统计";
        } else {
            sheetName = exportRedEnvelopStatisticses.get(0).getMerchName();
        }
        HSSFWorkbook hssfWorkbook = null;
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            hssfWorkbook = new HSSFWorkbook();
            exp.exportExcel(hssfWorkbook, sheetName, headers, exportRedEnvelopStatisticses);
            hssfWorkbook.write(out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(hssfWorkbook != null){
                try {
                    hssfWorkbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
