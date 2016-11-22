package cn.com.zhihetech.online.exception;

/**
 * Created by ShenYunjie on 2015/11/17.
 */
public class ParamException extends SystemRuntimeException {
    private String msg;

    public ParamException(String msg) {
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
