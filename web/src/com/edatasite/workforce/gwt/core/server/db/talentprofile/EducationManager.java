package com.edatasite.workforce.gwt.core.server.db.talentprofile;

import com.edatasite.workforce.core.domain.EdsEducation;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Dec 2, 2009
 * Time: 11:55:49 AM
 * To change this template use File | Settings | File Templates.
 */
public interface EducationManager extends Manager<EdsEducation> {
    List<EdsEducation> getTalentProfileDataByCandidate(ListingFilterParameter fp);
}
