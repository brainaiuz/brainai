package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.materialkanban.client.KanbanBoard;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CaseStatusModal extends KpiModal {


    public VerticalPanel panel;
    private SelectItem targetColumnLayoutData;
    private TextArea2 note;
    private Integer itemId;
    private Integer widgetIndex;
    private Integer prevItemId;
    private Integer afterItemId;
    private Integer caseId;
    private Integer statusId;
    private KanbanBoard.OnDropCard onDropCard;
    private boolean isKanban = false;
    private boolean fromView = false;

    public CaseStatusModal(SelectItem targetColumnLayoutData, Integer itemId, Integer widgetIndex, Integer prevItemId, Integer afterItemId, KanbanBoard.OnDropCard onDropCard) {
        this.targetColumnLayoutData = targetColumnLayoutData;
        this.itemId = itemId;
        this.widgetIndex = widgetIndex;
        this.prevItemId = prevItemId;
        this.afterItemId = afterItemId;
        this.onDropCard = onDropCard;
        this.isKanban = true;
        init();
    }

    public CaseStatusModal(Integer caseId, Integer statusId, boolean fromView) {
        this.caseId = caseId;
        this.statusId = statusId;
        this.isKanban = false;
        this.fromView = fromView;
        init();
    }


    public void init() {
        setTitle(wfmStrings.addNote());
        panel = new VerticalPanel();
        panel.addStyleName("options-stack-top");

        note = new TextArea2(3000);
        note.setHeight(250);
        panel.add(note);

        add(panel);

        addButton(new WfmButton2(wfmStrings.cancel(), clickEvent -> {
            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CASE_ADD, null, CaseStatusModal.this);
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
        if (isKanban) {
            targetColumnLayoutData.setCategory(note.getText());
            saveKanbanOrder();
        } else {
            changeCaseStatus(caseId, statusId, note.getText());
        }
    }

    private void saveKanbanOrder() {
        CRMService.App.get().changeCaseKanbanOrder(targetColumnLayoutData, itemId, widgetIndex,
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
                        Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.cases()), Info.Type.INFO);
                        close();
                    }
                });
    }

    private void changeCaseStatus(Integer caseID, Integer statusId, String note) {
        LoadingPanel.loading(true);
        CRMService.App.get().updateCaseStatus(caseID, statusId, note, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(Void result) {
                LoadingPanel.loading(false);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CASE_ADD, null, CaseStatusModal.this);
                if (fromView) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CASE_LOAD_STAGE_HISTORY, null, CaseStatusModal.this);
                }
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.cases()), Info.Type.INFO);
                close();

            }
        });
    }


//    private void saveCaseCell() {
//        CRMService.App.get().saveOppotunityEditCellValue(opportunityListItem, OpportunityListItem.STAGE, new AbstractAsyncCallback<Boolean>() {
//            @Override
//            public void onSuccess(Boolean result) {
//                Info.show(wfmStrings.successfullySavedOnly(), Info.Type.INFO);
//                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_OPPORTUNITY_LIST_RELOAD, null, OpportunityPercentageStageModal.this);
//                if (fromView) {
//                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_OPPORTUNITY_LOAD_STAGE_HISTORY, null, OpportunityPercentageStageModal.this);
//                }
//                close();
//            }
//        });
//    }

    public boolean validate() {
        boolean errorFound = true;

        if (note != null) {
            if (!Utils.isNullOrEmpty(note.getText())) {
                errorFound = false;
            }
        }
        return errorFound;
    }
}
