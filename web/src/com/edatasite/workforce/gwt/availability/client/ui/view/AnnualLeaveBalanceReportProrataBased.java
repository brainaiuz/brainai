package com.edatasite.workforce.gwt.availability.client.ui.view;

import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityServiceAsync;
import com.edatasite.workforce.gwt.availability.client.rpc.LeaveBalanceReport;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.TextBoxCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CellChange;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.ArrayList;
import java.util.Date;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.ADMIN;


/**
 * Created by Hurshid on 10/2/2017.
 */
public class AnnualLeaveBalanceReportProrataBased extends BaseListView {

    private final AvailabilityServiceAsync availabilityService = AvailabilityService.App.get();
    private static final NumberFormat numberFormat = NumberFormat.getFormat(",##0.00");
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private ListingPanel<LeaveBalanceReport> listingTable;
    private HTML asOfDateLabel, departmentLabel;
    private DatePicker asOfDate;
    private DataListBox departmentList;
    private final DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy");
    private final int currentYear = Integer.parseInt(dateFormat.format(new Date()));
    private Date openingBalanceDate = null;

    public AnnualLeaveBalanceReportProrataBased() {
        super("annualLeaveBalance");
        setDescription(property.getPlural(hrmsStrings.annualLeaveBalance()));
    }

    @Override
    public String getIconStyle() {
        return "availability av-welcome";
    }

    @Override
    protected Widget onInitialize() {
        listingTable = new ListingPanel<>(ListPanelType.AnnualLeaveBalanceList, getColumns(), getListData(), getDesign());

        listingTable.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadAnnualLeaveBalanceReportExcel";
            ListingFilterParameter fp = listingTable.getFilterParametrs();
            fp.setDepartmentId(departmentList.getSelectedId());
            if (asOfDate != null && asOfDate.getDate() != null) {
                fp.setDueDate(asOfDate.getDate());
            } else {
                fp.setDueDate(new Date());
            }
            listingTable.callListExcel(excelURL, fp);
        });

        listingTable.setOnReset(() -> {
            departmentList.setSelectedNullLabel();
            asOfDate.setDate(new Date());
        });

        add(listingTable);
        return null;
    }

    private ColumnDefinitionConfig[] getColumns() {
        ArrayList<CustomColumnDefinitionConfig> columns = new ArrayList<>();
        CustomColumnDefinitionConfig columnConfig;

        //Employee name
        columnConfig = new ColumnDefinitionConfig<LeaveBalanceReport, SimpleLink>(wfmStrings.employee(), LeaveBalanceReport.EMPLOYEE_NAME, 200) {
            @Override
            public SimpleLink getCellValue(LeaveBalanceReport item) {
                return getLink(item.getEmployeeName() != null ? item.getEmployeeName() : wfmStrings.notAvailable(), "employeeProfile|hrmsleaveRequests/" + item.getEmployeeID() + "/" + currentYear);
            }
        };
        columnConfig.setMinimumColumnWidth(150);
//        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);

        //Hire Date
        columnConfig = new ColumnDefinitionConfig<LeaveBalanceReport, String>(wfmStrings.hireDate(), LeaveBalanceReport.HIRE_DATE, 100) {
            @Override
            public String getCellValue(LeaveBalanceReport item) {
                return item.getHireDate() != null ? DateUtils.format1(item.getHireDate().getNonConvertedDate()) : "";
            }
        };
        columnConfig.setColumnSortable(false);
        columnConfig.setMinimumColumnWidth(100);
        columns.add(columnConfig);

        //Resign Date
        columnConfig = new ColumnDefinitionConfig<LeaveBalanceReport, String>(wfmStrings.resignationDate(), LeaveBalanceReport.RESIGN_DATE, 100) {
            @Override
            public String getCellValue(LeaveBalanceReport item) {
                return item.getResignDate() != null ? DateUtils.format1(item.getResignDate().getNonConvertedDate()) : "";
            }
        };
        columnConfig.setColumnSortable(false);
        columnConfig.setMinimumColumnWidth(100);
        columns.add(columnConfig);

        //Number of Worked Days
        columnConfig = new ColumnDefinitionConfig<LeaveBalanceReport, String>(hrmsStrings.workedDays(), LeaveBalanceReport.WORKED_DAYS, 80) {
            @Override
            public String getCellValue(LeaveBalanceReport item) {
                return numberFormat.format(item.getWorkedDays());
            }
        };
        columnConfig.setMinimumColumnWidth(60);
        columns.add(columnConfig);

        //Unpaid(days)
        columnConfig = new ColumnDefinitionConfig<LeaveBalanceReport, String>(wfmStrings.unPaidDays(), LeaveBalanceReport.UNPAID_DAYS, 80) {
            @Override
            public String getCellValue(LeaveBalanceReport item) {
                return numberFormat.format(item.getUnpaidDays());
            }
        };
        columnConfig.setMinimumColumnWidth(60);
        columns.add(columnConfig);

        //Opening balance (days)
        columnConfig = new ColumnDefinitionConfig<LeaveBalanceReport, String>(hrmsStrings.openingBalanceDays(), LeaveBalanceReport.OPENING_BALANCE, 100) {
            @Override
            public String getCellValue(LeaveBalanceReport item) {
                return String.valueOf(item.getOpeningBalance() != null ? item.getOpeningBalance() : 0.0);
            }

            @Override
            public void setCellValue(LeaveBalanceReport rowValue, String cellValue) {
                rowValue.setOpeningBalance(Double.valueOf(cellValue));
                saveCellValue(rowValue);
            }
        };

        columnConfig.setColumnSortable(false);
        columnConfig.setMinimumColumnWidth(100);
        if (Utils.hasRole(ADMIN)) {
            TextBoxCellEditor balanceCellEditor = new TextBoxCellEditor<String>() {
                @Override
                protected String getValue() {
                    return getText();
                }

                @Override
                protected void setValue(String cellValue) {
                    setText(cellValue);
                }
            };
            balanceCellEditor.addNumberValidation(true);

            columnConfig.setCellEditor(balanceCellEditor);
            columnConfig.setCellChangesSave((CellChange<LeaveBalanceReport>) (rowValue, columnCodeName) -> saveValue(rowValue));
        }
        columns.add(columnConfig);

        //Taken(days)
        columnConfig = new ColumnDefinitionConfig<LeaveBalanceReport, String>(hrmsStrings.takenDays(), LeaveBalanceReport.TAKEN_DAYS, 80) {
            @Override
            public String getCellValue(LeaveBalanceReport item) {
                return numberFormat.format(item.getTakenDays());
            }
        };
        columnConfig.setMinimumColumnWidth(60);
        columns.add(columnConfig);

        //Current Balance
        columnConfig = new ColumnDefinitionConfig<LeaveBalanceReport, String>(hrmsStrings.currentBalance(), LeaveBalanceReport.CURRENT_BALANCE, 80) {
            @Override
            public String getCellValue(LeaveBalanceReport item) {
                return numberFormat.format(item.getCurrentBalance());
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        columnConfig.setColumnSortable(false);
        columnConfig.setMinimumColumnWidth(60);
        columns.add(columnConfig);

        //status
        columnConfig = new ColumnDefinitionConfig<LeaveBalanceReport, String>(wfmStrings.status(), LeaveBalanceReport.STATUS, 100) {
            @Override
            public String getCellValue(LeaveBalanceReport item) {
                return item.getStatus();
            }
        };
        columnConfig.setColumnSortable(false);
        columnConfig.setMinimumColumnWidth(100);
        columns.add(columnConfig);


        //Opening balance Date
        columnConfig = new ColumnDefinitionConfig<LeaveBalanceReport, String>(wfmStrings.openingBalanceDate(), LeaveBalanceReport.OPENING_DATE, 100) {
            @Override
            public String getCellValue(LeaveBalanceReport item) {
                if (item.getOpeningDate() != null) {
                    openingBalanceDate = item.getOpeningDate().getNonConvertedDate();
                    return DateUtils.format(item.getOpeningDate().getNonConvertedDate());
                } else {
                    return "";
                }
            }
        };
        columnConfig.setColumnSortable(false);
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(100);
        columns.add(columnConfig);


        //Effective Start Date
        columnConfig = new ColumnDefinitionConfig<LeaveBalanceReport, String>(hrmsStrings.effectiveStartDate(), LeaveBalanceReport.EFFECTIVE_STARTDATE, 100) {
            @Override
            public String getCellValue(LeaveBalanceReport item) {
                return item.getEffectiveStartDate() != null ? DateUtils.format(item.getEffectiveStartDate().getNonConvertedDate()) : "";
            }
        };
        columnConfig.setColumnSortable(false);
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(100);
        columns.add(columnConfig);

        //Effective End Date
        columnConfig = new ColumnDefinitionConfig<LeaveBalanceReport, String>(hrmsStrings.effectiveEndDate(), LeaveBalanceReport.EFFECTIVE_ENDDATE, 100) {
            @Override
            public String getCellValue(LeaveBalanceReport item) {
                return item.getEffectiveEndDate() != null ? DateUtils.format(item.getEffectiveEndDate().getNonConvertedDate()) : "";
            }
        };
        columnConfig.setColumnSortable(false);
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(100);
        columns.add(columnConfig);

        //department
        columnConfig = new ColumnDefinitionConfig<LeaveBalanceReport, String>(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), LeaveBalanceReport.DEPARTMENT, 100) {
            @Override
            public String getCellValue(LeaveBalanceReport item) {
                return item.getDepartment();
            }
        };
        columnConfig.setColumnSortable(false);
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(100);
        columns.add(columnConfig);

        //Leave allowance (days)
        columnConfig = new ColumnDefinitionConfig<LeaveBalanceReport, String>(hrmsStrings.leaveAllowanceDays(), LeaveBalanceReport.LEAVE_ALLOWANCE_DAYS, 80) {
            @Override
            public String getCellValue(LeaveBalanceReport item) {
                return numberFormat.format(item.getLeaveAllowanceDays());
            }
        };
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(60);
        columns.add(columnConfig);


        return columns.toArray(new ColumnDefinitionConfig[0]);
    }

    private void saveValue(LeaveBalanceReport data) {
        AvailabilityService.App.get().saveLeaveBalanceReportData(data, new AsyncCallback<TestRPC>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(TestRPC testRPC) {
                listingTable.reloadPage();
            }
        });
    }

    private ListingPanelDesign getDesign() {

        asOfDateLabel = new HTML(wfmStrings.date() + ":&nbsp;&nbsp;");
        asOfDate = new DatePicker();
        asOfDate.setWidth("120px");
        asOfDate.setDate(new Date());
        asOfDate.addChangeHandler(changeEvent -> {
            if (openingBalanceDate != null && (openingBalanceDate.after(asOfDate.getDate()) || openingBalanceDate.equals(asOfDate.getDate()))) {
                Info.show(wfmMessages.asOfDateCannotBeEarlierThanOPeningDate(DateUtils.format(openingBalanceDate)), Info.Type.WARNING);
            }
            listingTable.reloadPage();
        });
        departmentLabel = new HTML(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()));

        departmentList = new DataListBox();
        departmentList.setWidth("200px");
        departmentList.setAllowFirstItem(true);
        CommonService.App.get().getTeamList(new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void success(SelectItem[] items) {
                departmentList.setItems(items);
            }
        });
        departmentList.addValueChangeHandler(sender -> listingTable.reloadPage());

        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null);
                listingTable.getPdfVersion().setVisible(false);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(hrmsStrings.thereAreNoResultOnSelectedDate());

                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public HorizontalPanel initTopToolBarWidgets() {
                HorizontalPanel topPanel = new HorizontalPanel();
                topPanel.setWidth("auto");
                topPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
                topPanel.add(asOfDateLabel);
//                topPanel.add(new HTML("&nbsp;&nbsp;&nbsp;&nbsp;"));
                topPanel.add(asOfDate);
                topPanel.setCellWidth(asOfDate, "130px");
//                topPanel.add(new HTML("&nbsp;&nbsp;&nbsp;&nbsp;"));

                topPanel.add(departmentLabel);
                topPanel.setCellVerticalAlignment(departmentLabel, HasVerticalAlignment.ALIGN_MIDDLE);
                topPanel.add(new HTML(":&nbsp;&nbsp;"));
                topPanel.add(departmentList);
                topPanel.setCellVerticalAlignment(departmentList, HasVerticalAlignment.ALIGN_MIDDLE);

                return topPanel;
            }
        };
    }

    private ListingRequestProvider<LeaveBalanceReport> getListData() {
        return (filterParameter, callback) -> {
            filterParameter.setDepartmentId(departmentList.getSelectedId());
            filterParameter.setDate(asOfDate.getDate());
            if (asOfDate.getDate() == null) {
                filterParameter.setDate(new Date());
            }
            availabilityService.getAnnnualLeaveBalanceReportProrataBased(filterParameter, new AbstractAsyncCallback<ListResult<LeaveBalanceReport>>() {
                @Override
                public void failure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                @Override
                public void success(ListResult<LeaveBalanceReport> reportList) {
                    callback.onSuccess(reportList);
                }
            });
        };
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

    public String getPropertyCode() {
        return "annualLeaveBalance";
    }
}
