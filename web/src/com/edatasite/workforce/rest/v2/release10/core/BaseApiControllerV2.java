package com.edatasite.workforce.rest.v2.release10.core;

import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsCompanyCustomFieldsSettings;
import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.ApprovalListResult;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrContactRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.app.AllInOneServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.WfmCommandServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.CompanyCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.CountryManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.crm.client.rpc.EventItem;
import com.edatasite.workforce.gwt.crm.client.rpc.MailMessageItem;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityListItem;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.server.app.DocumentsServiceLocal;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceList;
import com.edatasite.workforce.gwt.profile.server.app.ProfileServiceLocal;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.edatasite.workforce.gwt.task.server.app.TaskServiceLocal;
import com.edatasite.workforce.rest.base.enums.FolderRelationTypeEnum;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ApiResult;
import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ErrorTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.customfield.CustomFieldCategoryChooseTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.customfield.CustomFieldDateTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.customfield.CustomFieldFileUploadTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.customfield.CustomFieldMultiplyChooseObjectTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.customfield.CustomFieldNumberTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.customfield.CustomFieldTextTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.FilteredStatusItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.pm.task.CustomFieldsTO;
import com.edatasite.workforce.rest.v2.release10.core.to.status.ColorTO;
import com.edatasite.workforce.rest.v2.release10.enums.CustomFieldCategoryEnum;
import com.edatasite.workforce.rest.v2.release10.enums.CustomFieldTypeEnum;
import com.edatasite.workforce.rest.v2.release10.enums.EntityTypeEnum;
import com.edatasite.workforce.rest.v2.release10.enums.LinkTypeEnum;
import com.edatasite.workforce.rest.v2.release10.enums.OrderFieldEnum;
import com.edatasite.workforce.rest.v2.release10.enums.RequestActionEnum;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DATA_TYPE_PROFILE_IMAGE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.F_CUSTOM_FIELD_ITEM;

/**
 * Created by Dilsh0d on 9/25/2017.
 */
public class BaseApiControllerV2 implements ApiConstants {

    protected Pattern EMAIL_PATTERN = Pattern.compile("^\\w+([_.-]\\w+)*@(\\w+([_.-]\\w+)*)");

    protected final Integer MAX_LIMIT = 100000;
    protected static final Integer GAP_BTW_STATIC_AND_CUSTOM_FIELDS = 100000;

    protected static String customFieldFileNameRegex = "custom_field_(\\d*)_attachment_(\\d+)";
    protected static String attachmentNameRegex = "attachment_(\\d+)";
    protected static String entityFileNameRegex = "file[0-9]";
    protected static String entityItemFileNameRegex = "item_[0-9]_file_[0-9]";

    private static final Logger log = LoggerFactory.getLogger(BaseApiControllerV2.class);

    @Autowired
    protected UserManager userManager;
    @Autowired
    protected CountryManager countryManager;
    @Autowired
    protected AllInOneServiceLocal allInOneServiceLocal;
    @Autowired
    protected ProfileServiceLocal profileServiceLocal;
    @Autowired
    protected DocumentsServiceLocal documentsServiceLocal;
    @Autowired
    protected CommonService commonService;
    @Autowired
    protected CompanyCustomFieldsManager companyCustomFieldsManager;
    @Autowired
    @Qualifier("commonLocalizer")
    protected WfmMessageSource commonLocalizer;
    @Autowired
    @Qualifier("crmLocalizer")
    private WfmMessageSource crmLocalizer;
    @Autowired
    protected TaskServiceLocal taskServiceLocal;
    @Autowired
    protected WfmCommandServiceLocal wfmCommandServiceLocal;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    protected EmployeeManager employeeManager;

    private static final HashSet<String> countryCodes = new HashSet<>();

    public static ApiResult successResponse(ResponseData data) {
        ApiResult result = new ApiResult();
        result.setSuccess(Boolean.TRUE);
        result.setData(data);
        result.setError(new ErrorTO());
        return result;
    }

    protected Object validateUser() throws RestException {
        if (isExpireSession()) {
            throw new RestException("Session expired", "Session expired", EXPIRED, HttpStatus.UNAUTHORIZED);
        }

        EdsUser user = null;
        try {
            user = (EdsUser) ServerSecurityContext.getInstance().getUser();
            if (user == null) {
                throw new RestException("Session expired", "Session expired", EXPIRED, HttpStatus.UNAUTHORIZED);
            }
        } catch (RestException e) {
            log.error("", e);
            throw new RestException("Session expired", "Session expired", EXPIRED, HttpStatus.UNAUTHORIZED);
        }

        if (user instanceof EdsClientContact) {
            if (user.getClientContact().getAccess() == null || !user.getClientContact().getAccess()) {
                throw new RestException("Access denied", "Access denied", ACCESS_DENIED, HttpStatus.FORBIDDEN);
            }
        }

        if (user.getDeleted() || Constants.USER_TYPE_BMT_RESPONDENT.equals(user.getUserType())) {
            throw new RestException("Your account was disabled. Please contact your company admin.", "User is deleted/resigned.", ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }

        String status = userManager.getUserStatus(user.getObjectID());
        if (!Constants.EMPLOYEE_STATUS_ACTIVE.equals(status)) {
            throw new RestException("Please verify your registration from a confirmation email sent to you to proceed.", "User is not active.", ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }

        return user;
    }

    protected boolean isExpireSession() {
        return StringUtil.isEmpty(ServerSecurityContext.getInstance().getSessionId());
    }

    //This one will not work because we handling exceptions with RestExceptionHandler
    /*@ExceptionHandler(RestException.class)
    public ResponseEntity<ErrorResponse> handleApiException(HttpServletRequest req, RestException e1) {
        ErrorResponse error = new ErrorResponse(e1.getUser_msg(), e1.getDeveloper_msg(), e1.getError_code());
        return new ResponseEntity<ErrorResponse>(error, e1.getStatus());
    }*/

    /*@ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleBadRequest(HttpMessageNotReadableException e) {
        ErrorResponse error = new ErrorResponse(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR);
        return new ResponseEntity<ErrorResponse>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }*/

    //This one will not work because we handling exceptions with RestExceptionHandler
    /*@ExceptionHandler(Throwable.class)
    public
    @ResponseBody
    ResponseEntity<ErrorResponse> handleDefaultException(HttpServletRequest req, HttpServletResponse res, Throwable ex) {

        if (ex instanceof HttpMessageNotReadableException) {
            ErrorResponse error = new ErrorResponse(GENERAL_ERROR_MESSAGE, ex.getMessage(), INVALID);
            return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
        } else {
            ErrorResponse error = new ErrorResponse(GENERAL_ERROR_MESSAGE, ex.getMessage(), SERVER_ERROR);
            return new ResponseEntity<ErrorResponse>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/

    private static LinkedHashMap<OrderFieldEnum, String> getSortFieldMap(ListPanelType listPanelType) {
        LinkedHashMap<OrderFieldEnum, String> sortFieldMap = new LinkedHashMap<>();
        if (ListPanelType.LeadListPanel.equals(listPanelType)) {
            sortFieldMap.put(OrderFieldEnum.DATE, ContactListItem.CREATION_DATE);
            sortFieldMap.put(OrderFieldEnum.NAME, ContactListItem.CONTACT_NAME);
            sortFieldMap.put(OrderFieldEnum.ID, SolrContactRepresenter.FIELD_CONTACT_ID);
            sortFieldMap.put(OrderFieldEnum.COMPANY, ContactListItem.CRM_ACCOUNT);
            sortFieldMap.put(OrderFieldEnum.KANBAN_ORDER, ContactListItem.KANBAN_ORDER);
        } else if (ListPanelType.OpportunitiesListPanel.equals(listPanelType)) {
            sortFieldMap.put(OrderFieldEnum.DATE, OpportunityListItem.CREATED_DATE);
            sortFieldMap.put(OrderFieldEnum.NAME, OpportunityListItem.OPPORTUNITY_NAME);
            sortFieldMap.put(OrderFieldEnum.ID, OpportunityListItem.OBJECT_ID);
            sortFieldMap.put(OrderFieldEnum.COMPANY, OpportunityListItem.ACCOUNT_NAME);
            sortFieldMap.put(OrderFieldEnum.KANBAN_ORDER, OpportunityListItem.KANBAN_ORDER);
        } else if (ListPanelType.ContactListPanel.equals(listPanelType)) {
            sortFieldMap.put(OrderFieldEnum.DATE, ContactListItem.CREATION_DATE);
            sortFieldMap.put(OrderFieldEnum.NAME, ContactListItem.CONTACT_NAME);
            sortFieldMap.put(OrderFieldEnum.ID, SolrContactRepresenter.FIELD_CONTACT_ID);
            sortFieldMap.put(OrderFieldEnum.COMPANY, ContactListItem.CRM_ACCOUNT);
        } else if (ListPanelType.CrmAccountListPanel.equals(listPanelType)) {
            sortFieldMap.put(OrderFieldEnum.DATE, CrmAccountItem.CREATION_DATE);
            sortFieldMap.put(OrderFieldEnum.NAME, CrmAccountItem.ACCOUNT_NAME);
            sortFieldMap.put(OrderFieldEnum.ID, SolrContactRepresenter.FIELD_CRM_ACCOUNT_ID);
            sortFieldMap.put(OrderFieldEnum.COMPANY, CrmAccountItem.ACCOUNT_NAME);
        } else if (ListPanelType.MessageListPanel.equals(listPanelType) || ListPanelType.SentMessageListPanel.equals(listPanelType)) {
            sortFieldMap.put(OrderFieldEnum.DATE, MailMessageItem.CREATED);
            sortFieldMap.put(OrderFieldEnum.NAME, MailMessageItem.SUBJECT);
            sortFieldMap.put(OrderFieldEnum.ID, MailMessageItem.ID);
        } else if (ListPanelType.MessageCenter.equals(listPanelType)) {
            sortFieldMap.put(OrderFieldEnum.DATE, Email.CREATED_DATE);
            sortFieldMap.put(OrderFieldEnum.NAME, Email.SUBJECT);
            sortFieldMap.put(OrderFieldEnum.ID, Email.ID);
        } else if (ListPanelType.EventsListPanel.equals(listPanelType)) {
            sortFieldMap.put(OrderFieldEnum.DATE, EventItem.START_DATE);
            sortFieldMap.put(OrderFieldEnum.NAME, EventItem.SUBJECT);
            sortFieldMap.put(OrderFieldEnum.ID, EventItem.ID);
        } else if (ListPanelType.SaleQuoteListPanel.equals(listPanelType) || ListPanelType.SaleOrderListPanel.equals(listPanelType) || ListPanelType.SaleInvoiceListPanel.equals(listPanelType)) {
            sortFieldMap.put(OrderFieldEnum.DATE, InvoiceList.INVOICE_DATE);
            sortFieldMap.put(OrderFieldEnum.NAME, InvoiceList.INVOICE_NUMBER);
            sortFieldMap.put(OrderFieldEnum.ID, InvoiceList.ID);
        } else if (ListPanelType.TaskListPanel.equals(listPanelType)) {
            sortFieldMap.put(OrderFieldEnum.DATE, TaskListItem.START_DATE);
            sortFieldMap.put(OrderFieldEnum.NAME, TaskListItem.NUMBER);
            sortFieldMap.put(OrderFieldEnum.ID, TaskListItem.ID);
            sortFieldMap.put(OrderFieldEnum.KANBAN_ORDER, OpportunityListItem.KANBAN_ORDER);
        }
        return sortFieldMap;
    }

    public static String getSortField(OrderFieldEnum orderByEnum, ListPanelType listPanelType) {
        if (orderByEnum == null)
            return null;
        LinkedHashMap<OrderFieldEnum, String> sortFieldMap = getSortFieldMap(listPanelType);
        return sortFieldMap.get(orderByEnum);
    }

    /**
     * Request Action Mapping
     *
     * @param action
     * @return
     */
    protected static String getRequestAction(String action) {
        return getRequestActions().stream()
                .filter(x -> x.equals(action))
                .findFirst()
                .orElse(null);
    }

    private static ArrayList<String> getRequestActions() {
        ArrayList<String> actions = new ArrayList<>();
        actions.add(RequestActionEnum.APPROVE.name());
        actions.add(RequestActionEnum.REJECT.name());
        actions.add(RequestActionEnum.APPROVE_FOR_ALL.name());

        return actions;
    }

    protected ArrayList<SelectItem> getAllAvailableApprovers(String type) {
        ApprovalListResult approvalListResult = allInOneServiceLocal.getApprovers(type, null, true, null, false);
        ArrayList<SelectItem> itemTOs = new ArrayList<>();
        if (approvalListResult.getList() != null) {
            int order = 0;
            for (ApproverItem item : approvalListResult.getList()) {
                SelectItem itemTO = new SelectItem();
                itemTO.setId(item.getObjectID());
                itemTO.setName("Approver" + order);
                itemTO.setDescription(String.valueOf(order));
                itemTOs.add(itemTO);
                order++;
            }
        }
        return itemTOs;
    }

    protected ArrayList<ApproverItemMini> getChosenApprovers(ArrayList<Integer> approverIds, ArrayList<SelectItem> approverItems) {
        ArrayList<ApproverItemMini> result = new ArrayList<>();
        int order = 0;
        for (Integer approverid : approverIds) {
            ApproverItemMini item = new ApproverItem();
            item.setApproverOrder(order);
            item.setClonedFrom(approverItems.get(order).getId());

            SelectItem exactEmployee = new SelectItem();
            exactEmployee.setId(approverid);
            //exactEmployee.setName(approver.getName());
            item.setExactEmployee(exactEmployee);
            result.add(item);
            order++;
        }
        return result;
    }

    protected ArrayList<CustomFieldsTO> getCustomFields(List<CompanyCustomFieldItem> companyCustomFieldItems) {

        ArrayList<CustomFieldsTO> customFields = new ArrayList<>();
        NumberFormat numberFormat = new DecimalFormat("###.##");

        if (companyCustomFieldItems != null && companyCustomFieldItems.size() > 0) {

            SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

            companyCustomFieldItems.forEach(customFieldItem -> {
                if (customFieldItem.getEntityId() != null && (StringUtils.isNotBlank(customFieldItem.getFieldStringValue()) || customFieldItem.getFieldDateNonConvertedValue() != null)) {
                    CustomFieldsTO customField = null;
                    if ("Text".equalsIgnoreCase(customFieldItem.getDataType()) &&
                            ("TextBox".equalsIgnoreCase(customFieldItem.getUiType())
                                    || "TextArea".equalsIgnoreCase(customFieldItem.getUiType())
                                    || "HtmlTextArea".equalsIgnoreCase(customFieldItem.getUiType())
                                    || Constants.TYPE_ENTITY_LOOKUP.equalsIgnoreCase(customFieldItem.getUiType())
                                    || Constants.UI_TYPE_MULTI_LOOKUP.equalsIgnoreCase(customFieldItem.getUiType()))
                            && StringUtils.isNotBlank(customFieldItem.getFieldStringValue())) {
                        //if ("Text".equalsIgnoreCase(customFieldItem.getDataType()) && StringUtils.isNotBlank(customFieldItem.getFieldStringValue())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldTypeEnum.TEXT_INPUT.name());
                        customField.setObject(new CustomFieldTextTO(customFieldItem.getEntityId(), customFieldItem.getFieldName(), customFieldItem.getFieldStringValue()));
                    } else if ("Date".equalsIgnoreCase(customFieldItem.getDataType()) && customFieldItem.getFieldDateNonConvertedValue() != null) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldTypeEnum.DATE.name());
                        customField.setObject(new CustomFieldDateTO(customFieldItem.getEntityId(), customFieldItem.getFieldName(), longDateTimezoneFormat.format(customFieldItem.getFieldDateNonConvertedValue().getNonConvertedDate())));
                    } else if ("Number".equalsIgnoreCase(customFieldItem.getDataType()) && "TextBox".equalsIgnoreCase(customFieldItem.getUiType()) && StringUtils.isNotBlank(customFieldItem.getFieldStringValue())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldTypeEnum.NUMBER_INPUT.name());
                        Double number = Double.valueOf(customFieldItem.getFieldStringValue());
                        customField.setObject(new CustomFieldNumberTO(customFieldItem.getEntityId(), customFieldItem.getFieldName(), numberFormat.format(number)));
                    } else if ("File Upload".equalsIgnoreCase(customFieldItem.getDataType()) && customFieldItem.getFieldStringValue() != null) {
                        ArrayList<FileResource> fileResources = documentsServiceLocal.getFileResources(F_CUSTOM_FIELD_ITEM, Double.valueOf(customFieldItem.getFieldStringValue()).intValue(), customFieldItem.getObjectId());
                        if (fileResources != null && !fileResources.isEmpty()) {
                            ArrayList<AttachmentTO> files = new ArrayList<>();
                            for (FileResource fileResource : fileResources) {
                                AttachmentTO attachment = new AttachmentTO();
                                attachment.setFile_name(fileResource.getFileName());
                                attachment.setLink(fileResource.getDownloadUrl());
                                files.add(attachment);
                            }
                            customField = new CustomFieldsTO();
                            customField.setType(CustomFieldTypeEnum.FILE_UPLOAD.name());

                            CustomFieldFileUploadTO fileUpload = new CustomFieldFileUploadTO();
                            fileUpload.setId(customFieldItem.getEntityId());
                            fileUpload.setTitle(customFieldItem.getFieldName());
                            fileUpload.setFiles(files);
                            customField.setObject(fileUpload);
                        }
                    } else if (DATA_TYPE_PROFILE_IMAGE.equalsIgnoreCase(customFieldItem.getDataType()) && customFieldItem.getFieldStringValue() != null) {
                        String fileLink = commonService.getImageUrl(Double.valueOf(customFieldItem.getFieldStringValue()).intValue());
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldTypeEnum.FILE_UPLOAD.name());
                        AttachmentTO attachment = new AttachmentTO();
                        attachment.setFile_name("Photo");
                        attachment.setLink(fileLink);
                        customField.setObject(attachment);
                    } else if ("Text".equalsIgnoreCase(customFieldItem.getDataType()) && ("RadioButton".equalsIgnoreCase(customFieldItem.getUiType())
                            || "DropDown".equalsIgnoreCase(customFieldItem.getUiType()))
                            && StringUtils.isNotBlank(customFieldItem.getFieldStringValue()) && customFieldItem.getPredefinedValuesWithSorting() != null) {
                        customField = new CustomFieldsTO();
                        String[] customFieldMultiValues = customFieldItem.getPredefinedValues();
                        Integer customFieldId = null;
                        for (int i = 0; i < customFieldMultiValues.length; i++) {
                            if (customFieldMultiValues[i].equals(customFieldItem.getFieldStringValue())) {
                                customFieldId = ++i;
                            }
                        }
                        customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                        CategoryTO category = new CategoryTO(customFieldId, customFieldItem.getFieldStringValue());
                        customField.setObject(new CustomFieldCategoryChooseTO(customFieldItem.getEntityId(), customFieldItem.getFieldName(), category));
                    } else if (Constants.DATA_TYPE_TEXT.equals(customFieldItem.getDataType()) && Constants.UI_TYPE_LOOKUP.equalsIgnoreCase(customFieldItem.getUiType())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                        CategoryTO category = new CategoryTO(customFieldItem.getSelectedId(), customFieldItem.getFieldStringValue());
                        customField.setObject(new CustomFieldCategoryChooseTO(customFieldItem.getEntityId(), customFieldItem.getFieldName(), category));
                    } else if ("Number".equalsIgnoreCase(customFieldItem.getDataType()) && ("RadioButton".equalsIgnoreCase(customFieldItem.getUiType()) || "DropDown".equalsIgnoreCase(customFieldItem.getUiType())) &&
                            StringUtils.isNotBlank(customFieldItem.getFieldStringValue()) && customFieldItem.getPredefinedValuesWithSorting() != null) {
                        customField = new CustomFieldsTO();
                        SelectItem[] customFieldMultiValues = customFieldItem.getPredefinedValuesWithSorting();
                        Integer customFieldId = null;
                        for (SelectItem customFieldMultiValue : customFieldMultiValues) {
                            if (customFieldMultiValue.getId().toString().equals(customFieldItem.getFieldStringValue()) || customFieldMultiValue.getName().equals(customFieldItem.getFieldStringValue())) {
                                customFieldId = customFieldMultiValue.getId();
                            }
                        }
                        customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                        Double number = Double.valueOf(customFieldItem.getFieldStringValue());
                        CategoryTO category = new CategoryTO(customFieldId, numberFormat.format(number));
                        customField.setObject(new CustomFieldCategoryChooseTO(customFieldItem.getEntityId(), customFieldItem.getFieldName(), category));

                    } else if ("Text".equalsIgnoreCase(customFieldItem.getDataType()) && "CheckBox".equalsIgnoreCase(customFieldItem.getUiType())
                            && StringUtils.isNotBlank(customFieldItem.getFieldStringValue())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldCategoryEnum.MULTIPLY_CHOOSE.getCategory());
                        String[] choosenValues = customFieldItem.getFieldStringValue().split("-:-");
                        SelectItem[] customFieldMultiValues = customFieldItem.getPredefinedValuesWithSorting();
                        ArrayList<CategoryTO> choosed_items = new ArrayList<>();
                        if (choosenValues.length > 0) {
                            for (SelectItem customFieldMultiValue : customFieldMultiValues) {
                                for (String choosenValue : choosenValues) {
                                    if (customFieldMultiValue.getId().toString().equals(choosenValue) || customFieldMultiValue.getName().equals(choosenValue)) {
                                        CategoryTO category = new CategoryTO();
                                        category.setId(customFieldMultiValue.getId());
                                        category.setTitle(choosenValue);
                                        choosed_items.add(category);
                                    }
                                }
                            }
                            customField.setObject(new CustomFieldMultiplyChooseObjectTO(customFieldItem.getEntityId(), customFieldItem.getFieldName(), choosed_items));
                        }

                    } else if ("Number".equalsIgnoreCase(customFieldItem.getDataType()) && "CheckBox".equalsIgnoreCase(customFieldItem.getUiType())
                            && StringUtils.isNotBlank(customFieldItem.getFieldStringValue())) {
                        customField = new CustomFieldsTO();
                        customField.setType(CustomFieldCategoryEnum.MULTIPLY_CHOOSE.getCategory());
                        String[] choosenValues = customFieldItem.getFieldStringValue().split("-:-");
                        SelectItem[] customFieldMultiValues = customFieldItem.getPredefinedValuesWithSorting();
                        ArrayList<CategoryTO> choosed_items = new ArrayList<>();
                        if (choosenValues.length > 0) {
                            for (SelectItem customFieldMultiValue : customFieldMultiValues) {
                                for (String choosenValue : choosenValues) {
                                    if (customFieldMultiValue.getId().toString().equals(choosenValue) || customFieldMultiValue.getName().equals(choosenValue)) {
                                        CategoryTO category = new CategoryTO();
                                        category.setId(customFieldMultiValue.getId());
                                        category.setTitle(choosenValue);
                                        choosed_items.add(category);
                                    }
                                }
                            }
                            customField.setObject(new CustomFieldMultiplyChooseObjectTO(customFieldItem.getEntityId(), customFieldItem.getFieldName(), choosed_items));
                        }
                    }
                    if (customField != null) {
                        customFields.add(customField);
                    }
                }
            });
        }
        return customFields;
    }

    protected HashSet<String> getPhoneCountryCodes() {
        if (countryCodes.isEmpty()) {
            List<EdsCountry> countryList = countryManager.list(new ListingFilterParameter());
            countryList.forEach(edsCountry -> countryCodes.add(edsCountry.getTelCode()));
            return countryCodes;
        }
        return countryCodes;
    }

    protected String getLocalizedMessages(ViewName viewName, String code, String defaultMessage) {
        if (ViewName.Opportunity.equals(viewName) && "opportunitystage".equals(code)) {
            code = "stage";
        }
        String localizedMessage = crmLocalizer.localizeAccounting(code, defaultMessage);
        if (localizedMessage.equals(defaultMessage)) {
            localizedMessage = commonLocalizer.localizeAccounting(code, defaultMessage);
        }

        //Try to find out in custom fields
        if (viewName != null && localizedMessage.equals(defaultMessage)) {
            EdsCompanyCustomFieldsSettings customField = companyCustomFieldsManager.getCompanyCustomField(viewName.name(), code);
            if (customField != null) {
                localizedMessage = customField.getFieldName();
            }
        }
        return localizedMessage;
    }


    protected ArrayList<CompanyCustomFieldItem> convertCustomFields(ArrayList<Object> custom_fields, MultipartRequest multipartRequest) {

        ArrayList<CompanyCustomFieldItem> customFieldItems = new ArrayList<>();
        Pattern pattern = Pattern.compile(customFieldFileNameRegex);
        //Create Map of MultipartFiles with customfieldid as key
        TreeMap<Integer, ArrayList<MultipartFile>> customFieldAttachmentsMap = new TreeMap<>();
        if (multipartRequest != null && multipartRequest.getFileMap() != null && multipartRequest.getFileMap().size() > 0) {
            for (MultipartFile file : multipartRequest.getFileMap().values()) {
                if (file.getName().matches(customFieldFileNameRegex)) {
                    Matcher m = pattern.matcher(file.getName());
                    Integer customFieldFileId;
                    if (m.matches()) {
                        customFieldFileId = Integer.valueOf(m.group(1));
                        ArrayList<MultipartFile> files = customFieldAttachmentsMap.get(customFieldFileId) == null ? new ArrayList<>() : customFieldAttachmentsMap.get(customFieldFileId);
                        files.add(file);
                        customFieldAttachmentsMap.put(customFieldFileId, files);
                    }
                }
            }
        }

        if (custom_fields != null && custom_fields.size() > 0) {
            LinkedHashMap<Integer, ArrayList<AttachmentTO>> customFieldDraftAttachmentMap = new LinkedHashMap<>();
            SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);
            for (Object customFieldObject : custom_fields) {
                if (customFieldObject instanceof LinkedHashMap) {
                    LinkedHashMap<Object, Object> customFieldsMap = (LinkedHashMap<Object, Object>) customFieldObject;
                    if (customFieldsMap.get("id") != null) {
                        EdsCompanyCustomFieldsSettings edsCompanyCustomFieldsSettings = null;
                        Object object = customFieldsMap.get("id");
                        if (object instanceof Integer) {
                            Integer id = (Integer) object;
                            edsCompanyCustomFieldsSettings = companyCustomFieldsManager.get(id);
                        } else if (object instanceof String) {
                            Integer id = Integer.valueOf((String) object);
                            edsCompanyCustomFieldsSettings = companyCustomFieldsManager.get(id);
                        }
                        if (edsCompanyCustomFieldsSettings != null) {
                            CompanyCustomFieldItem companyCustomFieldItem = new CompanyCustomFieldItem();
                            companyCustomFieldItem.setEntityId(edsCompanyCustomFieldsSettings.getObjectID());
                            companyCustomFieldItem.setFieldName(edsCompanyCustomFieldsSettings.getFieldName());
                            companyCustomFieldItem.setAliasName(edsCompanyCustomFieldsSettings.getAliasName());
                            companyCustomFieldItem.setColumnCode(edsCompanyCustomFieldsSettings.getColumnCode());
                            companyCustomFieldItem.setDataType(edsCompanyCustomFieldsSettings.getDataType());
                            companyCustomFieldItem.setUiType(edsCompanyCustomFieldsSettings.getUiType());

                            if (StringUtils.isNotBlank((String) customFieldsMap.get("text"))) {//for text fields

                                companyCustomFieldItem.setFieldStringValue((String) customFieldsMap.get("text"));

                            } else if (customFieldsMap.get("value") != null) {//for number fields
                                if (Constants.DATA_TYPE_NUMBER.equalsIgnoreCase(edsCompanyCustomFieldsSettings.getDataType())) {
                                    companyCustomFieldItem.setFieldStringValue(customFieldsMap.get("value").toString());
                                }

                            } else if (customFieldsMap.get("category_id") != null) {//for number fields

                                if (edsCompanyCustomFieldsSettings.getPredefinedValues() != null && edsCompanyCustomFieldsSettings.getPredefinedValues().length > 0) {
                                    String[] values = edsCompanyCustomFieldsSettings.getPredefinedValues();
//                                    String values[] = edsCompanyCustomFieldsSettings.getPredefinedValues().split(",");
                                    try {
                                        Integer index;
                                        if (customFieldsMap.get("category_id") instanceof Integer) {
                                            index = (Integer) customFieldsMap.get("category_id");
                                        } else {
                                            index = Integer.valueOf(customFieldsMap.get("category_id").toString());
                                        }

                                        if (index > 0 && index <= values.length) {
                                            companyCustomFieldItem.setFieldStringValue(values[index - 1].split("=")[0]);
                                        }

                                    } catch (Exception e) {
                                        log.error("", e);
                                    }
                                } else {
                                    companyCustomFieldItem.setSelectedId((Integer) customFieldsMap.get("category_id"));
                                }
                            } else if (customFieldsMap.get("reference_id") != null) {
                                EdsReference reference = referenceManager.get((Integer) customFieldsMap.get("reference_id"));
                                companyCustomFieldItem.setFieldStringValue(reference != null ? reference.getName() : null);
                            } else if (customFieldsMap.get("choosed_ids") != null && customFieldsMap.get("choosed_ids") instanceof List) {//for number fields
                                if (edsCompanyCustomFieldsSettings.getPredefinedValues() != null && edsCompanyCustomFieldsSettings.getPredefinedValues().length > 0) {
                                    String[] values = edsCompanyCustomFieldsSettings.getPredefinedValues();
                                    try {
                                        ArrayList<Integer> indexes = new ArrayList<>();
                                        for (Object val : (List) customFieldsMap.get("choosed_ids")) {
                                            if (val instanceof Integer) {
                                                indexes.add((Integer) val);
                                            } else {
                                                indexes.add(Integer.valueOf(val.toString()));
                                            }
                                        }
                                        StringBuilder customFieldValue = new StringBuilder();
                                        for (Integer index : indexes) {
                                            if (index > 0 && index <= values.length) {
                                                customFieldValue.append(values[index - 1]).append("-:-");
                                            }
                                        }
                                        if (customFieldValue.length() > 1 && customFieldValue.substring(customFieldValue.length() - 3).equals("-:-")) {
                                            customFieldValue.substring(0, customFieldValue.length() - 3);
                                        }
                                        companyCustomFieldItem.setFieldStringValue(customFieldValue.toString());
                                    } catch (Exception e) {
                                        log.error("", e);
                                    }
                                }
                            } else if (StringUtils.isNotBlank((String) customFieldsMap.get("date"))) {//for date fields
                                try {
//                                    companyCustomFieldItem.setFieldDateValue(longDateTimezoneFormat.parse((String) customFieldsMap.get("date")));
                                    companyCustomFieldItem.setFieldDateNonConvertedValue(new DateNonConvertable(longDateTimezoneFormat.parse((String) customFieldsMap.get("date"))));
                                } catch (ParseException e) {
                                    log.error("", e);
                                }
                            } else if (customFieldsMap.get("draft_files") != null && customFieldsMap.get("draft_files") instanceof List) {//custom field draft files
                                List<LinkedHashMap<Object, Object>> objects = (List) customFieldsMap.get("draft_files");
                                for (LinkedHashMap<Object, Object> objMap : objects) {
                                    AttachmentTO attachmentTO = new AttachmentTO();
                                    attachmentTO.setFile_name((String) objMap.get("file_name"));
                                    attachmentTO.setLink((String) objMap.get("link"));

                                    ArrayList<AttachmentTO> files = customFieldDraftAttachmentMap.get(edsCompanyCustomFieldsSettings.getObjectID()) == null ? new ArrayList<>() : customFieldDraftAttachmentMap.get(edsCompanyCustomFieldsSettings.getObjectID());
                                    files.add(attachmentTO);
                                    customFieldDraftAttachmentMap.put(edsCompanyCustomFieldsSettings.getObjectID(), files);
                                }
                            }

                            customFieldItems.add(companyCustomFieldItem);
                        }
                    }
                }
            }
        }

        //Upload custom field attachments
        //if request is draft, there will not be draft attachments, but there may be files. If there are files,they should be uploaded
        if (!customFieldAttachmentsMap.isEmpty()) {

            EdsUser user = companyCustomFieldsManager.getUser();
            FolderResource tempFolder = documentsServiceLocal.getTempFolderByCompany(user.getCompany().getObjectID());

            for (Integer customFieldId : customFieldAttachmentsMap.keySet()) {
                EdsCompanyCustomFieldsSettings companyCustomFieldsSettings = companyCustomFieldsManager.get(customFieldId);

                if (companyCustomFieldsSettings != null) {
                    CompanyCustomFieldItem companyCustomFieldItem = new CompanyCustomFieldItem();
                    companyCustomFieldItem.setEntityId(companyCustomFieldsSettings.getObjectID());
                    companyCustomFieldItem.setEntityName(companyCustomFieldsSettings.getEntityName());
                    companyCustomFieldItem.setFieldName(companyCustomFieldsSettings.getFieldName());
                    companyCustomFieldItem.setAliasName(companyCustomFieldsSettings.getAliasName());
                    companyCustomFieldItem.setDataType(companyCustomFieldsSettings.getDataType());
                    companyCustomFieldItem.setUiType(companyCustomFieldsSettings.getUiType());
                    companyCustomFieldItem.setColumnCode(companyCustomFieldsSettings.getColumnCode());
                    companyCustomFieldItem.setFileUploadFieldId(companyCustomFieldsSettings.getObjectID());

                    ArrayList<FileItem> attachments = new ArrayList<>();
                    for (MultipartFile multipartFile : customFieldAttachmentsMap.get(customFieldId)) {
                        FileResource fileResource = documentsServiceLocal.saveDocumentFile(multipartFile, tempFolder.getObjectId(), Constants.F_CUSTOM_FIELD_ITEM, null, "");
                        FileItem fileItem = new FileItem();
                        fileItem.setId(fileResource.getObjectId());
                        fileItem.setFileName(fileResource.getFileName());
                        attachments.add(fileItem);
                    }
                    companyCustomFieldItem.setAttachments(attachments.toArray(new FileItem[]{}));

                    customFieldItems.add(companyCustomFieldItem);
                }
            }
        }

        return customFieldItems;
    }


    protected ArrayList<CompanyCustomFieldItem> convertCustomFields(List<Object> custom_fields, Map<Integer, ArrayList<AttachmentTO>> customFieldDraftAttachmentMap) {

        ArrayList<CompanyCustomFieldItem> customFieldItems = new ArrayList<>();

        if (custom_fields != null && custom_fields.size() > 0) {
            SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);
            for (Object customFieldObject : custom_fields) {
                if (customFieldObject instanceof LinkedHashMap) {
                    LinkedHashMap<Object, Object> customFieldsMap = (LinkedHashMap<Object, Object>) customFieldObject;
                    if (customFieldsMap.get("id") != null && (Integer) customFieldsMap.get("id") < GAP_BTW_STATIC_AND_CUSTOM_FIELDS) {
                        EdsCompanyCustomFieldsSettings edsCompanyCustomFieldsSettings = null;
                        Object object = customFieldsMap.get("id");
                        if (object instanceof Integer) {
                            Integer id = (Integer) object;
                            edsCompanyCustomFieldsSettings = companyCustomFieldsManager.get(id);
                        } else if (object instanceof String) {
                            Integer id = Integer.valueOf((String) object);
                            edsCompanyCustomFieldsSettings = companyCustomFieldsManager.get(id);
                        }
                        if (edsCompanyCustomFieldsSettings != null) {
                            CompanyCustomFieldItem companyCustomFieldItem = new CompanyCustomFieldItem();
                            companyCustomFieldItem.setEntityId(edsCompanyCustomFieldsSettings.getObjectID());
                            companyCustomFieldItem.setFieldName(edsCompanyCustomFieldsSettings.getFieldName());
                            companyCustomFieldItem.setAliasName(edsCompanyCustomFieldsSettings.getAliasName());
                            companyCustomFieldItem.setColumnCode(edsCompanyCustomFieldsSettings.getColumnCode());
                            companyCustomFieldItem.setDataType(edsCompanyCustomFieldsSettings.getDataType());
                            companyCustomFieldItem.setUiType(edsCompanyCustomFieldsSettings.getUiType());

                            if (StringUtils.isNotBlank((String) customFieldsMap.get("text"))) {//for text fields

                                companyCustomFieldItem.setFieldStringValue((String) customFieldsMap.get("text"));

                            } else if (customFieldsMap.get("value") != null) {//for number fields
                                if (Constants.DATA_TYPE_NUMBER.equalsIgnoreCase(edsCompanyCustomFieldsSettings.getDataType())) {
                                    companyCustomFieldItem.setFieldStringValue(customFieldsMap.get("value").toString());
                                }

                            } else if (customFieldsMap.get("category_id") != null) {//for number fields

                                if (edsCompanyCustomFieldsSettings.getPredefinedValues() != null && edsCompanyCustomFieldsSettings.getPredefinedValues().length > 0) {
                                    String[] values = edsCompanyCustomFieldsSettings.getPredefinedValues();
                                    try {
                                        Integer index;
                                        if (customFieldsMap.get("category_id") instanceof Integer) {
                                            index = (Integer) customFieldsMap.get("category_id");
                                        } else {
                                            index = Integer.valueOf(customFieldsMap.get("category_id").toString());
                                        }

                                        if (index > 0 && index <= values.length) {
                                            companyCustomFieldItem.setFieldStringValue(values[index - 1]);
                                        }

                                    } catch (Exception e) {
                                        log.error("", e);
                                    }
                                }
                            } else if (customFieldsMap.get("choosed_ids") != null && customFieldsMap.get("choosed_ids") instanceof List) {//for number fields
                                if (edsCompanyCustomFieldsSettings.getPredefinedValues() != null && edsCompanyCustomFieldsSettings.getPredefinedValues().length > 0) {
                                    String[] values = edsCompanyCustomFieldsSettings.getPredefinedValues();
                                    try {
                                        ArrayList<Integer> indexes = new ArrayList<>();
                                        for (Object val : (List) customFieldsMap.get("choosed_ids")) {
                                            if (val instanceof Integer) {
                                                indexes.add((Integer) val);
                                            } else {
                                                indexes.add(Integer.valueOf(val.toString()));
                                            }
                                        }
                                        StringBuilder customFieldValue = new StringBuilder();
                                        for (Integer index : indexes) {
                                            if (index > 0 && index <= values.length) {
                                                customFieldValue.append(values[index - 1]).append("-:-");
                                            }
                                        }
                                        if (customFieldValue.length() > 3 && customFieldValue.substring(customFieldValue.length() - 3).equals("-:-")) {
                                            customFieldValue = new StringBuilder(customFieldValue.substring(0, customFieldValue.length() - 3));
                                        }
                                        companyCustomFieldItem.setFieldStringValue(customFieldValue.toString());
                                    } catch (Exception e) {
                                        log.error("", e);
                                    }
                                }
                            } else if (StringUtils.isNotBlank((String) customFieldsMap.get("date"))) {//for date fields
                                try {
//                                    companyCustomFieldItem.setFieldDateValue(longDateTimezoneFormat.parse((String) customFieldsMap.get("date")));
                                    companyCustomFieldItem.setFieldDateNonConvertedValue(new DateNonConvertable(longDateTimezoneFormat.parse((String) customFieldsMap.get("date"))));
                                } catch (ParseException e) {
                                    log.error("", e);
                                }
                            } else if (customFieldDraftAttachmentMap != null && customFieldsMap.get("draft_files") != null && customFieldsMap.get("draft_files") instanceof List) {//custom field draft files
                                List<LinkedHashMap<Object, Object>> objects = (List) customFieldsMap.get("draft_files");
                                for (LinkedHashMap<Object, Object> objMap : objects) {
                                    AttachmentTO attachmentTO = new AttachmentTO();
                                    attachmentTO.setFile_name((String) objMap.get("file_name"));
                                    attachmentTO.setLink((String) objMap.get("link"));

                                    ArrayList<AttachmentTO> files = customFieldDraftAttachmentMap.get(edsCompanyCustomFieldsSettings.getObjectID()) == null ? new ArrayList<>() : customFieldDraftAttachmentMap.get(edsCompanyCustomFieldsSettings.getObjectID());
                                    files.add(attachmentTO);
                                    customFieldDraftAttachmentMap.put(edsCompanyCustomFieldsSettings.getObjectID(), files);
                                }
                            } else if (customFieldsMap.get("reference_id") != null) { //for reference fields
                                EdsReference reference = referenceManager.getReference((Integer) customFieldsMap.get("reference_id"));
                                companyCustomFieldItem.setFieldStringValue(reference != null ? reference.getName() : null);
                            }
                            companyCustomFieldItem.setFileUploadFieldId(edsCompanyCustomFieldsSettings.getObjectID());
                            customFieldItems.add(companyCustomFieldItem);
                        }
                    }
                }
            }
        }

        return customFieldItems;
    }


    protected TreeMap<Integer, ArrayList<MultipartFile>> getCustomFieldAttachmentsMap(MultipartRequest multipartRequest, String regexPattern) {
        Pattern pattern = Pattern.compile(regexPattern);
        TreeMap<Integer, ArrayList<MultipartFile>> customFieldAttachmentsMap = new TreeMap<>();
        if (multipartRequest != null && multipartRequest.getFileMap() != null && multipartRequest.getFileMap().size() > 0) {
            for (MultipartFile file : multipartRequest.getFileMap().values()) {
                if (file.getName().matches(regexPattern)) {
                    Matcher m = pattern.matcher(file.getName());
                    Integer customFieldFileId;
                    if (m.matches()) {
                        customFieldFileId = Integer.valueOf(m.group(1));
                        ArrayList<MultipartFile> files = customFieldAttachmentsMap.get(customFieldFileId) == null ? new ArrayList<>() : customFieldAttachmentsMap.get(customFieldFileId);
                        files.add(file);
                        customFieldAttachmentsMap.put(customFieldFileId, files);
                    }
                }
            }
        }
        return customFieldAttachmentsMap;
    }

    /**
     * @param customFieldsObjectMap
     * @param modelFieldsMap
     * @param customFieldObjects
     * @param modelFieldValueMap
     */
    protected void separateCustomFields(LinkedHashMap<Object, ArrayList<Object>> customFieldsObjectMap,
            LinkedHashMap<Integer, String> modelFieldsMap,
            ArrayList<Object> customFieldObjects,
            LinkedHashMap<String, Object> modelFieldValueMap) {
        if (customFieldsObjectMap != null && !customFieldsObjectMap.isEmpty()) {
            for (Object customFieldObject : customFieldsObjectMap.get("list")) {
                LinkedHashMap<Object, Object> customFieldsMap = (LinkedHashMap<Object, Object>) customFieldObject;
                if (customFieldsMap.get("id") != null) {
                    if ((Integer) customFieldsMap.get("id") < GAP_BTW_STATIC_AND_CUSTOM_FIELDS) {//it means real custom field
                        customFieldObjects.add(customFieldsMap);
                    } else {
                        String fieldID = modelFieldsMap.get((Integer) customFieldsMap.get("id") - GAP_BTW_STATIC_AND_CUSTOM_FIELDS);//it means model field
                        if (StringUtils.isNotBlank(fieldID)) {
                            if (StringUtils.isNotBlank((String) customFieldsMap.get("text"))) {//for text fields
                                modelFieldValueMap.put(fieldID, customFieldsMap.get("text"));
                            } else if (customFieldsMap.get("value") != null) {//for number fields
                                modelFieldValueMap.put(fieldID, customFieldsMap.get("value"));
                            } else if (customFieldsMap.get("category_id") != null) {//for drop down fields
                                modelFieldValueMap.put(fieldID, customFieldsMap.get("category_id"));
                            } else if (customFieldsMap.get("choosed_ids") != null) {//for multi drop down fields
                                modelFieldValueMap.put(fieldID, customFieldsMap.get("choosed_ids"));
                            } else if (customFieldsMap.get("date") != null) {//for date fields
                                modelFieldValueMap.put(fieldID, customFieldsMap.get("date"));
                            } else if (customFieldsMap.get("draft_files") != null) {//for attachments
                                modelFieldValueMap.put(fieldID, customFieldsMap.get("draft_files"));
                            }
                        }
                    }
                }
            }
        }
    }


    protected void uploadFiles(Integer entityId, Integer folderType, Integer folderId, ArrayList<MultipartFile> multipartFiles, ArrayList<FileResource> oldAttachments) {
        try {
            if (multipartFiles != null && multipartFiles.size() > 0) {
                //if old files are empty, upload new files
                if (oldAttachments == null || oldAttachments.size() == 0) {
                    for (MultipartFile file : multipartFiles) {
                        try {
                            documentsServiceLocal.saveDocumentFile(file, folderId, folderType, entityId, null);
                        } catch (Exception e) {
                            log.error("", e);
                        }
                    }
                } else {//If old files aren't empty, merge old and new files
                    HashSet<Integer> deleteIDs = new HashSet<>();
                    LinkedHashMap<String, FileResource> oldFilesMap = new LinkedHashMap<>();
                    for (FileResource file : oldAttachments) {
                        oldFilesMap.put(file.getFileName(), file);
                    }

                    for (MultipartFile multipartFile : multipartFiles) {
                        FileResource oldFile = oldFilesMap.get(multipartFile.getOriginalFilename());
                        if (oldFile != null) {
                            deleteIDs.add(oldFile.getObjectId());
                        }
                    }

                    if (deleteIDs.size() > 0) {
                        try {
                            documentsServiceLocal.deleteFiles(new ArrayList<>(deleteIDs));
                        } catch (ObjectNotFoundException | InsufficientPermissionsException e) {
                            log.error("", e);
                        }
                    }
                    for (MultipartFile file : multipartFiles) {
                        try {
                            documentsServiceLocal.saveDocumentFile(file, null, folderType, entityId, "");
                        } catch (Exception e) {
                            log.error("", e);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }


    private static LinkedHashMap<String, String> getEntityRelationMap() {

        LinkedHashMap<String, String> relationMap = new LinkedHashMap<>();
        relationMap.put(EntityTypeEnum.PROJECTS.name(), RelationItem.TYPE_PROJECT);
        relationMap.put(EntityTypeEnum.PROJECT.name(), RelationItem.TYPE_PROJECT);
        relationMap.put(EntityTypeEnum.CONTACTS.name(), RelationItem.TYPE_CONTACT);
        relationMap.put(EntityTypeEnum.CONTACT.name(), RelationItem.TYPE_CONTACT);
        relationMap.put(EntityTypeEnum.LEADS.name(), RelationItem.TYPE_LEAD);
        relationMap.put(EntityTypeEnum.LEAD.name(), RelationItem.TYPE_LEAD);
        relationMap.put(EntityTypeEnum.OPPORTUNITIES.name(), RelationItem.TYPE_OPPORTUNITY);
        relationMap.put(EntityTypeEnum.OPPORTUNITY.name(), RelationItem.TYPE_OPPORTUNITY);
        relationMap.put(EntityTypeEnum.TASKS.name(), RelationItem.TYPE_TASK);
        relationMap.put(EntityTypeEnum.TASK.name(), RelationItem.TYPE_TASK);
        relationMap.put(EntityTypeEnum.COMPANIES.name(), RelationItem.TYPE_CRM_ACCOUNT);
        relationMap.put(EntityTypeEnum.COMPANY.name(), RelationItem.TYPE_CRM_ACCOUNT);
        relationMap.put(EntityTypeEnum.CRM_ACCOUNTS.name(), RelationItem.TYPE_CRM_ACCOUNT);
        relationMap.put(EntityTypeEnum.CRM_ACCOUNT.name(), RelationItem.TYPE_CRM_ACCOUNT);
        relationMap.put(EntityTypeEnum.ACTIVITIES.name(), RelationItem.TYPE_EVENT);
        relationMap.put(EntityTypeEnum.ACTIVITY.name(), RelationItem.TYPE_EVENT);
        relationMap.put(EntityTypeEnum.EVENTS.name(), RelationItem.TYPE_EVENT);
        relationMap.put(EntityTypeEnum.EVENT.name(), RelationItem.TYPE_EVENT);
        relationMap.put(EntityTypeEnum.CALL.name(), RelationItem.TYPE_EVENT);
        relationMap.put(EntityTypeEnum.CALLS.name(), RelationItem.TYPE_EVENT);
        relationMap.put(EntityTypeEnum.ISSUES.name(), RelationItem.TYPE_ISSUE);
        relationMap.put(EntityTypeEnum.ISSUE.name(), RelationItem.TYPE_ISSUE);
        relationMap.put(EntityTypeEnum.CAMPAIGNS.name(), RelationItem.TYPE_CAMPAIGN);
        relationMap.put(EntityTypeEnum.CAMPAIGN.name(), RelationItem.TYPE_CAMPAIGN);
        relationMap.put(EntityTypeEnum.CASES.name(), RelationItem.TYPE_CASE);
        relationMap.put(EntityTypeEnum.CASE.name(), RelationItem.TYPE_CASE);
        relationMap.put(EntityTypeEnum.EMPLOYEES.name(), RelationItem.TYPE_EMPLOYEE);
        relationMap.put(EntityTypeEnum.EMPLOYEE.name(), RelationItem.TYPE_EMPLOYEE);
        relationMap.put(EntityTypeEnum.DEPARTMENTS.name(), RelationItem.TYPE_DEPARTMENT);
        relationMap.put(EntityTypeEnum.DEPARTMENT.name(), RelationItem.TYPE_DEPARTMENT);
        relationMap.put(EntityTypeEnum.SALES.name(), RelationItem.TYPE_SALEQUOTE);
        relationMap.put(EntityTypeEnum.QUOTE.name(), RelationItem.TYPE_SALEQUOTE);
        relationMap.put(EntityTypeEnum.PRODUCTS.name(), RelationItem.TYPE_PRODUCT);
        relationMap.put(EntityTypeEnum.PRODUCT.name(), RelationItem.TYPE_PRODUCT);
        relationMap.put(EntityTypeEnum.PURCHASES.name(), RelationItem.TYPE_PURCHASE_ORDER);
        relationMap.put(EntityTypeEnum.PURCHASE_ORDER.name(), RelationItem.TYPE_PURCHASE_ORDER);
        relationMap.put(EntityTypeEnum.SUPPLIERS.name(), RelationItem.TYPE_SUPPLIER);
        relationMap.put(EntityTypeEnum.SUPPLIER.name(), RelationItem.TYPE_SUPPLIER);
        relationMap.put(EntityTypeEnum.SUPPLIERS.name(), RelationItem.TYPE_SUPPLIER);
        relationMap.put(EntityTypeEnum.CUSTOMER.name(), RelationItem.TYPE_CLIENT);
        relationMap.put(EntityTypeEnum.CUSTOMERS.name(), RelationItem.TYPE_CLIENT);
        relationMap.put(EntityTypeEnum.CLIENTS_CUSTOMERS.name(), RelationItem.TYPE_CLIENT);

        return relationMap;
    }

    protected static String getEntityRelation(String entityType) {
        return getEntityRelationMap().get(entityType.toUpperCase());
    }


    private static LinkedHashMap<String, String> getLinkTypeMap() {

        LinkedHashMap<String, String> relationMap = new LinkedHashMap<>();
        relationMap.put(RelationItem.TYPE_PROJECT, LinkTypeEnum.PROJECT.name());
        relationMap.put(RelationItem.TYPE_CONTACT, LinkTypeEnum.CONTACT.name());
        relationMap.put(RelationItem.TYPE_LEAD, LinkTypeEnum.LEAD.name());
        relationMap.put(RelationItem.TYPE_OPPORTUNITY, LinkTypeEnum.OPPORTUNITY.name());
        relationMap.put(RelationItem.TYPE_TASK, LinkTypeEnum.TASK.name());
        relationMap.put(RelationItem.TYPE_CRM_ACCOUNT, LinkTypeEnum.CRM_ACCOUNT.name());
        relationMap.put(RelationItem.TYPE_EVENT, LinkTypeEnum.EVENTS.name());
        relationMap.put(RelationItem.TYPE_ISSUE, LinkTypeEnum.ISSUE.name());
        relationMap.put(RelationItem.TYPE_CAMPAIGN, LinkTypeEnum.CAMPAIGNS.name());
        relationMap.put(RelationItem.TYPE_CASE, LinkTypeEnum.CASE.name());
        relationMap.put(RelationItem.TYPE_EMPLOYEE, LinkTypeEnum.EMPLOYEE.name());
        relationMap.put(RelationItem.TYPE_DEPARTMENT, LinkTypeEnum.DEPARTMENT.name());
        relationMap.put(RelationItem.TYPE_SALEQUOTE, LinkTypeEnum.QUOTE.name());
        relationMap.put(RelationItem.TYPE_PRODUCT, LinkTypeEnum.PRODUCT.name());
        relationMap.put(RelationItem.TYPE_PURCHASE_ORDER, LinkTypeEnum.PURCHASE_ORDER.name());
        relationMap.put(RelationItem.TYPE_SUPPLIER, LinkTypeEnum.SUPPLIER.name());
        relationMap.put(RelationItem.TYPE_CLIENT, LinkTypeEnum.CLIENTS_CUSTOMERS.name());

        return relationMap;
    }

    protected static String getLinkType(String relationType) {
        if (StringUtils.isBlank(relationType)) {
            return null;
        }
        return getLinkTypeMap().get(relationType);
    }

    protected List<String> getCustomFieldValue(Integer custom_field_id) throws RestException {
        CompanyCustomFieldItem customFieldItem;
        try {
            customFieldItem = profileServiceLocal.getCustomFieldData(custom_field_id, null);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (customFieldItem == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Categories with custom field id " + custom_field_id + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);

        }
//        if (Constants.UI_TYPE_RADIOBUTTON.equalsIgnoreCase(customFieldItem.getUiType())
//                || Constants.UI_TYPE_CHECKBOX.equalsIgnoreCase(customFieldItem.getUiType())) {
//            String[] predefinedValues = customFieldItem.getPredefinedValues() != null ? customFieldItem.getPredefinedValues().split(splitter) : new String[0];
//            return Arrays.asList(predefinedValues);
//        }
//        if (Constants.UI_TYPE_DROPDOWN.equalsIgnoreCase(customFieldItem.getUiType())) {
//        }
        String[] predefinedValues = customFieldItem.getPredefinedValues() != null ? customFieldItem.getPredefinedValues() : new String[0];
        return Arrays.asList(predefinedValues);
//        return Collections.emptyList();
    }

    /*
     * Get options of custom lookup field
     * */
    protected List<SelectItem> getCustomFieldLookupValues(ListingFilterParameter filter, CompanyCustomFieldItem customFieldItem) throws RestException {
        if (Constants.UI_TYPE_LOOKUP.equalsIgnoreCase(customFieldItem.getUiType())) {
            return Arrays.asList(allInOneServiceLocal.getCustomFieldLookUpData(filter, customFieldItem.getLookUpTypeEnum()));
        } else {
            return Collections.emptyList();
        }
    }

    protected static HashMap<String, ViewName> CF_TYPES = new HashMap<>();
    protected static HashMap<String, String> FORM_TYPES = new HashMap<>();

    static {
        CF_TYPES.put("accounts", ViewName.CrmAccount);
        CF_TYPES.put("customers", ViewName.CrmAccount);
        CF_TYPES.put("suppliers", ViewName.CrmAccount);
        CF_TYPES.put("leads", ViewName.Lead);
        CF_TYPES.put("contacts", ViewName.Contact);
        CF_TYPES.put("opportunities", ViewName.Opportunity);
        CF_TYPES.put("events", ViewName.Activity);
        CF_TYPES.put("calls", ViewName.LogACall);
        CF_TYPES.put("tasks", ViewName.Task);
        CF_TYPES.put("leave_request", ViewName.LeaveRequest);

        FORM_TYPES.put("companies", LayoutRPC.ACCOUNT_FORM);
        FORM_TYPES.put("accounts", LayoutRPC.ACCOUNT_FORM);
        FORM_TYPES.put("customers", LayoutRPC.CLIENT_FORM);
        FORM_TYPES.put("suppliers", LayoutRPC.SUPPLIER_FORM);
        FORM_TYPES.put("leads", LayoutRPC.LEAD_FORM);
        FORM_TYPES.put("contacts", LayoutRPC.CONTACT_FORM);
        FORM_TYPES.put("opportunities", LayoutRPC.OPPORTUNITY_FORM);
        FORM_TYPES.put("events", LayoutRPC.ACTIVITY_FORM);
        FORM_TYPES.put("calls", LayoutRPC.ACTIVITY_FORM);
        FORM_TYPES.put("tasks", LayoutRPC.TASK_MAX_FORM);
        FORM_TYPES.put("leave_request", LayoutRPC.LEAVE_REQUEST_FORM);
    }


    protected Integer getFolderType(String entityType) {
        return FolderRelationTypeEnum.getRelationType(entityType);
    }

    protected Integer getFolder(String entityType, Integer entityId) {
        return getFolderId(getFolderType(entityType), entityId);
    }

    protected Integer getFolderId(Integer folderType, Integer relationId) {
        switch (folderType) {
            case Constants.F_TASK, Constants.F_PR_ISSUE -> {
                SelectItem projectItem = taskServiceLocal.getProjectByTask(relationId);
                return projectItem.getId();
            }
            case Constants.F_PROJECT, Constants.F_CRM_CONTACT, Constants.F_LEAD, Constants.F_CRM_ACCOUNT, Constants.F_LEAVE_REQUEST, Constants.F_NOTE, Constants.F_EMPLOYEE_PROFILE, Constants.F_EXP, Constants.F_EXP_DOC, Constants.F_OPPORTUNITY, Constants.F_EVENT -> {
                return relationId;
            }
            default -> {
                return Constants.F_DEFAULT;
            }
        }
    }

    protected static FilteredStatusItemTO getDefaultStatus() {
        FilteredStatusItemTO status = new FilteredStatusItemTO();
        status.setStatus_id(0);
        status.setStatus_name("N/A");
        status.setIs_system(false);
        status.setOrder_id(0);
        status.setStatus_color(getDefaultColor());

        return status;
    }

    protected static ColorTO getDefaultColor() {
        return new ColorTO(9, "#afb6be", "cool_grey_two");
    }


}
