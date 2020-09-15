package model;

import javax.persistence.*;

@Entity
@Table(name = "global_settings")
public class GlobalSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
