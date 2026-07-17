package com.edatasite.workforce.core.domain.customform;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFormLocalization;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "customFormLocalization")
public class EdsCustomFormLocalization extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Type(type = "text")
    @Column(name = "defaultName", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String defaultName;

    @Type(type = "text")
    @Column(name = "englishName", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String englishName;

    @Type(type = "text")
    @Column(name = "russianName", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String russianName;

    @Type(type = "text")
    @Column(name = "uzbekName", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String uzbekName;

    @Type(type = "text")
    @Column(name = "arabicName", length = Constants.DEFAULT_TEXT_AREA_LIMIT)
    private String arabicName;

    @Column(name = "form_id")
    private String formId;

    @Column(name = "type")
    private Integer type;

    @Column(name = "section")
    private String section;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId")
    private EdsCustomFormLocalization parent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    @Where(clause = "deleted = 'false' or deleted is null")
    private List<EdsCustomFormLocalization> children = new ArrayList<>();

    @Column(name = "deleted", columnDefinition = "boolean default false")
    private Boolean deleted;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getRussianName() {
        return russianName;
    }

    public void setRussianName(String russianName) {
        this.russianName = russianName;
    }

    public String getUzbekName() {
        return uzbekName;
    }

    public void setUzbekName(String uzbekName) {
        this.uzbekName = uzbekName;
    }

    public String getArabicName() {
        return arabicName;
    }

    public void setArabicName(String arabicName) {
        this.arabicName = arabicName;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public EdsCustomFormLocalization getParent() {
        return parent;
    }

    public void setParent(EdsCustomFormLocalization parent) {
        this.parent = parent;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public List<EdsCustomFormLocalization> getChildren() {
        return children;
    }

    public void setChildren(List<EdsCustomFormLocalization> children) {
        this.children = children;
    }

    public CustomFormLocalization getRPC() {
        CustomFormLocalization customFormLocalization = new CustomFormLocalization();
        customFormLocalization.setId(getObjectID());
        customFormLocalization.setDefaultName(getDefaultName());
        customFormLocalization.setEnglishName(getEnglishName());
        customFormLocalization.setArabicName(getArabicName());
        customFormLocalization.setRussianName(getRussianName());
        customFormLocalization.setUzbekName(getUzbekName());
        customFormLocalization.setFormId(getFormId());
        customFormLocalization.setLocalizedName(getNameLocalizationWithoutDefaultName(ServerUtils.getUserLocale().getLanguage()));
        customFormLocalization.setType(getType());
        customFormLocalization.setSection(getSection());
        if (getChildren() != null && getChildren().size() > 0) {
            ArrayList<CustomFormLocalization> children = new ArrayList<>();
            getChildren().forEach(child -> {
                children.add(child.getRPC());
            });
            customFormLocalization.setChildren(children);
        }
        return customFormLocalization;
    }

    public String getNameLocalization(String internationalization) {
        String result = "";
        if (internationalization != null) {
            result = switch (internationalization) {
                case "en" -> getEnglishName();
                case "ar" -> getArabicName();
                case "ru" -> getRussianName();
                case "uz" -> getUzbekName();
                default -> getDefaultName();
            };
        }
        return result == null || result.isEmpty() ? getDefaultName() : result;
    }

    public String getNameLocalizationWithoutDefaultName(String internationalization) {
        if (internationalization != null) {
            return switch (internationalization) {
                case "en" -> getEnglishName();
                case "ar" -> getArabicName();
                case "ru" -> getRussianName();
                case "uz" -> getUzbekName();
                default -> null;
            };
        }
        return null;
    }

    public String getNameValueByDefaultString(String defStr, String lang) {
        return !ServerUtils.isNullOrEmpty(defStr)
                && (defStr.equals(getEnglishName()) || defStr.equals(getArabicName())
                || defStr.equals(getUzbekName()) || defStr.equals(getRussianName())
                || defStr.equals(getDefaultName()))
                ? getNameLocalization(lang) : null;
    }
}
