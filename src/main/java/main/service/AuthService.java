package main.service;

import main.api.request.LoginRequest;
import main.api.request.PassChangeRequest;
import main.api.request.PassRecoverRequest;
import main.api.response.*;

import java.io.IOException;
import java.security.Principal;

public interface AuthService {
     void authorization(String email, String password);

     LoginResponse checkAuth(Principal principal);

     ResultResponse passRecover(PassRecoverRequest passRecoverRequest);

     ResultResponse passChange(PassChangeRequest passChangeRequest);

     ResultResponse registration(String email, String password, String name, String code, String secretCode);

     CaptchaResponse getCaptcha() throws IOException;

     ResultResponse logout();

     LoginResponse login(LoginRequest loginRequest);
}
