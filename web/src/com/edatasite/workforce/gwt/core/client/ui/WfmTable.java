package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.WfmTableItem;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.List;

public class WfmTable extends Composite {

    private List items;
    private List removePanels;
    private FlowPanel panel;

    public WfmTable(FlowPanel panel) {
        this.panel = panel;
        init();
        initWidget(panel);
    }

    private void init() {
        items = new ArrayList();
    }
    public void add(WfmTableItem item) {
        if (item != null) {
            insert(item, getItemsCount());
        }
    }

    public void insert(WfmTableItem item, int index) {
        items.add(index, item);
        panel.add(item);
    }

    public List getItems() {
        return items;
    }

    public WfmTableItem getItem(int index) {
        if (index < 0 || index >= getItemsCount()) {
            return null;
        }
        return (WfmTableItem) items.get(index);
    }

    public int getItemsCount() {
        return items.size();
    }

    public void remove(WfmTableItem item) {
        panel.remove(item);
        items.remove(item);
    }

    public void removeAll() {
        int count = getItemsCount();
        for (int i = 0; i < count; i++) {
            remove(getItem(0));
        }
    }

    //////////////////////////============Second Part of component============//////////////////////////////////////
    private FlexTable table;
    private FlowPanel form;
    private int rows = 0;
    private boolean isCollapse = true;

    public WfmTable() {
        initialize();
    }

    public WfmTable(boolean isCollapse) {
        this.isCollapse = isCollapse;
        initialize();
    }

    private void initialize() {
        table = new FlexTable();
        table.setWidth("100%");
        removePanels = new ArrayList();

        form = new FlowPanel(/*isCollapse ? (Style.HEADER | Style.COLLAPSE) : Style.HEADER*/);
        form.setWidth("600px");
        form.addStyleName("file--WfmTable");
        form.add(table);
        initWidget(form);
    }

    public void add(Object[] objects) {
        int column = 0;
        for (Object object : objects) {
            insert(object, column);
            column++;
        }
        rows++;
    }

    private void insert(Object object, int column) {
        if (object instanceof Widget) {
            table.setWidget(rows, column, (Widget) object);
        } else if (object instanceof String) {
            table.setWidget(rows, column, new HTML((String) object));
        } else if (object instanceof Long) {
            table.setWidget(rows, column, new HTML(initSize(((Long) object).intValue())));
        }
    }

    public void add(Object object) {
        insert(object, 0);
        rows++;
    }

    public RemovePanel createRemovePanel(int rowIndex, Widget removeLink) {
        RemovePanel panel = new RemovePanel(rowIndex, removeLink);
        removePanels.add(panel);
        return panel;
    }

    private String initSize(int size) {
        int kb = 1024;
        int mb = kb * 1024;
        if (size != 0) {
            if (size > kb && size < mb) {
                return size / kb + "." + String.valueOf(size % kb).substring(0, 1) + " KB";
            } else if (size > mb) {
                return size / mb + "." + String.valueOf(size % mb).substring(0, 1) + " MB";
            } else {
                return size + " B";
            }
        } else {
            return "0 B";
        }
    }

    public class RemovePanel extends FocusPanel {
        private int rowIndex;
        private Widget removeLink;

        public RemovePanel(int rowIndex, Widget removeLink) {
            this.rowIndex = rowIndex;
            this.removeLink = removeLink;
        }
    }

    public int getRowCount() {
        return table.getRowCount();
    }

    public void remove(RemovePanel removePanelRow) {
        table.removeRow(removePanelRow.rowIndex);
        removePanels.remove(removePanelRow);
        for (int i = removePanelRow.rowIndex; i < removePanels.size(); i++) {
            ((RemovePanel) removePanels.get(i)).rowIndex--;
        }

    }

    public FlowPanel getForm() {
        return form;
    }

    public void removeAlll() {
        int count = getRowCount();
        for (int i = 0; i < count; i++) {
            table.removeRow(0);
        }
    }
}
