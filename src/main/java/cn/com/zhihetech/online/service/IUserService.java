package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.RequestHeader;
import cn.com.zhihetech.online.bean.User;
import cn.com.zhihetech.online.commons.TokenAndUser;
import cn.com.zhihetech.online.vo.UserSearch;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import cn.com.zhihetech.util.hibernate.service.SupportService;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

/**
 * Created by ShenYunjie on 2015/12/15.
 */
public interface IUserService extends SupportService<User> {
    /**
     * 获取还未参加指定活动的指定商家可邀请的有效用户
     *
     * @param pager
     * @param queryParams
     * @param activityId  指定活动ID
     * @param merchantId  指定商家
     * @return
     */
    PageData<User> getAbleUserByActivityIdAndMerchantId(Pager pager, IQueryParams queryParams, String activityId, String merchantId);

    /**
     * 获取用户Id
     *
     * @param pager
     * @param queryParams
     * @return
     */
    String getUserId(Pager pager, IQueryParams queryParams);

    void addUser(User user, HttpServletRequest request);

    void updatePwd(String userId, String oldPwd, String newPwd);

    void updateUserPortrait(String userId, String imgInfoId);

    User updateUser(User user);

    List<Object> getProperty(String selector, Pager pager, IQueryParams queryParams);


    /**
     * 用户登录
     *
     * @param userPhone 用户手机号
     * @param pwd       登录密码
     * @param header    请求头信息
     * @return
     */
    TokenAndUser executeUserLogin(String userPhone, String pwd, RequestHeader header);

    /**
     * 用户关联查询
     * @param pager
     * @param queryParams
     * @param userSearch
     * @return
     */
    //TODO:通过时间段获取用户信息
    PageData<User> getRelationSearchEntitys(Pager pager,IQueryParams queryParams,UserSearch userSearch);
}
