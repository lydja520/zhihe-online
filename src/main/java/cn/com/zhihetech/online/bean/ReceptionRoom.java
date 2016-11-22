package cn.com.zhihetech.online.bean;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 会客厅实体类
 * Created by ShenYunjie on 2016/4/5.
 */
@Entity
@Table(name = "t_reception_room")
public class ReceptionRoom extends SerializableAndCloneable {
    private String roomId;
    private String roomName;
    private ImgInfo coverImg;   //封面图
    private String templatePath;
    private GoodsAttributeSet attributeSet; //会客厅所属的类别必填
    private boolean deleted = false;

    @javax.persistence.Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "room_id", length = 36)
    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    @Column(name = "room_name", length = 50, nullable = false)
    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    @ManyToOne
    @JoinColumn(name = "cover_img", nullable = true)
    public ImgInfo getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(ImgInfo coverImg) {
        this.coverImg = coverImg;
    }

    @Column(name = "template_path", length = 100, nullable = false)
    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    @ManyToOne
    @JoinColumn(name = "attribute_id", nullable = false)
    public GoodsAttributeSet getAttributeSet() {
        return attributeSet;
    }

    public void setAttributeSet(GoodsAttributeSet attributeSet) {
        this.attributeSet = attributeSet;
    }

    @Column(name = "deleted", nullable = true)
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
