package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.accounting.EdsManualJournalItem;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.customfields.EdsProjectCustomFields;
import com.edatasite.workforce.core.domain.customform.EdsModelField;
import com.edatasite.workforce.core.domain.customform.EdsProjectCustomItemTable;
import com.edatasite.workforce.core.domain.issue.EdsIssue;
import com.edatasite.workforce.core.domain.lucene.Indexable;
import com.edatasite.workforce.core.domain.workflow.EdsTraceable;
import com.edatasite.workforce.gwt.core.client.enums.EmployeeAssignmentEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrProjectListRepresenter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.domain.ObjectHistory;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrRelationUtils;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectListItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectSolrItem;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.solr.common.SolrInputDocument;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Slizer3D
 * Date: 22.03.2007
 * Time: 15:11:42
 * Software Team
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "project",
        indexes = {
                @Index(columnList = "clientId", name = "project_clientId_idx")
        })
public class EdsProject extends EdsTraceable implements PropertyChangeListener, ObjectHistory, Indexable/*, Traceable*/ {

    public static final String PROJECT_STATUS = "_PROJECT_STATUS";
    public static final String ONGOING = "ONGOING";
    public static final String NOT_STARTED = "PS_NOT_STARTED";
    public static final String COMPLETED = "PS_COMPLETED";
    public static final String CLOSED = "PS_CLOSED";
    public static final String ALL = "ALL";
    public static final String APPROVED_BY_CLIENT = "APPROVED_BY_CLIENT";
    public static final String REJECTED_BY_CLIENT = "REJECTED_BY_CLIENT";

    public static final String FIELD_MANAGER = "manager";
    public static final String FIELD_BACKUP_MANAGER = "backup_manager";
    public static final String FIELD_CLIENT = "client";
    public static final String FIELD_NAME = "name";
    private transient PropertyChangeSupport propertyChangeSupport;
    private transient ChangeListener changeListener;
    private transient boolean solrSensitiveFieldsChanged = false;
    private transient List<Integer> backupManagersBeforeEdit;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parnetId")
    private EdsProject parent;
    //
    @Column(name = "projectSource")
    private String projectSource; //project source -> copied from project, insert MSProject, convert from opportunity, and etc.
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "managerid")
    private EdsEmployee manager;
    @Column(insertable = false, updatable = false)
    private Integer managerid;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "backup_ManagerId")
    private EdsEmployee backupManager;
    @Column(insertable = false, updatable = false)
    private Integer backup_ManagerId;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "backup_ManagerId2")
    private EdsEmployee backupManager2;
    @Column(insertable = false, updatable = false)
    private Integer backup_ManagerId2;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "backup_ManagerId3")
    private EdsEmployee backupManager3;
    @Column(insertable = false, updatable = false)
    private Integer backup_ManagerId3;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "backup_ManagerId4")
    private EdsEmployee backupManager4;
    @Column(insertable = false, updatable = false)
    private Integer backup_ManagerId4;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "backup_ManagerId5")
    private EdsEmployee backupManager5;
    @Column(insertable = false, updatable = false)
    private Integer backup_ManagerId5;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "backup_ManagerId6")
    private EdsEmployee backupManager6;
    @Column(insertable = false, updatable = false)
    private Integer backup_ManagerId6;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "backup_ManagerId7")
    private EdsEmployee backupManager7;
    @Column(insertable = false, updatable = false)
    private Integer backup_ManagerId7;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "backup_ManagerId8")
    private EdsEmployee backupManager8;
    @Column(insertable = false, updatable = false)
    private Integer backup_ManagerId8;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "backup_ManagerId9")
    private EdsEmployee backupManager9;
    @Column(insertable = false, updatable = false)
    private Integer backup_ManagerId9;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "backup_ManagerId10")
    private EdsEmployee backupManager10;
    @Column(insertable = false, updatable = false)
    private Integer backup_ManagerId10;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clientId")
    private EdsCrmAccount client;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "project_clients",
            joinColumns = {@JoinColumn(name = "projectid")},
            inverseJoinColumns = {@JoinColumn(name = "clientid")}
    )
    private Set<EdsCrmAccount> clients = new HashSet<>();
    @Column(insertable = false, updatable = false)
    private Integer clientId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statusid", nullable = false)
    private EdsReference status;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    @Type(type = "text")
    private String description;
    @Column(name = "startDate")
    private Date startDate;
    @Column(name = "endDate")
    private Date endDate;
    @Column(name = "dueDate")
    private Date dueDate;
    @Column(name = "percent")
    private Float percent;
    @Column(name = "timespent")
    private Integer timespent;
    private Date completedDate;
    @Transient
    private Set<EdsIssue> issues = new HashSet<>();
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "projectid")
    private Set<EdsTask> tasks = new HashSet<>();
    @Transient
    private String timeSpentHM;
    @Column(name = "indexed")
    private Boolean indexed;
    @Column(name = "isDeleted")
    private Boolean deleted = false;
    @Transient
    private Integer teamsCount;
    @Column(name = "changed_On")
    private Date changedOn;
    @Column(name = "lastInvocedDate")
    private Date lastInvoicedDate;
    @Column(name = "lastExpenseClaimedDate")
    private Date lastExpenseClaimedDate;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "projectLocationId")
    private EdsLocation projectLocation;
    @Column(name = "number")
    private String number;
    @Column(name = "savedNumberFormula")
    private String savedNumberFormula;
    @Column(name = "intNumber")
    private Integer intNumber;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectcustomfieldsid", unique = true)
    private EdsProjectCustomFields projectCustomFields;
    @Column(columnDefinition = " boolean DEFAULT false")
    private boolean crmActivityProject = false;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "project")
    private final List<EdsManualJournalItem> journalItems = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "project")
    private List<EdsProjectPosition> projectPositions = new ArrayList<>();
    private Boolean calculationCompleted = false;
    private Double planedWageAmount = 0.0;
    private Double planedClientChargeAmount = 0.0;
    private Double planedIncomeAmount = 0.0;
    private Double planedExpensesAmount = 0.0;
    private Double actualWageAmount = 0.0;
    private Double actualClientChargeAmount = 0.0;
    private Double expensesAmount = 0.0;
    private Double incomeAmount = 0.0;
    @Column(columnDefinition = " boolean default true")
    private Boolean billable = true;
    @Enumerated(value = EnumType.STRING)
    private EmployeeAssignmentEnum employeeAssignment;
    @OrderBy("sorder asc")
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "project")
    private Set<EdsBillOfMaterial> billOfMaterials = new HashSet<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billOfMaterialStatusID")
    private EdsReference billOfMaterialStatus;
    @Type(type = "text")
    @Column(name = "rejectionReason")
    private String rejectionReason; //bill of materials rejection reason
    @Column(name = "contractId")
    private Integer contractId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorid")
    private EdsUser creator;
    @Column(name = "creationTime")
    private Date creationTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updaterid")
    private EdsUser updater;
    @Column(name = "lastUpdateTime")
    private Date lastUpdateTime;

    @OneToMany(mappedBy = "project", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<EdsProjectCustomItemTable> itemTables = new HashSet<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<EdsCheckInLocation> checkInLocation = new HashSet<>();


    public void setBackupManagersBeforeEdit(List<Integer> backupManagersBeforeEdit) {
        this.backupManagersBeforeEdit = backupManagersBeforeEdit;
    }

    public boolean isSolrSensitiveFieldsChanged() {
        return solrSensitiveFieldsChanged;
    }

    public void enableTaskChangeListener(ChangeListener changeListener) {
        if (changeListener == null) {
            throw new IllegalArgumentException("Listener must not be null");
        }
        this.changeListener = changeListener;
        propertyChangeSupport = new PropertyChangeSupport(this);
        propertyChangeSupport.addPropertyChangeListener(this);
    }

    public void disableTaskChangeListener() {
        this.changeListener = null;
        propertyChangeSupport.removePropertyChangeListener(this);
        this.propertyChangeSupport = null;
    }

    public Set<EdsProjectCustomItemTable> getItemTables() {
        return itemTables;
    }

    public void setItemTables(Set<EdsProjectCustomItemTable> itemTables) {
        this.itemTables = itemTables;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(FIELD_MANAGER)) {
            changeListener.onManagerChange((EdsEmployee) evt.getNewValue());
        }

        if (evt.getPropertyName().equals(FIELD_BACKUP_MANAGER)) {
            changeListener.onBackupManagerChange((EdsEmployee) evt.getNewValue());
        }
        if (evt.getPropertyName().equals(FIELD_CLIENT)) {
            changeListener.onClientChange((EdsCrmAccount) evt.getNewValue());
        }
        if (evt.getPropertyName().equals(FIELD_NAME)) {
            changeListener.onNameChange((String) evt.getNewValue());
        }
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public boolean isCrmActivityProject() {
        return crmActivityProject;
    }

    public void setCrmActivityProject(boolean crmActivityProject) {
        this.crmActivityProject = crmActivityProject;
    }

    public EdsCompany getCompany() {
        return getManager().getCompany();
    }

    public EdsEmployee getManager() {
        return manager;
    }

    public void setManager(EdsEmployee manager) {
        if (!ServerUtils.equalsEdsObject(this.manager, manager)) {
            solrSensitiveFieldsChanged = true;
            addChange(CustomFormConstants.MANAGER);
        }
        EdsEmployee oldValue = this.manager;
        this.manager = manager;
        if (propertyChangeSupport != null) {
            propertyChangeSupport.firePropertyChange(FIELD_MANAGER, oldValue, this.manager);
        }
    }

    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public Integer getManagerid() {
        return managerid;
    }

    public void setManagerid(Integer managerid) {
        this.managerid = managerid;
    }

    public EdsProject getParent() {
        return parent;
    }

    public void setParent(EdsProject parent) {
        this.parent = parent;
    }

    public String getProjectSource() {
        return projectSource;
    }

    public void setProjectSource(String projectSource) {
        this.projectSource = projectSource;
    }

    public Set<EdsBillOfMaterial> getBillOfMaterials() {
        if (billOfMaterials == null) {
            billOfMaterials = new HashSet<>();
        }
        return billOfMaterials;
    }

    public void setBillOfMaterials(Set<EdsBillOfMaterial> billOfMaterials) {
        this.billOfMaterials = billOfMaterials;
    }

    public EdsReference getBillOfMaterialStatus() {
        return billOfMaterialStatus;
    }

    public void setBillOfMaterialStatus(EdsReference billOfMaterialStatus) {
        this.billOfMaterialStatus = billOfMaterialStatus;
    }

    public EdsEmployee getBackupManager() {
        return backupManager;
    }

    public void setBackupManager(EdsEmployee backupManager) {
        if (!ServerUtils.equalsEdsObject(this.backupManager, backupManager)) {
            addChange(CustomFormConstants.PROJECT.BACKUP_MANAGER);
            solrSensitiveFieldsChanged = true;
        }
        EdsEmployee oldValue = this.backupManager;
        boolean isAlreadyBackupManager = backupManagersBeforeEdit != null && backupManagersBeforeEdit.contains(backupManager.getObjectID());
        this.backupManager = backupManager;
        if (propertyChangeSupport != null && !isAlreadyBackupManager) {
            propertyChangeSupport.firePropertyChange(FIELD_BACKUP_MANAGER, oldValue, this.backupManager);
        }
    }

    public EdsEmployee getBackupManager2() {
        return backupManager2;
    }

    public void setBackupManager2(EdsEmployee backupManager2) {
        EdsEmployee oldValue = this.backupManager2;
        boolean isAlreadyBackupManager = backupManagersBeforeEdit != null && backupManagersBeforeEdit.contains(backupManager2.getObjectID());
        this.backupManager2 = backupManager2;
        if (propertyChangeSupport != null && !isAlreadyBackupManager) {
            propertyChangeSupport.firePropertyChange(FIELD_BACKUP_MANAGER, oldValue, this.backupManager2);
        }
    }

    public EdsEmployee getBackupManager3() {
        return backupManager3;
    }

    public void setBackupManager3(EdsEmployee backupManager3) {
        EdsEmployee oldValue = this.backupManager3;
        boolean isAlreadyBackupManager = backupManagersBeforeEdit != null && backupManagersBeforeEdit.contains(backupManager3.getObjectID());
        this.backupManager3 = backupManager3;
        if (propertyChangeSupport != null && !isAlreadyBackupManager) {
            propertyChangeSupport.firePropertyChange(FIELD_BACKUP_MANAGER, oldValue, this.backupManager3);
        }
    }

    public EdsEmployee getBackupManager4() {
        return backupManager4;
    }

    public void setBackupManager4(EdsEmployee backupManager4) {
        EdsEmployee oldValue = this.backupManager4;
        boolean isAlreadyBackupManager = backupManagersBeforeEdit != null && backupManagersBeforeEdit.contains(backupManager4.getObjectID());
        this.backupManager4 = backupManager4;
        if (propertyChangeSupport != null && !isAlreadyBackupManager) {
            propertyChangeSupport.firePropertyChange(FIELD_BACKUP_MANAGER, oldValue, this.backupManager4);
        }
    }

    public EdsEmployee getBackupManager5() {
        return backupManager5;
    }

    public void setBackupManager5(EdsEmployee backupManager5) {
        EdsEmployee oldValue = this.backupManager5;
        boolean isAlreadyBackupManager = backupManagersBeforeEdit != null && backupManagersBeforeEdit.contains(backupManager5.getObjectID());
        this.backupManager5 = backupManager5;
        if (propertyChangeSupport != null && !isAlreadyBackupManager) {
            propertyChangeSupport.firePropertyChange(FIELD_BACKUP_MANAGER, oldValue, this.backupManager5);
        }
    }

    public EdsEmployee getBackupManager6() {
        return backupManager6;
    }

    public void setBackupManager6(EdsEmployee backupManager6) {
        EdsEmployee oldValue = this.backupManager6;
        boolean isAlreadyBackupManager = backupManagersBeforeEdit != null && backupManagersBeforeEdit.contains(backupManager6.getObjectID());
        this.backupManager6 = backupManager6;
        if (propertyChangeSupport != null && !isAlreadyBackupManager) {
            propertyChangeSupport.firePropertyChange(FIELD_BACKUP_MANAGER, oldValue, this.backupManager6);
        }
    }

    public EdsEmployee getBackupManager7() {
        return backupManager7;
    }

    public void setBackupManager7(EdsEmployee backupManager7) {
        EdsEmployee oldValue = this.backupManager7;
        boolean isAlreadyBackupManager = backupManagersBeforeEdit != null && backupManagersBeforeEdit.contains(backupManager7.getObjectID());
        this.backupManager7 = backupManager7;
        if (propertyChangeSupport != null && !isAlreadyBackupManager) {
            propertyChangeSupport.firePropertyChange(FIELD_BACKUP_MANAGER, oldValue, this.backupManager7);
        }
    }

    public EdsEmployee getBackupManager8() {
        return backupManager8;
    }

    public void setBackupManager8(EdsEmployee backupManager8) {
        EdsEmployee oldValue = this.backupManager8;
        boolean isAlreadyBackupManager = backupManagersBeforeEdit != null && backupManagersBeforeEdit.contains(backupManager8.getObjectID());
        this.backupManager8 = backupManager8;
        if (propertyChangeSupport != null && !isAlreadyBackupManager) {
            propertyChangeSupport.firePropertyChange(FIELD_BACKUP_MANAGER, oldValue, this.backupManager8);
        }
    }

    public EdsEmployee getBackupManager9() {
        return backupManager9;
    }

    public void setBackupManager9(EdsEmployee backupManager9) {
        EdsEmployee oldValue = this.backupManager9;
        boolean isAlreadyBackupManager = backupManagersBeforeEdit != null && backupManagersBeforeEdit.contains(backupManager9.getObjectID());
        this.backupManager9 = backupManager9;
        if (propertyChangeSupport != null && !isAlreadyBackupManager) {
            propertyChangeSupport.firePropertyChange(FIELD_BACKUP_MANAGER, oldValue, this.backupManager9);
        }
    }

    public EdsEmployee getBackupManager10() {
        return backupManager10;
    }

    public void setBackupManager10(EdsEmployee backupManager10) {
        EdsEmployee oldValue = this.backupManager10;
        boolean isAlreadyBackupManager = backupManagersBeforeEdit != null && backupManagersBeforeEdit.contains(backupManager10.getObjectID());
        this.backupManager10 = backupManager10;
        if (propertyChangeSupport != null && !isAlreadyBackupManager) {
            propertyChangeSupport.firePropertyChange(FIELD_BACKUP_MANAGER, oldValue, this.backupManager10);
        }
    }

    public List<EdsEmployee> getBackupManagers() {
        List<EdsEmployee> backupManagers = new ArrayList<>();
        if (backupManager != null) {
            backupManagers.add(backupManager);
        }
        if (backupManager2 != null) {
            backupManagers.add(backupManager2);
        }
        if (backupManager3 != null) {
            backupManagers.add(backupManager3);
        }
        if (backupManager4 != null) {
            backupManagers.add(backupManager4);
        }
        if (backupManager5 != null) {
            backupManagers.add(backupManager5);
        }
        if (backupManager6 != null) {
            backupManagers.add(backupManager6);
        }
        if (backupManager7 != null) {
            backupManagers.add(backupManager7);
        }
        if (backupManager8 != null) {
            backupManagers.add(backupManager8);
        }
        if (backupManager9 != null) {
            backupManagers.add(backupManager9);
        }
        if (backupManager10 != null) {
            backupManagers.add(backupManager10);
        }
        return backupManagers;
    }

    public List<Integer> getManagerIDs() {
        List<Integer> managers = new ArrayList<>();
        if (manager != null) {
            managers.add(manager.getObjectID());
        }
        managers.addAll(getBackupManagerIDs());
        return managers;
    }

    public ArrayList<Integer> getBackupManagerIDs() {
        ArrayList<Integer> backupManagers = new ArrayList<>();
        if (backupManager != null) {
            backupManagers.add(backupManager.getObjectID());
        }
        if (backupManager2 != null) {
            backupManagers.add(backupManager2.getObjectID());
        }
        if (backupManager3 != null) {
            backupManagers.add(backupManager3.getObjectID());
        }
        if (backupManager4 != null) {
            backupManagers.add(backupManager4.getObjectID());
        }
        if (backupManager5 != null) {
            backupManagers.add(backupManager5.getObjectID());
        }
        if (backupManager6 != null) {
            backupManagers.add(backupManager6.getObjectID());
        }
        if (backupManager7 != null) {
            backupManagers.add(backupManager7.getObjectID());
        }
        if (backupManager8 != null) {
            backupManagers.add(backupManager8.getObjectID());
        }
        if (backupManager9 != null) {
            backupManagers.add(backupManager9.getObjectID());
        }
        if (backupManager10 != null) {
            backupManagers.add(backupManager10.getObjectID());
        }
        return backupManagers;
    }

    public void clearProjectManagers(Integer backupManagersCount) {
        switch (backupManagersCount) {
            case 0:
                backupManager = null;
            case 1:
                backupManager2 = null;
            case 2:
                backupManager3 = null;
            case 3:
                backupManager4 = null;
            case 4:
                backupManager5 = null;
            case 5:
                backupManager6 = null;
            case 6:
                backupManager7 = null;
            case 7:
                backupManager8 = null;
            case 8:
                backupManager9 = null;
            case 9:
                backupManager10 = null;
                break;
        }
    }

    public boolean isUserBackupManager(Integer userID) {
        boolean isBackupManager = false;
        isBackupManager = (backupManager != null && userID.equals(backupManager.getObjectID()))
                || (backupManager2 != null && userID.equals(backupManager2.getObjectID()))
                || (backupManager3 != null && userID.equals(backupManager3.getObjectID()))
                || (backupManager4 != null && userID.equals(backupManager4.getObjectID()))
                || (backupManager5 != null && userID.equals(backupManager5.getObjectID()))
                || (backupManager6 != null && userID.equals(backupManager6.getObjectID()))
                || (backupManager7 != null && userID.equals(backupManager7.getObjectID()))
                || (backupManager8 != null && userID.equals(backupManager8.getObjectID()))
                || (backupManager9 != null && userID.equals(backupManager9.getObjectID()))
                || (backupManager10 != null && userID.equals(backupManager10.getObjectID()));
        return isBackupManager;
    }

    public void replaceBackupManager(EdsEmployee oldBackupManager, EdsEmployee newBackupManager) {
        if (backupManager != null && oldBackupManager.getObjectID().equals(backupManager.getObjectID())) {
            setBackupManager(newBackupManager);
        } else if (backupManager2 != null && oldBackupManager.getObjectID().equals(backupManager2.getObjectID())) {
            setBackupManager2(newBackupManager);
        } else if (backupManager3 != null && oldBackupManager.getObjectID().equals(backupManager3.getObjectID())) {
            setBackupManager3(newBackupManager);
        } else if (backupManager4 != null && oldBackupManager.getObjectID().equals(backupManager4.getObjectID())) {
            setBackupManager4(newBackupManager);
        } else if (backupManager5 != null && oldBackupManager.getObjectID().equals(backupManager5.getObjectID())) {
            setBackupManager5(newBackupManager);
        } else if (backupManager6 != null && oldBackupManager.getObjectID().equals(backupManager6.getObjectID())) {
            setBackupManager6(newBackupManager);
        } else if (backupManager7 != null && oldBackupManager.getObjectID().equals(backupManager7.getObjectID())) {
            setBackupManager7(newBackupManager);
        } else if (backupManager8 != null && oldBackupManager.getObjectID().equals(backupManager8.getObjectID())) {
            setBackupManager8(newBackupManager);
        } else if (backupManager9 != null && oldBackupManager.getObjectID().equals(backupManager9.getObjectID())) {
            setBackupManager9(newBackupManager);
        } else if (backupManager10 != null && oldBackupManager.getObjectID().equals(backupManager10.getObjectID())) {
            setBackupManager10(newBackupManager);
        }
    }

    public EdsCrmAccount getClient() {
        return client;
    }

    public void setClient(EdsCrmAccount client) {
        if (!ServerUtils.equalsEdsObject(this.client, client)) {
            addChange(CustomFormConstants.PROJECT.CLIENT);
            solrSensitiveFieldsChanged = true;
        }
        EdsCrmAccount oldValue = this.client;
        this.client = client;
        if (propertyChangeSupport != null) {
            propertyChangeSupport.firePropertyChange(FIELD_CLIENT, oldValue, this.client);
        }
    }

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        if (!ServerUtils.equalsReference(this.status, status)) {
            addChange(CustomFormConstants.STATUS);
        }
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (!ServerUtils.equalsString(this.name, name)) {
            solrSensitiveFieldsChanged = true;
            addChange(CustomFormConstants.NAME);
        }
        String oldValue = this.name;
        this.name = name;
        if (propertyChangeSupport != null) {
            propertyChangeSupport.firePropertyChange(FIELD_NAME, oldValue, this.name);
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (!ServerUtils.equalsString(this.description, description)) {
            addChange(CustomFormConstants.DESCRIPTION);
        }
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        if (!ServerUtils.equalsDate(this.startDate, startDate)) {
            addChange(CustomFormConstants.START_DATE);
        }
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        if (!ServerUtils.equalsDate(this.endDate, endDate)) {
            addChange(CustomFormConstants.END_DATE);
        }
        this.endDate = endDate;
    }

    public Set<EdsIssue> getIssues() {
        return issues;
    }

    public void setIssues(Set<EdsIssue> incidents) {
        this.issues = incidents;
    }

    public Integer getTimespent() {
        return timespent;
    }

    public void setTimespent(Integer timespent) {
        this.timespent = timespent;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    public String getTimeSpentHM() {
        if (timespent == null) {
            return timeSpentHM = "00:00";
        }
        timeSpentHM = "";
        if (timespent / 60 < 9) {
            timeSpentHM = "0";
        }
        timeSpentHM = timeSpentHM + timespent / 60;
        timeSpentHM = timeSpentHM + ":";
        if (timespent % 60 < 9) {
            timeSpentHM = timeSpentHM + "0";
        }
        timeSpentHM = timeSpentHM + timespent % 60;
        return timeSpentHM;
    }

    public void setTimeSpentHM(String timeSpentHM) {
        this.timeSpentHM = timeSpentHM;
    }

    public Boolean getIndexed() {
        return indexed;
    }

    public void setIndexed(Boolean indexed) {
        this.indexed = indexed;
    }

    public Boolean getDeleted() {
        return deleted == null ? Boolean.FALSE : deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Date getChangedOn() {
        return changedOn;
    }

    public void setChangedOn(Date changedOn) {
        this.changedOn = changedOn;
    }

    public Set<EdsTask> getTasks() {
        return tasks;
    }

    public void setTasks(Set<EdsTask> tasks) {
        this.tasks = tasks;
    }

    public Set<EdsTask> getUndeletedTasks() {
        Set<EdsTask> projectTasks = new HashSet<>();
        for (EdsTask projectTask : getTasks()) {
            if ((projectTask.getDeleted() == null || !projectTask.getDeleted()) && (projectTask.getIssue() == null || !projectTask.getIssue())) {
                projectTasks.add(projectTask);
            }
        }
        return projectTasks;
    }

    public Float getProjectTasksAveragePercentCompleted() {
        Float average = 0f;
        int count = 0;
        for (EdsTask tasks : getUndeletedTasks()) {
            count++;
            average += tasks.getPercent() != null ? tasks.getPercent() : 0;
        }
        return count > 0 ? (average / count) : 0;
    }

    public Float getProjectTasksAveragePercentCompletedNewLogic1(Float timespent, Float estimatedtime) {
        return estimatedtime != null && estimatedtime != 0 && timespent != null ? timespent * 100 / estimatedtime : 0;
    }

    public Float getProjectTasksAveragePercentCompletedNewLogic() {
        Float estimatedtime = 0f;
        Float timespent = 0f;

        for (EdsTask task : getUndeletedTasks()) {
            for (EdsEmployeeTask employeeTask : task.getUnDeletedAssignments()) {
                estimatedtime += employeeTask.getEstimatedTime() != null ? employeeTask.getEstimatedTime() : 0;
                timespent += employeeTask.getTimeSpent() != null ? employeeTask.getTimeSpent() : 0;
            }
        }
        return estimatedtime != 0 && estimatedtime != null ? timespent * 100 / estimatedtime : 0;//count > 0 ? (average / count) : 0;
    }

    public Float getPercent() {
        return percent != null ? new BigDecimal(percent).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue() : null;
    }

    public void setPercent(Float percent) {
        this.percent = percent;
    }

    //User log related

    public ProjectListItem createProjectListItem() {
        ProjectListItem result = new ProjectListItem();
        result.setObjectId(getObjectID());
        result.setNumber(getNumber());
        result.setName(getName());
        result.setDescription(getDescription());
        result.setManager(getManager() != null ? getManager().getName() : "N/A");
        result.setClient(getClient() != null && !getClient().isDeleted() ? getClient().getName() : "N/A");
        result.setActualHoursSpent(getTimeSpentHM() != null ? getTimeSpentHM() : "00:00");
        result.setStatus(getStatus() != null ? getStatus().getName() : "N/A");
        result.setComplete(getPercent() != null ? new BigDecimal(getPercent()).setScale(2, BigDecimal.ROUND_HALF_UP).toString() : "0.0");
        result.setStartDate(getStartDate());
        result.setEndDate(getEndDate());
        result.setDueDate(getDueDate());
        result.setLastUpdate(getLastUpdateTime());
        return result;
    }

    public ProjectSolrItem getSolrRPC() {
        ProjectSolrItem result = new ProjectSolrItem();

        SelectItem project = new SelectItem(getObjectID(), getName());
        project.setNumber(String.valueOf(getNumber()));
        result.setProject(project);
        result.setDescription(getDescription());
        result.setProjectTasksAveragePercentCompleted(getProjectTasksAveragePercentCompleted());
        result.setHourSpent(getTimeSpentHM());
        result.setStartDate(getStartDate());
        result.setDueDate(getDueDate());
        result.setEndDate(getEndDate());
        result.setLastUpdate(getLastUpdateTime());
        result.setProjectCreatedDate(getCreationDate());
        result.setProjectModifiedDate(getLastUpdateTime());
        if (getParent() != null) {
            result.setParentId(getParent().getObjectID());
        }
        if (getCreator() != null) {
            result.setCreatedBy(getCreator().getAsSelectItem());
        }
        if (getUpdater() != null) {
            result.setProjectModifiedBy(getUpdater().getName());
        }

        if (getStatus() != null) {
            result.setStatus(getStatus().getRPC());
        }
        if (getManager() != null) {
            result.setManager(getManager().getAsSelectItem());
        }
        if (getClient() != null) {
            result.setClient(getClient().getAsSelectItem());
        }

        StringBuilder clientNames = new StringBuilder("");
        if (getClients() != null) {
            getClients().forEach(edsCrmAccount -> {
                result.getProjectMultiClient().add(edsCrmAccount.getAsSelectItem());

                if ("".equals(clientNames.toString())) {
                    clientNames.append(edsCrmAccount.getName() != null ? edsCrmAccount.getName() : "");
                }
            });
        }
        result.setClientNameSort(clientNames.toString());
        if (getBackupManagers() != null) {
            getBackupManagers().forEach(edsEmployee -> {
                result.getBackupManager().add(edsEmployee.getAsSelectItem());
            });
        }
        if (getProjectLocation() != null) {
            result.setLocation(getProjectLocation().getAsSelectItem());
        }

        result.getUser().add(getManager().getAsSelectItem());
        result.setActualWageAmount(getActualWageAmount());
        result.setActualClientChargeAmount(getActualClientChargeAmount());
        result.setExpensesAmount(getExpensesAmount());
        result.setIncomeAmount(getIncomeAmount());

        result.setPlanedWageAmount(getPlanedWageAmount());
        result.setPlanedClientChargeAmount(getPlanedClientChargeAmount());
        result.setPlanedExpensesAmount(getPlanedExpensesAmount());
        result.setPlanedIncomeAmount(getPlanedIncomeAmount());
        result.setBillable(getBillable());

        return result;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        if (!ServerUtils.equalsDate(this.dueDate, dueDate)) {
            addChange(CustomFormConstants.DUE_DATE);
        }
        this.dueDate = dueDate;
    }

    public EdsUser getCreator() {
        return creator;
    }

    public void setCreator(EdsUser creator) {
        this.creator = creator;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public EdsUser getUpdater() {
        return updater;
    }

    public void setUpdater(EdsUser updater) {
        this.updater = updater;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Boolean hasAccess(EdsUser user) {
        return user.getCompany().getObjectID().equals(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()));
    }

    public Integer getTeamsCount() {
        return teamsCount;
    }

    public void setTeamsCount(Integer teamsCount) {
        this.teamsCount = teamsCount;
    }

    public Date getLastInvoicedDate() {
        return lastInvoicedDate;
    }

    public void setLastInvoicedDate(Date lastInvoicedDate) {
        this.lastInvoicedDate = lastInvoicedDate;
    }

    public EdsLocation getProjectLocation() {
        return projectLocation;
    }

    public void setProjectLocation(EdsLocation projectLocation) {
        if (!ServerUtils.equalsEdsObject(this.projectLocation, projectLocation)) {
            addChange(CustomFormConstants.LOCATION_FIELD);
        }
        this.projectLocation = projectLocation;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        if (!ServerUtils.equalsString(this.number, number)) {
            addChange(CustomFormConstants.NUMBER);
        }
        this.number = number;
    }

    public String getSavedNumberFormula() {
        return savedNumberFormula;
    }

    public void setSavedNumberFormula(String savedNumberFormula) {
        this.savedNumberFormula = savedNumberFormula;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public Double getActualWageAmount() {
        return actualWageAmount;
    }

    public void setActualWageAmount(Double actualWageAmount) {
        this.actualWageAmount = actualWageAmount;
    }

    public Double getActualClientChargeAmount() {
        return actualClientChargeAmount;
    }

    public void setActualClientChargeAmount(Double actualClientChargeAmount) {
        this.actualClientChargeAmount = actualClientChargeAmount;
    }

    public Double getExpensesAmount() {
        return expensesAmount;
    }

    public void setExpensesAmount(Double expensesAmount) {
        this.expensesAmount = expensesAmount;
    }

    public Double getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(Double incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public Boolean getBillable() {
        return billable != null ? billable : false;
    }

    public void setBillable(Boolean billable) {
        this.billable = billable;
    }

    public Double getProjectCost() {
        Double _projectCost = 0d;
        if (getActualWageAmount() != null) {
            _projectCost += getActualWageAmount();
        }

        if (getExpensesAmount() != null) {
            _projectCost += getExpensesAmount();
        }

        return _projectCost;
    }

    public Double getPlanedWageAmount() {
        return planedWageAmount;
    }

    public void setPlanedWageAmount(Double planedWageAmount) {
        this.planedWageAmount = planedWageAmount;
    }

    public Double getPlanedClientChargeAmount() {
        return planedClientChargeAmount;
    }

    public void setPlanedClientChargeAmount(Double planedClientChargeAmount) {
        this.planedClientChargeAmount = planedClientChargeAmount;
    }

    public Double getPlanedIncomeAmount() {
        return planedIncomeAmount;
    }

    public void setPlanedIncomeAmount(Double planedIncomeAmount) {
        this.planedIncomeAmount = planedIncomeAmount;
    }

    public Double getPlanedExpensesAmount() {
        return planedExpensesAmount;
    }

    public void setPlanedExpensesAmount(Double planedExpensesAmount) {
        this.planedExpensesAmount = planedExpensesAmount;
    }

    public EdsProjectCustomFields getProjectCustomFields() {
        return projectCustomFields;
    }

    public void setProjectCustomFields(EdsProjectCustomFields projectcustomfields) {
        this.projectCustomFields = projectcustomfields;
    }

    public Boolean isCalculationCompleted() {
        return calculationCompleted != null ? calculationCompleted : false;
    }

    public void setCalculationCompleted(Boolean calculationCompleted) {
        this.calculationCompleted = calculationCompleted;
    }

    public EmployeeAssignmentEnum getEmployeeAssignment() {
        return employeeAssignment;
    }

    public void setEmployeeAssignment(EmployeeAssignmentEnum employeeAssignment) {
        this.employeeAssignment = employeeAssignment;
    }

    public List<EdsProjectPosition> getProjectPositions() {
        return projectPositions;
    }

    public void setProjectPositions(List<EdsProjectPosition> projectPositions) {
        this.projectPositions = projectPositions;
    }

    public Set<EdsCrmAccount> getClients() {
        return clients;
    }

    public void setClients(Set<EdsCrmAccount> clients) {
        this.clients = clients;
    }

    public Date getLastExpenseClaimedDate() {
        return lastExpenseClaimedDate;
    }

    public void setLastExpenseClaimedDate(Date lastExpenseClaimedDate) {
        this.lastExpenseClaimedDate = lastExpenseClaimedDate;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public Set<EdsCheckInLocation> getCheckInLocations() {
        return checkInLocation;
    }

    public void setCheckInLocations(Set<EdsCheckInLocation> checkInLocations) {
        this.checkInLocation = checkInLocations;
    }

    public SolrInputDocument indexToSolr(List<EdsEmployee> edsAssigneesList, String saleInvoiceNumber, Integer companyId, List<EdsRelation> edsRelationList, boolean newProjectPercon, Float timespent, Float estimatedtime, boolean isAutomatic) {
        SolrInputDocument doc = new SolrInputDocument();
        String compositID = companyId + "_" + getObjectID();
        doc.addField(SolrProjectListRepresenter.FIELD_COMPOSITE_ID, compositID);
        doc.addField(SolrProjectListRepresenter.FIELD_COMPANY_ID, companyId);
        doc.addField(SolrProjectListRepresenter.FIELD_PROJECT_NUMBER, getNumber());
        doc.addField(SolrProjectListRepresenter.FIELD_PROJECT_ID, getObjectID());
        doc.addField(SolrProjectListRepresenter.FIELD_PROJECT_NAME, getName());
        doc.addField(SolrProjectListRepresenter.FIELD_PROJECT_NAME_NUMBER_COMPOSITE, getName() + " " + getNumber());
        doc.addField(SolrProjectListRepresenter.FIELD_PROJECT_DESCRIPTION, getDescription());
        if (newProjectPercon) {
            doc.addField(SolrProjectListRepresenter.FIELD_PROJECT_COMPLETED, getProjectTasksAveragePercentCompletedNewLogic1(timespent, estimatedtime));
        } else {
            if (isAutomatic) {
                doc.addField(SolrProjectListRepresenter.FIELD_PROJECT_COMPLETED, getPercent());
            } else {
                doc.addField(SolrProjectListRepresenter.FIELD_PROJECT_COMPLETED, getProjectTasksAveragePercentCompleted());
            }
        }
        doc.addField(SolrProjectListRepresenter.FIELD_PROJECT_HOUR_SPENT, getTimeSpentHM());
        doc.addField(SolrProjectListRepresenter.FIELD_START_DATE, getStartDate());
        doc.addField(SolrProjectListRepresenter.FIELD_DUE_DATE, getDueDate());
        doc.addField(SolrProjectListRepresenter.FIELD_END_DATE, getEndDate());
        doc.addField(SolrProjectListRepresenter.FIELD_LAST_UPDATE_DATE, getLastUpdateTime());
        doc.addField(SolrProjectListRepresenter.FIELD_PROJECT_CREATED_DATE, getCreationTime());
        doc.addField(SolrProjectListRepresenter.FIELD_PROJECT_MODIFIED_DATE, getLastUpdateTime());


        if (saleInvoiceNumber != null) {
            doc.addField(SolrProjectListRepresenter.FIELD_PROJECT_INVOICE, saleInvoiceNumber);
        }
        if (getParent() != null) {
            doc.addField(SolrProjectListRepresenter.FIELD_PARENT_ID, getParent().getObjectID());
        }
        if (getCreator() != null) {
            doc.addField(SolrProjectListRepresenter.FIELD_PROJECT_CREATOR_ID, getCreator().getObjectID());
            doc.addField(SolrProjectListRepresenter.FIELD_PROJECT_CREATOR, getCreator().getName());
        }

        if (getUpdater() != null) {
            doc.addField(SolrProjectListRepresenter.FIELD_PROJECT_MODIFIED_BY, getUpdater().getName());
        }

        if (getStatus() != null) {
            doc.addField(SolrProjectListRepresenter.FIELD_PROJECT_STATUS_ID, getStatus().getObjectID());
            doc.addField(SolrProjectListRepresenter.FIELD_PROJECT_STATUS_NAME, getStatus().getName());
            doc.addField(SolrProjectListRepresenter.FIELD_PROJECT_STATUS_ID_CODE, getStatus().getObjectID() + SolrProjectListRepresenter.SPLIT
                    + getStatus().getCode());
            doc.addField(SolrProjectListRepresenter.FIELD_PROJECT_STATUS_ID_CODE_NAME, getStatus().getObjectID() + SolrProjectListRepresenter.SPLIT
                    + getStatus().getCode() + SolrProjectListRepresenter.SPLIT + getStatus().getName());
            doc.addField(SolrProjectListRepresenter.FIELD_PROJECT_STATUS_CODE, getStatus().getCode());
            doc.addField(SolrProjectListRepresenter.FIELD_PROJECT_STATUS_SORDER, getStatus().getSorder());
        }
        if (getManager() != null) {
            doc.addField(SolrProjectListRepresenter.FIELD_PROJECT_MANAGER_ID, getManager().getObjectID());
            doc.addField(SolrProjectListRepresenter.FIELD_PROJECT_MANAGER_NAME, getManager().getName());
            doc.addField(SolrProjectListRepresenter.FIELD_PROJECT_MANAGER_ID_NAME, getManager().getObjectID()
                    + SolrProjectListRepresenter.SPLIT + getManager().getName());
        }
        if (getClient() != null) {
            doc.addField(SolrProjectListRepresenter.FIELD_PROJECT_CLIENT_ID, getClient().getObjectID());
            doc.addField(SolrProjectListRepresenter.FIELD_PROJECT_CLIENT_NAME, getClient().getName());
            doc.addField(SolrProjectListRepresenter.FIELD_PROJECT_CLIENT_ID_NAME, getClient().getObjectID() + SolrProjectListRepresenter.SPLIT
                    + getClient().getName());
        }
        StringBuilder clientNames = new StringBuilder();
        for (EdsCrmAccount client : getClients()) {
            doc.addField(SolrProjectListRepresenter.FIELD_PROJECT_MULTI_CLIENT_ID, client.getObjectID());
            doc.addField(SolrProjectListRepresenter.FIELD_PROJECT_MULTI_CLIENT_NAME, client.getName());
            doc.addField(SolrProjectListRepresenter.FIELD_PROJECT_MULTI_CLIENT_ID_NAME, client.getObjectID()
                    + SolrProjectListRepresenter.SPLIT + client.getName());
            if ("".equals(clientNames.toString())) {
                clientNames.append(client.getName() != null ? client.getName() : "");
            }
        }
        doc.addField(SolrProjectListRepresenter.FIELD_PROJECT_CLIENT_NAME_SORT, clientNames.toString());
        List<EdsEmployee> backupManagers = getBackupManagers();
        for (EdsEmployee backupManager : backupManagers) {
            doc.addField(SolrProjectListRepresenter.FIELD_PROJECT_BACKUP_MANAGER_ID, backupManager.getObjectID());
            doc.addField(SolrProjectListRepresenter.FIELD_PROJECT_BACKUP_MANAGER_NAME, backupManager.getName());
            doc.addField(SolrProjectListRepresenter.FIELD_PROJECT_BACKUP_MANAGER_ID_NAME, backupManager.getObjectID()
                    + SolrProjectListRepresenter.SPLIT + backupManager.getName());
        }
        if (getProjectLocation() != null) {
            String locations = getProjectLocation().getCountry().getName()
                    + ","
                    + (getProjectLocation().getState() != null ? (getProjectLocation().getState().getName() + ",") : "")
                    + getProjectLocation().getCity();
            doc.addField(SolrProjectListRepresenter.FIELD_PROJECT_LOCATION_ID, getProjectLocation().getObjectID());
            doc.addField(SolrProjectListRepresenter.FIELD_PROJECT_LOCATION_NAME, locations);
            doc.addField(SolrProjectListRepresenter.FIELD_PROJECT_LOCATION_ID_NAME, getProjectLocation().getObjectID()
                    + SolrProjectListRepresenter.SPLIT
                    + locations);
        }
        if (edsAssigneesList.size() != 0) {
            for (EdsEmployee assignee : edsAssigneesList) {
                if (assignee.getLocation() != null) {
                    doc.addField(SolrProjectListRepresenter.FIELD_USER_LOCATION_ID, assignee.getLocation().getObjectID());
                }
                doc.addField(SolrProjectListRepresenter.FIELD_USER_ID, assignee.getObjectID());
                doc.addField(SolrProjectListRepresenter.FIELD_USER_NAME, assignee.getName());
                doc.addField(SolrProjectListRepresenter.FIELD_USER_ID_NAME, assignee.getObjectID()
                        + SolrProjectListRepresenter.SPLIT + assignee.getName());
            }
        } else {
            doc.addField(SolrProjectListRepresenter.FIELD_USER_ID, getManager().getObjectID());
            doc.addField(SolrProjectListRepresenter.FIELD_USER_NAME, getManager().getName());
            doc.addField(SolrProjectListRepresenter.FIELD_USER_ID_NAME, getManager().getObjectID()
                    + SolrProjectListRepresenter.SPLIT + getManager().getName());
        }

        doc.addField(SolrProjectListRepresenter.FIELD_ACTUAL_WAGE_AMOUNT, getActualWageAmount());
        doc.addField(SolrProjectListRepresenter.FIELD_ACTUAL_CLIENT_CHARGE_AMOUNT, getActualClientChargeAmount());
        doc.addField(SolrProjectListRepresenter.FIELD_EXPENSES_AMOUNT, getExpensesAmount());
        doc.addField(SolrProjectListRepresenter.FIELD_INCOME_AMOUNT, getIncomeAmount());

        doc.addField(SolrProjectListRepresenter.FIELD_PLANED_WAGE_AMOUNT, getPlanedWageAmount());
        doc.addField(SolrProjectListRepresenter.FIELD_PLANED_CLIENT_CHARGE_AMOUNT, getPlanedClientChargeAmount());
        doc.addField(SolrProjectListRepresenter.FIELD_PLANED_EXPENSES_AMOUNT, getPlanedExpensesAmount());
        doc.addField(SolrProjectListRepresenter.FIELD_PLANED_INCOME_AMOUNT, getPlanedIncomeAmount());
        doc.addField(SolrProjectListRepresenter.FIELD_BILLIBLE, getBillable());

        SolrRelationUtils.addToSolrRelations(doc, edsRelationList, EdsRelation.TYPE_PROJECT);
        CustomFieldsUtils.setInSolrCustomFields(doc, getProjectCustomFields());
        return doc;
    }

    @Override
    public void setValueForField(EdsModelField field, Object value) {
        if (field != null && field.getField_ID() != null) {
            String fieldID = field.getField_ID();
            if (fieldID.equals(CustomFormConstants.NUMBER)) {
                setNumber((String) value);
            } else if (fieldID.equals(CustomFormConstants.NAME)) {
                setName((String) value);
            } else if (fieldID.equals(CustomFormConstants.DESCRIPTION)) {
                setDescription((String) value);
            } else if (fieldID.equals(CustomFormConstants.START_DATE)) {
                setStartDate((Date) value);
            } else if (fieldID.equals(CustomFormConstants.DUE_DATE)) {
                setDueDate((Date) value);
            } else if (fieldID.equals(CustomFormConstants.PROJECT.CLIENT)) {
                setClient((EdsCrmAccount) value);
            } else if (fieldID.equals(CustomFormConstants.STATUS)) {
                setStatus((EdsReference) value);
            } else if (fieldID.equals(CustomFormConstants.LOCATION_FIELD)) {
                setProjectLocation((EdsLocation) value);
            } else if (fieldID.equals(CustomFormConstants.MANAGER)) {
                setManager((EdsEmployee) value);
            } else if (fieldID.equals(CustomFormConstants.PROJECT.BACKUP_MANAGER)) {
                setBackupManager((EdsEmployee) value);
            } else if (field.isCustomField()) {
                Object ob = CustomFieldsUtils.getObjectValue(getProjectCustomFields(), fieldID);
                if (ob != null) {
                    if (ob instanceof String) {
                        String text = (String) ob;
                        if (!text.equals(value)) {
                            addChange(fieldID);
                        }
                    } else if (ob instanceof Number) {
                        String text = String.valueOf(((Double) ob).intValue());
                        if (!text.equals(value)) {
                            addChange(fieldID);
                        }
                    } else if (ob instanceof Date) {
                        Date date = (Date) ob;
                        if (!date.equals(value)) {
                            addChange(fieldID);
                        }
                    }
                } else {
                    addChange(fieldID);
                }
                Map<String, Object> customFieldsMap = new HashMap<>();
                customFieldsMap.put(fieldID, value);
                CustomFieldsUtils.setDomenObjectFieldChange(getProjectCustomFields(), customFieldsMap, fieldID);
            }
        }
        super.setValueForField(field, value);
    }

    @Override
    protected String getStringValueByFieldID(String realFieldID) {
        return super.getStringValueByFieldID(realFieldID);
    }

    @Override
    public Object getRealValue(String fieldID) {
        if (fieldID == null) {
            return null;
        } else if (fieldID.equals(CustomFormConstants.NUMBER)) {
            return getNumber();
        } else if (fieldID.equals(CustomFormConstants.NAME)) {
            return getName();
        } else if (fieldID.equals(CustomFormConstants.DESCRIPTION)) {
            return getDescription();
        } else if (fieldID.equals(CustomFormConstants.START_DATE)) {
            EdsUser user = (EdsUser) ServerSecurityContext.getInstance().getUser();
            return ServerUtils.shortDateFormat(getStartDate(), user, true);
        } else if (fieldID.equals(CustomFormConstants.DUE_DATE)) {
            EdsUser user = (EdsUser) ServerSecurityContext.getInstance().getUser();
            return ServerUtils.shortDateFormat(getDueDate(), user, true);
        } else if (fieldID.equals(CustomFormConstants.PROJECT.CLIENT)) {
            return getClient();
        } else if (fieldID.equals(CustomFormConstants.STATUS)) {
            return getStatus();
        } else if (fieldID.equals(CustomFormConstants.PROJECT.LOCATION)) {
            return getProjectLocation();
        } else if (fieldID.equals(CustomFormConstants.MANAGER)) {
            return getManager();
        } else if (fieldID.equals(CustomFormConstants.PROJECT.BACKUP_MANAGER)) {
            return getBackupManager();
        } else if (fieldID.equals(CustomFormConstants.CREATED_DATE)) {
            return getCreationTime();
        } else if (fieldID.equals(CustomFormConstants.UPDATED_DATE)) {
            return getLastUpdateTime();
        } else if (fieldID.equals("modified.by")) {
            return getUpdater();
        } else if (fieldID.equals("modification.date")) {
            return getLastUpdateTime();
        } else if (fieldID.contains("string_value") || fieldID.contains("double_value") || fieldID.contains("date_value")) {
            return getProjectCustomFields() != null ? CustomFieldsUtils.getObjectValue(getProjectCustomFields(), fieldID) : "";
        } else if (fieldID.equals(CustomFormConstants.PROJECT.BILLIBLE)) {
            return getBillable() ? "Yes" : "No";
        } else if (fieldID.equals(CustomFormConstants.CREATED_BY)) {
            return getCreator().getFullName();
        } else if (fieldID.equals(CustomFormConstants.UPDATED_BY)) {
            return getUpdater() != null ? getUpdater().getFullName() : null;
        } else if (fieldID.equals(CustomFormConstants.PROJECT.TIME_SPENT)) {
            return getTimeSpentHM();
        }
        return super.getRealValue(fieldID);
    }

    public interface ChangeListener {
        void onManagerChange(EdsEmployee manager);

        void onBackupManagerChange(EdsEmployee backupManager);

        void onClientChange(EdsCrmAccount client);

        void onNameChange(String name);
    }
}
