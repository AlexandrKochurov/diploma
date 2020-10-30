package service;

import model.User;

public interface AuthService {
    public void authorization(String email, String password);

    public User checkAuth(User user);

    public void passRecover(String email);

    public void passChange(String code, String captcha);

    public void registration(String email, String password, String name, String captcha);

    public void getCaptcha();

    public void logout(User user);
}
