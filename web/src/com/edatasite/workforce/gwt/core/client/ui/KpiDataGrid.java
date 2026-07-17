package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.HeaderPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Abdullo
 * Date: 05.04.12
 * Time: 18:17
 */
public class KpiDataGrid<T> extends DataGrid {

    private ListDataProvider<T> dataProvider;
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();

    /**
     * Constructs a table with a default page size of 50, and the given
     * {@link com.google.gwt.view.client.ProvidesKey key provider}.
     *
     * @param keyProvider an instance of ProvidesKey<T>, or null if the record
     *                    object should act as its own key
     */
    public KpiDataGrid(ProvidesKey keyProvider) {
        super(10000, keyProvider);
        dataProvider = new ListDataProvider<>();
        addStyleName("cellBasedWidget-mod cellBasedWidget-attachment box-radius--top cellBasedWidget-mod--static-body cellBasedWidget-mod--cell-not-overflow");
        addDataDisplay(this);
        this.getTableBodyElement().getParentElement().addClassName("dataTable cellBasedWidgetTable-body");
        this.getTableHeadElement().getParentElement().addClassName("headerTable cellBasedWidgetTable-header");
    }

    public KpiDataGrid(ProvidesKey keyProvider, boolean needHeight) {
        this(keyProvider);
        if (needHeight) {
            this.getScrollPanel().getElement().getParentElement().setPropertyString("style", "height:100%");
        }
    }


    public void addDataDisplay(HasData<T> display) {
        dataProvider.addDataDisplay(display);
    }

    public void refresh() {
        dataProvider.refresh();
    }

    public List<T> getList() {
        return dataProvider.getList();
    }

    public void supplyProvider(T[] listItems) {
        if (listItems != null) {
            List<T> listItem = getList();
            listItem.clear();
            Collections.addAll(listItem, listItems);
        }
    }

    public void appendProvider(T[] listItems) {
        if (listItems != null) {
            List<T> listItem = getList();
            Collections.addAll(listItem, listItems);
        }
    }

    public void supplyProvider(ArrayList<T> listItems) {
        List<T> listItem = getList();
        listItem.clear();
        listItem.addAll(listItems);
    }

    public void supplyProvider(List<T> listItems) {
        List<T> listItem = getList();
        listItem.clear();
        listItem.addAll(listItems);
    }

    public String refactor(String s) {
        return !Utils.isNullOrEmpty(s) ? s : "";
    }

    public ScrollPanel getScrollPanel() {
        HeaderPanel header = (HeaderPanel) getWidget();
        return (ScrollPanel) header.getContentWidget();
    }

    @Override
    public void setEmptyTableWidget(Widget widget) {
        super.setEmptyTableWidget(widget);
        if (widget != null && widget.getParent() != null)
            widget.getParent().addStyleName("cellBasedWidget-mod__message-table");

    }
}
