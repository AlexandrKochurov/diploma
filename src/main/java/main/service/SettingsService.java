package main.service;

import main.api.response.SettingsResponse;
import org.springframework.stereotype.Service;

@Service
public interface SettingsService {
    SettingsResponse getGlobalSettings();
}
