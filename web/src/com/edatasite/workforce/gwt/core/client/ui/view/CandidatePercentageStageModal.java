package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.Property;
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
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.rpc.RecruitmentService;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class CandidatePercentageStageModal extends KpiModal {

    private final SelectItem targetColumnLayoutData;
    private final Integer itemId;
    public VerticalPanel panel;
    private ArrayList<ReferenceItem> references;
    private LinkedHashMap<KpiRadioButton, ReferenceItem> radiobuttons;
    private LinkedHashMap<KpiRadioButton, TextArea2> descriptions;
    private TextArea2 note;
    private boolean isKanban;
    private Integer placementId;
    private boolean isPlacement = false;
    private ContactListItem candidate;
    private boolean zeroPercentage = false;
    CommandWithParam saveAction;
    CommandWithParam cancelAction;
    private ModalCompleteListener modalCompleteListener;

    public CandidatePercentageStageModal(SelectItem targetColumnLayoutData, Integer itemId) {
        this.targetColumnLayoutData = targetColumnLayoutData;
        this.itemId = itemId;
        this.zeroPercentage = targetColumnLayoutData != null && "0".equals(targetColumnLayoutData.getDescription());
        getData();
    }

    public CandidatePercentageStageModal(Integer placementId, boolean isPlacement) {
        this.isPlacement = isPlacement;
        this.placementId = placementId;
        this.targetColumnLayoutData = null;
        this.itemId = null;

        this.isKanban = false;
        getData();
    }

    private void getData() {
        String title = "";

        switch (LocaleInfo.getCurrentLocale().getLocaleName()) {
            case "uz":
                title = Property.get(Constants.CANDIDATE, wfmStrings.candidate()) + wfmStrings.reasonForLosing();
                break;
            case "ru":
                title = wfmStrings.reasonForLosing();
                break;
            default:
                title = wfmStrings.reasonForLosing() + " " + Property.get(Constants.CANDIDATE, wfmStrings.candidate()).toLowerCase();
                break;
        }

        setTitle(wfmStrings.addNote());
        if (zeroPercentage || (isPlacement && placementId != null)) {
            setTitle(title);
            AllInOneService.App.get().getReferenceChildren("_CANDIDATE_SUB_STAGE", new AbstractAsyncCallback<ArrayList<ReferenceItem>>() {
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
        } else {
            init();
        }
    }

    public void init() {
        panel = new VerticalPanel();
        panel.addStyleName("options-stack-top");

        VerticalPanel blackListPanel = new VerticalPanel();
        blackListPanel.addStyleName("options-stack-top");
        blackListPanel.getElement().getStyle().setPadding(10, Style.Unit.PX);

        VerticalPanel whiteListPanel = new VerticalPanel();
        whiteListPanel.addStyleName("options-stack-top");
        whiteListPanel.getElement().getStyle().setPadding(10, Style.Unit.PX);

        radiobuttons = new LinkedHashMap<>();
        descriptions = new LinkedHashMap<>();

        if (references != null && references.size() > 0) {
            for (ReferenceItem referenceItem : references) {
                KpiRadioButton radioButton = new KpiRadioButton("reference", referenceItem.getName());
                radiobuttons.put(radioButton, referenceItem);

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

                if (referenceItem.getOrder() <= 50) {
                    if (referenceItem.isRequiredComment()) {
                        blackListPanel.add(radioButton);
                        blackListPanel.add(textArea2);
                    } else {
                        blackListPanel.add(radioButton);
                    }
                } else {
                    if (referenceItem.isRequiredComment()) {
                        whiteListPanel.add(radioButton);
                        whiteListPanel.add(textArea2);
                    } else {
                        whiteListPanel.add(radioButton);
                    }
                }

            }
        } else {
            note = new TextArea2(3000);
            note.setHeight(250);
            panel.add(note);
        }

        if (blackListPanel.getWidgetCount() > 0) {
            Label blackListLabel = new Label("Black List");
            blackListLabel.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
            panel.add(blackListLabel);
            panel.add(blackListPanel);
        }

        if (whiteListPanel.getWidgetCount() > 0) {
            Label whiteListLabel = new Label("White List");
            whiteListLabel.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
            panel.add(whiteListLabel);
            panel.add(whiteListPanel);
        }


        add(panel);

        addButton(new WfmButton2(wfmStrings.cancel(), clickEvent -> {
            close();
            cancelAction.execute(targetColumnLayoutData);
        }));

        WfmButton2 send = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent ->
                save()
        );
        addButton(send);
        open();
        getCandidate(itemId);
    }

    private void save() {
        if (validate()) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return;
        }
        if (radiobuttons != null && radiobuttons.size() > 0) {
            for (KpiRadioButton radioButton : radiobuttons.keySet()) {
                if (radioButton != null && radioButton.getValue()) {
                    String note = null;
                    ReferenceItem selectedReference = radiobuttons.get(radioButton);
                    note = selectedReference.getName();
                    if (descriptions != null && descriptions.get(radioButton) != null) {
                        TextArea2 textArea2 = descriptions.get(radioButton);
                        note = note + ": " + textArea2.getText();
                    }
                    if (isPlacement) {
                        SelectItem selectItem = new SelectItem();
                        selectItem.setEntityId(selectedReference.getId());
                        selectItem.setCategory(note);
                        savePlacement(Constants.PLACEMENT_STATUS_REJECTED, note, selectItem);
                    }

                    if (isKanban){
                        targetColumnLayoutData.setEntityId(selectedReference.getId());
                        targetColumnLayoutData.setCategory(note);
                        saveAction.execute(targetColumnLayoutData);
                    }


                }
            }
            close();
        }
    }

    public void save(CommandWithParam command) {
        saveAction = command;
    }

    public void cancel(CommandWithParam command) {
        cancelAction = command;
    }

    private void savePlacement(String status, String rejectionReason, SelectItem targetColumnLayoutData) {
        AllInOneService.App.get().updateStatusPlacement(placementId, status, rejectionReason, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Void result) {
//                AllInOneService.App.get().changeCandidateStatusByPlacmentId(Constants.HRMS.RECRUITMENT.CANDIDATE, placementId,
//                        ContactListItem._CANDIDATE_STATUS, ContactListItem.C_S_ON_HOLD, isPlacement, targetColumnLayoutData, new AbstractAsyncCallback<String>() {
//                            @Override
//                            public void onFailure(Throwable caught) {
//                                LoadingPanel.loading(false);
//                            }
//
//                            @Override
//                            public void onSuccess(String result) {
//                                LoadingPanel.loading(false);
//                            }
//                        });
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PLACEMENT_ADD_EDIT, result, CandidatePercentageStageModal.this);
                notifyCompleteListener();
                close();
            }
        });
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

    private void getCandidate(Integer id) {
        RecruitmentService.App.get().getCandidateById(id, new AsyncCallback<ContactListItem>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(ContactListItem contactListItem) {
                candidate = contactListItem;
            }
        });
    }

    public interface CommandWithParam {
        void execute(Object o);
    }

    public void setModalCompleteListener(ModalCompleteListener listener) {
        this.modalCompleteListener = listener;
    }

    private void notifyCompleteListener() {
        if (modalCompleteListener != null) {
            modalCompleteListener.onModalComplete();
        }
    }

    public interface ModalCompleteListener {
        void onModalComplete();
    }
}
