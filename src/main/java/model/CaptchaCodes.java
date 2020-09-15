package model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "captcha_codes")
public class CaptchaCodes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //ID капчи NOT_NULL AUTO_INCREMENT
    private int id;

    //Дата и время генерации кода капчи DATETIME NOT_NULL
    @Column(nullable = false)
    private Date time;

    //Код, отображаемый на картинке капчи TINYTEXT NOT_NULL
    @Column(nullable = false)
    private String code;

    //Код, передаваемый в параметре TINYTEXT NOT_NULL
    @Column(name = "secret_code", nullable = false)
    private String secretCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSecretCode() {
        return secretCode;
    }

    public void setSecretCode(String secretCode) {
        this.secretCode = secretCode;
    }
}
