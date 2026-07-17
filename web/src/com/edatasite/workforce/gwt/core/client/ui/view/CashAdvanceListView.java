package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.MyUpdateItem;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCashAdvanceRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
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
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 02.08.14
 * Time: 5:06
 * To change this template use File | Settings | File Templates.
 */
public class CashAdvanceListView extends BaseListView implements Constants {

    private ListingPanel<CashAdvanceItem> list;
    private Integer employeeID;
    private boolean fromHrms;
    private boolean fromEmployeeProfile;

    public CashAdvanceListView() {
        super(CASH_ADVANCE_LIST);
        setDescription(property.getSingular(wfmStrings.cashAdvance()));
        if (hasPermissionToAdd()) {
            setAddNew("cashAdvance|add/add");
        }
    }

    private boolean hasPermissionToAdd() {
        return Utils.hasPermission(PermissionConstants.PAYROLL_CASH_ADVANCE_ADD);
    }

    public CashAdvanceListView(Integer id, boolean isFromHrms) {
        super(CASH_ADVANCE_LIST);
        setDescription(property.getSingular(wfmStrings.cashAdvance()));
        employeeID = id;
        fromHrms = isFromHrms;
        if (hasPermissionToAdd()) {
            setAddNew("cashAdvance|add/add");
        }
    }

    public CashAdvanceListView(Integer id, boolean isFromHrms, boolean isRequestFromEmployeeProfile) {
        super(CASH_ADVANCE_LIST);
        setDescription(property.getSingular(wfmStrings.cashAdvance()));
        employeeID = id;
        fromHrms = isFromHrms;
        fromEmployeeProfile=isRequestFromEmployeeProfile;
        if (hasPermissionToAdd()) {
            setAddNew("cashAdvance|add/add");
        }
    }




    @Override
    protected Widget onInitialize() {
        boolean[] reload = {true, false};
        Timer timer = new Timer() {
            @Override
            public void run() {
                if (reload[1]) {
                    list.reloadPage();
                    reload[1] = false;
                    reload[0] = true;
                }
            }
        };
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CASH_SAVED, CashAdvanceListView.this, (sender, args) -> {
            if (reload[0]) {
                list.reloadPage();
                reload[0] = false;
            } else {
                reload[1] = true;
            }
            if (!timer.isRunning()) {
                timer.schedule(2000);
            }
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CASH_REJECTED, CashAdvanceListView.this, (sender, args) -> {
            if (!timer.isRunning()) {
                list.reloadPage();
                timer.schedule(2000);
            }
        });

        list = new GuideListingPanel(ListPanelType.CashAdvancePanel, getColumn(), getListingRequestProvider(), getListDesign());

        list.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/cashAdvanceListExcelHandler";
            ListingFilterParameter filterParameter = list.getFilterParametrs();
            filterParameter.setPropertyCode(getPropertyCode());
            list.callListExcel(excelURL, filterParameter);
        });
        list.setPDFListener(clickEvent -> {
            String pdfURL = CommandConstants.PDF_URL + "/cashAdvanceListPDFHandler";
            ListingFilterParameter filterParameter = list.getFilterParametrs();
            filterParameter.setPropertyCode(getPropertyCode());
            list.callListPDF(pdfURL, filterParameter);
        });

        add(list);
        return null;
    }


    private GuideListingPanelDesign getListDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return hasPermissionToAdd() ? CashAdvanceListView.this::addNewCashAdvance : null;
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
                        return (data, callback) -> {
                            if (data.getStartDate() != null) {
                                data.setCustomDataPut(STARTDATE_NC, Utils.getStartDateNCForFilter(data.getStartDate()));
                            } else data.getCustomData().remove(STARTDATE_NC);
                            if (data.getEndDate() != null) {
                                data.setCustomDataPut(ENDDATE_NC, Utils.getEndDateNCForFilter(data.getEndDate()));
                            } else data.getCustomData().remove(ENDDATE_NC);
                            if (employeeID != null) {
                                data.setUserID(employeeID);
                            }
                            data.setHRMS(fromHrms);

                            RbacService.App.get().getCashAdvanceFacetFilterData(data, new AbstractAsyncCallback<FacetFilterRpc>() {
                                @Override
                                public void failure(Throwable throwable) {
                                    callback.onFailure(throwable);
                                }

                                @Override
                                public void success(FacetFilterRpc result) {
                                    callback.onSuccess(result);
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
                if (hasPermissionToAdd()) {
                    ActionButton addNew = getAddNewButton();
                    addNew.addClickHandler(clickEvent -> {
                        addNewCashAdvance();
                    });
                    return addNew;
                } else {
                    return null;
                }
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
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(property.getPlural(wfmStrings.currentlyThereAreNoCashAdvances(), wfmStrings.cashAdvance()));
                message.setHref(hasPermissionToAdd() ? "cashAdvance|add/add" : "");
                message.setTextBeforeLink(property.getPlural(wfmStrings.youCanAddCashAdvance(), wfmStrings.cashAdvance()));
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private void addNewCashAdvance() {
        SinksContainerFactory.entryPoint.onHistoryChanged("cashAdvance|add/add");
    }

    private FacetContentConfigure getFacetContentConfigure() {
        FacetContentConfigure contentConfigure = new FacetContentConfigure(3, wfmStrings.filter());

        contentConfigure.addContentConfigure(FacetContentType.CashAdvanceFacetFilter.getContentCode()[0], wfmStrings.employee(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrCashAdvanceRepresenter.FIELD_EMPLOYEE_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrCashAdvanceRepresenter.FIELD_EMPLOYEE_ID_NAME;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.CashAdvanceFacetFilter.getContentCode()[1], wfmStrings.approver(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrCashAdvanceRepresenter.FIELD_APPROVER_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrCashAdvanceRepresenter.FIELD_APPROVER_ID_NAME;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.CashAdvanceFacetFilter.getContentCode()[2], wfmStrings.amount(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrCashAdvanceRepresenter.FIELD_TOTAL_AMOUNT;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrCashAdvanceRepresenter.FIELD_TOTAL_AMOUNT;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.CashAdvanceFacetFilter.getContentCode()[3], wfmStrings.status(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrCashAdvanceRepresenter.FIELD_STATUS_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrCashAdvanceRepresenter.FIELD_STATUS_ID_NAME;
            }
        });

        return contentConfigure;
    }


    private ListingRequestProvider<CashAdvanceItem> getListingRequestProvider() {
        return (filterParametrs, listingCallback) -> {
            if (employeeID != null) {
                filterParametrs.setEmployeeId(employeeID);
                filterParametrs.setHRMS(fromHrms);
                filterParametrs.setFromEmployeeProfile(fromEmployeeProfile);
            }
            CoreService.App.get().getCashAdvanceList(filterParametrs, new AsyncCallback<ListResult<CashAdvanceItem>>() {
                @Override
                public void onFailure(Throwable caught) {
                    listingCallback.onFailure(caught);
                }

                @Override
                public void onSuccess(ListResult<CashAdvanceItem> result) {
                    listingCallback.onSuccess(result);
                }
            });
        };
    }

    private ColumnDefinitionConfig[] getColumn() {
        int index = 0;
        ColumnDefinitionConfig[] columnConfig = new ColumnDefinitionConfig[9];
        columnConfig[index] = new ColumnDefinitionConfig<CashAdvanceItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final CashAdvanceItem item) {
                int actionItemCount = 0;
                boolean isBeforeLockDate = (Utils.isCashAdvancesLocked() && DateUtils.getTransactionLockDate().after(item.getDate().getNonConvertedDate()));
                MenuBar menuBar = new MenuBar(true);

                if (!DRAFT.equals(item.getStatus().getCode()) && Utils.hasPermission(PermissionConstants.PAYROLL_CASH_ADVANCE_VIEW)) {
                    MenuPopItem view = new MenuPopItem(wfmStrings.summaryView(), "icon-employee-edit-profile");
                    view.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("cashAdvance|summary/" + item.getObjectID() + "/" + item.getStatus().getCode(), item.getNumber(), item.getEmployeeName()));
                    actionItemCount++;
                    menuBar.addItem(view);
                }

                if (!isBeforeLockDate && !PAID.equals(item.getStatus().getCode()) && !POSTED.equals(item.getStatus().getCode()) && Utils.hasPermission(PermissionConstants.PAYROLL_CASH_ADVANCE_EDIT)) {
                    MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-employee-edit-profile");
                    edit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("cashAdvance|edit/" + item.getObjectID() + "/" + item.getStatus().getCode(), item.getNumber(), item.getEmployeeName()));
                    actionItemCount++;
                    menuBar.addItem(edit);
                }

                if (!isBeforeLockDate && Utils.hasPermission(PermissionConstants.PAYROLL_CASH_ADVANCE_DELETE)) {
                    MenuPopItem delete = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    delete.setCommand(() -> {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                CoreService.App.get().deleteCashAdvance(item.getObjectID(), new AbstractAsyncCallback<Boolean>() {
                                    @Override
                                    public void onFailure(Throwable caught) {
                                    }

                                    @Override
                                    public void onSuccess(Boolean result) {
                                        if (result) {
                                            Info.show(property.getSingular(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.cashAdvance()));
                                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CASH_SAVED, null, CashAdvanceListView.this);
                                        } else {
                                            Info.warn(property.getSingular(wfmStrings.cashAdvanceCannotDelete(), wfmStrings.cashAdvance()));
                                        }
                                    }
                                });
                            }
                        });
                        messageBox.open();

                    });
                    menuBar.addItem(delete);

                }
                if (Utils.hasPermission(PermissionConstants.PAYROLL_CASH_ADVANCE_UPDATES)) {
                    MenuPopItem updates = new MenuPopItem(wfmStrings.updates(), "payslip-16");
                    updates.setCommand(() -> CoreService.App.get().getCashAdvanceUpdates(item.getObjectID(), new AbstractAsyncCallback<ArrayList<MyUpdateItem>>() {
                        @Override
                        public void onFailure(Throwable caught) {
                        }

                        @Override
                        public void onSuccess(ArrayList<MyUpdateItem> result) {
                            UpdatesDialogBox updatesDialogBox = new UpdatesDialogBox(property.getSingular(wfmStrings.cashAdvanceUpdates(), wfmStrings.cashAdvance()), result);
                            updatesDialogBox.open();
                        }
                    }));
                    actionItemCount++;
                    menuBar.addItem(updates);
                }
                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfig[index++].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);

        columnConfig[index] = new ColumnDefinitionConfig<CashAdvanceItem, String>(wfmStrings.employeeCode(), CashAdvanceItem.EMPLOYEE_CODE, 100) {
            @Override
            public String getCellValue(CashAdvanceItem item) {
                return item.getEmployeeCode();
            }
        };
        columnConfig[index].setColumnSortable(false);
        columnConfig[index++].setMinimumColumnWidth(100);

        columnConfig[index] = new ColumnDefinitionConfig<CashAdvanceItem, SimpleLink>(wfmStrings.employee(), CashAdvanceItem.EMPLOYEE_NAME, 140) {
            @Override
            public SimpleLink getCellValue(CashAdvanceItem item) {
                if (Utils.hasPermission(PermissionConstants.PAYROLL_CASH_ADVANCE_VIEW)) {
                    String action = null;
                    if (DRAFT.equals(item.getStatus().getCode()) && Utils.hasPermission(PermissionConstants.PAYROLL_CASH_ADVANCE_EDIT)) {
                        action = "cashAdvance|edit/" + item.getObjectID() + "/" + item.getStatus().getCode();
                    } else {
                        action = "cashAdvance|summary/" + item.getObjectID() + "/" + item.getStatus().getCode();
                    }
                    return getLink(item.getEmployeeName(), action, item.getNumber(), item.getEmployeeName());
                } else {
                    return getLink(item.getEmployeeName(), null);
                }
            }
        };
        columnConfig[index++].setMinimumColumnWidth(100);

        columnConfig[index] = new ColumnDefinitionConfig<CashAdvanceItem, SimpleLink>(wfmStrings.number(), CashAdvanceItem.NUMBER, 140) {
            @Override
            public SimpleLink getCellValue(CashAdvanceItem item) {
                String number = item.getNumber() != null ? item.getNumber() : "";
                if (Utils.hasPermission(PermissionConstants.PAYROLL_CASH_ADVANCE_VIEW)) {
                    String action = null;
                    if (DRAFT.equals(item.getStatus().getCode()) && Utils.hasPermission(PermissionConstants.PAYROLL_CASH_ADVANCE_EDIT)) {
                        action = "cashAdvance|edit/" + item.getObjectID() + "/" + item.getStatus().getCode();
                    } else {
                        action = "cashAdvance|summary/" + item.getObjectID() + "/" + item.getStatus().getCode();
                    }
                    return getLink(number, action, number, item.getEmployeeName());
                } else {
                    return getLink(number, null);
                }
            }
        };
        columnConfig[index].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnConfig[index++].setMinimumColumnWidth(100);

        columnConfig[index] = new ColumnDefinitionConfig<CashAdvanceItem, String>(wfmStrings.date(), CashAdvanceItem.DATE, 140) {
            @Override
            public String getCellValue(CashAdvanceItem item) {
                return DateUtils.format(item.getDate());
            }
        };
        columnConfig[index].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnConfig[index++].setMinimumColumnWidth(100);

        columnConfig[index] = new ColumnDefinitionConfig<CashAdvanceItem, String>(wfmStrings.approver(), CashAdvanceItem.APPROVER, 140) {
            @Override
            public String getCellValue(CashAdvanceItem item) {
                return item.getApprover() != null ? item.getApprover().getName() : wfmStrings.notAvailable();
            }
        };
        columnConfig[index++].setMinimumColumnWidth(100);

        columnConfig[index] = new ColumnDefinitionConfig<CashAdvanceItem, String>(wfmStrings.amount(), CashAdvanceItem.AMOUNT, 140) {
            @Override
            public String getCellValue(CashAdvanceItem item) {
                return Utils.getCalculationNumberFormat().format(item.getTotalAmount());
            }
        };
        columnConfig[index].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        columnConfig[index++].setMinimumColumnWidth(100);

        columnConfig[index] = new ColumnDefinitionConfig<CashAdvanceItem, String>(wfmStrings.dueAmount(), CashAdvanceItem.REMAINING_AMOUNT, 140) {
            @Override
            public String getCellValue(CashAdvanceItem item) {
                return item.getRemainingAmount() != null ? Utils.getNumberFormat().format(item.getRemainingAmount()) : wfmStrings.notAvailable();
            }
        };
        columnConfig[index].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        columnConfig[index++].setMinimumColumnWidth(100);

        columnConfig[index] = new ColumnDefinitionConfig<CashAdvanceItem, String>(wfmStrings.status(), CashAdvanceItem.STATUS, 140) {
            @Override
            public String getCellValue(CashAdvanceItem item) {
                return item.getStatus().getName() != null ? item.getStatus().getName() : "";
            }
        };
        columnConfig[index].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnConfig[index++].setMinimumColumnWidth(100);

        return columnConfig;
    }


    @Override
    public String getIconStyle() {
        return "payroll payments-list";
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
        return CASH_ADVANCE_LIST;
    }


    @Override
    public void initStatistics(Integer parentId, Span container) {
        loadDependents(new ListingFilterParameter(), null, container);
    }

    private void loadDependents(ListingFilterParameter fp, ListingCallback callback, Span container) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        fp.setEmployeeId(employeeID);
        if (employeeID != null) {
            fp.setEmployeeId(employeeID);
            fp.setHRMS(fromHrms);
            fp.setFromEmployeeProfile(fromEmployeeProfile);
            fp.setLimit(20);
        }
        CoreService.App.get().getCashAdvanceList(fp, new AsyncCallback<ListResult<CashAdvanceItem>>() {
            @Override
            public void onFailure(Throwable throwable) {
                if (callback != null) {
                    callback.onFailure(throwable);
                }
            }

            @Override
            public void onSuccess(ListResult<CashAdvanceItem> cashAdvanceItemListResult) {
                if (callback != null) {
                    callback.onSuccess(cashAdvanceItemListResult);
                }
                statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                if (statisticShortcut != null) {
                    if (cashAdvanceItemListResult.getTotal() != null && cashAdvanceItemListResult.getTotal() > 0) {
                        statisticShortcut.setText(countFormat(cashAdvanceItemListResult.getTotal()));
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
