package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.rpc.PaymentMethodItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiTextArea;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by Omonullo Abdullaev on 08.04.16.
 */
public class AddPaymentMethodView extends View implements Constants, AccountingConstants, Colapse {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingServiceAsync accountingService =  AccountingService.App.get();

    private Integer objectID;

    WfmForm form;
    private TextBox name;
    private KpiTextArea description;

    private final Command closeCommand;

    public AddPaymentMethodView(Command closeCommand) {
        super("paymentmethodadd", accountingStrings.addPaymentMethod());
        this.closeCommand = closeCommand;
    }

    public AddPaymentMethodView(Integer objectID, Command closeCommand) {
        super("edit", accountingStrings.editPaymentMethod());
        this.objectID = objectID;
        this.closeCommand = closeCommand;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected Widget onInitialize() {
        initWidgetFields();
        initForm();
        fillFormWithData();
        return null;
    }

    private void initWidgetFields(){
        form = new WfmForm();
        form.setStyleName("padding10 fixed-layout");
        form.setLabelSize("200px");
        name = new TextBox();
        name.ensureDebugId("name-TextBox");
        form.addField(wfmStrings.name(), name);
        description = new KpiTextArea();
        description.ensureDebugId("description-textArea");
        form.addField(wfmStrings.description(), description);
    }

    private void initForm(){
        add(form);
    }

    private void fillFormWithData(){
        if(objectID != null) {
            accountingService.getPaymentMethodById(objectID, new AsyncCallback<PaymentMethodItem>() {
                @Override
                public void onFailure(Throwable throwable) {
                    Info.warn(wfmStrings.sorrySomethingWentWrong());
                }

                @Override
                public void onSuccess(PaymentMethodItem result) {
                    if(result != null){
                        name.setValue(result.getName() != null ? result.getName() : "");
                        description.setText(result.getDescription() != null ? result.getDescription() : "");
                    }
                }
            });
        }
    }

    protected boolean save(){
        if(validate()){
            LoadingPanel.loading(true);
            PaymentMethodItem pmi = new PaymentMethodItem();
            pmi.setObjectID(objectID);
            pmi.setName(name.getValue() != null ? name.getValue() : "");
            pmi.setDescription(description.getText() != null ? description.getText() : "");
            accountingService.savePaymentMethod(pmi, new AsyncCallback<Integer>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    Info.warn(wfmStrings.sorrySomethingWentWrong());
                    if (closeCommand != null) {
                        closeCommand.execute();
                    }
                }

                @Override
                public void onSuccess(Integer result) {
                    LoadingPanel.loading(false);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PAYMENT_METHOD_ADD, result, AddPaymentMethodView.this);
                    if (closeCommand != null) {
                        closeCommand.execute();
                    }
                }
            });
            return true;
        }
        return false;
    }

    private Boolean validate(){
        boolean error = (name != null) && !Validation.validateTextBoxRequired(name);
        return !error;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
