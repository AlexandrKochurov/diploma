package main.controller;

import main.api.response.CheckResponse;
import main.api.response.InitResponse;
import main.api.response.SettingsResponse;
import main.api.response.TagResponse;
import main.service.SettingsService;
import main.service.impl.SettingsServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping(value = "/api/")
public class ApiGeneralController {

    private final InitResponse initResponse;
    private final SettingsServiceImpl settingsServiceImpl;
    private final TagResponse tagResponse;

    public ApiGeneralController(InitResponse initResponse, SettingsResponse settingsResponse, SettingsService settingsService, SettingsServiceImpl settingsServiceImpl, TagResponse tagResponse) {
        this.initResponse = initResponse;
        this.settingsServiceImpl = settingsServiceImpl;
        this.tagResponse = tagResponse;
    }

    @GetMapping("init")
    private InitResponse init() {
        return initResponse;
    }

    @GetMapping("settings")
    private SettingsResponse settings() {
        return settingsServiceImpl.getGlobalSettings();
    }

    @GetMapping("tag")
    private TagResponse tag() {
        return tagResponse;
    }
}
