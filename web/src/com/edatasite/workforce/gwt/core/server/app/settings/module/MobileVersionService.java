package com.edatasite.workforce.gwt.core.server.app.settings.module;

import com.edatasite.workforce.rest.v3.release10.core.to.settings.module.MobileVersionDTO;

import java.util.List;

public interface MobileVersionService {
    void create(MobileVersionDTO request);

    MobileVersionDTO getVersion(String name, String version);

    List<MobileVersionDTO> getAllVersionsByName(String name);

    void deleteVersion(String name, String version);
}
