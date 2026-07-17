package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.crm.contact.EdsContactCareer;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 01.12.2010
 * Time: 20:21:47
 * To change this template use File | Settings | File Templates.
 */
public interface ContactCareerManager extends Manager<EdsContactCareer> {

    List<EdsContactCareer> getContactCareers(Integer contactID);
}
