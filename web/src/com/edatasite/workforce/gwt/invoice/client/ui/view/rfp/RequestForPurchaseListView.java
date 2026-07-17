package com.edatasite.workforce.gwt.invoice.client.ui.view.rfp;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.*;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.*;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.PDFTemplateSelector;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceQuoteRequestObject;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFPData;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFQData;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 4/6/13
 * Time: 12:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class RequestForPurchaseListView extends BaseListView implements Constants, PermissionConstants {
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final QuoteServiceAsync quoteService = QuoteService.App.get();
    private ListingPanel listPanel;
    private ActionButton convertButton = null;
    private Integer projectId;

    private HashSet selectedItems = new HashSet();

    public RequestForPurchaseListView() {
        super(REQUEST_FOR_PURCHASE);
        setDescription(property.getPlural(wfmStrings.requestForPurchase()));
        if (hasPermissionToCreateRFP()) {
            setAddNew("requestforpurchase|add/add");
        }
    }

    public RequestForPurchaseListView(Integer projectId) {
        super(REQUEST_FOR_PURCHASE);
        this.projectId = projectId;
        setDescription(property.getPlural(wfmStrings.requestForPurchase()));
        if (hasPermissionToCreateRFP()) {
            setAddNew("requestforpurchase|add/add");
        }
    }

    public static String getIdsOnly(Set<RFQData> selectedItems) {
        StringBuilder ids = null;
        for (RFQData item : selectedItems) {
            if (ids == null) {
                ids = new StringBuilder();
                ids.append(item.getObjectID());
            } else {
                ids.append("," + item.getObjectID());
            }
        }
        return ids.toString();
    }

    private CustomColumnDefinitionConfig[] getColumnConfig() {
        ArrayList<ColumnDefinitionConfig> columns = new ArrayList<>();
        ColumnDefinitionConfig config = new ColumnDefinitionConfig<RFPData, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final RFPData item) {
                String statusCode = item.getStatus();
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                MenuPopItem summary = new MenuPopItem(wfmStrings.summaryView(), "icon-task-small");
                summary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("requestforpurchase|summary/" + item.getObjectID(), item.getNumberData().getNumberString()));
                actionItemCount++;
                if (!DRAFT.equals(statusCode)) {
                    menuBar.addItem(summary);
                }

//                if (item.isCurrentApprover() && !APPROVE.equals(statusCode) && !REJECT.equals(statusCode)) {
//                    MenuPopItem approve = new MenuPopItem(accountingStrings.approve(), "icon-approve");
//                    approve.setCommand(() -> quoteService.changeRFPstatus(item.getObjectID(), APPROVE, null, new AsyncCallback<Void>() {
//                        @Override
//                        public void onFailure(Throwable caught) {
//                        }
//
//                        @Override
//                        public void onSuccess(Void result) {
//                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_REQUEST_FOR_PURCHASE_ADD_EDIT, result, RequestForPurchaseListView.this);
//                            Info.show(property.getSingular(accountingStrings.approvedSuccessfullyRFP(), accountingStrings.requestForPurchase()), Info.Type.INFO);
//                        }
//                    }));
//                    actionItemCount++;
//                    menuBar.addItem(approve);
//
//                    if (!DRAFT.equals(statusCode) && !APPROVE.equals(statusCode) && !REJECT.equals(statusCode) && item.isCurrentApprover()) {
//                        MenuPopItem reject = new MenuPopItem(accountingStrings.reject(), "icon-decline");
//                        reject.setCommand(() -> showRejectionDialogBox(item.getObjectID()));
//                        actionItemCount++;
//                        menuBar.addItem(reject);
//                    }
//                }

                MenuPopItem edit1 = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                edit1.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("requestforpurchase|edit/" + item.getObjectID(), item.getNumberData().getNumberString(), item.getNumberData().getNumberString()));
                if (item.isEmployee() && (DRAFT.equals(statusCode) || REJECT.equals(statusCode) || APPROVE.equals(statusCode))
                        || item.isCurrentApprover() && !(REJECT.equals(statusCode) || APPROVE.equals(statusCode) || DRAFT.equals(statusCode))
                        || Utils.getUserID().equals(item.getCreator().getId()) && DRAFT.equals(statusCode) || REJECT.equals(statusCode) || APPROVE.equals(statusCode) || SUBMITTED_TO_MANAGER.equals(statusCode)) {
                    actionItemCount++;
                    menuBar.addItem(edit1);
                }

                MenuPopItem copyToRFP = new MenuPopItem(wfmStrings.copy(), "icon-edit");
                copyToRFP.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("requestforpurchase|add/add/" + item.getObjectID() + "/copy"));
                actionItemCount++;
                menuBar.addItem(copyToRFP);
                if (Utils.hasPermission(ACCOUNTING_REQUEST_FOR_PURCHASE_DELETE) && (!SUBMITTED_TO_MANAGER.equals(statusCode) || Utils.hasRole(ADMIN))) {
                    MenuPopItem delete = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    delete.setCommand(() -> {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                quoteService.deleteRFP(item.getObjectID(), new AsyncCallback<Void>() {
                                    @Override
                                    public void onFailure(Throwable throwable) {
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Info.show(property.getSingular(accountingMessages.rfpDeletedSuccessfully(), wfmStrings.requestForPurchase()), Info.Type.INFO);
                                        listPanel.reloadPage();
                                        removeDeletedTab(REQUEST_FOR_PURCHASE + item.getObjectID());
                                    }
                                });
                            }
                        });
                        messageBox.open();
                    });
                    actionItemCount++;
                    menuBar.addItem(delete);
                }

                MenuPopItem pdf = new MenuPopItem(wfmStrings.pdf());
                final HTMLPanel htmlPanel = new HTMLPanel("");

                pdf.setCommand(() -> new PDFTemplateSelector(AccountingConstants.RFP, new ExtendedCommand() {
                    @Override
                    public void execute(Integer id) {
                        generatePDF(htmlPanel, id, item.getObjectID());
                    }
                }));
                add(htmlPanel);
                actionItemCount++;
                menuBar.addItem(pdf);

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        config.setColumnSortable(false);
        config.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        config.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        config.setShow(true);
        columns.add(config);

        config = new ColumnDefinitionConfig<RFPData, SimpleLink>(wfmStrings.number(), RFPData.NUMBER, 100) {
            @Override
            public SimpleLink getCellValue(final RFPData item) {
                return getLink(item.getNumberData().getNumberString(), "requestforpurchase|" + (DRAFT.equals(item.getStatus()) ? "edit/" : "summary/") + item.getObjectID(), item.getNumberData().getNumberString(), item.getNumberData().getNumberString());
            }
        };
        config.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        config.setMinimumColumnWidth(50);
        columns.add(config);

        // Customer
        config = new ColumnDefinitionConfig<RFPData, String>(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()), RFPData.CUSTOMER, 100) {
            @Override
            public String getCellValue(final RFPData item) {
                return item.getCustomer() != null ? item.getCustomer().getName() : wfmStrings.notAvailable();
            }
        };
        config.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        config.setMinimumColumnWidth(100);
        columns.add(config);

        config = new ColumnDefinitionConfig<RFPData, String>(wfmStrings.dueDate(), RFPData.DUE_DATE, 100) {
            @Override
            public String getCellValue(RFPData item) {
                return DateUtils.format(item.getDueDate());
            }
        };
        config.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        config.setMinimumColumnWidth(100);
        columns.add(config);

        config = new ColumnDefinitionConfig<RFPData, String>(wfmStrings.currentApprover(), RFPData.MANAGER, 150) {
            @Override
            public String getCellValue(RFPData item) {
                return item.getCurrentApprover() != null ? item.getCurrentApprover().getName() : wfmStrings.notAvailable();
            }
        };
        config.setMinimumColumnWidth(100);
        columns.add(config);

        config = new ColumnDefinitionConfig<RFPData, String>(wfmStrings.employee(), RFPData.CREATOR, 150) {
            @Override
            public String getCellValue(RFPData item) {
                return item.getCreator().getName();
            }
        };
        config.setMinimumColumnWidth(100);
        columns.add(config);
        config.setShow(false);

        config = new ColumnDefinitionConfig<RFPData, String>(wfmStrings.project(), RFPData.RELATED_PROJECT, 180) {
            @Override
            public String getCellValue(RFPData rowValue) {
                return rowValue.getRelatedProject() != null ? rowValue.getRelatedProject().getName() : wfmStrings.notAvailable();
            }
        };
        config.setMinimumColumnWidth(180);
        columns.add(config);

        config = new ColumnDefinitionConfig<RFPData, String>(wfmStrings.status(), RFPData.STATUS, 100) {
            @Override
            public String getCellValue(RFPData item) {
                if (DRAFT.equals(item.getStatus())) {
                    return wfmStrings.draft();
                } else if (SUBMITTED_TO_MANAGER.equals(item.getStatus())) {
                    return wfmStrings.submitted();
                } else if (APPROVE.equals(item.getStatus())) {
                    return wfmStrings.approved();
                } else if (REJECT.equals(item.getStatus())) {
                    return wfmStrings.rejected();
                } else if (CONVERTED.equals(item.getStatus())) {
                    return wfmStrings.converted();
                } else if (OPEN.equals(item.getStatus())) {
                    return wfmStrings.open();
                } else if (PAID.equals(item.getStatus())) {
                    return wfmStrings.paid();
                } else if (OVER_DUE.equals(item.getStatus())) {
                    return accountingStrings.overdue();
                } else {
                    return item.getStatus();
                }
            }
        };
        config.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        config.setMinimumColumnWidth(100);
        columns.add(config);

        config = new ColumnDefinitionConfig<RFPData, String>(wfmStrings.createdDate(), RFPData.CREATED_DATE, 100) {
            @Override
            public String getCellValue(RFPData item) {
                return DateUtils.format(item.getCreatedDate());
            }
        };
        config.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        config.setMinimumColumnWidth(100);
        config.setShow(false);
        columns.add(config);

        return columns.toArray(new CustomColumnDefinitionConfig[0]);
    }

    private void showRejectionDialogBox(Integer objectID) {
        KpiModal dialogBox = new KpiModal();
        dialogBox.addStyleName("rfp-rejectPopup");
        dialogBox.setTitle(wfmStrings.reject());
        WfmForm form = new WfmForm();
        TextArea2 reason = new TextArea2(250, wfmStrings.rejectionReason());
        form.addField(null, reason);
        form.addButton(new WfmButton2(wfmStrings.reject(), clickEvent -> {
            quoteService.changeRFPstatus(objectID, REJECT, reason.getText(), true, new AsyncCallback<Void>() {
                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(Void result) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_REQUEST_FOR_PURCHASE_ADD_EDIT, result, RequestForPurchaseListView.this);
                    Info.show(property.getSingular(wfmStrings.messSuccessfullyRejected(), wfmStrings.requestForPurchase()), Info.Type.INFO);
                }
            });
            dialogBox.close();
        }));
        dialogBox.add(form);
        dialogBox.open();
    }

    @Override
    protected Widget onInitialize() {
        listPanel = new GuideListingPanel(ListPanelType.RequestForPurchaseListPanel, getColumnConfig(), getListProvider(), getListDesign(), SelectionGrid.SelectionPolicy.CHECKBOX);

        listPanel.setPDFListener(clickEvent -> {
            String pdfURL = CommandConstants.PDF_URL + "/requestForPurchaseListPDFHandler";
            ListingFilterParameter fp = listPanel.getFilterParametrs();
            fp.setPropertyCode(getPropertyCode());
            listPanel.callListPDF(pdfURL, fp);
        });
        listPanel.setExcelListener(clickEvent -> {
            String excelUrl = CommandConstants.COMMON_URL + "/downloadRequestForPurchaseExcel";
            ListingFilterParameter fp = listPanel.getFilterParametrs();
            fp.setPropertyCode(getPropertyCode());
            listPanel.callListExcel(excelUrl, fp);
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_REQUEST_FOR_PURCHASE_ADD_EDIT, RequestForPurchaseListView.this, (sender, args) -> listPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_REQUEST_FOR_PURCHASE_DELETE, RequestForPurchaseListView.this, (sender, args) -> listPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PURCHASEORDER_ADDED, RequestForPurchaseListView.this, (sender, args) -> listPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_STOCK_ADJUSTMENT_SAVED, RequestForPurchaseListView.this, (sender, args) -> listPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_REQUEST_FOR_QUOTE_ADDED, RequestForPurchaseListView.this, (sender, args) -> listPanel.reloadPage());
        listPanel.addSelectionRowHandler(selectedRows -> {
            selectedItems = selectedRows;
            if (selectedRows.size() > 0) {
                selectedItems = selectedRows;
                if (convertButton != null) {
                    convertButton.setVisible(true);
                }
            } else {
                if (convertButton != null) {
                    convertButton.setVisible(false);
                }
            }
        });
        add(listPanel);
        return null;
    }

    private GuideListingPanelDesign getListDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return hasPermissionToCreateRFP() ? () -> {
                    if (projectId != null) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("requestforpurchase|add/add/" + projectId + "/project");
                    } else {
                        SinksContainerFactory.entryPoint.onHistoryChanged("requestforpurchase|add/add");
                    }
                } : null;
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
            public ActionButton initTopToolBarNew() {
                if (hasPermissionToCreateRFP()) {
                    ActionButton addNew = getAddNewButton();
                    addNew.addClickHandler(clickEvent -> {
                        if (projectId != null) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("requestforpurchase|add/add/" + projectId + "/project");
                        } else {
                            SinksContainerFactory.entryPoint.onHistoryChanged("requestforpurchase|add/add");
                        }
                    });
                    return addNew;
                }
                return null;
            }

            @Override
            public ActionButton initTopToolBarPrint() {
                return getPrintButton(clickEvent -> printSelection());
            }

            public ActionButton initTopToolBarMore() {
                if (Utils.hasPermission(ACCOUNTING_REQUEST_FOR_PURCHASE_CONVERT)) {
                    convertButton = new ActionButton(wfmStrings.convert(), ActionButton.Type.TOOLMENU);
                    convertButton.ensureDebugId("invoice_convert_more_button");
                    convertButton.setVisible(false);
                    MenuBar menuBar = new MenuBar(true);
                    MenuPopItem convertToPO = new MenuPopItem(Property.get(Constants.PURCHASE_ORDER, wfmStrings.convertToo(), wfmStrings.purchaseorder()));
                    convertToPO.setCommand(() -> {
                        if (selectedItems.size() != 0) {
                            checkStatus(wfmStrings.purchaseorder(), new CloseHandler() {
                                @Override
                                public void onSubmit() {
                                    convertToPurchaseOrder();
                                }
                            });
                        }
                    });
                    MenuPopItem convertToSA = new MenuPopItem(accountingStrings.convertToStockAdjustment());
                    convertToSA.setCommand(() -> {
                        if (selectedItems.size() != 0) {
                            checkStatus(accountingStrings.stockAdjustment(), new CloseHandler() {
                                @Override
                                public void onSubmit() {
                                    convertToStockAdjustment();
                                }
                            });
                        }
                    });
                    MenuPopItem convertToRFQ = new MenuPopItem(property.getSingular(wfmStrings.convertToo(), wfmStrings.requestForQuote()));
                    convertToRFQ.setCommand(() -> {
                        if (selectedItems.size() != 0) {
                            checkStatus(wfmStrings.requestForQuote(), new CloseHandler() {
                                @Override
                                public void onSubmit() {
                                    convertToRFQ();
                                }
                            });
                        }
                    });

                    menuBar.addItem(convertToPO);
                    menuBar.addItem(convertToRFQ);
                    menuBar.addItem(convertToSA);
                    convertButton.setMenu(menuBar);
                    return convertButton;
                }
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {

                emptyDataTable.initEmptyDataTable(new DefaultNoItemsMessage() {
                    public VerticalPanel getWholeMessage() {
                        VerticalPanel vp = new VerticalPanel();
                        vp.add(new Label(wfmStrings.noDataAvailableInTableList()));
                        if (hasPermissionToCreateRFP()) {
                            HorizontalPanel hp = new HorizontalPanel();
                            SimpleLink link = new SimpleLink(wfmStrings.here(), "requestforpurchase|add/add", property.getSingular(wfmStrings.requestForPurchase()), property.getSingular(wfmStrings.requestForPurchase()));
                            link.setStyleName("addLinkStyle");
                            hp.add(new Label(wfmStrings.youCanStartAddingItemByClick()));
                            hp.add(link);
                            vp.add(hp);
                        }
                        return vp;
                    }
                });
            }
        };
    }

    private void printSelection() {
        if (selectedItems.size() == 0) {
            Info.show(accountingMessages.pleaseSelectOneRow(wfmStrings.requestForQuote()), Info.Type.WARNING);
        } else {
            new PDFTemplateSelector(REQUEST_FOR_PURCHASE, new ExtendedCommand() {
                @Override
                public void execute(Integer id) {
                    final HTMLPanel htmlPanel = new HTMLPanel("");
                    add(htmlPanel);
                    InvoiceQuoteRequestObject requestObject = new InvoiceQuoteRequestObject(getIdsOnly(selectedItems), id);
                    String pdfURL = CommandConstants.PDF_URL + "/requestForPurchsePDFHandler";
                    ListingFilterParameter fp = new ListingFilterParameter();
                    fp.setPropertyCode(getPropertyCode());
                    HashMap<String, String> parametrs = requestObject.getRequestParams();
                    Utils.sendPDFOrExcelRequest(htmlPanel, pdfURL, parametrs, "_blank");
                }
            });
        }
    }

    private boolean hasPermissionToCreateRFP() {
        return Utils.hasPermission(ACCOUNTING_REQUEST_FOR_PURCHASE_ADD);
    }

    private ListingRequestProvider getListProvider() {
        return (filterParametrs, callback) -> {
            loadRFPData(filterParametrs, callback, null);
        };
    }

    private void loadRFPData(ListingFilterParameter filterParametrs, ListingCallback callback, Span container) {
        if (projectId != null) {
            filterParametrs.setProjectId(projectId);
        }
        quoteService.getRFPList(filterParametrs, new AsyncCallback<ListResult<RFPData>>() {
            @Override
            public void onFailure(Throwable throwable) {
                if (callback != null) {
                    callback.onFailure(throwable);
                }
            }

            @Override
            public void onSuccess(ListResult<RFPData> listResult) {
                if (callback != null) {
                    callback.onSuccess(listResult);
                }

                statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                if (statisticShortcut != null) {
                    if (listResult.getTotal() != null && listResult.getTotal() > 0) {
                        statisticShortcut.setText(countFormat(listResult.getTotal()));
                        statisticShortcut.setClass("tab-label");
                    } else {
                        statisticShortcut.setText("");
                        statisticShortcut.removeStyleName("tab-label");
                    }
                }

            }
        });
    }

    @Override
    public String getIconStyle() {
        return "accountMark purchase-order-list";
    }

    @Override
    public void initStatistics(Integer parentId, Span container) {
        ListingFilterParameter fp = new ListingFilterParameter();
        loadRFPData(fp, null, container);
    }

    private void checkStatus(String type, CloseHandler closeHandler) {
        for (Object s : selectedItems) {
            if (!APPROVE.equals(((RFPData) s).getStatus())) {
                Info.show(property.getSingular(accountingStrings.errorConvertingToPO(), wfmStrings.requestForPurchase()), Info.Type.WARNING);
                return;
            }
        }

        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
        messageBox.setTitle(wfmStrings.warning());

        messageBox.setMessage(accountingMessages.confirmConvertingToPOMessage(type));
        messageBox.addCloseHandler(closeHandler);
        messageBox.open();
    }

    private void convertToPurchaseOrder() {
        goTo("purchaseorder|add/add/converFromRFP/" + getRFPIDsAsString());
    }

    private void convertToStockAdjustment() {
        goTo("stockadjustment|add/add/convertFromRFP/" + getRFPIDsAsString());
    }

    private void convertToRFQ() {
        goTo("requestforquote|add/add/convertFromRFP/" + getRFPIDsAsString());
    }

    private String getRFPIDsAsString() {
        String ids = "";
        for (Object obj : selectedItems) {
            RFPData item = (RFPData) obj;
            ids += item.getObjectID().toString();
            if (!"".equals(ids)) {
                ids += ",";
            }
        }
        return ids;
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

    @Override
    public String getPropertyCode() {
        return Constants.REQUEST_FOR_PURCHASE;
    }

    private void generatePDF(HTMLPanel panel, Integer templateID, Integer objectID) {
        InvoiceQuoteRequestObject requestObject = new InvoiceQuoteRequestObject(objectID);
        HashMap<String, String> parameters = requestObject.getRequestParams();
        if (templateID != null) {
            parameters.put("templateID", String.valueOf(templateID));
        }
        String pdfURL = CommandConstants.PDF_URL + "/requestForPurchsePDFHandler";
        Utils.sendPDFOrExcelRequest(panel, pdfURL, parameters, "_blank");
    }
}
