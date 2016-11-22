package cn.com.zhihetech.online.bean;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by ydc on 16-4-21.
 */
@Entity
@Table(name = "t_lucky_draw")
public class LuckyDraw extends SerializableAndCloneable {

    private String luckyDrawId;  //id
    private String desc; //次奖项的具体描述
    private int ldOrder;  //排序
    private int amount;  //奖品数量
    private float percentage; //所占百分比
    private Merchant merchant;  //此奖项的赞助商家
    private LuckyDrawActivity luckyDrawActivity;  //奖项属于哪个活动
    private boolean delState; //是否是删除状态,true则删除，false则为活动状态
    private String title;  //奖项名
    private boolean luckState; //幸运状态,如添加奖项为谢谢参与
    private int surplus;  //还剩多少

    @javax.persistence.Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "ld_id", length = 36, nullable = false)
    public String getLuckyDrawId() {
        return luckyDrawId;
    }

    public void setLuckyDrawId(String luckyDrawId) {
        this.luckyDrawId = luckyDrawId;
    }

    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name = "ld_desc", nullable = false)
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Column(name = "ld_order", nullable = false)
    public int getLdOrder() {
        return ldOrder;
    }

    public void setLdOrder(int ldOrder) {
        this.ldOrder = ldOrder;
    }

    @Column(name = "ld_amount")
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Column(name = "ld_percentage")
    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    @ManyToOne
    @JoinColumn(name = "merchant_id")
    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    @ManyToOne
    @JoinColumn(name = "lda_id")
    public LuckyDrawActivity getLuckyDrawActivity() {
        return luckyDrawActivity;
    }

    public void setLuckyDrawActivity(LuckyDrawActivity luckyDrawActivity) {
        this.luckyDrawActivity = luckyDrawActivity;
    }

    @Column(name = "del_state")
    public boolean isDelState() {
        return delState;
    }

    public void setDelState(boolean delState) {
        this.delState = delState;
    }

    @Column(name = "ld_title", nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "ld_luck_state")
    public boolean isLuckState() {
        return luckState;
    }

    public void setLuckState(boolean luckState) {
        this.luckState = luckState;
    }

    @Column(name = "surplus_amount", nullable = true)
    public int getSurplus() {
        return surplus;
    }

    public void setSurplus(int surplus) {
        this.surplus = surplus;
    }
}
