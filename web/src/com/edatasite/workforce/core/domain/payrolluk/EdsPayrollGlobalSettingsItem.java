package com.edatasite.workforce.core.domain.payrolluk;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.enums.EPPaymentType;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 9/11/14
 * Time: 1:40 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "paymentdeductionsettingsitem")
public class EdsPayrollGlobalSettingsItem extends EdsObject {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "pds_id")
    private Integer payrollGlobalSettingsId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pds_id", updatable = false, insertable = false, foreignKey = @ForeignKey(name = "none"))
    private EdsPayrollGlobalSettings payrollGlobalSettings;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "PayrollGlobalSettingsItemCategories",
            joinColumns = {@JoinColumn(name = "pdsId")},
            inverseJoinColumns = {@JoinColumn(name = "categoryId")})
    @Where(clause = "(deleted = 'false' or deleted is null)")
    private List<EdsPayrollCategory> linkedCategories = new ArrayList<>();

    @Column(name = "category_id")
    private Integer categoryId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", updatable = false, insertable = false, foreignKey = @ForeignKey(name = "none"))
    private EdsPayrollCategory category;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "pds_id")
    @Where(clause = "(deleted = 'false' or deleted is null) and isRecurring = 'true'")
    private List<EdsPaymentDeduction> categories = new ArrayList<>();

    private Boolean fromAllAllowances;
    private Integer payType;

    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private EPPaymentType paymentType;
    private Boolean deleted;


    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getPayrollGlobalSettingsId() {
        return payrollGlobalSettingsId;
    }

    public void setPayrollGlobalSettingsId(Integer payrollGlobalSettingsId) {
        this.payrollGlobalSettingsId = payrollGlobalSettingsId;
    }

    public EdsPayrollGlobalSettings getPayrollGlobalSettings() {
        return payrollGlobalSettings;
    }

    public void setPayrollGlobalSettings(EdsPayrollGlobalSettings payrollGlobalSettings) {
        this.payrollGlobalSettings = payrollGlobalSettings;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public EdsPayrollCategory getCategory() {
        return category;
    }

    public void setCategory(EdsPayrollCategory category) {
        this.category = category;
    }

    public List<EdsPaymentDeduction> getCategories() {
        return categories;
    }

    public void setCategories(List<EdsPaymentDeduction> categories) {
        this.categories = categories;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Boolean getFromAllAllowances() {
        return fromAllAllowances != null ? fromAllAllowances : false;
    }

    public void setFromAllAllowances(Boolean fromAllAllowances) {
        this.fromAllAllowances = fromAllAllowances;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Boolean isDeleted() {
        return deleted != null ? deleted : Boolean.FALSE;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public EPPaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(EPPaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public List<EdsPayrollCategory> getLinkedCategories() {
        return linkedCategories;
    }

    public void setLinkedCategories(List<EdsPayrollCategory> linkedCategories) {
        this.linkedCategories = linkedCategories;
    }
}
