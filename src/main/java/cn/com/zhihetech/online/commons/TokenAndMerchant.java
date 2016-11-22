package cn.com.zhihetech.online.commons;

import cn.com.zhihetech.online.bean.ChatUserInfo;
import cn.com.zhihetech.online.bean.Merchant;

/**
 * Created by YangDaiChun on 2016/2/29.
 */
public class TokenAndMerchant {
    private Merchant merchant;
    private String token;
    private ChatUserInfo chatUser;

    public TokenAndMerchant(Merchant merchant, String token) {
        this.merchant = merchant;
        this.token = token;
    }

    public TokenAndMerchant(Merchant merchant, String token, ChatUserInfo chatUser) {
        this(merchant, token);
        this.chatUser = chatUser;
    }

    public ChatUserInfo getChatUser() {
        return chatUser;
    }

    public void setChatUser(ChatUserInfo chatUser) {
        this.chatUser = chatUser;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

