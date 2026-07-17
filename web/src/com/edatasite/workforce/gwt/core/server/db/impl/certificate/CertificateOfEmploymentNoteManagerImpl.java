package com.edatasite.workforce.gwt.core.server.db.impl.certificate;

import com.edatasite.workforce.core.domain.certificate.EdsCertificateOfEmployeeNote;
import com.edatasite.workforce.gwt.core.server.db.certificate.CertificateOfEmploymentNoteManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Muxriddin on 15.02.22.
 */
@Repository("certificateOfEmploymentNoteManager")
public class CertificateOfEmploymentNoteManagerImpl extends BaseManager<EdsCertificateOfEmployeeNote> implements CertificateOfEmploymentNoteManager {
    public CertificateOfEmploymentNoteManagerImpl() {
        super(EdsCertificateOfEmployeeNote.class);
    }

    @Override
    public List<EdsCertificateOfEmployeeNote> getComments(Integer certificateId) {
        return find("select cn from EdsCertificateOfEmployeeNote cn where cn.certificateOfEmployment.objectID=" + certificateId);
    }
}
