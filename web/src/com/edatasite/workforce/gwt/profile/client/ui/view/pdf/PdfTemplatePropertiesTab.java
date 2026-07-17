package com.edatasite.workforce.gwt.profile.client.ui.view.pdf;

import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.PdfTemplateTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiTextBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.PdfTemplateService;
import com.edatasite.workforce.gwt.profile.client.rpc.PdfTemplateServiceAsync;
import com.edatasite.workforce.gwt.profile.client.rpc.SettingsPdfTemplateItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Abror Abdukadirov
 * Date: 29.01.2019 15:18
 */
public class PdfTemplatePropertiesTab extends Composite {
    interface PdfTemplatePropertiesTabUiBinder extends UiBinder<HTMLPanel, PdfTemplatePropertiesTab> {
    }

    private static final PdfTemplatePropertiesTabUiBinder ourUiBinder = GWT.create(PdfTemplatePropertiesTabUiBinder.class);
    private static final PdfTemplateServiceAsync pdfTemplateService = PdfTemplateService.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final SettingStrings settingsStrings = SettingStrings.App.get();

    @UiField
    HTMLPanel pdfTypePanel;
    @UiField
    HTMLPanel templateNamePanel;
    @UiField
    HTMLPanel defaultPanel;
    @UiField
    HTMLPanel orientationPanel;
    @UiField
    HTMLPanel marginTopPanel;
    @UiField
    HTMLPanel marginBottomPanel;
    @UiField
    HTMLPanel marginLeftPanel;
    @UiField
    HTMLPanel marginRightPanel;
    @UiField
    HTMLPanel fontPanel;

    private DataListBox typeListBox;
    private KpiTextBox templateName;
    private KpiCheckBox defaultCheckBox;
    private KpiRadioButton portraitRadioButtonn;
    private KpiRadioButton landscapeRadioButtonn;
    private TextBox marginTopBox;
    private TextBox marginBottomBox;
    private TextBox marginLeftBox;
    private TextBox marginRightBox;
    private DataListBox fontListBox;

    private ExtendedCommand generateCommand;
    private Integer objectId;
    private String pdfType = PdfTemplateTypeEnum.SALES_INVOICE.name();
    private boolean isSystemPdf;

    public PdfTemplatePropertiesTab(Integer objectId, String pdfType) {
        this.objectId = objectId;
        if (pdfType != null && !"".equals(pdfType)) {
            this.pdfType = pdfType;
        }
        initWidget(ourUiBinder.createAndBindUi(this));
        initialize();
    }

    private void initialize() {
        typeListBox = new DataListBox();
        typeListBox.setWithoutNullLabel(true);
        if (this.objectId != null) {
            typeListBox.setEnabled(false);
        } else {
            typeListBox.addValueChangeHandler(event -> {
                if (typeListBox.getSelectedItem() != null) {
                    if (typeListBox.getSelectedItem().getId() == -1) {
                        objectId = null;
                        isSystemPdf = true;
                        templateName.setEnabled(false);
                    } else if (typeListBox.getSelectedItem().getId() == -2) {
                        objectId = null;
                        isSystemPdf = false;
                        templateName.setEnabled(true);
                    } else {
                        isSystemPdf = false;
                        templateName.setEnabled(true);
                        objectId = typeListBox.getSelectedItem().getId();
                    }
                    getPropertiesSettings(true);
                }
            });
        }
        getPdfTemplates();

        templateName = new KpiTextBox();

        defaultCheckBox = new KpiCheckBox(wfmStrings.defaultTemplate());

        portraitRadioButtonn = new KpiRadioButton("portraitButton", wfmStrings.portrait());
        portraitRadioButtonn.setValue(true);
        landscapeRadioButtonn = new KpiRadioButton("portraitButton", wfmStrings.landscape());

        marginTopBox = new TextBox();
        marginTopBox.setText("15");
        marginTopBox.addValueChangeHandler(event -> {
            String key = event.getValue();
            if (!"".equals(event.getValue().trim())
                    && (Integer.valueOf(event.getValue()) > 50
                    || Integer.valueOf(event.getValue()) <= 0)) {
                marginTopBox.setText("1");
            }
        });
        Validation.addNumericKeyboardListener(marginTopBox);
        marginBottomBox = new TextBox();
        marginBottomBox.setText("1");
        marginBottomBox.addValueChangeHandler(event -> {
            String key = event.getValue();
            if (!"".equals(event.getValue().trim())
                    && (Integer.valueOf(event.getValue()) > 50
                    || Integer.valueOf(event.getValue()) <= 0)) {
                marginBottomBox.setText("1");
            }
        });
        Validation.addNumericKeyboardListener(marginBottomBox);
        marginLeftBox = new TextBox();
        marginLeftBox.setText("90");
        marginLeftBox.addValueChangeHandler(event -> {
            String key = event.getValue();
            if (!"".equals(event.getValue().trim())
                    && (Integer.valueOf(event.getValue()) > 100
                    || Integer.valueOf(event.getValue()) <= 0)) {
                marginLeftBox.setText("1");
            }
        });
        Validation.addNumericKeyboardListener(marginLeftBox);
        marginRightBox = new TextBox();
        marginRightBox.setText("15");
        marginRightBox.addValueChangeHandler(event -> {
            String key = event.getValue();
            if (!"".equals(event.getValue().trim())
                    && (Integer.valueOf(event.getValue()) > 100
                    || Integer.valueOf(event.getValue()) <= 0)) {
                marginRightBox.setText("1");
            }
        });
        Validation.addNumericKeyboardListener(marginRightBox);
        fontListBox = new DataListBox();
        fontListBox.setItems(getPdfFonts());
        fontListBox.setSelected(new SelectItem(1, "Arial", "ARIAL"));

        pdfTypePanel.add(new FormGroup(wfmStrings.type(), typeListBox, true));
        templateNamePanel.add(new FormGroup(wfmStrings.template(), templateName, true));
        defaultPanel.add(new FormGroup("&nbsp;", defaultCheckBox));
        FormGroup formGroup = new FormGroup(settingsStrings.orientation(), portraitRadioButtonn);
        formGroup.addToContent(landscapeRadioButtonn);
        formGroup.getGroupContent().addStyleName("stack-x");
        orientationPanel.add(formGroup);

        marginTopPanel.add(new FormGroup(wfmStrings.marginTop(), new AdvancedInputGroup(marginTopBox, new Span("px"))));
        marginBottomPanel.add(new FormGroup(wfmStrings.marginBottom(), new AdvancedInputGroup(marginBottomBox, new Span("px"))));
        marginLeftPanel.add(new FormGroup(wfmStrings.marginLeft(), new AdvancedInputGroup(marginLeftBox, new Span("px"))));
        marginRightPanel.add(new FormGroup(wfmStrings.marginRight(), new AdvancedInputGroup(marginRightBox, new Span("px"))));
        fontPanel.add(new FormGroup(wfmStrings.pdfFont(), fontListBox));
    }

    public void getPropertiesSettings(boolean generate) {
        LoadingPanel.loading(true);
        pdfTemplateService.getSettingsPdfTemplateProperties(objectId, pdfType, new AbstractAsyncCallback<SettingsPdfTemplateItem>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(SettingsPdfTemplateItem result) {
                LoadingPanel.loading(false);

                setPropertiesData(result);

                if (generate) {
                    generateCommand.execute(null);
                }
            }
        });
    }

    private void setPropertiesData(SettingsPdfTemplateItem item) {
        if (item == null) {
            return;
        }
        if (item.getTemplateItem() != null) {
            typeListBox.setSelected(item.getTemplateItem());
        }
        templateName.setText(item.getPdfName());
        if (item.getOrientation() != null && "landscape".equals(item.getOrientation())) {
            landscapeRadioButtonn.setValue(true);
        } else {
            portraitRadioButtonn.setValue(true);
        }
        if (item.getMarginTop() != null) {
            marginTopBox.setText(item.getMarginTop().replace("px", ""));
        }
        if (item.getMarginRight() != null) {
            marginRightBox.setText(item.getMarginRight().replace("px", ""));
        }
        if (item.getMarginBottom() != null) {
            marginBottomBox.setText(item.getMarginBottom().replace("px", ""));
        }
        if (item.getMarginLeft() != null) {
            marginLeftBox.setText(item.getMarginLeft().replace("px", ""));
        }
        defaultCheckBox.setValue(item.isDefaultTemplate());
    }

    public SettingsPdfTemplateItem getData(SettingsPdfTemplateItem item) {
        if (item == null) {
            item = new SettingsPdfTemplateItem();
        }
        item.setPdfName(templateName.getText());
        if (portraitRadioButtonn.getValue()) {
            item.setOrientation("portrait");
        } else {
            item.setOrientation("landscape");
        }
        item.setMarginTop(marginTopBox.getText());
        item.setMarginBottom(marginBottomBox.getText());
        item.setMarginLeft(marginLeftBox.getText());
        item.setMarginRight(marginRightBox.getText());
        if (defaultCheckBox.getValue() != null) {
            item.setDefaultTemplate(defaultCheckBox.getValue());
        }
        return item;
    }

    public void resetWidgets() {
        templateName.setText(wfmStrings.template());
        portraitRadioButtonn.setValue(true);
        marginTopBox.setText("1");
        marginBottomBox.setText("1");
        marginLeftBox.setText("90");
        marginRightBox.setText("15");
    }

    private SelectItem[] getPdfFonts() {
        return new SelectItem[]{new SelectItem(1, "Arial", "ARIAL")};
    }

    private void getPdfTemplates() {
        LoadingPanel.loading(true);
        pdfTemplateService.getClientPdfTemplatesByType(pdfType, new AbstractAsyncCallback<ArrayList<SelectItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ArrayList<SelectItem> result) {
                LoadingPanel.loading(false);

                List<SelectItem> items = new ArrayList<>();
                items.add(new SelectItem(-1, wfmStrings.default2()));
                items.add(new SelectItem(-2, wfmStrings.newStyle()));
                if (result != null && result.size() > 0) {
                    items.addAll(result);
                }
                typeListBox.setItems(items.toArray(new SelectItem[]{}));
                if (objectId == null) {
                    isSystemPdf = true;
                    templateName.setEnabled(false);
                    typeListBox.setSelected(new SelectItem(1, wfmStrings.default2()));
                }
            }
        });
    }

    public void setGenerateCommand(ExtendedCommand generateCommand) {
        this.generateCommand = generateCommand;
    }

    public String getPdfName() {
        return this.templateName.getText();
    }

    public boolean validate() {
        int errors = 0;

        if (!this.isSystemPdf) {
            if (!Validation.validateTextBoxRequired(templateName)) {
                errors++;
            }
        }
        if (!Validation.validateListBoxRequired(typeListBox)) {
            errors++;
        }
        return errors <= 0;
    }

    public TextBox getMarginTopBox() {
        return marginTopBox;
    }

    public TextBox getMarginBottomBox() {
        return marginBottomBox;
    }

    public TextBox getMarginLeftBox() {
        return marginLeftBox;
    }

    public TextBox getMarginRightBox() {
        return marginRightBox;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getPdfType() {
        return pdfType;
    }

    public boolean isSystemPdf() {
        return isSystemPdf;
    }
}
