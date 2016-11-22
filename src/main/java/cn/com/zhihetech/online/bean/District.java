package cn.com.zhihetech.online.bean;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by YangDaiChun on 2015/11/12.
 */
@Entity
@Table(name = "t_district")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class District extends SerializableAndCloneable {

    private String districtId;
    private Area districtArea;  //商圈所属区域
    private String districtName;
    private String districtDesc; //描述

    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid2")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "district_id", length = 36)
    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }


    @ManyToOne
    @JoinColumn(name = "area_id",nullable = false)
    public Area getDistrictArea() {
        return districtArea;
    }

    public void setDistrictArea(Area districtArea) {
        this.districtArea = districtArea;
    }

    @Column(name = "distract_name", length = 100, nullable = false)
    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    @Column(name = "district_desc", length = 300, nullable = true)
    public String getDistrictDesc() {
        return districtDesc;
    }

    public void setDistrictDesc(String districtDesc) {
        this.districtDesc = districtDesc;
    }
}
