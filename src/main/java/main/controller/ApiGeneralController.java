package main.controller;

import main.api.request.*;
import main.api.response.*;
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
    public ResponseEntity<SettingsResponse> getSettings() {
        return ResponseEntity.ok(generalServiceImpl.getGlobalSettings());
    }

    @PutMapping("settings")
    @PreAuthorize("hasAuthority('user:moderate')")
    public void setSettings(@RequestBody SettingsRequest settingsRequest) {
        generalServiceImpl.setGlobalSettings(settingsRequest);
    }

    @GetMapping("tag")
    public ResponseEntity<TagResponse> tag(
            @RequestParam(name = "query", required = false) String query
    ) {
        return ResponseEntity.ok(generalServiceImpl.tagsList(query));
    }

    @GetMapping("calendar")
//    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<CalendarResponse> calendar(
            @RequestParam(name = "year", required = false, defaultValue = "0") int year) {
        return ResponseEntity.ok(generalServiceImpl.calendar(year));
    }

    @GetMapping("statistics/all")
    public ResponseEntity<StatisticResponse> allStatistics() {
        return ResponseEntity.ok(generalServiceImpl.allStats());
    }

    @GetMapping("statistics/my")
    public ResponseEntity<StatisticResponse> myStatistics() {
        return ResponseEntity.ok(generalServiceImpl.myStats());
    }

    @PostMapping(value = "comment")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<CommentResponse> comment(@RequestBody CommentRequest commentRequest) {
        return ResponseEntity.ok(generalServiceImpl.comment(commentRequest));
    }

    @PostMapping(value = "image", consumes = {"multipart/form-data"})
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<String> image(@RequestPart("image") MultipartFile image) throws IOException {
        return ResponseEntity.ok(generalServiceImpl.imageUpload(image));
    }

    @PostMapping(value = "profile/my", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProfile(@Valid @RequestBody ChangeProfileRequest request){
        return ResponseEntity.ok(generalServiceImpl.changeProfile(request));
    }

    @PostMapping(value = "profile/my", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProfileWithPhoto(
            @RequestParam("photo") MultipartFile photo, // это картинка
            @RequestParam("removePhoto") Integer removePhoto,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "password", required = false) String password
    ) throws IOException {
        return ResponseEntity.ok(generalServiceImpl.changeProfileWithPhoto(photo, name, email, password, removePhoto));
    }

    @PostMapping(value = "moderation")
    @PreAuthorize("hasAuthority('user:moderate')")
    public ResponseEntity<ResultResponse> postModeration(@RequestBody PostModerationRequest request){
        return ResponseEntity.ok(generalServiceImpl.changeModeratorDecision(request));
    }
}
