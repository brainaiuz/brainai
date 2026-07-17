package com.edatasite.workforce.gwt.hrms.client;

import com.allen_sauer.gwt.dnd.client.DragHandlerAdapter;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeViewModel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.TreeSelectIcons;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Faxriddin on 2/9/2016.
 */
public class EmployeeDocumentDragView {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public interface CellTreeResources extends ClientBundle {
        @CssResource.NotStrict
        @Source ("cellMenuTree.css")
        CssResource cellTree();
    }

    public static CellTreeResources resource = GWT.create(CellTreeResources.class);

    private final TreeSelectIcons icons = GWT.create(TreeSelectIcons.class);

    private FlowPanel assignPanel;
    private FlowPanel verticalPanel;
    private VerticalPanel verticalDragPanel;
    private HTMLPanel itemsPanel;
    private KpiCheckBox selectAll;
    private FlowPanel selectedOption;
    private TextBox searchTextBox;
    private AbsolutePanel boundaryPanel;
    private PickupDragController columnDragController;
    protected HorizontalPanel columnCompositePanelN1;
    private MultiSelectionModel<KpiTreeInfo> selectionModel;
    private KpiTreeViewModel treeViewModel;
    private LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> items;
    private LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> searchItems;
    private ArrayList<KpiTreeInfo> selectedItems;
    private CellTree cellTree;
    private Div scrollPanel;


    public EmployeeDocumentDragView() {
    }

    protected Widget onInitialize() {
        resource.cellTree().ensureInjected();
        LoadingPanel.loading(true);
        assignPanel = new FlowPanel();
        assignPanel.addStyleName("addAssignees wg_employeeDocumentDrag");
        FlowPanel v1 = getVerticalPanel();
        v1.addStyleName("selectedLabel left");
        v1.setHeight("250px");
        getSelectedItemsTitle();
        getDragonDropPanel();
        itemsPanel = new HTMLPanel("");
        itemsPanel.addStyleName("wg_lang-select");
        selectedOption = new FlowPanel();
        selectedOption.addStyleName("wg_lang-select__search");
        searchBox();
        createSelectionModule();
        /*FlowPanel action = new FlowPanel();
        action.addStyleName("sect");
        selectAll = new CheckBox("All");
        selectAll.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            public void onValueChange(ValueChangeEvent<Boolean> booleanValueChangeEvent) {
                if (items != null) {
                    for (KpiTreeInfo key : items.keySet()) {
                        selectionModel.setSelected(key, booleanValueChangeEvent.getValue());
                        for (KpiTreeInfo item : items.get(key)) {
                            selectionModel.setSelected(item, booleanValueChangeEvent.getValue());
                            item.setSelected(booleanValueChangeEvent.getValue());
                        }
                    }
                }
            }
        });
        action.add(selectAll);
        selectedOption.add(action);*/
        itemsPanel.add(selectedOption);

        assignPanel.add(v1);
        assignPanel.add(itemsPanel);
        return assignPanel;
    }

    private void searchBox() {
        searchTextBox = new TextBox();
        searchTextBox.getElement().getStyle().setDisplay(Style.Display.INLINE);
        searchTextBox.setTitle(wfmStrings.search() + " " + wfmStrings.file());
        searchTextBox.setText(wfmStrings.search() + " " + wfmStrings.file());
        searchTextBox.addStyleName("search-textbox");
        searchTextBox.addFocusHandler(event -> {
            if ((wfmStrings.search() + " " + wfmStrings.file()).trim().equals(searchTextBox.getText().trim())) {
                searchTextBox.setText("");
                searchTextBox.removeStyleName("search-textbox");
            }
        });
        searchTextBox.addBlurHandler(event -> {
            if ("".equals(searchTextBox.getText())) {
                searchTextBox.setText(wfmStrings.search() + " " + wfmStrings.file());
                searchTextBox.addStyleName("search-textbox");
            }
        });
        searchItems = new LinkedHashMap<>();
        searchTextBox.addKeyUpHandler(event -> {
            searchItems.clear();
            for (KpiTreeInfo key : items.keySet()) {
                for (KpiTreeInfo item : items.get(key)) {
                    if (item.getName().toLowerCase().contains(searchTextBox.getText().toLowerCase())) {
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

        Icon clearImage = new Icon();
        clearImage.setStyleName(WfmButton2.ICON_CANCEL);
        clearImage.addClickHandler(sender -> {
            searchTextBox.setText(wfmStrings.search() + " " + wfmStrings.file());
            searchTextBox.addStyleName("search-textbox");
            searchTree(items);
        });

        selectedOption.add(searchTextBox);
        selectedOption.add(clearImage);
    }

    private void searchTree(LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> treeItems) {
        if (cellTree != null) {
            scrollPanel.removeFromParent();
            initializeTree(treeItems);
        }
    }

    private void getSelectedItemsTitle() {
        getColumnCompositePanel("dragTableHeader");
        FlexTable titleTable = new FlexTable();
        titleTable.setStyleName("table");
        titleTable.setWidth("100%");
        titleTable.setWidget(0, 0, new HTML(wfmStrings.name()));
        titleTable.getFlexCellFormatter().setWidth(0, 0, "80%");
        titleTable.setWidget(0, 1, new HTML(wfmStrings.action()));
        titleTable.getFlexCellFormatter().setWidth(0, 1, "14%");
        titleTable.getRowFormatter().setStyleName(0, "thead");
        columnCompositePanelN1.add(titleTable);
        columnCompositePanelN1.setWidth("100%");
        verticalPanel.add(columnCompositePanelN1);
    }

    private FlowPanel getVerticalPanel() {
        verticalPanel = new FlowPanel();
        return verticalPanel;
    }

    private void getDragonDropPanel() {
        getVerticalDragPanel();
        getBoundaryPanel();
        getPickupController();
    }

    public VerticalPanel getVerticalDragPanel() {
        verticalDragPanel = new VerticalPanel();
        verticalDragPanel.addStyleName("dragPanel");
        verticalDragPanel.setSpacing(0);
        return verticalDragPanel;
    }

    public void getBoundaryPanel() {
        boundaryPanel = new AbsolutePanel();
        boundaryPanel.add(verticalDragPanel);
        verticalPanel.add(boundaryPanel);
    }

    protected PickupDragController getPickupController() {
        columnDragController = new PickupDragController(boundaryPanel, false);
        columnDragController.setBehaviorMultipleSelection(false);
        VerticalPanelDropController columnDropController = new VerticalPanelDropController(verticalDragPanel);
        columnDragController.registerDropController(columnDropController);
        columnDragController.addDragHandler(new DragHandlerAdapter() {
            @Override
            public void onDragEnd(com.allen_sauer.gwt.dnd.client.DragEndEvent event) {
                changeItemOrder();

            }

        });
        return columnDragController;
    }

    private void changeItemOrder() {
        for (KpiTreeInfo treeInfo : selectedItems) {
            for (int i = 0; i < verticalDragPanel.getWidgetCount(); i++) {
                if (treeInfo.getId().toString().equals(verticalDragPanel.getWidget(i).getElement().getId())) {
                    treeInfo.setPositionId(i);
                }
            }
        }
    }

    private void getPrefixPanelForDragon(final KpiTreeInfo treeInfo) {
        getColumnCompositePanel("dragTable");
        columnCompositePanelN1.getElement().setId(String.valueOf(treeInfo.getId()));
        columnCompositePanelN1.setWidth("100%");
        HorizontalPanel itemHorizontalPanel = new HorizontalPanel();
        itemHorizontalPanel.setWidth("100%");
        verticalDragPanel.add(columnCompositePanelN1);
        Label itemName = new Label(treeInfo.getLabel());
        columnCompositePanelN1.add(itemHorizontalPanel);
        columnDragController.makeDraggable(columnCompositePanelN1, itemName);
        FlexTable itemRow = new FlexTable();
        itemRow.setWidth("100%");
        final Anchor remove = new Anchor(wfmStrings.delete());
        remove.addClickHandler(clickEvent -> removeFromDrag(String.valueOf(treeInfo.getId())));
        itemRow.setWidget(0, 0, itemName);
        itemRow.getFlexCellFormatter().setWidth(0, 0, "85%");

        itemRow.setWidget(0, 1, remove);
        itemRow.getFlexCellFormatter().setWidth(0, 1, "14%");

        itemHorizontalPanel.add(itemRow);
    }

    private void removeFromDrag(String titleId) {
        for (KpiTreeInfo item : selectedItems) {
            if (titleId.equals(item.getId().toString())) {
                selectionModel.setSelected(item, false);
                item.setSelected(false);
                item.setPositionId(null);
//                selectedItems.remove(item);
            }
        }
        viewShowItems();
    }

    public HorizontalPanel getColumnCompositePanel(String styleName) {
        columnCompositePanelN1 = new HorizontalPanel();
        columnCompositePanelN1.addStyleName(styleName);
        return columnCompositePanelN1;
    }

    public ArrayList<FileResource> getSelectedDocuments() {
        ArrayList<FileResource> selectedPages = new ArrayList<>();
        if (selectedItems != null && selectedItems.size() > 0) {
            KpiTreeInfo[] items = getOrderDrags(selectedItems);
            for (KpiTreeInfo menu : items) {
                FileResource menuItem = new FileResource();
                menuItem.setBodyId(menu.getId());
                menuItem.setName(menu.getLabel());
                menuItem.setDescription(menu.getSkills());
                menuItem.setAmazonLink(menu.getImageUrl());
                selectedPages.add(menuItem);
            }
        }
        return selectedPages;
    }

    public void reloadMenuCellTree(Integer userID, ArrayList<Integer> employeeDocuments) {
        if (userID != null) {
            ListingFilterParameter filterParametrs = new ListingFilterParameter();
            filterParametrs.setFolderType(Constants.F_EMPLOYEE_PROFILE);
            filterParametrs.setEntityID(userID);
            filterParametrs.setCrmEntityId(userID);
            filterParametrs.setModule(LayoutRPC.HRMS_SECTION);
            DocumentsService.App.get().getEmployeeDocumentsWithTreeInfo(filterParametrs, employeeDocuments, new AbstractAsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> result) {
                    setItems(result);
                    LoadingPanel.loading(false);
                }
            });
        }
    }

    public void setItems(LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> items) {
        this.items = items;
        drawTree();
    }

    private void drawTree() {
        if (cellTree != null) {
            createSelectionModule();
            selectedItems = null;
            selectedItems = new ArrayList<>();
            viewShowItems();
            scrollPanel.removeFromParent();
        }
        initializeTree(items);
    }

    private void createSelectionModule() {
        selectionModel = new MultiSelectionModel<>(KpiTreeInfo.KEY_PROVIDER);
        selectionModel.addSelectionChangeHandler(event -> drawSelection());
    }

    private void initializeTree(LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> treeItems) {
        cellTree = null;
        treeViewModel = null;
        treeViewModel = new KpiTreeViewModel(selectionModel, treeItems);
        cellTree = new CellTree(treeViewModel, null);
        cellTree.getElement().getStyle().setOverflow(Style.Overflow.AUTO);
        cellTree.setDefaultNodeSize(100);
        scrollPanel = new Div("scroll-box");
        scrollPanel.add(cellTree);
        itemsPanel.add(scrollPanel);
        openAllTree(true);
    }

    private void drawSelection() {
        selectedItems = null;
        selectedItems = new ArrayList<>();
        for (final KpiTreeInfo value : selectionModel.getSelectedSet()) {
            if (value.getDepartmentId() == null) {
                continue;
            }
            selectedItems.add(value);
        }
        viewShowItems();
    }

    private void openAllTree(boolean open) {
        for (int i = 0; i < cellTree.getRootTreeNode().getChildCount(); i++) {
            cellTree.getRootTreeNode().setChildOpen(i, open);
        }
    }

    private void viewShowItems() {
        verticalDragPanel.clear();
        if (selectedItems != null && selectedItems.size() > 0) {
            KpiTreeInfo[] infos;
            if (selectedItems.size() > 1) {
                infos = getOrderDrags(selectedItems);
            } else {
                infos = new KpiTreeInfo[1];
                infos[0] = selectedItems.get(0);
            }
            for (KpiTreeInfo treeInfo : infos) {
                getPrefixPanelForDragon(treeInfo);
            }
        }
    }

    private KpiTreeInfo[] getOrderDrags(ArrayList<KpiTreeInfo> selectedItems) {
        KpiTreeInfo[] treeInfos = selectedItems.toArray(new KpiTreeInfo[]{});
        int n = treeInfos.length;
        for (int i = 0; i < n - 1; i++) {
            KpiTreeInfo p1 = treeInfos[i];
            for (int j = i + 1; j < n; j++) {
                KpiTreeInfo p2 = treeInfos[j];
                if (((p1.getPositionId() != null && p2.getPositionId() != null) && (p1.getPositionId() > p2.getPositionId())) || (p1.getPositionId() == null && p2.getPositionId() != null)) {
                    treeInfos[i] = p2;
                    treeInfos[j] = p1;
                    p1 = p2;
                }
            }
        }
        return treeInfos;
    }
}
