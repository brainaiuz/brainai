package com.edatasite.workforce.gwt.task.client.ui;

import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.DateTimePickerCellEditor;
import com.google.gwt.user.client.Command;

import java.util.Date;

/**
 * User: Ilxom Lutfullaev
 * Date: 8/21/13
 * Time: 4:44 PM
 */

public class ChangeStartDatePopup extends KpiModal implements Constants {

    DateTimePickerCellEditor dp;
    Command command;

    public ChangeStartDatePopup() {
        super();
        setCloseButton(true);
        setTitle(wfmStrings.pleaseChooseValidDate());
        setSize(340, 275);

        dp = new DateTimePickerCellEditor<Date>(){
            @Override
            protected Date getValue() {return new Date();}

            @Override
            protected void setValue(Date cellValue) {
            }

            @Override
            protected void accept() {
                command.execute();
            }

            @Override
            protected void cancel() {
                close();
            }
        };
        add(dp);
    }

    public void onSubmit(Command command) {
        this.command = command;
    }

    public Date getDate() {
        return dp.getDate();
    }
}