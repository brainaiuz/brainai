package com.edatasite.workforce.gwt.core.client.ui;

import com.google.gwt.user.client.ui.HTML;
import gwt.material.design.client.ui.MaterialCollapsibleItem;

import java.util.List;
import java.util.Set;


public class PageBreakItem {
    private List<HTML> fields;
    private Set<MaterialCollapsibleItem> sections;
    private List<String> fieldsColumnCode;

    public PageBreakItem(List<HTML> fieldsList,
                         List<String> fieldsColumnCode,
                         Set<MaterialCollapsibleItem> sectionsSet) {
        this.fields = fieldsList;
        this.sections = sectionsSet;
        this.fieldsColumnCode = fieldsColumnCode;
    }

    public void visible(Boolean isVisible) {

        for (HTML html : fields) {
            if (html != null) {
                html.setVisible(isVisible);
            }
        }

        for (MaterialCollapsibleItem item : sections) {
            if (item != null) {
                item.setVisible(isVisible);
                if (isVisible) {
                    item.setActive(isVisible);
                }
            }
        }
    }

    public List<HTML> getFields() {
        return fields;
    }

    public void setFields(List<HTML> fields) {
        this.fields = fields;
    }

    public Set<MaterialCollapsibleItem> getSections() {
        return sections;
    }

    public void setSections(Set<MaterialCollapsibleItem> sections) {
        this.sections = sections;
    }

    public List<String> getFieldsColumnCode() {
        return fieldsColumnCode;
    }

    public void setFieldsColumnCode(List<String> fieldsColumnCode) {
        this.fieldsColumnCode = fieldsColumnCode;
    }
}
