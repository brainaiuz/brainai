package com.edatasite.workforce.gwt.availability.client.ui.view;

import com.edatasite.workforce.gwt.availability.client.localization.AvailabilityMessages;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TimeslotItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
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
import com.edatasite.workforce.gwt.core.client.ui.panel.VerticalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

public class TimeslotListView extends View implements Constants {

    private static final AvailabilityMessages availabilityMessages = AvailabilityMessages.App.get();
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    private ListingPanel<TimeslotItem> listPanel;

    private KpiModal shell;
    private int actionItemCount;
    private final String permission = PermissionConstants.SETTINGS_HRMS_SETTINGS_ADD_TIMESLOT;

    public TimeslotListView() {
        super("timeslot", wfmStrings.timeslot());
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected Widget onInitialize() {
        listPanel = new ListingPanel<>(ListPanelType.TimeslotListPanel, getColumnConfig(), getListProvider(), getListDesign());

        listPanel.setPDFListener(clickEvent -> {
            String pdfURL = CommandConstants.PDF_URL + "/timeslotListPDFHandler";
            listPanel.callListPDF(pdfURL, listPanel.getFilterParametrs());
        });
        listPanel.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadTimeSlotListExcel";
            listPanel.callListExcel(excelURL, listPanel.getFilterParametrs());
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TIMESLOT_ADD, TimeslotListView.this, (sender, args) -> listPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TIMESLOT_EDIT, TimeslotListView.this, (sender, args) -> listPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SHIFT_SETTINGS_ADD, TimeslotListView.this, (sender, args) -> listPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SHIFT_SETTINGS_ADD, TimeslotListView.this, (sender, args) -> listPanel.reloadPage());

        add(listPanel);
        return null;
    }

    private CustomColumnDefinitionConfig[] getColumnConfig() {
        ColumnDefinitionConfig[] columnConfig = new ColumnDefinitionConfig[4];
        //action
        columnConfig[0] = new ColumnDefinitionConfig<TimeslotItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final TimeslotItem item) {
                actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
//                if (Utils.hasRole(ADMIN_LOCATION) || Utils.hasRole(HR) || Utils.hasRole(DR) || Utils.hasRole(ADMIN) || Utils.hasRole(TL)) {
                //timeSlot summary
                MenuPopItem summaryTimeSlot = new MenuPopItem(wfmStrings.summaryView(), "icon-clock");
                summaryTimeSlot.getElement().setId("TimeSlot_Summary_button");
                summaryTimeSlot.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged((item.isShift() ? "shiftsettings" : "timeslot") + "|summary/" + item.getObjectID(), item.getName()));
                if (Utils.hasPermission(PermissionConstants.SETTINGS_HRMS_SETTINGS_EDIT_TIMESLOT)) {
                    actionItemCount++;
                    menuBar.addItem(summaryTimeSlot);
                    //edit timeSlot
                    final MenuPopItem editTimeSlot = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                    editTimeSlot.getElement().setId("TimeSlot_edit_button");
                    editTimeSlot.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged((item.isShift() ? "shiftsettings" : "timeslot") + "|edit/" + item.getObjectID(), item.getName()));
                    menuBar.addItem(editTimeSlot);
                }
                if (Utils.hasPermission(PermissionConstants.SETTINGS_HRMS_SETTINGS_DELETE_TIMESLOT) && (item.getObjectID() != 1 || item.isShift())) {
                    actionItemCount++;
                    //delete timeSlot
                    final MenuPopItem deleteTimeSlot = new MenuPopItem(wfmStrings.delete(), "icon-remove");
                    if (item.isShift()) {
                        deleteTimeSlot.setCommand(() -> AvailabilityService.App.get().deleteShiftSettings(item.getObjectID(), new AsyncCallback<Void>() {
                            @Override
                            public void onFailure(Throwable throwable) {
                                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                            }

                            @Override
                            public void onSuccess(Void result) {
                                Info.show(availabilityMessages.deletedTimeslot(item.getName()), Info.Type.INFO);
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SHIFT_SETTINGS_DELETE, result, TimeslotListView.this);
                                listPanel.reloadPage();
                            }
                        }));
                    } else {
                        deleteTimeSlot.setCommand(() -> getDeleteTimeSlotPopupShell(item, null));
                    }

                    menuBar.addItem(deleteTimeSlot);
                }

                final ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();

            }
        };
        columnConfig[0].setColumnSortable(false);
        columnConfig[0].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfig[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        //name
        columnConfig[1] = new ColumnDefinitionConfig<TimeslotItem, SimpleLink>(wfmStrings.name(), TimeslotItem.NAME, 250) {
            @Override
            public SimpleLink getCellValue(TimeslotItem item) {
                return new SimpleLink(item.getName(), (item.isShift() ? "shiftsettings" : "timeslot") + "|summary/" + item.getObjectID());
            }
        };
        columnConfig[1].setMinimumColumnWidth(60);
        //short name
        columnConfig[2] = new ColumnDefinitionConfig<TimeslotItem, String>(wfmStrings.shortName(), TimeslotItem.SHORT_NAME, 250) {
            @Override
            public String getCellValue(TimeslotItem item) {
                return item.getShortName();
            }
        };
        columnConfig[2].setMinimumColumnWidth(70);
        //description
        columnConfig[3] = new ColumnDefinitionConfig<TimeslotItem, String>(wfmStrings.description(), TimeslotItem.DESCRIPTION, 250) {
            @Override
            public String getCellValue(TimeslotItem item) {
                return item.getDescription();
            }
        };
        columnConfig[3].setMinimumColumnWidth(70);
//        //departments
//        columnConfig[4] = new ColumnDefinitionConfig<TimeslotItem, String>(Property.getPluralWithObjectCode(Constants.DEPARTMENT_LIST, wfmStrings.departments()), TimeslotItem.DEPARTMENTS, 250) {
//            @Override
//            public String getCellValue(TimeslotItem item) {
//                return item.getDepartmentsAsString();
//            }
//        };
//        columnConfig[4].setColumnSortable(false);
//        columnConfig[4].setMinimumColumnWidth(70);
        return columnConfig;
    }

    private ListingRequestProvider<TimeslotItem> getListProvider() {
        return (listingFilterParameter, listingCallback) -> AvailabilityService.App.get().getTimeslots(listingFilterParameter, new AsyncCallback<ListResult<TimeslotItem>>() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess(ListResult<TimeslotItem> timeslotItemListResult) {
                listingCallback.onSuccess(timeslotItemListResult);
            }
        });
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
                if (Utils.hasPermission(permission)) {
                    final MenuBar contextMenu = new MenuBar(true);
                    contextMenu.addItem("<span>" + wfmStrings.timeslot() + "</span>", true, (Command) () -> SinksContainerFactory.entryPoint.onHistoryChanged("timeslot|add/add"));
                    contextMenu.addItem("<span>" + hrmsStrings.shiftSettings() + "</span>", true, (Command) () -> SinksContainerFactory.entryPoint.onHistoryChanged("shiftsettings|add/add"));
                    ActionButton more = getAddNewButton(ActionButton.Type.TOOLMENU);
                    more.addClickHandler(clickEvent -> more.setMenu(contextMenu));
                    return more;
                }
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null, true);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(hrmsStrings.noTimeslotsMessage());
                if (Utils.hasPermission(permission)) {
                    message.setTextBeforeLink(hrmsStrings.noTimeslotBeforeLinkMessage());
                    message.setHref("timeslot|add/add");
                }
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private void getDeleteTimeSlotPopupShell(final TimeslotItem item, final Integer toTimeSlotID) {
        if (item.getDepartments() != null && toTimeSlotID != null) {
            AvailabilityService.App.get().deleteTimeslot(item.getObjectID(), toTimeSlotID, new AbstractAsyncCallback<Void>() {
                @Override
                public void failure(Throwable caught) {
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                @Override
                public void success(Void result) {
                    Info.show(availabilityMessages.deletedTimeslot(item.getName()), Info.Type.INFO);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TIMESLOT_DELETE, result, TimeslotListView.this);
                    shell.close();
                    listPanel.reloadPage();
                }
            });
        } else if ((item.getDepartments() == null || item.getDepartments().length == 0) && toTimeSlotID == null) {
            AvailabilityService.App.get().deleteTimeslot(item.getObjectID(), new AbstractAsyncCallback<Void>() {
                @Override
                public void failure(Throwable caught) {
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                @Override
                public void success(Void result) {
                    Info.show(availabilityMessages.deletedTimeslot(item.getName()), Info.Type.INFO);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TIMESLOT_DELETE, result, TimeslotListView.this);
                    listPanel.reloadPage();
                }
            });
        } else if (item.getDepartments() != null && item.getDepartments().length > 0 && toTimeSlotID == null) {
            shell = new KpiModal();
            shell.setTitle(hrmsStrings.deleteTimeslot());
            shell.setWidth(400);
            shell.open();

            final HTML message = new HTML(availabilityMessages.wantToDeleteTimeslot(item.getName()));
            message.getElement().getStyle().setPaddingBottom(20, Style.Unit.PX);
            final HTML listBoxLabel = new HTML(availabilityMessages.listBoxLabelText(item.getName()));
            listBoxLabel.getElement().getStyle().setPaddingBottom(20, Style.Unit.PX);

            final WfmButton2 ok = new WfmButton2(wfmStrings.yes(), WfmButton2.BTN_PRIMARY);
            final WfmButton2 cancel = new WfmButton2(wfmStrings.no(), WfmButton2.BTN_DEFAULT);

            final DataListBox listBox = new DataListBox();
            listBox.setWidth("150px");
            listBox.addClickHandler(event -> listBox.setStyleName(""));
            AvailabilityService.App.get().getTimeslotList(new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void failure(Throwable caught) {
                }

                @Override
                public void success(SelectItem[] result) {
                    if (result.length > 0) {
                        SelectItem[] items = new SelectItem[result.length - 1];
                        int s = 0;
                        for (SelectItem aResult : result) {
                            if (aResult.getId().intValue() != item.getObjectID().intValue()) {
                                items[s++] = aResult;
                            }
                        }
                        listBox.setItems(items);
                    }
                }
            });

            ok.addClickHandler(event -> {
                if (listBox.isSomethingSelected()) {
                    ok.setEnabled(false);
                    cancel.setEnabled(false);
                    getDeleteTimeSlotPopupShell(item, listBox.getSelectedItem().getId());
                } else {
                    listBox.setStyleName("x-form-invalid");
                }
            });
            cancel.addClickHandler(event -> shell.close());
            VerticalPanelDiv panelDiv = new VerticalPanelDiv();
            panelDiv.add(message);
            panelDiv.add(listBoxLabel);
            panelDiv.add(listBox);

            shell.add(panelDiv);
            shell.addButton(cancel);
            shell.addButton(ok);
        }
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