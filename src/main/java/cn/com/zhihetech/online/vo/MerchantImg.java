package cn.com.zhihetech.online.vo;

import cn.com.zhihetech.online.bean.ImgInfo;

/**
 * Created by ydc on 16-5-5.
 */
public class MerchantImg {

    private ImgInfo opraterIDPhoto;   //运营者手持身份证照片
    private ImgInfo orgPhoto;   //组织机构代码证原件照片
    private ImgInfo busLicePhoto;   //工商营业执照原件照片
    private ImgInfo applyLetterPhoto;   //加盖公章的申请认证公函（与商家纠纷事件裁定等）照片

    public ImgInfo getOpraterIDPhoto() {
        return opraterIDPhoto;
    }

    public void setOpraterIDPhoto(ImgInfo opraterIDPhoto) {
        this.opraterIDPhoto = opraterIDPhoto;
    }

    public ImgInfo getOrgPhoto() {
        return orgPhoto;
    }

    public void setOrgPhoto(ImgInfo orgPhoto) {
        this.orgPhoto = orgPhoto;
    }

    public ImgInfo getBusLicePhoto() {
        return busLicePhoto;
    }

    public void setBusLicePhoto(ImgInfo busLicePhoto) {
        this.busLicePhoto = busLicePhoto;
    }

    public ImgInfo getApplyLetterPhoto() {
        return applyLetterPhoto;
    }

    public void setApplyLetterPhoto(ImgInfo applyLetterPhoto) {
        this.applyLetterPhoto = applyLetterPhoto;
    }
}
