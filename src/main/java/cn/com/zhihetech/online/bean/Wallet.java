package cn.com.zhihetech.online.bean;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by YangDaiChun on 2016/3/4.
 */
@Entity
@Table(name = "t_wallet")
public class Wallet extends SerializableAndCloneable{

    private String walletId;
    private float totalMoney;
    private User user;

    @Id
    @GenericGenerator(name = "systemUUID",strategy = "uuid2")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "id",length = 36)
    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    @Column(name = "total_money")
    public float getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(float totalMoney) {
        this.totalMoney = totalMoney;
    }

    @OneToOne
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
