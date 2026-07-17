package com.edatasite.workforce.gwt.documents.client;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.TeamEmployees;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.rpc.rbac.GroupMembersViewItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.filterparams.SuperPuperHandler;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.NTreeSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.TreeSelect;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.rest.resource.PermissionHolder;
import com.edatasite.workforce.gwt.documents.client.rest.resource.UserResource;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Sherali
 */
public class PermissionsAddToClientDialog extends KpiModal {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    

    private final TreeSelect tree;
    private final HashMap<Integer, UserResource> employees;

    private final ListBox groupBox = new ListBox();

    private final KpiCheckBox read = new KpiCheckBox();

    private final KpiCheckBox write = new KpiCheckBox();

    private final KpiCheckBox delete = new KpiCheckBox();

    private final KpiCheckBox modifyACL = new KpiCheckBox();

    private final PermissionsList permList;

    public PermissionsAddToClientDialog(List<GroupMembersViewItem> _groups, PermissionsList _permList, String accountType) {
        // Set the dialog's caption.
        if (CrmConstants.CUSTOMER.equals(accountType)) {
            setTitle(Property.get(Constants.CLIENT_LIST, wfmStrings.addMess(), wfmStrings.customer()));
        } else if (CrmConstants.SUPPLIER.equals(accountType)) {
            setTitle(Property.get(Constants.SUPPLIER_LIST, wfmStrings.addMess(), wfmStrings.supplier()));
        }

        permList = _permList;

        VerticalPanel panel = new VerticalPanel();
        panel.addStyleName("doc-TabPanelBottom");
        FlexTable permTable = new FlexTable();
        FlexTable checkboxTable = new FlexTable();

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
                //Besides, we have another selection handler, and this one is firing first, therefore we  have reverse value of checked.
                onTreeItemSelection(nTreeSelectItem);
            }
        };

        tree = new TreeSelect().setHandler(command);
        if (CrmConstants.CUSTOMER.equals(accountType)) {
            tree.setSearchText(wfmStrings.searchClients());
        } else if (CrmConstants.SUPPLIER.equals(accountType)) {
            tree.setSearchText(wfmStrings.searchSuppliers());
        }

        if (tree.getRadioButtons() != null) {
            tree.getRadioButtons().setVisible(false);
        }
        tree.setHeight(250);

        generateClients(accountType);

        Div treePanel = new Div("selectPanelWidget__tree");
        treePanel.add(tree);
        permTable.setWidget(1, 0, treePanel);

        read.setValue(true);
        read.setEnabled(false);

        WfmButton2 ok = new WfmButton2(wfmStrings.ok(), WfmButton2.BTN_PRIMARY, event -> {
            addClientPermissions();
            close();
        });

        WfmButton2 cancel = new WfmButton2(wfmStrings.cancel(), event -> close());

        permTable.getElement().getStyle().setMarginTop(20, Style.Unit.PX);

        panel.add(checkboxTable);
        panel.add(permTable);

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

    private void generateClients(String accountType) {
        LoadingPanel.loading(true);
        AllInOneService.App.get().getCompanyAccountsForTree(accountType, new AsyncCallback<ArrayList<TeamEmployees>>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ArrayList<TeamEmployees> result) {
                LoadingPanel.loading(false);
                loadTree(result);
            }
        });
    }

    private void addClientPermissions() {
        for (WfmTreeItem item : tree.getCheckedItems()) {
            if (item.getParent() != null) {
                PermissionHolder perm = new PermissionHolder();
                for (PermissionHolder p : permList.getPermissions()) {
                    if (p.getUser() != null && item.getId().equals(p.getUser().getObjectId())) {
                        DocumentsView.get().displayError(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()) + " " + wfmStrings.alreadyHasAccessToTheResourse());
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
