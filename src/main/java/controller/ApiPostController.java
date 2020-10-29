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
import service.impl.PostServiceImpl;

import java.util.Date;


@RestController
@RequestMapping(value = "/api/post")
@Api(value = "Api Post Controller")
public class ApiPostController {

    private final PostServiceImpl postServiceImpl;

    @Autowired
    public ApiPostController(PostServiceImpl postServiceImpl){
        this.postServiceImpl = postServiceImpl;
    }

    @GetMapping
    @ApiOperation(value = "Вывод списка постов", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Посты успешно выведены"),
            @ApiResponse(code = 500, message = "Посты не найдены")
    })
    public ResponseEntity getPosts(
            @RequestParam(name = "limit", required = false) Integer limit
    ){
        postServiceImpl.getPosts(limit);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @ApiOperation(value = "Вывод списка постов по запросу", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Посты успешно найдены"),
            @ApiResponse(code = 500, message = "Посты не найдены")
    })
    public ResponseEntity searchPosts(
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "limit", required = false) Integer limit
    ){
        postServiceImpl.searchPosts(query, limit);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @ApiOperation(value = "Вывод списка постов за указанную дату", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Посты успешно найдены"),
            @ApiResponse(code = 500, message = "Посты не найдены")
    })
    public ResponseEntity postsByDate(@RequestBody Date date){
        postServiceImpl.postsByDate(date);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @ApiOperation(value = "Список постов на модерацию", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Посты успешно найдены"),
            @ApiResponse(code = 500, message = "Посты не найдены")
    })
    public ResponseEntity postsForModeration(
            @RequestParam(name = "limit", required = false) Integer limit
    ){
        postServiceImpl.postsForModeration(limit);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @ApiOperation(value = "Список моих постов", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Посты успешно найдены"),
            @ApiResponse(code = 500, message = "Посты не найдены")
    })
    public ResponseEntity myPosts(
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestParam(name = "id", required = false) Integer id
    ){
        postServiceImpl.myPosts(limit, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @ApiOperation(value = "Вывод поста по id", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Пост успешно найден"),
            @ApiResponse(code = 500, message = "Пост не найден")
    })
    public ResponseEntity postById(
            @RequestParam(name = "id", required = false) Integer id
    ) throws Exception {
        postServiceImpl.postById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    @ApiOperation(value = "Добавление поста", response = ResponseEntity.class)
    @ApiResponses(value =  {
            @ApiResponse(code = 200, message = "Пост успешно добавлен"),
            @ApiResponse(code = 500, message = "Пост не найден")
    })
    public ResponseEntity addPost(@RequestBody PostDTO postDTO){
        Post post = ConverterPostDTO.convertDtoToModel(postDTO);
        postServiceImpl.addPost(post);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    @ApiOperation(value = "Изменение поста", response = ResponseEntity.class)
    @ApiResponses(value =  {
            @ApiResponse(code = 200, message = "Пост успешно изменен"),
            @ApiResponse(code = 500, message = "Пост не найден")
    })
    public ResponseEntity editPost(
            @RequestBody PostDTO postDTO,
            @RequestParam(name = "id", required = false) Integer id
    ) throws Exception {
        Post post = ConverterPostDTO.convertDtoToModel(postDTO);
        postServiceImpl.editPost(id, post);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    @ApiOperation(value = "Удаление поста", response = ResponseEntity.class)
    @ApiResponses(value =  {
            @ApiResponse(code = 200, message = "Пост успешно удален"),
            @ApiResponse(code = 500, message = "Пост не найден")
    })
    public ResponseEntity deletePost(
            @RequestParam(name = "id", required = false) Integer id
    ) throws Exception {
        postServiceImpl.deletePost(id);
        return ResponseEntity.ok().build();
    }
}
