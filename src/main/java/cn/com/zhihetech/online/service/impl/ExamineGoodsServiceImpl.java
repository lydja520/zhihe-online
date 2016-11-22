package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.bean.Sku;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.dao.IExamineGoodsDao;
import cn.com.zhihetech.online.dao.IGoodsAttributeDao;
import cn.com.zhihetech.online.dao.IGoodsDao;
import cn.com.zhihetech.online.dao.ISkuDao;
import cn.com.zhihetech.online.exception.StatusCodeException;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.*;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import com.sun.istack.internal.NotNull;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ShenYunjie on 2016/7/12.
 */
@Service("examineGoodsService")
public class ExamineGoodsServiceImpl implements IExamineGoodsService {

    @Resource(name = "examineGoodsDao")
    private IExamineGoodsDao examineGoodsDao;
    @Resource(name = "goodsService")
    private IGoodsService goodsService;
    @Resource(name = "goodsDao")
    private IGoodsDao goodsDao;
    @Resource(name = "skuService")
    private ISkuService skuService;
    @Resource(name = "skuDao")
    private ISkuDao skuDao;
    @Resource(name = "goodsAttributeService")
    protected IGoodsAttributeService goodsAttributeService;
    @Resource(name = "goodsAttributeDao")
    private IGoodsAttributeDao goodsAttributeDao;
    @Resource(name = "goodsAttributeSetService")
    private IGoodsAttributeSetService goodsAttributeSetService;

    /**
     * 提交商品审核
     *
     * @param goodsId 商品ID
     */
    @Override
    public void executeSubmitGoodsExamine(@NotNull String goodsId) {
        int goodsState = this.goodsService.getGoodState(goodsId);
        if (goodsState != Constant.EXAMINE_STATE_NOT_SUBMIT && goodsState != Constant.EXAMINE_STATE_EXAMINED_NUOK) {
            throw new StatusCodeException("只有未提交或审核未通过的商品才支持此操作！", 500);
        }
        if (!checkGoodsSkuByGoodsId(goodsId)) {
            throw new SystemException("此商品的属性组合(SKU)与商品属性数量不相符！");
        }
        IQueryParams queryParams = new GeneralQueryParams().andEqual("goodsId", goodsId);
        Map<String, Object> values = new HashMap<>();
        values.put("examinState", Constant.EXAMINE_STATE_SUBMITED);
        values.put("examinMsg", "已提交，等待审核...");
        values.put("onsale", false);
        this.goodsDao.executeUpdate(values, queryParams);
    }

    /**
     * 审核商品（商品审核通过）
     *
     * @param goodsId
     */
    @Override
    public void executeExaminedGoods(String goodsId) {
        if (this.goodsService.getGoodState(goodsId) != Constant.EXAMINE_STATE_SUBMITED) {
            throw new StatusCodeException("商品还未提交审核，不能进行此操作！", 500);
        }
        Map<String, Object> values = new HashMap<>();
        values.put("examinState", Constant.EXAMINE_STATE_EXAMINED_OK);
        values.put("examinMsg", "审核通过");
        values.put("onsale", false);
        IQueryParams queryParams = new GeneralQueryParams().andEqual("goodsId", goodsId);
        this.goodsDao.executeUpdate(values, queryParams);
    }

    @Override
    public void executeUnExaminedGoods(String goodsId, String msg) {
        if (this.goodsService.getGoodState(goodsId) != Constant.EXAMINE_STATE_SUBMITED) {
            throw new StatusCodeException("当前商品还未提价审核，不支持此操作！", 500);
        }
        Map<String, Object> values = new HashMap<>();
        values.put("examinState", Constant.EXAMINE_STATE_EXAMINED_NUOK);
        values.put("examinMsg", msg);
        values.put("onsale", false);    //如果商品已上架，审核未通过时同时将商品下架（这种可能性很低）
        IQueryParams queryParams = new GeneralQueryParams().andEqual("goodsId", goodsId);
        this.goodsDao.executeUpdate(values, queryParams);
    }

    /**
     * 检查指定商品是否已经有库存,库存中价格及库存数量是否符合要求，且现库存的属性组合（SKU)与商品属性数量是否相等（每条sku由此商品的所有属相组合而成）
     *
     * @param goodsId
     * @return
     */
    protected boolean checkGoodsSkuByGoodsId(@NotNull String goodsId) {
        List<Sku> skuList = this.skuService.getSkuListByGoodsId(goodsId);
        if (skuList == null || skuList.isEmpty()) {
            throw new SystemException("请先添加商品库存（SKU）后再提交审核！");
        }
        int skuAttrCount = this.goodsAttributeSetService.getSkuAttrCountByGoodsId(goodsId); //获取此商品对应的商品属性的总数
        for (Sku sku : skuList) {
            if (sku.getPrice() <= 0) {
                throw new SystemException("商品价格必须大于0！");
            }
            if (sku.getStock() < 0) {
                throw new SystemException("商品库不能小于0！");
            }
            if (sku.getCurrentStock() < 0) {
                throw new SystemException("商品库必须大于商品销量！");
            }
            int goodsAttrCount = this.goodsAttributeService.getGoodsAttrCountBySkuId(sku.getSkuId());  //根据skuId获取商品与商品属性的组合总数
            if (skuAttrCount != goodsAttrCount) {
                throw new SystemException("此商品的属性数量与现有库存的属性组合(SKU)不相符！");
            }
        }
        return true;
    }
}
