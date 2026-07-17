package com.edatasite.workforce.gwt.project.client.ui.view.ganttchart;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialSwitch;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Abror Abdukadirov
 * Date: 06.05.2019 15:15
 */
public class ProjectGanttChartCustomizeSideNav extends KpiSideNavBox {

    private static final ProjectStrings projectStrings = ProjectStrings.App.get();

    private VerticalPanel columnsVerticalPanel;
    private PickupDragController draggableController;

    public ProjectGanttChartCustomizeSideNav() {
        super();
        this.initialize();
    }

    private void initialize() {
        Heading header = new Heading(HeadingSize.H1);
        header.setText(wfmStrings.availableColumns());

        this.initDraggablePanel();

        WfmButton2 saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveButton.addClickHandler(event -> {
            hide();
            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_GANTT_CHART_COLUMN_SETTINGS_CHANGE, null, ProjectGanttChartCustomizeSideNav.this);
        });
        addHeader(header);
        addFooter(saveButton);
    }

    private void initDraggablePanel() {
        AbsolutePanel draggableAbsolutePanel = new AbsolutePanel();

        columnsVerticalPanel = new VerticalPanel();
        draggableAbsolutePanel.add(columnsVerticalPanel);

        draggableController = new PickupDragController(draggableAbsolutePanel, false);
        draggableController.setBehaviorMultipleSelection(false);

        VerticalPanelDropController columnDropController = new VerticalPanelDropController(columnsVerticalPanel);
        draggableController.registerDropController(columnDropController);

        List<SelectItem> columns = getColumns();
        for (SelectItem column : columns) {
            this.drawColumns(column);
        }
        addBody(draggableAbsolutePanel);
    }

    private void drawColumns(SelectItem item) {
        String className = "drag-tile" + (item.isSelected() ? " state-on" : " state-off");
        MaterialPanel dragTile = new MaterialPanel(className);

        MaterialSwitch switcher = new MaterialSwitch();
        switcher.setLayoutData(item);
        switcher.setValue(item.isSelected());
        switcher.addValueChangeHandler(event -> {
            if (switcher.getValue()) {
                dragTile.removeStyleName("state-off");
                dragTile.addStyleName("state-on");
            } else {
                dragTile.removeStyleName("state-on");
                dragTile.addStyleName("state-off");
            }
        });
        MaterialPanel pnlGrip = new MaterialPanel("drag-tile__grip");

        Span columnTitle = new Span(item.getName());
        columnTitle.setStyleName("drag-tile__text");

        Div actionsWrapper = new Div("drag-tile__actions");
        actionsWrapper.add(switcher);

        dragTile.add(pnlGrip);
        dragTile.add(columnTitle);
        dragTile.add(actionsWrapper);
        dragTile.setLayoutData(switcher);

        Div dragRow = new Div();
        dragRow.add(dragTile);

        columnsVerticalPanel.add(dragRow);
        draggableController.makeDraggable(dragRow, pnlGrip);
    }

    public List<SelectItem> getActiveColumns() {
        List<SelectItem> result = new ArrayList<>();
        for (int i = 0; i < columnsVerticalPanel.getWidgetCount(); i++) {
            Div dragRow = (Div) columnsVerticalPanel.getWidget(i);
            MaterialPanel dragTile = (MaterialPanel) dragRow.getWidget(0);
            MaterialSwitch switcher = (MaterialSwitch) dragTile.getLayoutData();
            if (!switcher.getValue()) {
                continue;
            }
            result.add((SelectItem) switcher.getLayoutData());
        }
        return result;
    }

    private List<SelectItem> getColumns() {
        List<SelectItem> columns = new ArrayList<>();
        columns.add(new SelectItem(1, wfmStrings.startDate(), TaskListItem.START_DATE));
        columns.add(new SelectItem(2, wfmStrings.dueDate(), TaskListItem.END_DATE));
        columns.add(new SelectItem(3, wfmStrings.actualStartDate(), TaskListItem.ACTUAL_START_DATE));
        columns.add(new SelectItem(4, projectStrings.completedDateActual(), TaskListItem.ACTUAL_END_DATE));
        columns.add(new SelectItem(5, wfmStrings.estimatedTime(), TaskListItem.ESTIMATED));
        columns.add(new SelectItem(6, wfmStrings.timeSpentOnly(), TaskListItem.HOUR_SPENT));
        columns.add(new SelectItem(7, wfmStrings.actualTimeSpent(), TaskListItem.ACTUAL_HOURS_SPENT));
        columns.add(new SelectItem(8, wfmStrings.overAllStatus(), TaskListItem.OVERALL_STATUS_NAME));
        return columns;
    }
}
