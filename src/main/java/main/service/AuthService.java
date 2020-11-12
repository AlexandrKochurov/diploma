package main.service;

import main.model.User;

public interface AuthService {
     void authorization(String email, String password);

     User checkAuth(User user);

     void passRecover(String email);

     void passChange(String code, String captcha);

     void registration(String email, String password, String name, String captcha);

     void getCaptcha();

     void logout(User user);
}
