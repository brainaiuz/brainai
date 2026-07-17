package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.AdditionalPayment;
import com.edatasite.workforce.gwt.payroll.client.rpc.OvertimeObject;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

import java.util.Optional;

public class OvertimeListView extends BaseListView implements Constants {
    public static final String OVERTIME_EMPLOYEE_TYPE = "employee";
    public static final String OVERTIME_DEPARTMENT_TYPE = "department";
    public static final String OVERTIME_GROUP_EMPLOYEE_TYPE = "group";

    private final WfmStrings wfmStrings = WfmStrings.App.get();
    private final PayrollStrings payrollStrings = PayrollStrings.App.get();
    private ListingPanel<OvertimeObject> listingPanel;

    public OvertimeListView() {
        super(OVERTIME);
        setDescription(property.getPlural(wfmStrings.overtime()));
        setAddNew(() -> SinksContainerFactory.entryPoint.onHistoryChanged("overtime|add/add/" + OVERTIME_EMPLOYEE_TYPE)
        );
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

    @Override
    protected Widget onInitialize() {
        listingPanel = new GuideListingPanel(ListPanelType.Overtime, getColumn(), getListingRequestProvider(), getListDesign());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_OVERTIME_ADD, OvertimeListView.this, (sender, args) -> listingPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_OVERTIME_DELETE, OvertimeListView.this, (sender, args) -> listingPanel.reloadPage());


        listingPanel.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/additionalPaymentListExcelHandler";
            ListingFilterParameter filterParameter = listingPanel.getFilterParametrs();
            filterParameter.setPropertyCode(getPropertyCode());
            listingPanel.callListExcel(excelURL, filterParameter);
        });
        listingPanel.setPDFListener(clickEvent -> {
            String pdfURL = CommandConstants.PDF_URL + "/additionalPaymentItemPdfHandl";
            ListingFilterParameter filterParameter = listingPanel.getFilterParametrs();
            filterParameter.setPropertyCode(getPropertyCode());
            listingPanel.callListPDF(pdfURL, filterParameter);

        });
        add(listingPanel);
        return null;
    }

    private GuideListingPanelDesign getListDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return () -> {
                    SinksContainerFactory.entryPoint.onHistoryChanged("overtime|add/add/" + OVERTIME_EMPLOYEE_TYPE);
                };
            }

            @Override
            public Command getUploadButtonCommand() {
                return null;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {

            }

            @Override
            public ActionButton initTopToolBarNew() {
                //TODO: need to add permissions to the menuBar
                ActionButton newItem = getAddNewButton(ActionButton.Type.TOOLMENU);
                MenuBar menu = new MenuBar(true);

                MenuPopItem addEmployeeNew = new MenuPopItem(wfmStrings.employee());
                addEmployeeNew.ensureDebugId(OVERTIME + "_employeeAddPopUp");
                addEmployeeNew.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("overtime|add/add/" + OVERTIME_EMPLOYEE_TYPE));

                MenuPopItem addDepartmentEmployee = new MenuPopItem(wfmStrings.department());
                addDepartmentEmployee.ensureDebugId(OVERTIME + "_departmentAddPopUp");
                addDepartmentEmployee.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("overtime|add/add/" + OVERTIME_DEPARTMENT_TYPE));

                MenuPopItem addGroupEmployee = new MenuPopItem(wfmStrings.group());
                addGroupEmployee.ensureDebugId(OVERTIME + "_groupEmployeeAddPopUp");
                addGroupEmployee.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("overtime|add/add/" + OVERTIME_GROUP_EMPLOYEE_TYPE));

                menu.addItem(addEmployeeNew);
                menu.addItem(addDepartmentEmployee);
                menu.addItem(addGroupEmployee);

                newItem.setMenu(menu);
                if (Utils.hasPermission(PermissionConstants.PAYROLL_OVERTIME_ADD)) {
                    return newItem;
                }
                return null;
            }
        };
    }

    private ListingRequestProvider<OvertimeObject> getListingRequestProvider() {
        return (filterParametrs, callback) -> PayrollService.App.get().getOvertimeObjectList(filterParametrs, new AbstractAsyncCallback<ListResult<OvertimeObject>>() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(ListResult<OvertimeObject> result) {
                callback.onSuccess(result);
            }
        });
    }


    private CustomColumnDefinitionConfig[] getColumn() {
        int index = 0;
        ColumnDefinitionConfig[] columnConfig = new ColumnDefinitionConfig[10];
        columnConfig[index] = new ColumnDefinitionConfig<OvertimeObject, Anchor>(wfmStrings.action(), "OVERTIME", LISTING_ACTION.COLUMN_WIDTH) {

            @Override
            public Anchor getCellValue(final OvertimeObject item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                MenuPopItem view = new MenuPopItem(wfmStrings.edit(), "icon-task-small");
                boolean isEmployeeType = item.getSelectedEmployee() != null;
                view.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("overtime|add/edit/" + item.getOvertimeType() + "/" + item.getId(), wfmStrings.edit() + " " + wfmStrings.overtime()));
                actionItemCount++;
                if (Utils.hasPermission(PermissionConstants.PAYROLL_OVERTIME_EDIT)) {
                    menuBar.addItem(view);
                }

                view = new MenuPopItem(wfmStrings.summaryView(), "icon-task-small");
                view.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("overtime|summary/" + item.getId() + "/" + item.getOvertimeType(), Optional.ofNullable(item.getCode()).orElse(wfmStrings.add() + " " + wfmStrings.overtime())));
                actionItemCount++;
                if (Utils.hasPermission(PermissionConstants.PAYROLL_OVERTIME_SUMMARY)) {
                    menuBar.addItem(view);
                }

                view = new MenuPopItem(wfmStrings.delete(), "icon-task-small");
                view.setCommand(() -> deleteOvertimeItem(item.getId()));
                actionItemCount++;
                if (Utils.hasPermission(PermissionConstants.PAYROLL_OVERTIME_DELETE)) {
                    menuBar.addItem(view);
                }

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);

        columnConfig[++index] = new ColumnDefinitionConfig<OvertimeObject, SimpleLink>(wfmStrings.overtimeCode(), "overtimeCode", 110) {
            @Override
            public SimpleLink getCellValue(OvertimeObject item) {
                if (Utils.hasPermission(PermissionConstants.PAYROLL_OVERTIME_SUMMARY)) {
                    return getLink(item.getCode() != null ? item.getCode() : "N/A", "overtime|summary/" + item.getId() + "/" + item.getOvertimeType());
                } else {
                    return getLink(item.getCode() != null ? item.getCode() : "N/A", null);
                }
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columnConfig[++index] = new ColumnDefinitionConfig<OvertimeObject, String>(wfmStrings.createdBy(), "creator", 110) {
            @Override
            public String getCellValue(OvertimeObject item) {
                return item.getCreator() != null ? item.getCreator().getName() : "N/A";
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columnConfig[++index] = new ColumnDefinitionConfig<OvertimeObject, String>(wfmStrings.category(), "categoryType", 110) {
            @Override
            public String getCellValue(OvertimeObject item) {
                return item.getCategory() != null ? item.getCategory().getName() : "N/A";
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columnConfig[++index] = new ColumnDefinitionConfig<OvertimeObject, String>(wfmStrings.type(), "type", 110) {
            @Override
            public String getCellValue(OvertimeObject item) {
                String type;
                switch (item.getOvertimeType()) {
                    case OVERTIME_EMPLOYEE_TYPE:
                        type = payrollStrings.byEmployee();
                        break;
                    case OVERTIME_DEPARTMENT_TYPE:
                        type = payrollStrings.byDepartment();
                        break;
                    case OVERTIME_GROUP_EMPLOYEE_TYPE:
                        type = wfmStrings.group();
                        break;
                    default:
                        type = "N/A";
                        break;
                }
                return type;
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columnConfig[++index] = new ColumnDefinitionConfig<OvertimeObject, String>(wfmStrings.status(), "status", 110) {
            @Override
            public String getCellValue(OvertimeObject item) {
                return item.getStatus() != null ? item.getStatus() : "N/A";
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columnConfig[++index] = new ColumnDefinitionConfig<OvertimeObject, String>(wfmStrings.approver(), "approver", 110) {
            @Override
            public String getCellValue(OvertimeObject item) {
                return item.getApproverEmployee() != null ? item.getApproverEmployee().getName() : "N/A";
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columnConfig[++index] = new ColumnDefinitionConfig<OvertimeObject, String>(wfmStrings.createdDate(), "createdDate", 110) {
            @Override
            public String getCellValue(OvertimeObject item) {
                return item.getCreatedDate() != null ? DateUtils.formatInternal(item.getCreatedDate().getDate()) : "N/A";
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columnConfig[++index] = new ColumnDefinitionConfig<OvertimeObject, String>(wfmStrings.modifiedBy(), "updatedBy", 110) {
            @Override
            public String getCellValue(OvertimeObject item) {
                return item.getUpdater() != null ? item.getUpdater().getName() : "N/A";
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columnConfig[++index] = new ColumnDefinitionConfig<OvertimeObject, String>(wfmStrings.modifiedDate(), "updatedDate", 110) {
            @Override
            public String getCellValue(OvertimeObject item) {
                return item.getUpdatedDate() != null ? DateUtils.formatInternal(item.getUpdatedDate().getDate()) : "N/A";
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        return columnConfig;
    }

    private void createAdditionalPaymentFromOvertime(Integer overtimeItemId) {
        LoadingPanel.loading(false);
        PayrollService.App.get().createAdditionalPaymentFromOvertime(overtimeItemId, new AsyncCallback<AdditionalPayment>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(AdditionalPayment item) {
                LoadingPanel.loading(false);
                SinksContainerFactory.entryPoint.onHistoryChanged((item.getCategory() != null && PayrollConstants.CATEGORY_PAYMENT.equals(item.getCategory().getCode()) ? "additionalPayment|view/" : "additionalDeduction|view/") + item.getObjectID() + "/" + item.getStatusCode(), item.getReference());
            }
        });
    }

    private void deleteOvertimeItem(Integer id) {
        LoadingPanel.loading(true);
        PayrollService.App.get().deleteOvertimeItemById(id, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.warn(wfmStrings.sorrySomethingWentWrong());
            }

            @Override
            public void onSuccess(Void unused) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.messSuccessfulyyDeleted());
                listingPanel.reloadPage();
            }
        });
    }

    @Override
    public String getPropertyCode() {
        return OVERTIME;
    }
}
