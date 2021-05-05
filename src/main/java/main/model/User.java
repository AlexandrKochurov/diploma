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
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //ID пользователя NOT_NULL AUTO_INCREMENT
    private int id;

    //isModerator? TINYINT NOT_NULL
    @Column(name = "is_moderator", nullable = false)
    private byte isModerator;

    //Дата и время регистрации пользователя DATETIME NOT_NULL
    @Column(name = "reg_time", nullable = false)
    private Instant regTime;

    //Имя пользователя VARCHAR NOT_NULL
    @Column(nullable = false)
    private String name;

    //Email пользователя VARCHAR NOT_NULL
    @Column(nullable = false)
    private String email;

    //Пароль пользователя, VARCHAR NOT_NULL
    @Column(nullable = false)
    private String password;

    //Код для восстановления пароля VARCHAR
    @Column
    private String code;

    //Фотография(ссылка на файл) TEXT
    @Column
    private String photo;

    @OneToMany(mappedBy = "userId", fetch = FetchType.LAZY)
    private List<Post> posts;

    public Role getRole(){
        return isModerator == 1 ? Role.MODERATOR : Role.USER;
    }
}
