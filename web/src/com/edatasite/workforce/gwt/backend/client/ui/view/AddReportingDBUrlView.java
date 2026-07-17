package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.ReportingDBUrlListItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TeamEmployees;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.selectPanel.SelectPanel;
import com.edatasite.workforce.gwt.core.client.ui.table.TableColumn;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.NTreeSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.TreeSelect;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Virus
 * Date: 4/30/12
 * Time: 4:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddReportingDBUrlView extends View {

    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private WfmForm.Field dbUrlField;
    private TextBox dbUrlTextBox;
    private WfmForm.Field userNameField;
    private TextBox userNameTextBox;
    private WfmForm.Field passwordField;
    private TextBox passwordTextBox;
    private WfmForm table;
    private WfmButton2 saveAndClose;
    private Integer objectID;
    private SelectPanel sharedsTree;
    private static final CoreServiceAsync coreService = CoreService.App.get();
    private WfmForm companyForm;
    private WfmForm.Field companyField;
    private FlexTable mainPanel;
    private KpiCheckBox isCustom;
    private WfmForm.Field isCustomField;
    private ReportingDBUrlListItem item;
    private final TableColumn[] columns = new TableColumn[2];


    public AddReportingDBUrlView() {
        super("addreportingdburl", "Add/Edit Reporting DB URL");
        super.setDescription(wfmStrings.add() + "&nbsp;" + backendStrings.dbUrl());
    }

    public AddReportingDBUrlView(Integer objectID) {
        this();
        this.objectID = objectID;
        this.getElement().addClassName("addClassName");
    }

    @Override
    protected Widget onInitialize() {
        table = new WfmForm();
        dbUrlTextBox = new TextBox();
        userNameTextBox = new TextBox();
        passwordTextBox = new TextBox();
        columns[0] = new TableColumn("company", wfmStrings.company(), 150);
        columns[1] = new TableColumn("delete", wfmStrings.action(), 50);
        sharedsTree = new SelectPanel(columns);
//        sharedsTree.setTreePanelWidth(190);
        sharedsTree.setHeight(240);
//        sharedsTree.setSearchBoxWidth("132px");
        sharedsTree.setSearchText(wfmStrings.searchCompany());
//        sharedsTree.setTableWidth(200);
        sharedsTree.hideAvailablityCheckBox();
        companyForm = new WfmForm();
        companyForm.setVisible(false);
        companyField = companyForm.addField(wfmStrings.company(), sharedsTree, true);
        getCustomCheckBox();
        getSaveButton();

        mainPanel = new FlexTable();
        mainPanel.setWidget(1, 1, table);
        mainPanel.getFlexCellFormatter().setVerticalAlignment(1, 1, HasVerticalAlignment.ALIGN_TOP);
        mainPanel.setWidget(1, 2, companyForm);
        add(mainPanel);

        dbUrlField = table.addField(wfmStrings.urlname(), dbUrlTextBox, true);
        userNameField = table.addField(wfmStrings.username(), userNameTextBox, true);
        passwordField = table.addField(wfmStrings.password(), passwordTextBox, true);
        isCustomField = table.addField(wfmStrings.custom(), isCustom, false);
        table.addButton(saveAndClose);
        table.addStyleName("editReportingDBTableLeft");

        getReportingDBUrl();
        getCompanies();

        return null;
    }

    private void getSaveButton() {
        saveAndClose = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveAndClose.addClickHandler(event -> {
            if (item == null) {
                item = new ReportingDBUrlListItem();
            }
            item.setDbUrl(dbUrlTextBox.getText());
            item.setUserName(userNameTextBox.getText());
            item.setPassword(passwordTextBox.getText());
            if (isCustom.getValue()) {
                for (Integer companyID : sharedsTree.getSelectedItems()) {
                    item.getCompany().add(new SelectItem(companyID, ""));
                }
            } else {
                item.getCompany().clear();
            }

            coreService.saveReportingDBUrl(item, new AsyncCallback<Void>() {

                @Override
                public void onFailure(Throwable throwable) {
                    Info.show(wfmStrings.error());
                }

                @Override
                public void onSuccess(Void aVoid) {
                    closeTab();
                }
            });
        });
    }

    private void getCustomCheckBox() {
        isCustom = new KpiCheckBox();
        isCustom.addValueChangeHandler(booleanValueChangeEvent -> companyForm.setVisible(booleanValueChangeEvent.getValue()));
    }

    private void getReportingDBUrl() {
        if (objectID != null) {
            LoadingPanel.loading(true);
            coreService.getReportingDBUrl(objectID, new AsyncCallback<ReportingDBUrlListItem>() {
                @Override
                public void onFailure(Throwable throwable) {
                }

                @Override
                public void onSuccess(ReportingDBUrlListItem result) {
                    item = result;
                    dbUrlTextBox.setText(result.getDbUrl());
                    userNameTextBox.setText(result.getUserName());
                    passwordTextBox.setText(result.getPassword());
                    if (result.getCompany() == null || result.getCompany().size() == 0) {
                        isCustom.setValue(false);
                    } else {
                        isCustom.setValue(true, true);
                    }
                    LoadingPanel.loading(false);
                }
            });
        }
    }

    private void getCompanies() {
        LoadingPanel.loading(true);
        coreService.getReportingDBUrlCompanies(objectID, new AsyncCallback<ArrayList<TeamEmployees>>() {
            @Override
            public void onFailure(Throwable throwable) {
                Info.warn(wfmStrings.sorrySomethingWentWrong());
            }

            @Override
            public void onSuccess(ArrayList<TeamEmployees> result) {
                loadTree(result);
            }
        });
    }

    private void loadTree(ArrayList<TeamEmployees> companyList) {
        TreeSelect.setTickAllVisible(companyList.size() != 0);
        for (TeamEmployees teamEmployee : companyList) {
            sharedsTree.addTreeItem(teamEmployee.getTeam(), teamEmployee.getMembers());
        }
        for (int i = 0; i < sharedsTree.getTree().getItemCount(); i++) {
            NTreeSelectItem parent = (NTreeSelectItem) sharedsTree.getTree().getItem(i);
            for (int j = 0; j < parent.getChildCount(); j++) {
                NTreeSelectItem child = (NTreeSelectItem) parent.getChild(j);
                for (WfmTreeItem company : companyList.get(0).getMembers()) {
                    if (child.getItem().getId().equals(company.getId()) && company.isChecked()) {
                        child.setChecked(true);
                        sharedsTree.onTreeItemSelection(child, null);
                        break;
                    }
                }
            }
        }
        sharedsTree.expandTreeView();
        LoadingPanel.loading(false);
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                callback.onFailure(reason);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
