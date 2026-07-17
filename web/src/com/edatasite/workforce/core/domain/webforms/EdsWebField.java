package com.edatasite.workforce.core.domain.webforms;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.webforms.client.WebFormConstants;
import com.edatasite.workforce.gwt.webforms.client.forms.WebField;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Jul 28, 2010
 * Time: 9:29:41 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "webfield")
public class EdsWebField extends EdsObject implements WebFormConstants {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "webform_id")
    private EdsWebForm webForm;


    @Column(name = "originallabel")
    private String originalLabel;

    @Column(name = "label")
    private String label;


    @Column(name = "type")
    private Integer type;


    @Column(name = "defaultvalue")
    @Type(type = "text")
    private String defaultValue;


    @Column(name = "mandatory")
    private Boolean mandatory;


    @Column(name = "unchangable")
    private Boolean unchangable = false;


    @Column(name = "showinform")
    private Boolean showInForm;


    @Column(name = "savingfield")
    private Integer savingField;


    @Column(name = "drawline")
    private Boolean drawLine = false;


    @Column(name = "isCustomField")
    private Boolean isCustomField = false;


    @Column(name = "addgrouptitle")
    private String groupTitle;


    @Column(name = "sortorder")
    private Integer sortOrder;

    @Column(name = "onlyIntegerAllowed")
    private Boolean onlyIntegerAllowed = false;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsWebForm getWebForm() {
        return webForm;
    }

    public void setWebForm(EdsWebForm webForm) {
        this.webForm = webForm;
    }

    public String getOriginalLabel() {
        return originalLabel;
    }

    public void setOriginalLabel(String originalLabel) {
        this.originalLabel = originalLabel;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSavingField() {
        return savingField;
    }

    public void setSavingField(Integer savingField) {
        this.savingField = savingField;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Boolean getMandatory() {
        return mandatory;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    public Boolean getShowInForm() {
        return showInForm;
    }

    public void setShowInForm(Boolean showInForm) {
        this.showInForm = showInForm;
    }

    public Boolean isUnchangable() {
        return unchangable;
    }

    public void setUnchangable(Boolean unchangable) {
        this.unchangable = unchangable;
    }

    public Boolean getDrawLine() {
        return drawLine;
    }

    public void setDrawLine(Boolean drawLine) {
        this.drawLine = drawLine;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getCustomField() {
        return isCustomField;
    }

    public void setCustomField(Boolean customField) {
        isCustomField = customField;
    }

    public boolean isOnlyIntegerAllowed() {
        return onlyIntegerAllowed != null ? onlyIntegerAllowed : false;
    }

    public void setOnlyIntegerAllowed(boolean onlyIntegerAllowed) {
        this.onlyIntegerAllowed = onlyIntegerAllowed;
    }

    public WebField getRPC(WebField webField) {
        WebField item = new WebField();
        if (webField != null) {
            item = webField;
        }
        item.setObjectID(getObjectID());
        item.setSavingField(getSavingField());
        item.setLabel(getLabel());
        item.setOriginalLabel(getOriginalLabel());
        item.setDefaultValue(getDefaultValue());
        item.setMandatory(getMandatory());
        item.setShowInForm(getShowInForm());
        item.setType(getType());
        item.setUnchangable(isUnchangable() == null ? false : isUnchangable());
        item.setDrawLine(getDrawLine() != null && getDrawLine());
        item.setGroupTitle(getGroupTitle());
        item.setCustomField(getCustomField() != null && getCustomField());
        item.setSortOrder(getSortOrder());
        item.setOnlyIntegerAllowed(isOnlyIntegerAllowed());
        return item;
    }
}
