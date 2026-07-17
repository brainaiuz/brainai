package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.KeyValueStruct;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.EmployerSettings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.AdditionalPayment;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.ui.view.payslip.AdditionalPaymentUIBinder;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.List;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.ADDITIONAL_PAYMENT_ALLOWANCES;

/**
 * Created by Shohruh on 28 Oct 2016.
 */
public class AddEditAdditionalPaymentView extends FooteredView implements Colapse, FittedContent {

    private static final PayrollStrings payrollStrings = GWT.create(PayrollStrings.class);

    private Integer id;
    private Integer employeeId;
    private boolean summaryView;
    private boolean copyView;
    private String type;
    private String defaultPaymentType = "PLEASE_SELECT";
    private ArrayList<PaymentDeductionSelectItem> selectedCategories = new ArrayList<>();
    private String categoryType;

    public AddEditAdditionalPaymentView(String type) {
        super("addAdditionalPayment");
        setDescription(property.getSingular(wfmStrings.additionalPayment()));
        this.type = type;
    }

    public AddEditAdditionalPaymentView(Integer id, String type) {
        this(id, null, type);
    }

    public AddEditAdditionalPaymentView(Integer id, String param1, String type) {
        this(id, param1, null, type);
    }

    public AddEditAdditionalPaymentView(Integer id, String param1, String param2, String type) {
        super("addAdditionalPayment");
        setDescription(property.getSingular(wfmStrings.additionalPayment()));
        this.id = id;
        if (param1 != null && param1.equals("copy")) {
            copyView = true;
        } else if (param1 != null) {
            summaryView = true;
        }
        employeeId = param2 != null ? Integer.valueOf(param2) : null;
        this.type = type;
    }

    public AddEditAdditionalPaymentView(String type, String categoryType) {
        super("addAdditionalPayment");
        setDescription(property.getSingular(wfmStrings.additionalPayment()));
        this.type = type;
        this.categoryType = categoryType;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected Widget onInitialize() {
        getPayrollEmployerSettings();
//        createAdditionalPayment();
//        final AdditionalPaymentUIBinder uiBinder = new AdditionalPaymentUIBinder(() -> AddEditAdditionalPaymentView.this, id, employeeId, summaryView, type);
//        uiBinder.init();
//        Div formDiv = new Div("add-form");
//        formDiv.add(uiBinder.getRootElement());
//        formDiv.add(new ViewFooter(new IFooteredView() {
//            @Override
//            public List<Widget> getFooterLeftSideWidgets() {
//                return null;
//            }
//
//            @Override
//            public List<Widget> getFooterRightSideWidgets() {
//                List<Widget> rightWidgets = new ArrayList<>();
//
//                rightWidgets.add(uiBinder.getPdfDiv());
//                rightWidgets.add(uiBinder.getDraftDiv());
//                rightWidgets.add(uiBinder.getEditDiv());
//                rightWidgets.add(uiBinder.getDeclineDiv());
//                rightWidgets.add(uiBinder.getSubmitDiv());
//                rightWidgets.add(uiBinder.getApproveDiv());
//
//                return rightWidgets;
//            }
//        }));
//        add(formDiv);
//
//        ListingFilterParameter fp = new ListingFilterParameter();
//        fp.setObjectId(id);
//        fp.setEmployeeId(employeeId);
//        PayrollService.App.get().getAdditionalPaymentData(fp, new AsyncCallback<AdditionalPayment>() {
//            @Override
//            public void onFailure(Throwable throwable) {
//
//            }
//
//            @Override
//            public void onSuccess(AdditionalPayment data) {
//                if (id != null) {
//                    uiBinder.fillFormData(data);
//                } else {
//                    uiBinder.initDefaults(data);
//                }
//                LoadingPanel.loading(false);
//            }
//        });
        return null;
    }

    public interface AdditionalPaymentInterface {
        View getView();
    }

    private void getPayrollEmployerSettings() {
        LoadingPanel.loading(true);
        PayrollService.App.get().getCompanyPayrollSettings(new AsyncCallback<EmployerSettings>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(EmployerSettings employerSettings) {
                LoadingPanel.loading(false);
                for (KeyValueStruct setting : employerSettings.getSettings()) {
                    if (CustomFormConstants.EMPLOYER_SETTINGS.DEFAULT_ADDITIONAL_TYPE_SETTINGS.equals(setting.getKey()) && setting.getValue() != null) {
                        switch (setting.getValue()) {
                            case "0":
                                defaultPaymentType = "BASIC_SALARY";
                                break;
                            case "1":
                                defaultPaymentType = "BASIC_SALARY_ALLOWANCE";
                                break;
                            case "2":
                                defaultPaymentType = "FIXED_AMOUNT";
                                break;
                            default:
                                defaultPaymentType = "PLEASE_SELECT";
                                break;
                        }
                    }
                }
                selectedCategories = employerSettings.getAllowances(ADDITIONAL_PAYMENT_ALLOWANCES);
                createAdditionalPayment();
            }
        });

    }

    private void createAdditionalPayment() {
        final AdditionalPaymentUIBinder uiBinder = new AdditionalPaymentUIBinder(() -> AddEditAdditionalPaymentView.this, id, employeeId, summaryView, type, categoryType, copyView);

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setObjectId(id);
        fp.setEmployeeId(employeeId);
        LoadingPanel.loading(true);
        PayrollService.App.get().getAdditionalPaymentData(fp, new AsyncCallback<AdditionalPayment>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(AdditionalPayment data) {
                init(data, uiBinder);
                LoadingPanel.loading(false);
            }
        });
    }

    private void init(AdditionalPayment data, AdditionalPaymentUIBinder uiBinder) {
        uiBinder.itemTableColumnConfig(data);
        uiBinder.initTables(true);
        uiBinder.setDefaultPaymentType(defaultPaymentType);
        uiBinder.setSelectedCategories(selectedCategories);
        uiBinder.init();
        Div formDiv = new Div("add-form");
        formDiv.add(uiBinder.getRootElement());
        formDiv.add(new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                List<Widget> leftSideWidgets = new ArrayList<>();

                FooterInformer informer = new FooterInformer(SvgEnum.docHistory, wfmStrings.historyAndNotes(), uiBinder.getNoteHistoryWidget());
                informer.setInitialClasses("informer-item history-notes-container");
                leftSideWidgets.add(informer);
                return leftSideWidgets;
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                List<Widget> rightWidgets = new ArrayList<>();

                rightWidgets.add(uiBinder.getPdfDiv());
                rightWidgets.add(uiBinder.getDraftDiv());
                rightWidgets.add(uiBinder.getEditDiv());
                rightWidgets.add(uiBinder.getDeclineDiv());
                rightWidgets.add(uiBinder.getSubmitDiv());
                rightWidgets.add(uiBinder.getApproveDiv());

                return rightWidgets;
            }
        }));
        add(formDiv);

        if (id != null) {
            uiBinder.fillFormData(data);
        } else {
            uiBinder.initDefaults(data);
        }
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

    @Override
    public String getPropertyCode() {
        return Constants.ADDITIONAL_PAYMENT_LIST;
    }
}
