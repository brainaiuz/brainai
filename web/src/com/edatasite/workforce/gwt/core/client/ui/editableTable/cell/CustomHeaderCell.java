package com.edatasite.workforce.gwt.core.client.ui.editableTable.cell;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import org.gwt.advanced.client.ui.widget.EditableGrid;
import org.gwt.advanced.client.ui.widget.SimpleGrid;
import org.gwt.advanced.client.ui.widget.cell.AbstractCell;
import org.gwt.advanced.client.ui.widget.theme.ThemeImage;
import org.gwt.advanced.client.util.ThemeHelper;

public class CustomHeaderCell extends AbstractCell implements org.gwt.advanced.client.ui.widget.cell.HeaderCell {
    /** one pixel image name */
    public static final String SINGLE_IMAGE = ThemeHelper.getInstance().getFullResourceName("advanced/images/single.gif");
    /** order of sorting image */
    private Image image;
    /** sortable flag */
    private boolean sortable = false;
    /** ascending order flag */
    private boolean ascending = true;
    /** sorted flag */
    private boolean sorted = false;
    /** initialized status flag */
    private boolean initialized;

    /** {@inheritDoc} */
    public void displayActive(boolean active) {
        if (!isInitialized()) {
            prepare(createInactive());
            addListeners(null);
            initialized = true;

            addStyleName("header-cell");

            if (isSortable()) {
                addStyleName("sortable-header");
            } else {
                addStyleName("non-sortable-header");
            }
        }
    }

    /** {@inheritDoc} */
    protected Widget createActive() {
        return createInactive();
    }

    /** {@inheritDoc} */
    @SuppressWarnings({"deprecation"})
    protected Widget createInactive() {
        Label label = getLabel();

        String header = String.valueOf(getValue());
        label.setTextAsHtml(header.length() == 0 ? " " : header);

        return label;
    }

    /** {@inheritDoc} */
    protected void addListeners(Widget widget) {
        if (isSortable()) {
            CustomHeaderCell.SortHandler handler = new CustomHeaderCell.SortHandler();
            getLabel().addClickHandler(handler);
            getImage().addClickHandler(handler);
        }
    }

    /** {@inheritDoc} */
    protected void prepare (Widget widget) {
        FlexTable grid = getGrid();

        if (grid instanceof SimpleGrid) {
            int column = getColumn();

            if (getWidget() != null)
                remove(getWidget());

            add(widget);

            ((SimpleGrid)grid).setHeaderWidget(column, this);
        } else {
            super.prepare(widget);
        }
    }

    /** {@inheritDoc} */
    public void setFocus(boolean focus) {
    }

    /** {@inheritDoc} */
    public Object getNewValue () {
        return getValue();
    }

    /** {@inheritDoc} */
    public void setSortable(boolean sortable) {
        this.sortable = sortable;
    }

    /** {@inheritDoc} */
    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    /** {@inheritDoc} */
    public void setSorted(boolean sorted) {
        this.sorted = sorted;
    }

    /** {@inheritDoc} */
    public boolean isAscending() {
        return ascending;
    }

    /** {@inheritDoc} */
    public boolean isSortable() {
        return sortable;
    }

    /** {@inheritDoc} */
    public boolean isSorted() {
        return sorted;
    }

    /** {@inheritDoc} */
    public void sort() {
        if (!isSortable())
            return;

        FlexTable table = getGrid();
        if (table instanceof EditableGrid)
            ((EditableGrid)table).fireSort(this);

        getImage();
    }

    /**
     * Getter for property 'image'.
     *
     * @return Value for property 'image'.
     */
    protected Image getImage() {
        if (image == null) {
            image = new ThemeImage();
        }

        if (isSorted()) {
            if (isAscending())
                image.setUrl("bullet-up.gif");
            else
                image.setUrl("bullet-down.gif");
        } else
            DOM.setElementAttribute(image.getElement(), "src", SINGLE_IMAGE);

        return image;
    }

    /**
     * Getter for property 'initialized'.
     *
     * @return Value for property 'initialized'.
     */
    protected boolean isInitialized() {
        return initialized;
    }

    /**
     * This handler is invoked on sort event.
     *
     * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
     */
    protected class SortHandler implements ClickHandler {
        /**
         * Starts column sorting.
         *
         * @param event is a source event.
         */
        @Override
        public void onClick(ClickEvent event) {
            sort();
        }
    }
}
