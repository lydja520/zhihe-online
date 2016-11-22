package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.GoodsAttribute;
import cn.com.zhihetech.online.bean.Sku;
import cn.com.zhihetech.online.bean.SkuAttribute;
import cn.com.zhihetech.online.dao.IGoodsAttributeDao;
import cn.com.zhihetech.online.dao.ISkuAttributeDao;
import cn.com.zhihetech.online.dao.ISkuDao;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.*;
import cn.com.zhihetech.online.util.StringUtils;
import cn.com.zhihetech.online.vo.*;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import com.sun.istack.internal.NotNull;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by YangDaiChun on 2015/12/2.
 */
@Service("skuService")
public class SkuServiceImpl implements ISkuService {

    @Resource(name = "skuDao")
    protected ISkuDao skuDao;
    @Resource(name = "goodsAttributeService")
    protected IGoodsAttributeService goodsAttributeService;
    @Resource(name = "goodsService")
    protected IGoodsService goodsService;
    @Resource(name = "goodsAttributeSetService")
    protected IGoodsAttributeSetService goodsAttributeSetService;
    @Resource(name = "goodsAttributeDao")
    private IGoodsAttributeDao goodsAttributeDao;
    @Resource(name = "skuAttributeDao")
    private ISkuAttributeDao skuAttributeDao;
    @Resource(name = "merchantService")
    private IMerchantService merchantService;

    @Override
    public Sku getById(String id) {
        return this.skuDao.findEntityById(id);
    }

    @Override
    public void delete(Sku sku) {
        this.skuDao.deleteEntity(sku);
    }

    @Override
    public Sku add(Sku sku) {
        return this.skuDao.saveEntity(sku);
    }

    @Override
    public void update(Sku sku) {
        this.skuDao.updateEntity(sku);
    }

    @Override
    public List<Sku> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.skuDao.getEntities(pager, queryParams);
    }

    @Override
    public PageData<Sku> getPageData(Pager pager, IQueryParams queryParams) {
        return this.skuDao.getPageData(pager, queryParams);
    }

    /**
     * 根据商品ID，商品属性组合，价格、库存及sku封面图添加商品sku
     *
     * @param goodsId
     * @param goodsAttrs
     * @param sku
     */
    @Override
    public void addSkuByGoodsId(String goodsId, List<GoodsAttribute> goodsAttrs, Sku sku) {
        if (this.goodsService.isOnSale(goodsId)) {
            throw new SystemException("此商品已上架，不可编辑，如要编辑请将商品下架后重试！");
        }

        if (existGoodsAndAttrMixCode(goodsId, sku.getMixCode(), null)) {
            throw new SystemException("商品的此种属性组合已经存在，请勿重复添加！");
        }

        if (StringUtils.isEmpty(sku.getGoodsId()) || sku.getPrice() <= 0 || sku.getStock() <= 0) {
            throw new SystemException("params is not able empty or price,stock is not less than zero!");
        }

        int skuAttCount = this.goodsAttributeSetService.getSkuAttrCountByGoodsId(goodsId);
        int goodsAttrsCount = goodsAttrs == null ? 0 : goodsAttrs.size();
        if (skuAttCount != goodsAttrsCount) {
            throw new SystemException("商品属性组合与此商品对应的商品属性数量不符!");
        }

        this.skuDao.saveEntity(sku);
        for (GoodsAttribute goodsAttr : goodsAttrs) {
            goodsAttr.setSku(sku);
            this.goodsAttributeService.add(goodsAttr);
        }
        this.goodsService.executeUpdateGoodsTransientInfo(goodsId); //更新商品的库存、销量、最小价格和最大价格信息
        this.goodsService.executeUpdateGoodsNoSubmitExamine(goodsId);   //编辑后将商品状态更新为未提交审核
    }

    /**
     * 商品属性组合和sku编辑
     *
     * @param goodsAttrs   商品，商品属性组合
     * @param sku          需要编辑的sku
     * @param currentStock SKU 当前库存
     */
    @Override
    public void updateGoodsAttrAndSku(List<GoodsAttribute> goodsAttrs, Sku sku, long currentStock) {
        if (this.goodsService.isOnSale(sku.getGoodsId())) {
            throw new SystemException("此商品已上架，不可编辑，如要编辑请将商品下架后重试！");
        }

        if (existGoodsAndAttrMixCode(sku.getGoodsId(), sku.getMixCode(), sku.getSkuId())) {
            throw new SystemException("商品的此种属性组合已经存在，请勿重复添加！");
        }

        /**
         *根据现有库存动态计算此SKU对应的原始库存并赋值
         */
        sku.setStock(this.getSkuStockBySkuIdAndCurrentStock(sku.getSkuId(), currentStock));

        if (sku.getPrice() <= 0 || sku.getStock() < 0) {
            throw new SystemException("价格和库存必须大于0");
        }

        int skuAttCount = this.goodsAttributeSetService.getSkuAttrCountByGoodsId(sku.getGoodsId());
        int goodsAttrsCount = goodsAttrs == null ? 0 : goodsAttrs.size();
        if (skuAttCount != goodsAttrsCount) {
            throw new SystemException("商品属性组合与此商品对应的商品属性数量不符!");
        }

        for (GoodsAttribute goodsAttr : goodsAttrs) {
            if (StringUtils.isEmpty(goodsAttr.getGoodsAttrId())) {
                this.goodsAttributeService.add(goodsAttr);
            } else {
                this.goodsAttributeService.executeUpdate(goodsAttr);
            }
        }
        String merchantId = this.goodsService.getMerchantId(sku.getGoodsId());

        this.skuDao.updateEntity(sku);

        this.goodsService.executeUpdateGoodsTransientInfo(sku.getGoodsId()); //更新商品的库存、销量、最小价格和最大价格信息
        this.goodsService.executeUpdateGoodsNoSubmitExamine(sku.getGoodsId());   //编辑后将商品状态更新为未提交审核

        this.merchantService.updateMerchantUpdateDate(merchantId);
    }

    /**
     * 获取指定SKU中设定当前库存后动态计算原始库存量（原始库存 = 当前库存 + 销量）
     *
     * @param skuId
     * @param currentStock
     * @return
     */
    protected long getSkuStockBySkuIdAndCurrentStock(String skuId, long currentStock) {
        if (currentStock < 0) {
            throw new SystemException("库存不能小于0！");
        }
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("skuId", skuId);
        List<Object[]> values = this.skuDao.getProperties(new String[]{"stock", "volume"}, null, queryParams);
        if (values == null || values.isEmpty()) {
            return 0L;
        }
        long stock = Long.parseLong(String.valueOf(values.get(0)[0]));
        long volume = Long.parseLong(String.valueOf(values.get(0)[1]));
        //TODO 此处有争议
        /*if (stock > volume + currentStock) {
            throw new SystemException("系统出错，请联系管理员！错误代码：sku_stock-001");
        }*/
        return (volume + currentStock);
    }

    @Override
    public List<Sku> getSkuListByGoodsId(String goodsId) {
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("goodsId", goodsId);
        return this.skuDao.getEntities(null, queryParams);
    }

    /**
     * 获取指定商品默认的SKU
     *
     * @param goodsId
     * @return
     */
    @Override
    public Sku getDefaultSkuByGoodsId(String goodsId) {
        return this.skuDao.getMaxStockSkuByGoodsId(goodsId);
    }

    /**
     * 根据SKU ID和购买数量更新sku的销量
     *
     * @param skuAndCount
     */
    @Override
    public void executeAddSkuVolumeBySkuAndCount(Map<String, Long> skuAndCount) {
        if (skuAndCount == null || skuAndCount.isEmpty()) {
            return;
        }
        for (String skuId : skuAndCount.keySet()) {
            int count = this.skuDao.addSkuVolumeBySkuAndCount(skuId, skuAndCount.get(skuId));
            if (count < 1) {
                throw new SystemException("商品库存不足！");
            }
        }
    }

    @Override
    public String getSkuValueBySkuId(String skuId) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("sku.skuId", skuId);
        String[] selectors = new String[]{"attribute.skuAttName", "attrValue"};
        List<Object[]> oSkuValues = this.goodsAttributeDao.getProperties(selectors, null, queryParams);
        String skuValue = "";
        if (oSkuValues != null && !oSkuValues.isEmpty()) {
            for (Object[] oSkuValue : oSkuValues) {
                String attName = (String) oSkuValue[0];
                String attValue = (String) oSkuValue[1];
                skuValue += attName + ":" + attValue + "；";
            }
            skuValue = skuValue.substring(0, skuValue.length() - 1);
        }
        return skuValue;
    }

    /**
     * 根据商品ID获取商品对应的属性、属性值和sku
     *
     * @param goodsId 商品ID
     * @return
     */
    @Override
    public GoodsAttrSkuInfo getGoodsAttrSkuInfoByGoodsId(String goodsId) {
        List<GoodsAttrInfo> goodsAttrInfoList = this.goodsAttributeService.getGoodsAttrInfosByGoodsId(goodsId);
        List<GoodsSkuInfo> goodsSkuInfoList = this.getGoodsSkuInfosByGoodsId(goodsId);
        return new GoodsAttrSkuInfo(goodsAttrInfoList, goodsSkuInfoList);
    }

    /**
     * 根据商品ID获取GoodsSkuInfo
     *
     * @param goodsId
     * @return
     */
    @Override
    public List<GoodsSkuInfo> getGoodsSkuInfosByGoodsId(String goodsId) {
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("goodsId", goodsId);
        List<Sku> skuList = this.skuDao.getEntities(queryParams);
        List<GoodsSkuInfo> goodsSkuInfoList = new ArrayList<>();
        for (Sku sku : skuList) {
            String skuValue = this.getSkuValueBySkuId(sku.getSkuId());
            sku.setSkuValue(skuValue);
            GoodsSkuInfo skuInfo = new GoodsSkuInfo(sku);
            skuInfo.setAttrCodes(this.goodsAttributeService.getAttrAndValueArrayBySkuId(sku.getSkuId()));
            goodsSkuInfoList.add(skuInfo);
        }
        return goodsSkuInfoList;
    }

    /**
     * 根据商品的id获取商品的sku列表信息
     *
     * @param goodsId
     * @return
     */
    @Override
    public List<SkuListInfo> getSkuListInfoByGoodsId(String goodsId) {

        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("goodsId", goodsId);
        List<Object> oGoodsAttIds = this.goodsService.getProperty("goodsAttributeSet.goodsAttSetId", new Pager(), queryParams);
        String goodAttSetId = (String) oGoodsAttIds.get(0);
        queryParams = new GeneralQueryParams();
        queryParams.andEqual("goodsAttributeSet.goodsAttSetId", goodAttSetId).andEqual("permit", true);
        String[] skuAttInfo = new String[2];
        skuAttInfo[0] = "skuAttId";
        skuAttInfo[1] = "skuAttName";
        List<Object[]> oSkuAtts = this.skuAttributeDao.getProperties(skuAttInfo, null, queryParams);
        Map<String, String> skuAttIds = new HashMap<>();
        for (Object[] oSkuAtt : oSkuAtts) {
            skuAttIds.put((String) oSkuAtt[0], (String) oSkuAtt[1]);
        }

        List<SkuListInfo> skuListInfos = new LinkedList<>();
        queryParams = new GeneralQueryParams();
        queryParams.andEqual("goodsId", goodsId);
        List<Sku> skus = this.skuDao.getEntities(queryParams);
        for (Sku sku : skus) {
            SkuListInfo skuListInfo = new SkuListInfo();
            skuListInfo.setSkuId(sku.getSkuId());
            skuListInfo.setCoverImg(sku.getCoverImg());
            skuListInfo.setStock(sku.getStock());
            skuListInfo.setCurrentStock(sku.getStock() - sku.getVolume());
            skuListInfo.setPrice(sku.getPrice());
            skuListInfo.setVolume(sku.getVolume());
            queryParams = new GeneralQueryParams();
            queryParams.andEqual("sku.skuId", sku.getSkuId());
            String[] selectors = new String[3];
            selectors[0] = "attribute.skuAttId";
            selectors[1] = "attribute.skuAttName";
            selectors[2] = "attrValue";
            List<Object[]> oAttributes = this.goodsAttributeService.getProperties(selectors, null, queryParams);
            List<GoodsAttribute> goodsAttributes = new ArrayList<>();
            List<String> _skuAttIds = new ArrayList<>();
            for (Object[] oAttribute : oAttributes) {
                String attributeId = (String) oAttribute[0];
                _skuAttIds.add(attributeId);
                String attributeName = (String) oAttribute[1];
                String attributeValue = (String) oAttribute[2];
                GoodsAttribute goodsAttribute = new GoodsAttribute();
                SkuAttribute skuAttribute = new SkuAttribute();
                skuAttribute.setSkuAttName(attributeName);
                goodsAttribute.setAttribute(skuAttribute);
                goodsAttribute.setAttrValue(attributeValue);
                goodsAttributes.add(goodsAttribute);

            }
            for (String key : skuAttIds.keySet()) {
                if (!_skuAttIds.contains(key)) {
                    GoodsAttribute goodsAttribute = new GoodsAttribute();
                    SkuAttribute skuAttribute = new SkuAttribute();
                    skuAttribute.setSkuAttName(skuAttIds.get(key));
                    goodsAttribute.setAttribute(skuAttribute);
                    goodsAttribute.setAttrValue(null);
                    goodsAttributes.add(goodsAttribute);
                }
            }
            Collections.sort(goodsAttributes, new Comparator<GoodsAttribute>() {
                @Override
                public int compare(GoodsAttribute o1, GoodsAttribute o2) {
                    return o1.getAttribute().getSkuAttName().compareTo(o2.getAttribute().getSkuAttName());
                }
            });
            skuListInfo.setGoodsAttributes(goodsAttributes);
            skuListInfo.setSkuValue(this.getSkuValueBySkuId(sku.getSkuId()));
            skuListInfos.add(skuListInfo);
        }
        return skuListInfos;
    }

    /**
     * 指定商品的属性组合是否已经存在（如果skuId不为空，则排除skuId为’skuId‘的数据）
     *
     * @param goodsId 商品ID
     * @param mixCode 属性组合编码
     * @param skuId   skuId
     * @return
     */
    protected boolean existGoodsAndAttrMixCode(@NotNull String goodsId, @NotNull String mixCode, String skuId) {
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("goodsId", goodsId)
                .andEqual("mixCode", mixCode);
        if (!StringUtils.isEmpty(skuId)) {
            queryParams.andNotEq("skuId", skuId);
        }
        List<Object> objs = this.skuDao.getProperty("skuId", null, queryParams);
        if (objs != null && !objs.isEmpty()) {
            return true;
        }
        return false;
    }
}
