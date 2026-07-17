package com.edatasite.workforce.rest.v2.release10.core;

import com.edatasite.workforce.core.domain.EdsCompanyCustomFieldsSettings;
import com.edatasite.workforce.core.domain.EdsFormProperty;
import com.edatasite.workforce.core.domain.EdsNoteHistory;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormSection;
import com.edatasite.workforce.core.domain.customform.EdsModelField;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.contact.server.app.ContactServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelField;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.CompanyCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CustomFormSectionManager;
import com.edatasite.workforce.gwt.core.server.db.FormPropertyManager;
import com.edatasite.workforce.gwt.core.server.db.ModelFieldManager;
import com.edatasite.workforce.gwt.core.server.db.NoteHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.crm.client.rpc.EventItem;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityListItem;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.profile.server.app.ProfileServiceLocal;
import com.edatasite.workforce.gwt.task.server.app.TaskServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseItemsListData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseListData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.customfield.CustomFieldCategoryChooseTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.customfield.CustomFieldDateTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.customfield.CustomFieldFileUploadTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.customfield.CustomFieldListResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.customfield.CustomFieldListTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.customfield.CustomFieldMultiplyChooseObjectTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.customfield.CustomFieldNumberTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.customfield.CustomFieldTextTO;
import com.edatasite.workforce.rest.v2.release10.core.to.pm.task.CustomFieldsTO;
import com.edatasite.workforce.rest.v2.release10.enums.CustomFieldCategoryEnum;
import com.edatasite.workforce.rest.v2.release10.enums.CustomFieldTypeEnum;
import com.edatasite.workforce.rest.v2.release10.enums.EntityTypeEnum;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.*;

@Tag(name = "CustomField", description = "Custom Field API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
@Validated
public class ApiCustomFieldControllerV2 extends BaseApiControllerV2 {
    private static final Logger log = LoggerFactory.getLogger(ApiCustomFieldControllerV2.class);
    private static final HashMap<String, String> MOBILE_FIXED_FIELDS = new HashMap<>();
    private static final HashMap<String, String> MOBILE_FIXED_SECTIONS = new HashMap<>();
    private static final HashMap<String, String> MOBILE_EXCLUDED_FIELDS = new HashMap<>();

    static {
        //Lead fields for mobile app
        MOBILE_FIXED_FIELDS.put(LayoutRPC.LEAD_FORM + "_" + FIRST_NAME, FIRST_NAME);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.LEAD_FORM + "_" + LAST_NAME, LAST_NAME);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.LEAD_FORM + "_" + STATUS, STATUS);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.LEAD_FORM + "_" + PHONE, PHONE);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.LEAD_FORM + "_" + EMAIL, EMAIL);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.LEAD_FORM + "_" + CRM_ACCOUNT_NAME, CRM_ACCOUNT_NAME);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.LEAD_FORM + "_" + EMAIL_OPT_OUT, EMAIL_OPT_OUT);//to not return this field
        //Opportunity fields for mobile app
        MOBILE_FIXED_FIELDS.put(LayoutRPC.OPPORTUNITY_FORM + "_" + CRM_OPPORTUNITY_ASSIGNEE, CRM_OPPORTUNITY_ASSIGNEE);
//        MOBILE_FIXED_FIELDS.put(LayoutRPC.OPPORTUNITY_FORM + "_" + CRM_OPPORTUNITY_BACKUP_ASSIGNEE, CRM_OPPORTUNITY_BACKUP_ASSIGNEE);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.OPPORTUNITY_FORM + "_" + CRM_OPPORTUNITY_NAME, CRM_OPPORTUNITY_NAME);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.OPPORTUNITY_FORM + "_" + CRM_OPPORTUNITY_AMOUNT, CRM_OPPORTUNITY_AMOUNT);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.OPPORTUNITY_FORM + "_" + CURRENCY, CURRENCY);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.OPPORTUNITY_FORM + "_" + CRM_OPPORTUNITY_CLOSING_DATE, CRM_OPPORTUNITY_CLOSING_DATE);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.OPPORTUNITY_FORM + "_" + CRM_OPPORTUNITY_ACCOUNT_NAME, CRM_OPPORTUNITY_ACCOUNT_NAME);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.OPPORTUNITY_FORM + "_" + CRM_OPPORTUNITY_CONTACT_NAME, CRM_OPPORTUNITY_CONTACT_NAME);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.OPPORTUNITY_FORM + "_" + CRM_OPPORTUNITY_STAGE, CRM_OPPORTUNITY_STAGE);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.OPPORTUNITY_FORM + "_" + GET_PRODUCT, GET_PRODUCT);//to not return this field
        //Contacts fields for mobile app
        MOBILE_FIXED_FIELDS.put(LayoutRPC.CONTACT_FORM + "_" + FIRST_NAME, FIRST_NAME);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.CONTACT_FORM + "_" + LAST_NAME, LAST_NAME);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.CONTACT_FORM + "_" + PHONE, PHONE);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.CONTACT_FORM + "_" + EMAIL, EMAIL);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.CONTACT_FORM + "_" + CRM_ACCOUNT_NAME, CRM_ACCOUNT_NAME);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.CONTACT_FORM + "_" + EMAIL_OPT_OUT, EMAIL_OPT_OUT);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.CONTACT_FORM + "_" + REPORTS_TO, REPORTS_TO);
        //CrmAccount fields for mobile app
        MOBILE_FIXED_FIELDS.put(LayoutRPC.ACCOUNT_FORM + "_" + CRM_ACCOUNT_OWNER, CRM_ACCOUNT_OWNER);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.ACCOUNT_FORM + "_" + CRM_ACCOUNT_NAME, CRM_ACCOUNT_NAME);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.ACCOUNT_FORM + "_" + CRM_ACCOUNT_PARENT, CRM_ACCOUNT_PARENT);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.ACCOUNT_FORM + "_" + CRM_ACCOUNT_TYPE, CRM_ACCOUNT_TYPE);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.ACCOUNT_FORM + "_" + PRIMARY_CONTACT, PRIMARY_CONTACT);

        //Customer fields for mobile app
        MOBILE_FIXED_FIELDS.put(LayoutRPC.CLIENT_FORM + "_" + CRM_ACCOUNT_OWNER, CRM_ACCOUNT_OWNER);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.CLIENT_FORM + "_" + CRM_ACCOUNT_NAME, CRM_ACCOUNT_NAME);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.CLIENT_FORM + "_" + CRM_ACCOUNT_PARENT, CRM_ACCOUNT_PARENT);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.CLIENT_FORM + "_" + CRM_ACCOUNT_TYPE, CRM_ACCOUNT_TYPE);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.CLIENT_FORM + "_" + PRIMARY_CONTACT, PRIMARY_CONTACT);

        //Supplier fields for mobile app
        MOBILE_FIXED_FIELDS.put(LayoutRPC.SUPPLIER_FORM + "_" + CRM_ACCOUNT_OWNER, CRM_ACCOUNT_OWNER);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.SUPPLIER_FORM + "_" + CRM_ACCOUNT_NAME, CRM_ACCOUNT_NAME);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.SUPPLIER_FORM + "_" + CRM_ACCOUNT_PARENT, CRM_ACCOUNT_PARENT);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.SUPPLIER_FORM + "_" + CRM_ACCOUNT_TYPE, CRM_ACCOUNT_TYPE);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.SUPPLIER_FORM + "_" + PRIMARY_CONTACT, PRIMARY_CONTACT);

        //Task fields for mobile app
        MOBILE_FIXED_FIELDS.put(LayoutRPC.TASK_MAX_FORM + "_" + TASK.PROJECT, TASK.PROJECT);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.TASK_MAX_FORM + "_" + NAME, NAME);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.TASK_MAX_FORM + "_" + NUMBER, NUMBER);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.TASK_MAX_FORM + "_" + DESCRIPTION, DESCRIPTION);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.TASK_MAX_FORM + "_" + START_DATE, START_DATE);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.TASK_MAX_FORM + "_" + DUE_DATE, DUE_DATE);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.TASK_MAX_FORM + "_" + PRIORITY, PRIORITY);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.TASK_MAX_FORM + "_" + STATUS, STATUS);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.TASK_MAX_FORM + "_" + TASK.BILLIBLE, TASK.BILLIBLE);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.TASK_MAX_FORM + "_" + ASSIGNEE, ASSIGNEE);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.TASK_MAX_FORM + "_" + WORKSTREAM.PARENT_WORKSTREAM, WORKSTREAM.PARENT_WORKSTREAM);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.TASK_MAX_FORM + "_" + WORKSTREAM.DUE_DATE_REMINDER, WORKSTREAM.DUE_DATE_REMINDER);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.TASK_MAX_FORM + "_" + TASK.PREDECESSOR_TASK, TASK.PREDECESSOR_TASK);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.TASK_MAX_FORM + "_" + TASK.SUCCESSOR_TASK, TASK.SUCCESSOR_TASK);
        MOBILE_FIXED_FIELDS.put(LayoutRPC.TASK_MAX_FORM + "_" + RECALCULATE_HOURS_ON_RESOURCE_UTIL, RECALCULATE_HOURS_ON_RESOURCE_UTIL);

        //Fixed sections
        MOBILE_FIXED_SECTIONS.put(LayoutRPC.LEAD_FORM + "_" + ADDRESS_INFORMATION, ADDRESS_INFORMATION);
        MOBILE_FIXED_SECTIONS.put(LayoutRPC.CONTACT_FORM + "_" + ADDRESS_INFORMATION, ADDRESS_INFORMATION);
        MOBILE_FIXED_SECTIONS.put(LayoutRPC.ACCOUNT_FORM + "_" + CRM_ACCOUNT_ADDRESS_INFORMATION, CRM_ACCOUNT_ADDRESS_INFORMATION);
        MOBILE_FIXED_SECTIONS.put(LayoutRPC.CLIENT_FORM + "_" + CRM_ACCOUNT_ADDRESS_INFORMATION, CRM_ACCOUNT_ADDRESS_INFORMATION);
        MOBILE_FIXED_SECTIONS.put(LayoutRPC.SUPPLIER_FORM + "_" + CRM_ACCOUNT_ADDRESS_INFORMATION, CRM_ACCOUNT_ADDRESS_INFORMATION);

        //Excluded fields
        //---Lead form
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.LEAD_FORM + "_" + CRM_NOTE, CRM_NOTE);
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.LEAD_FORM + "_" + ATTACHMENTS, ATTACHMENTS);

        //--Task form
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.TASK_MAX_FORM + "_" + TASK.TASK_NOTE, TASK.TASK_NOTE);

        //--Contact form
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.CONTACT_FORM + "_" + CRM_NOTE, CRM_NOTE);
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.CONTACT_FORM + "_" + ATTACHMENTS, ATTACHMENTS);

        //--Opportunity form
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.OPPORTUNITY_FORM + "_" + CRM_NOTE, CRM_NOTE);
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.OPPORTUNITY_FORM + "_" + CRM_OPPORTUNITY_ATTACHMENTS, CRM_OPPORTUNITY_ATTACHMENTS);
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.OPPORTUNITY_FORM + "_" + CRM_OPPORTUNITY_EXPECTED_REVENUE, CRM_OPPORTUNITY_EXPECTED_REVENUE);
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.OPPORTUNITY_FORM + "_" + CRM_OPPORTUNITY_PROBABILITY, CRM_OPPORTUNITY_PROBABILITY);

        //--Account form
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.ACCOUNT_FORM + "_" + CRM_NOTE, CRM_NOTE);
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.ACCOUNT_FORM + "_" + ATTACHMENTS, ATTACHMENTS);
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.ACCOUNT_FORM + "_" + CURRENCY, CURRENCY);
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.ACCOUNT_FORM + "_" + VAT_NUMBER, VAT_NUMBER);
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.ACCOUNT_FORM + "_" + PAYMENT_METHOD, PAYMENT_METHOD);
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.ACCOUNT_FORM + "_" + REGISTRATION_NUMBER, REGISTRATION_NUMBER);
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.ACCOUNT_FORM + "_" + CLIENT_INVOICE_TERM, CLIENT_INVOICE_TERM);

        //--Client form
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.CLIENT_FORM + "_" + CRM_NOTE, CRM_NOTE);
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.CLIENT_FORM + "_" + ATTACHMENTS, ATTACHMENTS);
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.CLIENT_FORM + "_" + CLIENT_BANK_ACCOUNT, CLIENT_BANK_ACCOUNT);
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.CLIENT_FORM + "_" + CURRENCY, CURRENCY);
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.CLIENT_FORM + "_" + VAT_NUMBER, VAT_NUMBER);
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.CLIENT_FORM + "_" + PAYMENT_METHOD, PAYMENT_METHOD);
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.CLIENT_FORM + "_" + REGISTRATION_NUMBER, REGISTRATION_NUMBER);
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.CLIENT_FORM + "_" + CLIENT_INVOICE_TERM, CLIENT_INVOICE_TERM);
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.CLIENT_FORM + "_" + CLIENT_VAT, CLIENT_VAT);
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.CLIENT_FORM + "_" + CLIENT_DISCOUNT, CLIENT_DISCOUNT);
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.CLIENT_FORM + "_" + ACCOUNTS_RECEIVABLE_PAYABLE, ACCOUNTS_RECEIVABLE_PAYABLE);
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.CLIENT_FORM + "_" + GL_ACCOUNT, GL_ACCOUNT);
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.CLIENT_FORM + "_" + CLIENT_AS_OF_DATE, CLIENT_AS_OF_DATE);
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.CLIENT_FORM + "_" + CLIENT_AMOUNT, CLIENT_AMOUNT);
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.CLIENT_FORM + "_" + CLIENT_CREDIT_LIMIT, CLIENT_CREDIT_LIMIT);
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.CLIENT_FORM + "_" + CLIENT_QUOTE_CREDIT_LIMIT, CLIENT_QUOTE_CREDIT_LIMIT);
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.CLIENT_FORM + "_" + PRICE_LEVEL, PRICE_LEVEL);

        //--Supplier form
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.SUPPLIER_FORM + "_" + CRM_NOTE, CRM_NOTE);
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.SUPPLIER_FORM + "_" + ATTACHMENTS, ATTACHMENTS);
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.SUPPLIER_FORM + "_" + CURRENCY, CURRENCY);
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.SUPPLIER_FORM + "_" + VAT_NUMBER, VAT_NUMBER);
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.SUPPLIER_FORM + "_" + PAYMENT_METHOD, PAYMENT_METHOD);
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.SUPPLIER_FORM + "_" + SUPPLIER_VAT, SUPPLIER_VAT);
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.SUPPLIER_FORM + "_" + REGISTRATION_NUMBER, REGISTRATION_NUMBER);
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.SUPPLIER_FORM + "_" + ACCOUNTS_RECEIVABLE_PAYABLE, ACCOUNTS_RECEIVABLE_PAYABLE);
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.SUPPLIER_FORM + "_" + GL_ACCOUNT, GL_ACCOUNT);
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.SUPPLIER_FORM + "_" + CLIENT_INVOICE_TERM, CLIENT_INVOICE_TERM);
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.SUPPLIER_FORM + "_" + SUPPLIER_AS_OF_DATE, SUPPLIER_AS_OF_DATE);
        MOBILE_EXCLUDED_FIELDS.put(LayoutRPC.SUPPLIER_FORM + "_" + SUPPLIER_AMOUNT, SUPPLIER_AMOUNT);

    }

    private final SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);
    private final String MODE_CREATE = "CREATE";
    private final String MODE_EDIT = "EDIT";
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private ProfileServiceLocal profileServiceLocal;
    @Autowired
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private ContactServiceLocal contactServiceLocal;
    @Autowired
    private ModelFieldManager modelFieldManager;
    @Autowired
    private ModelFieldLocalizer modelFieldLocalizer;
    @Autowired
    private CompanyCustomFieldsManager companyCFSettingsManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private NoteHistoryManager noteHistoryManager;
    @Autowired
    private TaskServiceLocal taskServiceLocal;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    private FormPropertyManager formPropertyManager;
    @Autowired
    private CustomFormSectionManager customFormSectionManager;

    @Operation(summary = "Get Custom Fields list", description = "Retrieves the custom field list based on request_type. Request Type should be EXPENSE_CLAIM only")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of custom fields based on request_type"),
            @ApiResponse(responseCode = "400", description = "request_type is required"),
            @ApiResponse(responseCode = "422", description = "request_type should be EXPENSE_CLAIM ")})
    @RequestMapping(value = "/custom_fields", method = RequestMethod.GET)
    public Object getCustomFieldList(@RequestParam(value = "request_type") String request_type) throws RestException {
        if (StringUtils.isBlank(request_type)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "request_type is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (!CustomFormConstants.EXPENSE_CLAIM.equals(request_type)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "request_type should be EXPENSE_CLAIM only", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        ArrayList<CompanyCustomFieldItem> customFieldItem;
        try {
            customFieldItem = commonServiceLocal.getCompanyCustomFields(ViewName.ExpenceReportView);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ArrayList<CustomFieldListTO> customFieldLists = new ArrayList<>();

        if (customFieldItem != null) {
            for (CompanyCustomFieldItem companyCustomFieldItem : customFieldItem) {
                CustomFieldListTO customFieldList = new CustomFieldListTO();
                customFieldList.setId(companyCustomFieldItem.getObjectId());
                customFieldList.setRequired(companyCustomFieldItem.isRequired());
                customFieldList.setTitle(companyCustomFieldItem.getFieldName());
                if ("Text".equalsIgnoreCase(companyCustomFieldItem.getDataType()) && ("TextBox".equalsIgnoreCase(companyCustomFieldItem.getUiType()) || "TextArea".equalsIgnoreCase(companyCustomFieldItem.getUiType()))) {
                    customFieldList.setField_type(CustomFieldCategoryEnum.TEXT_INPUT.getCategory());
                } else if ("Date".equalsIgnoreCase(companyCustomFieldItem.getDataType())) {
                    customFieldList.setField_type(CustomFieldCategoryEnum.DATE.getCategory());
                } else if ("Number".equalsIgnoreCase(companyCustomFieldItem.getDataType()) && "TextBox".equalsIgnoreCase(companyCustomFieldItem.getUiType())) {
                    customFieldList.setField_type(CustomFieldCategoryEnum.NUMBER_INPUT.getCategory());
                } else if ("File Upload".equalsIgnoreCase(companyCustomFieldItem.getDataType())) {
                    customFieldList.setField_type(CustomFieldCategoryEnum.FILE_UPLOAD.getCategory());
                } else if (("Text".equalsIgnoreCase(companyCustomFieldItem.getDataType()) || "Number".equalsIgnoreCase(companyCustomFieldItem.getDataType())) && ("RadioButton".equalsIgnoreCase(companyCustomFieldItem.getUiType()) || "DropDown".equalsIgnoreCase(companyCustomFieldItem.getUiType()))) {
                    customFieldList.setField_type(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                } else if (("Text".equalsIgnoreCase(companyCustomFieldItem.getDataType()) || "Number".equalsIgnoreCase(companyCustomFieldItem.getDataType())) && "CheckBox".equalsIgnoreCase(companyCustomFieldItem.getUiType())) {
                    customFieldList.setField_type(CustomFieldCategoryEnum.MULTIPLY_CHOOSE.getCategory());
                }
                customFieldLists.add(customFieldList);
            }
        }

        return successResponse(new CustomFieldListResultTO(customFieldLists));
    }

    @Operation(summary = "Get Custom Fields list", description = "Retrieves the custom field list based on request_type. Request Type should be one of leads/contacts/opportunities/events/calls/suppliers/customers/accounts/leave_request")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of custom fields based on request_type"),
            @ApiResponse(responseCode = "400", description = "main_entity_path is required."),
            @ApiResponse(responseCode = "422", description = "main_entity_path should be one of leads/contacts/opportunities/events/calls/suppliers/customers/accounts/leave_request.")})
    @RequestMapping(value = "/{main_entity_path}/custom_fields", method = RequestMethod.GET)
    public Object getCustomFieldListByEntity(@Valid @PathVariable("main_entity_path") String main_entity_path,
                                             @Pattern(regexp = "CREATE|EDIT",
                                                     message = "mode is either one of CREATE/EDIT")
                                             @Parameter(name = "mode", example = "CREATE,EDIT")
                                             @RequestParam(value = "mode", required = false) String mode) throws RestException {

        if (StringUtils.isBlank(main_entity_path)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "main_entity_path is required", REQUIRED, HttpStatus.BAD_REQUEST);
        } else if (CF_TYPES.get(main_entity_path.toLowerCase()) == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "main_entity_path should be one of leads/contacts/opportunities/events/calls/suppliers/customers/accounts/tasks/leave_request", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        //If mode param is not set, consider CREATE value by default.(from Apiary)
        if (StringUtils.isBlank(mode) || !MODE_CREATE.equalsIgnoreCase(mode) && !MODE_EDIT.equalsIgnoreCase(mode)) {
            mode = MODE_CREATE;
        }

        ArrayList<CustomFieldListTO> customFieldLists = new ArrayList<>();
        //Get Static/Fixed Fields first
        getStaticFixedFieldsOfEntity(customFieldLists, main_entity_path, mode);


        return successResponse(new ResponseListData<>(customFieldLists));
    }

    @Operation(summary = "Get Custom Fields list", description = "Retrieves the custom field list for particular company")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of custom fields based on request_type"),
            @ApiResponse(responseCode = "400", description = "main_entity_path is required.")})
    @RequestMapping(value = "/companies/{company_id}/custom_fields", method = RequestMethod.GET)
    public Object getCompanyCustomFields(@PathVariable("company_id") Integer company_id) throws RestException {
        if (company_id == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "company_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        ArrayList<CustomFieldListTO> customFieldLists = new ArrayList<>();

        EdsCrmAccount edsCrmAccount = crmAccountManager.get(company_id);

        String entityPath = "accounts";
        if (edsCrmAccount != null) {
            List<ModelField> modelFields = modelFieldManager.getFields(FORM_TYPES.get(entityPath));
            if (modelFields != null && modelFields.size() > 0) {
                for (ModelField edsModelField : modelFields) {
                    if (!edsModelField.isHideInCustomizeForm() && !edsModelField.isHide()) {

                        String fieldType = getTypeOfStaticField(edsModelField, entityPath, MODE_EDIT);
                        //We must retrieve only non fixed fields and sections
                        String fixedField = MOBILE_FIXED_FIELDS.get(FORM_TYPES.get(entityPath) + "_" + edsModelField.getField_ID());
                        String fixedSection = MOBILE_FIXED_SECTIONS.get(FORM_TYPES.get(entityPath) + "_" + edsModelField.getSection());

                        if (StringUtils.isNotBlank(fieldType) && StringUtils.isBlank(fixedField) && StringUtils.isBlank(fixedSection)) {

                            CustomFieldListTO field = new CustomFieldListTO();
                            field.setRequired(edsModelField.isSystemMandatory() || edsModelField.isMandatory());
                            //Localize Field name
                            if (!edsModelField.isIsCustomField()) {
                                field.setId(edsModelField.getObjectID() + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                                field.setTitle(modelFieldLocalizer.localizeByFieldID(FORM_TYPES.get(entityPath), edsModelField.getField_ID()));
                            } else {
                                EdsCompanyCustomFieldsSettings cf = companyCFSettingsManager.getCompanyCustomField(CF_TYPES.get(entityPath).name(), edsModelField.getField_ID());
                                if (cf != null) {
                                    field.setId(cf.getObjectID());
                                }
                                field.setTitle(edsModelField.getLabel());
                            }
                            field.setField_type(fieldType);

                            //Return it only if title is not empty
                            if (StringUtils.isNotBlank(field.getTitle())) {
                                customFieldLists.add(field);
                            } else {
                                log.info("Couldnt translate modelfield:" + FORM_TYPES.get(entityPath) + "->" + edsModelField.getField_ID());
                            }
                        }
                    }
                }
            }
        }
        return successResponse(new ResponseListData<>(customFieldLists));
    }

    @Operation(summary = "Get Custom Category field categories", description = "Retrieves custom field categories based on field type CATEGORY_CHOOSE and MULTIPLY_CHOOSE")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have custom category field categories"),
            @ApiResponse(responseCode = "400", description = "field_id is required"),
            @ApiResponse(responseCode = "404", description = "Categories with custom field_id is not found")})
    @RequestMapping(value = "/custom_fields/categories", method = RequestMethod.GET)
    public Object getCustomCategoryFieldCategories(@RequestParam(value = "field_id") Integer field_id) throws RestException {
        if (field_id == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "field_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        CompanyCustomFieldItem customFieldItem;
        try {
            customFieldItem = profileServiceLocal.getCustomFieldData(field_id, null);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);

        }
        if (customFieldItem == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Categories with custom field id " + field_id + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        ArrayList<CategoryTO> items = new ArrayList<>();

        if (Constants.UI_TYPE_DROPDOWN.equalsIgnoreCase(customFieldItem.getUiType()) || Constants.UI_TYPE_RADIOBUTTON.equalsIgnoreCase(customFieldItem.getUiType())
                || Constants.UI_TYPE_CHECKBOX.equalsIgnoreCase(customFieldItem.getUiType())) {
            SelectItem[] predefinedValues = customFieldItem.getPredefinedValuesWithSorting() != null ? customFieldItem.getPredefinedValuesWithSorting() : new SelectItem[0];
            for (SelectItem values : predefinedValues) {
                Integer id = values.getId() != null ? values.getId() : predefinedValues.length;
                CategoryTO category = new CategoryTO();
                category.setId(id);
                category.setTitle(values.getName());
                items.add(category);
            }
        } else if (Constants.UI_TYPE_LOOKUP.equalsIgnoreCase(customFieldItem.getUiType())) {

        }

        return successResponse(new ResponseItemsListData<>(items));
    }

    @Operation(summary = "Get Additional Information", description = "Retrieves the additional information for entities like Lead, Company and Contact\n" +
            "main_entity_name should be leads, contacts or companies")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have the additional information of certain entities")})
    @RequestMapping(value = "/{main_entity_name}/{item_id}/additional_information", method = RequestMethod.GET)
    public Object getEntityAdditionalInformation(
            @PathVariable(value = "main_entity_name") String main_entity_name,
            @PathVariable(value = "item_id") Integer item_id) throws RestException {

        if (StringUtils.isBlank(main_entity_name)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "main_entity_name is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (item_id == null || item_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        ResponseListData<CustomFieldsTO> entityCustomFields = new ResponseListData<>();
        ArrayList<CustomFieldsTO> customFields = new ArrayList<>();
        CustomFieldsTO customField;

        if (EntityTypeEnum.LEADS.name().equalsIgnoreCase(main_entity_name)) {
            ArrayList<String> leadFieldTypes = new ArrayList<>();
            leadFieldTypes.add(LEAD_OWNER); //done
            leadFieldTypes.add(ASSIGNEE);  // done
            leadFieldTypes.add(BACKUP_ASSIGNEE); //done
            leadFieldTypes.add(LEAD_SOURCE); //done
            leadFieldTypes.add(CRM_CAMPAIGN_NAME); // done
            leadFieldTypes.add(CRM_ACCOUNT_ORGANIZATION_TYPE); // done
            leadFieldTypes.add(CRM_ACCOUNT_NUMBER_OF_EMPLOYEE); // done
            leadFieldTypes.add(RATING); // done
            leadFieldTypes.add(CRM_ACCOUNT_OWNERSHIP); // done
            leadFieldTypes.add(CRM_ACCOUNT_ANNUAL_REVENUE); // done
            leadFieldTypes.add(CRM_ACCOUNT_INDUSTRY); // done
            leadFieldTypes.add(WEB_ADDRESS); // done
            leadFieldTypes.add(IM_ADDRESS); //done
            leadFieldTypes.add(JOB_TITLE); // done
            leadFieldTypes.add(CRM_ACCOUNT_TYPE); // done

            ContactListItem lead = crmServiceLocal.getLead(item_id);
            String imAddress = null;
            HashMap<Integer, ArrayList<String>> contactListParams = ContactListItem.getItemParamsAsMap(lead, Constants.CONTACT_IMADDRESSES);
            for (HashMap.Entry<Integer, ArrayList<String>> entry : contactListParams.entrySet()) {
                if (entry != null) {
                    switch (entry.getKey()) {
                        case Constants.G_GOOGLE_TALK -> {
                            ArrayList<String> googleTalk = entry.getValue();
                            for (String item : googleTalk) {
                                imAddress = item;
                            }
                        }
                        case Constants.G_AIM -> {
                            ArrayList<String> gAim = entry.getValue();
                            for (String item : gAim) {
                                imAddress = item;
                            }
                        }
                        case Constants.G_YAHOO -> {
                            ArrayList<String> yahoo = entry.getValue();
                            for (String item : yahoo) {
                                imAddress = item;
                            }
                        }
                        case Constants.G_SKYPE -> {
                            ArrayList<String> skype = entry.getValue();
                            for (String item : skype) {
                                imAddress = item;
                            }
                        }
                        case Constants.G_QQ -> {
                            ArrayList<String> qq = entry.getValue();
                            for (String item : qq) {
                                imAddress = item;
                            }
                        }
                        case Constants.G_MSN -> {
                            ArrayList<String> msn = entry.getValue();
                            for (String item : msn) {
                                imAddress = item;
                            }
                        }
                        case Constants.G_ICQ -> {
                            ArrayList<String> icq = entry.getValue();
                            for (String item : icq) {
                                imAddress = item;
                            }
                        }
                        case Constants.G_JABBER -> {
                            ArrayList<String> jabber = entry.getValue();
                            for (String item : jabber) {
                                imAddress = item;
                            }
                        }
                    }
                }
            }
            List<EdsModelField> modelFields = modelFieldManager.getSpecificFields(LayoutRPC.LEAD_FORM, leadFieldTypes);
            if (modelFields == null || modelFields.size() <= 0) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Model Fields are not found with provided types", NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            LinkedHashMap<String, Integer> modelFieldMap = new LinkedHashMap<>();
            if (lead != null) {
                for (EdsModelField modelField : modelFields) {

                    modelFieldMap.put(modelField.getField_ID(), modelField.getObjectID());

                    if (lead.getOwnerId() != null && LEAD_OWNER.equals(modelField.getField_ID())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                        CategoryTO category = new CategoryTO(lead.getOwnerId(), lead.getOwner());
                        customField.setObject(new CustomFieldCategoryChooseTO(modelFieldMap.get(LEAD_OWNER) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS, modelFieldLocalizer.localizeContact(LEAD_OWNER), category));
                        customFields.add(customField);
                    }
                    if (lead.getLeadAssigneeID() != null && ASSIGNEE.equals(modelField.getField_ID())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                        CategoryTO category = new CategoryTO(lead.getLeadAssigneeID(), lead.getLeadAssignee());
                        customField.setObject(new CustomFieldCategoryChooseTO(modelFieldMap.get(ASSIGNEE) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS, modelFieldLocalizer.localizeContact(ASSIGNEE), category));
                        customFields.add(customField);
                    }
                    if (lead.getLeadBackupAssigneeID() != null && BACKUP_ASSIGNEE.equals(modelField.getField_ID())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                        CategoryTO category = new CategoryTO(lead.getLeadBackupAssigneeID(), lead.getLeadBackupAssignee());
                        customField.setObject(new CustomFieldCategoryChooseTO(modelFieldMap.get(BACKUP_ASSIGNEE) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS, modelFieldLocalizer.localizeContact(BACKUP_ASSIGNEE), category));
                        customFields.add(customField);
                    }
                    if (StringUtils.isNotBlank(lead.getJobTitle()) && JOB_TITLE.equals(modelField.getField_ID())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldCategoryEnum.TEXT_INPUT.name());
                        CustomFieldTextTO text = new CustomFieldTextTO();
                        text.setId(modelFieldMap.get(JOB_TITLE) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                        text.setTitle(modelFieldLocalizer.localizeContact(JOB_TITLE));
                        text.setText(lead.getJobTitle());
                        customField.setObject(text);

                        customFields.add(customField);
                    }
                    if (StringUtils.isNotBlank(imAddress) && IM_ADDRESS.equals(modelField.getField_ID())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldCategoryEnum.TEXT_INPUT.name());
                        CustomFieldTextTO text = new CustomFieldTextTO();
                        text.setId(modelFieldMap.get(IM_ADDRESS) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                        text.setTitle(modelFieldLocalizer.localizeContact(IM_ADDRESS));
                        text.setText(imAddress);
                        customField.setObject(text);
                        customFields.add(customField);
                    }

                    if (lead.getWorkWebSite() != null && lead.getWorkWebSite().size() > 0 && WEB_ADDRESS.equals(modelField.getField_ID())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldCategoryEnum.TEXT_INPUT.name());
                        CustomFieldTextTO text = new CustomFieldTextTO();
                        text.setId(modelFieldMap.get(WEB_ADDRESS) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                        text.setTitle(modelFieldLocalizer.localizeContact(WEB_ADDRESS));
                        for (String website : lead.getWorkWebSite()) {
                            text.setText(website);
                        }
                        customField.setObject(text);

                        customFields.add(customField);
                    }

                    if (lead.getLeadSourceID() != null && LEAD_SOURCE.equals(modelField.getField_ID())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                        CategoryTO category = new CategoryTO(lead.getLeadSourceID(), lead.getLeadSource());
                        customField.setObject(new CustomFieldCategoryChooseTO(modelFieldMap.get(LEAD_SOURCE) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS, modelFieldLocalizer.localizeContact(LEAD_SOURCE), category));
                        customFields.add(customField);
                    }
                    if (lead.getLeadRatingID() != null && RATING.equals(modelField.getField_ID())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                        CategoryTO category = new CategoryTO(lead.getLeadRatingID(), lead.getLeadRating());
                        customField.setObject(new CustomFieldCategoryChooseTO(modelFieldMap.get(RATING) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS, modelFieldLocalizer.localizeContact(RATING), category));
                        customFields.add(customField);
                    }
                    if (lead.getCampaignId() != null && CRM_CAMPAIGN_NAME.equals(modelField.getField_ID())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                        CategoryTO category = new CategoryTO(lead.getCampaignId(), lead.getCampaign());
                        customField.setObject(new CustomFieldCategoryChooseTO(modelFieldMap.get(CRM_CAMPAIGN_NAME) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS, modelFieldLocalizer.localizeContact(CRM_CAMPAIGN_NAME), category));
                        customFields.add(customField);
                    }
                    if (lead.getCrmAccount() != null) {
                        if (lead.getCrmAccount().getIndustryID() != null && CRM_ACCOUNT_INDUSTRY.equals(modelField.getField_ID())) {
                            customField = new CustomFieldsTO();
                            customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                            CategoryTO category = new CategoryTO(lead.getCrmAccount().getIndustryID(), lead.getCrmAccount().getIndustry());
                            customField.setObject(new CustomFieldCategoryChooseTO(modelFieldMap.get(CRM_ACCOUNT_INDUSTRY) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS, modelFieldLocalizer.localizeContact(CRM_ACCOUNT_INDUSTRY), category));
                            customFields.add(customField);
                        }
                        if (lead.getCrmAccount().getOrganizationTypeID() != null && CRM_ACCOUNT_ORGANIZATION_TYPE.equals(modelField.getField_ID())) {
                            customField = new CustomFieldsTO();
                            customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                            CategoryTO category = new CategoryTO(lead.getCrmAccount().getOrganizationTypeID(), lead.getCrmAccount().getOrganizationType());
                            customField.setObject(new CustomFieldCategoryChooseTO(modelFieldMap.get(CRM_ACCOUNT_ORGANIZATION_TYPE) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS, modelFieldLocalizer.localizeContact(CRM_ACCOUNT_ORGANIZATION_TYPE), category));
                            customFields.add(customField);
                        }
                        if (lead.getCrmAccount().getNumberOfEmployeeID() != null && CRM_ACCOUNT_NUMBER_OF_EMPLOYEE.equals(modelField.getField_ID())) {
                            customField = new CustomFieldsTO();
                            customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                            CategoryTO category = new CategoryTO(lead.getCrmAccount().getNumberOfEmployeeID(), lead.getCrmAccount().getNumberOfEmployee());
                            customField.setObject(new CustomFieldCategoryChooseTO(modelFieldMap.get(CRM_ACCOUNT_NUMBER_OF_EMPLOYEE) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS, modelFieldLocalizer.localizeContact(CRM_ACCOUNT_NUMBER_OF_EMPLOYEE), category));
                            customFields.add(customField);
                        }
                        if (lead.getCrmAccount().getOwnershipId() != null && CRM_ACCOUNT_OWNERSHIP.equals(modelField.getField_ID())) {
                            customField = new CustomFieldsTO();
                            customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                            CategoryTO category = new CategoryTO(lead.getCrmAccount().getOwnershipId(), lead.getCrmAccount().getOwnership());
                            customField.setObject(new CustomFieldCategoryChooseTO(modelFieldMap.get(CRM_ACCOUNT_OWNERSHIP) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS, modelFieldLocalizer.localizeContact(CRM_ACCOUNT_OWNERSHIP), category));
                            customFields.add(customField);
                        }
                        if (lead.getCrmAccount().getAnnualRevenueID() != null && CRM_ACCOUNT_ANNUAL_REVENUE.equals(modelField.getField_ID())) {
                            customField = new CustomFieldsTO();
                            customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                            CategoryTO category = new CategoryTO(lead.getCrmAccount().getAnnualRevenueID(), lead.getCrmAccount().getAnnualRevenue());
                            customField.setObject(new CustomFieldCategoryChooseTO(modelFieldMap.get(CRM_ACCOUNT_ANNUAL_REVENUE) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS, modelFieldLocalizer.localizeContact(CRM_ACCOUNT_ANNUAL_REVENUE), category));
                            customFields.add(customField);
                        }
                        if (lead.getCrmAccount().getAccountTypes() != null && CRM_ACCOUNT_TYPE.equals(modelField.getField_ID())) {
                            customField = new CustomFieldsTO();
                            customField.setType(CustomFieldCategoryEnum.MULTIPLY_CHOOSE.getCategory());
                            ArrayList<CategoryTO> accountTypes = new ArrayList<>();
                            for (SelectItem item : lead.getCrmAccount().getAccountTypes()) {
                                if (item != null && item.isSelected()) {
                                    CategoryTO category = new CategoryTO();
                                    category.setId(item.getId());
                                    category.setTitle(item.getName());
                                    accountTypes.add(category);
                                }
                            }
                            customField.setObject(new CustomFieldMultiplyChooseObjectTO(modelFieldMap.get(CRM_ACCOUNT_TYPE) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS, modelFieldLocalizer.localizeContact(CRM_ACCOUNT_TYPE), accountTypes));
                            customFields.add(customField);
                        }

                    }

                }
                customFields.addAll(getCustomFields(lead.getCustomFields()));
                entityCustomFields.setList(customFields);
            }
            return successResponse(entityCustomFields);
        }
        if (EntityTypeEnum.COMPANIES.name().equalsIgnoreCase(main_entity_name)) {
            EdsCrmAccount crmAccountItem = crmAccountManager.get(item_id);
            if (crmAccountItem == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Company with id ".concat(item_id.toString()).concat(" is not found"), NOT_FOUND, HttpStatus.NOT_FOUND);
            }

            ArrayList<String> companyFields = new ArrayList<>();

            companyFields.add(PRIMARY_CONTACT);
            companyFields.add(CRM_ACCOUNT_NUMBER);
            companyFields.add(CRM_ACCOUNT_EMAIL);
            companyFields.add(CRM_ACCOUNT_PHONE);
            companyFields.add(CRM_ACCOUNT_WEBSITE);
            companyFields.add(CRM_ACCOUNT_FAX);
            companyFields.add(CRM_NOTE);
            companyFields.add(CRM_ACCOUNT_INDUSTRY);
            companyFields.add(CLIENT_TYPE);
            companyFields.add(CRM_ACCOUNT_OWNERSHIP);
            companyFields.add(CRM_ACCOUNT_ORGANIZATION_TYPE);
            companyFields.add(CRM_ACCOUNT_NUMBER_OF_EMPLOYEE);
            companyFields.add(CRM_ACCOUNT_ANNUAL_REVENUE);
            companyFields.add(CRM_ACCOUNT_RATING);
            companyFields.add(CRM_ACCOUNT_PAYMENT_TYPE);

            LinkedHashMap<String, Integer> modelFieldMap = new LinkedHashMap<>();
            List<EdsModelField> modelFields = modelFieldManager.getSpecificFields(LayoutRPC.ACCOUNT_FORM, companyFields);

            if (modelFields == null || modelFields.size() <= 0) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Model Fields are not found with provided types", NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            for (EdsModelField modelField : modelFields) {
                modelFieldMap.put(modelField.getField_ID(), modelField.getObjectID());

                if (crmAccountItem.getPrimaryContact() != null && PRIMARY_CONTACT.equals(modelField.getField_ID())) {
                    customField = new CustomFieldsTO();
                    customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                    CategoryTO category = new CategoryTO(crmAccountItem.getPrimaryContact().getObjectID(), crmAccountItem.getPrimaryContact().getName());
                    CustomFieldCategoryChooseTO categoryChoose = new CustomFieldCategoryChooseTO();
                    categoryChoose.setId(modelFieldMap.get(PRIMARY_CONTACT) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                    categoryChoose.setTitle(modelFieldLocalizer.localizeCrmAccount(PRIMARY_CONTACT));
                    categoryChoose.setCategory(category);
                    customField.setObject(categoryChoose);
                    customFields.add(customField);
                }

                if (StringUtils.isNotBlank(crmAccountItem.getNumber()) && CRM_ACCOUNT_NUMBER.equals(modelField.getField_ID())) {
                    customField = new CustomFieldsTO();
                    customField.setType(CustomFieldCategoryEnum.TEXT_INPUT.name());
                    CustomFieldTextTO text = new CustomFieldTextTO();
                    text.setId(modelFieldMap.get(CRM_ACCOUNT_NUMBER) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                    text.setTitle(modelFieldLocalizer.localizeCrmAccount(CRM_ACCOUNT_NUMBER));
                    text.setText(crmAccountItem.getNumber());
                    customField.setObject(text);

                    customFields.add(customField);
                }
                if (StringUtils.isNotBlank(crmAccountItem.getEmail()) && CRM_ACCOUNT_EMAIL.equals(modelField.getField_ID())) {
                    customField = new CustomFieldsTO();
                    customField.setType(CustomFieldCategoryEnum.TEXT_INPUT.name());
                    CustomFieldTextTO text = new CustomFieldTextTO();
                    text.setId(modelFieldMap.get(CRM_ACCOUNT_EMAIL) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                    text.setTitle(modelFieldLocalizer.localizeCrmAccount(CRM_ACCOUNT_EMAIL));
                    text.setText(crmAccountItem.getEmail());
                    customField.setObject(text);

                    customFields.add(customField);
                }

                if (StringUtils.isNotBlank(crmAccountItem.getPhone()) && CRM_ACCOUNT_PHONE.equals(modelField.getField_ID())) {
                    customField = new CustomFieldsTO();
                    customField.setType(CustomFieldCategoryEnum.TEXT_INPUT.name());
                    CustomFieldTextTO text = new CustomFieldTextTO();
                    text.setId(modelFieldMap.get(CRM_ACCOUNT_PHONE) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                    text.setTitle(modelFieldLocalizer.localizeCrmAccount(CRM_ACCOUNT_PHONE));
                    text.setText(crmAccountItem.getPhone());
                    customField.setObject(text);

                    customFields.add(customField);
                }
                if (StringUtils.isNotBlank(crmAccountItem.getWebsite()) && CRM_ACCOUNT_WEBSITE.equals(modelField.getField_ID())) {
                    customField = new CustomFieldsTO();
                    customField.setType(CustomFieldCategoryEnum.TEXT_INPUT.name());
                    CustomFieldTextTO text = new CustomFieldTextTO();
                    text.setId(modelFieldMap.get(CRM_ACCOUNT_WEBSITE) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                    text.setTitle(modelFieldLocalizer.localizeCrmAccount(CRM_ACCOUNT_WEBSITE));
                    text.setText(crmAccountItem.getWebsite());
                    customField.setObject(text);

                    customFields.add(customField);
                }
                if (StringUtils.isNotBlank(crmAccountItem.getFax()) && CRM_ACCOUNT_FAX.equals(modelField.getField_ID())) {
                    customField = new CustomFieldsTO();
                    customField.setType(CustomFieldCategoryEnum.TEXT_INPUT.name());
                    CustomFieldTextTO text = new CustomFieldTextTO();
                    text.setId(modelFieldMap.get(CRM_ACCOUNT_FAX) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                    text.setTitle(modelFieldLocalizer.localizeCrmAccount(CRM_ACCOUNT_FAX));
                    text.setText(crmAccountItem.getFax());
                    customField.setObject(text);

                    customFields.add(customField);
                }
                ListingFilterParameter filterParameter = new ListingFilterParameter();
                filterParameter.setRelationType(RelationItem.TYPE_CRM_ACCOUNT);
                filterParameter.setRelationID(crmAccountItem.getObjectID());
                List<EdsNoteHistory> notes = noteHistoryManager.getNoteList(filterParameter);
                if (notes != null && notes.size() > 0 && CRM_NOTE.equals(modelField.getField_ID())) {
                    customField = new CustomFieldsTO();
                    customField.setType(CustomFieldCategoryEnum.TEXT_INPUT.name());
                    CustomFieldTextTO text = new CustomFieldTextTO();
                    text.setId(modelFieldMap.get(CRM_NOTE) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                    text.setTitle(modelFieldLocalizer.localizeCrmAccount(CRM_NOTE));
                    notes.forEach(edsNoteHistory -> text.setText(edsNoteHistory.getComment()));
                    customField.setObject(text);

                    customFields.add(customField);
                }
                if (crmAccountItem.getIndustry() != null && CRM_ACCOUNT_INDUSTRY.equals(modelField.getField_ID())) {
                    customField = new CustomFieldsTO();
                    customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                    CategoryTO category = new CategoryTO(crmAccountItem.getIndustry().getObjectID(), crmAccountItem.getIndustry().getName());
                    customField.setObject(new CustomFieldCategoryChooseTO(modelFieldMap.get(CRM_ACCOUNT_INDUSTRY) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS, modelFieldLocalizer.localizeCrmAccount(CRM_ACCOUNT_INDUSTRY), category));
                    customFields.add(customField);
                }

                if (crmAccountItem.getOwnership() != null && CRM_ACCOUNT_OWNERSHIP.equals(modelField.getField_ID())) {
                    customField = new CustomFieldsTO();
                    customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                    CategoryTO category = new CategoryTO(crmAccountItem.getOwnership().getObjectID(), crmAccountItem.getOwnership().getName());
                    customField.setObject(new CustomFieldCategoryChooseTO(modelFieldMap.get(CRM_ACCOUNT_OWNERSHIP) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS, modelFieldLocalizer.localizeCrmAccount(CRM_ACCOUNT_OWNERSHIP), category));
                    customFields.add(customField);
                }

                if (crmAccountItem.getOrganizationType() != null && CRM_ACCOUNT_ORGANIZATION_TYPE.equals(modelField.getField_ID())) {
                    customField = new CustomFieldsTO();
                    customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                    CategoryTO category = new CategoryTO(crmAccountItem.getOrganizationType().getObjectID(), crmAccountItem.getOrganizationType().getName());
                    customField.setObject(new CustomFieldCategoryChooseTO(modelFieldMap.get(CRM_ACCOUNT_ORGANIZATION_TYPE) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS, modelFieldLocalizer.localizeCrmAccount(CRM_ACCOUNT_ORGANIZATION_TYPE), category));
                    customFields.add(customField);
                }
                if (crmAccountItem.getNumberOfEmployees() != null && CRM_ACCOUNT_NUMBER_OF_EMPLOYEE.equals(modelField.getField_ID())) {
                    customField = new CustomFieldsTO();
                    customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                    CategoryTO category = new CategoryTO(crmAccountItem.getNumberOfEmployees().getObjectID(), crmAccountItem.getNumberOfEmployees().getName());
                    customField.setObject(new CustomFieldCategoryChooseTO(modelFieldMap.get(CRM_ACCOUNT_NUMBER_OF_EMPLOYEE) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS, modelFieldLocalizer.localizeCrmAccount(CRM_ACCOUNT_NUMBER_OF_EMPLOYEE), category));
                    customFields.add(customField);
                }
                if (crmAccountItem.getAnnualRevenue() != null && CRM_ACCOUNT_ANNUAL_REVENUE.equals(modelField.getField_ID())) {
                    customField = new CustomFieldsTO();
                    customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                    CategoryTO category = new CategoryTO(crmAccountItem.getAnnualRevenue().getObjectID(), crmAccountItem.getAnnualRevenue().getName());
                    customField.setObject(new CustomFieldCategoryChooseTO(modelFieldMap.get(CRM_ACCOUNT_ANNUAL_REVENUE) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS, modelFieldLocalizer.localizeCrmAccount(CRM_ACCOUNT_ANNUAL_REVENUE), category));
                    customFields.add(customField);
                }
                if (crmAccountItem.getRating() != null && CRM_ACCOUNT_RATING.equals(modelField.getField_ID())) {
                    customField = new CustomFieldsTO();
                    customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                    CategoryTO category = new CategoryTO(crmAccountItem.getRating().getObjectID(), crmAccountItem.getRating().getName());
                    customField.setObject(new CustomFieldCategoryChooseTO(modelFieldMap.get(CRM_ACCOUNT_RATING) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS, modelFieldLocalizer.localizeCrmAccount(CRM_ACCOUNT_RATING), category));
                    customFields.add(customField);
                }

                if (crmAccountItem.getPaymentMethod() != null && CRM_ACCOUNT_PAYMENT_TYPE.equals(modelField.getField_ID())) {
                    customField = new CustomFieldsTO();
                    customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                    CategoryTO category = new CategoryTO(crmAccountItem.getPaymentMethod().getObjectID(), crmAccountItem.getPaymentMethod().getName());
                    customField.setObject(new CustomFieldCategoryChooseTO(modelFieldMap.get(CRM_ACCOUNT_PAYMENT_TYPE) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS, modelFieldLocalizer.localizeCrmAccount(CRM_ACCOUNT_PAYMENT_TYPE), category));
                    customFields.add(customField);
                }

            }
            ArrayList<String> types = new ArrayList<>();
            types.add(CLIENT_SUBSIDIARIES);
            types.add(CLIENT_TYPE);
            modelFields = modelFieldManager.getSpecificFields(LayoutRPC.CLIENT_FORM, types);
            if (modelFields != null) {
                for (EdsModelField modelField : modelFields) {
                    if (crmAccountItem.getSubsidiary() != null && CLIENT_SUBSIDIARIES.equals(modelField.getField_ID())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                        CategoryTO category = new CategoryTO(crmAccountItem.getSubsidiary().getObjectID(), crmAccountItem.getSubsidiary().getName());
                        CustomFieldCategoryChooseTO categoryChoose = new CustomFieldCategoryChooseTO();
                        categoryChoose.setId(modelFieldMap.get(CLIENT_SUBSIDIARIES) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                        categoryChoose.setTitle(modelFieldLocalizer.localizeCrmAccount(CLIENT_SUBSIDIARIES));
                        categoryChoose.setCategory(category);
                        customField.setObject(categoryChoose);
                        customFields.add(customField);
                    }

                    if (crmAccountItem.getClientType() != null && CLIENT_TYPE.equals(modelField.getField_ID())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                        CategoryTO category = new CategoryTO(crmAccountItem.getClientType().getObjectID(), crmAccountItem.getClientType().getName());
                        customField.setObject(new CustomFieldCategoryChooseTO(modelFieldMap.get(CLIENT_TYPE) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS, modelFieldLocalizer.localizeCrmAccount(CLIENT_TYPE), category));
                        customFields.add(customField);
                    }
                }
            }
            if (crmAccountItem.isSupplier()) {
                ArrayList<String> bankFields = new ArrayList<>();
                bankFields.add(SUPPLIER_BANK_NAME);
                bankFields.add(SUPPLIER_ACCOUNT_NAME);
                bankFields.add(SUPPLIER_ACCOUNT_NUMBER);
                bankFields.add(SUPPLIER_SWIFT_CODE);
                bankFields.add(SUPPLIER_SORT_CODE);
                bankFields.add(SUPPLIER_IBAN_CODE);
                bankFields.add(SUPPLIER_BRANCH);
                bankFields.add(SUPPLIER_BANK_ADDRESS);
                LinkedHashMap<String, Integer> bankAccountMap = new LinkedHashMap<>();
                modelFields = modelFieldManager.getSpecificFields(LayoutRPC.SUPPLIER_FORM, bankFields);
                if (modelFields == null || modelFields.size() <= 0) {
                    throw new RestException(GENERAL_ERROR_MESSAGE, "Model Fields are not found with provided types", NOT_FOUND, HttpStatus.NOT_FOUND);
                }
                for (EdsModelField modelField : modelFields) {
                    bankAccountMap.put(modelField.getField_ID(), modelField.getObjectID());

                    if (StringUtils.isNotBlank(crmAccountItem.getBankName()) && SUPPLIER_BANK_NAME.equals(modelField.getField_ID())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldCategoryEnum.TEXT_INPUT.name());
                        CustomFieldTextTO text = new CustomFieldTextTO();
                        text.setId(bankAccountMap.get(SUPPLIER_BANK_NAME) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                        text.setTitle(modelFieldLocalizer.localizeCrmAccount(SUPPLIER_BANK_NAME));
                        text.setText(crmAccountItem.getBankName());
                        customField.setObject(text);

                        customFields.add(customField);
                    }
                    if (StringUtils.isNotBlank(crmAccountItem.getAccountName()) && SUPPLIER_ACCOUNT_NAME.equals(modelField.getField_ID())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldCategoryEnum.TEXT_INPUT.name());
                        CustomFieldTextTO text = new CustomFieldTextTO();
                        text.setId(bankAccountMap.get(SUPPLIER_ACCOUNT_NAME) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                        text.setTitle(modelFieldLocalizer.localizeCrmAccount(SUPPLIER_ACCOUNT_NAME));
                        text.setText(crmAccountItem.getAccountName());
                        customField.setObject(text);

                        customFields.add(customField);
                    }
                    if (StringUtils.isNotBlank(crmAccountItem.getAccountNo()) && SUPPLIER_ACCOUNT_NUMBER.equals(modelField.getField_ID())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldCategoryEnum.TEXT_INPUT.name());
                        CustomFieldTextTO text = new CustomFieldTextTO();
                        text.setId(bankAccountMap.get(SUPPLIER_ACCOUNT_NUMBER) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                        text.setTitle(modelFieldLocalizer.localizeCrmAccount(SUPPLIER_ACCOUNT_NUMBER));
                        text.setText(crmAccountItem.getAccountNo());
                        customField.setObject(text);

                        customFields.add(customField);
                    }
                    if (StringUtils.isNotBlank(crmAccountItem.getSwiftCode()) && SUPPLIER_SWIFT_CODE.equals(modelField.getField_ID())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldCategoryEnum.TEXT_INPUT.name());
                        CustomFieldTextTO text = new CustomFieldTextTO();
                        text.setId(bankAccountMap.get(SUPPLIER_SWIFT_CODE) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                        text.setTitle(modelFieldLocalizer.localizeCrmAccount(SUPPLIER_SWIFT_CODE));
                        text.setText(crmAccountItem.getSwiftCode());
                        customField.setObject(text);

                        customFields.add(customField);
                    }
                    if (StringUtils.isNotBlank(crmAccountItem.getSortCode()) && SUPPLIER_SORT_CODE.equals(modelField.getField_ID())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldCategoryEnum.TEXT_INPUT.name());
                        CustomFieldTextTO text = new CustomFieldTextTO();
                        text.setId(bankAccountMap.get(SUPPLIER_SORT_CODE) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                        text.setTitle(modelFieldLocalizer.localizeCrmAccount(SUPPLIER_SORT_CODE));
                        text.setText(crmAccountItem.getSortCode());
                        customField.setObject(text);

                        customFields.add(customField);
                    }
                    if (StringUtils.isNotBlank(crmAccountItem.getIbanCode()) && SUPPLIER_IBAN_CODE.equals(modelField.getField_ID())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldCategoryEnum.TEXT_INPUT.name());
                        CustomFieldTextTO text = new CustomFieldTextTO();
                        text.setId(bankAccountMap.get(SUPPLIER_IBAN_CODE) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                        text.setTitle(modelFieldLocalizer.localizeCrmAccount(SUPPLIER_IBAN_CODE));
                        text.setText(crmAccountItem.getIbanCode());
                        customField.setObject(text);

                        customFields.add(customField);
                    }
                    if (StringUtils.isNotBlank(crmAccountItem.getBranch()) && SUPPLIER_BRANCH.equals(modelField.getField_ID())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldCategoryEnum.TEXT_INPUT.name());
                        CustomFieldTextTO text = new CustomFieldTextTO();
                        text.setId(bankAccountMap.get(SUPPLIER_BRANCH) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                        text.setTitle(modelFieldLocalizer.localizeCrmAccount(SUPPLIER_BRANCH));
                        text.setText(crmAccountItem.getBranch());
                        customField.setObject(text);

                        customFields.add(customField);
                    }
                    if (StringUtils.isNotBlank(crmAccountItem.getBankAddress()) && SUPPLIER_BANK_ADDRESS.equals(modelField.getField_ID())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldCategoryEnum.TEXT_INPUT.name());
                        CustomFieldTextTO text = new CustomFieldTextTO();
                        text.setId(bankAccountMap.get(SUPPLIER_BANK_ADDRESS) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                        text.setTitle(modelFieldLocalizer.localizeCrmAccount(SUPPLIER_BANK_ADDRESS));
                        text.setText(crmAccountItem.getBankAddress());
                        customField.setObject(text);

                        customFields.add(customField);
                    }
                }
            }

            CrmAccountItem accountItem = crmServiceLocal.getAccount(crmAccountItem.getObjectID(), null);
            customFields.addAll(getCustomFields(accountItem.getCustomFields()));
            entityCustomFields.setList(customFields);

            return successResponse(entityCustomFields);
        }
        if (EntityTypeEnum.CONTACTS.name().equalsIgnoreCase(main_entity_name)) {
            ArrayList<String> contactTypes = new ArrayList<>();
            contactTypes.add(OWNER); //done
            contactTypes.add(CRM_CAMPAIGN_NAME); //done
            contactTypes.add(BIRTH_DAY); //done
            contactTypes.add(JOB_TITLE); // done
            contactTypes.add(DEPARTMENT); // done
            contactTypes.add(CRM_ACCOUNT_INDUSTRY); //done
            contactTypes.add(CRM_ACCOUNT_TYPE); //done
            contactTypes.add(CRM_ACCOUNT_ORGANIZATION_TYPE); // done
            contactTypes.add(CRM_ACCOUNT_NUMBER_OF_EMPLOYEE); // done
            contactTypes.add(CRM_ACCOUNT_OWNERSHIP); // done
            contactTypes.add(CRM_ACCOUNT_ANNUAL_REVENUE); //done
            contactTypes.add(CATEGORY); // done
            contactTypes.add(RELATIONSHIP); // done
            contactTypes.add(IM_ADDRESS);
            contactTypes.add(WEB_ADDRESS); //  done
            contactTypes.add(REPORTS_TO); //  done

            ContactListItem contactListItem = contactServiceLocal.getContact(item_id, true);
            HashMap<Integer, ArrayList<String>> contactListParams = ContactListItem.getItemParamsAsMap(contactListItem, Constants.CONTACT_IMADDRESSES);
            String imAddress = null;
            for (Map.Entry<Integer, ArrayList<String>> entry : contactListParams.entrySet()) {
                if (entry != null) {
                    switch (entry.getKey()) {
                        case Constants.G_GOOGLE_TALK -> {
                            ArrayList<String> googleTalk = entry.getValue();
                            for (String item : googleTalk) {
                                imAddress = item;
                            }
                        }
                        case Constants.G_AIM -> {
                            ArrayList<String> gAim = entry.getValue();
                            for (String item : gAim) {
                                imAddress = item;
                            }
                        }
                        case Constants.G_YAHOO -> {
                            ArrayList<String> yahoo = entry.getValue();
                            for (String item : yahoo) {
                                imAddress = item;
                            }
                        }
                        case Constants.G_SKYPE -> {
                            ArrayList<String> skype = entry.getValue();
                            for (String item : skype) {
                                imAddress = item;
                            }
                        }
                        case Constants.G_QQ -> {
                            ArrayList<String> qq = entry.getValue();
                            for (String item : qq) {
                                imAddress = item;
                            }
                        }
                        case Constants.G_MSN -> {
                            ArrayList<String> msn = entry.getValue();
                            for (String item : msn) {
                                imAddress = item;
                            }
                        }
                        case Constants.G_ICQ -> {
                            ArrayList<String> icq = entry.getValue();
                            for (String item : icq) {
                                imAddress = item;
                            }
                        }
                        case Constants.G_JABBER -> {
                            ArrayList<String> jabber = entry.getValue();
                            for (String item : jabber) {
                                imAddress = item;
                            }
                        }
                    }
                }
            }

            List<EdsModelField> modelFields = modelFieldManager.getSpecificFields(LayoutRPC.CONTACT_FORM, contactTypes);
            if (modelFields == null || modelFields.size() <= 0) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Model Fields are not found with provided types", NOT_FOUND, HttpStatus.NOT_FOUND);
            }

            LinkedHashMap<String, Integer> modelFieldMap = new LinkedHashMap<>();

            if (contactListItem != null) {
                for (EdsModelField modelField : modelFields) {
                    modelFieldMap.put(modelField.getField_ID(), modelField.getObjectID());

                    if (contactListItem.getOwnerId() != null && OWNER.equals(modelField.getField_ID())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                        CategoryTO category = new CategoryTO(contactListItem.getOwnerId(), contactListItem.getOwner());
                        customField.setObject(new CustomFieldCategoryChooseTO(modelFieldMap.get(OWNER) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS, modelFieldLocalizer.localizeContact(OWNER), category));

                        customFields.add(customField);
                    }
                    if (contactListItem.getCampaignId() != null && CRM_CAMPAIGN_NAME.equals(modelField.getField_ID())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                        CategoryTO category = new CategoryTO(contactListItem.getCampaignId(), contactListItem.getCampaign());
                        customField.setObject(new CustomFieldCategoryChooseTO(modelFieldMap.get(CRM_CAMPAIGN_NAME) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS, modelFieldLocalizer.localizeContact(CRM_CAMPAIGN_NAME), category));

                        customFields.add(customField);
                    }

                    if (contactListItem.getBirthDate() != null && BIRTH_DAY.equals(modelField.getField_ID())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldTypeEnum.DATE.name());
                        CustomFieldDateTO date = new CustomFieldDateTO();
                        date.setId(modelFieldMap.get(BIRTH_DAY) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                        date.setTitle(modelFieldLocalizer.localizeContact(BIRTH_DAY));
                        date.setDate(longDateTimezoneFormat.format(contactListItem.getBirthDate().getNonConvertedDate()));
                        customField.setObject(date);

                        customFields.add(customField);
                    }
                    if (StringUtils.isNotBlank(contactListItem.getJobTitle()) && JOB_TITLE.equals(modelField.getField_ID())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldCategoryEnum.TEXT_INPUT.name());
                        CustomFieldTextTO text = new CustomFieldTextTO();
                        text.setId(modelFieldMap.get(JOB_TITLE) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                        text.setTitle(modelFieldLocalizer.localizeContact(JOB_TITLE));
                        text.setText(contactListItem.getJobTitle());
                        customField.setObject(text);

                        customFields.add(customField);
                    }
                    if (StringUtils.isNotBlank(imAddress) && IM_ADDRESS.equals(modelField.getField_ID())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldCategoryEnum.TEXT_INPUT.name());
                        CustomFieldTextTO text = new CustomFieldTextTO();
                        text.setId(modelFieldMap.get(IM_ADDRESS) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                        text.setTitle(modelFieldLocalizer.localizeContact(IM_ADDRESS));
                        text.setText(imAddress);
                        customField.setObject(text);
                        customFields.add(customField);
                    }

                    if (contactListItem.getWorkWebSite() != null && contactListItem.getWorkWebSite().size() > 0 && WEB_ADDRESS.equals(modelField.getField_ID())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldCategoryEnum.TEXT_INPUT.name());
                        CustomFieldTextTO text = new CustomFieldTextTO();
                        text.setId(modelFieldMap.get(WEB_ADDRESS) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                        text.setTitle(modelFieldLocalizer.localizeContact(WEB_ADDRESS));
                        for (String website : contactListItem.getWorkWebSite()) {
                            text.setText(website);
                        }
                        customField.setObject(text);

                        customFields.add(customField);
                    }

                    if (StringUtils.isNotBlank(contactListItem.getDepartment()) && DEPARTMENT.equals(modelField.getField_ID())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldCategoryEnum.TEXT_INPUT.name());
                        CustomFieldTextTO text = new CustomFieldTextTO();
                        text.setId(modelFieldMap.get(DEPARTMENT) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                        text.setTitle(modelFieldLocalizer.localizeContact(DEPARTMENT));
                        text.setText(contactListItem.getDepartment());
                        customField.setObject(text);

                        customFields.add(customField);
                    }
                    if (contactListItem.getSelectedCategories() != null && CATEGORY.equals(modelField.getField_ID())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldCategoryEnum.MULTIPLY_CHOOSE.getCategory());
                        ArrayList<CategoryTO> categories = new ArrayList<>();
                        for (SelectItem item : contactListItem.getSelectedCategories()) {
                            if (item != null) {
                                CategoryTO category = new CategoryTO();
                                category.setId(item.getId());
                                category.setTitle(item.getName());
                                categories.add(category);
                            }
                        }
                        customField.setObject(new CustomFieldMultiplyChooseObjectTO(modelFieldMap.get(CATEGORY) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS, modelFieldLocalizer.localizeContact(CATEGORY), categories));
                        customFields.add(customField);
                    }

                    if (contactListItem.getReportsToId() != null && REPORTS_TO.equals(modelField.getField_ID())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                        CategoryTO category = new CategoryTO(contactListItem.getReportsToId(), contactListItem.getReportsTo());
                        customField.setObject(new CustomFieldCategoryChooseTO(modelFieldMap.get(REPORTS_TO) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS, modelFieldLocalizer.localizeContact(REPORTS_TO), category));
                        customFields.add(customField);
                    }

                    if (contactListItem.getCrmAccount() != null) {
                        if (contactListItem.getCrmAccount().getIndustryID() != null && CRM_ACCOUNT_INDUSTRY.equals(modelField.getField_ID())) {
                            customField = new CustomFieldsTO();
                            customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                            CategoryTO category = new CategoryTO(contactListItem.getCrmAccount().getIndustryID(), contactListItem.getCrmAccount().getIndustry());
                            customField.setObject(new CustomFieldCategoryChooseTO(modelFieldMap.get(CRM_ACCOUNT_INDUSTRY) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS, modelFieldLocalizer.localizeContact(CRM_ACCOUNT_INDUSTRY), category));
                            customFields.add(customField);
                        }
                        if (contactListItem.getCrmAccount().getAccountTypes() != null && CRM_ACCOUNT_TYPE.equals(modelField.getField_ID())) {
                            customField = new CustomFieldsTO();
                            customField.setType(CustomFieldCategoryEnum.MULTIPLY_CHOOSE.getCategory());
                            ArrayList<CategoryTO> accountTypes = new ArrayList<>();
                            for (SelectItem item : contactListItem.getCrmAccount().getAccountTypes()) {
                                if (item != null) {
                                    CategoryTO category = new CategoryTO();
                                    category.setId(item.getId());
                                    category.setTitle(item.getName());
                                    accountTypes.add(category);
                                }
                            }
                            customField.setObject(new CustomFieldMultiplyChooseObjectTO(modelFieldMap.get(CRM_ACCOUNT_TYPE) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS, modelFieldLocalizer.localizeContact(CRM_ACCOUNT_TYPE), accountTypes));
                            customFields.add(customField);
                        }

                        if (contactListItem.getCrmAccount().getOrganizationTypeID() != null && CRM_ACCOUNT_ORGANIZATION_TYPE.equals(modelField.getField_ID())) {
                            customField = new CustomFieldsTO();
                            customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                            CategoryTO category = new CategoryTO(contactListItem.getCrmAccount().getOrganizationTypeID(), contactListItem.getCrmAccount().getOrganizationType());
                            customField.setObject(new CustomFieldCategoryChooseTO(modelFieldMap.get(CRM_ACCOUNT_ORGANIZATION_TYPE) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS, modelFieldLocalizer.localizeContact(CRM_ACCOUNT_ORGANIZATION_TYPE), category));
                            customFields.add(customField);
                        }
                        if (contactListItem.getCrmAccount().getNumberOfEmployeeID() != null && CRM_ACCOUNT_NUMBER_OF_EMPLOYEE.equals(modelField.getField_ID())) {
                            customField = new CustomFieldsTO();
                            customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                            CategoryTO category = new CategoryTO(contactListItem.getCrmAccount().getNumberOfEmployeeID(), contactListItem.getCrmAccount().getNumberOfEmployee());
                            customField.setObject(new CustomFieldCategoryChooseTO(modelFieldMap.get(CRM_ACCOUNT_NUMBER_OF_EMPLOYEE) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS, modelFieldLocalizer.localizeContact(CRM_ACCOUNT_NUMBER_OF_EMPLOYEE), category));
                            customFields.add(customField);
                        }
                        if (contactListItem.getCrmAccount().getRatingId() != null && CRM_ACCOUNT_RATING.equals(modelField.getField_ID())) {
                            customField = new CustomFieldsTO();
                            customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                            CategoryTO category = new CategoryTO(contactListItem.getCrmAccount().getRatingId(), contactListItem.getCrmAccount().getRating());
                            customField.setObject(new CustomFieldCategoryChooseTO(modelFieldMap.get(CRM_ACCOUNT_RATING) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS, modelFieldLocalizer.localizeContact(CRM_ACCOUNT_RATING), category));
                            customFields.add(customField);
                        }
                        if (contactListItem.getCrmAccount().getOwnershipId() != null && CRM_ACCOUNT_OWNERSHIP.equals(modelField.getField_ID())) {
                            customField = new CustomFieldsTO();
                            customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                            CategoryTO category = new CategoryTO(contactListItem.getCrmAccount().getOwnershipId(), contactListItem.getCrmAccount().getOwnership());
                            customField.setObject(new CustomFieldCategoryChooseTO(modelFieldMap.get(CRM_ACCOUNT_OWNERSHIP) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS, modelFieldLocalizer.localizeContact(CRM_ACCOUNT_OWNERSHIP), category));
                            customFields.add(customField);
                        }
                        if (contactListItem.getCrmAccount().getAnnualRevenueID() != null && CRM_ACCOUNT_ANNUAL_REVENUE.equals(modelField.getField_ID())) {
                            customField = new CustomFieldsTO();
                            customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                            CategoryTO category = new CategoryTO(contactListItem.getCrmAccount().getAnnualRevenueID(), contactListItem.getCrmAccount().getAnnualRevenue());
                            customField.setObject(new CustomFieldCategoryChooseTO(modelFieldMap.get(CRM_ACCOUNT_ANNUAL_REVENUE) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS, modelFieldLocalizer.localizeContact(CRM_ACCOUNT_ANNUAL_REVENUE), category));
                            customFields.add(customField);
                        }
                        if (contactListItem.getSelectedRelationships() != null && contactListItem.getSelectedRelationships().size() > 0 && RELATIONSHIP.equals(modelField.getField_ID())) {
                            customField = new CustomFieldsTO();
                            customField.setType(CustomFieldCategoryEnum.TEXT_INPUT.name());
                            CustomFieldTextTO text = new CustomFieldTextTO();
                            text.setId(modelFieldMap.get(RELATIONSHIP) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                            text.setTitle(modelFieldLocalizer.localizeContact(RELATIONSHIP));
                            for (SelectItem selectItem : contactListItem.getSelectedRelationships()) {
                                text.setText(selectItem.getDescription());
                            }
                            customField.setObject(text);

                            customFields.add(customField);
                        }
                    }
                }
                customFields.addAll(getCustomFields(contactListItem.getCustomFields()));
                entityCustomFields.setList(customFields);
            }
            return successResponse(entityCustomFields);
        }
        if (EntityTypeEnum.OPPORTUNITIES.name().equalsIgnoreCase(main_entity_name)) {

            ArrayList<String> opportunityFieldTypes = new ArrayList<>();
            opportunityFieldTypes.add(CRM_OPPORTUNITY_NUMBER);
            opportunityFieldTypes.add(CRM_OPPORTUNITY_TYPE);
            opportunityFieldTypes.add(CRM_OPPORTUNITY_NEXT_STEP);
            opportunityFieldTypes.add(CRM_OPPORTUNITY_PROBABILITY);
            opportunityFieldTypes.add(CRM_OPPORTUNITY_EXPECTED_REVENUE);
            opportunityFieldTypes.add(CRM_OPPORTUNITY_CAMPAIGN_SOURCE);
            opportunityFieldTypes.add(CRM_OPPORTUNITY_LEAD_SOURCE);

            OpportunityListItem opportunityListItem = crmServiceLocal.getOpportunity(item_id);

            LinkedHashMap<String, Integer> modelFieldMap = new LinkedHashMap<>();
            List<EdsModelField> modelFields = modelFieldManager.getSpecificFields(LayoutRPC.OPPORTUNITY_FORM, opportunityFieldTypes);

            if (modelFields == null || modelFields.size() <= 0) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Model Fields are not found with provided types", NOT_FOUND, HttpStatus.NOT_FOUND);
            }

            if (opportunityListItem != null) {
                for (EdsModelField modelField : modelFields) {

                    modelFieldMap.put(modelField.getField_ID(), modelField.getObjectID());

                    if (opportunityListItem.getNumberData() != null && CRM_OPPORTUNITY_NUMBER.equals(modelField.getField_ID())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldCategoryEnum.TEXT_INPUT.name());
                        CustomFieldTextTO text = new CustomFieldTextTO();
                        text.setId(modelFieldMap.get(CRM_OPPORTUNITY_NUMBER) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                        text.setTitle(modelFieldLocalizer.localizeOpportunity(CRM_OPPORTUNITY_NUMBER));
                        text.setText(opportunityListItem.getNumberData().getNumberString());
                        customField.setObject(text);

                        customFields.add(customField);
                    }
                    if (opportunityListItem.getTypeId() != null && CRM_OPPORTUNITY_TYPE.equals(modelField.getField_ID())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                        CategoryTO category = new CategoryTO(opportunityListItem.getTypeId(), opportunityListItem.getType());
                        customField.setObject(new CustomFieldCategoryChooseTO(modelFieldMap.get(CRM_OPPORTUNITY_TYPE) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS, modelFieldLocalizer.localizeOpportunity(CRM_OPPORTUNITY_TYPE), category));
                        customFields.add(customField);
                    }
                    if (StringUtils.isNotBlank(opportunityListItem.getNextStep()) && CRM_OPPORTUNITY_NEXT_STEP.equals(modelField.getField_ID())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldCategoryEnum.TEXT_INPUT.name());
                        CustomFieldTextTO text = new CustomFieldTextTO();
                        text.setId(modelFieldMap.get(CRM_OPPORTUNITY_NEXT_STEP) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                        text.setTitle(modelFieldLocalizer.localizeOpportunity(CRM_OPPORTUNITY_NEXT_STEP));
                        text.setText(opportunityListItem.getNextStep());
                        customField.setObject(text);

                        customFields.add(customField);
                    }
                    if (opportunityListItem.getProbability() != null && CRM_OPPORTUNITY_PROBABILITY.equals(modelField.getField_ID())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldCategoryEnum.NUMBER_INPUT.name());
                        CustomFieldNumberTO number = new CustomFieldNumberTO();
                        number.setId(modelFieldMap.get(CRM_OPPORTUNITY_PROBABILITY) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                        number.setTitle(modelFieldLocalizer.localizeOpportunity(CRM_OPPORTUNITY_PROBABILITY));
                        number.setValue(String.valueOf(opportunityListItem.getProbability()));
                        customField.setObject(number);

                        customFields.add(customField);
                    }
                    if (opportunityListItem.getExpectedRevenue() != null && opportunityListItem.getExpectedRevenue() != 0d && CRM_OPPORTUNITY_EXPECTED_REVENUE.equals(modelField.getField_ID())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldCategoryEnum.NUMBER_INPUT.name());
                        CustomFieldNumberTO number = new CustomFieldNumberTO();
                        number.setId(modelFieldMap.get(CRM_OPPORTUNITY_EXPECTED_REVENUE) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                        number.setTitle(modelFieldLocalizer.localizeOpportunity(CRM_OPPORTUNITY_EXPECTED_REVENUE));
                        number.setValue(String.valueOf(opportunityListItem.getExpectedRevenue()));
                        customField.setObject(number);

                        customFields.add(customField);
                    }
                    if (opportunityListItem.getCampaignId() != null && CRM_OPPORTUNITY_CAMPAIGN_SOURCE.equals(modelField.getField_ID())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                        CategoryTO category = new CategoryTO(opportunityListItem.getCampaignId(), opportunityListItem.getCampaign());
                        customField.setObject(new CustomFieldCategoryChooseTO(modelFieldMap.get(CRM_OPPORTUNITY_CAMPAIGN_SOURCE) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS, modelFieldLocalizer.localizeOpportunity(CRM_OPPORTUNITY_CAMPAIGN_SOURCE), category));
                        customFields.add(customField);
                    }
                    if (opportunityListItem.getLeadSourceId() != null && CRM_OPPORTUNITY_LEAD_SOURCE.equals(modelField.getField_ID())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                        CategoryTO category = new CategoryTO(opportunityListItem.getLeadSourceId(), opportunityListItem.getLeadSource());
                        customField.setObject(new CustomFieldCategoryChooseTO(modelFieldMap.get(CRM_OPPORTUNITY_LEAD_SOURCE) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS, modelFieldLocalizer.localizeOpportunity(CRM_OPPORTUNITY_LEAD_SOURCE), category));
                        customFields.add(customField);
                    }
                }
                customFields.addAll(getCustomFields(opportunityListItem.getCustomFields()));
                entityCustomFields.setList(customFields);
            }
            return successResponse(entityCustomFields);
        }
        if (EntityTypeEnum.TASKS.name().equalsIgnoreCase(main_entity_name)) {
            ArrayList<String> taskTypes = new ArrayList<>();
            taskTypes.add(NUMBER);
            taskTypes.add(ATTACHMENTS);
            LinkedHashMap<String, Integer> modelFieldMap = new LinkedHashMap<>();
            TaskSingleItem taskItem;
            try {
                taskItem = taskServiceLocal.getTask(item_id, true);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            List<EdsModelField> taskModelFields = modelFieldManager.getSpecificFields(LayoutRPC.TASK_MAX_FORM, taskTypes);
            if (taskModelFields == null || taskModelFields.size() <= 0) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Model Fields are not found with provided types", NOT_FOUND, HttpStatus.NOT_FOUND);
            }

            if (taskItem != null) {
                for (EdsModelField taskModelField : taskModelFields) {
                    modelFieldMap.put(taskModelField.getField_ID(), taskModelField.getObjectID());

                    if (taskItem.getNumberData() != null && NUMBER.equals(taskModelField.getField_ID())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldCategoryEnum.TEXT_INPUT.name());
                        CustomFieldTextTO text = new CustomFieldTextTO();
                        text.setId(modelFieldMap.get(NUMBER) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                        text.setTitle(modelFieldLocalizer.localizeTask(NUMBER));
                        text.setText(taskItem.getNumberData().getNumberString());
                        customField.setObject(text);

                        customFields.add(customField);
                    }
                    List<FileResource> taskAttachments = attachmentUtilsManager.getAttachments(Constants.F_TASK, taskItem.getProjectID(), taskItem.getObjectID());
                    if (taskAttachments != null && !taskAttachments.isEmpty() && ATTACHMENTS.equals(taskModelField.getField_ID())) {
                        ArrayList<AttachmentTO> files = new ArrayList<>();
                        for (FileResource fileResource : taskAttachments) {
                            AttachmentTO attachment = new AttachmentTO();
                            attachment.setFile_name(fileResource.getFileName());
                            attachment.setLink(fileResource.getDownloadUrl());
                            files.add(attachment);
                        }
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldTypeEnum.FILE_UPLOAD.name());

                        CustomFieldFileUploadTO fileUpload = new CustomFieldFileUploadTO();

                        fileUpload.setId(taskModelField.getObjectID() + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                        fileUpload.setTitle(modelFieldLocalizer.localizeTask(ATTACHMENTS));
                        fileUpload.setFiles(files);

                        customField.setObject(fileUpload);

                        customFields.add(customField);
                    }
                }
                customFields.addAll(getCustomFields(taskItem.getCustomFieldItems()));
                entityCustomFields.setList(customFields);
            }
            return successResponse(entityCustomFields);
        }
        if (EntityTypeEnum.EVENTS.name().equalsIgnoreCase(main_entity_name) || EntityTypeEnum.CALLS.name().equalsIgnoreCase(main_entity_name)) {
            EventItem event;
            try {
                event = crmServiceLocal.getEvent(item_id);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            if (event == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Event with id ".concat(item_id.toString()).concat(" is not found"), NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            customFields.addAll(getCustomFields(event.getCustomFieldItems()));
            entityCustomFields.setList(customFields);
            return successResponse(entityCustomFields);
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "main_entity_name should be one of leads, companies, contacts, opportunities, tasks, events, calls", REQUIRED, HttpStatus.BAD_REQUEST);
        }
    }

    private void getStaticFixedFieldsOfEntity(ArrayList<CustomFieldListTO> customFieldLists,
                                              String main_entity_path, String mode) throws RestException {

        List<ModelField> edsModelFields;
        try {
            edsModelFields = modelFieldManager.getFields(FORM_TYPES.get(main_entity_path));
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (edsModelFields != null) {
            LinkedHashMap<String, FormProperty> fields = new LinkedHashMap<>();
            EdsFormProperty edsFormProperty = formPropertyManager.getByFormID(FORM_TYPES.get(main_entity_path));
            if (edsFormProperty != null) {
                Gson gson = new Gson();
                FormProperty[] formFields = gson.fromJson(edsFormProperty.getSettingsJSONData(), FormProperty[].class);
                for (FormProperty formProperty : formFields) {
                    if (formProperty != null) {
                        if (formProperty.getDefaultValue() != null && formProperty.getDefaultValue().length() == 0) {
                            formProperty.setDefaultValue(null);
                        }
                        if (formProperty.getRoleEdit() != null && formProperty.getRoleEdit().size() > 0) {
                            if (userManager.getUser().hasEitherRoles(formProperty.getRoleEdit().toArray(new Integer[]{}))) {
                                formProperty.setDisabled(false);
                            }
                        }
                        fields.put(formProperty.getCode(), formProperty);
                    }
                }
            }
            List<EdsCustomFormSection> sectionsList = customFormSectionManager.getSections(LayoutRPC.OPPORTUNITY_FORM);
            LinkedHashMap<String, EdsCustomFormSection> sections = new LinkedHashMap<>();
            sectionsList.forEach(sec -> {
                if (sec.getLabel() == null) {
                    sec.setLabel(modelFieldLocalizer.localizeOpportunity(sec.getSection()));
                }
                sections.put(sec.getSection(), sec);
            });
            edsModelFields.forEach(edsModelField -> {

                //for events and calls only custom fields must be returned
                if (("events".equalsIgnoreCase(main_entity_path) || "calls".equalsIgnoreCase(main_entity_path)) && !edsModelField.isIsCustomField()) {
                    return;
                }

                //Opportunity probability and expected revenue are read only fields and the system calc itself their values
                if (CustomFormConstants.CRM_OPPORTUNITY_EXPECTED_REVENUE.equals(edsModelField.getField_ID()) || CustomFormConstants.CRM_OPPORTUNITY_PROBABILITY.equals(edsModelField.getField_ID())) {
                    return;
                }
                //we need to check this otherwise other non visible fields will apear
                if (!edsModelField.isHideInCustomizeForm() && !edsModelField.isHide()) {
                    //Determine what is the Field type (One of TEXT_INPUT, DATE, NUMBER_INPUT, FILE_UPLOAD, CATEGORY_CHOOSE, MULTIPLY_CHOOSE)
                    String fieldType = getTypeOfStaticField(edsModelField, main_entity_path, mode);
                    //We must retrieve only non fixed fields and sections
                    String fixedField = MOBILE_FIXED_FIELDS.get(FORM_TYPES.get(main_entity_path) + "_" + edsModelField.getField_ID());
                    String fixedSection = MOBILE_FIXED_SECTIONS.get(FORM_TYPES.get(main_entity_path) + "_" + edsModelField.getSection());

                    if (StringUtils.isNotBlank(fieldType) && StringUtils.isBlank(fixedField) && StringUtils.isBlank(fixedSection)) {

                        CustomFieldListTO field = new CustomFieldListTO();
                        field.setRequired(edsModelField.isSystemMandatory() || edsModelField.isMandatory());
                        //Localize Field name
                        if (!edsModelField.isIsCustomField()) {
                            field.setId(edsModelField.getObjectID() + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                            field.setTitle(modelFieldLocalizer.localizeByFieldID(FORM_TYPES.get(main_entity_path), edsModelField.getField_ID()));
                            FormProperty formProperty = fields.get(edsModelField.getField_ID());
                            if (formProperty != null) {
                                field.addProperty("canEdit", !formProperty.isDisabled());
                                if (formProperty.isChanged()) {
                                    field.setTitle(formProperty.getTitle());
                                }
                                field.addProperty("disabled", formProperty.isDisabled());
                                field.setRequired(formProperty.isRequired());
                            }
                        } else {
                            EdsCompanyCustomFieldsSettings cf = companyCFSettingsManager.getCompanyCustomField(CF_TYPES.get(main_entity_path).name(), edsModelField.getField_ID());
                            if (cf != null) {
                                field.setId(cf.getObjectID());
                                if (cf.getAllowedRoles() != null && !cf.getAllowedRoles().isEmpty()) {
                                    EdsUser user = userManager.getUser();
                                    if (user.hasEitherRoles(cf.getAllowedRoles().toArray(new EdsRole[]{}))) {
                                        field.setTitle(edsModelField.getLabel());
                                    }
                                } else {
                                    field.setTitle(edsModelField.getLabel());
                                }
                            }
                        }
                        field.setField_type(fieldType);
                        field.addProperty("colType", edsModelField.getColumnType() != null ? edsModelField.getColumnType().name() : "");
                        field.addProperty("order", edsModelField.getForder());
                        if (main_entity_path.equals("opportunities")) {
                            field.addProperty("section", sections.get(edsModelField.getSection()).getLabel());
                            field.addProperty("sectionId", sections.get(edsModelField.getSection()).getObjectID());
                        }
                        //Return it only if title is not empty
                        if (StringUtils.isNotBlank(field.getTitle())) {
                            customFieldLists.add(field);
                        } else {
                            log.info("Couldnt translate modelfield:" + FORM_TYPES.get(main_entity_path) + "->" + edsModelField.getField_ID());
                        }
                    }
                }

            });
        }
    }

    private String getTypeOfStaticField(ModelField edsModelField, String main_entity_path, String mode) {

        //Dont return below in EDIT mode
        if (MODE_EDIT.equalsIgnoreCase(mode)) {

//            if ("leads".equalsIgnoreCase(main_entity_path) || "contacts".equalsIgnoreCase(main_entity_path) || "opportunities".equalsIgnoreCase(main_entity_path)) {
            if (MOBILE_EXCLUDED_FIELDS.get(FORM_TYPES.get(main_entity_path) + "_" + edsModelField.getField_ID()) != null) {
                return null;
            }
//            }
        }

        if ((CRM_ACCOUNT_TYPE.equals(edsModelField.getField_ID()) && "MULTITABLE".equalsIgnoreCase(edsModelField.getWidget()))
                || (CATEGORY.equals(edsModelField.getField_ID()) && "UNKNOWN".equalsIgnoreCase(edsModelField.getWidget()))) {

            //This part is hardcoded because we dont have type of MULTITABLE in customfields and its logic behind it is different
            return CustomFieldCategoryEnum.MULTIPLY_CHOOSE.getCategory();
        } else if ((ATTACHMENTS.equals(edsModelField.getField_ID()) || CRM_OPPORTUNITY_ATTACHMENTS.equals(edsModelField.getField_ID()))
                && "UNKNOWN".equalsIgnoreCase(edsModelField.getWidget())) {

            //This part is hardcoded because we dont have type of MULTITABLE in customfields and its logic behind it is different
            return CustomFieldCategoryEnum.FILE_UPLOAD.getCategory();
        } else if (("FileUploadItem".equalsIgnoreCase(edsModelField.getWidget()) || "FileUploadWidget".equalsIgnoreCase(edsModelField.getWidget()))
                && "File Upload".equalsIgnoreCase(edsModelField.getType())) {

            return CustomFieldCategoryEnum.FILE_UPLOAD.getCategory();

        } else if ("TextBox".equalsIgnoreCase(edsModelField.getWidget()) && "Number".equalsIgnoreCase(edsModelField.getType())) {

            return CustomFieldCategoryEnum.NUMBER_INPUT.getCategory();

        } else if ("TextBox".equalsIgnoreCase(edsModelField.getWidget()) || "TextArea".equalsIgnoreCase(edsModelField.getWidget())
                || "NoteWidget".equalsIgnoreCase(edsModelField.getWidget()) || "MULTITABLE".equalsIgnoreCase(edsModelField.getWidget())) {

            return CustomFieldCategoryEnum.TEXT_INPUT.getCategory();
        } else if ("DropDown".equalsIgnoreCase(edsModelField.getWidget())
                || "LOOKUP".equalsIgnoreCase(edsModelField.getWidget()) || "RadioButton".equalsIgnoreCase(edsModelField.getWidget())) {

            return CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory();
        } else if ("CheckBox".equalsIgnoreCase(edsModelField.getWidget())) {

            return CustomFieldCategoryEnum.MULTIPLY_CHOOSE.getCategory();
        } else if ("DatePicker".equalsIgnoreCase(edsModelField.getWidget())) {

            return CustomFieldCategoryEnum.DATE.getCategory();
        } else {
            return null;
        }
    }
}
