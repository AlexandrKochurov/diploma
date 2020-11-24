package main.controller;

import io.swagger.annotations.Api;
import main.api.response.CheckResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/auth/")
@Api(value = "Api Auth")
public class ApiAuthController {
    private final CheckResponse checkResponse;

    public ApiAuthController(CheckResponse checkResponse) {
        this.checkResponse = checkResponse;
    }

    @GetMapping("check")
    private CheckResponse checkResponse(){
        return checkResponse;
    }
}
