package cn.com.zhihetech.online.controller.v1.api;

import cn.com.zhihetech.online.bean.ActivityGoods;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.service.IActivityGoodsService;
import cn.com.zhihetech.online.service.ISkuService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by YangDaiChun on 2016/3/8.
 */
@Controller
public class ActivityGoodsApiController extends ApiController {

    @Resource(name = "activityGoodsService")
    private IActivityGoodsService activityGoodsService;
    @Resource(name = "skuService")
    private ISkuService skuService;

    /**
     * <h3>获取活动商品</h3>
     * url : api/activity/activityGoods/list <br>
     * <p>
     * <p>参数</p>
     * acitivitId  ： 活动ID  <br>
     * merchantId  ： 商家ID  <br>
     */
    @ResponseBody
    @RequestMapping(value = "activity/activityGoods/list")
    public PageData<ActivityGoods> getActivityGoodsList(String acitivitId, @RequestParam(value = "merchantId", required = true) String adminOrMerchantId, HttpServletRequest request) {
        PageData<ActivityGoods> activityGoodsPageData = this.activityGoodsService.getActivityGoodsByAdminIdOrMerchantId(this.createPager(request), acitivitId, adminOrMerchantId);
        for (ActivityGoods activityGoods : activityGoodsPageData.getRows()) {
            String skuValue = this.skuService.getSkuValueBySkuId(activityGoods.getSku().getSkuId());
            activityGoods.getSku().setSkuValue(skuValue);
        }
        return activityGoodsPageData;
    }

    /**
     * <h3>获取活动商品列表（最新接口）</h3>
     * url : api/activity/seckillGoods/list <br>
     * <p>
     * <p>参数</p>
     * acitivitId  ： 活动ID  <br>
     * merchantId  ： 商家ID  <br>
     */
    @ResponseBody
    @RequestMapping(value = "activity/seckillGoods/list")
    public PageData<ActivityGoods> getSeckillGoodses(@RequestParam(value = "acitivitId", required = true) String activityId, String merchantId, HttpServletRequest request) {
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("activity.activitId", activityId).andEqual("merchant.merchantId", merchantId);
        PageData<ActivityGoods> activityGoodsPageData = this.activityGoodsService.getPageData(this.createPager(request), queryParams);
        for (ActivityGoods activityGoods : activityGoodsPageData.getRows()) {
            String skuValue = this.skuService.getSkuValueBySkuId(activityGoods.getSku().getSkuId());
            activityGoods.getSku().setSkuValue(skuValue);
        }
        return activityGoodsPageData;
    }

    /**
     * <h3>根据活动商品的Id获取活动商品信息</h3>
     * url:  api/activityGoods/{agId}  <br>
     * {agId}  : 活动商品ID  <br>
     *
     * @param activitId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "activityGoods/{agId}")
    public ResponseMessage getActivityGoodsById(@PathVariable(value = "agId") String activitId) {
        ActivityGoods activityGoods = this.activityGoodsService.getById(activitId);
        String skuValue = this.skuService.getSkuValueBySkuId(activityGoods.getSku().getSkuId());
        activityGoods.getSku().setSkuValue(skuValue);
        if (activityGoods != null) {
            return executeResult(ResponseStatusCode.SUCCESS_CODE, "获取成功", activityGoods);
        }
        return executeResult(ResponseStatusCode.PAGE_NOT_FOUND_CODE, "找不到该活动商品");
    }


}
