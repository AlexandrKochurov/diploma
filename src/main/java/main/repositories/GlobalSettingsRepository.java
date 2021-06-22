package main.repositories;

import main.model.GlobalSettings;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface GlobalSettingsRepository extends CrudRepository<GlobalSettings, Integer> {
    @Query(value = "select value from global_settings where code like :code",
    nativeQuery = true)
    boolean getSettingsValueByCode(@Param("code") String code);

    @Transactional
    @Modifying
    @Query(value = "update global_settings set value = :value where code = :code",
    nativeQuery = true)
    void setGlobalSettings(@Param("value") boolean value, @Param("code") String code);
}
