package main.service.impl;

import main.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import main.repositories.CaptchaCodesRepository;
import main.repositories.UserRepository;
import main.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {
    private final CaptchaCodesRepository captchaCodesRepository;
    private final UserRepository userRepository;

    @Autowired
    public AuthServiceImpl(CaptchaCodesRepository captchaCodesRepository, UserRepository userRepository){
        this.captchaCodesRepository = captchaCodesRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void authorization(String email, String password) {

    }

    @Override
    public User checkAuth(User user) {
        return null;
    }

    @Override
    public void passRecover(String email) {

    }

    @Override
    public void passChange(String code, String captcha) {

    }

    @Override
    public void registration(String email, String password, String name, String captcha) {

    }

    @Override
    public void getCaptcha() {

    }

    @Override
    public void logout(User user) {

    }
}
