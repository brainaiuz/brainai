package com.edatasite.workforce.gwt.core.server.db.impl.security;

import com.edatasite.workforce.core.domain.security.EdsAllowedIPAddress;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.security.AllowedIPAddressManager;
import org.springframework.stereotype.Repository;

@Repository
public class AllowedIPAddressManagerImpl extends BaseManager<EdsAllowedIPAddress> implements AllowedIPAddressManager {

    public AllowedIPAddressManagerImpl() {
        super(EdsAllowedIPAddress.class);
    }

    @Override
    public EdsAllowedIPAddress findByIpAddress(String ipAddress) {
        return (EdsAllowedIPAddress) findSingle("from EdsAllowedIPAddress where ip = ? ", ipAddress);
    }
}
