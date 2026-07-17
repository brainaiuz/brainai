package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingCallback;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseReportsListItem;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseService;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseServiceAsync;
import com.edatasite.workforce.gwt.expenses.client.ui.ExpenseConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.html.Span;


/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Nov 10, 2009
 * Time: 3:12:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class HrmsExpenseReportListView extends BaseListView implements Constants, AccountingConstants, PermissionConstants {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final ExpenseServiceAsync expenseService = ExpenseService.App.get();

    private ListingPanel<ExpenseReportsListItem> list;
    private Integer employeeId;
    private final boolean addEmployeeExpense = Utils.hasPermission(HRMS_ADD_NEW_EXPENSE_CLAIM);
    private final boolean addCompanyExpense = Utils.hasPermission(HRMS_ADD_NEW_COMPANY_EXPENSE_CLAIM);

    public HrmsExpenseReportListView(Integer employeeId) {
        super(HRMS_EXPENSE_REPORT_LIST);
        setDescription(property.getPlural(wfmStrings.expenses()));
        setEmployeeId(employeeId);
        if (addEmployeeExpense) {
            setAddNew("expenseReports|add/add");
        } else if (addCompanyExpense) {
            setAddNew("expenseReports|add/add/" + ExpenseConstants.COMPANY_EXPENSE);
        }
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    protected Widget onInitialize() {
        list = new GuideListingPanel(ListPanelType.HrmsExpenceReportListPanel, drowColumns(), getListingRequestProvider(), getListingPanelDesign());

        list.setPDFListener(clickEvent -> {
            String pdfURL;
//                if (!(list.getPagingScrollTable().getSelectedRowValues().iterator().next() != null)) {
            pdfURL = CommandConstants.PDF_URL + "/expensesListPDFHandler";
            ListingFilterParameter filterParametrs = list.getFilterParametrs();
            filterParametrs.setPropertyCode(getPropertyCode());
            list.callListPDF(pdfURL, filterParametrs);
//                }
        });

        list.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadExpenseReportListExcel";
            ListingFilterParameter filterParametrs = list.getFilterParametrs();
            filterParametrs.setPropertyCode(getPropertyCode());
            filterParametrs.setHRMS(true);
            list.callListExcel(excelURL, filterParametrs);
        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EXPENSEREPORT_SAVED, HrmsExpenseReportListView.this, (sender, args) -> list.reloadPage());
        add(list);
        return null;
    }

    private GuideListingPanelDesign getListingPanelDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return addEmployeeExpense ? () -> SinksContainerFactory.entryPoint.onHistoryChanged("expenseReports|add/add") : addCompanyExpense ? () -> SinksContainerFactory.entryPoint.onHistoryChanged("expenseReports|add/add/" + ExpenseConstants.COMPANY_EXPENSE) : null;
            }

            @Override
            public Command getUploadButtonCommand() {
                return null;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }

                };
            }

//            @Override
//            public void initTopToolBarWidgets(HorizontalPanel topPanel) {
//                ToolBar toolBar = new ToolBar();
//                toolBar.setHeight(21);
//                toolBar.setStyleName("quickViewHeader");
//                if (!Utils.hasRole(CLIENT)) {
//                    ToolItem addNew = new ToolItem(Style.PUSH);
//                    addNew.setText(accountingStrings.addNewExpenseClaim());
//                    addNew.setIconStyle("icon-addtask");
//                    addNew.addSelectionListener(new SelectionListener() {
//                        public void widgetSelected(BaseEvent be) {
//                            if (getEmployeeId() != null) {
//                                SinksContainerFactory.entryPoint.onHistoryChanged("expenseReports|add/add/" + getEmployeeId());
//                            } else {
//                                SinksContainerFactory.entryPoint.onHistoryChanged("expenseReports|add/add");
//                            }
//
//                        }
//                    });
//                    toolBar.add(addNew);
//                    topPanel.add(toolBar);
//                    topPanel.setCellWidth(toolBar, "400px");
//                }
//            }


            @Override
            public ActionButton initTopToolBarNew() {
                if (addCompanyExpense && addEmployeeExpense) {
                    return addBothExpenses();
                } else if (addEmployeeExpense) {
                    return addEmployeeExpense();
                } else if (addCompanyExpense) {
                    return addCompanyExpense();
                }
                return null;
            }


            private ActionButton addBothExpenses() {
                ActionButton newExpenseReportItem = getAddNewButton(ActionButton.Type.TOOLMENU);
                MenuBar menu = new MenuBar(true);

                MenuPopItem addCompanyExpense = new MenuPopItem(wfmStrings.companyExpense());
                addCompanyExpense.setCommand(() -> {
                    goTo("expenseReports|add/add/" + ExpenseConstants.COMPANY_EXPENSE);
                });
                menu.addItem(addCompanyExpense);

                MenuPopItem addEmployeeExpense = new MenuPopItem(accountingStrings.employeeExpense());
                addEmployeeExpense.setCommand(() -> {
                    if (getEmployeeId() != null) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("expenseReports|add/add/HRMS_LIST/" + getEmployeeId());
                    } else {
                        SinksContainerFactory.entryPoint.onHistoryChanged("expenseReports|add/add");
                    }
                });
                menu.addItem(addEmployeeExpense);

                newExpenseReportItem.setMenu(menu);
                return newExpenseReportItem;
            }

            private ActionButton addEmployeeExpense() {
                ActionButton newExpenseReportItem = getAddNewButton(ActionButton.Type.TOOLMENU);

                newExpenseReportItem.addClickHandler(event -> {
                    if (getEmployeeId() != null) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("expenseReports|add/add/HRMS_LIST/" + getEmployeeId());
                    } else {
                        SinksContainerFactory.entryPoint.onHistoryChanged("expenseReports|add/add");
                    }
                });
                return newExpenseReportItem;
            }

            private ActionButton addCompanyExpense() {
                ActionButton newExpenseReportItem = getAddNewButton(ActionButton.Type.TOOLMENU);

                newExpenseReportItem.addClickHandler(event -> {
                    SinksContainerFactory.entryPoint.onHistoryChanged("expenseReports|add/add" + ExpenseConstants.COMPANY_EXPENSE);
                });
                return newExpenseReportItem;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null, true);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(property.getSingular(accountingStrings.currentlyYouHaveNoRegisteredExpenseClaim(), wfmStrings.expenseClaim()));
                if (Utils.hasPermission(HRMS_ADD_NEW_EXPENSE_CLAIM)) {
//				if (!Utils.hasRole(CLIENT)) {
//					if (Integer.valueOf(28492).equals(Utils.getCompanyID())) {  //companyID = 28492, Company Name: ITS DEPARTMENT - PROJECT MANAGEMENT & COLLABORATION PORTAL
//						if (!Utils.hasRole(AUDITOR)) {
//							message.setTextBeforeLink(accountingStrings.toStartReportingClick());
//							message.setHref("expenseReports|add/add");
//						}
//					} else {
                    message.setTextBeforeLink(accountingStrings.toStartReportingClick());
                    message.setHref("expenseReports|add/add");
//					}
//				}
                    emptyDataTable.initEmptyDataTable(message);
                }
            }
        };
    }

    private ListingRequestProvider<ExpenseReportsListItem> getListingRequestProvider() {
        return (filterParametrs, callback) -> {
            loadExpenseReports(filterParametrs, callback, null);
        };
    }


    public String getIconStyle() {
        return "hrms hrms-expense-report-list";
    }


    private ColumnDefinitionConfig[] drowColumns() {

        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[8];

        columns[0] = new ColumnDefinitionConfig<ExpenseReportsListItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {

            @Override
            public Anchor getCellValue(final ExpenseReportsListItem item) {
                int actionItemCount = 0;
                final boolean hasAccessToChange = !Utils.isLockCompletedProjecItems() || !PS_CLOSED.equals(item.getProjectStatusCode());
                boolean hasAccountingBeforeBlockDate = (Utils.isExpensesLocked() && DateUtils.getTransactionLockDate().after(item.getStartDate().getNonConvertedDate()));

                MenuBar menuBar = new MenuBar(true);


                if (Utils.hasPermission(HRMS_VIEW_EXPENSE_CLAIM)) {
                    MenuPopItem reportSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-expense-report-small");
                    reportSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("expenseReports|previewReport/" + item.getId() + "/" + Constants.EXPENSE_VIEW, item.getExpenseNumber(), item.getTitle()));
                    actionItemCount++;
                    menuBar.addItem(reportSummary);
                }
                String statusCode = item.getStatusCode();
                boolean isDrAdmAccAndDoubleApproveDisabled = Utils.hasPermission(HRMS_CAN_APPROVE_EXPENSE_CLAIM) && !item.isDoubleApproverEnabled();

                if (hasAccessToChange && (isDrAdmAccAndDoubleApproveDisabled || item.isPreferredApprover(Utils.getUserID())) && !hasAccountingBeforeBlockDate) {
                    if (EXPENSE_SUBMITTED.equals(statusCode)) {
                        if (item.isCategoriesSelected()) {
                            MenuPopItem approveReport = new MenuPopItem(wfmStrings.approve(), "icon-approve");
                            approveReport.setCommand(() -> changeStatus(item, EXPENSE_APPROVED, accountingStrings.expenseClaimApproved(), accountingStrings.unableToSubmit(), null));
                            actionItemCount++;
                            menuBar.addItem(approveReport);
                        }

                        MenuPopItem declineReport = new MenuPopItem(wfmStrings.reject(), "icon-decline");
                        declineReport.setCommand(() -> declineExpenseReport(item));
                        actionItemCount++;
                        menuBar.addItem(declineReport);

                    }
                }

                boolean isSubmittedExpenseEditable = isDrAdmAccAndDoubleApproveDisabled && Utils.hasRole(ADMIN) && (EXPENSE_APPROVED.equals(statusCode) || EXPENSE_SUBMITTED.equals(statusCode));
                if (hasAccessToChange && (Utils.hasPermission(HRMS_EXPENSE_REPORT_EDIT)) && (isDrAdmAccAndDoubleApproveDisabled || Utils.getUserID().equals(item.getReporterId()))
                        && (EXPENSE_DRAFT.equals(statusCode) || (EXPENSE_SUBMITTED.equals(item.getStatusCode())) || EXPENSE_DECLINED.equals(statusCode) || isSubmittedExpenseEditable || EXPENSE_APPROVED.equals(statusCode))
                        && !hasAccountingBeforeBlockDate && (!item.isAllocatedToPO() && (item.getPurchaseOrder() == null || item.getPurchaseOrder().getId() == null))) {

                    MenuPopItem editReport = new MenuPopItem(wfmStrings.edit(), "icon-edit-report");
                    editReport.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("expenseReports|edit/" + item.getId(), item.getExpenseNumber(), item.getTitle()));
                    actionItemCount++;
                    menuBar.addItem(editReport);
                }

                if (hasAccessToChange && Utils.hasPermission(HRMS_ADD_NEW_EXPENSE_CLAIM)) {
                    MenuPopItem copyTo = new MenuPopItem(wfmStrings.copy(), "icon-copy");
                    copyTo.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("expenseReports|add/add/" + COPY_FROM_EXISTING + "/" + item.getId()));
                    actionItemCount++;
                    menuBar.addItem(copyTo);
                }

                if (hasAccessToChange && (Utils.hasPermission(HRMS_EXPENCE_REPORT_REMOVE) || (Utils.getUserID().equals(item.getReporterId())
                        && (EXPENSE_DRAFT.equals(statusCode) || EXPENSE_DECLINED.equals(statusCode))))
                        && !hasAccountingBeforeBlockDate && (!item.isAllocatedToPO() && (item.getPurchaseOrder() == null || item.getPurchaseOrder().getId() == null))) {

                    MenuPopItem deleteReport = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    deleteReport.setCommand(() -> {
                        WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        wfmMessageBox.setTitle(wfmStrings.confirmation());
                        wfmMessageBox.setMessage(wfmStrings.sureYouWantToDelete());

                        wfmMessageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                expenseService.deleteExpenseReport(item.getId(), new AbstractAsyncCallback<Boolean>() {
                                    public void failure(Throwable caught) {
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    public void success(Boolean result) {
                                        if (result) {
                                            list.reloadPage();
                                            Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), accountingStrings.expenseReports()), Info.Type.INFO);
                                        } else {
                                            Info.show(accountingStrings.deleteExpenseErrorMessage(), Info.Type.INFO);
                                        }
                                    }
                                });
                            }
                        });

                        wfmMessageBox.open();
                    });
                    actionItemCount++;
                    menuBar.addItem(deleteReport);
                }

                if (hasAccessToChange && Utils.hasPermission(HRMS_EXPENSE_REPORT_VOID) && isDrAdmAccAndDoubleApproveDisabled && (EXPENSE_APPROVED.equals(statusCode) || EXPENSE_PAID.equals(statusCode) || PARTIALLY_PAID.equals(statusCode))
                        && !hasAccountingBeforeBlockDate && (!item.isAllocatedToPO() && (item.getPurchaseOrder() == null || item.getPurchaseOrder().getId() == null))) {

                    MenuPopItem voidExpenseReport = new MenuPopItem(accountingStrings.voide(), "icon-puchase-invoise-small");
                    voidExpenseReport.setCommand(() -> {
                        final WfmMessageBox confirmBox = new WfmMessageBox(IconEnum.INFO, Action.YesNo);
                        confirmBox.setTitle(wfmStrings.confirmation());
                        confirmBox.setMessage(accountingStrings.areYouSureYouWantToVoidTheExpense());
                        confirmBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                final KpiModal dialogBox = new KpiModal();
                                dialogBox.setCloseButton(true);
                                dialogBox.setWidth(400);
                                final DatePicker datePicker = new DatePicker(item.getStartDate().getNonConvertedDate());
                                dialogBox.setTitle(wfmStrings.selectVoidDate());
                                datePicker.setWidth("180px");
                                datePicker.getElement().getStyle().setMargin(10, Style.Unit.PX);
                                dialogBox.add(datePicker);
                                final WfmButton2 voidButton = new WfmButton2(accountingStrings.voide(), WfmButton2.BTN_PRIMARY);
                                dialogBox.addButton(voidButton);
                                voidButton.addClickHandler(clickEvent -> {
                                    if (AccountingUtils.validateVoidDate(datePicker.getDate(), item.getStartDate().getNonConvertedDate())) {
                                        voidButton.setEnabled(false);
                                        AccountingService.App.get().voidExpenseTransaction(item.getId(), new DateNonConvertable(datePicker.getDate()), new AbstractAsyncCallback<Integer>() {
                                            public void failure(Throwable caught) {
                                                dialogBox.close();
                                                caught.printStackTrace();
                                            }

                                            public void success(Integer result) {
                                                dialogBox.close();
                                                if (result == 0) {
                                                    Info.show(accountingStrings.youCantVoidTheExpenseWhichHasPayment(), Info.Type.WARNING);
                                                } else {
                                                    Info.show(accountingStrings.expenseReversedSuccessfully(), Info.Type.INFO);
                                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EXPENSE_VOID, result, HrmsExpenseReportListView.this);
                                                }
                                            }
                                        });
                                    }
                                });
                                dialogBox.open();
                            }
                        });
                        confirmBox.open();
                    });
                    actionItemCount++;
                    menuBar.addItem(voidExpenseReport);
                }

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columns[0].setColumnSortable(false);
        columns[0].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);

        columns[1] = new ColumnDefinitionConfig<ExpenseReportsListItem, SimpleLink>(wfmStrings.title(), TITLE_COLUMN, 80) {

            @Override
            public SimpleLink getCellValue(ExpenseReportsListItem item) {
                String reportName = item.getTitle() == null ? wfmStrings.notAvailable() : item.getTitle();
                return new SimpleLink(reportName, "expenseReports|previewReport/" + item.getId() + "/" + Constants.EXPENSE_VIEW, item.getTitle(), item.getExpenseNumber());
            }
        };

        //Report period
        columns[2] = new ColumnDefinitionConfig<ExpenseReportsListItem, String>(wfmStrings.date(), PERIOD_COLUMN, 100) {

            @Override
            public String getCellValue(ExpenseReportsListItem item) {
                return DateUtils.format(item.getStartDate()) + Utils.getHijriDate(item.getStartDate().getNonConvertedDate());
            }
        };
        columns[2].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        //Related project
        columns[3] = new ColumnDefinitionConfig<ExpenseReportsListItem, String>(Property.get(Constants.PROJECT, wfmStrings.relatedProject(), wfmStrings.project()), PROJECT_COLUMN, 100) {

            @Override
            public String getCellValue(ExpenseReportsListItem item) {
                return item.getProjectName() == null ? wfmStrings.notAvailable() : item.getProjectName();
            }
        };

        //Reporter name
        columns[4] = new ColumnDefinitionConfig<ExpenseReportsListItem, String>(wfmStrings.reporter(), REPORTER_COLUMN, 100) {

            @Override
            public String getCellValue(ExpenseReportsListItem item) {
                return item.getReporterName() != null ? item.getReporterName() : wfmStrings.notAvailable();
            }
        };

        //Approver's name
        columns[5] = new ColumnDefinitionConfig<ExpenseReportsListItem, String>(wfmStrings.approver(), APPROVER_COLUMN, 70) {

            @Override
            public String getCellValue(ExpenseReportsListItem item) {
                return item.getCurrentApproverEmployeeName() == null ? wfmStrings.notAvailable() : item.getCurrentApproverEmployeeName();
            }
        };

        //Status
        columns[6] = new ColumnDefinitionConfig<ExpenseReportsListItem, String>(wfmStrings.status(), STATUS_COLUMN, 70) {

            @Override
            public String getCellValue(ExpenseReportsListItem item) {
                return item.getOverallStatusName();
            }
        };
        columns[6].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        //Amount
        columns[7] = new ColumnDefinitionConfig<ExpenseReportsListItem, String>(wfmStrings.amount(), DUE_AMOUNT_COLUMN, 50) {

            @Override
            public String getCellValue(ExpenseReportsListItem item) {
                return item.getTotal() == null ? wfmStrings.notAvailable() : "" + (item.getBaseCurrency() != null && item.getBaseCurrency().getSymbol() != null ? item.getBaseCurrency().getSymbol() : "") + Utils.getCalculationNumberFormat().format(item.getTotal());
            }
        };

        columns[7].setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        columns[7].setColumnSortable(true);

        return columns;
    }

    private void changeStatus(final ExpenseReportsListItem item, final String expenseStatus, final String successMessage, final String failureMessage, final String note) {
        LoadingPanel.loading(true);
        expenseService.changeExpenseStatus(item.getId(), expenseStatus, note, null, null, new AbstractAsyncCallback() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(failureMessage, Info.Type.WARNING);
            }

            public void success(Object result) {
                LoadingPanel.loading(false);
                Info.show(successMessage, Info.Type.INFO);
                item.setStatusCode(expenseStatus);
                list.reloadPage();
                if (EXPENSE_DECLINED.equals(expenseStatus)) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EXPENSE_DELETED, result, HrmsExpenseReportListView.this);
                }
                if (EXPENSE_CLOSED.equals(expenseStatus)) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EXPENSE_DELETED, result, HrmsExpenseReportListView.this);
                }
                if (EXPENSE_APPROVED.equals(expenseStatus)) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EXPENSE_APPROVED, result, HrmsExpenseReportListView.this);
                }

            }
        });
    }


    private void declineExpenseReport(final ExpenseReportsListItem item) {
        final KpiModal messageModal = new KpiModal();

        final TextArea2 note = new TextArea2(3000);
        note.setHeight(140);

        final WfmButton2 save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, event -> {
            changeStatus(item, EXPENSE_DECLINED, property.getSingular(accountingStrings.expenseClaimDeclined(), wfmStrings.expenseClaim()), accountingStrings.unableToSubmit(), note.getText());
            messageModal.close();
        });
        final WfmButton2 cancel = new WfmButton2(wfmStrings.cancel(), event -> messageModal.close());

        messageModal.add(note);
        messageModal.addButton(cancel);
        messageModal.addButton(save);

        messageModal.setWidth(400);
        messageModal.setTitle(wfmStrings.rejectionReason());
        messageModal.open();
    }

    @Override
    public void initStatistics(Integer parentId, Span container) {
        loadExpenseReports(new ListingFilterParameter(), null, container);
    }

    private void loadExpenseReports(ListingFilterParameter fp, ListingCallback callback, Span container) {
        fp = fp == null ? new ListingFilterParameter() : fp;
        //If we uncomment below filter then facetfilter will stop working
        fp.setEmployeeId(employeeId);
        fp.setHRMS(true);
        expenseService.getEmployeesReportList(fp, new AsyncCallback<ListResult<ExpenseReportsListItem>>() {

            @Override
            public void onFailure(Throwable throwable) {
                if (callback != null) {
                    callback.onFailure(throwable);
                }
            }

            @Override
            public void onSuccess(ListResult<ExpenseReportsListItem> result) {
                if (callback != null) {
                    callback.onSuccess(result);
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

    @Override
    public String getPropertyCode() {
        return Constants.EXPENSES_CLAIM;
    }
}
