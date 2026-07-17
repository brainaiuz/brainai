package com.edatasite.workforce.rest.v1.release10.core;

import com.edatasite.workforce.core.domain.EdsCompanyCustomFieldsSettings;
import com.edatasite.workforce.gwt.core.client.rpc.ApprovalListResult;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.app.AllInOneServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.CompanyCustomFieldsManager;
import com.edatasite.workforce.rest.base.enums.ApiActionEnum;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.base.to.SelectItemTO;
import com.edatasite.workforce.rest.base.to.UserTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.edatasite.workforce.rest.base.helpers.ApiConstants.FORMAT_WITH_DATETIME_AND_TIMEZONE;

/**
 * web
 * Created by Sher on 2/2/2015.
 */
public class BaseApiControllerV1 {

    private static final Logger log = LoggerFactory.getLogger(BaseApiControllerV1.class);
    public String ERROR_RESOURCE_NOT_FOUND = "The specified resource does not exist.";
    @Autowired
    protected CompanyCustomFieldsManager companyCustomFieldsManager;
    protected String ERROR_INVALID_BODY_PARAM = "A body parameter specified in the request content is outside the permissible range.";
    protected String ERROR_INVALID_QUERY_PARAM = "A query parameter specified in the request URI is outside the permissible range.";
    protected String ERROR_INVALID_QUERY_PARAM_LIST = "A required query parameter was not specified for this request.";

    protected Pattern EMAIL_PATTERN = Pattern.compile("^\\w+([_.-]\\w+)*@(\\w+([_.-]\\w+)*)");


    //Timesheet Plugin API response do not delete
    protected String ACCESS_DENIED = "Access denied";
    protected String ACCESS_PERMITTED = "Access Permitted";
    //Timesheet Plugin API response do not delete

    protected String ERROR_FAILED_SAVE = "Saving failed.";
    protected String ERROR_FAILED_UPDATE = "Updating failed.";
    protected String ERROR_FAIL_DELETE = "Deleting failed.";

    protected String SUCCESS_DELETE = "Deleted successfully.";
    protected String SUCCESS_UPDATE = "Updated successfully.";
    protected String SUCCESS_SAVE = "Saved successfully.";

    protected String ERROR_ACCESS_DENIED = "Access denied.";
    protected String ERROR_RUNTIME = "Runtime exception.";

    protected String REQUEST_USER_AUTHORIZED = "User already authorized!";
    protected String REQUEST_USER_NOT_AUTHORIZED = "User not authorized!";
    protected String REQUEST_USER_SESSION_EXPIRED = "Session expired!";
    protected Integer RESPONSE_SUCCESS_CODE = 1;
    protected Integer RESPONSE_SESSION_EXPIRED_CODE = -2;
    @Autowired
    protected AllInOneServiceLocal allInOneServiceLocal;
    private String FREE_TRIAL_EXPIRED = "14 days free trial expired!";
    private String MESSAGE_TYPE_INFO = "info";
    private String MESSAGE_TYPE_DANGER = "danger";
    private String MESSAGE_TYPE_SUCCESS = "success";
    private String MESSAGE_TYPE_WARNING = "warning";
    private Integer RESPONSE_ERROR_CODE = 0;
    private Integer RESPONSE_FREE_TRIAL_EXPIRED_CODE = -1;
    private String RESPONSE_FIELD_DATA = "result";
    private String RESPONSE_FIELD_STATUS = "status";
    private String RESPONSE_FIELD_MESSAGE = "message";
    private String RESPONSE_FIELD_MESSAGE_TYPE = "messageType";

    protected static ApiActionEnum getActionType(String type) {
        if (type == null) {
            return null;
        }
        return ApiActionEnum.valueOf(type.toUpperCase());
    }

    protected HashMap<String, Object> successResponse() {
        return response(null, null, RESPONSE_SUCCESS_CODE, MESSAGE_TYPE_SUCCESS);
    }

    protected HashMap<String, Object> successResponse(String message, Object data, Integer successCode) {
        return response(message, data, successCode, MESSAGE_TYPE_SUCCESS);
    }

    protected HashMap<String, Object> successResponse(String message) {
        return response(message, null, RESPONSE_SUCCESS_CODE, MESSAGE_TYPE_SUCCESS);
    }

    protected HashMap<String, Object> successResponse(Object data) {
        return response(null, data, RESPONSE_SUCCESS_CODE, MESSAGE_TYPE_SUCCESS);
    }

    protected HashMap<String, Object> successResponse(String message, Object data) {
        return response(message, data, RESPONSE_SUCCESS_CODE, MESSAGE_TYPE_SUCCESS);
    }

    protected HashMap<String, Object> infoResponse(String message) {
        return response(message, null, RESPONSE_SUCCESS_CODE, MESSAGE_TYPE_INFO);
    }

    protected HashMap<String, Object> warningResponse(String message) {
        return response(message, null, RESPONSE_ERROR_CODE, MESSAGE_TYPE_WARNING);
    }

    protected HashMap<String, Object> errorResponse(Object data) {
        return response(null, data, RESPONSE_ERROR_CODE, MESSAGE_TYPE_DANGER);
    }

    protected HashMap<String, Object> errorResponse(String message) {
        return response(message, null, RESPONSE_ERROR_CODE, MESSAGE_TYPE_DANGER);
    }

    protected HashMap<String, Object> errorResponse() {
        return response(null, null, RESPONSE_ERROR_CODE, MESSAGE_TYPE_DANGER);
    }

    protected HashMap<String, Object> errorResponse(String message, Integer errorCode) {
        return response(message, null, errorCode, MESSAGE_TYPE_DANGER);
    }

    protected HashMap<String, Object> errorResponse(String message, Object data, Integer errorCode) {
        return response(message, data, errorCode, MESSAGE_TYPE_DANGER);
    }

    /*protected WebApplicationException exception(Response.Status status, String message) {
        Response.ResponseBuilder builder = Response.status(status);

        builder.type("application/json");
        builder.entity(this.errorResponse(message));
        return new WebApplicationException(builder.build());
    }*/

    protected HashMap<String, Object> freeTrailExpiredException() {
        return response(FREE_TRIAL_EXPIRED, null, RESPONSE_FREE_TRIAL_EXPIRED_CODE, MESSAGE_TYPE_WARNING);
    }

    protected HashMap<String, Object> response(String message, Object data, Integer status, String messageType) {
        HashMap<String, Object> map = new HashMap<>();
        if (data instanceof Collection) {
            data = ((Collection) data).isEmpty() ? null : data;
        }
        if (data instanceof Map) {
            data = ((Map) data).isEmpty() ? null : data;
        }
        if (data instanceof ListResultTO) {
            data = (((ListResultTO) data).getItems() == null || ((ListResultTO) data).getItems().size() == 0) ? null : data;
        }
        if (data instanceof SelectItemTO[]) {
            data = ((SelectItemTO[]) data).length == 0 ? null : data;
        }

        map.put(RESPONSE_FIELD_DATA, data);
        map.put(RESPONSE_FIELD_STATUS, status);
        map.put(RESPONSE_FIELD_MESSAGE, message);
        map.put(RESPONSE_FIELD_MESSAGE_TYPE, messageType);

        return map;
    }

    ListPanelType validatePanelType(String type) {
        return ListPanelType.valueOf(type);
    }

    public ArrayList<ApproverItemMini> getChosenApprovers(ArrayList<UserTO> approvers, ArrayList<SelectItemTO> approverItems) {
        ArrayList<ApproverItemMini> result = new ArrayList<>();
        int order = 0;
        for (UserTO approver : approvers) {
            ApproverItemMini item = new ApproverItem();
            item.setApproverOrder(order);
            item.setClonedFrom(approverItems.get(order).getId());
            SelectItem exactEmployee = new SelectItem();
            exactEmployee.setId(approver.getId());
            exactEmployee.setName(approver.getName());
            item.setExactEmployee(exactEmployee);
            result.add(item);
            order++;
        }
        return result;
    }

    protected ArrayList<SelectItemTO> getChooseApprovers(String type) {
        ApprovalListResult approvalListResult = allInOneServiceLocal.getApprovers(type, null, true, null, false);
        ArrayList<SelectItemTO> itemTOs = new ArrayList<>();
        if (approvalListResult.getList() != null) {
            int order = 0;
            for (ApproverItem item : approvalListResult.getList()) {
                SelectItemTO itemTO = new SelectItemTO();
                itemTO.setId(item.getObjectID());
                itemTO.setName("Approver" + order);
                itemTO.setDescription(String.valueOf(order));
                itemTOs.add(itemTO);
                order++;
            }
        }
        return itemTOs;
    }

    protected ArrayList<CompanyCustomFieldItem> convertCustomFields(List<Object> custom_fields, Map<Integer, ArrayList<AttachmentTO>> customFieldDraftAttachmentMap) {

        ArrayList<CompanyCustomFieldItem> customFieldItems = new ArrayList<>();

        if (custom_fields != null && custom_fields.size() > 0) {
            SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);
            for (Object customFieldObject : custom_fields) {
                if (customFieldObject instanceof LinkedHashMap) {
                    LinkedHashMap<Object, Object> customFieldsMap = (LinkedHashMap<Object, Object>) customFieldObject;
                    if (customFieldsMap.get("id") != null && (Integer) customFieldsMap.get("id") < 100000) {
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
                                        Integer index = 0;
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
                                                customFieldValue.append(values[index - 1]).append(",");
                                            }
                                        }
                                        if (customFieldValue.length() > 1 && customFieldValue.charAt(customFieldValue.length() - 1) == ',') {
                                            customFieldValue = customFieldValue.deleteCharAt(customFieldValue.length() - 1);
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
}
