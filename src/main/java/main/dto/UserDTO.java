package main.dto;


import lombok.Data;


@Data
public class UserDTO {
    private int id;

    private String name;

    private String photo;

    public UserDTO(int id, String name){
        this.id = id;
        this.name = name;
    }

    public UserDTO(int id, String name, String photo){
        this.id = id;
        this.name = name;
        this.photo = photo;
    }
}
