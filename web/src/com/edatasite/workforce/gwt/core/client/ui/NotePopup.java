package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.Date;

public class NotePopup extends KpiModal implements Constants {
    private final Integer entityID;
    private final String entityType;
    private TextArea2 noteTextArea;
    private NotePopupCommand command;
    private final static AllInOneServiceAsync allInOneService = AllInOneService.App.get();



    public NotePopup(Integer entityID, String entityType, NotePopupCommand command) {
        this(entityID, entityType);
        this.command = command;
    }

    public NotePopup(Integer entityID, String entityType) {
        this.entityID = entityID;
        this.entityType = entityType;
        setTitle(wfmStrings.addNote());
        setWidth(400);
        init();
    }

    private void init() {
        noteTextArea = new TextArea2(1000);
        noteTextArea.setHeight(100);
        add(noteTextArea);
        addButton(new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT, clickEvent -> {
            if (command != null) {
                command.onCancel(noteTextArea.getText());
            }
            close();
        }));
        addButton(new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> save()));
        open();
    }

    private void save() {

        if (command == null) {
            if (!Validation.validateTextAreaRequired(noteTextArea.getTextArea())) {
                Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
                return;
            }
            HistoryListItem item = new HistoryListItem();
            item.setComment(noteTextArea.getText());
            item.setEventDate(new Date());
            LoadingPanel.loading(true);
            allInOneService.saveCrmNote(entityType, entityID, item, new AsyncCallback<Integer>() {
                @Override
                public void onFailure(Throwable caught) {
                    GWT.log(caught.getMessage());
                    LoadingPanel.loading(false);
                    close();
                }

                @Override
                public void onSuccess(Integer result) {
                    LoadingPanel.loading(false);
                    close();
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullyAdded(), wfmStrings.note()), Info.Type.INFO);
                }
            });
            return;
        }


        if (!Utils.isNullOrEmpty(noteTextArea.getText())) {

            if (command != null) {
                command.onSave(noteTextArea.getText());
            }
            close();
        } else {
            close();
            if (command != null) {
                command.onSaved(noteTextArea.getText());
            }
        }
    }
}
