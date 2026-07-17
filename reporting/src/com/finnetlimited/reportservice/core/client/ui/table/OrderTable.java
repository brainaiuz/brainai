package com.finnetlimited.reportservice.core.client.ui.table;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandlerAdapter;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.HorizontalPanelDropController;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.finnetlimited.reportservice.core.client.enumtype.IdType;
import com.finnetlimited.reportservice.core.client.ui.panel.DRSDragDropPanel;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: ${Dilsh0d}
 * Date: 24-Mar-2010
 * Time: 16:31:52
 */
public class OrderTable extends HTMLPanel {

    private static final String _id = IdType.ORDER_TABLE.getName();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static int num = 0;

    private String id;
    private FlexTable table;
    private AbsolutePanel boundaryPanel;
    private FlexTable.FlexCellFormatter formatter;

    private ArrayList<ColumnRpc> columnNames;
    private HashMap<String, Integer> widgetsPos = new HashMap<>();
    private Command command;

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public OrderTable() {
        super("");
        setStyleName("sdbr-1");
        getElement().setAttribute("id", (_id + num));
        id = _id + num;
        num++;

    }

    private void init() {
        table = new FlexTable();
        table.setStyleName("order-table");
        formatter = table.getFlexCellFormatter();

        table.setHTML(0, 0, "<b style='padding-left:15px'>" + wfmStrings.standartSummaryFields() + "</b>");
        formatter.setStyleName(0, 0, "bheader");
        table.getRowFormatter().setStyleName(0, "theader");

        boundaryPanel = new AbsolutePanel();
        boundaryPanel.setSize("100%", "100%");
        table.setWidget(1, 0, boundaryPanel);

        add(table, id);
    }

    public void removeAllColumn() {
        widgetsPos.clear();
        table.removeAllRows();
    }

    public void addColumns(ArrayList<ColumnRpc> columns) {
        init();
        this.columnNames = columns;
        if (columnNames != null && columnNames.size() != 0) {

            ScrollPanel panel = new ScrollPanel();
            panel.setSize("620px", "150px");

            boundaryPanel.add(panel);

            // initialize our column drag controller
            PickupDragController columnDragController = new PickupDragController(boundaryPanel, false);
            columnDragController.setBehaviorMultipleSelection(false);

            // initialize horizontal panel to hold our columns
            final HorizontalPanel horizontalPanel = new HorizontalPanel();
            horizontalPanel.setSpacing(0);
            panel.add(horizontalPanel);

            // initialize our column drop controller
            HorizontalPanelDropController columnDropController = new HorizontalPanelDropController(horizontalPanel);
            columnDragController.registerDropController(columnDropController);

            columnDragController.addDragHandler(new DragHandlerAdapter() {
                @Override
                public void onDragEnd(DragEndEvent event) {
                    if (command != null) {
                        command.execute();
                    }
                    DRSDragDropPanel dragPanel = (DRSDragDropPanel) event.getSource();
                    int nowPos = horizontalPanel.getWidgetIndex(dragPanel);
                    sortByPostion(nowPos, dragPanel.getName());
                }
            });


            for (int i = 0; i < columnNames.size(); i++) {
                //initialize a vertical panel to hold the heading and a second vertical
                DRSDragDropPanel columnCompositePanel = new DRSDragDropPanel(columnNames.get(i).getTitle(), columnNames.get(i).getName());
                horizontalPanel.add(columnCompositePanel);

                //make the column draggable by its heading
                columnDragController.makeDraggable(columnCompositePanel, columnCompositePanel.getHeading());

                widgetsPos.put(columnNames.get(i).getName(), i);
            }
        }
    }

    private void sortByPostion(int newPos, String name) {
        int oldPos = widgetsPos.get(name);
        widgetsPos.put(name, newPos);

        for (String key : widgetsPos.keySet()) {
            if (!key.equals(name)) {
                int keyValue = widgetsPos.get(key);
                if (oldPos > newPos && oldPos > keyValue && newPos <= keyValue) {
                    keyValue++;
                    widgetsPos.put(key, keyValue);
                } else if (oldPos < newPos && oldPos < keyValue && newPos >= keyValue) {
                    keyValue--;
                    widgetsPos.put(key, keyValue);
                }
            }
        }

        ColumnRpc column = columnNames.get(oldPos);
        columnNames.remove(column);
        columnNames.add(newPos, column);
    }

    public ArrayList<ColumnRpc> getCustomOrderColumns() {
        return columnNames;
    }
}
