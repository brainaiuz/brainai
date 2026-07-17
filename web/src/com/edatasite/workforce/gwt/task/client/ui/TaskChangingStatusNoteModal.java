package com.edatasite.workforce.gwt.task.client.ui;

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
import com.edatasite.workforce.gwt.materialkanban.client.KanbanBoard;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.edatasite.workforce.gwt.task.client.rpc.TaskServiceAsync;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TaskChangingStatusNoteModal extends KpiModal {

    private static final TaskServiceAsync taskService = TaskService.App.get();

    public VerticalPanel panel;
    private SelectItem targetColumnLayoutData;
    private TextArea2 note;
    private Integer itemId;
    private Integer widgetIndex;
    private Integer prevItemId;
    private Integer afterItemId;
    private Integer taskId;
    private Integer statusId;
    private KanbanBoard.OnDropCard onDropCard;
    private boolean isKanban = false;
    private boolean fromView = false;

    public TaskChangingStatusNoteModal(SelectItem targetColumnLayoutData, Integer itemId, Integer widgetIndex, Integer prevItemId, Integer afterItemId, KanbanBoard.OnDropCard onDropCard) {
        this.targetColumnLayoutData = targetColumnLayoutData;
        this.itemId = itemId;
        this.widgetIndex = widgetIndex;
        this.prevItemId = prevItemId;
        this.afterItemId = afterItemId;
        this.onDropCard = onDropCard;
        this.isKanban = true;
        init();
    }

    public TaskChangingStatusNoteModal(Integer taskId, Integer statusId, boolean fromView) {
        this.taskId = taskId;
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
            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TASK_ADD, null, TaskChangingStatusNoteModal.this);
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
            changeTaskStatus(taskId, statusId, note.getText());
        }
    }

    private void saveKanbanOrder() {
        taskService.changeTaskKanbanOrder(targetColumnLayoutData, itemId, prevItemId, afterItemId, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Void aVoid) {
                if (onDropCard != null) {
                    onDropCard.onDropCard();
                }
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.task()), Info.Type.INFO);
                close();
            }

        });
    }

    private void changeTaskStatus(Integer taskId, Integer statusId, String note) {
        LoadingPanel.loading(true);
        taskService.updateTaskStatus(taskId, statusId, note, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(Void aVoid) {
                LoadingPanel.loading(false);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TASK_EDIT, aVoid, TaskChangingStatusNoteModal.this);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_DASHBOARD_TASK_REFRESH, aVoid, TaskChangingStatusNoteModal.this);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TASK_STATUS_CHANGES, aVoid, TaskChangingStatusNoteModal.this);
                if (fromView) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TASK_LOAD_STATUS_HISTORY, aVoid, TaskChangingStatusNoteModal.this);
                }
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.task()), Info.Type.INFO);
                close();
            }
        });
    }

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
