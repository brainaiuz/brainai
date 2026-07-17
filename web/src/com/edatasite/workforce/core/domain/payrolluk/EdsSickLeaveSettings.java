package com.edatasite.workforce.core.domain.payrolluk;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.payroll.client.rpc.SickLeaveSettings;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "sickleavesettings")
public class EdsSickLeaveSettings extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "fullypaidleavedays", nullable = false)
    private Integer fullyPaidLeaveDays;
    @Column(name = "halfpaidleavedays", nullable = false)
    private Integer halfPaidLeaveDays;
    @Column(name = "unpaidleavedays", nullable = false)
    private Integer unPaidLeaveDays;
    @Column(name = "minperiodofservice", nullable = false)
    private Integer minPeriodOfService;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryid", nullable = false)
    private EdsPayrollCategory sickLeaveCategory;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "sickLeaveSettings")
    @Where(clause = "(deleted = 'false' or deleted is null)")
    private List<EdsPayrollCategory> categories = new ArrayList<>();

    public Integer getFullyPaidLeaveDays() {
        return fullyPaidLeaveDays;
    }

    public void setFullyPaidLeaveDays(Integer fullyPaidLeaveDays) {
        this.fullyPaidLeaveDays = fullyPaidLeaveDays;
    }

    public Integer getHalfPaidLeaveDays() {
        return halfPaidLeaveDays;
    }

    public void setHalfPaidLeaveDays(Integer halfPaidLeaveDays) {
        this.halfPaidLeaveDays = halfPaidLeaveDays;
    }

    public Integer getUnPaidLeaveDays() {
        return unPaidLeaveDays;
    }

    public void setUnPaidLeaveDays(Integer unPaidLeaveDays) {
        this.unPaidLeaveDays = unPaidLeaveDays;
    }

    public Integer getMinPeriodOfService() {
        return minPeriodOfService;
    }

    public void setMinPeriodOfService(Integer minPeriodOfService) {
        this.minPeriodOfService = minPeriodOfService;
    }

    public EdsPayrollCategory getSickLeaveCategory() {
        return sickLeaveCategory;
    }

    public void setSickLeaveCategory(EdsPayrollCategory sickLeaveCategory) {
        this.sickLeaveCategory = sickLeaveCategory;
    }


    public List<EdsPayrollCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<EdsPayrollCategory> categories) {
        this.categories = categories;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public SickLeaveSettings getRPC() {
        SickLeaveSettings result = new SickLeaveSettings();
        result.setSickLeaveCategory(getSickLeaveCategory() != null ? getSickLeaveCategory().getAsSelectItem() : null);
        result.setFullyPaidLeaveDays(getFullyPaidLeaveDays());
        result.setHalfPaidLeaveDays(getHalfPaidLeaveDays());
        result.setUnPaidLeaveDays(getUnPaidLeaveDays());
        result.setMinPeriodOfService(getMinPeriodOfService());
        if (getCategories() != null && getCategories().size() > 0) {
            for (EdsPayrollCategory category : getCategories()) {
                result.getAllowances().add(category.createPaymentDeductionSelectItem());
            }
        }
        return result;
    }

}
