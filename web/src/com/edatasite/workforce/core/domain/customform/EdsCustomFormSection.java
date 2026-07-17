package com.edatasite.workforce.core.domain.customform;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.form.DynamicSectionsRpc;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "customformsection")
public class EdsCustomFormSection extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;
    private String form_ID;
    private String section;
    private Integer sorder;
    @Column(name = "active", columnDefinition = " boolean default true")
    private Boolean active = true;

    @Column(name = "expanded", columnDefinition = " boolean default false")
    private Boolean expanded = false;

    @Column(name = "custom", columnDefinition = " boolean default false")
    private Boolean custom = false;

    private String label;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customFormLocalizationId")
    @NotFound(action = NotFoundAction.IGNORE)
    private EdsCustomFormLocalization edsCustomFormLocalization;

    @Column(name = "isPagination")
    private Boolean isPagination = false;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getForm_ID() {
        return form_ID;
    }

    public void setForm_ID(String form_ID) {
        this.form_ID = form_ID;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public Integer getSorder() {
        return sorder;
    }

    public void setSorder(Integer sorder) {
        this.sorder = sorder;
    }

    public Boolean isActive() {
        if (active == null) {
            active = Boolean.TRUE;
        }
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getCustom() {
        if (custom == null) {
            custom = false;
        }
        return custom;
    }

    public void setCustom(Boolean custom) {
        this.custom = custom;
    }

    public Boolean getExpanded() {
        if (expanded == null) {
            expanded = Boolean.FALSE;
        }
        return expanded;
    }

    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public EdsCustomFormLocalization getEdsCustomFormLocalization() {
        return edsCustomFormLocalization;
    }

    public void setEdsCustomFormLocalization(EdsCustomFormLocalization edsCustomFormLocalization) {
        this.edsCustomFormLocalization = edsCustomFormLocalization;
    }

    public DynamicSectionsRpc toRpc(String internationalization) {
        DynamicSectionsRpc rpc = new DynamicSectionsRpc(getObjectID(), getSection());
        rpc.setFormID(getForm_ID());
        rpc.setActive(isActive());
        rpc.setSorder(getSorder());
        rpc.setCustom(getCustom());
        rpc.setExpanded(getExpanded());
        rpc.setPagination(getPagination() != null && getPagination());
        if (internationalization != null && getEdsCustomFormLocalization() != null) {
            switch (internationalization) {
                case "en" ->
                        rpc.setLabel(getEdsCustomFormLocalization().getEnglishName() != null ? getEdsCustomFormLocalization().getEnglishName() : getEdsCustomFormLocalization().getDefaultName());
                case "ar" ->
                        rpc.setLabel(getEdsCustomFormLocalization().getArabicName() != null ? getEdsCustomFormLocalization().getArabicName() : getEdsCustomFormLocalization().getDefaultName());
                case "ru" ->
                        rpc.setLabel(getEdsCustomFormLocalization().getRussianName() != null ? getEdsCustomFormLocalization().getRussianName() : getEdsCustomFormLocalization().getDefaultName());
                case "uz" ->
                        rpc.setLabel(getEdsCustomFormLocalization().getUzbekName() != null ? getEdsCustomFormLocalization().getUzbekName() : getEdsCustomFormLocalization().getDefaultName());
            }
        } else {
            rpc.setLabel(getLabel());
        }
        return rpc;
    }

    public Boolean getPagination() {
        return isPagination;
    }

    public void setPagination(Boolean pagination) {
        isPagination = pagination;
    }
}
