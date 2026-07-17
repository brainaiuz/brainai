package com.finnetlimited.reportservice.core.client.gwtrpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Ulugbek
 * Date: 10/4/12
 * Time: 7:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReportTemplateItem implements IsSerializable {

    private Integer id;
    private String name;
    private String body;
    private Integer categoryId;
    private Boolean isCustom;
    private Boolean isLibrary;
    private Boolean isSimplified;
    private SelectItem[] categories;
    private String code;
    private Integer stepId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Boolean getCustom() {
        return isCustom;
    }

    public void setCustom(Boolean custom) {
        isCustom = custom;
    }

    public Boolean getLibrary() {
        return isLibrary;
    }

    public void setLibrary(Boolean library) {
        isLibrary = library;
    }

    public Boolean getSimplified() {
        return isSimplified;
    }

    public void setSimplified(Boolean simplified) {
        isSimplified = simplified;
    }

    public SelectItem[] getCategories() {
        return categories;
    }

    public void setCategories(SelectItem[] categories) {
        this.categories = categories;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String isCode() {
        return code;
    }

    public void setStepId(Integer stepId) {
        this.stepId = stepId;
    }

    public Integer getStepId() {
        return stepId;
    }
}
