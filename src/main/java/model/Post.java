package model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //ID поста NOT_NULL AUTO_INCREMENT
    private int id;

    //Скрыта или активна публикация: 0 или 1 TINYINT NOT_NULL
    @Column(name = "is_active", nullable = false)
    private byte isActive;

    //Статус модерации, по умолчанию значение NEW
    @Enumerated(EnumType.STRING)
    @Column(name = "moderator_status")
    private ModerationStatus modStatus = ModerationStatus.NEW;

    //ID пользователя-модератора принявшего решение или NULL INT
    @Column(name = "moderator_id")
    private int moderatorId;

    //Автор поста, INT NOT_NULL
    @Column(name = "user_id", nullable = false)
    private int userId;

    //Дата и время публикации поста DATETIME NOT_NULL
    @Column(nullable = false)
    private Date time;

    //Заголовок поста VARCHAR NOT_NULL
    @Column(nullable = false)
    private String title;

    //Текст поста TEXT NOT_NULL
    @Column(nullable = false)
    private String text;

    //Кол-во просмотров поста INT NOT_NULL
    @Column(name = "view_count", nullable = false)
    private int viewCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte getIsActive() {
        return isActive;
    }

    public void setIsActive(byte isActive) {
        this.isActive = isActive;
    }

    public int getModeratorId() {
        return moderatorId;
    }

    public void setModeratorId(int moderatorId) {
        this.moderatorId = moderatorId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }
}
