package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import static com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC.MINIMUM_WAGE_FORM;

public class AddMinimumWageView extends CustomForm implements Constants, FittedContent, Colapse {
    private static final PayrollStrings payrollStrings = PayrollStrings.App.get();
    private DatePicker effectiveDate;
    private TextBox amount;
    private DataListBox type;
    private final Integer id;

    public AddMinimumWageView(Integer id) {
        super("addMinimumWage", payrollStrings.minimumWage());
        this.id = id;
    }

    protected Widget onInitialize() {
        super.onInitialize();
        registerFields();
        return null;
    }

    private void registerFields() {
        effectiveDate = new DatePicker();

        amount = new TextBox();
        Validation.addNumericKeyboardListener(amount, 2);

        type = new DataListBox();
        type.setWithoutNullLabel(true);
        type.setItems(new SelectItem[]{new SelectItem(0, wfmStrings.internal(), INTERNAL), new SelectItem(1, payrollStrings.governmental(), GOVERNMENTAL)});

        addField(EFFECTIVE_DATE, effectiveDate, getTitle(wfmStrings.effectiveDate(), true));
        addField(AMOUNT, amount, getTitle(wfmStrings.amount(), true));
        addField(TYPE, type, getTitle(wfmStrings.type(), true));
        show();
    }

    private void save() {
        enableButton(false);
        if (!validate()) {
            enableButton(true);
            return;
        }
        LoadingPanel.loading(true);
        ProfileService.App.get().saveMinimumWage(getValues(), new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                enableButton(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(Void unused) {
                enableButton(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.messSuccessfullySaved());
                closeTab();
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_MINIMUM_WAGE_ADD, null, AddMinimumWageView.this);
            }
        });
    }

    private boolean validate() {
        int errors = 0;
        clearErrorStyle();

        errors += markAsError(EFFECTIVE_DATE, effectiveDate, !Validation.validateDate(effectiveDate));
        errors += markAsError(AMOUNT, amount, !Validation.validateTextBoxRequired(amount));
        errors += markAsError(TYPE, type, !Validation.validateDataListBoxRequired(type));

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private SelectItem getValues() {
        SelectItem item = new SelectItem();
        item.setId(id);
        item.setDate(effectiveDate.getDateAsNonConvertable());
        item.setQtyAmount(Utils.parseToBigDecimal(amount.getText()));
        item.setCode(type.getSelectedItem().getDescription());

        return item;
    }

    @Override
    public String getIconStyle() {
        return "icon-edit";
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    @Override
    protected void addButtons() {
        addButton(new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, event -> save()));
    }

    @Override
    protected void getDataToFillFields() {
        if (id != null) {
            ProfileService.App.get().getMinimumWage(id, new AsyncCallback<SelectItem>() {
                @Override
                public void onFailure(Throwable caught) {

                }

                @Override
                public void onSuccess(SelectItem result) {
                    effectiveDate.setDate(result.getDate().getNonConvertedDate());
                    amount.setText(result.getQtyAmount().toString());
                    type.setSelectedByDescription(result.getCode());
                }
            });
        }
    }

    @Override
    protected String getFormID() {
        return MINIMUM_WAGE_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }
}
