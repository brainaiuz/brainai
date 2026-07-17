package com.edatasite.workforce.gwt.documents.client;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.TeamEmployees;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.rpc.rbac.GroupMembersViewItem;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.filterparams.SuperPuperHandler;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.NTreeSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.TreeSelect;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.rest.resource.PermissionHolder;
import com.edatasite.workforce.gwt.documents.client.rest.resource.UserResource;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Sherali
 */
public class PermissionsAddDialog extends KpiModal {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private TreeSelect tree;
    private HashMap<Integer, UserResource> employees;

    private ArrayList<GroupMembersViewItem> groups;

    private ListBox groupBox = new ListBox();

    private KpiCheckBox read = new KpiCheckBox();

    private KpiCheckBox write = new KpiCheckBox();

    private KpiCheckBox delete = new KpiCheckBox();

    private KpiCheckBox modifyACL = new KpiCheckBox();

    private final PermissionsList permList;

    boolean userAdd;

    public PermissionsAddDialog(ArrayList<GroupMembersViewItem> _groups, PermissionsList _permList, boolean _userAdd) {
        // Set the dialog's caption.
        if (_userAdd) {
            setTitle(wfmStrings.addUser());
        } else {
            setTitle(wfmStrings.addGroup());
        }
        userAdd = _userAdd;
        permList = _permList;

        final VerticalPanel panel = new VerticalPanel();
        panel.addStyleName("doc-TabPanelBottom");
        FlexTable permTable = new FlexTable();
        FlexTable checkboxTable = new FlexTable();

        if (userAdd) {
            Span userLable = new Span(wfmStrings.usersGroups());
            userLable.setStyleName("form-label");
            Span readLable = new Span(wfmStrings.read());
            readLable.setStyleName("form-label");
            Span writeLable = new Span(wfmStrings.write());
            writeLable.setStyleName("form-label");
            Span deleteLable = new Span(wfmStrings.delete());
            deleteLable.setStyleName("form-label");
            Span modifyAccesLable = new Span(wfmStrings.modifyAcces());
            modifyAccesLable.setStyleName("form-label");

            permTable.setWidget(0, 0, userLable);

            checkboxTable.setWidget(0, 0, readLable);
            checkboxTable.setWidget(0, 1, writeLable);
            checkboxTable.setWidget(0, 2, deleteLable);
            checkboxTable.setWidget(0, 3, modifyAccesLable);

            checkboxTable.setWidget(1, 0, read);
            checkboxTable.setWidget(1, 1, write);
            checkboxTable.setWidget(1, 2, delete);
            checkboxTable.setWidget(1, 3, modifyACL);

            employees = new HashMap<>();

            SuperPuperHandler<NTreeSelectItem> command = nTreeSelectItem -> {
                if (nTreeSelectItem.getParentItem() == null) {     //Tick ITEMs
                    for (int i = 0; i < nTreeSelectItem.getChildCount(); i++) {
                        NTreeSelectItem selectedItem = (NTreeSelectItem) nTreeSelectItem.getChild(i);
                        onTreeItemSelection(selectedItem);
                    }
                } else if (nTreeSelectItem.getChildCount() == 0) { //Child Item chnaged value
                    onTreeItemSelection(nTreeSelectItem);
                }
            };

            tree = new TreeSelect().setHandler(command);

            if (tree.getRadioButtons() != null) {
                tree.getRadioButtons().setVisible(false);
            }
            tree.setHeight(250);

            DocumentsView.get().getDocumentsService().getCompanyEmployeesWithTeams(false, new AbstractAsyncCallback<ArrayList<TeamEmployees>>() {
                public void success(ArrayList<TeamEmployees> result) {
                    loadTree(result);
                }
            });

            Div treePanel = new Div("selectPanelWidget__tree");
            treePanel.add(tree);
            permTable.setWidget(1, 0, treePanel);

            permTable.getElement().getStyle().setMarginTop(20, Style.Unit.PX);

            panel.add(checkboxTable);
            panel.add(permTable);
        } else {
            if (_groups == null) {
                getGroups();
            } else {
                groups = _groups;
                populateGroups();
            }

            Span userLable = new Span(wfmStrings.usersGroups());
            userLable.setStyleName("form-label");
            Span readLable = new Span(wfmStrings.read());
            readLable.setStyleName("form-label");
            Span writeLable = new Span(wfmStrings.write());
            writeLable.setStyleName("form-label");
            Span deleteLable = new Span(wfmStrings.delete());
            deleteLable.setStyleName("form-label");
            Span modifyAccesLable = new Span(wfmStrings.modifyAcces());
            modifyAccesLable.setStyleName("form-label");

            permTable.setWidget(0, 0, userLable);
            permTable.setWidget(1, 0, groupBox);

            checkboxTable.setWidget(0, 0, readLable);
            checkboxTable.setWidget(0, 1, writeLable);
            checkboxTable.setWidget(0, 2, deleteLable);
            checkboxTable.setWidget(0, 3, modifyAccesLable);

            checkboxTable.setWidget(1, 0, read);
            checkboxTable.setWidget(1, 1, write);
            checkboxTable.setWidget(1, 2, delete);
            checkboxTable.setWidget(1, 3, modifyACL);

            checkboxTable.getElement().getStyle().setMarginTop(20, Style.Unit.PX);

            panel.add(permTable);
            panel.add(checkboxTable);
        }
        read.setValue(true);
        read.setEnabled(false);

        WfmButton2 ok = new WfmButton2(wfmStrings.ok(), WfmButton2.BTN_PRIMARY, (ClickHandler) event -> {
            addPermission();
            close();
        });

        WfmButton2 cancel = new WfmButton2(wfmStrings.cancel(), (ClickHandler) event -> close());

        add(panel);
        addButton(cancel);
        addButton(ok);

        setWidth(350);

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

    private void getGroups() {
        LoadingPanel.loading(true);
        RbacService.App.get().getCompanyGroupsWithMembers(new AbstractAsyncCallback<ArrayList<GroupMembersViewItem>>() {

            @Override
            public void success(ArrayList<GroupMembersViewItem> result) {
                LoadingPanel.loading(false);
                groups = result;
                populateGroups();
            }
        });
    }

    private void populateGroups() {

        for (GroupMembersViewItem group : groups) {

            groupBox.addItem(group.getGroupName(), group.getGroupName());

        }
    }

    private void addPermission() {
        if (userAdd) {
            addUserPermissions();
        } else {
            PermissionHolder perm = new PermissionHolder();
            String groupId = groupBox.getValue(groupBox.getSelectedIndex());
            GroupMembersViewItem selected = null;
            for (GroupMembersViewItem g : groups) {
                if (g.getGroupName().equals(groupId)) {
                    selected = g;
                }
            }
            if (selected == null) {
                return;
            }
            for (PermissionHolder p : permList.getPermissions()) {
                if (p.getGroup() != null && selected.getGroupName().equals(p.getGroup().getGroupName())) {
                    DocumentsView.get().displayError(wfmStrings.group() + " " + wfmStrings.alreadyHasAccessToTheResourse());
                    return;
                }
            }
            perm.setGroup(selected);
            boolean readValue = read.getValue();
            boolean writeValue = write.getValue();
            boolean deleteValue = delete.getValue();
            boolean modifyValue = modifyACL.getValue();

            perm.setRead(readValue);
            perm.setWrite(writeValue);
            perm.setDelete(deleteValue);
            perm.setModifyACL(modifyValue);
            permList.addPermission(perm);
            permList.updateTable();
        }
    }

    private void addUserPermissions() {
        for (WfmTreeItem item : tree.getCheckedItems()) {
            if (item.getParent() != null) {
                PermissionHolder perm = new PermissionHolder();
                for (PermissionHolder p : permList.getPermissions()) {
                    if (p.getUser() != null && item.getId().equals(p.getUser().getObjectId())) {
                        DocumentsView.get().displayError(wfmStrings.user() + " " + wfmStrings.alreadyHasAccessToTheResourse());
                        return;
                    }
                }
                UserResource user = new UserResource();
                user.setObjectId(item.getId());
                user.setName(item.getName());
                perm.setUser(user);
                boolean readValue = read.getValue();
                boolean writeValue = write.getValue();
                boolean deleteValue = delete.getValue();
                boolean modifyValue = modifyACL.getValue();

                perm.setRead(readValue);
                perm.setWrite(writeValue);
                perm.setDelete(deleteValue);
                perm.setModifyACL(modifyValue);
                permList.addPermission(perm);
                permList.updateTable();
            }
        }
    }

    private void loadTree(ArrayList<TeamEmployees> teamEmployees) {
        for (TeamEmployees teamEmployee : teamEmployees) {
            tree.add(teamEmployee.getTeam(), teamEmployee.getMembers());
        }
        tree.expandAll();
    }
}
