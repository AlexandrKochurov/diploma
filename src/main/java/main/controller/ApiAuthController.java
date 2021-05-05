package main.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
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
@Api(value = "Api Auth")
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
    @ApiOperation(value = "Получение капчи", response = ResponseEntity.class)
    @ApiResponse(code = 200, message = "Captcha was generated successfully!")
    public ResponseEntity<CaptchaResponse> getCaptcha() throws IOException {
        return ResponseEntity.ok(authService.getCaptcha());
    }

    @PostMapping("/register")
    @ApiOperation(value = "Регистрация пользователя", response = ResponseEntity.class)
    @ApiResponse(code = 200, message = "Пользователь был успешно добавлен!")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(
                authService.registration(registerRequest.getEmail(),
                        registerRequest.getPassword(),
                        registerRequest.getName(),
                        registerRequest.getCaptcha(),
                        registerRequest.getCaptcha_secret()));
    }

    @PostMapping("/login")
    @ApiOperation(value = "Логин пользователя", response = ResponseEntity.class)
    @ApiResponse(code = 200, message = "Пользователь успешно залогинился")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @GetMapping("/logout")
    @ApiOperation(value = "Разлогин пользователя", response = ResponseEntity.class)
    @ApiResponse(code = 200, message = "Пользователь успешно разлогинился")
    public ResponseEntity<LogoutResponse> logout() {
        return ResponseEntity.ok(authService.logout());
    }

    @PostMapping("/restore")
    @ApiOperation(value = "Восстановление пароля", response = ResponseEntity.class)
    @ApiResponse(code = 200, message = "Пароль успешно восстановлен")
    public ResponseEntity<PassRecoverResponse> passwordRestore(@RequestBody PassRecoverRequest passRecoverRequest) {
        return ResponseEntity.ok(authService.passRecover(passRecoverRequest));
    }

    @PostMapping("/password")
    @ApiOperation(value = "Изменение пароля", response = ResponseEntity.class)
    @ApiResponse(code = 200, message = "Пароль успешно изменен")
    public ResponseEntity<PassChangeResponse> passwordChange(@RequestBody PassChangeRequest passChangeRequest) {
        return ResponseEntity.ok(authService.passChange(passChangeRequest));
    }
}
