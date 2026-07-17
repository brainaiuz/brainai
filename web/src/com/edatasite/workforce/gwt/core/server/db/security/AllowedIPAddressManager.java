package com.edatasite.workforce.gwt.core.server.db.security;

import com.edatasite.workforce.core.domain.security.EdsAllowedIPAddress;
import com.edatasite.workforce.gwt.core.server.db.Manager;

public interface AllowedIPAddressManager extends Manager<EdsAllowedIPAddress> {

    EdsAllowedIPAddress findByIpAddress(String ipAddress);
}
