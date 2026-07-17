package com.edatasite.workforce.gwt.task.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.PositionsSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.cell.CheckBoxCell;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.view.client.ProvidesKey;

import java.util.LinkedHashSet;

/**
 * User: Sherali
 * Date: Apr 15, 2009
 * Time: 3:51:50 PM
 */
public class TaskAssigneesWidget extends Composite {
    private final LinkedHashSet<PositionsSelectItem> selectItems = new LinkedHashSet<>();
    private KpiDataGrid<PositionsSelectItem> initialGrid;
    private KpiDataGrid<PositionsSelectItem> resultGrid;

    public TaskAssigneesWidget() {
        init();
    }

    public static final ProvidesKey<PositionsSelectItem> KEY_PROVIDER = item -> item == null ? null : item.getId() != null ? item.getId() : item.getName();

    private KpiModal assignDialogBox;
    public boolean checkedAll = false;


    public static boolean messegeShowed = true;
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public void init() {
        initialGrid = new KpiDataGrid<>(KEY_PROVIDER);
        initialGrid.setWidth("100%");
        initialGrid.addStyleName("cellBasedWidget-mod");
        initialGrid.getElement().getStyle().setOverflow(Style.Overflow.VISIBLE);
        renderInitialGrid();

        resultGrid = new KpiDataGrid<>(KEY_PROVIDER);
        resultGrid.removeStyleName("cellBasedWidget-mod--static-body");
        renderResultGrid();
        resultGrid.addDomHandler(event -> {
            for (int k = 0; k < initialGrid.getList().size(); k++) {
                initialGrid.getList().get(k).setSelected(false);
            }
            selectItems.addAll(resultGrid.getList());
            for (int k = 0; k < initialGrid.getList().size(); k++) {
                for (PositionsSelectItem i : resultGrid.getList()) {
                    if (i.getId().equals(initialGrid.getList().get(k).getId())) {
                        initialGrid.getList().get(k).setSelected(true);
                        break;
                    }
                }
            }
            initialGrid.refresh();
            assignDialogBox.open();
        }, ClickEvent.getType());

        final HorizontalPanel reHp = new HorizontalPanel();
        reHp.add(resultGrid);
        initWidget(reHp);

        assignDialogBox = new KpiModal();


        assignDialogBox.setWidth(550);
        assignDialogBox.setTitle(wfmStrings.assignees());
        WfmButton2 saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, event -> {
            if (initResult()) {
                assignDialogBox.close();
            } else {
                messegeShowed = true;
            }
        });
        saveButton.ensureDebugId("Task_assignee_save_button");

        WfmButton2 cancelButton = new WfmButton2(wfmStrings.cancel());
        cancelButton.ensureDebugId("Task_assignee_cancel_button");
        cancelButton.addClickHandler(clickEvent -> {
            assignDialogBox.close();
        });

        assignDialogBox.addButton(cancelButton);
        assignDialogBox.addButton(saveButton);

        assignDialogBox.add(initialGrid);
//        assignDialogBox.setScrollable(true);

    }

    private void renderInitialGrid() {
        Header<Boolean> header = new Header<Boolean>(new CheckBoxCell(true, true)) {
            @Override
            public Boolean getValue() {
                return checkedAll;
            }
        };
        header.setUpdater(value -> {
            checkedAll = !checkedAll;
            for (int i = 0; i < initialGrid.getList().size(); i++) {
                initialGrid.getList().get(i).setSelected(checkedAll);
            }
            initialGrid.refresh();
        });
        Column<PositionsSelectItem, Boolean> checkboxCell = new Column<PositionsSelectItem, Boolean>(new CheckBoxCell(true, true)) {

            @Override
            public Boolean getValue(final PositionsSelectItem object) {
/*
                if (object.isSelected()) {
                    selectItems.add(object);
                } else {
                    selectItems.remove(object);
                }
*/
                return object.isSelected();
            }
        };
        checkboxCell.setFieldUpdater((index, object, value) -> object.setSelected(value));

        initialGrid.addColumn(checkboxCell, header);
        initialGrid.setColumnWidth(checkboxCell, 13, com.google.gwt.dom.client.Style.Unit.PCT);

        Column<PositionsSelectItem, String> employee = new Column<PositionsSelectItem, String>(new TextCell()) {

            @Override
            public String getValue(final PositionsSelectItem object) {
                return object.getName();
            }
        };
        initialGrid.addColumn(employee, wfmStrings.employee());
        initialGrid.setColumnWidth(employee, 60, com.google.gwt.dom.client.Style.Unit.PCT);

        final TextInputCell textInputCell = new TextInputCell();
        Column<PositionsSelectItem, String> time = new Column<PositionsSelectItem, String>(textInputCell) {

            @Override
            public String getValue(final PositionsSelectItem object) {
                return Utils.formatMinutes(object.getTime());
            }
        };

        time.setFieldUpdater((index, object, value) -> {
            if (!"".equals(value) && !"00:00".equals(value)) {
                object.setTime(Utils.parseMinutes(value));
            }
        });
        initialGrid.addColumn(time, wfmStrings.estimatedTime());
        initialGrid.setColumnWidth(time, 27, com.google.gwt.dom.client.Style.Unit.PCT);
    }

    private void renderResultGrid() {
        Column<PositionsSelectItem, String> employee = new Column<PositionsSelectItem, String>(new TextCell()) {

            @Override
            public String getValue(final PositionsSelectItem object) {
                return object.getName();
            }
        };
        resultGrid.addColumn(employee);
        resultGrid.setColumnWidth(employee, 60, com.google.gwt.dom.client.Style.Unit.PCT);

        Column<PositionsSelectItem, String> time = new Column<PositionsSelectItem, String>(new TextCell()) {

            @Override
            public String getValue(final PositionsSelectItem object) {
                return Utils.formatMinutes(object.getTime());
            }
        };

        /*time.setFieldUpdater(new FieldUpdater<PositionsSelectItem, String>() {

            @Override
            public void update(int index, final PositionsSelectItem object, String value) {
                if (!"".equals(value) && !"00:00".equals(value)) {
                    object.setTime(Utils.parseMinutes(value));
                }
            }

        });*/
        resultGrid.addColumn(time);
        resultGrid.setColumnWidth(time, 30, com.google.gwt.dom.client.Style.Unit.PCT);
    }

    private void updateChecked() {
        for (PositionsSelectItem item : initialGrid.getList()) {
            if (item.isSelected()) {
                selectItems.remove(item);
                selectItems.add(item);
            } else {
                selectItems.remove(item);
            }
        }
    }

    public void setItems(PositionsSelectItem[] items) {
        initialGrid.supplyProvider(items);
        initialGrid.refresh();
    }

    public PositionsSelectItem[] getAllItems() {
        return initialGrid.getList().toArray(new PositionsSelectItem[initialGrid.getList().size()]);
    }

    public void clear() {
        checkedAll = false;
        selectItems.clear();
        initialGrid.redraw();
        resultGrid.supplyProvider(getSelectedItems());
        resultGrid.refresh();
    }

    private boolean initResult() {
        updateChecked();
        resultGrid.supplyProvider(getSelectedItems());
        resultGrid.refresh();
        return true;
    }

    public boolean initResult(PositionsSelectItem[] items) {
        updateChecked();
        resultGrid.supplyProvider(items);
        resultGrid.refresh();
        return true;
    }

    public PositionsSelectItem[] getSelectedItems() {
        return selectItems.toArray(new PositionsSelectItem[selectItems.size()]);
    }

    public LinkedHashSet<PositionsSelectItem> getSelectItemsList() {
        return selectItems;
    }

    public KpiDataGrid<PositionsSelectItem> getResultGrid() {
        return resultGrid;
    }
}
