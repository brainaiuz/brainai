package com.edatasite.workforce.gwt.core.server.db.impl.trainingcenter;

import com.edatasite.workforce.core.domain.trainingcenter.EdsCertificateType;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.CertificateTypeManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sherzod
 * Date: 8/17/12
 * Time: 8:00 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("certificateTypeManager")
public class CertificateTypeManagerImpl extends BaseManager<EdsCertificateType> implements CertificateTypeManager{
    public CertificateTypeManagerImpl() {
        super(EdsCertificateType.class);
    }

    @Override
    public List<EdsCertificateType> getCertificateTypes() {
        return find("select ct from EdsCertificateType ct order by ct.objectID");
    }

    @Override
    public List<Integer> getCertificateTypeCourses(Integer certificateTypeID) {
        return find("select distinct ctc.course.objectID from EdsCertificateTypeCourses ctc where ctc.certificateType.objectID = ?", certificateTypeID);
    }
}
