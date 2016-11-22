package cn.com.zhihetech.online.bean;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by ShenYunjie on 2016/7/6.
 */
/*@Entity
@Table(name = "t_goods_sku")*/
public class GoodsSku extends SerializableAndCloneable {
    private String goodsSkuId;
    private Goods goods;
    private String skuValue;
    private float price; //商品单价
    private long stock; //商品库存量
    private long volume; //商品的销量
    private ImgInfo coverImg;   //对应此sku组合的封面（如果没有默认为对应的商品封面）

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "goods_sku_id", length = 36)
    public String getGoodsSkuId() {
        return goodsSkuId;
    }

    public void setGoodsSkuId(String goodsSkuId) {
        this.goodsSkuId = goodsSkuId;
    }

    @ManyToOne
    @JoinColumn(name = "goods_id", nullable = false)
    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    @Column(name = "sku_value", nullable = false, length = 200)
    public String getSkuValue() {
        return skuValue;
    }

    public void setSkuValue(String skuValue) {
        this.skuValue = skuValue;
    }

    @Column(name = "price", nullable = false)
    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
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

    @ManyToOne
    @JoinColumn(name = "img_id", nullable = false)
    public ImgInfo getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(ImgInfo coverImg) {
        this.coverImg = coverImg;
    }
}
