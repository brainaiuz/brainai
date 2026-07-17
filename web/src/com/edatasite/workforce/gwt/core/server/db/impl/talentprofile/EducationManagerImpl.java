package com.edatasite.workforce.gwt.core.server.db.impl.talentprofile;

import com.edatasite.workforce.core.domain.EdsEducation;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.talentprofile.EducationManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Dec 2, 2009
 * Time: 11:58:46 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository("educationManager")
public class EducationManagerImpl extends BaseManager<EdsEducation> implements EducationManager {

    public EducationManagerImpl() {
        super(EdsEducation.class);
    }


    @Override
    public List<EdsEducation> getTalentProfileDataByCandidate(ListingFilterParameter fp) {
        if (fp.getContactID() != null){
            return (List<EdsEducation>) findNative("select * from " + getCompanyId() + ".education where candidateid =" + fp.getContactID(),EdsEducation.class);
        }
        return (List<EdsEducation>) findNative("select * from " + getCompanyId() + ".education where candidateid =" + fp.getEmployeeId(),EdsEducation.class);
    }
}
