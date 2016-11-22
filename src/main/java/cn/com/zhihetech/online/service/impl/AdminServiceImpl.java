package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.*;
import cn.com.zhihetech.online.commons.TokenAndMerchant;
import cn.com.zhihetech.online.dao.IAdminDao;
import cn.com.zhihetech.online.dao.IMerchantDao;
import cn.com.zhihetech.online.exception.DataRepeatException;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IAdminService;
import cn.com.zhihetech.online.service.ILoginJournalService;
import cn.com.zhihetech.online.service.IRoleService;
import cn.com.zhihetech.util.common.MD5Utils;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by ShenYunjie on 2015/11/17.
 */
@Service("adminService")
@Transactional
public class AdminServiceImpl implements IAdminService {
    @Resource(name = "adminDao")
    private IAdminDao adminDao;
    @Resource(name = "roleService")
    private IRoleService roleService;
    @Resource(name = "merchantDao")
    private IMerchantDao merchantDao;
    @Resource(name = "loginJournalService")
    private ILoginJournalService loginJournalService;

    @Override
    public Admin getById(String id) {
        return this.adminDao.findEntityById(id);
    }

    @Override
    public void delete(Admin admin) {
        this.adminDao.deleteEntity(admin);
    }

    @Override
    public Admin add(Admin admin) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("adminCode", admin.getAdminCode());
        List<Admin> admins = getAllByParams(null, queryParams);
        if (admins.size() > 0) {
            throw new DataRepeatException("管理员登录账号已存在");
        }
        return this.adminDao.saveEntity(admin);
    }

    @Override
    public void update(Admin admin) {
        this.adminDao.updateEntity(admin);
    }

    @Override
    public List<Admin> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.adminDao.getEntities(pager, queryParams);
    }

    @Override
    public PageData<Admin> getPageData(Pager pager, IQueryParams queryParams) {
        return this.adminDao.getPageData(pager, queryParams);
    }

    @Override
    public Admin executeLogin(String adminCode, String adminPwd) {
        IQueryParams queryParams = new GeneralQueryParams().andEqual("adminCode", adminCode)
                .andEqual("adminPwd", adminPwd).andEqual("permit", true);
        List<Admin> admins = this.adminDao.getEntities(queryParams);
        if (admins.size() < 1) {
            return null;
        }
        Admin admin = admins.get(0);
        if(admin.getMerchant() != null && !admin.getMerchant().isPermit()){
            throw new SystemException("此商家已经被禁用了！");
        }
        return admin;
    }

    @Override
    public List<Menu> getMenusByAndmin(Admin currentAdmin) {
        return this.adminDao.getMenusByAdminId(currentAdmin.getAdminId());
    }

    @Override
    public void disableAdmin(Admin admin) {
        Admin _admin = this.adminDao.findEntityById(admin.getAdminId());
        _admin.setPermit(admin.isPermit());
        this.adminDao.updateEntity(_admin);
    }

    @Override
    public void resetPwd(Admin admin) {
        Admin _admin = this.adminDao.findEntityById(admin.getAdminId());
        _admin.setAdminPwd(admin.getAdminPwd());
        this.adminDao.updateEntity(_admin);
    }

    @Override
    public Merchant getMerchant(Admin admin) {
        String merchantId = this.getMerchantId(admin);
        if (StringUtils.isEmpty(merchantId)) {
            return null;
        }
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("merchantId", merchantId);
        return this.merchantDao.findEntityById(merchantId);
    }

    @Override
    public String getMerchantId(Admin admin) {
        IQueryParams queryParams = new GeneralQueryParams().andEqual("adminId", admin.getAdminId());
        List<Object> tmps = this.adminDao.getProperty("merchant.merchantId", null, queryParams);
        if (tmps.size() > 0) {
            return (String) tmps.get(0);
        }
        return null;
    }

    @Override
    public void changePassword(String adminCode, String oldPwd, String newPwd) {
        IQueryParams queryParams = new GeneralQueryParams();
        oldPwd = MD5Utils.getMD5Msg(oldPwd);
        queryParams.andEqual("adminCode", adminCode).andEqual("adminPwd", oldPwd);
        List<Admin> admins = this.adminDao.getEntities(null, queryParams);
        if (admins.size() > 0) {
            Admin admin = admins.get(0);
            newPwd = MD5Utils.getMD5Msg(newPwd);
            admin.setAdminPwd(newPwd);
            this.adminDao.updateEntity(admin);
        } else {
            throw new SystemException("旧密码错误");
        }
    }

    @Override
    public long getRecordTotal(IQueryParams queryParams) {
        return this.adminDao.getRecordTotal(queryParams);
    }

    @Override
    public TokenAndMerchant executeAppLogin(String adminCode, String adminPwd, RequestHeader header) {
        LoginJournal journal = new LoginJournal(header);
        journal.setUserCode(adminCode);
        journal.setLoginDate(new Date());
        journal.setLoginType(LoginJournal.LoginType.merchant);
        journal.setLoginJournalId(UUID.randomUUID().toString());
        try {
            if (StringUtils.isEmpty(adminCode) || StringUtils.isEmpty(adminPwd)) {
                throw new SystemException("用户名或密码为空！");
            }
            adminPwd = MD5Utils.getMD5Msg(adminPwd);
            IQueryParams queryParams = new GeneralQueryParams().andEqual("adminCode", adminCode)
                    .andNotNull("merchant");
            List<Admin> admins = this.adminDao.getEntities(null, queryParams);
            if (admins.size() < 1) {
                throw new SystemException("用户不存在！");
            }
            Admin admin = admins.get(0);
            if (!adminPwd.equals(admin.getAdminPwd())) {
                throw new SystemException("登录密码不正确！");
            }
            if (!admin.isPermit()) {
                throw new SystemException("此账号已被禁用！");
            }
            Merchant merchant = admin.getMerchant();
            TokenAndMerchant tokenAndMerchant = TokenInfo.createMerchantToken(admin, merchant, journal.getLoginJournalId());
            tokenAndMerchant.setChatUser(createChatUser(admin));
            journal.setSuccess(true);
            return tokenAndMerchant;
        } catch (Exception e) {
            journal.setSuccess(false);
            journal.setFailReason(e.getMessage());
            throw new SystemException(e.getMessage());
        } finally {
            this.loginJournalService.saveJournalAlways(journal);
        }
    }

    /**
     * 添加系统平台管理员
     *
     * @param admin
     * @return
     */
    @Override
    public Admin addOfficialAdmin(Admin admin) {
        admin.setOfficial(true);
        admin.setSuperAdmin(false);
        return add(admin);
    }

    @Override
    public void executeAdminPortrait(Map<String, Object> paramAndValue, IQueryParams queryParams) {
        this.adminDao.executeUpdate(paramAndValue, queryParams);
    }

    protected ChatUserInfo createChatUser(Admin admin) {
        ChatUserInfo chatUserInfo = new ChatUserInfo(admin.getChatUserName(), admin.getChatPassword());
        if (admin.getMerchant() != null) {
            chatUserInfo.setAppUserId(admin.getAdminId()); //对应为当前管理员的id
        }
        if (!StringUtils.isEmpty(admin.getChatNickName())) {
            chatUserInfo.setNickName(admin.getChatNickName());
        }
        if (admin.getPortrait() != null) {
            chatUserInfo.setPortraitUrl(admin.getPortrait().getUrl());
        }
        return chatUserInfo;
    }
}
