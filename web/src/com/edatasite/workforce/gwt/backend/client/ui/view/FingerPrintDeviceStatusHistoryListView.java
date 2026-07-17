package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.FingerPrintDeviceStatusHistoryListItem;
import com.edatasite.workforce.gwt.backend.client.ui.view.imageBundle.BackendImageBundle;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Farrukh Abdurakhmonov on 26.10.2017.
 * Date: 26/10/17
 * Time: 3:19 PM
 */
public class FingerPrintDeviceStatusHistoryListView extends BaseListView {

    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private Integer companyID;
    private String companyName;
    private ListingPanel<FingerPrintDeviceStatusHistoryListItem> listingPanel;
    private DataListBox deviceListBox;
    private ListingFilterParameter filterParameter = new ListingFilterParameter();

    private BackendImageBundle imageBundle = (BackendImageBundle) GWT.create(BackendImageBundle.class);


    public FingerPrintDeviceStatusHistoryListView(Integer companyID, String companyName) {
        super("fingerPrintDeviceStatusHistoryListView", backendStrings.fingerprintDeviceStatusHistory());
        this.companyID = companyID;
        this.companyName = companyName;
    }

    @Override
    public String getIconStyle() {
        return "icon-backendManagementView";
    }

    @Override
    protected Widget onInitialize() {
        filterParameter.setCompanyID(companyID);
        String shortCompanyName = companyName;
        if (shortCompanyName.length() > 25) {
            shortCompanyName = shortCompanyName.substring(0, 25) + "...";
        }
        if (container != null) {
            container.setDescription(shortCompanyName);
        }

        listingPanel = new ListingPanel<>(ListPanelType.FingerPrintDeviceStatusHistoryListPanel, drawColumns(), getProvider(), getDesign());

        add(listingPanel);
        return null;
    }

    private ColumnDefinitionConfig[] drawColumns() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[3];

        columns[0] = new ColumnDefinitionConfig<FingerPrintDeviceStatusHistoryListItem, String>(backendStrings.deviceName(), FingerPrintDeviceStatusHistoryListItem.DEVICE_NAME, 50) {
            @Override
            public String getCellValue(FingerPrintDeviceStatusHistoryListItem rowValue) {
                return rowValue.getDeviceName();
            }
        };
        columns[0].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns[0].setColumnSortable(false);


        columns[1] = new ColumnDefinitionConfig<FingerPrintDeviceStatusHistoryListItem, Widget>(wfmStrings.status(), FingerPrintDeviceStatusHistoryListItem.STATUS, 15) {
            @Override
            public Widget getCellValue(FingerPrintDeviceStatusHistoryListItem rowValue) {
                Image onImage = new Image(imageBundle.on());
                Image offImage = new Image(imageBundle.off());
                onImage.setHeight("18px");
                onImage.setSize("18px", "18px");
                offImage.setSize("18px", "18px");
                Image statusImage = "OFF".equalsIgnoreCase(rowValue.getDeviceStatus()) ? offImage : onImage;
                onImage.setTitle("ON");
                offImage.setTitle("OFF");
                FlowPanel statusPanel = new FlowPanel();
                statusPanel.add(statusImage);
                return statusPanel;
            }
        };
        columns[1].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns[1].setColumnSortable(false);

        columns[2] = new ColumnDefinitionConfig<FingerPrintDeviceStatusHistoryListItem, String>(backendStrings.updateDate(), FingerPrintDeviceStatusHistoryListItem.UPDATED_TIME, 40) {
            @Override
            public String getCellValue(FingerPrintDeviceStatusHistoryListItem rowValue) {
                return rowValue.getStatusUpdateTime() != null ? DateUtils.formatInternal(rowValue.getStatusUpdateTime().getNonConvertedDate()) : "";
            }
        };
        columns[2].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns[2].setColumnSortable(false);

        columns[3] = new ColumnDefinitionConfig<FingerPrintDeviceStatusHistoryListItem, String>(wfmStrings.description(), FingerPrintDeviceStatusHistoryListItem.DESCRIPTION, 100) {
            @Override
            public String getCellValue(FingerPrintDeviceStatusHistoryListItem rowValue) {
                return rowValue.getDescription();
            }
        };
        columns[3].setColumnSortable(false);
        return columns;
    }

    private ListingPanelDesign getDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public HorizontalPanel initTopToolBarWidgets() {
                HorizontalPanel topPanel = new HorizontalPanel();
                HorizontalPanel panel = new HorizontalPanel();
                panel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
                panel.setSpacing(5);

                Label deviceName = new Label();
                deviceName.setWidth("100px");
                deviceName.setTextAsHtml("<b>Device Name:</b>");

                deviceListBox = new DataListBox();
                deviceListBox.setWidth("180px");
                BackendService.App.get().getDeviceUniqueKeyListMap(filterParameter, new AsyncCallback<LinkedHashMap<String, String>>() {
                    @Override
                    public void onFailure(Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(LinkedHashMap<String, String> deviceMap) {
                        ArrayList<SelectItem> items = new ArrayList<>();
                        for (String deviceKey : deviceMap.keySet()) {
                            SelectItem item = new SelectItem();
                            item.setName(deviceMap.get(deviceKey));
                            item.setCode(deviceKey);
                            items.add(item);
                        }
                        deviceListBox.setItems(items.toArray(new SelectItem[items.size()]));
                        deviceListBox.addValueChangeHandler(changeEvent -> {
                            listingPanel.getFilterParametrs().setDeviceID(deviceListBox.getSelectedItem() == null ? null : deviceListBox.getSelectedItem().getCode());
                            listingPanel.reloadPage();
                        });
                    }
                });

                panel.add(deviceName);
                panel.add(deviceListBox);
                topPanel.add(panel);

                topPanel.setCellWidth(panel, "250px");
                topPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
                topPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
                return topPanel;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                emptyDataTable.initEmptyDataTable(new DefaultNoItemsMessage(backendStrings.currentlyThereAreNoAnyItems()));
            }
        };
    }

    private ListingRequestProvider<FingerPrintDeviceStatusHistoryListItem> getProvider() {
        return (fp, listingCallback) -> {
            if (fp == null) {
                fp = filterParameter;
            }
            fp.setCompanyID(companyID);
            if (deviceListBox.getSelectedItem() != null) {
                fp.setDeviceID(deviceListBox.getSelectedItem().getCode());
            }
            BackendService.App.get().getFingerPrintDeviceHistoryList(fp, new AbstractAsyncCallback<ListResult<FingerPrintDeviceStatusHistoryListItem>>() {
                @Override
                public void failure(Throwable throwable) {
                    listingCallback.onFailure(throwable);
                }

                @Override
                public void success(ListResult<FingerPrintDeviceStatusHistoryListItem> result) {
                    listingCallback.onSuccess(result);
                }
            });
        };
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