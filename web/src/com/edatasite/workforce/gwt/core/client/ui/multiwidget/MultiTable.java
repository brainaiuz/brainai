package com.edatasite.workforce.gwt.core.client.ui.multiwidget;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Icon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 04-Jul-2010
 * Time: 20:28:24
 */
public class MultiTable extends Composite {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    public static final String LIST_BOX = "LIST_BOX";
    public static final String LOOK_UP_BOX = "LOOK_UP_BOX";
    public static final String TEXT_BOX = "TEXT_BOX";
    public static final String CHECK_BOX = "CHECK_BOX";
    public static final String DATE_PICKER = "DATE_PICKER";
    public static final String PHONE_NUMBER_COUNTRY = "PHONE_NUMER_COUNTRY";
    public static final String PHONE_NUMBER_PHONE = "PHONE_NUMER_PHONE";
    public static final String PHONE_NUMBER = "PHONE_NUMER";
    public static final String RADION_BUTTON = "RADIO_BUTTON";
    public static final String MULTI_TABLE = "MULTI_TABLE";
    public static final String ADDRESS = "ADDRESS";


    private int row = 0;
    private MaterialLink add;
    private SimpleLink[] extraLinks;
    private WidgetsMap widgetsMap;
    private Map<Integer, WidgetsMap> mapOfRows;
    private MultiTableWidgets multiTableWidgets;
    private FlexTable.FlexCellFormatter formatter;
    private Command onLinesAdded;
    private boolean canRemoveSingleRow;
    private FlowPanel links;
    private FlexTable flexTable;
    private FlowPanel wrapper;
    private boolean isViewMode;
    private boolean isBottomButton;
    private String removableMessage;
    private String multiTable = "multi_table_";
    private static int sch = 0;
    private int spacing = 0;
    private Integer rowLimit;

    public MultiTable(int i, boolean canRemoveSingleRow, MultiTableWidgets multiTableWidgets, SimpleLink... extraLinks) {
        this(multiTableWidgets, null, extraLinks);
        this.spacing = i;
        this.canRemoveSingleRow = canRemoveSingleRow;
    }

    public void setViewMode(boolean viewMode) {
        isViewMode = viewMode;
        if (isViewMode) {
            removeAddButton();
            hideRemoveButtons();
        }
    }

    private void hideRemoveButtons() {
        if (flexTable.getRowCount() > 0) {
            for (int i = 0; i < flexTable.getRowCount(); i++) {
                Widget widget = flexTable.getWidget(0, widgetsMap.getWidgets().length);
                if (widget != null) {
                    widget.setVisible(false);
                }
            }
        }
    }


    public MultiTable(MultiTableWidgets multiTableWidgets, String title, SimpleLink... extraLinks) {
        flexTable = new FlexTable();
//        flexTable.addStyleName(Constants.DEFAULT_WIDTH); //https://prnt.sc/rmohki
        wrapper = new FlowPanel();
        mapOfRows = new HashMap<>();
        this.widgetsMap = multiTableWidgets.getWidgetsMaps();
        this.multiTableWidgets = multiTableWidgets;
        flexTable.setCellSpacing(0);
        flexTable.setCellPadding(0);
        this.formatter = flexTable.getFlexCellFormatter();
        this.extraLinks = extraLinks;
        initTitle(title);
        initWidget(wrapper);
    }

    public boolean isFilled() {
        return this.multiTableWidgets != null && this.multiTableWidgets.isFilled();
    }

    public void setWidth(String width) {
        wrapper.setWidth(width);
        flexTable.setWidth(width);
    }

    public MultiTable(Integer rowLimit, MultiTableWidgets multiTableWidgets, SimpleLink... extraLinks) {
        this(multiTableWidgets, null, extraLinks);
        this.rowLimit = rowLimit;
    }

    public MultiTable(boolean canRemoveSingleRow, Integer rowLimit, MultiTableWidgets multiTableWidgets, SimpleLink... extraLinks) {
        this(multiTableWidgets, null, extraLinks);
        this.canRemoveSingleRow = canRemoveSingleRow;
        this.rowLimit = rowLimit;
    }

    public MultiTable(MultiTableWidgets multiTableWidgets, SimpleLink... extraLinks) {
        this(multiTableWidgets, null, extraLinks);
    }

    public MultiTable(boolean canRemoveSingleRow, MultiTableWidgets multiTableWidgets, SimpleLink... extraLinks) {
        this(multiTableWidgets, null, extraLinks);
        this.canRemoveSingleRow = canRemoveSingleRow;
    }

    public MultiTable(boolean canRemoveSingleRow, boolean isBottomButton, MultiTableWidgets multiTableWidgets, SimpleLink... extraLinks) {
        this(multiTableWidgets, null, extraLinks);
        this.canRemoveSingleRow = canRemoveSingleRow;
        this.isBottomButton = isBottomButton;
    }

    public MultiTable(boolean canRemoveSingleRow, boolean isBottomButton, String removableMessage, MultiTableWidgets multiTableWidgets, SimpleLink... extraLinks) {
        this(multiTableWidgets, null, extraLinks);
        this.canRemoveSingleRow = canRemoveSingleRow;
        this.isBottomButton = isBottomButton;
        this.removableMessage = removableMessage;
    }

    /**
     * Set Table Title
     *
     * @param title
     */
    private void initTitle(String title) {
        links = new FlowPanel();
        add = new MaterialLink();
        add.ensureDebugId(multiTable + "add_link" + (sch++));
        Icon addIcon = new Icon();
        addIcon.setStyleName("ficon--plus");
        add.add(addIcon);
        add.setText(wfmStrings.add());
        links.add(add);
        addExtraLinks(links);
        if (title != null) {
            HTML titleHtml = new HTML("<b>" + title + "</b>");
            titleHtml.setWordWrap(true);
            HorizontalPanel hp = new HorizontalPanel();
            hp.setCellHorizontalAlignment(titleHtml, HasHorizontalAlignment.ALIGN_LEFT);
            hp.add(titleHtml);
            hp.add(new HTML("&nbsp;&nbsp;"));
            wrapper.add(hp);
        }
        wrapper.add(flexTable);
        wrapper.add(links);
        add.addClickHandler(event -> onAddLinkClicked());
        cloneWidgetsRow();
    }

	public void onAddLinkClicked() {
		cloneWidgetsRow();
		showRemoveAnchor();
		if (getOnLinesAdded() != null) {
			getOnLinesAdded().execute();
		}
		showHideAddLinkByRowLimit();
	}

    public void addRows(int count) {
        for (int i = 0; i < count; i++) {
            onAddLinkClicked();
        }
    }
	private void addExtraLinks(final FlowPanel links) {
        if (extraLinks != null && extraLinks.length > 0) {
            HTML space = new HTML("&nbsp;&nbsp;&nbsp;");
            for (SimpleLink extraLink : extraLinks) {
                if (extraLink != null) {
                    extraLink.getElement().getStyle().setColor("blue");
                    links.add(space);
                    links.add(extraLink);
                }
            }
        }
    }

    /**
     * @param newWidgets
     */
    public void addWidgets(final WidgetsMap newWidgets) {
        if (newWidgets == null) {
            return;
        }
        mapOfRows.put(row, newWidgets);
        Widget[] widgets = newWidgets.getWidgets();
        if (widgets.length > 0) {
            int i = 0;
            for (Widget widget : widgets) {
                flexTable.setWidget(row, i, widget);
                formatter.getElement(row, i).getStyle().setPaddingBottom(5, Style.Unit.PX);
                formatter.getElement(row, i).getStyle().setPaddingLeft(5, Style.Unit.PX);
                formatter.setAlignment(row, i, HorizontalPanel.ALIGN_LEFT, VerticalPanel.ALIGN_MIDDLE);
                i++;
            }
        }
        Icon removeRowIcon = new Icon();
        removeRowIcon.setStyleName(WfmButton2.ICON_TRASH + " pointer");
        removeRowIcon.setDataAttribute("customvalue", String.valueOf(row));
        removeRowIcon.getElement().getStyle().setMarginBottom(11, Style.Unit.PX);
//        removeRowIcon.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        removeRowIcon.getElement().getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        flexTable.setWidget(row, widgetsMap.getWidgets().length, removeRowIcon);
        formatter.setWidth(row, widgetsMap.getWidgets().length, "30px");
        formatter.setAlignment(row, widgetsMap.getWidgets().length, HorizontalPanel.ALIGN_CENTER, (isBottomButton ? VerticalPanel.ALIGN_BOTTOM : VerticalPanel.ALIGN_BOTTOM));
        formatter.getElement(row, widgetsMap.getWidgets().length).getStyle().setPaddingTop(5, Style.Unit.PX);
        formatter.getElement(row, widgetsMap.getWidgets().length).getStyle().setPaddingLeft(2, Style.Unit.PX);
        removeRowIcon.addClickHandler(event -> {
            if (removableMessage != null && !"".equals(removableMessage)) {
                final WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, removableMessage, new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        removeRowTable(Integer.valueOf(removeRowIcon.getDataAttribute("customvalue")));
                        if (getOnLinesAdded() != null) {
                            getOnLinesAdded().execute();
                        }
                        showHideAddLinkByRowLimit();
                    }
                });
                wfmMessageBox.setTitle(wfmStrings.confirmation());
                wfmMessageBox.open();
            } else {
                removeRowTable(Integer.valueOf(removeRowIcon.getDataAttribute("customvalue")));
                if (getOnLinesAdded() != null) {
                    getOnLinesAdded().execute();
                }
                showHideAddLinkByRowLimit();
            }
        });
        row++;
        if (isViewMode || (row == 1 && !canRemoveSingleRow)) {
            removeRowIcon.setVisible(false);
        }
    }

    /**
     * Show hide add link option by row limit
     */
    private void showHideAddLinkByRowLimit() {
        if (rowLimit == null || rowLimit - 1 >= row) {
            add.setVisible(true);
        } else {
            add.setVisible(false);
        }
    }

    /**
     * Get Widgets
     *
     * @return
     */
    public ArrayList<HashMap<String, Widget>> getWidgets() {
        ArrayList<HashMap<String, Widget>> rows = new ArrayList<>();
        for (Map.Entry<Integer, WidgetsMap> entry : mapOfRows.entrySet()) {
            if (entry != null && entry.getValue() != null) {
                rows.add(entry.getValue().getWidgetsMap());
            }
        }
        return rows;
    }

    /**
     * Get Widgets
     *
     * @return
     */
    public List<WidgetsMap> getWidgetsMaps() {
        List<WidgetsMap> rows = new ArrayList<>();
        for (Map.Entry<Integer, WidgetsMap> entry : mapOfRows.entrySet()) {
            if (entry != null && entry.getValue() != null) {
                rows.add(entry.getValue());
            }
        }
        return rows;
    }

    /**
     * Return widgets row
     *
     * @return
     */
    public int size() {
        return row;
    }

    /**
     * Return true if not empty
     *
     * @return
     */
    public boolean isEmpty() {
        return size() < 1;
    }

    /**
     * Clone Widgets
     */
    public void cloneWidgetsRow() {
        WidgetsMap cloneWidgets = multiTableWidgets.getWidgetsMaps();
        addWidgets(cloneWidgets);
    }

    /**
     * Remove row
     *
     * @param rowId
     */
    public void removeRowTable(Integer rowId) {
        flexTable.removeRow(rowId);
        removeAndReindexMap(rowId);
        row--;
        for (int i = 0; i < row; i++) {
            Icon removeRowIcon = (Icon) flexTable.getWidget(i, widgetsMap.getWidgets().length);
            removeRowIcon.setDataAttribute("customvalue", String.valueOf(i));
        }
        showRemoveAnchor();
    }

    /**
     * set margin top px to removable icon
     *
     * @param top - top
     */
    public void setRemovableIconMarginTop(int top) {
        for (int i = 0; i < row; i++) {
            flexTable.getWidget(i, widgetsMap.getWidgets().length).getElement().getStyle().setMarginTop(top, Style.Unit.PX);
        }
    }

    public void removeFirstRow() {
        removeRowTable(0);
    }

    private void removeAndReindexMap(Integer rowId) {
        if (rowId == mapOfRows.size()) {
            mapOfRows.remove(rowId);
            mapOfRows.put(rowId, mapOfRows.get(rowId + 1));
        } else if (rowId < mapOfRows.size()) {
            for (int i = rowId; i < mapOfRows.size(); i++) {
                mapOfRows.remove(i);
                if (mapOfRows.containsKey(i + 1)) {
                    mapOfRows.put(i, mapOfRows.get(i + 1));
                }
            }
        }
        if (mapOfRows.size() > 1 && mapOfRows.containsKey(mapOfRows.size())) {
            mapOfRows.remove(mapOfRows.size());
        }
    }

    public void removeAllRows() {
        for (int i = row; i > 0; i--) {
            removeRowTable(i - 1);
        }
    }

    /**
     * Remove link last first row
     */
    private void showRemoveAnchor() {
        if (flexTable.getRowCount() > 0) {
            if(flexTable.isCellPresent(0, widgetsMap.getWidgets().length)){
                Widget widget = flexTable.getWidget(0, widgetsMap.getWidgets().length);
                widget.setVisible(!isViewMode && (canRemoveSingleRow || row > 1));
                if (!isViewMode && (canRemoveSingleRow || row > 1)) {
                    widget.getElement().getStyle().setDisplay(Style.Display.BLOCK);
                }
            }
        }
    }

    public Command getOnLinesAdded() {
        return onLinesAdded;
    }

    public void setOnLinesAdded(Command onLinesAdded) {
        this.onLinesAdded = onLinesAdded;
    }

    public MaterialLink getAdd() {
        return add;
    }

    public WidgetsMap getWidgetMapByRowID(Integer rowID) {
        return mapOfRows.get(rowID);
    }

    public void setLinksOff(boolean hide) {
        links.setVisible(!hide);
    }

    public void setLinksLeftPanel() {
        links.setStyleName("left");
    }

    public FlowPanel getLinksPanel() {
        return links;
    }

    public void setAddLabel(String s) {
        add.setText(s);
    }

    public void removeAddButton() {
        if (add != null) {
            add.removeFromParent();
        }
    }

    public void setSpacing() {
        flexTable.setCellSpacing(spacing);
    }

    public void setSpacing(int spacing) {
        flexTable.setCellSpacing(spacing);
    }

    public FlexTable getFlexTable() {
        return flexTable;
    }
}
