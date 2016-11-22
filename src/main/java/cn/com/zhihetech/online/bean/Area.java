package cn.com.zhihetech.online.bean;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by YangDaiChun on 2015/11/12.
 */

@Entity
@Table(name = "t_area")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Area extends SerializableAndCloneable {

    private String areaId;
    private String areaName;
    private boolean isRoot;
    private Area parentArea;
    private String fullName;

    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid2")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "area_id", length = 36)
    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    @Column(name = "area_name", length = 300)
    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    @Column(name = "is_root")
    public boolean getIsRoot() {
        return isRoot;
    }

    public void setIsRoot(boolean isRoot) {
        this.isRoot = isRoot;
    }

    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = true)
    public Area getParentArea() {
        return parentArea;
    }

    public void setParentArea(Area parentArea) {
        this.parentArea = parentArea;
    }

    @Transient
    public String getFullName() {
        this.fullName = this.getAreaName();
        if(this.getParentArea()!=null){
            this.fullName = getParentFullName(this.getParentArea()) + this.fullName;
        }
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    private String getParentFullName(Area pArea) {

        String tmp = pArea.getAreaName();
        if (pArea.getParentArea() != null && !pArea.isRoot){
            tmp = getParentFullName(pArea.getParentArea()) + tmp;
        }
        return tmp + ",";
    }
}
