package main.repositories;

import main.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    @Transactional
    @Modifying
    @Query(value = "insert into users(email, is_moderator, name, password, reg_time) values (:email, 0, :name, :password, now())",
            nativeQuery = true)
    void addNewUser(@Param("email") String email, @Param("name") String name, @Param("password") String password);


    Optional<User> findByEmail(String email);

    @Transactional
    @Modifying
    @Query(value = "update users set code = :code where email = :email", nativeQuery = true)
    void addCodeToUser(@Param("code") String code, @Param("email") String email);

    @Transactional
    @Modifying
    @Query(value = "update users set password = :password where code = :code", nativeQuery = true)
    void changePass(@Param("password") String password, @Param("code") String code);
}
