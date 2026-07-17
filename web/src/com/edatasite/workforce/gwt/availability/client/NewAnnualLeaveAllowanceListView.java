package com.edatasite.workforce.gwt.availability.client;

import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.LeaveSettingsItem;
import com.edatasite.workforce.gwt.availability.client.rpc.TimeslotSetting;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.components.ImportFileActionLink;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listTable.ImportFilePopUp;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * User: Eminem
 * Date: 6/12/12
 * Time: 11:38 AM
 */
public class NewAnnualLeaveAllowanceListView extends BaseListView implements Constants {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private DataListBox yearListBox;
    private DataListBox departmentListBox;
    private final DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy");
    private int currentYear = Integer.parseInt(dateFormat.format(new Date()));
    private HTML yearHtml;
    private HTML departmentHtml;
    private ListingPanel list;
    private HashMap<Integer, Widget[]> editableColumnsByRow;


    public NewAnnualLeaveAllowanceListView() {
        super(ANNUAL_LEAVE_ALLOWANCE_LIST_VIEW, hrmsStrings.annualLeaveAllowance());
    }

    @Override
    public String getIconStyle() {
        return "availability annual-allowance";
    }

    @Override
    protected Widget onInitialize() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_OVERTIME_CHANGE, NewAnnualLeaveAllowanceListView.this, (sender, args) -> list.reloadPage());
        AvailabilityService.App.get().getLeaveRequestSettingsData(new AsyncCallback<LeaveSettingsItem>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(LeaveSettingsItem result) {
                list = new ListingPanel<>(ListPanelType.AnnualAllowanceListPanel, getDrawColumns(), getProvider(), getDesigner());
                list.setOnReset(() -> {
                    departmentListBox.setSelectedNullLabel();
                    currentYear = Integer.valueOf(DateUtils.getYear(new Date()));
                    yearListBox.setSelected(currentYear);
                });

                list.setExcelListener(clickEvent -> {
                    String excelURL = CommandConstants.COMMON_URL + "/downloadLeaveAllowanceListExcel";
                    ListingFilterParameter fp = list.getFilterParametrs();
                    fp.setPropertyCode(getPropertyCode());
                    list.callListExcel(excelURL, fp);
                });

                add(list);
            }
        });

        return null;
    }

    private ColumnDefinitionConfig[] getDrawColumns() {
        editableColumnsByRow = new HashMap<>();
        ArrayList<ColumnDefinitionConfig> columns = new ArrayList<>();
        //Employee name
        ColumnDefinitionConfig employee = new ColumnDefinitionConfig<TimeslotSetting, HTML>(wfmStrings.employee(), TimeslotSetting.EMPLOYEE_NAME, 150) {
            @Override
            public HTML getCellValue(final TimeslotSetting rowValue) {
                HTML employeename = new HTML("<a>" + rowValue.getEmployeeName() + "</a>");
                employeename.addClickHandler(clickEvent ->
                        gotoLeaveTypesView(rowValue.getEmployeeID(), rowValue.getEmployeeName()));
                return employeename;
            }
        };
        columns.add(employee);

        //Department name
        ColumnDefinitionConfig departmentName = new ColumnDefinitionConfig<TimeslotSetting, String>(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), TimeslotSetting.DEPARTMENT_NAME, 150) {
            @Override
            public String getCellValue(TimeslotSetting rowValue) {
                return rowValue.getDepartmentName();
            }
        };
        departmentName.setColumnSortable(false);
        columns.add(departmentName);
        //Days
        ColumnDefinitionConfig daysColumn = new ColumnDefinitionConfig<TimeslotSetting, String>(hrmsStrings.currentAllowance(), TimeslotSetting.DAYS, 100) {
            @Override
            public String getCellValue(final TimeslotSetting rowValue) {
                return rowValue.getAnnualAllowance();
            }
        };
        daysColumn.setColumnSortable(false);
        daysColumn.setMaximumColumnWidth(180);
        columns.add(daysColumn);

        ColumnDefinitionConfig timeslotName = new ColumnDefinitionConfig<TimeslotSetting, String>(hrmsStrings.timeslotName(), TimeslotSetting.TIMESLOT, 190) {
            @Override
            public String getCellValue(final TimeslotSetting rowValue) {
                return rowValue.getTimeslotName();
            }
        };
        timeslotName.setColumnSortable(false);
        columns.add(timeslotName);

        return columns.toArray(new ColumnDefinitionConfig[]{});
    }

    private void gotoLeaveTypesView(Integer employeeID, String empName) {
        SinksContainerFactory.entryPoint.onHistoryChanged("employeeLeaveTypes|employeetypeview/" + employeeID + "/" + yearListBox.getSelectedItem().getId(), empName);
    }

    private ListingPanelDesign getDesigner() {
        //Year filter start
        yearHtml = new HTML(wfmStrings.year());
        Integer currentYear = Integer.valueOf(DateUtils.getYear(new Date()));
        yearListBox = new DataListBox();
        yearListBox.setWidth("100px");
        yearListBox.setWithoutNullLabel(true);

        SelectItem[] items = new SelectItem[3];
        int j = 0;
        for (int i = currentYear - 1; i <= currentYear + 1; i++) {
            SelectItem year = new SelectItem();
            year.setId(i);
            year.setName(String.valueOf(i));
            items[j] = year;
            j++;
        }
        yearListBox.setItems(items);
        yearListBox.setSelected(currentYear);

        yearListBox.addValueChangeHandler(event -> list.reloadPage());

        //Department filter start
        departmentHtml = new HTML(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()));
        departmentListBox = new DataListBox();
        departmentListBox.setWidth("200px");
        departmentListBox.setAllowFirstItem(true);
        //
        CommonService.App.get().getTeamList(new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void success(SelectItem[] items) {
                departmentListBox.setItems(items);
            }
        });
        departmentListBox.addValueChangeHandler(sender -> list.reloadPage());
        //Department filter end

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
            public HorizontalPanel initTopToolBarWidgets() {
                HorizontalPanel topPanel = new HorizontalPanel();
                topPanel.setWidth("75%");

                topPanel.add(yearHtml);
                topPanel.setCellVerticalAlignment(yearHtml, HasVerticalAlignment.ALIGN_MIDDLE);
                topPanel.add(yearListBox);
                topPanel.setCellVerticalAlignment(yearListBox, HasVerticalAlignment.ALIGN_MIDDLE);

                topPanel.add(departmentHtml);
                topPanel.setCellVerticalAlignment(departmentHtml, HasVerticalAlignment.ALIGN_MIDDLE);
                topPanel.add(departmentListBox);
                topPanel.setCellVerticalAlignment(departmentListBox, HasVerticalAlignment.ALIGN_MIDDLE);
                topPanel.add(new HTML("&nbsp;&nbsp;"));

                return topPanel;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                ImportFilePopUp imp = new ImportFilePopUp(ImportTypeEnum.ANNUAL_ALLOWANCE, null);
                imp.setSubmitCompleted(() -> {
                    if (imp.getObjectId() != null) {
                        goTo("importemployeeallowance|add/add/" + imp.getObjectId());
                    }
                });

                ImportFileActionLink link = new ImportFileActionLink();
                link.addClickHandler(ch -> imp.open());
                menuContainer.add(link);

                exportOption.initExport(null, true);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmStrings.noEmployeeMessage());
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private ListingRequestProvider<TimeslotSetting> getProvider() {
        return (filterParametrs, listingCallback) -> {
            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }
            filterParametrs.setYear(yearListBox.getSelectedItem().getId());
            filterParametrs.setDepartmentId(departmentListBox.getSelectedItem() != null && departmentListBox.getSelectedItem().getId()>0 ? departmentListBox.getSelectedItem().getId() : null);
            filterParametrs.setEmailIncluded(false);//this is for search key, we don't need email search
            filterParametrs.setResignedEmployeesIncluded(false);
            AvailabilityService.App.get().getEmployeesAndTimeslot(filterParametrs, new AbstractAsyncCallback<ListResult<TimeslotSetting>>() {
                @Override
                public void failure(Throwable throwable) {
                    listingCallback.onFailure(throwable);
                }

                @Override
                public void success(ListResult<TimeslotSetting> result) {
                    listingCallback.onSuccess(result);
                }
            });
        };
    }

    private void registerEditableCell(Widget editableCellWidget, int widgetIndex, int rowID) {
        if (editableColumnsByRow.get(rowID) != null) {
            editableColumnsByRow.get(rowID)[widgetIndex] = editableCellWidget;
        } else {
            Widget[] editableCellWidgets = new Widget[4];
            editableCellWidgets[widgetIndex] = editableCellWidget;
            editableColumnsByRow.put(rowID, editableCellWidgets);
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
