package cn.com.zhihetech.online.vo;

import cn.com.zhihetech.online.bean.SerializableAndCloneable;

import java.util.List;

/**
 * 用于返回给App端的商品属性信息bean
 * Created by ShenYunjie on 2016/7/8.
 */
public class GoodsAttrInfo extends SerializableAndCloneable {
    private String goodsId;
    private String attrId;
    private String attrName;
    private List<String> tags;

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
