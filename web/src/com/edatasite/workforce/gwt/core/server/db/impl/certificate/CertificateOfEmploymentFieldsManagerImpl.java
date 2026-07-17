package com.edatasite.workforce.gwt.core.server.db.impl.certificate;

import com.edatasite.workforce.core.domain.certificate.EdsCertificateOfEmploymentFields;
import com.edatasite.workforce.gwt.core.server.db.certificate.CertificateOfEmploymentFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * Created by Khasan on 06.10.14.
 */
@Repository("certificateOfEmploymentFieldsManager")
public class CertificateOfEmploymentFieldsManagerImpl extends BaseManager<EdsCertificateOfEmploymentFields> implements CertificateOfEmploymentFieldsManager {
    public CertificateOfEmploymentFieldsManagerImpl() {
        super(EdsCertificateOfEmploymentFields.class);
    }
}
