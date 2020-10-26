package service;

import model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.PostRepository;


@Service
public class PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    public void addPost(Post post){
        postRepository.save(post);
    }

    public Iterable<Post> getPosts(){
        return postRepository.findAll();
    }

    public Post getPost(Integer id) throws Exception {
        return postRepository.findById(id).orElseThrow(() -> new Exception("Post doesn't exist"));
    }

    public void updatePost(Post post) throws Exception{
        postRepository.findById(post.getId()).orElseThrow(() -> new Exception("Post doesn't exist"));
        postRepository.save(post);
    }

    public void removePost(Post post) throws Exception{
        postRepository.findById(post.getId()).orElseThrow(() -> new Exception("Post doesn't exist"));
        postRepository.delete(post);
    }


}
