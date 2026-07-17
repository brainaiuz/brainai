package com.edatasite.workforce.core.domain.payrolluk;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 05.05.14
 * Time: 12:55
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "eos_settings")
public class EndOfServiceSettings extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String countryCode;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "settings")
    private List<EdsRuleEosSettings> ruleSettings = new ArrayList<>();

    private Integer payFrom; /*0 - from Basic Salary, 1 - from Basic Salary+Allowance*/

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "endOfServiceSettings")
    @Where(clause = "(deleted = 'false' or deleted is null)")
    private List<EdsPayrollCategory> categories = new ArrayList<>();

    private Boolean includeLeaveAllowances;

    private Boolean includeBenefitPayments;

    private Boolean fromAllAllowances; /*  if true all allowances will be taken from the last paid payrun */

    private Boolean fromLastPayment;  /* if true selected allowances will be taken from the last paid payrun else from Employee recurring allowances*/

    private Boolean useMonthPayment;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public List<EdsRuleEosSettings> getRuleSettings() {
        return ruleSettings;
    }

    public void setRuleSettings(List<EdsRuleEosSettings> ruleSettings) {
        this.ruleSettings = ruleSettings;
    }

    public Integer getPayFrom() {
        return payFrom;
    }

    public void setPayFrom(Integer payFrom) {
        this.payFrom = payFrom;
    }

    public List<EdsPayrollCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<EdsPayrollCategory> categories) {
        this.categories = categories;
    }

    public Boolean isIncludeLeaveAllowances() {
        return includeLeaveAllowances != null && includeLeaveAllowances;
    }

    public void setIncludeLeaveAllowances(Boolean includeLeaveAllowances) {
        this.includeLeaveAllowances = includeLeaveAllowances;
    }

    public Boolean isIncludeBenefitPayments() {
        return includeBenefitPayments != null && includeBenefitPayments;
    }

    public void setIncludeBenefitPayments(Boolean includeBenefitPayments) {
        this.includeBenefitPayments = includeBenefitPayments;
    }

    public Boolean isFromAllAllowances() {
        return fromAllAllowances != null && fromAllAllowances;
    }

    public void setFromAllAllowances(Boolean fromAllAllowances) {
        this.fromAllAllowances = fromAllAllowances;
    }

    public Boolean isFromLastPayment() {
        return fromLastPayment != null && fromLastPayment;
    }

    public void setFromLastPayment(Boolean fromLastPayment) {
        this.fromLastPayment = fromLastPayment;
    }

    public void setUseMonthPayment(Boolean useMonthPayment) {
        this.useMonthPayment = useMonthPayment;
    }

    public Boolean isUseMonthPayment() {
        return useMonthPayment != null ? useMonthPayment : false;
    }
}
