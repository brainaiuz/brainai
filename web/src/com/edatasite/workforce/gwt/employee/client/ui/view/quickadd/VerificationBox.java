package com.edatasite.workforce.gwt.employee.client.ui.view.quickadd;

import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTextBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;

import java.util.ArrayList;
import java.util.List;

public class VerificationBox {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();


    private TextBox lookUp;
    private WfmButton2 closeButton;
    private WfmButton2 addButton;
    private EditableTable table;

    public VerificationBox() {
        init();
    }


    private void init() {
        lookUp = new TextBox();
        lookUp.setPlaceHolder(wfmStrings.enterNameOrNumber());
        closeButton = new WfmButton2(wfmStrings.close(), WfmButton2.BTN_DEFAULT);

        KpiModal shell = new KpiModal();
        shell.setWidth("1000px");
        shell.setTitle(wfmStrings.verification());
        shell.add(lookUp);
        table = new EditableTable(getColumns(), false, false);


        lookUp.addChangeHandler(event -> {
            EmployeeService.App.get().getEmployeeForVerification(lookUp.getText(), new AsyncCallback<List<ProfileItem>>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(List<ProfileItem> profileItems) {
                    if (profileItems == null || profileItems.isEmpty()) {

                    }
                    initRows(profileItems);
                    shell.add(table);
                }
            });
        });
        lookUp.getElement().setAttribute("style", "margin-bottom: 15px;");
        closeButton = new WfmButton2(wfmStrings.close());
        closeButton.addClickHandler(event -> {
            shell.close();
        });
        shell.addButton(closeButton);
        shell.open();

    }


    private ColumnConfig[] getColumns() {
        ArrayList<ColumnConfig> columns = new ArrayList<>();
//        columns.add(new ColumnConfig(CustomCell.class, "PHOTO", "", 120,false, Constants.CENTER_ALIGN_CELL));
        columns.add(new ColumnConfig(CustomCell.class, "NAME", wfmStrings.fullName(), 120, false, Constants.LEFT_ALIGN_CELL));
        columns.add(new ColumnConfig(CustomCell.class, "NUMBER", wfmStrings.number(), 120, false, Constants.LEFT_ALIGN_CELL));
        columns.add(new ColumnConfig(CustomCell.class, "PASSPORT", wfmStrings.passportNumber(), 120, false, Constants.LEFT_ALIGN_CELL));
        columns.add(new ColumnConfig(CustomCell.class, "STATUS", wfmStrings.status(), 100, false, Constants.LEFT_ALIGN_CELL));
        columns.add(new ColumnConfig(CustomCell.class, "STATE", wfmStrings.state(), 150, false, Constants.LEFT_ALIGN_CELL));

        return columns.toArray(new ColumnConfig[]{});
    }

    private void initRows(List<ProfileItem> profileItems) {
        if (profileItems != null) {
            table.removeAllRows();
            for (ProfileItem profileItem : profileItems) {
                List<Object> widgets = new ArrayList<>();
//                EditableAvatarBox avatarImage = new EditableAvatarBox();
//                if (profileItem.getEmployeeImageUrl() != null) {
//                    avatarImage.setUrl(profileItem.getEmployeeImageUrl());
//                }

                EditableTextBox name = new EditableTextBox();
                name.setText(profileItem.getName());

                EditableTextBox number = new EditableTextBox();
                number.setText(profileItem.getEmpCode());

                EditableTextBox passport = new EditableTextBox();
                passport.setText(profileItem.getPassportNumber());

                EditableTextBox statusBox = new EditableTextBox();
                statusBox.setText(profileItem.getStatus());

                EditableTextBox resignStatusBox = new EditableTextBox();
                resignStatusBox.setText(profileItem.getRejectionReason());


                name.setEnabled(false);
                number.setEnabled(false);
                passport.setEnabled(false);
                statusBox.setEnabled(false);
                resignStatusBox.setEnabled(false);

                widgets.add(name);
                widgets.add(number);
                widgets.add(passport);
                widgets.add(statusBox);
                widgets.add(resignStatusBox);

                table.addRow(widgets.toArray());
            }
        }
    }


}
