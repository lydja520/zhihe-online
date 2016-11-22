package cn.com.zhihetech.online.bean;

import cn.com.zhihetech.online.commons.Constant;

import javax.persistence.*;

/**
 * Created by ShenYunjie on 2016/1/13.
 */
//@Entity
//@DiscriminatorValue("buyer_type")
public class BuyerUser extends User{
    private ImgInfo wxPhoto;    //微信朋友圈截图
    private int wnFriendsCount; //微信好友人数
    private float wxMFRatio;  //微信好友男女比例

    @ManyToOne
    @JoinColumn(name = "wx_photo")
    public ImgInfo getWxPhoto() {
        return wxPhoto;
    }

    public void setWxPhoto(ImgInfo wxPhoto) {
        this.wxPhoto = wxPhoto;
    }

    @Column(name = "wx_friends_count")
    public int getWnFriendsCount() {
        return wnFriendsCount;
    }

    public void setWnFriendsCount(int wnFriendsCount) {
        this.wnFriendsCount = wnFriendsCount;
    }

    @Column(name = "wx_mf_ratio")
    public float getWxMFRatio() {
        return wxMFRatio;
    }

    public void setWxMFRatio(float wxMFRatio) {
        this.wxMFRatio = wxMFRatio;
    }
}
