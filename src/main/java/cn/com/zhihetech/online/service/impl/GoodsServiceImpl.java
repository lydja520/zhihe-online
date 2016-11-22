package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.*;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.dao.*;
import cn.com.zhihetech.online.exception.StatusCodeException;
import cn.com.zhihetech.online.service.IExamineGoodsService;
import cn.com.zhihetech.online.service.IGoodsService;
import cn.com.zhihetech.online.service.IGoodsTransientInfoService;
import cn.com.zhihetech.online.service.IMerchantService;
import cn.com.zhihetech.online.vo.GoodsAndSku;
import cn.com.zhihetech.online.vo.GoodsTransientInfo;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import com.sun.istack.internal.NotNull;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by YangDaiChun on 2015/12/14.
 */
@Service(value = "goodsService")
public class GoodsServiceImpl implements IGoodsService {

    @Resource(name = "goodsDao")
    private IGoodsDao goodsDao;
    @Resource(name = "goodsDetailDao")
    private IGoodsDetailDao goodsDetailDao;
    @Resource(name = "goodsBannerDao")
    private IGoodsBannerDao goodsBannerDao;
    @Resource(name = "recommendDao")
    private IRecommendDao recommendDao;
    @Resource(name = "activityGoodsDao")
    private IActivityGoodsDao activityGoodsDao;
    @Resource(name = "goodsAttributeDao")
    private IGoodsAttributeDao goodsAttributeDao;
    @Resource(name = "skuDao")
    private ISkuDao skuDao;
    @Resource(name = "examineGoodsService")
    protected IExamineGoodsService examineGoodsService;
    @Resource(name = "goodsTransientInfoService")
    protected IGoodsTransientInfoService goodsTransientInfoService;
    @Resource(name = "merchantService")
    private IMerchantService merchantService;

    @Override
    public Goods getById(String id) {
        Goods goods = this.goodsDao.findEntityById(id);
        //goods.setTransientInfo(this.goodsTransientInfoService.getGoodsTransientInfoByGoodsId(id));
        return goods;
    }

    @Override
    public void delete(Goods goods) {

    }

    @Override
    public Goods add(Goods goods) {
        return this.goodsDao.saveEntity(goods);
    }

    @Override
    public Goods addOrUpdate(Goods goods, List<GoodsBanner> goodsBanners, List<GoodsDetail> goodsDetails) {
        if (!StringUtils.isEmpty(goods.getGoodsId())) {
            GoodsTransientInfo transientInfo = this.goodsTransientInfoService.getGoodsTransientInfoByGoodsId(goods.getGoodsId());
            goods.setTransientInfo(transientInfo);
        }
        this.goodsDao.saveOrUpdate(goods);
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("goods", goods);

        this.goodsBannerDao.executeDelete(queryParams);
        this.goodsDetailDao.executeDelete(queryParams);
        for (GoodsBanner goodsBanner : goodsBanners) {
            goodsBanner.setGoods(goods);
            this.goodsBannerDao.saveEntity(goodsBanner);
        }
        for (GoodsDetail goodsDetail : goodsDetails) {
            goodsDetail.setGoods(goods);
            this.goodsDetailDao.saveEntity(goodsDetail);
        }
        return goods;
    }

    /**
     * 根据商家ID获取可推荐的商品
     *
     * @param queryParams
     * @param pager
     * @param merchantId
     * @return
     */
    @Override
    public PageData<Goods> getAbleRecommendGoodsesByMerchantId(IQueryParams queryParams, Pager pager, String merchantId) {
        IQueryParams queryParams1 = new GeneralQueryParams().andEqual("merchant.merchantId", merchantId);
        List<Object> ids = this.recommendDao.getProperty("goods.goodsId", null, queryParams1);
        queryParams.andNotIn("goodsId", ids).andEqual("onsale", true).andProParam("volume < stock")
                .andEqual("merchant.merchantId", merchantId).andEqual("deleteState", false);

        PageData<Goods> goodsPageData = goodsDao.getPageData(pager, queryParams);
        return goodsPageData;
    }

    @Override
    public ResponseMessage addOrUpdateGoods(Goods goods, List<GoodsBanner> goodsBanners, List<GoodsDetail> goodsDetails, Merchant merchant) {
        ResponseMessage responseMessage = null;
        String msg = "恭喜你，操作成功！";
        if (StringUtils.isEmpty(goods.getGoodsId())) {
            goods.setMerchant(merchant);
            goods.setCreateDate(new Date());
            goods.setExaminState(Constant.EXAMINE_STATE_NOT_SUBMIT);    //默认为未提交审核
            goods.setIsActivityGoods(false);
            goods.setOnsale(false);
            goods.setDeleteState(false);
            goods.setVolume(0);
            this.merchantService.updateMerchantUpdateDate(merchant.getMerchantId());
        } else {
            Goods goods1 = this.goodsDao.findEntityById(goods.getGoodsId());
            if (goods1.isOnsale()) {
                throw new StatusCodeException("当前商品已上架不能进行修改，请下架后重试！", 500);
            }
            goods1.setGoodsName(goods.getGoodsName());
            goods1.setGoodsAttributeSet(goods.getGoodsAttributeSet());
            //goods1.setStock(goods.getStock());
            //goods1.setPrice(goods.getPrice());
            goods1.setCoverImg(goods.getCoverImg());
            goods1.setIsPick(goods.getIsPick());
            goods1.setGoodsDesc(goods.getGoodsDesc());
            goods1.setCarriageMethod(goods.getCarriageMethod());
            goods1.setCarriage(goods.getCarriage());
            goods1.setExaminMsg(null);
            goods1.setExaminState(Constant.EXAMINE_STATE_NOT_SUBMIT);
            if (StringUtils.isEmpty(goods.getCarriageMethod())) {
                goods1.setCarriageMethod(null);
                goods1.setCarriage(0f);
            }
            goods = goods1;
            msg = "商品修改成功，如需上架请重新提交审核！";
        }

        this.addOrUpdate(goods, goodsBanners, goodsDetails);
        responseMessage = new ResponseMessage(ResponseStatusCode.SUCCESS_CODE, true, msg);
        responseMessage.setAttribute(goods.getGoodsId());
        return responseMessage;
    }

    @Override
    public void update(Goods goods) {
        if (!StringUtils.isEmpty(goods.getGoodsId())) {
            GoodsTransientInfo transientInfo = this.goodsTransientInfoService.getGoodsTransientInfoByGoodsId(goods.getGoodsId());
            goods.setTransientInfo(transientInfo);
        }
        this.goodsDao.updateEntity(goods);
    }

    @Override
    public List<Goods> getAllByParams(Pager pager, IQueryParams queryParams) {
        List<Goods> goodsList = this.goodsDao.getEntities(pager, queryParams);
        return goodsList;
    }

    @Override
    public PageData<Goods> getPageData(Pager pager, IQueryParams queryParams) {
        PageData<Goods> goodsPageData = this.goodsDao.getPageData(pager, queryParams);
        return goodsPageData;
    }

    @Override
    public void updateDeleteState(String id) {
        if (isOnSale(id)) {
            throw new StatusCodeException("不能删除已上架的商品，请下架后重试！", 500);
        }
        Map<String, Object> paramAndValue = new HashMap<String, Object>();
        paramAndValue.put("deleteState", true);
        paramAndValue.put("onsale", false);
        paramAndValue.put("examinState", Constant.EXAMINE_STATE_NOT_SUBMIT);
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("goodsId", id);
        this.goodsDao.executeUpdate(paramAndValue, queryParams);
    }

    @Override
    public void updateOnsalState(String id, boolean onsale) {
        if (onsale) {
            executeOnSaleGoods(id);
            IQueryParams queryParams = new GeneralQueryParams();
            queryParams.andEqual("goodsId", id);
            List<Object> oMerchants = this.goodsDao.getProperty("merchant.merchantId", null, queryParams);
            if (oMerchants != null && !oMerchants.isEmpty()) {
                this.merchantService.updateMerchantUpdateDate((String) oMerchants.get(0));
            }
        } else {
            executeOffLineGoods(id);
        }
    }

    /**
     * 下架商品
     *
     * @param goodsId
     */
    @Override
    public void executeOffLineGoods(String goodsId) {
        if (!isOnSale(goodsId)) {
            throw new StatusCodeException("此商品已经下架，请勿重复操作！", 500);
        }

        Collection<Object> states = new ArrayList<>();
        states.add(Constant.ACTIVITY_STATE_EXAMINED_OK);
        states.add(Constant.ACTIVITY_STATE_STARTED);
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("goods.goodsId", goodsId)
                .andIn("activity.currentState", states);
        Object obj = this.activityGoodsDao.getProperty("count(agId)", null, queryParams).get(0);
        long count = (Long) obj;
        if (count > 0) {
            throw new StatusCodeException("此商品正在参加活动暂不能下架，请等待活动结束后重试！", 500);
        }

        Map<String, Object> values = new HashMap<>();
        values.put("examinState", Constant.EXAMINE_STATE_NOT_SUBMIT);
        values.put("examinMsg", null);
        values.put("onsale", false);
        queryParams = new GeneralQueryParams().andEqual("goodsId", goodsId);
        this.goodsDao.executeUpdate(values, queryParams);
    }

    /**
     * 上架商品
     *
     * @param goodsId
     */
    @Override
    public void executeOnSaleGoods(String goodsId) {
        if (isOnSale(goodsId)) {
            throw new StatusCodeException("此商品已经上架，请勿重复操作！", 500);
        }

        int state = getGoodState(goodsId);
        IQueryParams queryParams;
        switch (state) {
            case Constant.EXAMINE_STATE_NOT_SUBMIT:
                throw new StatusCodeException("商品还未提交审核不能上架,请提交审核后重试！", 500);
            case Constant.EXAMINE_STATE_SUBMITED:
                throw new StatusCodeException("商品正在审核中,请审核通过后重试！", 500);
            case Constant.EXAMINE_STATE_EXAMINED_NUOK:
                String msg = "该商品未通过审核，请修改后重试！";
                queryParams = new GeneralQueryParams().andEqual("goodsId", goodsId);
                List<Object> msgs = this.goodsDao.getProperty("examinMsg", null, queryParams);
                if (msgs != null && !msgs.isEmpty()) {
                    msg = String.valueOf(msgs.get(0));
                }
                throw new StatusCodeException(msg, 500);
        }

        Map<String, Object> values = new HashMap<>();
        values.put("onsale", true);
        values.put("onSaleDate", new Date());
        queryParams = new GeneralQueryParams().andEqual("goodsId", goodsId);
        this.goodsDao.executeUpdate(values, queryParams);
    }

    /**
     * 提交商品审核
     *
     * @param goodsId
     */
    @Override
    public void executeSubmitExamine(String goodsId) {
        this.examineGoodsService.executeSubmitGoodsExamine(goodsId);
    }

    /**
     * 审核商品并审核通过
     *
     * @param goodsId 商品ID
     */
    @Override
    public void executeExamineGoods(String goodsId) {
        this.examineGoodsService.executeExaminedGoods(goodsId);
    }

    /**
     * 商品审核未通过
     *
     * @param goodsId 商品ID
     * @param msg     审核未通过原因
     */
    @Override
    public void executeUnExamineGoods(String goodsId, String msg) {
        this.examineGoodsService.executeUnExaminedGoods(goodsId, msg);
    }

    /**
     * 批量更新商品销量
     *
     * @param goodsIds    商品ID列表
     * @param goodsCounts 商品购买购买的数量列表
     */
    @Override
    public void executeAddVolumeByGoodsIdsAndGoodsCounts(List<String> goodsIds, List<Long> goodsCounts) {
        this.goodsDao.executeAddVolumeByGoodsIdsAndGoodsCounts(goodsIds, goodsCounts);
    }

    /**
     * 根据商品列表获取商品的sku
     *
     * @param goodses
     * @return
     */
    @Override
    public List<GoodsAndSku> getGoodsAndSkus(List<Goods> goodses, String goodsId) {
        IQueryParams queryParams = null;
        List<GoodsAndSku> goodsAndSkus = new ArrayList<>();
        List<GoodsAndSku> childrenGoodsAndSkus = null;
        if (StringUtils.isEmpty(goodsId)) {//如果goodsId为空，则表示获取的是根节点
            for (Goods goods : goodses) {
                GoodsAndSku goodsAndSku = new GoodsAndSku();
                goodsAndSku.setGoodsId(goods.getGoodsId());
                goodsAndSku.setGoodsName(goods.getGoodsName());
                goodsAndSku.setGoodsDesc(goods.getGoodsDesc());
                goodsAndSku.setOnsale(goods.isOnsale());
                goodsAndSku.setExaminState(goods.getExaminState());
                goodsAndSku.setExaminMsg(goods.getExaminMsg());
                goodsAndSku.setCreateDate(goods.getCreateDate());
                goodsAndSku.setCarriage(goods.getCarriage());
                goodsAndSku.setCarriageMethod(goods.getCarriageMethod());
                goodsAndSku.setImgUrl(goods.getCoverImg().getUrl());
                goodsAndSku.setPick(goods.getIsPick());
                childrenGoodsAndSkus = new ArrayList<>();
                goodsAndSku.setChildren(childrenGoodsAndSkus);
                goodsAndSkus.add(goodsAndSku);
            }
            return goodsAndSkus;
        } else {//如果goodsId不为空则说明获取的是goodsId对应的商品的所有sku
            queryParams = new GeneralQueryParams();
            Goods goods = this.goodsDao.findEntityById(goodsId);
            queryParams = new GeneralQueryParams();
            queryParams.andEqual("goods.goodsId", goodsId);
            List<Object> oSkuIds = this.goodsAttributeDao.getProperty("distinct(sku.skuId)", null, queryParams);
            if (oSkuIds.size() > 0) {
                childrenGoodsAndSkus = new ArrayList<>();
                for (Object oSkuId : oSkuIds) {
                    String skuId = (String) oSkuId;
                    Sku sku = this.skuDao.findEntityById(skuId);
                    queryParams = new GeneralQueryParams();
                    queryParams.andEqual("sku.skuId", sku.getSkuId());
                    List<Object> oGoodsAttValue = this.goodsAttributeDao.getProperty("attrValue", null, queryParams);
                    StringBuffer goodsName = new StringBuffer("");
                    for (Object object : oGoodsAttValue) {
                        goodsName.append((String) object).append("+");
                    }
                    GoodsAndSku childGoodsAndSku = new GoodsAndSku();
                    childGoodsAndSku.setGoodsName(goodsName.toString());
                    childGoodsAndSku.setExaminMsg(goods.getExaminMsg());
                    childGoodsAndSku.setGoodsDesc(goods.getGoodsDesc());
                    childGoodsAndSku.setOnsale(goods.isOnsale());
                    childGoodsAndSku.setExaminState(goods.getExaminState());
                    childGoodsAndSku.setExaminMsg(goods.getExaminMsg());
                    childGoodsAndSku.setCreateDate(goods.getCreateDate());
                    childGoodsAndSku.setCarriage(goods.getCarriage());
                    childGoodsAndSku.setCarriageMethod(goods.getCarriageMethod());
                    childGoodsAndSku.setPick(goods.getIsPick());
                    childGoodsAndSku.setGoodsId(sku.getSkuId());
                    childGoodsAndSku.setPrice(sku.getPrice());
                    childGoodsAndSku.setStock(sku.getStock());
                    childGoodsAndSku.setVolume(sku.getVolume());
                    childGoodsAndSku.setImgUrl(sku.getCoverImg().getUrl());
                    childrenGoodsAndSkus.add(childGoodsAndSku);
                }
            }
            return childrenGoodsAndSkus;
        }
    }

    @Override
    public List<Object> getProperty(String s, Pager pager, IQueryParams queryParams) {
        return this.goodsDao.getProperty(s, pager, queryParams);
    }

    /**
     * 更新商品的库存、销量、最小价格和最大价格信息
     *
     * @param goodsId
     */
    @Override
    public void executeUpdateGoodsTransientInfo(String goodsId) {
        GoodsTransientInfo transientInfo = this.goodsTransientInfoService.getGoodsTransientInfoByGoodsId(goodsId);
        if (transientInfo == null) {
            return;
        }
        Map<String, Object> values = new HashMap<>();
        values.put("price", transientInfo.getPrice());
        values.put("minPrice", transientInfo.getMinPrice());
        values.put("maxPrice", transientInfo.getMaxPrice());
        values.put("stock", transientInfo.getCurrentStock());
        values.put("volume", transientInfo.getVolume());
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("goodsId", goodsId);
        this.goodsDao.executeUpdate(values, queryParams);
    }

    @Override
    public String getMerchantId(String goodsId) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("goodsId", goodsId);
        List<Object> merchantIds = this.goodsDao.getProperty("merchant.merchantId", null, queryParams);
        if (merchantIds != null && !merchantIds.isEmpty()) {
            return (String) merchantIds.get(0);
        }
        return "";
    }

    /**
     * 获取商品状态（未提交，待审核，已审核，未通过）
     *
     * @param goodsId
     */
    @Override
    public Integer getGoodState(String goodsId) {
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("goodsId", goodsId);
        List<Object> objs = this.goodsDao.getProperty("examinState", null, queryParams);
        return (Integer) objs.get(0);
    }

    @Override
    public void executeUpdateGoodsNoSubmitExamine(@NotNull String goodsId) {
        Map<String, Object> values = new HashMap<>();
        values.put("examinState", Constant.EXAMINE_STATE_NOT_SUBMIT);
        values.put("examinMsg", "未提交审核");
        values.put("onsale", false);    //如果商品已上架，审核未通过时同时将商品下架（这种可能性很低）
        IQueryParams queryParams = new GeneralQueryParams().andEqual("goodsId", goodsId);
        this.goodsDao.executeUpdate(values, queryParams);
    }

    /**
     * 获取商品是否是上线状态
     */
    @Override
    public boolean isOnSale(String goodsId) {
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("goodsId", goodsId);
        List<Object> objs = this.goodsDao.getProperty("onsale", null, queryParams);
        if (objs != null && !objs.isEmpty()) {
            return (Boolean) objs.get(0) ? true : false;
        }
        return false;
    }
}
