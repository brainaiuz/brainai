package com.edatasite.workforce.rest.v3.release10.accounting.dto;

import com.edatasite.workforce.rest.v3.release10.core.request.CustomFieldRequest;
import com.edatasite.workforce.rest.v3.release10.core.to.DynamicDto;
import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;
import com.edatasite.workforce.rest.v3.release10.core.to.IdName;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ExpenseDto extends DynamicDto {

    private Integer id;
    private IdName supplier;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonDeserialize(using = DateDeserializer.class)
    private Date date;
    private String number;
    private String reportTitle;
    private IdCode currency;
    @Pattern(regexp = "NO_TAX|TAX_INCLUSIVE|TAX_EXCLUSIVE", message = "amounts must be one of NO_TAX/TAX_INCLUSIVE/TAX_EXCLUSIVE")
    private String amounts;
    private IdName approver;
    private IdName employee;
    private IdCode fixedAsset;
    private IdCode opportunity;
    private IdCode relatedProject;
    private IdCode relatedPO;
    private IdCode accountsPayable;
    private List<ExpenseItemDto> items;
    private Boolean isCompanyExpense;
    private String status;
    private String statusColor;
    @Valid
    private List<? extends CustomFieldRequest> customFields;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonDeserialize(using = DateDeserializer.class)
    private Date createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonDeserialize(using = DateDeserializer.class)
    private Date updatedAt;
    private BigDecimal baseTotal;
    private Date startDate;
    private BigDecimal total;

    public ExpenseDto() {
    }

    public ExpenseDto(Integer id, IdName supplier, Date date, String number, String reportTitle, IdCode currency, String amounts, IdName approver, IdName employee, IdCode fixedAsset, IdCode opportunity, IdCode relatedProject, IdCode relatedPO, IdCode accountsPayable, List<ExpenseItemDto> items, Boolean isCompanyExpense, String status, List<? extends CustomFieldRequest> customFields, Date createdAt, Date updatedAt) {
        this.id = id;
        this.supplier = supplier;
        this.date = date;
        this.number = number;
        this.reportTitle = reportTitle;
        this.currency = currency;
        this.amounts = amounts;
        this.approver = approver;
        this.employee = employee;
        this.fixedAsset = fixedAsset;
        this.opportunity = opportunity;
        this.relatedProject = relatedProject;
        this.relatedPO = relatedPO;
        this.accountsPayable = accountsPayable;
        this.items = items;
        this.isCompanyExpense = isCompanyExpense;
        this.status = status;
        this.customFields = customFields;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public IdName getSupplier() {
        return supplier;
    }

    public void setSupplier(IdName supplier) {
        this.supplier = supplier;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public IdCode getCurrency() {
        return currency;
    }

    public void setCurrency(IdCode currency) {
        this.currency = currency;
    }

    public String getAmounts() {
        return amounts;
    }

    public void setAmounts(String amounts) {
        this.amounts = amounts;
    }

    public IdName getApprover() {
        return approver;
    }

    public void setApprover(IdName approver) {
        this.approver = approver;
    }

    public IdName getEmployee() {
        return employee;
    }

    public void setEmployee(IdName employee) {
        this.employee = employee;
    }

    public IdCode getFixedAsset() {
        return fixedAsset;
    }

    public void setFixedAsset(IdCode fixedAsset) {
        this.fixedAsset = fixedAsset;
    }

    public IdCode getOpportunity() {
        return opportunity;
    }

    public void setOpportunity(IdCode opportunity) {
        this.opportunity = opportunity;
    }

    public IdCode getRelatedProject() {
        return relatedProject;
    }

    public void setRelatedProject(IdCode relatedProject) {
        this.relatedProject = relatedProject;
    }

    public IdCode getRelatedPO() {
        return relatedPO;
    }

    public void setRelatedPO(IdCode relatedPO) {
        this.relatedPO = relatedPO;
    }

    public IdCode getAccountsPayable() {
        return accountsPayable;
    }

    public void setAccountsPayable(IdCode accountsPayable) {
        this.accountsPayable = accountsPayable;
    }

    public List<ExpenseItemDto> getItems() {
        return items;
    }

    public void setItems(List<ExpenseItemDto> items) {
        this.items = items;
    }

    public Boolean getCompanyExpense() {
        return isCompanyExpense;
    }

    public void setCompanyExpense(Boolean companyExpense) {
        isCompanyExpense = companyExpense;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<? extends CustomFieldRequest> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<? extends CustomFieldRequest> customFields) {
        this.customFields = customFields;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setStatusColor(String statusColor) {
        this.statusColor = statusColor;
    }

    public String getStatusColor() {
        return statusColor;
    }
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getTotal() {
        return total;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExpenseDto)) return false;

        ExpenseDto that = (ExpenseDto) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (supplier != null ? !supplier.equals(that.supplier) : that.supplier != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (number != null ? !number.equals(that.number) : that.number != null) return false;
        if (reportTitle != null ? !reportTitle.equals(that.reportTitle) : that.reportTitle != null) return false;
        if (currency != null ? !currency.equals(that.currency) : that.currency != null) return false;
        if (amounts != null ? !amounts.equals(that.amounts) : that.amounts != null) return false;
        if (approver != null ? !approver.equals(that.approver) : that.approver != null) return false;
        if (employee != null ? !employee.equals(that.employee) : that.employee != null) return false;
        if (fixedAsset != null ? !fixedAsset.equals(that.fixedAsset) : that.fixedAsset != null) return false;
        if (opportunity != null ? !opportunity.equals(that.opportunity) : that.opportunity != null) return false;
        if (relatedProject != null ? !relatedProject.equals(that.relatedProject) : that.relatedProject != null)
            return false;
        if (relatedPO != null ? !relatedPO.equals(that.relatedPO) : that.relatedPO != null) return false;
        if (accountsPayable != null ? !accountsPayable.equals(that.accountsPayable) : that.accountsPayable != null)
            return false;
        if (items != null ? !items.equals(that.items) : that.items != null) return false;
        if (isCompanyExpense != null ? !isCompanyExpense.equals(that.isCompanyExpense) : that.isCompanyExpense != null)
            return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (customFields != null ? !customFields.equals(that.customFields) : that.customFields != null) return false;
        if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null) return false;
        if (updatedAt != null ? !updatedAt.equals(that.updatedAt) : that.updatedAt != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (supplier != null ? supplier.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (number != null ? number.hashCode() : 0);
        result = 31 * result + (reportTitle != null ? reportTitle.hashCode() : 0);
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        result = 31 * result + (amounts != null ? amounts.hashCode() : 0);
        result = 31 * result + (approver != null ? approver.hashCode() : 0);
        result = 31 * result + (employee != null ? employee.hashCode() : 0);
        result = 31 * result + (fixedAsset != null ? fixedAsset.hashCode() : 0);
        result = 31 * result + (opportunity != null ? opportunity.hashCode() : 0);
        result = 31 * result + (relatedProject != null ? relatedProject.hashCode() : 0);
        result = 31 * result + (relatedPO != null ? relatedPO.hashCode() : 0);
        result = 31 * result + (accountsPayable != null ? accountsPayable.hashCode() : 0);
        result = 31 * result + (items != null ? items.hashCode() : 0);
        result = 31 * result + (isCompanyExpense != null ? isCompanyExpense.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (customFields != null ? customFields.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ExpenseDto{" +
                "id=" + id +
                ", supplier=" + supplier +
                ", date=" + date +
                ", number='" + number + '\'' +
                ", reportTitle='" + reportTitle + '\'' +
                ", currency=" + currency +
                ", amounts='" + amounts + '\'' +
                ", approver=" + approver +
                ", employee=" + employee +
                ", fixedAsset=" + fixedAsset +
                ", opportunity=" + opportunity +
                ", relatedProject=" + relatedProject +
                ", relatedPO=" + relatedPO +
                ", accountsPayable=" + accountsPayable +
                ", items=" + items +
                ", isCompanyExpense=" + isCompanyExpense +
                ", status='" + status + '\'' +
                ", customFields=" + customFields +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
