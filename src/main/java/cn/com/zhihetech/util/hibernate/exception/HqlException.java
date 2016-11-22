package cn.com.zhihetech.util.hibernate.exception;

/**
 * 自定义拼接HQL语句异常
 * Created by ShenYunjie on 2015/11/13.
 */
public class HqlException extends AppRuntimeException {

    private String msg;

    public HqlException(String message) {
        super(message);
        this.msg = message;
    }

    public HqlException(String message, Throwable cause) {
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
