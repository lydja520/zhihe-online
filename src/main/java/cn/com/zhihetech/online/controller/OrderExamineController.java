package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.OrderExamine;
import cn.com.zhihetech.online.commons.AppConfig;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IMerchantBillService;
import cn.com.zhihetech.online.service.IOrderExamineService;
import cn.com.zhihetech.online.util.StringUtils;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by ShenYunjie on 2016/6/29.
 */
@Controller
public class OrderExamineController extends SupportController {

    @Resource(name = "orderExamineService")
    private IOrderExamineService orderExamineService;
    @Resource(name = "merchantBillService")
    private IMerchantBillService merchantBillService;

    /**
     * 订单审核（对账）
     *
     * @return
     */
    @RequestMapping("admin/orderExamine")
    public String pageIndex() {
        return "admin/billManager/orderExamine";
    }

    /**
     * 获取对账单列表
     *
     * @param request
     * @param batchCode
     * @param payType
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/orderExamine/list")
    public PageData<OrderExamine> orderExamineList(HttpServletRequest request, String batchCode, String payType) {
        IQueryParams queryParams = createQueryParams(request).andEqual("payType", payType);
        if (!StringUtils.isEmpty(batchCode)) {
            queryParams.andEqual("batchCode", batchCode);
        }
        return this.orderExamineService.getPageData(createPager(request), queryParams);
    }

    /**
     * 根据账单批次和支付方式获取支付的最小时间和最大时间
     *
     * @param batchCode
     * @param payType
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/orderExamine/minAndMaxDate")
    public ResponseMessage getMinAndMaxDateByBatchCodeAndPayType(String batchCode, String payType) {
        Map<String, String> dateMap = this.merchantBillService.getMinAndMaxPayDateByBatchCodeAndPayType(batchCode, payType);
        return executeResult(200, "获取成功！", dateMap);
    }

    /**
     * 获取账单批次
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/merchantBill/batchCode/list")
    public List<Map<String, String>> getMerchantBatchCode() {
        return this.merchantBillService.getMerchantBillBatchCodes();
    }

    /**
     * 上传支付账单
     *
     * @param billFile  账单文件
     * @param batchCode 账单批次
     * @param payType   支付方式
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/payBill/upload")
    public ResponseMessage uploadPayBill(@RequestParam(value = "billFile", required = true) MultipartFile billFile, String batchCode, String payType) {
        if (!payType.equals(AppConfig.PayType.ALI_PAY) && !payType.equals(AppConfig.PayType.WX_PAY)) {
            throw new SystemException("支付方式只能传递'wx'和'alipay'两种形式！");
        }
        Workbook workbook = null;
        try {
            workbook = new HSSFWorkbook(billFile.getInputStream());
            this.orderExamineService.executeCreateExamineBill(workbook, batchCode, payType);
        } catch (IOException e) {
            throw new SystemException("账单上传失败！");
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return executeResult();
    }
}
