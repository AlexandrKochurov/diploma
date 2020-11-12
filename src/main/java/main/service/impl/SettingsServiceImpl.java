package main.service.impl;

import main.api.response.SettingsResponse;
import main.service.SettingsService;
import org.springframework.stereotype.Service;

@Service
public class SettingsServiceImpl implements SettingsService {
    @Override
    public SettingsResponse getGlobalSettings() {
        SettingsResponse settingsResponse = new SettingsResponse();
        settingsResponse.setStatisticsIsPublic(true);
        settingsResponse.setPostModeration(true);
        settingsResponse.setMultiuserMode(true);
        return settingsResponse;
    }
}
