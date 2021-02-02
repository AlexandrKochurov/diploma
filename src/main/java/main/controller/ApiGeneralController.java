package main.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import main.api.request.CommentRequest;
import main.api.response.*;
import main.model.User;
import main.service.impl.GeneralServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api")
@Api(value = "General Controller")
public class ApiGeneralController {

    private final InitResponse initResponse;
    private final GeneralServiceImpl generalServiceImpl;

    @Autowired
    public ApiGeneralController(InitResponse initResponse, GeneralServiceImpl generalServiceImpl) {
        this.initResponse = initResponse;
        this.generalServiceImpl = generalServiceImpl;
    }

    @GetMapping("/init/")
//    @PreAuthorize("hasAuthority('user:write')")
    private InitResponse init() {
        return initResponse;
    }

    @GetMapping("/settings")
    private SettingsResponse settings() {
        return generalServiceImpl.getGlobalSettings();
    }

    @GetMapping("/tag")
    @ApiOperation(value = "Вывод списка тегов", response = ResponseEntity.class)
    @ApiResponse(code = 200, message = "Список тегов выведен")
//    @PreAuthorize("hasAuthority('user:write')")
    private ResponseEntity<TagResponse> tag(
            @RequestParam(name = "query", required = false) String query
    ) {
        return ResponseEntity.ok(generalServiceImpl.tagsList(query));
    }

    @GetMapping("/calendar")
    @ApiOperation(value = "Вывод календаря", response = ResponseEntity.class)
    @ApiResponse(code = 200, message = "Календарь выведен")
//    @PreAuthorize("hasAuthority('user:write')")
    private ResponseEntity<CalendarResponse> calendar(
            @RequestParam(name = "year", required = false, defaultValue = "0") int year){
        return ResponseEntity.ok(generalServiceImpl.calendar(year));
    }

    @GetMapping("/statistics/all")
    @ApiOperation(value = "Статистика по всему блогу", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Статистика выведена"),
            @ApiResponse(code = 401, message = "Статистика заблокирована")
    })
//    @PreAuthorize("hasAuthority('user:moderate')")
    private ResponseEntity<StatisticResponse> statistics(User user){
        return ResponseEntity.ok(generalServiceImpl.allStats(user));
    }

    @PostMapping(value = "/comment")
    @ApiOperation(value = "Добавление комментария", response = ResponseEntity.class)
    @ApiResponse(code = 200, message = "Комментарий успешно добавлен")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<CommentResponse> comment(@RequestBody CommentRequest commentRequest){
        return ResponseEntity.ok(generalServiceImpl.comment(commentRequest.getParentId(), commentRequest.getPostId(), commentRequest.getText()));
    }
}
