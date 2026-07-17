package com.edatasite.workforce.gwt.accounting.client.ui.wfmJournal;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 23.02.2009
 * Time: 19:36:44
 * To change this template use File | Settings | File Templates.
 */
public class JournalTableColumn {

    private final int minWidth = 20;
    private final int maxWidth = 500;

    private String id;
    private String text;
    private Widget wText;
    private String textType;
    private int width = minWidth;
    private double widthPercentage;

    public static final String TYPE_STRING = "STRING";

    public static final String TYPE_WIDGET = "WIDGET";

    private HasHorizontalAlignment.HorizontalAlignmentConstant horizontalAlignment;

    /**
     * Creates a new column instance.
     *
     * @param id   the column id
     * @param text the column text
     */
    public JournalTableColumn(String id, String text) {
        this(id, text, 0);
    }

    public JournalTableColumn(String id, Widget wText) {

    }

    /**
     * Creates a new column instance.
     *
     * @param id    the column id
     * @param width the column width
     */
    public JournalTableColumn(String id, int width) {
        this(id, id, width);
    }

    /**
     * Creates a new column instance.
     *
     * @param id    the column id
     * @param text  the column text
     * @param width the column width
     */
    public JournalTableColumn(String id, String text, int width) {
        this.id = id;
        this.text = text;
        this.width = width;
        textType = TYPE_STRING;
    }

    public JournalTableColumn(String id, Widget wText, int width) {
        this.id = id;
        this.wText = wText;
        this.width = width;
        textType = TYPE_WIDGET;
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
    public Object getText() {
        if (getTextType() == TYPE_WIDGET)
            return wText;
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

    /**
     * get the column's TextType: Widget or String
     *
     * @return
     */
    public String getTextType() {
        return textType;
    }


    public HasHorizontalAlignment.HorizontalAlignmentConstant getHorizontalAlignment() {
        return horizontalAlignment;
    }

    public void setHorizontalAlignment(HasHorizontalAlignment.HorizontalAlignmentConstant horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }

    public double getWidthPercentage() {
        return widthPercentage;
    }

    public void setWidthPercentage(double widthPercentage) {
        this.widthPercentage = widthPercentage;
    }
}
