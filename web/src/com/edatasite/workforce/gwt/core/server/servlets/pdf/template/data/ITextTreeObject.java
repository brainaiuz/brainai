package com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class ITextTreeObject {
    public String key;
    public LinkedHashMap<String, String> value;
    public LinkedList<ITextTreeObject> childs;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public LinkedHashMap<String, String> getValue() {
        return value;
    }

    public void setValue(LinkedHashMap<String, String> value) {
        this.value = value;
    }

    public LinkedList<ITextTreeObject> getChilds() {
        return childs != null ? childs : new LinkedList<>();
    }

    public void setChilds(LinkedList<ITextTreeObject> childs) {
        this.childs = childs;
    }
}
