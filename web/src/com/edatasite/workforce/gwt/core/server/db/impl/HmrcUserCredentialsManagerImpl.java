package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.hmrc.EdsHmrcUserCredentials;
import com.edatasite.workforce.gwt.core.server.db.hmrc.HmrcUserCredentialsManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("hmrcUserCredentialsManager")
public class HmrcUserCredentialsManagerImpl extends BaseManager<EdsHmrcUserCredentials> implements HmrcUserCredentialsManager {
    public HmrcUserCredentialsManagerImpl() {
        super(EdsHmrcUserCredentials.class);
    }

    @Override
    public Optional<EdsHmrcUserCredentials> findFirst() {
        return Optional.ofNullable((EdsHmrcUserCredentials) findSingle("SELECT huc FROM EdsHmrcUserCredentials huc"));
    }
}
