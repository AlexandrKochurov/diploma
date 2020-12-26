package main.dto;

import lombok.Data;
import main.model.Post;
import main.model.PostComments;
import main.model.Tag;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class PostsDTO {
    private static final int ANNOUNCE_LENGTH = 23;

    private int id;

    private long timestamp;

    private String title;

    private String announce;

    private int likeCount;

    private int dislikeCount;

    private int commentCount;

    private int viewCount;

    private UserDTO user;

    private boolean active;

    private String text;

    private List<PostComments> comments;

    private List<String> tags;

    //Constructor for solo id post
    public PostsDTO(int id, long timestamp, String title, int likeCount, int dislikeCount, int viewCount, UserDTO user, boolean active, String text, List<PostComments> comments, List<String> tags) {
        this.id = id;
        this.timestamp = timestamp;
        this.title = title;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.viewCount = viewCount;
        this.user = user;
        this.active = active;
        this.text = text;
        this.comments = comments;
        this.tags = tags;
    }

    //Constructor for list of posts
    public PostsDTO(int id, long timestamp, String title, String announce, int likeCount, int dislikeCount, int commentCount, int viewCount, UserDTO user) {
        this.id = id;
        this.timestamp = timestamp;
        this.title = title;
        this.announce = announce;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.commentCount = commentCount;
        this.viewCount = viewCount;
        this.user = user;
    }

    public static PostsDTO postById2PostDTO(Post post){
        List<String> tags = post.getTagList().stream().filter(tag -> tag.getId() == post.getId()).map(Tag::getName).collect(Collectors.toList());
        return new PostsDTO(
                post.getId(),
                post.getInstant().getEpochSecond(),
                post.getTitle(),
                (int) post.getPostVoteList().stream().filter(postVote -> postVote.getValue() == 1).count(),
                (int) post.getPostVoteList().stream().filter(postVote -> postVote.getValue() == -1).count(),
                post.getViewCount(),
                new UserDTO(post.getUserId().getId(), post.getUserId().getName()),
                Boolean.parseBoolean(String.valueOf(post.getIsActive())),
                post.getText(),
                post.getPostCommentsList(),
                tags
        );
    }
}
