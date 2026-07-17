package com.edatasite.workforce.gwt.core.client.rpc.form;

import com.edatasite.workforce.gwt.core.client.enums.ColumnType;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: Hayot
 * Date: 02/08/2014
 * Time: 10:51 AM
 */
public class ModelField implements IsSerializable {
    private Integer objectID;
    private String label;
    private String customLabel;
    private String dynamicLabel;
    private String form_ID;
    private String field_ID;
    private Integer sorder = 0;
    private Integer forder = 0;
    private String widget;
    private String type;
    private boolean systemMandatory;
    private boolean mandatory;
    private boolean hide = false;
    private boolean isCustomField = false;
    private boolean fullWidth = false;
    private String section;
    private String helpMessage;
    private boolean closeSection = false;
    private boolean openSection = false;
    private boolean closePart = false;
    private boolean openPart = false;
    private boolean disableUpdate = false;
    private boolean hideInCustomizeForm = false;
    private boolean isCustom;
    //isEntityField means if this field is shown in entity or it's Form field like(Latest Activities, Contacts, Status History)
    private boolean isEntityField;
    private boolean usableByWorkflow;
    private boolean workflowAttribute;
    private boolean customizableTable;
    private String source;
    private String noLabelFor = "";//LayoutRPC.ADD + "@"+ LayoutRPC.EDIT+"@" + LayoutRPC.VIEW ;
    private String noWrapperFor = "";//LayoutRPC.ADD + "@"+ LayoutRPC.EDIT+"@" + LayoutRPC.VIEW ;
    private Integer place = 0;
    private ColumnType columnType;
    private String defaultValue;
    private ReferenceItem referenceItem;
    private String widgetForBm;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public boolean isEntityField() {
        return usableByWorkflow || isEntityField;
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

    public boolean isWorkflowAttribute() {
        return workflowAttribute;
    }

    public void setWorkflowAttribute(boolean workflowAttribute) {
        this.workflowAttribute = workflowAttribute;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCustomLabel() {
        return customLabel;
    }

    public void setCustomLabel(String customLabel) {
        this.customLabel = customLabel;
    }

    public String getDynamicLabel() {
        return dynamicLabel;
    }

    public void setDynamicLabel(String dynamicLabel) {
        this.dynamicLabel = dynamicLabel;
    }

    public String getForm_ID() {
        return form_ID;
    }

    public void setForm_ID(String form_ID) {
        this.form_ID = form_ID;
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

    public void setSorder(Integer sorder) {
        this.sorder = sorder;
    }

    public String getWidget() {
        return widget;
    }

    public void setWidget(String widget) {
        this.widget = widget;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isMandatory() {
        return mandatory || systemMandatory;
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
        if (isSystemMandatory()) {
            return false;
        }
        return hide;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }

    public boolean isIsCustomField() {
        return isCustomField;
    }

    public void setIsCustomField(boolean isCustomField) {
        this.isCustomField = isCustomField;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getHelpMessage() {
        return helpMessage;
    }

    public void setHelpMessage(String helpMessage) {
        this.helpMessage = helpMessage;
    }

    public String getNoLabelFor() {
        if (noLabelFor == null) {
            setNoLabelFor("");
        }
        return noLabelFor;
    }

    public void setNoLabelFor(String noLabelFor) {
        this.noLabelFor = noLabelFor;
    }

    public String getNoWrapperFor() {
        if (noWrapperFor == null) {
            setNoWrapperFor("");
        }
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

    public boolean isLabelLess(String formType) {
        return formType != null && getNoLabelFor().toLowerCase().contains(formType.toLowerCase());
    }

    public boolean isWrapless(String formType) {
        return formType != null && getNoWrapperFor().toLowerCase().contains(formType.toLowerCase());
    }

    public boolean closeSection() {
        return closeSection;
    }

    public void closeSection(boolean b) {
        closeSection = b;
    }

    public boolean closePart() {
        return closePart;
    }

    public void closePart(boolean b) {
        closePart = b;
    }

    public boolean openSection() {
        return openSection;
    }

    public void openSection(boolean b) {
        openSection = b;
    }

    public boolean openPart() {
        return openPart;
    }

    public void openPart(boolean b) {
        openPart = b;
    }

    public boolean isDisableUpdate() {
        return disableUpdate;
    }

    public void setDisableUpdate(boolean disableUpdate) {
        this.disableUpdate = disableUpdate;
    }

    public boolean isHideInCustomizeForm() {
        return hideInCustomizeForm;
    }

    public void setHideInCustomizeForm(boolean hideInCustomizeForm) {
        this.hideInCustomizeForm = hideInCustomizeForm;
    }

    public boolean isCustom() {
        return isCustom || objectID == null;
    }

    public void setCustom(boolean custom) {
        this.isCustom = custom;
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

    public Integer getPlace() {
        return place;
    }

    public void setPlace(Integer place) {
        this.place = place;
    }

    public ReferenceItem asReferenceItem() {
        return new ReferenceItem(getObjectID(), getField_ID(), getField_ID());
    }

    public ColumnType getColumnType() {
        return columnType;
    }

    public void setColumnType(ColumnType columnType) {
        this.columnType = columnType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isCustomizableTable() {
        return customizableTable;
    }

    public void setCustomizableTable(boolean customizableTable) {
        this.customizableTable = customizableTable;
    }

    public ReferenceItem getReferenceItem() {
        return referenceItem;
    }

    public void setReferenceItem(ReferenceItem referenceItem) {
        this.referenceItem = referenceItem;
    }

    public Integer getForder() {
        return forder;
    }

    public void setForder(Integer forder) {
        this.forder = forder;
    }

    public String getWidgetForBm() {
        return widgetForBm;
    }

    public void setWidgetForBm(String widgetForBm) {
        this.widgetForBm = widgetForBm;
    }

    public interface SOURCE {
        String REFERENCE = "REFERENCE@";
        String COUNTRY = "COUNTRY";
        String STATE = "STATE";

        interface CRM {
            String CRM = "CRM@";
            String CRM_LEAD = "CRM@LEAD";
            String CRM_CONTACT = "CRM@CONTACT";
            String CRM_ACCOUNT = "CRM@ACCOUNT";
            String CRM_OPPORTUNITY = "CRM@OPPORTUNITY";
            String CRM_CAMPAIGN = "CRM@CAMPAIGN";
            String CRM_CASE = "CRM@CASE";
            String CRM_EVENT = "CRM@EVENT";
            String CRM_EMPLOYEE = "CRM@EMPLOYEE";
            String CRM_LEAD_ASSIGNEE = "CRM@LEAD_ASSIGNEE";
            String CRM_CASE_ASSIGNEE = "CRM@CASE_ASSIGNEE";
            String CRM_CASE_RESOLVER = "CRM@CASE_RESOLVER";
            String CRM_OPPORTUNITY_TYPE = "CRM@OPPORTUNITY_TYPE";
            String CRM_OPPORTUNITY_LEAD_SOURCE = "CRM@OPPORTUNITY_LEAD_SOURCE";
            String CRM_OPPORTUNITY_ASSIGNEE = "CRM@OPPORTUNITY_ASSIGNEE";
            String CRM_OPPORTUNITY_BACKUP_ASSIGNEE = "CRM@OPPORTUNITY_BACKUP_ASSIGNEE";
            String CRM_OPPORTUNITY_STAGE = "CRM@OPPORTUNITY_STAGE";
            String CRM_CATEGORY = "CRM@CATEGORY";
            String CRM_PAYMENT_METHOD = "CRM@PAYMENT_METHOD";
            String CRM_ACCOUNT_OWNER = "CRM@ACCOUNT_OWNER";
            String CRM_ITEM_TABLE_BRAND = "CRM@ITEM_BRAND";
            String CRM_ITEM_TABLE_CATEGORY = "CRM@ITEM_CATEGORY";
            String CRM_ITEM_TABLE_PROJECT = "CRM@ITEM_PROJECT";
            String CRM_ITEM_TABLE_PRODUCT = "CRM@ITEM_PRODUCT";
            String CRM_ITEM_TABLE_SUPPLIER = "CRM@ITEM_CLIENT";
        }

        interface PM {
            String PM = "PM@";
            String PM_PROJECT_CLIENT = "PM@PROJECT_CLIENT";
            String PM_PROJECT_MANAGER = "PM@PROJECT_MANAGER";

            String PM_TASK_PROJECT = "PM@TASK_PROJECT";
        }

        interface TRAINING_CENTER {
            String TC = "TC@";
            String TC_LOCATION = "TC@LOCATION";
            String TC_COURSE = "TC@COURSE";
            String TC_LANGUAGE = "TC@LANGUAGE";
            String TC_INSTRUCTOR = "TC@INSTRUCTOR";
            String TC_ASSESSOR = "TC@ASSESSOR";
        }

        interface HRMS {
            String HRMS = "HRMS@";
            String HRMS_CANDIDATE = "CRM@CANDIDATE";
            String HRMS_SUPERVISOR = "HRMS@SUPERVISOR";
            String HRMS_DEPARTMENT = "HRMS@DEPARTMENT";
            String HRMS_LOCATION = "HRMS@LOCATION";
            String HRMS_POSITION = "HRMS@POSITION";
            String HRMS_CERTIFICATE_TYPE = "HRMS@CERTIFICATE_TYPE";
            String HRMS_ROLES = "HRMS@ROLES";
            String HRMS_EMBASSY = "HRMS@EMBASSY";
            String HRMS_JOB_FAMILY = "HRMS@JOB_FAMILY";
            String HRMS_PROJECT = "HRMS@PROJECT";
            String HRMS_MANAGER = "HRMS@MANAGER";
            String HRMS_LEAVE_REASONS = "HRMS@LEAVEREASONS";
        }

        interface ACCOUNTING {
            String ACCOUNTING = "ACCOUNTING@";
            String ACCOUNTING_SALE_INVOICE_TYPE = "ACCOUNTING@SALE_INVOICE_TYPE";
            String ACCOUNTING_SALE_INVOICE_AMOUNT = "ACCOUNTING@SALE_INVOICE_AMOUNT";
            String ACCOUNTING_BANK_ACCOUNT = "ACCOUNTING@BANK_ACCOUNT";
            String ACCOUNTING_SALE_QUOTE_CUSTOMER = "ACCOUNTING@SALE_QUOTE_CUSTOMER";
            String ACCOUNTING_SALE_QUOTE_PROJECT = "ACCOUNTING@SALE_QUOTE_PROJECT";
            String ACCOUNTING_SALE_QUOTE_MANAGER = "ACCOUNTING@SALE_QUOTE_MANAGER";
            String ACCOUNTING_SALE_QUOTE_SHIP_VIA = "ACCOUNTING@SALE_QUOTE_SHIP_VIA";
            String ACCOUNTING_SALE_QUOTE_STATUS = "ACCOUNTING@SALE_QUOTE_STATUS";

            String ACCOUNTING_PURCHASE_ORDER_SUPPLIER = "ACCOUNTING@PURCHASE_ORDER_SUPPLIER";
            String ACCOUNTING_PURCHASE_ORDER_PROJECT = "ACCOUNTING@PURCHASE_ORDER_PROJECT";
            String ACCOUNTING_PURCHASE_ORDER_STATUS = "ACCOUNTING@PURCHASE_ORDER_STATUS";
            String ACCOUNTING_PURCHASE_ORDER_SALE_QUOTE = "ACCOUNTING@PURCHASE_ORDER_SALE_QUOTE";

            String ACCOUNTING_PURCHASE_INVOICE_STATUS = "ACCOUNTING@PURCHASE_INVOICE_STATUS";

            String ACCOUNTING_EXPENSE_CLAIM_STATUS = "ACCOUNTING@EXPENSE_CLAIM_STATUS";
            String ACCOUNTING_EXPENSE_CLAIM_PROJECT = "ACCOUNTING@EXPENSE_CLAIM_PROJECT";

            String ACCOUNTING_SALE_INVOICE_STATUS = "ACCOUNTING@SALE_INVOICE_STATUS";
            String ACCOUNTING_CURRENCY = "ACCOUNTING@CURRENCY";

            String ACCOUNTING_PRODUCT_CATEGORY = "ACCOUNTING@PRODUCT_CATEGORY";
            String ACCOUNTING_PRODUCT_BRAND = "ACCOUNTING@PRODUCT_BRAND";
            String ACCOUNTING_COGS_ACCOUNT = "ACCOUNTING@COGS_ACCOUNT";
            String ACCOUNTING_ASSET_ACCOUNT = "ACCOUNTING@ASSET_ACCOUNT";
            String ACCOUNTING_CLIENT_INVOICE_TERM = "ACCOUNTING@CLIENT_INVOICE_TERM";
            String ACCOUNTING_MEMORIZED_TRANSACTION = "ACCOUNTING@MEMORIZED_TRANSACTION";
            String ACCOUNTING_RFQ_PROJECT = "ACCOUNTING@RFQ_PROJECT";
            String ACCOUNTING_RFQ_CUSTOMER = "ACCOUNTING@RFQ_CUSTOMER";
            String ACCOUNTING_RENTAL_PRODUCT_BRAND = "ACCOUNTING@RENTAL_PRODUCT_BRAND";
        }

        interface PAYROLL {
            String PAYROLL = "PAYROLL@";
            String PAYROLL_CASH_ADVANCE_REQUESTER = "PAYROLL@CASH_ADVANCE_REQUESTER";
            String PAYROLL_CASH_ADVANCE_CATEGORY = "PAYROLL@CASH_ADVANCE_CATEGORY";

            String PAYROLL_CASH_ADVANCE_CATEGORY_DEDUCTION = "Deduction";
        }
    }

    public interface TYPE {
        String INTEGER = Constants.DATA_TYPE_NUMBER;
        String STRING = Constants.DATA_TYPE_TEXT;
        String DATE = Constants.DATA_TYPE_DATE;
    }

}
