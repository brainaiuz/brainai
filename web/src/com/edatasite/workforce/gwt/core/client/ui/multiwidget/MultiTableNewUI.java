package com.edatasite.workforce.gwt.core.client.ui.multiwidget;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBox;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: Hayot
 * Date: 16-Mar-2018
 * Time: 20:28:24
 */
public class MultiTableNewUI extends FlowPanel {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    public static final String LIST_BOX = "LIST_BOX";
    public static final String LOOK_UP_BOX = "LOOK_UP_BOX";
    public static final String TEXT_BOX = "TEXT_BOX";
    public static final String CHECK_BOX = "CHECK_BOX";
    public static final String DATE_PICKER = "DATE_PICKER";
    public static final String PHONE_NUMBER = "PHONE_NUMER";
    public static final String RADION_BUTTON = "RADIO_BUTTON";
    public static final String MULTI_TABLE = "MULTI_TABLE";
    public static final String ADDRESS = "ADDRESS";


    private SimpleLink[] extraLinks;
    private final WidgetsMap widgetsMap;
    private static final int sch = 0;
    private boolean canRemoveSingleRow;
    private Command onLinesAdded;
    private Command onLinesRemoved;
    private boolean isViewMode;
    private boolean isBottomButton;
    private String removableMessage;
    private final String multiTable = "multi_table_";
    //    private Map<Integer, WidgetsMap> mapOfRows;
    private final MultiTableWidgets multiTableWidgets;
    private int spacing = 0;
    private Integer rowLimit;
    public Command saveButtonListener;

    public void removeAllRows() {
        clear();
    }

    public class MultiTableRow extends FlowPanel {
        FlowPanel left = new FlowPanel();
        FlowPanel right = new FlowPanel();
        WidgetsMap map;

        public MultiTableRow(WidgetsMap newWidgets) {
            map = newWidgets;
            setStyleName("input-group mb-3");
            left.setStyleName("input-group-prepend");
            right.setStyleName("input-group-append");
            add(left);
//            add(right);
        }

        public WidgetsMap getMap() {
            return map;
        }

        public void addWidget(IsWidget widget) {
            insert(widget, getWidgetCount());
        }

        public void addToLeft(IsWidget widget) {
            if (widget instanceof CheckBox) {
                Div d = new Div("input-group-text");
                d.add(widget);
                widget = d;
            }
            left.add(widget);
        }

        public void addToRight(Widget widget) {
            if (getWidgetIndex(right) < 0) {
                add(right);
            }
            right.add(widget);
        }
    }

    WfmButton2 minusButton = null;

    public MultiTableNewUI(int i, boolean canRemoveSingleRow, MultiTableWidgets multiTableWidgets, SimpleLink... extraLinks) {
        this(multiTableWidgets, null, false, extraLinks);
        this.spacing = i;
        this.canRemoveSingleRow = canRemoveSingleRow;
    }

    public MultiTableNewUI(MultiTableWidgets multiTableWidgets, String title, boolean isViewMode, SimpleLink... extraLinks) {
        this.isViewMode = isViewMode;
        this.widgetsMap = multiTableWidgets.getWidgetsMaps();
        this.multiTableWidgets = multiTableWidgets;
        this.extraLinks = extraLinks;
        initTitle(title);
    }

    public void setViewMode(boolean viewMode) {

        isViewMode = viewMode;
    }

    protected void hideRemoveButtons() {
        //todo we can't do it it write now: STAS!
//        if (flexTable.getRowCount() > 0) {
//            for (int i = 0; i < flexTable.getRowCount(); i++) {
//                Widget widget = flexTable.getWidget(0, widgetsMap.getWidgets().length);
//                if (widget != null) {
//                    widget.setVisible(false);
//                }
//            }
//        }
    }


    public boolean isFilled() {
        return this.multiTableWidgets != null && this.multiTableWidgets.isFilled();
    }

    //    public void setWidth(String width) {
//        wrapper.setWidth(width);
//        flexTable.setWidth(width);
//    }
//
    public MultiTableNewUI(Integer rowLimit, MultiTableWidgets multiTableWidgets, SimpleLink... extraLinks) {
        this(multiTableWidgets, null, false, extraLinks);
        this.rowLimit = rowLimit;
    }

    public MultiTableNewUI(boolean canRemoveSingleRow, Integer rowLimit, MultiTableWidgets multiTableWidgets, SimpleLink... extraLinks) {
        this(multiTableWidgets, null, false, extraLinks);
        this.canRemoveSingleRow = canRemoveSingleRow;
        this.rowLimit = rowLimit;
    }

    public MultiTableNewUI(MultiTableWidgets multiTableWidgets, String str) {
        this.widgetsMap = multiTableWidgets.getWidgetsMaps();
        this.multiTableWidgets = multiTableWidgets;
        cloneWidget();
    }

    public MultiTableNewUI(MultiTableWidgets multiTableWidgets, SimpleLink... extraLinks) {
        this(multiTableWidgets, null, false, extraLinks);
    }

    public MultiTableNewUI(MultiTableWidgets multiTableWidgets, boolean isViewMode, SimpleLink... extraLinks) {
        this(multiTableWidgets, null, isViewMode, extraLinks);
    }

    public MultiTableNewUI(boolean canRemoveSingleRow, MultiTableWidgets multiTableWidgets, SimpleLink... extraLinks) {
        this(multiTableWidgets, null, false, extraLinks);
        this.canRemoveSingleRow = canRemoveSingleRow;
    }

    public MultiTableNewUI(boolean canRemoveSingleRow, boolean isBottomButton, MultiTableWidgets multiTableWidgets, SimpleLink... extraLinks) {
        this(multiTableWidgets, null, false, extraLinks);
        this.canRemoveSingleRow = canRemoveSingleRow;
        this.isBottomButton = isBottomButton;
    }

    public MultiTableNewUI(boolean canRemoveSingleRow, boolean isBottomButton, String removableMessage, MultiTableWidgets multiTableWidgets, SimpleLink... extraLinks) {
        this(multiTableWidgets, null, false, extraLinks);
        this.canRemoveSingleRow = canRemoveSingleRow;
        this.isBottomButton = isBottomButton;
        this.removableMessage = removableMessage;
    }

    /**
     * Set Table Title
     *
     * @param title
     */
    protected void initTitle(String title) {
        cloneWidgetsRow(getWidgetCount());
    }

    protected void cloneWidget() {
        cloneWidgetsRowColumn(getWidgetCount());
    }

    public void onAddLinkClicked(int index) {
        if (isViewMode) {
            return;
        }
        cloneWidgetsRow(index);
        if (getOnLinesAdded() != null) {
            getOnLinesAdded().execute();
        }
    }

    public void onAddLinkClickedColumn(int index) {
        if (isViewMode) {
            return;
        }
        cloneWidgetsRowColumn(index);
        if (getOnLinesAdded() != null) {
            getOnLinesAdded().execute();
        }
    }

    /**
     * @param newWidgets
     */
    public void initWidgets(final WidgetsMap newWidgets) {
        clear();
        addWidgets(newWidgets, -1);
    }

    public void addWidgets(final WidgetsMap newWidgets) {
        addWidgets(newWidgets, -1);
    }

    public void addColumnsWidget(final WidgetsMap newWidgets) {
        addColumnsWidget(newWidgets, -1);
    }

    public void addWidgets(final WidgetsMap newWidgets, int preferredIndex) {
        final MultiTableRow tableRow = new MultiTableRow(newWidgets);
        if (newWidgets == null) {
            return;
        }
        Widget[] widgets = newWidgets.getWidgets();
        if (widgets.length > 0) {
            int i = 0;
            for (Widget widget : widgets) {
                if (newWidgets.isLeftWidget(widget)) {
                    tableRow.addToLeft(widget);
                } else if (newWidgets.isRightWidget(widget)) {
                    tableRow.addToRight(widget);
                } else {
                    tableRow.addWidget(widget);
                }
                i++;
            }

        }
        if (tableRow.left != null && tableRow.left.getWidgetCount() == 0) {
            tableRow.left.removeFromParent();
        }
        addMinusButton(tableRow);
        addPlusButton(tableRow);
        addAdditionalButton(tableRow);
        if (preferredIndex >= 0) {
            insert(tableRow, preferredIndex);
        } else {
            add(tableRow);
        }
    }

    public void addColumnsWidget(final WidgetsMap newWidgets, int preferredIndex) {
        final MultiAddressRow tableRow = new MultiAddressRow(newWidgets);
        if (newWidgets == null) {
            return;
        }
        Widget[] widgets = newWidgets.getWidgets();
        HashMap<String, Widget> map = newWidgets.getWidgetsMap();
        AtomicInteger count = new AtomicInteger();
        map.forEach((key, value) -> {
            if (count.get() < 4 && count.get() > 0) {
                tableRow.addColumn1(key, value);
            } else if (count.get() < 7 && count.get() > 0) {
                tableRow.addColumn2(key, value);
            } else if (count.get() > 0) {
                tableRow.addColumn3(key, value);
            }
            count.getAndIncrement();
        });
        addMinusButton(tableRow);
        addPlusButton(tableRow);
        if (preferredIndex >= 0) {
            insert(tableRow, preferredIndex);
        } else {
            add(tableRow);
        }
    }

    protected void addMinusButton(final MultiTableRow tableRow) {
        if (isViewMode) {
            return;
        }
        WfmButton2 button = new WfmButton2("", WfmButton2.BTN_WHITE);
        button.addStyleName("btn--icon");
        button.add(new SvgIcon(SvgEnum.minus));
        button.addClickHandler(event -> removeFromTableRow(tableRow));
        new KpiToolTip(button, wfmStrings.delete());
        tableRow.addToRight(button);
    }

    protected void addMinusButton(final MultiAddressRow multiAddress) {
        if (isViewMode) {
            return;
        }
        minusButton = new WfmButton2("", WfmButton2.BTN_WHITE);
        minusButton.addStyleName("btn--icon");
        minusButton.add(new SvgIcon(SvgEnum.minus));
        minusButton.addClickHandler(event -> removeFromTableRow(multiAddress));
        new KpiToolTip(minusButton, wfmStrings.delete());
    }

    protected void addPlusButton(final MultiAddressRow tableRow) {
        if (isViewMode) {
            return;
        }
        WfmButton2 button = new WfmButton2("", WfmButton2.BTN_WHITE);
        button.addStyleName("btn--icon");
        button.add(new SvgIcon(SvgEnum.plus));
        button.addClickHandler(event -> onAddLinkClickedColumn(getWidgetIndex(tableRow) + 1));
        new KpiToolTip(button, wfmStrings.add());
        GRow row = new GRow(new GColumn(GColumnEnum.COL_1, new FormGroup(button)));
        if (size() > 0) {
            row.add(new GColumn(GColumnEnum.COL_1, new FormGroup(minusButton)));
        }
        tableRow.addColumn3(null, row);
    }

    protected void addPlusButton(final MultiTableRow tableRow) {
        if (isViewMode) {
            return;
        }
        WfmButton2 button = new WfmButton2("", WfmButton2.BTN_WHITE);
        button.addStyleName("btn--icon");
        button.add(new SvgIcon(SvgEnum.plus));
        button.addClickHandler(event -> onAddLinkClicked(getWidgetIndex(tableRow) + 1));
        new KpiToolTip(button, wfmStrings.add());
        tableRow.addToRight(button);
    }

    protected void addAdditionalButton(final MultiTableRow tableRow) {
    }

    protected void removeFromTableRow(MultiAddressRow tableRow) {
        if (isViewMode) {
            return;
        }
        tableRow.removeFromParent();
        if (onLinesRemoved != null) {
            onLinesRemoved.execute();
        }
        if (getWidgetCount() == 0) {
            onAddLinkClickedColumn(-1);
        }
    }

    protected void removeFromTableRow(MultiTableRow tableRow) {
        if (isViewMode) {
            return;
        }
        tableRow.removeFromParent();
        if (onLinesRemoved != null) {
            onLinesRemoved.execute();
        }
        if (getWidgetCount() == 0) {
            onAddLinkClicked(-1);
        }
    }

    public LinkedList<HashMap<String, Widget>> getColumnsWidget() {
        LinkedList<HashMap<String, Widget>> rows = new LinkedList<>();
        for (int i = 0; i < getWidgetCount(); i++) {
            if (getWidget(i) != null && getWidget(i) instanceof MultiAddressRow) {
                rows.add(((MultiAddressRow) getWidget(i)).getMap().getWidgetsMap());
            }
        }
        return rows;
    }

    /**
     * Show hide add link option by row limit
     */
    protected void showHideAddLinkByRowLimit() {
        //todo we can't do it right now
//        if (rowLimit == null || rowLimit - 1 >= row) {
//            add.setVisible(true);
//        } else {
//            add.setVisible(false);
//        }
    }

    /**
     * Get Widgets
     *
     * @return
     */
    public LinkedList<HashMap<String, Widget>> getWidgets() {
        LinkedList<HashMap<String, Widget>> rows = new LinkedList<>();
        for (int i = 0; i < getWidgetCount(); i++) {
            if (getWidget(i) != null && getWidget(i) instanceof MultiTableRow) {
                rows.add(((MultiTableRow) getWidget(i)).getMap().getWidgetsMap());
            }
        }
        return rows;
    }

    public void cloneWidgetsRowColumn(int index) {
        WidgetsMap cloneWidgets = multiTableWidgets.getWidgetsMaps();
        addColumnsWidget(cloneWidgets, index);
    }

    /**
     * Get WidgetsMap
     *
     * @return
     */
    public List<WidgetsMap> getWidgetsMaps() {
        List<WidgetsMap> rows = new ArrayList<>();

        for (int i = 0; i < getWidgetCount(); i++) {
            if (getWidget(i) != null && getWidget(i) instanceof MultiTableRow) {
                rows.add(((MultiTableRow) getWidget(i)).getMap());
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
        return getWidgetCount();
    }

    /**
     * Return true if not empty
     *
     * @return
     */
    public boolean isEmpty() {
        return getWidgetCount() == 0;
    }

    /**
     * Clone Widgets
     */
    public void cloneWidgetsRow(int index) {
        WidgetsMap cloneWidgets = multiTableWidgets.getWidgetsMaps();
        addWidgets(cloneWidgets, index);
    }

    public class MultiAddressRow extends FlowPanel {
        GColumn column1 = new GColumn(GColumnEnum.COL_4);
        GColumn column2 = new GColumn(GColumnEnum.COL_4);
        GColumn column3 = new GColumn(GColumnEnum.COL_4);
        WidgetsMap map;
        GBox box = new GBox();
        GRow row = new GRow();

        public MultiAddressRow(WidgetsMap newWidget) {
            map = newWidget;
            row.addAll(column1, column2, column3);
            box.add(row);
            add(box);
        }

        public WidgetsMap getMap() {
            return map;
        }

        public void addColumn1(String nameOfWidget, Widget widget) {
            column1.add(new FormGroup(nameOfWidget, widget));
        }

        public void addColumn2(String nameOfWidget, Widget widget) {
            column2.add(new FormGroup(nameOfWidget, widget));
        }

        public void addColumn3(String nameOfWidget, Widget widget) {
            if (nameOfWidget == null) {
                column3.add(widget);
            } else {
                column3.add(new FormGroup(nameOfWidget, widget));
            }
        }
    }

    public Command getOnLinesAdded() {
        return onLinesAdded;
    }

    public void setOnLinesAdded(Command onLinesAdded) {
        this.onLinesAdded = onLinesAdded;
    }

    public void setOnLinesRemoved(Command onLinesRemoved) {
        this.onLinesRemoved = onLinesRemoved;
    }

    public void setSaveButtonListener(Command saveButtonListener) {
        this.saveButtonListener = saveButtonListener;
    }
}
