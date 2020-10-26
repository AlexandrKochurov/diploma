package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "post_comments")
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
}
