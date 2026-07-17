package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.prepayment;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.rpc.InvoicePaymentRequestObject;
import com.edatasite.workforce.gwt.accounting.client.rpc.PrePaymentListItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.PDFTemplateSelector;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.PrepaymentService;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

import java.util.HashMap;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.*;

public class CustomerPrepaymentListView extends PrePaymentListView {

    private final boolean summaryPermission = Utils.hasPermission(Utils.isLogistics() ? PermissionConstants.LOGISTICS_PREPAYMENT_SUMMARY : PermissionConstants.ACCOUNTING_PREPAYMENT_SUMMARY);
    private final boolean editPermission = Utils.hasPermission(Utils.isLogistics() ? PermissionConstants.LOGISTICS_PREPAYMENT_EDIT : PermissionConstants.ACCOUNTING_PREPAYMENT_EDIT);
    private final boolean voidPermission = Utils.hasPermission(Utils.isLogistics() ? PermissionConstants.LOGISTICS_PREPAYMENT_VOID : PermissionConstants.ACCOUNTING_PREPAYMENT_VOID);
    private final boolean deletePermission = Utils.hasPermission(Utils.isLogistics() ? PermissionConstants.LOGISTICS_PREPAYMENT_DELETE : PermissionConstants.ACCOUNTING_PREPAYMENT_DELETE);
    private final boolean pdfPermission = Utils.hasPermission(Utils.isLogistics() ? PermissionConstants.LOGISTICS_PREPAYMENT_PDF : PermissionConstants.ACCOUNTING_PREPAYMENT_PDF);
    private final boolean copyPermission = Utils.hasPermission(Utils.isLogistics() ? PermissionConstants.LOGISTICS_PREPAYMENT_COPY : PermissionConstants.ACCOUNTING_PREPAYMENT_COPY);

    public CustomerPrepaymentListView() {
        super("prepayments");
        setDescription(property.getPlural(wfmStrings.prepayments()));
        if (hasPermissionToAdd()) {
            setAddNew("prepayment|add/add/");
        }
    }

    public CustomerPrepaymentListView(ListingFilterParameter filterParameter) {
        super("prepayments");
        setDescription(property.getPlural(wfmStrings.prepayments()));
        this.summaryFilter = filterParameter;
    }

    @Override
    protected String getPaymentType() {
        return AccountingConstants.PREPAYMENT;
    }

    @Override
    protected ListPanelType getPanelType() {
        return ListPanelType.PrePaymentListPanel;
    }

    @Override
    protected ViewName getViewName() {
        return ViewName.Prepayment;
    }

    @Override
    protected ActionButton initTopToolBarNewButton() {
        ActionButton newItem = getAddNewButton(ActionButton.Type.TOOLMENU);
        MenuBar menu = new MenuBar(true);

        if (hasPermissionToAdd()) {
            MenuPopItem addNew = new MenuPopItem(accountingStrings.customerPrepayment());
            addNew.setCommand(this::addNewItem);
            menu.addItem(addNew);
        }

        if (Utils.hasPermission(PermissionConstants.CUSTOMER_PREPAYMENT_REFUND_ADD)) {
            MenuPopItem customerRefund = new MenuPopItem(accountingStrings.customerRefund());
            customerRefund.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("customerRefund|add/add"));
            menu.addItem(customerRefund);
        }

        newItem.setMenu(menu);
        return newItem;
    }

    private SinksContainer addNewItem() {
        return SinksContainerFactory.entryPoint.onHistoryChanged(getAddNewToken());
    }

    /**
     * When opened inside a customer/CRM-account summary, the add form must open with that
     * account preselected and locked, so the token carries the account id.
     */
    private String getAddNewToken() {
        Integer accountId = null;
        if (summaryFilter != null) {
            accountId = summaryFilter.getClientId() != null ? summaryFilter.getClientId() : summaryFilter.getCrmAccountId();
        }
        return accountId != null ? "prepayment|add/add/account/" + accountId : "prepayment|add/add/";
    }

    private boolean hasPermissionToAdd() {
        return Utils.hasPermission(Utils.isLogistics() ? PermissionConstants.LOGISTICS_PREPAYMENT_ADD : PermissionConstants.ACCOUNTING_PREPAYMENT_ADD);
    }


    @Override
    protected DefaultNoItemsMessage initEmptyDataText() {
        if (hasPermissionToAdd()) {
            DefaultNoItemsMessage message = new DefaultNoItemsMessage(accountingStrings.currentlyYouDoNotHaveAnyPrepayment());
            message.setTextBeforeLink(accountingStrings.noPrepayment());
            message.setHref(getAddNewToken());
            return message;
        }
        return null;
    }

    @Override
    protected String getViewType() {
        return AccountingConstants.RECEIVABLE_PREPAYMENT;
    }

    @Override
    protected CustomColumnDefinitionConfig[] getColumnConfigs() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[10];
        int i = 0;
        columns[i] = new ColumnDefinitionConfig<PrePaymentListItem, Object>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Object getCellValue(final PrePaymentListItem item) {
                final Integer objectID = item.getObjectID();
                int actionItemCount = 0;
                boolean hasAccountingBeforeBlockDate = (Utils.isBankingLocked() && DateUtils.getTransactionLockDate().after(item.getDate().getNonConvertedDate()));
                MenuBar menuBar = new MenuBar(true);

                if (summaryPermission) {
                    MenuPopItem prePaymentView = new MenuPopItem(wfmStrings.summaryView(), "icon-puchase-invoise-small");
                    prePaymentView.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("invoicepayment|paymentView/" + item.getObjectID() + "/prepayment", item.getNumber()));
                    actionItemCount++;
                    menuBar.addItem(prePaymentView);
                }

                if (item.isEditable()) {
                    if (!hasAccountingBeforeBlockDate && editPermission) {
                        MenuPopItem editPrePayment = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                        editPrePayment.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("prepayment|edit/" + item.getObjectID(), item.getNumber()));
                        actionItemCount++;
                        menuBar.addItem(editPrePayment);
                    }
                    if (!hasAccountingBeforeBlockDate && voidPermission) {
                        MenuPopItem voidMenuItem = createVoidMenuItem(item);
                        if (voidMenuItem != null) {
                            menuBar.addItem(voidMenuItem);
                            actionItemCount++;
                        }
                    }

                    if (Utils.hasPermission(PermissionConstants.CUSTOMER_PREPAYMENT_REFUND_ADD) && !hasAccountingBeforeBlockDate
                            && (AccountingConstants.PRE_PAYMENT_OPEN_STATUS.equals(item.getStatus())) || AccountingConstants.PRE_PAYMENT_PARTIAL_APPLIED_STATUS.equals(item.getStatus())) {
                        MenuPopItem customerRefund = new MenuPopItem(accountingStrings.addRefund(), "icon-edit");
                        customerRefund.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("customerRefund|add/add/" + "Prepayment/" + item.getObjectID()));
                        actionItemCount++;
                        menuBar.addItem(customerRefund);
                    }

                    if (copyPermission) {
                        MenuPopItem copyItem = new MenuPopItem(wfmStrings.copy(), "icon-edit");
                        copyItem.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("prepayment|add/" + item.getObjectID() + "/copy"));
                        actionItemCount++;
                        menuBar.addItem(copyItem);
                    }

                    if (!hasAccountingBeforeBlockDate && deletePermission) {
                        MenuPopItem deletePrePayment = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                        deletePrePayment.setCommand(() -> {
                            final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                            messageBox.setTitle(wfmStrings.warning());
                            messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                            messageBox.addCloseHandler(new CloseHandler() {
                                @Override
                                public void onSubmit() {
                                    PrepaymentService.App.get().deletePrePayment(item.getObjectID(), new AsyncCallback<Integer>() {
                                        @Override
                                        public void onFailure(Throwable caught) {

                                        }

                                        @Override
                                        public void onSuccess(Integer result) {
                                            if (result == -1) {
                                                Info.show((accountingStrings.cannotDeletePrepaymentsIfAppliedToInvoices()), Info.Type.WARNING);
                                            } else if (result == -2) {
                                                Info.show(property.getSingular(accountingStrings.errorDeletingProduct(), accountingStrings.prepayment()), Info.Type.WARNING);
                                            } else {
                                                Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), accountingStrings.prepayment()), Info.Type.INFO);
                                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PREPAYMENT_SAVE, null, CustomerPrepaymentListView.this);
                                            }
                                        }
                                    });
                                }
                            });
                            messageBox.center();
                        });
                        actionItemCount++;
                        menuBar.addItem(deletePrePayment);
                    }
                    if (pdfPermission) {
                        MenuPopItem pdfItem = new MenuPopItem(wfmStrings.pdf(), "icon-document-pdf");
                        pdfItem.setCommand(() -> new PDFTemplateSelector(AccountingConstants.PREPAYMENT, new ExtendedCommand() {
                            @Override
                            public void execute(Integer pdfTemplateID) {
                                InvoicePaymentRequestObject requestObject = new InvoicePaymentRequestObject(objectID, pdfTemplateID);
                                String pdfURL = CommandConstants.PDF_URL + "/invoicePaymentViewPDFHandler";
                                HashMap<String, String> parametrs = requestObject.getRequestParams();
                                parametrs.put("isPrePayment", "true");
                                parametrs.put("isReceivable", "true");
                                parametrs.put("isCustomerPrepayment", "true");
                                if (pdfTemplateID != null) {
                                    parametrs.put("templateID", String.valueOf(pdfTemplateID));
                                }
                                Utils.sendPDFOrExcelRequest(hPanel, pdfURL, parametrs, "_blank");
                            }
                        }));
                        actionItemCount++;
                        menuBar.addItem(pdfItem);
                    }
                    ToolItem toolItem = new ToolItem(actionItemCount);
                    toolItem.setWidget(menuBar);
                    return toolItem.getAction();
                }

                return null;
            }
        };
        columns[i].setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns[i].setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns[i].setColumnSortable(false);

        columns[++i] = new ColumnDefinitionConfig<PrePaymentListItem, Widget>(wfmStrings.number(), PrePaymentListItem.CODE, 150) {

            @Override
            public Widget getCellValue(PrePaymentListItem item) {
                if (!(Utils.isBankingLocked() && DateUtils.getTransactionLockDate().after(item.getDate().getNonConvertedDate())) && summaryPermission) {
                    return new SimpleLink(item.getNumber(), "invoicepayment|paymentView/" + item.getObjectID() + "/prepayment", item.getCustomerName(), item.getNumber());
                }
                HTML label = new HTML(item.getNumber());
                return label;
            }
        };
        columns[i].setShow(true);
        columns[i].setMinimumColumnWidth(100);
        columns[i].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

        columns[++i] = new ColumnDefinitionConfig<PrePaymentListItem, Widget>(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()), PrePaymentListItem.CUSTOMER, 200) {

            @Override
            public Widget getCellValue(PrePaymentListItem item) {
                if (/*!(Utils.isBankingLocked() && DateUtils.getTransactionLockDate().after(item.getDate().getNonConvertedDate())) &&*/ Utils.hasPermission(PermissionConstants.CUSTOMER_CLICKABLE)) {
                    return new SimpleLink(item.getCustomerName(), "client|summary/" + item.getAccountID() + "/" + false + "/" + item.getCustomerName(), item.getNumber(), item.getCustomerName());
                }
                HTML label = new HTML(item.getCustomerName());
                return label;
            }
        };
        columns[i].setMinimumColumnWidth(180);
        columns[i].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

        columns[++i] = new ColumnDefinitionConfig<PrePaymentListItem, String>(wfmStrings.paidTo(), PrePaymentListItem.PAY_ACCOUNT, 200) {

            @Override
            public String getCellValue(PrePaymentListItem item) {
                return item.getPayAccount() != null ? item.getPayAccount() : "";
            }
        };
        columns[i].setMinimumColumnWidth(180);
        columns[i].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

        columns[++i] = new ColumnDefinitionConfig<PrePaymentListItem, String>(wfmStrings.amount(), PrePaymentListItem.AMOUNT, 100) {

            @Override
            public String getCellValue(PrePaymentListItem item) {
                return AccountingUtils.get().formatPrice(item.getAmount());
            }
        };
        columns[i].setMinimumColumnWidth(80);
        columns[i].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

        columns[++i] = new ColumnDefinitionConfig<PrePaymentListItem, String>(wfmStrings.status(), PrePaymentListItem.STATUS, 100) {

            @Override
            public String getCellValue(PrePaymentListItem item) {
                return AccountingConstants.PRE_PAYMENT_APPLIED_STATUS.equals(item.getStatus()) ? wfmStrings.applied()
                        : AccountingConstants.PRE_PAYMENT_OPEN_STATUS.equals(item.getStatus()) ? wfmStrings.open()
                        : AccountingConstants.PRE_PAYMENT_PARTIAL_APPLIED_STATUS.equals(item.getStatus()) ? wfmStrings.partialApplied() : "";
            }
        };
        columns[i].setShow(false);
        columns[i].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns[i].setShow(true);

        columns[++i] = new ColumnDefinitionConfig<PrePaymentListItem, String>(wfmStrings.date(), PrePaymentListItem.DATE, 100) {

            @Override
            public String getCellValue(PrePaymentListItem item) {
                return DateUtils.format(item.getDate().getNonConvertedDate());
            }
        };
        columns[i].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        columns[++i] = new ColumnDefinitionConfig<PrePaymentListItem, String>(wfmStrings.currency(), PrePaymentListItem.CURRENCY, 100) {

            @Override
            public String getCellValue(PrePaymentListItem item) {
                return item.getCurrency();
            }
        };
        columns[i].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns[i].setColumnSortable(true);

        columns[++i] = new ColumnDefinitionConfig<PrePaymentListItem, String>(wfmStrings.createdBy(), PrePaymentListItem.CREATOR, 100) {
            @Override
            public String getCellValue(PrePaymentListItem rowValue) {
                return rowValue.getCreator() != null ? rowValue.getCreator() : "";
            }
        };
        columns[i].setShow(false);

        columns[++i] = new ColumnDefinitionConfig<PrePaymentListItem, String>(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), PrePaymentListItem.DEPARTMENT, 100) {
            @Override
            public String getCellValue(PrePaymentListItem rowValue) {
                return rowValue.getDepartment() != null ? rowValue.getDepartment() : "";
            }
        };
        columns[i].setShow(false);

        columns[++i] = new ColumnDefinitionConfig<PrePaymentListItem, Widget>(Property.get(Constants.SALE_QUOTE, wfmStrings.salesQuote()),
                PrePaymentListItem.SALE_QUOTE, 180) {

            @Override
            public Widget getCellValue(PrePaymentListItem item) {
                if (item.getRentalOrder() != null) {
                    String rentalOrderName = item.getRentalOrder() != null ? item.getRentalOrder().getName() : "";
                    SimpleLink label = getLink(rentalOrderName, null);
                    if (!Utils.isNullOrEmpty(rentalOrderName) && (Utils.hasPermission(ACCOUNTING_RENTAL_ORDER_SUMMARY))) {
                        final Integer objectId = item.getRentalOrder().getId();
                        label.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("rentalorder|summary/" + objectId, rentalOrderName, rentalOrderName));
                    }
                    return label;
                } else {
                    String saleQuoteName = item.getSaleQuote() != null ? item.getSaleQuote().getName() : "";
                    SimpleLink label = getLink(saleQuoteName, null);
                    if (!Utils.isNullOrEmpty(saleQuoteName) && (Utils.isCRM() ? Utils.hasPermission(CRM_SALES_QUOTE_SUMMARY) : (Utils.hasPermission(ACCOUNTING_SALES_QUOTE_SUMMARY)))) {
                        final Integer objectId = item.getSaleQuote().getId();
                        label.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("salequote|summary/" + objectId, saleQuoteName, saleQuoteName));
                    }
                    return label;
                }
            }
        };
        columns[i].setMinimumColumnWidth(100);
        columns[i].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns[i].setShow(false);

        columns[++i] = new ColumnDefinitionConfig<PrePaymentListItem, Widget>(Property.get(Constants.SALE_INVOICE, wfmStrings.salesInvoice()),
                PrePaymentListItem.SALE_INVOICE, 180) {

            @Override
            public Widget getCellValue(PrePaymentListItem item) {
                String saleInvoiceName = item.getSaleInvoice() != null ? item.getSaleInvoice().getName() : "";
                SimpleLink label = getLink(saleInvoiceName, null);
                if (!Utils.isNullOrEmpty(saleInvoiceName) && Utils.hasPermission(ACCOUNTING_SALES_INVOICE_SUMMARY)) {
                    final Integer objectId = item.getSaleInvoice().getId();
                    label.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("saleinvoice|summary/" + objectId, saleInvoiceName, saleInvoiceName));
                }
                return label;
            }
        };
        columns[i].setMinimumColumnWidth(100);
        columns[i].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns[i].setShow(false);

        columns[++i] = new ColumnDefinitionConfig<PrePaymentListItem, String>(accountingStrings.remainingBalance(),
                PrePaymentListItem.REMAINING_BALANCE, 150) {

            @Override
            public String getCellValue(PrePaymentListItem item) {
                return item.getRemainingBalance() != null ? Utils.formatDouble(item.getRemainingBalance().doubleValue()) : "0";
            }
        };
        columns[i].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns[i].setShow(false);

        columns[++i] = new ColumnDefinitionConfig<PrePaymentListItem, String>(wfmStrings.reference(),
                PrePaymentListItem.REFERENCE, 150) {

            @Override
            public String getCellValue(PrePaymentListItem item) {
                return item.getReference() != null ? item.getReference() : "";
            }
        };
        columns[i].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns[i].setShow(false);

        columns[++i] = new ColumnDefinitionConfig<PrePaymentListItem, String>(wfmStrings.project(),
                PrePaymentListItem.PROJECT, 180) {

            @Override
            public String getCellValue(PrePaymentListItem item) {
                return item.getProject() != null ? item.getProject() : "N/A";
            }
        };
        columns[i].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns[i].setShow(false);

        return columns;
    }

    @Override
    protected Command getAddNewItemCommand() {
        return hasPermissionToAdd() ? (Command) SinksContainerFactory.entryPoint.onHistoryChanged(getAddNewToken()) : null;
    }

    @Override
    public String getPropertyCode() {
        return Constants.CUSTOMER_PREPAYMENT;
    }
}
