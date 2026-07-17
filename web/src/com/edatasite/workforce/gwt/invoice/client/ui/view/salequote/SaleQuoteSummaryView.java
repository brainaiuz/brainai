package com.edatasite.workforce.gwt.invoice.client.ui.view.salequote;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.PdfTemplateTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewAddFiledsCodeName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.InvoiceDynamicBankAccountListBox;
import com.edatasite.workforce.gwt.core.client.ui.lookup.InvoiceDynamicProjectLookupBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinksInterface;
import com.edatasite.workforce.gwt.core.client.ui.view.PDFTemplateSelector;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.ActivityQuickAddForm;
import com.edatasite.workforce.gwt.invoice.client.container.salequote.SaleQuoteViewSinksContainer;
import com.edatasite.workforce.gwt.invoice.client.rpc.*;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.edatasite.workforce.gwt.invoice.client.ui.view.InvoiceSummaryView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.RejectionReasonInvoices;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.InvoiceAdvancedFields;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.InvoiceAdvancedOptions;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.ProductsTable;
import com.edatasite.workforce.gwt.invoice.client.ui.view.shippingData.GdnAndGrnListNavBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 06.03.2009
 * Time: 16:14:04
 * To change this template use File | Settings | File Templates.
 */
public class SaleQuoteSummaryView extends InvoiceSummaryView implements HasLinksInterface {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private static final Integer PDF_VERSION_DEFAULT = 1;
    private static final Integer PDF_VERSION_PICK = 2;
    private static final String CONVERT_TO_INVOICE = "CONVERT_TO_INVOICE";

    private static final String CUSTOMER_PRE_PAYMENT = "CUSTOMER_PRE_PAYMENT";
    private static final String CONVERT_TO_ORDER = "CONVERT_TO_ORDER";
    private static final String CONVERT_TO_PROJECT = "CONVERT_TO_PROJECT";
    private static final String COMPANY_EXPENSE = "COMPANY_EXPENSE";
    private final Integer saleQuoteID;
    private WfmButton2 edit, reject, goToPickList;
    private final boolean isSalesOrder;
    private String sales = "";
    private SplitButtonItem packingSlip, pdfVersion, clientApprove, approveAndEmail;
    private boolean openOrApproveStatus = false;
    private FooterInformer link;
    private final ArrayList<SplitButtonItem> salesOrderSplitButtonItems = new ArrayList<>();
    private boolean processing = false;

    public SaleQuoteSummaryView(Integer saleQuoteID) {
        super("summary", saleQuote, SALE_QUOTE, Utils.hasPermission(ACCOUNTING_SALES_QUOTE_HISTORY_NOTES), Utils.hasPermission(ACCOUNTING_SALES_QUOTE_UPLOAD_FILES));
        this.saleQuoteID = saleQuoteID;
        isSalesOrder = false;
        property = new Property(propertyCode());
        sales = "saleQuote_";
    }

    public SaleQuoteSummaryView(Integer saleQuoteID, boolean isSalesOrder) {
        super("summary", saleOrder, SALE_ORDER, Utils.hasPermission(ACCOUNTING_SALES_ORDER_HISTORY_NOTES), Utils.hasPermission(ACCOUNTING_SALES_ORDER_UPLOAD_FILES));
        this.saleQuoteID = saleQuoteID;
        this.isSalesOrder = isSalesOrder;
        property = new Property(propertyCode());
        sales = isSalesOrder ? "saleOrder_" : "";
    }

    @Override
    protected LinkedHashMap<String, DynamicTableColumn> getColumnsMap(ColumnConfigs[] customColumns) {
        LinkedHashMap<String, DynamicTableColumn> columnsMap = new LinkedHashMap<>();

        if (customColumns != null && customColumns.length > 0) {

            DynamicTableColumn dynamicTableColumn;

            for (ColumnConfigs column : customColumns) {

                boolean isPixel = (column.getWidth() == null || column.getWidth() == 0);

                switch (column.getCode()) {
                    case ProductsTable.PRODUCT:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.itemName(), ProductsTable.PRODUCT, Utils.getColumnWidth(column.getWidth(), 200));
                        columnsMap.put(ProductsTable.PRODUCT, dynamicTableColumn);
                        break;
                    case ProductsTable.DESCRIPTION:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.description(), ProductsTable.DESCRIPTION, Utils.getColumnWidth(column.getWidth(), 250));
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.DESCRIPTION, dynamicTableColumn);
                        break;
                    case ProductsTable.QTY:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.qty(), ProductsTable.QTY, Utils.getColumnWidth(column.getWidth(), 75), Constants.RIGHT_ALIGN_CELL);
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.QTY, dynamicTableColumn);
                        //columnsMap.get(ProductsTable.QTY).setPixel(true);
                        break;
                    case ProductsTable.MEASUREMENT:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.measurement(), ProductsTable.MEASUREMENT, Utils.getColumnWidth(column.getWidth(), 60));
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.MEASUREMENT, dynamicTableColumn);
                        //columnsMap.get(ProductsTable.MEASUREMENT).setPixel(true);
                        break;
                    case ProductsTable.UNITPRICE:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.price(), ProductsTable.UNITPRICE, Utils.getColumnWidth(column.getWidth(), 75), Constants.RIGHT_ALIGN_CELL);
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.UNITPRICE, dynamicTableColumn);
                        //columnsMap.get(ProductsTable.UNITPRICE).setPixel(true);
                        break;
                    case ProductsTable.COMISSION:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.commission(), ProductsTable.COMISSION, Utils.getColumnWidth(column.getWidth(), 100));
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.COMISSION, dynamicTableColumn);
                        break;
                    case ProductsTable.DISCOUNT_AMT:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.discount(), ProductsTable.DISCOUNT_AMT, Utils.getColumnWidth(column.getWidth(), 75), Constants.RIGHT_ALIGN_CELL);
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.DISCOUNT_AMT, dynamicTableColumn);
                        //columnsMap.get(ProductsTable.DISCOUNT_AMT).setPixel(true);
                        break;
                    case ProductsTable.DOUBLE_DISCOUNT_AMT:
                        if (SALE_QUOTE.equals(viewType)) {
                            dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : accountingStrings.discount2(), ProductsTable.DOUBLE_DISCOUNT_AMT, Utils.getColumnWidth(column.getWidth(), 75), Constants.RIGHT_ALIGN_CELL);
                            dynamicTableColumn.setPixel(isPixel);
                            dynamicTableColumn.setForceWidthInPercent(!isPixel);
                            columnsMap.put(ProductsTable.DOUBLE_DISCOUNT_AMT, dynamicTableColumn);
                        }
                        break;
                    case ProductsTable.DEPARTMENT:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), ProductsTable.DEPARTMENT, Utils.getColumnWidth(column.getWidth(), 100));
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.DEPARTMENT, dynamicTableColumn);
                        break;
                    case ProductsTable.ACCOUNT:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.salesAccount(), ProductsTable.ACCOUNT, Utils.getColumnWidth(column.getWidth(), 100));
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.ACCOUNT, dynamicTableColumn);
                        break;
                    case ProductsTable.NET_AMT:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.netAmount(), ProductsTable.NET_AMT, Utils.getColumnWidth(column.getWidth(), 80), Constants.RIGHT_ALIGN_CELL);
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.NET_AMT, dynamicTableColumn);
                        //columnsMap.get(ProductsTable.NET_AMT).setPixel(true);
                        break;
                    case ProductsTable.TAX_LIST:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.taxRate(), ProductsTable.TAX_LIST, Utils.getColumnWidth(column.getWidth(), 100));
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.TAX_LIST, dynamicTableColumn);
                        break;
                    case ProductsTable.DOUBLE_TAX_LIST:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.taxRate(), ProductsTable.DOUBLE_TAX_LIST, Utils.getColumnWidth(column.getWidth(), 100));
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.DOUBLE_TAX_LIST, dynamicTableColumn);
                        break;
                    case ProductsTable.TOTAL_AMT:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.totalAmount(), ProductsTable.TOTAL_AMT, Utils.getColumnWidth(column.getWidth(), 100), Constants.RIGHT_ALIGN_CELL);
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.TOTAL_AMT, dynamicTableColumn);
                        //columnsMap.get(ProductsTable.TOTAL_AMT).setPixel(true);
                        break;
                    case ProductsTable.PROJECT:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : Property.get(Constants.PROJECT, wfmStrings.project()), ProductsTable.PROJECT, Utils.getColumnWidth(column.getWidth(), 100));
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.PROJECT, dynamicTableColumn);
                        break;
                    case ProductsTable.ATTACHMENT:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.attachment(), ProductsTable.ATTACHMENT, Utils.getColumnWidth(column.getWidth(), 100));
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.ATTACHMENT, dynamicTableColumn);
                        break;
                    default:
                        dynamicTableColumn = new DynamicTableColumn(column.getTitle(), column.getCode(), Utils.getColumnWidth(column.getWidth(), 100), column.isRequired());
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(column.getCode(), dynamicTableColumn);
                        columnsMap.get(column.getCode()).setPixel(true);
                        break;
                }
            }

            //Sales order special columns
            columnsMap.put(ProductsTable.DELIVERED_QTY, new DynamicTableColumn(crmStrings.delivered(), ProductsTable.DELIVERED_QTY, 75));
            columnsMap.get(ProductsTable.DELIVERED_QTY).setPixel(true);
        } else {
            columnsMap.put(ProductsTable.PRODUCT, new DynamicTableColumn(wfmStrings.itemName(), ProductsTable.PRODUCT, 200));
            columnsMap.put(ProductsTable.DESCRIPTION, new DynamicTableColumn(wfmStrings.description(), ProductsTable.DESCRIPTION, 250));

            columnsMap.put(ProductsTable.QTY, new DynamicTableColumn(wfmStrings.qty(), ProductsTable.QTY, 75, Constants.RIGHT_ALIGN_CELL));
            columnsMap.get(ProductsTable.QTY).setPixel(true);

            columnsMap.put(ProductsTable.UNITPRICE, new DynamicTableColumn(wfmStrings.price(), ProductsTable.UNITPRICE, 75, Constants.RIGHT_ALIGN_CELL));
            columnsMap.get(ProductsTable.UNITPRICE).setPixel(true);

            columnsMap.put(ProductsTable.DISCOUNT_AMT, new DynamicTableColumn(wfmStrings.discount(), ProductsTable.DISCOUNT_AMT, 75, Constants.RIGHT_ALIGN_CELL));
            columnsMap.get(ProductsTable.DISCOUNT_AMT).setPixel(true);

            columnsMap.put(ProductsTable.ACCOUNT, new DynamicTableColumn(wfmStrings.salesAccount(), ProductsTable.ACCOUNT, 100));

            columnsMap.put(ProductsTable.NET_AMT, new DynamicTableColumn(wfmStrings.netAmount(), ProductsTable.NET_AMT, 80, Constants.RIGHT_ALIGN_CELL));
            columnsMap.get(ProductsTable.NET_AMT).setPixel(true);

            columnsMap.put(ProductsTable.TAX_LIST, new DynamicTableColumn(wfmStrings.taxRate(), ProductsTable.TAX_LIST, 100));
            columnsMap.put(ProductsTable.DELIVERED_QTY, new DynamicTableColumn(crmStrings.delivered(), ProductsTable.DELIVERED_QTY, 75));
            columnsMap.get(ProductsTable.DELIVERED_QTY).setPixel(true);
        }

        return columnsMap;
    }

    protected void initializeInvoiceData() {
        quoteService.getQuoteSummaryData(saleQuoteID, new AbstractAsyncCallback<NewInvoice>() {
            public void success(NewInvoice result) {
                if (result.getIntroduction() != null && !result.getIntroduction().isEmpty()) {
                    drawIntroductionPanel(result.getIntroduction());
                }
                if (result.getPaymentInstruction() != null && !result.getPaymentInstruction().isEmpty()) {
                    drawInstructionPanel(result.getPaymentInstruction());
                }
                if (result.getAmazonLink() != null) {
                    salesOrderSplitButtonItems.add(new SplitButtonItem(SEND_BY_WHATSAPP, wfmStrings.sendByWhatsApp(), () -> {
                        CrmAccountItem profileItem = new CrmAccountItem();
                        profileItem.setAmazonLink(result.getAmazonLink());
                        profileItem.setObjectId(result.getID());
                        profileItem.setSaleType("quote");
                        AccountingService.App.get().shortenLink(result.getAmazonLink(), profileItem, new AsyncCallback<String>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                throw new RuntimeException("Failed to shorten link or save link: {} ", caught);
                            }

                            @Override
                            public void onSuccess(String link) {
                                profileItem.setShortLink(link);
                                new ActivityQuickAddForm(Appointment.WHATSAPP, profileItem, saleQuoteID, RelationItem.newEventRelation(RelationItem.TYPE_SALESINVOICE, saleQuoteID, result.getInvoiceNumber()));
                            }
                        });
                    }));
                }
                initializeFormData(invoiceData = result);
                // Due Amount of Sales Order
                HTML label = new HTML(wfmStrings.dueAmount());
                HTML value = new HTML(utils.formatPrice(invoiceData.getOrderDueAmount()));
                totalsTable.setDueAmount(label, value);

                AtomicBoolean firstClick = new AtomicBoolean(true);
                link.addClickHandler(event -> {
                    if (firstClick.get()) {
                        getLinkingUtil().getAddLinkSideNavBox();
                        getLinkingUtil().getAddLinkSideNavBox().setSelectedRelations(result.getRelations(), false);
                        firstClick.set(false);
                    } else {
                        getLinkingUtil().getAddLinkSideNavBox().show();
                    }

                });

                link.setBadgeCount(result.getRelations().size());
            }
        });
    }

    protected void setEnableButtons(boolean b) {
        if (Utils.hasRole(CLIENT)) {
            if (openOrApproveStatus) {
                if (approveButton.getItemsMap() != null && approveButton.getItemsMap().size() > 0) {
                    approveButton.showOrHideMenuItem(APPROVE, b);
                    //approve.setEnabled(b);
                }
                if (approveButton.getItemsMap() != null && approveButton.getItemsMap().size() > 0) {
                    approveButton.showOrHideMenuItem(APPROVE_AND_SEND, b);
                    //approveAndSendToClient.setEnabled(b);
                }
            } else {
                if (approveButton.getItemsMap() != null && approveButton.getItemsMap().size() > 0) {
                    approveButton.showOrHideMenuItem(APPROVE, b);
                }
                if (approveButton.getItemsMap() != null && approveButton.getItemsMap().size() > 0) {
                    approveButton.showOrHideMenuItem(APPROVE_AND_SEND, b);
                }
            }
        } else {
            if (approveButton.getItemsMap() != null && approveButton.getItemsMap().size() > 0) {
                approveButton.showOrHideMenuItem(APPROVE, b);
            }
            if (approveButton.getItemsMap() != null && approveButton.getItemsMap().size() > 0) {
                approveButton.showOrHideMenuItem(APPROVE_AND_SEND, b);
            }
        }
        if (reject != null) {
            reject.setEnabled(b);
        }
        if (edit != null) {
            edit.setEnabled(b);
        }
        if (goToPickList != null) {
            goToPickList.setEnabled(b);
        }
        if (optionsSplitButton != null) {
            optionsSplitButton.showOrHideMenuItem(CONVERT_TO_ORDER, b);
        }
    }

    protected void initializeButtons() {
        ArrayList<SplitButtonItem> splitButtonItems = new ArrayList<>();
        ArrayList<SplitButtonItem> pdfButtonItems = new ArrayList<>();
        ArrayList<SplitButtonItem> optionsCommandSubItems = new ArrayList<>();

        String status = invoiceData.getStatusCode();
        if (!PENDING.equals(status)) {
            final boolean hasAccessToChange = !Utils.isLockCompletedProjecItems() || (Utils.isLockCompletedProjecItems() && !PS_CLOSED.equals(invoiceData.getProjectStatusCode()));
            boolean isClient = Utils.hasRole(CLIENT);

            if (Utils.hasRoles(DR, ADMIN, ACCOUNTANT, PM, TL, MEM)) {

                if (invoiceData.getAllocateComissionItems() != null && invoiceData.getAllocateComissionItems().size() > 0) {
                    int row = 0;
                    FlexTable splitComissionTable = (FlexTable) widgetsMap.get(INPUT_SPLIT_COMISSION_TABLE);
                    splitComissionTable.getColumnFormatter().setWidth(0, "110px");
                    splitComissionTable.getColumnFormatter().setWidth(1, "70px");
                    splitComissionTable.setWidget(row, 0, new HTML("<b>" + accountingStrings.splitComissionTable() + "</b>"));
                    splitComissionTable.getFlexCellFormatter().setColSpan(row, 0, 2);
                    splitComissionTable.getFlexCellFormatter().setHorizontalAlignment(row++, 0, HasHorizontalAlignment.ALIGN_CENTER);

                    HTML salesManLabel = new HTML("<b>" + accountingStrings.salesPeople() + "</b>");
                    HTML amountLabel = new HTML("<b>" + wfmStrings.amount() + "</b>");
                    salesManLabel.setStyleName(STYLE_TITLE_LABEL);
                    amountLabel.setStyleName(STYLE_TITLE_LABEL);
                    splitComissionTable.setWidget(row, 0, salesManLabel);
                    splitComissionTable.setWidget(row, 1, amountLabel);
                    splitComissionTable.getFlexCellFormatter().setHorizontalAlignment(row++, 1, HasHorizontalAlignment.ALIGN_RIGHT);
                    for (AllocateComissionItem item : invoiceData.getAllocateComissionItems()) {
                        splitComissionTable.setWidget(row, 0, new Label(item.getSalesMan().getName()));
                        splitComissionTable.setWidget(row, 1, new Label(AccountingUtils.get().formatPrice(item.getAllocateTotal())));
                        splitComissionTable.getFlexCellFormatter().setHorizontalAlignment(row++, 1, HasHorizontalAlignment.ALIGN_RIGHT);
                    }
                }
            }

            if (OPEN.equals(status) || APPROVE.equals(status)) {
                openOrApproveStatus = true;
                if (Utils.hasPermission(SALES_QUOTE_CLIENT_APPROVE)) {
                    clientApprove = new SplitButtonItem(APPROVE, isClient ? wfmStrings.approve() : accountingStrings.clientApprove(), () -> {
                        changeQuoteStatus(CLIENT_APPROVE, false);
                    }, true);
                    clientApprove.ensureDebugId(sales + "clientApproveItem");
                    splitButtonItems.add(clientApprove);
                }

                SplitButtonItem reject = new SplitButtonItem(REJECT, isClient ? wfmStrings.reject() : accountingStrings.clientReject(), () -> setUpReasonBox(REJECT));
                splitButtonItems.add(reject);
            }

            if (!isClient) {
                boolean isManagerApproval = invoiceData.getCurrentApproverSelectItem() != null;
                boolean isManager = invoiceData.isCurrentApprover(Utils.getUserID());
                boolean isEditable, isFullAccessEditable, hasEditPermission;
                boolean isDraftOrRejected = status.equals(DRAFT) || status.equals(REJECT) || status.equals(MANAGER_REJECT);

                if (!isSalesOrder) {
                    isEditable = (Utils.hasPermission(ACCOUNTING_SALES_QUOTE_EDIT) || invoiceData.isSubmitter(Utils.getUserID())) && ((isDraftOrRejected) ||
                            (!isManagerApproval && (isDraftOrRejected || APPROVE.equals(status) || CLIENT_APPROVE.equals(status) || OPEN.equals(status) || OVER_DUE.equals(status))) ||
                            (isManagerApproval && (SUBMITTED_TO_MANAGER.equals(status) || APPROVE.equals(status) || CLIENT_APPROVE.equals(status) || OPEN.equals(status))) ||
                            (isManagerApproval && Utils.getUserID().equals(invoiceData.getCurrentApproverSelectItem().getId()) && DRAFT.equals(status)));
                    isFullAccessEditable = Utils.hasPermission(ACCOUNTING_SALES_QUOTE_FULL_EDIT_ACCESS) && ((!isManagerApproval && (isDraftOrRejected || APPROVE.equals(status) || CLIENT_APPROVE.equals(status) || OPEN.equals(status) || OVER_DUE.equals(status))) ||
                            (isManagerApproval && (SUBMITTED_TO_MANAGER.equals(status) || APPROVE.equals(status) || CLIENT_APPROVE.equals(status) || OPEN.equals(status))));
                    hasEditPermission = ((Utils.hasPermission(ACCOUNTING_SALES_QUOTE_EDIT) && isEditable) || (Utils.hasPermission(ACCOUNTING_SALES_QUOTE_FULL_EDIT_ACCESS) && isFullAccessEditable));
                } else {
                    isEditable = (SALE_ORDER.equals(status) || PARTIAL_INVOICED.equals(status) || PICKED.equals(status) || PACKED.equals(status) ||
                            INVOICED.equals(status) || PARTIAL_SHIPPED.equals(status) || SHIPPED.equals(status));
                    isFullAccessEditable = SALE_ORDER.equals(status) || PICKED.equals(status) || PACKED.equals(status) || INVOICED.equals(status) ||
                            PARTIAL_SHIPPED.equals(status) || SHIPPED.equals(status);
                    hasEditPermission = ((Utils.hasPermission(ACCOUNTING_SALES_ORDER_EDIT) && isEditable) || (Utils.hasPermission(ACCOUNTING_SALES_ORDER_FULL_EDIT_ACCESS) && isFullAccessEditable));
                }

                if (Utils.hasPermission(SALES_ORDER_APPROVE_EMAIL_SEND) && !isDraftOrRejected && !CONVERTED.equals(status) && !INVOICED.equals(status)) {
                    if (isSalesOrder) {
                        salesOrderSplitButtonItems.add(new SplitButtonItem(EMAIL, wfmStrings.sendEmail(), () -> {
                            sendToClient(SALES_ORDER_CATEGORY);
                        }));
                        if (salesOrderSplitButtonItems != null && salesOrderSplitButtonItems.get(0) != null
                                && salesOrderSplitButtonItems.get(0).getKey().equals(SEND_BY_WHATSAPP)) {
                            Collections.swap(salesOrderSplitButtonItems, 0, 1);
                        }
                    } else {
                        splitButtonItems.add(new SplitButtonItem(EMAIL, wfmStrings.sendEmail(), () -> {
                            sendToClient(SALES_QUOTE_CATEGORY);
                        }));
                    }
                }

                if (isSalesOrder && isManagerApproval && SUBMITTED_TO_MANAGER.equals(status) && (isManager)) {
                    salesOrderSplitButtonItems.add(new SplitButtonItem(APPROVE, wfmStrings.approve(), () -> changeQuoteStatus(APPROVE, false), true));
                    if (invoiceData.isApproveForAll()) {
                        salesOrderSplitButtonItems.add(new SplitButtonItem(APPROVE_ALL_BTN, wfmStrings.approveForAll(), () -> changeQuoteStatus(APPROVE, true)));
                    }
                    salesOrderSplitButtonItems.add(new SplitButtonItem(MANAGER_REJECT, wfmStrings.reject(), () -> setUpReasonBox(MANAGER_REJECT)));
                }
                if (isSalesOrder && APPROVE.equals(status)) {
                    salesOrderSplitButtonItems.add(new SplitButtonItem(SALE_ORDER, wfmStrings.saveAndApprove(), () -> changeQuoteStatus(SALE_ORDER, false)));
                }
                boolean hasInvoice = INVOICED.equals(status) || PARTIAL_INVOICED.equals(status) || CONVERTED.equals(status) && (invoiceData.getInvoicedItems() != null && invoiceData.getInvoicedItems().length > 0);
                boolean canEditSalesOrder = isSalesOrder && hasInvoice && Utils.hasGenericAccess(GenericSettingsEnum.CAN_EDIT_SALES_ORDER_IF_HAS_INVOICE_WORKAROUND);

                if (hasAccessToChange && hasEditPermission && ((invoiceData.isProgressInvoicing() && !invoiceData.getInvoicedItemsExist()) || !invoiceData.isProgressInvoicing())) {
                    optionsCommandSubItems.add(new SplitButtonItem("EDIT_OPTION", wfmStrings.edit(), () -> {
                        String editPath = isSalesOrder ? "saleorder|edit/" : "salequote|edit/";
                        if (isSalesOrder && hasInvoice && !canEditSalesOrder) {
                            Info.show("Please delete the invoice to edit the sales order.", Info.Type.INFO);
                        } else {
                            closeTab();
                            SinksContainerFactory.entryPoint.onHistoryChanged(editPath + saleQuoteID, invoiceData.getInvoiceNumber());
                        }
                    }));
                }

                if (Utils.hasPermission(ACCOUNTING_SALES_ORDER_PICKLIST) && invoiceData.getPickListID() != null && !invoiceData.getStatusCode().equals(INVOICE_STATUS_CLOSED)
                        && (isSalesOrder
                        || (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_SALES_QUOTE_PICKLIST))) &&
                        (INVOICED.equals(invoiceData.getStatusCode())
                                || SALE_ORDER.equals(invoiceData.getStatusCode())
                                || PICKED.equals(invoiceData.getStatusCode())
                                || PACKED.equals(invoiceData.getStatusCode())
                                || PARTIAL_SHIPPED.equals(invoiceData.getStatusCode())
                                || SHIPPED.equals(invoiceData.getStatusCode())
                                || PARTIAL_INVOICED.equals(invoiceData.getStatusCode())
                                || CLIENT_APPROVE.equals(invoiceData.getStatusCode()))
                ) {
                    SplitButtonItem pickListItem = new SplitButtonItem("GO_TO_PICK_LIST", accountingStrings.goToPickList(), () -> {
                        if (Utils.hasGenericAccess(GenericSettingsEnum.ALMADAR_PRODUCT_SERIAL_ENABLED)) {
                            quoteService.checkForCreditLimit(saleQuoteID, new AbstractAsyncCallback<SaveResult>() {
                                public void failure(Throwable caught) {
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                public void success(SaveResult result) {
                                    if (result.getExceededCreditLimit()) {
                                        WfmMessageBox creditLimitExceedMessage = new WfmMessageBox(IconEnum.WARN, Action.OK);
                                        String message = accountingMessages.creditLimitPicklistMessage(result.getMessage(), AccountingUtils.get().formatPrice(result.getCreditLimit()), AccountingUtils.get().formatPrice(result.getRemainingBalance()));
                                        creditLimitExceedMessage.setMessage(message);
                                        creditLimitExceedMessage.open();
                                    } else {
                                        goTo("picklist|edit/" + invoiceData.getPickListID(), "Picklist: " + invoiceData.getInvoiceNumber());
                                    }
                                }
                            });
                        } else {
                            goTo("picklist|edit/" + invoiceData.getPickListID(), "Picklist: " + invoiceData.getInvoiceNumber());
                        }
                    });

                    // if progress invoicing is enabled, we should not show the picklist button
                    if (hasAccessToChange && !invoiceData.isProgressInvoicing()) {
                        optionsCommandSubItems.add(pickListItem);
                    }
                }

                if (((Utils.isAccounting() && hasAccessToChange && Utils.hasPermission(CONVERT_SALE_QUOTE_TO_SALE_ORDER) && CLIENT_APPROVE.equals(status))
                        || (!Utils.isAccounting() && Utils.hasPermission(CONVERT_SALE_QUOTE_TO_SALE_ORDER) && CLIENT_APPROVE.equals(status))) && !Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_SALES_QUOTE_PICKLIST)) {
                    SplitButtonItem convertToSaleOrderItem = new SplitButtonItem(CONVERT_TO_ORDER, Property.get(SALE_ORDER_CODE, wfmStrings.convertToo(), wfmStrings.order()), () -> {
                        if (processing) {
                            return;
                        }
                        processing = true;
                        setEnableButtons(false);
                        quoteService.convertToSaleOrder(saleQuoteID, new AbstractAsyncCallback<SelectItem>() {
                            public void failure(Throwable caught) {
                                processing = false;
                                setEnableButtons(true);
                                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                            }

                            public void success(final SelectItem result) {
                                Info.show(accountingStrings.convertedSuccessfullyQuote(), Info.Type.INFO);
                                closeTab();
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEQUOTE_CONVERTED_TO_SALEORDER, result.getOrderId(), SaleQuoteSummaryView.this);
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEORDER_SHIPPED, null, null);
                                goTo(SALE_ORDER_CODE + "|summary/" + result.getId(), result.getNumber());
                            }
                        });
                    });
                    optionsCommandSubItems.add(convertToSaleOrderItem);
                }
                if (Utils.hasPermission(PermissionConstants.ACCOUNTING_PREPAYMENT_ADD) && isSalesOrder && !status.equals(SUBMITTED_TO_MANAGER) && !status.equals(DRAFT) && !status.equals(REJECTED)) {
                    SplitButtonItem costumerPrePayment = new SplitButtonItem(CUSTOMER_PRE_PAYMENT, Property.get(Constants.CUSTOMER_PREPAYMENT, accountingStrings.addcustomerPrepayment()),
                            () -> redirectProperly("prepayment|add/copyFromOrder/" + invoiceData.getID(), ""));
                    optionsCommandSubItems.add(costumerPrePayment);
                }
                if (isSalesOrder && Utils.hasPermission(ACCOUNTING_COMPANY_EXPENSE_ADD) && (!status.equals(SUBMITTED) && !status.equals(DRAFT))) {
                    SplitButtonItem addCompanyExpense = new SplitButtonItem(COMPANY_EXPENSE, wfmStrings.companyExpense(),
                            () -> goTo("expenseReports|add/add/" + COMPANY_EXPENSE + "/" + invoiceData.getID()));
                    optionsCommandSubItems.add(addCompanyExpense);
                }

                if ((!isSalesOrder && Utils.hasPermission(CONVERT_SALE_QUOTE_TO_SALE_INVOICE) || Utils.hasPermission(CONVERT_SALE_ORDER_TO_SALE_INVOICE) && isSalesOrder)
                        && (!INVOICE_STATUS_CLOSED.equals(status) || INVOICE_STATUS_CLOSED.equals(status) && !invoiceData.isAllGdnInvoiced())
                        && hasAccessToChange
                        && (SALE_ORDER.equals(status)
                        || PICKED.equals(status)
                        || PARTIAL_INVOICED.equals(status)
                        || PACKED.equals(status)
                        || PARTIAL_SHIPPED.equals(status)
                        || SHIPPED.equals(status)
                        || CLIENT_APPROVE.equals(status))) {

                    SplitButtonItem convertToSaleInvoiceItem = new SplitButtonItem(CONVERT_TO_INVOICE, Property.getShortName(Constants.SALE_INVOICE, wfmStrings.convertToo(), accountingStrings.invoice()), () -> {

                        if (invoiceData.isProgressInvoicing()) {
                            quoteService.getQuote(invoiceData.getID(), null, new AsyncCallback<NewInvoice>() {
                                @Override
                                public void onFailure(Throwable throwable) {

                                }

                                @Override
                                public void onSuccess(NewInvoice newInvoice) {
                                    if (newInvoice.getProgressInvoicingType() != null) {
                                        redirectProperly("progressinvoicing|" + newInvoice.getProgressInvoicingType() + "/" + newInvoice.getID() + "/" + false + "/" + newInvoice.getProgressInvoicingType(), "");
                                    } else {
                                        redirectProperly("progressinvoicing|" + AccountingConstants.BY_AMOUNT + "/" + newInvoice.getID() + "/" + false, "");
                                    }
                                }
                            });

                        } else {
                            closeTab();
                            goTo("saleinvoice|add/add/convertToInvoice/" + invoiceData.getID(), "");
                        }
                    });
                    optionsCommandSubItems.add(convertToSaleInvoiceItem);
                }

                if (!isSalesOrder && !INVOICE_STATUS_CLOSED.equals(status) && hasAccessToChange && isManagerApproval && SUBMITTED_TO_MANAGER.equals(status) && (isManager)) {
                    splitButtonItems.add(new SplitButtonItem(APPROVE, wfmStrings.approve(), () -> {
                        setEnableButtons(false);

                        if (Utils.isDoubleMessageEnable()) {
                            WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, wfmStrings.doYouWantToSaveChanges(), new CloseHandler() {
                                @Override
                                public void onSubmit() {
                                    changeStatus();
                                }

                                @Override
                                public void onCancel() {
                                    setEnableButtons(true);
                                }
                            });
                            wfmMessageBox.setTitle(wfmStrings.confirmation());
                            wfmMessageBox.open();
                        } else {
                            changeStatus();
                        }

                    }, true));

                    splitButtonItems.add(new SplitButtonItem(MANAGER_REJECT, wfmStrings.reject(), () -> setUpReasonBox(MANAGER_REJECT)));
                }
            }

            if (Utils.hasPermission(PermissionConstants.PM_MAIN_MENU) && hasAccessToChange
                    && !invoiceData.isConvertedToProject() && invoiceData.getRelatedProjectID() == null && Utils.hasPermission(PermissionConstants.ACCOUNTING_CONVERT_TO_PROJECT)
                    && (CLIENT_APPROVE.equals(status) || SALE_ORDER.equals(status) || PARTIAL_INVOICED.equals(status) || PICKED.equals(status) || PACKED.equals(status) || SHIPPED.equals(status) || CONVERTED.equals(status))) {
                SplitButtonItem convertToProjectItem = new SplitButtonItem(CONVERT_TO_PROJECT, Property.get(Constants.PROJECT, wfmStrings.convertToo(), Property.get(Constants.PROJECT, wfmStrings.project())), () -> {
                    LoadingPanel.loading(true);
                    QuoteService.App.get().convertToProject(invoiceData.getID(), new AsyncCallback<Integer>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void onSuccess(Integer projectID) {
                            LoadingPanel.loading(false);
                            closeTab();
                            Info.show(property.getSingular(accountingMessages.salesQuoteConvertedToProject(), wfmStrings.salesQuote()), Info.Type.INFO);
                            if (projectID != null) {
                                String editProject = GWT.getHostPageBaseURL() + "ProjectManagement.html#" + Constants.PROJECT + "|edit/" + projectID;
                                Window.open(editProject, "_blank", "");
                            }
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEQUOTE_ADDED, projectID, SaleQuoteSummaryView.this);
                        }
                    });
                });
                optionsCommandSubItems.add(convertToProjectItem);
            }

            if (hasAccessToChange && !isSalesOrder) {
                approveButton.addItemList(splitButtonItems);
            } else if (isSalesOrder) {
                approveButton.addItemList(salesOrderSplitButtonItems);
            }

            if (!optionsCommandSubItems.isEmpty()) {

                if (optionsCommandSubItems.size() > 1) {
                    optionsCommandSubItems.add(new SplitButtonItem("OPTIONS", wfmStrings.options(), null, true));
                }
                optionsSplitButton.addItemList(optionsCommandSubItems);
            }
            Integer defaultTemplateId = null;

            if (invoiceData != null
                    && invoiceData.getPdfTemplateList() != null
                    && invoiceData.getPdfTemplateList().getItems() != null) {
                invoiceData.getPdfTemplateList().getItems();
                for (SelectItem pdfItem : invoiceData.getPdfTemplateList().getItems()) {

                    if (pdfItem.isDefaultSelected()) {
                        defaultTemplateId = pdfItem.getId();
                    }
                    pdfButtonItems.add(new SplitButtonItem("PDF_TEMPLATE_" + pdfItem.getId(), pdfItem.getName(), () -> pdfVersion(htmlPanel, PDF_VERSION_DEFAULT, pdfItem.getId())));
                }
            }
            Integer finalDefaultTemplateId = defaultTemplateId;
            pdfVersion = new SplitButtonItem(PDF_VERSION, wfmStrings.pdfVersion(), () -> pdfVersion(htmlPanel, PDF_VERSION_DEFAULT, finalDefaultTemplateId), true);
            pdfVersion.ensureDebugId(sales + "pdfVersionItem");
            pdfButtonItems.add(pdfVersion);

            if (isSalesOrder) {
                packingSlip = new SplitButtonItem(PACKING_SLIP, accountingStrings.packingSlip(), () -> pdfVersion(htmlPanel, PDF_VERSION_PICK, finalDefaultTemplateId), false);
                packingSlip.ensureDebugId(sales + "packingSlip");
                if (Utils.hasPermission(PermissionConstants.ACCOUNTING_PACKING_LIST)) {
                    pdfButtonItems.add(packingSlip);
                }
            }

            if (invoiceData != null
                    && invoiceData.getInvoicedItems() != null
                    && invoiceData.getInvoicedItems().length > 0
                    && invoiceData.isProgressInvoicing()
                    && (AccountingConstants.BY_MULTI_PROGRESS.equals(invoiceData.getProgressInvoicingType()) || AccountingConstants.BY_CUSTOM_PERCENTAGE.equals(invoiceData.getProgressInvoicingType()))) {
                pdfButtonItems.add(new SplitButtonItem("PROGRESS_INVOICING", wfmStrings.progressInvoicing(), () -> generateProgressInvoicingPDF(htmlPanel)));
            }
            if (Utils.hasRole(ADMIN)) {
                pdfButtonItems.add(new SplitButtonItem("PDF_CUSTOMIZATION", wfmStrings.customize(), () -> Utils.openURL(GWT.getHostPageBaseURL() + "Settings.html#pdftemplate|summary/null/" + (isSalesOrder ? PdfTemplateTypeEnum.SALES_ORDER.name() : PdfTemplateTypeEnum.SALES_QUOTE.name()))));
            }

            if ((isSalesOrder && (Utils.hasPermission(PermissionConstants.ACCOUNTING_SALES_ORDER_PDF) || Utils.hasPermission(PermissionConstants.CRM_SALES_ORDER_PDF))) ||
                    (!isSalesOrder && (Utils.hasPermission(PermissionConstants.ACCOUNTING_SALES_QUOTE_PDF) || Utils.hasPermission(PermissionConstants.CRM_SALES_QUOTE_PDF)))) {
                if (!pdfButtonItems.isEmpty()) {
                    printPdfSplitButton.addItemList(pdfButtonItems);
                }
            }

            WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALES_QUOTE_TAB_CLOSE, SaleQuoteSummaryView.this, (sender, args) -> {
                if (args != null && args instanceof SaleQuoteViewSinksContainer) {
                    String viewType = ((SaleQuoteViewSinksContainer) args).getDescription();
                    if (viewType != null) {
                        if (salesQuoteView != null && salesQuoteView.equals(viewType)) {
                            goTo("accounting|salequote");
                        } else if (salesOrderView != null && salesOrderView.equals(viewType)) {
                            goTo("accounting|SALE_ORDER");
                        }
                    }
                }
            });
        }
    }

    private void changeQuoteStatus(String statusCode, boolean hasApproveForAll) {
        setEnableButtons(false);
        LoadingPanel.loading(true);
        quoteService.changeQuoteStatus(saleQuoteID, statusCode, null, hasApproveForAll, new AbstractAsyncCallback() {
            public void failure(Throwable caught) {
                setEnableButtons(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Object result) {
                setEnableButtons(true);
                LoadingPanel.loading(false);
                Info.show(property.getShort(wfmStrings.messSuccessfullyApproved(), property.getShort(accountingStrings.sq(), accountingStrings.quote())), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEQUOTE_ADDED, result, SaleQuoteSummaryView.this);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEORDER_APPROVAL, result, SaleQuoteSummaryView.this);
                closeTab();
                if (CLIENT_APPROVE.equals(statusCode)) {
                    goTo(Constants.SALE_QUOTE + "|summary/" + saleQuoteID, invoiceData.getInvoiceNumber());
                }
            }
        });
    }

    private void redirectProperly(String url, String tabName) {
        if (Utils.isAccounting()) {
            goTo(url, tabName);
        } else {
            Utils.openURL(GWT.getHostPageBaseURL() + "Accounting.html#" + url);
        }
    }

    private void changeStatus() {
        if (Utils.hasGenericAccess(GenericSettingsEnum.RESERVE_QUOTE_ITEM_ENABLE)) {
            ArrayList<QuantityItem> quantityItems = new ArrayList<>();
            for (NewInvoiceItem item : invoiceData.getItems()) {
                if (item.getItemID() != null) {
                    QuantityItem quantityItem = new QuantityItem();
                    quantityItem.setId(item.getItemID());
                    quantityItem.setQuantity(item.getQuantity());
                    quantityItems.add(quantityItem);
                }
            }

            quoteService.validateItemsInStock(quantityItems.toArray(new QuantityItem[]{}), invoiceData.getID(), invoiceData.getInvoiceDate(), invoiceData.getDueDate(), new AsyncCallback<String[]>() {
                @Override
                public void onFailure(Throwable throwable) {
                    setEnableButtons(true);
                }

                @Override
                public void onSuccess(String[] items) {
                    if (items.length > 0) {
                        setEnableButtons(true);
                        if (invoiceData.getCurrentApproverSelectItem() == null) {
                            alertStockItemsMessage2(items);
                        } else {
                            alertStockItemsMessage(items);
                        }
                    } else {
                        changeQuoteStatus(APPROVE, false);
//                        quoteService.approveQuote(saleQuoteID, new AbstractAsyncCallback() {
//                            public void failure(Throwable caught) {
//                                setEnableButtons(true);
//                                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
//                            }
//
//                            public void success(Object result) {
//                                setEnableButtons(true);
//                                Info.show(property.getSingular(accountingStrings.approvedSuccessfullyQuote(), accountingStrings.quote()), Info.Type.INFO);
//                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEQUOTE_ADDED, result, SaleQuoteSummaryView.this);
//                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEORDER_APPROVAL, result, SaleQuoteSummaryView.this);
//                                closeTab("accounting|salequote");
//                            }
//                        });
                    }
                }
            });
        } else {
            changeQuoteStatus(APPROVE, false);
//            quoteService.approveQuote(saleQuoteID, new AbstractAsyncCallback() {
//                public void failure(Throwable caught) {
//                    setEnableButtons(true);
//                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
//                }
//
//                public void success(Object result) {
//                    setEnableButtons(true);
//                    Info.show(property.getSingular(accountingStrings.approvedSuccessfullyQuote(), accountingStrings.quote()), Info.Type.INFO);
//                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEQUOTE_ADDED, result, SaleQuoteSummaryView.this);
//                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEORDER_APPROVAL, result, SaleQuoteSummaryView.this);
//                    closeTab("accounting|salequote");
//                }
//            });
        }

    }

    @Override
    protected void initializeSpecificWidgets() {

        if (widgetsMap.get(INPUT_NUMBER) != null) {
            FormGroup numberField = (FormGroup) widgetsMap.get(INPUT_NUMBER);
            numberField.setLabel(isSalesOrder ? (property.getShortForNumber(wfmStrings.orderNumber1())) :
                    (property.getShortForNumber(wfmStrings.quoteNumber())));
        }

        if (invoiceData.isProgressInvoicing()) {
            systemCustomFieldsMap.put(INPUT_PROGRESS_INVOICING, new HTML(wfmStrings.progressInvoicing()));
        } else {
            systemCustomFieldsMap.put(INPUT_PROGRESS_INVOICING, new HTML(""));
        }

        /**
         * SQ manager field initialization
         */
        if (!isSalesOrder) {
            widgetsMap.put(INPUT_MANAGER, new FormGroup(wfmStrings.manager(), getWidgetAsFormControl(invoiceData.getCurrentApproverSelectItem() != null ? invoiceData.getCurrentApproverSelectItem().getName() : "")));
        }

        if (widgetsMap.get(INPUT_DATE) != null) {
            FormGroup dateField = (FormGroup) widgetsMap.get(INPUT_DATE);
            dateField.setLabel(wfmStrings.date());
        }

        //DUE_DATE FIELD LABEL
        if (widgetsMap.get(INPUT_DUE_DATE) != null) {
            FormGroup dueDateField = (FormGroup) widgetsMap.get(INPUT_DUE_DATE);
            dueDateField.setLabel(invoiceData.getInvoiceTermsItem() != null ? wfmStrings.terms() : accountingStrings.validDate());
        }
        String approverName = invoiceData.getCurrentApproverSelectItem() != null
                ? invoiceData.getCurrentApproverSelectItem().getName()
                : "";
        widgetsMap.put(INPUT_MANAGER, new GColumn(GColumnEnum.COL_6, new FormGroup(wfmStrings.manager(), getWidgetAsFormControl(approverName))));
    }

    @Override
    protected InvoiceAdvancedOptions createAdvancedOptions() {
        return new InvoiceAdvancedOptions(new InvoiceAdvancedFields() {
            @Override
            public List<Widget> getOptionWidgets() {
                List<Widget> result = new ArrayList<>();

                if (!Utils.hasRole(CLIENT)) {
                    result.add(new InvoiceDynamicBankAccountListBox(saleQuoteID, invoiceData.getBankAccount(), viewType));
                }
                if (!Utils.isProjectInLineItemEnable() && Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
                    result.add(new InvoiceDynamicProjectLookupBox(saleQuoteID, invoiceData.getRelatedProject(), RECEIVABLE, viewType, invoiceData.getClientID()));
                }
                //PO number field
//                result.add(new FormGroup(accountingStrings.poNumber(), getWidgetAsFormControl(!Utils.isNullOrEmpty(invoiceData.getPoNumber()) ? invoiceData.getPoNumber() : "")));
                //Price level
                if (invoiceData.getPriceLevel() != null) {
                    result.add(new FormGroup(accountingStrings.relatedPriceLevel(), getWidgetAsFormControl(invoiceData.getPriceLevel() != null
                            ? invoiceData.getPriceLevel().getName()
                            : "")));
                }
                return result;
            }
        });
    }

    @Override
    protected void loadTotals() {
        super.loadTotals();

        if (!invoiceData.isMultiQuoteConvertEnabled() && invoiceData.isProgressInvoicing() && invoiceData.getInvoicedAmount() != null && (customItemColumns == null || customItemColumns != null && customItemColumns.length == 0 || customItemColumns != null && customItemColumns.length > 0 && columnsMap.get(ProductsTable.UNITPRICE) != null)) {
            totalsTable.addPaidItem(new HTML(accountingStrings.invoiceAmount()), new HTML(utils.formatPrice(invoiceData.getInvoicedAmount())));
            totalsTable.setDueAmount(new HTML(Constants.INVOICE_STATUS_CLOSED.equals(invoiceData.getStatusCode()) ? accountingStrings.closedAmount() : accountingStrings.remainingBalance()), new HTML(utils.formatPrice(invoiceData.getTotalInInvoiceCurrency().subtract(invoiceData.getInvoicedAmount()))));
        }
    }

    @Override
    protected Integer getUploadFolderType() {
        return F_SALE_QUOTE;
    }

    protected void pdfVersion(final HTMLPanel hp, final Integer pdftype, Integer templateID) {
        if (PDF_VERSION_PICK.equals(pdftype)) {
            new PDFTemplateSelector(SO_PACKING_SLIP, new ExtendedCommand() {
                @Override
                public void execute(Integer id) {
                    generatePDF(hp, pdftype, id);
                }
            });
        } else {
            generatePDF(hp, 1, templateID);
        }
    }

    private void generatePDF(HTMLPanel hp, Integer pdftype, Integer pdfTemplateID) {
        String pdfURL = null;
        InvoiceQuoteRequestObject requestObject = new InvoiceQuoteRequestObject(saleQuoteID, pdfTemplateID, null);
        if (isSalesOrder && (pdftype == 1)) {
            pdfURL = CommandConstants.PDF_URL + "/savedSaleOrderViewPDFHandler";
        } else if (PDF_VERSION_PICK.equals(2) && isSalesOrder) {
            pdfURL = CommandConstants.PDF_URL + "/savedSalesOrderPackingSlipPDFHandler";
        } else {
            pdfURL = CommandConstants.PDF_URL + "/savedSaleQuoteViewPDFHandler";
        }
        HashMap<String, String> parametrs = requestObject.getRequestParams();
        Utils.sendPDFOrExcelRequest(hp, pdfURL, parametrs, "_blank");
    }

    private void generateProgressInvoicingPDF(Panel hp) {
        String pdfURL = CommandConstants.PDF_URL + "/progressInvoicingViewPDFHandler";
        PostFormPanel post = new PostFormPanel(pdfURL, "_blank");
        hp.add(post);
        invoiceService.getInvoicesByConvertedQuote(invoiceData.getID(), new AsyncCallback<NewInvoice[]>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(NewInvoice[] newInvoices) {
                if (invoiceData != null
                        && invoiceData.getProgressInvoicePDFTemplateList() != null
                        && invoiceData.getProgressInvoicePDFTemplateList().getItems() != null) {
                    invoiceData.getProgressInvoicePDFTemplateList().getItems();
                    for (SelectItem pdfItem : invoiceData.getProgressInvoicePDFTemplateList().getItems()) {

                        if (pdfItem.isDefaultSelected()) {
                            invoiceData.setPdfTemplateID(pdfItem.getId());
                        }
                    }
                }
                new PDFProgressInvoiceTransferObject(post, newInvoices, invoiceData);
                post.submit();
            }
        });
    }

    public String getIconStyle() {
        return null;
    }

    protected ViewAddFiledsCodeName getViewTypeForCustomFields() {
        return isSalesOrder ? ViewAddFiledsCodeName.SaleOrderAdd : ViewAddFiledsCodeName.SaleQuoteAdd;
    }

    private void setUpReasonBox(final String rejectStatus) {
        SelectItem params = new SelectItem();
        params.setCode(isSalesOrder ? Constants.SALE_ORDER : Constants.SALE_QUOTE);
        params.setDescription(rejectStatus);
        params.setEntityId(saleQuoteID);
        RejectionReasonInvoices rejectionReasonInvoices = new RejectionReasonInvoices(params);
        rejectionReasonInvoices.setSaveRejection(() -> {
            if (isSalesOrder) {
                Info.show(property.getSingular(wfmStrings.messSuccessfullyRejected(), accountingStrings.salesOrder()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEORDER_APPROVAL, null, SaleQuoteSummaryView.this);
                closeTab();
            } else {
                Info.show(property.getSingular(wfmStrings.messSuccessfullyRejected(), accountingStrings.quote()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEQUOTE_ADDED, null, SaleQuoteSummaryView.this);
                closeTab("accounting|salequote");
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
    protected Widget onInitialize() {
        QuoteService.App.get().checkForAccess(saleQuoteID, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable throwable) {
                closeTab();
            }

            @Override
            public void onSuccess(Boolean value) {
                if (value == null) {
                    Info.show(wfmMessages.withThisParameterDoesNotExist(property.getSingular(wfmStrings.salesQuote())), Info.Type.WARNING);
                    closeTab();
                } else if (value) {
                    SaleQuoteSummaryView.super.onInitialize();
                }
            }
        });
        return null;
    }

    private void alertStockItemsMessage(String[] items) {
        StringBuilder itemNames = new StringBuilder();
        for (int i = 0; i < items.length; i++) {
            if (i != 0) {
                itemNames.append(", ");
            }
            itemNames.append("\"").append(items[i]).append("\"");
        }
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OkCancel, accountingMessages.youDoNotHaveEnoughQuantityToReserve(itemNames.toString()), accountingStrings.getPropertyContinue(), new CloseHandler() {
            @Override
            public void onSubmit() {
                changeQuoteStatus(APPROVE, false);
//                quoteService.approveQuote(saleQuoteID, new AbstractAsyncCallback() {
//                    public void failure(Throwable caught) {
//                        setEnableButtons(true);
//                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
//                    }
//
//                    public void success(Object result) {
//                        setEnableButtons(true);
//                        Info.show(property.getSingular(accountingStrings.approvedSuccessfullyQuote(), accountingStrings.quote()), Info.Type.INFO);
//                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEQUOTE_ADDED, result, SaleQuoteSummaryView.this);
//                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEORDER_APPROVAL, result, SaleQuoteSummaryView.this);
//                        closeTab("accounting|salequote");
//                    }
//                });
            }
        });
        messageBox.setWidth(560);
        messageBox.setTitle(accountingStrings.notEnoughQuantity());
        messageBox.setMessage(accountingMessages.youDoNotHaveEnoughQuantityToReserve(itemNames.toString()));
        messageBox.open();
    }

    private void alertStockItemsMessage2(String[] items) {
        StringBuilder itemNames = new StringBuilder();
        for (int i = 0; i < items.length; i++) {
            if (i != 0) {
                itemNames.append(", ");
            }
            itemNames.append("\"").append(items[i]).append("\"");
        }
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OK, true);
        messageBox.setWidth(560);
        messageBox.setTitle(accountingStrings.notEnoughQuantity());
        messageBox.setMessage(accountingMessages.youDoNotHaveEnoughQuantityToReserveSubmitToManager(itemNames.toString()));
        messageBox.open();
    }


    @Override
    public List<Widget> getFooterLeftSideWidgets() {
        List<Widget> leftSideWidgets = super.getFooterLeftSideWidgets();
        link = new FooterInformer(SvgEnum.link, wfmStrings.links(), null);
        if (Utils.hasPermission(isSalesOrder ? ACCOUNTING_SALES_ORDER_LINKS : ACCOUNTING_SALES_QUOTE_LINKS)) {
            leftSideWidgets.add(link);
        }
        if (Utils.hasPermission(ACCOUNTING_SALES_ORDER_PICKLIST) && invoiceData.getPickListID() != null) {
            FooterInformer gdn = new FooterInformer(SvgEnum.delivered, "GDN", null);
            new KpiToolTip(gdn, "Goods Delivered Notes");
            leftSideWidgets.add(gdn);
            reloadPickList(gdn);
            WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALEORDER_SHIPPED, this, (sender, args) -> {
                closeTab();
                goTo(Constants.SALE_ORDER_CODE + "|summary/" + saleQuoteID, invoiceData.getInvoiceNumber());
                reloadPickList(gdn);
            });
            WfmUiEventsBus.addWfmUiListener(WfmUiEventType.PICKLIST_RELOAD_PAGE, this, (sender, args) -> {
                reloadPickList(gdn);
            });

        }

        return leftSideWidgets;

    }

    private void reloadPickList(FooterInformer gdn) {
        quoteService.getPickList(invoiceData.getPickListID(), new AsyncCallback<PickList>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(PickList pickListData) {
                if (pickListData != null && pickListData.getGdnCount() != null && pickListData.getGdnCount() > 0) {
                    gdn.setBadgeCount(pickListData.getGdnCount());
                    gdn.setVisible(true);
                    gdn.addClickHandler(clickEvent -> new GdnAndGrnListNavBox(invoiceData.getPickListID(), false).show());
                } else {
                    gdn.setBadgeCount(0);
                    gdn.setVisible(false);
                }
            }
        });
    }

    public String propertyCode() {
        if (isSalesOrder) {
            return Constants.SALE_ORDER_CODE;
        }
        return Constants.SALE_QUOTE;
    }

    private HasLinks linkingUtil;

    @Override
    public HasLinks getLinkingUtil() {
        if (linkingUtil == null) {
            linkingUtil = new HasLinks(SaleQuoteSummaryView.this) {
                @Override
                protected boolean isActionEditing() {
                    return false;
                }

                @Override
                public Integer getRelationID() {
                    return saleQuoteID;
                }

                @Override
                public String getRelationType() {
                    return isSalesOrder ? RelationItem.TYPE_SALEORDER : RelationItem.TYPE_SALEQUOTE;
                }

                @Override
                public String getRelationName() {
                    return invoiceData.getInvoiceNumber();
                }

                @Override
                public boolean hasNoSummaryTab() {
                    return true;
                }
            };
        }
        return linkingUtil;
    }
}
