package cn.com.zhihetech.online.commons;

import cn.com.zhihetech.online.bean.User;

/**
 * Created by YangDaiChun on 2015/12/22.
 */
public class TokenAndUser {

    private User user;
    private String token;

    public TokenAndUser(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
