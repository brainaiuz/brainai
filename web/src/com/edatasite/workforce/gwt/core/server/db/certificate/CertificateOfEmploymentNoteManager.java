package com.edatasite.workforce.gwt.core.server.db.certificate;

import com.edatasite.workforce.core.domain.certificate.EdsCertificateOfEmployeeNote;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by Muxriddin on 15.02.22.
 */
public interface CertificateOfEmploymentNoteManager extends Manager<EdsCertificateOfEmployeeNote> {

    List<EdsCertificateOfEmployeeNote> getComments(Integer certificateId);

}
