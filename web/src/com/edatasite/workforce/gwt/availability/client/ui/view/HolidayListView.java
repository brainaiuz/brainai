package com.edatasite.workforce.gwt.availability.client.ui.view;

import com.edatasite.workforce.gwt.availability.client.localization.AvailabilityMessages;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.HolidayItem;
import com.edatasite.workforce.gwt.core.client.*;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.filter.ListingChooseFilter;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: JavaZone
 * Date: Sep 2, 2011
 * Time: 4:58:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class HolidayListView extends BaseListView implements Constants {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final AvailabilityMessages availabilityMessages = AvailabilityMessages.App.get();

    private static final int countOfYears = 6;

    private ListingPanel<HolidayItem> listingTable;
    private final DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy");
    private int currentYear = Integer.parseInt(dateFormat.format(new Date()));
    private int currentYearID = 0;
    private HTML yearHtml;
    private SelectItem[] years;
    private DataListBox yearListBox;
    private HorizontalPanel topPanel;
    private int actionItemCount;

    public HolidayListView() {
        super("holiday", wfmStrings.publicHolidays());
    }

    public String getIconStyle() {
        return null;
    }

    protected Widget onInitialize() {
        listingTable = new ListingPanel<>(ListPanelType.HolidayListPanel, getColumnConfigs(), getListingRequestProvider(), getListingPanelDesign());

        listingTable.setExcelListener(clickEvent -> {
            String excelURL;
            excelURL = CommandConstants.COMMON_URL + "/downloadPublicHolidaysExcel";
            ListingFilterParameter filterParametrs = listingTable.getFilterParametrs();
            listingTable.callListExcel(excelURL, filterParametrs);
        });
        listingTable.setPDFListener(clickEvent -> {
            String pdfURL;
            pdfURL = CommandConstants.PDF_URL + "/holidayListPDFHandler";
            ListingFilterParameter filterParametrs = listingTable.getFilterParametrs();
            listingTable.callListPDF(pdfURL, filterParametrs);
        });


        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_HOLIDAY_ADD, HolidayListView.this, (sender, args) -> listingTable.reloadPage());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_HOLIDAY_EDIT, HolidayListView.this, (sender, args) -> listingTable.reloadPage());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_HOLIDAY_DELETED, HolidayListView.this, (sender, args) -> listingTable.reloadPage());

        add(listingTable);
        return null;
    }

    private ListingPanelDesign getListingPanelDesign() {

        yearHtml = new HTML("<b class=customTitle style=\"padding-top:-5px;margin-top:-5px;\">" + wfmStrings.year() + ":</b>");

        years = new SelectItem[countOfYears];

        for (int yearID = 0; yearID < countOfYears; yearID++) {
            years[yearID] = new SelectItem(yearID, String.valueOf(currentYear - (yearID - 1)));
            if ((currentYear - (yearID - 1)) == currentYear) {
                currentYearID = yearID;
            }
        }

        yearListBox = new DataListBox();
        yearListBox.setWidth("85px");
        yearListBox.setAllowFirstItem(true);
        yearListBox.setWithoutNullLabel(true);
        yearListBox.setItems(years);
        yearListBox.setSelected(currentYearID);

        yearListBox.addValueChangeHandler(event -> {
            currentYear = Integer.parseInt(years[yearListBox.getSelectedId()].getName());
            listingTable.reloadPage();//
        });

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

                    @Override
                    public long initSimpleFilterType() {
                        return ListingChooseFilter.LOCATION_LIST;
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (Utils.hasPermission(PermissionConstants.SETTINGS_HRMS_SETTINGS_ADD_HOLIDAY)) {
                    ActionButton addNewHolidayMenu = getAddNewButton();
                    addNewHolidayMenu.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("holiday|add/add"));
                    return addNewHolidayMenu;
                }
                return null;
            }

            public HorizontalPanel initTopToolBarWidgets() {
                topPanel = new HorizontalPanel();
                topPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
                topPanel.setWidth("35%");
                topPanel.add(yearHtml);
                topPanel.add(yearListBox);

                return topPanel;
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
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(availabilityMessages.noHolidaysMessage(String.valueOf(currentYear)));
                if (Utils.hasRole(DR) || Utils.hasRole(ADMIN)) {
                    message.setTextBeforeLink(hrmsStrings.noHolidayBeforeLinkMessage());
                    message.setHref("holiday|add/add");
                }
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private ListingRequestProvider<HolidayItem> getListingRequestProvider() {
        return (filterParametrs, holidayItemListingCallback) -> {
            filterParametrs.setYear(currentYear);
            AvailabilityService.App.get().getHolidays(filterParametrs, new AsyncCallback<ListResult<HolidayItem>>() {

                @Override
                public void onFailure(Throwable throwable) {
                    holidayItemListingCallback.onFailure(throwable);
                }

                @Override
                public void onSuccess(ListResult<HolidayItem> holidayItemListResult) {
                    holidayItemListingCallback.onSuccess(holidayItemListResult);
                }
            });
        };
    }

    private ColumnDefinitionConfig[] getColumnConfigs() {

        ColumnDefinitionConfig[] columnConfigs = new ColumnDefinitionConfig[8];
        columnConfigs[0] = new ColumnDefinitionConfig<HolidayItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final HolidayItem rowValue) {
                actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                MenuPopItem summaryHoliday = new MenuPopItem(wfmStrings.summaryView(), "icon-edit");
                summaryHoliday.getElement().setId("Public_Holiday_summary_button");
                summaryHoliday.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("holiday|view/" + rowValue.getObjectID(), rowValue.getName()));
                menuBar.addItem(summaryHoliday);
                if (Utils.hasPermission(PermissionConstants.SETTINGS_HRMS_SETTINGS_EDIT_HOLIDAY)) {
                    actionItemCount++;
                    MenuPopItem editHoliday = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                    editHoliday.getElement().setId("Public_holiday_edit_button");
                    editHoliday.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("holiday|edit/" + rowValue.getObjectID(), rowValue.getName()));
                    menuBar.addItem(editHoliday);
                }
                if (Utils.hasPermission(PermissionConstants.SETTINGS_HRMS_SETTINGS_DELETE_HOLIDAY)) {
                    actionItemCount++;
                    MenuPopItem delete = new MenuPopItem(wfmStrings.delete(), "icon-remove");
                    delete.getElement().setId("public_holiday_delete_button");
                    delete.setCommand(() -> {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage(availabilityMessages.wantToDeleteHoliday(rowValue.getName()));
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                AvailabilityService.App.get().deleteHoliday(rowValue.getObjectID(), new AsyncCallback() {
                                    public void onFailure(Throwable caught) {
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    public void onSuccess(Object result) {
                                        Info.show(availabilityMessages.holidayDeleted(rowValue.getName()), Info.Type.INFO);
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_HOLIDAY_DELETED, result, HolidayListView.this);
                                        listingTable.reloadPage();
                                    }
                                });
                            }
                        });
                        messageBox.open();
                    });
                    actionItemCount++;
                    menuBar.addItem(delete);
                }
                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columnConfigs[0].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfigs[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfigs[0].setColumnSortable(false);
        columnConfigs[1] = new ColumnDefinitionConfig<HolidayItem, SimpleLink>(wfmStrings.name(), HolidayItem.NAME, 200) {
            @Override
            public SimpleLink getCellValue(HolidayItem rowValue) {
                return getLink(rowValue.getName(), "holiday|view/" + rowValue.getObjectID());
            }
        };
        columnConfigs[1].setMinimumColumnWidth(100);

        columnConfigs[2] = new ColumnDefinitionConfig<HolidayItem, HTML>(wfmStrings.description(), HolidayItem.DESCRIPTION, 250) {
            @Override
            public HTML getCellValue(HolidayItem rowValue) {
                return new HTML(rowValue.getDescription());
            }
        };
        columnConfigs[2].setMinimumColumnWidth(120);

        columnConfigs[3] = new ColumnDefinitionConfig<HolidayItem, String>(wfmStrings.from(), HolidayItem.FROM, 100) {
            @Override
            public String getCellValue(HolidayItem rowValue) {
                return DateUtils.format(rowValue.getFrom().getNonConvertedDate());
            }
        };
        columnConfigs[3].setMinimumColumnWidth(80);

        columnConfigs[4] = new ColumnDefinitionConfig<HolidayItem, String>(wfmStrings.to(), HolidayItem.TO, 100) {
            @Override
            public String getCellValue(HolidayItem rowValue) {
                return DateUtils.format(rowValue.getTo().getNonConvertedDate());
            }
        };
        columnConfigs[4].setMinimumColumnWidth(80);


        columnConfigs[5] = new ColumnDefinitionConfig<HolidayItem, String>(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), HolidayItem.LOCATION, 100) {

            @Override
            public String getCellValue(HolidayItem rowValue) {
                return rowValue.getLocationName();
            }
        };
        columnConfigs[5].setMinimumColumnWidth(100);
        columnConfigs[5].setColumnSortable(false);
        columnConfigs[6] = new ColumnDefinitionConfig<HolidayItem, String>(wfmStrings.recurring(), HolidayItem.RECURRING, 50) {
            @Override
            public String getCellValue(HolidayItem rowValue) {
                return rowValue.isRepeat() ? wfmStrings.yes() : wfmStrings.no();
            }
        };
        columnConfigs[6].setMinimumColumnWidth(100);
        columnConfigs[6].setColumnSortable(false);

        columnConfigs[7] = new ColumnDefinitionConfig<HolidayItem, String>(wfmStrings.dayOff(), HolidayItem.DAY_OFF, 50) {
            @Override
            public String getCellValue(HolidayItem rowValue) {
                return rowValue.isDayOff() ? wfmStrings.yes() : wfmStrings.no();
            }
        };
        columnConfigs[7].setMinimumColumnWidth(70);

        return columnConfigs;
    }


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
