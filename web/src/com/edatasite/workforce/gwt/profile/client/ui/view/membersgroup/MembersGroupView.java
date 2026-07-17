/*
package com.edatasite.workforce.gwt.profile.client.ui.view.membersgroup;

import com.edatasite.workforce.gwt.core.client.View;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.rbac.GroupMembersViewItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;


*/
/**
 * User: Ilhombek
 * Date: 03.06.2010
 * Time: 15:48:25
 *//*

public class MembersGroupView extends View implements Constants {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    
    private WfmForm table;
    private WfmForm.Field groupNamesField;
    private WfmForm.Field nameField;
    private WfmForm.Field descriptionField;

    private DataListBox groupNames;
    private Integer groupID;
    private String groupName;
    private String groupDescription;
    private Integer groupEntryType;
    private HTML nameLabel;
    private HTML descriptionLabel;
    private MembersGroupTab membersGroupTab;
    private SimpleLink addLink;


    public MembersGroupView() {
        super("groupMembers", wfmStrings.groupMembersView());
    }

    @Override
    protected Widget onInitialize() {
        //getGroupMembers();
        return null;
    }

   */
/* private void getGroupMembers() {
        table = new WfmForm(new String[]{"30%", "69%"});
        table.setLabelSize("150px");
        table.setLabelAlignment(WfmForm.ALIGN_RIGHT);

        groupNames = new DataListBox();
        groupNames.addStyleName(DEFAULT_WIDTH);
        nameLabel = new HTML();
        descriptionLabel = new HTML();
        getCompanyGroups();
        groupNames.addValueChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                if (!groupNames.isSomethingSelected()) {
                    clearAllItems();
                    //editItem.setEnabled(false);
                    //deleteItem.setEnabled(false);
                } else {
                    //editItem.setEnabled(true);
                    groupID = groupNames.getSelectedItem().getId();
                    groupName = groupNames.getSelectedItem().getName();
                    groupDescription = groupNames.getSelectedItem().getDescription();
                    getGroupAllItems();
                }

            }
        });

        final Command afterAddOrUpdateGroupCommand = new Command() {
            public void execute() {
                getCompanyGroups();
                WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_GROUP_ADD_EDIT, MembersGroupView.this, new WfmUiEvent() {
                    public void onWfmUiEvent(Widget sender, Object args) {
                        if (args instanceof Integer) {
                            groupID = (Integer) args;
                            getGroupAllItems();
                        }
                    }
                });
            }
        };

        addLink = new SimpleLink(wfmStrings.addGroup(), SimpleLink.ADD_ICON);
        addLink.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                new AddMembersGroupView(AddMembersGroupView.ADD_VIEW, null, null, "", "", afterAddOrUpdateGroupCommand);
            }
        });

        toolItem = new ToolItem(Style.MENU);
        toolItem.setText(wfmStrings.action());

        Menu menu = new Menu();
        menu.setWidth(150);
        editItem = new MenuItem(Style.PUSH);
        editItem.setText(wfmStrings.edit());
        editItem.setEnabled(false);
        editItem.setIconStyle("icon-client-edit-small");
        editItem.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(BaseEvent baseEvent) {
                new AddMembersGroupView(AddMembersGroupView.EDIT_VIEW, groupID, groupEntryType, groupName, groupDescription, afterAddOrUpdateGroupCommand);
            }
        });
        menu.add(editItem);
        deleteItem = new MenuItem(Style.PUSH);
        deleteItem.setText(wfmStrings.delete());
        deleteItem.setEnabled(false);
        deleteItem.setIconStyle("removeItemStyle-profile");
        deleteItem.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(BaseEvent baseEvent) {
                RbacService.App.get().deleteGroup(groupID, new AbstractAsyncCallback<Void>() {
                    @Override
                    public void failure(Throwable caught) {
                    }

                    @Override
                    public void success(Void result) {
                         Info.show("", settingsStrings.groupDeleteSuccessfully(), Info.Type.INFO);
                        getCompanyGroups();
                        clearAllItems();
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_GROUP_DELETE, result, MembersGroupView.this);
                    }
                });
            }
        });
        menu.add(deleteItem);
        toolItem.setMenu(menu);

        FlexTable flexTable = new FlexTable();
        flexTable.setCellSpacing(1);
        flexTable.setCellPadding(0);
        flexTable.setWidget(0, 0, groupNames);
        flexTable.getFlexCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
        flexTable.setWidget(0, 1, addLink);
        flexTable.getFlexCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
        flexTable.getFlexCellFormatter().setWidth(0, 1, "80px");
        flexTable.setWidget(0, 2, toolItem);
        flexTable.getFlexCellFormatter().setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_LEFT);

        groupNamesField = table.addField(coreStrings.groupName(), flexTable);
        nameField = table.addField(wfmStrings.nameField(), nameLabel);
        descriptionField = table.addField(wfmStrings.descriptionField(), descriptionLabel);
        add(table);

        membersGroupTab = new MembersGroupTab(wfmStrings.groupMembersView());
        CustomTabBar membersTabBar = new CustomTabBar(1);
        membersTabBar.setPanelSize(530, 300);
        membersTabBar.setMargin("5px 5px 5px 5px");
        membersTabBar.addWidget(membersGroupTab);
        membersTabBar.selectTab(0);
        add(membersTabBar);
    }*//*


    */
/*private void getGroupAllItems() {
        LoadingPanel.get().show(membersGroupTab.getTable());
        RbacService.App.get().getGroupMembers(groupID, new AbstractAsyncCallback<GroupMembersViewItem>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(GroupMembersViewItem result) {
                groupEntryType = result.getGroupEntryType();
                if (groupEntryType != null && groupEntryType.equals(BUILT_IN)) {
                    deleteItem.setEnabled(false);
                } else {
                    deleteItem.setEnabled(true);
                }
                LoadingPanel.loading(false);
                changedMembersSummary(result);
            }
        });
    }*//*


    private void getCompanyGroups() {
        RbacService.App.get().getCompanyGroups(new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable caught) {
            }

            @Override
            public void success(SelectItem[] result) {
                groupNames.setItems(result);
                if (groupID != null) {
                    groupNames.setSelected(groupID);
                }
            }
        });
    }

    private void changedMembersSummary(GroupMembersViewItem groupMembersViewItem) {
        clearAllItems();
        nameLabel.setHTML(groupMembersViewItem.getGroupName());
        descriptionLabel.setHTML(groupMembersViewItem.getGroupDescription());
        membersGroupTab.setMemberItems(groupMembersViewItem.getMembers());
        membersGroupTab.viewShow();
    }

    private void clearAllItems() {
        nameLabel.setHTML("");
        descriptionLabel.setHTML("");
        membersGroupTab.getTable().removeAll();
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }


}
*/
