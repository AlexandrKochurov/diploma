package main.controller;

import main.service.impl.GeneralServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @GetMapping("/upload/{firstDir}/{secondDir}/{thirdDir}/{fileName}")
    public @ResponseBody byte[] getImage(@PathVariable("firstDir") String firstDir,
                    @PathVariable("secondDir") String secondDir,
                    @PathVariable("thirdDir") String thirdDir,
                    @PathVariable("fileName") String fileName) {
        String path = "/" + firstDir + "/" + secondDir + "/" + thirdDir + "/" + fileName;
        return generalService.getImage(path);
    }
}
