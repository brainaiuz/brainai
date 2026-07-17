package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.SolutionItem;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Created by IntelliJ IDEA.
 * User: Azazello
 * Date: Apr 13, 2018
 * Time: 8:24:10 PM
 */
public class CloseCaseView extends KpiModal {
    private static final CrmStrings crmStrings = CrmStrings.App.get();
    private CaseItem caseItem;
    private DataListBox status;
    private DataListBox reason;
    private TextBox title;
    private TextArea comments;
    private TextArea details;
    private WfmButton2 save;
    private Command command;

    public CloseCaseView(CaseItem caseItem, Command command) {
        this.caseItem = caseItem;
        this.command = command;
        setWidth(700);
        setTitle(Property.get(Constants.CASE_LIST, crmStrings.closeCase(), wfmStrings.caseID()));
        init();
        getData();
    }

    public void init() {
        status = new DataListBox();
        reason = new DataListBox();

        comments = new TextArea();
        comments.setHeight("8em");

        title = new TextBox();

        details = new TextArea();
        details.setHeight("8em");

        addWidget(status, wfmStrings.status());
        addWidget(reason, wfmStrings.reason());
        addWidget(comments, wfmStrings.comments());

        addWidget(title, crmStrings.solutionTitle());
        addWidget(details, crmStrings.solutionDetails());

        addButton(new WfmButton2(wfmStrings.cancel(), clickEvent -> close()));
        save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> save());
        addButton(save);
        open();
    }

    private void getData() {
        CRMService.App.get().getCaseStatusLisItems(new AbstractAsyncCallback<SelectItem[]>() {
            public void success(final SelectItem[] items) {
                status.setItems(items);
                for (SelectItem item : items) {
                    if ((item).getName().toLowerCase().contains("close")) {
                        status.setSelected(item.getId());
                    }
                }
            }
        });
        CRMService.App.get().getCaseReasonItems(new AbstractAsyncCallback<SelectItem[]>() {
            public void success(final SelectItem[] items) {
                reason.setItems(items);
                for (SelectItem item : items) {
                    if ((item).getName().equals(caseItem.getCaseReason())) {
                        reason.setSelected(item.getId());
                    }
                }
            }
        });
        title.setText(caseItem.getSubject());
    }

    public boolean validate() {
        int errors = 0;
        if (!Validation.validateDataListBoxRequired(status)) {
            errors++;
        }
        if (!Validation.validateDataListBoxRequired(reason)) {
            errors++;
        }
        if (!Validation.validateTextBoxRequired(title)) {
            errors++;
        }
        if (errors > 0) {
            Info.warn(wfmStrings.sureEnteredAllData());
            return false;
        }
        return true;
    }

    private void save() {
        if (!validate()) {
            return;
        }
        caseItem.setStatus(status.getSelectedItem());
        caseItem.setCaseReasonId(reason.getSelectedItem().getId());
        caseItem.setInternalComment(comments.getText());

        SolutionItem solItem = new SolutionItem();
        solItem.setTitle(title.getText());
        solItem.setDetails(details.getText());

        LoadingPanel.loading(true);
        CRMService.App.get().saveCaseAndSolution(caseItem, solItem, new AbstractAsyncCallback<Void>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.warn(wfmStrings.someErrorsOccurred());
            }

            public void success(final Void o) {
                LoadingPanel.loading(false);
                close();
                if(command != null){
                    command.execute();
                }
                Info.show(Property.get(Constants.CASE_LIST, crmStrings.messCaseSavedWithSolution(), wfmStrings.caseID()));
            }
        });
    }
}
