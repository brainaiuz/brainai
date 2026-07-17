package com.edatasite.workforce.gwt.availability.client.ui.view;

import com.edatasite.workforce.gwt.availability.client.rpc.UserFingerPrintDeviceItem;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsServiceAsync;
import com.edatasite.workforce.gwt.timesheet.client.ui.view.widgets.TabDataGrid;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.google.gwt.user.client.ui.HasVerticalAlignment.ALIGN_MIDDLE;

/**
 * Created by Muhammad on 20.04.2016.
 */
public class FingerPrintListView extends BaseListView implements Constants {

    protected static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private final HrmsServiceAsync hrmsService = HrmsService.App.get();

    private EmployeeLookUpWithCode employeeLookUp;
    private WfmButton2 saveButton;
    private ListDataProvider<UserFingerPrintDeviceItem> dataProvider = null;
    private List<SelectItem> headerList;
    private HashMap<String, String> userDeviceFingerPrintMap = null;
    private HashMap<String, String> userDeviceFingerPrintUpdaterMap = null;
    private TabDataGrid<UserFingerPrintDeviceItem> cellTable = null;
    private ViewFooter footer;

    private final ProvidesKey<UserFingerPrintDeviceItem> KEY_PROVIDER = item -> item == null ? null : item.getUserId();

    public FingerPrintListView() {
        super(FINGERPRINT_SETUP, wfmStrings.fingerprintSetup());
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected Widget onInitialize() {

        LoadingPanel.loading(true);
        hrmsService.getFingerprintSetup(new AsyncCallback<ArrayList<SelectItem>>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ArrayList<SelectItem> deviceList) {
                headerList = deviceList;
                initialization();
                addButtonListener();
                initUserDeviseFingerprint();
                getComapanyUsers();
                LoadingPanel.loading(false);
            }
        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_FINGERPRINT_SETUP_ADD, FingerPrintListView.this, (sender, args) -> getComapanyUsers());
        return null;
    }

    private void addButtonListener() {
        saveButton.getElement().setId("Finger_print_save_button");
        saveButton.addClickHandler(clickEvent -> {
            if (Utils.hasPermission(PermissionConstants.HRMS_ADD_FINGERPRINT)) {
                save();
            } else {
                Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
            }
        });
    }

    private void save() {
        hrmsService.saveUserFingerPrintDeviceData(userDeviceFingerPrintUpdaterMap.size() > 0 ? userDeviceFingerPrintUpdaterMap : null, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(Void aVoid) {
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), hrmsStrings.fingerprint()), Info.Type.INFO);
                initUserDeviseFingerprint();
                getComapanyUsers();
            }
        });
    }

    private void initialization() {
        userDeviceFingerPrintMap = new HashMap<>();
        userDeviceFingerPrintUpdaterMap = new HashMap<>();
        //save button
        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);

        // Employee selected LookUp
        employeeLookUp = new EmployeeLookUpWithCode();
        employeeLookUp.getSuggestBox().addSelectionHandler(selectionEvent -> getComapanyUsers());
        employeeLookUp.showClearButton();
        employeeLookUp.setClearCommand(() -> {
            employeeLookUp.clearAndClearItems();
            employeeLookUp.refreshOracle(true);
            getComapanyUsers();
        });

        HTML employeeName = new HTML(wfmStrings.employee() + ":");

        HorizontalPanel employeeLookUpPanel = new HorizontalPanel();
        employeeLookUpPanel.add(employeeName);
        employeeLookUpPanel.add(employeeLookUp);
        employeeLookUpPanel.setCellVerticalAlignment(employeeName, ALIGN_MIDDLE);

        HorizontalPanel toolBar = new HorizontalPanel();
        toolBar.setStyleName("box-bg--1 box-radius--top");
        employeeLookUpPanel.setStyleName("pg_fingetprintlist_LookUpPanel");
        toolBar.add(employeeLookUpPanel);

        //add(toolBar);
        dataProvider = new ListDataProvider<>();
        cellTable = new TabDataGrid<>(50, KEY_PROVIDER);
        cellTable.setMinimumTableWidth(990, Style.Unit.PX);
        cellTable.setHeight((Window.getClientHeight() - 184) + "px");
        addDataDisplay(cellTable);
        cellTable.setStyleName("cellBasedWidget-mod");

        // Simple Pager;
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        SimplePager pager = new SimplePager(SimplePager.TextLocation.CENTER, pagerResources, false, 50, true);
        pager.getElement().getStyle().setVerticalAlign(Style.VerticalAlign.MIDDLE);
        pager.setDisplay(cellTable);
        pager.setStyleName("pg_fingetprintlist_pager");

        footer = new ViewFooter(new IFooteredView() {

            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return null;
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return null;
            }
        });
        footer.addToRightSide(saveButton);
        footer.addToLeftSide(pager);

        WfmForm mainForm = new WfmForm();
        mainForm.addWidget(toolBar);
        mainForm.addWidget(cellTable);
        mainForm.addWidget(footer);
        add(mainForm);

        initsializationStructure();
    }

    private void initsializationStructure() {
        Column<UserFingerPrintDeviceItem, SafeHtml> firstNameColumn = new Column<UserFingerPrintDeviceItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(UserFingerPrintDeviceItem item) {
                SafeHtmlBuilder htmlBuilder = new SafeHtmlBuilder();
                htmlBuilder.appendEscaped(item.getUserName());

                return htmlBuilder.toSafeHtml();
            }
        };
        final SafeHtmlBuilder sb = new SafeHtmlBuilder();
        sb.appendHtmlConstant(wfmStrings.employee());
        cellTable.addColumn(firstNameColumn, sb.toSafeHtml());
        cellTable.setColumnWidth(firstNameColumn, 100, Style.Unit.PX);

        for (SelectItem item : headerList) {
            String branchName = item.getName();

            final String deviceID = item.getReferenceCode();
            TextInputCell inputCell = new TextInputCell();
            inputCell.setWidth("75%");
            Column<UserFingerPrintDeviceItem, String> stringColumn = new Column<UserFingerPrintDeviceItem, String>(inputCell) {
                @Override
                public String getValue(UserFingerPrintDeviceItem deviceItem) {
                    String key = deviceItem.getUserId() + "__" + deviceID;
                    if (!userDeviceFingerPrintMap.isEmpty() && userDeviceFingerPrintMap.containsKey(key)) {
                        return userDeviceFingerPrintMap.get(key);
                    }else{
                        return "";
                    }
                }

            };

            stringColumn.setFieldUpdater((index, object, value) -> {
                    String key = object.getUserId() + "__" + deviceID;
                    userDeviceFingerPrintUpdaterMap.put(key, value);
                    object.setFingerPrintId(value);
            });
            cellTable.addColumn(stringColumn, branchName);
            cellTable.setColumnWidth(stringColumn, 60, Style.Unit.PX);
        }
        cellTable.setKeyboardSelectionPolicy(HasKeyboardPagingPolicy.KeyboardSelectionPolicy.DISABLED);
    }

    private void addDataDisplay(HasData<UserFingerPrintDeviceItem> display) {
        dataProvider.addDataDisplay(display);
    }

    private void initUserDeviseFingerprint() {
        // base fingerprintData;
        hrmsService.getUserDeviceFingerPrint(new AsyncCallback<HashMap<String, String>>() {

            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(HashMap<String, String> map) {
                userDeviceFingerPrintMap.clear();
                userDeviceFingerPrintUpdaterMap.clear();
                if (map != null) {
                    userDeviceFingerPrintMap = map;
                }
            }
        });
    }

    private void getComapanyUsers() {
        LoadingPanel.loading(true);
        hrmsService.getComapanyUsers("".equals(employeeLookUp.getText()) ? null : employeeLookUp.getSelectedItemID(), new AsyncCallback<ArrayList<UserFingerPrintDeviceItem>>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ArrayList<UserFingerPrintDeviceItem> userFingerPrintDeviceItems) {
                supplyProvider(userFingerPrintDeviceItems);
                LoadingPanel.loading(false);
            }
        });

    }

    private void supplyProvider(List<UserFingerPrintDeviceItem> userFingerPrintDeviceItems) {
        dataProvider.getList().clear();
        dataProvider.getList().addAll(userFingerPrintDeviceItems);
        if (userFingerPrintDeviceItems.isEmpty()) {
            cellTable.setEmptyTableWidget(DefaultNoItemsMessage.getNoItemsMessage("There are no users in this period", "", null));
        }else{
            cellTable.setEmptyTableWidget(DefaultNoItemsMessage.getNoItemsMessage("Not Found", "", null));
        }
        dataProvider.refresh();
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
