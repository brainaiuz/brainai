package com.edatasite.workforce.gwt.messagecenter.client.view;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TeamEmployees;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.UserEmailItem;
import com.edatasite.workforce.gwt.core.client.rpc.rbac.GroupMembersViewItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.filterparams.SuperPuperHandler;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.NTreeSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.TreeSelect;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.PermissionsList;
import com.edatasite.workforce.gwt.documents.client.rest.resource.PermissionHolder;
import com.edatasite.workforce.gwt.documents.client.rest.resource.UserResource;
import com.google.gwt.dom.client.Style;
import gwt.material.design.client.ui.MaterialPanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Azazello
 * Date: 06.03.18
 * Time: 00:00
 * To change this template use File | Settings | File Templates.
 */
public class EmailAccountShareModal extends KpiModal implements Constants {
    private PermissionsList permList;
    private UserEmailItem item;
    private WfmButton2 save;

    public EmailAccountShareModal(Integer emailAccountID) {
        super();
        setTitle(wfmStrings.share());
        setWidth(450);
        LoadingPanel.loading(true);
        AllInOneService.App.get().getUserEmailItem(emailAccountID, new AbstractAsyncCallback<UserEmailItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(UserEmailItem result) {
                LoadingPanel.loading(false);
                item = result;
                initialize();
            }
        });
    }

    private void initialize() {
        permList = new PermissionsList(item.getPermissions(), item.getOwner(), null, true);
        add(permList);

        addButton(new WfmButton2(wfmStrings.cancel(), clickEvent -> close()));
//        addButton(new WfmButton2(wfmStrings.addGroup(), clickEvent -> new AddUserOrGroupDlg(item, false)));
        addButton(new WfmButton2(wfmStrings.addUser(), clickEvent -> new AddUserOrGroupDlg(item, true)));
        save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> saveUserEmailItem());
        addButton(save);

        open();
    }


    private void saveUserEmailItem() {
        item.setPermissions(permList.getPermissions());
        save.setEnabled(false);
        LoadingPanel.loading(true);
        AllInOneService.App.get().updateEmailRbacEntries(item, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                save.setEnabled(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Void result) {
                save.setEnabled(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.success(), Info.Type.INFO);
                close();
            }
        });
    }

    private class AddUserOrGroupDlg extends KpiModal {
        private boolean isUserAdd = false;
        private Map<Integer, GroupMembersViewItem> groups = new HashMap<>();
        private HashMap<Integer, UserResource> employees = new HashMap<>();
        private DataListBox groupBox;
        private MaterialPanel mainPanel = new MaterialPanel("selectPanelWidget");
        private MaterialPanel treePanel = new MaterialPanel("selectPanelWidget__tree");
        private TreeSelect tree;
        private UserEmailItem item;

        public AddUserOrGroupDlg(UserEmailItem item, boolean isUserAdd) {
            super();
            setTitle(isUserAdd ? wfmStrings.addUser() : wfmStrings.addGroup());
            setWidth(isUserAdd ? 500 : 300);
            this.item = item;
            this.isUserAdd = isUserAdd;
            initialize();
        }

        private void initialize(){
            if (isUserAdd) {
                SuperPuperHandler<NTreeSelectItem> command = nTreeSelectItem -> {
                    if (nTreeSelectItem.getParentItem() == null) {
                        for (int i = 0; i < nTreeSelectItem.getChildCount(); i++) {
                            NTreeSelectItem selectedItem = (NTreeSelectItem) nTreeSelectItem.getChild(i);
                            UserResource item1 = new UserResource();
                            item1.setObjectId(selectedItem.getItem().getId());
                            item1.setName(selectedItem.getItem().getName());
                            if (selectedItem.isChecked()) {
                                employees.put(item1.getObjectId(), item1);
                            } else {
                                employees.remove(item1.getObjectId());
                            }
                        }
                    } else if (nTreeSelectItem.getChildCount() == 0) {
                        UserResource item1 = new UserResource();
                        item1.setObjectId(nTreeSelectItem.getItem().getId());
                        item1.setName(nTreeSelectItem.getItem().getName());
                        if (nTreeSelectItem.isChecked()) {
                            employees.put(item1.getObjectId(), item1);
                        } else {
                            employees.remove(item1.getObjectId());
                        }
                    }

                };
                tree = new TreeSelect().setHandler(command);
                if (tree.getRadioButtons() != null) {
                    tree.getRadioButtons().setVisible(false);
                }
                loadTree(item.getTeamEmployees());
                tree.hideAvailablityCheckBox();
                tree.expandAll();
                treePanel.add(tree);
                treePanel.getElement().getStyle().setMarginBottom(0, Style.Unit.PX);
                mainPanel.add(treePanel);
                add(mainPanel);
            } else {
                groupBox = new DataListBox();
                ArrayList<SelectItem> items = new ArrayList<>();
                if (item.getGroups() != null) {
                    for (GroupMembersViewItem groupItem : item.getGroups()) {
                        groups.putIfAbsent(groupItem.getGroupID(), groupItem);
                        items.add(new SelectItem(groupItem.getGroupID(), groupItem.getGroupName()));
                    }
                }
                groupBox.setItems(items.toArray(new SelectItem[]{}));
                add(groupBox);
            }

            addButton(new WfmButton2(wfmStrings.cancel(), clickEvent -> close()));
            addButton(new WfmButton2(wfmStrings.add(), WfmButton2.BTN_PRIMARY, clickEvent -> {
                addPermission();
                close();
            }));
            open();
        }

        private void loadTree(ArrayList<TeamEmployees> teamEmployees) {
            for (TeamEmployees teamEmployee : teamEmployees) {
                tree.add(teamEmployee.getTeam(), teamEmployee.getMembers());
            }
            tree.collapseAll();
        }

        private void addPermission() {
            if (isUserAdd) {
                addUserShare();
            } else {
                addGroupShare();
            }
        }

        private void addGroupShare() {
            if (groupBox.getSelectedId() == null) {
                return;
            }
            for (PermissionHolder p : permList.getPermissions()) {
                if (p.getGroup() != null && groupBox.getSelectedId().equals(p.getGroup().getGroupID())) {
                    Info.show(wfmStrings.group() + wfmStrings.alreadyHasAccessToTheResourse(), Info.Type.INFO);
                    return;
                }
            }
            PermissionHolder perm = new PermissionHolder();
            perm.setGroup(groups.get(groupBox.getSelectedId()));
            permList.addPermission(perm);
            permList.updateTable();
        }

        private void addUserShare() {
            for (WfmTreeItem treeItem : tree.getCheckedItems()) {
                if (treeItem.getParent() != null) {
                    PermissionHolder perm = new PermissionHolder();
                    for (PermissionHolder p : permList.getPermissions()) {
                        if (p.getUser() != null && treeItem.getId().equals(p.getUser().getObjectId())) {
                            Info.show(wfmStrings.user() + wfmStrings.alreadyHasAccessToTheResourse(), Info.Type.INFO);
                            return;
                        }
                    }
                    UserResource user = new UserResource();
                    user.setObjectId(treeItem.getId());
                    user.setName(treeItem.getName());
                    perm.setUser(user);
                    if (item.getOwner() != null && user.getObjectId() != null && user.getObjectId().equals(item.getOwner().getObjectId())) {
                        perm.setRelationship(Constants.EMAIL_OWNER);
                    }
                    permList.addPermission(perm);
                    permList.updateTable();
                }
            }
        }
    }
}