package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class RejectionReasonInvoices extends KpiModal {


    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();

    public VerticalPanel panel;
    private ArrayList<ReferenceItem> references;
    private LinkedHashMap<KpiRadioButton, Integer> radiobuttons;
    private LinkedHashMap<KpiRadioButton, TextArea2> descriptions;
    private TextArea2 note;
    private String rejectionStatus;
    private Integer entityId;
    private String fromType;
    private WfmButton2 save;
    private Command saveRejection;


    public RejectionReasonInvoices(SelectItem params) {
        this.fromType = params.getCode();
        this.rejectionStatus = params.getDescription();
        this.entityId = params.getEntityId();
        setTitle(accountingMessages.pleaseSpecifyRejectionReason());
        AllInOneService.App.get().getReferenceChildren(Constants.SALE_ORDER.equals(fromType) ? "SALES_ORDER_REJECTION_REASON" : "SALES_QUOTE_REJECTION_REASON", new AbstractAsyncCallback<ArrayList<ReferenceItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(ArrayList<ReferenceItem> result) {
                references = result;
                init();
            }
        });
    }

    public void init() {
        panel = new VerticalPanel();
        panel.addStyleName("options-stack-top");
        radiobuttons = new LinkedHashMap<>();
        descriptions = new LinkedHashMap<>();

        if (references != null && references.size() > 0) {
            for (ReferenceItem referenceItem : references) {
                KpiRadioButton radioButton = new KpiRadioButton("reference", referenceItem.getName());
                radiobuttons.put(radioButton, referenceItem.getId());

                TextArea2 textArea2 = new TextArea2(3000);
                textArea2.setVisible(false);
                if (referenceItem.isRequiredComment()) {
                    descriptions.put(radioButton, textArea2);
                }

                radioButton.addValueChangeHandler(valueChangeEvent -> {
                    for (TextArea2 textArea : descriptions.values()) {
                        textArea.setVisible(false);
                    }
                    if (radioButton.getValue() && referenceItem.isRequiredComment()) {
                        textArea2.setVisible(true);
                    }
                });

                if (referenceItem.isRequiredComment()) {
                    panel.add(radioButton);
                    panel.add(textArea2);
                } else {
                    panel.add(radioButton);
                }
            }
        } else {
            note = new TextArea2(3000);
            note.setHeight(250);
            panel.add(note);
        }
        add(panel);

        addButton(new WfmButton2(wfmStrings.cancel(), clickEvent -> {
            close();
        }));

        save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> save());
        addButton(save);
        open();
    }

    private void save() {
        save.setEnabled(false);
        if (validate()) {
            Info.warn(accountingMessages.pleaseSpecifyRejectionReason());
            return;
        }
        if (radiobuttons != null && radiobuttons.size() > 0) {
            for (KpiRadioButton radioButton : radiobuttons.keySet()) {
                if (radioButton != null && radioButton.getValue()) {
                    String note = null;
                    Integer selectedReference = radiobuttons.get(radioButton);

                    if (descriptions != null && descriptions.get(radioButton) != null) {
                        TextArea2 textArea2 = descriptions.get(radioButton);
                        note = textArea2.getText();
                    }
                    rejectQuote(note, selectedReference);
                }
            }
        } else {
            rejectQuote(note.getText(), null);
        }
    }

    public boolean validate() {
        boolean errorFound = true;
        if (radiobuttons != null && radiobuttons.size() > 0) {
            for (KpiRadioButton radioButton : radiobuttons.keySet()) {
                if (radioButton != null && radioButton.getValue()) {
                    errorFound = false;

                    if (descriptions != null && descriptions.get(radioButton) != null) {
                        TextArea2 textArea2 = descriptions.get(radioButton);
                        errorFound = !Validation.validateTextAreaRequired(textArea2);
                    }
                    break;
                }
            }
        }

        if (note != null) {
            if (!Utils.isNullOrEmpty(note.getText())) {
                errorFound = false;
            }
        }
        return errorFound;
    }

    private void rejectQuote(String rejectionReason, Integer selectedReference) {
        LoadingPanel.loading(true);
        QuoteService.App.get().changeQuoteStatus(entityId, rejectionStatus, new SelectItem(selectedReference, rejectionReason), false, new AbstractAsyncCallback() {
            public void failure(Throwable caught) {
                save.setEnabled(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Object result) {
                save.setEnabled(true);
                LoadingPanel.loading(false);
                saveRejection.execute();
                close();
            }
        });
    }

    public void setSaveRejection(final Command saveRejection) {
        this.saveRejection = saveRejection;
    }
}
