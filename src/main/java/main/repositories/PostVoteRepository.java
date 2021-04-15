package main.repositories;

import main.model.PostVote;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PostVoteRepository extends CrudRepository<PostVote, Integer> {
    @Query(value = "select count(*) from post_votes where value = 1",
    nativeQuery = true)
    int countAllLikes();

    @Query(value = "select count(*) from post_votes where value = -1",
            nativeQuery = true)
    int countAllDislikes();

    @Query(value = "select count(*) from post_votes where value = 1 and user_id = :id",
    nativeQuery = true)
    int countLikesById(int id);

    @Query(value = "select count(*) from post_votes where value = 1 and user_id = :id",
    nativeQuery = true)
    int countDislikesById(int id);
}