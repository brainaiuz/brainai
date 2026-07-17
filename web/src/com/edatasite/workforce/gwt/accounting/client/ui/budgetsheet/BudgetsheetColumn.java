package com.edatasite.workforce.gwt.accounting.client.ui.budgetsheet;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 16.03.2009
 * Time: 19:30:08
 * To change this template use File | Settings | File Templates.
 */
public class BudgetsheetColumn {

    private final int minWidth = 20;
    private final int maxWidth = 500;

    private String id;
    private String text;
    private int width = minWidth;
    private String style;

    private Object data;

    /**
     * Creates a new column instance.
     *
     * @param id    the column id
     * @param width the column width
     */
    public BudgetsheetColumn(String id, int width, String style) {
        this(id, id, width, style);
    }

    /**
     * Creates a new column instance.
     *
     * @param id    the column id
     * @param text  the column text
     * @param width the column width
     */
    public BudgetsheetColumn(String id, String text, int width, String style) {
        this.id = id;
        this.text = text;
        this.width = width;
        this.style = style;
    }

    /**
     * Returns the column's id.
     *
     * @return the id
     */
    public String getID() {
        return id;
    }

    /**
     * Returns the column's text.
     *
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Returns the column's minimum width.
     *
     * @return the min width
     */
    public int getMinWidth() {
        return minWidth;
    }

    /**
     * Returns the column's maximum width.
     *
     * @return the max width
     */
    public int getMaxWidth() {
        return maxWidth;
    }

    /**
     * Returns the column's width.
     *
     * @return
     */
    public int getWidth() {
        return width;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getStyle() {
        return style;
    }
}
