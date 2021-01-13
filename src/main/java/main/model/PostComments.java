package main.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

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
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private PostComments parentId;

    //ID поста к которому написан комментарий INT NOT_NULL
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post postId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;

    //Дата и время комментария DATETIME NOT_NULL
    @Column(nullable = false)
    private Instant time;

    //Текст комментария TEXT NOT_NULL
    @Column(nullable = false)
    private String text;
}
