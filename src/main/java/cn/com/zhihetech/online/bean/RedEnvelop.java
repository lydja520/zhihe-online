package cn.com.zhihetech.online.bean;

import cn.com.zhihetech.online.commons.Constant;
import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 红包对应实体类
 * Created by ShenYunjie on 2015/12/11.
 */
@Entity
@Table(name = "t_red_envelop")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RedEnvelop extends SerializableAndCloneable {
    private String envelopId;
    private float totalMoney;   //红包金额（总额）
    private int numbers;    //红包个数
    private Merchant merchant;  //红包对应的商家
    private Activity activity;  //红包对应的活动
    private Date createDate;
    private boolean sended; //是否已发出
    private Date sendDate;  //红包发出时间
    private String envelopMsg;  //留言信息
    private boolean payState; //红包的钱是否已经通过支付包打给实淘

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "envelop_id", length = 36)
    public String getEnvelopId() {
        return envelopId;
    }

    public void setEnvelopId(String envelopId) {
        this.envelopId = envelopId;
    }

    @Column(name = "total_money", nullable = false)
    public float getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(float totalMoney) {
        this.totalMoney = totalMoney;
    }

    @Column(name = "numbers", nullable = false)
    public int getNumbers() {
        return numbers;
    }

    public void setNumbers(int numbers) {
        this.numbers = numbers;
    }

    @ManyToOne
    @JoinColumn(name = "merchant_id", nullable = false)
    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false)
    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", updatable = false, nullable = false)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "sended", nullable = false)
    public boolean isSended() {
        return sended;
    }

    public void setSended(boolean sended) {
        this.sended = sended;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sended_date")
    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    @Column(name = "envelop_msg", length = 100)
    public String getEnvelopMsg() {
        return envelopMsg;
    }

    public void setEnvelopMsg(String envelopMsg) {
        this.envelopMsg = envelopMsg;
    }

    @Column(name = "pay_state")
    public boolean isPayState() {
        return payState;
    }

    public void setPayState(boolean payState) {
        this.payState = payState;
    }
}
