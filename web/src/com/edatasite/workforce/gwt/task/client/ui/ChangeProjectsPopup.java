package com.edatasite.workforce.gwt.task.client.ui;

import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;

import java.util.ArrayList;

public class ChangeProjectsPopup extends KpiModal {
    private ArrayList<Integer> itemIDs;
    private CRMLookUp projects;
    private WfmButton2 save, cancel;

    public ChangeProjectsPopup() {
        setTitle(wfmStrings.projects());
        setWidth(350);
        init();
    }

    private void init() {
        projects = new CRMLookUp(LookUpConstants.PROJECT);
        projects.addStyleName(Constants.DEFAULT_WIDTH);
        add(projects);

        save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> {
            try {
                save();
                itemIDs = new ArrayList<>();
            } catch (NumberExistingException e) {
                e.printStackTrace();
            }
        });
        cancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);
        cancel.addClickHandler(clickEvent -> {
            close();
            projects.clear();
            itemIDs = new ArrayList<>();
        });
        addButton(cancel);
        addButton(save);
    }

    private void save() throws NumberExistingException {
        if(!Validation.validateLookUpRequired(projects)){
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return;
        }
        save.setEnabled(false);
        cancel.setEnabled(false);
        LoadingPanel.loading(true, ChangeProjectsPopup.this);

        TaskService.App.get().updateTasksProject(getItemIDs(), projects.getSelectedItemID(), new AbstractAsyncCallback<Void>() {
            public void failure(Throwable caught) {
                saved(false);
            }

            public void success(Void result) {
                saved(true);
            }
        });
    }

    private void saved(boolean success) {
        LoadingPanel.loading(false);
        save.setEnabled(true);
        cancel.setEnabled(true);
        if (success) {
            if (listRefresh != null) {
                listRefresh.refreshList();
            }
            close();
            projects.clear();
        }
    }

    public ArrayList<Integer> getItemIDs() {
        if (itemIDs == null) {
            itemIDs = new ArrayList<>();
        }
        return itemIDs;
    }

    private TaskListRefresh listRefresh;

    public interface TaskListRefresh {
        void refreshList();
    }

    public void setListRefresh(TaskListRefresh listRefresh) {
        this.listRefresh = listRefresh;
    }
}
