package com.edatasite.workforce.gwt.core.client.ui.listpanel;

import com.edatasite.workforce.gwt.core.client.ui.treetable.TreeTable;
import com.edatasite.workforce.gwt.core.client.ui.treetable.TreeTableRenderer;
import com.edatasite.workforce.gwt.core.client.ui.treetable.WfmTreeTableColumn;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.HasResizeHandlers;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.gen2.table.client.HasTableDefinition;
import com.google.gwt.gen2.table.client.TableDefinition;
import com.google.gwt.gen2.table.client.TableModel;
import com.google.gwt.gen2.table.client.TableModelHelper;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 7/28/12
 * Time: 8:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class PagingTreeTable<T> extends HTMLPanel implements HasTableDefinition<T>, HasResizeHandlers {
    private static int numId = 0;
    public TreeTable treeTable;
    private ScrollPanel contantPanel;
    private HTMLPanel table;
    private WfmTreeTableHeader header;
    private TableModel<T> tableModel;
    private int pageSize = 0;
    private int pageCount = 0;
    private int currentPage = -1;
    public WfmTreeTableColumn[] columns;
    private Command pageChangeListener, pageCountChangeListener, pageLoadListener, pagingFailureListener, sortingListener;
    private boolean isPageLoading;
    private TableModelHelper.Request lastRequest = null;
    private SimplePanel emptyTableWidgetWrapper;
    private TreeListingCallBack pagingCallBack;
    private Map<Integer, String> headerWithCode;
    private String sortField;
    private boolean ascending = true;

    public PagingTreeTable(TableModel tableModel, WfmTreeTableColumn[] columns) {
        super("");
        this.columns = columns;
        this.tableModel = tableModel;
        initialization();
    }

    private void initialization() {
        initMap();
        treeTable = new TreeTable();
        treeTable.setWidth("100%");
        treeTable.setCellPadding(0);
        treeTable.setCellSpacing(0);
        treeTable.setRenderer(new TreeRender());
        treeTable.getElement().getStyle().setProperty("tableLayout", "fixed");
        treeTable.addStyleName("file--PagingTreeTable");

        contantPanel = new ScrollPanel();
        contantPanel.getElement().getStyle().setPosition(Style.Position.ABSOLUTE);
        contantPanel.getElement().getStyle().setLeft(0, Style.Unit.EM);
        contantPanel.getElement().getStyle().setTop(24, Style.Unit.PX);
        contantPanel.getElement().getStyle().setRight(0, Style.Unit.EM);
        contantPanel.getElement().getStyle().setBottom(0, Style.Unit.EM);
        contantPanel.setWidth("100%");
        contantPanel.add(treeTable);

        header = new WfmTreeTableHeader();
        header.getElement().getStyle().setOverflow(Style.Overflow.HIDDEN);

        contantPanel.addScrollHandler(scrollEvent -> header.getElement().setScrollLeft(scrollEvent.getRelativeElement().getScrollLeft()));

        String tableId = "wfm-treetable-content" + numId;
        table = new HTMLPanel("");
        table.getElement().getStyle().setPosition(Style.Position.RELATIVE);
        table.getElement().setAttribute("id", tableId);
        table.getElement().getStyle().setWidth(100, Style.Unit.PCT);
        table.getElement().getStyle().setHeight(100, Style.Unit.PCT);
        table.setStyleName("wfm-treeTable");
        table.add(header, tableId);

        // Setup the empty table widget wrapper
        emptyTableWidgetWrapper = new SimplePanel();
        emptyTableWidgetWrapper.getElement().getStyle().setProperty("width", "100%");
        emptyTableWidgetWrapper.getElement().getStyle().setProperty("overflow",
                "hidden");
        emptyTableWidgetWrapper.getElement().getStyle().setPropertyPx("border", 0);
        emptyTableWidgetWrapper.getElement().getStyle().setPropertyPx("margin", 0);
        emptyTableWidgetWrapper.getElement().getStyle().setPropertyPx("padding", 0);
        table.add(emptyTableWidgetWrapper);
        setEmptyTableWidgetVisible(false);

        table.add(contantPanel, tableId);
        pagingCallBack = new TreeListingCallBack();

        add(table);
    }

    private void initMap() {
        headerWithCode = new HashMap<>();

        for (int i = 0; i < columns.length; i++) {
            headerWithCode.put(i, columns[i].getColumnCode());
        }
    }

    @Override
    public TableDefinition<T> getTableDefinition() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void gotoFirstPage() {
        gotoPage(0, false);
    }

    public void gotoLastPage() {
        if (getPageCount() >= 0) {
            gotoPage(getPageCount(), false);
        }
    }

    public void gotoNextPage() {
        gotoPage(currentPage + 1, false);
    }

    public void gotoPreviousPage() {
        gotoPage(currentPage - 1, false);
    }

    public void gotoPage(int page, boolean forced) {
        int oldPage = currentPage;
        int numPages = getPageCount();
        if (numPages >= 0) {
            currentPage = Math.max(0, Math.min(page, numPages - 1));
        } else {
            currentPage = page;
        }

        if (currentPage != oldPage || forced) {
            isPageLoading = true;

            pageChangeListener.execute();

            // Request the new data from the table model
            int firstRow = getAbsoluteFirstRowIndex();
            int lastRow = pageSize == 0 ? tableModel.getRowCount() : pageSize;
            lastRequest = new TableModelHelper.Request(firstRow, lastRow);
            tableModel.requestRows(lastRequest, pagingCallBack.getCallback());
        }
    }

    public int getPageCount() {
        if (pageSize < 1) {
            return 1;
        } else {
            int numDataRows = tableModel.getRowCount();
            return (int) Math.ceil(numDataRows / (pageSize + 0.0));
        }
    }

    public void setEmptyTableWidget(Widget emptyTable) {
        emptyTableWidgetWrapper.setWidget(emptyTable);
    }

    /**
     * @return the absolute index of the first visible row
     */
    public int getAbsoluteFirstRowIndex() {
        return currentPage * pageSize;
    }

    /**
     * Set whether or not the empty table widget is visible.
     *
     * @param visible true to show the empty table widget
     */
    protected void setEmptyTableWidgetVisible(boolean visible) {
        emptyTableWidgetWrapper.setVisible(visible);
//        if (header != null)
//            header.setVisible(!visible);
    }

    public void changePageCount() {
        if (pageCountChangeListener != null) {
            pageCountChangeListener.execute();
        }
    }

    public void onPageLoad() {
        if (pageLoadListener != null) {
            pageLoadListener.execute();
        }
    }

    public void onPagingFailure() {
        if (pagingFailureListener != null) {
            pagingFailureListener.execute();
        }
    }

    /**
     * Reload the current page.
     */
    public void reloadPage() {
        if (currentPage >= 0) {
            gotoPage(currentPage, true);
        } else {
            gotoPage(0, true);
        }
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public TreeTable getTreeTable() {
        return treeTable;
    }

    public TableModel<T> getTableModel() {
        return tableModel;
    }

    public void setPageChangeListener(Command pageChangeListener) {
        this.pageChangeListener = pageChangeListener;
    }

    public void setPageCountChangeListener(Command pageCountChangeListener) {
        this.pageCountChangeListener = pageCountChangeListener;
    }

    public void setPageLoadListener(Command pageLoadListener) {
        this.pageLoadListener = pageLoadListener;
    }

    public void setPagingFailureListener(Command pagingFailureListener) {
        this.pagingFailureListener = pagingFailureListener;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    @Override
    public HandlerRegistration addResizeHandler(ResizeHandler resizeHandler) {
        Window.alert("Resize");
        return null;
    }

    public void setSortingListener(Command sortingListener) {
        this.sortingListener = sortingListener;
    }

    public String getSortField() {
        return sortField;
    }

    public boolean isAscending() {
        return ascending;
    }

    /**
     * Tree Table header
     */
    protected class WfmTreeTableHeader extends HTMLPanel {

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
            header.setCellSpacing(0);
            header.setCellPadding(0);
            header.getElement().getStyle().setPosition(Style.Position.RELATIVE);
            header.getElement().getStyle().setProperty("tableLayout", "fixed");
            header.getElement().getStyle().setProperty("width", 100, Style.Unit.PCT);
            HTML image;
            for (int i = 0; i < columns.length; i++) {
                String lastColStyle = "";
                if (i == columns.length-1) {
                    lastColStyle = "style='padding-right: 22px'";
                }
                header.setHTML(0, i, "<div class='wfm-treetable-inner-col' style='width:" + columns[i].getWidth() + "px'><div class='wfm-treetable-inner-col-pad'" + lastColStyle + ">" + columns[i].getColumnName() + "</div></div>");
                header.getFlexCellFormatter().setAlignment(0, i, columns[i].getAlignment(), VerticalPanel.ALIGN_MIDDLE);
                header.getFlexCellFormatter().setStyleName(0, i, "wfm-treeTable-header");
                header.getFlexCellFormatter().getElement(0, i).getStyle().setWidth(columns[i].getWidth(), Style.Unit.PX);
            }
            this.add(header, id);
            listener();
        }

        /**
         * listener for sorting
         */
        private void listener() {
            final HTML image = new HTML("");

            header.addClickHandler(clickEvent -> {

                HTMLTable.Cell cell = header.getCellForEvent(clickEvent);

                if (cell != null) {
                    int cellIndex = cell.getCellIndex();
                    if (!"".equals(headerWithCode.get(cellIndex))) {
                        if (headerWithCode.get(cellIndex).equals(sortField)) {
                            ascending = !ascending;
                        } else {
                            sortField = headerWithCode.get(cellIndex);
                            ascending = true;
                        }
                        if (ascending) {
                            image.setStyleName("wfm-treeTable-asc");
                        } else {
                            image.setStyleName("wfm-treeTable-desc");
                        }
                        sortingListener.execute();
                    }
                }
                if (cell != null && !"".equals(headerWithCode.get(cell.getCellIndex()))) {
                    if (!DOM.isOrHasChild((Element) cell.getElement().getChild(0), image.getElement())) {
                        DOM.appendChild((Element) cell.getElement().getChild(0), image.getElement());
                    }
                }
            });
        }

    }

    class TreeRender implements TreeTableRenderer {

        @Override
        public void renderTreeItem(TreeTable table, com.edatasite.workforce.gwt.core.client.ui.treetable.TreeItem item, int row) {
            Object o = item.getUserObject();
            if (o != null) {
                if (columns[0].getTreeTableCellWidget().getTreeTableCell(o) instanceof Widget) {
                    item.setWidget((Widget) columns[0].getTreeTableCellWidget().getTreeTableCell(o));
                } else {
                    item.setHTML(columns[0].getTreeTableCellWidget().getTreeTableCell(o).toString());
                }
                table.getFlexCellFormatter().setStyleName(row, 0, "wfm-treeTable-column");
                table.getFlexCellFormatter().setAlignment(row, 0, columns[0].getAlignment(), VerticalPanel.ALIGN_MIDDLE);
                table.getFlexCellFormatter().getElement(row, 0).getStyle().setWidth(columns[0].getWidth(), Style.Unit.PX);

                for (int col = 1; col < columns.length; col++) {
                    if (columns[col].getTreeTableCellWidget().getTreeTableCell(o) instanceof Widget) {
                        SimplePanel div = new SimplePanel();
                        div.setWidth(columns[col].getWidth() + "px");
                        div.setStyleName("wfm-treetable-inner-col");
                        SimplePanel div1 = new SimplePanel();
                        div1.setStyleName("wfm-treetable-inner-col-pad");
                        div1.setWidget((Widget) columns[col].getTreeTableCellWidget().getTreeTableCell(o));
                        div.setWidget(div1);
                        table.setWidget(row, col, div);
                    } else if (columns[col].getTreeTableCellWidget().getTreeTableCell(o) != null) {
                        table.setHTML(row, col, "<div class='wfm-treetable-inner-col' style='width:" + columns[col].getWidth() + "px'><div class='wfm-treetable-inner-col-pad'>" + columns[col].getTreeTableCellWidget().getTreeTableCell(o).toString() + "</div></div>");
                    } else {
                        table.setHTML(row, col, "<div class='wfm-treetable-inner-col' style='width:" + columns[col].getWidth() + "px'><div class='wfm-treetable-inner-col-pad'>" + "&nbsp;" + "</div></div>");
                    }

                    table.getFlexCellFormatter().setStyleName(row, col, "wfm-treeTable-column");
                    table.getFlexCellFormatter().setAlignment(row, col, columns[col].getAlignment(), VerticalPanel.ALIGN_MIDDLE);
                    table.getFlexCellFormatter().getElement(row, col).getStyle().setWidth(columns[col].getWidth(), Style.Unit.PX);
                }
                if (row % 2 == 0) {
                    table.getRowFormatter().setStyleName(row, "wfm-treeTable-evenrow");
                } else {
                    table.getRowFormatter().setStyleName(row, "wfm-treeTable-oddrow");
                }
            }
        }
    }
}
