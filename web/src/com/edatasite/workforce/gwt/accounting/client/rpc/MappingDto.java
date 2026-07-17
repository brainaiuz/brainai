package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.List;

public class MappingDto implements IsSerializable {
    private Integer index;
    private String systemField;
    private String matchedField;

    private List<ValueDto> values = new ArrayList<ValueDto>();

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getSystemField() {
        return systemField;
    }

    public void setSystemField(String systemField) {
        this.systemField = systemField;
    }

    public String getMatchedField() {
        return matchedField;
    }

    public void setMatchedField(String matchedField) {
        this.matchedField = matchedField;
    }

    public List<ValueDto> getValues() {
        return values;
    }

    public void setValues(List<ValueDto> values) {
        this.values = values;
    }
}
