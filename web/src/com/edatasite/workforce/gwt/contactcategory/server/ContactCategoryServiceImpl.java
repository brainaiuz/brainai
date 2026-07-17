package com.edatasite.workforce.gwt.contactcategory.server;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.contact.EdsContactCategory;
import com.edatasite.workforce.core.domain.crm.contact.EdsContactPermission;
import com.edatasite.workforce.core.domain.crm.contact.EdsContactPolicy;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.documents.EdsAuditInfo;
import com.edatasite.workforce.core.domain.rbac.EdsRelationship;
import com.edatasite.workforce.core.domain.rbac.EdsTrusteeType;
import com.edatasite.workforce.core.domain.rbac.contact.EdsContactCategoryRbac;
import com.edatasite.workforce.core.domain.rbac.documents.EdsFolderRbac;
import com.edatasite.workforce.core.solr.component.ContactSolrComponent;
import com.edatasite.workforce.gwt.contactcategory.client.rpc.ContactCategoryListItem;
import com.edatasite.workforce.gwt.contactcategory.client.rpc.ContactCategoryService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.rbac.GroupMembersViewItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.ContactCategoryManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.impl.rbac.contact.ContactCategoryRbacManager;
import com.edatasite.workforce.gwt.core.server.db.impl.rbac.contact.ContactPolicyManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.GroupManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.RelationshipManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.CrmContactCategoryEventListener;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.documents.client.rest.resource.PermissionHolder;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: Dilshod Madrahimov
 * Date: 10.05.2018
 * Time: 19:39:00
 */
@Transactional
@Service("contactCategoryService")
public class ContactCategoryServiceImpl implements ContactCategoryService, ContactCategoryServiceLocal, Constants {
    private static final Logger log = LoggerFactory.getLogger(ContactCategoryServiceImpl.class);

    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    @Qualifier("crmLocalizer")
    private WfmMessageSource crmLocalizer;
    @Autowired
    private RbacService rbacService;
    @Autowired
    private ContactCategoryManager contactCategoryManager;
    @Autowired
    private ContactCategoryRbacManager contactCategoryRbacManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private ContactPolicyManager contactPolicyManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private RelationshipManager relationshipManager;
    @Autowired
    private GroupManager groupManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private ContactSolrComponent contactSolrComponent;


    @Override
    public Boolean deleteContactCategory(Integer categoryID, Integer selectedCategoryID, boolean deleteWithChildren) {
        EdsContactCategory category = contactCategoryManager.get(categoryID);
        List<EdsContactCategory> removingCategories = EdsContactCategory.asList(deleteWithChildren, category);
        EdsContactCategory selectedCategory = contactCategoryManager.get(selectedCategoryID);
        if (category != null && selectedCategory != null) {
            if (!deleteWithChildren && category.getSubContactCategories() != null && !category.getSubContactCategories().isEmpty()) {
                for (EdsContactCategory child : category.getSubContactCategories()) {
                    child.setParent(selectedCategory);
                    copyRbacsFromParent(child, true, true);
                }
            }
            List<EdsCrmContact> contacts = crmContactManager.getContactsByCategoryIDs(EdsContactCategory.getObjectIDs(removingCategories), 0, 500);
            if (contacts != null && !contacts.isEmpty()) {
                do {
                    for (EdsCrmContact contact : contacts) {
                        removingCategories.forEach(contact.getCategories()::remove);
                        contact.addCategories(selectedCategory);
                    }
                    try {
                        contactSolrComponent.indexes(contacts);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    contacts = crmContactManager.getContactsByCategoryIDs(EdsContactCategory.getObjectIDs(removingCategories), contacts.get(contacts.size() - 1).getObjectID(), 500);
                } while (contacts.size() > 0);
            }
            category = contactCategoryManager.get(categoryID);
            category.setDeleted(true, deleteWithChildren);
        }
        return Boolean.TRUE;
    }

    private void copyRbacsFromParent(EdsContactCategory contactCategory, boolean firstDeleteInheriteds, boolean doSameWithChildren) {
        if (contactCategory != null && contactCategory.getParent() != null) {
            if (firstDeleteInheriteds) {
                deleteInheritedRbacs(contactCategory);
            }
            List<EdsContactCategoryRbac> rbacs = contactCategory.getParent().getContactCategoryRbacs();
            if (rbacs != null && rbacs.size() > 0) {
                List<EdsContactCategoryRbac> newRbacs = new ArrayList<>();
                for (EdsContactCategoryRbac rbac : rbacs) {
                    if (contactCategory.getParent().getCategoryType() != EdsContactCategory.PRIVATE_CONTACT_CATEGORY && (contactCategory.getParent().getType() != EdsContactCategory.SYSTEM_BUILTIN || !EdsRelationship.CONTACT_OWNER.equals(rbac.getRelationship()))) {
                        if (contactCategory.getOwner() != null && rbac.getUser() != null) {
                            if (!contactCategory.getOwner().getObjectID().equals(rbac.getUser().getObjectID())) {
                                EdsContactCategoryRbac newRbac = rbac.copyRbac(contactCategory);
                                if (rbac.getRelationship().equals(EdsRelationship.CONTACT_OWNER) || rbac.getRelationship().equals(EdsRelationship.CONTACT_CO_OWNERS)) {
                                    EdsContactPolicy ownerPolicy = contactPolicyManager.getCompanyRelationPolicy(EdsRelationship.CONTACT_CO_OWNERS);
                                    newRbac.setRelationship(ownerPolicy.getRelation().getCode());
                                    newRbac.setRelationRank(ownerPolicy.getRelation().getRank());
                                    newRbac.setContactPermission(ownerPolicy.getPermission());
                                }
                                newRbac.setEntryType(EdsFolderRbac.INHERITED);
                                contactCategoryRbacManager.createOrUpdate(newRbac);
                                newRbacs.add(newRbac);
                            }
                        }
                    }
                }
                contactCategory.setContactCategoryRbacs(newRbacs);
                contactCategoryManager.update(contactCategory);
            }
            if (doSameWithChildren && contactCategory.hasChildren()) {
                for (EdsContactCategory child : contactCategory.getSubContactCategories()) {
                    copyRbacsFromParent(child, firstDeleteInheriteds, true);
                }
            }
        }
    }

    private void deleteInheritedRbacs(EdsContactCategory contactCategory) {
        List<EdsContactCategoryRbac> rbacs = contactCategory.getContactCategoryRbacs();
        if (rbacs != null && rbacs.size() > 0) {
            for (EdsContactCategoryRbac rbac : rbacs) {
                if (rbac.getEntryType() != EdsContactCategory.INHERITED) {
                    contactCategoryRbacManager.delete(rbac);
                }
            }
        }
    }


    @Override
    public ArrayList<ContactCategoryListItem> getContactCategories() {
        ArrayList<ContactCategoryListItem> categoryListItems = contactCategoryManager.getContactCategories();
        int index = 0;
        for (ContactCategoryListItem item : categoryListItems) {
            String categoryName = item.getName().replace(" ", "_");
            String localizeCategoryName = commonLocalizer.localize(categoryName, item.getName());
            categoryListItems.get(index++).setName(localizeCategoryName);
        }
        return categoryListItems;
    }

    @Override
    public ListResult<ContactCategoryListItem> getContactCategoryList(ListingFilterParameter filterParameter) {
        List<EdsContactCategory> contactCategories = contactCategoryManager.getContactCategoryList(filterParameter);
        Integer contactCategoriesTotal = contactCategoryManager.getContactCategoryCount(filterParameter);
        ArrayList<ContactCategoryListItem> list = new ArrayList<>();
        for (EdsContactCategory category : contactCategories) {
            list.add(category.getDTO());
        }

        return new ListResult<>(list, contactCategoriesTotal);
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ContactCategoryListItem editContactCategory(Integer objectID) {
        EdsUser user = contactCategoryManager.getUser();
        EdsContactCategory contactCategory = (objectID != null && contactCategoryManager.get(objectID) != null) ? contactCategoryManager.get(objectID) : new EdsContactCategory();
        ContactCategoryListItem item = contactCategory.getObjectID() != null ? contactCategory.getDTO() : new ContactCategoryListItem();
        item.setGroups(rbacService.getCompanyGroupsWithMembers());
        item.setOwner(contactCategory.getOwner() != null ? contactCategory.getOwner().getDTO() : user.getDTO());
        item.setModificationDate(contactCategory.getAuditInfo() != null ? new DateNonConvertable(contactCategory.getAuditInfo().getModificationDate()) : null);
        if (contactCategory.getObjectID() != null) {
            EdsContactPermission permission = contactCategoryRbacManager.getContactCategoryPermissionForUser(user, contactCategory);
            if (permission != null) {
                item.setPermission(permission.getDTO());
            }
            item.setPermissions(getContactOrCategoryPermissions(contactCategory.getObjectID(), false));
        }
        item.setParents(getContactCategories().toArray(new ContactCategoryListItem[]{}));
        return item;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public HashSet<PermissionHolder> getContactOrCategoryPermissions(Integer objectID, boolean isContact) {
        List<EdsContactCategoryRbac> res = isContact ? contactCategoryRbacManager.getContactRbacEntries(objectID) : contactCategoryRbacManager.getContactCategoryRbacEntries(objectID);
        HashSet<PermissionHolder> permissionHolderSet = new HashSet<>();
        for (EdsContactCategoryRbac rbac : res) {
            if (!rbac.getRelationship().equals(EdsRelationship.CONTACT_CO_OWNERS)) {
                PermissionHolder p = new PermissionHolder();
                p.setObjectId(rbac.getContactPermission().getObjectID());
                p.setRelationship(rbac.getRelationship());
                if (EdsTrusteeType.GROUP.equals(rbac.getTrusteeType())) {
                    GroupMembersViewItem groupM = rbac.getGroup().getDTO();
                    groupM.setGroupName(commonLocalizer.localize(groupM.getGroupConstantName(), groupM.getGroupName()));
                    p.setGroup(groupM);
                }
                if (EdsTrusteeType.USER.equals(rbac.getTrusteeType())) {
                    p.setUser(rbac.getUser().getDTO());
                }
                p.setCanChange(true);
                if (EdsRelationship.DOC_ADMINISTRATOR.equals(rbac.getRelationship())) {

                } else if (EdsRelationship.DOC_DIRECTOR.equals(rbac.getRelationship())) {

                } else if (EdsRelationship.CONTACT_OWNER.equals(rbac.getRelationship())) {
                    p.setCanChange(false);
                } else if (EdsRelationship.CONTACT_EDITOR.equals(rbac.getRelationship())) {

                } else if (EdsRelationship.CONTACT_VIEWER.equals(rbac.getRelationship())) {

                }
                p.setDelete(rbac.getContactPermission().isDelete());
                p.setRead(rbac.getContactPermission().isRead());
                p.setWrite(rbac.getContactPermission().isWrite());
                p.setModifyACL(rbac.getContactPermission().isModifyACL());
                permissionHolderSet.add(p);
            }
        }
        return permissionHolderSet;
    }

    @Transactional
    public ContactCategoryListItem saveContactCategory(ContactCategoryListItem item) {
        return saveContactCategory(item, userManager.getUser(), FROM_CRM_CONTACT_CATEGORY);
    }

    @Transactional
    public ContactCategoryListItem saveContactCategory(ContactCategoryListItem item, EdsUser user, String from) {
        EdsContactCategory contactCategory;
        if (item.getObjectID() != null) {
            contactCategory = contactCategoryManager.get(item.getObjectID());
        } else {
            contactCategory = new EdsContactCategory();
        }
        contactCategory.setName(item.getName());
        contactCategory.setDescription(item.getDescription());
        EdsAuditInfo auditInfo = contactCategory.getAuditInfo() != null ? contactCategory.getAuditInfo() : new EdsAuditInfo();
        auditInfo.setCreatedBy(contactCategory.getAuditInfo() != null ? contactCategory.getAuditInfo().getCreatedBy() : user);
        auditInfo.setCreationDate(contactCategory.getAuditInfo() != null ? contactCategory.getAuditInfo().getCreationDate() : new Date());
        auditInfo.setModifiedBy(user);
        auditInfo.setModificationDate(new Date());
        contactCategory.setCategoryType(item.getCategoryType());
        contactCategory.setDoNotShow(item.isDoNotShow());
        if (item.getParentID() != null) {
            contactCategory.setParent(contactCategoryManager.get(item.getParentID()));
        } else {
            contactCategory.setParent(null);
        }
        contactCategory.setAuditInfo(auditInfo);
        contactCategory.setOwner(contactCategory.getOwner() != null ? contactCategory.getOwner() : user);
        contactCategory.setType(item.getType() > 0 ? item.getType() : EdsContactCategory.CUSTOM);
        if (contactCategory.getType() == EdsContactCategory.SYSTEM_BUILTIN && contactCategory.getConstantName() == null) {
            contactCategory.setConstantName(contactCategory.getName());
        }
        boolean isNew = contactCategoryManager.createOrUpdate(contactCategory);
        if (isNew) {
            contactCategoryRbacManager.addRbacEntries(contactCategory);
            copyRbacsFromParent(contactCategory, false, false);
        } else {
            updateRbacEntries(contactCategory, item.getPermissions(), false);
        }
        if (FROM_CRM_CONTACT_CATEGORY.equals(from)) {
            baseEventPostProcessor.registerEvent(CrmContactCategoryEventListener.TYPE, isNew ? BaseEventsPostProcessorImpl.EVENT_TYPE_ADD : BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, contactCategory, user);
        }
        return contactCategory.getDTO();
    }

    @Transactional
    public void updateRbacEntries(EdsContactCategory contactCategory, Set<PermissionHolder> permissions, boolean isSubCategory) {
        // Delete previous entries
        EdsUser user = contactCategoryRbacManager.getUser();
        List<EdsContactCategoryRbac> rbacList = contactCategoryRbacManager.getContactCategoryRbacEntries(contactCategory.getObjectID());
        List<EdsContactCategoryRbac> rbacList2 = new ArrayList<>();
        for (EdsContactCategoryRbac rbac : rbacList) {
            boolean deleted = false;
            if (rbac.getEntryType() != EdsContactCategoryRbac.CUSTOM || !isSubCategory) {
                if (EdsRelationship.CONTACT_VIEWER.equals(rbac.getRelationship())) {
                    contactCategoryRbacManager.delete(rbac);
                    deleted = true;
                } else {
                    if (EdsRelationship.CONTACT_EDITOR.equals(rbac.getRelationship())) {
                        contactCategoryRbacManager.delete(rbac);
                        deleted = true;
                    }
                }
            }
            if (!deleted) {
                rbacList2.add(rbac);
            }
        }

        for (PermissionHolder dto : permissions) {
            if (dto.isCanChange()) {
                // Don't include 'empty' permission
                if (!dto.isRead() && !dto.isWrite() && !dto.isDelete() && !dto.isModifyACL()) {
                    continue;
                }
                EdsContactCategoryRbac rbac = null;
                if (dto.getGroup() != null) {
                    rbac = getCategoryOrContactRbacForUser(rbacList2, dto.getGroup().getGroupID(), false);
                } else {
                    if (dto.getUser() != null) {
                        rbac = getCategoryOrContactRbacForUser(rbacList2, dto.getUser().getObjectId(), true);
                    }
                }
                if (rbac != null) {
                    rbac.setContactPermission(rbac.getContactPermission().mergePermission(getPermission(dto)));
                    contactCategoryRbacManager.update(rbac);
                    continue;
                }
                EdsContactPermission permission = getPermission(dto);
                EdsRelationship relationship;
                if (null != dto.getRelationship() && !"".equals(dto.getRelationship())) {
                    relationship = relationshipManager.getRelationship(dto.getRelationship());
                } else {
                    relationship = relationshipManager.getRelationship(EdsRelationship.CONTACT_VIEWER);
                }
                EdsObject userOrGroup = dto.getGroup() != null ? groupManager.get(dto.getGroup().getGroupID()) : userManager.get(dto.getUser().getObjectId());
                contactCategoryRbacManager.createRbacEntry(user.getCompany(), contactCategory, userOrGroup, relationship.getCode(), relationship.getRank(), isSubCategory ? EdsFolderRbac.INHERITED : EdsFolderRbac.CUSTOM, permission);
            }
        }
        for (EdsContactCategory sub : contactCategory.getSubContactCategories()) {
            updateRbacEntries(sub, permissions, true);
        }
    }

    private EdsContactCategoryRbac getCategoryOrContactRbacForUser(List<EdsContactCategoryRbac> rbacList, Integer userOrGroupId, boolean isUser) {
        for (EdsContactCategoryRbac rbac : rbacList) {
            if (isUser && EdsTrusteeType.USER.equals(rbac.getTrusteeType()) && userOrGroupId.equals(rbac.getUser().getObjectID())) {
                return rbac;
            }
            if (!isUser && EdsTrusteeType.GROUP.equals(rbac.getTrusteeType()) && userOrGroupId.equals(rbac.getGroup().getObjectID())) {
                return rbac;
            }
        }
        return null;
    }

    private EdsContactPermission getPermission(PermissionHolder dto) {
        EdsContactPermission res = new EdsContactPermission();
        res.setRead(dto.isRead());
        res.setWrite(dto.isWrite());
        res.setDelete(dto.isDelete());
        res.setModifyACL(dto.isModifyACL());
        return res;
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<ContactCategoryListItem> getContactCategoriesWithPermissions() {
        ArrayList<ContactCategoryListItem> contactCategoryListItem = contactCategoryManager.getContactCategories();
        for (ContactCategoryListItem item : contactCategoryListItem) {
            setPermission(item);
        }
        return contactCategoryListItem;
    }

    private void setPermission(ContactCategoryListItem item) {
        List<EdsContactCategoryRbac> contactCategoryRbacs = contactCategoryRbacManager.getContactCategoryEntryForUser(item.getObjectID(), contactCategoryManager.getUser().getObjectID());
        if (contactCategoryRbacs != null && contactCategoryRbacs.size() > 0) {
            PermissionHolder p = new PermissionHolder();
            p.setWrite(contactCategoryRbacs.get(0).getContactPermission().isWrite());
            p.setDelete(contactCategoryRbacs.get(0).getContactPermission().isDelete());
            p.setModifyACL(contactCategoryRbacs.get(0).getContactPermission().isModifyACL());
            item.setPermission(p);
            if (item.getChildren() != null) {
                item.getChildren();
                for (ContactCategoryListItem subItem : item.getChildren()) {
                    if (subItem != null) {
                        setPermission(subItem);
                    }
                }
            }
        } else {
            if (item.getChildren() != null) {
                item.getChildren();
                for (ContactCategoryListItem subItem : item.getChildren()) {
                    if (subItem != null) {
                        setPermission(subItem);
                    }
                }
            }
        }
    }

    @Transactional
    public void createSystemContactCategories(String from) {
        Integer companyID = null;
        try {
            companyID = Integer.parseInt(ServerSecurityContext.getInstance().getCompanyId());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (companyID != null) {
            System.out.println("COMPANY_ID:>>>>>>>> : " + companyID);

            List<EdsEmployee> admins = employeeManager.getAdministrators();
            EdsUser companyCreator = null;
            if (admins != null && admins.size() > 0) {
                companyCreator = admins.get(0);
            }
            if (companyCreator != null) {
                //CRM category
                ContactCategoryListItem crmCategory = new ContactCategoryListItem();
                crmCategory.setType(EdsContactCategory.SYSTEM_BUILTIN);
                crmCategory.setName(crmLocalizer.localize(PdfLocalizationName.salesContacts));
                crmCategory.setDescription(commonLocalizer.localize(PdfLocalizationName.crmContact));
                crmCategory.setCategoryType(EdsContactCategory.CRM_CONTACT_CATEGORY);
                saveContactCategory(crmCategory, companyCreator, from);

                //Client category
                ContactCategoryListItem clientCategory = new ContactCategoryListItem();
                clientCategory.setType(EdsContactCategory.SYSTEM_BUILTIN);
                clientCategory.setName(crmLocalizer.localize(PdfLocalizationName.customerContacts));
                clientCategory.setDescription(commonLocalizer.localize(PdfLocalizationName.clientContact));
                clientCategory.setCategoryType(EdsContactCategory.CLIENT_CONTACT_CATEGORY);
                saveContactCategory(clientCategory, companyCreator, from);

                //Supplier category
                ContactCategoryListItem supplierCategory = new ContactCategoryListItem();
                supplierCategory.setType(EdsContactCategory.SYSTEM_BUILTIN);
                supplierCategory.setName(commonLocalizer.localize(PdfLocalizationName.supplierContact));
                supplierCategory.setDescription(commonLocalizer.localize(PdfLocalizationName.supplierContact));
                supplierCategory.setCategoryType(EdsContactCategory.SUPPLIER_CONTACT_CATEGORY);
                saveContactCategory(supplierCategory, companyCreator, from);

                //Employee category
                ContactCategoryListItem employeeCategory = new ContactCategoryListItem();
                employeeCategory.setType(EdsContactCategory.SYSTEM_BUILTIN);
                employeeCategory.setName(crmLocalizer.localize(PdfLocalizationName.employeeContact));
                employeeCategory.setDescription(crmLocalizer.localize(PdfLocalizationName.employeeContact));
                employeeCategory.setCategoryType(EdsContactCategory.EMPLOYEE_CONTACT_CATEGORY);
                saveContactCategory(employeeCategory, companyCreator, from);

                //Private category
                ContactCategoryListItem privateCategory = new ContactCategoryListItem();
                privateCategory.setType(EdsContactCategory.SYSTEM_BUILTIN);
                privateCategory.setName(crmLocalizer.localize(PdfLocalizationName.privateContacts));
                privateCategory.setDescription(crmLocalizer.localize(PdfLocalizationName.myPrivateContacts));
                privateCategory.setCategoryType(EdsContactCategory.PRIVATE_CONTACT_CATEGORY);
                saveContactCategory(privateCategory, companyCreator, from);

                //Private category
                ContactCategoryListItem leadCategory = new ContactCategoryListItem();
                leadCategory.setType(EdsContactCategory.SYSTEM_BUILTIN);
                leadCategory.setName(crmLocalizer.localize(PdfLocalizationName.leadContacts));
                leadCategory.setDescription(crmLocalizer.localize(PdfLocalizationName.leadContacts));
                leadCategory.setCategoryType(EdsContactCategory.LEAD_CONTACT_CATEGORY);
                leadCategory.setDoNotShow(true);
                saveContactCategory(leadCategory, companyCreator, from);
            }
        }
    }


    @Override
    @Transactional
    public ArrayList<Integer> changeCategory(Integer categoryId, ArrayList<Integer> iDs, int action) {
        if (iDs != null && !iDs.isEmpty()) {
            List<EdsCrmContact> contacts = crmContactManager.getSharedOrOwnedContactsByIDs(iDs);
            if (!contacts.isEmpty()) {
                EdsContactCategory category = contactCategoryManager.get(categoryId);
                if (category != null) {
                    for (EdsCrmContact contact : contacts) {
                        if (contact != null) {
                            if (action == ContactCategoryListItem.MOVE) {
                                contact.getCategories().clear();
                            }
                            contact.addCategories(category);
                        }
                    }
                }
                try {
                    contactSolrComponent.indexes(contacts);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            iDs.removeAll(EdsCrmContact.getObjectIDs(contacts));
        }
        return iDs;
    }

}