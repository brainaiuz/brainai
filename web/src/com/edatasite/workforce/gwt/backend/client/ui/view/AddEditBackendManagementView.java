package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendManagementListItem;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendServiceAsync;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.customlist.CustomList;
import com.edatasite.workforce.gwt.core.client.ui.customlist.CustomListItem;
import com.edatasite.workforce.gwt.core.client.ui.customlist.Design;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

//import java.net.InetAddress;
//import java.net.UnknownHostException;

/**
 * User: Ilhombek
 * Date: 4/23/12
 * Time: 5:28 PM
 */
public class AddEditBackendManagementView extends View {

    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private static final BackendServiceAsync backendService = BackendService.App.get();
    private Integer companyID, objectID;
    private String companyName;
    private FlexTable generalTable;
    private DataListBox userListBox;
    private CustomList hostNameBox;
    private TextBox promotionCode;
    private KpiCheckBox enableSalesBackend, enableSupportBackend, enableAdminBackend, /*enablePDFBackend,*/
            enableDeveloperBackend,enablePartnerAdminBackend;
    private BackendManagementListItem backendManagementListItem;
    private KpiCheckBox checkAll;

    public AddEditBackendManagementView(String companyID, String companyName, String objectID) {
        super("add", objectID == null ? backendStrings.addBackendOptions() : "Edit Backend Options");
        this.companyID = Integer.parseInt(companyID);
        this.objectID = objectID == null ? null : Integer.parseInt(objectID);
        this.companyName = companyName;
    }

    @Override
    public String getIconStyle() {
        return "icon-addBackendManagementView";
    }

    @Override
    protected Widget onInitialize() {
        generalTable = new FlexTable();
        generalTable.setCellPadding(15);
        generalTable.setCellSpacing(15);
        drawInitialize();
        return null;
    }

    private void drawInitialize() {

        generalTable.setHTML(0, 0, "<h2><b class=customTitle style='font-size:14px;'>" + backendStrings.enableBackendOptions() + "</b></h2>");
        generalTable.getFlexCellFormatter().setColSpan(0, 0, 3);
        generalTable.addStyleName("EnableBackendOptions-table file--AddEditBackendManagementView");
        userListBox = new DataListBox();
        userListBox.addStyleName(Constants.DEFAULT_WIDTH);

        backendService.getCompanyActiveUsers(companyID, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void success(SelectItem[] result) {
                if (result != null && result.length > 0) {
                    userListBox.setItems(result);
                }
            }
        });
        checkAll = new KpiCheckBox("Check All");

        hostNameBox = new CustomList(Design.CHECK, true);
        hostNameBox.addStyleName(Constants.DEFAULT_WIDTH);
        hostNameBox.setSearchText(wfmStrings.search() + " " + backendStrings.host().toLowerCase());
        hostNameBox.setHeight(150);
        backendService.getCompanyHosts(new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void success(SelectItem[] result) {
                for (SelectItem item : result) {
                    hostNameBox.add(item);
                }
            }
        });

        HorizontalPanel hostNamePanel = new HorizontalPanel();
        hostNamePanel.addStyleName(Constants.DEFAULT_WIDTH);
        hostNamePanel.addStyleName("hostNamePanel");
        hostNamePanel.add(hostNameBox);
        hostNamePanel.add(checkAll);

        promotionCode = new TextBox();
        promotionCode.addStyleName(Constants.DEFAULT_WIDTH);
        promotionCode.addStyleName("promotionCode");
        enableSalesBackend = new KpiCheckBox("backendOption");
        enableSalesBackend.setHTML(backendStrings.salesBackend());
        enableSalesBackend.addClickHandler(event -> removeRadioButtonErrorStyle());
        enableSupportBackend = new KpiCheckBox("backendOption");
        enableSupportBackend.setHTML(backendStrings.supportBackend());
        enableSupportBackend.addClickHandler(event -> removeRadioButtonErrorStyle());
        enableAdminBackend = new KpiCheckBox("backendOption");
        enableAdminBackend.setHTML(backendStrings.adminBackend());
        enableAdminBackend.addClickHandler(event -> removeRadioButtonErrorStyle());
        /*enablePDFBackend = new KpiCheckBox("backendOption");
        enablePDFBackend.setHTML(backendStrings.pdfBackend());
        enablePDFBackend.addClickHandler(event -> removeRadioButtonErrorStyle());*/
        enableDeveloperBackend = new KpiCheckBox("backendOption");
        enableDeveloperBackend.setHTML(backendStrings.developerBackend());
        enableDeveloperBackend.addClickHandler(event -> removeRadioButtonErrorStyle());

        enablePartnerAdminBackend = new KpiCheckBox("backendOption");
        enablePartnerAdminBackend.setHTML(backendStrings.partnerAdminBackend());
        enablePartnerAdminBackend.addClickHandler(event -> removeRadioButtonErrorStyle());
        enablePartnerAdminBackend.addStyleName("enablePartnerAdminBackend");

        VerticalPanel backendCheckboxPanel = new VerticalPanel();
        backendCheckboxPanel.addStyleName(Constants.DEFAULT_WIDTH);
        backendCheckboxPanel.addStyleName("backendCheckboxPanel");
        backendCheckboxPanel.setSpacing(5);
//        backendCheckboxPanel.getElement().getStyle().setBorderColor("#c3c3c3");
//        backendCheckboxPanel.getElement().getStyle().setBorderWidth(1, Style.Unit.PX);
//        backendCheckboxPanel.getElement().getStyle().setBorderStyle(Style.BorderStyle.SOLID);
        backendCheckboxPanel.add(enableSalesBackend);
        backendCheckboxPanel.add(enableSupportBackend);
        backendCheckboxPanel.add(enableAdminBackend);
//        backendCheckboxPanel.add(enablePDFBackend);
        backendCheckboxPanel.add(enableDeveloperBackend);
        backendCheckboxPanel.add(enablePartnerAdminBackend);

        //initialize save button
        WfmButton2 saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveButton.addClickHandler(event -> save());
        WfmButton2 cancelButton = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);
        cancelButton.addClickHandler(clickEvent -> closeTab());
        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        buttonPanel.setSpacing(10);

        generalTable.setHTML(1, 0, "<b class=customTitle>" + companyName + "</b> (" + backendStrings.companyID() + ":" + companyID + ")");
        generalTable.getFlexCellFormatter().setColSpan(1, 0, 3);
        generalTable.setHTML(2, 0, getCustomTITLE(wfmStrings.username()));
        generalTable.setWidget(2, 1, userListBox);
        generalTable.setHTML(3, 0, getTitle(backendStrings.promotionCode()));
        generalTable.setWidget(3, 1, promotionCode);
        generalTable.setHTML(4, 0, getCustomTITLE(backendStrings.hostName()));
        generalTable.setWidget(4, 1, hostNamePanel);
        generalTable.setHTML(5, 0, getCustomTITLE(backendStrings.backendOptions()));
        generalTable.getFlexCellFormatter().setVerticalAlignment(5, 0, HasVerticalAlignment.ALIGN_TOP);
        generalTable.setWidget(5, 1, backendCheckboxPanel);
        generalTable.setWidget(6, 1, buttonPanel);

        checkAll.addValueChangeHandler(valueChange -> {
            hostNameBox.setCheckAllItems(valueChange.getValue());
            promotionCode.setEnabled(!valueChange.getValue());

        });

        if (objectID != null) {
            backendService.getBackendManagement(objectID, new AsyncCallback<BackendManagementListItem>() {

                @Override
                public void onFailure(Throwable throwable) {
                    backendManagementListItem = null;
                }

                @Override
                public void onSuccess(BackendManagementListItem result) {
                    backendManagementListItem = result;
                    fillFormWithData();
                }
            });
        }
        add(generalTable);
    }

    private void fillFormWithData() {
        if (backendManagementListItem == null) {
            backendManagementListItem = new BackendManagementListItem();
        }
        userListBox.setSelectedByValue(backendManagementListItem.getUserName());
        promotionCode.setText(backendManagementListItem.getPromotionCode());
        enableSalesBackend.setValue(backendManagementListItem.isEnableSalesBackend());
        enableSupportBackend.setValue(backendManagementListItem.isEnableSupportBackend());
        enableAdminBackend.setValue(backendManagementListItem.isEnableAdminBackend());
//        enablePDFBackend.setValue(backendManagementListItem.isEnablePDFBackend());
        enableDeveloperBackend.setValue(backendManagementListItem.isEnableDeveloperBackend());
        enablePartnerAdminBackend.setValue(backendManagementListItem.isEnablePartnerAdminBackend());

        String[] selectedHostArray = backendManagementListItem.getHostName().split(",");
        ArrayList<String> selectedHosts = new ArrayList<>(Arrays.asList(selectedHostArray));
        if (selectedHosts.contains("All") || selectedHosts.size() == hostNameBox.getCheckedItemCount()) {
            checkAll.setValue(true);
            hostNameBox.setCheckAllItems(true);
            promotionCode.setEnabled(false);
        } else {
            for (CustomListItem host : hostNameBox.getItems()) {
                for (String selectedHost : selectedHosts) {
                    if (host.getItem().getName().equals(selectedHost)) {
                        host.setCheck(true);
                    }
                }
            }
        }
        if (selectedHosts.size() > 1) {
            promotionCode.setEnabled(false);
        }
    }


    private String getCustomTITLE(String text) {
        return "<b class=customTitle>" + text + "<font color='red'>*</font>:</b>";
    }

    private String getTitle(String text) {
        return "<b class=customTitle>" + text + ":</b>";
    }

    private void removeRadioButtonErrorStyle() {
        if (enableSalesBackend.getStyleName() != null && enableSalesBackend.getStyleName().contains(Constants.ERROR_FORM_STYLE)) {
            enableSalesBackend.removeStyleName(Constants.ERROR_FORM_STYLE);
        }
        if (enableSupportBackend.getStyleName() != null && enableSupportBackend.getStyleName().contains(Constants.ERROR_FORM_STYLE)) {
            enableSupportBackend.removeStyleName(Constants.ERROR_FORM_STYLE);
        }
        if (enableAdminBackend.getStyleName() != null && enableAdminBackend.getStyleName().contains(Constants.ERROR_FORM_STYLE)) {
            enableAdminBackend.removeStyleName(Constants.ERROR_FORM_STYLE);
        }
        /*if (enablePDFBackend.getStyleName() != null && enablePDFBackend.getStyleName().contains("x-form-invalid")) {
            enablePDFBackend.removeStyleName("x-form-invalid");
        }*/
        if (enableDeveloperBackend.getStyleName() != null && enableDeveloperBackend.getStyleName().contains(Constants.ERROR_FORM_STYLE)) {
            enableDeveloperBackend.removeStyleName(Constants.ERROR_FORM_STYLE);
        }
        if (enablePartnerAdminBackend.getStyleName() != null && enablePartnerAdminBackend.getStyleName().contains(Constants.ERROR_FORM_STYLE)) {
            enablePartnerAdminBackend.removeStyleName(Constants.ERROR_FORM_STYLE);
        }
    }

    private void save() {
        if (validate()) {
            return;
        }
        if (backendManagementListItem == null) {
            backendManagementListItem = new BackendManagementListItem();
        }
        backendManagementListItem.setCompanyID(companyID);
        backendManagementListItem.setCompanyName(companyName);
        backendManagementListItem.setCreatorName(Utils.getUserName());
        backendManagementListItem.setCreatorID(Utils.getUserID());
        backendManagementListItem.setCreateTime(new Date());
        backendManagementListItem.setUpdaterID(Utils.getUserID());
        backendManagementListItem.setUpdaterName(Utils.getUserName());
        backendManagementListItem.setUpdateTime(new Date());
        backendManagementListItem.setEnableSalesBackend(enableSalesBackend.getValue());
        backendManagementListItem.setEnableSupportBackend(enableSupportBackend.getValue());
        backendManagementListItem.setEnableAdminBackend(enableAdminBackend.getValue());
//        backendManagementListItem.setEnablePDFBackend(enablePDFBackend.getValue());
        backendManagementListItem.setEnableDeveloperBackend(enableDeveloperBackend.getValue());
        backendManagementListItem.setEnablePartnerAdminBackend(enablePartnerAdminBackend.getValue());
        if (userListBox.isSomethingSelected()) {
            backendManagementListItem.setUserName(userListBox.getSelectedItem().getName());
            backendManagementListItem.setUserID(userListBox.getSelectedItem().getId());
        }
        if (hostNameBox.getItems() != null && hostNameBox.getItems().size() > 0) {
            StringBuilder selectedHostNames = new StringBuilder();
            if (checkAll.getValue() || hostNameBox.getItems().size() == hostNameBox.getSelectItems().size()) {
                backendManagementListItem.setHostName("All");
                promotionCode.setEnabled(false);
            } else {
                for (CustomListItem item : hostNameBox.getItems()) {
                    if (item.getValue()) {
                        selectedHostNames.insert(0, item.getItem().getName() + ",");
                    }
                }
                backendManagementListItem.setHostName(selectedHostNames.substring(0, selectedHostNames.length() - 1));
            }
        }
        backendManagementListItem.setPromotionCode(promotionCode.isEnabled() ? promotionCode.getText() : "");

        LoadingPanel.loading(true);
        backendService.saveBackendManagement(backendManagementListItem, new AbstractAsyncCallback<Void>() {
            @Override
            public void success(Void result) {
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), backendStrings.backendManagement()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_BACKEND_OPTIONS_ADD, result, AddEditBackendManagementView.this);
                closeTab();
            }
        });
    }

    private boolean validate() {
        int errors = 0;
        if (!Validation.validateListBoxRequired(userListBox, new HTML(), "")) {
            errors++;
        }
        if (enableSalesBackend.getValue() == null || enableSupportBackend.getValue() == null || enableAdminBackend.getValue() == null ||
                /*enablePDFBackend.getValue() == null ||*/ enableDeveloperBackend.getValue() == null || enablePartnerAdminBackend == null || hostNameBox.getCheckedItemCount() == 0) {
            enableSalesBackend.addStyleName(Constants.ERROR_FORM_STYLE);
            enableSupportBackend.addStyleName(Constants.ERROR_FORM_STYLE);
            enableAdminBackend.addStyleName(Constants.ERROR_FORM_STYLE);
//            enablePDFBackend.addStyleName("x-form-invalid");
            enableDeveloperBackend.addStyleName(Constants.ERROR_FORM_STYLE);
            enablePartnerAdminBackend.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return true;
        }
        return false;
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