package cn.com.zhihetech.util.hibernate.param.and;

import cn.com.zhihetech.util.hibernate.LikeType;
import cn.com.zhihetech.util.hibernate.param.LikeParam;
import org.hibernate.Query;

/**
 * Created by ShenYunjie on 2015/11/13.
 */
public class AndLikeParam extends LikeParam {
    public AndLikeParam(String key, Object value) {
        super(key, value);
    }

    public AndLikeParam(String key, Object value, LikeType likeType) {
        super(key, value, likeType);
    }

    @Override
    public void initQueryValues(Query query) {
        switch (getLikeType()) {
            case LEFT_LIKE:
                query.setParameter(getPlaceholderTitle(), "%" + this.value);
                break;
            case RIGHT_LIKE:
                query.setParameter(getPlaceholderTitle(), this.value + "%");
                break;
            default:
                query.setParameter(getPlaceholderTitle(), "%" + this.value + "%");
                break;
        }
    }

    @Override
    public String getTargetHQL() {
        return getLikeHQL(LOGIC_AND_FLAG);
    }
}
