package com.edatasite.workforce.gwt.expenses.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.WftHTMLPanel;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.footerFileUpload.FooterUploadPanel;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpensePaymentData;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseService;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_EXPENSE_DELETE_PAYMENT;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_EXPENSE_EDIT_PAYMENT;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_JOURNAL_REPORT;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_EXPENSE_DELETE_PAYMENT;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_EXPENSE_EDIT_PAYMENT;

/**
 * Created by IntelliJ IDEA.
 * User: Marat
 * Date: 23.03.12
 * Time: 17:32
 * To change this template use File | Settings | File Templates.
 */
public class ExpensePaymentView extends FooteredView implements FittedContent, Colapse, Constants, AccountingCustomFormConstants {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final AccountingUtils accountingUtils = AccountingUtils.get();
    public static final String EXPENSE_PAYMENT = "EXPENSE_PAYMENT";

    private final Integer objectId;
    private WfmButton2 deleteButton, pdfButton, editButton;
    private HashMap<String, Widget> widgetsMap;
    private ExpensePaymentData paymentData;
    private FooterUploadPanel footerUploadPanel;
    private HTMLPanel htmlPanel;

    public ExpensePaymentView(Integer objectID) {
        super("summary", accountingStrings.expenseClaimsPaymentView());
        this.objectId = objectID;

    }

    protected Widget onInitialize() {
        widgetsMap = new HashMap<>();
        loadData();
        return null;
    }

    public void loadData() {
        ExpenseService.App.get().getPaymentData(objectId, new AsyncCallback<ExpensePaymentData>() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess(ExpensePaymentData expensePaymentData) {
                if (expensePaymentData != null) {
                    paymentData = expensePaymentData;
                    initForm();
                    htmlPanel = new WftHTMLPanel(expensePaymentData.getLayoutHTML(), widgetsMap).getContainer();
                    htmlPanel.setStyleName("add-form invoice-form");
                    htmlPanel.add(createFooter());
                    add(htmlPanel);
                }
            }
        });
    }

    private void initForm() {
        SimpleLink numberLink = new SimpleLink(paymentData.getNumberData() != null ? paymentData.getNumberData() : "",
                "expenseReports|previewReport/" + paymentData.getReportId() + "/EXPENSE_VIEW/ACCOUNTING");

        FormGroup numberField = new FormGroup(numberLink);
        numberField.getGroupContent().addStyleName("form-control");

        Div numberFieldLabel = numberField.getGroupLabel();
        numberFieldLabel.addStyleName("label-group");
        numberFieldLabel.add(new Span(wfmStrings.expense() + " #"));

        //update widget map
        widgetsMap.put(INPUT_PAYMENT_DATE, new FormGroup(wfmStrings.paymentDate(), getWidgetAsFormControl(DateUtils.format(paymentData.getDate()))));
        widgetsMap.put(INPUT_REFERENCE, new FormGroup(wfmStrings.reference(), getWidgetAsFormControl(paymentData.getReferenceNumber())));
        widgetsMap.put(INPUT_REPORT_TITLE, new FormGroup(wfmStrings.reportTitle(), getWidgetAsFormControl(paymentData.getTitle())));
        widgetsMap.put(INPUT_NUMBER, numberField);
        widgetsMap.put(INPUT_NAME, new FormGroup(wfmStrings.paidFrom(), getWidgetAsFormControl(paymentData.getPaymentAccount().getName())));
//        widgetsMap.put(INPUT_DATE, new FormGroup(wfmStrings.date(), getWidgetAsFormControl(DateUtils.format(paymentData.getDate()))));
        widgetsMap.put(INPUT_AMOUNT, new FormGroup(wfmStrings.paidAmount(), getWidgetAsFormControl(accountingUtils.formatPrice(paymentData.getPaymentAmount()))));
        widgetsMap.put(INPUT_TOTALS_TABLE, totalsTable());
        if (paymentData.getSupplier() != null && paymentData.getSupplier().getName() != null) {
            widgetsMap.put(INPUT_CUSTOMER, new FormGroup(Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()), getWidgetAsFormControl(paymentData.getSupplier().getName())));
        }

        if (paymentData.getBaseCurrency() != null && paymentData.getCurrency() != null) {
            HTML rateLabel = new HTML("1 " + paymentData.getBaseCurrency().getName() + " = " +
                    AccountingUtils.get().formatExRate(paymentData.getExchangeRate()) + " " + paymentData.getCurrency().getName());
            widgetsMap.put(INPUT_EXCHANGE_RATE, new FormGroup(wfmStrings.currency(), wrapWidgetToFormControl(rateLabel)));
        }


    }

    private FlexTable totalsTable() {

        FlexTable totalTable = new FlexTable();
        totalTable.setStyleName("mod_table--auto mod_table--cellpadding right");

        int i = 0;

        totalTable.setWidget(i, 0, new HTML("<b>" + accountingStrings.totalExpenseAmount() + "(" + paymentData.getExpenseCurrency().getName() + ")" + "</b>"));
        totalTable.setWidget(i, 1, new HTML("<b>" + accountingUtils.formatPrice(paymentData.getTotalExpenseAmount()) + "</b>"));
        totalTable.getCellFormatter().setHorizontalAlignment(i, 1, HasHorizontalAlignment.ALIGN_RIGHT);
        i++;

        if (paymentData.getBaseCurrency().getId() != paymentData.getExpenseCurrency().getId()) {
            totalTable.setWidget(i, 0, new HTML("<b>" + accountingStrings.totalExpenseAmount() + "(" + paymentData.getBaseCurrency().getName() + ")" + "</b>"));
            totalTable.setWidget(i, 1, new HTML("<b>" + accountingUtils.formatPrice(paymentData.getTotalExpenseAmountinBase()) + "</b>"));
            totalTable.getCellFormatter().setHorizontalAlignment(i, 1, HasHorizontalAlignment.ALIGN_RIGHT);
            i++;
        }

        if (paymentData.getCurrency() != null) {
            totalTable.setWidget(i, 0, new HTML("<b>" + wfmStrings.paymentAmount() + "(" + paymentData.getCurrency().getName() + ")" + "</b>"));
            totalTable.setWidget(i, 1, new HTML("<b>" + accountingUtils.formatPrice(paymentData.getPaymentAmount()) + "</b>"));
            totalTable.getCellFormatter().setHorizontalAlignment(i, 1, HasHorizontalAlignment.ALIGN_RIGHT);
            i++;
        }

        if (paymentData.getCurrency() == null || paymentData.getCurrency() != null && paymentData.getBaseCurrency().getId() != paymentData.getCurrency().getId()) {
            totalTable.setWidget(i, 0, new HTML("<b>" + wfmStrings.paymentAmount() + "(" + paymentData.getBaseCurrency().getName() + ")" + "</b>"));
            totalTable.setWidget(i, 1, new HTML("<b>" + accountingUtils.formatPrice(paymentData.getPaymentAmountInExpenseCurrency()) + "</b>"));
            totalTable.getCellFormatter().setHorizontalAlignment(i, 1, HasHorizontalAlignment.ALIGN_RIGHT);
        }


        return totalTable;
    }

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return ExpensePaymentView.this.getFooterLeftSideWidgets();
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return ExpensePaymentView.this.getFooterRightSideWidgets();
            }
        });
    }

    private List<Widget> getFooterLeftSideWidgets() {
        List<Widget> leftSideWidgets = new ArrayList<>();

        footerUploadPanel = new FooterUploadPanel(Constants.F_EXP_PAYMENT, paymentData.getObjectID(), true);

        footerUploadPanel.setInitialClasses("informer-item history-notes-container");
        leftSideWidgets.add(footerUploadPanel);

        if (paymentData != null && paymentData.getJournalId() != null && Utils.hasPermission(ACCOUNTING_JOURNAL_REPORT)) {
            FooterInformer showJournal = new FooterInformer(SvgEnum.wallet, wfmStrings.showJournal(), null);
            showJournal.addClickHandler(clickEvent -> {
                SinksContainerFactory.entryPoint.onHistoryChanged("clickedreport|journalReport/" + paymentData.getJournalId(), accountingStrings.reportView() + ": " + paymentData.getNumberData() != null ? paymentData.getNumberData() : "", accountingStrings.reportView() + ": " + paymentData.getNumberData() != null ? paymentData.getNumberData() : "");
            });
            showJournal.setBadgeCount(1);

            leftSideWidgets.add(showJournal);
        }

        return leftSideWidgets;
    }

    public List<Widget> getFooterRightSideWidgets() {
        List<Widget> result = new ArrayList<>();

        Div pdfWrapper = new Div();
        pdfButton = new WfmButton2(wfmStrings.pdfVersion(), WfmButton2.BTN_WHITE_OUTLINE);
        pdfButton.addClickHandler(click -> generatePdf());
        pdfWrapper.add(pdfButton);
        result.add(pdfWrapper);
        if (Utils.isHRMS() ? Utils.hasPermission(HRMS_EXPENSE_DELETE_PAYMENT) : Utils.hasPermission(ACCOUNTING_EXPENSE_DELETE_PAYMENT) && !paymentData.getStatus().equals(EXPENSE_REVERSED)) {
            Div deleteWrapper = new Div();
            deleteButton = new WfmButton2(wfmStrings.delete(), WfmButton2.BTN_WHITE_OUTLINE);
            deleteButton.addClickHandler(event -> {
                final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                messageBox.setTitle(wfmStrings.warning());
                messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                messageBox.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        ExpenseService.App.get().deleteExpensePayment(objectId, new AbstractAsyncCallback<Void>() {
                            @Override
                            public void failure(Throwable caught) {

                            }

                            @Override
                            public void success(Void result) {
                                setEnabledButtons(true);
                                Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.payment()), Info.Type.INFO);
                                LoadingPanel.loading(false);
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EXPENSE_PAYMENT_DELETE, EXPENSE_PAYMENT
                                        , ExpensePaymentView.this);
                                closeTab();
                            }
                        });
                    }
                });
                messageBox.open();
            });
            deleteWrapper.add(deleteButton);
            result.add(deleteWrapper);
        }

        if (Utils.isHRMS() ? Utils.hasPermission(HRMS_EXPENSE_EDIT_PAYMENT) : Utils.hasPermission(ACCOUNTING_EXPENSE_EDIT_PAYMENT) && !paymentData.getStatus().equals(EXPENSE_REVERSED)) {
            Div editWrapper = new Div();
            editButton = new WfmButton2(wfmStrings.edit(), WfmButton2.BTN_PRIMARY);
            editButton.addClickHandler(click ->
                    SinksContainerFactory.entryPoint.onHistoryChanged("expensepayment|edit/" + objectId, paymentData.getReferenceNumber(), paymentData.getReferenceNumber())
            );
            editWrapper.add(editButton);
            result.add(editWrapper);
        }

        return result;
    }


    private void generatePdf() {
        String pdfURL = CommandConstants.PDF_URL + "/expensePaymentViewPDFHandler";
        RequestObject requestObject = new RequestObject();
        requestObject.setObjectID(objectId);
        HashMap<String, String> parameters = requestObject.getRequestParams();
        Utils.sendPDFOrExcelRequest(htmlPanel, pdfURL, parameters, "_blank");
    }

    private void setEnabledButtons(boolean b) {
        if (deleteButton != null) {
            deleteButton.setEnabled(b);
        }
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    @Override
    public String getIconStyle() {
        return null;
    }
}
