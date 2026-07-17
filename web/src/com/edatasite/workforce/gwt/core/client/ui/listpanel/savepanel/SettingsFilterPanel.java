package com.edatasite.workforce.gwt.core.client.ui.listpanel.savepanel;


import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import gwt.material.design.client.constants.Position;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Label;
import gwt.material.design.client.ui.html.Span;

public class SettingsFilterPanel extends Composite {
    interface SettingsFilterPanelUiBinder extends UiBinder<HTMLPanel, SettingsFilterPanel> {
    }

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private static SettingsFilterPanelUiBinder ourUiBinder = GWT.create(SettingsFilterPanelUiBinder.class);
    @UiField
    Label pageSizeLabel;
    @UiField
    DataListBox pageSizeList;
    @UiField
    Label columnsLabel;
    @UiField
    DataListBox columnsList;
    @UiField
    Div sortingTypes;
    @UiField
    Span titleLabel;

    private WfmButton2 btnAsc;
    private WfmButton2 btnDesc;

    public static final String ASCENDING = "asc";
    public static final String DESCENDING = "desc";

    private String sortType = ASCENDING;


    private int pageSize;
    public SettingsFilterPanel(String sortType, int pageSize) {
        this.sortType = sortType;
        this.pageSize = pageSize;
        initWidget(ourUiBinder.createAndBindUi(this));

        initialize();
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    public String getSortType() {
        return sortType;
    }

    private Integer getPageSize() {
        return pageSizeList.getSelectedId() != null ? pageSizeList.getSelectedId() : 20;
    }


    public void addColumnLisItem(SelectItem item) {
        columnsList.addListItem(item);
    }

    public boolean isAnyColumnSelected() {
        return columnsList != null && columnsList.isSomethingSelected();
    }

    public Integer getColumnItemCount() {
        return columnsList.getItemCount();
    }

    public SelectItem[] getColumnItems() {
        return columnsList.getItems();
    }

    public void removeColumnListItem(SelectItem item) {
        columnsList.removeListItem(item);
    }

    public SelectItem getSelectedColumnItem() {
        return columnsList.getSelectedItem();
    }

    public void setSelectedColumn(SelectItem item) {
        columnsList.setSelected(item);
    }

    public void setColumnChangeEvent(Command changeEvent) {
        columnsList.setChangeEvent(() -> {
            if (isAnyColumnSelected()) {
                // If a column is selected, show sorting buttons
                // Если выбран столбец, показать кнопки сортировки
                btnAsc.setVisible(true);
                btnDesc.setVisible(true);
            } else {
                //  Otherwise, hide sorting buttons
                // В противном случае скрыть кнопки сортировки
                btnAsc.setVisible(false);
                btnDesc.setVisible(false);
            }
            // Execute the user-provided change event if not null
            // Вызов переданного пользователем события изменения
            if (changeEvent != null) {
                changeEvent.execute();
            }
        });
    }

    public DataListBox getPageSizeListBox() {
        return pageSizeList;
    }


    public void setSortType(String sortType) {
        this.sortType = sortType;

        //clear all
        btnAsc.removeStyleName("active");
        btnDesc.removeStyleName("active");

        if (ASCENDING.equals(sortType)) {
            btnAsc.addStyleName("active");
        } else {
            btnDesc.addStyleName("active");
        }
    }

    private void initialize() {
        //page size
        pageSizeLabel.setText(wfmStrings.pageSize());
        pageSizeList.setWithoutNullLabel(true);
        pageSizeList.setItems(getPageSizeList());
        pageSizeList.setSelected(pageSize);

        //sort by column
        columnsLabel.setText(wfmStrings.sortBy());

        //sorting columns
        btnAsc = new WfmButton2("","btn btn--icon btn--white active", "ficon--order-accending");
        btnAsc.removeStyleName("hasicon--left");
        btnDesc = new WfmButton2("","btn btn--icon btn--white", "ficon--order-descending");
        btnDesc.removeStyleName("hasicon--left");

        btnAsc.addClickHandler(ch -> {
            setSortType(ASCENDING);
        });
        btnDesc.addClickHandler(ch -> {
            setSortType(DESCENDING);
        });

        btnAsc.setVisible(false);
        btnDesc.setVisible(false);

//        if (DESCENDING.equals(sortType)) {
//            sortType = DESCENDING;
//            btnDesc.addStyleName("active");
//        } else {
//            btnAsc.addStyleName("active");
//        }
        new KpiToolTip(btnAsc, wfmStrings.ascending(), Position.TOP);
        new KpiToolTip(btnDesc, wfmStrings.descending(), Position.TOP);
        sortingTypes.add(btnAsc);
        sortingTypes.add(btnDesc);
    }


    private SelectItem[] getPageSizeList() {
        SelectItem[] items = new SelectItem[5];

        items[0] = new SelectItem(10, "10");
        items[1] = new SelectItem(20, "20");
        items[2] = new SelectItem(30, "30");
        items[3] = new SelectItem(40, "40");
        items[4] = new SelectItem(50, "50");
//        items[5] = new SelectItem(100, "100");
//        items[6] = new SelectItem(150, "150");
//        items[7] = new SelectItem(200, "200");

        return items;
    }



}