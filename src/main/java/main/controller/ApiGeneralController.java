package main.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import main.api.response.*;
import main.service.SettingsService;
import main.service.impl.GeneralServiceImpl;
import main.service.impl.SettingsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping(value = "/api/")
@Api(value = "General Controller")
public class ApiGeneralController {

    private final InitResponse initResponse;
    private final SettingsServiceImpl settingsServiceImpl;
    private final CalendarResponse calendarResponse;
    private final GeneralServiceImpl generalServiceImpl;

    @Autowired
    public ApiGeneralController(InitResponse initResponse, SettingsServiceImpl settingsServiceImpl, CalendarResponse calendarResponse, GeneralServiceImpl generalServiceImpl) {
        this.initResponse = initResponse;
        this.settingsServiceImpl = settingsServiceImpl;
        this.calendarResponse = calendarResponse;
        this.generalServiceImpl = generalServiceImpl;
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
    @ApiOperation(value = "Вывод списка тегов", response = ResponseEntity.class)
    @ApiResponse(code = 200, message = "Список тегов выведен")
    private ResponseEntity<TagResponse> tag(
            @RequestParam(name = "query", required = false) String query
    ) {
        return ResponseEntity.ok(generalServiceImpl.tagsList(query));
    }

    @GetMapping("calendar")
    @ApiOperation(value = "Вывод календаря", response = ResponseEntity.class)
    @ApiResponse(code = 200, message = "Календарь выведен")
    private ResponseEntity<CalendarResponse> calendar(
            @RequestParam(name = "year", required = false) int year
    ){
        return ResponseEntity.ok(generalServiceImpl.calendar(year));
    }
}
