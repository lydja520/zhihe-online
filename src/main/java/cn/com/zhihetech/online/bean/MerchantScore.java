package cn.com.zhihetech.online.bean;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by YangDaiChun on 2016/2/18.
 */
@Entity
@Table(name = "t_merchant_score")
public class MerchantScore extends SerializableAndCloneable {
    private String merScoreId;
    private Merchant merchant;
    private float score = 0;

    @Id
    @GenericGenerator(name = "systemUUID",strategy = "uuid2")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "id",length = 36)
    public String getMerScoreId() {
        return merScoreId;
    }

    public void setMerScoreId(String merScoreId) {
        this.merScoreId = merScoreId;
    }

    @ManyToOne
    @JoinColumn(name = "merchant_id")
    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    @Column(name = "score")
    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
