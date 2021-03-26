package main.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Data
@Table(name = "posts")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @OneToMany(mappedBy = "postId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostComments> postCommentsList;

    @OneToMany(mappedBy = "parentId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostComments> parentComments;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "tag2post",
                joinColumns = {@JoinColumn(name = "post_id")},
                inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private List<Tag> tagList;

    @OneToMany(mappedBy = "postId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostVote> postVoteList;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @ManyToOne
    @JoinColumn(name = "moderator_id")
    private User moderatorId;

    //Автор поста, INT NOT_NULL
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;

    //Дата и время публикации поста DATETIME NOT_NULL
    @Column(nullable = false)
    private Instant instant;

    //Заголовок поста VARCHAR NOT_NULL
    @Column(nullable = false)
    private String title;

    //Текст поста TEXT NOT_NULL
    @Column(nullable = false)
    private String text;

    //Кол-во просмотров поста INT NOT_NULL
    @Column(name = "view_count", nullable = false)
    private int viewCount;
}
