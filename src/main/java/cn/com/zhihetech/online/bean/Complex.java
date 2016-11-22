package cn.com.zhihetech.online.bean;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by YangDaiChun on 2016/2/17.
 */

@Entity
@Table(name = "t_complex")
public class Complex extends SerializableAndCloneable{

    private String complexId;
    private String name;
    private District district;

    @Id
    @GenericGenerator(name = "systemUUID",strategy = "uuid2")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "id",length = 36)
    public String getComplexId() {
        return complexId;
    }

    public void setComplexId(String complexId) {
        this.complexId = complexId;
    }

    @Column(name = "name",nullable = true,length = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne
    @JoinColumn(name = "district_id")
    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }
}
