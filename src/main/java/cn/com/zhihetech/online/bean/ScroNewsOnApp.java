package cn.com.zhihetech.online.bean;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by YangDaiChun on 2016/8/19.
 */
@Entity
@Table(name = "t_scro_news_app")
public class ScroNewsOnApp {

    private String newsId;
    private String newsContent;
    private JumpType jumpType;
    private String jumpTarget;

    public static enum JumpType {
        goods, merchant, url;
    }

    @javax.persistence.Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "news_id", length = 36, nullable = false, unique = true)
    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    @Column(name = "news_content", nullable = false, length = 100)
    public String getNewsContent() {
        return newsContent;
    }

    public void setNewsContent(String newsContent) {
        this.newsContent = newsContent;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "jump_type", nullable = false)
    public JumpType getJumpType() {
        return jumpType;
    }

    public void setJumpType(JumpType jumpType) {
        this.jumpType = jumpType;
    }

    @Column(name = "jump_target", nullable = false)
    public String getJumpTarget() {
        return jumpTarget;
    }

    public void setJumpTarget(String jumpTarget) {
        this.jumpTarget = jumpTarget;
    }
}
