package com.edatasite.workforce.gwt.backend.client.ui.view.tools;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.SavedReportTemplate;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Virus
 * Date: 12.12.12
 * Time: 16:00
 * To change this template use File | Settings | File Templates.
 */
public class SavedReportColumnsChange extends View {
    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private DataListBox reportTemplateListBox = new DataListBox();
    private TextBox columnTextBox = new TextBox();
    private TextBox changedColumnTextBox = new TextBox();
    private TextBox companyTextBox = new TextBox();
    private WfmForm table;
    private WfmForm.Field reportTemplateListBoxField;
    private WfmForm.Field columnTextBoxField;
    private WfmForm.Field changedColumnTextBoxField;
    private WfmForm.Field companyColumnTextBoxField;
    private WfmForm.Field typeSelectListBoxField;
    private FlexTable mainPanel;
    private Button applyButton;
    private DataListBox typeSelectListBox = new DataListBox();

    public SavedReportColumnsChange() {
        super("savedReportColumnsChange", backendStrings.savedReportColumnsChange());
//        super.setDescription(wfmStrings.add() + "&nbsp;" + wfmStrings.dbUrl());
    }

    @Override
    public String getIconStyle() {
        return "backend reportXmlTemListView";
    }

    @Override
    protected Widget onInitialize() {
        table = new WfmForm();
        mainPanel = new FlexTable();
        applyButton = new Button(wfmStrings.apply());
        applyButton.addStyleName("btn btn--primary");
        mainPanel.setWidget(1, 1, table);
        add(mainPanel);
        getTypeSelectListBox();
        reportTemplateListBoxField = table.addField(wfmStrings.template(), reportTemplateListBox, true);
        typeSelectListBoxField = table.addField(wfmStrings.select(), typeSelectListBox, true);
        columnTextBoxField = table.addField(wfmStrings.columns(), columnTextBox, true);
        changedColumnTextBoxField = table.addField(wfmStrings.to(), changedColumnTextBox, true);
        companyColumnTextBoxField = table.addField(wfmStrings.company(), companyTextBox, false);
        table.addButton(applyButton);
        LoadingPanel.loading(true);
        CoreService.App.get().getReportTemplates(null, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(SelectItem[] result) {
                reportTemplateListBox.setItems(result);
                LoadingPanel.loading(false);
            }
        });
        applyButton.addClickHandler(event -> {
            SavedReportTemplate item = new SavedReportTemplate();
            item.setObjectID(reportTemplateListBox.getSelectedItem().getId());
            item.setFrom(columnTextBox.getText());
            item.setTo(changedColumnTextBox.getText());

            item.setCompanies(companyTextBox.getText().split(","));
            item.setType(typeSelectListBox.getSelectedItem().getId());
            item.setResponse("");
            CoreService.App.get().savedReportChange(item, new AsyncCallback<SavedReportTemplate>() {

                @Override
                public void onFailure(Throwable caught) {

                }

                @Override
                public void onSuccess(SavedReportTemplate result) {
                    Info.show(result.getResponse());
                }
            });
        });
        WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OK, wfmStrings.payAttantionForImportantPage());
        messageBox.open();
        return null;
    }

    private void getTypeSelectListBox() {
        typeSelectListBox.setAllowFirstItem(true);
        typeSelectListBox.addListItem(new SelectItem(0, wfmStrings.select() + " " + wfmStrings.columns()));
        typeSelectListBox.addListItem(new SelectItem(1, wfmStrings.select() + " " + wfmStrings.filters()));
        typeSelectListBox.addListItem(new SelectItem(2, wfmStrings.select() + " " + wfmStrings.groups()));
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
            }

            @Override
            public void onSuccess() {
                onInitialize();
            }
        });
    }
}