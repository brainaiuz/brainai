package com.edatasite.workforce.gwt.expenses.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.localization.Reference;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrExpenseReportRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.ImportFileActionLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listTable.ImportFilePopUp;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ColumnColor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
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
import com.edatasite.workforce.gwt.core.client.ui.view.PDFTemplateSelector;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseReportsListItem;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseRequestObject;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseService;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseServiceAsync;
import com.edatasite.workforce.gwt.expenses.client.ui.ExpenseConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ExpenseListView extends BaseListView implements Constants, AccountingConstants, PermissionConstants {
    private static final Reference reference = Reference.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final ExpenseServiceAsync expenseService = ExpenseService.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();

    private final boolean isDoubleApproverEnabled = Utils.hasGenericAccess(GenericSettingsEnum.EXPENSE_DOUBLE_APPROVER_ENABLED);
    private final ImportFilePopUp importEmployeeExpense = new ImportFilePopUp(ImportTypeEnum.EXPENSE, null);
    private ListingPanel<ExpenseReportsListItem> list;
    private String context;
    private final boolean addEmployeeExpense = Utils.hasPermission(ACCOUNTING_EXPENSE_REPORT_ADD);
    private final boolean addCompanyExpense = Utils.hasPermission(ACCOUNTING_COMPANY_EXPENSE_ADD);

    private boolean isFromAccounting = true;
    protected HashSet selectedItems = new HashSet();
    private final ActionButton delete = null;

    public ExpenseListView() {
        this(null);
    }

    public ExpenseListView(String context) {
        super(EXPENSES_CLAIM);
        setDescription(property.getPlural(wfmStrings.expenseClaims()));
        this.context = context;

        if (addCompanyExpense && (addEmployeeExpense || !addEmployeeExpense)) {
            setAddNew("expenseReports|add/add/" + ExpenseConstants.COMPANY_EXPENSE);
        } else if (addEmployeeExpense) {
            setAddNew("expenseReports|add/add");
        }
    }

    public ExpenseListView(boolean isFromAccounting) {
        super("expenseList");
        setDescription(property.getPlural(wfmStrings.expenseClaims()));
        this.isFromAccounting = isFromAccounting;
        if (Utils.hasPermission(ACCOUNTING_EXPENSE_REPORT_ADD)) {
            setAddNew("expenseReports|add/add");
        }
    }

    public void refresh() {
        list.reloadPage();
    }

    protected Widget onInitialize() {
        initializeList();
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EXPENSEREPORT_SAVED, ExpenseListView.this, (sender, args) -> list.reloadPage());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EXPENSE_SUBMITTED, ExpenseListView.this, (sender, args) -> {
            Timer t = new Timer() {
                @Override
                public void run() {
                    list.reloadPage();
                }
            };
            t.schedule(5000);
        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EXPENSE_VOID, ExpenseListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EXPENSE_DELETED, ExpenseListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EXPENSE_APPROVED, ExpenseListView.this, (sender, args) -> {
            Timer t = new Timer() {
                @Override
                public void run() {
                    list.reloadPage();
                }
            };
            t.schedule(3500);
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EXPENSE_CLOSED, ExpenseListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EXPENSE_DECLINED, ExpenseListView.this, (sender, args) -> list.reloadPage());

        return null;
    }

    public static ArrayList<Integer> getIDsOnly(HashSet<ExpenseReportsListItem> selectedItems) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (ExpenseReportsListItem item : selectedItems) {
            ids.add(item.getId());
        }
        return ids;
    }

    private void saveExpenseCellValue(ExpenseReportsListItem rowValue, String columnCodeName) {
        if (rowValue.hasAccess()) {
            expenseService.saveExpenseCellValue(rowValue, columnCodeName, new AbstractAsyncCallback<Boolean>() {
            });
        } else {
            Info.warn(wfmStrings.youDontHavePermission());
            list.reloadPage();
        }
    }

    private void createNewExpense() {
        if (isFromAccounting) {
            if (addCompanyExpense && (addEmployeeExpense || !addEmployeeExpense)) {
                goTo("expenseReports|add/add/" + ExpenseConstants.COMPANY_EXPENSE);
            } else if (addEmployeeExpense) {
                goTo("expenseReports|add/add");
            }
        } else {
            goTo("expenseReports|add/add/" + ExpenseConstants.DISBURSEMENT);
        }
    }

    private void initializeList() {
        list = new GuideListingPanel(ListPanelType.ExpenceReportListPanel, drawColumns(), getListingRequestProvider(), getListingPanelDesign(), SelectionGrid.SelectionPolicy.CHECKBOX, false, Utils.hasPermission(ACCOUNTING_EXPENSE_REPORT_LIST_CUSTOMIZE));

        list.setCustomFieldsEditCellSaveChanges((rowValue, columnCodeName) -> saveExpenseCellValue((ExpenseReportsListItem) rowValue, columnCodeName));

        list.setPDFListener(clickEvent -> {
            String pdfURL;
            pdfURL = CommandConstants.PDF_URL + "/expensesListPDFHandler";
            ListingFilterParameter filterParameter = list.getFilterParametrs();
            filterParameter.setPropertyCode(getPropertyCode());
            filterParameter.setStartDateNC(filterParameter.getStartDate() != null ? Utils.getStartDateNCForFilter(filterParameter.getStartDate()) : null);
            filterParameter.setEndDateNC(filterParameter.getEndDate() != null ? Utils.getEndDateNCForFilter(filterParameter.getEndDate()) : null);
            filterParameter.setShowYTD(isDoubleApproverEnabled);
            list.callListPDF(pdfURL, filterParameter);
        });

        list.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadExpenseReportListExcel";
            ListingFilterParameter filterParameter = list.getFilterParametrs();
            filterParameter.setPropertyCode(getPropertyCode());
            filterParameter.setStartDateNC(filterParameter.getStartDate() != null ? Utils.getStartDateNCForFilter(filterParameter.getStartDate()) : null);
            filterParameter.setEndDateNC(filterParameter.getEndDate() != null ? Utils.getEndDateNCForFilter(filterParameter.getEndDate()) : null);
            filterParameter.setShowYTD(isDoubleApproverEnabled);
            list.callListExcel(excelURL, filterParameter);
        });

        list.addSelectionRowHandler(selectedRows -> {
            if (selectedRows.size() > 0) {
                selectedItems = selectedRows;
                if (delete != null) {
                    delete.setVisible(true);
                }
            } else {
                if (delete != null) {
                    delete.setVisible(false);
                }
            }
        });

        add(list);
    }

    public static String getIdsOnly(Set<ExpenseReportsListItem> selectedItems) {
        StringBuilder ids = null;
        for (ExpenseReportsListItem item : selectedItems) {
            if (ids == null) {
                ids = new StringBuilder();
                ids.append(item.getId());
            } else {
                ids.append("," + item.getId());
            }
        }
        return ids.toString();
    }

    private GuideListingPanelDesign getListingPanelDesign() {
        return new GuideListingPanelDesign() {

            @Override
            public Command getAddNewItemCommand() {
                return ExpenseListView.this::createNewExpense;
            }

            @Override
            public Command getUploadButtonCommand() {
                return Utils.hasPermission(PermissionConstants.SHOW_IMPORT_EXPENCE) ? importEmployeeExpense::open : null;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return (data, callback) -> {

                            if (data.getStartDate() != null) {
                                data.setCustomDataPut(STARTDATE_NC, Utils.getStartDateNCForFilter(data.getStartDate()));
                            } else
                                data.getCustomData().remove(STARTDATE_NC);
                            if (data.getEndDate() != null) {
                                data.setCustomDataPut(ENDDATE_NC, Utils.getEndDateNCForFilter(data.getEndDate()));
                            } else
                                data.getCustomData().remove(ENDDATE_NC);
                            RbacService.App.get().getExpenseReportClaimsFacetFilterData(data, null, false, new AbstractAsyncCallback<FacetFilterRpc>() {
                                public void failure(Throwable caught) {
                                    callback.onFailure(caught);
                                }

                                public void success(FacetFilterRpc data) {
                                    callback.onSuccess(data);
                                }
                            });
                        };
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return getFacetContentConfigure();
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addNew = null;
                MenuBar menuBar = new MenuBar(true);
                if (!isFromAccounting) {
                    menuBar = new MenuBar(true);
                    MenuPopItem expense = new MenuPopItem(wfmStrings.expense());
                    MenuPopItem timesheetExpense = new MenuPopItem(wfmStrings.projectBaseExpense());

                    expense.setCommand(() -> goTo("expenseReports|add/add/" + ExpenseConstants.DISBURSEMENT));
                    timesheetExpense.setCommand(() -> goTo("projectBaseExpense|add/add"));
                    menuBar.addItem(expense);
                    menuBar.addItem(timesheetExpense);
                    addNew = getAddNewButton(ActionButton.Type.TOOLMENU);
                    addNew.setMenu(menuBar);
                } else {
                    if (addCompanyExpense && addEmployeeExpense) {
                        return addButton();
                    } else if (addCompanyExpense) {
                        return addNewCompanyExpense();
                    } else if (addEmployeeExpense) {
                        return addNewEmployeeExpense();
                    }
                    return null;
                }
                return addNew;
            }

            @Override
            public ActionButton initTopToolBarPrint() {
                return getPrintButton(clickEvent -> printSelection());
            }

            @Override
            public ActionButton initTopToolBarMore() {
                if (Utils.hasPermission(ACCOUNTING_EXPENSE_REPORT_DELETE)) {
                    return getRemoveMoreButton(clickEvent -> deleteSelection());
                }
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {

                if (Utils.hasPermission(PermissionConstants.SHOW_IMPORT_EXPENCE)) {
                    importEmployeeExpense.setSubmitCompleted(() -> {
                        if (importEmployeeExpense.getObjectId() != null) {
                            goTo("importcustomexpense|add/add/" + importEmployeeExpense.getObjectId());
                        }
                    });

                    ImportFilePopUp importCompanyExpense = new ImportFilePopUp(ImportTypeEnum.COMPANY_EXPENSE, null);
                    importCompanyExpense.setSubmitCompleted(() -> {
                        if (importCompanyExpense.getObjectId() != null) {
                            goTo("importcustomexpense|add/add/" + importCompanyExpense.getObjectId() + "/" + Constants.IMPORT_COMPANY_EXPENSE_CLAIMS);
                        }
                    });

                    ImportFileActionLink eeLink = new ImportFileActionLink();
                    eeLink.setText(wfmStrings.importString());
                    eeLink.addClickHandler(ch -> importEmployeeExpense.open());
                    menuContainer.add(eeLink);

                    ImportFileActionLink ceLink = new ImportFileActionLink();
                    ceLink.setText(wfmStrings.companyExpense());
                    ceLink.addClickHandler(ch -> importCompanyExpense.open());


                    exportOption.initExport(null, true);
                } else {
                    exportOption.initExport(null);
                }
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(property.getPlural(accountingStrings.currentlyYouHaveNoRegisteredExpenseClaim(), wfmStrings.expenseClaims()));
                if (addCompanyExpense && (addEmployeeExpense || !addEmployeeExpense)) {
                    message.setTextBeforeLink(accountingStrings.toStartReportingClick());
                    message.setHref("expenseReports|add/add" + ExpenseConstants.COMPANY_EXPENSE);
                } else if (addEmployeeExpense) {
                    message.setTextBeforeLink(accountingStrings.toStartReportingClick());
                    message.setHref("expenseReports|add/add");
                }
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return Utils.hasPermission(ACCOUNTING_EXPENSE_REPORT_EDIT);
            }
        };
    }

    private void printSelection() {
        if (selectedItems.size() == 0) {
            Info.show(accountingMessages.pleaseSelectOneRow(wfmStrings.expense()), Info.Type.WARNING);
        } else {
            new PDFTemplateSelector(EXPENSE_REPORTS, new ExtendedCommand() {
                @Override
                public void execute(Integer id) {
                    final HTMLPanel htmlPanel = new HTMLPanel("");
                    add(htmlPanel);
                    ExpenseRequestObject requestObject = new ExpenseRequestObject(getIdsOnly(selectedItems), id);
                    String pdfURL = CommandConstants.PDF_URL + "/expensesViewPDFHandler";
                    HashMap<String, String> parametrs = requestObject.getRequestParams();
                    Utils.sendPDFOrExcelRequest(htmlPanel, pdfURL, parametrs, "_blank");
                }
            });
        }
    }

    private void deleteSelection() {
        if (selectedItems.size() == 0) {
            Info.show(accountingMessages.pleaseSelectOneRow(wfmStrings.expense()), Info.Type.WARNING);
        } else {
            showDeleteMessage();
        }
    }

    private void showDeleteMessage() {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
        messageBox.setTitle(wfmStrings.warning());
        String message = wfmStrings.areYouSureYouWantToDeleteTheSelectedRecords();
        messageBox.setMessage(message);
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                ArrayList<Integer> ids = getIDsOnly(selectedItems);
                if (ids.size() > 0) {
                    LoadingPanel.loading(true);
                    expenseService.deleteSelectedExpenseReports(ids, new AbstractAsyncCallback<Void>() {
                        @Override
                        public void failure(Throwable caught) {
                            LoadingPanel.loading(false);
                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                        }

                        @Override
                        public void success(Void result) {
                            list.reloadPage();
                            LoadingPanel.loading(false);
                            Info.show(property.getSingular(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.expenses()), Info.Type.INFO);

                            for (Integer id : ids) {
                                removeDeletedTab(EXPENSE_REPORTS + id);
                            }
                        }
                    });
                }
            }
        });
        messageBox.open();
    }

    private ActionButton addButton() {
        ActionButton newItem = getAddNewButton(ActionButton.Type.TOOLMENU);
        MenuBar menu = new MenuBar(true);

        MenuPopItem addCompanyExpense = new MenuPopItem(wfmStrings.companyExpense());
        addCompanyExpense.setCommand(() -> {
            goTo("expenseReports|add/add/" + ExpenseConstants.COMPANY_EXPENSE);
        });
        menu.addItem(addCompanyExpense);

        MenuPopItem addEmployeeExpense = new MenuPopItem(accountingStrings.employeeExpense());
        addEmployeeExpense.setCommand(() -> {
            goTo("expenseReports|add/add");
        });
        menu.addItem(addEmployeeExpense);

        newItem.setMenu(menu);
        return newItem;
    }

    private ActionButton addNewCompanyExpense() {
        ActionButton addCompanyExpense = getAddNewButton(ActionButton.Type.TOOLMENU);
        addCompanyExpense.addClickHandler(clickEvent -> {
            goTo("expenseReports|add/add/" + ExpenseConstants.COMPANY_EXPENSE);
        });
        return addCompanyExpense;
    }

    public ActionButton addNewEmployeeExpense() {
        ActionButton addEmployeeExpense = getAddNewButton(ActionButton.Type.TOOLMENU);
        addEmployeeExpense.addClickHandler(clickEvent -> {
            goTo("expenseReports|add/add");
        });
        return addEmployeeExpense;
    }

    private ListingRequestProvider<ExpenseReportsListItem> getListingRequestProvider() {
        return (filterParametrs, callback) -> {

            if (filterParametrs.getFacetFilter() != null) {
                filterParametrs.setStartDate(filterParametrs.getFacetFilter().getStartDate());
                filterParametrs.setEndDate(filterParametrs.getFacetFilter().getEndDate());
            }
            filterParametrs.setStartDateNC(filterParametrs.getStartDate() != null ? Utils.getStartDateNCForFilter(filterParametrs.getStartDate()) : null);
            filterParametrs.setEndDateNC(filterParametrs.getEndDate() != null ? Utils.getEndDateNCForFilter(filterParametrs.getEndDate()) : null);

            expenseService.getExpenseReportsDataFromSolr(filterParametrs, new AsyncCallback<ListResult<ExpenseReportsListItem>>() {
                @Override
                public void onFailure(Throwable caught) {
                    callback.onFailure(caught);
                    caught.printStackTrace();
                }

                @Override
                public void onSuccess(ListResult<ExpenseReportsListItem> result) {
                    callback.onSuccess(result);
                }
            });
        };
    }

    private FacetContentConfigure getFacetContentConfigure() {
        FacetContentConfigure contentConfigure = new FacetContentConfigure(isDoubleApproverEnabled ? 4 : 3, wfmStrings.filter());
        contentConfigure.addContentConfigure(FacetContentType.ExpenseReportsClaimsFacetFilter.getContentCode()[0], Property.get(Constants.PROJECT, wfmStrings.project()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return Utils.isProjectInLineItemEnable() ? SolrExpenseReportRepresenter.FIELD_MULTI_PROJECT_ID : SolrExpenseReportRepresenter.FIELD_RELATED_PROJECT_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return Utils.isProjectInLineItemEnable() ? SolrExpenseReportRepresenter.FIELD_MULTI_PROJECT_ID_NAME : SolrExpenseReportRepresenter.FIELD_RELATED_PROJECT_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.ExpenseReportsClaimsFacetFilter.getContentCode()[1], wfmStrings.reporter(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrExpenseReportRepresenter.FIELD_REPORTER_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrExpenseReportRepresenter.FIELD_REPORTER_ID_NAME;
            }
        });
        if (isDoubleApproverEnabled) {

            contentConfigure.addContentConfigure(FacetContentType.ExpenseReportsClaimsFacetFilter.getContentCode()[3], wfmStrings.approver(), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrExpenseReportRepresenter.FIELD_APPROVER2_ID;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrExpenseReportRepresenter.FIELD_APPROVER2_ID_NAME;
                }

//                @Override
//                public boolean isShowFacetConttentFilter() {
//                    return false;
//                }
            });

            contentConfigure.addContentConfigure(FacetContentType.ExpenseReportsClaimsFacetFilter.getContentCode()[2], wfmStrings.accountant(), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrExpenseReportRepresenter.FIELD_APPROVER_ID;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrExpenseReportRepresenter.FIELD_APPROVER_ID_NAME;
                }

                @Override
                public boolean isShowFacetConttentFilter() {
                    return false;
                }
            });

            contentConfigure.addContentConfigure(FacetContentType.ExpenseReportsClaimsFacetFilter.getContentCode()[4], wfmStrings.accountantStatus(), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrExpenseReportRepresenter.FIELD_STATUS_ID;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrExpenseReportRepresenter.FIELD_STATUS_ID_NAME;
                }

            });
            contentConfigure.addContentConfigure(FacetContentType.ExpenseReportsClaimsFacetFilter.getContentCode()[5], wfmStrings.approverStatus(), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrExpenseReportRepresenter.FIELD_STATUS2_ID;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrExpenseReportRepresenter.FIELD_STATUS2_ID_NAME;
                }

                @Override
                public boolean isShowFacetConttentFilter() {
                    return false;
                }
            });
        } else {

            contentConfigure.addContentConfigure(FacetContentType.ExpenseReportsClaimsFacetFilter.getContentCode()[5], wfmStrings.status(), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrExpenseReportRepresenter.FIELD_STATUS_ID;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrExpenseReportRepresenter.FIELD_STATUS_ID_NAME;
                }
            });
            contentConfigure.addContentConfigure(FacetContentType.ExpenseReportsClaimsFacetFilter.getContentCode()[3], wfmStrings.approver(), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrExpenseReportRepresenter.FIELD_APPROVER_ID;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrExpenseReportRepresenter.FIELD_APPROVER_ID_NAME;
                }

//                @Override
//                public boolean isShowFacetConttentFilter() {
//                    return false;
//                }
            });
        }
//        contentConfigure.addContentConfigure(FacetContentType.ExpenseReportsClaimsFacetFilter.getContentCode()[6], wfmStrings.amount(), new FacetFieldConfigure() {
//            @Override
//            public String getSolrFieldCriteriaName() {
//                return SolrExpenseReportRepresenter.FIELD_ORIGINAL_AMOUNT;
//            }
//
//            @Override
//            public String getSolrFacetFieldName() {
//                return SolrExpenseReportRepresenter.FIELD_ORIGINAL_AMOUNT;
//            }
//
//            @Override
//            public boolean isConditionItemId() {
//                return false;
//            }
//        });
        contentConfigure.addContentConfigure(FacetContentType.ExpenseReportsClaimsFacetFilter.getContentCode()[7], Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrExpenseReportRepresenter.FIELD_SUPPLIER_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrExpenseReportRepresenter.FIELD_SUPPLIER_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.ExpenseReportsClaimsFacetFilter.getContentCode()[8], wfmStrings.currency(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrExpenseReportRepresenter.FIELD_CURRENCY_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrExpenseReportRepresenter.FIELD_CURRENCY_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.ExpenseReportsClaimsFacetFilter.getContentCode()[9], wfmStrings.type(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrExpenseReportRepresenter.FIELD_IS_COMPANY_EXPENSE;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrExpenseReportRepresenter.FIELD_IS_COMPANY_EXPENSE;
            }

            @Override
            public boolean isWithID() {
                return false;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        return contentConfigure;
    }

    public String getIconStyle() {
        return "accountMark report-list";
    }

    private int actionItemCount;

    private ColumnDefinitionConfig[] drawColumns() {

        ArrayList<CustomColumnDefinitionConfig> columns = new ArrayList<>();

        //Expense Claims title
        ColumnDefinitionConfig column = new ColumnDefinitionConfig<ExpenseReportsListItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {

            @Override
            public Anchor getCellValue(final ExpenseReportsListItem item) {
                actionItemCount = 0;
                final boolean hasAccessToChange = !Utils.isLockCompletedProjecItems() || (Utils.isLockCompletedProjecItems() && !PS_CLOSED.equals(item.getProjectStatusCode()));
                boolean hasAccountingBeforeBlockDate = (Utils.isExpensesLocked() && DateUtils.getTransactionLockDate().after(item.getStartDate().getNonConvertedDate()));

                String statusCode = item.getStatusCode();
                boolean isSubmittedExpenseEditable = !isDoubleApproverEnabled && Utils.hasRole(ADMIN) && (EXPENSE_APPROVED.equals(statusCode) || EXPENSE_SUBMITTED.equals(statusCode));
                boolean hasFullEdit = Utils.hasPermission(ACCOUNTING_EXPENSE_REPORT_FULL_EDIT_ACCESS);
                boolean hasEdit = Utils.hasPermission(ACCOUNTING_EXPENSE_REPORT_EDIT);

                boolean isAccessToEdit = ((hasEdit && item.getReporterId().equals(Utils.getUserID())) || hasFullEdit) && !item.isAllocatedToPO();
                if (item.isApproveProcessEnabled()) {
                    isAccessToEdit = hasEdit && item.getApproverSelectItem() != null && Utils.getUserID().equals(item.getApproverSelectItem().getId()) && !item.isAllocatedToPO();
                }


                MenuBar menuBar = new MenuBar(true);
                MenuPopItem reportSummary = new MenuPopItem(wfmStrings.summaryView(), isFromAccounting ? "icon-edit" : "icon-task-small");

                if (!EXPENSE_DRAFT.equals(statusCode)) {
                    reportSummary.setCommand(() -> {
                        String contextStr = context != null && !"".equals(context.trim()) ? "/" + context : "";
                        SinksContainerFactory.entryPoint.onHistoryChanged("expenseReports|previewReport/" + item.getId() + "/" + Constants.EXPENSE_VIEW + contextStr, item.getExpenseNumber());
                    });
                    actionItemCount++;
                    menuBar.addItem(reportSummary);
                }


                boolean isDrAdmAccAndDoubleApproveDisabled = Utils.hasPermission(ACCOUNTING_CAN_APPROVE_EXPENSE_CLAIM);
                if (Utils.getUserID().equals(item.getApproverSelectItem().getId()) && !isDoubleApproverEnabled) {
                    isDrAdmAccAndDoubleApproveDisabled = true;
                }

//                if (hasAccessToChange && (isDrAdmAccAndDoubleApproveDisabled || item.isPreferredApprover(Utils.getUserID())) && !hasAccountingBeforeBlockDate) {
//                    if (EXPENSE_SUBMITTED.equals(statusCode)) {
//                        if (item.isCategoriesSelected()) {
//                            MenuPopItem approveReport = new MenuPopItem(accountingStrings.approve(), "icon-approve");
//                            approveReport.setCommand(() -> changeStatus(item, EXPENSE_APPROVED, accountingStrings.expenseClaimApproved(), accountingStrings.unableToSubmit(), null));
//                            actionItemCount++;
//                            menuBar.addItem(approveReport);
//                        }
//
//                        MenuPopItem declineReport = new MenuPopItem(accountingStrings.decline(), "icon-decline");
//                        declineReport.setCommand(() -> declineExpenseReport(item));
//                        actionItemCount++;
//                        menuBar.addItem(declineReport);
//
//                    }
//                }


                if (EXPENSE_DRAFT.equals(statusCode) && (hasEdit || hasFullEdit) || (hasAccessToChange && isAccessToEdit && (EXPENSE_DRAFT.equals(statusCode) || (EXPENSE_SUBMITTED.equals(item.getStatusCode())) || EXPENSE_DECLINED.equals(statusCode) || isSubmittedExpenseEditable || EXPENSE_APPROVED.equals(statusCode) || EXPENSE_PAID.equals(statusCode) || PARTIALLY_PAID.equals(statusCode)) && !hasAccountingBeforeBlockDate)) {
                    item.setAccess(true);
                    MenuPopItem editReport = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                    editReport.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("expenseReports|edit/" + item.getId(), item.getExpenseNumber()));
                    actionItemCount++;
                    menuBar.addItem(editReport);

                }

                if (hasAccessToChange && Utils.hasPermission(ACCOUNTING_EXPENSE_REPORT_COPY)) {
                    MenuPopItem copyTo = new MenuPopItem(wfmStrings.copy(), "icon-copy");
                    copyTo.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("expenseReports|add/add/" + COPY_FROM_EXISTING + "/" + item.getId()));
                    actionItemCount++;
                    menuBar.addItem(copyTo);
                }

                if (hasAccessToChange && !hasAccountingBeforeBlockDate && (isDrAdmAccAndDoubleApproveDisabled || Utils.getUserID().equals(item.getReporterId()))
                        && (EXPENSE_DRAFT.equals(statusCode) || EXPENSE_DECLINED.equals(statusCode))) {

                    if (!item.isDoubleApproverEnabled() && Utils.getUserID().equals(item.getApproverSelectItem().getId())) {
                        /*MenuPopItem approveReport = new MenuPopItem(accountingStrings.approve(), "icon-approve");
                        approveReport.setCommand(new Command() {
                            @Override
                            public void execute() {
                                approveExpenseReport(item);
                            }
                        });
                        actionItemCount++;
                        menuBar.addItem(approveReport);*/
                    } else {
                        MenuPopItem submitReport = new MenuPopItem(EXPENSE_DRAFT.equals(item.getStatusCode()) ? wfmStrings.submit() : wfmStrings.resubmit(), "icon-submit-report");
                        submitReport.setCommand(() -> submitShell(item));
                        actionItemCount++;
                        menuBar.addItem(submitReport);
                    }
                }
                boolean hasFullDelete = Utils.hasPermission(ACCOUNTING_EXPENSE_REPORT_FULL_DELETE_ACCESS);
                boolean hasDelete = Utils.hasPermission(ACCOUNTING_EXPENSE_REPORT_DELETE);
                if (hasAccessToChange &&
                        ((hasDelete && item.getReporterId().equals(Utils.getUserID())) || hasFullDelete) &&
                        !hasAccountingBeforeBlockDate && !item.isAllocatedToPO()) {

                    MenuPopItem deleteReport = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    deleteReport.setCommand(() -> {
                        WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                        wfmMessageBox.setTitle(wfmStrings.warning());
                        wfmMessageBox.setMessage(wfmStrings.sureYouWantToDelete());

                        wfmMessageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                LoadingPanel.loading(true);
                                expenseService.deleteExpenseReport(item.getId(), new AsyncCallback<Boolean>() {
                                    public void onFailure(Throwable caught) {
                                        LoadingPanel.loading(false);
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    public void onSuccess(Boolean result) {
                                        LoadingPanel.loading(false);
                                        if (result) {
                                            list.reloadPage();
                                            Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), accountingStrings.expenseReports()), Info.Type.INFO);
                                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EXPENSE_DELETED, result, ExpenseListView.this);
                                            removeDeletedTab(EXPENSE_REPORTS + item.getId());
                                        } else {
                                            Info.show(accountingStrings.cannotDeleteExpenseClaim(), Info.Type.WARNING);
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

                if (hasAccessToChange && (Utils.hasPermission(ACCOUNTING_EXPENSE_REPORT_VOID)) && isDrAdmAccAndDoubleApproveDisabled && (EXPENSE_APPROVED.equals(statusCode) || EXPENSE_PAID.equals(statusCode) || PARTIALLY_PAID.equals(statusCode))
                        && !hasAccountingBeforeBlockDate && !item.isAllocatedToPO()) {
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
                                final DatePicker datePicker = new DatePicker(item.getStartDate().getNonConvertedDate());
                                dialogBox.setTitle(wfmStrings.selectVoidDate());
                                datePicker.setWidth("180px");
                                datePicker.getElement().getStyle().setMargin(10, Style.Unit.PX);
                                dialogBox.add(datePicker);
                                final WfmButton2 voidButton = new WfmButton2(accountingStrings.voide(), WfmButton2.BTN_PRIMARY);
                                dialogBox.addButton(voidButton);
                                voidButton.addClickHandler(clickEvent -> {
                                    if (AccountingUtils.validateExpanseVoidDate(datePicker.getDate(), item.getStartDate().getNonConvertedDate())) {
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
                                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EXPENSE_VOID, result, ExpenseListView.this);
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

                if (hasAccessToChange && Utils.hasGenericAccess(GenericSettingsEnum.EXPENSE_CLOSE_STATUS_ENABLED) && (Utils.hasPermission(ACCOUNTING_EXPENSE_REPORT_VOID)) && EXPENSE_PAID.equals(statusCode) && !hasAccountingBeforeBlockDate) {
                    MenuPopItem closeExpenseReport = new MenuPopItem(accountingStrings.markAsClose(), "icon-puchase-invoise-small");
                    closeExpenseReport.setCommand(() -> changeStatus(item, EXPENSE_CLOSED, property.getSingular(accountingStrings.expenseClaimClosed(), wfmStrings.expenseClaim()), accountingStrings.unableToSubmit(), null));
                    actionItemCount++;
                    menuBar.addItem(closeExpenseReport);
                }


                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        column.setColumnSortable(false);
        column.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns.add(column);

        //Expense numbering column
        column = new ColumnDefinitionConfig<ExpenseReportsListItem, Widget>(property.getShortForNumber(wfmStrings.number()), NUMBER_COLUMN, 100) {

            @Override
            public Widget getCellValue(ExpenseReportsListItem item) {
                return getLink(item);
            }
        };
        columns.add(column);

        column = new ColumnDefinitionConfig<ExpenseReportsListItem, Widget>(wfmStrings.title(), TITLE_COLUMN, 210) {

            @Override
            public Widget getCellValue(ExpenseReportsListItem item) {
                if (EXPENSE_DRAFT.equals(item.getStatusCode())) {
                    boolean hasFullEdit = Utils.hasPermission(ACCOUNTING_EXPENSE_REPORT_FULL_EDIT_ACCESS);
                    boolean hasEdit = Utils.hasPermission(ACCOUNTING_EXPENSE_REPORT_EDIT);

                    if ((hasEdit || hasFullEdit)) {
                        return new SimpleLink(item.getTitle(), "expenseReports|edit/" + item.getId(), item.getExpenseNumber());
                    }
                    return new Label(item.getTitle());
                } else {
                    String contextStr = context != null && !context.trim().isEmpty() ? "/" + context : "";
                    return new SimpleLink(item.getTitle() != null ? item.getTitle() : "", "expenseReports|previewReport/" + item.getId() + "/" + Constants.EXPENSE_VIEW + contextStr, item.getExpenseNumber(), item.getExpenseNumber());
                }
            }
        };
        columns.add(column);

        //Reporter's name
        column = new ColumnDefinitionConfig<ExpenseReportsListItem, String>(wfmStrings.reporter(), REPORTER_COLUMN, 120) {

            @Override
            public String getCellValue(ExpenseReportsListItem item) {
                return item.getReporterName() != null ? item.getReporterName() : wfmStrings.notAvailable();
            }
        };
        columns.add(column);


        //Report period
        column = new ColumnDefinitionConfig<ExpenseReportsListItem, String>(wfmStrings.date(), PERIOD_COLUMN, 100) {

            @Override
            public String getCellValue(ExpenseReportsListItem item) {
                return DateUtils.format(item.getStartDate());
            }
        };
        column.addStyleAttribute("paddingLeft", "5px");
        columns.add(column);

        //Original Amount
        column = new ColumnDefinitionConfig<ExpenseReportsListItem, String>(wfmStrings.amount(), ORIGINAL_AMOUNT_COLUMN, 120) {

            @Override
            public String getCellValue(ExpenseReportsListItem item) {
                return item.getTotal() == null ? wfmStrings.notAvailable() : AccountingUtils.get().formatPrice(item.getTotal());
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        column.addStyleAttribute("paddingRight", "5px");
        columns.add(column);

        //Total Tax Amount
        column = new ColumnDefinitionConfig<ExpenseReportsListItem, String>(wfmStrings.taxAmount(), TAX_AMOUNT_COLUMN, 120) {
            @Override
            public String getCellValue(ExpenseReportsListItem item) {
                return item.getTaxTotal() == null ? wfmStrings.notAvailable() : AccountingUtils.get().formatPrice(item.getTaxTotal());
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        column.addStyleAttribute("paddingRight", "5px");
        columns.add(column);

        //Paid Amount
        column = new ColumnDefinitionConfig<ExpenseReportsListItem, String>(wfmStrings.paidAmount(), PAID_AMOUNT_COLUMN, 100) {

            @Override
            public String getCellValue(ExpenseReportsListItem item) {
                return item.getPaidTotal() == null ? wfmStrings.notAvailable() : AccountingUtils.get().formatPrice(item.getPaidTotal());
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        columns.add(column);

        //Due Amount
        column = new ColumnDefinitionConfig<ExpenseReportsListItem, String>(wfmStrings.dueAmount(), DUE_AMOUNT_COLUMN, 100) {

            @Override
            public String getCellValue(ExpenseReportsListItem item) {
                return item.getDueTotal() == null ? wfmStrings.notAvailable() : AccountingUtils.get().formatPrice(item.getDueTotal());
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        columns.add(column);

        //Status
        column = new ColumnDefinitionConfig<ExpenseReportsListItem, String>(wfmStrings.status(), STATUS_COLUMN, 100) {

            @Override
            public String getCellValue(ExpenseReportsListItem item) {
                if (EXPENSE_APPROVED.equals(item.getStatusCode())) {
                    return wfmStrings.approved();
                } else if (EXPENSE_SUBMITTED.equals(item.getStatusCode())) {
                    return wfmStrings.submitted();
                } else if (EXPENSE_DECLINED.equals(item.getStatusCode())) {
                    return wfmStrings.rejected();
                } else if (INVOICED.equals(item.getStatusCode())) {
                    return accountingStrings.invoiced();
                } else if (EXPENSE_PAID.equals(item.getStatusCode())) {
                    return wfmStrings.paid();
                } else if (EXPENSE_DRAFT.equals(item.getStatusCode())) {
                    return wfmStrings.draft();
                } else if (EXPENSE_REVERSED.equals(item.getStatusCode())) {
                    return wfmStrings.reversed();
                } else {
                    return item.getStatus();
                }
            }
        };
        column.addColor(new ColumnColor(reference.EXPENSE_PAID(), "r", "2BBF57"));
        column.addColor(new ColumnColor(reference.EXPENSE_APPROVED(), "r", "007DE7"));
        column.addColor(new ColumnColor(reference.EXPENSE_DECLINED(), "c", "DC0C0C"));
        column.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columns.add(column);

        //Approver's name
        column = new ColumnDefinitionConfig<ExpenseReportsListItem, String>(wfmStrings.approver(), APPROVER_COLUMN, 70) {

            @Override
            public String getCellValue(ExpenseReportsListItem item) {
                return item.getApproverSelectItem() != null && item.getApproverSelectItem().getName() != null ? item.getApproverSelectItem().getName() : "";
            }
        };
        column.setShow(false);
        columns.add(column);

        column = new ColumnDefinitionConfig<ExpenseReportsListItem, String>(wfmStrings.currency(), CURRENCY_COLUMN, 100) {
            @Override
            public String getCellValue(ExpenseReportsListItem item) {
                return item.getExpenseCurrency() != null ? item.getExpenseCurrency().getName() : "";
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        column.setMinimumColumnWidth(70);
        column.setShow(false);
        columns.add(column);

        //Related project
        column = new ColumnDefinitionConfig<ExpenseReportsListItem, String>(Property.get(Constants.PROJECT, wfmStrings.relatedSupplier(), wfmStrings.project()), PROJECT_COLUMN, 100) {

            @Override
            public String getCellValue(ExpenseReportsListItem item) {
                return item.getProjectName() == null ? wfmStrings.notAvailable() : item.getProjectName();
            }
        };
        column.addStyleAttribute("paddingLeft", "5px");
        column.setShow(false);
        columns.add(column);

        column = new ColumnDefinitionConfig<ExpenseReportsListItem, String>(accountingStrings.relatedPO(), RELATED_PO, 100) {
            @Override
            public String getCellValue(ExpenseReportsListItem item) {
                return item.getPurchaseOrderNumber() != null ? item.getPurchaseOrderNumber() : "";
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        column.setMinimumColumnWidth(70);
        column.setColumnSortable(false);
        column.setShow(false);
        columns.add(column);


        column = new ColumnDefinitionConfig<ExpenseReportsListItem, String>(wfmStrings.fixedAsset(), FIXED_ASSET, 100) {
            @Override
            public String getCellValue(ExpenseReportsListItem item) {
                return item.getFixedAsset() != null ? item.getFixedAsset().getName() : "";
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        column.setMinimumColumnWidth(70);
        column.setColumnSortable(false);
        column.setShow(false);
        columns.add(column);

        column = new ColumnDefinitionConfig<ExpenseReportsListItem, String>(Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()), SUPPLIER, 100) {
            @Override
            public String getCellValue(ExpenseReportsListItem item) {
                return item.getSupplier() != null ? item.getSupplier().getName() : "";
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        column.setMinimumColumnWidth(70);
        column.setColumnSortable(false);
        column.setShow(false);
        columns.add(column);

        column = new ColumnDefinitionConfig<ExpenseReportsListItem, String>(wfmStrings.type(), TYPE_COLUMN, 100) {
            @Override
            public String getCellValue(ExpenseReportsListItem item) {
                return item.isCompanyExpense() ? wfmStrings.companyExpense() : accountingStrings.employeeExpense();
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        column.setShow(false);

        columns.add(column);


        return columns.toArray(new ColumnDefinitionConfig[columns.size()]);
    }

    private void declineExpenseReport(final ExpenseReportsListItem item) {
        final KpiModal messageModal = new KpiModal();

        final TextArea2 textArea = new TextArea2(3000);
        textArea.getTextArea().setWidth("100%");
        textArea.getTextAreaPanel().setWidth("150px");

        final WfmButton2 save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, event -> {
            changeStatus(item, EXPENSE_DECLINED, property.getSingular(accountingStrings.expenseClaimDeclined(), wfmStrings.expenseClaim()), accountingStrings.unableToSubmit(), textArea.getText());
            messageModal.close();
        });
        final WfmButton2 cancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT, event -> messageModal.close());

        messageModal.setTitle(wfmStrings.rejectionReason());
        messageModal.add(textArea);
        messageModal.addButton(cancel);
        messageModal.addButton(save);

        messageModal.setWidth(400);
        messageModal.open();
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
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EXPENSE_DELETED, result, ExpenseListView.this);
                }
                if (EXPENSE_CLOSED.equals(expenseStatus)) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EXPENSE_DELETED, result, ExpenseListView.this);
                }
                if (EXPENSE_APPROVED.equals(expenseStatus)) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EXPENSE_APPROVED, result, ExpenseListView.this);
                }

            }
        });
    }

    private void submitShell(final ExpenseReportsListItem report) {
        final WfmMessageBox alert = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo);
        alert.setTitle(wfmStrings.submit());
        alert.setMessage(accountingStrings.doYouWantToSubmit() + " " + report.getTitle() + " " + property.getPlural(wfmStrings.expenseClaims()));
        alert.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                changeStatus(report, Constants.EXPENSE_SUBMITTED, property.getSingular(accountingStrings.expenseClaimSubmitted(), wfmStrings.expenseClaim()), accountingStrings.unableToSubmit(), null);
            }
        });
        alert.open();
    }

    private Widget getLink(ExpenseReportsListItem item) {

        if (EXPENSE_DRAFT.equals(item.getStatusCode())) {
            boolean hasFullEdit = Utils.hasPermission(ACCOUNTING_EXPENSE_REPORT_FULL_EDIT_ACCESS);
            boolean hasEdit = Utils.hasPermission(ACCOUNTING_EXPENSE_REPORT_EDIT);

            if ((hasEdit || hasFullEdit)) {
                return new SimpleLink(item.getExpenseNumber(), "expenseReports|edit/" + item.getId(), item.getExpenseNumber());
            }
            return new Label(item.getExpenseNumber());
        } else {
            String contextStr = context != null && !"".equals(context.trim()) ? "/" + context : "";
            return new SimpleLink(item.getExpenseNumber(), "expenseReports|previewReport/" + item.getId() + "/" + Constants.EXPENSE_VIEW + contextStr, item.getExpenseNumber());
        }
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
        return EXPENSES_CLAIM;
    }

}
