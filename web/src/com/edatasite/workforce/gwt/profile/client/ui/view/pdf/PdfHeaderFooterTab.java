package com.edatasite.workforce.gwt.profile.client.ui.view.pdf;
//header and footer customize page for all standard PDF prints

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.panelStack.PanelStack;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.PdfFooterHeaderContentItem;
import com.edatasite.workforce.gwt.profile.client.rpc.PdfTemplateService;
import com.edatasite.workforce.gwt.profile.client.rpc.PdfTemplateServiceAsync;
import com.edatasite.workforce.gwt.profile.client.rpc.SettingsPdfTemplateItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;

import java.util.ArrayList;
import java.util.HashMap;
//Change footer and header for all pdf print out files--> STANDARD ONES

public class PdfHeaderFooterTab extends Composite {
    //header and footer widgets
    private static final PdfHeaderFooterTabUiBinder ourUiBinder = GWT.create(PdfHeaderFooterTabUiBinder.class);
    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final PdfTemplateServiceAsync pdfTemplateService = PdfTemplateService.App.get();
    private final String nickDebugId = "add_workflow_alert_view_";
    private final HashMap<String, PdfFooterHeaderItemWidget> contentItemWidgetsMap = new HashMap<>();
    private final HashMap<String, PdfFooterHeaderItemWidget> itemWidgets = new HashMap<>();
    private final String pdfType;
    //form structure
    @UiField
    HTMLPanel topPanel;
    @UiField
    HTMLPanel bottomPanel;
    @UiField
    HTMLPanel fullPanel;
    @UiField
    HTMLPanel leftPanel;
    private DataListBox attributeFieldCodes;
    private HTML attribute;
    private KpiEditor content;
    private ExtendedCommand generateCommand;
    private Integer objectId;
    private KpiSwitcher headerSwitcher;
    private KpiSwitcher footerSwitcher;

    public PdfHeaderFooterTab(Integer objectId, String pdfType) {
        this.objectId = objectId;
        this.pdfType = pdfType;
        initWidget(ourUiBinder.createAndBindUi(this));
        registerFields();

    }

    private void registerFields() {

        //Header ON-OFF
        fullPanel.setVisible(true);
        headerSwitcher = new KpiSwitcher();
        headerSwitcher.setValue(true);
        headerSwitcher.addClickHandler((event) -> {
            switchHandler(true);
        });

        PanelStack headerPanelStack = new PanelStack();
        headerPanelStack.setHeaderTitle(wfmStrings.header());
        headerPanelStack.setHeaderWidget(headerSwitcher);
        headerPanelStack.removeBodyRowPanel();
        topPanel.add(headerPanelStack);
        topPanel.getElement().getStyle().setMarginBottom(8, Style.Unit.PX);

        //initiate description text editor
        bottomPanel.setVisible(true);
        footerSwitcher = new KpiSwitcher();
        footerSwitcher.setValue(true);
        footerSwitcher.addClickHandler((event) -> {
            switchHandler(false);
        });
        PanelStack footerPanelStack = new PanelStack();
        footerPanelStack.setHeaderTitle(wfmStrings.footer());
        footerPanelStack.setHeaderWidget(footerSwitcher);
        footerPanelStack.removeBodyRowPanel();
        leftPanel.add(footerPanelStack);
        leftPanel.getElement().getStyle().setMarginBottom(8, Style.Unit.PX);
        drawDescriptionsPanel();
    }

    private void switchHandler(boolean isHeader) {
        if (isHeader) {
            fullPanel.setVisible(!fullPanel.isVisible());
            setEnableValueToWidgets("HEADER", !fullPanel.isVisible());
        } else {
            bottomPanel.setVisible(!bottomPanel.isVisible());
            setEnableValueToWidgets("FOOTER", !bottomPanel.isVisible());
        }
    }

    private void setEnableValueToWidgets(String code, Boolean value) {
        contentItemWidgetsMap.forEach((k, v) -> {
            if (k.contains(code)) {
                v.setEnable(!value);
            }
        });
    }

    private void drawDescriptionsPanel() {
        drawDescriptionPanel(Constants.HEADER_LEFT, Boolean.TRUE, wfmStrings.left(), headerSwitcher.getValue());
        drawDescriptionPanel(Constants.HEADER_CENTER, Boolean.TRUE, wfmStrings.center(), headerSwitcher.getValue());
        drawDescriptionPanel(Constants.HEADER_RIGHT, Boolean.TRUE, wfmStrings.right(), headerSwitcher.getValue());
        drawDescriptionPanel(Constants.FOOTER_LEFT, Boolean.FALSE, wfmStrings.left(), footerSwitcher.getValue());
        drawDescriptionPanel(Constants.FOOTER_CENTER, Boolean.FALSE, wfmStrings.center(), footerSwitcher.getValue());
        drawDescriptionPanel(Constants.FOOTER_RIGHT, Boolean.FALSE, wfmStrings.right(), footerSwitcher.getValue());
    }

    //DRAW DESCRIPTION PANEL DETAILS
    private void drawDescriptionPanel(String position, Boolean isHeader, String listBoxName, Boolean isEnable) {

        PdfFooterHeaderItemWidget widget = new PdfFooterHeaderItemWidget(position, isEnable);
        if (isHeader) {
            fullPanel.add(widget);
        } else {
            bottomPanel.add(widget);
        }
        contentItemWidgetsMap.put(position, widget);
    }

    private void setValuesToWidgetMap(SettingsPdfTemplateItem item) {
        if (item.getValueByPosition() != null) {
            item.getValueByPosition().forEach((v) -> {
                contentItemWidgetsMap.get(v.getPosition()).setItem(v);
            });
        }
    }


    public SettingsPdfTemplateItem getData(SettingsPdfTemplateItem item) {
        if (item == null) {
            item = new SettingsPdfTemplateItem();
        }

        item.setObjectId(objectId);
        item.setCustomizedHeader(headerSwitcher.getValue());
        item.setCustomizedFooter(footerSwitcher.getValue());
        ArrayList<PdfFooterHeaderContentItem> values = new ArrayList<>();
        contentItemWidgetsMap.values().forEach(v -> {
            PdfFooterHeaderContentItem itemPdf = v.getItem();
            if (itemPdf.getPosition().contains("HEADER")) {
                itemPdf.setEnable(headerSwitcher.getValue());
            } else {
                itemPdf.setEnable(footerSwitcher.getValue());
            }
            values.add(itemPdf);
        });
        item.setValueByPosition(values);

        return item;
    }

    public void getFooterAndHeaderSettings(boolean generate) {
        LoadingPanel.loading(true);
        pdfTemplateService.getSettingsPdfTemplateFooterAndHeader(this.objectId, this.pdfType, new AbstractAsyncCallback<SettingsPdfTemplateItem>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(SettingsPdfTemplateItem result) {
                LoadingPanel.loading(false);

                setFooterAndHeaderData(result);

                if (generate) {
                    generateCommand.execute(null);
                }
            }
        });
    }


    private void setFooterAndHeaderData(SettingsPdfTemplateItem item) {
        if (item == null) {
            return;
        }
        setValuesToWidgetMap(item);
        headerSwitcher.setValue(item.getCustomizedHeader());
        leftPanel.setVisible(item.getCustomizedHeader());
        footerSwitcher.setValue(item.getCustomizedFooter());
        bottomPanel.setVisible(item.getCustomizedFooter());
    }

    interface PdfHeaderFooterTabUiBinder extends UiBinder<HTMLPanel, PdfHeaderFooterTab> {
    }


    public void resetWidgets() {
        this.getFooterAndHeaderSettings(true);
    }

    public boolean validate() {
        int errors = 0;
        return errors <= 0;
    }

    public void setSystemPdf(boolean isSystemPdf) {
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }
    public void setGenerateCommand(ExtendedCommand generateCommand) {
        this.generateCommand = generateCommand;
    }

    interface PdfFooterTabUiBinder extends UiBinder<HTMLPanel, PdfHeaderFooterTab> {
    }


}