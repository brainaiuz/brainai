/*
package com.edatasite.workforce.gwt.profile.client.ui.view.membersgroup;

import com.edatasite.workforce.gwt.core.client.Validation;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;
import com.edatasite.workforce.gwt.core.client.rpc.rbac.GroupMemberItem;
import com.edatasite.workforce.gwt.core.client.rpc.rbac.GroupMembersViewItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.MembersSelector;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmDialogBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

*/
/**
 * User: Ilhombek
 * Date: 04.06.2010
 * Time: 16:36:24
 *//*


public class AddMembersGroupView extends KpiModal implements Constants {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    
    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private VerticalPanel generalPanel;
    private WfmForm table;
    private WfmForm.Field groupNameField;
    private WfmForm.Field descriptionField;
    private WfmForm.Field membersSelectorField;
    private TextBox name;
    private TextBox description;
    private MembersSelector membersSelector;
    private WfmButton2 saveButton;
    private WfmButton2 closeButton;

    public static String ADD_VIEW = "Add";
    public static String EDIT_VIEW = "Edit";
    private String addOrEdit;
    private Integer groupID;
    private Integer groupEntryType;
    private String groupName;
    private String groupDescription;
    private Command command;

    public AddMembersGroupView(String addOrEdit, Integer groupID, Integer groupEntryType, String groupName, String groupDescription, Command command) {
        super(false, true);
        setText(addOrEdit + " " + wfmStrings.view());
        this.addOrEdit = addOrEdit;
        this.groupID = groupID;
        this.groupEntryType = groupEntryType;
        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.command = command;
        setSize(666, 450);

        generalPanel = new VerticalPanel();
        generalPanel.setSpacing(5);
        add(generalPanel);
        generated();
        this.center();
    }

    private void generated() {
        table = new WfmForm(new String[]{"24%", "75%"});
        table.setLabelSize("100px");
        table.setLabelAlignment(WfmForm.ALIGN_RIGHT);

        name = new TextBox();
        name.setWidth("215px");

        description = new TextBox();
        description.setWidth("215px");
        if (groupEntryType != null && groupEntryType.equals(BUILT_IN)) {
            name.setReadOnly(true);
            description.setReadOnly(true);
        } else {
            name.setReadOnly(false);
            description.setReadOnly(false);
        }
        membersSelector = new MembersSelector("timeslot");
        generatedEmployees();
        saveButton = new WfmButton2(wfmStrings.save());
        saveButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                saveGroup();
            }
        });
        closeButton = new WfmButton2(wfmStrings.close());
        closeButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                close();
            }
        });
        groupNameField = table.addField(wfmStrings.groupName(), name, true);
        descriptionField = table.addField(wfmStrings.description(), description);
        membersSelectorField = table.addField(wfmStrings.members(), membersSelector, true);
        table.addButton(saveButton);
        table.addButton(closeButton);

        generalPanel.add(table);
    }

    private void saveGroup() {
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
        ProjectMember[] members = membersSelector.getSelectedItems();
        GroupMemberItem[] memberItems = new GroupMemberItem[members.length];
        for (int i = 0; i < membersSelector.getSelectedItems().length; i++) {
            memberItems[i] = new GroupMemberItem();
            memberItems[i].setTrusteeID(members[i].getId());
            memberItems[i].setTrusteeName(members[i].getName());
        }
        viewItem.setMembers(memberItems);

        LoadingPanel.get().show(wfmStrings.saving());
        RbacService.App.get().saveGroup(viewItem, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(Integer result) {
                LoadingPanel.loading(false);
                command.execute();
                if (ADD_VIEW.equals(addOrEdit)) {
                     Info.show("", settingsStrings.membersSavedSuccessfully(), Info.Type.INFO);
                } else {
                     Info.show("", settingsStrings.membersUpdatedSuccessfully(), Info.Type.INFO);
                }
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_GROUP_ADD_EDIT, result, AddMembersGroupView.this);
                close();
            }
        });
    }

    private boolean validate() {
        int errors = 0;
        table.cleanupErrors();
        if (!Validation.validateTextBoxRequired(name, groupNameField)) {
            errors++;
        }
        */
/*if (membersSelector.getSelectedItems().length == 0) {
            membersSelectorField.setErrorMessage("Please select member ", "");
            errors++;
        }*//*

        if (!Validation.validateCheckedListRequired(membersSelector.getInitialList(), membersSelectorField, wfmStrings.pleaseSelectMember())) {
            errors++;
        }
        if (errors > 0) {
             Info.show("", wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void generatedEmployees() {
        LoadingPanel.get().show(coreStrings.loading());
        if (!"".equals(addOrEdit) && addOrEdit.equals(ADD_VIEW)) {
            AllInOneService.App.get().getCompanyEmployeesWithTeams(new AbstractAsyncCallback<ProjectMember[]>() {
                @Override
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(ProjectMember[] result) {
                    LoadingPanel.loading(false);
                    membersSelector.clearSelected();
                    membersSelector.addMembers(result);
                }
            });
        } else {
            RbacService.App.get().getGroupMembersList(groupID, new AbstractAsyncCallback<ProjectMember[]>() {
                @Override
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(ProjectMember[] result) {
                    LoadingPanel.loading(false);
                    membersSelector.clearSelected();
                    membersSelector.addMembers(result);
                    name.setText(groupName);
                    description.setText(groupDescription);
                }
            });
        }
    }
}
*/
