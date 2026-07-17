package com.edatasite.workforce.gwt.contact.client.ui;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.Key;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 2012.04.14
 * Time: 18:17
 */
public abstract class AbstractDataGrid<T> extends DataGrid {

    public static final WfmStrings wfmStrings = WfmStrings.App.get();

    private ListDataProvider<T> dataProvider;
    public static final int GRID_LIMIT = 20;

    /**
     * Constructs a table with a default page size of 50, and the given
     * {@link com.google.gwt.view.client.ProvidesKey key provider}.
     *
     * @param keyProvider an instance of ProvidesKey<T>, or null if the record
     *                    object should act as its own key
     */
    public AbstractDataGrid() {
        super(item -> ((Key) item).getKey());
        dataProvider = new ListDataProvider<>();
        this.setWidth("100%");
        this.setHeight("100%");
        addStyleName("cellBasedWidget-mod cellBasedWidget-attachment cellBasedWidget-mod--static-body box-radius--top");
        this.getElement().getStyle().setOverflow(com.google.gwt.dom.client.Style.Overflow.AUTO);
        if (getElement().getLastChild().getNodeType() == Node.ELEMENT_NODE) {
            ((Element) getElement().getLastChild()).addClassName("h-auto");
        }
    }

    public AbstractDataGrid(int pageSize) {
        super(pageSize, item -> ((Key) item).getKey());
        dataProvider = new ListDataProvider<>();
        this.setWidth("100%");
        this.setHeight("100%");
        addStyleName("cellBasedWidget-mod cellBasedWidget-attachment cellBasedWidget-mod--static-body box-radius--top");
        this.getElement().getStyle().setOverflow(com.google.gwt.dom.client.Style.Overflow.AUTO);
        if (getElement().getLastChild().getNodeType() == Node.ELEMENT_NODE) {
            ((Element) getElement().getLastChild()).addClassName("h-auto");
        }
    }

    protected void initialize() {
        addColums();
        addDataDisplay(this);
        refresher();
    }

    protected abstract void addColums();

    private void addDataDisplay(HasData<T> display) {
        dataProvider.addDataDisplay(display);
    }

    public void reDrawItems() {
        dataProvider.refresh();
    }

    public List<T> getList() {
        return dataProvider.getList();
    }

    public void supplyProvider(T[] listItems) {
        List<T> listItem = getList();
        listItem.clear();
        Collections.addAll(listItem, listItems);
    }

    public abstract void refresher();

    protected String formatDate(Date date, boolean withTime) {
        if (date == null) {
            return "";
        }
        if (withTime) {
            return DateUtils.formatInternal(date);
        }
        return DateUtils.format(date);
    }

    protected boolean isNotNull(Object object) {
        return object != null;
    }
}
