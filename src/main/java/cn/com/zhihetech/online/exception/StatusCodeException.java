package cn.com.zhihetech.online.exception;

/**
 * 带有状态码的异常
 * Created by ShenYunjie on 2016/5/7.
 */
public class StatusCodeException extends SystemRuntimeException {
    private int code;

    public StatusCodeException(String message, int code) {
        super(message);
        this.code = code;
    }

    public StatusCodeException(String message, int code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
