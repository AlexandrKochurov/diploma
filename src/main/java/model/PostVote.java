package model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "post_votes")
public class PostVote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //ID лайка/дизлайка NOT_NULL AUTO_INCREMENT
    private int id;

    //ID того кто поставил лайк/дизлайк INT NOT_NULL
    @Column(name = "user_id", nullable = false)
    private int userId;

    //Пост которому поставлен лайк/дизлайк INT NOT_NULL
    @Column(name = "post_id", nullable = false)
    private int postId;

    //Дата и время лайка/дизлайка DATETIME NOT_NULL
    @Column(nullable = false)
    private Date time;

    //Лайк или дизлайк 1 или -1 TINYINT NOT_NULL
    @Column(nullable = false)
    private byte value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }
}
