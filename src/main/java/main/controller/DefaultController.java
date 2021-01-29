package main.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {
    @GetMapping("/")
//    @PreAuthorize("hasAuthority('user:write')")
    public String index() {
        return "index";
    }

    @GetMapping(value = "/**/{path:[^\\\\.]*}")
//    @PreAuthorize("hasAuthority('user:write')")
    public String redirectToIndex() {
        return "forward:/"; //делаем перенаправление
    }
}
