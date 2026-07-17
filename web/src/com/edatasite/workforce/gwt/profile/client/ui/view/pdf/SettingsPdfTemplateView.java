package com.edatasite.workforce.gwt.profile.client.ui.view.pdf;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.PdfTemplateService;
import com.edatasite.workforce.gwt.profile.client.rpc.PdfTemplateServiceAsync;
import com.edatasite.workforce.gwt.profile.client.rpc.SettingsPdfTemplateGenerateItem;
import com.edatasite.workforce.gwt.profile.client.rpc.SettingsPdfTemplateItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialTab;
import gwt.material.design.client.ui.MaterialTabItem;
import gwt.material.design.client.ui.html.Div;

/**
 * User: Abror Abdukadirov
 * Date: 12.12.2018 15:49
 */
public class SettingsPdfTemplateView extends View implements Colapse {
    private static final SettingStrings settingsStrings = SettingStrings.App.get();

    interface PdfTemplateViewUiBinder extends UiBinder<HTMLPanel, SettingsPdfTemplateView> {
    }

    private static final PdfTemplateViewUiBinder ourUiBinder = GWT.create(PdfTemplateViewUiBinder.class);
    private static final PdfTemplateServiceAsync pdfTemplateService = PdfTemplateService.App.get();

    @UiField
    HTMLPanel tabPanel;
    @UiField
    HTMLPanel tabContainer;
    @UiField
    HTMLPanel buttonsPanel;
    @UiField
    DivElement pdfWrapper;
    @UiField
    IFrameElement headerIframe;
    @UiField
    IFrameElement bodyIframe;
    @UiField
    IFrameElement footerIframe;

    private MaterialTab tabs;
    private int tabStep = 0;

    private PdfTemplatePropertiesTab templatePropertiesTab;
    private final String pdfType;
    private PdfItemTableTab itemTableTabPanel;
    private PdfHeaderFooterTab headerTabPanel;
    // private PdfFooterTab footerTabPanel;
    private Integer objectId;

    public SettingsPdfTemplateView(Integer objectId, String pdfType) {
        super("pdftemplate", settingsStrings.pdfSettings());
        this.objectId = objectId;
        this.pdfType = pdfType;
    }

    @Override
    protected Widget onInitialize() {
        MainLayout.get().setSideMenuOpen(false);
        if (this.objectId == null) {
            SinksContainer sinksContainer = SinksContainerFactory.entryPoint.getContainerFactory().getContainerByName("emailSettings");
            SinksContainer currentContainer = SinksContainerFactory.entryPoint.getContainerFactory().getContainerByName("pdftemplatenull");
            if (sinksContainer != null) {
                sinksContainer.setPreparedView("pdfTemplateList");
                sinksContainer.setDescription(wfmStrings.pdfTemplates());
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ADD_TAB, sinksContainer, MainLayout.get().getNavToolBar());
            }
            if (currentContainer != null) {
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SELECT_TAB, currentContainer, MainLayout.get().getNavToolBar());
            }
        }
        add(ourUiBinder.createAndBindUi(this));

        tabs = new MaterialTab();
        templatePropertiesTab = new PdfTemplatePropertiesTab(objectId, pdfType);
        templatePropertiesTab.setGenerateCommand(new ExtendedCommand() {
            @Override
            public void execute(Integer id) {
                generatePdfPreview();
            }
        });
        headerTabPanel = new PdfHeaderFooterTab(templatePropertiesTab.getObjectId(), templatePropertiesTab.getPdfType());
        headerTabPanel.setGenerateCommand(new ExtendedCommand() {
            @Override
            public void execute(Integer id) {
                generatePdfPreview();
            }
        });
        itemTableTabPanel = new PdfItemTableTab(templatePropertiesTab.getObjectId(), templatePropertiesTab.getPdfType());
        itemTableTabPanel.setGenerateCommand(new ExtendedCommand() {
            @Override
            public void execute(Integer id) {
                generatePdfPreview();
            }
        });
//        headerTabPanel.setHeight("100%");
        // footerTabPanel = new PdfFooterTab();
//        headerTabPanel.setGenerateCommand(new ExtendedCommand() {
//            @Override
//            public void execute(Integer id) {
//                generatePdfPreview();
//            }
//        });

        MaterialTabItem templatePropertiesTab = new MaterialTabItem();
        MaterialLink templatePropertiesLink = new MaterialLink();
        templatePropertiesLink.setHref("#templateProperties_tab");
        templatePropertiesLink.setText(wfmStrings.properties());
        templatePropertiesLink.addClickHandler(event -> {
            tabStep = 0;
            this.templatePropertiesTab.getPropertiesSettings(true);
            changeTabContainer(this.templatePropertiesTab);
        });
        templatePropertiesTab.add(templatePropertiesLink);
        tabs.add(templatePropertiesTab);

        MaterialTabItem footerHeaderTab = new MaterialTabItem();
        MaterialLink footerHeaderLink = new MaterialLink();
        footerHeaderLink.setHref("#header_tab");
        footerHeaderLink.setText(settingsStrings.headerAndfooter());
        footerHeaderLink.addClickHandler(event -> {
            tabStep = 1;
            this.headerTabPanel.setObjectId(this.templatePropertiesTab.getObjectId());
            this.headerTabPanel.setSystemPdf(this.templatePropertiesTab.isSystemPdf());
            this.headerTabPanel.getFooterAndHeaderSettings(true);
            changeTabContainer(this.headerTabPanel);
        });
        footerHeaderTab.add(footerHeaderLink);
        tabs.add(footerHeaderTab);

        MaterialTabItem contentTab = new MaterialTabItem();
        MaterialLink itemTableLink = new MaterialLink();
        itemTableLink.setHref("#content_tab");
        itemTableLink.setText(wfmStrings.itemTable());
        itemTableLink.addClickHandler(event -> {
            tabStep = 2;
            this.itemTableTabPanel.setObjectId(this.templatePropertiesTab.getObjectId());
            if (this.itemTableTabPanel.hasLayout()) {
                this.itemTableTabPanel.getItemTableLayoutSettings(true);
            } else {
                this.itemTableTabPanel.getItemTableLabelsSettings(true);
            }
            changeTabContainer(this.itemTableTabPanel);
        });
        contentTab.add(itemTableLink);
        tabs.add(contentTab);
        tabPanel.add(tabs);

        WfmButton2 saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveButton.addClickHandler(event -> {
            if (!validate()) {
                return;
            }
            savePdfSettings();
        });

        WfmButton2 resetButton = new WfmButton2(wfmStrings.reset(), WfmButton2.BTN_WHITE_OUTLINE);
        resetButton.addClickHandler(event -> {
            WfmMessageBox modal = new WfmMessageBox(IconEnum.CONFIRM, Action.OK, true);
            modal.setTitle(wfmStrings.confirmationMessage());
            String message = "";
            if (tabStep == 0) {
                message = wfmMessages.youWantToResetSettings(settingsStrings.property(), wfmStrings.settings());
            } else if (tabStep == 1) {
                message = wfmMessages.youWantToResetSettings(wfmStrings.header() + " , " + wfmStrings.footer(), wfmStrings.settings());
            } else if (tabStep == 2) {
                message = wfmMessages.youWantToResetSettings(wfmStrings.content(), wfmStrings.settings());
            }
            modal.setMessage(message);
            modal.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    resetPdfSettings();
                }
            });
            modal.open();
        });

        WfmButton2 previewButton = new WfmButton2(wfmStrings.preview(), WfmButton2.BTN_WHITE_OUTLINE);
        previewButton.addClickHandler(event -> {
            generatePdfPreview();
        });
        Div buttonGroup = new Div("btns-group");
        buttonGroup.add(saveButton);
        buttonGroup.add(resetButton);
        buttonGroup.add(previewButton);
        buttonsPanel.clear();
        buttonsPanel.add(buttonGroup);
        buttonsPanel.getElement().getStyle().setZIndex(1);

        this.templatePropertiesTab.getPropertiesSettings(false);
        this.headerTabPanel.getFooterAndHeaderSettings(false);
        this.itemTableTabPanel.getItemTableLayoutSettings(false);
        this.itemTableTabPanel.getItemTableLabelsSettings(true);
        changeTabContainer(this.templatePropertiesTab);

        tabs.selectTab("templateProperties_tab");

        pdfWrapper.getStyle().setMarginTop(15, Style.Unit.PX);
        pdfWrapper.getStyle().setMarginRight(15, Style.Unit.PX);
        pdfWrapper.getStyle().setMarginBottom(1, Style.Unit.PX);
        pdfWrapper.getStyle().setMarginLeft(90, Style.Unit.PX);

        headerIframe.setAttribute("width", "100%");
        headerIframe.setAttribute("height", "300px");

        bodyIframe.setAttribute("width", "100%");
        bodyIframe.setAttribute("height", "500px");

        footerIframe.setAttribute("width", "100%");
        footerIframe.setAttribute("height", "240px");

        return null;
    }

    private void changeTabContainer(Widget panel) {
        tabContainer.clear();
        tabContainer.add(panel);
    }

    private void generatePdfPreview() {
        LoadingPanel.loading(true);
        pdfTemplateService.generateSettingsPdf(getPdfValue(), new AbstractAsyncCallback<SettingsPdfTemplateGenerateItem>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(SettingsPdfTemplateGenerateItem result) {
                LoadingPanel.loading(false);
                if (result == null) {
                    return;
                }
                headerIframe.getContentDocument().getBody().removeAllChildren();
                bodyIframe.getContentDocument().getBody().removeAllChildren();
                footerIframe.getContentDocument().getBody().removeAllChildren();

                if (result.getHeaderHtml() != null) {
                    headerIframe.getContentDocument().getBody().setInnerHTML(result.getHeaderHtml());
                }
                if (result.getBodyHtml() != null) {
                    bodyIframe.getContentDocument().getBody().setInnerHTML(result.getBodyHtml());
                }
                if (result.getFooterHtml() != null) {
                    footerIframe.getContentDocument().getBody().setInnerHTML(result.getFooterHtml());
                }
                pdfWrapper.getStyle().setMarginTop(Integer.valueOf(templatePropertiesTab.getMarginTopBox().getText().replace("px", "")), Style.Unit.PX);
                pdfWrapper.getStyle().setMarginRight(Integer.valueOf(templatePropertiesTab.getMarginRightBox().getText().replace("px", "")), Style.Unit.PX);
                pdfWrapper.getStyle().setMarginBottom(Integer.valueOf(templatePropertiesTab.getMarginBottomBox().getText().replace("px", "")), Style.Unit.PX);
                pdfWrapper.getStyle().setMarginLeft(Integer.valueOf(templatePropertiesTab.getMarginLeftBox().getText().replace("px", "")), Style.Unit.PX);
            }
        });
    }

    private void savePdfSettings() {
        LoadingPanel.loading(true);
        AbstractAsyncCallback callback = new AbstractAsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(Integer result) {
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.pdf()), Info.Type.INFO);
                if (result != null) {
                    objectId = result;
                    templatePropertiesTab.setObjectId(result);
                    itemTableTabPanel.setObjectId(result);
                    headerTabPanel.setObjectId(result);
                }
                if (tabStep == 0 || tabStep == 2) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SETTINGS_PDF_TEMPLATE_ADD_EDIT, result, SettingsPdfTemplateView.this);
                }
            }
        };
        if (tabStep == 0) {
            SettingsPdfTemplateItem item = this.templatePropertiesTab.getData(null);
            item.setObjectId(this.templatePropertiesTab.getObjectId());
            item.setPdfType(this.templatePropertiesTab.getPdfType());
            item.setSystemPdf(this.templatePropertiesTab.isSystemPdf());
            pdfTemplateService.savePdfPropertiesSettings(item, callback);
        } else if (tabStep == 1) {
            pdfTemplateService.savePdfFooterHeaderSettings(this.headerTabPanel.getData(null), callback);
        } else if (tabStep == 2) {
            SettingsPdfTemplateItem item = this.itemTableTabPanel.getData(null);
            item.setObjectId(this.templatePropertiesTab.getObjectId());
            item.setSystemPdf(this.templatePropertiesTab.isSystemPdf());
            item.setPdfName(this.templatePropertiesTab.getPdfName());
            pdfTemplateService.savePdfContentSettings(item, callback);
        }
    }

    private SettingsPdfTemplateItem getPdfValue() {
        SettingsPdfTemplateItem item = new SettingsPdfTemplateItem();
        this.headerTabPanel.getData(item);
        this.itemTableTabPanel.getData(item);
        return item;
    }

    private boolean validate() {
        int errors = 0;

        if (tabStep == 1) {
            if (!this.headerTabPanel.validate()) {
                errors++;
            }
        } else if (tabStep == 2) {
            if (!this.itemTableTabPanel.validate()) {
                return false;
            }
            if (!this.templatePropertiesTab.validate()) {
                errors++;
            }
        } else if (tabStep == 3) {
            if (!this.headerTabPanel.validate()) {
                errors++;
            }
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void resetPdfSettings() {
        LoadingPanel.loading(true);
        pdfTemplateService.resetPdfSettings(tabStep, this.templatePropertiesTab.getPdfType(), this.templatePropertiesTab.getObjectId(), new AbstractAsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Void result) {
                LoadingPanel.loading(false);
                resetWidgets();
                if (tabStep == 0) {
                    Info.show("Property settings reset successfully");
                    generatePdfPreview();
                } else if (tabStep == 1) {
                    Info.show("Header settings reset successfully");
                    generatePdfPreview();
                } else if (tabStep == 2) {
                    if (itemTableTabPanel.hasLayout()) {
                        itemTableTabPanel.getItemTableLayoutSettings(true);
                    } else {
                        itemTableTabPanel.getItemTableLabelsSettings(true);
                    }
                    Info.show("Content settings reset successfully");
                } else {
                    Info.show("Footer settings reset successfully");
                    generatePdfPreview();
                }
            }
        });
    }

    private void resetWidgets() {
        LoadingPanel.loading(true);
        if (tabStep == 0) {
            this.templatePropertiesTab.resetWidgets();
        } else if (tabStep == 1) {
            this.headerTabPanel.resetWidgets();
        } else if (tabStep == 2) {
            this.itemTableTabPanel.resetWidgets();
        } else {
            this.headerTabPanel.resetWidgets();
        }
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                callback.onFailure(reason);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
