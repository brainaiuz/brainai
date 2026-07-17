package com.edatasite.workforce.gwt.core.server.db.impl.rbac.contact;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.contact.EdsContactCategory;
import com.edatasite.workforce.core.domain.crm.contact.EdsContactPermission;
import com.edatasite.workforce.core.domain.rbac.contact.EdsContactCategoryRbac;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Oct 25, 2010
 * Time: 4:23:13 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ContactCategoryRbacManager extends Manager<EdsContactCategoryRbac> {

    void addRbacEntries(EdsContactCategory contactCategory);

    EdsContactPermission getContactCategoryPermissionForUser(EdsUser user, EdsContactCategory... categories);

    List<EdsContactCategoryRbac> getContactCategoryEntryForUser(EdsContactCategory category, EdsUser user);

    List<EdsContactCategoryRbac> getContactCategoryEntryForUser(Integer categoryID, Integer userID);

    List<EdsContactCategoryRbac> getContactRbacEntries(Integer objectID);

    List<EdsContactCategoryRbac> getContactCategoryRbacEntries(Integer objectID);

    EdsContactCategoryRbac createRbacEntry(EdsCompany company, EdsObject contactOrCategory, EdsObject userOrGroup, String relationShip, Integer relationRank, int entryType, EdsContactPermission permission);
}
