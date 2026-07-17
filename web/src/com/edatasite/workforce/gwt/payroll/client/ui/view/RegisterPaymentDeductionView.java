package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.payroll.client.rpc.CategoryObject;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollClientUtils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import static com.finnetlimited.reportservice.core.client.ui.Constants.DEFAULT_WIDTH;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: 19.02.2009
 * Time: 23:41:32
 * To change this template use File | Settings | File Templates.
 */
public class RegisterPaymentDeductionView extends View {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private DataListBox categoryChooser;
    private DataListBox employeeList;
    private TextBox payAmount;
    private DatePicker payDate;

    private WfmForm form;

    private WfmForm.Field categoryChooserField;
    private WfmForm.Field employeeListField;
    private WfmForm.Field payAmountField;
    private WfmForm.Field payDateField;

    private final String nickDebudId = "register_payment_deducation_view_";

    public RegisterPaymentDeductionView() {
        super("addpayment", wfmStrings.registerPaymentDeduction());
    }

    protected Widget onInitialize() {
        form = new WfmForm();
        categoryChooser = new DataListBox();
        categoryChooser.addStyleName(DEFAULT_WIDTH);
        categoryChooser.ensureDebugId(nickDebudId + "categoryChooser");

        employeeList = new DataListBox();
        employeeList.addStyleName(DEFAULT_WIDTH);
        employeeList.ensureDebugId(nickDebudId + "employeeList");

        payAmount = new TextBox();
        payAmount.addStyleName(DEFAULT_WIDTH);
        payAmount.ensureDebugId(nickDebudId + "payAmount");

        payDate = new DatePicker(true);
        payDate.setWidth("100px");
        payDate.ensureDebugId(nickDebudId + "payDate");

        /*
        PayrollService.App.get().getCompanyEmployeesAsSelectItems(new AbstractAsyncCallback(){
            public void failure(Throwable throwable) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void success(Object o) {
                SelectItem[] employees = (SelectItem[])o;
                employeeList.setItems(employees);
            }
        });
        */
        SimpleLink addCategory = new SimpleLink(wfmStrings.addCategory(), SimpleLink.ADD_ICON, "payrollcategory|add/add");
        addCategory.setWordWrap(false);
        categoryChooserField = form.addField(wfmStrings.category(), new Widget[]{categoryChooser, addCategory}, true, 2);
        employeeListField = form.addField(wfmStrings.employee(), employeeList, true);
        String baseCurrency = Utils.getParam(Utils.BASE_CURRENCY);
        payAmountField = form.addField(wfmStrings.payamount() + ((baseCurrency != null && !"".equals(baseCurrency)) ? (" (" + baseCurrency + ") ") : ""), payAmount, true);
        payDateField = form.addField(wfmStrings.paydate(), new Widget[]{payDate}, true, 2);

        Validation.addNumericKeyboardListener(payAmount);

        form.addButton(new WfmButton2(wfmStrings.saveAndNew(), clickEvent -> {
            if (validate()) {
                PaymentDeductionObject paymentItem = new PaymentDeductionObject();
                paymentItem.setCategoryItem(new PaymentDeductionSelectItem(categoryChooser.getSelectedItem().getId(), categoryChooser.getSelectedItem().getName(), null, null));
                paymentItem.setEmployee(employeeList.getSelectedItem());
                paymentItem.setPaymentAmount(PayrollClientUtils.parseToBigDecimal(payAmount.getText()));
                paymentItem.setPaymentDate(payDate.getDate());
                LoadingPanel.loading(true);
                PayrollService.App.get().createPaymentDeduction(paymentItem, new AbstractAsyncCallback() {
                    public void failure(Throwable caught) {
                        LoadingPanel.loading(false);
                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    }

                    public void success(Object result) {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PAYMENT_DEDUCTION_ADD, result, RegisterPaymentDeductionView.this);
                        LoadingPanel.loading(false);
                        Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.changes()), Info.Type.INFO);
                        categoryChooser.clearSelected();
                        employeeList.clearSelected();
                        payAmount.setText("");
                        payDate.clearSelected();
                    }
                });
            }
        }));

        form.addButton(new WfmButton2(wfmStrings.save(), clickEvent -> {
            if (validate()) {
                PaymentDeductionObject paymentItem = new PaymentDeductionObject();
//                    paymentItem.setCategoryID(categoryChooser.getCategory().getId());
                paymentItem.setCategoryItem(new PaymentDeductionSelectItem(categoryChooser.getSelectedItem().getId(), categoryChooser.getSelectedItem().getName(), null, null));
                paymentItem.setEmployee(employeeList.getSelectedItem());
                paymentItem.setPaymentAmount(PayrollClientUtils.parseToBigDecimal(payAmount.getText()));
                paymentItem.setPaymentDate(payDate.getDate());
                //paymentItem.setStatusID();
                LoadingPanel.loading(true);
                PayrollService.App.get().createPaymentDeduction(paymentItem, new AbstractAsyncCallback() {

                    public void failure(Throwable caught) {
                        LoadingPanel.loading(false);
                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    }

                    public void success(Object result) {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PAYMENT_DEDUCTION_ADD, result, RegisterPaymentDeductionView.this);
                        LoadingPanel.loading(false);
                        Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.changes()), Info.Type.INFO);
                        closeTab();
                    }
                });
            }
        }));

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PAYMENT_DEDUCTION_CATEGORY_ADD, RegisterPaymentDeductionView.this, (sender, args) -> {
            if (args instanceof Integer) {
                initCategories((Integer) args);
            }
        });

        add(form);
        EmployeeService.App.get().getCompanyEmployeesAsSelectItems(new AbstractAsyncCallback<SelectItem[]>() {
            public void failure(Throwable throwable) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void success(SelectItem[] o) {
                SelectItem[] employees = o;
                employeeList.setItems(employees);
                employeeList.clearSelected();
            }
        });

        initCategories(null);
        return null;
    }

    private void initCategories(final Integer selectedCategoryId) {
        PayrollService.App.get().getCompanyCategories(Utils.isArabicCompany(), new AbstractAsyncCallback() {
            public void failure(Throwable throwable) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void success(Object o) {
                CategoryObject[] cats = (CategoryObject[]) o;
                SelectItem[] items = new SelectItem[cats.length];
                for (int i = 0; i < cats.length; i++) {
                    items[i] = new SelectItem(cats[i].getId(), cats[i].getName() + "/" + cats[i].getType());
                }
                categoryChooser.setItems(items);
                categoryChooser.clearSelected();
                if (selectedCategoryId != null) {
                    categoryChooser.setSelected(selectedCategoryId);
                }
            }
        });
    }

    private boolean validate() {
        int errors = 0;
        form.cleanupErrors();
//        if (!Validation.validateTextBoxRequired(categoryChooser.getTextBox(), categoryChooserField) && categoryChooser.getCategory()!=null) {
        if (!Validation.validateListBoxRequired(categoryChooser, categoryChooserField, "")) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(employeeList, employeeListField, "")) {
            errors++;
        }
        if (!Validation.validateTextBoxRequired(payAmount, payAmountField)) {
            errors++;
        }
        if (!Validation.validateDate(payDate, payDateField, true)) {
            errors++;
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    public String getIconStyle() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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

}
