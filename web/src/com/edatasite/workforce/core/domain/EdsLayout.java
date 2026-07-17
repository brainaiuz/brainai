package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormValidation;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: Hayot
 * Date: 4/5/12
 * Time: 10:35 AM
 * To change this template use File | Settings | File Templates.
 */
@MappedSuperclass
public abstract class EdsLayout extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "layout")
    @Type(type = "text")
    private String layout;

    @Column(name = "formid")
    private String formID;

    @Column(name = "title")
    private String title;

    @Column(name = "active", columnDefinition = "boolean default true")
    private boolean active = false;

    @Column(name = "addForm", columnDefinition = "boolean default false")
    private boolean addForm = false;

    @Column(name = "editForm", columnDefinition = "boolean default false")
    private boolean editForm = false;

    @Column(name = "viewForm", columnDefinition = "boolean default false")
    private boolean viewForm = false;

    @Column(name = "importForm", columnDefinition = "boolean default false")
    private boolean importForm = false;

    @Column(name = "webForm", columnDefinition = "boolean default false")
    private boolean webForm = false;

    @Column(name = "customcss")
    @Type(type = "text")
    private String customCss;

    @Column(name = "validations")
    @Type(type = "text")
    private String validations;

    public EdsLayout() {

    }

    public String getValidations() {
        return validations;
    }

    public void setValidations(String validations) {
        this.validations = validations;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getFormID() {
        return formID;
    }

    public void setFormID(String formID) {
        this.formID = formID;
    }

    public String getCustomCss() {
        return customCss;
    }

    public void setCustomCss(String customCss) {
        this.customCss = customCss;
    }

    public boolean isAddForm() {
        return addForm;
    }

    public void setAddForm(boolean addForm) {
        this.addForm = addForm;
    }

    public boolean isEditForm() {
        return editForm;
    }

    public void setEditForm(boolean editForm) {
        this.editForm = editForm;
    }

    public boolean isWebForm() {
        return webForm;
    }

    public void setWebForm(boolean webForm) {
        this.webForm = webForm;
    }

    public boolean isViewForm() {
        return viewForm;
    }

    public void setViewForm(boolean viewForm) {
        this.viewForm = viewForm;
    }

    public boolean isImportForm() {
        return importForm;
    }

    public void setImportForm(boolean importForm) {
        this.importForm = importForm;
    }

    public static ArrayList<String> toArray(String sections) {
        ArrayList<String> result = new ArrayList<>();
        if (sections != null && !"".equals(sections)) {
            String[] temp = sections.split("\\|");
            result.addAll(Arrays.asList(temp));
        }
        return result;
    }

    public LayoutRPC getRPC() {
        LayoutRPC item = new LayoutRPC();
        item.setObjectID(getObjectID());
        item.setFormID(getFormID());
        item.setLayout(getLayout());
        item.setCustomCss(getCustomCss());
        item.setTitle(getTitle());
        item.setActive(isActive());
        item.setAddForm(isAddForm());
        item.setEditForm(isEditForm());
        item.setViewForm(isViewForm());
        item.setImportForm(isImportForm());
        item.setValidations(CustomFormValidation.parse(getValidations()));
        item.setWebForm(isWebForm());
        return item;
    }
}
