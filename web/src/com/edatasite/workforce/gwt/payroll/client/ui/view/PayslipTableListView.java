package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.MyUpdateItem;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrGroupPayrunRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.ImportFileActionLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listTable.ImportFilePopUp;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.UpdatesDialogBox;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.GroupPayrunData;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 06.03.14
 * Time: 10:32
 * To change this template use File | Settings | File Templates.
 */
public class PayslipTableListView extends BaseListView implements Constants {
    
    private static final PayrollStrings payrollStrings = GWT.create(PayrollStrings.class);
    private ListingPanel<GroupPayrunData> list;
    private final HashMap<Integer, String> monthItems = new HashMap<>();


    public PayslipTableListView() {
        super(PAYSLIP_TABLE_LIST);
        setDescription(property.getPlural(payrollStrings.groupPayruns()));
        if (hasPermissionToAdd()) {
            setAddNew("payslipTable|add/add");
        }
    }

    private boolean hasPermissionToAdd() {
        return Utils.hasPermission(PermissionConstants.PAYROLL_GROUP_PAYRUN_ADD);
    }

    protected Widget onInitialize() {
        Date date = DateUtil.getYearFirstDay(new Date());
        for (int i = 0; i < 12; i++) {
            monthItems.put(i, DateTimeFormat.getFormat("MMMM").format(date));
            date = DateUtil.addMonths(date, 1);
        }

        list = new GuideListingPanel(ListPanelType.PayslipTableListPanel, getColumn(), getListProvider(), getListDesign());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PAYSLIP_SAVED, PayslipTableListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PAYRUN_PAYMENT_ADD, PayslipTableListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PAYRUN_PAYMENT_DELETE, PayslipTableListView.this, (sender, args) -> list.reloadPage());

        if (Utils.hasPermission(PermissionConstants.PAYROLL_GROUP_PAYRUN_LIST_EXCEL)) {
            list.setExcelListener(event -> {
                String excelURL = CommandConstants.COMMON_URL + "/downloadGroupPayrunListExcel";
                ListingFilterParameter filterParametrs = list.getFilterParametrs();
                filterParametrs.setPropertyCode(getPropertyCode());
                list.callListExcel(excelURL, filterParametrs);
            });

            list.setPDFListener(event -> {
                String pdfURL = CommandConstants.PDF_URL + "/groupPayrunListPDFHandler";
                ListingFilterParameter filterParametrs = list.getFilterParametrs();
                filterParametrs.setPropertyCode(getPropertyCode());
                list.callListPDF(pdfURL, filterParametrs);
            });
        }
        add(list);
        return null;
    }

    private ColumnDefinitionConfig[] getColumn() {
        ArrayList<ColumnDefinitionConfig> columnConfigs = new ArrayList<>();

        ColumnDefinitionConfig columnConfig = new ColumnDefinitionConfig<GroupPayrunData, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {

            @Override
            public Anchor getCellValue(final GroupPayrunData item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                boolean isProcessing = PAYRUN_STATUS_PROCESSING.equals(item.getStatusCode());
                if ((PAYRUN_STATUS_APPROVED.equals(item.getStatusCode())
                        || PAYRUN_STATUS_SUBMITTED.equals(item.getStatusCode())
                        || PAYRUN_STATUS_PARTIAL_PAID.equals(item.getStatusCode())
                        || PAYRUN_STATUS_PAID.equals(item.getStatusCode()))
                        && Utils.hasPermission(PermissionConstants.PAYROLL_GROUP_PAYRUN_VIEW) && !isProcessing) {
                    MenuPopItem view = new MenuPopItem(wfmStrings.summaryView(), "icon-employee-edit-profile");
                    view.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("payslipTable|summary/" + item.getObjectID(), item.getMonth(), item.getApprover().getName()));
                    actionItemCount++;
                    menuBar.addItem(view);
                } else if (Utils.hasPermission(PermissionConstants.PAYROLL_GROUP_PAYRUN_EDIT) && !isProcessing) {
                    MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-employee-edit-profile");
                    edit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("payslipTable|edit/" + item.getObjectID(), item.getMonth(), item.getApprover().getName()));
                    actionItemCount++;
                    menuBar.addItem(edit);
                }
                if (Utils.hasPermission(PermissionConstants.PAYROLL_CAN_APPROVE_PAYSLIP)) {
                    MenuPopItem delete = new MenuPopItem(wfmStrings.delete(), "icon-remove");
                    delete.setCommand(() -> {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                PayrollService.App.get().deletePayslipTable(item.getObjectID(), new AbstractAsyncCallback<Void>() {
                                    @Override
                                    public void onFailure(Throwable caught) {
                                    }

                                    @Override
                                    public void onSuccess(Void result) {
                                        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.payslip()), Info.Type.INFO);
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PAYSLIP_SAVED, null, PayslipTableListView.this);
                                    }
                                });
                            }
                        });
                        messageBox.open();

                    });
                    menuBar.addItem(delete);
                }
                //todo hide for now (payment list is not ready yet)
                /*if ((PAYRUN_STATUS_PARTIAL_PAID.equals(item.getStatusCode()) || PAYRUN_STATUS_PAID.equals(item.getStatusCode())) &&
                        Utils.hasPermission(PermissionConstants.PAYROLL_GROUP_PAYRUN_VIEW)) {
                    MenuPopItem view = new MenuPopItem(payrollStrings.payments(), "");
                    view.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("payrunPaymentList|view/" + item.getObjectID()));
                    actionItemCount++;
                    menuBar.addItem(view);
                }*/
                if (Utils.hasPermission(PermissionConstants.PAYROLL_GROUP_PAYRUN_UPDATES)) {
                    final MenuPopItem updates = new MenuPopItem(wfmStrings.updates(), "payslip-16");
                    updates.setCommand(() -> PayrollService.App.get().getGroupPayrunUpdates(item.getObjectID(), new AbstractAsyncCallback<ArrayList<MyUpdateItem>>() {
                        @Override
                        public void onFailure(Throwable caught) {
                        }

                        @Override
                        public void onSuccess(ArrayList<MyUpdateItem> result) {
                            final UpdatesDialogBox updatesDialogBox = new UpdatesDialogBox(property.getSingular(payrollStrings.groupPayrunUpdates(), wfmStrings.groupPayrun()), result);
                            updatesDialogBox.open();
                        }
                    }));
                    menuBar.addItem(updates);
                }
                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columnConfig.setColumnSortable(false);
        columnConfig.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfig.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<GroupPayrunData, SimpleLink>(wfmStrings.period(), GroupPayrunData.PERIOD, 140) {
            @Override
            public SimpleLink getCellValue(GroupPayrunData item) {
                String month = item.getMonthID() != null && monthItems.get(item.getMonthID()) != null ? monthItems.get(item.getMonthID()) : item.getMonth();
                if ((PAYRUN_STATUS_APPROVED.equals(item.getStatusCode())
                        || PAYRUN_STATUS_SUBMITTED.equals(item.getStatusCode())
                        || PAYRUN_STATUS_PARTIAL_PAID.equals(item.getStatusCode())
                        || PAYRUN_STATUS_PAID.equals(item.getStatusCode()))
                        && Utils.hasPermission(PermissionConstants.PAYROLL_GROUP_PAYRUN_VIEW) && !PAYRUN_STATUS_PROCESSING.equals(item.getStatusCode())) {
                    return month != null
                            ? getLink(month, "payslipTable|summary/" + item.getObjectID(), month, item.getApprover().getName())
                            : getLink("N/A", null);
                } else if (Utils.hasPermission(PermissionConstants.PAYROLL_GROUP_PAYRUN_EDIT) && !PAYRUN_STATUS_PROCESSING.equals(item.getStatusCode())) {
                    return month != null
                            ? getLink(month, "payslipTable|edit/" + item.getObjectID(), month, item.getApprover().getName())
                            : getLink("N/A", null);
                } else {
                    return month != null ? getLink(month, null, month, item.getApprover().getName()) : getLink("N/A", null);
                }
            }
        };
        columnConfig.setMinimumColumnWidth(100);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<GroupPayrunData, String>(wfmStrings.approver(), GroupPayrunData.APPROVER, 140) {
            @Override
            public String getCellValue(GroupPayrunData item) {
                return item.getApprover() != null ? item.getApprover().getName() : wfmStrings.notAvailable();
            }
        };
        columnConfig.setMinimumColumnWidth(100);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<GroupPayrunData, String>(wfmStrings.createdBy(), GroupPayrunData.PREPARER, 140) {
            @Override
            public String getCellValue(GroupPayrunData item) {
                return item.getCreator() != null ? item.getCreator().getName() : wfmStrings.notAvailable();
            }
        };
        columnConfig.setColumnSortable(false);
        columnConfig.setMinimumColumnWidth(100);
        columnConfigs.add(columnConfig);


        columnConfig = new ColumnDefinitionConfig<GroupPayrunData, String>(wfmStrings.status(), GroupPayrunData.STATUS, 140) {
            @Override
            public String getCellValue(GroupPayrunData item) {
                return item.getStatus() != null ? item.getStatus() : wfmStrings.notAvailable();
            }
        };
        columnConfig.setMinimumColumnWidth(100);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<GroupPayrunData, String>(payrollStrings.payrollGroup(), GroupPayrunData.BATCH, 140) {
            @Override
            public String getCellValue(GroupPayrunData item) {
                return item.getPayrollBatchItem() != null && item.getPayrollBatchItem().getName() != null ? item.getPayrollBatchItem().getName() :
                        (item.getProjectItem() != null && item.getProjectItem().getName() != null ? item.getProjectItem().getName() : wfmStrings.allEmployees());
            }
        };
        columnConfig.setColumnSortable(false);
        columnConfig.setMinimumColumnWidth(100);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<GroupPayrunData, String>(wfmStrings.total(), GroupPayrunData.TOTAL_AMOUNT, 140) {
            @Override
            public String getCellValue(GroupPayrunData item) {
                return item.getTotalAmount() != null ? AccountingUtils.get().formatPrice(item.getTotalAmount()) : "0.00";
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        columnConfig.setMinimumColumnWidth(100);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<GroupPayrunData, String>(payrollStrings.totalInBase(), GroupPayrunData.TOTAL_IN_BASE, 140) {
            @Override
            public String getCellValue(GroupPayrunData item) {
                return item.getTotalInBase() != null ? AccountingUtils.get().formatPrice(item.getTotalInBase()) : "0.00";
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        columnConfig.setMinimumColumnWidth(100);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<GroupPayrunData, String>(wfmStrings.currency(), GroupPayrunData.CURRENCY_NAME, 140) {
            @Override
            public String getCellValue(GroupPayrunData item) {
                return item.getCurrencyName() != null ? item.getCurrencyName() : wfmStrings.notAvailable();
            }
        };
        columnConfig.setMinimumColumnWidth(100);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<GroupPayrunData, String>(wfmStrings.paymentMethod(), GroupPayrunData.PAYMENT_METHOD, 140) {
            @Override
            public String getCellValue(GroupPayrunData item) {
                return item.getPayMethodName() != null ? item.getPayMethodName() : wfmStrings.notAvailable();
            }
        };
        columnConfig.setMinimumColumnWidth(100);
        columnConfig.setShow(false);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<GroupPayrunData, String>(wfmStrings.processDate(), GroupPayrunData.PROCESS_DATE, 140) {
            @Override
            public String getCellValue(GroupPayrunData item) {
                return item.getProcessDate() != null ? DateUtils.format(item.getProcessDate()) : wfmStrings.notAvailable();
            }
        };
        columnConfig.setMinimumColumnWidth(100);
        columnConfig.setShow(false);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<GroupPayrunData, String>(wfmStrings.basicSalary(), GroupPayrunData.BASIC_SALARY, 140) {
            @Override
            public String getCellValue(GroupPayrunData item) {
                return item.getBasicSalary() != null ? AccountingUtils.get().formatPrice(item.getBasicSalary()) : "0.00";
            }
        };
        columnConfig.setMinimumColumnWidth(100);
        columnConfig.setShow(false);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<GroupPayrunData, String>(wfmStrings.allowance(), GroupPayrunData.ALLOWANCE, 140) {
            @Override
            public String getCellValue(GroupPayrunData item) {
                return item.getAllowance() != null ? AccountingUtils.get().formatPrice(item.getAllowance()) : "0.00";
            }
        };
        columnConfig.setMinimumColumnWidth(100);
        columnConfig.setShow(false);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<GroupPayrunData, String>(payrollStrings.pension(), GroupPayrunData.PENSION, 140) {
            @Override
            public String getCellValue(GroupPayrunData item) {
                return item.getPension() != null ? AccountingUtils.get().formatPrice(item.getPension()) : "0.00";
            }
        };
        columnConfig.setMinimumColumnWidth(100);
        columnConfig.setShow(false);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<GroupPayrunData, String>(wfmStrings.deduction(), GroupPayrunData.DEDUCTION, 140) {
            @Override
            public String getCellValue(GroupPayrunData item) {
                return item.getDeduction() != null ? AccountingUtils.get().formatPrice(item.getDeduction()) : "0.00";
            }
        };
        columnConfig.setMinimumColumnWidth(100);
        columnConfig.setShow(false);
        columnConfigs.add(columnConfig);

        if (Utils.isEnableAccountingModule()) {
            columnConfig = new ColumnDefinitionConfig<GroupPayrunData, String>(wfmStrings.expense(), GroupPayrunData.EXPENSE, 140) {
                @Override
                public String getCellValue(GroupPayrunData item) {
                    return item.getExpense() != null ? AccountingUtils.get().formatPrice(item.getExpense()) : "0.00";
                }
            };
            columnConfig.setMinimumColumnWidth(100);
            columnConfig.setShow(false);
            columnConfigs.add(columnConfig);
        }
        return columnConfigs.toArray(new ColumnDefinitionConfig[columnConfigs.size()]);
    }

    private GuideListingPanelDesign getListDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return hasPermissionToAdd() ? PayslipTableListView.this::addNewGroupPayrun : null;
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
                            RbacService.App.get().getGroupPayrunFacetFilterData(data, new AbstractAsyncCallback<FacetFilterRpc>() {
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
                };  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (hasPermissionToAdd()) {
                    ActionButton addNew = getAddNewButton();
                    addNew.addClickHandler(clickEvent -> {
                        addNewGroupPayrun();
                    });
                    return addNew;
                } else {
                    return null;
                }
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                if (Utils.hasPermission(PermissionConstants.PAYROLL_GROUP_PAYRUN_IMPORT)) {
                    final ImportFilePopUp imp = new ImportFilePopUp(ImportTypeEnum.GROUP_PAYRUN, null);
                    imp.setSubmitCompleted(() -> {
                        if (imp.getObjectId() != null) {
                            goTo("importgrouppayrun|add/add/" + imp.getObjectId());
                        }
                    });

                    ImportFileActionLink link = new ImportFileActionLink();
                    link.addClickHandler(ch -> imp.open());
                    menuContainer.add(link);
                    exportOption.initExport(null);
                } else {
                    exportOption.initExport(null, Utils.hasPermission(PermissionConstants.PAYROLL_GROUP_PAYRUN_LIST_EXCEL));
                }
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(property.getPlural(payrollStrings.currentlyThereAreNoGroupPayruns(), payrollStrings.groupPayruns()));
                if (hasPermissionToAdd()) {
                    message.setHref("payslipTable|add/add");
                    message.setTextBeforeLink(property.getPlural(payrollStrings.messAddingGroupPayrunTablesClicking(), payrollStrings.groupPayruns()));
                }
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private void addNewGroupPayrun() {
        SinksContainerFactory.entryPoint.onHistoryChanged("payslipTable|add/add");
    }

    private FacetContentConfigure getFacetContentConfigure() {
        FacetContentConfigure contentConfigure = new FacetContentConfigure(3, wfmStrings.filter());

        contentConfigure.addContentConfigure(FacetContentType.GroupPayrunFacetFilter.getContentCode()[0], wfmStrings.month(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrGroupPayrunRepresenter.FIELD_MONTH_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrGroupPayrunRepresenter.FIELD_MONTH_ID_NAME;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.GroupPayrunFacetFilter.getContentCode()[1], wfmStrings.year(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrGroupPayrunRepresenter.FIELD_YEAR;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrGroupPayrunRepresenter.FIELD_YEAR;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.GroupPayrunFacetFilter.getContentCode()[2], wfmStrings.approver(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrGroupPayrunRepresenter.FIELD_APPROVER_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrGroupPayrunRepresenter.FIELD_APPROVER_ID_NAME;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.GroupPayrunFacetFilter.getContentCode()[3], wfmStrings.status(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrGroupPayrunRepresenter.FIELD_STATUS_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrGroupPayrunRepresenter.FIELD_STATUS_ID_NAME;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.GroupPayrunFacetFilter.getContentCode()[4], wfmStrings.createdBy(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrGroupPayrunRepresenter.FIELD_PREPARER_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrGroupPayrunRepresenter.FIELD_PREPARER_NAME;
            }
        });

        contentConfigure.setDatePeriodPanelEnabled(false);
        return contentConfigure;
    }

    private ListingRequestProvider<GroupPayrunData> getListProvider() {
        return (filterParametrs, callback) -> {
            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }
            PayrollService.App.get().getPayslipTableList(filterParametrs, new AbstractAsyncCallback<ListResult<GroupPayrunData>>() {
                @Override
                public void onFailure(Throwable caught) {
                    callback.onFailure(caught);
                }

                @Override
                public void onSuccess(ListResult<GroupPayrunData> result) {
                    callback.onSuccess(result);
                }
            });
        };
    }


    @Override
    public String getIconStyle() {
        return "payroll payslip-list";
    }

    @Override
    public String getPropertyCode() {
        return PAYSLIP_TABLE_LIST;
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
}
