package service;

import dto.PostDTO;
import model.Post;

public class ConverterPostDTO {
    public static Post convertDtoToModel(PostDTO postDTO){
        return Post.builder().
                id(postDTO.getId())
                .isActive(postDTO.getIsActive())
                .modStatus(postDTO.getModStatus())
                .moderatorId(postDTO.getModeratorId())
                .userId(postDTO.getUserId())
                .time(postDTO.getTime())
                .title(postDTO.getTitle())
                .text(postDTO.getText())
                .viewCount(postDTO.getViewCount())
                .build();
    }
}
