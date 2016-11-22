package cn.com.zhihetech.online.exception;

/**
 * Created by ShenYunjie on 2015/11/18.
 */
public class SystemException extends SystemRuntimeException {
    private String msg;

    public SystemException(String message) {
        super(message);
        this.msg = message;
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
        this.msg = message;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
