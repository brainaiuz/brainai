package com.finnetlimited.reportservice.core.client.ui.table;

import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.finnetlimited.reportservice.core.client.ui.listbox.DRSListBox;
import com.finnetlimited.reportservice.core.client.ui.panel.DRSVerticalPanel;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Ulugbek Normatov
 * Date: Mar 2, 2011
 * Time: 12:49:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChartTableSeriaAddRow extends VerticalPanel {

    private LinkedList<ColumnRpc> columns;
    private HashMap<Integer, ColumnRpc> map = new HashMap<>();

    public ChartTableSeriaAddRow(LinkedList<ColumnRpc> columns) {
        this.columns = columns;
        init();
        // addRow();
    }

    private void init() {
        int i = -1;
        for (ColumnRpc column : columns) {
            map.put(++i, column);
        }

    }

    public void addRow() {
        SeriaAddRow row = new SeriaAddRow();
        add(row);
    }

    public void removeRow(Widget widget) {
        remove(widget);
    }

    public ArrayList<ColumnRpc> getSelectedColumns() {
        ArrayList<ColumnRpc> selectedSeriaColumns = new ArrayList<>();
        for (int i = 0; i < getWidgetCount(); i++) {
            SeriaAddRow row = (SeriaAddRow) getWidget(i);
            selectedSeriaColumns.add(row.getValue());
        }
        return selectedSeriaColumns;
    }

    public void setSelectedColumns(ArrayList<ColumnRpc> columns) {
        clear();
        int i = -1;
        for (ColumnRpc column : columns) {
            SeriaAddRow row = new SeriaAddRow(column);
            add(row);
            i++;
        }
    }

    public void clear() {
        for (int i = 0; i < getWidgetCount(); i++) {
            this.remove(i);
        }
    }

    public class SeriaAddRow extends HorizontalPanel {

        private DRSListBox columnsListBox;
        private ColumnRpc column;
        private TextBox name;
        private Anchor remove;

        public SeriaAddRow() {
            init();
        }

        public SeriaAddRow(ColumnRpc column) {
            this.column = column;
            init();
        }

        private void init() {
            columnsListBox = new DRSListBox();
            columnsListBox.setWidth("100px");
            int i = 0;
            for (ColumnRpc column : columns) {
                columnsListBox.addItem(column.getTitle(), column.getName());
                if (this.column != null && this.column.getName().equals(column.getName())) {
                    columnsListBox.setSelectedIndex(i + 1);
                }
                i++;
            }

            name = new TextBox();
            name.setWidth("100px");
            if (column != null) {
                name.setValue(column.getTitle());
            }

            remove = new Anchor("<span style='color:#DB2461;'>Remove</span>", true);
            remove.addClickHandler(clickEvent -> {
                Anchor button = (Anchor) clickEvent.getSource();
                removeRow(button.getParent());
            });

            remove.setVisible(true);
            add(new DRSVerticalPanel("Seria", columnsListBox));
            add(new DRSVerticalPanel("Title", name));
            add(remove);
            setCellVerticalAlignment(remove, VerticalPanel.ALIGN_BOTTOM);
        }

        public void setValue(ColumnRpc column) {
            for (int i = 0; i < columnsListBox.getItemCount(); i++) {
                if (columnsListBox.getValue(i).equals(column.getName())) {
                    columnsListBox.setSelectedIndex(i);
                    break;
                }
            }
            name.setValue(column.getTitle());
        }

        public ColumnRpc getValue() {
            ColumnRpc column = map.get(columnsListBox.getSelectedIndex() - 1);
            if (name != null && !"".equals(name.getValue())) {
                column.setTitle(name.getValue());
            }
            return column;
        }
    }
}
