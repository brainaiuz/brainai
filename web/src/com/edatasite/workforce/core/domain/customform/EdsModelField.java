package com.edatasite.workforce.core.domain.customform;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.gwt.core.client.enums.ColumnType;
import com.edatasite.workforce.gwt.core.client.form.CustomizeFormItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelField;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Created by Hayot on 2/7/14.
 */
@MappedSuperclass
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"form_id", "field_id"}),
        @UniqueConstraint(columnNames = {"form_id", "sorder"})})
@Where(clause = "(deleted is null or deleted = 'false')")
public class EdsModelField extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;
    @Column(length = 1000)
    private String label;
    private String customLabel;
    private String form_ID;
    private String field_ID;
    private Integer sorder = 0;
    private String widget;
    private String type = "text";
    @Column(name = "mandatory", nullable = false, columnDefinition = " boolean default false")
    private boolean mandatory = false;
    @Column(name = "systemmandatory", nullable = false, columnDefinition = " boolean default false")
    private boolean systemMandatory = false;
    @Column(name = "hide", nullable = false, columnDefinition = " boolean default false")
    private boolean hide = false;
    @Column(name = "isCustomField", nullable = false, columnDefinition = " boolean default false")
    private boolean isCustomField = false;
    private String section;
    private String fsection;//for new UI
    private String noLabelFor = "";//LayoutRPC.ADD + "@"+ LayoutRPC.EDIT+"@" + LayoutRPC.VIEW ;
    private String noWrapperFor = "";//LayoutRPC.ADD + "@"+ LayoutRPC.EDIT+"@" + LayoutRPC.VIEW ;
    @Column(name = "fullWidth", nullable = false, columnDefinition = " boolean default false")
    private boolean fullWidth = false;
    private String sectionStyle = "";
    private String fieldSetStyle = "";
    private String halfSetStyle = "";
    private String rowStyle = "";
    private String fieldStyle = "";
    @Column(name = "split", nullable = false, columnDefinition = " boolean default false")
    private boolean split = false;
    @Column(name = "source")
    @Type(type = "text")
    private String source;
    @Column(name = "usableByWorkflow", nullable = false, columnDefinition = " boolean default false")
    private boolean usableByWorkflow = false;
    @Column(name = "disableUpdate", nullable = false, columnDefinition = " boolean default false")
    private boolean disableUpdate = false;
    @Column(name = "isEntityField", nullable = false, columnDefinition = " boolean default false")
    private boolean isEntityField = false;

    @Column(name = "defaultvalue")
    @Type(type = "text")
    private String defaultValue;

    @Column(name = "place", columnDefinition = "int4 default 0")
    private Integer place = 0;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(name = "helpMessage")
    @Type(type = "text")
    private String helpMessage;

    @Column(name = "hideInCustomizeForm",nullable = false, columnDefinition = " boolean default false")
    private boolean hideInCustomizeForm = false;
    @Column(name = "systemDisable",nullable = false, columnDefinition = " boolean default false")
    private boolean systemDisable = false;
    @Column(name = "isWorkflowAttribute",nullable = false, columnDefinition = " boolean default false")
    private boolean isWorkflowAttribute = false;
    @Column(name = "customizableTable",nullable = false, columnDefinition = " boolean default false")
    private boolean customizableTable = false;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(32) default 'COL_1'")
    private ColumnType columnType;

    @Column(name = "forder", nullable = false, columnDefinition = "int default 0")
    private Integer forder = 0;

    @Column(nullable = false, columnDefinition = " int default 0")
    private int gridX = 0;

    @Column(nullable = false, columnDefinition = " int default 0")
    private int gridY = 0;

    @Column(nullable = false, columnDefinition = " int default 4")
    private int gridWidth = 4;

    @Column(nullable = false, columnDefinition = " int default 1")
    private int gridHeight = 1;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customFormLocalizationId")
    @NotFound(action = NotFoundAction.IGNORE)
    private EdsCustomFormLocalization customFormLocalization;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referenceId")
    private EdsReference reference;

    private String widgetForBm;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getHelpMessage() {
        return helpMessage;
    }

    public void setHelpMessage(String helpMessage) {
        this.helpMessage = helpMessage;
    }

    public String getCustomLabel() {
        return customLabel;
    }

    public void setCustomLabel(String customLabel) {
        this.customLabel = customLabel;
    }

    public String getField_ID() {
        return field_ID;
    }

    public void setField_ID(String field_ID) {
        this.field_ID = field_ID;
    }

    public Integer getSorder() {
        return sorder;
    }

    public void setSorder(Integer order) {
        this.sorder = order;
    }

    public String getForm_ID() {
        return form_ID;
    }

    public void setForm_ID(String form_ID) {
        this.form_ID = form_ID;
    }

    public String getWidget() {
        return widget;
    }

    public void setWidget(String widgetType) {
        this.widget = widgetType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public boolean isSystemMandatory() {
        return systemMandatory;
    }

    public void setSystemMandatory(boolean systemMandatory) {
        this.systemMandatory = systemMandatory;
    }

    public boolean isHide() {
        return hide;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }

    public boolean isCustomField() {
        return isCustomField;
    }

    public void setCustomField(boolean isCustomField) {
        this.isCustomField = isCustomField;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String group) {
        this.section = group;
    }

    public String getFsection() {
        return fsection;
    }

    public void setFsection(String fsection) {
        this.fsection = fsection;
    }

    /*public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }*/

    public String getNoLabelFor() {
        return noLabelFor;
    }

    public void setNoLabelFor(String noLabelFor) {
        this.noLabelFor = noLabelFor;
    }

    public String getNoWrapperFor() {
        return noWrapperFor;
    }

    public void setNoWrapperFor(String noWrapperFor) {
        this.noWrapperFor = noWrapperFor;
    }

    public boolean isFullWidth() {
        return fullWidth;
    }

    public void setFullWidth(boolean fullWidth) {
        this.fullWidth = fullWidth;
    }

    public String getSource() {
        if (source != null) {
            if (source.contains("-:-")) {
                return source;
            } else {
                return source.replace(",", "-:-").replace(";", "-:-");
            }
        }
        return null;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isEntityField() {
        return isEntityField;
    }

    public void setEntityField(boolean isEntityField) {
        this.isEntityField = isEntityField;
    }

    public boolean isUsableByWorkflow() {
        return usableByWorkflow;
    }

    public void setUsableByWorkflow(boolean usableByWorkflow) {
        this.usableByWorkflow = usableByWorkflow;
    }

    public Integer getPlace() {
        return place;
    }

    public void setPlace(Integer place) {
        this.place = place;
    }

    public boolean isSplit() {
        return split;
    }

    public void setSplit(boolean lastInPart) {
        this.split = lastInPart;
    }

    public boolean isDisableUpdate() {
        return disableUpdate;
    }

    public void setDisableUpdate(boolean enableUpdate) {
        this.disableUpdate = enableUpdate;
    }

    public String getSectionStyle() {
        return sectionStyle;
    }

    public void setSectionStyle(String sectionStyle) {
        this.sectionStyle = sectionStyle;
    }

    public String getFieldSetStyle() {
        return fieldSetStyle;
    }

    public void setFieldSetStyle(String fieldSetStyle) {
        this.fieldSetStyle = fieldSetStyle;
    }

    public String getHalfSetStyle() {
        return halfSetStyle;
    }

    public void setHalfSetStyle(String halfSetStyle) {
        this.halfSetStyle = halfSetStyle;
    }

    public String getRowStyle() {
        return rowStyle;
    }

    public void setRowStyle(String rowStyle) {
        this.rowStyle = rowStyle;
    }

    public String getFieldStyle() {
        return fieldStyle;
    }

    public void setFieldStyle(String fieldStyle) {
        this.fieldStyle = fieldStyle;
    }

    public boolean isHideInCustomizeForm() {
        return hideInCustomizeForm;
    }

    public void setHideInCustomizeForm(boolean hideInCustomizeForm) {
        this.hideInCustomizeForm = hideInCustomizeForm;
    }

    public boolean isSystemDisable() {
        return systemDisable;
    }

    public void setSystemDisable(boolean systemDisable) {
        this.systemDisable = systemDisable;
    }

    public boolean isWorkflowAttribute() {
        return isWorkflowAttribute;
    }

    public void setWorkflowAttribute(boolean workflowAttribute) {
        isWorkflowAttribute = workflowAttribute;
    }

    public ColumnType getColumnType() {
        if (columnType == null) {
            columnType = ColumnType.COL_1;
        }
        return columnType;
    }

    public void setColumnType(ColumnType columnType) {
        this.columnType = columnType;
    }

    public Integer getForder() {
        if (forder == null) {
            forder = 0;
        }
        return forder;
    }

    public void setForder(Integer forder) {
        this.forder = forder;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public int getGridX() {
        return gridX;
    }

    public void setGridX(int gridX) {
        this.gridX = gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public void setGridY(int gridY) {
        this.gridY = gridY;
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public void setGridWidth(int gridWidth) {
        this.gridWidth = gridWidth;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public void setGridHeight(int gridHeight) {
        this.gridHeight = gridHeight;
    }

    public boolean isCustomizableTable() {
        return customizableTable;
    }

    public void setCustomizableTable(boolean customizableTable) {
        this.customizableTable = customizableTable;
    }

    public EdsCustomFormLocalization getCustomFormLocalization() {
        return customFormLocalization;
    }

    public void setCustomFormLocalization(EdsCustomFormLocalization customFormLocalization) {
        this.customFormLocalization = customFormLocalization;
    }

    public ModelField getRPC(ModelField item, String dynamicLabel) {
        if (item == null) {
            item = new ModelField();
        }
        item.setObjectID(getObjectID());
        item.setWidget(getWidget());
        item.setType(getType());
        item.setIsCustomField(isCustomField());
//        item.setCustomLabel(getCustomLabel());
        item.setHide(isHide());
        item.setField_ID(getField_ID());
        item.setForm_ID(getForm_ID());
        item.setMandatory(isMandatory());
        item.setSystemMandatory(isSystemMandatory());
        item.setSection(getSection());
        item.setLabel(getLabel());
        item.setSorder(getSorder());
        item.setNoLabelFor(getNoLabelFor());
//        item.setNoWrapperFor(getNoWrapperFor());
//        item.setFullWidth(isFullWidth());
        item.setCustom(isCustom());
//        item.setHelpMessage(getHelpMessage());
        item.setSource(getSource());
        item.setEntityField(isEntityField());
        item.setUsableByWorkflow(isUsableByWorkflow());
//        item.setPlace(getPlace());
//        item.closePart(isSplit());
        item.setDisableUpdate(isDisableUpdate());
        item.setHideInCustomizeForm(isHideInCustomizeForm());
        item.setDynamicLabel(dynamicLabel);
        item.setWorkflowAttribute(isWorkflowAttribute());
        item.setCustomizableTable(isCustomizableTable());
        item.setReferenceItem(getReference() != null ? getReference().getRPC() : null);
        item.setWidgetForBm(getWidgetForBm());
        return item;
    }

    public ModelField getRPC(ModelField item) {
        return getRPC(item, null);
    }

    protected boolean isCustom() {
        return false;
    }

    public CustomizeFormItem toRpc(String viewName, String internationalization) {
        CustomizeFormItem field = new CustomizeFormItem();
        field.setId(getObjectID());
        field.setSection(getFsection());
        field.setName(getField_ID());
        field.setCustomField(isCustomField());
        field.setUiType(!"UNKNOWN".equals(getWidget()) ? getWidget() : getField_ID());
        field.setDataType(getType());
        field.setActive(!isHide());
        field.setForder(getForder());
        field.setColumnType(getColumnType());
        field.setSystemMandatory(isMandatory());
        field.setNoLabelFor(getNoLabelFor());
        field.setCustom(isCustom());
        field.setEntityName(viewName);
        field.setFormID(getForm_ID());
        field.setDefaultValue(getDefaultValue());
        field.setX(getGridX());
        field.setY(getGridY());
        field.setWidth(getGridWidth());
        field.setHeight(getGridHeight());
        field.setCustomizableTable(isCustomizableTable());

        if (internationalization != null && getCustomFormLocalization() != null) {
            switch (internationalization) {
                case "en" -> field.setLabel(getCustomFormLocalization().getEnglishName());
                case "ar" -> field.setLabel(getCustomFormLocalization().getArabicName());
                case "ru" -> field.setLabel(getCustomFormLocalization().getRussianName());
                case "uz" -> field.setLabel(getCustomFormLocalization().getUzbekName());
            }
        } else {
            field.setLabel(getLabel());
        }
        return field;
    }

    public EdsReference getReference() {
        return reference;
    }

    public void setReference(EdsReference reference) {
        this.reference = reference;
    }

    public String getWidgetForBm() {
        return widgetForBm;
    }

    public void setWidgetForBm(String widgetForBm) {
        this.widgetForBm = widgetForBm;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
