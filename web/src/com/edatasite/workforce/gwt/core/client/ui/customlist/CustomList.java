package com.edatasite.workforce.gwt.core.client.ui.customlist;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;

/**
 * User: Ruslan Muhammadov
 * Date: Mar 27, 2010
 * Time: 4:05:29 PM
 */
public class CustomList extends Composite {

    public static class Style {

        private String name;

        public Style(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static final Style BLUE_HEADER = new Style("blue-header");
    public static final Style WHITE_HEADER = new Style("white-header");
    public static final Style LIST_BORDER = new Style("bknd-hostNameList");

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    @UiTemplate("com.edatasite.workforce.gwt.core.client.ui.customlist.CustomList.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, CustomList> {
    }

    @UiField
    HTMLPanel panel;

    private final int headerHeight = 25;

    private Label header;
    private VerticalPanel mainPanel = new VerticalPanel();
    private HorizontalPanel headerPanel = new HorizontalPanel();
    private Div topPanel = new Div();
    private ScrollPanel footer = new ScrollPanel();
    protected VerticalPanel listPanel = new VerticalPanel();

    protected ArrayList<CustomListItem> items = new ArrayList<>();

    private String headerText;
    private Design design;
    private Style borderCurrentStyle;

    protected TextBox searchBox;
    private String searchText;
    private boolean showSearchPanel;

    public CustomList() {
        this(null, Design.NONE, false);
    }

    public CustomList(Design design) {
        this(null, design, false);
    }

    public CustomList(Design design, boolean showSearchPanel) {
        this(null, design, showSearchPanel);
    }

    public CustomList(String headerText, Design design) {
        this(headerText, design, false);
    }

    public CustomList(String headerText) {
        this(headerText, Design.NONE, false);
    }

    public CustomList(String headerText, Design design, boolean showSearchPanel) {
        this.headerText = headerText;
        this.design = design;
        this.showSearchPanel = showSearchPanel;

        build();
    }

    public void add(CustomListItem item) {
        insert(getItemCount(), item);
    }

    public void add(SelectItem item) {
        insert(getItemCount(), item);
    }

    public boolean contains(CustomListItem item) {
        return items.contains(item);
    }

    public boolean contains(SelectItem item) {
        for (int i = 0; i < getItemCount(); i++) {
            CustomListItem listItem = getItem(i);
            if (listItem.getItem().getId().equals(item.getId()) && listItem.getItem().getName().equals(item.getName())) {
                return true;
            }
        }

        return false;
    }

    public Design getDesign() {
        return design;
    }

    public CustomListItem getItem(int index) {
        return items.get(index);
    }

    public int getItemCount() {
        return items.size();
    }

    public ArrayList<CustomListItem> getItems() {
        return items;
    }

    public void insert(int index, CustomListItem item) {
        item.setList(this);

        items.add(index, item);
        listPanel.insert(item, index);
    }

    public void insert(int index, SelectItem item) {
        insert(index, new CustomListItem(item));
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void removeBorderCurrentStyle() {
        mainPanel.removeStyleName(borderCurrentStyle.getName());
    }

    public void removeBorderStyle(Style style) {
        mainPanel.removeStyleName(style.getName());
    }

    public void removeHeaderStyle(Style style) {
        header.removeStyleName(style.getName());
    }

    public void removeItem(CustomListItem item) {
        listPanel.remove(item);
        items.remove(item);
    }

    public void removeItems() {
        listPanel.clear();
        items.clear();
    }

    public void setCheckAllItems(boolean check) {
        setCheckAllItems(check, true);
    }

    public void setCheckAllItems(boolean check, boolean fireEvents) {
        if (design == Design.CHECK) {
            for (CustomListItem item : items) {
                item.setCheck(check, fireEvents);
            }
        }
    }

    /**
     * If desing (type) is checkbox, method will return
     * a count of checked items. Otherwise, return 0
     *
     * @return - item count
     */
    public int getCheckedItemCount() {
        int i = 0;
        if (design == Design.CHECK) {
            for (CustomListItem item : items) {
                if (item.getValue()) {
                    i++;
                }
            }
        }
        return i;
    }

    public void setBorders(boolean show) {
        if (show) {
            mainPanel.addStyleName(LIST_BORDER.getName());
        } else {
            mainPanel.removeStyleName(LIST_BORDER.getName());
        }
    }

    public void setBorderStyle(Style style) {
        borderCurrentStyle = style;

        mainPanel.setStyleName(style.getName());
    }

    public void setHeader(String headerText) {
        this.headerText = headerText;

        if (header == null) {
            initializeHeader();
        } else {
            header.setText(headerText);
        }
    }

    public void setHeaderStyle(Style style) {
        headerPanel.setStyleName(style.getName());
    }

    public void setHeight(int height) {
        if (height < headerHeight) {
            height = headerHeight;
        }

        footer.setHeight((height - headerHeight) + "px");
    }

    @Override
    public void setHeight(String height) {
        setHeight(Integer.parseInt(height.split("px")[0]));
    }

    public void setSearchText(String text) {
        searchBox.setText(searchText = text);
    }

    @Override
    public void setSize(String width, String height) {
        setWidth(width);
        setHeight(height);
    }

    public void setWidth(int width) {
        footer.setWidth(width + "px");
    }

    @Override
    public void setWidth(String width) {
        footer.setWidth(width);
        if (showSearchPanel) {
            searchBox.setWidth((Integer.parseInt(width.split("px")[0]) - 27) + "px");
        }
    }

    public void setMainPanelWidth(String width) {
        panel.setWidth(width);
    }

    private void build() {
        MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
        initWidget(uiBinder.createAndBindUi(this));
        addStyleName("wg_lang-selec file--CustomList");
        setBorderStyle(LIST_BORDER);

        if (showSearchPanel) {
            buildSearchPanel();
        }

        if (headerText != null) {
            initializeHeader();
        }

        mainPanel.addStyleName("bknd-hostNameList__mainPanel");

        footer.add(listPanel);
        mainPanel.add(topPanel);
        mainPanel.add(footer);
        panel.add(mainPanel);
    }

    private void buildSearchPanel() {
        searchBox = new TextBox();
        searchBox.ensureDebugId("Categories_searchbox");
        searchBox.setText(searchText = "Search Employees");
        searchBox.setStyleName("search-textbox form-control");
        searchBox.ensureDebugId("");
        searchBox.getElement().getStyle().setMarginBottom(3, com.google.gwt.dom.client.Style.Unit.PX);
        searchBox.addFocusHandler(event -> {

            String text = ((TextBox) event.getSource()).getText();
            if (searchText.equals(text)) {
                searchBox.setText("");
            }
        });
        searchBox.addBlurHandler(event -> {
            String text = ((TextBox) event.getSource()).getText();
            if ("".equals(text)) {
                searchBox.setText(searchText);
            }
        });
        searchBox.addKeyUpHandler(event -> {
            String text = ((TextBox) event.getSource()).getText();
            if (text.equals("")) {
                refresh();
            } else {
                filterEmployees(text);
            }
        });

        Span xCancel = new Span();
        xCancel.addStyleName("btn--icon");
        xCancel.add(new SvgIcon(SvgEnum.xBold));
        xCancel.addClickHandler(e -> {
            searchBox.setText(searchText);
            searchBox.addStyleName("search-textbox form-control");
            refresh();
        });

        MaterialPanel searchPanel = new MaterialPanel("wg_lang-select__search");
        searchPanel.add(searchBox);
        searchPanel.add(xCancel);

        panel.add(searchPanel);
    }

    private boolean contains(String full, String searched) {
        return full.toLowerCase().contains(searched.toLowerCase());
    }

    private void filterEmployees(String text) {
        for (int i = 0; i < getItemCount(); i++) {
            CustomListItem item = items.get(i);
            item.setVisible(contains(item.getItem().getName(), text));
        }
    }

    private void initializeHeader() {
        header = new Label(headerText);
        headerPanel.setSpacing(3);
        headerPanel.setSize("100%", headerHeight + "px");
        headerPanel.add(header);
        headerPanel.setCellVerticalAlignment(header, HasAlignment.ALIGN_MIDDLE);

        setHeaderStyle(BLUE_HEADER);

        mainPanel.add(headerPanel);
    }

    private void refresh() {
        for (int i = 0; i < getItemCount(); i++) {
            CustomListItem item = items.get(i);
            item.setVisible(true);
        }
    }

    public void refreshEnabled() {
        for (int i = 0; i < getItemCount(); i++) {
            CustomListItem item = items.get(i);
            item.setEnabled(true);
        }
    }

    public void setEnabledItem(SelectItem item, boolean enabledItem, boolean check) {
        for (int i = 0; i < getItemCount(); i++) {
            CustomListItem customListItem = items.get(i);
            if (customListItem.getItem().getId().equals(item.getId())) {
                customListItem.setCheck(check);
                customListItem.setEnabled(enabledItem);
            }
        }
    }

    public ArrayList<SelectItem> getSelectItems(){
        ArrayList<SelectItem> selectItems = new ArrayList<>();
        for (int i = 0; i < getItemCount(); i++) {
            CustomListItem customListItem = items.get(i);
            if (customListItem.getValue()) {
                selectItems.add(customListItem.getItem());
            }
        }
        return selectItems;
    }

    public void setCheckedItem(SelectItem item, boolean check) {
        for (int i = 0; i < getItemCount(); i++) {
            CustomListItem customListItem = items.get(i);
            if (customListItem.getItem().getId().equals(item.getId())) {
                customListItem.setCheck(check);
            }
        }
    }

    public Div getTopPanel() {
        return topPanel;
    }
}