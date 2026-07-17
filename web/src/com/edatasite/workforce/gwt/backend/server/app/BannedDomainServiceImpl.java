package com.edatasite.workforce.gwt.backend.server.app;

import com.edatasite.workforce.gwt.core.server.db.BannedDomainManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("bannedDomainService")
public class BannedDomainServiceImpl implements BannedDomainServiceLocal {

    @Autowired
    private BannedDomainManager bannedDomainManager;

    @Override
    public boolean areEmailAndDomainBanned(String email) {
        return bannedDomainManager.areEmailAndDomainBanned(email);
    }
}
