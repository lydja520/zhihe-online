package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.bean.GoodsScore;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.dao.IGoodsDao;
import cn.com.zhihetech.online.service.IEvaluateManagerService;
import cn.com.zhihetech.online.service.IGoodsScoreService;
import cn.com.zhihetech.online.service.IGoodsService;
import cn.com.zhihetech.online.service.IOrderService;
import cn.com.zhihetech.online.vo.OrderAndOrderEvaluate;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by YangDaiChun on 2016/4/5.
 */
@Controller
public class EvaluateManagerController extends SupportController {

    @Resource(name = "evaluateManagerService")
    private IEvaluateManagerService evaluateManagerService;
    @Resource(name = "orderService")
    private IOrderService orderService;
    @Resource(name = "goodsScoreService")
    private IGoodsScoreService goodsScoreService;
    @Resource(name = "goodsService")
    private IGoodsService goodsService;

    @RequestMapping(value = "admin/orderEvaluate")
    public String orderEvaluate() {
        return "admin/evaluate/orderEvaluate";
    }

    @RequestMapping(value = "admin/orderEvaluateContent")
    public ModelAndView orderEvaluateContent(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("admin/evaluate/orderEvaluateContent");
        String merchantId = this.getCurrentMerchatId(request);
        PageData<OrderAndOrderEvaluate> orderAndEvaluatePgData = this.orderService.getOrderAndOrderEvaluate(merchantId, this.createPager(request), this.createQueryParams(request));
        mv.addObject("orderAndEvaluatePgData", orderAndEvaluatePgData);
        return mv;
    }

    @RequestMapping(value = "admin/merchantRank")
    public String merchantRank() {
        return "admin/evaluate/merchantRank";
    }

    @RequestMapping(value = "admin/goodsEvaluate")
    public String goodsEvaluate() {
        return "admin/evaluate/goodsEvaluate";
    }

    @RequestMapping(value = "admin/goods/{goodsId}/evaluateDetail")
    public ModelAndView goodsEvaluateDetail(@PathVariable(value = "goodsId") String goodsId) {
        ModelAndView mv = new ModelAndView("admin/evaluate/goodsEvaluateDetail");
        mv.addObject("goodsId", goodsId);
        return mv;
    }

    @RequestMapping(value = "admin/goods/{goodsId}/evaluateDetailContent")
    public ModelAndView goodsEvaluateDetailContent(@PathVariable(value = "goodsId") String goodsId, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("admin/evaluate/goodsEvaluateDetailContent");
        String merchantId = this.getCurrentMerchatId(request);
        List<GoodsScore> goodsScores = this.goodsScoreService.
                getGoodsScoreBygoodsId(merchantId, goodsId, this.createPager(request), this.createQueryParams(request));
        Goods goods = this.goodsService.getById(goodsId);
        mv.addObject("goods", goods);
        mv.addObject("goodsScores", goodsScores);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/goodsEvaluate/list")
    public PageData<Goods> getEvaluateGoods(HttpServletRequest request) {
        IQueryParams queryParams = this.createQueryParams(request);
        String merchantId = this.getCurrentMerchatId(request);
        queryParams.andEqual("merchant.merchantId", merchantId)
                .andEqual("deleteState", false)
                .andEqual("examinState", Constant.EXAMINE_STATE_EXAMINED_OK)
                .sort("volume", Order.DESC);
        return this.goodsService.getPageData(this.createPager(request), queryParams);
    }

    @RequestMapping(value = "admin/allGoodsEvaluate")
    public String allGoodsEvaluate() {
        return "admin/evaluate/allGoodsEvaluate";
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/allGoodsEvaluate/list")
    public PageData<GoodsScore> getAllGoodsScore(HttpServletRequest request) {
        IQueryParams queryParams = this.createQueryParams(request);
        String merchantId = this.getCurrentMerchatId(request);
        queryParams.andEqual("order.orderState", Constant.ORDER_STATE_ALREADY_EVALUATE).andEqual("order.merchant.merchantId", merchantId).sort("createDate",Order.DESC);
        PageData<GoodsScore> goodsScorePageData = this.goodsScoreService.getAllPageData(this.createPager(request), queryParams);
        return goodsScorePageData;
    }

}
