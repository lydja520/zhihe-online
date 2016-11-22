package cn.com.zhihetech.online.bean;

import cn.com.zhihetech.online.commons.Constant;
import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 活动类别
 * Created by ShenYunjie on 2015/12/4.
 */
@Entity
@Table(name = "t_activity_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ActivityCategory extends SerializableAndCloneable {
    private String categId;
    private String categName;
    private boolean official;    //是否是官方活动，如果是则商家自行组织的活动类别中不显示此项活动类别
    private int categType;   //活动种类，活动只能是系统常量中的活动种类的值
    private String categDesc;
    private Date createDate;

    @javax.persistence.Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "category_id", length = 36)
    public String getCategId() {
        return categId;
    }

    public void setCategId(String categId) {
        this.categId = categId;
    }

    @Column(name = "cate_name", length = 50, nullable = false)
    public String getCategName() {
        return categName;
    }

    public void setCategName(String categName) {
        this.categName = categName;
    }

    @Column(name = "official", nullable = false)
    public boolean getOfficial() {
        return official;
    }

    public void setOfficial(boolean official) {
        this.official = official;
    }

    @Column(name = "cate_type", nullable = false)
    public int getCategType() {
        return categType;
    }

    public void setCategType(int categType) {
        this.categType = categType;
    }

    @Column(name = "cate_desc", length = 300)
    public String getCategDesc() {
        return categDesc;
    }

    public void setCategDesc(String categDesc) {
        this.categDesc = categDesc;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", updatable = false, nullable = false)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
