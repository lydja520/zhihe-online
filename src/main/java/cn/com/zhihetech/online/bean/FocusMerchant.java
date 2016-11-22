package cn.com.zhihetech.online.bean;

import cn.com.zhihetech.online.commons.Constant;
import com.alibaba.fastjson.annotation.JSONField;
import org.aspectj.lang.annotation.control.CodeGenerationHint;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by YangDaiChun on 2015/11/12.
 */
@Entity
@Table(name = "t_focus_merchant")
public class FocusMerchant extends SerializableAndCloneable {

    private String focusMerchantId;
    private User user;
    private Merchant merchant;
    private Date focusDate;

    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid2")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "focus_id", length = 36)
    public String getFocusMerchantId() {
        return focusMerchantId;
    }

    public void setFocusMerchantId(String focusMerchantId) {
        this.focusMerchantId = focusMerchantId;
    }


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "merchant_id", nullable = false)
    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Column(name = "focus_date", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getFocusDate() {
        return focusDate;
    }

    public void setFocusDate(Date focusDate) {
        this.focusDate = focusDate;
    }
}
