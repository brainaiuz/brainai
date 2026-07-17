package com.edatasite.workforce.core.config.multitenancy;

import com.edatasite.shared.db.EdsScope;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {
    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenantIdentifier = ServerSecurityContext.getInstance().getCompanyId();
        if (StringUtils.isEmpty(tenantIdentifier)) {
            return EdsScope.PUBLIC_SCHEMA;
        }
        return tenantIdentifier;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return false;
    }
}
