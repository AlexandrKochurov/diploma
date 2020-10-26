package controller;

import dto.PostDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import model.Post;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import service.ConverterPostDTO;
import service.PostService;


@RestController
@RequestMapping(value = "/api/post")
@Api(value = "Api Post Controller")
public class ApiPostController {

    private final PostService postService;

    @Autowired
    public ApiPostController(PostService postService){
        this.postService = postService;
    }

    @PostMapping
    @ApiOperation(value = "Создание поста", response = ResponseEntity.class)
    @ApiResponses(value =  {
        @ApiResponse(code = 200, message = "Пост успешно добавлен"),
    })
    public ResponseEntity addPost(@RequestBody PostDTO postDTO){
        Post post = ConverterPostDTO.convertDtoToModel(postDTO);
        postService.addPost(post);
        return ResponseEntity.ok().build();
    }
}
