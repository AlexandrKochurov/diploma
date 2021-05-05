package main.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "post_votes")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
