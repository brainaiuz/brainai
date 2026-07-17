package com.edatasite.workforce.core.domain.workflow;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * User : Akhror
 * Date : 10.01.2022
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "web_hook_body")
public class EdsWebHookBody extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Enumerated(value = EnumType.STRING)
    private WebHookBodyType type;

    private String format;
    @Column(name = "raw_text", length = 10000)
    private String rawText;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "body_id")
    private List<EdsWebHookParameter> formDataParams = new ArrayList<>();

    public EdsWebHookBody() {
    }

    public EdsWebHookBody(Integer objectID, WebHookBodyType type, String format, String rawText, List<EdsWebHookParameter> formDataParams) {
        this.objectID = objectID;
        this.type = type;
        this.format = format;
        this.rawText = rawText;
        this.formDataParams = formDataParams;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public WebHookBodyType getType() {
        return type;
    }

    public void setType(WebHookBodyType type) {
        this.type = type;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getRawText() {
        return rawText;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    public List<EdsWebHookParameter> getFormDataParams() {
        return formDataParams;
    }

    public void setFormDataParams(List<EdsWebHookParameter> formDataParams) {
        this.formDataParams = formDataParams;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EdsWebHookBody)) return false;
        if (!super.equals(o)) return false;

        EdsWebHookBody that = (EdsWebHookBody) o;

        if (getObjectID() != null ? !getObjectID().equals(that.getObjectID()) : that.getObjectID() != null)
            return false;
        if (getType() != that.getType()) return false;
        if (getFormat() != null ? !getFormat().equals(that.getFormat()) : that.getFormat() != null) return false;
        if (getRawText() != null ? !getRawText().equals(that.getRawText()) : that.getRawText() != null) return false;
        if (getFormDataParams() != null ? !getFormDataParams().equals(that.getFormDataParams()) : that.getFormDataParams() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getObjectID() != null ? getObjectID().hashCode() : 0);
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        result = 31 * result + (getFormat() != null ? getFormat().hashCode() : 0);
        result = 31 * result + (getRawText() != null ? getRawText().hashCode() : 0);
        result = 31 * result + (getFormDataParams() != null ? getFormDataParams().hashCode() : 0);
        return result;
    }
}
