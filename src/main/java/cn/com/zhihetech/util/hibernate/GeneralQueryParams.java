package cn.com.zhihetech.util.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by ShenYunjie on 2015/11/13.
 */
public class GeneralQueryParams extends SimpleQueryParams {

    private static Log log = LogFactory.getLog(GeneralQueryParams.class);

    @Override
    public String getTargetHQL() {
        StringBuffer hql = new StringBuffer(getWhereHQL());
        hql.append(" ").append(getSortHQL());
        log.debug(hql);
        return hql.toString().trim();
    }

    @Override
    public String getWithOutOrderTargetHQL() {
        return getWhereHQL();
    }
}
