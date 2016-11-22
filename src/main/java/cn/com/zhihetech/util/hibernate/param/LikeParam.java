package cn.com.zhihetech.util.hibernate.param;


import cn.com.zhihetech.util.hibernate.LikeType;
import org.hibernate.Query;

/**
 * Created by ShenYunjie on 2015/11/13.
 */
public abstract class LikeParam extends SimpleParam {

    protected final int LOGIC_AND_FLAG = 1;
    protected final int LOGIC_OR_FLAG = 2;

    private LikeType likeType;

    /**
     * @param key   属性名称
     * @param value 属性值
     */
    public LikeParam(String key, Object value) {
        super(key, value);
        this.likeType = LikeType.BOTH_LIKE;
    }

    public LikeType getLikeType() {
        return this.likeType;
    }

    /**
     * @param key      属性名称
     * @param value    属性值
     * @param likeType 模糊查询方式
     */
    public LikeParam(String key, Object value, LikeType likeType) {
        super(key, value);
        this.likeType = likeType;
    }

    /**
     * 获取此参数值的最终HQL语句
     *
     * @return
     */
    @Override
    public Object getValue() {
        return this.value;
    }

    protected String getLikeHQL(int logicFlag) {
        String _like = ":" + getPlaceholderTitle();
        switch (logicFlag) {
            case LOGIC_AND_FLAG:
                _like = LOGIC_AND_KEY + " " + this.key + " like " + _like;
                break;
            case LOGIC_OR_FLAG:
                _like = LOGIC_OR_KEY + " " + this.key + " like " + _like;
                break;
        }
        return _like;
    }

    @Override
    public abstract void initQueryValues(Query query);
}
