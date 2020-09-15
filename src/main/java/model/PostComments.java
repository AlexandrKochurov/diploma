package model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "post_comments")
public class PostComments {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //ID комментария NOT_NULL AUTO_INCREMENT
    private int id;

    //ID комментария на который оставлен комментарий, может быть NULL(если коммент к посту) INT
    @Column(name = "parent_id")
    private int parentId;

    //ID поста к которому написан комментарий INT NOT_NULL
    @Column(name = "post_id", nullable = false)
    private int postId;

    //ID автора комментария INT NOT_NULL
    @Column(name = "user_id", nullable = false)
    private int userId;

    //Дата и время комментария DATETIME NOT_NULL
    @Column(nullable = false)
    private Date time;

    //Текст комментария TEXT NOT_NULL
    @Column(nullable = false)
    private String text;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
