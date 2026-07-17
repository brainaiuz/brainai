package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.crm.contact.EdsContactCareer;
import com.edatasite.workforce.gwt.core.server.db.ContactCareerManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 01.12.2010
 * Time: 20:21:09
 * To change this template use File | Settings | File Templates.
 */
@Repository("contactCareerManager")
public class ContactCareerManagerImpl extends BaseManager<EdsContactCareer> implements ContactCareerManager {

    public ContactCareerManagerImpl() {
        super(EdsContactCareer.class);
    }

    public List<EdsContactCareer> getContactCareers(Integer contactID) {
        return find("select car from EdsContactCareer car where car.deleted <> true and car.contact.objectID = ? order by car.fromYear desc ", contactID);
    }
}
