package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.CategoryObject;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: 17.02.2009
 * Time: 23:16:34
 * To change this template use File | Settings | File Templates.
 */
public class AddCategoryView extends View {
    private static final PayrollStrings payrollStrings = GWT.create(PayrollStrings.class);

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private TextBox name;
    private TextBox code;
    private DataListBox type;
    private KpiCheckBox taxable;
    private KpiCheckBox niable;

    private WfmForm form;
    private WfmForm.Field nameField;
    private WfmForm.Field codeField;
    private WfmForm.Field typeField;
    private WfmForm.Field taxableField;
    private WfmForm.Field niableField;

    public AddCategoryView() {
        super("addcategory", wfmStrings.addCategory());

    }

    private KpiModal shell;
    private Command provider;
    private String typeString;
    private boolean advancePayment;

    public AddCategoryView(Command provider, String typeString) {
        super("addcategory", wfmStrings.addCategory());
        this.provider = provider;
        this.typeString = typeString;
        shell = new KpiModal();
        shell.setSize(600, 400);
        onInitialize();
    }

    public AddCategoryView(Command provider, String typeString, boolean advancePayment) {
        this(provider, typeString);
        this.advancePayment = advancePayment;
    }

    public AddCategoryView(Command command) {
        this.provider = command;
        shell = new KpiModal();
        shell.setSize(500, 300);
        LoadingPanel.loading(true);
        asyncOnInitialize(new AbstractAsyncCallback<Widget>() {
            public void failure(Throwable reason) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.addCategory() + " (" + reason + ")", Info.Type.WARNING);
            }

            public void success(Widget result) {
                LoadingPanel.loading(false);
            }
        });
    }

    public static final String PAYMENT_STRING = "Payment";
    public static final String DEDUCTION_STRING = "Deduction";

    protected Widget onInitialize() {

        form = new WfmForm();
        form.setLabelSize("150px");

        name = new TextBox();
        name.addStyleName(DEFAULT_WIDTH);
        code = new TextBox();
        code.addStyleName(DEFAULT_WIDTH);
        type = new DataListBox();
        type.addStyleName(DEFAULT_WIDTH);
        /*type.addItem("payment","Payment");
        type.addItem("deduction","Deduction");*/
        type.setItems(new SelectItem[]{new SelectItem(Constants.PAYMENT, PAYMENT_STRING), new SelectItem(Constants.DEDUCTION, DEDUCTION_STRING)});
        if (typeString != null) {
            for (int i = 0; i < type.getItems().length; i++) {
                if (typeString.equals(type.getItems()[i].getName())) {
                    type.setSelected(type.getItems()[i].getId());
                }
            }
        }
        /*type.addListItem(new SelectItem(Constants.PAYMENT,"Payment"));
        type.addListItem(new SelectItem(Constants.DEDUCTION,"Deduction"));*/
        taxable = new KpiCheckBox();
        niable = new KpiCheckBox();
        /*saveandanother = new Button("Save & Add Another");
        saveandclose = new Button("Save & Close");*/

        nameField = form.addField(wfmStrings.categoryName(), name, true);
        codeField = form.addField(wfmStrings.code(), code, true);
        typeField = form.addField(wfmStrings.type(), type, true);
        taxableField = form.addField(wfmStrings.taxable(), taxable);
        niableField = form.addField(wfmStrings.niable(), niable);
        WfmButton2 saveAdd = new WfmButton2(wfmStrings.saveAndNew(), clickEvent -> {
            if (validate()) {
                CategoryObject category = new CategoryObject();
                category.setCode(code.getText());
                category.setName(name.getText());
                category.setNiable(niable.isChecked());
                category.setTaxable(taxable.isChecked());
                category.setType(type.getSelectedItem().getName());
                category.setArabic(Utils.isUAECompany());
                category.setAdvancePayment(advancePayment);
                PayrollService.App.get().createCategory(category, new AbstractAsyncCallback() {
                    public void failure(Throwable caught) {
                        LoadingPanel.loading(false);
                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    }

                    public void success(Object result) {
                        LoadingPanel.loading(false);
                        Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.changes()), Info.Type.INFO);
                        name.setText("");
                        code.setText("");
                        type.clearSelected();
                        taxable.setChecked(false);
                        niable.setChecked(false);
                    }
                });
            }
        });
        WfmButton2 saveClose = new WfmButton2(wfmStrings.save(), clickEvent -> {
            if (validate()) {
                CategoryObject category = new CategoryObject();
                category.setCode(code.getText());
                category.setName(name.getText());
                category.setNiable(niable.isChecked());
                category.setTaxable(taxable.isChecked());
                category.setType(type.getSelectedItem().getName());
                category.setAdvancePayment(advancePayment);
                category.setArabic(Utils.isUAECompany());
                PayrollService.App.get().createCategory(category, new AbstractAsyncCallback() {
                    public void failure(Throwable caught) {
                        LoadingPanel.loading(false);
                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    }

                    public void success(Object result) {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PAYMENT_DEDUCTION_CATEGORY_ADD, result, AddCategoryView.this);
                        LoadingPanel.loading(false);
                        Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.changes()), Info.Type.INFO);
                        if (shell != null) {
                            shell.close();
                        } else {
                            closeTab();
                        }
                    }
                });
            }
        });
        FlexTable buttonTable = new FlexTable();
        buttonTable.setWidget(0, 0, saveAdd);
        buttonTable.setWidget(0, 1, saveClose);
        buttonTable.setWidth("340px");
        form.addField(null, buttonTable);
        if (shell != null) {
            shell.add(form);
            shell.addCloseHandler(popupPanelCloseEvent -> provider.execute());
            shell.open();
        } else {
            add(form);
        }
        name.setText("");
        code.setText("");
        taxable.setChecked(false);
        niable.setChecked(false);
        return null;
    }

    public String getIconStyle() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private boolean validate() {
        int errors = 0;
        form.cleanupErrors();
        if (!Validation.validateTextBoxRequired(name, nameField)) {
            errors++;
        }
        if (!Validation.validateTextBoxRequired(name, nameField)) {
            errors++;
        }
        if (!Validation.validateTextBoxRequired(code, codeField)) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(type, typeField, wfmStrings.pleaseSpecifyType())) {
            errors++;
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
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
