package com.edatasite.workforce.core.domain.goal;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.accounting.EdsUnitMeasurement;
import com.edatasite.workforce.core.domain.customfields.EdsGoalCustomFields;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: Oct 26, 2009
 * Time: 4:51:20 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "goal")
public class EdsGoal extends EdsObject {
    //GOAL CATEGORY
    public static final String _GOAL_CATEGORY = "_GOAL_CATEGORY";
    public static final String DEPARTMENT_GOAL = "DEPARTMENT_GOAL";
    public static final String PERSONAL_GOAL = "PERSONAL_GOAL";
    public static final String PROJECT_GOAL = "PROJECT_GOAL";
    public static final String BUSINESS_GOAL = "BUSINESS_GOAL";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "businessgoal_id")
    private EdsBusinessGoal businessGoal;

    @Column(name = "title")
    private String title;

    @Column(name = "description", length = 10000)
    private String description;

    @Column(name = "actionsteps", length = 10000)
    private String actionSteps;

    @Column(name = "fromDate")
    private Date fromDate;

    @Column(name = "toDate")
    private Date toDate;
    @Column(name = "creationtime")
    private Date creationTime;

    @Column(name = "intnumber")
    private Integer intNumber;
    @Column(name = "numberData")
    private String numberData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private EdsReference status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goalcategory_id")
    private EdsReference goalCategory;

    @JoinColumn(name = "progress")
    private Double progress = 0.d;

    @Column(name = "weight")
    private int weight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resolver_id")
    private EdsEmployee resolver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private EdsUser creator;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @Where(clause = "deleted = 'false'")
    @JoinColumn(name = "goal_id")
    private Set<EdsGoalAssignees> goalAssigneeses = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private EdsProject project;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_goal_id")
    private EdsGoal projectGoal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private EdsDepartment department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private EdsLocation location;

    @Column(name = "targetGoal")
    private Integer targetGoal;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goalcustomfieldsid")
    private EdsGoalCustomFields goalCustomFields;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id")
    private EdsValidityPeriod validityPeriod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "score_calculation_id")
    private EdsReference scoreCalculation;

    @ManyToOne
    @JoinColumn(name = "measurement_unit_id")
    private EdsUnitMeasurement measurementUnit;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(name = "groupId")
    private Integer groupID;


    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsBusinessGoal getBusinessGoal() {
        return businessGoal;
    }

    public void setBusinessGoal(EdsBusinessGoal businessGoal) {
        this.businessGoal = businessGoal;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActionSteps() {
        return actionSteps;
    }

    public void setActionSteps(String actionSteps) {
        this.actionSteps = actionSteps;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        this.status = status;
    }

    public EdsReference getGoalCategory() {
        return goalCategory;
    }

    public void setGoalCategory(EdsReference goalCategory) {
        this.goalCategory = goalCategory;
    }

    public Double getProgress() {
        return progress;
    }

    public void setProgress(Double progress) {
        this.progress = progress;
    }

    public int getWeight() {
        return weight ;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public EdsEmployee getResolver() {
        return resolver;
    }

    public void setResolver(EdsEmployee resolver) {
        this.resolver = resolver;
    }

    public EdsUser getCreator() {
        return creator;
    }

    public void setCreator(EdsUser creator) {
        this.creator = creator;
    }

    public EdsProject getProject() {
        return project;
    }

    public void setProject(EdsProject project) {
        this.project = project;
    }

    public Set<EdsGoalAssignees> getGoalAssigneeses() {
        return goalAssigneeses;
    }

    public void setGoalAssigneeses(Set<EdsGoalAssignees> goalAssigneeses) {
        this.goalAssigneeses = goalAssigneeses;
    }

    public Set<EdsGoalAssignees> getUndeletedGoalAssignees() {
        Set<EdsGoalAssignees> edsGoalAssigneeses = new HashSet<>();
        for (EdsGoalAssignees goalAssignees : getGoalAssigneeses()) {
            if (goalAssignees.isDeleted() == null || !goalAssignees.isDeleted()) {
                edsGoalAssigneeses.add(goalAssignees);
            }
        }
        return edsGoalAssigneeses;
    }

    public EdsGoalAssignees getGoalAssignee(EdsEmployee employee) {
        for (EdsGoalAssignees goalAssignees : getUndeletedGoalAssignees()) {
            if (employee.equals(goalAssignees.getAssignee())) {
                return goalAssignees;
            }
        }
        return null;
    }

    public EdsGoalAssignees getGoalAssigneeByEmployeeId(Integer employeeId) {
        for (EdsGoalAssignees goalAssignees : getUndeletedGoalAssignees()) {
            if (employeeId.equals(goalAssignees.getAssignee().getObjectID())) {
                return goalAssignees;
            }
        }
        return null;
    }

    public EdsDepartment getDepartment() {
        return department;
    }

    public void setDepartment(EdsDepartment department) {
        this.department = department;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public EdsGoalCustomFields getGoalCustomFields() {
        return goalCustomFields;
    }

    public void setGoalCustomFields(EdsGoalCustomFields goalCustomFields) {
        this.goalCustomFields = goalCustomFields;
    }

    public EdsValidityPeriod getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(EdsValidityPeriod validityPeriod) {
        this.validityPeriod = validityPeriod;
    }


    public EdsReference getScoreCalculation() {
        return scoreCalculation;
    }

    public void setScoreCalculation(EdsReference scoreCalculation) {
        this.scoreCalculation = scoreCalculation;
    }

    public EdsUnitMeasurement getMeasurementUnit() {
        return measurementUnit;
    }

    public void setMeasurementUnit(EdsUnitMeasurement measurementUnit) {
        this.measurementUnit = measurementUnit;
    }

    public Integer getGroupID() {
        return groupID;
    }

    public void setGroupID(Integer groupID) {
        this.groupID = groupID;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public String getNumberData() {
        return numberData;
    }

    public void setNumberData(String numberData) {
        this.numberData = numberData;
    }

    public EdsGoal getProjectGoal() {
        return projectGoal;
    }

    public void setProjectGoal(EdsGoal projectGoal) {
        this.projectGoal = projectGoal;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public EdsLocation getLocation() {
        return location;
    }

    public void setLocation(EdsLocation location) {
        this.location = location;
    }

    public Integer getTargetGoal() {
        return targetGoal;
    }

    public void setTargetGoal(Integer targetGoal) {
        this.targetGoal = targetGoal;
    }

    @Override
    public SelectItem getAsSelectItem() {
        String title = getTitle();
        SelectItem item = new SelectItem(getObjectID(), title!=null && !title.isEmpty() ? title : getNumberData());
        item.setQtyAmount(BigDecimal.valueOf(getWeight()));
        return item;
    }
}
