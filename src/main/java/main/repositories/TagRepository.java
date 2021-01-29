package main.repositories;

import main.dto.TagInterfaceProjection;
import main.model.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends CrudRepository<Tag, Integer> {
    @Query(value = "select * from tags",
    nativeQuery = true)
    List<Tag> allTags();

    @Query(value = "select tags.name as name, " +
            "(select count(*) from tag2post where tag2post.tag_id = tags.id) / (select count(*) from posts where posts.is_active = 1 and posts.moderator_status = 'ACCEPTED' and posts.instant < now()) as weight " +
            "from tags " +
            "where (:query is null or tags.name like :query) group by tags.id",
            nativeQuery = true)
    List<TagInterfaceProjection> tagsByWeight(@Param("query") String query);
}
