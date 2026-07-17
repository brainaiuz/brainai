package com.edatasite.workforce.gwt.invoice.client.ui.view.payment;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.TransactionPDFObject;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ChooseFilter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
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
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.filter.ListingChooseFilter;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.PDFTemplateSelector;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.BatchPaymentListItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Sherzod on 7/6/2015.
 */
public abstract class BatchPaymentsListView extends BaseListView implements PermissionConstants, Constants {

    protected static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    protected HashSet selectedItems = new HashSet();
    private final ActionButton delete = null;

    private ListingPanel<BatchPaymentListItem> listingPanel;

    public BatchPaymentsListView(String name) {
        super(name);
    }

    public BatchPaymentsListView(String name, String description) {
        super(name, description);
    }

    protected abstract String getBatchPaymentDataType();

    protected abstract ListPanelType getListPanelType();

    protected abstract String getPdfTemplateType();

    protected abstract ViewName getViewName();

    protected abstract void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable);

    protected abstract ActionButton initTopToolBarNew();

    protected abstract String getPdfUrl();

    protected abstract String getEditPermission();

    protected abstract String getDeletePermission();

    protected abstract String getVoidPermission();

    protected abstract String getPdfPermission();

    protected abstract String getSummaryPermission();

    public static ArrayList<Integer> getIDsOnly(HashSet<BatchPaymentListItem> selectedItems) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (BatchPaymentListItem item : selectedItems) {
            ids.add(item.getObjectID());
        }
        return ids;
    }

    private void saveBatchPaymentCellValue(BatchPaymentListItem rowValue, String columnCodeName) {
        boolean hasAccountingBeforeBlockDate = (Utils.isBankingLocked() && DateUtils.getTransactionLockDate().after(rowValue.getDate().getNonConvertedDate()));
        if (!hasAccountingBeforeBlockDate) {
            InvoiceService.App.get().saveBatchPaymentCellValue(rowValue, columnCodeName, new AbstractAsyncCallback<Boolean>() {
            });
        } else {
            Info.warn(wfmStrings.youDontHavePermission());
            listingPanel.reloadPage();
        }
    }

    private ListingRequestProvider<BatchPaymentListItem> getListingRequestProvider() {
        return (filterParametrs, callback) -> {
            filterParametrs.setDataType(getBatchPaymentDataType());
            setFilterValues(filterParametrs);
            InvoiceService.App.get().getBatchPayments(filterParametrs, new AsyncCallback<ListResult<BatchPaymentListItem>>() {

                @Override
                public void onFailure(Throwable caught) {
                    callback.onFailure(caught);
                }

                @Override
                public void onSuccess(ListResult<BatchPaymentListItem> list) {
                    callback.onSuccess(list);
                }
            });
        };
    }

    private CustomColumnDefinitionConfig[] getColumnConfigs() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[Utils.isEnablePaymentDepartment() ? 10 : 9];
        int i = 0;
        columns[i] = new ColumnDefinitionConfig<BatchPaymentListItem, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {

            @Override
            public Anchor getCellValue(final BatchPaymentListItem item) {
                MenuBar menuBar = new MenuBar(true);
                int actionItemCount = 0;
                boolean hasAccountingBeforeBlockDate = (Utils.isBankingLocked() && DateUtils.getTransactionLockDate().after(item.getDate().getNonConvertedDate()));
                if (Utils.hasPermission(getSummaryPermission())) {
                    MenuPopItem itemView = new MenuPopItem(wfmStrings.summaryView(), "icon-task-small");
                    itemView.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("receivepayment|summary/" + item.getObjectID() + "/" + getBatchPaymentDataType(), item.getNumber()));
                    actionItemCount++;
                    menuBar.addItem(itemView);
                }

                if (Utils.hasPermission(getEditPermission()) && !hasAccountingBeforeBlockDate) {
                    MenuPopItem itemEdit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                    itemEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("receivepayment|edit/" + item.getObjectID() + "/" + getBatchPaymentDataType(), item.getNumber()));
                    actionItemCount++;
                    menuBar.addItem(itemEdit);
                }

                if (!hasAccountingBeforeBlockDate && !item.isReversed() && Utils.hasPermission(getVoidPermission())) {
                    MenuPopItem itemVoid = new MenuPopItem(accountingStrings.voide(), "icon-remove-storefront");
                    itemVoid.setCommand(() -> {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.confirmation());
                        messageBox.setMessage(wfmStrings.doYoureallyWantTovoidThisPayment());
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                final KpiModal dialogBox = new KpiModal();
                                dialogBox.setCloseButton(true);
                                dialogBox.setWidth(400);
                                final DatePicker datePicker = new DatePicker(item.getDate().getNonConvertedDate());
                                dialogBox.setTitle(wfmStrings.selectVoidDate());
                                datePicker.setWidth("180px");
                                datePicker.getElement().getStyle().setMargin(10, Style.Unit.PX);
                                dialogBox.add(datePicker);
                                final WfmButton2 voidButton = new WfmButton2(accountingStrings.voide(), WfmButton2.BTN_PRIMARY);
                                dialogBox.addButton(voidButton);
                                voidButton.addClickHandler(clickEvent -> {
                                    if (AccountingUtils.validateVoidDate(datePicker.getDate(), item.getDate().getNonConvertedDate())) {
                                        voidButton.setEnabled(false);
                                        InvoiceService.App.get().voidBatchPayment(item.getObjectID(), new DateNonConvertable(datePicker.getDate()), new AsyncCallback<Integer>() {
                                            @Override
                                            public void onFailure(Throwable throwable) {
                                                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                                dialogBox.close();
                                            }

                                            @Override
                                            public void onSuccess(Integer result) {
                                                dialogBox.close();
                                                Info.show(wfmStrings.paymentVoidSuccessfully(), Info.Type.INFO);
                                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_INVOICEPAYMENT_CHANGE, null, BatchPaymentsListView.this);
                                            }
                                        });
                                    }
                                });
                                dialogBox.open();
                            }
                        });
                        messageBox.open();
                    });
                    actionItemCount++;
                    menuBar.addItem(itemVoid);
                }

                if (Utils.hasPermission(getDeletePermission()) && !hasAccountingBeforeBlockDate ) {
                    MenuPopItem itemDelete = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    itemDelete.setCommand(() -> {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                InvoiceService.App.get().deleteBatchPayment(item.getObjectID(), new AbstractAsyncCallback<TestRPC>() {
                                    @Override
                                    public void failure(Throwable throwable) {
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    @Override
                                    public void success(TestRPC result) {
                                        if (result != null && result.isError()) {
                                            Info.show(result.getMessage(), Info.Type.WARNING);

                                        } else {
                                            Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.payment()), Info.Type.INFO);
                                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_INVOICEPAYMENT_CHANGE, null, BatchPaymentsListView.this);
                                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_BANK_ACCOUNT_TRANSACTION_DELETED, null, BatchPaymentsListView.this);
                                        }
                                    }
                                });
                            }
                        });
                        messageBox.open();
                    });
                    actionItemCount++;
                    menuBar.addItem(itemDelete);
                }
                if (Utils.hasPermission(getPdfPermission())) {
                    MenuPopItem generatePdf = new MenuPopItem(wfmStrings.pdf(), "icon-pdf");
                    final HTMLPanel htmlPanel = new HTMLPanel("");
                    generatePdf.setCommand(() -> new PDFTemplateSelector(getPdfTemplateType(), new ExtendedCommand() {
                        @Override
                        public void execute(Integer id) {
                            generatePDF(htmlPanel, id, item.getObjectID());
                        }
                    }));
                    add(htmlPanel);
                    actionItemCount++;
                    menuBar.addItem(generatePdf);
                }

                final ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();

            }
        };
        columns[i].setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns[i].setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns[i].setColumnSortable(false);

        columns[++i] = new ColumnDefinitionConfig<BatchPaymentListItem, SimpleLink>(wfmStrings.number(), BatchPaymentListItem.NUMBER, 80) {

            @Override
            public SimpleLink getCellValue(BatchPaymentListItem item) {
                return getLinkWithTabTitle(item.getNumber(), "receivepayment|summary/" + item.getObjectID() + "/" + getBatchPaymentDataType(), item.getNumber());
            }
        };
        columns[i].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columns[++i] = new ColumnDefinitionConfig<BatchPaymentListItem, String>((Constants.RECEIVABLE.equals(getBatchPaymentDataType()) ? Property.get(Constants.CLIENT_LIST, wfmStrings.customer()) : Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier())), BatchPaymentListItem.CRM_ACCOUNT, 150) {

            @Override
            public String getCellValue(BatchPaymentListItem item) {
                return item.getCrmAccount() != null ? item.getCrmAccount().getName() : "";
            }
        };
        columns[i].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columns[++i] = new ColumnDefinitionConfig<BatchPaymentListItem, String>(wfmStrings.date(), BatchPaymentListItem.DATE, 100) {

            @Override
            public String getCellValue(BatchPaymentListItem item) {
                return item.getDate() != null ? DateUtils.format(item.getDate()) : "";
            }
        };
        columns[i].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columns[++i] = new ColumnDefinitionConfig<BatchPaymentListItem, String>(wfmStrings.account(), BatchPaymentListItem.ACCOUNT, 120) {

            @Override
            public String getCellValue(BatchPaymentListItem item) {
                return item.getAccount() != null ? item.getAccount().getName() : "";
            }
        };
        columns[i].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columns[++i] = new ColumnDefinitionConfig<BatchPaymentListItem, String>(wfmStrings.amount(), BatchPaymentListItem.AMOUNT, 100) {

            @Override
            public String getCellValue(BatchPaymentListItem item) {
                return item.getTotalAmount() != null ? AccountingUtils.get().format(item.getTotalAmount()) : "";
            }
        };
        columns[i].setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);

        columns[++i] = new ColumnDefinitionConfig<BatchPaymentListItem, String>(wfmStrings.currency(), BatchPaymentListItem.CURRENCY, 100) {

            @Override
            public String getCellValue(BatchPaymentListItem item) {
                return item.getCurrency() != null ? item.getCurrency().getName() : "";
            }
        };
        columns[i].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columns[++i] = new ColumnDefinitionConfig<BatchPaymentListItem, String>(Property.get(Constants.PROJECT, wfmStrings.project()), BatchPaymentListItem.PROJECT, 100) {

            @Override
            public String getCellValue(BatchPaymentListItem item) {
                return item.getProject() != null ? item.getProject() : "";
            }
        };
        columns[i].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columns[i].setShow(false);

        columns[++i] = new ColumnDefinitionConfig<BatchPaymentListItem, String>(wfmStrings.createdBy(), BatchPaymentListItem.CREATOR, 100) {

            @Override
            public String getCellValue(BatchPaymentListItem item) {
                return item.getCreator() != null ? item.getCreator() : "";
            }
        };
        columns[i].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columns[i].setShow(false);

        if (Utils.isEnablePaymentDepartment()) {
            columns[++i] = new ColumnDefinitionConfig<BatchPaymentListItem, String>(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), BatchPaymentListItem.DEPARTMENT, 100) {

                @Override
                public String getCellValue(BatchPaymentListItem item) {
                    return item.getDepartment() != null ? item.getDepartment() : "";
                }
            };
            columns[i].setShow(false);
        }

        columns[++i] = new ColumnDefinitionConfig<BatchPaymentListItem, String>(wfmStrings.reference(), BatchPaymentListItem.REFERENCE, 100) {

            @Override
            public String getCellValue(BatchPaymentListItem item) {
                return item.getReference() != null ? item.getReference() : "";
            }
        };
        columns[i].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columns[i].setShow(false);

        return columns;
    }

    private void setFilterValues(ListingFilterParameter filterParametrs) {
        if (filterParametrs.getStartDate() != null && filterParametrs.getEndDate() != null) {
            DateNonConvertable fromDate = new DateNonConvertable(DateUtil.resetTime(filterParametrs.getStartDate()));
            DateNonConvertable toDate = new DateNonConvertable(DateUtil.getDayLastTime(filterParametrs.getEndDate()));
            filterParametrs.setStartDateWithoutOffset(fromDate.getNonConvertedDate());
            filterParametrs.setEndDateWithoutOffset(toDate.getNonConvertedDate());
        }
    }

    protected abstract Command getNewItemAddCommand();

    public static String getIdsOnly(Set<BatchPaymentListItem> selectedItems) {
        StringBuilder ids = null;
        for (BatchPaymentListItem item : selectedItems) {
            if (ids == null) {
                ids = new StringBuilder();
                ids.append(item.getObjectID());
            } else {
                ids.append("," + item.getObjectID());
            }
        }
        return ids.toString();
    }

    @Override
    protected Widget onInitialize() {
        listingPanel = new GuideListingPanel(getListPanelType(), getColumnConfigs(), getListingRequestProvider(), getListingPanelDesign(), SelectionGrid.SelectionPolicy.CHECKBOX);

        listingPanel.setCustomFieldsEditCellSaveChanges((rowValue, columnCodeName) -> saveBatchPaymentCellValue((BatchPaymentListItem) rowValue, columnCodeName));

        listingPanel.setPDFListener(clickEvent -> {
            String pdfURL = CommandConstants.PDF_URL + "/batchPaymentListPDFHandler";
            ListingFilterParameter fp = listingPanel.getFilterParametrs();
            fp.setPropertyCode(getPropertyCode());
            setFilterValues(fp);
            listingPanel.callListPDF(pdfURL, fp);
        });

        listingPanel.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/batchPaymentListExcelHandler";
            ListingFilterParameter fp = listingPanel.getFilterParametrs();
            fp.setPropertyCode(getPropertyCode());
            setFilterValues(fp);
            listingPanel.callListExcel(excelURL, fp);
        });

        listingPanel.addSelectionRowHandler(selectedRows -> {
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

        add(listingPanel);

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_INVOICEPAYMENT_CHANGE, BatchPaymentsListView.this, (sender, args) -> listingPanel.reloadPage());

        return null;
    }

    private GuideListingPanelDesign getListingPanelDesign() {
        return new GuideListingPanelDesign() {

            @Override
            public Command getAddNewItemCommand() {
                return getNewItemAddCommand();
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
                        return null;
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return null;
                    }

                    @Override
                    public long initSimpleFilterType() {
                        return ChooseFilter.INVOICE_FILTER;
                    }

                    @Override
                    public ArrayList<String> getCustomFacetFilterFields() {
                        ArrayList<String> fields = new ArrayList<>();
                        fields.add(ListingChooseFilter.TYPE);
                        fields.add(Constants.RECEIVABLE.equals(getBatchPaymentDataType()) ? ListingChooseFilter.CUSTOMER : ListingChooseFilter.SUPPLIER);
                        fields.add(ListingChooseFilter.FROM_AMOUNT);
                        fields.add(ListingChooseFilter.TO_AMOUNT);
                        fields.add(ListingChooseFilter.FROM_DATE);
                        fields.add(ListingChooseFilter.TO_DATE);
                        fields.add(ListingChooseFilter.CREATOR);
                        return fields;
                    }

                    @Override
                    public ViewName getView() {
                        return getViewName();
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                return BatchPaymentsListView.this.initTopToolBarNew();
            }

            @Override
            public ActionButton initTopToolBarPrint() {
                return getPrintButton(clickEvent -> printSelection());
            }

            @Override
            public ActionButton initTopToolBarMore() {
                return getRemoveMoreButton(clickEvent -> deleteSelection());
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
                BatchPaymentsListView.this.initDataEmptyTable(emptyDataTable);
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return Utils.adminOrDirector() || Utils.hasRole(Constants.ACCOUNTANT);
            }
        };
    }

    private void printSelection() {
        if (selectedItems.size() == 0) {
            Info.show(accountingMessages.pleaseSelectOneRow(wfmStrings.salesInvoice()), Info.Type.WARNING);
        } else {
            new PDFTemplateSelector(getPdfTemplateType(), new ExtendedCommand() {
                @Override
                public void execute(Integer id) {
                    if (selectedItems.isEmpty()) {
                        Info.show(accountingStrings.youShouldSelectOnlySalesInvoice(), Info.Type.WARNING);
                    } else {
                        final HTMLPanel htmlPanel = new HTMLPanel("");
                        add(htmlPanel);
                        TransactionPDFObject requestObject = new TransactionPDFObject(getIdsOnly(selectedItems), id, getBatchPaymentDataType(), null);
                        String pdfURL = CommandConstants.PDF_URL + getPdfUrl();
                        HashMap<String, String> parametrs = requestObject.getRequestParams();
                        Utils.sendPDFOrExcelRequest(htmlPanel, pdfURL, parametrs, "_blank");
                    }
                }
            });
        }
    }

    private void deleteSelection() {
        if (selectedItems.size() == 0) {
            Info.show(accountingMessages.pleaseSelectOneRow(accountingStrings.invoice()), Info.Type.WARNING);
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
                    InvoiceService.App.get().deleteBatchPayment(ids, new AsyncCallback<Void>() {

                        @Override
                        public void onFailure(Throwable throwable) {
                        }

                        @Override
                        public void onSuccess(Void aVoid) {
                            Info.show(accountingStrings.paymentDeletedSuccessfully(), Info.Type.INFO);
                            for (Integer id : ids) {
                                removeDeletedTab(SALE_INVOICE + id);
                            }
                        }
                    });
                }
            }
        });
        messageBox.open();
    }

    private void generatePDF(HTMLPanel hp, Integer pdfTemplateID, Integer objectID) {
        TransactionPDFObject requestObject = new TransactionPDFObject(objectID, pdfTemplateID, getBatchPaymentDataType(), null);
        String pdfURL = CommandConstants.PDF_URL + (getPdfUrl());
        HashMap<String, String> parametrs = requestObject.getRequestParams();
        Utils.sendPDFOrExcelRequest(hp, pdfURL, parametrs, "_blank");
    }

    @Override
    public String getIconStyle() {
        return "accountMark manual-journals";
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
