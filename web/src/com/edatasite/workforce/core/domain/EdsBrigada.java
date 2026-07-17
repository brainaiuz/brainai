package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.customfields.EdsBrigadaCustomFields;
import com.edatasite.workforce.gwt.core.client.enums.EmployeeAssignmentEnum;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectListItem;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "brigadas")
public class EdsBrigada extends EdsObject {

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
    private transient EdsProject.ChangeListener changeListener;
    private transient boolean solrSensitiveFieldsChanged = false;
    private transient List<Integer> backupManagersBeforeEdit;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parnetId")
    private EdsProject parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statusid")
    private EdsReference status;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
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

    @Column(name = "name")
    private String name;
    @Column(name = "description")
    @Type(type = "text")
    private String description;

    @Column(name = "isDeleted")
    private Boolean deleted = false;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "projectLocationId")
    private EdsLocation projectLocation;
    @Column(name = "number")
    private String number;
    @Column(name = "savedNumberFormula")
    private String savedNumberFormula;
    @Column(name = "intNumber")
    private Integer intNumber;

    @Column(name = "brigada_code")
    private String brigadaCode;


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

    @OneToOne
    @JoinColumn(name = "customfieldsid")
    private EdsBrigadaCustomFields customFields;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "brigada_owners",
            joinColumns = {@JoinColumn(name = "brigada_id")},
            inverseJoinColumns = {@JoinColumn(name = "owner_id")})
    @Where(clause = "(deleted = 'false' or deleted is null)")
    private List<EdsUser> owners = new ArrayList<>();

    private String ownersId;

    public void setBackupManagersBeforeEdit(List<Integer> backupManagersBeforeEdit) {
        this.backupManagersBeforeEdit = backupManagersBeforeEdit;
    }

    public boolean isSolrSensitiveFieldsChanged() {
        return solrSensitiveFieldsChanged;
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


    public EdsCompany getCompany() {
        return getManager().getCompany();
    }

    public EdsEmployee getManager() {
        return manager;
    }

    public void setManager(EdsEmployee manager) {
        if (!ServerUtils.equalsEdsObject(this.manager, manager)) {
            solrSensitiveFieldsChanged = true;
        }
        EdsEmployee oldValue = this.manager;
        this.manager = manager;
        if (propertyChangeSupport != null) {
            propertyChangeSupport.firePropertyChange(FIELD_MANAGER, oldValue, this.manager);
        }
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


    public EdsEmployee getBackupManager() {
        return backupManager;
    }

    public void setBackupManager(EdsEmployee backupManager) {
        if (!ServerUtils.equalsEdsObject(this.backupManager, backupManager)) {
//            addChange(CustomFormConstants.PROJECT.BACKUP_MANAGER);
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


    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (!ServerUtils.equalsString(this.name, name)) {
            solrSensitiveFieldsChanged = true;
//            addChange(CustomFormConstants.NAME);
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
//            addChange(CustomFormConstants.DESCRIPTION);
        }
        this.description = description;
    }

    //User log related

    public ProjectListItem createProjectListItem() {
        ProjectListItem result = new ProjectListItem();
        result.setObjectId(getObjectID());
        result.setNumber(getNumber());
        result.setName(getName());
        result.setDescription(getDescription());
        result.setStatus(getStatus() != null ? getStatus().getName() : "N/A");
        result.setManager(getManager() != null ? getManager().getName() : "N/A");
        result.setLastUpdate(getLastUpdateTime());
        return result;
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


    public EdsLocation getProjectLocation() {
        return projectLocation;
    }


    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        if (!ServerUtils.equalsString(this.number, number)) {
//            addChange(CustomFormConstants.NUMBER);
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

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        this.status = status;
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


    public interface ChangeListener {

        void onManagerChange(EdsEmployee manager);

        void onBackupManagerChange(EdsEmployee backupManager);

        void onClientChange(EdsCrmAccount client);

        void onNameChange(String name);
    }

    public String getBrigadaCode() {
        return brigadaCode;
    }

    public void setBrigadaCode(String brigadaCode) {
        this.brigadaCode = brigadaCode;
    }

    public EdsBrigadaCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsBrigadaCustomFields customFields) {
        this.customFields = customFields;
    }

    public String getOwnersId() {
        return ownersId;
    }

    public void setOwnersId(String ownersId) {
        this.ownersId = ownersId;
    }

    public List<EdsUser> getOwners() {
        return owners;
    }

    public void setOwners(List<EdsUser> owners) {
        this.owners = owners;
    }
}
