package cn.com.zhihetech.online.bean;

import cn.com.zhihetech.online.commons.AppConfig;
import cn.com.zhihetech.online.commons.TokenAndMerchant;
import cn.com.zhihetech.online.commons.TokenAndUser;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.util.EncryptUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Date;

/**
 * Created by ShenYunjie on 2016/4/19.
 */
public class TokenInfo extends SerializableAndCloneable {
    private String appKey = AppConfig.APP_KEY;
    private float dateStamp;
    private String uuid;
    private String userId;
    private String userCode;

    /**
     * @param user           登录的用户
     * @param loginJournalId 登录日志ID
     * @return
     */
    public static TokenAndUser createUserToken(User user, String loginJournalId) {
        TokenInfo token = new TokenInfo();
        token.setUserCode(user.getUserPhone());
        token.setUuid(loginJournalId);
        token.setUserId(user.getUserId());
        token.setDateStamp(new Date().getTime());
        try {
            return new TokenAndUser(user, token.encrypt());
        } catch (Exception e) {
            throw new SystemException("5401: encrypt error ! ");
        }
    }

    /**
     * @param admin          等于的管理员
     * @param merchant       管理员对应的商家
     * @param loginJournalId 登录日志ID
     * @return
     */
    public static TokenAndMerchant createMerchantToken(Admin admin, Merchant merchant, String loginJournalId) {
        TokenInfo token = new TokenInfo();
        token.setUserCode(admin.getAdminCode());
        token.setUuid(loginJournalId);
        token.setUserId(admin.getAdminId());
        token.setDateStamp(new Date().getTime());
        try {
            return new TokenAndMerchant(merchant, token.encrypt());
        } catch (Exception e) {
            throw new SystemException("5401: encrypt error ! ");
        }
    }

    public float getDateStamp() {
        return dateStamp;
    }

    public void setDateStamp(float dateStamp) {
        this.dateStamp = dateStamp;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    /**
     * 获取加密token
     *
     * @return
     */
    public String encrypt() throws Exception {
        String data = JSON.toJSONString(this);
        String token = EncryptUtil.desEncrypt(data, createSecretKey()).replaceAll("\r", "").replaceAll("\n", "");
        return token;
    }

    public TokenInfo decrypt(String token) throws Exception {
        String data = EncryptUtil.desDecrypt(token, createSecretKey());
        return JSONObject.parseObject(data, TokenInfo.class);
    }

    private String createSecretKey() {
        return EncryptUtil.base64Encode(this.appKey);
    }

    @Override
    public String toString() {
        return "TokenInfo{" +
                "dateStamp=" + dateStamp +
                ", uuid='" + uuid + '\'' +
                ", userId='" + userId + '\'' +
                ", userCode='" + userCode + '\'' +
                ", appKey='" + appKey + '\'' +
                '}';
    }
}
