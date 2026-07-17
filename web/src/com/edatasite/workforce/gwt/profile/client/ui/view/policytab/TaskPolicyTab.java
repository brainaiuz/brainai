/*
package com.edatasite.workforce.gwt.profile.client.ui.view.policytab;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.server.rpc.TaskPermissionItem;
import com.edatasite.workforce.gwt.core.server.rpc.TaskPolicyItem;
import com.edatasite.workforce.gwt.core.client.ui.billboard.BillboardPanel;
import com.edatasite.workforce.gwt.core.client.ui.customtabbar.CustomTabWidget;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;

*/
/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 02-Jun-2010
 * Time: 18:25:34
 *//*

public class TaskPolicyTab extends CustomTabWidget {

    private Table table;
    private static final SettingStrings settingsStrings=SettingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public TaskPolicyTab() {
        super(settingsStrings.taskPolicyTable());
    }


    public void initData() {

        int k = 0;
        TableColumn[] columns = new TableColumn[12];

        columns[k] = new TableColumn(settingsStrings.relationName(), 175);
        columns[k].setAlignment(Style.LEFT);
        columns[k].setMinWidth(100);
        columns[k++].setMaxWidth(250);

        columns[k] = new TableColumn(wfmStrings.view(), 35);
        columns[k].setAlignment(Style.CENTER);
        columns[k].setMinWidth(35);
        columns[k++].setMaxWidth(35);

        columns[k] = new TableColumn(wfmStrings.edit(), 35);
        columns[k].setAlignment(Style.CENTER);
        columns[k].setMinWidth(35);
        columns[k++].setMaxWidth(35);

        columns[k] = new TableColumn(wfmStrings.delete(), 40);
        columns[k].setAlignment(Style.CENTER);
        columns[k].setMinWidth(40);
        columns[k++].setMaxWidth(40);

        columns[k] = new TableColumn(wfmStrings.statusEdit(), 65);
        columns[k].setAlignment(Style.CENTER);
        columns[k].setMinWidth(80);
        columns[k++].setMaxWidth(80);

        columns[k] = new TableColumn(settingsStrings.assigneeView(), 85);
        columns[k].setAlignment(Style.CENTER);
        columns[k].setMinWidth(85);
        columns[k++].setMaxWidth(85);


        columns[k] = new TableColumn(settingsStrings.assigneeEdit(), 85);
        columns[k].setAlignment(Style.CENTER);
        columns[k].setMinWidth(85);
        columns[k++].setMaxWidth(85);

        columns[k] = new TableColumn(settingsStrings.assigneeStatusEdit(), 110);
        columns[k].setAlignment(Style.CENTER);
        columns[k].setMinWidth(110);
        columns[k++].setMaxWidth(110);

        columns[k] = new TableColumn(settingsStrings.permissionsEdit(), 95);
        columns[k].setAlignment(Style.CENTER);
        columns[k].setMinWidth(95);
        columns[k++].setMaxWidth(95);

        columns[k] = new TableColumn(settingsStrings.timesheetEntryAdd(), 110);
        columns[k].setAlignment(Style.CENTER);
        columns[k].setMinWidth(110);
        columns[k++].setMaxWidth(110);

        columns[k] = new TableColumn(settingsStrings.fullControll(), 70);
        columns[k].setAlignment(Style.CENTER);
        columns[k].setMinWidth(70);
        columns[k++].setMaxWidth(70);

        columns[k] = new TableColumn(settingsStrings.action(), 70);
        columns[k].setAlignment(Style.CENTER);
        columns[k].setMinWidth(70);
        columns[k++].setMaxWidth(70);

        TableColumnModel cm = new TableColumnModel(columns);
        table = new Table(Style.MULTI | Style.HORIZONTAL, cm);
        table.setHeight("178px");
        table.setBorders(false);
        add(table);
    }


    public void viewShow() {
        RbacService.App.get().getCompanyDirectTaskPolicy(new AbstractAsyncCallback<TaskPolicyItem[]>() {
            public void failure(Throwable caught) {

            }

            public void success(TaskPolicyItem[] items) {
                drawPolicyTable(items);
            }
        });
    }

    private void drawPolicyTable(TaskPolicyItem[] items) {
        for (int i = 0; i < items.length; i++) {
            final TaskPolicyItem item = items[i];
            TaskPermissionItem permissionItem = item.getPermissionItems();
            final CheckBox view = new CheckBox();
            view.setValue(permissionItem.isView());
            final CheckBox edit = new CheckBox();
            edit.setValue(permissionItem.isEdit());
            final CheckBox delete = new CheckBox();
            delete.setValue(permissionItem.isDelete());
            final CheckBox statusEdit = new CheckBox();
            statusEdit.setValue(permissionItem.isStatusEdit());
            final CheckBox assigneeView = new CheckBox();
            assigneeView.setValue(permissionItem.isAssigneeView());
            final CheckBox assigneeEdit = new CheckBox();
            assigneeEdit.setValue(permissionItem.isAssigneeEdit());
            final CheckBox assigneeStatusEdit = new CheckBox();
            assigneeStatusEdit.setValue(permissionItem.isAssigneeStatusEdit());
            final CheckBox permesionsEdit = new CheckBox();
            permesionsEdit.setValue(permissionItem.isPermissionsEdit());
            final CheckBox timesheetAdd = new CheckBox();
            timesheetAdd.setValue(permissionItem.isTimesheetEntryAdd());
            final CheckBox fullControll = new CheckBox();
            fullControll.setValue(permissionItem.isFullControl());

            if (permissionItem.isFullControl()) {
                view.setEnabled(false);
                edit.setEnabled(false);
                delete.setEnabled(false);
                statusEdit.setEnabled(false);
                assigneeEdit.setEnabled(false);
                assigneeView.setEnabled(false);
                assigneeStatusEdit.setEnabled(false);
                permesionsEdit.setEnabled(false);
            } else if (permissionItem.isEdit()) {
                view.setEnabled(false);
                assigneeView.setEnabled(false);
                assigneeEdit.setEnabled(false);
                statusEdit.setEnabled(false);
            } else if (permissionItem.isAssigneeStatusEdit() || permissionItem.isAssigneeEdit()) {
                assigneeView.setEnabled(false);
                view.setEnabled(false);
            } else if (permissionItem.isAssigneeView() || permissionItem.isTimesheetEntryAdd()) {
                view.setEnabled(false);
            }

            view.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    if (!view.getValue() && edit.getValue()) {
                        view.setValue(true);
                    } else if (!view.getValue() && assigneeStatusEdit.getValue()) {
                        view.setValue(true);
                    } else if (!view.getValue() && statusEdit.getValue()) {
                        view.setValue(true);
                    } else if (!view.getValue() && assigneeEdit.getValue()) {
                        view.setValue(true);
                    } else if (!view.getValue() && assigneeView.getValue()) {
                        view.setValue(true);
                    } else if (!view.getValue() && timesheetAdd.getValue()) {
                        view.setValue(true);
                    }
                }
            });

            assigneeView.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    if (assigneeView.getValue()) {
                        view.setValue(true);
                        view.setEnabled(false);
                    } else {
                        view.setEnabled(true);
                    }
                    if (!assigneeView.getValue() && assigneeEdit.getValue()) {
                        assigneeView.setValue(true);
                    } else if (!assigneeView.getValue() && assigneeStatusEdit.getValue()) {
                        assigneeView.setValue(true);
                    } else if (!assigneeView.getValue() && edit.getValue()) {
                        assigneeView.setValue(true);
                    }
                }
            });

            statusEdit.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    if (!statusEdit.getValue() && edit.getValue()) {
                        statusEdit.setValue(true);
                    }
                }
            });

            assigneeEdit.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    if (assigneeEdit.getValue()) {
                        assigneeView.setValue(true);
                        view.setValue(true);
                        assigneeView.setEnabled(false);
                        view.setEnabled(false);
                    } else {
                        assigneeView.setEnabled(true);
                        view.setEnabled(true);
                    }
                    if (!assigneeEdit.getValue() && edit.getValue()) {
                        assigneeEdit.setValue(true);
                    }
                }
            });

            assigneeStatusEdit.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    if (assigneeStatusEdit.getValue()) {
                        assigneeView.setValue(true);
                        view.setValue(true);
                        assigneeView.setEnabled(false);
                        view.setEnabled(false);
                    } else {
                        assigneeView.setEnabled(true);
                        view.setEnabled(true);
                    }
                }
            });

            edit.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    if (edit.getValue()) {
                        view.setValue(true);
                        assigneeView.setValue(true);
                        assigneeEdit.setValue(true);
                        statusEdit.setValue(true);
                        view.setEnabled(false);
                        assigneeView.setEnabled(false);
                        assigneeEdit.setEnabled(false);
                        statusEdit.setEnabled(false);
                    } else {
                        view.setEnabled(true);
                        assigneeView.setEnabled(true);
                        assigneeEdit.setEnabled(true);
                        statusEdit.setEnabled(true);
                    }
                }
            });

            fullControll.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    if (fullControll.getValue()) {
                        view.setValue(true);
                        edit.setValue(true);
                        delete.setValue(true);
                        statusEdit.setValue(true);
                        assigneeEdit.setValue(true);
                        assigneeView.setValue(true);
                        assigneeStatusEdit.setValue(true);
                        permesionsEdit.setValue(true);

                        view.setEnabled(false);
                        edit.setEnabled(false);
                        delete.setEnabled(false);
                        statusEdit.setEnabled(false);
                        assigneeEdit.setEnabled(false);
                        assigneeView.setEnabled(false);
                        assigneeStatusEdit.setEnabled(false);
                        permesionsEdit.setEnabled(false);

                    } else {
                        view.setEnabled(true);
                        edit.setEnabled(true);
                        delete.setEnabled(true);
                        statusEdit.setEnabled(true);
                        assigneeEdit.setEnabled(true);
                        assigneeView.setEnabled(true);
                        assigneeStatusEdit.setEnabled(true);
                        permesionsEdit.setEnabled(true);
                    }
                }
            });

            final WfmButton2 update = new WfmButton2(wfmStrings.update());
            update.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent be) {
                    item.getPermissionItems().setView(view.getValue());
                    item.getPermissionItems().setEdit(edit.getValue());
                    item.getPermissionItems().setDelete(delete.getValue());
                    item.getPermissionItems().setStatusEdit(statusEdit.getValue());
                    item.getPermissionItems().setAssigneeView(assigneeView.getValue());
                    item.getPermissionItems().setAssigneeEdit(assigneeEdit.getValue());
                    item.getPermissionItems().setAssigneeStatusEdit(assigneeStatusEdit.getValue());
                    item.getPermissionItems().setPermissionsEdit(permesionsEdit.getValue());
                    item.getPermissionItems().setTimesheetEntryAdd(timesheetAdd.getValue());
                    item.getPermissionItems().setFullControl(fullControll.getValue());
                    saveUpdateTaskPolice(update, item);
                }
            });

            HTML name = new HTML(item.getRelation());
            name.setTitle(item.getRelationDescription());
            Object[] values = new Object[12];
            values[0] = name;
            values[1] = view;
            values[2] = edit;
            values[3] = delete;
            values[4] = statusEdit;
            values[5] = assigneeView;
            values[6] = assigneeEdit;
            values[7] = assigneeStatusEdit;
            values[8] = permesionsEdit;
            values[9] = timesheetAdd;
            values[10] = fullControll;
            values[11] = update;

            TableItem tItem = new TableItem(values);
            table.add(tItem);
        }
    }

    private void saveUpdateTaskPolice(final WfmButton2 update, TaskPolicyItem item) {
        update.setEnabled(false);
        BillboardPanel.get().show();
        RbacService.App.get().saveTaskPolicy(item, new AbstractAsyncCallback<Void>() {
            public void failure(Throwable caught) {
                BillboardPanel.get().hide();
                update.setEnabled(true);
            }

            public void success(Void result) {
                BillboardPanel.get().hide();
                 Info.show("", settingsStrings.yourCompanysTaskPolicyUpdated(), Info.Type.INFO);
                update.setEnabled(true);
            }
        });
    }
}
*/
