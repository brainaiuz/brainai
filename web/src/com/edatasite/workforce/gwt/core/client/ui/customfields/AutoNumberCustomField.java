package com.edatasite.workforce.gwt.core.client.ui.customfields;

import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;

public class AutoNumberCustomField extends Composite {

    private HTML numberLabel;
    private CompanyCustomFieldItem customFieldItem;
    private boolean isEditView = false;

    public AutoNumberCustomField(CompanyCustomFieldItem customFieldItem, boolean... isEditView) {
        this.customFieldItem = customFieldItem;
        this.isEditView = isEditView != null && isEditView.length > 0 && isEditView[0];
        initialize();
    }

    private void initialize() {
        numberLabel = new HTML();
        numberLabel.addStyleName("form-control");
        if (!isEditView) {
            fetchNumberingValue();
        }
        initWidget(numberLabel);
    }

    private void fetchNumberingValue() {
        CommonService.App.get().getMaxValueOfAutoNumbering(customFieldItem, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(String result) {
                numberLabel.setHTML(result);
            }
        });
    }

    public void setText(String value) {
        this.numberLabel.setText(value);
    }

    public String getText() {
        return this.numberLabel.getText();
    }
}
