package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.reconciliation;

import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

public class ImportStatementView extends FooteredView {
    private Integer id;
    private String bankAccounName;

    private Integer currentStep = 1;
    private Integer bankAccountAttachmentId;

    public ImportStatementView(Integer id,String bankAccounName) {
        this.id = id;
        this.bankAccounName = bankAccounName;
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

    public Widget onInitialize() {
        Widget widget = super.onInitialize();
        ImportStatementWidget importStatementWidget = new ImportStatementWidget(id,bankAccounName,ImportStatementView.this);
        add(importStatementWidget);
        return widget;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    public Integer getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(Integer currentStep) {
        this.currentStep = currentStep;
    }

    public Integer getBankAccountAttachmentId() {
        return bankAccountAttachmentId;
    }

    public void setBankAccountAttachmentId(Integer bankAccountAttachmentId) {
        this.bankAccountAttachmentId = bankAccountAttachmentId;
    }
}
