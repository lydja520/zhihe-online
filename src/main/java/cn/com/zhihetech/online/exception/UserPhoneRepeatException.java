package cn.com.zhihetech.online.exception;

/**
 * Created by ShenYunjie on 2015/12/16.
 */
public class UserPhoneRepeatException extends SystemRuntimeException {
    private String msg;

    public UserPhoneRepeatException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
