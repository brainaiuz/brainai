package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.ui.HTML;
import gwt.material.design.client.ui.html.Div;


public class DynamicBankAccountListBox extends FormGroup {
    private AccountingStrings accountingStrings = AccountingStrings.App.get();
    private WfmStrings wfmStrings = WfmStrings.App.get();
    private DataListBox bankAccounts;
    private HTML namePanel;
    private InputGroup inputGroup;
    private Div appendableDiv;
    private WfmButton2 editButton;
    private WfmButton2 saveButton;
    private WfmButton2 cancelButton;
    public DynamicBankAccountListBox(SelectItem bankAccountItem, ISaveBankAccount saveCommand) {
        setLabel(wfmStrings.bankDetails());
        bankAccounts = new DataListBox();
        bankAccounts.addStyleName("form-control");
        namePanel = new HTML();
        namePanel.addStyleName("form-control");
        inputGroup = new InputGroup(namePanel);
        appendableDiv = new Div("input-group-append");
        editButton = new WfmButton2(wfmStrings.edit(), WfmButton2.BTN_WHITE, e -> {
            activateEditMode();
        });
        cancelButton = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_GREY, e -> {
            activateViewMode();
        });
        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, e -> {
            saveCommand.save(bankAccounts.getSelectedItem());
            setSelected(bankAccounts.getSelectedItem());
            activateViewMode();
        });
        setSelected(bankAccountItem);
        activateViewMode();
        setContent(inputGroup);
    }

    private void activateViewMode() {
        inputGroup.clear();
        appendableDiv.clear();
        inputGroup.add(namePanel);
        appendableDiv.add(editButton);
        inputGroup.add(appendableDiv);
    }

    private void activateEditMode() {
        inputGroup.clear();
        appendableDiv.clear();

        AccountingService.App.get().getBankAccountItemsForReference(new AbstractAsyncCallback<SelectItem[]>() {
            public void failure(Throwable caught) {
            }

            public void success(SelectItem[] result) {
                bankAccounts.setItems(result);
            }
        });

        inputGroup.add(bankAccounts);

        appendableDiv.add(saveButton);
        appendableDiv.add(cancelButton);

        inputGroup.add(appendableDiv);
    }

    private void setSelected(SelectItem bankAccountItem) {
        if (bankAccountItem == null) {
            namePanel.setText("");
            bankAccounts.clear();
            return;
        }
        namePanel.setText(bankAccountItem.getName());
        bankAccounts.clear();
        bankAccounts.addItem(bankAccountItem);
        bankAccounts.setSelected(bankAccountItem);
    }

    public interface ISaveBankAccount {
        void save(SelectItem bankAccountItem);
    }
}