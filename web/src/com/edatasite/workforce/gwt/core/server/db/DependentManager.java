package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsDependent;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * User: unni
 * Date: Oct 22, 2009
 * Time: 10:48:41 AM
 */
public interface DependentManager extends Manager<EdsDependent> {

    List<EdsDependent> getDependentList(ListingFilterParameter fp);

    List<EdsDependent> getDependentList(EdsEmployee employee);

    List<EdsDependent> getDependenstByCandidate(EdsCrmContact candidate);

    void deleteRelatedDependents(Integer id, String relation);
}
