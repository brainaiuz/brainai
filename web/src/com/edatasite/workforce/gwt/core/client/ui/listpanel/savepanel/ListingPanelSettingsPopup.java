package com.edatasite.workforce.gwt.core.client.ui.listpanel.savepanel;

import com.allen_sauer.gwt.dnd.client.*;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.WordUtils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ColumnColor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ColumnTool;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelSaveChanges;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.gen2.table.client.FixedWidthFlexTable;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.gen2.table.client.property.SortableProperty;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.VerticalPanel;
import gwt.material.design.client.ui.MaterialPanel;

import java.util.*;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 26-Aug-2010
 * Time: 17:59:31
 */
public class ListingPanelSettingsPopup extends KpiSideNavBox implements ClickHandler {

    private KpiCheckBox additionalInformationInPopup;
    private KpiCheckBox applyForAllUsers;
    private final List<CustomColumnDefinitionConfig> columnConfigs;
    private FlexTable content;
    private ListingPanelSaveChanges listingPanelSaveChanges;
    private final ListPanelToolRpc panelTools;
    private WfmButton2 save;
    private SelectItem sortByColumn;
    private final FixedWidthFlexTable tableHeader;
    private final SelectionGrid.SelectionPolicy policy;
    private SettingsFilterPanel filterPanel;

    private final boolean hasAdditionalInformation;
    private final Integer stepID;
    private final String formID;

    public ListingPanelSettingsPopup(List<CustomColumnDefinitionConfig> columnConfigs, ListPanelToolRpc panelTools, LinkedHashMap<String, CustomColumnDefinitionConfig> mapColumn, FixedWidthFlexTable tableHeader, SelectionGrid.SelectionPolicy policy, boolean hasAdditionalInformation, Integer stepID, String formId) {
        this.panelTools = panelTools;
        this.columnConfigs = columnConfigs;
        this.tableHeader = tableHeader;
        this.policy = policy;
        this.hasAdditionalInformation = hasAdditionalInformation;
        this.stepID = stepID;
        this.formID = formId;

        addStyleName("quick-add");
        getContentHeader().removeStyleName("side-nav__title");
        initialization();
        addBody(content);
        show();
    }

    private void initialization() {
        content = new FlexTable();

        //setting filter panel
        filterPanel = new SettingsFilterPanel(panelTools.getSortByType(), panelTools.getPageSize());
        addHeader(filterPanel);

        try {
            filterPanel.setTitle(wfmStrings.customizeList());
        } catch (MissingResourceException ignored) {
            filterPanel.setTitle(wfmStrings.customizeList());
        } catch (Exception byDefault) {
            filterPanel.setTitle(wfmStrings.customizeList());
        }

        ColumnSetttings columnSetttings = new ColumnSetttings();

        save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        save.ensureDebugId("ListingPanelSettingsPopup_save_button");

        save.addClickHandler(be -> {
            save(columnSetttings);
        });
        // Apply for all users check
        applyForAllUsers = new KpiCheckBox();

        additionalInformationInPopup = new KpiCheckBox();
        additionalInformationInPopup.setValue(panelTools != null && panelTools.isShowPopup());

        FlexTable columnWidth = new FlexTable();

        int row = 0;

        if (panelTools != null && hasAdditionalInformation) {
            columnWidth.setWidget(row, 0, additionalInformationInPopup);
            columnWidth.setText(row++, 1, wfmStrings.showAdditionalInformationInPopup());
        }

        if (Utils.hasRoles(Constants.ADMIN, Constants.DR)) {
            columnWidth.setWidget(row, 0, applyForAllUsers);
            columnWidth.setText(row, 1, wfmStrings.applyTheseSettingsForAllUsers());
        }

        filterPanel.setColumnChangeEvent(() -> {

            if (filterPanel.getSelectedColumnItem() != null) {
                filterPanel.setSortType(SettingsFilterPanel.ASCENDING);
            }
        });

        row++;
        content.setWidget(row, 0, columnSetttings);

        row++;
        content.setHTML(row, 0, "&nbsp;");

        row++;
        content.setWidget(row, 0, columnWidth);


        addFooter(save);
    }

    private void save(ColumnSetttings columnSettings) {
        save.setEnabled(false);
        boolean applyForAll = applyForAllUsers.getValue() != null && applyForAllUsers.getValue();

        panelTools.getColumnCodeName().clear();
        panelTools.getColunmsTool().clear();
        panelTools.setApplySettingsToAll(applyForAll);
        panelTools.setShowPopup(additionalInformationInPopup.getValue() != null && additionalInformationInPopup.getValue());
        if (applyForAll) {
            saveListPanelColumnWidth();
        }
        int n = SelectionGrid.SelectionPolicy.CHECKBOX.equals(policy) || SelectionGrid.SelectionPolicy.RADIO.equals(policy) ? 1 : 0;
        for (ColumnPanelWidget _column : columnSettings.getAllColumns()) {
            if (_column.getSwitcherValue()) {
                ColumnTool columnTool = new ColumnTool();
                //GWT.log("SAVE CHANGES: "  + _column.getColumnConfig().getCodeName() + " " + _column.getColumnConfig().getPreferredColumnWidth() +"    - "+ tableHeader.getColumnWidth(n) );
                columnTool.setColumnWidth(tableHeader.getColumnWidth(n));
//                    columnTool.setColumnWidth(_column.getColumnConfig().getPreferredColumnWidth())
                for (ColumnColor color : _column.getColorData()) {
                    columnTool.addColor(color);
                }
                panelTools.addColumnTool(_column.getColumnConfig().getCodeName(), columnTool);
            } else {
                panelTools.removeColumnTools(_column.getColumnConfig().getCodeName());
            }
            n++;
        }
        if (filterPanel != null && filterPanel.isAnyColumnSelected()) {
            panelTools.setSortBy(filterPanel.getSelectedColumnItem().getDescription());
            panelTools.setSortByType(filterPanel.getSortType());
        } else {
            panelTools.setSortBy(null);
        }
        if (filterPanel.getPageSizeListBox() != null) {
            panelTools.setPageSize(filterPanel.getPageSizeListBox().getSelectedId(true));
        }

        LoadingPanel.loading(true, ListingPanelSettingsPopup.this);
        panelTools.setStepID(stepID);
        panelTools.setFormID(formID);
        CommonService.App.get().saveListPanelSettings(panelTools, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false, ListingPanelSettingsPopup.this);
                save.setEnabled(true);
            }

            @Override
            public void success(Void result) {
                LoadingPanel.loading(false, ListingPanelSettingsPopup.this);
                save.setEnabled(true);
                listingPanelSaveChanges.saveListingPanelChanges(panelTools);
                remove();
            }
        });
    }

    private SelectItem[] getAllColumnNames() {
        ArrayList<SelectItem> result = new ArrayList<>();
        int count = 0;
        for (CustomColumnDefinitionConfig cc : columnConfigs) {
            if (!Constants.LISTING_ACTION.COLUMN_CODE.equals(cc.getCodeName())) {
                result.add(new SelectItem(count++, ((String) cc.getColumnName()), cc.getCodeName()));
            }
        }
        result.sort(Comparator.comparing(o -> o.getName()));
        return result.toArray(new SelectItem[]{});
    }


    /**
     * Saved Listing Panel Column Width
     */
    private void saveListPanelColumnWidth() {
        if (SelectionGrid.SelectionPolicy.MULTI_ROW.equals(policy) || SelectionGrid.SelectionPolicy.ONE_ROW.equals(policy)) {
            for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
                if (panelTools.getColumnCodeName().get(i) != null && panelTools.getColunmsTool().get(panelTools.getColumnCodeName().get(i)) != null) {
                    panelTools.getColunmsTool().get(panelTools.getColumnCodeName().get(i)).setColumnWidth(tableHeader.getColumnWidth(i));
                }
            }
        } else {// first column checkbox or radio button
            for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
                if (panelTools.getColumnCodeName().get(i) != null && panelTools.getColunmsTool().get(panelTools.getColumnCodeName().get(i)) != null) {
                    panelTools.getColunmsTool().get(panelTools.getColumnCodeName().get(i)).setColumnWidth(tableHeader.getColumnWidth(i + 1));
                }
            }
        }
    }

    /**
     * Save Button clicked doing interface
     *
     * @param listingPanelSaveChanges
     */
    public void setListingPanelSaveChanges(ListingPanelSaveChanges listingPanelSaveChanges) {
        this.listingPanelSaveChanges = listingPanelSaveChanges;
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

    private class ColumnSetttings extends FlexTable {

        private VerticalPanel columnVerticalPanel;
        private PickupDragController showColumnDragController;
        private MaterialPanel container;


        public ColumnSetttings() {
            initilazation();
        }

        private ArrayList<ColumnPanelWidget> getAllColumns() {
            ArrayList<ColumnPanelWidget> result = new ArrayList<>();
            for (int i = 0; i < columnVerticalPanel.getWidgetCount(); i++) {
                if (columnVerticalPanel.getWidget(i) instanceof ColumnPanelWidget) {
                    result.add((ColumnPanelWidget) columnVerticalPanel.getWidget(i));
                }
            }
            return result;
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
            columnVerticalPanel = new VerticalPanel();
            columnVerticalPanel.clear();
            boundaryPanel.add(columnVerticalPanel);
            container.add(boundaryPanel);

            // initialize our column drag controller
            showColumnDragController = new PickupDragController(boundaryPanel, false);
            showColumnDragController.setBehaviorMultipleSelection(false);

            // initialize our column drop controller
            VerticalPanelDropController columnDropController = new VerticalPanelDropController(columnVerticalPanel);
            showColumnDragController.registerDropController(columnDropController);

            //panelTools.getColumnCodeName().forEach(c-> GWT.log(c));
            HashMap<String, CustomColumnDefinitionConfig> allColumns = new HashMap<>();
            HashMap<String, CustomColumnDefinitionConfig> activeColumns = new HashMap<>();
            HashMap<String, CustomColumnDefinitionConfig> inactiveColumns = new HashMap<>();

            for (CustomColumnDefinitionConfig columnConfig : columnConfigs) {
                allColumns.put(columnConfig.getCodeName(), columnConfig);

                if (!panelTools.getColumnCodeName().contains(columnConfig.getCodeName())) {
                    inactiveColumns.put(columnConfig.getCodeName(), columnConfig);
                }
            }

            for (String c : panelTools.getColumnCodeName()) {
                if (allColumns.containsKey(c)) {
                    activeColumns.put(c, allColumns.get(c));
                }
            }

            for (Map.Entry<String, CustomColumnDefinitionConfig> activeCol : activeColumns.entrySet()) {
                createNewColumnPanel(activeCol.getValue());
            }

            for (Map.Entry<String, CustomColumnDefinitionConfig> inactiveCol : inactiveColumns.entrySet()) {
                createNewColumnPanel(inactiveCol.getValue());
            }

            // set column for sorting list
            if (sortByColumn != null) {
                filterPanel.setSelectedColumn(sortByColumn);
            }

            // set sorting type
            if (panelTools.getSortByType() != null) {

                if ("asc".equals(panelTools.getSortByType())) {
                    filterPanel.setSortType(SettingsFilterPanel.ASCENDING);
                } else {
                    filterPanel.setSortType(SettingsFilterPanel.DESCENDING);
                }
            }
        }

        private void addNewColumn(CustomColumnDefinitionConfig columnConfig) {

            SortableProperty columnProperty = (SortableProperty) columnConfig.getColumnProperty(SortableProperty.TYPE);

            if (columnProperty != null && columnProperty.isColumnSortable()) {

                SelectItem column = new SelectItem();
                column.setId(filterPanel.getColumnItemCount());
                column.setName(WordUtils.capitalizeFirst(!"".equals(columnConfig.getColumnName()) ? (String) columnConfig.getColumnName() : columnConfig.getCodeName()));
                column.setDescription(columnConfig.getCodeName());//Returns key (name) of the column.

                filterPanel.addColumnLisItem(column);
            }
        }

        private void removeColumn(CustomColumnDefinitionConfig columnConfig) {

            for (SelectItem item : filterPanel.getColumnItems()) {
                if (columnConfig.getCodeName().equals(item.getDescription())) {
                    filterPanel.removeColumnListItem(item);
                    break;
                }
            }
        }

        /**
         * Create new List Column
         *
         * @param columnConfig
         */


        private void createNewColumnPanel(CustomColumnDefinitionConfig columnConfig) {
            ColumnPanelWidget pnlColumn = new ColumnPanelWidget(columnConfig, panelTools.getColumnCodeName().contains(columnConfig.getCodeName()));
            LinkedHashMap<String, ColumnTool> cTools = panelTools.getColunmsTool();// TODO WTF? make columnConfigs carry Color Settings data, too
            if (cTools != null && cTools.containsKey(columnConfig.getCodeName())) {
                pnlColumn.setColorData(new ArrayList<>(cTools.get(columnConfig.getCodeName()).getColors().values()));
            }
            pnlColumn.setOnSwitch(() -> {
                if (pnlColumn.getSwitcherValue()) {
                    addNewColumn(columnConfig);
                } else if (panelTools.getColumnCodeName().size() != 1) {
                    removeColumn(columnConfig);
                } else {
                    pnlColumn.setSwitcherValue(Boolean.TRUE);
                }
            });
            SortableProperty columnProperty = (SortableProperty) columnConfig.getColumnProperty(SortableProperty.TYPE);
            if (pnlColumn.getSwitcherValue() && columnProperty != null && columnProperty.isColumnSortable()) {

                SelectItem column = new SelectItem();
                column.setId(filterPanel.getColumnItemCount());
                column.setName(pnlColumn.getColumnTitleText());
                column.setDescription(columnConfig.getCodeName());

                filterPanel.addColumnLisItem(column);
                if (column.getDescription().equals(panelTools.getSortBy())) {
                    sortByColumn = column;
                }
            }
            columnVerticalPanel.add(pnlColumn);
            pnlColumn.makeDraggable(showColumnDragController);
            showColumnDragController.addDragHandler(new DragHandler() {
                @Override
                public void onDragEnd(DragEndEvent event) {

                }

                @Override
                public void onDragStart(DragStartEvent event) {
                    if (event.getSource() instanceof ColumnPanelWidget) {
                        ((ColumnPanelWidget) event.getSource()).closeColorPanel();
                    }
                }

                @Override
                public void onPreviewDragEnd(DragEndEvent event) throws VetoDragException {

                }

                @Override
                public void onPreviewDragStart(DragStartEvent event) throws VetoDragException {

                }
            });
        }
    }
}
