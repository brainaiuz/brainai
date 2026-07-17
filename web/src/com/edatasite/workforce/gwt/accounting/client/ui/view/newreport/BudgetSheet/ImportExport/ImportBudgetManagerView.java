package com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.BudgetSheet.ImportExport;

import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.importfile.client.ImportAbstractView;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;


public class ImportBudgetManagerView extends ImportAbstractView implements Constants {

    private DataListBox dateListBox;
    private DataListBox nameListBox;

    private Integer budgetId;

    public ImportBudgetManagerView(Integer objectID, Integer budgetId) {
        super("add", wfmStrings.importBudgetManager());
        this.objectId = objectID;
        this.budgetId = budgetId;
    }

    @Override
    protected ViewName getViewName() {
        return null;
    }


    public void initialize() {
        initInternal();
        super.initialize();
    }


    protected void initInternal() {

        nameListBox = new DataListBox();
        nameListBox.addStyleName(DEFAULT_WIDTH);

        dateListBox = new DataListBox();
        dateListBox.addStyleName(DEFAULT_WIDTH);

    }

    @Override
    public void drawForm() {
        super.drawForm();
        addField(ManualTransactionImport.NAME, nameListBox, getTitle(wfmStrings.name()));
        addField(ManualTransactionImport.DATE, dateListBox, getTitle(wfmStrings.date()));
    }

    @Override
    public void setItems(SelectItem[] items) {

        dateListBox.setItems(items, wfmStrings.date());
        nameListBox.setItems(items, wfmStrings.name());
        LoadingPanel.loading(false);
    }

    @Override
    protected ImportFile getImportFile() {
        ImportFile importFile = new ImportFile();
        importFile.setFileID(objectId);
        importFile.setBudgetID(budgetId);
        return importFile;
    }


    @Override
    protected ImportTypeEnum getType() {
        return ImportTypeEnum.BUDGET_MANAGER;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.IMPORT_BUDGET_MANAGER_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.IMPORT;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
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
