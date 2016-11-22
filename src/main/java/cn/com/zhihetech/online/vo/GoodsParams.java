package cn.com.zhihetech.online.vo;

import cn.com.zhihetech.online.bean.SerializableAndCloneable;
import cn.com.zhihetech.util.hibernate.Order;

/**
 * Created by ShenYunjie on 2016/7/13.
 */
public class GoodsParams extends SerializableAndCloneable {

    public final static String SORT_PRICE = "price";
    public final static String SORT_VOLUME = "volume";
    public final static String SORT_CREATE_DATE = "createDate";
    public final static String SORT_ON_SALE_DATE = "onSaleDate";

    private String merchantId;  //商家ID,查询指定商家的商品
    private String goodsAttrSetId;  //商品类别ID,查询指定商品类别的商品
    private String searchName;  //查询属性名称
    private String searchValue; //查询值
    private String sort;    //排序属性名称
    private String order;    //排序方式

    public GoodsParams() {
    }

    /**
     * @param merchantId  商家ID
     * @param searchName  搜索属性名称
     * @param searchValue 搜索值
     * @param sort        排序名称
     * @param order       排序方式（asc,desc)
     */
    public GoodsParams(String merchantId, String goodsAttrSetId, String searchName, String searchValue, String sort, String order) {
        this.merchantId = merchantId;
        this.goodsAttrSetId = goodsAttrSetId;
        this.searchName = searchName;
        this.searchValue = searchValue;
        this.sort = sort;
        this.order = order;
    }


    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getGoodsAttrSetId() {
        return goodsAttrSetId;
    }

    public void setGoodsAttrSetId(String goodsAttrSetId) {
        this.goodsAttrSetId = goodsAttrSetId;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
