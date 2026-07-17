package com.edatasite.workforce.gwt.profile.client.ui.view.locking;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.locking.TransactionLockingModule;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import gwt.material.design.addins.client.iconmorph.MaterialIconMorph;
import gwt.material.design.client.constants.IconSize;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Heading;

public class TransactionLockingFormItem extends Composite {
    interface TransactionLockingFormItemUiBinder extends UiBinder<Div, TransactionLockingFormItem> {}
    private static TransactionLockingFormItemUiBinder uiBinder = GWT.create(TransactionLockingFormItemUiBinder.class);

    private final WfmStrings wfmStrings = WfmStrings.App.get();

    @UiField
    Div descriptionField;
    @UiField
    Heading title;
    @UiField
    Div figureImage;
    @UiField
    Div figureActions;
    private Div panel;
    private TransactionLockingModule item;

    TransactionLockingFormItem(final TransactionLockingModule item) {
        panel = uiBinder.createAndBindUi(this);
        initWidget(panel);
        this.item = item;
        ModuleDescription md = getLocale(item.getModule());
        title.setText(md.getModuleName());
        descriptionField.getElement().setInnerText(md.getDescription());

        MaterialIconMorph lockMorph = new MaterialIconMorph();
        lockMorph.setIconSize(IconSize.SMALL);
        lockMorph.setSource(new MaterialIcon(IconType.LOCK_OPEN));
        lockMorph.setTarget(new MaterialIcon(IconType.LOCK));
        if (!isLocked()) {
            lockMorph.getElement().toggleClassName("morphed");
        }
        lockMorph.addIconMorphedHandler(event -> {
            if (event.isMorphed()) {
                this.item.setStatus("unlocked");
            } else {
                this.item.setStatus("locked");
            }
        });
        figureActions.add(lockMorph);

        initHandler();
    }

    private void initHandler() {

    }

    private boolean isLocked() {
        return "locked".equals(item.getStatus());
    }

    public TransactionLockingModule getItem() {
        return item;
    }

    private ModuleDescription getLocale(String code) {
        switch (code) {
            case "sales" : return new ModuleDescription(wfmStrings.sales(), "In the Sales module, it means that all sales records up to that date are finalized. This ensures that invoices, sales orders, and customer details from previous periods cannot be changed, preserving the accuracy of sales data for financial reporting and analysis.");
            case "purchases" : return new ModuleDescription(wfmStrings.purchases(), "For the Purchases module, locking transactions by a certain date means no past purchase orders or supplier invoices before the cut-off date can be modified. This ensures the integrity of historical purchase data, crucial for financial audits and budgeting.");
            case "banking" : return new ModuleDescription("Banking", "In Banking, locking transactions by a specific date means that all banking activities, such as deposits, withdrawals, and transfers made before the cut-off date, are confirmed and unchangeable. This helps in accurate financial reconciliation and prevents retroactive alterations that could affect financial statements.");
            case "employees" : return new ModuleDescription(wfmStrings.employees(), "For the Employees module, locking transactions up to a certain date means that any changes to employee records, contracts, or personal details must be made after the cut-off date. This ensures that historical employee data remains consistent, which is important for HR management and compliance with labor laws.");
            case "attendance" : return new ModuleDescription(wfmStrings.attendanceTracking(), " In Attendance tracking, locking transactions by a certain date means no alterations can be made to attendance records before the cut-off date. This is critical for accurate calculation of wages, leave balances, and maintaining consistent attendance records.");
            case "recruitment" : return new ModuleDescription(wfmStrings.recruitmentOnly(), " In Recruitment, locking transactions by this date means all hiring activities and candidate records up to locked date, cannot be modified. This maintains the integrity and transparency of the recruitment process.");
            case "payslips" : return new ModuleDescription(wfmStrings.payslips(), " In the Payslips module, locking transactions up to a specific date means that all payslip entries before the locking date are finalized. This prevents retroactive changes to pay rates, hours worked, deductions, and ensures the accuracy of payroll records for both the employer and the employees.");
            case "cashAdvances" : return new ModuleDescription(wfmStrings.cashAdvance(), "For Cash Advances, transactions locked as of date, finalize all advances given up to that date. This prevents alterations and ensures clarity in financial obligations and repayments.");
            case "additionalPayments" : return new ModuleDescription(wfmStrings.additionalPayment(), "Locking transactions by cut-off date, means no changes can be made to additional payments like bonuses or overtime issued before this date. This ensures accuracy in payroll records and compliance with financial reporting.");
        }
        return null;
    }

    private static class ModuleDescription {
        String moduleName;
        String description;

        public ModuleDescription(String moduleName, String description) {
            this.moduleName = moduleName;
            this.description = description;
        }

        public String getModuleName() {
            return moduleName;
        }

        public String getDescription() {
            return description;
        }
    }
}
