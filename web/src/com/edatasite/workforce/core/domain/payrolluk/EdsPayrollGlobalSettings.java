package com.edatasite.workforce.core.domain.payrolluk;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsEmployee;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 9/10/14
 * Time: 10:40 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "paymentdeductionsettings")
public class EdsPayrollGlobalSettings extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToMany(fetch = FetchType.LAZY)
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "pds_employee",
            joinColumns = {@JoinColumn(name = "pds_id")},
            inverseJoinColumns = {@JoinColumn(name = "employee_id")})
    private List<EdsEmployee> employees;

    @Column(name = "category_type")
    private Integer categoryType; //0-Payment, 1-Deduction, 2 - Basic Salary

    @Column(name = "settings_type")
    private Integer settingsType; //0-by PayrollBatch, 1-by Position, 2 - by Department

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "batch_id")
    private EdsPayrollBatch payrollBatch;

    @Column(name = "rateType")
    private String rateType;

    @Column(name = "salary_category_id")
    private Integer salaryCategoryId;

    @Column(name = "salary")
    private BigDecimal salary;

    @Column(name = "name")
    private String name;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private EdsCurrency currency;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "payrollGlobalSettings")
    @Where(clause = "(deleted = 'false' or deleted is null)")
    private List<EdsPayrollGlobalSettingsItem> items = new ArrayList<>();

    @Column(name = "last_update")
    private Date lastUpdate;

    private Boolean deleted;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public List<EdsEmployee> getEmployees() {
        return employees;
    }

    public Integer getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Integer categoryType) {
        this.categoryType = categoryType;
    }

    public Integer getSettingsType() {
        return settingsType;
    }

    public void setSettingsType(Integer settingsType) {
        this.settingsType = settingsType;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public Integer getSalaryCategoryId() {
        return salaryCategoryId;
    }

    public void setSalaryCategoryId(Integer salaryCategoryId) {
        this.salaryCategoryId = salaryCategoryId;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public void setEmployees(List<EdsEmployee> employees) {
        this.employees = employees;
    }

    public List<EdsPayrollGlobalSettingsItem> getItems() {
        return items;
    }

    public void setItems(List<EdsPayrollGlobalSettingsItem> items) {
        this.items = items;
    }

    public Boolean isDeleted() {
        return deleted != null ? deleted : Boolean.FALSE;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public EdsCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(EdsCurrency currency) {
        this.currency = currency;
    }

    public EdsPayrollBatch getPayrollBatch() {
        return payrollBatch;
    }

    public void setPayrollBatch(EdsPayrollBatch payrollBatch) {
        this.payrollBatch = payrollBatch;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
