package cn.com.zhihetech.util.hibernate.param;

import cn.com.zhihetech.util.hibernate.SubQueryParams;
import com.sun.istack.internal.NotNull;
import org.hibernate.Query;


/**
 * Created by ShenYunjie on 2015/11/13.
 */
public abstract class SubParam implements IParam {

    protected final int LOGIC_OR_FLAG = 1;
    protected final int LOGIC_AND_FLAG = 2;

    /**
     * 查询参数前缀
     */
    final static String PREFIX_KEY = "sub_query_";

    private String key;
    private SubQueryParams subQueryParams;
    private String keyAlias;

    public SubParam(String key, @NotNull SubQueryParams subQueryParams) {
        this.subQueryParams = subQueryParams;
        this.key = key;
        this.keyAlias = this.key;
    }

    @Override
    public void setKeyAlias(String keyAlias) {
        this.keyAlias = keyAlias;
    }

    @Override
    public String getPlaceholderTitle() {
        return this.keyAlias;
    }

    @Override
    public void initQueryValues(Query query) {
        subQueryParams.initQueryParamsValue(query);
    }

    protected String getSubParamHQL(int logicFlag) {
        StringBuffer hql = new StringBuffer();
        if (logicFlag == LOGIC_AND_FLAG) {
            hql.append(LOGIC_AND_KEY);
        } else if (logicFlag == LOGIC_OR_FLAG) {
            hql.append(LOGIC_OR_KEY);
        }
        hql.append(" ( ");
        hql.append(this.subQueryParams.getTargetHQL());
        hql.append(" )");
        return hql.toString().trim();
    }

    /**
     * 获取查询参数的值
     *
     * @return
     */
    @Override
    public Object getValue() {
        return this.subQueryParams;
    }
}
