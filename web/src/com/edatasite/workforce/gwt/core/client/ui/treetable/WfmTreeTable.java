package com.edatasite.workforce.gwt.core.client.ui.treetable;

import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.SortCommand;
import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;

import java.util.ArrayList;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 17-Jul-2010
 * Time: 13:18:20
 * <p/>
 * WFM Tree Table Widget
 */
public class WfmTreeTable extends HTMLPanel {

    private static final String tableId = "tree-table";
    private static int numId = 0;

    private String id;
    private boolean isSelectGetTreeChilds;
    private WfmToolBar topToolBar;
    private WfmToolBar bottomToolBar;
    private WfmTreeTableHeader header;
    private TreeTable treeTable;
    private HTMLPanel table;
    private ScrollPanel contantPanel;
    private TreeListDataCallback treeCallback;
    private WfmTreeTableColumn[] columns;
    private WfmTreeTableChildProvider childProvider;
    private WfmTreeTableCallbackProvider provider;
    private WfmTreeTableDesigner tableDesigner;
    private MaterialPanel emptyPanel;

    public WfmTreeTable(WfmTreeTableColumn[] columns, WfmTreeTableCallbackProvider provider, WfmTreeTableChildProvider childProvider, WfmTreeTableDesigner tableDesigner) {
        this(columns, provider, childProvider, tableDesigner, true);
    }

    public WfmTreeTable(WfmTreeTableColumn[] columns, WfmTreeTableCallbackProvider provider, WfmTreeTableChildProvider childProvider, WfmTreeTableDesigner tableDesigner, boolean isSelectGetTreeChilds) {
        super("");
        setSize("100%", "100%");
        getElement().setAttribute("id", id = (tableId + numId));
        getElement().getStyle().setPosition(Style.Position.STATIC);
        this.columns = columns;
        this.provider = provider;
        this.childProvider = childProvider;
        this.tableDesigner = tableDesigner;
        this.isSelectGetTreeChilds = isSelectGetTreeChilds;
        this.setStyleName("wfm-treeTable");
        initialization();
        addWidgets();
        numId++;
    }

    /**
     * Table Widgets Initilazation
     */
    private void initialization() {
        treeTable = new TreeTable();
        treeTable.getElement().getStyle().setTableLayout(Style.TableLayout.FIXED);
//        treeTable.setWidth("100%");
//        treeTable.setCellPadding(0);
//        treeTable.setCellSpacing(0);
        treeTable.setRenderer(new TreeRenderer());
        emptyPanel = new MaterialPanel();
        emptyPanel.setVisible(false);
        emptyPanel.getElement().getStyle().setMarginTop(10, Style.Unit.PCT);
        emptyPanel.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
        treeTableListeners();
        contantPanel = new ScrollPanel();
//        contantPanel.setWidth("100%");
        contantPanel.add(treeTable);
        contantPanel.addStyleName("gwt-Style");
    }

    /**
     * Tree Table Listeners
     */
    private void treeTableListeners() {
        treeTable.addTreeTableListener(new TreeTableListener() {
            public void onTreeItemSelected(TreeItem item) {
                Object itemObject = item.getUserObject();
                if (isSelectGetTreeChilds && item.isHaveChilds() && !item.isClickedTree()) {
                    item.updateToLoading();
                    provider.getTreeTableData(itemObject, item, treeCallback);
                } else {
                    item.updateState();
                }
            }

            public void onTreeItemStateChanged(TreeItem item) {

            }
        });

        treeCallback = new TreeListDataCallback() {
            public void onSuccess(Object[] data, TreeItem item, Integer itemID, ArrayList<Integer> childrenIds) {
                if (item != null) {
                    if ((data == null || data.length == 0)) {
                        item.setHaveChilds(false);
                    } else {
                        item.setHaveChilds(true);
                    }
                    item.updateState();
                    item.setClickedTree(true);
                }
                fillTreeTable(data, item, itemID, childrenIds);
            }

            public void onFailure(Throwable t, TreeItem item) {
                item.updateState();
            }
        };
        provider.getTreeTableData(null, null, treeCallback);
    }

    /**
     * Fill Tree Table
     *
     * @param data     - data
     * @param treeItem - treeItem
     */
    private void fillTreeTable(Object[] data, TreeItem treeItem, Integer itemID, ArrayList<Integer> childrenIds) {
        if (data != null && data.length > 0) {
            contantPanel.remove(treeTable);
            contantPanel.remove(emptyPanel);
            contantPanel.add(treeTable);
            emptyPanel.setVisible(false);
            for (int i = 0; i < data.length; i++) {
                if (treeItem != null) {
                    if (treeItem.getObjectID() == null) {
                        treeItem.setObjectID(itemID);
                    }
                    TreeItem childrenTreeItem = treeItem.addItem(data[i], childProvider.isHaveChilds(data[i]));
                    childrenTreeItem.setObjectID(childrenIds.get(i));
                } else {
                    TreeItem parentTreeItem = treeTable.addItem(data[i], childProvider.isHaveChilds(data[i]));
                    parentTreeItem.setObjectID(childrenIds.get(i));
                }
                if (!isSelectGetTreeChilds) {
                    fillTreeTable(childProvider.getChilds(data[i]), treeItem, itemID, childrenIds);
                }
            }
        } else {
            if (treeItem == null) {
                emptyPanel.clear();
                emptyPanel.setVisible(true);
                contantPanel.remove(treeTable);
                contantPanel.remove(emptyPanel);
                contantPanel.add(emptyPanel);
                tableDesigner.initDataEmptyTable(dataMessage -> {
                    if (dataMessage != null && dataMessage.getWholeMessage() != null) {
                        emptyPanel.add(dataMessage.getDivPanelMessage());
                    }
                });
            }
        }
    }

    /**
     * Add widget to Tree Table
     */
    private void addWidgets() {
        topToolBar = new WfmToolBar();
        topToolBar.getElement().getStyle().setVerticalAlign(Style.VerticalAlign.TOP);
        topToolBar.getElement().addClassName("wfm-treeTable__inner"); // it wrapp the operPanel https://prnt.sc/oq2od4
//        topToolBar.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);
        bottomToolBar = new WfmToolBar();
        bottomToolBar.getElement().getStyle().setVerticalAlign(Style.VerticalAlign.BOTTOM);
        bottomToolBar.getElement().getStyle().setPosition(Style.Position.ABSOLUTE);
        bottomToolBar.getElement().getStyle().setBottom(0, Style.Unit.PX);
        tableDesigner.treeTableTopPanel(topToolBar);
        tableDesigner.treeTableBottomPanel(bottomToolBar);

        header = new WfmTreeTableHeader();
        String tableId = "wfm-treetable-content" + numId;
        table = new HTMLPanel("");
//        table.getElement().getStyle().setPosition(Style.Position.RELATIVE);
        table.getElement().setAttribute("id", tableId);
        table.setStyleName("wfm-treeTable");
        table.add(header, tableId);
        table.add(contantPanel, tableId);

        add(topToolBar, id);
        add(table, id);
        add(bottomToolBar, id);
    }

    @Override
    protected void onLoad() {
        super.onLoad();
    }

    /**
     * Refresh Table
     */
    public void refresh() {
        treeTable.clear();
        provider.getTreeTableData(null, null, treeCallback);
    }

    public void refresh2(Integer removedItemID, Integer refreshedItemID) {
        TreeItem removedItem = treeTable.getItemByObjectID(removedItemID);
        if (removedItem != null) {
            TreeItem removedItemParent = removedItem.getParentItem();
            if (removedItemParent != null) {
                if (removedItemParent.getChildren() == null || removedItemParent.getChildren().size() == 0) {
                    removedItemParent.setHaveChilds(false);
                } else if (removedItemParent.getChildren().size() == 1) {
                    TreeItem child = removedItemParent.getChild(0);
                    if (child != null && child.getObjectID() != null && child.getObjectID().equals(removedItemID)) {
                        removedItemParent.setHaveChilds(false);
                    }
                }
            }
            removedItem.remove();
        }
        TreeItem refreshedItem = treeTable.getItemByObjectID(refreshedItemID);
        if (refreshedItem != null) {
            refreshedItem.removeItems();
            Object itemObject = refreshedItem.getUserObject();
            provider.getTreeTableData(itemObject, refreshedItem, treeCallback);
            TreeItem refreshedItemParent = refreshedItem.getParentItem();
            if (refreshedItemParent != null) {
                if (refreshedItemParent.getChildren() != null && refreshedItemParent.getChildren().size() > 0) {
                    refreshedItemParent.setHaveChilds(true);
                }
                refreshedItemParent.updateState();
            }
        }
    }

    /**
     * Tree Table header
     */
    protected class WfmTreeTableHeader extends AbstractWfmTreeTableHeader {

        private static final String headerId = "treetable-header";

        private String id;
        private FlexTable header;

        public WfmTreeTableHeader() {
            super("");
            getElement().setAttribute("id", id = (headerId + numId));
            init();
            sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEOUT);
        }

        /**
         * Initialization Header
         */
        private void init() {
            header = new FlexTable();
            header.setStyleName("file--WfmTreeTable"); //https://prnt.sc/sqheok
            header.setCellSpacing(0);
            header.setCellPadding(0);
            header.getElement().getStyle().setProperty("tableLayout", "fixed");
            for (int i = 0; i < columns.length; i++) {

                Div div = new Div();
                div.setStyleName("wfm-treetable-inner-col");

                HorizontalPanelDiv divInner = new HorizontalPanelDiv();
                divInner.setStyleName("wfm-treetable-inner-col-pad");
                if (columns[i].getColumnStyleName() != null) {
                    divInner.addStyleName(columns[i].getColumnStyleName());
                }
                divInner.add(new HTML(columns[i].getColumnName()));

                Icon sortIcon = null;
                if (columns[i].getSortCommand() != null) {
                    sortIcon = new Icon();
                    sortIcon.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    sortIcon.setStyleName("ficon--keyboard-arrow-up");
                    sortIcon.setDisplay(Display.NONE);
                    divInner.add(sortIcon);
                }

                div.add(divInner);
                header.setWidget(0, i, div);
                header.getFlexCellFormatter().setAlignment(0, i, columns[i].getAlignment(), VerticalPanel.ALIGN_MIDDLE);
                header.getFlexCellFormatter().setStyleName(0, i, "wfm-treeTable-header");
                header.getFlexCellFormatter().getElement(0, i).getStyle().setWidth(columns[i].getWidth(), Style.Unit.PCT);

                if (columns[i].getSortCommand() != null) {
                    setSortCommand(columns[i].getSortCommand(), div, sortIcon);
                }

            }
            header.getRowFormatter().setStyleName(0, "thead");
            this.add(header, id);
        }

        @Override
        public void setSortCommand(SortCommand sortCommand, Div div,Icon sortIcon) {
            sortIcon.setDisplay(Display.NONE);
            div.addClickHandler(event -> {
                sortIcon.setDisplay(Display.INLINE);
                if (sortIcon.getStyleName() == null || "ficon--keyboard-arrow-down".equals(sortIcon.getStyleName())) {
                    sortIcon.setStyleName("ficon--keyboard-arrow-up");
                    sortCommand.execute(Constants.ASC_STR);
                } else {
                    sortIcon.setStyleName("ficon--keyboard-arrow-down");
                    sortCommand.execute(Constants.DESC_STR);
                }
            });
        }
    }

    private abstract class AbstractWfmTreeTableHeader extends HTMLPanel {

        public AbstractWfmTreeTableHeader(String html) {
            super(html);
        }

        public abstract void setSortCommand(SortCommand sortCommand, Div div,Icon sortIcon);

    }

    /**
     * Tree Table Renderer
     */
    private class TreeRenderer implements TreeTableRenderer {
        public void renderTreeItem(TreeTable table, TreeItem item, int row) {
            Object o = item.getUserObject();
            if (o != null) {
                if (columns[0].getTreeTableCellWidget().getTreeTableCell(o) instanceof Widget) {
                    item.setWidget((Widget) columns[0].getTreeTableCellWidget().getTreeTableCell(o));
                } else {
                    item.setHTML(columns[0].getTreeTableCellWidget().getTreeTableCell(o).toString());
                }
                table.getFlexCellFormatter().setStyleName(row, 0, "wfm-treeTable-column");
                table.getFlexCellFormatter().setAlignment(row, 0, columns[0].getAlignment(), VerticalPanel.ALIGN_MIDDLE);
                table.getFlexCellFormatter().getElement(row, 0).getStyle().setWidth(columns[0].getWidth(), Style.Unit.PCT);

                for (int col = 1; col < columns.length; col++) {
                    if (columns[col].getTreeTableCellWidget().getTreeTableCell(o) instanceof Widget) {
                        SimplePanel div = new SimplePanel();
                        div.setStyleName("wfm-treetable-inner-col");
                        SimplePanel div1 = new SimplePanel();
                        div1.setStyleName("wfm-treetable-inner-col-pad");
                        div1.setWidget((Widget) columns[col].getTreeTableCellWidget().getTreeTableCell(o));
                        div.setWidget(div1);
                        table.setWidget(row, col, div);
                    } else if (columns[col].getTreeTableCellWidget().getTreeTableCell(o) != null) {
                        table.setHTML(row, col, "<div class='wfm-treetable-inner-col'> <div class='wfm-treetable-inner-col-pad'>" + columns[col].getTreeTableCellWidget().getTreeTableCell(o).toString() + "</div></div>");
                    } else {
                        table.setHTML(row, col, "<div class='wfm-treetable-inner-col'> <div class='wfm-treetable-inner-col-pad'>" + "&nbsp;" + "</div></div>");
                    }

                    table.getFlexCellFormatter().setStyleName(row, col, "wfm-treeTable-column");
                    table.getFlexCellFormatter().setAlignment(row, col, columns[col].getAlignment(), VerticalPanel.ALIGN_MIDDLE);
                    table.getFlexCellFormatter().getElement(row, col).getStyle().setWidth(columns[col].getWidth(), Style.Unit.PCT);
                }
            }
        }
    }

    public void setContentPanelHeight(String height) {
        contantPanel.setHeight(height);
    }

    public void setContentPanelMaxHeight(String maxHeight) {
        contantPanel.getElement().getStyle().clearWidth();
        contantPanel.getElement().getStyle().setProperty("maxHeight", maxHeight);
    }

    public ScrollPanel getContantPanel() {
        return contantPanel;
    }
}
