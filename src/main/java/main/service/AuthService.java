package main.service;

import main.api.request.LoginRequest;
import main.api.response.CaptchaResponse;
import main.api.response.LoginResponse;
import main.api.response.LogoutResponse;
import main.api.response.RegisterResponse;
import main.model.User;

import java.io.IOException;
import java.security.Principal;

public interface AuthService {
     void authorization(String email, String password);

     LoginResponse checkAuth(Principal principal);

     void passRecover(String email);

     void passChange(String code, String captcha);

     RegisterResponse registration(String email, String password, String name, String code, String secretCode);

     CaptchaResponse getCaptcha() throws IOException;

     LogoutResponse logout();

     LoginResponse login(LoginRequest loginRequest);
}
