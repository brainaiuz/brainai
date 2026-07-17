package com.edatasite.workforce.gwt.payroll.client.ui.view.payslip;

import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.PayslipItemFilter;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.calendardatepicker.CalendarUtil;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.rpc.SinglePayrunItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 1/26/15
 * Time: 5:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class SinglePayrunGenerateView extends FooteredView implements Colapse, FittedContent {

    private static final PayrollStrings payrollStrings = GWT.create(PayrollStrings.class);
    private final DateTimeFormat format_year = DateTimeFormat.getFormat("yyyy");

    private Integer singlePayrunId;
    private Integer employeeID;

    public SinglePayrunGenerateView() {
        super("addsinglePayrun");
        setDescription(property.getSingular(wfmStrings.payslip()));
    }

    public SinglePayrunGenerateView(Integer singlePayrunId, Integer employeeID) {
        super("addsinglePayrun");
        setDescription(property.getSingular(wfmStrings.payslip()));
        this.singlePayrunId = singlePayrunId;
        this.employeeID = employeeID;
    }

    @Override
    protected Widget onInitialize() {
        final PayslipItemFilter filter = new PayslipItemFilter();
        filter.setObjectID(singlePayrunId);
        filter.setEmployeeID(employeeID);
        Date currentDate = new Date();
        final int currentYear = Integer.valueOf(format_year.format(currentDate));
        int monthDayCount = CalendarUtil.getMonthDaysCount(currentDate.getMonth(), currentYear);
        DateNonConvertable fromDate = new DateNonConvertable(new Date(currentYear - 1900, currentDate.getMonth(), 1));
        DateNonConvertable toDate = new DateNonConvertable(new Date(currentYear - 1900, currentDate.getMonth(), monthDayCount));
        filter.setDaysOfMonth(monthDayCount);
        filter.setFromDate(fromDate);
        filter.setToDate(toDate);
        if (singlePayrunId == null) {
            filter.setPeriodChecker(currentDate.getMonth() + "," + currentYear);
        }
        filter.setMonth(currentDate.getMonth());
        filter.setYear(currentYear);
        filter.setCalculateBasicSalaryFromProject(Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_PAYRUN_EMPLOYEE_SALARY_CURRENCY));

        LoadingPanel.loading(true);
        PayrollService.App.get().getSinglePayrunData(filter, new AsyncCallback<SinglePayrunItem>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(final SinglePayrunItem result) {
                SinglePayrunUiBinder uiBinder = new SinglePayrunUiBinder(new SinglePayrunInterface() {
                    @Override
                    public Integer getSinglePayrunID() {
                        return result.getObjectID();
                    }

                    @Override
                    public Boolean isDoubleApprovedEnabled() {
                        return result.isDoubleApprovedEnabled();
                    }

                    @Override
                    public Boolean isCalculatePension() {
                        return result.isCalculatePension();
                    }

                    @Override
                    public Boolean isAtsCustomizationEnabled() {
                        return result.isAtsCustomization();
                    }

                    @Override
                    public SinglePayrunGenerateView getView() {
                        return SinglePayrunGenerateView.this;
                    }

                    @Override
                    public PayslipItemFilter getFilter() {
                        return filter;
                    }
                }, singlePayrunId);
                uiBinder.setEditable(result.isEditable() || singlePayrunId == null || Utils.hasPermission(PermissionConstants.PAYROLL_PAYSLIP_EDITABLE));
                uiBinder.setEnabledMultiCurrency(result.isEnabledMultiCurrency());
                uiBinder.init(result.getEnabledAccounting());
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

                        return leftSideWidgets;
                    }

                    @Override
                    public List<Widget> getFooterRightSideWidgets() {
                        List<Widget> rightWidgets = new ArrayList<>();

                        Div saveDraftDiv = new Div();
                        saveDraftDiv.add(uiBinder.getSaveAsDraftButton());
                        rightWidgets.add(saveDraftDiv);

                        Div saveAndNewDiv = new Div();
                        saveAndNewDiv.add(uiBinder.getSaveAndNewButton());
                        rightWidgets.add(saveAndNewDiv);

                        Div saveAndApprovDiv = new Div();
                        saveAndApprovDiv.add(uiBinder.getSaveAndApproveButton());
                        rightWidgets.add(saveAndApprovDiv);
                        if (Utils.hasPermission(PermissionConstants.PAYROLL_PAYSLIP_PDF) && result.getObjectID() != null) {
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

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public String getPropertyCode() {
        return Constants.SINGLE_PAYRUN_LIST;
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
