package com.edatasite.workforce.rest.v4.hrms.service;

import com.edatasite.workforce.core.domain.EdsOrgBoardSettings;
import com.edatasite.workforce.gwt.core.server.db.LocationManager;
import com.edatasite.workforce.gwt.core.server.db.OrgBoardSettingsManager;
import com.edatasite.workforce.gwt.team.client.services.dto.OrgBoardSettingsItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class OrgBoardSettingsService {

    private static final Logger log = LoggerFactory.getLogger(OrgBoardSettingsService.class);

    private final OrgBoardSettingsManager settingsManager;
    private final LocationManager locationManager;

    public OrgBoardSettingsService(OrgBoardSettingsManager settingsManager, LocationManager locationManager) {
        this.settingsManager = settingsManager;
        this.locationManager = locationManager;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public OrgBoardSettingsItem getOrgBoardSettings(Integer userId) {
        EdsOrgBoardSettings settings = settingsManager.findSettingsByEmployee(userId);
        if (settings != null) {
            return settings.getAsDTO();
        }
        settings = new EdsOrgBoardSettings();
        settings.setShowEmployees(true);
        settings.setShowGoals(true);
        settings.setShowDescription(true);
        settings.setShowShortDescription(true);
        settings.setEmployeeId(userId);
        settings.setLocation(null);
        settingsManager.create(settings);
        return settings.getAsDTO();
    }

    @Transactional
    public OrgBoardSettingsItem updateOrgBoardSettings(Integer userId, OrgBoardSettingsItem body) {
        var settings = Optional.of(settingsManager.findSettingsByEmployee(userId)).orElseThrow(() -> new RuntimeException("Invalid User: " + userId));

        if (!settings.getShowEmployees().equals(body.getShowEmployees()))
            settings.setShowEmployees(body.getShowEmployees());
        if (!settings.getShowShortDescription().equals(body.getShowShortDescription()))
            settings.setShowShortDescription(body.getShowShortDescription());
        if (!settings.getShowDescription().equals(body.getShowDescription()))
            settings.setShowDescription(body.getShowDescription());
        if (!settings.getShowGoals().equals(body.getShowGoals())) settings.setShowGoals(body.getShowGoals());

        settings.setLocation(
                body.getLocationId() != null
                        ? locationManager.get(body.getLocationId())
                        : null
        );

        settingsManager.update(settings);
        return settings.getAsDTO();
    }
}
