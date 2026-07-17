package com.edatasite.workforce.core.domain.customform;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.form.IntroductionPageRpc;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "introduction_page")
public class EdsIntroductionPage extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;
    @Column(name = "parent_form_id", unique = true)
    private String parentFormId;
    @Column(name = "form_id")
    private String formId;
    @Column(name = "is_active")
    private Boolean isActive;
    @Column(name = "editor_value")
    @Type(type = "text")
    private String editorValue;
    @Column(name = "ok_button_name")
    private String okButtonName;
    @Column(name = "cancel_button_name")
    private String cancelButtonName;

    public IntroductionPageRpc toRpc() {
        IntroductionPageRpc pageRpc = new IntroductionPageRpc();

        pageRpc.setId(getObjectID());
        pageRpc.setParentFormId(getParentFormId());
        pageRpc.setFormId(getFormId());
        pageRpc.setEditorValue(getEditorValue());
        pageRpc.setCancelButtonName(getCancelButtonName());
        pageRpc.setOkButtonName(getOkButtonName());
        pageRpc.setActive(getActive());

        return pageRpc;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public String getParentFormId() {
        return parentFormId;
    }

    public void setParentFormId(String parentFormId) {
        this.parentFormId = parentFormId;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getEditorValue() {
        return editorValue;
    }

    public void setEditorValue(String editorValue) {
        this.editorValue = editorValue;
    }

    public String getOkButtonName() {
        return okButtonName;
    }

    public void setOkButtonName(String okButtonName) {
        this.okButtonName = okButtonName;
    }

    public String getCancelButtonName() {
        return cancelButtonName;
    }

    public void setCancelButtonName(String cancelButtonName) {
        this.cancelButtonName = cancelButtonName;
    }
}
