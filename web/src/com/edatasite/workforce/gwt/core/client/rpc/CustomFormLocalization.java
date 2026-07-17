package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

public class CustomFormLocalization implements IsSerializable {

    public static final int MAX_LENGTH_OF_VALUE = 1000;
    private Integer id;
    private String defaultName;
    private String englishName;
    private String russianName;
    private String uzbekName;
    private String arabicName;
    private Integer type;
    private String formId;
    private String section;
    private String localizedName;
    private ArrayList<CustomFormLocalization> children;

    public CustomFormLocalization() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getRussianName() {
        return russianName;
    }

    public void setRussianName(String russianName) {
        this.russianName = russianName;
    }

    public String getUzbekName() {
        return uzbekName;
    }

    public void setUzbekName(String uzbekName) {
        this.uzbekName = uzbekName;
    }

    public String getArabicName() {
        return arabicName;
    }

    public void setArabicName(String arabicName) {
        this.arabicName = arabicName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public ArrayList<CustomFormLocalization> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<CustomFormLocalization> children) {
        this.children = children;
    }

    public String getLocalizedName() {
        return localizedName;
    }

    public void setLocalizedName(String localizedName) {
        this.localizedName = localizedName;
    }
}
