package cn.com.zhihetech.util.hibernate;

import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.exception.HqlException;
import cn.com.zhihetech.util.hibernate.param.IParam;
import com.sun.istack.internal.NotNull;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 子查询参数
 * Created by ShenYunjie on 2015/11/13.
 */
public class SubQueryParams extends SimpleQueryParams {

    private static Log log = LogFactory.getLog(SubQueryParams.class);
    protected final String SUB_QUERY_KEY = "_sub_";

    /**
     * 返回不以"and"或"or"开始的HQL语句
     *
     * @return
     */
    @Override
    public String getTargetHQL() {
        StringBuffer hql = new StringBuffer("1 = 1");
        hql.append(" ").append(getWhereHQL())
                .append(" ").append(getSortHQL());
        log.debug(hql);
        return hql.toString().trim();
    }

    /**
     * 返回不以"and"或"or"开始的不带排序的HQL语句
     *
     * @return
     */
    @Override
    public String getWithOutOrderTargetHQL() {
        StringBuffer hql = new StringBuffer("1 = 1").append(" ")
                .append(getWhereHQL().trim());
        log.debug(hql);
        return hql.toString();
    }

    @Override
    public IQueryParams addParam(@NotNull String key, IParam param) {
        if (StringUtils.isEmpty(key)) {
            throw new HqlException("property name not able null");
        }
        key = key.trim();
        String keyAlias = SUB_QUERY_KEY + key;
        int index = 0;
        param.setKeyAlias(keyAlias);
        while (this.queryParams.containsKey(param.getPlaceholderTitle())) {
            index++;
            keyAlias = SUB_QUERY_KEY + key + index;
            param.setKeyAlias(keyAlias);
        }
        if (!key.equals(keyAlias)) {
            param.setKeyAlias(keyAlias);
        }
        queryParams.put(param.getPlaceholderTitle(), param);
        return this;
    }

    /**
     * 自定义参数别名
     *
     * @param keyName  需要设置别名的属性名称
     * @param keyAlias 别名
     */
    public void setKeyAlias(String keyName, String keyAlias) {
        for (String key : this.queryParams.keySet()) {
            if (key.equals(keyName)) {
                this.queryParams.get(key).setKeyAlias(keyAlias);
            }
        }
    }
}
