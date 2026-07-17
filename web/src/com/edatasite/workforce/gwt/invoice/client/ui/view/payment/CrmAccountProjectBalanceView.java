package com.edatasite.workforce.gwt.invoice.client.ui.view.payment;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.grayForm.GrayForm;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CrmAccountLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ProjectLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProjectAllocateData;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 11/28/11
 * Time: 5:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrmAccountProjectBalanceView extends View {
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private CrmAccountLookUp crmAccountLookUp;
    private HTML unAllocatedAmount;
    private ProjectLookUp projectLookUp;
    private ProjectBalanceTextBox projectBalance;
    private Button saveButton;

    private WfmForm mainForm;
    private GrayForm grayForm;

    private WfmForm.Field crmAccountField;
    private WfmForm.Field projectField;

    private final Integer crmAccountID;

    public CrmAccountProjectBalanceView(Integer crmAccountID) {
        super("balanceView", accountingStrings.customerProjectBalance());
        this.crmAccountID = crmAccountID;
    }

    @Override
    protected Widget onInitialize() {
        crmAccountLookUp = new CrmAccountLookUp(CrmAccountLookUp.CUSTOMER, true);
        unAllocatedAmount = new HTML(AccountingUtils.get().formatPrice(AccountingConstants.ZERO));
        projectLookUp = new ProjectLookUp(Constants.RECEIVABLE);
        projectBalance = new ProjectBalanceTextBox();
        saveButton = new Button(wfmStrings.save());

        crmAccountLookUp.getSuggestBox().setWidth("200px");
        projectLookUp.getSuggestBox().setWidth("200px");

        Validation.addNumericKeyboardListener(projectBalance, 2);
        projectBalance.setAlignment(ValueBoxBase.TextAlignment.RIGHT);

        crmAccountLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> onCrmAccountChange());
        projectLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            LoadingPanel.loading(true);
            ProjectAllocateData data = new ProjectAllocateData();
            data.setCrmAccount(crmAccountLookUp.getSelectedItem());
            data.setProject(projectLookUp.getSelectedItem());
            InvoiceService.App.get().getCrmAccountProjectBalance(data, new AsyncCallback<ProjectAllocateData>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(ProjectAllocateData data) {
                    LoadingPanel.loading(false);
                    projectBalance.setBalance(data.getAmount());
                }
            });
        });

        saveButton.addClickHandler(clickEvent -> {
            if (!validateAllocation()) {
                return;
            }
            saveButton.setEnabled(false);
            LoadingPanel.loading(true);
            ProjectAllocateData data = new ProjectAllocateData();
            data.setCrmAccount(crmAccountLookUp.getSelectedItem());
            data.setProject(projectLookUp.getSelectedItem());
            data.setAmount(projectBalance.getDifference());
            InvoiceService.App.get().saveCrmAccountProjectBalance(data, new AsyncCallback<Void>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    Info.show(accountingMessages.errorOccuredWhileSavingProjectBalance(), Info.Type.WARNING);
                    saveButton.setEnabled(true);
                }

                @Override
                public void onSuccess(Void aVoid) {
                    LoadingPanel.loading(false);
                    saveButton.setEnabled(true);

                    BigDecimal unAllocated = AccountingUtils.get().parseToBigDecimal(unAllocatedAmount.getText());
                    unAllocated = unAllocated.subtract(projectBalance.getDifference());
                    unAllocatedAmount.setText(AccountingUtils.get().formatPrice(unAllocated));

                    projectLookUp.clear();
                    projectBalance.setBalance(AccountingConstants.ZERO);
                }
            });
        });

        mainForm = new WfmForm();
        mainForm.setLabelSize("150px");
        crmAccountField = mainForm.addField(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()), crmAccountLookUp, true);
        mainForm.addField(accountingStrings.unallocatedAmount(), unAllocatedAmount);
        projectField = mainForm.addField(Property.get(Constants.PROJECT, wfmStrings.project()), projectLookUp, true);
        mainForm.addField(accountingStrings.projectBalance(), projectBalance, true);
        mainForm.addButton(saveButton);

        grayForm = new GrayForm();
        grayForm.setWidth("700px");
        grayForm.addBookmark(accountingStrings.customerProjectBalance());
        grayForm.addInnerPanel().add(mainForm);
        add(grayForm);

        return null;
    }

    private boolean validateAllocation() {
        int errors = 0;
        if (!Validation.validateLookUpRequired(crmAccountLookUp, crmAccountField, accountingStrings.pleaseSelectCustomer())) {
            errors++;
        }
        if (!Validation.validateLookUpRequired(projectLookUp, projectField, wfmStrings.pleaseSelectProject())) {
            errors++;
        }
        if (errors > 0) {
            Info.show(wfmStrings.fillRequiredField(), Info.Type.WARNING);
            return false;
        }

        if (projectBalance.getDifference().compareTo(AccountingUtils.get().parseToBigDecimal(unAllocatedAmount.getText())) > 0) {
            Info.show(accountingStrings.youCantAllocateMoreThanUnallocatedAmount(), Info.Type.WARNING);
            return false;
        }

        return true;
    }

    private void onCrmAccountChange() {
        projectLookUp.clear();
        projectLookUp.setClientSupplierID(crmAccountLookUp.getSelectedItemID());
        projectBalance.setText(AccountingUtils.get().formatPrice(AccountingConstants.ZERO));
        if (crmAccountLookUp.getSelectedItemID() != null) {
            LoadingPanel.loading(true);
            ProjectAllocateData data = new ProjectAllocateData();
            data.setCrmAccount(crmAccountLookUp.getSelectedItem());
            InvoiceService.App.get().getCrmAccountProjectBalance(data, new AsyncCallback<ProjectAllocateData>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(ProjectAllocateData data) {
                    LoadingPanel.loading(false);
                    unAllocatedAmount.setText(AccountingUtils.get().formatPrice(data.getAmount()));
                }
            });
        }
    }

    public class ProjectBalanceTextBox extends TextBox {
        private BigDecimal balance = AccountingConstants.ZERO;

        public ProjectBalanceTextBox() {
            Validation.addNumericKeyboardListener(this);
        }

        public void setBalance(BigDecimal amount) {
            this.balance = amount;
            setText(AccountingUtils.get().formatPrice(amount));
        }

        public BigDecimal getDifference() {
            return AccountingUtils.get().parseToBigDecimal(projectBalance.getText()).subtract(balance);
        }
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
