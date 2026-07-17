package com.edatasite.workforce.gwt.payroll.client.ui.view.payslip;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.PayslipItemFilter;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.rpc.SinglePayrunItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 1/29/15
 * Time: 12:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class SinglePayrunSummaryView extends FooteredView implements Colapse, FittedContent {

    private static final PayrollStrings payrollStrings = GWT.create(PayrollStrings.class);

    private final Integer singlePayrunID;

    public SinglePayrunSummaryView(Integer singlePayrunID) {
        super("viewPayslip", payrollStrings.viewPayslip());
        this.singlePayrunID = singlePayrunID;

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PAYRUN_PAYMENT_ADD, SinglePayrunSummaryView.this, (sender, args) -> reload());
    }

    protected void reload() {
        clear();
        onInitialize();
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        final PayslipItemFilter filter = new PayslipItemFilter();
        filter.setObjectID(singlePayrunID);
        filter.setFromView(true);
        LoadingPanel.loading(true);
        PayrollService.App.get().getSinglePayrunData(filter, new AsyncCallback<SinglePayrunItem>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(final SinglePayrunItem result) {
                SinglePayrunSummaryUiBinder uiBinder = new SinglePayrunSummaryUiBinder(() -> SinglePayrunSummaryView.this);
                uiBinder.setPayslipData(result);
                uiBinder.init();
                uiBinder.fillFormData(result);

                uiBinder.setShowAdvancedOptionCommand(() -> showAdvancedOptions(wfmStrings.additionalFields(), uiBinder.getAdvancedOptions()));

                Div formDiv = new Div("add-form");
                formDiv.add(uiBinder.getRootElement());
                formDiv.add(new ViewFooter(new IFooteredView() {
                    @Override
                    public List<Widget> getFooterLeftSideWidgets() {
                        List<Widget> leftSideWidgets = new ArrayList<>();
                        FooterInformer informer = new FooterInformer(SvgEnum.messageSquare,
                                payrollStrings.paymentPolicy(),
                                null);
                        informer.addClickHandler(event -> uiBinder.getPaymentPolicyModal().open());
                        leftSideWidgets.add(informer);

                        if (result != null && result.getJournalId() != null && Utils.hasPermission(PermissionConstants.ACCOUNTING_MAIN_MENU)) {
                            FooterInformer showJournal = new FooterInformer(SvgEnum.wallet, wfmStrings.showJournal(), null);
                            showJournal.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("clickedreport|journalReport/" + result.getJournalId(), accountingStrings.reportView() + ": " + result.getEmployeeCode(), accountingStrings.reportView() + ": " + result.getMonth()));
                            showJournal.setBadgeCount(1);

                            leftSideWidgets.add(showJournal);
                        }

                        return leftSideWidgets;
                    }

                    @Override
                    public List<Widget> getFooterRightSideWidgets() {
                        List<Widget> rightWidgets = new ArrayList<>();
                        boolean isBeforeLockDate = (Utils.isPayslipsLocked() && DateUtils.getTransactionLockDate().after(result.getToDate().getNonConvertedDate()));
                        if (!isBeforeLockDate && !(Constants.PAYRUN_STATUS_APPROVED.equals(result.getStatusCode())
                                || Constants.PAYRUN_STATUS_PARTIAL_PAID.equals(result.getStatusCode())
                                || Constants.PAYRUN_STATUS_PAID.equals(result.getStatusCode()))
                                && Utils.hasPermission(PermissionConstants.PAYROLL_PAYSLIP_EDIT)) {
                            rightWidgets.add(uiBinder.getEdit());
                        }
                        if (!isBeforeLockDate && Constants.PAYRUN_STATUS_SUBMITTED.equals(result.getStatusCode()) && Utils.hasPermission(PermissionConstants.PAYROLL_CAN_APPROVE_PAYSLIP)) {
                            rightWidgets.add(uiBinder.getApprove());
                        }
                        if (!isBeforeLockDate && (Constants.PAYRUN_STATUS_APPROVED.equals(result.getStatusCode())
                                || Constants.PAYRUN_STATUS_PARTIAL_PAID.equals(result.getStatusCode()))
                                && Utils.hasPermission(PermissionConstants.PAYROLL_CAN_APPROVE_PAYSLIP)) {
                            rightWidgets.add(uiBinder.getMakePayment());
                        }
                        if (Utils.hasPermission(PermissionConstants.PAYROLL_PAYSLIP_PDF) || Utils.isHRMS()) {
                            rightWidgets.add(uiBinder.getPdfVersionButton());
                        }
                        return rightWidgets;
                    }
                }));
                add(formDiv);
                LoadingPanel.loading(false);
            }
        });

        return null;
    }

    public Widget wrapFormControl(Widget widget) {
        return super.wrapWidgetToFormControl(widget);
    }

    @Override
    public String getIconStyle() {
        return null;
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
