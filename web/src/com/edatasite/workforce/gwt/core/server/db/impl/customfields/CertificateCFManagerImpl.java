package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsCertificateCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.CertificateCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * User : Akhror on 28/10/2021
 */
@Repository("certificateCFManager")
public class CertificateCFManagerImpl extends BaseManager<EdsCertificateCustomFields> implements CertificateCFManager {
    public CertificateCFManagerImpl() {
        super(EdsCertificateCustomFields.class);
    }
}
