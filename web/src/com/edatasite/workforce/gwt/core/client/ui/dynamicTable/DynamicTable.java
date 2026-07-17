package com.edatasite.workforce.gwt.core.client.ui.dynamicTable;

import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.headers.AbstractHeader;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.headers.Header;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.recources.DynamicTableResources;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

/**
 * AbstractDynamicTable class implementation.
 */
public class DynamicTable extends AbstractDynamicTable {

    private static final String DEFAULT_BORDER = "dynamictable-border";
    private static final String DEFAULT_HEADER = "dynamictable-header";
    private static final String DEFAULT_STYLE = "dynamictable";
    private static final String DEFAULT_STYLE_FULL_WIDTH = "dynamictable--full-width";

    private String borderStyle = null;
    private String headerStyle = null;

    //dynamic table recource files
    private DynamicTableResources resources = GWT.create(DynamicTableResources.class);

    /**
     * Simply creates table with plus-minus panel at the left.
     * Row deletes automaticaly after minis clicled.
     * After plus button clicked table simply call AddListener plusClicked() function.
     * You should implement add row logic by yourself.
     *
     * @param dynamicTableColumn
     */
    public DynamicTable(DynamicTableColumn[] dynamicTableColumn) {
        super(dynamicTableColumn);
        setStylePrimaryName(DEFAULT_STYLE);
        addStyleName(DEFAULT_STYLE_FULL_WIDTH);
    }


    /**
     * You can define show or not button panel at the left.
     * If false - button panel will not be shown.
     *
     * @param dynamicTableColumn
     */
    public DynamicTable(DynamicTableColumn[] dynamicTableColumn, boolean showButtons, boolean withoutDefaultStyle) {
        super(dynamicTableColumn, showButtons);
        if (!withoutDefaultStyle) {
            setStylePrimaryName(DEFAULT_STYLE);
            addStyleName(DEFAULT_STYLE_FULL_WIDTH);
        }
    }

    public DynamicTable(DynamicTableColumn[] dynamicTableColumn, boolean showButtons) {
        super(dynamicTableColumn, showButtons);
        setStylePrimaryName(DEFAULT_STYLE);
        addStyleName(DEFAULT_STYLE_FULL_WIDTH);
    }

    public DynamicTable(DynamicTableColumn[] dynamicTableColumn, boolean showButtons, boolean withoutDefaultStyle, boolean isAdmin) {
        super(dynamicTableColumn, showButtons, isAdmin);
        if (!withoutDefaultStyle) {
            setStylePrimaryName(DEFAULT_STYLE);
            addStyleName(DEFAULT_STYLE_FULL_WIDTH);
        }
    }

    public DynamicTable() {
        super();
        setStylePrimaryName(DEFAULT_STYLE);
        addStyleName(DEFAULT_STYLE_FULL_WIDTH);
    }


    /**
     * This custom constructor is for difine your own header and border styles.
     * You can see example of style at the top of getBorderStyle() and getHeader() functions.
     *
     * @param dynamicTableColumn
     * @param headerStyle
     * @param borderStyle
     */
    public DynamicTable(DynamicTableColumn[] dynamicTableColumn, String headerStyle, String borderStyle) {
        super();
        this.headerStyle = headerStyle;
        this.borderStyle = borderStyle;
        setShowButtons(true);
        drawHeaderRow(dynamicTableColumn);

        setStylePrimaryName(DEFAULT_STYLE);
        addStyleName(DEFAULT_STYLE_FULL_WIDTH);
    }


    /**
     * Costum constructor with ability to define header and border styles.
     * Also you can define show or not button panel at the left.
     * If showButtons parameter is false, then button panel will not be shown.
     *
     * @param dynamicTableColumn
     * @param headerStyle
     * @param borderStyle
     * @param showButtons
     */
    public DynamicTable(DynamicTableColumn[] dynamicTableColumn, String headerStyle, String borderStyle, boolean showButtons) {
        this(dynamicTableColumn, headerStyle, borderStyle, showButtons, null);
    }

    public DynamicTable(DynamicTableColumn[] dynamicTableColumn, String headerStyle, String borderStyle, boolean showButtons, String buttonsWidth) {
        super();
        this.headerStyle = headerStyle;
        this.borderStyle = borderStyle;
        setShowButtons(showButtons);
//        if (buttonsWidth != null && !"".equals(buttonsWidth)) {
//            setButtonsWidth(buttonsWidth);
//        }
        drawHeaderRow(dynamicTableColumn);

        setStylePrimaryName(DEFAULT_STYLE);
        addStyleName(DEFAULT_STYLE_FULL_WIDTH);
    }


    /**
     * You can define your own header style.
     * Header should be child of AbstractHeader class.
     * <p/>
     * By default header style:
     * <p/>
     * .blue-header{
     * background:url(BlueHeader/top2.gif);
     * }
     *
     * @return
     */
    protected AbstractHeader getHeader() {

        //if defined header style is null, then user default style.
        if (headerStyle == null) {
            return new Header(DEFAULT_HEADER);
        }
        return new Header(headerStyle);
    }


    /**
     * Borders style should be close to header style, so you should define this style also.
     * <p/>
     * By default border style:
     * <p/>
     * .blue-border{
     * border:1px solid #99bbe8;
     * border-collapse:collapse;
     * background-color:white;
     * }
     * <p/>
     * .blue-border th{
     * border-color:#99bbe8;
     * }
     * <p/>
     * .blue-border td{
     * border-color:#99bbe8;
     * }
     *
     * @return
     */
    public String getBorderStyle() {

        //if defined border style is null, then user default style.
        if (borderStyle == null) {
            return DEFAULT_BORDER;
        }
        return borderStyle;
    }

    public void setHeaderTextToCenter(Integer startColumnIntex, Style.TextAlign alignment) {
        int cellCount = getCellCount(0);
        for (int i = startColumnIntex; i < cellCount; i++) {
            getCellFormatter().getElement(0, i).getStyle().setTextAlign(alignment);
        }
    }

    public void horizontalAlign(int rowID, int column, HasHorizontalAlignment.HorizontalAlignmentConstant hAlign) {
        super.getFlexCellFormatter().setAlignment(rowID, column, hAlign, HasVerticalAlignment.ALIGN_MIDDLE);
    }
}
