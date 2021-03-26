package main.service;

import main.api.request.LoginRequest;
import main.api.request.PassChangeRequest;
import main.api.request.PassRecoverRequest;
import main.api.response.*;
import main.model.User;

import java.io.IOException;
import java.security.Principal;

public interface AuthService {
     void authorization(String email, String password);

     LoginResponse checkAuth(Principal principal);

     PassRecoverResponse passRecover(PassRecoverRequest passRecoverRequest);

     PassChangeResponse passChange(PassChangeRequest passChangeRequest);

     RegisterResponse registration(String email, String password, String name, String code, String secretCode);

     CaptchaResponse getCaptcha() throws IOException;

     LogoutResponse logout();

     LoginResponse login(LoginRequest loginRequest);
}
