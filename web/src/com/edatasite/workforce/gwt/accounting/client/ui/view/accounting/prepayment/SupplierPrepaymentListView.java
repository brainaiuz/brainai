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

public class SupplierPrepaymentListView extends PrePaymentListView {

    private final boolean summaryPermission = Utils.hasPermission(Utils.isLogistics() ? PermissionConstants.LOGISTICS_SUPPLIER_CREDIT_SUMMARY : PermissionConstants.ACCOUNTING_SUPPLIER_CREDIT_SUMMARY);
    private final boolean editPermission = Utils.hasPermission(Utils.isLogistics() ? PermissionConstants.LOGISTICS_SUPPLIER_CREDIT_EDIT : PermissionConstants.ACCOUNTING_SUPPLIER_CREDIT_EDIT);
    private final boolean voidPermission = Utils.hasPermission(Utils.isLogistics() ? PermissionConstants.LOGISTICS_SUPPLIER_CREDIT_VOID : PermissionConstants.ACCOUNTING_SUPPLIER_CREDIT_VOID);
    private final boolean deletePermission = Utils.hasPermission(Utils.isLogistics() ? PermissionConstants.LOGISTICS_SUPPLIER_CREDIT_DELETE : PermissionConstants.ACCOUNTING_SUPPLIER_CREDIT_DELETE);
    private final boolean pdfPermission = Utils.hasPermission(Utils.isLogistics() ? PermissionConstants.LOGISTICS_SUPPLIER_CREDIT_PDF : PermissionConstants.ACCOUNTING_SUPPLIER_CREDIT_PDF);
    private final boolean copyPermission = Utils.hasPermission(Utils.isLogistics() ? PermissionConstants.LOGISTICS_SUPPLIER_CREDIT_COPY : PermissionConstants.ACCOUNTING_SUPPLIER_CREDIT_COPY);

    public SupplierPrepaymentListView() {
        super("supplierCredits");
        setDescription(property.getPlural(wfmStrings.supplierCredits()));
        if (hasPermissionToAdd()) {
            setAddNew("supplierCredit|add/add/");
        }
    }

    public SupplierPrepaymentListView(ListingFilterParameter filterParameter) {
        super("supplierCredits");
        setDescription(property.getPlural(wfmStrings.supplierCredits()));
        this.summaryFilter = filterParameter;
    }

    @Override
    protected String getPaymentType() {
        return AccountingConstants.SUPPLIER_CREDIT;
    }

    @Override
    protected ListPanelType getPanelType() {
        return ListPanelType.SupplierCreditListPanel;
    }

    @Override
    protected ViewName getViewName() {
        return ViewName.Supplier;
    }

    @Override
    protected ActionButton initTopToolBarNewButton() {
        ActionButton newItem = getAddNewButton(ActionButton.Type.TOOLMENU);
        MenuBar menu = new MenuBar(true);

        if (hasPermissionToAdd()) {
            MenuPopItem addNew = new MenuPopItem(Property.get(Constants.SUPPLIER_LIST, accountingStrings.supplierPrepayment(), wfmStrings.supplier()));
            addNew.setCommand(this::addNewItem);
            menu.addItem(addNew);
        }

        if (Utils.hasPermission(PermissionConstants.SUPPLIER_PREPAYMENT_REFUND_ADD)) {
            MenuPopItem supplierRefund = new MenuPopItem(accountingStrings.supplierRefund());
            supplierRefund.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("supplierRefund|add/add"));
            menu.addItem(supplierRefund);
        }

        newItem.setMenu(menu);
        return newItem;
    }

    private boolean hasPermissionToAdd() {
        return Utils.hasPermission(Utils.isLogistics() ? PermissionConstants.LOGISTICS_SUPPLIER_CREDIT_ADD : PermissionConstants.ACCOUNTING_SUPPLIER_CREDIT_ADD);
    }

    private SinksContainer addNewItem() {
        return SinksContainerFactory.entryPoint.onHistoryChanged(getAddNewToken());
    }

    /**
     * When opened inside a supplier/CRM-account summary, the add form must open with that
     * account preselected and locked, so the token carries the account id.
     */
    private String getAddNewToken() {
        Integer accountId = null;
        if (summaryFilter != null) {
            accountId = summaryFilter.getSupplierId() != null ? summaryFilter.getSupplierId() : summaryFilter.getCrmAccountId();
        }
        return accountId != null ? "supplierCredit|add/add/account/" + accountId : "supplierCredit|add/add/";
    }

    @Override
    protected DefaultNoItemsMessage initEmptyDataText() {
        if (hasPermissionToAdd()) {
            DefaultNoItemsMessage message = new DefaultNoItemsMessage(property.getSingular(accountingStrings.currentlyYouDoNotHaveAnySupplierCredit(), wfmStrings.supplier()));
            message.setTextBeforeLink(property.getSingular(accountingStrings.noSupplierCredit(), wfmStrings.supplier()));
            message.setHref(getAddNewToken());
            return message;
        }
        return null;
    }

    @Override
    protected String getViewType() {
        return AccountingConstants.PAYABLE_SUPPLIER_CREDIT;
    }

    @Override
    protected CustomColumnDefinitionConfig[] getColumnConfigs() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[10];
        int i = 0;
        columns[i] = new ColumnDefinitionConfig<PrePaymentListItem, Object>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Object getCellValue(final PrePaymentListItem item) {
                return createActionMenu(item);
            }
        };
        columns[i].setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns[i].setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns[i].setColumnSortable(false);

        columns[++i] = new ColumnDefinitionConfig<PrePaymentListItem, Widget>(wfmStrings.number(), PrePaymentListItem.CODE, 150) {

            @Override
            public Widget getCellValue(PrePaymentListItem item) {
                if (/*!(Utils.isBankingLocked() && DateUtils.getTransactionLockDate().after(item.getDate().getNonConvertedDate())) && */summaryPermission) {
                    return new SimpleLink(item.getNumber(), "invoicepayment|paymentView/" + item.getObjectID() + "/supplierCredit", item.getCustomerName(), item.getNumber());
                }
                HTML label = new HTML(item.getNumber());
                return label;
            }
        };
        columns[i].setShow(true);
        columns[i].setMinimumColumnWidth(100);
        columns[i].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

        columns[++i] = new ColumnDefinitionConfig<PrePaymentListItem, Widget>(property.getSingular(wfmStrings.supplier()), PrePaymentListItem.SUPPLIER, 200) {

            @Override
            public Widget getCellValue(PrePaymentListItem item) {
                if (!(Utils.isBankingLocked() && DateUtils.getTransactionLockDate().after(item.getDate().getNonConvertedDate()))) {
                    return new SimpleLink(item.getCustomerName(), "suppliersummary|summary/" + item.getAccountID(), item.getCustomerName(), item.getNumber());
                }
                HTML label = new HTML(item.getCustomerName());
                return label;
            }
        };
        columns[i].setMinimumColumnWidth(180);
        columns[i].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

        columns[++i] = new ColumnDefinitionConfig<PrePaymentListItem, String>(wfmStrings.paidFrom(), PrePaymentListItem.PAY_ACCOUNT, 200) {

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
                        : AccountingConstants.VOID.equals(item.getStatus()) ? accountingStrings.voide()
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

        columns[++i] = new ColumnDefinitionConfig<PrePaymentListItem, String>(wfmStrings.purchaseorder(),
                PrePaymentListItem.PURCHASE_ORDER, 180) {

            @Override
            public String getCellValue(PrePaymentListItem item) {
                return item.getPurchaseOrder() != null ? item.getPurchaseOrder().getName() : "";
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

    protected Object createActionMenu(PrePaymentListItem item) {
        final Integer objectID = item.getObjectID();
        int actionItemCount = 0;
        boolean hasAccountingBeforeBlockDate = (Utils.isBankingLocked() && DateUtils.getTransactionLockDate().after(item.getDate().getNonConvertedDate()));
        MenuBar menuBar = new MenuBar(true);

        if (summaryPermission) {
            MenuPopItem prePaymentView = new MenuPopItem(wfmStrings.summaryView(), "icon-puchase-invoise-small");
            prePaymentView.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("invoicepayment|paymentView/" + item.getObjectID() + "/supplierCredit", item.getNumber()));
            actionItemCount++;
            menuBar.addItem(prePaymentView);
        }

        if (item.isEditable()) {
            if (!hasAccountingBeforeBlockDate && editPermission) {
                MenuPopItem editPrePayment = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                editPrePayment.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("supplierCredit|edit/" + item.getObjectID(), item.getNumber()));
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
            if (Utils.hasPermission(PermissionConstants.SUPPLIER_PREPAYMENT_REFUND_ADD) && !hasAccountingBeforeBlockDate
                    && (AccountingConstants.PRE_PAYMENT_OPEN_STATUS.equals(item.getStatus())) || AccountingConstants.PRE_PAYMENT_PARTIAL_APPLIED_STATUS.equals(item.getStatus())) {
                MenuPopItem supplierRefund = new MenuPopItem(accountingStrings.addRefund(), "icon-edit");
                supplierRefund.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("supplierRefund|add/add/" + "Prepayment/" + item.getObjectID()));
                actionItemCount++;
                menuBar.addItem(supplierRefund);
            }
            if (copyPermission) {
                MenuPopItem copyItem = new MenuPopItem(wfmStrings.copy(), "icon-edit");
                copyItem.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("supplierCredit|add/" + item.getObjectID() + "/copy"));
                actionItemCount++;
                menuBar.addItem(copyItem);
            }
            if (!hasAccountingBeforeBlockDate && deletePermission) {
                MenuPopItem deletePrePayment = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                deletePrePayment.setCommand(
                        () -> {
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
                                                Info.show(property.getSingular(accountingStrings.cannotDeleteSupplierCreditsIfAppliedToInvoices(), wfmStrings.supplier()), Info.Type.WARNING);
                                            } else if (result == -2) {
                                                Info.show(property.getSingular(accountingStrings.errorDeletingProduct(), accountingStrings.prepayment()), Info.Type.WARNING);
                                            } else {
                                                Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), accountingStrings.prepayment()), Info.Type.INFO);
                                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PREPAYMENT_SAVE, null, SupplierPrepaymentListView.this);
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
                pdfItem.setCommand(() -> new PDFTemplateSelector(AccountingConstants.SUPPLIER_CREDIT, new ExtendedCommand() {
                    @Override
                    public void execute(Integer pdfTemplateID) {
                        InvoicePaymentRequestObject requestObject = new InvoicePaymentRequestObject(objectID, pdfTemplateID);
                        String pdfURL = CommandConstants.PDF_URL + "/invoicePaymentViewPDFHandler";
                        HashMap<String, String> parametrs = requestObject.getRequestParams();
                        parametrs.put("isCashRefund", "false");
                        parametrs.put("isPrePayment", "true");
                        parametrs.put("isReceivable", "false");
                        parametrs.put("isSupplierCredit", "true");
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

    @Override
    public String getPropertyCode() {
        return AccountingConstants.SUPPLIER_PREPAYMENT;
    }
}
