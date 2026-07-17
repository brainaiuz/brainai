package com.edatasite.workforce.core.domain.customform;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelForm;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Created by Hayot on 2/7/14.
 */
@MappedSuperclass
public class EdsModel extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String formID;
    private boolean active = true;
    private String title;// tilte
    private String viewName;// tilte
    @Column(name = "stepForm", columnDefinition = " boolean default false")
    private boolean stepForm = false;
    @Column(name = "customForm", columnDefinition = " boolean default false")
    private boolean customForm = false;
    @Column(name = "certificateForm", columnDefinition = " boolean default false")
    private boolean certificateForm = false;
    @Column(name = "quizForm", columnDefinition = " boolean default false")
    private boolean quizForm = false;
    @Column(name = "anonymousForm", columnDefinition = " boolean default false")
    private boolean anonymousForm = false;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public String getFormID() {
        return formID;
    }

    public void setFormID(String formID) {
        this.formID = formID;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isStepForm() {
        return stepForm;
    }

    public void setStepForm(boolean stepForm) {
        this.stepForm = stepForm;
    }

    public boolean isCustomForm() {
        return customForm;
    }

    public void setCustomForm(boolean customForm) {
        this.customForm = customForm;
    }

    public boolean isCertificateForm() {
        return certificateForm;
    }

    public void setCertificateForm(boolean certificateForm) {
        this.certificateForm = certificateForm;
    }

    public ModelForm getRPC(ModelForm item) {
        if (item == null) {
            item = new ModelForm();
        }
        item.setTitle(getTitle());
        item.setActive(isActive());
        item.setFormID(getFormID());
        item.setObjectID(getObjectID());
        item.setCustom(isCustom());
        item.setViewName(getViewName());
        item.setStepForm(isStepForm());
        item.setCustomForm(isCustomForm());
        item.setCertificateForm(isCertificateForm());
        item.setQuizForm(isQuizForm());
        return item;
    }

    protected boolean isCustom() {
        return false;
    }

    public EdsModelCustom convertToCustom() {
        EdsModelCustom custom = new EdsModelCustom();
        custom.setTitle(getTitle());
        custom.setViewName(getViewName());
        custom.setFormID(getFormID());
        custom.setActive(isActive());
        custom.setStepForm(isStepForm());
        custom.setCertificateForm(isCertificateForm());
        return custom;
    }

    public boolean isQuizForm() {
        return quizForm;
    }

    public void setQuizForm(boolean quizForm) {
        this.quizForm = quizForm;
    }

    public boolean isAnonymousForm() {
        return anonymousForm;
    }

    public void setAnonymousForm(boolean anonymousForm) {
        this.anonymousForm = anonymousForm;
    }
}
