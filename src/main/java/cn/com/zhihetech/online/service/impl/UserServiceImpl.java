package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.*;
import cn.com.zhihetech.online.commons.*;
import cn.com.zhihetech.online.dao.IActivityFansDao;
import cn.com.zhihetech.online.dao.IFocusMerchantDao;
import cn.com.zhihetech.online.dao.IMerchantDao;
import cn.com.zhihetech.online.dao.IUserDao;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.exception.UserPhoneRepeatException;
import cn.com.zhihetech.online.service.ILoginJournalService;
import cn.com.zhihetech.online.service.IUserService;
import cn.com.zhihetech.online.util.DateUtils;
import cn.com.zhihetech.online.util.JerseyUtils;
import cn.com.zhihetech.online.util.VerifyRegexp;
import cn.com.zhihetech.online.vo.ClientSecretCredential;
import cn.com.zhihetech.online.vo.Credential;
import cn.com.zhihetech.online.vo.EndPoints;
import cn.com.zhihetech.online.vo.UserSearch;
import cn.com.zhihetech.util.common.MD5Utils;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.*;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.glassfish.jersey.client.JerseyWebTarget;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ShenYunjie on 2015/12/15.
 */
@Service("userService")
public class UserServiceImpl implements IUserService {
    @Resource(name = "userDao")
    private IUserDao userDao;
    @Resource(name = "activityFansDao")
    private IActivityFansDao activityFansDao;
    @Resource(name = "focusMerchantDao")
    private IFocusMerchantDao focusMerchantDao;
    @Resource(name = "merchantDao")
    private IMerchantDao merchantDao;
    @Resource(name = "loginJournalService")
    private ILoginJournalService loginJournalService;

    private static final String APPKEY = AppConfig.EasemobConfig.EM_APP_KEY;
    private static final JsonNodeFactory factory = new JsonNodeFactory(false);

    // 通过app的client_id和client_secret来获取app管理员token
    private static Credential credential = new ClientSecretCredential(AppConfig.EasemobConfig.APP_CLIENT_ID, AppConfig.EasemobConfig.APP_CLIENT_SECRET, Roles.USER_ROLE_APPADMIN);

    /**
     * 根据ID获取持久化对象
     *
     * @param id 对象ID
     * @return
     */
    @Override
    public User getById(String id) {
        return this.userDao.findEntityById(id);
    }

    /**
     * 删除持久化对象
     *
     * @param user 需要删除的持久化对象
     */
    @Override
    public void delete(User user) {

    }

    /**
     * 添加一个对象到数据库
     *
     * @param user 需要持久化的对象
     * @return
     */
    @Override
    public User add(User user) {
        return this.userDao.saveEntity(user);
    }

    /**
     * 添加一个用户
     *
     * @param user
     * @param request
     * @return
     */
    @Override
    public void addUser(User user, HttpServletRequest request) {
        if (isExists(user)) {
            throw new UserPhoneRepeatException("此号码已注册，请勿重复注册");
        }
        if (!VerifyRegexp.isMobileNum(user.getUserPhone())) {
            throw new SystemException("你注册的账号并不是手机号码，账号处请填入正确的手机号码！");
        }
        user.setPwd(MD5Utils.getMD5Msg(user.getPwd()));
        user.setCreateDate(new Date());
        user.setPermit(true);
        String userId = UUID.randomUUID().toString();
        user.setUserId(userId);
        user.setChatPassword(userId);
        user.setChatUserName(userId.replaceAll("-", ""));
        this.userDao.saveEntity(user);
        /*注册环信用户[单个]*/
        ObjectNode datanode = JsonNodeFactory.instance.objectNode();
        datanode.put("username", user.getChatUserName());
        datanode.put("password", user.getChatPassword());
        ObjectNode createNewIMUserSingleNode = createNewIMUserSingle(datanode);
        if (createNewIMUserSingleNode == null) {
            //TODO:短信注册失败
            throw new SystemException("环信注册失败");
        }
    }

    @Override
    public void updatePwd(String userId, String oldPwd, String newPwd) {
        GeneralQueryParams queryParams = new GeneralQueryParams();
        oldPwd = MD5Utils.getMD5Msg(oldPwd);
        queryParams.andEqual("userId", userId).andEqual("pwd", oldPwd);
        List<User> users = this.userDao.getEntities(null, queryParams);
        if (users.size() > 0) {
            User user = users.get(0);
            newPwd = MD5Utils.getMD5Msg(newPwd);
            user.setPwd(newPwd);
            this.userDao.updateEntity(user);
        } else {
            throw new SystemException("旧密码错误！");
        }
    }

    @Override
    public void updateUserPortrait(String userId, String imgInfoId) {
        User user = this.userDao.findEntityById(userId);
        if (user == null) {
            throw new SystemException("不存在该用户");
        }
        ImgInfo imgInfo = new ImgInfo();
        imgInfo.setImgInfoId(imgInfoId);
        user.setPortrait(imgInfo);
        this.userDao.updateEntity(user);
    }

    @Override
    public User updateUser(User user) {
        User _user = this.userDao.findEntityById(user.getUserId());
        if (_user == null) {
            throw new SystemException("不存在该用户");
        }
        _user.setUserName(user.getUserName());
        _user.setSex(user.isSex());
        _user.setBirthday(user.getBirthday());
        _user.setOccupation(user.getOccupation());
        _user.setArea(user.getArea());
        _user.setIncome(user.getIncome());
        this.userDao.updateEntity(_user);
        return _user;
    }

    /**
     * 注册IM用户[单个]
     * <p>
     * 给指定AppKey创建一个新的用户
     *
     * @param dataNode
     * @return
     */
    public static ObjectNode createNewIMUserSingle(ObjectNode dataNode) {
        ObjectNode objectNode = null;
        try {
            JerseyWebTarget webTarget = EndPoints.USERS_TARGET.resolveTemplate("org_name", APPKEY.split("#")[0]).resolveTemplate("app_name", APPKEY.split("#")[1]);
            objectNode = JerseyUtils.sendRequest(webTarget, dataNode, credential, HTTPMethod.METHOD_POST, null);
        } catch (Exception e) {
            return null;
        }
        return objectNode;
    }

    /**
     * 更新一个持久化对象
     *
     * @param user
     */
    @Override
    public void update(User user) {
        this.userDao.updateEntity(user);
    }

    /**
     * 根据分页参数和查询条件查询持久化对象
     *
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return
     */
    @Override
    public List<User> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.userDao.getEntities(pager, queryParams);
    }

    /**
     * @param pager
     * @param queryParams
     * @return 返回用户的id
     */
    public String getUserId(Pager pager, IQueryParams queryParams) {
        List<User> users = this.userDao.getEntities(pager, queryParams);
        if (users.size() == 0) {
            return null;
        }
        User user = users.get(0);
        return user.getUserId();
    }


    /**
     * 根据分页参数和查询条件查询持久化对象
     *
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return 经过分页的数据
     */
    @Override
    public PageData<User> getPageData(Pager pager, IQueryParams queryParams) {
        //TODO:实现获取用户分页信息
        return this.userDao.getPageData(pager, queryParams);
    }

    /**
     * @param pager
     * @param queryParams
     * @param activityId  指定活动ID
     * @param merchantId  指定商家
     * @return
     */
    @Override
    public PageData<User> getAbleUserByActivityIdAndMerchantId(Pager pager, IQueryParams queryParams, String activityId, String merchantId) {
        IQueryParams _queryParams = new GeneralQueryParams();
        _queryParams.andEqual("activity.activitId", activityId);
        List<Object> joindIds = this.activityFansDao.getProperty("fans.userId", null, _queryParams);
        _queryParams = new GeneralQueryParams();
        _queryParams.andEqual("merchant.merchantId", merchantId);
        List<Object> unJoinIds = this.focusMerchantDao.getProperty("user.userId", null, _queryParams);

        queryParams.andNotIn("userId", joindIds).andIn("userId", unJoinIds).andEqual("permit", true);
        return this.userDao.getPageData(pager, queryParams);
    }

    /**
     * 判断一个用户是否一存在
     *
     * @param user
     * @return
     */
    private boolean isExists(User user) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("userPhone", user.getUserPhone());
        List<Object> ids = this.userDao.getProperty("userId", null, queryParams);
        if (ids.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<Object> getProperty(String selector, Pager pager, IQueryParams queryParams) {
        return this.userDao.getProperty(selector, pager, queryParams);
    }

    /**
     * 用户登录
     *
     * @param userPhone 用户手机号
     * @param pwd       登录密码
     * @param header    请求头信息
     * @return
     */
    @Override
    public TokenAndUser executeUserLogin(String userPhone, String pwd, RequestHeader header) {
        LoginJournal journal = new LoginJournal(header);
        journal.setUserCode(userPhone);
        journal.setLoginDate(new Date());
        journal.setLoginType(LoginJournal.LoginType.user);
        journal.setLoginJournalId(UUID.randomUUID().toString());
        try {
            if (StringUtils.isEmpty(userPhone) || StringUtils.isEmpty(pwd)) {
                throw new SystemException("手机号码或密码为空！");
            }
            pwd = MD5Utils.getMD5Msg(pwd);
            IQueryParams queryParams = new GeneralQueryParams().andEqual("userPhone", userPhone);
            List<User> users = this.userDao.getEntities(queryParams);
            if (users.size() < 1) {
                throw new SystemException("用户不存在！");
            }
            User user = users.get(0);
            if (!user.isPermit()) {
                throw new SystemException("用户已被禁用！");
            }
            if (!pwd.equals(user.getPwd())) {
                throw new SystemException("密码不正确！");
            }
            TokenAndUser tokenAndUser = TokenInfo.createUserToken(user, journal.getLoginJournalId());
            journal.setSuccess(true);
            return tokenAndUser;
        } catch (Exception e) {
            journal.setSuccess(false);
            journal.setFailReason(e.getMessage());
            throw new SystemException(e.getMessage());
        } finally {
            this.loginJournalService.saveJournalAlways(journal);
        }
    }

    //TODO:关联查询用户信息
    @Override
    public PageData<User> getRelationSearchEntitys(Pager pager, IQueryParams queryParams, UserSearch userSearch) {
        StringBuffer initTime = null;
        StringBuffer endTime = null;
        /* ======电话号码和邀请码搜索======*/
        if (!cn.com.zhihetech.online.util.StringUtils.isEmpty(userSearch.getSearchPhone())) {
            queryParams.andAllLike("userPhone", userSearch.getSearchPhone());
        }
        if (!cn.com.zhihetech.online.util.StringUtils.isEmpty(userSearch.getSearchInvitCode())) {
            queryParams.andAllLike("invitCode", userSearch.getSearchInvitCode());
        }
        /*   ========TimePeriod时间段查询=========     */
        if (!cn.com.zhihetech.online.util.StringUtils.isEmpty(userSearch.getInitTime())) {
            initTime = new StringBuffer(userSearch.getInitTime());
            initTime.append(" 00:00:00");
            Date startTime = DateUtils.String2DateTime(initTime.toString());
            queryParams.andMoreAndEq("createDate", startTime);
        }
        if (!cn.com.zhihetech.online.util.StringUtils.isEmpty(userSearch.getEndTime())) {
            endTime = new StringBuffer(userSearch.getEndTime());
            endTime.append(" 23:59:59");
            Date overTime = DateUtils.String2DateTime(endTime.toString());
            queryParams.andLessAndEq("createDate", overTime);
        }
        /*===============时间排序==================*/
        if (!queryParams.sortContainsKey("createDate")) {
            queryParams.sort("createDate", cn.com.zhihetech.util.hibernate.Order.DESC);
        }
        return this.userDao.getPageData(pager, queryParams);
    }
}
