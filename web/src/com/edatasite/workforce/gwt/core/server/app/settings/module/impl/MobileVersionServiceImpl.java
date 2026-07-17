package com.edatasite.workforce.gwt.core.server.app.settings.module.impl;

import com.edatasite.workforce.core.domain.settings.mobile.EdsMobileVersion;
import com.edatasite.workforce.gwt.core.server.app.settings.module.MobileVersionService;
import com.edatasite.workforce.gwt.core.server.db.settings.mobile.MobileVersionManager;
import com.edatasite.workforce.rest.v3.release10.core.to.settings.module.MobileVersionDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("mobileVersionService")
public class MobileVersionServiceImpl implements MobileVersionService {
    private final MobileVersionManager mobileVersionManager;

    public MobileVersionServiceImpl(MobileVersionManager mobileVersionManager) {
        this.mobileVersionManager = mobileVersionManager;
    }


    @Override
    @Transactional
    public void create(MobileVersionDTO request) {
        if (mobileVersionManager.findByNameAndVersion(request.getName(), request.getVersion()) != null) {
            throw new RuntimeException("Version already exists");
        }
        EdsMobileVersion entity = new EdsMobileVersion();
        entity.setName(request.getName());
        entity.setVersion(request.getVersion());
        entity.setActive(request.isActive());
        mobileVersionManager.create(entity);
    }

    @Override
    public MobileVersionDTO getVersion(String name, String version) {
        EdsMobileVersion edsMobileVersion = mobileVersionManager.findByNameAndVersion(name, version);
        if (edsMobileVersion == null) return null;
        return toDto(edsMobileVersion);
    }

    @Override
    public List<MobileVersionDTO> getAllVersionsByName(String name) {
        List<EdsMobileVersion> edsMobileVersions = mobileVersionManager.findAllByName(name);
        if (edsMobileVersions == null) {
            return new ArrayList<>();
        }
        return edsMobileVersions.stream().map(this::toDto).toList();
    }

    @Override
    @Transactional
    public void deleteVersion(String name, String version) {
        EdsMobileVersion edsMobileVersion = mobileVersionManager.findByNameAndVersion(name, version);
        if (edsMobileVersion == null) {
            return;
        }
        mobileVersionManager.delete(edsMobileVersion);
    }

    private MobileVersionDTO toDto(EdsMobileVersion edsMobileVersion) {
        MobileVersionDTO dto = new MobileVersionDTO();
        dto.setId(edsMobileVersion.getObjectID());
        dto.setName(edsMobileVersion.getName());
        dto.setVersion(edsMobileVersion.getVersion());
        dto.setActive(edsMobileVersion.isActive());
        return dto;
    }
}
