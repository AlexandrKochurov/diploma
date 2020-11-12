package main.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "captcha_codes")
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
}
