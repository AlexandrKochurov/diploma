package controller;

import model.Post;
import model.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ApiPostController {
    @Autowired
    private PostRepository postRepository;

    @RequestMapping(value = "/api/post/", method = RequestMethod.GET)
    public List<Post> postList(){
        Iterable<Post> postIterable = postRepository.findAll();
        ArrayList<Post> posts = new ArrayList<>();
        for(Post post: postIterable){
            posts.add(post);
        }
        return posts;
    }
}
