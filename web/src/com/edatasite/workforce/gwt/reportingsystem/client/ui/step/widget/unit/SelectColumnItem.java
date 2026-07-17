package com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit;

import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;

/**
 * Created by Virus on 8/27/14.
 */
public class SelectColumnItem extends KpiCheckBox {

    private ColumnRpc columnRpc;
    private GroupCommand command;

    public SelectColumnItem(final ColumnRpc columnRpc) {

        this.columnRpc = columnRpc;

        this.setText(columnRpc.getTitle());
        this.setValue(columnRpc.isChecked());
        this.addValueChangeHandler((event) -> {
            check(this.getValue(), true);
        });
    }

    public ColumnRpc getColumnRpc() {
        return columnRpc;
    }

    public void makeGrouoColumn(boolean isGroupColumn) {
        if (isGroupColumn) {
            check(isGroupColumn, true);
        }
        setEnabled(!isGroupColumn);
    }

    public void check(boolean check, boolean timeToSelectAll) {
        if (check != this.columnRpc.isChecked()) {
            this.setValue(check);
            this.columnRpc.setChecked(check);
            if (command != null) {
                command.execute(timeToSelectAll);
            }
        }
    }

    public void setCommand(GroupCommand command) {
        this.command = command;
    }

    public interface GroupCommand {
        void execute(boolean isTimeToSelectAll);
    }
}