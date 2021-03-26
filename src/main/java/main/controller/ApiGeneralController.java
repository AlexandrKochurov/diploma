package main.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import main.api.request.ChangeProfileRequest;
import main.api.request.CommentRequest;
import main.api.request.PostModerationRequest;
import main.api.request.SettingsRequest;
import main.api.response.*;
import main.model.User;
import main.service.impl.GeneralServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;


@RestController
@RequestMapping(value = "/api/")
@Api(value = "General Controller")
public class ApiGeneralController {

    private final GeneralServiceImpl generalServiceImpl;
    private final InitResponse initResponse;

    @Autowired
    public ApiGeneralController(GeneralServiceImpl generalServiceImpl, InitResponse initResponse) {
        this.generalServiceImpl = generalServiceImpl;
        this.initResponse = initResponse;
    }

    @GetMapping("init")
    public InitResponse init() {
        return initResponse;
    }

    @GetMapping("settings")
    @ApiOperation(value = "Получение глобальных настроек", response = ResponseEntity.class)
    @ApiResponse(code = 200, message = "Глобальные настройки получены")
    public ResponseEntity<SettingsResponse> getSettings() {
        return ResponseEntity.ok(generalServiceImpl.getGlobalSettings());
    }

    @PutMapping("settings")
    @ApiOperation(value = "Изменение глобальных настроек", response = ResponseEntity.class)
    @ApiResponse(code = 200, message = "Глобальные настройки изменены")
    @PreAuthorize("hasAuthority('user:moderate')")
    public void setSettings(@RequestBody SettingsRequest settingsRequest) {
        generalServiceImpl.setGlobalSettings(settingsRequest);
    }

    @GetMapping("tag")
    @ApiOperation(value = "Вывод списка тегов", response = ResponseEntity.class)
    @ApiResponse(code = 200, message = "Список тегов выведен")
//    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<TagResponse> tag(
            @RequestParam(name = "query", required = false) String query
    ) {
        return ResponseEntity.ok(generalServiceImpl.tagsList(query));
    }

    @GetMapping("calendar")
    @ApiOperation(value = "Вывод календаря", response = ResponseEntity.class)
    @ApiResponse(code = 200, message = "Календарь выведен")
//    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<CalendarResponse> calendar(
            @RequestParam(name = "year", required = false, defaultValue = "0") int year) {
        return ResponseEntity.ok(generalServiceImpl.calendar(year));
    }

    @GetMapping("statistics/all")
    @ApiOperation(value = "Статистика по всему блогу", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Статистика выведена"),
            @ApiResponse(code = 401, message = "Статистика заблокирована")
    })
//    @PreAuthorize("hasAuthority('user:moderate')")
    public ResponseEntity<StatisticResponse> statistics(User user) {
        return ResponseEntity.ok(generalServiceImpl.allStats(user));
    }

    @PostMapping(value = "comment")
    @ApiOperation(value = "Добавление комментария", response = ResponseEntity.class)
    @ApiResponse(code = 200, message = "Комментарий успешно добавлен")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<CommentResponse> comment(@RequestBody CommentRequest commentRequest) {
        return ResponseEntity.ok(generalServiceImpl.comment(commentRequest.getParentId(), commentRequest.getPostId(), commentRequest.getText()));
    }

    @PostMapping(value = "image", consumes = {"multipart/form-data"})
    @ApiOperation(value = "Загрузка изображения", response = ResponseEntity.class)
    @ApiResponse(code = 200, message = "Изображение успешно загружено")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<String> image(@RequestPart("image") MultipartFile image) throws IOException {
        return ResponseEntity.ok(generalServiceImpl.imageUpload(image));
    }

    @PostMapping(value = "profile/my", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProfile(@Valid @RequestBody ChangeProfileRequest request) throws IOException {
        return ResponseEntity.ok(generalServiceImpl.changeProfile(request));
    }

    @PostMapping(value = "profile/my", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProfileWithPhoto(
            @RequestParam("photo") MultipartFile photo, // это картинка
            @RequestParam("removePhoto") int removePhoto,
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam(name = "password", required = false) String password
    ) throws IOException {
        ChangeProfileRequest changeProfileRequest = new ChangeProfileRequest(photo, name, email, password, removePhoto);
        return ResponseEntity.ok(generalServiceImpl.changeProfile(changeProfileRequest));
    }

    @PostMapping(value = "moderation")
    @ApiOperation(value = "Решение модератора", response = ResponseEntity.class)
    @ApiResponse(code = 200, message = "Решение принято")
    @PreAuthorize("hasAuthority('user:moderate')")
    public ResponseEntity<ModeratorDecisionResponse> postModeration(@RequestBody PostModerationRequest request){
        return ResponseEntity.ok(generalServiceImpl.changeModeratorDecision(request));
    }
}
