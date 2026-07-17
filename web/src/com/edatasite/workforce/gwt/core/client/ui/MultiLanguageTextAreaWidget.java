package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Heading;

import java.util.HashMap;
import java.util.Map;

public class MultiLanguageTextAreaWidget extends Widget {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private KpiSideNavBox sideNavBox;
    private TextArea2 textArea2;
    private Div textBoxDiv;

    private int textAreaHeight = 150;
    private String sideBarLabel;

    private String[] languages = new String[]{"uz", "en", "ru", "ar"};
    private Map<String, TextArea2> textAreaMap = new HashMap<>();
    private HashMap<String, String> valueMap = new HashMap<>();

    public MultiLanguageTextAreaWidget(TextArea2 textArea2) {
        this.textArea2 = textArea2;
        init();
    }

    public MultiLanguageTextAreaWidget(String title) {
        this.textArea2 = new TextArea2(title);
        init();
    }

    public MultiLanguageTextAreaWidget(Div div) {
        this.textBoxDiv = div;
        String label = div.getChildren()
                .get(0).getElement()
                .getInnerText()
                .replace("*", "")
                .replace(":", "");
        this.textArea2 = new TextArea2(label);
        init();
    }

    public void init() {
        sideNavBox = new KpiSideNavBox();
        textArea2.getTextArea().addValueChangeHandler(valueChangeEvent -> setCurrentLangText(textArea2.getText()));
        addLocalizeButton();
        initTextAreaMap();

        drawSideBar();
        drawSideBarHeader();
        drawSideBarBody();
        drawSideBarFooter();
    }

    private void addLocalizeButton() {

        MaterialLink label = new MaterialLink(wfmStrings.vacancyLocale());
        label.addStyleName("btn-small btn--default mb-1");
        label.addClickHandler(clickEvent -> sideNavBox.show());

        MaterialIcon plusIcon = new MaterialIcon();
        plusIcon.setStyleName("ficon--plus-circle");
        label.add(plusIcon);


        if (textBoxDiv == null) {
            textArea2.localize.add(label);
        } else {
            textBoxDiv.insert(label, 0);
        }
    }

    private void drawSideBar() {
        sideBarLabel = textArea2.getLabel();
    }

    private void drawSideBarHeader() {
        Heading heading = new Heading(HeadingSize.H6);
        heading.setText(sideBarLabel);
        sideNavBox.addHeader(heading);
    }

    private void drawSideBarBody() {
        sideNavBox.clearBody();
        for (TextArea2 textArea : textAreaMap.values()) {
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
                textArea2.setText(valueMap.get(Utils.getUserLanguage()));
            }
            sideNavBox.close();
        });
        footerPanel.add(saveButton);

        sideNavBox.addFooter(footerPanel);
    }

    private void initTextAreaMap() {
        TextArea2 textArea = new TextArea2(textArea2.getMAX_LENGTH(), "English");
        textArea.setHeight(textAreaHeight);
        textArea.setText(valueMap.get("en"));
        textAreaMap.put("en", textArea);

        textArea = new TextArea2(textArea2.getMAX_LENGTH(), "Русский");
        textArea.setHeight(textAreaHeight);
        textArea.setText(valueMap.get("ru"));
        textAreaMap.put("ru", textArea);

        textArea = new TextArea2(textArea2.getMAX_LENGTH(), "O'zbek");
        textArea.setHeight(textAreaHeight);
        textArea.setText(valueMap.get("uz"));
        textAreaMap.put("uz", textArea);

        textArea = new TextArea2(textArea2.getMAX_LENGTH(), "عربى");
        textArea.setHeight(textAreaHeight);
        textArea.setText(valueMap.get("ar"));
        textAreaMap.put("ar", textArea);
    }

    private void initReferenceValuesFromMap() {
        valueMap.put("uz", textAreaMap.get("uz").getText());
        valueMap.put("en", textAreaMap.get("en").getText());
        valueMap.put("ru", textAreaMap.get("ru").getText());
        valueMap.put("ar", textAreaMap.get("ar").getText());
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
    public void setSideBarLabel(String label) {
        sideNavBox.getContentHeader().getElement().setInnerText(label);
    }

    public TextArea2 getTextArea2() {
        return textArea2;
    }

    public void setCurrentLangText(String text) {
        textAreaMap.get(Utils.getUserLanguage()).setText(text);
    }

    public HashMap<String, String> getValueMap() {
        return valueMap;
    }

    public void setValueMap(HashMap<String, String> valueMap) {
        if (valueMap != null) {
            this.valueMap = valueMap;
            initTextAreaMap();
            drawSideBarBody();
        }
    }

    public void show() {
        sideNavBox.open();
    }
}
