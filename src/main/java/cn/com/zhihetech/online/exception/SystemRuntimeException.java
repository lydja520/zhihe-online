package cn.com.zhihetech.online.exception;

/**
 * Created by ShenYunjie on 2015/11/17.
 */
public abstract class SystemRuntimeException extends RuntimeException {

    public SystemRuntimeException() {
    }

    public SystemRuntimeException(String message) {
        super(message);
    }

    public SystemRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SystemRuntimeException(Throwable cause) {
        super(cause);
    }

    public SystemRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
