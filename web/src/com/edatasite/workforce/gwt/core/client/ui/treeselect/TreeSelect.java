package com.edatasite.workforce.gwt.core.client.ui.treeselect;


import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.ui.FlexPanel;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.ProjectEmployeesAvailabilityCheck;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.filterparams.SuperPuperHandler;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialPanel;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Nov 26, 2009
 * Time: 5:42:01 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * This class provides tree and leaf. Only after opening of the tree, it loads all data
 * and draws its leaves depending on the information that localted in the base.
 */
public class TreeSelect extends Composite {
    private SuperPuperHandler<NTreeSelectItem> handler;

    interface MyUiBinder extends UiBinder<Widget, TreeSelect> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    private static final WfmStrings wfmStrings = WfmStrings.App.get();


    @UiField
    HTMLPanel panel;

    private static final KpiCheckBox tickAll = new KpiCheckBox(wfmStrings.selectAll());
    private final int searchPanelHeight = 20;
    private final WfmTreeItem draftItem = new WfmTreeItem(00, "...");
    private final TreeSelectIcons icons = GWT.create(TreeSelectIcons.class);
    

    private Tree tree;
    private FlexPanel treePanel;
    private TextBox searchBox;
    private String searchText;
    private boolean expanded;
    private boolean withSearchBox;
    private boolean loadAll = false;

    private TreeSelectCallback callback;
    private LinkedList<NTreeSelectItem> checkedItems;
    private ProjectEmployeesAvailabilityCheck onlyAvailableEmployeesHandler;
    private ProjectEmployeesAvailabilityCheck allEmployeesHandler;
    private ProjectEmployeesAvailabilityCheck eventStartAndEndDateHandler;
    private RadioButton availableEmployees;
    private RadioButton allEmployees;
    private HorizontalPanel radioButtons;
    private HorizontalPanel searchPanel;

    public void setLoadAll(boolean loadAll) {
        this.loadAll = loadAll;
    }

    /**
     * Constructor that puts its default tree images.
     */
    public TreeSelect() {
        this(null, true);
    }

    public void setAllEmployeesRadioButtonChecked() {
        allEmployees.setValue(true);
    }

    public boolean isAllEmployeesRadioButtonChecked() {
        return allEmployees.getValue();
    }

    public TreeSelect(boolean withSearchBox) {
        this(null, withSearchBox);
    }

    public TreeSelect setHandler(SuperPuperHandler<NTreeSelectItem> handler) {
        this.handler = handler;
        return this;
    }

    /**
     * Constructors that allows to put custom images to the tree and leaves.
     */
    public TreeSelect(Tree.Resources resources, boolean withSearchBox) {
        this.withSearchBox = withSearchBox;

        initialize(resources, false);
    }

    public TreeSelect(boolean showTreeDefaultImages, boolean withSearchBox) {
        this.withSearchBox = withSearchBox;

        initialize(null, showTreeDefaultImages);
    }

    public void setEmployeeAvailabilityCheckHandler(ProjectEmployeesAvailabilityCheck handler) {
        this.onlyAvailableEmployeesHandler = handler;
    }

    public void setAllEmployeesHandler(ProjectEmployeesAvailabilityCheck handler) {
        this.allEmployeesHandler = handler;
    }

    public void setEventStartAndEndDateHandler(ProjectEmployeesAvailabilityCheck eventStartAndEndDateHandler) {
        this.eventStartAndEndDateHandler = eventStartAndEndDateHandler;
    }

    public FlexPanel getPanel() {
        return this.treePanel;
    }

    public boolean isAvailableEmployeesRadioButtonChecked() {
        return availableEmployees.getValue();
    }

    /**
     * Initalization of local variables.
     */
    private void initialize(Tree.Resources resources, boolean showTreeDefaultImages) {
        if (showTreeDefaultImages) {
            tree = new Tree();
        } else {
            if (resources == null) {
                resources = getDefaultTreeImageResources();
            }
            tree = new Tree(resources, false);
        }
        checkedItems = new LinkedList<>();
        tree.addOpenHandler(event -> {
            TreeItem item = event.getTarget();
            if (item != null && item.getState()) {
                NTreeSelectItem selectItem = (NTreeSelectItem) item;
                if (!selectItem.isChildPopulated()) {
                    addChildren(selectItem);
                }
            }
        });

        treePanel = new FlexPanel();
        treePanel.setWidth("100%");
        treePanel.setScrollEnabled(true);
        treePanel.getElement().getStyle().setZIndex(1);
        treePanel.getElement().setClassName("treePanel-class");
        treePanel.add(tickAll);
//        tickAll.getElement().getStyle().setMarginTop(5, Style.Unit.PX); //https://prnt.sc/r1rjf5
//        tickAll.getElement().getStyle().setMarginRight(15, Style.Unit.PX);
//        tickAll.getElement().getStyle().setMarginBottom(5, Style.Unit.PX);
//        tickAll.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        treePanel.add(tree);
        tickAll.addClickHandler(event -> {
            if (tickAll.getValue()) {
                tickAll(NTreeSelectItem.CHECK);
            } else {
                tickAll(NTreeSelectItem.UNCHECK);
            }
        });

        initWidget(uiBinder.createAndBindUi(this));

        if (withSearchBox) {
            buildSearchPanel();
        }

        panel.add(treePanel);
    }

    /**
     * Either adds a child to the item through changing state or only changes its state.
     *
     * @param currentParentItem
     */
    private void addChildren(final NTreeSelectItem currentParentItem) {
        if (getTreeCallback() != null) {
            LoadingPanel.loading(true);

            getTreeCallback().addChildren(currentParentItem, () -> {
                if (currentParentItem.getChildCount() > 1) {
                    currentParentItem.removeItem(currentParentItem.getChild(0));
                    currentParentItem.setChildPopulated(true);
                }
                if (currentParentItem.getOpenChildrenCommand() != null) {
                    currentParentItem.getOpenChildrenCommand().execute();
                }
                LoadingPanel.loading(false);
            });
        }
    }

    private void tickAll(int type) {
        for (int i = 0; i < tree.getItemCount(); i++) {
            final NTreeSelectItem treeSelectItem = (NTreeSelectItem) tree.getItem(i);
            treeSelectItem.getItem().setType(type);
            treeSelectItem.setChecked(getTickAll().getValue());
            treeSelectItem.fireCommand();
//            handler.onFire(treeSelectItem);
        }
        boolean isTick = false;
        if (type == NTreeSelectItem.CHECK) {
            isTick = true;
        } else if (type == NTreeSelectItem.UNCHECK) {
            isTick = false;
        }
        for (KpiCheckBox checkBox : NTreeSelectItem.getParentItems()) {
            checkBox.setValue(isTick);
        }
    }

    private void buildSearchPanel() {
        searchText = wfmStrings.searchEmployee();

        searchBox = new TextBox();
        searchBox.getElement().setId("treeSelect_searchBox");
        searchBox.setText(searchText);
        searchBox.setStyleName("gwt-TextBox form-control search-textbox");
        final boolean[] a = {true};
        searchBox.addFocusListener(new FocusListener() {
            public void onFocus(Widget sender) {
                String text = ((TextBox) sender).getText();
                if (searchText.equals(text)) {
                    searchBox.setText("");
                }
                if (a[0] && loadAll) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMPLOYE_TREE_WIDGET_REFRESH, text, TreeSelect.this);
                }
                a[0] = false;
            }

            public void onLostFocus(Widget sender) {
                String text = ((TextBox) sender).getText();
                if ("".equals(text)) {
                    searchBox.setText(searchText);
                }
            }
        });
        searchBox.addKeyUpHandler(event -> {
            final String text = ((TextBox) event.getSource()).getText();
            if (text.equals("")) {
                refresh();
            } else {
                filterEmployeeList(text);
            }
        });

//        Icon clearIcon = new Icon();
        WfmButton2 clearIcon = new WfmButton2("", WfmButton2.BTN_WHITE);

        clearIcon.add(new SvgIcon(SvgEnum.x));
        clearIcon.addClickHandler(event -> {
            searchBox.setText(searchText);
            refresh();
        });

        MaterialPanel expandCollapseButton = new MaterialPanel("selectPanelWidget__expandCollapse");
        expandCollapseButton.addClickHandler(event -> {
            if (!expanded) {
                expandAll();
            } else {
                collapseAll();
            }
        });

        String text;
        text = wfmStrings.employees();
        allEmployees = new KpiRadioButton("radioButtons", wfmStrings.all() + " " + text);
        allEmployees.setValue(true);
        availableEmployees = new KpiRadioButton("radioButtons", wfmStrings.onlyAvailable());

        allEmployees.addClickHandler(event -> {
            if (allEmployeesHandler != null) {
                setTickAllVisible(false);
                allEmployeesHandler.onAllRadioButtonClick();
            }
        });

        availableEmployees.addClickHandler(event -> {
            if (eventStartAndEndDateHandler != null) {
                setTickAllVisible(false);
                eventStartAndEndDateHandler.onOnlyAvailableClickedSetStartAndEndDate();
            }
            if (onlyAvailableEmployeesHandler != null) {
                setTickAllVisible(false);
                onlyAvailableEmployeesHandler.onOnlyAvailableRadioButtonClick();
            }
        });

        radioButtons = new HorizontalPanel();
        radioButtons.getElement().getStyle().setPaddingTop(5, Style.Unit.PX);
        radioButtons.getElement().getStyle().setPaddingLeft(5, Style.Unit.PX);
        searchPanel = new HorizontalPanel();
        searchPanel.setWidth("100%");
        searchPanel.setSpacing(2);
        radioButtons.add(allEmployees);
        radioButtons.add(availableEmployees);
        MaterialPanel searchDiv = new MaterialPanel("selectPanelWidget__search");
        searchDiv.add(searchBox);
        searchPanel.add(new InputGroup(searchBox, clearIcon));
        searchPanel.add(expandCollapseButton);
        expandCollapseButton.getParent().getElement().getElementsByTagName("td").getItem(1).getStyle().setWidth(50, Style.Unit.PX);
        expandCollapseButton.getParent().setHeight("20px");

        panel.add(radioButtons);
        panel.add(searchPanel);
    }

    private void filterEmployeeList(String text) {
        for (int i = 0; i < tree.getItemCount(); i++) {
            boolean visible = false;
            final TreeItem rootItem = tree.getItem(i);
            for (int j = 0; j < rootItem.getChildCount(); j++) {
                final TreeItem childItem = rootItem.getChild(j);
                if (contains(childItem.getText().toLowerCase(), text)) {
                    if (!rootItem.getState()) {
                        rootItem.setState(true);
                    }
                    childItem.setVisible(visible = true);
                } else {
                    childItem.setVisible(false);
                }
            }
            rootItem.setVisible(visible);
        }
    }

    private boolean contains(String full, String searched) {
        return full.contains(searched.toLowerCase());
    }

    private void refresh() {
        for (int i = 0; i < tree.getItemCount(); i++) {
            final TreeItem rootItem = tree.getItem(i);
            for (int j = 0; j < rootItem.getChildCount(); j++) {
                final TreeItem childItem = rootItem.getChild(j);
                childItem.setVisible(true);
            }

            rootItem.setVisible(true);
            rootItem.setState(false);
        }
    }

    /**
     * Adds mouse handlers to label in order to apply or
     * remove the certain style according to mouse's action.
     *
     * @param label
     * @param mouseOverStyle
     * @param mouseOutStyle
     */
    private void fireMouseEvents(Label label, final String mouseOverStyle, final String mouseOutStyle) {
        label.addMouseOverHandler(event -> {
            final Label sender = (Label) event.getSource();
            sender.setStyleName(mouseOverStyle);
        });

        label.addMouseOutHandler(event -> {
            final Label sender = (Label) event.getSource();
            sender.setStyleName(mouseOutStyle);
        });
    }

    /**
     * Returns the images in case, there is not allowed to put custom images.
     *
     * @return
     */
    private Tree.Resources getDefaultTreeImageResources() {
        return new Tree.Resources() {
            public ImageResource treeClosed() {
                return icons.getTreeClosed();
            }

            public ImageResource treeLeaf() {
                return icons.user();
            }

            public ImageResource treeOpen() {
                return icons.getTreeOpen();
            }
        };
    }

    /**
     * Sets the custom search text. By default its value is 'Search Employees'.
     *
     * @param text
     */
    public void setSearchText(String text) {
        searchBox.setText(searchText = text);
    }

    /**
     * Sets the width to the search box.
     *
     * @param width
     */
    public void setSearchBoxWidth(String width) {
        searchBox.setWidth(width);
    }

    /**
     * Sets the height to the search box.
     *
     * @param height
     */
    public void setSearchBoxHeight(String height) {
        searchBox.setHeight(height);
    }

    /**
     * Adds an item as a parent item to the tree that each of them contains single child item.
     *
     * @param parent
     */
    public void add(WfmTreeItem parent) {
        add(parent, false);
    }

    /**
     * Adds an item as a parent item to the tree that each of them contains single child item.
     *
     * @param parent              - parent
     * @param customChildPopulate - custom child populate
     */
    public void add(WfmTreeItem parent, boolean customChildPopulate) {
        NTreeSelectItem rootItem = new NTreeSelectItem(parent, this, tree, false, customChildPopulate);
        rootItem.setSuperPuperHandler(handler);
        rootItem.addItem(draftItem);//If tree has not leaves, its image becomes as the leaf's.

        tree.addItem(rootItem);
    }

    /**
     * Adds a parent item and loads all children of that item.
     *
     * @param parent
     * @param children
     */
    public void add(WfmTreeItem parent, LinkedList<WfmTreeItem> children) {
        NTreeSelectItem rootItem = null;
        for (int i = 0; i < tree.getItemCount(); i++) {
            NTreeSelectItem p = (NTreeSelectItem) tree.getItem(i);
            if (parent.getId().equals(p.getItem().getId())) {
                rootItem = p;
                rootItem.addItems(children);
                break;
            }
        }
        if (rootItem == null) {
            rootItem = new NTreeSelectItem(parent, this, tree, false);
            rootItem.setSuperPuperHandler(handler);
            rootItem.addItems(children);
            tree.addItem(rootItem);
        }
    }

    public void clearTree() {
        tree.removeItems();
        checkedItems.clear();
    }


    /**
     * This method lets to the tree to load its children during opening it.
     * This method supports lazy loading of tree items.
     *
     * @param callback
     */
    public void setTreeCallback(TreeSelectCallback callback) {
        this.callback = callback;
    }

    /**
     * Adds selection handler to the tree and allows to fire when its item selected.
     *
     * @param handler
     */
    public void addSelectionHandler(SelectionHandler<TreeItem> handler) {
        tree.addSelectionHandler(handler);
    }

    /**
     * Expands all loaded items of tree.
     */
    public void expandAll() {
        expanded = true;
        expandOrCollapse(true);
    }

    /**
     * Collapses all loaded items of tree.
     */
    public void collapseAll() {
        expanded = false;
        expandOrCollapse(false);
    }

    /**
     * Expands or collapses items of current tree.
     *
     * @param state - state of the tree.
     */
    private void expandOrCollapse(boolean state) {
        for (int i = 0; i < tree.getItemCount(); i++) {
            tree.getItem(i).setState(state);
        }
    }

    /**
     * Forcibly puts checkboxes to all tree's child items.
     *
     * @param checked
     */
    public void checkAllItems(boolean checked) {
        for (int parent = 0; parent < tree.getItemCount(); parent++) {
            final TreeItem parentItem = tree.getItem(parent);
            for (int child = 0; child < parentItem.getChildCount(); child++) {
                ((NTreeSelectItem) parentItem.getChild(child)).setChecked(checked);
            }
        }
    }

    /**
     * Returns checked items with their id and name as a WfmTreeItem.
     *
     * @return
     */
    public WfmTreeItem[] getCheckedItems() {
        final WfmTreeItem[] items = new WfmTreeItem[checkedItems.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = checkedItems.get(i).getItem();
        }
        return items;
    }

    /**
     * Returns the array of id of checked items.
     *
     * @return
     */
    public Integer[] getCheckedItemsID() {
        Integer[] ids = new Integer[checkedItems.size()];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = checkedItems.get(i).getItem().getId();
        }
        return ids;
    }

    /**
     * Adds checked item to the list and replaces unchecked image to checked one.
     *
     * @param item
     */
    protected void addCheckedItem(NTreeSelectItem item) {
        if (!checkedItems.contains(item) && item.getChildCount() == 0) {
            checkedItems.add(item);
        }
    }

    /**
     * Removes checked item from the list and replaces checked image to unchecked one.
     *
     * @param item
     */
    protected void removeCheckedItem(NTreeSelectItem item) {
        if (checkedItems.contains(item)) {
            checkedItems.remove(item);
        }
        tickAll.setValue(false);
    }

    /**
     * Returns TreeCallback to call it in the item class.
     *
     * @return
     */
    protected TreeSelectCallback getTreeCallback() {
        return callback;
    }

    /**
     * Gives full access to controll the tree.
     *
     * @return
     */
    public Tree getTree() {
        return tree;
    }


    public void setWidth(int width) {
        panel.setWidth(width + "px");
    }


    public void setHeight(int height) {
        panel.setHeight(height + searchPanelHeight + 4 + "px");
        treePanel.setHeight(height + "px");

    }

    @Override
    public void setSize(String width, String height) {
        panel.setSize(width, height);
    }

    public void hideAvailablityCheckBox() {
        radioButtons.removeFromParent();
    }

    public HorizontalPanel getRadioButtons() {
        return radioButtons;
    }

    public void setRadioButtons(HorizontalPanel radioButtons) {
        this.radioButtons = radioButtons;
    }

    public HorizontalPanel getSearchPanel() {
        return searchPanel;
    }

    public KpiCheckBox getTickAll() {
        return tickAll;
    }

    public static void setTickAllVisible(final boolean visible) {
        tickAll.getElement().getStyle().setDisplay(visible ? Style.Display.BLOCK : Style.Display.NONE);
        tickAll.setValue(false);

    }
}
