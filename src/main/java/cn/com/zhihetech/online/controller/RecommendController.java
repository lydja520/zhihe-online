package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.bean.Recommend;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.service.IGoodsService;
import cn.com.zhihetech.online.service.IRecommendService;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import com.sun.istack.internal.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by ShenYunjie on 2016/1/11.
 */
@Controller
public class RecommendController extends SupportController {

    @Resource(name = "recommendService")
    private IRecommendService recommendService;
    @Resource(name = "goodsService")
    private IGoodsService goodsService;

    @RequestMapping(value = "admin/merchant/recommend", method = RequestMethod.GET)
    public String indexPage() {
        return "admin/merchant/recommend";
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/merchant/{merchantId}/recommends")
    public PageData<Recommend> getRecommendPage(HttpServletRequest request, @PathVariable(value = "merchantId") @NotNull String merchantId) {
        IQueryParams queryParams = createQueryParams(request).andEqual("merchant.merchantId", merchantId);
        if (!queryParams.sortContainsKey("orderIndex")) {
            queryParams.sort("orderIndex", Order.ASC);
        }
        return this.recommendService.getPageData(createPager(request), queryParams);
    }

    @ResponseBody
    @RequestMapping("admin/api/merchant/{merchantId}/ableRecommendGoodses")
    public PageData<Goods> getAbleRecommendGoodses(HttpServletRequest request, @PathVariable(value = "merchantId") @NotNull String merchantId) {
        return this.goodsService.getAbleRecommendGoodsesByMerchantId(createQueryParams(request), createPager(request), merchantId);
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/recommend/add", method = RequestMethod.POST)
    public ResponseMessage addRecommend(Recommend recommend) {
        this.recommendService.add(recommend);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/recommend/{recommendId}/delete")
    public ResponseMessage deleteRecommend(@PathVariable("recommendId") @NotNull String recommendId) {
        this.recommendService.deleteRecommendById(recommendId);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/recommend/{recommendId}/edit")
    public ResponseMessage editRecommend(@PathVariable("recommendId") @NotNull String recommendId, int orderIndex, String reason) {
        this.recommendService.updateOderAndReasonById(recommendId,orderIndex,reason);
        return executeResult();
    }
}
