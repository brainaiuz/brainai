package com.edatasite.workforce.gwt.invoice.client.ui.view.itemtablesettings;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.WordUtils;
import com.edatasite.workforce.gwt.core.client.localization.WfmConstantsWithLookup;
import com.edatasite.workforce.gwt.core.client.rpc.ICommand;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingService;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingsItem;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialSwitch;
import gwt.material.design.client.ui.html.Div;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Normurod on 3/13/2017.
 */
public class ColumnSettigns extends KpiSideNavBox {

    private final WfmConstantsWithLookup coreConstantsWithLookUp = WfmConstantsWithLookup.App.get();


    CSInterface csTemplate = GWT.create(CSInterface.class);

    private HashMap<String, ColumnConfigs> columnsMap;

    private VerticalPanel showVerticalPanel;
    private PickupDragController showColumnDragController;
    private Div container;

    private WfmButton2 btnApply, btnCancel;
    private ICommand commandProvider;
    private String uuid;

    private ColumnConfigs[] allColumns;

    public ColumnSettigns(ItemTableEnum section) {
        addHeader(new InlineHTML(csTemplate.settingsTitle(coreConstantsWithLookUp.getString(section.name()))));
        initilazation();
    }

    public void setItemTableSettings(ItemTableSettingsItem settingsItem) {
        this.allColumns = settingsItem.getAllColumns();
        this.columnsMap = new HashMap<>();

        container.clear();
        initColumns();
    }

    private void initilazation() {
        container = new Div();
        addBody(container);

        btnApply = new WfmButton2(wfmStrings.applyChanges(), WfmButton2.BTN_PRIMARY);
        btnApply.addClickHandler(clickEvent -> {

            if (commandProvider != null) {
                commandProvider.execute(getUuid());
            }
            hide();
        });

        btnCancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);
        btnCancel.addClickHandler(ch -> hide());

        initColumns();

        addFooter(btnApply);
        addFooter(btnCancel);
    }

    public ColumnConfigs[] getConfiguredColumns() {
        LinkedList<ColumnConfigs> list = new LinkedList<>();

        for (int i = 0; i < showVerticalPanel.getWidgetCount(); i++) {
            MaterialPanel columnContainer = (MaterialPanel) showVerticalPanel.getWidget(i);
            MaterialSwitch switcher = (MaterialSwitch) columnContainer.getLayoutData();

            if (switcher.getValue()) {
                list.add(columnsMap.get(switcher.getLayoutData()));
            }
        }

        return list.toArray(new ColumnConfigs[]{});
    }

    private void initColumns() {
        // dragable container
        AbsolutePanel boundaryPanel = new AbsolutePanel();

        // initialize vertical panel to hold our columns
        showVerticalPanel = new VerticalPanel();

        boundaryPanel.add(showVerticalPanel);
        container.add(boundaryPanel);

        // initialize our column drag controller
        showColumnDragController = new PickupDragController(boundaryPanel, false);
        showColumnDragController.setBehaviorMultipleSelection(false);

        // initialize our column drop controller
        VerticalPanelDropController columnDropController = new VerticalPanelDropController(showVerticalPanel);
        showColumnDragController.registerDropController(columnDropController);

        if (allColumns != null && allColumns.length > 0) {
            for (ColumnConfigs column : allColumns) {
                addNewColumn(column);
                columnsMap.put(column.getCode(), column);
            }
        }
    }

    private void addNewColumn(ColumnConfigs columnConfig) {
        MaterialSwitch switcher = new MaterialSwitch();
        switcher.setLayoutData(columnConfig.getCode());
        switcher.setValue(columnConfig.isSelected());
        switcher.setEnabled(!columnConfig.isRequired());

        MaterialPanel pnlColumn = new MaterialPanel("drag-tile" + (switcher.getValue() ? " state-on" : " state-off"));
        MaterialPanel pnlGrip = new MaterialPanel("drag-tile__grip");

        HTML columnTitle = new HTML(WordUtils.capitalizeFirst(!Utils.isNullOrEmpty(columnConfig.getTitle()) ? columnConfig.getTitle() : columnConfig.getCode()));
        columnTitle.setStyleName("drag-tile__text");

        Div pnlAction = new Div("drag-tile__actions");
        pnlAction.add(switcher);

        switcher.addValueChangeHandler(vh -> {

            if (switcher.getValue()) {

                pnlColumn.removeStyleName("state-off");
                pnlColumn.addStyleName("state-on");

            } else {
                pnlColumn.removeStyleName("state-on");
                pnlColumn.addStyleName("state-off");
            }
        });


        pnlColumn.add(pnlGrip);
        pnlColumn.add(columnTitle);
        pnlColumn.add(pnlAction);
        pnlColumn.setLayoutData(switcher);

        showVerticalPanel.add(pnlColumn);
        showColumnDragController.makeDraggable(pnlColumn, pnlGrip);
    }

    public void setCommandProvider(ICommand command) {
        this.commandProvider = command;
    }

    public void show(String uuid) {
        setUuid(uuid);
        show();
        if (uuid != null || uuid.length() > 0) {
            container.clear();
            ItemTableSettingService.App.get().getTableSettingsColumnConfigs(ItemTableEnum.CUSTOM_FORM, uuid, new AsyncCallback<ItemTableSettingsItem>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(ItemTableSettingsItem settingsItem) {
                    setItemTableSettings(settingsItem);
                }
            });
        }
    }

    interface CSInterface extends SafeHtmlTemplates {
        @Template("<h1 class=\"hasicon--left\"><span>{0}</span></h1>")
        SafeHtml settingsTitle(String title);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
