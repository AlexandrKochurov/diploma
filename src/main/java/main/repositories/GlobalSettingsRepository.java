package main.repositories;

import main.model.GlobalSettings;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalSettingsRepository extends CrudRepository<GlobalSettings, Integer> {
    @Query(value = "select value from global_settings where code like :code",
    nativeQuery = true)
    String getSettingsValueByCode(@Param("code") String code);
}
