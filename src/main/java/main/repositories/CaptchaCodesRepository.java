package main.repositories;

import main.model.CaptchaCodes;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CaptchaCodesRepository extends CrudRepository<CaptchaCodes, Integer> {

    @Transactional
    @Modifying
    @Query(value = "insert into captcha_codes(code, secret_code, time) values (:code, :secret_code, now())",
    nativeQuery = true)
    void addNewCaptcha(@Param("code") String code, @Param("secret_code") String secretCode);

    @Transactional
    @Modifying
    @Query(value = "delete from captcha_codes where timestampdiff(hour, captcha_codes.time, now()) >= 1",
    nativeQuery = true)
    void checkOldCaptcha();
}
