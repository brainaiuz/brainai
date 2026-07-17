package com.edatasite.workforce.core.domain.recruitment;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsPosition;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.approving.EdsApprovable;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customfields.EdsPlacementCustomFields;
import com.edatasite.workforce.core.domain.customform.EdsModelField;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.StaticContextAccessor;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.domain.ObjectHistory;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.hrms.client.rpc.PlacementItem;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

/**
 * User: Ilhombek
 * Date: 7/3/12
 * Time: 3:32 PM
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "placement")
public class EdsPlacement extends EdsApprovable implements ObjectHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id")
    private EdsCrmContact candidate;

    @Column(name = "creationTime")
    private Date creationTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private EdsUser creator;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private EdsDepartment department;

    @Column(name = "lastUpdateTime")
    private Date lastUpdateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private EdsLocation location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    private EdsPosition position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private EdsProject project;

    @Column(name = "offer_date")
    private Date offerDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private EdsReference status;

    private Integer candidateType;
    private String plalcemantCode;

    @Column(name = "intnumber")
    private Integer intNumber;

    @Column(name = "numberData")
    private String numberData;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @Where(clause = "deleted = 'false'")
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "placement_vacancies",
            joinColumns = {@JoinColumn(name = "placement_id")},
            inverseJoinColumns = {@JoinColumn(name = "vacancy_id")})
    private Set<EdsVacancy> vacancies = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "placementcustomfieldsid", unique = true)
    private EdsPlacementCustomFields placementCustomFields;

    @OneToMany(mappedBy = "placement", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<EdsPlacementItemTable> itemTables = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entityID", fetch = FetchType.LAZY)
    @Where(clause = "entityType = 'PLACEMENT'")
    @OrderBy(value = "approverOrder ASC")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private List<EdsApprover> approvers = new ArrayList<>();

    @Column(name = "groupPlacementId")
    private Integer groupPlacementId;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "groupPlacementId", updatable = false, insertable = false)
    private EdsGroupPlacement groupPlacement;

    public Integer getObjectID() {
        return objectID;
    }

    public Set<EdsPlacementItemTable> getItemTables() {
        return itemTables;
    }

    public void setItemTables(Set<EdsPlacementItemTable> itemTables) {
        this.itemTables = itemTables;
    }

    public void addItemTable(EdsPlacementItemTable itemTable) {
        itemTables.add(itemTable);
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsCrmContact getCandidate() {
        return candidate;
    }

    public void setCandidate(EdsCrmContact candidate) {
        this.candidate = candidate;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public EdsUser getCreator() {
        return this.creator;
    }

    public void setCreator(final EdsUser creator) {
        this.creator = creator;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public EdsDepartment getDepartment() {
        return department;
    }

    public void setDepartment(EdsDepartment department) {
        this.department = department;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Override
    public void setUpdater(EdsUser user) {
    }

    public EdsLocation getLocation() {
        return location;
    }

    public void setLocation(EdsLocation location) {
        this.location = location;
    }

    public EdsPosition getPosition() {
        return position;
    }

    public void setPosition(EdsPosition position) {
        this.position = position;
    }

    public EdsProject getProject() {
        return project;
    }

    public void setProject(EdsProject project) {
        this.project = project;
    }

    public Date getOfferDate() {
        return offerDate;
    }

    public void setOfferDate(Date offerDate) {
        this.offerDate = offerDate;
    }

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        this.status = status;
    }

    public Set<EdsVacancy> getVacancies() {
        return vacancies == null ? new HashSet<>() : vacancies;
    }

    public void setVacancies(Set<EdsVacancy> vacancies) {
        this.vacancies = vacancies;
    }

    public EdsPlacementCustomFields getPlacementCustomFields() {
        return placementCustomFields;
    }

    public void setPlacementCustomFields(EdsPlacementCustomFields placementCustomFields) {
        this.placementCustomFields = placementCustomFields;
    }

    public Integer getCandidateType() {
        return candidateType;
    }

    public void setCandidateType(Integer candidateType) {
        this.candidateType = candidateType;
    }

    public PlacementItem getRPC() {
        PlacementItem placementItem = new PlacementItem();
        placementItem.setObjectID(getObjectID());
        placementItem.setCandidateID(getCandidate().getObjectID());
        placementItem.setCandidateName(getCandidate().getFullName());
        if (getDepartment() != null) {
            placementItem.setDepartmentID(getDepartment().getObjectID());
            placementItem.setDepartmentName(getDepartment().getName());
        }
        if (getLocation() != null) {
            placementItem.setLocationID(getLocation().getObjectID());
            placementItem.setLocationName(getLocation().getAsSelectItem().getName());
        }
        if (getCreator() != null) {
            placementItem.setCreator(getCreator().getAsSelectItem());
        }
        if (getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null) {
            placementItem.setApprover(getCurrentApprover().getExactEmployee().getAsSelectItem());
        }
        if (getPosition() != null) {
            placementItem.setPositionID(getPosition().getObjectID());
            placementItem.setPositionName(getPosition().getName());
        }
        if (getProject() != null) {
            placementItem.setProjectID(getProject().getObjectID());
            placementItem.setProjectName(getProject().getName());
        }
        if (getCandidateType() != null) {
            placementItem.setCandidateType(getCandidateType());
        }
        if (getNumberData() != null) {
            NumberData numberData = new NumberData();
            numberData.setFirstNumberString(getNumberData());
            numberData.setNumberFormat("_");
            placementItem.setNumberData(numberData);
        }
        placementItem.setDateOffed(getOfferDate());
        if (getStatus() != null) {
            placementItem.setStatusID(getStatus().getObjectID());
            placementItem.setStatusName(getStatus().getName());
            placementItem.setStatusCode(getStatus().getCode());
        }
        ArrayList<SelectItem> vacanciesListItem = new ArrayList<>();
        if (getCandidate() != null && getCandidate().getVacancies() != null && getCandidate().getVacancies().size() > 0) {
            for (EdsVacancy vacancy : getCandidate().getVacancies()) {
                vacanciesListItem.add(vacancy.getAsSelectItem());
            }
        }
        if (getVacancies() != null && getVacancies().size() > 0 && vacanciesListItem.size() > 0) {
            for (EdsVacancy vacancy : getVacancies()) {
                SelectItem asSelectItem = vacancy.getAsSelectItem();
                for (SelectItem s : vacanciesListItem) {
                    if (s.getId().equals(asSelectItem.getId())) {
                        s.setSelected(true);
                    }
                }
            }
        }
        placementItem.getVacancies().addAll(vacanciesListItem);

        return placementItem;
    }

    @Override
    public void setValueForField(EdsModelField field, Object value) {
        if (field != null && field.getField_ID() != null) {
            String fieldID = field.getField_ID();
            switch (fieldID) {
                case CustomFormConstants.PLACEMENT.CANDIDATE:
                    setCandidate((EdsCrmContact) value);
                    break;
                case CustomFormConstants.PLACEMENT.DEPARTMENT:
                    setDepartment((EdsDepartment) value);
                    break;
                case CustomFormConstants.PLACEMENT.DATE_OFFERED:
                    setOfferDate((Date) value);
                    break;
                case CustomFormConstants.PLACEMENT.LOCATION:
                    setLocation((EdsLocation) value);
                case CustomFormConstants.PLACEMENT.POSITION:
                    setPosition((EdsPosition) value);
            }
        }
        super.setValueForField(field, value);
    }

    @Override
    protected String getStringValueByFieldID(String realFieldID) {
        return super.getStringValueByFieldID(realFieldID);
    }

    @Override
    public List<EdsApprover> getApprovers() {
        return approvers;
    }

    @Override
    public void setApprovers(List<EdsApprover> approvers) {
        this.approvers = approvers;
    }

    @Override
    public void setEntityStatus(EdsReference overallStatus) {
        setStatus(overallStatus);
        setOverallStatus(overallStatus);
    }

    @Override
    public boolean isCurrentApproverApproved() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && Constants.PLACEMENT_STATUS_APPROVED.equals(getCurrentApprover().getStatus().getCode());

    }

    @Override
    public boolean isCurrentApproverRejected() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && Constants.PLACEMENT_STATUS_REJECTED.equals(getCurrentApprover().getStatus().getCode());
    }

    @Override
    protected EdsReference getStatusByMarkedAction(Integer actionID) {
        if (!isOk(actionID)) {
            return null;
        }
        ReferenceManager referenceManager = StaticContextAccessor.getBean(ReferenceManager.class);
        if (actionID.equals(ApproverItem.MARK_AS_REJECTED)) {
            return referenceManager.findReference(Constants.PLACEMENT_STATUS, Constants.PLACEMENT_STATUS_REJECTED);
        } else if (actionID.equals(ApproverItem.MARK_AS_APPROVED)) {
            return referenceManager.findReference(Constants.PLACEMENT_STATUS, Constants.PLACEMENT_STATUS_APPROVED);
        } else if (actionID.equals(ApproverItem.SEND_TO_CREATOR)) {
            return referenceManager.findReference(Constants.PLACEMENT_STATUS, Constants.PLACEMENT_STATUS_REJECTED);
        } else if (actionID.equals(ApproverItem.SEND_TO_DIRECTORS)) {
            return referenceManager.findReference(Constants.PLACEMENT_STATUS, Constants.PLACEMENT_STATUS_REJECTED);
        }
        return null;
    }

    @Override
    public Object getRealValue(String fieldID) {
        if (fieldID == null) {
            return null;
        } else if (fieldID.equals(CustomFormConstants.PLACEMENT.CANDIDATE)) {
            return getCandidate();
        } else if (fieldID.equals(CustomFormConstants.PLACEMENT.DEPARTMENT)) {
            return getDepartment();
        } else if (fieldID.equals(CustomFormConstants.PLACEMENT.DATE_OFFERED)) {
            return getOfferDate();
        } else if (fieldID.equals(CustomFormConstants.PLACEMENT.LOCATION)) {
            return getLocation();
        } else if (fieldID.equals(CustomFormConstants.PLACEMENT.POSITION)) {
            return getPosition();
        } else if (fieldID.contains("string_value") || fieldID.contains("double_value") || fieldID.contains("date_value")) {
            return getPlacementCustomFields() != null ? CustomFieldsUtils.getObjectValue(getPlacementCustomFields(), fieldID) : "";
        }
        return super.getRealValue(fieldID);
    }

    public String getPlalcemantCode() {
        return this.plalcemantCode;
    }

    public void setPlalcemantCode(final String plalcemantCode) {
        this.plalcemantCode = plalcemantCode;
    }

    public Integer getIntNumber() {
        return this.intNumber;
    }

    public void setIntNumber(final Integer intNumber) {
        this.intNumber = intNumber;
    }

    public String getNumberData() {
        return this.numberData;
    }

    public void setNumberData(final String numberData) {
        this.numberData = numberData;
    }

    public Integer getGroupPlacementId() {
        return groupPlacementId;
    }

    public void setGroupPlacementId(Integer groupPlacementId) {
        this.groupPlacementId = groupPlacementId;
    }

    public EdsGroupPlacement getGroupPlacement() {
        return groupPlacement;
    }

    public void setGroupPlacement(EdsGroupPlacement groupPlacement) {
        this.groupPlacement = groupPlacement;
    }
}
