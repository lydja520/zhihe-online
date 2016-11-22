package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.bean.Role;
import cn.com.zhihetech.online.bean.SerializableAndCloneable;

import java.util.Date;
import java.util.Set;

/**
 * Created by ShenYunjie on 2015/12/8.
 */
public class SystemUser extends SerializableAndCloneable {
    private String sysUserId;
    private String loginCode;   //登录账号
    private String loginPwd;    //登录密码
    private boolean supperUser; //是否超级用户
    private Set<Role> roles;    //用户拥有的权限
    private boolean permit;
    private Date createDate;
    private Merchant merchant;
}
