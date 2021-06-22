package main.controller;

import main.service.impl.GeneralServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {
    private final GeneralServiceImpl generalService;

    public DefaultController(GeneralServiceImpl generalService) {
        this.generalService = generalService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping(value = "/**/{path:[^\\\\.]*}")
    public String redirectToIndex() {
        return "forward:/"; //делаем перенаправление
    }
}
