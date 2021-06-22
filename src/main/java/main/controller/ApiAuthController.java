package main.controller;

import main.api.request.LoginRequest;
import main.api.request.PassChangeRequest;
import main.api.request.PassRecoverRequest;
import main.api.request.RegisterRequest;
import main.api.response.*;
import main.service.impl.AuthServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping(value = "/api/auth")
public class ApiAuthController {
    private final AuthServiceImpl authService;

    public ApiAuthController(AuthServiceImpl authService) {
        this.authService = authService;
    }

    @GetMapping("/check")
    public ResponseEntity<LoginResponse> check(Principal principal) {
        return ResponseEntity.ok(authService.checkAuth(principal));
    }

    @GetMapping("/captcha")
    public ResponseEntity<CaptchaResponse> getCaptcha() throws IOException {
        return ResponseEntity.ok(authService.getCaptcha());
    }

    @PostMapping("/register")
    public ResponseEntity<ResultResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(
                authService.registration(registerRequest.getEmail(),
                        registerRequest.getPassword(),
                        registerRequest.getName(),
                        registerRequest.getCaptcha(),
                        registerRequest.getCaptcha_secret()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @GetMapping("/logout")
    public ResponseEntity<ResultResponse> logout() {
        return ResponseEntity.ok(authService.logout());
    }

    @PostMapping("/restore")
    public ResponseEntity<ResultResponse> passwordRestore(@RequestBody PassRecoverRequest passRecoverRequest) {
        return ResponseEntity.ok(authService.passRecover(passRecoverRequest));
    }

    @PostMapping("/password")
    public ResponseEntity<ResultResponse> passwordChange(@RequestBody PassChangeRequest passChangeRequest) {
        return ResponseEntity.ok(authService.passChange(passChangeRequest));
    }
}
