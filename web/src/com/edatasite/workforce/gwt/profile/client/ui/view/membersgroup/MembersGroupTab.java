/*
package com.edatasite.workforce.gwt.profile.client.ui.view.membersgroup;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.rbac.GroupMemberItem;
import com.edatasite.workforce.gwt.core.client.ui.customtabbar.CustomTabWidget;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;

*/
/**
 * User: Ilhombek
 * Date: 03.06.2010
 * Time: 18:05:31
 *//*

public class MembersGroupTab extends CustomTabWidget {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final SettingStrings settingsStrings =SettingStrings.App.get();

    private Table table;
    private GroupMemberItem[] memberItems;

    public MembersGroupTab(String tabName) {
        super(tabName);
    }

    @Override
    public void initData() {
        TableColumn[] columns = new TableColumn[2];
        columns[0] = new TableColumn("memberName", wfmStrings.employeeName(), 190);
        columns[1] = new TableColumn("trusteeType", settingsStrings.trusteeType(), 310);

        TableColumnModel columnModel = new TableColumnModel(columns);
        table = new Table(Style.MULTI | Style.HORIZONTAL, columnModel);
        table.setBorders(false);
        table.setWidth("525px");
        table.setHeight("300px");
        add(table);
    }

    @Override
    public void viewShow() {
        if (memberItems != null && memberItems.length != 0) {
            for (int i = 0; i < memberItems.length; i++) {
                final GroupMemberItem item = memberItems[i];
                Object[] values = new Object[2];
                values[0] = item.getTrusteeName();
                values[1] = item.getTrusteeDescription();
                TableItem tableItem = new TableItem(values);
                table.add(tableItem);
            }
        }
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public GroupMemberItem[] getMemberItems() {
        return memberItems;
    }

    public void setMemberItems(GroupMemberItem[] memberItems) {
        this.memberItems = memberItems;
    }
}
*/
