package com.edatasite.workforce.gwt.profile.client.ui.view.pdf;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiTextBox;
import com.edatasite.workforce.gwt.core.client.ui.components.panelStack.PanelStack;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.savepanel.ColorWidget;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.PdfTemplateService;
import com.edatasite.workforce.gwt.profile.client.rpc.PdfTemplateServiceAsync;
import com.edatasite.workforce.gwt.profile.client.rpc.PdfTemplateTableSettingsItem;
import com.edatasite.workforce.gwt.profile.client.rpc.SettingsPdfTemplateItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.ui.MaterialSwitch;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * User: Abror Abdukadirov
 * Date: 29.01.2019 15:13
 */
public class PdfItemTableTab extends Composite {
    interface PdfItemTableTabUiBinder extends UiBinder<HTMLPanel, PdfItemTableTab> {
    }

    private static final PdfItemTableTabUiBinder ourUiBinder = GWT.create(PdfItemTableTabUiBinder.class);
    private static final PdfTemplateServiceAsync pdfTemplateService = PdfTemplateService.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final SettingStrings settingsStrings = SettingStrings.App.get();

    @UiField
    WfmButton2 labelsButton;
    @UiField
    WfmButton2 layoutButton;
    @UiField
    HTMLPanel leftPanel;
    @UiField
    HTMLPanel rightPanel;
    @UiField
    HTMLPanel labelPanel;

    //content widgets
    private KpiSwitcher tableBorderSwitcher;
    private ColorWidget tableBorderColorWidget;
    private KpiTextBox tableHeaderFontSizeBox;
    private KpiCheckBox tableHeaderBackgroundCheckBox;
    private ColorWidget tableHeaderBackgroundColorWidget;
    private ColorWidget tableHeaderColorWidget;
    private KpiSwitcher tableItemRowSwitcher;
    private KpiTextBox tableItemRowFontSizeBox;
    private KpiCheckBox tableItemRowBackgroundCheckBox;
    private ColorWidget tableItemRowBackgroundColorWidget;
    private ColorWidget tableItemRowColorWidget;
    private final Element totalWidthElement = Document.get().createElement("b");
    private int totalWidth;

    private VerticalPanel columnsVerticalPanel;
    private PickupDragController draggableController;
    private boolean hasLayout = false;

    private Integer objectId;
    private final String pdfType;
    private ExtendedCommand generateCommand;

    public PdfItemTableTab(Integer objectId, String pdfType) {
        this.objectId = objectId;
        this.pdfType = pdfType;

        initWidget(ourUiBinder.createAndBindUi(this));
        initialize();
    }

    private void initialize() {
        labelsButton.setText("Labels");
        labelsButton.addStyleName("btn--outline active");
        labelsButton.addClickHandler(event -> {
            if (!this.hasLayout) {
                return;
            }
            labelsButton.addStyleName("active");
            layoutButton.removeStyleName("active");
            getItemTableLabelsSettings(true);
            this.showLabelPanel(true);
            this.hasLayout = false;
        });

        layoutButton.setText("Layout");
        layoutButton.addStyleName("btn--outline");
        layoutButton.addClickHandler(event -> {
            if (this.hasLayout) {
                return;
            }
            layoutButton.addStyleName("active");
            labelsButton.removeStyleName("active");
            getItemTableLayoutSettings(true);
            this.showLabelPanel(false);
            this.hasLayout = true;
        });

        tableBorderSwitcher = new KpiSwitcher();
        tableBorderSwitcher.setValue(false);

        tableBorderColorWidget = new ColorWidget();
        tableBorderColorWidget.setColor("#ced5db");

        tableHeaderFontSizeBox = new KpiTextBox(InputType.NUMBER);
        tableHeaderFontSizeBox.setMaxLength(30);
        tableHeaderFontSizeBox.setWidth("55px");
        tableHeaderFontSizeBox.setText(String.valueOf(7));
        tableHeaderFontSizeBox.addValueChangeHandler(event -> {
            String key = event.getValue();
            if (!"".equals(event.getValue().trim())
                && Integer.valueOf(event.getValue()) > 30) {
                tableHeaderFontSizeBox.setText("30");
            }
        });
        tableHeaderBackgroundCheckBox = new KpiCheckBox();
        tableHeaderBackgroundCheckBox.setText(wfmStrings.backgroundColor());

        tableHeaderBackgroundColorWidget = new ColorWidget();
        tableHeaderColorWidget = new ColorWidget();
        tableHeaderColorWidget.setColor("#000000");

        tableItemRowSwitcher = new KpiSwitcher();
        tableItemRowSwitcher.setValue(true);

        tableItemRowFontSizeBox = new KpiTextBox(InputType.NUMBER);
        tableItemRowFontSizeBox.setMaxLength(30);
        tableItemRowFontSizeBox.setWidth("55px");
        tableItemRowFontSizeBox.setText(String.valueOf(7));
        tableItemRowFontSizeBox.addValueChangeHandler(event -> {
            String key = event.getValue();
            if (!"".equals(event.getValue().trim())
                && Integer.valueOf(event.getValue()) > 30) {
                tableItemRowFontSizeBox.setText("30");
            }
        });
        tableItemRowBackgroundCheckBox = new KpiCheckBox();
        tableItemRowBackgroundCheckBox.setText(wfmStrings.backgroundColor());

        tableItemRowBackgroundColorWidget = new ColorWidget();
        tableItemRowColorWidget = new ColorWidget();
        tableItemRowColorWidget.setColor("#000000");

        PopupPanel tableBorderColorPopup = new PopupPanel(true, true);
        tableBorderColorPopup.add(tableBorderColorWidget);
        tableBorderColorPopup.setWidth("300px");
        PopupPanel tableHeaderBackgroundColorPopup = new PopupPanel(true, true);
        tableHeaderBackgroundColorPopup.add(tableHeaderBackgroundColorWidget);
        tableHeaderBackgroundColorPopup.setWidth("300px");
        PopupPanel tableHeaderColorPopup = new PopupPanel(true, true);
        tableHeaderColorPopup.add(tableHeaderColorWidget);
        tableHeaderColorPopup.setWidth("300px");
        PopupPanel tableItemRowBackgroundColorPopup = new PopupPanel(true, true);
        tableItemRowBackgroundColorPopup.add(tableItemRowBackgroundColorWidget);
        tableItemRowBackgroundColorPopup.setWidth("300px");
        PopupPanel tableItemRowColorPopup = new PopupPanel(true, true);
        tableItemRowColorPopup.add(tableItemRowColorWidget);
        tableItemRowColorPopup.setWidth("300px");

        // first panel
        PanelStack tableBorderPanelStack = new PanelStack();
        tableBorderPanelStack.setHeaderTitle(settingsStrings.tableBorder());
        tableBorderPanelStack.setHeaderWidget(tableBorderSwitcher);
        Div colorPanel = new Div("color-picker");
        if (tableBorderColorWidget.getColor() != null) {
            colorPanel.getElement().getStyle().setBackgroundColor(tableBorderColorWidget.getColor());
        }
        colorPanel.addClickHandler(event -> {
            tableBorderColorPopup.setPopupPosition(colorPanel.getAbsoluteLeft() - 150,
                                                   colorPanel.getAbsoluteTop() + colorPanel.getOffsetHeight() + 5);
            tableBorderColorPopup.show();
        });
        tableBorderColorWidget.setChangeHandler(() -> {
            if (tableBorderColorWidget.getColor() != null) {
                colorPanel.getElement().getStyle().setBackgroundColor(tableBorderColorWidget.getColor());
            }
        });
        tableBorderPanelStack.addBodyRow(wfmStrings.color(), colorPanel);
        leftPanel.add(tableBorderPanelStack);

        PanelStack tableItemRowPanelStack = new PanelStack();
        tableItemRowPanelStack.setHeaderTitle(wfmStrings.itemRow());
        tableItemRowPanelStack.setHeaderWidget(tableItemRowSwitcher);
        tableItemRowPanelStack.addBodyRow(settingsStrings.fontSize(), tableItemRowFontSizeBox);
        Div tableItemBackgroundcolorPanel = new Div("color-picker");
        if (tableItemRowBackgroundColorWidget.getColor() != null) {
            tableItemBackgroundcolorPanel.getElement().getStyle().setBackgroundColor(tableItemRowBackgroundColorWidget.getColor());
        }
        tableItemBackgroundcolorPanel.addClickHandler(event -> {
            tableItemRowBackgroundColorPopup.setPopupPosition(tableItemBackgroundcolorPanel.getAbsoluteLeft() - 150,
                                                              tableItemBackgroundcolorPanel.getAbsoluteTop() + tableItemBackgroundcolorPanel.getOffsetHeight() + 5);
            tableItemRowBackgroundColorPopup.show();
        });
        tableItemRowBackgroundColorWidget.setChangeHandler(() -> {
            if (tableItemRowBackgroundColorWidget.getColor() != null) {
                tableItemBackgroundcolorPanel.getElement().getStyle().setBackgroundColor(tableItemRowBackgroundColorWidget.getColor());
            }
        });
        tableItemRowPanelStack.addBodyRow(tableItemRowBackgroundCheckBox, tableItemBackgroundcolorPanel);
        Div tableItemRowcolorPanel = new Div("color-picker");
        if (tableItemRowColorWidget.getColor() != null) {
            tableItemRowcolorPanel.getElement().getStyle().setBackgroundColor(tableItemRowColorWidget.getColor());
        }
        tableItemRowcolorPanel.addClickHandler(event -> {
            tableItemRowColorPopup.setPopupPosition(tableItemRowcolorPanel.getAbsoluteLeft() - 150,
                                                    tableItemRowcolorPanel.getAbsoluteTop() + tableItemRowcolorPanel.getOffsetHeight() + 5);
            tableItemRowColorPopup.show();
        });
        tableItemRowColorWidget.setChangeHandler(() -> {
            if (tableItemRowColorWidget.getColor() != null) {
                tableItemRowcolorPanel.getElement().getStyle().setBackgroundColor(tableItemRowColorWidget.getColor());
            }
        });
        tableItemRowPanelStack.addBodyRow(settingsStrings.fontColor(), tableItemRowcolorPanel);
        leftPanel.add(tableItemRowPanelStack);

        // second panel
        PanelStack tableHeaderPanelStack = new PanelStack();
        tableHeaderPanelStack.setHeaderTitle(wfmStrings.tableHeader());
        tableHeaderPanelStack.addBodyRow(settingsStrings.fontSize(), tableHeaderFontSizeBox);

        Div tableHeaderBackgroundcolorPanel = new Div("color-picker");
        if (tableHeaderBackgroundColorWidget.getColor() != null) {
            tableHeaderBackgroundcolorPanel.getElement().getStyle().setBackgroundColor(tableHeaderBackgroundColorWidget.getColor());
        }
        tableHeaderBackgroundcolorPanel.addClickHandler(event -> {
            tableHeaderBackgroundColorPopup.setPopupPosition(tableHeaderBackgroundcolorPanel.getAbsoluteLeft() - 150,
                                                             tableHeaderBackgroundcolorPanel.getAbsoluteTop() + tableHeaderBackgroundcolorPanel.getOffsetHeight() + 5);
            tableHeaderBackgroundColorPopup.show();
        });
        tableHeaderBackgroundColorWidget.setChangeHandler(() -> {
            if (tableHeaderBackgroundColorWidget.getColor() != null) {
                tableHeaderBackgroundcolorPanel.getElement().getStyle().setBackgroundColor(tableHeaderBackgroundColorWidget.getColor());
            }
        });
        tableHeaderPanelStack.addBodyRow(tableHeaderBackgroundCheckBox, tableHeaderBackgroundcolorPanel);

        Div tableHeaderColorPanel = new Div("color-picker");
        if (tableHeaderColorWidget.getColor() != null) {
            tableHeaderColorPanel.getElement().getStyle().setBackgroundColor(tableHeaderColorWidget.getColor());
        }
        tableHeaderColorPanel.addClickHandler(event -> {
            tableHeaderColorPopup.setPopupPosition(tableHeaderColorPanel.getAbsoluteLeft() - 150,
                                                   tableHeaderColorPanel.getAbsoluteTop() + tableHeaderColorPanel.getOffsetHeight() + 5);
            tableHeaderColorPopup.show();
        });
        tableHeaderColorWidget.setChangeHandler(() -> {
            if (tableHeaderColorWidget.getColor() != null) {
                tableHeaderColorPanel.getElement().getStyle().setBackgroundColor(tableHeaderColorWidget.getColor());
            }
        });
        tableHeaderPanelStack.addBodyRow(settingsStrings.fontColor(), tableHeaderColorPanel);
        rightPanel.add(tableHeaderPanelStack);

        columnsVerticalPanel = new VerticalPanel();
        AbsolutePanel draggableAbsolutePanel = new AbsolutePanel();
        draggableAbsolutePanel.addStyleName("drag-tiles--bordered");
        draggableAbsolutePanel.add(columnsVerticalPanel);

        draggableController = new PickupDragController(draggableAbsolutePanel, false);
        draggableController.setBehaviorMultipleSelection(false);

        VerticalPanelDropController columnDropController = new VerticalPanelDropController(columnsVerticalPanel);
        draggableController.registerDropController(columnDropController);

        labelPanel.add(draggableAbsolutePanel);

        labelPanel.add(drawTotalWidthPanel());

        this.showLabelPanel(true);
    }

    private Div drawTotalWidthPanel() {
        Div rowPanel = new Div("form-row");
        Div div7 = new Div("col-7");
        rowPanel.add(div7);
        Div div2 = new Div("col-2");
        div2.getElement().appendChild(totalWidthElement);
        rowPanel.add(div2);
        Div div3 = new Div("col-3");
        rowPanel.add(div3);

        return rowPanel;
    }

    public void getItemTableLabelsSettings(boolean generate) {
        if (pdfType == null) {
            return;
        }
        LoadingPanel.loading(true);
        pdfTemplateService.getPdfTableColumns(objectId, pdfType, new AbstractAsyncCallback<ArrayList<PdfTemplateTableSettingsItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ArrayList<PdfTemplateTableSettingsItem> result) {
                LoadingPanel.loading(false);

                if (result != null) {
                    columnsVerticalPanel.clear();
                    draggableController.clearSelection();
                    totalWidth = 0;
                    for (PdfTemplateTableSettingsItem config : result) {
                        addNewColumn(config);
                        if (config.isSelected()) {
                            totalWidth += config.getWidth();
                        }
                    }
                    totalWidthElement.setInnerHTML(totalWidth + "%");
                    if (generate) {
                        generateCommand.execute(null);
                    }
                }
            }
        });
    }

    public void getItemTableLayoutSettings(boolean generate) {
        LoadingPanel.loading(true);
        pdfTemplateService.getSettingsPdfTemplateContentLayout(new AbstractAsyncCallback<SettingsPdfTemplateItem>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(SettingsPdfTemplateItem result) {
                LoadingPanel.loading(false);

                setLayoutData(result);

                if (generate) {
                    generateCommand.execute(null);
                }
            }
        });
    }

    private void addNewColumn(PdfTemplateTableSettingsItem tableConfig) {
        MaterialSwitch switcher = new MaterialSwitch();
        switcher.setLayoutData(tableConfig);
        switcher.setValue(tableConfig.isSelected());

        Div formRowPanel = new Div("form-row" + (switcher.getValue() ? " state-on" : " state-off"));
        Div col7 = new Div("col-7");
        Div col2 = new Div("col-2");
        Div col3 = new Div("col-3");

        KpiTextBox widthTextBox = new KpiTextBox(InputType.NUMBER);
        widthTextBox.setText(tableConfig.getWidth() + "");
        widthTextBox.setPlaceholder("width");
        widthTextBox.setEnabled(switcher.getValue());
        widthTextBox.addValueChangeHandler(event -> {
            widthTextBox.setEnabled(false);
            calculateTotalWidth();
            widthTextBox.setEnabled(true);
        });
        col2.add(widthTextBox);

        KpiTextBox labelTextBox = new KpiTextBox();
        labelTextBox.setText(tableConfig.getColumnTitle());
        labelTextBox.setLayoutData(tableConfig.isCustomField());
        labelTextBox.setPlaceholder("label");
        if (tableConfig.isCustomField()) {
            labelTextBox.setEnabled(false);
        } else {
            labelTextBox.setEnabled(switcher.getValue());
        }
        col3.add(labelTextBox);

        Div pnlColumn = new Div("drag-tile drag-tile--sm");
        Div pnlGrip = new Div("drag-tile__grip");

        HTML columnTitle = new HTML(!Utils.isNullOrEmpty(tableConfig.getColumnDefaultTitle()) ? tableConfig.getColumnDefaultTitle() : tableConfig.getColumnCode());
        columnTitle.setStyleName("drag-tile__text");

        Div pnlAction = new Div("drag-tile__actions");
        pnlAction.add(switcher);

        switcher.addValueChangeHandler(vh -> {
            if (switcher.getValue()) {
                formRowPanel.removeStyleName("state-off");
                formRowPanel.addStyleName("state-on");
                widthTextBox.setEnabled(true);
                if (labelTextBox.getLayoutData() != null && !((Boolean)labelTextBox.getLayoutData())) {
                    labelTextBox.setEnabled(true);
                }
                totalWidth += Integer.valueOf(widthTextBox.getText());
            } else {
                formRowPanel.removeStyleName("state-on");
                formRowPanel.addStyleName("state-off");
                widthTextBox.setEnabled(false);
                labelTextBox.setEnabled(false);
                totalWidth -= Integer.valueOf(widthTextBox.getText());
            }
            totalWidthElement.setInnerHTML(totalWidth + "%");
        });
        pnlColumn.add(pnlGrip);
        pnlColumn.add(columnTitle);
        pnlColumn.add(pnlAction);
        pnlColumn.setLayoutData(switcher);
        col7.add(pnlColumn);

        formRowPanel.add(col7);
        formRowPanel.add(col2);
        formRowPanel.add(col3);

        columnsVerticalPanel.add(formRowPanel);
        draggableController.makeDraggable(formRowPanel, pnlGrip);
    }

    private void setLayoutData(SettingsPdfTemplateItem item) {
        if (item == null || !item.getCustomizedContent()) {
            return;
        }
        tableBorderSwitcher.setValue(item.getTableBorderEnabled());
        if (item.getTableBorderColor() != null) {
            tableBorderColorWidget.setColor(item.getTableBorderColor());
        }
        if (item.getTableHeaderFontSize() != null) {
            tableHeaderFontSizeBox.setText(item.getTableHeaderFontSize());
        }
        tableHeaderBackgroundCheckBox.setValue(item.getTableHeaderBackgroundColorEnabled());
        if (item.getTableHeaderBackgroundColor() != null) {
            tableHeaderBackgroundColorWidget.setColor(item.getTableHeaderBackgroundColor());
        }
        if (item.getTableHeaderFontColor() != null) {
            tableHeaderColorWidget.setColor(item.getTableHeaderFontColor());
        }
        tableItemRowSwitcher.setValue(item.getItemRowEnabled());
        if (item.getItemRowFontSize() != null) {
            tableItemRowFontSizeBox.setText(item.getItemRowFontSize());
        }
        tableItemRowBackgroundCheckBox.setValue(item.getItemRowBackgroundColorEnabled());
        if (item.getItemRowBackgroundColor() != null) {
            tableItemRowBackgroundColorWidget.setColor(item.getItemRowBackgroundColor());
        }
        if (item.getItemRowFontColor() != null) {
            tableItemRowColorWidget.setColor(item.getItemRowFontColor());
        }
    }

    public SettingsPdfTemplateItem getData(SettingsPdfTemplateItem item) {
        if (item == null) {
            item = new SettingsPdfTemplateItem();
        }
        item.setObjectId(objectId);
        item.setPdfType(pdfType);
        item.setTableColumns(getConfiguredColumns());
        item.setTableBorderEnabled(tableBorderSwitcher.getValue());
        item.setTableBorderColor(tableBorderColorWidget.getColor());
        item.setTableHeaderFontSize(tableHeaderFontSizeBox.getText());
        item.setTableHeaderBackgroundColorEnabled(tableHeaderBackgroundCheckBox.getValue());
        item.setTableHeaderBackgroundColor(tableHeaderBackgroundColorWidget.getColor());
        item.setTableHeaderFontColor(tableHeaderColorWidget.getColor());
        item.setItemRowEnabled(tableItemRowSwitcher.getValue());
        item.setItemRowFontSize(tableItemRowFontSizeBox.getText());
        item.setItemRowBackgroundColorEnabled(tableItemRowBackgroundCheckBox.getValue());
        item.setItemRowBackgroundColor(tableItemRowBackgroundColorWidget.getColor());
        item.setItemRowFontColor(tableItemRowColorWidget.getColor());
        return item;
    }

    public LinkedList<PdfTemplateTableSettingsItem> getConfiguredColumns() {
        LinkedList<PdfTemplateTableSettingsItem> list = new LinkedList<>();

        int sorder = 1;
        for (int i = 0; i < columnsVerticalPanel.getWidgetCount(); i++) {
            Div formRowDiv = (Div) columnsVerticalPanel.getWidget(i);
            Div col7Div = (Div) formRowDiv.getWidget(0);
            Div col2Div = (Div) formRowDiv.getWidget(1);
            Div col3Div = (Div) formRowDiv.getWidget(2);
            Div columnContainer = (Div) col7Div.getWidget(0);
            MaterialSwitch switcher = (MaterialSwitch) columnContainer.getLayoutData();

            PdfTemplateTableSettingsItem item = new PdfTemplateTableSettingsItem();
            PdfTemplateTableSettingsItem config = (PdfTemplateTableSettingsItem) switcher.getLayoutData();
            item.setColumnCode(config.getColumnCode());
            item.setSelected(switcher.getValue());
            item.setCustomField(config.isCustomField());
            if (col2Div.getWidgetCount() > 0) {
                int width = 10;
                try {
                    width = Integer.valueOf(((KpiTextBox) col2Div.getWidget(0)).getText());
                } catch (NumberFormatException ignored) {
                }
                item.setWidth(width);
            }
            if (col3Div.getWidgetCount() > 0) {
                KpiTextBox titleBox = (KpiTextBox) col3Div.getWidget(0);
                item.setColumnTitle(titleBox.getText());
                item.setAlignment(config.getAlignment());
            }
            if (!switcher.getValue()) {
                item.setSorder(columnsVerticalPanel.getWidgetCount() + 2);
            } else {
                item.setSorder(sorder);
                sorder++;
            }
            list.add(item);
        }
        return list;
    }

    public void calculateTotalWidth() {
        int tmpTotalWidth = 0;
        for (int i = 0; i < columnsVerticalPanel.getWidgetCount(); i++) {
            Div formRowDiv = (Div) columnsVerticalPanel.getWidget(i);
            Div col7Div = (Div) formRowDiv.getWidget(0);
            Div col2Div = (Div) formRowDiv.getWidget(1);
            Div columnContainer = (Div) col7Div.getWidget(0);
            MaterialSwitch switcher = (MaterialSwitch) columnContainer.getLayoutData();
            if (!switcher.getValue()) {
                continue;
            }
            tmpTotalWidth += Integer.valueOf(((KpiTextBox) col2Div.getWidget(0)).getText());
        }
        totalWidth = tmpTotalWidth;
        totalWidthElement.setInnerHTML(totalWidth + "%");
    }

    public boolean validate() {
        int errors = 0;

        if (!Validation.validateTextBoxRequired(tableItemRowFontSizeBox)) {
            errors++;
        }
        if (!Validation.validateTextBoxRequired(tableHeaderFontSizeBox)) {
            errors++;
        }
        int activeColumns = 0;
        int activeColumnsWidth = 0;
        for (int i = 0; i < columnsVerticalPanel.getWidgetCount(); i++) {
            Div formRowDiv = (Div) columnsVerticalPanel.getWidget(i);
            Div col7Div = (Div) formRowDiv.getWidget(0);
            Div col2Div = (Div) formRowDiv.getWidget(1);
            Div col3Div = (Div) formRowDiv.getWidget(2);
            Div columnContainer = (Div) col7Div.getWidget(0);
            MaterialSwitch switcher = (MaterialSwitch) columnContainer.getLayoutData();
            if (switcher.getValue()) {
                activeColumns++;
            }
            KpiTextBox textBox = (KpiTextBox) col2Div.getWidget(0);
            textBox.removeStyleName("x-form-invalid");
            int width = 0;
            try {
                width = Integer.parseInt(textBox.getText());
            } catch (NumberFormatException ignored) {
            }
            if (switcher.getValue()) {
                activeColumnsWidth += width;
            }
            if (textBox.getText() == null || "".equals(textBox.getText()) || width <= 0) {
                textBox.addStyleName("x-form-invalid");
                errors++;
                break;
            }
            KpiTextBox textBox2 = (KpiTextBox) col3Div.getWidget(0);
            textBox2.removeStyleName("x-form-invalid");
            if (textBox2.getText() == null || "".equals(textBox2.getText())) {
                textBox2.addStyleName("x-form-invalid");
                errors++;
                break;
            }
        }
        if (activeColumns <= 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        if (activeColumnsWidth < 100 || activeColumnsWidth > 100) {
            Info.show("Table width should be 100%", Info.Type.WARNING);
            return false;
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    public void resetWidgets() {
        tableBorderSwitcher.setValue(false);
        tableBorderColorWidget.setColor("#ced5db");
        tableHeaderFontSizeBox.setText("7");
        tableHeaderBackgroundCheckBox.setValue(false);
        tableHeaderBackgroundColorWidget.setColor("#000000");
        tableHeaderColorWidget.setColor("#000000");
        tableItemRowSwitcher.setValue(true);
        tableItemRowFontSizeBox.setText("7");
        tableItemRowBackgroundCheckBox.setValue(false);
        tableItemRowBackgroundColorWidget.setColor("#000000");
        tableItemRowColorWidget.setColor("#000000");
    }

    public void showLabelPanel(boolean visible) {
        this.labelPanel.setVisible(visible);

        this.leftPanel.setVisible(!visible);
        this.rightPanel.setVisible(!visible);
    }

    public void setGenerateCommand(ExtendedCommand generateCommand) {
        this.generateCommand = generateCommand;
    }

    public boolean hasLayout() {
        return hasLayout;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }
}
