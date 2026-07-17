package com.edatasite.workforce.gwt.core.client.ui.view.payslip;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.ui.view.LeavePaymentItem;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 3/24/13
 * Time: 8:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class PayslipItemAmountWidget extends FlexTable implements CustomCellInterface {

    private ExpenseLink expenseLink;
    private Integer[] relatedIDs;
    private Integer itemID;
    private Integer psdId;
    private Integer pensionType;
    private Integer companyPensionType;
    private Integer pensionValueType;
    private Integer leaveDeductType;
    private BigDecimal remainingAmount;
    private BigDecimal percentage;
    private BigDecimal pensionRate;
    private BigDecimal nonLocalPensionRate;
    private BigDecimal companyPensionRate;
    private BigDecimal companyNonLocalPensionRate;
    private BigDecimal companyPensionAmount;
    private BigDecimal empMaxTaxableAmount = BigDecimal.ZERO;
    private BigDecimal compMaxTaxableAmount = BigDecimal.ZERO;
    private BigDecimal numberOfWorkDays;
    private BigDecimal leaveDaysCount;
    private BigDecimal baseAmount;
    private BigDecimal gross;
    private boolean isLoan;
    private boolean isPercentage;
    private boolean calculatePension;
    private boolean isLocal;
    private boolean isFromAllAllowances;
    private boolean isUsed;
    private boolean isSalaryObject;
    private boolean isCashAdvance;

    private boolean isTaxable;
    private Object object;
    private Label percentageLabel;
    public TextBox amountTextBox;
    private LeavePaymentItem leavePaymentItem;
    private List<PaymentDeductionSelectItem> pensionAllowances;
    private List<PaymentDeductionObject> linkedCategories;
    private Integer type;
    private HandlerRegistration keyPressHandler;
    private PaymentDeductionObject petrollExcess;
    private ArrayList<Integer> sickRequestIds;

    private DateNonConvertable starttDate;
    private DateNonConvertable enddDate;

    private boolean editable = true;
    public PayslipItemAmountWidget() {
        super();
        amountTextBox = new TextBox();
        amountTextBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.checkToFocusTextBox(amountTextBox, Utils.getCalculationNumberFormat().format(BigDecimal.ZERO));
        addNumericKeyboardListener(Utils.getAccountingCalculationScale() != null ? Utils.getAccountingCalculationScale() : 2);
        percentageLabel = new Label("%");
        setWidget(0, 0, amountTextBox);
    }

    public PayslipItemAmountWidget(BigDecimal gross) {
        this();
        this.gross = gross;
    }

    @Override
    public String getDisplayValue() {
        return isPercentage ? Utils.getNumberFormat().format(getAmount()) + " %" : Utils.getCalculationNumberFormat().format(getAmount());
    }

    @Override
    public void setItemValue(Object value) {
        amountTextBox.setText(String.valueOf(value));
    }

    @Override
    public void setItemFocus(boolean focused) {
        amountTextBox.setFocus(focused);
    }

    public void setAmount(BigDecimal amount) {
        amountTextBox.setText(Utils.getNumberFormat().format(amount != null ? amount : BigDecimal.ZERO));
    }

    public BigDecimal getAmount() {
        String text = amountTextBox.getText();
        if (text != null && text.length() > 0) {
            String currencyCode = Utils.getParam(Utils.BASE_CURRENCY);
            if (currencyCode != null && text.startsWith(currencyCode)) {
                return new BigDecimal(Utils.getNumberFormat().parse(text.replace(currencyCode, "")));
            }
            return new BigDecimal(Utils.getNumberFormat().parse(text));
        }
        return BigDecimal.ZERO;
    }

    public void setExpenseLink(ExpenseLink expenseLink) {
        this.expenseLink = expenseLink;
        if (expenseLink != null) {
            setWidget(1, 0, expenseLink);
        } else if (getRowCount() > 1) {
            removeCell(1, 0);
        }
    }

    public void showPercentage(boolean show) {
        if (show) {
            isPercentage = true;
            setWidget(0, 1, percentageLabel);
            addNumericKeyboardListener(2);
        } else {
            isPercentage = false;
            if (isCellPresent(0, 1)) {
                removeCell(0, 1);
            }
            if (Utils.getAccountingCalculationScale() != null) {
                addNumericKeyboardListener(Utils.getAccountingCalculationScale());
            }
        }
    }

    public void addNumericKeyboardListener(final int scale) {
        if (keyPressHandler != null) {
            keyPressHandler.removeHandler();
        }
        keyPressHandler = amountTextBox.addKeyPressHandler(event -> {
            char key = event.getCharCode();
            if (Utils.isArabicLanguage()) {
                return;
            }
            if (key == (char) 0) {
                return;
            }
            String validateString = amountTextBox.getText().substring(amountTextBox.getText().lastIndexOf('.') + 1, amountTextBox.getText().length());
            if (amountTextBox.getText().contains(".") && (key == '.' || ((key != (char) KeyCodes.KEY_BACKSPACE) && (key != (char) KeyCodes.KEY_LEFT)
                    && (key != (char) KeyCodes.KEY_RIGHT) && (key != (char) KeyCodes.KEY_DELETE)
                    && (amountTextBox.getCursorPos() > amountTextBox.getText().lastIndexOf('.') && validateString.length() >= scale)))) {
                ((TextBox) event.getSource()).cancelKey();
                return;
            }

            if ((!Character.isDigit(key)) && (key != (char) KeyCodes.KEY_TAB)
                    && (key != (char) KeyCodes.KEY_BACKSPACE)
                    && (key != (char) KeyCodes.KEY_DELETE) && (key != (char) KeyCodes.KEY_ENTER)
                    && (key != (char) KeyCodes.KEY_HOME) && (key != (char) KeyCodes.KEY_END)
                    && (key != (char) KeyCodes.KEY_LEFT) && (key != (char) KeyCodes.KEY_UP)
                    && (key != (char) KeyCodes.KEY_RIGHT) && (key != (char) KeyCodes.KEY_DOWN)
                    && (key != '.') && (key != ',')) {
                ((TextBox) event.getSource()).cancelKey();
            }
        });
    }

    public void setAmountEnabled(boolean enabled) {
        amountTextBox.setEnabled(enabled);
    }

    public PaymentDeductionObject getPetrollExcess() {
        return petrollExcess;
    }

    public void setPetrollExcess(PaymentDeductionObject petrollExcess) {
        this.petrollExcess = petrollExcess;
    }

    public boolean isAmountValid() {
        return amountTextBox.getText() != null && !amountTextBox.getText().trim().isEmpty();
    }

    public TextBox getAmountTextBox() {
        return amountTextBox;
    }

    public ExpenseLink getExpenseLink() {
        return expenseLink;
    }

    public Integer[] getRelatedIDs() {
        return relatedIDs;
    }

    public void setRelatedIDs(Integer[] relatedIDs) {
        this.relatedIDs = relatedIDs;
    }

    public Integer getLeaveDeductType() {
        return leaveDeductType;
    }

    public void setLeaveDeductType(Integer leaveDeductType) {
        this.leaveDeductType = leaveDeductType;
    }

    public BigDecimal getNumberOfWorkDays() {
        return numberOfWorkDays;
    }

    public void setNumberOfWorkDays(BigDecimal numberOfWorkDays) {
        this.numberOfWorkDays = numberOfWorkDays;
    }

    public BigDecimal getLeaveDaysCount() {
        return leaveDaysCount;
    }

    public void setLeaveDaysCount(BigDecimal leaveDaysCount) {
        this.leaveDaysCount = leaveDaysCount;
    }

    public Integer getItemID() {
        return itemID;
    }

    public void setItemID(Integer itemID) {
        this.itemID = itemID;
    }

    public Integer getPsdId() {
        return this.psdId;
    }

    public void setPsdId(final Integer psdId) {
        this.psdId = psdId;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public boolean isSalaryObject() {
        return isSalaryObject;
    }

    public void setSalaryObject(boolean salaryObject) {
        isSalaryObject = salaryObject;
    }

    public boolean isCashAdvance() {
        return isCashAdvance;
    }

    public void setCashAdvance(boolean cashAdvance) {
        isCashAdvance = cashAdvance;
    }

    public boolean isTaxable() {
        return isTaxable;
    }

    public void setTaxable(boolean taxable) {
        isTaxable = taxable;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public boolean isLoan() {
        return isLoan;
    }

    public void setLoan(boolean loan) {
        isLoan = loan;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public BigDecimal getPensionRate() {
        return pensionRate;
    }

    public void setPensionRate(BigDecimal pensionRate) {
        this.pensionRate = pensionRate;
    }

    public BigDecimal getNonLocalPensionRate() {
        return nonLocalPensionRate;
    }

    public void setNonLocalPensionRate(BigDecimal nonLocalPensionRate) {
        this.nonLocalPensionRate = nonLocalPensionRate;
    }

    public Integer getPensionType() {
        return pensionType;
    }

    public void setPensionType(Integer pensionType) {
        this.pensionType = pensionType;
    }

    public Integer getPensionValueType() {
        return pensionValueType;
    }

    public void setPensionValueType(Integer pensionValueType) {
        this.pensionValueType = pensionValueType;
    }

    public BigDecimal getCompanyPensionRate() {
        return companyPensionRate;
    }

    public void setCompanyPensionRate(BigDecimal companyPensionRate) {
        this.companyPensionRate = companyPensionRate;
    }

    public BigDecimal getCompanyPensionAmount() {
        return companyPensionAmount;
    }

    public void setCompanyPensionAmount(BigDecimal companyPensionAmount) {
        this.companyPensionAmount = companyPensionAmount;
    }

    public boolean isPercentage() {
        return isPercentage;
    }

    public void setPercentage(boolean percentage) {
        isPercentage = percentage;
    }

    public boolean isCalculatePension() {
        return calculatePension;
    }

    public void setCalculatePension(boolean calculatePension) {
        this.calculatePension = calculatePension;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    public Label getPercentageLabel() {
        return percentageLabel;
    }

    public void setPercentageLabel(Label percentageLabel) {
        this.percentageLabel = percentageLabel;
    }

    public Integer getCompanyPensionType() {
        return companyPensionType;
    }

    public void setCompanyPensionType(Integer companyPensionType) {
        this.companyPensionType = companyPensionType;
    }

    public List<PaymentDeductionSelectItem> getPensionAllowances() {
        return pensionAllowances;
    }

    public void setPensionAllowances(List<PaymentDeductionSelectItem> pensionAllowances) {
        this.pensionAllowances = pensionAllowances;
    }

    public boolean isFromAllAllowances() {
        return isFromAllAllowances;
    }

    public void setFromAllAllowances(boolean fromAllAllowances) {
        isFromAllAllowances = fromAllAllowances;
    }

    public List<PaymentDeductionObject> getLinkedCategories() {
        return linkedCategories;
    }

    public void setLinkedCategories(List<PaymentDeductionObject> linkedCategories) {
        this.linkedCategories = linkedCategories;
    }

    public BigDecimal getCompanyNonLocalPensionRate() {
        return companyNonLocalPensionRate;
    }

    public void setCompanyNonLocalPensionRate(BigDecimal companyNonLocalPensionRate) {
        this.companyNonLocalPensionRate = companyNonLocalPensionRate;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        amountTextBox.setEnabled(editable);
    }

    public BigDecimal getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(BigDecimal baseAmount) {
        this.baseAmount = baseAmount;
    }

    public void updateAmount(BigDecimal exchangeRate) {
        if (baseAmount != null) {
            setAmount(baseAmount.multiply(exchangeRate));
        }
    }

    public BigDecimal getEmpMaxTaxableAmount() {
        return empMaxTaxableAmount;
    }

    public void setEmpMaxTaxableAmount(BigDecimal empMaxTaxableAmount) {
        this.empMaxTaxableAmount = empMaxTaxableAmount;
    }

    public BigDecimal getCompMaxTaxableAmount() {
        return compMaxTaxableAmount;
    }

    public void setCompMaxTaxableAmount(BigDecimal compMaxTaxableAmount) {
        this.compMaxTaxableAmount = compMaxTaxableAmount;
    }

    public ArrayList<Integer> getSickRequestIds() {
        if (sickRequestIds == null) {
            sickRequestIds = new ArrayList<>();
        }
        return sickRequestIds;
    }

    public void setSickRequestIds(ArrayList<Integer> sickRequestIds) {
        this.sickRequestIds = sickRequestIds;
    }

    public LeavePaymentItem getLeavePaymentItem() {
        return leavePaymentItem;
    }

    public void setLeavePaymentItem(LeavePaymentItem leavePaymentItem) {
        this.leavePaymentItem = leavePaymentItem;
    }

    public BigDecimal getGross() {
        return gross;
    }

    public void setGross(BigDecimal gross) {
        this.gross = gross;
    }

    public DateNonConvertable getStarttDate() {
        return this.starttDate;
    }

    public void setStarttDate(final DateNonConvertable starttDate) {
        this.starttDate = starttDate;
    }

    public DateNonConvertable getEnddDate() {
        return this.enddDate;
    }

    public void setEnddDate(final DateNonConvertable enddDate) {
        this.enddDate = enddDate;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(final Integer type) {
        this.type = type;
    }
}
