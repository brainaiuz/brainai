package com.edatasite.workforce.gwt.core.server.db.trainingcenter;

import com.edatasite.workforce.core.domain.trainingcenter.EdsCertificateType;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sherzod
 * Date: 8/17/12
 * Time: 8:01 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CertificateTypeManager extends Manager<EdsCertificateType> {
    List<EdsCertificateType> getCertificateTypes();

    List<Integer> getCertificateTypeCourses(Integer certificateTypeID);
}
