package com.edatasite.workforce.gwt.documents.client;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.TeamEmployees;
import com.edatasite.workforce.gwt.core.client.rpc.rbac.GroupMembersViewItem;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.filterparams.SuperPuperHandler;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.NTreeSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.TreeSelect;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.UserResource;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.ScrollPanel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Sherali
 */
public class UserAddDialog extends KpiModal {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private TreeSelect tree;
    private HashMap<Integer, UserResource> employees;

    /**
     * The widget's constructor.
     */
    public UserAddDialog() {
        setTitle(wfmStrings.addUser());
        employees = new HashMap<>();
        SuperPuperHandler<NTreeSelectItem> command = nTreeSelectItem -> {
            if (nTreeSelectItem.getParentItem() == null) {     //Tick ITEMs
                for (int i = 0; i < nTreeSelectItem.getChildCount(); i++) {
                    NTreeSelectItem selectedItem = (NTreeSelectItem) nTreeSelectItem.getChild(i);
                    onTreeItemSelection(selectedItem);
                }
            } else if (nTreeSelectItem.getChildCount() == 0) { //Child Item chnaged value
                //Besides, we have another selection handler, and this one is firing first, therefore we  have reverse value of checked.
                onTreeItemSelection(nTreeSelectItem);
            }

        };

        tree = new TreeSelect().setHandler(command);
        tree.getRadioButtons().setVisible(false);
        tree.setHeight("250px");

        DocumentsView.get().getDocumentsService().getCompanyEmployeesWithTeams(false, new AbstractAsyncCallback<ArrayList<TeamEmployees>>() {
            public void success(ArrayList<TeamEmployees> result) {
                loadTree(result);
            }
        });

        ScrollPanel scrollPanel = new ScrollPanel();
        scrollPanel.getElement().getStyle().setBorderWidth(1, Style.Unit.PX);
        scrollPanel.getElement().getStyle().setBorderStyle(Style.BorderStyle.SOLID);
        scrollPanel.getElement().getStyle().setBorderColor("#e8e8e8");
        scrollPanel.getElement().getStyle().setPadding(5, Style.Unit.PX);
        scrollPanel.setWidth("100%");
        scrollPanel.add(tree);

        WfmButton2 ok = new WfmButton2(wfmStrings.ok(), WfmButton2.BTN_PRIMARY, (ClickHandler) event -> {
            addUser();
            close();
        });
        WfmButton2 cancel = new WfmButton2(wfmStrings.cancel(), (ClickHandler) event -> close());

        add(scrollPanel);
        addButton(cancel);
        addButton(ok);

        setWidth(350);
    }

    private void loadTree(ArrayList<TeamEmployees> teamEmployees) {
        for (TeamEmployees teamEmployee : teamEmployees) {
            tree.add(teamEmployee.getTeam(), teamEmployee.getMembers());
        }
        tree.expandAll();
    }

    private void onTreeItemSelection(NTreeSelectItem selectedItem) {
        UserResource item = new UserResource();
        item.setObjectId(selectedItem.getItem().getId());
        item.setName(selectedItem.getItem().getName());
        if (selectedItem.isChecked()) {
            employees.put(item.getObjectId(), item);
        } else {
            employees.remove(item.getObjectId());
        }
    }

    /**
     * Generate a request to add a user to a group.
     */
    private void addUser() {
        GroupMembersViewItem group = (GroupMembersViewItem) DocumentsView.get().getCurrentSelection();
        if (group == null) {
            DocumentsView.get().displayError(wfmStrings.emptyGroupName());
            return;
        }
        if (employees.size() == 0) {
            DocumentsView.get().displayError(wfmStrings.noUserWasSElected());
            return;
        }
        ArrayList<Integer> emIdList = new ArrayList<>();
        emIdList.addAll(employees.keySet());
        LoadingPanel.loading(true);
        DocumentsView.get().getDocumentsService().addUsersToGroup(group.getGroupID(), emIdList, new AbstractAsyncCallback() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                try {
                    throw throwable;
                } catch (InsufficientPermissionsException e) {
                    DocumentsView.get().displayError(wfmStrings.youDontHavePermission());
                } catch (Throwable e) {
                    // last resort  a very unexpected exception
                }
            }

            @Override
            public void success(Object result) {
                DocumentsView.get().getGroups().updateGroups();
                DocumentsView.get().showUserList();
                LoadingPanel.loading(false);
            }
        });

    }

}
