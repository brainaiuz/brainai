package com.edatasite.workforce.gwt.core.client.ui.assigneetree;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.view.client.MultiSelectionModel;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: Abdullo
 * Date: 18.04.12
 * Time: 20:09
 */
public class KpiCellTree extends Composite {

    public interface CellTreeResources extends ClientBundle {
        @CssResource.NotStrict
        @Source("cellTree.css")
        CssResource cellTree();
    }

    public static CellTreeResources resource = GWT.create(CellTreeResources.class);

    private LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> items;
    private LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> searchItems;

    private boolean open = true;
    private boolean fromBudget = false;

    private MultiSelectionModel<KpiTreeInfo> selectionModel;

    private KpiTreeViewModel treeViewModel;

    private ArrayList<KpiTreeInfo> selectedItems;
    private HashMap<Integer, List<Integer>> budgetTreeItems;

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private String searchDefaultText = wfmStrings.searchTypeMessage();
    /**
     * The CellTree.
     */
    private CellTree cellTree;

    /**
     * The label that shows selectedWithCategories names.
     */
    FlowPanel selectedPanel;

    HTMLPanel treePanel;
    FlowPanel treeInnerPanel;

    FlowPanel content;

    private TextBox searchTextBox;
    private HTMLPanel actionsPanel;
    private FlowPanel selectedOption;
    private SelectionContainer selectionContainer;
    private KpiDataGrid<KpiTreeInfo> selectedDataGrid;
    private ColumnSortEvent.ListHandler<KpiTreeInfo> sortHandler;

    private KpiCheckBox selectAll;
    private boolean hideLeftPanel;
    private Integer limit;
    private String limitErrorMessage;

    public KpiCellTree(LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> treeItems) {
        this();
        if (treeItems != null) {
            this.items = treeItems;
            drawTree();
        }
    }

    public KpiCellTree(LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> treeItems, boolean hideLeftPanel) {
        this.hideLeftPanel = hideLeftPanel;
        onInitialize();
        if (treeItems != null) {
            this.items = treeItems;
            drawTree();
        }
    }

    public KpiCellTree(String searchDefaultText) {
        this.searchDefaultText = searchDefaultText;
        onInitialize();
    }

    public KpiCellTree(boolean fromBudget) {
        this.fromBudget = fromBudget;
        onInitialize();
    }

    public KpiCellTree() {
        onInitialize();
    }

    public interface KpiResources extends CellTree.Resources {

        @ImageResource.ImageOptions(flipRtl = true)
        @Source("cellTreeClosedItem.gif")
        ImageResource cellTreeClosedItem();

        @ImageResource.ImageOptions(flipRtl = true)
        @Source("cellTreeOpenItem.gif")
        ImageResource cellTreeOpenItem();

        @Override
        @Source("com/edatasite/workforce/gwt/core/client/ui/assigneetree/gwt-CellTree.css")
        CellTree.Style cellTreeStyle();
    }

    private KpiResources res = GWT.create(KpiResources.class);

    /**
     * Initialize this widget.
     */
    public void onInitialize() {

        resource.cellTree().ensureInjected();
        selectedItems = new ArrayList<>();
        content = new FlowPanel();
        treePanel = new HTMLPanel("");
        treeInnerPanel = new FlowPanel();
        selectedPanel = new FlowPanel();
        selectedOption = new FlowPanel();
        selectAll = new KpiCheckBox(wfmStrings.selectAll());
        actionsPanel = new HTMLPanel("span", "");

        content.addStyleName("addAssignees");
        selectedPanel.addStyleName("selectedLabel");
        treePanel.addStyleName("tree");
        treePanel.setWidth(fromBudget ? "50%" : "40%");
        selectedOption.addStyleName("selectOpt");
        actionsPanel.addStyleName("c sortSwitcher");

//        FlowPanel action = new FlowPanel();
//        action.addStyleName("sect");

        selectedDataGrid = new KpiDataGrid<>(KpiTreeInfo.KEY_PROVIDER);
        sortHandler = new ColumnSortEvent.ListHandler<>(selectedDataGrid.getList());
        selectedDataGrid.addColumnSortHandler(sortHandler);
        selectedDataGrid.setSize("100%", "100%");
        selectedDataGrid.addStyleName("cellBasedWidget-mod");

        createSelectionModule();

        selectAll.addValueChangeHandler(booleanValueChangeEvent -> {
            if (items != null) {
                int checkedCount = selectedItems.size();
                for (KpiTreeInfo key : items.keySet()) {
                    selectionModel.setSelected(key, booleanValueChangeEvent.getValue());
                    for (KpiTreeInfo item : items.get(key)) {
                        if (selectAll.getValue() && limit != null && limit.equals(checkedCount)) {
                            break;
                        }
                        selectionModel.setSelected(item, booleanValueChangeEvent.getValue());
                        if (!item.isSelected()) {
                            checkedCount++;
                        }
                        item.setSelected(booleanValueChangeEvent.getValue());
                    }
                    if (selectAll.getValue() && limit != null && limit.equals(checkedCount)) {
                        break;
                    }
                }
            }
        });

//        action.add(selectAll);

        selectedPanel.add(selectedDataGrid);

        searchBox();

        if (fromBudget) {
            Div div = new Div();
            div.getElement().setAttribute("style", "margin-left:29px");
            div.add(selectAll);
            selectedOption.add(div);

        }

        treeInnerPanel.add(actionsPanel);
//        selectedOption.add(action);
        treePanel.add(treeInnerPanel);
        treeInnerPanel.add(selectedOption);

        content.add(treePanel);

        if (hideLeftPanel) {
        } else {
            content.add(selectedPanel);
        }
        initWidget(content);
    }

    public void removeStyleNameFromDataGrid(String style) {
        selectedDataGrid.removeStyleName(style);
    }

    public void addStyleNameToDataGrid(String style) {
        selectedDataGrid.addStyleName(style);
    }

    private void createSelectionModule() {
        selectionModel = new MultiSelectionModel<>(fromBudget ? KpiTreeInfo.KEY_PROVIDER_BUDGET : KpiTreeInfo.KEY_PROVIDER);
        selectionModel.addSelectionChangeHandler(event -> {
            if (limit != null && selectedItems.size() == limit && treeViewModel.getLastCheckedItem() != null && treeViewModel.getLastCheckedItem().isSelected()) {
                Info.show(limitErrorMessage, Info.Type.WARNING);
                treeViewModel.getLastCheckedItem().setSelected(false);
                drawSelection();
            } else {
                drawSelection();
            }
        });
    }

    private void searchBox() {
        searchTextBox = new TextBox();
        searchTextBox.setTitle(searchDefaultText);
        searchTextBox.setText(searchDefaultText);
        searchTextBox.addStyleName("search-textbox");
        searchTextBox.addFocusHandler(event -> {
            if ((searchDefaultText).trim().equals(searchTextBox.getText().trim())) {
                searchTextBox.setText("");
                searchTextBox.removeStyleName("search-textbox");
            }
        });
        searchTextBox.addBlurHandler(event -> {
            if ("".equals(searchTextBox.getText())) {
                searchTextBox.setText(searchDefaultText);
                searchTextBox.addStyleName("search-textbox");
            }
        });
        searchItems = new LinkedHashMap<>();
        searchTextBox.addKeyUpHandler(event -> {
            searchItems.clear();
            for (KpiTreeInfo key : items.keySet()) {
                for (KpiTreeInfo item : items.get(key)) {
                    if (item.getName().toLowerCase().contains(searchTextBox.getText().toLowerCase())) {
                        if (item.getMatchSortString() != null) {
                            if (item.getMatchSortString().toLowerCase().equals(searchTextBox.getText().toLowerCase())) {
                                item.setMatchSortInteger(1);
                            }
                        }
                        if (searchItems.get(key) == null) {
                            ArrayList<KpiTreeInfo> value = new ArrayList<>();
                            value.add(item);
                            searchItems.put(key, value);
                        } else {
                            searchItems.get(key).add(item);
                        }
                    }
                }
            }

            searchTree(searchItems);
        });

//        Icon clearImage = new Icon();
        WfmButton2 clearImage = new WfmButton2("", WfmButton2.BTN_WHITE);
        clearImage.setStyleName("btn--icon");
        clearImage.add(new SvgIcon(SvgEnum.x));
        clearImage.setTitle(wfmStrings.clear() + " " + wfmStrings.search() + " " + wfmStrings.text());
        clearImage.addClickHandler(event -> {
            searchTextBox.setText(searchDefaultText);
            searchTextBox.addStyleName("search-textbox");
            searchTree(items);
        });

        Div div = new Div("selectOpt__search");
        div.add(searchTextBox);
        div.add(clearImage);
        selectedOption.add(div);
    }

    public void drawSelectedSide(SelectionContainer selectionContainer) {
        this.selectionContainer = selectionContainer;
        //interface method calling in below rows
        selectionContainer.selectedDataGrid(selectedDataGrid, sortHandler, selectionModel);
        selectionContainer.additionalActions(actionsPanel);

//        if (selectedDataGrid.getColumnCount() <= 2) {
//            selectedPanel.addStyleName("selectedLabel--fixed");
//        } else {
//            selectedPanel.removeStyleName("selectedLabel--fixed");
//        }
    }

    private void drawSelection() {
        selectedItems = new ArrayList<>();
        budgetTreeItems = new HashMap<>();
        for (final KpiTreeInfo value : selectionModel.getSelectedSet()) {
            if (value.getDepartmentId() == null) {
                continue;
            }
            selectedItems.add(value);

            if (fromBudget) {
                if (budgetTreeItems.get(value.getDepartmentId()) != null) {
                    List<Integer> ids = budgetTreeItems.get(value.getDepartmentId());
                    ids.add(value.getId());
                    budgetTreeItems.remove(value.getDepartmentId());
                    budgetTreeItems.put(value.getDepartmentId(), ids);
                } else {
                    List<Integer> ids = new ArrayList<>();
                    ids.add(value.getId());
                    budgetTreeItems.put(value.getDepartmentId(), ids);
                }
            }
        }
        viewShowItems();
    }

    private void drawTree() {
        if (cellTree != null) {
            createSelectionModule();
            selectedItems = new ArrayList<>();
            viewShowItems();
            cellTree.removeFromParent();
        }
        initializeTree(items);
    }

    private void drawAddedTree() {
        if (cellTree != null) {
            selectionModel.clear();
            selectedItems = null;
            selectedItems = new ArrayList<>();
            viewShowItems();
            cellTree.removeFromParent();
        }
        initializeTree(items);
    }

    private void initializeTree(LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> treeItems) {
        cellTree = null;
        treeViewModel = null;
        treeViewModel = new KpiTreeViewModel(selectionModel, treeItems, limit, limitErrorMessage);
        cellTree = new CellTree(treeViewModel, null, res);
        cellTree.getElement().getStyle().setOverflow(Style.Overflow.AUTO);
        cellTree.setDefaultNodeSize(10000);
        if (hideLeftPanel) {
            cellTree.setHeight("auto");
        }
        treeInnerPanel.add(cellTree);
        for (KpiTreeInfo key : treeItems.keySet()) {
            int itemsCount = treeItems.get(key).size();
            int selectedCount = 0;
            for (KpiTreeInfo item : treeItems.get(key)) {
                if (item.isSelected()) {
                    selectionModel.setSelected(item, item.isSelected());
                    selectedCount++;
                }
            }
            if (selectedCount > 0 && selectedCount == itemsCount) {
                selectionModel.setSelected(key, true);
            }
        }
        openAllTree(open);
    }

    private void searchTree(LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> treeItems) {
        if (cellTree != null) {
            cellTree.removeFromParent();
            initializeTree(treeItems);
        }
    }

    public ArrayList<KpiTreeInfo> getSelectedData() {
        return selectedItems;
    }

    public HashMap<Integer, List<Integer>> getBudgetTreeItems() {
        return this.budgetTreeItems;
    }

    public SelectItem[] getSelectedItems() {
        /*SelectItem[] selection = new SelectItem[selectedItems.size()];
        for (int i = 0; i < selection.length; i++) {
            if (selectedItems.get(i).getEssRole() != null && !selectedItems.get(i).getEssRole()) {
                selection[i] = new SelectItem(selectedItems.get(i).getId(), selectedItems.get(i).getName());
            }
        }
        return selection;*/
        return selectedItems.stream().filter(treeInfo ->
                (treeInfo.getEssRole() != null && !treeInfo.getEssRole()) || Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_ESS_AS_PROJECT_MANAGER)
        ).map(treeInfo -> new SelectItem(treeInfo.getId(), treeInfo.getName())).toArray(SelectItem[]::new);
    }

    public boolean isEmpty() {
        return selectedItems.isEmpty();
    }

    public Integer[] getSelectedIds() {
        if (selectedItems != null && selectedItems.size() > 0) {
            Integer[] ids = new Integer[selectedItems.size()];
            int kk = 0;
            for (KpiTreeInfo treeInfo : selectedItems) {
                ids[kk] = treeInfo.getId();
                kk++;
            }
            return ids;
        }
        return null;
    }

    public List<Integer> getSelectedEntityIDs() {
        if (selectedItems != null && selectedItems.size() > 0) {
            List<Integer> ids = new ArrayList<>();
            for (KpiTreeInfo treeInfo : selectedItems) {
                ids.add(treeInfo.getEmployeeId());
            }
            return ids;
        }
        return null;
    }

    public LinkedHashMap<Integer, Integer> getSelectedEmployeeIDsWithStatus() {
        if (selectedItems != null && selectedItems.size() > 0) {
            return selectedItems.stream().collect(
                    Collectors.toMap(KpiTreeInfo::getEmployeeId, KpiTreeInfo::getStatusId, (x1, x2) -> x1, LinkedHashMap::new));
        }
        return null;
    }

    public KpiCheckBox getSelectAll() {
        return selectAll;
    }

    private void openAllTree(boolean open) {
        for (int i = 0; i < cellTree.getRootTreeNode().getChildCount(); i++) {
            cellTree.getRootTreeNode().setChildOpen(i, open);
        }
    }

    public LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getItems() {
        return items;
    }

    public void setItems(LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> items) {
        this.items = items;
        drawTree();
    }

    public void addItems(LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> items/*, boolean withOldItems*/) {
        this.items = items;
        drawAddedTree();
    }

    private void viewShowItems() {
        if (selectedItems != null) {
            selectedDataGrid.supplyProvider(selectedItems);
            selectedDataGrid.refresh();
        }
    }

    public void clear() {
        setItems(new LinkedHashMap<>());
    }

    public void refreshDataGrid() {
        selectedDataGrid.refresh();
    }

    public void setSelectAllText(String text) {
        selectAll.setText(text);
    }

    public void setSearchBackgraundText(String text) {
        searchTextBox.setText(text);
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public void setLimitErrorMessage(String limitErrorMessage) {
        this.limitErrorMessage = limitErrorMessage;
    }

    public void setSearchDefaultText(String searchDefaultText) {
        this.searchDefaultText = searchDefaultText;
        searchTextBox.setText(searchDefaultText);
        selectAll.setText(searchDefaultText);
    }

    public void showTreePanel(boolean show) {
        treePanel.removeFromParent();
    }

    public void setOpen(final boolean open) {
        this.open = open;
    }
}
