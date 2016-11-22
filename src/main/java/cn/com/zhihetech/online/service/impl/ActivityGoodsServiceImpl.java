package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.*;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.dao.*;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IActivityGoodsService;
import cn.com.zhihetech.online.service.IActivityService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by YangDaiChun on 2016/3/4.
 */
@Service(value = "activityGoodsService")
public class ActivityGoodsServiceImpl implements IActivityGoodsService {

    @Resource(name = "activityGoodsDao")
    private IActivityGoodsDao activityGoodsDao;
    @Resource(name = "goodsDao")
    private IGoodsDao goodsDao;
    @Resource(name = "activityService")
    private IActivityService activityService;
    @Resource(name = "merchantDao")
    private IMerchantDao merchantDao;
    @Resource(name = "adminDao")
    private IAdminDao adminDao;
    @Resource(name = "skuDao")
    private ISkuDao skuDao;

    @Override
    public ActivityGoods getById(String id) {
        return this.activityGoodsDao.findEntityById(id);
    }

    @Override
    public void delete(ActivityGoods activityGoods) {
        String activitId = activityGoods.getActivity().getActivitId();
        Activity activity = this.activityService.getById(activitId);
        if (activity == null) {
            throw new SecurityException("活动不存在！");
        }
        if (!activityService.isEditAble(activity)) {
            throw new SystemException("当前活动状态不支持添加操作");
        }
        this.activityGoodsDao.deleteEntity(activityGoods);
    }

    @Override
    public ActivityGoods add(ActivityGoods activityGoods) {

        return this.activityGoodsDao.saveEntity(activityGoods);
    }

    @Override
    public void update(ActivityGoods activityGoods) {
        String activitId = activityGoods.getActivity().getActivitId();
        Activity activity = this.activityService.getById(activitId);
        if (activity == null) {
            throw new SecurityException("活动不存在！");
        }
        if (!activityService.isEditAble(activity)) {
            throw new SystemException("当前活动状态不支持修改操作");
        }
        Goods goods = this.goodsDao.findEntityById(activityGoods.getGoods().getGoodsId());
        long currentStock = goods.getCurrentStock();
        long volume = goods.getVolume();
        if (currentStock - activityGoods.getAgCount() < 0) {
            throw new SystemException("商品的库存量不足以参加本次活动，请输入比商品库存量小的活动商品数量！");
        }
        goods.setVolume(volume + activityGoods.getAgCount());
        this.goodsDao.updateEntity(goods);
        this.activityGoodsDao.updateEntity(activityGoods);
    }

    @Override
    public List<ActivityGoods> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.activityGoodsDao.getEntities(pager, queryParams);
    }

    @Override
    public PageData<ActivityGoods> getPageData(Pager pager, IQueryParams queryParams) {
        return this.activityGoodsDao.getPageData(pager, queryParams);
    }

    @Override
    public void addActivityGoods(ActivityGoods activityGoods) {
        String activitId = activityGoods.getActivity().getActivitId();
        Activity activity = this.activityService.getById(activitId);
        if (activity == null) {
            throw new SecurityException("活动不存在！");
        }
        if (!activityService.isEditAble(activity)) {
            throw new SystemException("当前活动状态不支持添加操作");
        }
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("activity.activitId", activityGoods.getActivity().getActivitId())
                .andEqual("sku.skuId", activityGoods.getSku().getSkuId())
                .andEqual("merchant.merchantId", activityGoods.getMerchant().getMerchantId());
        List<Object> objects = this.activityGoodsDao.getProperty("agId", null, queryParams);
        if (objects.size() > 0) {
            throw new SystemException("该商品的此种组合sku已经参与了该活动，无需重复添加");
        }
        queryParams = new GeneralQueryParams();
        queryParams.andEqual("goodsId", activityGoods.getGoods().getGoodsId())
                .andEqual("deleteState", false)
                .andEqual("onsale", true)
                .andEqual("examinState", Constant.EXAMINE_STATE_EXAMINED_OK);
        long total = this.goodsDao.getRecordTotal(queryParams);
        if (total <= 0) {
            throw new SystemException("对应的活动商品不存在或可能被删除或下架了！");
        }
        Map<String, Object> paramAndValue = new HashMap<>();
        paramAndValue.put("isActivityGoods", true);
        queryParams = new GeneralQueryParams();
        queryParams.andEqual("goodsId", activityGoods.getGoods().getGoodsId());
        this.goodsDao.executeUpdate(paramAndValue, queryParams);
        Sku sku = this.skuDao.findEntityById(activityGoods.getSku().getSkuId());
        int currentStock = sku.getCurrentStock();
        if (currentStock <= 0 || currentStock < activityGoods.getAgCount()) {
            throw new SystemException("此商品的此种组合库存量不足！");
        }
        this.activityGoodsDao.saveEntity(activityGoods);
    }

    //TODO:得到每一个商家的活动商品
    @Override
    public Map<String, List<ActivityGoods>> getActivityGoods(Pager pager, List<Merchant> merchants, String activityId) {
        Map<String, List<ActivityGoods>> allActivityGoods = new HashMap<String, List<ActivityGoods>>();
        for (Merchant merchant : merchants) {
            IQueryParams queryParams = new GeneralQueryParams();
            List<ActivityGoods> oneActivityGoods = new ArrayList<ActivityGoods>(
                    this.activityGoodsDao.getEntities(queryParams.andEqual("merchant.merchantId", merchant.getMerchantId()).andEqual("activity.activitId", activityId))
            );
            allActivityGoods.put(merchant.getMerchantId(), oneActivityGoods);
        }
        return allActivityGoods;
    }

    @Override
    public PageData<ActivityGoods> getActivityGoodsByAdminIdOrMerchantId(Pager pager, String acitivitId, String adminOrMerchantId) {
        IQueryParams queryParams = new GeneralQueryParams().andEqual("merchantId", adminOrMerchantId);
        List<Object> tmps = this.merchantDao.getProperty("merchantId", null, queryParams);
        if (tmps == null || tmps.isEmpty()) {
            queryParams = new GeneralQueryParams().andEqual("adminId", adminOrMerchantId);
            tmps = this.adminDao.getProperty("merchant.merchantId", null, queryParams);
        }
        queryParams = new GeneralQueryParams().andEqual("activity.activitId", acitivitId).andEqual("merchant.merchantId", tmps.get(0));
        return activityGoodsDao.getPageData(pager, queryParams);
    }

    @Override
    public float getActivityGoodsPriceById(String activityGodosId) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("agId", activityGodosId);
        List<Object> oActivityPrice = this.activityGoodsDao.getProperty("activityPrice", null, queryParams);
        if (oActivityPrice == null || oActivityPrice.isEmpty()) {
            throw new SystemException("没有找到对应的秒杀商品！");
        }
        return Float.parseFloat(oActivityPrice.get(0).toString());
    }

    @Override
    public List<Object> getProperty(String s, Pager pager, IQueryParams queryParams) {
        return this.activityGoodsDao.getProperty(s, pager, queryParams);
    }
}
