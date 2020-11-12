package main.service.impl;

import main.model.ModerationStatus;
import main.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import main.repositories.PostRepository;
import main.service.PostService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    @Override
    public List<Post> getPosts(int limit) {
        Iterable<Post> postIterable = postRepository.findAll();
        ArrayList<Post> posts = new ArrayList<>();
        for(Post post: postIterable){
            if(limit == 0) break;
            if(post.getIsActive() == 1 && post.getModStatus() == ModerationStatus.ACCEPTED && post.getTime().before(new Date())){
                posts.add(post);
                limit--;
            }
        }
        return posts;
    }

    @Override
    public List<Post> searchPosts(String query, int limit) {
        ArrayList<Post> posts = new ArrayList<>();
        Iterable<Post> postIterable = postRepository.findAll();
        for(Post post: postIterable){
            if(limit == 0) break;
            if(post.getText().contains(query)){
                posts.add(post);
                limit--;
            }
        }
        return posts;
    }

    @Override
    public List<Post> postsByDate(Date date) {
        ArrayList<Post> posts = new ArrayList<>();
        Iterable<Post> postIterable = postRepository.findAll();
        for(Post post: postIterable){
            if(post.getTime().compareTo(date) == 0){
                posts.add(post);
            }
        }
        return posts;
    }

    @Override
    public List<Post> postsForModeration(int limit) {
        ArrayList<Post> posts = new ArrayList<>();
        Iterable<Post> postIterable = postRepository.findAll();
        for(Post post: postIterable){
            if(limit == 0) break;
            if(post.getModStatus() == ModerationStatus.NEW){
                posts.add(post);
                limit--;
            }
        }
        return posts;
    }

    @Override
    public List<Post> myPosts(int limit, int id) {
        ArrayList<Post> posts = new ArrayList<>();
        Iterable<Post> postIterable = postRepository.findAll();
        for(Post post: postIterable){
            if(limit == 0) break;
            if(post.getUserId().getId() == id){
                posts.add(post);
            }
        }
        return posts;
    }

    @Override
    public Post postById(int id) throws Exception {
        return postRepository.findById(id).orElseThrow(() -> new Exception("Post doesn't exist"));
    }

    @Override
    public void addPost(Post post) {
        postRepository.save(post);
    }

    @Override
    public void editPost(int id, Post post) throws Exception {
        post.setModStatus(ModerationStatus.NEW);
        postRepository.findById(id).orElseThrow(() -> new Exception("Post doesn't exist"));
        postRepository.save(post);
    }

    @Override
    public void deletePost(int id) throws Exception {
        postRepository.findById(id).orElseThrow(() -> new Exception("Post doesn't exist"));
        postRepository.deleteById(id);
    }
}
