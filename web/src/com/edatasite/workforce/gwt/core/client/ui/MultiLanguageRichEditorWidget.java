package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Heading;

import java.util.HashMap;
import java.util.Map;

public class MultiLanguageRichEditorWidget extends Widget {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private KpiSideNavBox sideNavBox;
    private final TextAreaWithSuggestionPopup textArea2;
    private Div textBoxDiv;

    private int textAreaHeight = 150;
    private String sideBarLabel;

    private String[] languages = new String[]{"uz", "en", "ru", "ar"};
    private final Map<String, FlowPanel> textAreaMap = new HashMap<>();
    private HashMap<String, String> valueMap = new HashMap<>();
    private Boolean forName;
    private Integer relatedId;

    private Label headerLabel;
    private Heading sideNavHeading;

    public MultiLanguageRichEditorWidget(TextAreaWithSuggestionPopup textArea2) {
        this.textArea2 = textArea2;
        init();
    }

    public MultiLanguageRichEditorWidget(String title) {
        this.textArea2 = new TextAreaWithSuggestionPopup(null);
        init();
    }

    public void init() {
        sideNavBox = new KpiSideNavBox();
        sideNavBox.setWidth(500);
        textArea2.getMaterialRichEditor().getRichEditor().addValueChangeHandler(valueChangeEvent -> {
                    String text = textArea2.getMaterialRichEditor().getData().replaceAll("</p>|<p>|<br>|&nbsp;|</b>|<b>|</span>", "");
                    setCurrentLangText(text);
                }
        );
        addLocalizeButton();
        initTextAreaMap();

        drawSideBar();
        drawSideBarHeader();
        drawSideBarBody();
        drawSideBarFooter();
    }

    private void addLocalizeButton() {
        headerLabel = new Label(textArea2.getText());
        headerLabel.addClickHandler(e -> sideNavBox.show());

        MaterialLink localeLink = new MaterialLink(wfmStrings.vacancyLocale());
        localeLink.addStyleName("btn-small btn--default mb-1");
        localeLink.addClickHandler(e -> sideNavBox.show());

        MaterialIcon plusIcon = new MaterialIcon();
        plusIcon.setStyleName("ficon--plus-circle");
        localeLink.add(plusIcon);

        HorizontalPanel headerPanel = new HorizontalPanel();
        headerPanel.setWidth("100%");
        headerPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

        headerPanel.add(headerLabel);
        headerPanel.add(localeLink);

        headerPanel.setCellHorizontalAlignment(headerLabel, HasHorizontalAlignment.ALIGN_LEFT);
        headerPanel.setCellHorizontalAlignment(localeLink, HasHorizontalAlignment.ALIGN_RIGHT);
        headerPanel.setCellWidth(localeLink, "1%");

        textArea2.textPanel.clear();
        textArea2.textPanel.add(headerPanel);
    }

    public void setTitleAndReinit(String newTitle){
        if (newTitle == null) return;

        // 1. Update the underlying data in the popup
        textArea2.setTitle(newTitle);

        // 2. Update the visible Label on the main form
        if (headerLabel != null) {
            headerLabel.setText(newTitle);
        }

        // 3. Update the SideNav Header title
        if (sideNavHeading != null) {
            sideNavHeading.setText(newTitle);
        }

        this.sideBarLabel = newTitle;

    }

    private void drawSideBar() {
        sideBarLabel = textArea2.getText();
    }

    private void drawSideBarHeader() {
        sideNavHeading = new Heading(HeadingSize.H6);
        sideNavHeading.setText(textArea2.getText());
        sideNavBox.addHeader(sideNavHeading);
    }

    private void drawSideBarBody() {
        sideNavBox.clearBody();
        for (FlowPanel textArea : textAreaMap.values()) {
            sideNavBox.addBody(textArea);
        }
    }

    private void drawSideBarFooter() {
        HorizontalPanel footerPanel = new HorizontalPanel();

        WfmButton2 saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveButton.addClickHandler(clickEvent -> {
            initReferenceValuesFromMap();
            if (textBoxDiv != null && textBoxDiv.getChildren().size() > 2 && textBoxDiv.getChildren().get(2) != null) {
                TextBox textBox = (TextBox) textBoxDiv.getChildren().get(2);
                textBox.setText(valueMap.get(Utils.getUserLanguage()));
            } else {
                textArea2.getMaterialRichEditor().setData(valueMap.get(Utils.getUserLanguage()));
            }
            sideNavBox.close();
            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_LOCALIZATION_ADD_FOR_SKILL_NAME, valueMap, MultiLanguageRichEditorWidget.this);
        });
        footerPanel.add(saveButton);

        sideNavBox.addFooter(footerPanel);
    }

    private void initTextAreaMap() {
        addKpiEditor("uz", "O'zbek");
        addKpiEditor("en", "English");
        addKpiEditor("ru", "Русский");
        addKpiEditor("ar", "عربى");
    }

    private void addKpiEditor(String languageCode, String label) {
        KpiEditor textArea = new KpiEditor(true);
        textArea.getRichEditor().setText(valueMap.get(languageCode));
        textArea.getElement().getStyle().setMarginTop(5, Style.Unit.PX); // Adjust the value as needed
        textArea.getElement().getStyle().setMarginBottom(10, Style.Unit.PX); // Adjust the value as needed

        Label titleLabel = new Label(label);
        // Create a container to hold the title and text area
        FlowPanel container = new FlowPanel();
        container.add(titleLabel);
        container.add(textArea);

        textAreaMap.put(languageCode, container);
    }

    private void initReferenceValuesFromMap() {
        KpiEditor uz = (KpiEditor) textAreaMap.get("uz").getWidget(1);
        KpiEditor en = (KpiEditor) textAreaMap.get("en").getWidget(1);
        KpiEditor ru = (KpiEditor) textAreaMap.get("ru").getWidget(1);
        KpiEditor ar = (KpiEditor) textAreaMap.get("ar").getWidget(1);
        valueMap.put("uz", uz.getData());
        valueMap.put("en", en.getData());
        valueMap.put("ru", ru.getData());
        valueMap.put("ar", ar.getData());
    }

    public int getTextAreaHeight() {
        return textAreaHeight;
    }

    public void setTextAreaHeight(int textAreaHeight) {
        this.textAreaHeight = textAreaHeight;
    }

    public String[] getLanguages() {
        return languages;
    }

    public void setLanguages(String[] languages) {
        this.languages = languages;
    }

    public TextAreaWithSuggestionPopup getTextArea2() {
        return textArea2;
    }

    public void setCurrentLangText(String text) {
        KpiEditor area = (KpiEditor) textAreaMap.get(Utils.getUserLanguage()).getWidget(1);
        area.setData(text);
    }

    public HashMap<String, String> getValueMap() {
        return valueMap;
    }

    public void setValueMap(HashMap<String, String> valueMap) {
        this.setValueMap(valueMap, false);
    }

    public void setValueMap(HashMap<String, String> valueMap, boolean forName) {
        this.forName = forName;
        if (valueMap != null) {
            this.valueMap = valueMap;
            initTextAreaMap();
            drawSideBarBody();
        }
    }

    public Boolean getForName() {
        return forName;
    }

    public Integer getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(Integer relatedId) {
        this.relatedId = relatedId;
    }

    public void show() {
        sideNavBox.open();
    }
}
