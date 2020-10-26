package controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class DefaultController {
    private HashMap<String, String> initMap;

    @Value("MyBlog")
    private String title;

    @Value("About Java")
    private String subtitle;

    @Value("89920135900")
    private String phone;

    @Value("kochurov92@mail.ru")
    private String eMail;

    @Value("Кочуров Александр")
    private String copyright;

    @Value("2020")
    private String copyrightFrom;

    @GetMapping(value = "/api/init/")
    public HashMap<String, String> init(){
        initMap = new HashMap<>();
        initMap.put("title", title);
        initMap.put("subtitle", subtitle);
        initMap.put("phone", phone);
        initMap.put("email", eMail);
        initMap.put("copyright", copyright);
        initMap.put("copyrightFrom", copyrightFrom);
        return initMap;
    }
}
