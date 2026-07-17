package com.edatasite.workforce.gwt.materialkanban.client;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandlerAdapter;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.WordUtils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.materialkanban.client.rpc.KanbanColumn;
import com.edatasite.workforce.gwt.materialkanban.client.rpc.KanbanService;
import com.edatasite.workforce.gwt.materialkanban.client.rpc.KanbanServiceAsync;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialSwitch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * User: Anvar Akramov
 * Date: 17-Apr-2018
 * Time: 17:59:31
 */
public class KanbanSettingsPopup extends KpiSideNavBox implements ClickHandler {

    


    //    private List<CustomColumnDefinitionConfig> columnConfigs;
    private KpiCheckBox applyForAllUsers;
    private HashMap<Integer, KanbanColumn> activeColumnsMap = new HashMap<>();
    private List<SelectItem> allColumns = new ArrayList<>();
    private FlexTable content;
    private Integer pageSize;
    private WfmButton2 save;
    private KanbanSettingsTopPanel topPanel;
    private KanbanBoard kanbanboard;

    private KanbanServiceAsync kanbanService = KanbanService.App.get();

    public KanbanSettingsPopup(KanbanBoard kanbanboard) {

        this.kanbanboard = kanbanboard;
        if(kanbanboard!=null) {
            this.pageSize = kanbanboard.getPageSize();

            if( kanbanboard.getVisibleColumns()!=null) {
                ArrayList<KanbanColumn> cols = kanbanboard.getVisibleColumns();
                cols.forEach(activeColumn -> this.activeColumnsMap.put(activeColumn.getId(), activeColumn));
            }
        }

        addStyleName("quick-add");
        getContentHeader().removeStyleName("side-nav__title");
        initialization();
        addBody(content);
        show();
    }

    private void initialization() {
        content = new FlexTable();
        //setting filter panel
        topPanel = new KanbanSettingsTopPanel(pageSize);
//        KpiToolTip toolTip = new KpiToolTip(topPanel);
        addHeader(topPanel);

        try {
            topPanel.setTitle(wfmStrings.customize() + " " + kanbanboard.getViewType().getListName());
        } catch (Exception byDefault) {
            topPanel.setTitle(wfmStrings.customize());
        }
        /*try {
            topPanel.setTitle(wfmStrings.customize() + " " + coreConstantsWithLookUp.getString( kanbanboard.getViewType().getViewName().name() ) );
            GWT.log("11111111: " +  kanbanboard.getViewType().getListName() + " " + kanbanboard.getViewType().getViewName().name());
        } catch (MissingResourceException ignored) {
            topPanel.setTitle(wfmStrings.customize() + " " + kanbanboard.getViewType().getListName());
            GWT.log("22222222: " +  kanbanboard.getViewType().getListName());
        } catch (Exception byDefault) {
            topPanel.setTitle(wfmStrings.customize());
        }*/

        ColumnSetttings columnSetttings = new ColumnSetttings();

        save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        save.ensureDebugId("ListingPanelSettingsPopup_save_button");

        save.addClickHandler(be -> {
            save.setEnabled(false);

            /*panelTools.setApplySettingsToAll(applyForAllUsers.getValue());
            panelTools.setColumnCodeName(columnSetttings.getOrderedColumns());*/

            LoadingPanel.loading(true, KanbanSettingsPopup.this);
            boolean applyForAll = applyForAllUsers.getValue() != null && applyForAllUsers.getValue();

            /*CommonService.App.get().saveListPanelSettings(panelTools, new AbstractAsyncCallback<Void>() {
                @Override
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false, KanbanSettingsPopup.this);
                    change = false;
                    save.setEnabled(true);
                    close.setEnabled(true);
                }

                @Override
                public void success(Void result) {
                    LoadingPanel.loading(false, KanbanSettingsPopup.this);
                    save.setEnabled(true);
                    close.setEnabled(true);
                    change = false;
                    listingPanelSaveChanges.saveListingPanelChanges(panelTools);
                    remove();
                }
            });*/
            kanbanService.saveKanbanBoardSettings(kanbanboard.getViewType(), topPanel.getPageSize(), columnSetttings.getActiveItems(),applyForAll, new AbstractAsyncCallback<Void>() {
                @Override
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(Void v) {
                    LoadingPanel.loading(false);
                    save.setEnabled(true);
                    kanbanboard.setPageSize(topPanel.getPageSize());
                    remove();
                    kanbanboard.init();
                }
            });
        });


        // Apply for all users check
        applyForAllUsers = new KpiCheckBox();

        FlexTable columnWidth = new FlexTable();

        int row = 0;


        if (Utils.hasRoles(Constants.ADMIN, Constants.DR)) {
            columnWidth.setWidget(row, 0, applyForAllUsers);
            columnWidth.setText(row, 1, wfmStrings.applyTheseSettingsForAllUsers());
        }

        row++;
        content.setWidget(row, 0, columnSetttings);

        row++;
        content.setHTML(row, 0, "&nbsp;");

        row++;
        content.setWidget(row, 0, columnWidth);

        addFooter(save);
    }

    /**
     * Hide Popup
     *
     * @param
     */
    @Override
    public void onClick(ClickEvent event) {
        hide();
    }

    //Column show and order table

    protected class ColumnSetttings extends FlexTable {

//        private Map<String, CustomColumnDefinitionConfig> activeColumnsMap = new HashMap<>();
//        private Map<String, CustomColumnDefinitionConfig> inActiveColumnsMap = new HashMap<>();

        private VerticalPanel showVerticalPanel;
        private PickupDragController showColumnDragController;
        private MaterialPanel container;


        ColumnSetttings() {
            initilazation();
        }

        private void initilazation() {

            container = new MaterialPanel("drag-tiles");
            setWidget(0, 0, container);

            initColumns();
        }

        private void initColumns() {
            // dragable container
            AbsolutePanel boundaryPanel = new AbsolutePanel();

            // initialize vertical panel to hold our columns
            showVerticalPanel = new VerticalPanel();

            boundaryPanel.add(showVerticalPanel);
            container.add(boundaryPanel);

            // initialize our column drag controller
            showColumnDragController = new PickupDragController(boundaryPanel, false);
            showColumnDragController.setBehaviorMultipleSelection(false);

            // initialize our column drop controller
            VerticalPanelDropController columnDropController = new VerticalPanelDropController(showVerticalPanel);
            showColumnDragController.registerDropController(columnDropController);
            showColumnDragController.addDragHandler(new DragHandlerAdapter() {
                @Override
                public void onDragEnd(DragEndEvent event) {
                }
            });
            if(kanbanboard!=null && kanbanboard.getKanbanBoardDesign()!=null) {

                kanbanboard.getKanbanBoardDesign().loadDefaultColumns(new AbstractAsyncCallback<ArrayList<SelectItem>>() {
                    @Override
                    public void failure(Throwable throwable) {
                        super.failure(throwable);
                    }

                    @Override
                    public void success(ArrayList<SelectItem> result) {
                        super.success(result);
                        allColumns = result;

                        activeColumnsMap.forEach((key, value) -> createNewColumnPanel(value, true));
                        for (SelectItem column : allColumns) {
                            if(activeColumnsMap.get(column.getId())==null) {
                                createNewColumnPanel(column, false);
                            }
                        }
                    }
                });
            }
/*
            for (int i = 0; i < columnConfigs.size(); i++) {
                CustomColumnDefinitionConfig columnConfig = columnConfigs.get(i);

                if (panelTools.getColumnCodeName().contains(columnConfig.getCodeName())) {
                    activeColumnsMap.put(columnConfig.getCodeName(), columnConfig);
                } else {
                    inActiveColumnsMap.put(columnConfig.getCodeName(), columnConfig);
                }
            }

            for (int i = 0; i < panelTools.getColumnCodeName().size(); i ++) {
                createNewColumnPanel(activeColumnsMap.get(panelTools.getColumnCodeName().get(i)));
            }
            inActiveColumnsMap.keySet().stream().forEach(key -> createNewColumnPanel(inActiveColumnsMap.get(key)));*/

        }

        /*private void addNewColumn(SelectItem columnConfig) {
            change = true;

//            SortableProperty columnProperty = (SortableProperty) columnConfig.getColumnProperty(SortableProperty.TYPE);

            ColumnTool columnTool = new ColumnTool();
            columnTool.setColumnWidth(columnConfig.getPreferredColumnWidth());
            panelTools.getColumnCodeName().add(columnConfig.getCodeName());
            panelTools.getColunmsTool().put(columnConfig.getCodeName(), columnTool);
        }

        private void removeColumn(CustomColumnDefinitionConfig columnConfig) {
            change = true;
            panelTools.removeColumnTools(columnConfig.getCodeName());
        }*/

        /**
         * Create new List Column
         *
         * @param columnConfig
         */
        private void createNewColumnPanel(SelectItem columnConfig, boolean active) {
            MaterialSwitch switcher = new MaterialSwitch();
            switcher.setLayoutData(columnConfig);
            switcher.setValue(active);


            MaterialPanel pnlColumn = new MaterialPanel("drag-tile" + (switcher.getValue() ? " state-on" : " state-off"));
            MaterialPanel pnlGrip = new MaterialPanel("drag-tile__grip");

            HTML columnTitle = new HTML(WordUtils.capitalizeFirst(!"".equals(columnConfig.getName()) ? (String) columnConfig.getName() : columnConfig.getCode()));
            columnTitle.setStyleName("drag-tile__text KanbanSettingsPopup");

            MaterialPanel pnlAction = new MaterialPanel("drag-tile__actions");
            pnlAction.add(switcher);

            switcher.addValueChangeHandler(vh -> {

                /*if (switcher.getValue()) {
                    addNewColumn(columnConfig);

                    pnlColumn.removeStyleName("state-off");
                    pnlColumn.addStyleName("state-on");

                } else if (activeColumnsMap.get(columnConfig.getId()) != null) {
                    removeColumn(columnConfig);

                    pnlColumn.removeStyleName("state-on");
                    pnlColumn.addStyleName("state-ff");
                } else {
                    change = false;
                    switcher.setValue(Boolean.TRUE);
                }*/
            });


            pnlColumn.add(pnlGrip);
            pnlColumn.add(columnTitle);
            pnlColumn.add(pnlAction);
            pnlColumn.setLayoutData(switcher);

            showVerticalPanel.add(pnlColumn);
            showColumnDragController.makeDraggable(pnlColumn, pnlGrip);
//            MaterialToast.fireToast("Created:" + columnConfig.getName());
//            SortableProperty columnProperty = (SortableProperty) columnConfig.getColumnProperty(SortableProperty.TYPE);

        }

        ArrayList<KanbanColumn> getActiveItems() {
            ArrayList<KanbanColumn> result = new ArrayList<>();
            if(showVerticalPanel!=null && showVerticalPanel.getWidgetCount()>0) {
                for(int i = 0; i<showVerticalPanel.getWidgetCount();i++) {
                    MaterialSwitch materialSwitch = (MaterialSwitch) (showVerticalPanel.getWidget(i).getLayoutData());
                    if(materialSwitch.getValue()) {
                        result.add( (KanbanColumn) materialSwitch.getLayoutData());
                    }
                }
            }
            return result;
        }
    }
}
