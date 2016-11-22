package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.RecommendMerchantForAdmin;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IMerchantService;
import cn.com.zhihetech.online.service.IRecMerchantForAdminService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by ydc on 16-8-15.
 */
@Controller
public class RecMerchantForAdminController extends SupportController {

    @Resource(name = "recMerchantForAdminService")
    private IRecMerchantForAdminService recMerchantService;
    @Resource(name = "merchantService")
    private IMerchantService merchantService;

    /**
     * 首页推荐商家数据列表显示页
     *
     * @return
     */
    @RequestMapping(value = "admin/recMerchantForAdmin")
    public String indexPage() {
        return "admin/recommend/recMerchantForAdmin";
    }

    /**
     * 首页推荐商家数据列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/recMerchantForAdmin/list")
    public PageData<RecommendMerchantForAdmin> getPageData(HttpServletRequest request) {
        IQueryParams queryParams = this.createQueryParams(request);
        queryParams.sort("recOrder", Order.ASC);
        return this.recMerchantService.getPageData(this.createPager(request), queryParams);
    }

    /**
     * 添加推荐商家
     *
     * @param recMerchant
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/recMerchantForAdmin/add")
    public ResponseMessage addRecMerchantForAdmin(RecommendMerchantForAdmin recMerchant) {
        long total = this.recMerchantService.getRecTotal(new GeneralQueryParams());
        if (total >= 5) {
            throw new SystemException("推荐店铺最多只能添加5个，若继续推荐此店铺，请删除现有的推荐！");
        }
        this.recMerchantService.add(recMerchant);
        return executeResult();
    }

    /**
     * 可供选择的商家
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/recMerchant/select")
    public PageData getMerchants(HttpServletRequest request) {
        List<Object> ids = this.recMerchantService.getProperty("merchant.merchantId", new Pager(), new GeneralQueryParams());
        IQueryParams queryParams = this.createQueryParams(request);
        queryParams.andEqual("permit", true)
                .andEqual("examinState", Constant.EXAMINE_STATE_EXAMINED_OK)
                .andNotIn("merchantId", ids)
                .sort("createDate", Order.DESC);
        return this.merchantService.getPageData(this.createPager(request), queryParams);
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/recMerchant/edit")
    public ResponseMessage updateRecMerchant(RecommendMerchantForAdmin recMerchant) {
        this.recMerchantService.update(recMerchant);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/recMerchantForAdmin/{id}/delete")
    public ResponseMessage delRecMerchantForAdmin(@PathVariable(value = "id") String id) {
        RecommendMerchantForAdmin recMerchant = this.recMerchantService.getById(id);
        if (recMerchant == null) {
            throw new SystemException("该条记录已经被删除了，请刷新后再试！");
        }
        this.recMerchantService.delete(recMerchant);
        return executeResult();
    }


}
