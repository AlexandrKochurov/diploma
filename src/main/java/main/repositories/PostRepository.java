package main.repositories;

import main.dto.CalendarInterfaceProjection;
import main.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends CrudRepository<Post, Integer> {

    @Query(value = "select count(*) from posts where posts.is_active=1 and posts.moderator_status='ACCEPTED' and posts.instant<=now()",
            nativeQuery = true)
    int countAllPosts();

    @Query(value = "select count(*) from posts " +
            "where posts.is_active=1 and posts.moderator_status='ACCEPTED' and posts.instant<=now() " +
            "and posts.title like :query " +
            "order by posts.instant desc",
            nativeQuery = true)
    int countSearchPosts(@Param("query") String query);

    @Query(value = "select count(*) from posts " +
            "where posts.is_active=1 and posts.moderator_status='ACCEPTED' " +
            "and posts.instant =:date",
            nativeQuery = true)
    int countPostsByDate(@Param("date") String date);

    @Query(value = "select count(*) from posts left join tag2post on posts.id=tag2post.post_id " +
            "left join tags on tag2post.tag_id=tags.id " +
            "where posts.is_active=1 and posts.moderator_status='ACCEPTED' and posts.instant<=now() " +
            "and tags.name like :tag",
            nativeQuery = true)
    int countPostsByTag(@Param("tag") String tag);

    @Query(value = "select count(*) from posts " +
            "where posts.is_active=1 " +
            "and posts.moderator_status like :status " +
            "order by posts.instant desc",
            nativeQuery = true)
    int countPostsForModeration(@Param("status")String status);

    @Query(value = "select count(*) from posts where posts.user_id=:user_id",
            nativeQuery = true)
    int countMyPosts(@Param("user_id") int userId);

    @Query(value = "select count(*) from posts " +
            "where posts.user_id=:user_id and posts.moderator_status like :status and posts.is_active=:active",
            nativeQuery = true)
    int countMyPostsByStatus(@Param("user_id") int userId, @Param("status") String status, @Param("active") int active);

    @Query(value = "select * from posts " +
            "where posts.is_active=1 and posts.moderator_status='ACCEPTED' and posts.instant<=now() " +
            "order by posts.instant desc",
            nativeQuery = true)
    List<Post> recentPosts(Pageable pageable);

    @Query(value = "select * from posts " +
            "where posts.is_active=1 and posts.moderator_status='ACCEPTED' and posts.instant<=now() " +
            "order by posts.instant asc",
            nativeQuery = true)
    List<Post> earlyPosts(Pageable pageable);

    @Query(value = "select * from posts left join post_comments on posts.id=post_comments.post_id " +
            "where posts.is_active=1 and posts.moderator_status='ACCEPTED' and posts.instant<=now() " +
            "group by posts.id " +
            "order by count(post_comments.post_id) desc",
            nativeQuery = true)
    List<Post> popularPosts(Pageable pageable);

    @Query(value = "select * from posts left join post_votes on posts.id=post_votes.post_id " +
            "where posts.is_active=1 and posts.moderator_status='ACCEPTED' and posts.instant<=now() " +
            "group by posts.id " +
            "order by count(post_votes.post_id) desc",
            nativeQuery = true)
    List<Post> bestPosts(Pageable pageable);

    @Query(value = "select * from posts " +
            "where posts.is_active=1 and posts.moderator_status='ACCEPTED' and posts.instant<=now() " +
            "and (posts.title like %:query% or posts.text like %:query%) " +
            "order by posts.instant desc",
            nativeQuery = true)
    List<Post> searchPosts(Pageable pageable, @Param("query") String query);

    @Query(value = "select * from posts " +
            "where posts.is_active=1 and posts.moderator_status='ACCEPTED' " +
            "and date_format(posts.instant, '%Y-%m-%d') like :date",
            nativeQuery = true)
    List<Post> postsByDate(Pageable pageable, @Param("date") String date);

    @Query(value = "select * from posts left join tag2post on posts.id=tag2post.post_id " +
            "left join tags on tag2post.tag_id=tags.id " +
            "where posts.is_active=1 and posts.moderator_status='ACCEPTED' and posts.instant<=now() " +
            "and tags.name like :tag",
            nativeQuery = true)
    List<Post> postsByTag(Pageable pageable, @Param("tag") String tag);

    @Query(value = "select * from posts " +
            "where posts.is_active=1 " +
            "and posts.moderator_status like :status " +
            "order by posts.instant desc",
            nativeQuery = true)
    List<Post> postsForModeration(Pageable pageable, @Param("status")String status);

    @Query(value = "select * from posts " +
            "where posts.user_id=:user_id and posts.moderator_status like :status and posts.is_active=:active",
            nativeQuery = true)
    List<Post> myPosts(Pageable pageable, @Param("user_id") int userId, @Param("status") String status, @Param("active") int active);

    @Query(value = "select * from posts where id = :post_id and posts.is_active=1 and posts.instant<=now() and posts.moderator_status='ACCEPTED'",
            nativeQuery = true)
    Optional<Post> postById(@Param("post_id") int postId);

    @Query(value = "select * from posts where id = :post_id and posts.is_active=1",
            nativeQuery = true)
    Optional<Post> postByIdForModeration(@Param("post_id") int postId);

    @Query(value = "select year(posts.instant) as year, date_format(posts.instant, '%Y-%m-%d') as date, count(*) as amount from posts " +
            "where year(posts.instant)= :year and posts.is_active=1 and posts.moderator_status='ACCEPTED' and posts.instant<=now() " +
            "group by year, date order by date",
            nativeQuery = true)
    List<CalendarInterfaceProjection> allByDate(@Param("year") int year);

    @Query(value = "select year(posts.instant) as year from posts " +
            "where posts.is_active=1 and posts.moderator_status='ACCEPTED' and posts.instant<=now() group by year",
    nativeQuery = true)
    List<Integer> allByYears();

    @Query(value = "select sum(view_count) from posts",
    nativeQuery = true)
    int viewCountSum();

    @Query(value = "select sum(view_count) from posts where user_id = :id",
            nativeQuery = true)
    Integer viewCountSumById(int id);

    @Query(value = "select MIN(instant) from posts",
    nativeQuery = true)
    Instant getFirstPublication();

    @Query(value = "select MIN(instant) from posts where user_id = :id ",
            nativeQuery = true)
    Instant getFirstPublicationById(int id);
}
