package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.*;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.dao.IActivityGoodsDao;
import cn.com.zhihetech.online.dao.IGoodsDao;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.*;
import cn.com.zhihetech.online.vo.SkuListInfo;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by YangDaiChun on 2016/3/4.
 */
@Controller
public class ActivityGoodsController extends SupportController {

    @Resource(name = "activityGoodsService")
    private IActivityGoodsService activityGoodsService;
    @Resource(name = "adminService")
    private IAdminService adminService;
    @Resource(name = "activityService")
    private IActivityService activityService;
    @Resource(name = "goodsService")
    private IGoodsService goodsService;
    @Resource(name = "skuService")
    private ISkuService skuService;

    /**
     * 活动商品列表 页面
     *
     * @param activitId
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/merchActivity/{activityId}/activityGoods")
    public ModelAndView activityGoodsIndexPage(@PathVariable(value = "activityId") String activitId, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("admin/activityGoods");
        Merchant merchant = this.adminService.getMerchant(getCurrentAdmin(request));
        Activity activity = this.activityService.getById(activitId);
        modelAndView.addObject("merchant", merchant);
        modelAndView.addObject("activity", activity);
        return modelAndView;
    }

    /**
     * 活动根据活动id和商家id活动活动商品列表
     *
     * @param activitId
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/activity/{activityId}/activityGoods/list")
    public PageData<ActivityGoods> getActivityGoodsPagedata(@PathVariable(value = "activityId") String activitId, HttpServletRequest request) {
        if (StringUtils.isEmpty(activitId)) {
            throw new SystemException("查询参数不足");
        }
        IQueryParams queryParams = this.createQueryParams(request);
        Merchant merchant = this.adminService.getMerchant(getCurrentAdmin(request));
        queryParams.andEqual("activity.activitId", activitId).andEqual("goods.merchant.merchantId", merchant.getMerchantId());
        PageData<ActivityGoods> activityGoodsPageData = this.activityGoodsService.getPageData(this.createPager(request), queryParams);
        for (ActivityGoods activityGoods : activityGoodsPageData.getRows()) {
            String skuValue = this.skuService.getSkuValueBySkuId(activityGoods.getSku().getSkuId());
            activityGoods.getSku().setSkuValue(skuValue);
            String merchantId = activityGoods.getMerchant().getMerchantId();
            activityGoods.setMerchant(new Merchant(merchantId));
            String _activitId = activityGoods.getActivity().getActivitId();
            Activity activity = new Activity();
            activity.setActivitId(_activitId);
            activityGoods.setActivity(activity);
        }
        return activityGoodsPageData;
    }

    /**
     * 活动商品添加
     *
     * @param activityGoods
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/activityGoodsAdd")
    public ResponseMessage addActivityGoods(ActivityGoods activityGoods, HttpServletRequest request) {
        Merchant merchant = this.adminService.getMerchant(getCurrentAdmin(request));
        activityGoods.setMerchant(merchant);
        this.activityGoodsService.addActivityGoods(activityGoods);
        return executeResult();
    }

    /**
     * 活动商品修改
     *
     * @param activityGoods
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/activityGoodsUpdate")
    public ResponseMessage updateActivityGoods(ActivityGoods activityGoods, HttpServletRequest request) {
        this.activityGoodsService.update(activityGoods);
        return executeResult();
    }

    /**
     * 活动商品删除
     *
     * @param activityGoods
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/activityGoods/delete")
    public ResponseMessage deleteActivityGoods(ActivityGoods activityGoods) {
        this.activityGoodsService.delete(activityGoods);
        return executeResult();
    }

    /**
     * 选择普通商品作为活动商品的列表
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/activityGoods/select/list")
    public PageData<Goods> getMerchantGoodsList(HttpServletRequest request) {
        String merchantId = this.getCurrentMerchatId(request);
        IQueryParams queryParams = this.createQueryParams(request);
        queryParams.andEqual("merchant.merchantId", merchantId).andEqual("deleteState", false)
                .andEqual("onsale", true)
                .andEqual("examinState", Constant.EXAMINE_STATE_EXAMINED_OK);
        return this.goodsService.getPageData(this.createPager(request), queryParams);
    }

    /**
     * 根据商家id在页面上列出满足条件的商品的sku列表 带checkbox
     *
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "admin/api/activity/{activitId}/goods/{goodsId}/sku/list")
    public ModelAndView goodsSkuList3(@PathVariable(value = "goodsId") String goodsId, @PathVariable(value = "activitId") String activitId, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("admin/goods/goodsSkuList3");
        String merchantId = this.getCurrentMerchatId(request);
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("activity.activitId", activitId)
                .andEqual("merchant.merchantId", merchantId);
        List<Object> objects = this.activityGoodsService.getProperty("sku.skuId", null, queryParams);
        List<SkuListInfo> skuListInfos = this.skuService.getSkuListInfoByGoodsId(goodsId);
        Goods goods = this.goodsService.getById(goodsId);
        List<SkuListInfo> skuListInfos1 = new LinkedList<>();
        for (Object o : objects) {
            String skuId = (String) o;
            for (SkuListInfo skuListInfo : skuListInfos) {
                if (skuListInfo.getSkuId().equals(skuId)) {
                    skuListInfos1.add(skuListInfo);
                }
            }
        }
        skuListInfos.removeAll(skuListInfos1);
        mv.addObject("skuList", skuListInfos);
        mv.addObject("goods", goods);
        return mv;
    }
}
