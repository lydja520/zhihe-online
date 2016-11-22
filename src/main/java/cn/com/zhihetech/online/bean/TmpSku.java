package cn.com.zhihetech.online.bean;

import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.util.common.MD5Utils;
import cn.com.zhihetech.util.common.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by YangDaiChun on 2015/12/2.
 * <p>
 * 商品的SKU，例如一件上架的衣服，它的其中的一种组合 XL + 红色 就会形成这件衣服的一个SKU
 */
@Entity
@Table(name = "tmp_sku", uniqueConstraints = {@UniqueConstraint(columnNames = {"goods_id", "mix_code"})})
public class TmpSku extends SerializableAndCloneable {

    private String skuId;
    private String goodsId; //商品ID
    private double price; //商品单价
    private long stock; //商品库存量
    private long volume; //商品的销量
    private String mixCode; //商品的属性的组合编码,在每个商品内唯一
    private ImgInfo coverImg;   //对应此sku组合的封面（如果没有默认为对应的商品封面）
    private int currentStock;
    private String skuValue;

    public TmpSku() {
    }

    public TmpSku(String skuId) {
        this.skuId = skuId;
    }

    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid2")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "sku_id", length = 36)
    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    @Column(name = "goods_id", nullable = false, length = 40, updatable = false)
    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    @Column(name = "price", nullable = false)
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Column(name = "stock", nullable = false)
    public long getStock() {
        return stock;
    }

    public void setStock(long stock) {
        this.stock = stock;
    }

    @Column(name = "volume", nullable = false)
    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    @Column(name = "mix_code", nullable = false, length = 40)
    public String getMixCode() {
        return mixCode;
    }

    public void setMixCode(String mixCode) {
        this.mixCode = mixCode;
    }


    /**
     * 根据商品和商品属性组合获取此组合的编码
     *
     * @param goodsId
     * @param goodsAttrList
     * @return
     */
    @Transient
    public final static String getGoodsAttrMixCode(String goodsId, List<GoodsAttribute> goodsAttrList) {
        if (StringUtils.isEmpty(goodsId) || goodsAttrList == null) {
            throw new SystemException("goods is not able null or goodsAttrs is not able null!");
        }
        Collections.sort(goodsAttrList, new Comparator<GoodsAttribute>() {
            @Override
            public int compare(GoodsAttribute o1, GoodsAttribute o2) {
                return o1.getAttribute().getSkuAttId().compareTo(o2.getAttribute().getSkuAttId());
            }
        });
        String mixCode = "";
        for (GoodsAttribute goodsAttr : goodsAttrList) {
            if (!goodsId.equals(goodsAttr.getGoods().getGoodsId())) {
                throw new SystemException("商品属性中的的商品与所传递的商品不是同一件商品");
            }
            mixCode += goodsAttr.getAttribute().getSkuAttId().trim() + ":" + goodsAttr.getAttrValue().trim() + ";";
        }
        mixCode = StringUtils.isEmpty(mixCode) ? "" : mixCode.substring(0, mixCode.length() - 1);

        /*try {
            mixCode = new String(mixCode.getBytes(), "UTF-8").trim();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
        mixCode = MD5Utils.getMD5Msg(mixCode.trim());

        return mixCode;
    }

    @Transient
    public int getCurrentStock() {
        int _tmp = (int) (this.stock - this.getVolume());
        return _tmp <= 0 ? 0 : _tmp;
    }

    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }

    @Transient
    public String getSkuValue() {
        return skuValue;
    }

    public void setSkuValue(String skuValue) {
        this.skuValue = skuValue;
    }
}
