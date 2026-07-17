package com.edatasite.workforce.gwt.availability.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.website.AttendanceDeviceStatus;
import com.edatasite.workforce.gwt.core.client.rpc.website.AttendanceTerminal;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AttendanceTerminalListView extends BaseListView implements Constants {
    private final int actionItemCount = 3;
    private ListingPanel<AttendanceTerminal> listPanel;
    private final Map<Long, Date> syncedDates = new HashMap<>();
    private final Map<Long, Boolean> deviceStatus = new HashMap<>();

    public AttendanceTerminalListView() {
        super("attendanceTerminal", wfmStrings.attendanceTerminal());
    }

    @Override
    protected Widget onInitialize() {
        listPanel = new ListingPanel<>(ListPanelType.AttendanceTerminalListPanel, getColumnConfig(), getListProvider(), getListDesign());
        listPanel.setExcelListener(clickEvent -> listPanel.callListExcel(CommandConstants.COMMON_URL + "/downloadAttendanceTerminalListExcel", listPanel.getFilterParametrs()));
        listPanel.setPDFListener(clickEvent -> listPanel.callListPDF(CommandConstants.PDF_URL + "/attendanceTerminalListPDFHandler", listPanel.getFilterParametrs()));

        add(listPanel);
        initListeners();
        return null;
    }

    private CustomColumnDefinitionConfig[] getColumnConfig() {
        CustomColumnDefinitionConfig[] columnConfig = new CustomColumnDefinitionConfig[6];

        columnConfig[0] = new ColumnDefinitionConfig<AttendanceTerminal, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(AttendanceTerminal item) {
                boolean systemManaged = item.getExternalId() != null;
                MenuBar menuBar = new MenuBar(true);

                MenuPopItem summary = new MenuPopItem(wfmStrings.summaryView(), "icon-clock");
                summary.getElement().setId("AttendanceTerminal_Summary_button");
                summary.setScheduledCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("attendanceTerminal|summary/" + item.getId(), item.getCompanyBranchName()));
                menuBar.addItem(summary);

                if (!systemManaged) {
                    MenuPopItem editButton = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                    editButton.getElement().setId("AttendanceTerminal_edit_button");
                    editButton.setScheduledCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("attendanceTerminal|edit/edit/" + item.getId(), item.getCompanyBranchName()));
                    menuBar.addItem(editButton);

                    MenuPopItem deleteButton = new MenuPopItem(wfmStrings.delete(), "icon-remove");
                    deleteButton.setScheduledCommand(() -> {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                CommonService.App.get().deleteAttendanceTerminal(item.getId(), new AsyncCallback<Void>() {
                                    @Override
                                    public void onFailure(Throwable throwable) {
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    @Override
                                    public void onSuccess(Void result) {
                                        refresh();
                                    }
                                });
                            }
                        });
                        messageBox.open();
                    });
                    menuBar.addItem(deleteButton);
                }

                ToolItem toolItem = new ToolItem(systemManaged ? 1 : actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();

            }
        };
        columnConfig[0].setColumnSortable(false);
        columnConfig[0].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfig[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);

        columnConfig[1] = new ColumnDefinitionConfig<AttendanceTerminal, Widget>(wfmStrings.id(), "deviceId", 250) {
            @Override
            public Widget getCellValue(AttendanceTerminal item) {
                Label label = new Label(item.getCompanyUniqueID());
                label.setStyleName("uploadLinkStyle2");
                label.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("attendanceTerminal|summary/" + item.getId(), item.getCompanyUniqueID(), item.getCompanyBranchName()));
                return label;
            }
        };
        columnConfig[1].setMinimumColumnWidth(70);

        columnConfig[2] = new ColumnDefinitionConfig<AttendanceTerminal, String>(wfmStrings.branch(), "branchName", 250) {
            @Override
            public String getCellValue(AttendanceTerminal item) {
                return item.getCompanyBranchName();
            }
        };
        columnConfig[2].setMinimumColumnWidth(70);

        columnConfig[3] = new ColumnDefinitionConfig<AttendanceTerminal, Boolean>(wfmStrings.dynamicType(), "dynamicStatus", 100) {
            @Override
            public Boolean getCellValue(AttendanceTerminal item) {
                return item.getDynamicStatus();
            }
        };
        columnConfig[3].setMinimumColumnWidth(70);

        columnConfig[4] = new ColumnDefinitionConfig<AttendanceTerminal, String>(wfmStrings.executionTime(), "lastSyncedAt", 100) {
            @Override
            public String getCellValue(AttendanceTerminal item) {
                if (item.getExternalId() == null) {
                    return DateUtils.getDateAndTimeFormatShort(new Date());
                }
                return syncedDates.containsKey(item.getExternalId()) ? DateUtils.getDateAndTimeFormatShort(syncedDates.get(item.getExternalId())) : "N/A";
            }
        };
        columnConfig[4].setMaximumColumnWidth(70);

        columnConfig[5] = new ColumnDefinitionConfig<AttendanceTerminal, String>(wfmStrings.active(), "deviceActive", 30) {

            @Override
            public String getCellValue(AttendanceTerminal rowValue) {
                if (!deviceStatus.containsKey(rowValue.getExternalId())) return "N/A";
                return deviceStatus.get(rowValue.getExternalId()) ? wfmStrings.yes() : wfmStrings.no();
            }
        };
        columnConfig[5].setMaximumColumnWidth(30);

        return columnConfig;
    }

    private ListingRequestProvider<AttendanceTerminal> getListProvider() {
        return (listingFilterParameter, listingCallback) -> CommonService.App.get().getAttendanceTerminals(listingFilterParameter.getSearchKey(), new AsyncCallback<ArrayList<AttendanceTerminal>>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(ArrayList<AttendanceTerminal> result) {
                CommonService.App.get().fetchAttendanceTerminalStatus(new AsyncCallback<ArrayList<AttendanceDeviceStatus>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        listingCallback.onSuccess(new ListResult<>(result, result.size()));
                    }

                    @Override
                    public void onSuccess(ArrayList<AttendanceDeviceStatus> apiResponse) {
                        deviceStatus.clear();
                        syncedDates.clear();
                        for (AttendanceDeviceStatus device : apiResponse) {
                            Long externalId = device.getId();
                            Boolean active = device.getActive();
                            deviceStatus.put(externalId, active);

                            if (device.getTime() == null) continue;
                            String formattedTime = formattedDate(device);

                            Date value = parseISODate(formattedTime);
                            if (value != null) syncedDates.put(externalId, value);
                        }
                        listingCallback.onSuccess(new ListResult<>(result, result.size()));
                    }

                    private String pad(int value, int length) {
                        String str = "" + value;
                        while (str.length() < length) {
                            str = "0" + str;
                        }
                        return str;
                    }

                    private String formattedDate(AttendanceDeviceStatus device) {
                        ArrayList<Integer> timeValue = device.getTime().getValue();

                        int year = timeValue.get(0);
                        int month = timeValue.get(1);
                        int day = timeValue.get(2);
                        int hour = timeValue.get(3);
                        int minute = timeValue.get(4);
                        int second = timeValue.size() > 5 ? timeValue.get(5) : 0;

                        return pad(year, 4) + "-"
                                + pad(month, 2) + "-"
                                + pad(day, 2) + " "
                                + pad(hour, 2) + ":"
                                + pad(minute, 2) + ":"
                                + pad(second, 2);
                    }
                });
            }
        });
    }

    private Date parseISODate(String isoDate) {
        try {
            Date date = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss").parse(isoDate);  // fixme, extract formatter
            return new Date(date.getTime() + 18000000); // fixme, convert timezone properly, 18000 = 5h which is Asia/Tashkent
        } catch (Exception e) {
            return null;
        }
    }

    private ListingPanelDesign getListDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return null;
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return null;
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addNew = getAddNewButton();
                addNew.addClickHandler(clickEvent -> addNewAttendanceTerminal());
                return addNew;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmStrings.no());
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null, true);
            }
        };
    }

    private void addNewAttendanceTerminal() {
        SinksContainerFactory.entryPoint.onHistoryChanged("attendanceTerminal|add/add");
    }

    protected void initListeners() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ATTENDANCE_TERMINAL_ADD, AttendanceTerminalListView.this, (sender, args) -> refresh());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ATTENDANCE_TERMINAL_EDIT, AttendanceTerminalListView.this, (sender, args) -> refresh());
    }

    protected void refresh() {
        if (listPanel.isListingPage()) {
            listPanel.reloadPage();
        } else {
            listPanel.requestKanbanData();
        }
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
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
