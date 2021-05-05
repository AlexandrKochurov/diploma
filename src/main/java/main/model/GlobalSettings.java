package main.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "global_settings")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GlobalSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //ID настройки NOT_NULL AUTO_INCREMENT
    private int id;

    //Системное имя настройки VARCHAR NOT_NULL
    @Column(nullable = false)
    private String code;

    //Название настройки VARCHAR NOT_NULL
    @Column(nullable = false)
    private String name;

    //Значение настройки VARCHAR NOT_NULL
    @Column(nullable = false)
    private String value;
}
