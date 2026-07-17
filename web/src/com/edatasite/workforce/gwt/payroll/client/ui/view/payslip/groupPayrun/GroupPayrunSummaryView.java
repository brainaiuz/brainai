package com.edatasite.workforce.gwt.payroll.client.ui.view.payslip.groupPayrun;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.calendardatepicker.CalendarUtil;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollTotalTO;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrunPayment;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollClientUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.FigCaption;
import gwt.material.design.client.ui.html.FigureWidget;
import gwt.material.design.client.ui.html.Small;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GroupPayrunSummaryView extends GroupPayrunEditView {

    private SplitButton approveRejectButtons;

    private Div paymentButtonPanel;

    public GroupPayrunSummaryView(Integer id) {
        super("summary");
        setDescription(property.getPlural(payrollStrings.groupPayruns()));
        this.id = id;
    }

    @Override
    protected void initEmployeeTable() {
        setColumns();
        employeeTable = new EditableTable(getColumns(), false);
        grid = employeeTable.getGrid();

        Div div = new Div();
        div.setStyleName("scroll-box--x");
        div.add(employeeTable);

        addField(PAYROLL_STARTER.EMPLOYEE_PAYSLIP_TABLE, div);
    }

    @Override
    protected void initializeForm() {
        super.initializeForm();
        addFormListeners();
    }

    private void addFormListeners() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PAYRUN_PAYMENT_ADD, GroupPayrunSummaryView.this, (sender, args) -> onInitialize());
    }

    protected void setValues() {
        super.setValues();
        updateTotalTable();
    }

    private void updateTotalTable() {
        BigDecimal dueAmount = groupPayrunData.getTotalAmount();
        BigDecimal paymentAmount = BigDecimal.ZERO;
        if (groupPayrunData.getPayments() != null && groupPayrunData.getPayments().size() > 0) {
            for (PayrunPayment payment : groupPayrunData.getPayments()) {
                setPaymentInfoToTable(payment);
                paymentAmount = paymentAmount.add(payment.getAmount());
            }
            dueAmount = dueAmount.subtract(paymentAmount);
        }
        HTML dueAmountHTML = new HTML(PayrollClientUtils.format(dueAmount));
        totalTable.setDueAmount(new HTML(wfmStrings.dueAmount()), dueAmountHTML);
    }

    protected void setPaymentInfoToTable(PayrunPayment payment) {
        String title = payment.getObjectID() != null ? wfmStrings.payment() : payrollStrings.singlePayments();
        String action = payment.getObjectID() != null ? "payrunPayment|view/" + payment.getObjectID() : null;
        PaymentInformation paymentInformation = new PaymentInformation(payment, title, action);
        totalTable.addPaidItem(paymentInformation, (paymentInformation.getAction() != null) ? new MaterialLink(PayrollClientUtils.format(payment.getAmount()), paymentInformation.getAction()) : new HTML(PayrollClientUtils.format(payment.getAmount())));
    }

    @Override
    protected List<Widget> getFooterRightSideWidgets() {
        List<Widget> rightWidgets = new ArrayList<>();
        if (paymentButtonPanel == null) {
            paymentButtonPanel = new Div();
        }
        rightWidgets.add(paymentButtonPanel);
        if (buttonsPanel == null) {
            buttonsPanel = new Div();
        }
        rightWidgets.add(buttonsPanel);
        rightWidgets.add(getExportOptions());

        return rightWidgets;
    }

    @Override
    protected List<Widget> getFooterLeftSideWidgets() {
        List<Widget> widgets = new ArrayList<>();

        Div filterDiv = new Div("frame__info-paging");
        filterDiv.add(drawPaginationPanel());
        widgets.add(filterDiv);

        showJournal = new FooterInformer(SvgEnum.wallet, wfmStrings.showJournal(), null);
        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_MAIN_MENU)) {
            widgets.add(showJournal);
        }
        return widgets;
    }

    @Override
    protected void initButtons() {
        boolean canApprove = !(Constants.PAYRUN_STATUS_APPROVED.equals(groupPayrunData.getStatusCode())
                || Constants.PAYRUN_STATUS_PARTIAL_PAID.equals(groupPayrunData.getStatusCode())
                || Constants.PAYRUN_STATUS_PAID.equals(groupPayrunData.getStatusCode())
                || Constants.PAYRUN_STATUS_PROCESSING.equals(groupPayrunData.getStatusCode()))
                && Utils.hasPermission(PermissionConstants.PAYROLL_CAN_APPROVE_PAYSLIP)
                && Utils.getUserID().equals(groupPayrunData.getApprover().getId());
        if (buttonsPanel == null) {
            buttonsPanel = new Div();
        } else {
            buttonsPanel.clear();
        }
        if (paymentButtonPanel == null) {
            paymentButtonPanel = new Div();
        } else {
            paymentButtonPanel.clear();
        }

        boolean isBeforeLockDate = false;
        if (Utils.isPayslipsLocked()) {
            Integer currentYear = groupPayrunData.getYear();
            Integer currentMonth = groupPayrunData.getMonthID();
            int monthDayCount = CalendarUtil.getMonthDaysCount(currentMonth, currentYear);
            DateNonConvertable toDate = new DateNonConvertable(new Date(currentYear - 1900, currentMonth, monthDayCount));

            isBeforeLockDate = DateUtils.getTransactionLockDate().after(toDate.getNonConvertedDate());
        }

        if (!isBeforeLockDate && canApprove) {
            List<SplitButtonItem> items = new ArrayList<>();
            SplitButtonItem approveAll = new SplitButtonItem("APPROVE_ALL", wfmStrings.approve(), () -> {
                approveRejectButtons.setEnabled(false);
                if (!groupPayrunData.isDoubleConfirmationEnabled()) {
                    batchUpdateStatus(Constants.PAYRUN_STATUS_APPROVED);
                    return;
                }
                WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, property.getSingular(wfmStrings.saveAndApproveGroupPayrunConfirmation(), wfmStrings.groupPayrun()), new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        batchUpdateStatus(Constants.PAYRUN_STATUS_APPROVED);
                    }
                });

                wfmMessageBox.setTitle(wfmStrings.confirmation());
                wfmMessageBox.setMessage(payrollStrings.areYouSureApproveAllItems());
                wfmMessageBox.setWidth("300px");
                wfmMessageBox.center();
            });
            items.add(approveAll);

            SplitButtonItem rejectAll = new SplitButtonItem("REJECT_ALL", wfmStrings.reject(), () -> {
                approveRejectButtons.setEnabled(false);
                if (!groupPayrunData.isDoubleConfirmationEnabled()) {
                    batchUpdateStatus(Constants.PAYRUN_STATUS_REJECTED);
                    return;
                }
                WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, property.getSingular(wfmStrings.saveAndApproveGroupPayrunConfirmation(), wfmStrings.groupPayrun()), new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        batchUpdateStatus(Constants.PAYRUN_STATUS_REJECTED);
                    }
                });

                wfmMessageBox.setTitle(wfmStrings.confirmation());
                wfmMessageBox.setMessage(payrollStrings.areYouSureRejectAllItems());

                wfmMessageBox.setWidth("300px");
                wfmMessageBox.center();
            });
            items.add(rejectAll);

            approveRejectButtons = new SplitButton(null, 97, Constants.BTN_PRIMARY, true);
            approveRejectButtons.addItemList(items);
            buttonsPanel.add(approveRejectButtons);
        }

        if (Constants.PAYRUN_STATUS_APPROVED.equals(groupPayrunData.getStatusCode())
                || Constants.PAYRUN_STATUS_PARTIAL_PAID.equals(groupPayrunData.getStatusCode())
                || Constants.PAYRUN_STATUS_PAID.equals(groupPayrunData.getStatusCode())) {
            WfmButton2 sendNotifButton = new WfmButton2(wfmStrings.sendNotification(), BTN_DEFAULT_OUTLINE);
            sendNotifButton.addClickHandler(event -> {
                final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                messageBox.setTitle(wfmStrings.confirmation());
                messageBox.setMessage(payrollStrings.areYouSureYouWantToSendNotificationToEmployees());
                messageBox.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        LoadingPanel.loading(true);
                        PayrollService.App.get().sendPayslipNotification(groupPayrunData.getObjectID(), new AsyncCallback<Void>() {
                            @Override
                            public void onFailure(Throwable throwable) {
                            }

                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        });
                        LoadingPanel.loading(false);
                        Info.show(property.getSingular(wfmStrings.groupPayrunHasBeenSent(), wfmStrings.groupPayrun()));
                    }
                });
                messageBox.open();
            });
            buttonsPanel.add(sendNotifButton);
        }

        if (!isBeforeLockDate && Constants.PAYRUN_STATUS_APPROVED.equals(groupPayrunData.getStatusCode())
                || Constants.PAYRUN_STATUS_PARTIAL_PAID.equals(groupPayrunData.getStatusCode())) {
            WfmButton2 addPayment = new WfmButton2(payrollStrings.payEmployees(), BTN_PRIMARY, clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("payrunPayment|add/add/" + id));
            Div paymentDiv = new Div();
            paymentDiv.add(addPayment);
            paymentButtonPanel.add(paymentDiv);
        }
    }

    private void batchUpdateStatus(String status) {
        LoadingPanel.loading(true);
        payrollService.batchChangePayrollGroupStatus(groupPayrunData.getObjectID(), status, new AbstractAsyncCallback<PayrollTotalTO>() {
            @Override
            public void failure(Throwable throwable) {
                if (approveRejectButtons != null) {
                    approveRejectButtons.setEnabled(true);
                }
                LoadingPanel.loading(false);
            }

            @Override
            public void success(PayrollTotalTO result) {
                if (approveRejectButtons != null) {
                    approveRejectButtons.setEnabled(true);
                }
                LoadingPanel.loading(false);
                Info.show("Payslip data successfully updated", Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PAYSLIP_SAVED, null, GroupPayrunSummaryView.this);
                closeTab();
            }
        });
    }

    @Override
    protected void registerEventHandlers() {
        //don't register events
    }

    @Override
    protected void onApproverLookupSelected() {
        //don't react on approver selection
    }

    @Override
    protected void disableFields() {
        super.disableFields();
        frequency.setEnabled(false);
        approver.setEnabled(false);
        processDate.setEnabled(false);
        paymentMethodListBox.setEnabled(false);
    }

    @Override
    public String getPropertyCode() {
        return PAYSLIP_TABLE_LIST;
    }

    protected class PaymentInformation extends FigureWidget {

        private final String action;

        public PaymentInformation(PayrunPayment payment, String title, String action) {
            this.action = action;
            addStyleName("right-label");

            FigCaption figCaption = new FigCaption();
            add(figCaption);

            Div container = new Div();
            figCaption.add(container);

            HorizontalPanelDiv pnlCont = new HorizontalPanelDiv();

            if (payment.getObjectID() != null) {
                SvgIcon trashIcon = new SvgIcon((SvgEnum.trash2));
                MaterialLink removePaymentLink = new MaterialLink();
                removePaymentLink.setClass("btn--icon");
                removePaymentLink.add(trashIcon);
                removePaymentLink.addClickHandler(ch -> deletePayment(payment));
                pnlCont.add(removePaymentLink);
            }

            if (action != null && !action.isEmpty()) {
                MaterialLink detailsLink = new MaterialLink(title, action);
                pnlCont.add(detailsLink);
            } else {
                pnlCont.add(new Span(title));
            }
            container.add(pnlCont);

            figCaption.add(new Small(DateUtils.format(payment.getPaymentDate())));

            SvgIcon svgIcon = new SvgIcon(SvgEnum.check);
            Div iconWrapper = new Div();
            iconWrapper.setClass("icon-wrapp--circle");
            iconWrapper.add(svgIcon);
            add(iconWrapper);
        }

        private void deletePayment(PayrunPayment payment) {
            final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.INFO, Action.YesNo);
            messageBox.setTitle(wfmStrings.confirmation());
            messageBox.setMessage(wfmMessages.sureYouWantToDelete(wfmStrings.payment(), "?"));

            if (Utils.isPayslipsLocked()) {
                Integer currentYear = groupPayrunData.getYear();
                Integer currentMonth = groupPayrunData.getMonthID();
                int monthDayCount = CalendarUtil.getMonthDaysCount(currentMonth, currentYear);
                DateNonConvertable toDate = new DateNonConvertable(new Date(currentYear - 1900, currentMonth, monthDayCount));

                if (DateUtils.getTransactionLockDate().after(toDate.getNonConvertedDate())) {
                    Info.show(wfmMessages.dateShouldBeAfterClosedBeforeDate(wfmStrings.payslips(), Utils.getTransactionLockDate()), Info.Type.WARNING);
                    return;
                }
            }

            messageBox.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    LoadingPanel.loading(true);
                    PayrollService.App.get().deletePayrunPayment(payment.getObjectID(), new AbstractAsyncCallback<Boolean>() {
                        public void failure(Throwable caught) {
                            LoadingPanel.loading(false);
                        }

                        public void success(Boolean result) {
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PAYRUN_PAYMENT_DELETE, null, GroupPayrunSummaryView.this);
                            clear();
                            onInitialize();
                        }
                    });
                }

                @Override
                public void onCancel() {

                }
            });
            messageBox.open();
        }

        public String getAction() {
            return action;
        }
    }
}
