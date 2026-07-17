package com.edatasite.workforce.core.domain.assessment;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsValidityPeriod;

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
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * User: Sher(sherali.pirnafaosov@gmail.com)
 * Date: 9/18/12
 * Time: 12:35 PM
 * Finnet Technologies
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "bonus_distribution")
public class EdsBonusDistribution extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id")
    private EdsValidityPeriod validityPeriod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private EdsDepartment department;

    /**
     * Bonus Distribution status
     * {@link com.edatasite.workforce.gwt.hrms.client.rpc.BonusDistributionItem}
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "distribution_status_id")
    private EdsReference distributionStatus;

    /**
     * Bonus Distribution approval status
     * {@link com.edatasite.workforce.gwt.hrms.client.rpc.BonusDistributionItem}
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approval_status_id")
    private EdsReference approvalStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bonus_settings_id")
    private EdsBonusSettings bonusSettings;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "bonus_distribution_employee_bonus_item",
            joinColumns = {@JoinColumn(name = "bonus_distribution_id")},
            inverseJoinColumns = {@JoinColumn(name = "employeebonusitems_id")})
    private Set<EdsEmployeeBonusItem> employeeBonusItems = new HashSet<>();

    @Column(name = "stepName")
    private String stepName;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsValidityPeriod getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(EdsValidityPeriod validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public EdsDepartment getDepartment() {
        return department;
    }

    public void setDepartment(EdsDepartment department) {
        this.department = department;
    }

    public EdsReference getDistributionStatus() {
        return distributionStatus;
    }

    public void setDistributionStatus(EdsReference distributionStatus) {
        this.distributionStatus = distributionStatus;
    }

    public EdsReference getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(EdsReference approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public EdsBonusSettings getBonusSettings() {
        return bonusSettings;
    }

    public void setBonusSettings(EdsBonusSettings bonusSettings) {
        this.bonusSettings = bonusSettings;
    }

    public Set<EdsEmployeeBonusItem> getEmployeeBonusItems() {
        return employeeBonusItems;
    }

    public void setEmployeeBonusItems(Set<EdsEmployeeBonusItem> employeeBonusItems) {
        this.employeeBonusItems = employeeBonusItems;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

}
