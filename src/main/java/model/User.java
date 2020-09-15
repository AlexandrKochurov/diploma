package model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users")
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
    private String code;

    //Фотография(ссылка на файл) TEXT
    private String photo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte getIsModerator() {
        return isModerator;
    }

    public void setIsModerator(byte isModerator) {
        this.isModerator = isModerator;
    }

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
