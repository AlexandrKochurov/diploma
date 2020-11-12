package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class SettingsResponse {
    @JsonProperty("MULTIUSER_MODE")
    private boolean multiuserMode;

    @JsonProperty("POST_PREMODERATION")
    private boolean postModeration;

    @JsonProperty("STATISTICS_IS_PUBLIC")
    private boolean statisticsIsPublic;
}
