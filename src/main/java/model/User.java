package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //ID пользователя NOT_NULL AUTO_INCREMENT
    private int id;

    //isModerator? TINYINT NOT_NULL
    @Column(name = "is_moderator", nullable = false)
    private byte isModerator;

    //Дата и время регистрации пользователя DATETIME NOT_NULL
    @Column(name = "reg_time", nullable = false)
    private Date regTime;

    //Имя пользователя VARCHAR NOT_NULL
    @Column(nullable = false)
    private String name;

    //Email пользователя VARCHAR NOT_NULL
    @Column(name = "email", nullable = false)
    private String eMail;

    //Пароль пользователя, VARCHAR NOT_NULL
    @Column(nullable = false)
    private String password;

    //Код для восстановления пароля VARCHAR
    @Column
    private String code;

    //Фотография(ссылка на файл) TEXT
    @Column
    private String photo;
}
