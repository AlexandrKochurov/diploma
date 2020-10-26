package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "tags")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //ID тэга NOT_NULL AUTO_INCREMENT
    private int id;

    //Имя тега VARCHAR NOT_NULL
    @Column(nullable = false)
    private String name;
}
