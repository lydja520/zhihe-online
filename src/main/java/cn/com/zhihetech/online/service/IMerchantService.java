package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.Admin;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import cn.com.zhihetech.util.hibernate.service.SupportService;

import java.util.List;

/**
 * Created by ShenYunjie on 2015/11/20.
 */
public interface IMerchantService extends SupportService<Merchant> {
    /**
     * 更新商家封面图
     *
     * @param merchant 商家
     */
    void updateCoverImg(Merchant merchant);

    /**
     * 更新商家顶部图
     *
     * @param merchant
     */
    void updateHeadImg(Merchant merchant);

    /**
     * 修改商家的审核状态为审核通过
     *
     * @param merchant
     */
    void updateExaminStateOk(Merchant merchant, Admin admin);

    /**
     * 修改商家的审核状态为审核未通过
     *
     * @param merchantId
     * @param examinMsg
     */
    void updateExaminStateDissmiss(String merchantId, String examinMsg);

    /**
     * 商家注册后保存的商家表和管理员表
     *
     * @param merchant
     * @param admin
     */
    void addMerchantAndAdmin(Merchant merchant, Admin admin);

    /**
     * 商家使用状态更新
     *
     * @param merchantId
     * @param permit
     */
    void updatePermit(String merchantId, boolean permit, String permitMsg);

    /**
     * 修改除商家图片以外的基本信息
     *
     * @param merchant
     */
    void editMerchantInfoForm(Merchant merchant);

    /**
     * @param pager
     * @param queryParams
     * @param goodsNum
     * @return
     */
    PageData<Merchant> getMerchantAndGoods(Pager pager, IQueryParams queryParams, int goodsNum);

    PageData<Merchant> getMerchantsByGoodsAttSet(String goodsAttSetId, Pager pager);

    void addIMMerchant();

    void executeUpdateBusLicePhoto(String merchantId, String imgInfoId);

    List<Object> getProperty(String selector, Pager pager, GeneralQueryParams generalQueryParams);

    void updateMerchantUpdateDate(String merchantId);
}
