package com.edatasite.workforce.gwt.core.client.ui.listpanel.savepanel;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.edatasite.workforce.gwt.core.client.WordUtils;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ColumnColor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HTML;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialSwitch;
import gwt.material.design.client.ui.html.Div;

import java.util.List;

public class ColumnPanelWidget extends MaterialPanel {

    private CustomColumnDefinitionConfig columnConfig;
    private final MaterialPanel pnlGrip;
    private MaterialSwitch switcher;
    private final HTML columnTitle;
    private Command onSwitch;
    private final MaterialPanel dragTile;
    private final ColumnColorSettings colorSettings;

    public ColumnPanelWidget(CustomColumnDefinitionConfig columnConfig, boolean enabled) {
        super();
        this.columnConfig = columnConfig;
        dragTile = new MaterialPanel("drag-tile" + (enabled ? " state-on" : " state-off"));
        switcher = new MaterialSwitch();
        switcher.setValue(enabled);

        WfmButton2 colorSettingButton = new WfmButton2("<i class='icon-colors'></i>", " ");
        colorSettingButton.addStyleName("btn--icon");

        pnlGrip = new MaterialPanel("drag-tile__grip");

        columnTitle = new HTML(WordUtils.capitalizeFirst(!"".equals(columnConfig.getColumnName()) ? (String) columnConfig.getColumnName() : columnConfig.getCodeName()));
        columnTitle.setStyleName("drag-tile__text");
        switcher.addValueChangeHandler(vh -> {

            if (switcher.getValue()) {
                onSwitchEnabled();
            } else {
                onSwitchDisabled();
            }
            if (onSwitch != null) {
                onSwitch.execute();
            }
        });
        Div actionsWrapper = new Div("drag-tile__actions");
        actionsWrapper.add(colorSettingButton);
        actionsWrapper.add(switcher);
        dragTile.add(pnlGrip);
        dragTile.add(columnTitle);
        dragTile.add(actionsWrapper);
        dragTile.setLayoutData(switcher);
        add(dragTile);
        colorSettings = new ColumnColorSettings();
        add(colorSettings);
        colorSettingButton.addClickHandler(event -> {
            ColumnColorSettings.handlePopup(colorSettings);
        });
    }

    public ColumnPanelWidget(SelectItem item, List<SelectItem> selectItems) {
        super();
        dragTile = new MaterialPanel("drag-tile state-on");


        WfmButton2 colorSettingButton = new WfmButton2("<i class='icon-colors'></i>", " ");
        colorSettingButton.addStyleName("btn--icon");

        pnlGrip = new MaterialPanel("drag-tile__grip");

        columnTitle = new HTML(WordUtils.capitalizeFirst(item.getName()));
        columnTitle.setStyleName("drag-tile__text");
        Div actionsWrapper = new Div("drag-tile__actions");
        actionsWrapper.add(colorSettingButton);
        dragTile.add(pnlGrip);
        dragTile.add(columnTitle);
        dragTile.add(actionsWrapper);
        add(dragTile);
        colorSettings = new ColumnColorSettings(true);
        add(colorSettings);
        colorSettingButton.addClickHandler(event -> {
            ColumnColorSettings.handlePopup(colorSettings);
        });
    }

    public void closeColorPanel() {
        colorSettings.activate(false);
    }

    public void setColorData(List<ColumnColor> data) {
        colorSettings.setData(data);
    }

    public List<ColumnColor> getColorData() {
        return colorSettings.getData();
    }

    private void onSwitchEnabled() {
        dragTile.removeStyleName("state-off");
        dragTile.addStyleName("state-on");
    }

    private void onSwitchDisabled() {
        dragTile.removeStyleName("state-on");
        dragTile.addStyleName("state-off");
    }

    public void setOnSwitch(Command onSwitch) {
        this.onSwitch = onSwitch;
    }

    public String getColumnTitleText() {
        return columnTitle.getText();
    }

    public void makeDraggable(PickupDragController showColumnDragController) {
        showColumnDragController.makeDraggable(ColumnPanelWidget.this, pnlGrip);
    }

    public void setSwitcherValue(Boolean value) {
        switcher.setValue(value);
        if (value != null && value) {
            onSwitchEnabled();
        } else {
            onSwitchDisabled();
        }
    }

    public boolean getSwitcherValue() {
        return switcher.getValue();
    }

    public CustomColumnDefinitionConfig getColumnConfig() {
        return columnConfig;
    }

}