package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipTableItem;
import com.edatasite.workforce.gwt.core.client.rpc.ExpenseData;
import com.edatasite.workforce.gwt.core.client.ui.Frequency;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.payroll.client.rpc.SinglePayrunItem;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Dilsh0d Madrahimov on 06.02.2017.
 */
public class SinglePayrunTO implements IsSerializable {

    Integer id;
    Long processDate;
    UserTO employee;
    UserTO creator;
    UserTO approver;
    SelectItemTO frequency;
    SelectItemTO status;
    SelectItemTO currency;
    SelectItemTO baseCurrency;
    Integer month;
    Integer year;
    Long fromDate;
    Long toDate;
    SelectItemTO payMethod;

    ArrayList<PayrollCategoryTO> payments;
    ArrayList<PayrollCategoryTO> deductions;
    ArrayList<PayrollCategoryTO> taxes;
    ArrayList<PayrollCategoryTO> expenses;

    BigDecimal totalPayment = BigDecimal.ZERO;
    BigDecimal totalDeduction = BigDecimal.ZERO;
    BigDecimal totalTax = BigDecimal.ZERO;
    BigDecimal totalExpense = BigDecimal.ZERO;

    BigDecimal totalExpensePayment = BigDecimal.ZERO;
    BigDecimal totalExpenseDeduction = BigDecimal.ZERO;
    BigDecimal totalPension = BigDecimal.ZERO;

    BigDecimal total = BigDecimal.ZERO;

    boolean canApprove;
    boolean canSubmit;


    public SinglePayrunTO() {
    }

    public SinglePayrunTO(EdsPayslipTableItem item, Integer calculationScale) {
        this.id = item.getObjectID();
        this.employee = item.getEmployee() != null ? new UserTO(item.getEmployee().getObjectID(), item.getEmployee().getName()) : null;
        this.status = item.getStatus() != null ? new SelectItemTO(item.getStatus().getObjectID(), item.getStatus().getName(), item.getStatus().getCode(), "") : null;
        this.currency = item.getCurrency() != null ? new SelectItemTO(item.getCurrency().getObjectID(), item.getCurrency().getName(), item.getCurrency().getSymbol(), "") : null;
        this.year = item.getYear();
        this.month = item.getMonthID();
        this.total = WrapUtils.getTotal(item.getTotal(), calculationScale);
    }

    public SinglePayrunTO(SinglePayrunItem item, Integer calculationScale) {
        this.id = item.getObjectID();
        this.employee = new UserTO(item.getEmployeeID(), item.getEmployee());
        this.status = new SelectItemTO(item.getStatusID(), item.getStatus(), item.getStatusCode(), "");
        this.currency = item.getCurrency() != null ? new SelectItemTO(item.getCurrency().getId(), item.getCurrency().getName(), item.getCurrency().getSymbol(), "") : null;
        this.year = item.getYear();
        this.month = item.getMonthID();
        this.processDate = (item.getProcessDate() != null && item.getProcessDate().getNonConvertedDate() != null) ? WrapUtils.dateToLong(item.getProcessDate().getNonConvertedDate()) : null;
        this.creator = item.getCreator() != null ? new UserTO(item.getCreator().getId(), item.getCreator().getName()) : null;
        this.approver = item.getApprover() != null ? new UserTO(item.getApprover().getId(), item.getApprover().getName()) : null;
        if (item.getFrequency() != null) {
            Frequency frequencyEnum = Frequency.getByID(item.getFrequency());
            this.frequency = new SelectItemTO(frequencyEnum.getId(), frequencyEnum.getName(), frequencyEnum.getCode(), "");
        }
        this.fromDate = (item.getFromDate() != null && item.getFromDate().getNonConvertedDate() != null) ? WrapUtils.dateToLong(item.getFromDate().getNonConvertedDate()) : null;
        this.toDate = (item.getToDate() != null && item.getToDate().getNonConvertedDate() != null) ? WrapUtils.dateToLong(item.getToDate().getNonConvertedDate()) : null;
        this.payMethod = new SelectItemTO(item.getPayMethodId(), item.getPayMethodName());

        if (item.getPaymentCategories().size() > 0) {
            ArrayList<PayrollCategoryTO> payrollCategoryTOs = new ArrayList<>();
            for (PaymentDeductionObject payment : item.getPaymentCategories()) {
                payrollCategoryTOs.add(new PayrollCategoryTO(payment));
                totalPayment = totalPayment.add(payment.getAmount());
            }
            this.payments = payrollCategoryTOs;
        }
        if (item.getDeductionCategories().size() > 0) {
            ArrayList<PayrollCategoryTO> deductionCategoryTOs = new ArrayList<>();
            for (PaymentDeductionObject deduction : item.getDeductionCategories()) {
                deductionCategoryTOs.add(new PayrollCategoryTO(deduction));
                totalDeduction = totalDeduction.add(deduction.getAmount());
            }
            this.deductions = deductionCategoryTOs;
        }
        if (item.getTaxCategories().size() > 0) {
            ArrayList<PayrollCategoryTO> taxCategoryTOs = new ArrayList<>();
            for (PaymentDeductionObject deduction : item.getTaxCategories()) {
                taxCategoryTOs.add(new PayrollCategoryTO(deduction));
                totalTax = totalTax.add(deduction.getAmount());
            }
            this.taxes = taxCategoryTOs;
        }
        if (item.getEmployeeExpenses() != null && item.getEmployeeExpenses().getExpenses() != null && item.getEmployeeExpenses().getExpenses().length > 0) {
            ArrayList<PayrollCategoryTO> expenseTOs = new ArrayList<>();
            for (ExpenseData expense : item.getEmployeeExpenses().getExpenses()) {
                PayrollCategoryTO category = new PayrollCategoryTO();
                category.setCategory(new SelectItemTO(expense.getTitle()));
                category.setAmount(expense.getAmount() != null ? BigDecimal.valueOf(expense.getAmount()) : BigDecimal.ZERO);
                category.setAccount(new SelectItemTO(expense.getAccountID(), expense.getAccount()));
                category.setType(new SelectItemTO(expense.getPaymentType() == 0 ? "Payment" : "Deduction"));
                expenseTOs.add(category);
                if (expense.getPaymentType() == 0) {
                    totalExpensePayment = totalExpensePayment.add(category.getAmount());
                } else {
                    totalExpenseDeduction = totalExpenseDeduction.add(category.getAmount());
                }
                totalExpense = totalExpense.add(category.getAmount());
            }
            this.expenses = expenseTOs;
        }
        total = total.add(totalPayment);
        total = total.add(totalExpensePayment);
        total = total.subtract(totalExpenseDeduction);
        total = total.subtract(totalDeduction);
        total = total.subtract(totalTax);
        if (item.getPensionAmount() != null) {
            total = total.subtract(item.getPensionAmount());
            totalPension = item.getPensionAmount();
        }

        //set scale
        totalPayment = WrapUtils.getTotal(totalPayment, calculationScale);
        totalDeduction = WrapUtils.getTotal(totalDeduction, calculationScale);
        totalTax = WrapUtils.getTotal(totalTax, calculationScale);
        totalExpense = WrapUtils.getTotal(totalExpense, calculationScale);
        totalExpensePayment = WrapUtils.getTotal(totalExpensePayment, calculationScale);
        totalExpenseDeduction = WrapUtils.getTotal(totalExpenseDeduction, calculationScale);
        totalPension = WrapUtils.getTotal(totalPension, calculationScale);
        total = WrapUtils.getTotal(total, calculationScale);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getProcessDate() {
        return processDate;
    }

    public void setProcessDate(Long processDate) {
        this.processDate = processDate;
    }

    public UserTO getEmployee() {
        return employee;
    }

    public void setEmployee(UserTO employee) {
        this.employee = employee;
    }

    public UserTO getCreator() {
        return creator;
    }

    public void setCreator(UserTO creator) {
        this.creator = creator;
    }

    public UserTO getApprover() {
        return approver;
    }

    public void setApprover(UserTO approver) {
        this.approver = approver;
    }

    public SelectItemTO getFrequency() {
        return frequency;
    }

    public void setFrequency(SelectItemTO frequency) {
        this.frequency = frequency;
    }

    public SelectItemTO getStatus() {
        return status;
    }

    public void setStatus(SelectItemTO status) {
        this.status = status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public SelectItemTO getCurrency() {
        return currency;
    }

    public void setCurrency(SelectItemTO currency) {
        this.currency = currency;
    }

    public Long getFromDate() {
        return fromDate;
    }

    public void setFromDate(Long fromDate) {
        this.fromDate = fromDate;
    }

    public Long getToDate() {
        return toDate;
    }

    public void setToDate(Long toDate) {
        this.toDate = toDate;
    }

    public SelectItemTO getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(SelectItemTO payMethod) {
        this.payMethod = payMethod;
    }

    public ArrayList<PayrollCategoryTO> getPayments() {
        return payments;
    }

    public void setPayments(ArrayList<PayrollCategoryTO> payments) {
        this.payments = payments;
    }

    public ArrayList<PayrollCategoryTO> getDeductions() {
        return deductions;
    }

    public void setDeductions(ArrayList<PayrollCategoryTO> deductions) {
        this.deductions = deductions;
    }

    public ArrayList<PayrollCategoryTO> getTaxes() {
        return taxes;
    }

    public void setTaxes(ArrayList<PayrollCategoryTO> taxes) {
        this.taxes = taxes;
    }

    public ArrayList<PayrollCategoryTO> getExpenses() {
        return expenses;
    }

    public void setExpenses(ArrayList<PayrollCategoryTO> expenses) {
        this.expenses = expenses;
    }

    public BigDecimal getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(BigDecimal totalPayment) {
        this.totalPayment = totalPayment;
    }

    public BigDecimal getTotalExpensePayment() {
        return totalExpensePayment;
    }

    public void setTotalExpensePayment(BigDecimal totalExpensePayment) {
        this.totalExpensePayment = totalExpensePayment;
    }

    public BigDecimal getTotalExpenseDeduction() {
        return totalExpenseDeduction;
    }

    public void setTotalExpenseDeduction(BigDecimal totalExpenseDeduction) {
        this.totalExpenseDeduction = totalExpenseDeduction;
    }

    public BigDecimal getTotalDeduction() {
        return totalDeduction;
    }

    public void setTotalDeduction(BigDecimal totalDeduction) {
        this.totalDeduction = totalDeduction;
    }

    public BigDecimal getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(BigDecimal totalTax) {
        this.totalTax = totalTax;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(BigDecimal totalExpense) {
        this.totalExpense = totalExpense;
    }

    public BigDecimal getTotalPension() {
        return totalPension;
    }

    public void setTotalPension(BigDecimal totalPension) {
        this.totalPension = totalPension;
    }

    public SelectItemTO getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(SelectItemTO baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public boolean isCanApprove() {
        return canApprove;
    }

    public void setCanApprove(boolean canApprove) {
        this.canApprove = canApprove;
    }

    public boolean isCanSubmit() {
        return canSubmit;
    }

    public void setCanSubmit(boolean canSubmit) {
        this.canSubmit = canSubmit;
    }
}
