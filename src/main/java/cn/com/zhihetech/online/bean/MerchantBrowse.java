package cn.com.zhihetech.online.bean;

import cn.com.zhihetech.online.commons.Constant;
import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by YangDaiChun on 2015/11/13.
 */

@Entity
@Table(name = "t_merchant_browse")
public class MerchantBrowse {

    private String merchantBrowseId;
    private Merchant merchant;
    private User user;
    private Date browseDate;

    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid2")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "browse_id", length = 36)
    public String getMerchantBrowseId() {
        return merchantBrowseId;
    }

    public void setMerchantBrowseId(String merchantBrowseId) {
        this.merchantBrowseId = merchantBrowseId;
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
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Column(name = "browse_date",updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getBrowseDate() {
        return browseDate;
    }

    public void setBrowseDate(Date browseDate) {
        this.browseDate = browseDate;
    }
}
