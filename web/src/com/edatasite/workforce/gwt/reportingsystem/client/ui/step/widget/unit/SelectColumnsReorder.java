package com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandlerAdapter;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.edatasite.workforce.gwt.core.client.form.EditableLabel;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.components.CollapsiblePanel;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.service.ReportingService;
import com.edatasite.workforce.gwt.reportingsystem.client.service.ReportingServiceAsync;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.ReportingStepControlView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.VerticalPanel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Virus on 8/30/14.
 */
public class SelectColumnsReorder extends Composite {

    private final String GROUP_PREFIX = "group@";
    private final String LINE = "@";
    private final String DEFAULT_GROUP = "defaultGroup";
    private final String TABLE_GROUP = "table_group@";

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final ReportingServiceAsync reportingService = ReportingService.App.get();
    private final AbsolutePanel absolutePanel = new AbsolutePanel();
    private final MaterialPanel groupingPanel = new MaterialPanel();
    private PickupDragController columnDragController;
    private VerticalPanel verticalPanel = new VerticalPanel();
    private LinkedHashMap<String, Div> list = new LinkedHashMap<>();
    private LinkedList<String> nameList = new LinkedList<>();
    private LinkedHashMap<String, ArrayList<String>> nameListByGroup = new LinkedHashMap<>();
    private LinkedHashMap<String, String> columnKeyByGroup = new LinkedHashMap<>();
    private LinkedHashMap<String, VerticalPanelDropController> dropControllerMap = new LinkedHashMap<>();
    private LinkedList<String> groupingList = new LinkedList<>();
    private HashMap<String, VerticalPanel> panelMap = new HashMap<>();
    private VerticalPanelDropController columnDropController;
    private ReportingStepControlView view;

    public SelectColumnsReorder() {
        initWidget(absolutePanel);
        groupingPanel.setPaddingBottom(0);
        absolutePanel.add(groupingPanel);

        columnDragController = new PickupDragController(absolutePanel, false);
        columnDragController.setBehaviorMultipleSelection(false);

        // initialize our widget drag controller
        PickupDragController widgetDragController = new PickupDragController(absolutePanel, false);
        widgetDragController.setBehaviorMultipleSelection(false);


        // initialize horizontal panel to hold our columns
        VerticalPanel horizontalPanel = new VerticalPanel();
        horizontalPanel.setSpacing(0);
        absolutePanel.add(horizontalPanel);

        // initialize our column drop controller
        columnDropController = new VerticalPanelDropController(horizontalPanel);
        columnDragController.registerDropController(columnDropController);
        columnDragController = new PickupDragController(absolutePanel, false);
        absolutePanel.addStyleName("customReport_tab_3__dnd_columns drag-tiles");
        absolutePanel.add(verticalPanel);
        columnDropController = new VerticalPanelDropController(verticalPanel);

        columnDragController.registerDropController(columnDropController);
        columnDragController.addDragHandler(new DragHandlerAdapter() {

            @Override
            public void onDragEnd(DragEndEvent event) {
                nameList.clear();
                columnKeyByGroup.clear();
                for (int i = 0; i < verticalPanel.getWidgetCount(); i++) {
                    Div lbl = (Div) verticalPanel.getWidget(i);
                    if (lbl.getElement().getId().startsWith(GROUP_PREFIX)) {
                        String groupName = lbl.getElement().getId().split(LINE)[1];
                        Element element = DOM.getElementById(TABLE_GROUP + groupName.trim().replace(" ", "_"));
                        NodeList<Element> tds = element != null ? element.getElementsByTagName("td") : null;
                        if (tds != null) {
                            if (nameListByGroup.get(groupName) != null) {
                                nameListByGroup.get(groupName).clear();
                            }
                            for (int j = 0; j < tds.getLength(); j++) {
                                Element groupElement = tds.getItem(j);
                                columnKeyByGroup.put(groupElement.getFirstChildElement().getId(), groupName);
                                nameList.add(groupElement.getFirstChildElement().getId());
                                if (nameListByGroup.get(groupName) != null && !nameListByGroup.get(groupName).contains(groupElement.getFirstChildElement().getId())) {
                                    nameListByGroup.get(groupName).add(groupElement.getFirstChildElement().getId());
                                }
                            }
                            if (nameListByGroup.get(groupName) != null && nameListByGroup.get(groupName).isEmpty()) {
                                lbl.removeStyleName("tileDragTarget--hasChild");
                            } else {
                                lbl.addStyleName("tileDragTarget--hasChild");
                            }
                        }
                    } else {
                        nameList.add(lbl.getElement().getId());
                    }
                }
                view.getReport().setSelectedColumns(getColumns(view.getReport().getGroupColumns(), view.getReport().getColumnMap()));
                view.getReport().setColumnsByGroupMap(columnKeyByGroup);
            }
        });
    }

    /**
     * <div class="dragdrop-draggable drag-tile state-on">
     * <div class="dragdrop-handle drag-tile__grip"></div>
     * <div class="drag-tile__text">Invoice Number</div>
     * </div>
     */

    public void addColumn(ColumnRpc rpc, boolean draggable, boolean fromCheckbox) {
        if (rpc.isChecked() || !draggable) {
            Div label = createLabel(rpc);
            if (draggable) {
                addToPanel(label, fromCheckbox ? verticalPanel : getOrCreatePanel(rpc));
                makeLabelDraggable(label);
            } else {
                nameList.add(groupingList.size(), rpc.getName());
                verticalPanel.insert(label, groupingList.size());
                groupingList.add(rpc.getName());
                label.removeStyleName("state-on");
                label.addStyleName("state-off");
            }
            list.put(rpc.getName(), label);
        }
    }

    private void addToPanel(Div label, VerticalPanel panel) {
        addRow(label, panel);
    }

    private VerticalPanel getOrCreatePanel(ColumnRpc rpc) {
        VerticalPanel panel = verticalPanel;
        String groupName = columnKeyByGroup.get(rpc.getName());
        if (groupName != null) {
            if (panelMap.get(groupName) == null) {
                createGroupSelector(groupName, "END", null);
                if (list.get(groupName) != null) {
                    list.get(groupName).addStyleName("tileDragTarget--hasChild");
                }
            }
            panel = panelMap.get(groupName);
            if (!nameListByGroup.get(groupName).contains(rpc.getName())) {
                nameListByGroup.get(groupName).add(rpc.getName());
            }
        }
        return panel;
    }

    private Div createLabel(ColumnRpc rpc) {
        Div label = new Div("drag-tile state-on");
        Div div = new Div("dragdrop-handle drag-tile__grip");
        label.add(div);
        Div dragTitle = new Div("drag-tile__text");
        dragTitle.getElement().setInnerHTML(rpc.getTitle());
        label.add(dragTitle);
        label.getElement().setId(rpc.getName());
        return label;
    }

    private void makeLabelDraggable(Div label) {
        nameList.add(label.getId());
        columnDragController.makeDraggable(label);
        label.removeStyleName("state-off");
        label.addStyleName("state-on");
    }

    private void addRow(Div label, VerticalPanel panelGroup) {
        GWT.log(label.getId());
        if (panelGroup.getWidgetCount() > 0) {
            boolean exists = false;

            for (int i = 0; i < panelGroup.getWidgetCount(); i++) {
                Div lbl = (Div) panelGroup.getWidget(i);
                if (lbl.getElement().getId().equals(label.getId())) {
                    exists = true;
                }
            }
            if (!exists) {
                panelGroup.add(label);
            }
        } else {
            panelGroup.add(label);
        }
    }

    public void removeColumn(ColumnRpc rpc, boolean fromCheckbox) {
        Div label = list.get(rpc.getName());
        if (label != null) {
            VerticalPanel customGroupPanel = panelMap.get(columnKeyByGroup.get(rpc.getName()));
            if (customGroupPanel != null && fromCheckbox) {
                customGroupPanel.forEach(e -> {
                    if (e.getElement().getId().equals(rpc.getName())) {
                        e.removeFromParent();
                    }
                });
                nameListByGroup.get(columnKeyByGroup.get(rpc.getName())).remove(rpc.getName());
                columnKeyByGroup.remove(rpc.getName());
                view.getReport().setColumnsByGroupMap(columnKeyByGroup);
            } else {
                verticalPanel.remove(label);
            }
            nameList.remove(rpc.getName());
            list.remove(rpc.getName());
            groupingList.remove(rpc.getName());
        }
    }

    public LinkedList<ColumnRpc> getColumns(LinkedList<ColumnRpc> groupColumns, HashMap<String, ColumnRpc> map) {
        LinkedList<ColumnRpc> columnRpcs = new LinkedList<>(groupColumns);
        for (String item : nameList) {
            columnRpcs.add(map.get(item));
        }
        return columnRpcs;
    }

    public void makeDraggable(ColumnRpc rpc) {
        removeColumn(rpc, false);
        addColumn(rpc, true, false);
    }

    public void makeGrouping(ColumnRpc rpc) {
        if (nameList.contains(rpc.getName())) {
            removeColumn(rpc, false);
        }
        addColumn(rpc, false, false);
    }

    public void clearGroup(LinkedList<ColumnRpc> sumaries) {
        panelMap.forEach((k,v) -> {
            Div group = list.get(k);
            if (group != null) {
                verticalPanel.remove(group);
            }
            nameListByGroup.remove(k);
            columnDragController.unregisterDropController(dropControllerMap.get(k));
        });
        panelMap.clear();
        for (ColumnRpc item : sumaries) {
            removeColumn(item, false);
        }
    }

    public void setView(ReportingStepControlView view) {
        this.view = view;
    }

    public boolean createGroupSelector(String group, String condition, String column) {
        Div cLabel;
        int widgetIndex = 0;
        if (nameListByGroup.get(group) != null) {
            Info.warn("Group with name \"" + group + "\" already exists");
            return false;
        }

        Div label = new Div("drag-tile state-on tileDragTarget__wrapper tileColReorder");
        Div div = new Div("draggable-group drag-tile__grip");
        label.add(div);
        label.getElement().setId(GROUP_PREFIX + group.trim().replace(" ", "_"));

        GroupSelector selector = new GroupSelector(group);
        selector.getElement().addClassName("tileDragTarget");
        label.add(selector);

        columnDragController.makeDraggable(label, div);

        if (column != null) {
            cLabel = list.get(column);
            widgetIndex = verticalPanel.getWidgetIndex(cLabel);
        }

        switch (condition) {
            case "BEGINNING":
                verticalPanel.insert(label, 0);
                break;
            case "AFTER":
                verticalPanel.insert(label, widgetIndex + 1);
                break;
            case "BEFORE":
                verticalPanel.insert(label, widgetIndex);
                break;
            default:
                verticalPanel.add(label);
                break;
        }

        nameListByGroup.put(group, new ArrayList<>());
        list.put(group, label);
        return true;
    }

    public boolean validateGroups() {
        AtomicBoolean valid = new AtomicBoolean(true);
        if (!nameListByGroup.isEmpty()) {
            nameListByGroup.forEach((k, v) -> {
                ArrayList<String> values = nameListByGroup.get(k);
                if (values.isEmpty()) {
                    Info.warn("You cannot create an empty group");
                    valid.set(false);
                }
            });
        }
        return valid.get();
    }


    public void setCustomGroups(LinkedHashMap<String, String> columnsByGroupMap) {
        this.columnKeyByGroup = columnsByGroupMap;
    }


    protected class GroupSelector extends FlexTable {

        private String name;
        private MaterialPanel container;
        private EditableLabel label;


        GroupSelector(String name) {
            this.name = name;
            initilazation();
        }

        private void initilazation() {
            container = new MaterialPanel("tileDragTarget-posHolder");
            setWidget(0, 0, container);
            drawForm();
        }

        private void drawForm() {
            AbsolutePanel boundaryPanel = new AbsolutePanel();
            VerticalPanel showVerticalPanel = new VerticalPanel();
            showVerticalPanel.getElement().setId(TABLE_GROUP + name.trim().replace(" ", "_"));
            showVerticalPanel.getElement().setClassName("tileDragTarget__target__tbl");

            boundaryPanel.add(showVerticalPanel);

            VerticalPanelDropController columnDropController = new VerticalPanelDropController(showVerticalPanel);
            columnDragController.registerDropController(columnDropController);
            dropControllerMap.put(name, columnDropController);

            CollapsiblePanel collapsiblePanel = new CollapsiblePanel();
            collapsiblePanel.addWidget(boundaryPanel);
            boundaryPanel.getElement().addClassName("tileDragTarget__target");
            boundaryPanel.getParent().getElement().addClassName("tileDragTarget__body");

            container.add(collapsiblePanel);

            LinkedList<GColumn> columns = new LinkedList<>();

            MenuBar menuBar = new MenuBar(true);
            menuBar.setAutoOpen(true);

            MenuPopItem deleteButton = new MenuPopItem(wfmStrings.delete());
            deleteButton.ensureDebugId("delete-button");
            deleteButton.setCommand(() -> {
                Div group = list.get(name);
                verticalPanel.remove(group);
                rearrangeFields();
            });
            menuBar.addItem(deleteButton);

            ToolItem toolItem = new ToolItem(1);
            toolItem.setWidget(menuBar);

            this.label = new EditableLabel(name);

            this.label.addValueChangeHandler(valueChangeEvent -> {
                String updatedGroupName = this.label.getText();
                if (!updatedGroupName.equals(name)) {
                    if (nameListByGroup.get(updatedGroupName) != null) {
                        Info.warn("Group with name \"" + updatedGroupName + "\" already exists");
                        this.label.cancelLabelChange();
                    } else {
                        updateGroupNames(updatedGroupName);
                    }
                }
            });

            GColumn customizeText = new GColumn(GColumnEnum.COL, label);
            customizeText.setStyleName("panel-w-switch");
            columns.add(customizeText);

            GColumn customizeAction = new GColumn(GColumnEnum.COL_AUTO, toolItem.getAction());
//            columns.add(new GColumn(GColumnEnum.COL_2, toolItem.getAction()));
            customizeAction.getElement().addClassName("action-listing__wrapper");
            columns.add(customizeAction);
            collapsiblePanel.setCustomizeHeader(columns);

            collapsiblePanel.setActive(true);
            panelMap.put(name, showVerticalPanel);
        }

        private void updateGroupNames(String groupName) {

            ArrayList<String> oldColumnList = nameListByGroup.remove(name);
            columnDragController.unregisterDropController(dropControllerMap.remove(name));
            Div oldGroupLabel = list.remove(name);
            verticalPanel.remove(oldGroupLabel);
            panelMap.remove(name);

            createGroupSelector(groupName, "END", null);

            if (oldColumnList != null) {
                for (String column : oldColumnList) {
                    columnKeyByGroup.put(column, groupName);
                    nameListByGroup.get(groupName).add(column);
                    Div label = list.get(column);
                    panelMap.get(groupName).add(label);
                }
            }

            this.name = groupName;
            view.getReport().setColumnsByGroupMap(columnKeyByGroup);
        }

        private void rearrangeFields() {
            panelMap.remove(name);
            columnDragController.unregisterDropController(dropControllerMap.get(name));
            ArrayList<String> columns = nameListByGroup.get(name);
            columns.forEach(column -> {
                Div label = list.get(column);
                verticalPanel.add(label);
                columnKeyByGroup.remove(column);
                columnDragController.makeDraggable(label);
                label.removeStyleName("state-off");
                label.addStyleName("state-on");
            });
            nameListByGroup.remove(name);
        }
    }
}
