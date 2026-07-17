/*
package com.edatasite.workforce.gwt.profile.client.ui.view.policytab;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.server.rpc.TaskPermissionItem;
import com.edatasite.workforce.gwt.core.server.rpc.TaskPolicyItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.billboard.BillboardPanel;
import com.edatasite.workforce.gwt.core.client.ui.customtabbar.CustomTabWidget;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Icon;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;

*/
/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 02-Jun-2010
 * Time: 19:06:30
 *//*

public class IndirectRelationTab extends CustomTabWidget implements Constants {

    private static final SettingStrings settingsStrings=SettingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private Table table;

    public IndirectRelationTab() {
        super(settingsStrings.indirectRelationTable());
    }

    public void initData() {

        int k = 0;
        TableColumn[] columns = new TableColumn[14];

        columns[k] = new TableColumn(settingsStrings.trusteeName(), 100);
        columns[k].setAlignment(Style.LEFT);
        columns[k].setSortable(false);
        columns[k].setMinWidth(70);
        columns[k++].setMaxWidth(200);

        columns[k] = new TableColumn(settingsStrings.trusteeType(), 60);
        columns[k].setAlignment(Style.LEFT);
        columns[k].setSortable(false);
        columns[k].setMinWidth(50);
        columns[k++].setMaxWidth(150);

        columns[k] = new TableColumn(wfmStrings.view(), 35);
        columns[k].setAlignment(Style.CENTER);
        columns[k].setSortable(false);
        columns[k].setMinWidth(35);
        columns[k++].setMaxWidth(35);

        columns[k] = new TableColumn(wfmStrings.edit(), 25);
        columns[k].setAlignment(Style.CENTER);
        columns[k].setSortable(false);
        columns[k].setMinWidth(25);
        columns[k++].setMaxWidth(25);

        columns[k] = new TableColumn(wfmStrings.delete(), 40);
        columns[k].setAlignment(Style.CENTER);
        columns[k].setSortable(false);
        columns[k].setMinWidth(40);
        columns[k++].setMaxWidth(40);

        columns[k] = new TableColumn(wfmStrings.statusEdit(), 60);
        columns[k].setAlignment(Style.CENTER);
        columns[k].setSortable(false);
        columns[k].setMinWidth(60);
        columns[k++].setMaxWidth(60);

        columns[k] = new TableColumn(settingsStrings.assigneeView(), 85);
        columns[k].setAlignment(Style.CENTER);
        columns[k].setSortable(false);
        columns[k].setMinWidth(85);
        columns[k++].setMaxWidth(85);


        columns[k] = new TableColumn(settingsStrings.assigneeEdit(), 75);
        columns[k].setAlignment(Style.CENTER);
        columns[k].setSortable(false);
        columns[k].setMinWidth(75);
        columns[k++].setMaxWidth(75);

        columns[k] = new TableColumn(settingsStrings.assigneeStatusEdit(), 110);
        columns[k].setAlignment(Style.CENTER);
        columns[k].setSortable(false);
        columns[k].setMinWidth(110);
        columns[k++].setMaxWidth(110);

        columns[k] = new TableColumn(settingsStrings.permissionsEdit(), 85);
        columns[k].setAlignment(Style.CENTER);
        columns[k].setSortable(false);
        columns[k].setMinWidth(85);
        columns[k++].setMaxWidth(85);

        columns[k] = new TableColumn(settingsStrings.timesheetEntryAdd(), 110);
        columns[k].setAlignment(Style.CENTER);
        columns[k].setSortable(false);
        columns[k].setMinWidth(110);
        columns[k++].setMaxWidth(110);

        columns[k] = new TableColumn(settingsStrings.fullControll(), 65);
        columns[k].setAlignment(Style.CENTER);
        columns[k].setSortable(false);
        columns[k].setMinWidth(65);
        columns[k++].setMaxWidth(65);

        columns[k] = new TableColumn(wfmStrings.update(), 60);
        columns[k].setAlignment(Style.CENTER);
        columns[k].setSortable(false);
        columns[k].setMinWidth(60);
        columns[k++].setMaxWidth(60);

        columns[k] = new TableColumn(wfmStrings.delete(), 60);
        columns[k].setAlignment(Style.CENTER);
        columns[k].setSortable(false);
        columns[k].setMinWidth(60);
        columns[k++].setMaxWidth(60);

        TableColumnModel cm = new TableColumnModel(columns);
        table = new Table(Style.MULTI | Style.HORIZONTAL, cm);
        table.setHeight("248px");
        table.setBorders(false);
        add(table);
    }


    public void viewShow() {
        RbacService.App.get().getCompanyIndirectTaskPolicy(new AbstractAsyncCallback<TaskPolicyItem[]>() {
            public void failure(Throwable caught) {

            }

            public void success(TaskPolicyItem[] items) {
                table.removeAll();
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

            final WfmButton2 del = new WfmButton2(wfmStrings.delete());
            if (item.getEntityType() == BUILT_IN) {
                del.setEnabled(false);
            }

            Object[] values = new Object[14];
            values[0] = item.getTrusteeName();
            values[1] = item.getTrusteeType();
            values[2] = view;
            values[3] = edit;
            values[4] = delete;
            values[5] = statusEdit;
            values[6] = assigneeView;
            values[7] = assigneeEdit;
            values[8] = assigneeStatusEdit;
            values[9] = permesionsEdit;
            values[10] = timesheetAdd;
            values[11] = fullControll;
            values[12] = update;
            values[13] = del;

            final TableItem tItem = new TableItem(values);
            table.add(tItem);

            del.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent be) {
                    final WfmMessageBox msg = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo,true);
                    msg.setText(settingsStrings.deleteTaskPolicy());
                    msg.setMessage(settingsStrings.areYouSureWantRemoveTrustee());
                    msg.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            deleteTaskPolice(item.getTaskPolicyId(), del, tItem);
                        }
                    });
                    msg.open();
                }
            });
        }
    }

    private void deleteTaskPolice(Integer taskPolicyId, final WfmButton2 delete, final TableItem tItem) {
        delete.setEnabled(false);
        BillboardPanel.get().show();
        RbacService.App.get().deleteTaskPolice(taskPolicyId, new AbstractAsyncCallback<Void>() {
            public void failure(Throwable caught) {
                BillboardPanel.get().hide();
                delete.setEnabled(true);
            }

            public void success(Void result) {
                BillboardPanel.get().hide();
                 Info.show("", settingsStrings.yourCompanysTaskPolicy(), Info.Type.INFO);
                table.remove(tItem);
            }
        });
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
