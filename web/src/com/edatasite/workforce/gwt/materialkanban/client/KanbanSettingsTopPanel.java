package com.edatasite.workforce.gwt.materialkanban.client;


import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import gwt.material.design.client.ui.html.Label;
import gwt.material.design.client.ui.html.Span;

public class KanbanSettingsTopPanel extends Composite {
    interface SettingsFilterPanelUiBinder extends UiBinder<HTMLPanel, KanbanSettingsTopPanel> {
    }

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private static SettingsFilterPanelUiBinder ourUiBinder = GWT.create(SettingsFilterPanelUiBinder.class);
    @UiField
    Label pageSizeLabel;
    @UiField
    DataListBox pageSizeList;
    @UiField
    Span titleLabel;

    private Integer pageSize;

    public KanbanSettingsTopPanel(Integer pageSize) {
        this.pageSize = pageSize;

        initWidget(ourUiBinder.createAndBindUi(this));

        initialize();
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    public Integer getPageSize() {
        return pageSizeList.getSelectedId() != null ? pageSizeList.getSelectedId() : 10;
    }

    public DataListBox getPageSizeListBox() {
        return pageSizeList;
    }


    private void initialize() {
        //page size
        pageSizeLabel.setText(wfmStrings.pageSize());
        pageSizeLabel.addStyleName("KanbanSettingsTopPanel");
        pageSizeList.setWithoutNullLabel(true);
        pageSizeList.setItems(getPageSizeList());
        pageSizeList.setSelected(pageSize);
    }


    private SelectItem[] getPageSizeList() {
        SelectItem[] items = new SelectItem[2];

//        items[0] = new SelectItem(5, "5");
        items[0] = new SelectItem(10, "10");
        items[1] = new SelectItem(15, "15");
//        items[2] = new SelectItem(20, "20");
        /*items[3] = new SelectItem(30, "30");
        items[4] = new SelectItem(40, "40");
        items[5] = new SelectItem(50, "50");
        items[6] = new SelectItem(100, "100");
        items[7] = new SelectItem(150, "150");
        items[8] = new SelectItem(200, "200");*/

        return items;
    }



}