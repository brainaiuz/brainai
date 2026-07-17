package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.client.client.rpc.EmployeePayslipItem;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.PayslipTableRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.view.PayrollPDFTemplateSelector;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.PAYSLIP_LIST;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 18.04.14
 * Time: 17:42
 * To change this template use File | Settings | File Templates.
 */
public class EmployeePayslipListView extends BaseListView {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    NumberFormat numFormat = Utils.getCalculationNumberFormat();
    private ListingPanel<EmployeePayslipItem> list;
    private final Integer employeeId;
    private final HorizontalPanel horizontalPanel = new HorizontalPanel();
    private final HashMap<Integer, String> monthItems = new HashMap<>();

    public EmployeePayslipListView(Integer employeeId) {
        super("EmployeePayslipList");
        setDescription(property.getPlural(wfmStrings.payslips()));
        this.employeeId = employeeId;
    }

    @Override
    protected Widget onInitialize() {
        Date date = DateUtil.getYearFirstDay(new Date());
        for (int i = 0; i < 12; i++) {
            monthItems.put(i, DateTimeFormat.getFormat("MMMM").format(date));
            date = DateUtil.addMonths(date, 1);
        }
        list = new ListingPanel<>(ListPanelType.EmployeePayslipListPanel, drawColumns(), getListingRequestProvider(), getListingPanelDesign());
        add(horizontalPanel);
        add(list);
        return null;
    }


    private ColumnDefinitionConfig[] drawColumns() {
        ArrayList<ColumnDefinitionConfig> list = new ArrayList<>();

        ColumnDefinitionConfig column = new ColumnDefinitionConfig<EmployeePayslipItem, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {

            @Override
            public Anchor getCellValue(final EmployeePayslipItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                if (Utils.hasPermission(PermissionConstants.HRMS_PAYSLIP_SUMMARY)) {
                    MenuPopItem view = new MenuPopItem(wfmStrings.summaryView(), "icon-employee-edit-profile");
                    view.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("singlePayrun|viewPayslip/" + item.getObjectID(), item.getEmployee(), item.getEmployee()));
                    actionItemCount++;
                    menuBar.addItem(view);
                }

                if (Utils.hasPermission(PermissionConstants.HRMS_PAYSLIP_PDF)) {
                    MenuPopItem pdf = new MenuPopItem(wfmStrings.pdf(), "icon-pdf");
                    pdf.setCommand(() -> new PayrollPDFTemplateSelector(Constants.SINGLE_PAYRUN, new ExtendedCommand() {
                        @Override
                        public void execute(Integer id) {
                            generatePDF(item, id);
                        }
                    }));
                    actionItemCount++;
                    menuBar.addItem(pdf);
                }

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        column.setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        column.setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        column.setColumnSortable(false);
        list.add(column);

        column = new ColumnDefinitionConfig<EmployeePayslipItem, String>(wfmStrings.month(), "month", 140) {
            @Override
            public String getCellValue(EmployeePayslipItem item) {
                String month = item.getMonthID() != null && monthItems.get(item.getMonthID()) != null ? monthItems.get(item.getMonthID()) : item.getMonth();
                return month + " " + item.getYear();
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        list.add(column);

        column = new ColumnDefinitionConfig<EmployeePayslipItem, String>(wfmStrings.total(), "total", 100) {
            @Override
            public String getCellValue(EmployeePayslipItem item) {
                return numFormat.format(item.getTotal());
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        list.add(column);

        column = new ColumnDefinitionConfig<EmployeePayslipItem, String>(wfmStrings.approver(), "approver", 140) {
            @Override
            public String getCellValue(EmployeePayslipItem item) {
                return item.getApprover();
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        list.add(column);

        column = new ColumnDefinitionConfig<EmployeePayslipItem, String>(wfmStrings.createdBy(), "preparer", 140) {
            @Override
            public String getCellValue(EmployeePayslipItem item) {
                return item.getCreator();
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        list.add(column);

        column = new ColumnDefinitionConfig<EmployeePayslipItem, String>(wfmStrings.status(), "status", 120) {
            @Override
            public String getCellValue(EmployeePayslipItem item) {
                return item.getStatus();
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        list.add(column);


        return list.toArray(new ColumnDefinitionConfig[]{});
    }

    private void generatePDF(EmployeePayslipItem item, Integer templateId) {
        PayslipTableRequestObject requestObject = new PayslipTableRequestObject(item.getObjectID());
        requestObject.setEmployeeName(item.getEmployee());
        requestObject.setMonth(item.getMonth());
        requestObject.setYear(item.getYear());
        requestObject.setPdfTemplateID(templateId);
        String pdfURL = CommandConstants.PDF_URL + "/singlePayrunPdfHandler";
        Utils.sendPDFOrExcelRequest(horizontalPanel, pdfURL, requestObject.getRequestParams(), "_blank");
    }

    private ListingRequestProvider<EmployeePayslipItem> getListingRequestProvider() {
        return (filterParametrs, callback) -> {
            loadEmployeePayslips(filterParametrs, callback, null);
        };
    }

    private ListingPanelDesign getListingPanelDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmStrings.currentlyThereAreNoPayslips());
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    @Override
    public String getIconStyle() {
        return "employeeBonus employeeBonus-list";
    }

    @Override
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

    public String getPropertyCode() {
        return PAYSLIP_LIST;
    }

    @Override
    public void initStatistics(Integer parentId, Span container) {
        loadEmployeePayslips(new ListingFilterParameter(), null, container);
    }

    private void loadEmployeePayslips(ListingFilterParameter listingFilterParameter, ListingCallback listingCallback, Span container) {
        listingFilterParameter = listingFilterParameter == null ? new ListingFilterParameter() : listingFilterParameter;
        listingFilterParameter.setEmployeeId(employeeId);
        EmployeeService.App.get().getEmployeePayslips(listingFilterParameter, new AsyncCallback<ListResult<EmployeePayslipItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                if (listingCallback != null) {
                    listingCallback.onFailure(caught);
                }
            }

            @Override
            public void onSuccess(ListResult<EmployeePayslipItem> result) {
                if (listingCallback != null) {
                    listingCallback.onSuccess(result);
                }

                statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                if (statisticShortcut != null) {
                    if (result.getTotal() != null && result.getTotal() > 0) {
                        statisticShortcut.setText(countFormat(result.getTotal()));
                        statisticShortcut.setClass("tab-label");
                    } else {
                        statisticShortcut.setText("");
                        statisticShortcut.removeStyleName("tab-label");
                    }
                }
            }
        });
    }
}
