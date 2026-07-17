package com.edatasite.workforce.core.domain.documents;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.rbac.EdsTrusteeType;
import com.edatasite.workforce.core.domain.rbac.documents.EdsFolderRbac;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.client.rpc.solr.SolrFolderRepresenter;
import org.apache.solr.common.SolrInputDocument;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Version;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 13.05.2010
 * Time: 21:01:08
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "folder")
public class EdsFolder extends EdsObject implements Constants {

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
     * The folder constantname.
     */
    private String constantName;

    /**
     * The files in this folder. A List so we can keep order.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "folder", fetch = FetchType.LAZY)
    @OrderBy("name")
    private List<EdsFileHeader> files = new ArrayList<>();

    /**
     * The subfolders in this folder. A List so we can keep order.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent", fetch = FetchType.LAZY)
    @OrderBy("name")
    private List<EdsFolder> subfolders = new ArrayList<>();

    /**
     * The parent folder of this one.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private EdsFolder parent;

    /**
     * The rbac in this folder. A List so we can keep order.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "folder", fetch = FetchType.LAZY)
    private List<EdsFolderRbac> folderRbacs = new ArrayList<>();

    private int folderType = F_DEFAULT;

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

    private int type = CUSTOM;

    @Column(columnDefinition = " boolean DEFAULT false")
    private boolean hasChild = false;


    public boolean isHasChild() {
        return hasChild;
    }

    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * Retrieve the icon.
     *
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Replace the icon.
     *
     * @param newIcon the new icon
     */
    public void setIcon(final String newIcon) {
        icon = newIcon;
    }

    /**
     * Retrieve the folder name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Modify the folder name.
     *
     * @param newName the new name
     */
    public void setName(final String newName) {
        name = newName;
    }

    public String getConstantName() {
        return constantName;
    }

    public void setConstantName(String constantName) {
        this.constantName = constantName;
    }

    /**
     * Retrieve the list of files in the folder.
     *
     * @return a list of file header objects
     */
    public List<EdsFileHeader> getFiles() {
        return files;
    }

    /**
     * Replace the list of files in the folder.
     *
     * @param newFiles the new list of files
     */
    public void setFiles(final List<EdsFileHeader> newFiles) {
        files = newFiles;
    }

    /**
     * Retrieve the list of subfolders.
     *
     * @return the subfolders
     */
    public List<EdsFolder> getSubfolders() {
        return subfolders;
    }

    /**
     * Replace the list of subfolders.
     *
     * @param newSubfolders the new subfolders
     */
    public void setSubfolders(final List<EdsFolder> newSubfolders) {
        subfolders = newSubfolders;
    }

    /**
     * Retrieve the parent folder.
     *
     * @return the parent
     */
    public EdsFolder getParent() {
        return parent;
    }

    /**
     * Replace the parent folder.
     *
     * @param newParent the new parent
     */
    public void setParent(final EdsFolder newParent) {
        parent = newParent;
    }

    /**
     * Retrieve the owner.
     *
     * @return the owner
     */
    public EdsUser getOwner() {
        return owner;
    }

    /**
     * Modify the owner.
     *
     * @param newOwner the new owner
     */
    public void setOwner(final EdsUser newOwner) {
        owner = newOwner;
    }

    /**
     * Retrieve the audit info.
     *
     * @return the audit info
     */
    public EdsAuditInfo getAuditInfo() {
        return auditInfo;
    }

    /**
     * Modify the audit info.
     *
     * @param newAuditInfo the new audit info
     */
    public void setAuditInfo(final EdsAuditInfo newAuditInfo) {
        auditInfo = newAuditInfo;
    }

    public int getFolderType() {
        return folderType;
    }

    public void setFolderType(int folderType) {
        this.folderType = folderType;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public List<EdsFolderRbac> getFolderRbacs() {
        return folderRbacs;
    }

    public void setFolderRbacs(List<EdsFolderRbac> folderRbacs) {
        this.folderRbacs = folderRbacs;
    }

    /**
     * Adds a subfolder to this folder. If the child already belongs to another
     * parent folder, it is first removed from it.
     *
     * @param subfolder Folder to add
     * @throws IllegalArgumentException if folder is null
     */
    public void addSubfolder(final EdsFolder subfolder) {
        if (subfolder == null) {
            throw new IllegalArgumentException("Can't add a null subfolder as a child.");
        }
        // Remove from old parent folder
        if (subfolder.getParent() != null) {
            subfolder.getParent().removeSubfolder(subfolder);
        }
        // Set parent in child
        subfolder.setParent(this);
        if (subfolder.getAuditInfo() != null) {
            subfolder.getAuditInfo().setModificationDate(new Date());
        }
        // Set child in parent
        getSubfolders().add(subfolder);
    }

    /**
     * Removes a subfolder from this folder.
     *
     * @param subfolder Folder to remove
     * @throws IllegalArgumentException if subfolder is null
     */
    public void removeSubfolder(final EdsFolder subfolder) {
        if (subfolder == null) {
            throw new IllegalArgumentException("Can't remove a null subfolder.");
        }
        getSubfolders().remove(subfolder);
        subfolder.setParent(null);
    }

    /**
     * Adds a file to this folder. If the file already belongs to another parent
     * folder, it is first removed from it.
     *
     * @param file FileHeader to add
     * @throws IllegalArgumentException if file is null
     */
    public void addFile(final EdsFileHeader file) {
        if (file == null) {
            throw new IllegalArgumentException("Can't add a null file.");
        }
        // Remove from old parent folder
        if (file.getFolder() != null) {
            file.getFolder().removeFile(file);
        }

        getFiles().add(file);
        file.setFolder(this);
    }

    /**
     * Removes a file from this folder.
     *
     * @param file FileHeader to remove
     * @throws IllegalArgumentException if file is null
     */
    public void removeFile(final EdsFileHeader file) {
        if (file == null) {
            throw new IllegalArgumentException("Can't remove a null file.");
        }
        getFiles().remove(file);
        file.setFolder(null);
    }

    /**
     * Return the FolderDTO for this Folder object. The object graph that is
     * constructed has maximum depth 2. This method is mainly intended for use
     * by the web application interface.
     *
     * @return the FolderDTO that corresponds to this Folder
     */
    public FolderResource getDTO() {
        return getDTO(0);
    }

    /**
     * Return the FolderDTO for this Folder object. The object graph that is
     * constructed has the specified maximum depth and contains marked as deleted folders
     *
     * @param depth the maximum depth of the returned folder tree
     * @return the FolderDTO that corresponds to this Folder
     */
    public FolderResource getDTO(final int depth) {
        FolderResource f = new FolderResource();
        f.setObjectId(objectID);
        f.setName(name);
        f.setPath(getPath());
        f.setOwner(owner.getDTO());
        f.setModificationDate(auditInfo.getModificationDate());
        f.setCreatedBy(auditInfo.getCreatedBy() != null ? auditInfo.getCreatedBy().getFullName() : null);
        f.setCreationDate(auditInfo.getCreationDate());
        f.setModifiedBy(auditInfo.getModifiedBy() != null ? auditInfo.getModifiedBy().getFullName() : null);
        f.setDeleted(isDeleted());
        f.setEntityId(getEntityId());
        f.setFileType(getFolderType());
        f.setHasChild(isHasChild());

        if (parent != null) {
            f.setParent(parent.getDTO(0));
        }
        if (depth > 0) {
            for (EdsFolder subfolder : subfolders) {
                if (depth > 0) {
                    f.getFolders().add(subfolder.getDTO(depth - 1));
                }
            }
        }
        return f;
    }

    public FileResource getFileDTO() {
        FileResource f = new FileResource();
        f.setObjectId(objectID);
        f.setName(name);
        f.setOwner(owner.getDTO());
        f.setModificationDate(auditInfo.getModificationDate());
        f.setCreatedBy(auditInfo.getCreatedBy().getFullName());
        f.setCreationDate(auditInfo.getCreationDate());
        f.setDeleted(isDeleted());
        f.setFolder(true);
        f.setPath(getPath());
        return f;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean newDeleted) {
        deleted = newDeleted;
    }

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

    public String getPathRaw() {
        if (parent == null) {
            return "/";
        }
        return parent.getPathRaw() + name + '/';
    }

    public Collection<SolrInputDocument> indexToSolr(List<EdsFolderRbac> folderRbacEntries, Integer companyId) {
        Map<String, SolrInputDocument> solrDocs = new HashMap<>();
        Map<String, Integer> ranks = new HashMap<>();
        for (EdsFolderRbac folderRbac : folderRbacEntries) {
            String compositID = "";
            String trusteeType = folderRbac.getTrusteeType() + "";

            if (EdsTrusteeType.USER.equals(folderRbac.getTrusteeType())) {
                compositID = companyId + "_" + getObjectID() + "_" + folderRbac.getUser().getObjectID() + "_" + folderRbac.getTrusteeType();
            } else if (EdsTrusteeType.GROUP.equals(folderRbac.getTrusteeType())) {
                compositID = companyId + "_" + getObjectID() + "_" + folderRbac.getGroup().getObjectID() + "_" + folderRbac.getTrusteeType();
            }
            SolrInputDocument doc = solrDocs.get(compositID);
            Integer currentRelationRank;
            if (doc == null) {
                currentRelationRank = folderRbac.getRelationRank();
                ranks.put(compositID, currentRelationRank);
                doc = new SolrInputDocument();
                solrDocs.put(compositID, doc);

                doc.addField(SolrFolderRepresenter.FIELD_COMPOSITE_ID, compositID);
                doc.addField(SolrFolderRepresenter.FIELD_COMPANY_ID, companyId);
                doc.addField(SolrFolderRepresenter.FIELD_RANK, currentRelationRank);
                doc.addField(SolrFolderRepresenter.FIELD_FOLDER_ID, getObjectID());
                doc.addField(SolrFolderRepresenter.FIELD_FOLDER_NAME, getName());
                doc.addField(SolrFolderRepresenter.FIELD_OWNER_ID, getOwner().getObjectID());
                doc.addField(SolrFolderRepresenter.FIELD_FOLDER_DESCRIPTION, "");

                if (EdsTrusteeType.USER.equals(folderRbac.getTrusteeType())) {
                    doc.addField(SolrFolderRepresenter.FIELD_USER_ID, folderRbac.getUser().getObjectID());
                } else if (EdsTrusteeType.GROUP.equals(folderRbac.getTrusteeType())) {
                    doc.addField(SolrFolderRepresenter.FIELD_GROUP_NAME, folderRbac.getGroup().getName());
                    doc.addField(SolrFolderRepresenter.FIELD_GROUP_ID, folderRbac.getGroup().getObjectID());
                }
                doc.addField(SolrFolderRepresenter.FIELD_TRUSTEE_TYPE, trusteeType);
                doc.addField(SolrFolderRepresenter.FIELD_DATE_CREATION, getAuditInfo().getCreationDate());
                doc.addField(SolrFolderRepresenter.FIELD_DATE_MODIFICATION, getAuditInfo().getModificationDate());
                doc.addField(SolrFolderRepresenter.FIELD_FOLDER_CONSTANT_NAME, getConstantName());
                doc.addField(SolrFolderRepresenter.FIELD_ENTITY_ID, getEntityId());
                doc.addField(SolrFolderRepresenter.FIELD_FOLDER_TYPE_ID, getFolderType());
                if (getParent() != null) {
                    doc.addField(SolrFolderRepresenter.FIELD_PARENT_ID, getParent().getObjectID());
                    doc.addField(SolrFolderRepresenter.FIELD_HAS_PARENT, true);
                } else {
                    doc.addField(SolrFolderRepresenter.FIELD_HAS_PARENT, false);
                }
                doc.addField(SolrFolderRepresenter.FIELD_DELETED, isDeleted());
                doc.addField(SolrFolderRepresenter.FIELD_CREATED_ID, getAuditInfo().getCreatedBy().getObjectID());
                doc.addField(SolrFolderRepresenter.FIELD_MODIFIED_ID, getAuditInfo().getModifiedBy().getObjectID());
                doc.addField(SolrFolderRepresenter.FIELD_IS_FILE, false);
                doc.addField(SolrFolderRepresenter.FIELD_TYPE, getType());
            } else {
                currentRelationRank = ranks.get(compositID);
            }

            if (currentRelationRank < folderRbac.getRelationRank()) {
                doc.setField(SolrFolderRepresenter.FIELD_RANK, currentRelationRank);
            }
            String[] permissions = ServerUtils.getPermissions(folderRbac.getDocumentPermission());
            for (String perm : permissions) {
                doc.addField(SolrFolderRepresenter.FIELD_PERMISSIONS, perm);
            }
            doc.addField(SolrFolderRepresenter.FIELD_RELATIONSHIPS, folderRbac.getRelationship());
        }
        return solrDocs.values();
    }
}
