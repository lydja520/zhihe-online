package cn.com.zhihetech.online.vo;

import cn.com.zhihetech.online.bean.SerializableAndCloneable;

import java.util.List;

/**
 * Created by ShenYunjie on 2016/7/8.
 */
public class GoodsAttrSkuInfo extends SerializableAndCloneable {
    private List<GoodsAttrInfo> goodsAttrInfos;
    private List<GoodsSkuInfo> goodsSkuInfos;

    public GoodsAttrSkuInfo(List<GoodsAttrInfo> goodsAttrInfos, List<GoodsSkuInfo> goodsSkuInfos) {
        super();
        this.goodsAttrInfos = goodsAttrInfos;
        this.goodsSkuInfos = goodsSkuInfos;
    }

    public List<GoodsAttrInfo> getGoodsAttrInfos() {
        return goodsAttrInfos;
    }

    public void setGoodsAttrInfos(List<GoodsAttrInfo> goodsAttrInfos) {
        this.goodsAttrInfos = goodsAttrInfos;
    }

    public List<GoodsSkuInfo> getGoodsSkuInfos() {
        return goodsSkuInfos;
    }

    public void setGoodsSkuInfos(List<GoodsSkuInfo> goodsSkuInfos) {
        this.goodsSkuInfos = goodsSkuInfos;
    }
}
