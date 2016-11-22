package cn.com.zhihetech.online.bean;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by ShenYunjie on 2016/1/13.
 */
@Entity
@Table(name = "t_hobby_tag")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class HobbyTag extends SerializableAndCloneable {
    private String tagId;
    private String tagName;
    private String tagDesc; //标签描述
    private HobbyTag parentTag; //父标签

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "tag_id", length = 36)
    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    @Column(name = "tag_name", length = 200, nullable = false)
    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    @Column(name = "tag_desc", length = 500)
    public String getTagDesc() {
        return tagDesc;
    }

    public void setTagDesc(String tagDesc) {
        this.tagDesc = tagDesc;
    }

    @ManyToOne
    @JoinColumn(name = "parent_tag")
    public HobbyTag getParentTag() {
        return parentTag;
    }

    public void setParentTag(HobbyTag parentTag) {
        this.parentTag = parentTag;
    }
}
