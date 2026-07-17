package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceLocale;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Heading;

public class AddEditLocaleView extends KpiSideNavBox {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private TextBox name;
    private Div offsetContent;
    private Label localeName;
    private TextBox localeEnglishValue;
    private TextBox localeRussiaValue;
    private TextBox localeUzbekValue;
    private TextBox localeArabicValue;
    private ReferenceLocale localeItem;
    private Integer localeItemId;

    private String fontStyle = "font-style: normal;";
    public AddEditLocaleView() {
    }

    public AddEditLocaleView(String nameValue, ReferenceLocale localeItem) {
        if (name == null) name = new TextBox();
        name.setText(nameValue);
        this.localeItem = localeItem;
        localeEnglishValue = new TextBox();
        localeEnglishValue.setStyleName("form-control");
        localeRussiaValue = new TextBox();
        localeRussiaValue.setStyleName("form-control");
        localeUzbekValue = new TextBox();
        localeUzbekValue.setStyleName("form-control");
        localeArabicValue = new TextBox();
        localeArabicValue.setStyleName("form-control");
        if (localeItem != null){
            this.localeItemId = localeItem.getObjectId();
            getDataToFillFields();
        }
        initialize();
    }


    private void initialize() {
        Heading header = new Heading(HeadingSize.H1);
        header.setText(wfmStrings.vacancyLocale());
        addHeader(header);
        addHeaderContainer(getContentHeader());
        addBody(createLocaleBody());
        WfmButton2 saveLocale = new WfmButton2(wfmStrings.save());
        saveLocale.addStyleName("btn btn--primary");

        saveLocale.addClickHandler(event -> {
            saveLocaleItems();
            hide();
        });
        addFooter(saveLocale);
        show();
    }

    private Widget createLocaleBody() {
        offsetContent = new Div();
        String value = name.getText() != null ? name.getText() : "";
        localeName = new Label();
        localeName.setStyleName("form-group__label");
        offsetContent.add(localeName);
        offsetContent.add(getFormGroup("English:", localeEnglishValue));
        offsetContent.add(getFormGroup("Русский:", localeRussiaValue));
        offsetContent.add(getFormGroup("عربى", localeArabicValue));
        offsetContent.add(getFormGroup("O'zbek:", localeUzbekValue));
        return offsetContent;
    }

    private FormGroup getFormGroup(String label, Widget widget){
        localeName = new Label(label);
        localeName.setStyleName("form-group__label");
        FormGroup formGroup = new FormGroup(localeName,widget);
        formGroup.setStyle("form-group");
        return formGroup;
    }

    private void saveLocaleItems() {
        if (localeItem == null)
            localeItem = new ReferenceLocale();

        localeItem.setObjectId(localeItemId);
        localeItem.setArabic("".equals(localeArabicValue.getText()) ? null : localeArabicValue.getText());
        localeItem.setEnglish("".equals(localeEnglishValue.getText()) ? null : localeEnglishValue.getText());
        localeItem.setRussian("".equals(localeRussiaValue.getText()) ? null : localeRussiaValue.getText());
        localeItem.setUzbek("".equals(localeUzbekValue.getText()) ? null : localeUzbekValue.getText());
    }

    private void getDataToFillFields(){
        localeArabicValue.setText(localeItem.getArabic());
        localeEnglishValue.setText(localeItem.getEnglish());
        localeUzbekValue.setText(localeItem.getUzbek());
        localeRussiaValue.setText(localeItem.getRussian());
    }

    public ReferenceLocale getLocaleItem(){
        return localeItem;
    }

    public void showView() {
        clear();
        initialize();
    }

    public void setNameValue(String nameValue) {
        this.name.setText(nameValue);
    }

    public void setLocaleItem(ReferenceLocale localeItem) {
        this.localeItem = localeItem;
    }
}
