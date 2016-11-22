package cn.com.zhihetech.online.exception;

/**
 * 数据重复异常
 * Created by ShenYunjie on 2015/11/17.
 */
public class DataRepeatException extends SystemRuntimeException {
    private String msg;

    public DataRepeatException(String msg) {
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
