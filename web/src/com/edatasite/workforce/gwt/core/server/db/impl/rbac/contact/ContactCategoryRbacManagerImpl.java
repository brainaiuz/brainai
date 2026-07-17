package com.edatasite.workforce.gwt.core.server.db.impl.rbac.contact;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.contact.EdsContactCategory;
import com.edatasite.workforce.core.domain.crm.contact.EdsContactPermission;
import com.edatasite.workforce.core.domain.crm.contact.EdsContactPolicy;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.rbac.EdsGroup;
import com.edatasite.workforce.core.domain.rbac.EdsRelationship;
import com.edatasite.workforce.core.domain.rbac.EdsTrusteeType;
import com.edatasite.workforce.core.domain.rbac.contact.EdsContactCategoryRbac;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Oct 25, 2010
 * Time: 4:22:21 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("contactCategoryRbacManager")
public class ContactCategoryRbacManagerImpl extends BaseManager<EdsContactCategoryRbac> implements ContactCategoryRbacManager {

    @Autowired
    private ContactPolicyManager contactPolicyManager;

    public ContactCategoryRbacManagerImpl() {
        super(EdsContactCategoryRbac.class);
    }

    public void addRbacEntries(EdsContactCategory contactCategory) {
        removeContactEntries(contactCategory.getObjectID());
        EdsContactPolicy ownerPolicy = contactPolicyManager.getCompanyRelationPolicy(EdsRelationship.CONTACT_OWNER);
        createRbacEntry(contactCategory.getOwner().getCompany(), contactCategory, contactCategory.getOwner(), ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), EdsContactCategoryRbac.INHERITED, ownerPolicy.getPermission());
    }

    public EdsContactCategoryRbac createRbacEntry(EdsCompany company, EdsObject contactOrCategory, EdsObject userOrGroup, String relationShip, Integer relationRank, int entryType, EdsContactPermission permission) {
        boolean isCategory = contactOrCategory instanceof EdsContactCategory;
        boolean isUser = userOrGroup instanceof EdsUser;
        EdsContactCategoryRbac rbac = new EdsContactCategoryRbac();
        if (isUser) {
            rbac.setUser((EdsUser) userOrGroup);
            rbac.setTrusteeType(EdsTrusteeType.USER);
        } else {
            rbac.setGroup((EdsGroup) userOrGroup);
            rbac.setTrusteeType(EdsTrusteeType.GROUP);
        }
        rbac.setEntryType(entryType);
        rbac.setRelationship(relationShip);
        rbac.setRelationRank(relationRank);
        rbac.setContactPermission(permission);
        if (isCategory) {
            rbac.setContactCategory((EdsContactCategory) contactOrCategory);
            rbac.setEntityId(((EdsContactCategory) contactOrCategory).getEntityId());
            rbac.setContactCategoryType(((EdsContactCategory) contactOrCategory).getCategoryType());
        } else {
            rbac.setContact((EdsCrmContact) contactOrCategory);
            rbac.setEntityId(contactOrCategory.getObjectID());
        }
        rbac.setContact(!isCategory);
        create(rbac);
        return rbac;
    }

    @Override
    public EdsContactPermission getContactCategoryPermissionForUser(EdsUser user, EdsContactCategory... categories) {
        List<EdsContactPermission> permissions = new ArrayList<>();
        if (categories != null) {
            for (EdsContactCategory category : categories) {
                for (EdsContactCategoryRbac rbac : getContactCategoryEntryForUser(category, user)) {
                    permissions.add(rbac.getContactPermission());
                }
            }
        }
        EdsContactPermission permission = new EdsContactPermission();
        permission.mergePermissions(permission, permissions);
        return permission;
    }

    public void removeContactEntries(Integer objectID) {
        update("DELETE FROM EdsContactCategoryRbac cc WHERE cc.contactCategory.objectID = ? ", objectID);
    }

    @Override
    public List<EdsContactCategoryRbac> getContactCategoryEntryForUser(EdsContactCategory category, EdsUser user) {
        if (category == null || user == null) {
            return null;
        }
        return getContactCategoryEntryForUser(category.getObjectID(), user.getObjectID());
    }

    @Override
    public List<EdsContactCategoryRbac> getContactCategoryEntryForUser(Integer categoryID, Integer userID) {
        Map params = new HashMap();
        params.put("userId", userID);
        params.put("categoryId", categoryID);
        params.put("trusteeType", EdsTrusteeType.USER);
        return (List<EdsContactCategoryRbac>) findByNamedParams("select distinct rbac from EdsContactCategoryRbac rbac where (rbac.user.objectID=:userId or rbac.group.objectID in " +
                "(select distinct gg.objectID from EdsGroup gg join gg.members memb where memb.objectID in (select distinct t.objectID from EdsTrustee t where t.trusteeID=:userId and t.type.objectID=:trusteeType))) and rbac.contactCategory.objectID=:categoryId", params);
    }

    @Override
    public List<EdsContactCategoryRbac> getContactRbacEntries(Integer objectID) {
        return (List<EdsContactCategoryRbac>) find("SELECT ti FROM EdsContactCategoryRbac ti WHERE ti.contact.objectID = ? and ti.isContact is true", objectID);
    }

    @Override
    public List<EdsContactCategoryRbac> getContactCategoryRbacEntries(Integer objectID) {
        return (List<EdsContactCategoryRbac>) find("SELECT ti FROM EdsContactCategoryRbac ti left join ti.user u WHERE ti.contactCategory.objectID = ? and ti.isContact<>true and (u.deleted is null or u.deleted is not true)", objectID);
    }

}
