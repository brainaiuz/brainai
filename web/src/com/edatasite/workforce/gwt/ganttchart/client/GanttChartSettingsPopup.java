package com.edatasite.workforce.gwt.ganttchart.client;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandlerAdapter;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.edatasite.workforce.gwt.core.client.localization.WfmConstantsWithLookup;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.WfmContentPanel;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Ilxom Lutfullaev
 * Date: 27-Mar-2014
 * Time: 15:52:31
 */
public class GanttChartSettingsPopup extends KpiModal implements ClickHandler {

    
    private final WfmConstantsWithLookup coreConstantsWithLookUp = WfmConstantsWithLookup.App.get();

    private boolean change = false;
    private WfmButton2 close;
	private FlowPanel content;
    private ColumnSetttings columnSettings;
    private WfmButton2 save;
	private StringBuilder columns;
	private final GanttChart ganttChart;


    public GanttChartSettingsPopup(GanttChart ganttChart) {
		this.ganttChart = ganttChart;
		setTitle(wfmStrings.customiseGanttChartcolumns());

        initialization();
        setWidth(510);
        add(content);
    }

    private void initialization() {
		content = new FlowPanel();
		columnSettings = new ColumnSetttings();
		close = new WfmButton2(wfmStrings.close(), WfmButton2.BTN_DEFAULT);
        close.addClickHandler(event -> close());
        save = new WfmButton2(wfmStrings.save());
        save.addClickHandler(be -> {
            if (change) {
                columns = new StringBuilder();
                save.setEnabled(false);
                close.setEnabled(false);

                if (columnSettings.getShowColumns().getWidgetCount() > 0) {
                    for (int i=0; i< columnSettings.getShowColumns().getWidgetCount(); i++) {
                        Label widget = (Label) columnSettings.getShowColumns().getWidget(i);
                        if (wfmStrings.startDate().equals(widget.getText())) {
                            columns.append(TaskListItem.START_DATE + ",");
                        } else if (wfmStrings.dueDate().equals(widget.getText())) {
                            columns.append(TaskListItem.END_DATE + ",");
                        } /*else if (wfmStrings.percent().equals(widget.getText())) {
                            columns.append(TaskListItem.COMPLETE + ",");
                        }*/ else if (wfmStrings.assignedTo().equals(widget.getText())) {
                            columns.append(TaskListItem.ASSIGNED_TO + ",");
                        } else if (wfmStrings.overAllStatus().equals(widget.getText())) {
                            columns.append(TaskListItem.OVERALL_STATUS_NAME + ",");
                        } else if (wfmStrings.priority().equals(widget.getText())) {
                            columns.append(TaskListItem.PRIORITY_NAME + ",");
                        } else if (wfmStrings.estimatedTime().equals(widget.getText())) {
                            columns.append(TaskListItem.ESTIMATED + ",");
                        } else if (wfmStrings.actualTimeSpent().equals(widget.getText())) {
                            columns.append(TaskListItem.ACTUAL_TIME + ",");
                        } else if (wfmStrings.actualStartDate().equals(widget.getText())) {
                            columns.append(TaskListItem.ACTUAL_START_DATE + ",");
                        } else if (wfmStrings.actualEndDate().equals(widget.getText())) {
                            columns.append(TaskListItem.ACTUAL_END_DATE + ",");
                        } else if (wfmStrings.billable().equals(widget.getText())) {
                            columns.append(TaskListItem.BILLABLE + ",");
                        }
                    }
                    if (columns.length() > 1) {
                        columns = columns.deleteCharAt(columns.lastIndexOf(","));
                    }
                }
                GanttChartService.App.get().saveGanttChartSettings(ganttChart.getProjectID(), columns.toString(), new AbstractAsyncCallback<Void>() {
                    @Override
                    public void failure(Throwable caught) {
                        change = false;
                        save.setEnabled(true);
                        close.setEnabled(true);
                    }

                    @Override
                    public void success(Void result) {
                        save.setEnabled(true);
                        close.setEnabled(true);
                        change = false;
                        close();
//							Info.show("", "GanttChart settings successfully saved.", Info.Type.INFO_AutoHide);
                        ganttChart.setColumnNames(columns.toString());
                        ganttChart.redrawTasks(true);
                    }
                });
            } else {
                close();
            }
        });

        content.add(columnSettings);
        addButton(close);
        addButton(save);
    }

    private ArrayList<SelectItem> getAllColumnNames() {
		ArrayList<SelectItem> columnNames = new ArrayList<>();
        columnNames.add(new SelectItem(0, wfmStrings.startDate(), TaskListItem.START_DATE));
        columnNames.add(new SelectItem(1, wfmStrings.dueDate(), TaskListItem.END_DATE));
//        columnNames.add(new SelectItem(2, wfmStrings.percent(), TaskListItem.COMPLETE));
		columnNames.add(new SelectItem(2, wfmStrings.assignedTo(), TaskListItem.ASSIGNED_TO));
		columnNames.add(new SelectItem(3, wfmStrings.overAllStatus(), TaskListItem.OVERALL_STATUS_NAME));
		columnNames.add(new SelectItem(4, wfmStrings.priority(), TaskListItem.PRIORITY_NAME));
		columnNames.add(new SelectItem(5, wfmStrings.estimatedTime(), TaskListItem.ESTIMATED));
		columnNames.add(new SelectItem(6, wfmStrings.actualTimeSpent(), TaskListItem.ACTUAL_TIME));
		columnNames.add(new SelectItem(7, wfmStrings.actualStartDate(), TaskListItem.ACTUAL_START_DATE));
		columnNames.add(new SelectItem(8, wfmStrings.actualEndDate(), TaskListItem.ACTUAL_END_DATE));
		columnNames.add(new SelectItem(9, wfmStrings.billable(), TaskListItem.BILLABLE));
        return columnNames;
    }

    @Override
    public void onClick(ClickEvent event) {
        close();
    }

    protected class ColumnSetttings extends FlexTable {

        private final Map<Integer, Label> allColumnMap = new HashMap<>();
        private final HashMap<String, Integer> widgetsPos = new HashMap<>();

        private WfmButton2 prev;
        private VerticalPanel showVerticalPanel;
        private PickupDragController showColumnDragController;
        private WfmContentPanel showColumns;
        private WfmContentPanel allColumns;
		private int oldPos;

        public ColumnSetttings() {
            initilazation();
        }

        private void initilazation() {
            showColumns = new WfmContentPanel();
            showColumns.setSize("200px", "200px");
            showColumns.setCaptionLeftHTML(wfmStrings.showColumns());
            allColumns = new WfmContentPanel();
            allColumns.setSize("200px", "200px");
            allColumns.setCaptionLeftHTML(wfmStrings.allColumns());
            prev = new WfmButton2("", WfmButton2.BTN_DEFAULT, "key keyBack");
            setCellPadding(0);
            setCellSpacing(3);

            setWidget(0, 0, showColumns);
            getFlexCellFormatter().setAlignment(0, 0, HorizontalPanel.ALIGN_LEFT, VerticalPanel.ALIGN_TOP);
            setWidget(0, 1, prev);
            getFlexCellFormatter().setAlignment(0, 1, HorizontalPanel.ALIGN_LEFT, VerticalPanel.ALIGN_MIDDLE);
            setWidget(0, 2, allColumns);
            getFlexCellFormatter().setAlignment(0, 2, HorizontalPanel.ALIGN_LEFT, VerticalPanel.ALIGN_TOP);

			initOnlyShowColumn();
			initShowAllColumn();
        }

		public VerticalPanel getShowColumns() {
			return showVerticalPanel;
		}

		/**
         * All Columns
         */
        public void initShowAllColumn() {
            VerticalPanel allColumnPanel = new VerticalPanel();
            allColumns.add(allColumnPanel);
			final ArrayList<SelectItem> allColumnNames = getAllColumnNames();
			for (int i = 0; i < allColumnNames.size(); i++) {
				final int index = i;
				final KpiCheckBox check = new KpiCheckBox();
                check.addClickHandler(event -> {
                    if (check.getValue()) {
                        addNewColumn(allColumnNames.get(index));
                    } else {
                        Label label = allColumnMap.get(index);
                        showVerticalPanel.remove(label);
                    }
                    change = true;
                });
				boolean enabled = ganttChart.getColumnNames().contains(allColumnNames.get(index).getDescription());
				check.setValue(enabled);
				if (enabled) {
					addNewColumn(allColumnNames.get(index));
				}

				Label label = getColumnWidget(allColumnNames.get(i).getName());

                HorizontalPanel panel = new HorizontalPanel();
                panel.setSpacing(3);
                panel.add(check);
                panel.setCellVerticalAlignment(check, VerticalPanel.ALIGN_MIDDLE);
                panel.add(label);
                panel.setCellVerticalAlignment(label, VerticalPanel.ALIGN_MIDDLE);

                allColumnPanel.add(panel);

            }
        }

        /**
         * IsShow=true Shows Columns
         */
        public void initOnlyShowColumn() {
            AbsolutePanel boundaryPanel = new AbsolutePanel();
            boundaryPanel.getElement().getStyle().setOverflow(Style.Overflow.VISIBLE);
            boundaryPanel.setSize("100%", "100%");
//            boundaryPanel.getElement().getStyle().setOverflow(Style.Overflow.AUTO);

            // initialize vertical panel to hold our columns
            showVerticalPanel = new VerticalPanel();
            showVerticalPanel.setWidth("100%");
            showVerticalPanel.setSpacing(0);

            boundaryPanel.add(showVerticalPanel);
            showColumns.add(boundaryPanel);

            // initialize our column drag controller
            showColumnDragController = new PickupDragController(boundaryPanel, false);
            showColumnDragController.setBehaviorMultipleSelection(false);

            // initialize our column drop controller
            VerticalPanelDropController columnDropController = new VerticalPanelDropController(showVerticalPanel);
            showColumnDragController.registerDropController(columnDropController);
            showColumnDragController.addDragHandler(new DragHandlerAdapter() {
				@Override
				public void onDragStart(DragStartEvent event) {
					Label label = (Label) event.getSource();
					oldPos = showVerticalPanel.getWidgetIndex(label);
				}

				@Override
                public void onDragEnd(DragEndEvent event) {
//                    Label label = (Label) event.getSource();
//                    int nowPos = showVerticalPanel.getWidgetIndex(label);
//                    if (nowPos != oldPos) {
                        change = true;
//                    }
                }

            });
        }

        /**
         * add New Column
         *
		 * @param columnConfig
		 */
        private void addNewColumn(SelectItem columnConfig) {
			Label label = getColumnWidget(columnConfig.getName());
            label.setStyleName("wfm-listing-panel-label");

            widgetsPos.put(label.getText(), widgetsPos.size());
            showVerticalPanel.add(label);
            showColumnDragController.makeDraggable(label);
			allColumnMap.put(columnConfig.getId(), label);
        }

        private Label getColumnWidget(String columnName) {
            return new Label(columnName, false);
        }
    }
}
