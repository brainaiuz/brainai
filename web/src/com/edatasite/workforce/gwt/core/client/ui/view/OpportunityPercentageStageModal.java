package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityListItem;
import com.edatasite.workforce.gwt.materialkanban.client.KanbanBoard;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.ArrayList;
import java.util.LinkedHashMap;


public class OpportunityPercentageStageModal extends KpiModal {

    public VerticalPanel panel;
    private ArrayList<ReferenceItem> references;
    private LinkedHashMap<KpiRadioButton, Integer> radiobuttons;
    private LinkedHashMap<KpiRadioButton, TextArea2> descriptions;
    private SelectItem targetColumnLayoutData;
    private TextArea2 note;
    private Integer itemId;
    private Integer widgetIndex;
    private Integer prevItemId;
    private Integer afterItemId;
    private KanbanBoard.OnDropCard onDropCard;
    private OpportunityListItem opportunityListItem;
    private boolean isKanban = false;
    private boolean requiredComment = false;
    private boolean zeroPercentage = false;
    private boolean fromView = false;

    public OpportunityPercentageStageModal(SelectItem targetColumnLayoutData, Integer itemId, Integer widgetIndex, Integer prevItemId, Integer afterItemId, KanbanBoard.OnDropCard onDropCard) {
        this.targetColumnLayoutData = targetColumnLayoutData;
        this.itemId = itemId;
        this.widgetIndex = widgetIndex;
        this.prevItemId = prevItemId;
        this.afterItemId = afterItemId;
        this.onDropCard = onDropCard;
        this.zeroPercentage = targetColumnLayoutData != null && "0".equals(targetColumnLayoutData.getDescription());
        this.isKanban = true;
        getData();
    }

    public OpportunityPercentageStageModal(SelectItem targetColumnLayoutData, Integer itemId, Integer widgetIndex, Integer prevItemId, Integer afterItemId) {
        this.targetColumnLayoutData = targetColumnLayoutData;
        this.itemId = itemId;
        this.widgetIndex = widgetIndex;
        this.prevItemId = prevItemId;
        this.afterItemId = afterItemId;
        this.zeroPercentage = true;
        this.isKanban = true;
        getData();
    }


    public OpportunityPercentageStageModal(OpportunityListItem item, boolean requiredComment, boolean zeroPercentage) {
        this.opportunityListItem = item;
        this.isKanban = false;
        this.requiredComment = requiredComment;
        this.zeroPercentage = zeroPercentage;
        getData();
    }

    public OpportunityPercentageStageModal(OpportunityListItem item, boolean requiredComment, boolean zeroPercentage, boolean fromView) {
        this.opportunityListItem = item;
        this.isKanban = false;
        this.requiredComment = requiredComment;
        this.zeroPercentage = zeroPercentage;
        this.fromView = fromView;
        getData();
    }

    private void getData() {
        String title = "";

        switch (LocaleInfo.getCurrentLocale().getLocaleName()) {
            case "uz":
                title = Property.get(Constants.Opportunities, wfmStrings.opportunity()) + wfmStrings.reasonForLosing();
                break;
            case "ru":
                title = wfmStrings.reasonForLosing();
                break;
            default:
                title = wfmStrings.reasonForLosing() + " " + Property.get(Constants.Opportunities, wfmStrings.opportunity()).toLowerCase();
                break;
        }
        setTitle(wfmStrings.addNote());
        if (zeroPercentage) {
            setTitle(title);
            AllInOneService.App.get().getReferenceChildren("_OPPORTUNITY_SUB_STAGE", new AbstractAsyncCallback<ArrayList<ReferenceItem>>() {
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
            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_OPPORTUNITY_LIST_RELOAD, false, OpportunityPercentageStageModal.this);
            close();
        }));

        WfmButton2 send = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> save());
        addButton(send);
        open();
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
                    Integer selectedReference = radiobuttons.get(radioButton);

                    if (descriptions != null && descriptions.get(radioButton) != null) {
                        TextArea2 textArea2 = descriptions.get(radioButton);
                        note = textArea2.getText();
                    }

                    if (isKanban) {
                        targetColumnLayoutData.setEntityId(selectedReference);
                        targetColumnLayoutData.setCategory(note);
                        saveKanbanOrder();
                    } else {
                        opportunityListItem.setSelectedSubStageId(selectedReference);
                        opportunityListItem.setNote(note);
                        saveOpportunityCell();
                    }
                }
            }
        } else {
            if (isKanban) {
                targetColumnLayoutData.setCategory(note.getText());
                saveKanbanOrder();
            } else {
                opportunityListItem.setNote(note.getText());
                saveOpportunityCell();
            }
        }
    }

    private void saveKanbanOrder() {
        CRMService.App.get().changeOpportunityKanbanOrder(targetColumnLayoutData, itemId, widgetIndex,
                prevItemId, afterItemId, new AsyncCallback<Integer>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        if (onDropCard != null) {
                            onDropCard.onDropCard();
                        }
                        Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.opportunity()), Info.Type.INFO);
                        close();
                    }
                });
    }

    private void saveOpportunityCell() {
        CRMService.App.get().saveOppotunityEditCellValue(opportunityListItem, OpportunityListItem.STAGE, new AbstractAsyncCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.opportunity()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_OPPORTUNITY_LIST_RELOAD, true, OpportunityPercentageStageModal.this);
                if (fromView) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_OPPORTUNITY_LOAD_STAGE_HISTORY, true, OpportunityPercentageStageModal.this);
                }
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
}
