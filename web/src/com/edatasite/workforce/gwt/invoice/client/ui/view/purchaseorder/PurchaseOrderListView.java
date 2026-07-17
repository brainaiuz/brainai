package com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseorder;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.ConvertItem;
import com.edatasite.workforce.gwt.core.client.rpc.PropertyItem;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrSaleInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.MessageCommand;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
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
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.PDFTemplateSelector;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceList;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceQuoteRequestObject;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteServiceAsync;
import com.edatasite.workforce.gwt.invoice.client.ui.view.shippingData.GdnAndGrnListNavBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Rinat
 * Date: 18.07.2011
 * Time: 15:09:15
 * To change this template use File | Settings | File Templates.
 */

public class PurchaseOrderListView extends BaseListView implements Constants, AccountingConstants, PermissionConstants {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private final QuoteServiceAsync quoteService = QuoteService.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private Integer clientID;
    private Integer productId;
    private ListingPanel listPanel;
    private ListingFilterParameter fp;
    protected HashSet selectedItems = new HashSet();
    private final ActionButton delete = null;
    private final ImportFilePopUp imp = new ImportFilePopUp(getImportType(), null);

    public PurchaseOrderListView() {
        super(PURCHASE_ORDER);
        setDescription(property.getPlural(wfmStrings.purchaseOrders()));
        if (hasPermissionToCreatePO()) {
            setAddNew("purchaseorder|add/add");
        }
    }

    public PurchaseOrderListView(Integer productId, Boolean productPO) {
        super(PURCHASE_ORDER);
        setDescription(property.getPlural(wfmStrings.purchaseOrders()));
        this.productId = productId;
        if (hasPermissionToCreatePO()) {
            setAddNew("purchaseorder|add/add");
        }
    }

    public PurchaseOrderListView(ListingFilterParameter fP) {
        super(PURCHASE_ORDER);
        setDescription(property.getPlural(wfmStrings.purchaseOrders()));
        this.fp = fP;
        if (fp != null && fp.getSupplierId() != null) {
            this.clientID = fp.getSupplierId();
        }
        if (hasPermissionToCreatePO()) {
            setAddNew("purchaseorder|add/add");
        }
    }

    @Override
    protected Widget onInitialize() {
        listPanel = new GuideListingPanel(ListPanelType.PurchaseOrderListPanel, getColumnConfig(), getListProvider(), getListDesign(), SelectionGrid.SelectionPolicy.CHECKBOX, false, Utils.hasPermission(ACCOUNTING_PURCHASE_ORDER_LIST_CUSTOMIZE));

        listPanel.setCustomFieldsEditCellSaveChanges((rowValue, columnCodeName) -> savePurchaseOrderCellValue((NewInvoice) rowValue, columnCodeName));

        listPanel.setPDFListener(clickEvent -> {
            String pdfURL = CommandConstants.PDF_URL + "/purchaseOrderListPDFHandler";
            ListingFilterParameter filterParametrs = listPanel.getFilterParametrs();
            filterParametrs.setPropertyCode(getPropertyCode());
            filterParametrs.setStartDateNC(filterParametrs.getStartDate() != null ? Utils.getStartDateNCForFilter(filterParametrs.getStartDate()) : null);
            filterParametrs.setEndDateNC(filterParametrs.getEndDate() != null ? Utils.getEndDateNCForFilter(filterParametrs.getEndDate()) : null);
            listPanel.callListPDF(pdfURL, filterParametrs);
        });
        listPanel.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadPurchaseOrderListExcel";
            ListingFilterParameter filterParametrs = listPanel.getFilterParametrs();
            filterParametrs.setPropertyCode(getPropertyCode());
            filterParametrs.setStartDateNC(filterParametrs.getStartDate() != null ? Utils.getStartDateNCForFilter(filterParametrs.getStartDate()) : null);
            filterParametrs.setEndDateNC(filterParametrs.getEndDate() != null ? Utils.getEndDateNCForFilter(filterParametrs.getEndDate()) : null);
            listPanel.callListExcel(excelURL, filterParametrs);
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PURCHASEORDER_ADDED, PurchaseOrderListView.this, (sender, args) -> listPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PURCHASEORDER_RECEIVED, PurchaseOrderListView.this, (sender, args) -> listPanel.reloadPage());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALEQUOTE_ADDED, PurchaseOrderListView.this, (sender, args) -> listPanel.reloadPage());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALE_QUOTE_DELETED, PurchaseOrderListView.this, (sender, args) -> listPanel.reloadPage());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_REQUEST_FOR_QUOTE_CONVERTED, PurchaseOrderListView.this, (sender, args) -> listPanel.reloadPage());

        listPanel.addSelectionRowHandler(selectedRows -> {
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

        add(listPanel);
        return null;
    }

    private void savePurchaseOrderCellValue(NewInvoice rowValue, String columnCodeName) {
        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_PURCHASE_ORDER_EDIT)) {
            quoteService.savePurchaseOrderCellValue(rowValue, columnCodeName, new AbstractAsyncCallback<Boolean>() {
            });
        } else {
            Info.warn(wfmStrings.youDontHavePermission());
            listPanel.reloadPage();
        }
    }

    private boolean hasPermissionToCreatePO() {
        return Utils.hasPermission(ACCOUNTING_PURCHASE_ORDER_ADD);
    }

    private CustomColumnDefinitionConfig[] getColumnConfig() {
        boolean purchaseClientEnabled = Utils.hasGenericAccess(GenericSettingsEnum.PURCHASE_CLIENT_ENABLED);
        ArrayList<ColumnDefinitionConfig> columns = new ArrayList<>();

        ColumnDefinitionConfig columnConfig = new ColumnDefinitionConfig<NewInvoice, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final NewInvoice invoice) {
                String statusCode = invoice.getStatusCode();
                final Integer objectID = invoice.getID();
                final boolean hasAccessToChange = !Utils.isLockCompletedProjecItems() || (Utils.isLockCompletedProjecItems() && !PS_CLOSED.equals(invoice.getProjectStatusCode()));
                boolean canEdit = DRAFT.equalsIgnoreCase(statusCode) || REJECT.equalsIgnoreCase(statusCode) || APPROVE.equalsIgnoreCase(statusCode) || SUBMITTED_TO_MANAGER.equalsIgnoreCase(statusCode) ||
                        statusCode.equals(PARTIAL_RECEIVED) || statusCode.equals(RECEIVED) || statusCode.equals(INVOICED) || statusCode.equals(OPEN);
                if (APPROVE.equals(statusCode) && invoice.getCurrentApproverSelectItem() != null) {
                    canEdit = invoice.getCurrentApproverSelectItem().getId().equals(Utils.getUserID());
                }
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                menuBar.setAutoOpen(true);

                if (!DRAFT.equalsIgnoreCase(statusCode) && Utils.hasPermission(ACCOUNTING_PURCHASE_ORDER_SUMMARY)) {
                    MenuPopItem purchaseOrderSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-task-small");
                    purchaseOrderSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(PURCHASE_ORDER + "|summary/" + objectID, invoice.getInvoiceNumber()));
                    actionItemCount++;
                    menuBar.addItem(purchaseOrderSummary);
                }

                if (hasAccessToChange && (Utils.hasPermission(ACCOUNTING_PURCHASE_ORDER_EDIT) || Utils.hasPermission(ACCOUNTING_PURCHASE_ORDER_FULL_EDIT_ACCESS))) {
                    if (canEdit) {

                        invoice.setAccess(true);

                        MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                        edit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("purchaseorder|edit/" + objectID, invoice.getInvoiceNumber()));
                        actionItemCount++;
                        menuBar.addItem(edit);
                    }
                }
                if (hasAccessToChange && Utils.hasPermission(ACCOUNTING_PURCHASE_ORDER_DELETE)) {
                    MenuPopItem delete = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    delete.setCommand(() -> {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                deletePurchaseOrder(objectID);
                            }
                        });
                        messageBox.open();
                    });
                    actionItemCount++;
                    menuBar.addItem(delete);
                }


                PropertyItem propertyItem = Utils.getProperTy(Constants.PURCHASE_ORDER);
                if (propertyItem != null && propertyItem.getConvertItems() != null && propertyItem.getConvertItems().length > 0) {
                    MenuPopItem convertMenuPopItem = new MenuPopItem(wfmStrings.convert(), "icon-add-green");

                    MenuBar convertMenu = new MenuBar(true);
                    convertMenu.setAutoOpen(true);
                    int convertItems = 0;
                    for (ConvertItem convertItem : propertyItem.getConvertItems()) {
                        if (convertItem != null) {
                            convertItems = getConvertItems(invoice, menuBar, convertMenu, convertItems, convertItem);
                        }
                    }

                    if (convertItems > 0) {
                        convertMenuPopItem.setSubMenu(convertMenu);
                        actionItemCount++;
                        menuBar.addItem(convertMenuPopItem);
                    }
                }

                if (hasAccessToChange && Utils.hasPermission(ACCOUNTING_PURCHASE_ORDER_CLOSED)) {

                    if (REJECT.equalsIgnoreCase(statusCode)
                            || APPROVE.equalsIgnoreCase(statusCode)
                            || PARTIAL_RECEIVED.equalsIgnoreCase(statusCode)
                            || PARTIAL_INVOICED.equalsIgnoreCase(statusCode)
                            || SUBMITTED_TO_MANAGER.equalsIgnoreCase(statusCode)) {
                        MenuPopItem closedOption = new MenuPopItem(wfmStrings.closed(), "icon-puchase-invoise-small");
                        closedOption.ensureDebugId(PURCHASE_ORDER + "closed");
                        closedOption.setCommand(() -> {
                            final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                            messageBox.setTitle(wfmStrings.confirmation());
                            messageBox.setMessage(wfmStrings.areYouSureYouWanttoCloseThe_() + " " + (property.getSingular(wfmStrings.purchaseorder())).toLowerCase() + "?");
                            messageBox.addCloseHandler(new CloseHandler() {
                                @Override
                                public void onSubmit() {
                                    LoadingPanel.loading(true);
                                    quoteService.closedOrder(objectID, new AbstractAsyncCallback() {
                                        public void failure(Throwable caught) {
                                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                        }

                                        public void success(Object result) {
                                            Info.show(property.getPlural(wfmStrings.purchaseorder()) + " " + wfmStrings.closedSuccessfully(), Info.Type.INFO);
                                            listPanel.reloadPage();
                                        }
                                    });
                                }
                            });
                            messageBox.open();
                        });

                        actionItemCount++;
                        menuBar.addItem(closedOption);
                    }
                }
                if (Utils.hasPermission(ACCOUNTING_PURCHASE_ORDER_GOODS_RECEIVED_NOTE)) {
                    final MenuPopItem goodsReceivedNotes = new MenuPopItem(accountingStrings.goodsReceivedNotes(),
                            "icon-sales-quote-small");

                    goodsReceivedNotes.setCommand(() -> {
                        new GdnAndGrnListNavBox(objectID, true).show();
                    });
                    actionItemCount++;
                    menuBar.addItem(goodsReceivedNotes);
                }

                MenuPopItem generatePdf = new MenuPopItem(wfmStrings.pdf(), "icon-pdf");
                final HTMLPanel htmlPanel = new HTMLPanel("");
                generatePdf.setCommand(() -> new PDFTemplateSelector(PURCHASE_ORDER, new ExtendedCommand() {
                    @Override
                    public void execute(Integer id) {
                        generatePDF(htmlPanel, id, objectID);
                    }
                }));
                add(htmlPanel);
                actionItemCount++;
                menuBar.addItem(generatePdf);


                MenuPopItem copyMenuPopItem = new MenuPopItem(wfmStrings.copy(), "icon-add-green");

                MenuBar copyMenu = new MenuBar(true);
                copyMenu.setAutoOpen(true);

                int copyItems = 0;
                copyItems = getCopyAction(invoice, menuBar, copyMenu, copyItems);

                if (copyItems > 0) {
                    copyMenuPopItem.setSubMenu(copyMenu);
                    actionItemCount++;
                    menuBar.addItem(copyMenuPopItem);
                }

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();

            }
        };
        columnConfig.setColumnSortable(false);
        columnConfig.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfig.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, Widget>(wfmStrings.number(), InvoiceList.INVOICE_NUMBER, 120) {
            @Override
            public Widget getCellValue(NewInvoice invoice) {

                Label label = new Label(invoice.getInvoiceNumber() != null ? invoice.getInvoiceNumber() : "");

                if (invoice.getInvoiceNumber() != null) {
                    label.setStyleName("uploadLinkStyle2");
                    final Integer objectID = invoice.getID();

                    label.addClickHandler(clickEvent -> {
                        if (DRAFT.equalsIgnoreCase(invoice.getStatusCode())) {
                            boolean hasAccessToChange = !Utils.isLockCompletedProjecItems() || (Utils.isLockCompletedProjecItems() && !PS_CLOSED.equals(invoice.getProjectStatusCode()));

                            if (hasAccessToChange && ((invoice.isSubmitter(Utils.getUserID()) &&
                                    Utils.hasPermission(ACCOUNTING_PURCHASE_ORDER_EDIT))
                                    || Utils.hasPermission(ACCOUNTING_PURCHASE_ORDER_FULL_EDIT_ACCESS))) {
                                invoice.setAccess(true);
                                SinksContainerFactory.entryPoint.onHistoryChanged("purchaseorder|edit/" + objectID, invoice.getInvoiceNumber());
                            } else {
                                label.removeStyleName("uploadLinkStyle2");
                            }
                        } else {
                            SinksContainerFactory.entryPoint.onHistoryChanged(PURCHASE_ORDER + "|summary/" + objectID, invoice.getInvoiceNumber());
                        }

                    });
                }
                return label;
            }
        };
        columnConfig.addStyleAttribute("paddingLeft", "5px");
        columnConfig.setMinimumColumnWidth(100);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, SimpleLink>(Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()), InvoiceList.CLIENT, 250) {
            @Override
            public SimpleLink getCellValue(NewInvoice invoice) {
                SimpleLink label = new SimpleLink(invoice.getClientName() != null ? invoice.getClientName() : "");
                if (invoice.getClientID() != null && Utils.hasPermission(ACCOUNTING_SUPPLIER_SUMMARY)) {
                    label.addClickHandler(clickEvent -> {
                        SinksContainerFactory.entryPoint.onHistoryChanged("suppliersummary|summary/" + invoice.getClientID());
                    });
                }
                return label;
            }
        };
        columnConfig.setColumnSortable(false);
        columnConfig.addStyleAttribute("paddingLeft", "5px");
        columnConfig.setMinimumColumnWidth(200);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.date(), InvoiceList.INVOICE_DATE, 120) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                return DateUtils.format(invoice.getInvoiceDate());
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig.setMinimumColumnWidth(100);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(accountingStrings.validDate(), InvoiceList.DUE_DATE, 150) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                return DateUtils.format(invoice.getDueDate());
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig.setMinimumColumnWidth(100);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.amount(), InvoiceList.DUE_AMOUNT, 120) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                return AccountingUtils.get().formatPrice(invoice.getAmount().subtract(invoice.getPaidAmount() != null ? invoice.getPaidAmount() : ZERO));
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        columnConfig.addStyleAttribute("paddingLeft", "5px");
        columnConfig.setMinimumColumnWidth(80);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.status(), InvoiceList.STATUS, 150) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                if (APPROVE.equals(invoice.getStatusCode())) {
                    return wfmStrings.approved();
                } else if (SUBMITTED_TO_MANAGER.equals(invoice.getStatusCode())) {
                    return wfmStrings.submitted();
                } else if (SALE_ORDER.equals(invoice.getStatusCode())) {
                    return accountingStrings.salesOrder();
                } else if (INVOICED.equals(invoice.getStatusCode())) {
                    return accountingStrings.invoiced();
                } else if (RECEIVED.equals(invoice.getStatusCode())) {
                    return accountingStrings.received();
                } else if (DRAFT.equals(invoice.getStatusCode())) {
                    return wfmStrings.draft();
                } else if (PAID.equals(invoice.getStatusCode())) {
                    return wfmStrings.paid();
                } else if (OVER_DUE.equals(invoice.getStatusCode())) {
                    return accountingStrings.overdue();
                } else {
                    return invoice.getStatus();
                }
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig.setMinimumColumnWidth(100);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.currency(), InvoiceList.CURRENCY, 100) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                return invoice.getCurrencyName() != null ? invoice.getCurrencyName() : "";
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig.setMinimumColumnWidth(80);
        columnConfig.setShow(false);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(Property.get(Constants.PROJECT, wfmStrings.project()), InvoiceList.RELATED_PROJECT, 150) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                return invoice.getRelatedProjectName() != null ? invoice.getRelatedProjectName() : "";
            }
        };
        columnConfig.addStyleAttribute("paddingLeft", "5px");
        columnConfig.setMinimumColumnWidth(120);
        columnConfig.setShow(false);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.quoteNumber(), InvoiceList.QUOTE_NUMBER, 100) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                return invoice.getQuoteNumber();
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);
        columnConfig.setMinimumColumnWidth(70);
        columnConfig.setShow(false);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.createdBy(), InvoiceList.CREATOR, 100) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                return invoice.getCreator() != null ? invoice.getCreator().getName() : "";
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);
        columnConfig.setMinimumColumnWidth(70);
        columnConfig.setShow(false);
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.currentApprover(), InvoiceList.MANAGER, 100) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                return invoice.getCurrentApproverSelectItem() != null ? invoice.getCurrentApproverSelectItem().getName() : "";
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);
        columnConfig.setMinimumColumnWidth(70);
        columnConfig.setColumnSortable(false);
        columnConfig.setShow(false);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.subtotal(), InvoiceList.SUB_TOTAL, 100) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                return AccountingUtils.get().formatPrice(invoice.getSubtotal() != null ? invoice.getSubtotal() : BigDecimal.ZERO);
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        columnConfig.setMinimumColumnWidth(70);
        columnConfig.setShow(false);
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.taxTotal(), InvoiceList.TAX_TOTAL, 100) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                return AccountingUtils.get().formatPrice(invoice.getTotalTaxesInInvoiceCurrency() != null ? invoice.getTotalTaxesInInvoiceCurrency() : BigDecimal.ZERO);
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        columnConfig.setMinimumColumnWidth(70);
        columnConfig.setShow(false);
        columnConfig.setColumnSortable(true);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.total() + " (" + AccountingUtils.getBaseCurrencySymbol() + ")", InvoiceList.BASE_TOTAL, 100) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                return AccountingUtils.get().formatPrice(invoice.getTotal() != null ? invoice.getTotal() : BigDecimal.ZERO);
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        columnConfig.setMinimumColumnWidth(70);
        columnConfig.setShow(false);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, Widget>(Property.get(Constants.Opportunities, wfmStrings.opportunity()), InvoiceList.OPPORTUNITY_NUMBER, 100) {
            @Override
            public Widget getCellValue(NewInvoice invoice) {
                final String[] url = {""};
                invoice.getOpportunityNumber();
                SimpleLink label = new SimpleLink(invoice.getOpportunityNumber() != null ? invoice.getOpportunityNumber() : "");

                label.addClickHandler(clickEvent -> {
                    SinksContainerFactory.entryPoint.onHistoryChanged("opportunity|summary/" + invoice.getOpportunityID(), invoice.getOpportunityNumber());
                });
                return label;
            }
        };
        columnConfig.setColumnSortable(false);
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(80);
        columns.add(columnConfig);

        if (purchaseClientEnabled) {
            columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()), InvoiceList.CUSTOMER, 100) {
                @Override
                public String getCellValue(NewInvoice invoice) {
                    return invoice.getCustomerName() != null ? invoice.getCustomerName() : "";
                }
            };
            columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
            columnConfig.setMinimumColumnWidth(70);
            columnConfig.setShow(false);
            columns.add(columnConfig);
        }
        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.reference(), InvoiceList.REFERENCE, 100) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                return invoice.getReference() != null ? invoice.getReference() : "";
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(80);
        columns.add(columnConfig);


        return columns.toArray(new ColumnDefinitionConfig[0]);
    }

    public static String getIdsOnly(Set<NewInvoice> selectedItems) {
        StringBuilder ids = null;
        for (NewInvoice item : selectedItems) {
            if (ids == null) {
                ids = new StringBuilder();
                ids.append(item.getID());
            } else {
                ids.append("," + item.getID());
            }
        }
        return ids.toString();
    }

    private GuideListingPanelDesign getListDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return hasPermissionToCreatePO() ? PurchaseOrderListView.this::createNewPO : null;
            }

            @Override
            public Command getUploadButtonCommand() {
                return hasImportButton() ? imp::open : null;
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
                            data.setName(LayoutRPC.ACCOUNTING_SECTION);

                            if (clientID != null) {
                                SelectItem[] item = new SelectItem[]{new SelectItem(clientID)};
                                data.getFacetContentMap().get(FacetContentType.PurchaseOrderFacetFilter.getContentCode()[0]).setFacetItems(item);
                            }
                            RbacService.App.get().getPurchaseOrderFacetFilterData(data, new AbstractAsyncCallback<FacetFilterRpc>() {
                                @Override
                                public void failure(Throwable caught) {
                                    callback.onFailure(caught);
                                }

                                @Override
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
                if (hasPermissionToCreatePO()) {
                    ActionButton addNew = getAddNewButton();
                    addNew.ensureDebugId(PURCHASE_ORDER + "addNewButton");
                    String shortCut = "Alt + Shift + o";
                    if (Utils.isChrome()) {
                        shortCut = "Alt + o";
                    }
                    addNew.setTitle(shortCut);
                    addNew.addClickHandler(clickEvent -> {
                        createNewPO();
                    });
                    return addNew;
                }
                return null;
            }

            @Override
            public ActionButton initTopToolBarMore() {
                if (Utils.hasPermission(ACCOUNTING_PURCHASE_ORDER_DELETE)) {
                    return getRemoveMoreButton(clickEvent -> deleteSelection());
                }
                return null;
            }

            @Override
            public ActionButton initTopToolBarPrint() {
                return getPrintButton(clickEvent -> printSelection());
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                imp.setSubmitCompleted(() -> {
                    if (imp.getObjectId() != null) {
                        goTo("importPurchaseOrders|add/add/" + imp.getObjectId());
                    }
                });
                if (hasImportButton()) {
                    ImportFileActionLink link = new ImportFileActionLink();
                    link.addClickHandler(ch -> imp.open());
                    menuContainer.add(link);
                }
                exportOption.initExport(null);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(property.getPlural(accountingStrings.messCurrentlyPurchaseOrders(), wfmStrings.purchaseOrders()));
                if (hasPermissionToCreatePO()) {
                    if (clientID != null) {
                        message.setHref("purchaseorder|add/add/fromSupplierList/" + clientID);
                    } else {
                        message.setHref("purchaseorder|add/add");
                    }
                    message.setTextBeforeLink(property.getSingular(accountingStrings.messAddingPurchaseOrderClicking(), wfmStrings.purchaseorder()));
                }
                emptyDataTable.initEmptyDataTable(message);
            }

            public void initDataEmptyTable(HTML emptyTable) {
                emptyTable.setText("");
                emptyTable.setStyleName("drawColumns");
                emptyTable.addClickHandler(clickEvent -> {
                    //To change body of implemented methods use File | Settings | File Templates.
                });

            }

            @Override
            public boolean isEditCustomFieldCell() {
                return Utils.hasPermission(PermissionConstants.ACCOUNTING_PURCHASE_ORDER_EDIT);
            }
        };
    }

    private void printSelection() {
        if (selectedItems.size() == 0) {
            Info.show(accountingMessages.pleaseSelectOneRow(wfmStrings.purchaseorder()), Info.Type.WARNING);
        } else {
            new PDFTemplateSelector(PURCHASE_ORDER, new ExtendedCommand() {
                @Override
                public void execute(Integer id) {
                    final HTMLPanel htmlPanel = new HTMLPanel("");
                    add(htmlPanel);
                    InvoiceQuoteRequestObject requestObject = new InvoiceQuoteRequestObject(getIdsOnly(selectedItems), id);
                    String pdfURL = CommandConstants.PDF_URL + "/savedPurchaseOrderViewPDFHandler";
                    HashMap<String, String> parametrs = requestObject.getRequestParams();
                    Utils.sendPDFOrExcelRequest(htmlPanel, pdfURL, parametrs, "_blank");
                }
            });
        }
    }

    private void deleteSelection() {
        if (selectedItems.size() == 0) {
            Info.show(accountingMessages.pleaseSelectOneRow(wfmStrings.purchaseorder()), Info.Type.WARNING);
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
                    quoteService.deleteSelectedPurchaseOrders(ids, new AbstractAsyncCallback<Void>() {
                        @Override
                        public void failure(Throwable caught) {
                            LoadingPanel.loading(false);
                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                        }

                        @Override
                        public void success(Void result) {
                            listPanel.reloadPage();
                            LoadingPanel.loading(false);
                            Info.show(property.getSingular(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.purchaseOrders()), Info.Type.INFO);

                            for (Integer id : ids) {
                                removeDeletedTab(PURCHASE_ORDER + id);
                            }
                        }
                    });
                }
            }
        });
        messageBox.open();
    }

    private void createNewPO() {
        if (fp != null && fp.getOpportunityID() != null) {
            SinksContainerFactory.entryPoint.onHistoryChanged(Constants.PURCHASE_ORDER + "|add/add/opportunity/" + fp.getOpportunityID());
        } else if (clientID != null) {
            SinksContainerFactory.entryPoint.onHistoryChanged("purchaseorder|add/add/fromSupplierList/" + clientID);
        } else {
            SinksContainerFactory.entryPoint.onHistoryChanged("purchaseorder|add/add");
        }
    }

    private int getCopyAction(NewInvoice invoice, MenuBar menuBar, MenuBar copyMenu, int copyItems) {
        final boolean hasAccessToChange = !Utils.isLockCompletedProjecItems() || (Utils.isLockCompletedProjecItems() && !PS_CLOSED.equals(invoice.getProjectStatusCode()));
        final Integer objectID = invoice.getID();
        String statusCode = invoice.getStatusCode();


        if (hasAccessToChange && Utils.hasPermission(ACCOUNTING_PURCHASE_ORDER_COPY) && !INVOICE_STATUS_CLOSED.equals(statusCode)) {
            MenuPopItem copyExistingDataLink = new MenuPopItem(property.getShort(wfmStrings.purchaseorder()), "icon-copy");
            copyExistingDataLink.setCommand(() -> {
                copyExistingDataLink.closeAll(menuBar);
                SinksContainerFactory.entryPoint.onHistoryChanged(PURCHASE_ORDER + "|add/add/copyFromExistingData/" + objectID);
            });
            copyMenu.addItem(copyExistingDataLink);
            copyItems++;
        }

        if (hasAccessToChange && Utils.hasPermission(ACCOUNTING_PURCHASE_ORDER_COPY_SQ) && !INVOICE_STATUS_CLOSED.equals(statusCode)) {
            MenuPopItem copyToSQLink = new MenuPopItem(Property.getShortName(Constants.SALE_QUOTE, wfmStrings.salesQuote()), "icon-copy");
            copyToSQLink.setCommand(() -> {
                copyToSQLink.closeAll(menuBar);
                SinksContainerFactory.entryPoint.onHistoryChanged(SALE_QUOTE + "|add/add/copyFromPO/" + objectID);
            });
            copyMenu.addItem(copyToSQLink);
            copyItems++;
        }

        if (hasAccessToChange && !INVOICED.equals(statusCode) && !INVOICE_STATUS_CLOSED.equals(statusCode) && Utils.hasPermission(ACCOUNTING_PURCHASE_ORDER_COPY_PI)) {
            MenuPopItem copyExistingDataLink = new MenuPopItem(Property.getShortName(Constants.PURCHASE_INVOICE, wfmStrings.purchaseinvoice()), "icon-copy");
            copyExistingDataLink.setCommand(() -> {
                copyExistingDataLink.closeAll(menuBar);
                SinksContainerFactory.entryPoint.onHistoryChanged(PURCHASE_INVOICE + "|add/add/copyFromPO/" + objectID);
            });
            copyMenu.addItem(copyExistingDataLink);
            copyItems++;

        }
        return copyItems;
    }

    private void generatePDF(HTMLPanel hp, Integer pdfTemplateID, Integer objectId) {
        InvoiceQuoteRequestObject requestObject = new InvoiceQuoteRequestObject(objectId, pdfTemplateID, null);
        String pdfURL = CommandConstants.PDF_URL + "/savedPurchaseOrderViewPDFHandler";
        HashMap<String, String> parametrs = requestObject.getRequestParams();
        Utils.sendPDFOrExcelRequest(hp, pdfURL, parametrs, "_blank");
    }

    private FacetContentConfigure getFacetContentConfigure() {
        FacetContentConfigure contentConfigure = new FacetContentConfigure(3, wfmStrings.filter());
        contentConfigure.addContentConfigure(FacetContentType.PurchaseOrderFacetFilter.getContentCode()[0], Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID_NAME;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.PurchaseOrderFacetFilter.getContentCode()[1], wfmStrings.status(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrSaleInvoiceRepresenter.FIELD_STATUS_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrSaleInvoiceRepresenter.FIELD_STATUS_ID_NAME;
            }
        });
//        contentConfigure.addContentConfigure(FacetContentType.PurchaseOrderFacetFilter.getContentCode()[2], wfmStrings.amount(), new FacetFieldConfigure() {
//            @Override
//            public String getSolrFieldCriteriaName() {
//                return SolrSaleInvoiceRepresenter.FIELD_DUE_AMOUNT;
//            }
//
//            @Override
//            public String getSolrFacetFieldName() {
//                return SolrSaleInvoiceRepresenter.FIELD_DUE_AMOUNT;
//            }
//
//            @Override
//            public boolean isConditionItemId() {
//                return false;
//            }
//        });
        contentConfigure.addContentConfigure(FacetContentType.PurchaseOrderFacetFilter.getContentCode()[3], Property.get(Constants.PROJECT, wfmStrings.project()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return Utils.isProjectInLineItemEnable() ? SolrSaleInvoiceRepresenter.FIELD_MULTI_PROJECT_ID : SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return Utils.isProjectInLineItemEnable() ? SolrSaleInvoiceRepresenter.FIELD_MULTI_PROJECT_ID_NAME : SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_ID_NAME;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.PurchaseOrderFacetFilter.getContentCode()[4], wfmStrings.currency(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrSaleInvoiceRepresenter.FIELD_CURRENCY_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrSaleInvoiceRepresenter.FIELD_CURRENCY_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.PurchaseOrderFacetFilter.getContentCode()[6], wfmStrings.createdBy(), new FacetFieldConfigure() {

            @Override
            public String getSolrFieldCriteriaName() {
                return SolrSaleInvoiceRepresenter.FIELD_CREATOR_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrSaleInvoiceRepresenter.FIELD_CREATOR_ID_NAME;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.PurchaseOrderFacetFilter.getContentCode()[7], wfmStrings.currentApprover(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrSaleInvoiceRepresenter.FIELD_MANAGER_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrSaleInvoiceRepresenter.FIELD_MANAGER_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        return contentConfigure;
    }

    private ListingRequestProvider getListProvider() {
        return (filterParametrs, listingCallback) -> {
            filterParametrs = filterParametrs == null ? new ListingFilterParameter() : filterParametrs;
            if (filterParametrs.getFacetFilter() != null) {
                filterParametrs.setStartDate(filterParametrs.getFacetFilter().getStartDate());
                filterParametrs.setEndDate(filterParametrs.getFacetFilter().getEndDate());
            }
            initPurchaseOrderData(filterParametrs, listingCallback, null);
        };
    }

    private void initPurchaseOrderData(ListingFilterParameter filterParametrs, ListingCallback listingCallback, Span container) {
        if (clientID != null) {
            filterParametrs.setClientId(clientID);
        }
        if (fp != null) {
            if (fp.getOpportunityID() != null) {
                filterParametrs.setOpportunityID(fp.getOpportunityID());
            }
            if (fp.getRelationID() != null && fp.getRelationType() != null) {
                filterParametrs.setRelationID(fp.getRelationID());
                filterParametrs.setRelationType(fp.getRelationType());
            }
        }
        if (productId != null) {
            filterParametrs.setProductId(productId);
        }
        filterParametrs.setModule(LayoutRPC.ACCOUNTING_SECTION);

        quoteService.getPurchaseOrderData(filterParametrs, new AbstractAsyncCallback<InvoiceList>() {
            @Override
            public void failure(Throwable throwable) {
                if (listingCallback != null) {
                    listingCallback.onFailure(throwable);
                }
            }

            @Override
            public void success(InvoiceList invoiceList) {
                if (listingCallback != null) {
                    listingCallback.onSuccess(invoiceList);
                }
                statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                if (statisticShortcut != null) {
                    if (invoiceList.getTotal() != null && invoiceList.getTotal() > 0) {
                        statisticShortcut.setText(countFormat(invoiceList.getTotal()));
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

    private void deletePurchaseOrder(Integer objectID) {
        LoadingPanel.loading(true);
        quoteService.deleteQuote(objectID, PURCHASE_ORDER, new AbstractAsyncCallback<TestRPC>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(TestRPC result) {
                LoadingPanel.loading(false);
                if (MessageCommand.hasConvertedItems == result.getMessageCommand()) {
                    Info.show(property.getSingular(accountingStrings.cannotDeletepurchaseOrderHasConvertedInvoices(), wfmStrings.purchaseorder()) + property.getPlural(wfmStrings.purchaseInvoices()), Info.Type.WARNING);
                } else if (MessageCommand.hasOutTransactions == result.getMessageCommand()) {
                    Info.show(property.getSingular(accountingStrings.cannotDeletepurchaseOrderHasOutTransactions(), wfmStrings.purchaseorder()), Info.Type.WARNING);
                } else if (MessageCommand.hasShippingData == result.getMessageCommand()) {
                    Info.show(property.getSingular(accountingStrings.cannotDeletepurchaseOrderHasGoodsReceivedNotes(), wfmStrings.purchaseorder()), Info.Type.WARNING);
                } else {
                    Info.show(property.getSingular(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.purchaseorder()), Info.Type.INFO);
                    listPanel.reloadPage();

                    removeDeletedTab(PURCHASE_ORDER + objectID);
                }
            }
        });
    }

    public static ArrayList<Integer> getIDsOnly(HashSet<NewInvoice> selectedItems) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (NewInvoice item : selectedItems) {
            ids.add(item.getID());
        }
        return ids;
    }

    @Override
    public void initStatistics(Integer parentId, Span container) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setLimit(1);
        initPurchaseOrderData(fp, null, container);
    }

    @Override
    public String getPropertyCode() {
        return Constants.PURCHASE_ORDER;
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

    private int getConvertItems(NewInvoice rowValue, MenuBar menuBar, MenuBar convertMenu, int convertItems, ConvertItem convertItem) {
        if (RelationItem.TYPE_CASE.equals(convertItem.getCode()) && Utils.hasPermission(ADD_NEW_CASE)) {
            final MenuPopItem convertToCase = new MenuPopItem(Property.get(Constants.CASE_LIST, wfmStrings.crmCase()), "icon-send-sales-invoice");
            convertToCase.setCommand(() -> {
                convertToCase.closeAll(menuBar);
                if (Utils.getPathName().contains("Accounting.html") || Utils.getPathName().contains("Crm.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("case|add/add/CONVERT/" + RelationItem.TYPE_PURCHASE_ORDER + "/" + rowValue.getID());
                } else {
                    Utils.openURL("Crm.html#case|add/add/CONVERT/" + RelationItem.TYPE_PURCHASE_ORDER + "/" + rowValue.getID());
                }
            });
            convertToCase.ensureDebugId("convert_case");
            convertMenu.addItem(convertToCase);
            convertItems++;
        } else if (convertItem.getCode().contains("_FORM") && Utils.hasPermission(convertItem.getCode() + "_ADD_" + Utils.getCompanyID())) {
            final MenuPopItem convertToCF = new MenuPopItem(convertItem.getName(), "icon-send-sales-invoice");
            convertToCF.setCommand(() -> {
                convertToCF.closeAll(menuBar);
                SinksContainerFactory.entryPoint.onHistoryChanged(Constants.ITEM_LIST + "|add/add/" + convertItem.getEntityId() + "/" + convertItem.getCode() + "/CONVERT/" + RelationItem.TYPE_PURCHASE_ORDER + "/" + rowValue.getID());
            });
            convertToCF.ensureDebugId("convert_to_" + convertItem.getName());
            convertMenu.addItem(convertToCF);
            convertItems++;
        }
        return convertItems;
    }

    private ImportTypeEnum getImportType() {
        return ImportTypeEnum.PURCHASE_ORDER;
    }

    private boolean hasImportButton() {
        return true;
    }

}
