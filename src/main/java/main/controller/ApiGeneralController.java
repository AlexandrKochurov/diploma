package main.controller;

import main.api.response.InitResponse;
import main.api.response.SettingsResponse;
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

    public ApiGeneralController(InitResponse initResponse, SettingsResponse settingsResponse, SettingsService settingsService, SettingsServiceImpl settingsServiceImpl) {
        this.initResponse = initResponse;
        this.settingsServiceImpl = settingsServiceImpl;
    }

    @GetMapping("init")
    private InitResponse init(){
        return initResponse;
    }

    @GetMapping("settings")
    private SettingsResponse settings(){
        return settingsServiceImpl.getGlobalSettings();
    }
}
