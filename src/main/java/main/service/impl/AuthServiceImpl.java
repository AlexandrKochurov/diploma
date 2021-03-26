package main.service.impl;

import com.github.cage.Cage;
import com.github.cage.GCage;
import main.api.request.LoginRequest;
import main.api.request.PassChangeRequest;
import main.api.request.PassRecoverRequest;
import main.api.response.*;
import main.model.User;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import main.repositories.CaptchaCodesRepository;
import main.repositories.UserRepository;
import main.service.AuthService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Service
public class AuthServiceImpl implements AuthService {
    private final CaptchaCodesRepository captchaCodesRepository;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final int PICTURE_WIDTH = 100;
    private final int PICTURE_HEIGHT = 35;
    private static String secret = secretCodeGenerator();

    @Autowired
    public AuthServiceImpl(CaptchaCodesRepository captchaCodesRepository, UserRepository userRepository, AuthenticationManager authenticationManager) {
        this.captchaCodesRepository = captchaCodesRepository;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void authorization(String email, String password) {

    }

    @Override
    public LoginResponse checkAuth(Principal principal) {
        if (principal == null){
            return new LoginResponse(false);
        }
        return getLoginResponse(principal.getName());
    }

    @Override
    public PassRecoverResponse passRecover(PassRecoverRequest passRecoverRequest) {
        if(userRepository.findByEmail(passRecoverRequest.getEmail()).isPresent()){
            BCryptPasswordEncoder passenc = new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2Y);
            String hash = passenc.encode(secretCodeGenerator());
            userRepository.addCodeToUser(hash, passRecoverRequest.getEmail());
            return new PassRecoverResponse(true);
        }
        return new PassRecoverResponse(false);
    }

    @Override
    public PassChangeResponse passChange(PassChangeRequest passChangeRequest) {
        Map<String, String> errors = new HashMap<>();
        return new PassChangeResponse();
    }

    @Override
    public RegisterResponse registration(String email, String password, String name, String code, String secretCode) {
        Map<String, String> errors = new HashMap<>();
        if (!secret.equals(secretCode)) {
            errors.put("captcha", "Код с картинки введен не верно");
        }
        if (!email.matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")) {
            errors.put("email", "Введен неверный email");
        }
        if (!name.matches("\\D+")) {
            errors.put("name", "Имя указано неверно");
        }
        if ((password.length() < 6)) {
            errors.put("password", "Пароль должен быть не менее 6 символов");
        }
        if (!errors.isEmpty()) {
            return new RegisterResponse(false, errors);
        }
        BCryptPasswordEncoder passenc = new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2Y);
        userRepository.addNewUser(code, email, name, passenc.encode(password));
        return new RegisterResponse(true);
    }

    @Override
    public CaptchaResponse getCaptcha() throws IOException {
        Cage cage = new GCage();
        String code = cage.getTokenGenerator().next();
        BufferedImage buimg = Scalr.resize(cage.drawImage(code), Scalr.Method.ULTRA_QUALITY, PICTURE_WIDTH, PICTURE_HEIGHT);
        String image = "data:image/png;base64, " + Base64.getEncoder().encodeToString(bufImgToBytes(buimg));
        captchaCodesRepository.addNewCaptcha(code, secret);
        captchaCodesRepository.checkOldCaptcha();
        return new CaptchaResponse(secret, image);
    }

    @Override
    public LogoutResponse logout() {
        SecurityContextHolder.clearContext();
        return new LogoutResponse(true);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        return getLoginResponse(user.getUsername());
    }

    private static String secretCodeGenerator() {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        String alphabet = "abcdefghijklmnopqrstuvwxyz1234567890";
        for (int i = 0; i < 16; i++) {
            stringBuilder.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }
        return stringBuilder.toString();
    }

    private byte[] bufImgToBytes(BufferedImage source) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(source, "png", baos);
        return baos.toByteArray();
    }

    private LoginResponse getLoginResponse(String email) {
        main.model.User currentUser = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));

        UserLoginResponse userLoginResponse = new UserLoginResponse();
        userLoginResponse.setId(currentUser.getId());
        userLoginResponse.setName(currentUser.getName());
        userLoginResponse.setPhoto(currentUser.getPhoto());
        userLoginResponse.setEmail(currentUser.getEmail());
        userLoginResponse.setModeration(currentUser.getIsModerator() == 1);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setResult(true);
        loginResponse.setUserLoginResponse(userLoginResponse);

        return loginResponse;
    }
}
