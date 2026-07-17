package com.edatasite.workforce.gwt.documents.client;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.TeamEmployees;
import com.edatasite.workforce.gwt.core.client.rpc.rbac.GroupMemberItem;
import com.edatasite.workforce.gwt.core.client.rpc.rbac.GroupMembersViewItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxRow;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.selectPanel.SelectPanel;
import com.edatasite.workforce.gwt.core.client.ui.table.TableColumn;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.NTreeSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.TreeSelect;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.exceptions.DuplicateNameException;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TreeItem;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Label;

import java.util.ArrayList;

/**
 * @author Sherali
 */
public class GroupPropertiesDialog2 extends KpiModal implements Constants {

    private final MaterialPanel generalPanel;
    private TextBox name;
    private TextBox description;
    private KpiRadioButton userRadioButton;
    private KpiRadioButton clientRadioButton;

    private final TableColumn[] columns = new TableColumn[2];
    private SelectPanel assigneePanel;

    private WfmButton2 saveButton;
    private WfmButton2 closeButton;

    private Integer groupID;
    private Integer groupEntryType;
    private String groupName;
    private String groupDescription;
    /**
     * A flag that denotes whether the dialog will be used to create or modify a
     * folder.
     */
    private final boolean create;

    private GroupMembersViewItem gr = null;

    /**
     * The widget's constructor.
     *
     * @param _create true if the dialog is displayed for creating a new
     *                sub-folder of the selected folder, false if it is displayed
     *                for modifying the selected folder
     */
    public GroupPropertiesDialog2(final boolean _create) {
        create = _create;
        // Use this opportunity to set the dialog's caption.
        if (create) {
            setTitle(wfmStrings.create() + " " + wfmStrings.group());
        } else {
            setTitle(wfmStrings.groupProperties());
        }
        setWidth(740);
        TreeItem current = DocumentsView.get().getGroups().getCurrent();

        if (current != null && current.getUserObject() instanceof GroupMembersViewItem && DocumentsView.get().getCurrentSelection() instanceof GroupMembersViewItem) {
            gr = ((GroupMembersViewItem) current.getUserObject());
        }
        if (!create) {
            this.groupID = gr.getGroupID();
            this.groupEntryType = gr.getGroupEntryType();
            this.groupName = gr.getGroupName();
            this.groupDescription = gr.getGroupDescription();
        }

        generalPanel = new MaterialPanel();
        add(generalPanel);
        generated();
        generalPanel.addStyleName("doc-TabPanelBottom");
        name.setFocus(true);
    }


    private void generated() {
        name = new TextBox();

        description = new TextBox();
        if (groupEntryType != null && groupEntryType.equals(BUILT_IN)) {
            name.setReadOnly(true);
            description.setReadOnly(true);
        } else {
            name.setReadOnly(false);
            description.setReadOnly(false);
        }

        userRadioButton = new KpiRadioButton("usertype", wfmStrings.employee(), true);
        userRadioButton.setValue(true);
        userRadioButton.addClickHandler(event -> getUserList(true));
        clientRadioButton = new KpiRadioButton("usertype", Property.get(Constants.CLIENT_LIST, wfmStrings.customer()), true);
        clientRadioButton.addClickHandler(event -> getUserList(false));

        FlexTable flexTable = new FlexTable();
        flexTable.setWidget(0, 0, userRadioButton);
        flexTable.setWidget(0, 1, clientRadioButton);

        //assignee to Task
        columns[0] = new TableColumn(wfmStrings.employee(), wfmStrings.employee());
        columns[1] = new TableColumn(wfmStrings.delete(), wfmStrings.action(), 15);
        MaterialPanel selectPanelDiv = new MaterialPanel("col-12");
        assigneePanel = new SelectPanel(columns);
        assigneePanel.setHeight(250);
        assigneePanel.hideAvailablityCheckBox();
        selectPanelDiv.add(assigneePanel);
        if (groupID != null) {
            userRadioButton.setEnabled(false);
            clientRadioButton.setEnabled(false);
            if (IS_EMPLOYEE.equals(gr.getType())) {
                userRadioButton.setValue(Boolean.TRUE);
                getUserList(true);
            } else {
                clientRadioButton.setValue(Boolean.TRUE);
                getUserList(false);
            }
        } else {
            getUserList(true);
        }
        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveButton.addClickHandler(event -> saveGroup());
        closeButton = new WfmButton2(wfmStrings.close());
        closeButton.addClickHandler(event -> close());

        GBox groupBox = new GBox();
        groupBox.setStyleNoPadding(true);
        GBoxRow groupBoxRow = new GBoxRow();
        groupBox.add(groupBoxRow);

        GBoxItem groupItem = new GBoxItem(wfmStrings.groupName(), name);
        groupItem.getLabel().addStyleName("form-label--required");
        groupBoxRow.add(groupItem);
        groupBoxRow.add(new GBoxItem(wfmStrings.description(), description));

        GBoxItem typeItem = new GBoxItem(wfmStrings.type(), flexTable);
        typeItem.setStyleNoBorder(true);
        GBox typeGroupBox = new GBox(new GBoxRow(typeItem));
        typeGroupBox.setStyleNoPadding(true);

        MaterialPanel memberPanel = new MaterialPanel();
        Label memberLabel = new Label(wfmStrings.members());
        memberLabel.setStyleName("form-label form-label--required");
        memberPanel.add(memberLabel);
        memberPanel.add(selectPanelDiv);

        generalPanel.add(typeGroupBox);
        generalPanel.add(groupBox);
        generalPanel.add(memberPanel);

        addButton(closeButton);
        addButton(saveButton);
    }

    private void saveGroup() {
        if (gr != null && !gr.isCanChange()) {
            Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
            return;
        }
        if (!validate()) {
            return;
        }
        GroupMembersViewItem viewItem = new GroupMembersViewItem();
        viewItem.setGroupID(groupID);
        viewItem.setGroupName(name.getText());
        viewItem.setGroupDescription(description.getText());
        if (groupEntryType != null) {
            viewItem.setGroupEntryType(groupEntryType);
        }
        if (userRadioButton.getValue()) {
            viewItem.setType(IS_EMPLOYEE);
        } else {
            viewItem.setType(IS_CLIENT);
        }

        Integer[] members = assigneePanel.getSelectedItems();
        GroupMemberItem[] memberItems = new GroupMemberItem[members.length];
        for (int i = 0; i < members.length; i++) {
            memberItems[i] = new GroupMemberItem();
            memberItems[i].setTrusteeID(members[i]);
        }
        viewItem.setMembers(memberItems);

        LoadingPanel.loading(true);
        RbacService.App.get().saveGroup(viewItem, new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                try {
                    throw throwable;
                } catch (DuplicateNameException e) {
                    DocumentsView.get().displayError(e.getMessage());
                } catch (Throwable e) {
                    // last resort  a very unexpected exception
                }
            }

            @Override
            public void onSuccess(Integer result) {
                LoadingPanel.loading(false);
                if (create) {
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullyAdded(), wfmStrings.group()), Info.Type.INFO);
                } else {
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.group()), Info.Type.INFO);
                }
                DocumentsView.get().getGroups().updateGroups();
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_GROUP_ADD_EDIT, result, GroupPropertiesDialog2.this);
                clear();
                close();
            }
        });
    }

    public boolean validate() {
        int errors = 0;
        if (!Validation.validateTextBoxRequired(name)) {
            errors++;
        }
        if (!Validation.validateSelectPanel(assigneePanel)) {
            errors++;
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void getUserList(boolean isEmployee) {
        assigneePanel.clearTreeView();
        if (isEmployee) {
            generatedEmployees();
        } else {
            generateClients();
        }
    }

    private void generateClients() {
        LoadingPanel.loading(true);
        AllInOneService.App.get().getCompanyAccountsForTree(CrmConstants.CUSTOMER, new AsyncCallback<ArrayList<TeamEmployees>>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ArrayList<TeamEmployees> result) {
                LoadingPanel.loading(false);
//                DocumentsView.get().getGroups().updateGroups();
//                DocumentsView.get().showUserList();
                loadTree(result);
            }
        });
    }


    private void generatedEmployees() {
        LoadingPanel.loading(true);
        AllInOneService.App.get().getCompanyEmployeesForTree(new AsyncCallback<ArrayList<TeamEmployees>>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ArrayList<TeamEmployees> result) {
                LoadingPanel.loading(false);
//                DocumentsView.get().getGroups().updateGroups();
//                DocumentsView.get().showUserList();
                loadTree(result);
            }
        });
    }

    private void loadTree(ArrayList<TeamEmployees> teamEmployees) {
        TreeSelect.setTickAllVisible(teamEmployees.size() != 0);
        for (TeamEmployees teamEmployee : teamEmployees) {
            assigneePanel.addTreeItem(teamEmployee.getTeam(), teamEmployee.getMembers());
        }
        if (groupID == null) {
            boolean isChecked = false;
            for (int i = 0; i < assigneePanel.getTree().getItemCount(); i++) {
                final NTreeSelectItem parent = (NTreeSelectItem) assigneePanel.getTree().getItem(i);
                for (int j = 0; j < parent.getChildCount(); j++) {
                    final NTreeSelectItem child = (NTreeSelectItem) parent.getChild(j);
                    if (child.getItem().getId().equals(Utils.getUserID())) {
                        child.setChecked(true);
                        assigneePanel.onTreeItemSelection(child, null);
                        isChecked = true;
                        break;
                    }
                }
                if (isChecked) {
                    break;
                }
            }
        }

        if (groupID != null) {
            name.setText(groupName);
            description.setText(groupDescription);
            LoadingPanel.loading(true);
            RbacService.App.get().getGroupMembersListForTree(groupID, new AsyncCallback<ArrayList<Integer>>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(ArrayList<Integer> result) {
                    LoadingPanel.loading(false);
                    DocumentsView.get().getGroups().updateGroups();
                    DocumentsView.get().showUserList();

                    for (int i = 0; i < assigneePanel.getTree().getItemCount(); i++) {
                        NTreeSelectItem parent = (NTreeSelectItem) assigneePanel.getTree().getItem(i);
                        for (int j = 0; j < parent.getChildCount(); j++) {
                            NTreeSelectItem child = (NTreeSelectItem) parent.getChild(j);
                            for (Integer id : result) {
                                if (child.getItem().getId().equals(id)) {
                                    child.setChecked(true);
                                    assigneePanel.onTreeItemSelection(child, null);
                                    break;
                                }
                            }
                        }
                    }

                    assigneePanel.expandTreeView();
                    LoadingPanel.loading(false);
                }
            });
        } else {
            assigneePanel.expandTreeView();
        }
    }
}