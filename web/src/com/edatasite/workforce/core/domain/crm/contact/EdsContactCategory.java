package com.edatasite.workforce.core.domain.crm.contact;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.documents.EdsAuditInfo;
import com.edatasite.workforce.core.domain.rbac.contact.EdsContactCategoryRbac;
import com.edatasite.workforce.gwt.contactcategory.client.rpc.ContactCategoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Version;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 13.05.2010
 * Time: 21:01:08
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "contactcategory")
public class EdsContactCategory extends EdsObject implements Constants {

    public final static int CUSTOM_CONTACT_CATEGORY = 0;
    public final static int CRM_CONTACT_CATEGORY = EdsCrmContact.CRM_CONTACT;
    public final static int CLIENT_CONTACT_CATEGORY = EdsCrmContact.CLIENT_CONTACT;
    public final static int SUPPLIER_CONTACT_CATEGORY = EdsCrmContact.SUPPLIER_CONTACT;
    public final static int EMPLOYEE_CONTACT_CATEGORY = EdsCrmContact.EMPLOYEE_CONTACT;
    public final static int LEAD_CONTACT_CATEGORY = EdsCrmContact.LEAD_CONTACT;
    public final static int CANDIDATE_CONTACT_CATEGORY = EdsCrmContact.CANDIDATE;
    public final static int PRIVATE_CONTACT_CATEGORY = 111;

    public final static String CRM_CONTACT_CATEGORY_CONSTANT_NAME = "CRM Contacts";
    public final static String CLIENT_CONTACT_CATEGORY_CONSTANT_NAME = "Client Contacts";
    public final static String SUPPLIER_CONTACT_CATEGORY_CONSTANT_NAME = "Supplier Contacts";
    public final static String EMPLOYEE_CONTACT_CATEGORY_CONSTANT_NAME = "Employee Contacts";
    public final static String PRIVATE_CONTACT_CATEGORY_CONSTANT_NAME = "My Private Contacts";
    public final static String LEAD_CONTACT_CATEGORY_CONSTANT_NAME = "Lead Contacts";
    public final static String CANDIDATE_CATEGORY_CONSTANT_NAME = "Candidates";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    /**
     * Version field for optimistic locking.
     */
    @SuppressWarnings("unused")
    @Version
    private int version;

    /**
     * The audit information.
     */
    @Embedded
    private EdsAuditInfo auditInfo;

    /**
     * The icon filename.
     */
    private String icon;

    /**
     * The folder name.
     */
    private String name;

    /**
     * The folder description.
     */
    private String description;

    /**
     * The folder constantname.
     */
    private String constantName;

    /**
     * The contacts in this category. A List so we can keep order.
     */
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "crmcontact_contactcategory",
            joinColumns = {@JoinColumn(name = "categories_id")},
            inverseJoinColumns = {@JoinColumn(name = "crmcontact_id")}
    )
    private List<EdsCrmContact> contacts = new ArrayList<>();

    /**
     * The subfolders in this folder. A List so we can keep order.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent", fetch = FetchType.LAZY)
    @OrderBy("name")
    private List<EdsContactCategory> subContactCategories = new ArrayList<>();

    /**
     * The parent folder of this one.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private EdsContactCategory parent;

    /**
     * The rbac in this folder. A List so we can keep order.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contactCategory", fetch = FetchType.LAZY)
    private List<EdsContactCategoryRbac> contactCategoryRbacs = new ArrayList<>();

    @Column(name = "foldertype")
    private int categoryType = CUSTOM_CONTACT_CATEGORY;

    private Integer entityId;

    /**
     * The owner of this folder.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private EdsUser owner;

    /**
     * Is this folder temporarily deleted?
     */
    @Column(columnDefinition = " boolean DEFAULT false")
    private boolean deleted = false;

    /**
     * Is this folder temporarily not shown?
     */
    @Column(columnDefinition = " boolean DEFAULT false")
    private boolean doNotShow = false;

    private int type = CUSTOM;

    /**
     * Retrieve the list of files in the folder.
     *
     * @return a list of file header objects
     */
    public List<EdsCrmContact> getContacts(boolean... childrensContactsAlso) {
        List<EdsCrmContact> contacts_ = new ArrayList<>();
        if (childrensContactsAlso != null && childrensContactsAlso.length > 0 && childrensContactsAlso[0]) {
            if (hasChildren()) {
                for (EdsContactCategory child : getSubContactCategories()) {
                    contacts_.addAll(child.getContacts(childrensContactsAlso));
                }
            }
            if (contacts != null) {
                contacts_.addAll(contacts);
            }
            return contacts_;
        }
        return contacts;
    }

    /**
     * Replace the parent folder.
     *
     * @param newParent the new parent
     */
    public void setParent(final EdsContactCategory newParent) {
        parent = newParent;
        if (parent != null) {
            setCategoryType(parent.getCategoryType());
        }
    }

    /**
     * Adds a crmContact to this folder. If the crmContact already belongs to another parent
     * folder, it is first removed from it.
     *
     * @param crmContact FileHeader to add
     * @throws IllegalArgumentException if crmContact is null
     */
    @Deprecated
    public void addCrmContact(final EdsCrmContact crmContact) {
        if (crmContact == null) {
            throw new IllegalArgumentException("Can't add a null crmContact.");
        }

        getContacts().add(crmContact);
        crmContact.addCategories(this);
    }

    /**
     * Removes a contact from this folder.
     *
     * @param contact FileHeader to remove
     * @throws IllegalArgumentException if contact is null
     */
    @Deprecated
    public void removeContact(final EdsCrmContact contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Can't remove a null contact.");
        }
        contact.removeCategory(this);
    }

    /**
     * Return the FolderDTO for this Folder object. The object graph that is
     * constructed has maximum depth 2. This method is mainly intended for use
     * by the web application interface.
     *
     * @return the FolderDTO that corresponds to this Folder
     */
    public ContactCategoryListItem getDTO() {
        return getDTO(1);
    }

    /**
     * Return the FolderDTO for this Folder object. The object graph that is
     * constructed has maximum depth 2. This method is mainly intended for use
     * by the web application interface.
     *
     * @return the FolderDTO that corresponds to this Folder
     */
    public ContactCategoryListItem getDTOUntilEnd(boolean onlyShared, List<Integer> sharedCategoryIDs) {
        if (isDeleted()) {
            return null;
        }
        ContactCategoryListItem f = getDTO(0);
        if (onlyShared && getType() != SYSTEM_BUILTIN && !sharedCategoryIDs.contains(getObjectID())) {
            f.setShared(false);
        } else if (onlyShared && sharedCategoryIDs.contains(getObjectID()) && getType() == SYSTEM_BUILTIN) {
            onlyShared = false;
        }
        for (EdsContactCategory subContactCategory : subContactCategories) {
            f.addChild(subContactCategory.getDTOUntilEnd(onlyShared, sharedCategoryIDs));
        }
        return f;
    }

    /**
     * Return the FolderDTO for this Folder object. The object graph that is
     * constructed has the specified maximum depth and contains marked as deleted folders
     *
     * @param depth the maximum depth of the returned folder tree
     * @return the FolderDTO that corresponds to this Folder
     */
    public ContactCategoryListItem getDTO(int depth) {
        if (isDeleted()) {
            return null;
        }
        ContactCategoryListItem f = new ContactCategoryListItem();
        f.setObjectID(objectID);
        f.setName(name);
        f.setDescription(description);
        f.setType(getType());
        f.setCategoryType(getCategoryType());
        if (getOwner() != null) {
            f.setOwner(getOwner().getDTO());
        }
        if (parent != null) {
            f.setParent(parent.getDTO(0));
        }
        for (EdsContactCategory subfolder : subContactCategories) {
            if (depth > 0) {
                f.addChild(subfolder.getDTO(depth - 1));
            }
        }
        return f;
    }

    /**
     * Modify the deleted.
     *
     * @param deleted the deleted to set
     */
    public void setDeleted(boolean deleted, boolean... childrenAlso) {
        this.deleted = deleted;
        if (childrenAlso != null && childrenAlso.length > 0 && childrenAlso[0] && getSubContactCategories() != null && getSubContactCategories().size() > 0) {
            for (EdsContactCategory child : getSubContactCategories()) {
                child.setDeleted(deleted, childrenAlso);
            }
        }
    }

    /**
     * Retrieve the full path of the folder, URL-encoded in the form:
     * /parent1/parent2/parent3/name
     *
     * @return the full path from the root of the files namespace
     */
    public String getPath() {
        if (parent == null) {
            return "/";
        }
        try {
            return parent.getPath() + URLEncoder.encode(name, "UTF-8") + '/';
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public SelectItem getAsSelectItem() {
        return new SelectItem(getObjectID(), getName(), getType() == SYSTEM_BUILTIN);
    }

    public static ArrayList<ContactCategoryListItem> getRPCsWithChildren(List<EdsContactCategory> contactCategories, List<EdsContactCategory> sharedCategories) {
        ArrayList<ContactCategoryListItem> items = new ArrayList<>();
        List<Integer> sharedCategoryIDs = EdsObject.getObjectIDs(sharedCategories);
        if (contactCategories != null && contactCategories.size() > 0) {
            for (EdsContactCategory contactCategory : contactCategories) {
                items.add(contactCategory.getDTOUntilEnd(true, sharedCategoryIDs));
            }
        }
        items.sort((o1, o2) -> o1 == null || o2 == null ? 0 : o1.getObjectID().compareTo(o2.getObjectID()));
        return items;
    }

    public static Map<Integer, EdsContactCategory> asMap(List<EdsContactCategory> categoryList) {
        Map<Integer, EdsContactCategory> map = new HashMap<>();
        if (categoryList != null && categoryList.size() > 0) {
            for (EdsContactCategory object : categoryList) {
                map.put(object.getObjectID(), object);
            }
        }
        return map;
    }

    public boolean hasChildren() {
        return getSubContactCategories() != null && getSubContactCategories().size() > 0;
    }

    public static List<EdsContactCategory> asList(boolean childrenAlso, EdsContactCategory... categories) {
        List<EdsContactCategory> result = new ArrayList<>();
        if (categories != null && categories.length > 0) {
            for (EdsContactCategory category : categories) {
                if (category != null) {
                    result.add(category);
                    if (childrenAlso && category.hasChildren()) {
                        result.addAll(asList(childrenAlso, category.getSubContactCategories().toArray(new EdsContactCategory[]{})));
                    }
                }
            }
        }
        return result;
    }

    public static List<String> getNames(boolean isCooOrAtm, Map<Integer, EdsContactCategory> categories, Integer... integers) {
        List<String> names = new ArrayList<>();
        if (integers != null && integers.length > 0) {
            for (Integer id : integers) {
                if (categories.containsKey(id)) {
                    EdsContactCategory category = categories.get(id);
                    if (category != null && (!isCooOrAtm || category.getType() != EdsContactCategory.SYSTEM_BUILTIN)) {
                        names.add(category.getName());
                    }
                }
            }
        }
        return names;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public EdsAuditInfo getAuditInfo() {
        return auditInfo;
    }

    public void setAuditInfo(EdsAuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConstantName() {
        return constantName;
    }

    public void setConstantName(String constantName) {
        this.constantName = constantName;
    }

    public List<EdsCrmContact> getContacts() {
        return contacts;
    }

    public void setContacts(List<EdsCrmContact> contacts) {
        this.contacts = contacts;
    }

    public List<EdsContactCategory> getSubContactCategories() {
        return subContactCategories;
    }

    public void setSubContactCategories(List<EdsContactCategory> subContactCategories) {
        this.subContactCategories = subContactCategories;
    }

    public EdsContactCategory getParent() {
        return parent;
    }

    public List<EdsContactCategoryRbac> getContactCategoryRbacs() {
        return contactCategoryRbacs;
    }

    public void setContactCategoryRbacs(List<EdsContactCategoryRbac> contactCategoryRbacs) {
        this.contactCategoryRbacs = contactCategoryRbacs;
    }

    public int getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(int categoryType) {
        this.categoryType = categoryType;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public EdsUser getOwner() {
        return owner;
    }

    public void setOwner(EdsUser owner) {
        this.owner = owner;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isDoNotShow() {
        return doNotShow;
    }

    public void setDoNotShow(boolean doNotShow) {
        this.doNotShow = doNotShow;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static List<String> getNames(boolean isCooOrAtm, Map<Integer, EdsContactCategory> categories, List<Integer> integers) {
        List<String> names = new ArrayList<>();
        if (integers != null && integers.size() > 0) {
            for (Integer id : integers) {
                if (categories.containsKey(id)) {
                    EdsContactCategory category = categories.get(id);
                    if (category != null && (!isCooOrAtm || category.getType() != EdsContactCategory.SYSTEM_BUILTIN)) {
                        names.add(category.getName());
                    }
                }
            }
        }
        return names;
    }
}
