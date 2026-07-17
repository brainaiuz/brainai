package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/2/13
 * Time: 1:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class BankAccountImportStatementData implements IsSerializable{
    private BankAccountTransactionMapper[] formMappedData;
    private String datePattern;
    private Integer csvTemplateID;
    private ArrayList<CsvTemplateItem> csvTemplateData;

    private SelectItem[] csvTemplates;

    private List<MappingDto> mappings;

    private Map<String, String> mappinMap;

    public BankAccountImportStatementData() {
    }

    public BankAccountTransactionMapper[] getFormMappedData() {
        return formMappedData;
    }

    public void setFormMappedData(BankAccountTransactionMapper[] formMappedData) {
        this.formMappedData = formMappedData;
    }

    public String getDatePattern() {
        return datePattern;
    }

    public void setDatePattern(String datePattern) {
        this.datePattern = datePattern;
    }

    public Integer getCsvTemplateID() {
        return csvTemplateID;
    }

    public void setCsvTemplateID(Integer csvTemplateID) {
        this.csvTemplateID = csvTemplateID;
    }

    public ArrayList<CsvTemplateItem> getCsvTemplateData() {
        return csvTemplateData;
    }

    public void setCsvTemplateData(ArrayList<CsvTemplateItem> csvTemplateData) {
        this.csvTemplateData = csvTemplateData;
    }

    public SelectItem[] getCsvTemplates() {
        return csvTemplates;
    }

    public void setCsvTemplates(SelectItem[] csvTemplates) {
        this.csvTemplates = csvTemplates;
    }

    public List<MappingDto> getMappings() {
        return mappings;
    }

    public void setMappings(List<MappingDto> mappings) {
        this.mappings = mappings;
    }

    public Map<String, String> getMappinMap() {
        return mappinMap;
    }

    public void setMappinMap(Map<String, String> mappinMap) {
        this.mappinMap = mappinMap;
    }
}
