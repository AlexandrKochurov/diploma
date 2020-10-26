package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "tag2post")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagToPost {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //ID связи NOT_NULL AUTO_INCREMENT
    private int id;

    //ID поста NOT_NULL INT
    @Column(name = "post_id")
    private int postId;

    //ID тэга NOT_NULL INT
    @Column(name = "tag_id")
    private int tagId;
}
