package com.edatasite.workforce.gwt.core.server.utils;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsPaymentMethod;
import com.edatasite.workforce.core.domain.EdsPosition;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsUserEmailSettings;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsInvoiceTerms;
import com.edatasite.workforce.core.domain.accounting.EdsProductCategory;
import com.edatasite.workforce.core.domain.accounting.EdsQuote;
import com.edatasite.workforce.core.domain.accounting.EdsUnitMeasurement;
import com.edatasite.workforce.core.domain.crm.EdsCase;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customfields.EdsCustomFields;
import com.edatasite.workforce.core.solr.document.BaseSolrDoc;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFormLocalization;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrTaskRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.StaticContextAccessor;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.commons.ExcelData;
import com.edatasite.workforce.gwt.core.server.db.CaseManager;
import com.edatasite.workforce.gwt.core.server.db.CountryManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.CurrencyManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceTermsManager;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.LocationManager;
import com.edatasite.workforce.gwt.core.server.db.OpportunityManager;
import com.edatasite.workforce.gwt.core.server.db.PaymentMethodManager;
import com.edatasite.workforce.gwt.core.server.db.PositionManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.QuoteManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.UserEmailSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ProductCategoryManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.UnitMeasurementManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.server.app.DocumentsServiceLocal;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.edatasite.workforce.rest.v3.release10.core.request.CustomFieldRequest;
import com.edatasite.workforce.rest.v3.release10.core.to.CustomFieldDto;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.edatasite.workforce.rest.v3.release10.core.utils.DateDeserializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lowagie.text.Element;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 11-Nov-2010
 * Time: 17:47:10
 */
public class CustomFieldsUtils {

    private static final String SORTABLE_ = "SORTABLE_";

    public static final String STRING_VALUE = "string_value";
    public static final String DATE_VALUE = "date_value";
    public static final String DOUBLE_VALUE = "double_value";
    private static final String SEPARATOR = "-:-";
    public static final int F_CUSTOM_FIELD_ITEMS = 57;
    public static final DecimalFormat codeFormat = new DecimalFormat("0000");

    public static EdsCustomFields setDateCustomFields(EdsCustomFields edsCustomFieldsDate, CompanyCustomFieldItem customFieldItem) {
        if (edsCustomFieldsDate == null || customFieldItem == null) {
            return edsCustomFieldsDate;
        }
        Date dateValue = customFieldItem.getFieldDateNonConvertedValue() != null ? customFieldItem.getFieldDateNonConvertedValue().getNonConvertedDate() : null;
      /*  if (isNonConvertedDate) {
            dateValue = customFieldItem.getFieldDateNonConvertedValue() != null ? customFieldItem.getFieldDateNonConvertedValue().getNonConvertedDate() : null;
        } else if (customFieldItem.getFieldDateValue() != null) {
            dateValue = customFieldItem.getFieldDateValue();
        }*/
        try {
            setValueCustomField(edsCustomFieldsDate, customFieldItem.getColumnCode(), dateValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return edsCustomFieldsDate;
    }

    public static EdsCustomFields setDoubleCustomFields(EdsCustomFields edsCustomFieldsDouble, CompanyCustomFieldItem customFieldItem) {
        if (edsCustomFieldsDouble == null || customFieldItem == null) {
            return edsCustomFieldsDouble;
        }
        Double doubleValue = null;
        Locale userLocale = ServerUtils.getUserLocale();
        NumberFormat localeFormat = NumberFormat.getInstance(userLocale);
        if (customFieldItem.getFieldStringValue() != null && !"".equals(customFieldItem.getFieldStringValue()) && !"null".equals(customFieldItem.getFieldStringValue())) {
            try {
                doubleValue = localeFormat.parse(customFieldItem.getFieldStringValue()).doubleValue();
            } catch (NumberFormatException | ParseException e) {
                e.printStackTrace();
            }
        }
        setValueCustomField(edsCustomFieldsDouble, customFieldItem.getColumnCode(), doubleValue);

        return edsCustomFieldsDouble;
    }

    private static void setValueCustomField(EdsCustomFields edsCustomFieldsDouble, String columnCode, Object doubleValue) {
        try {
            Class<?> type = EdsCustomFields.class.getDeclaredMethod(getterName(columnCode)).getReturnType();
            EdsCustomFields.class.getDeclaredMethod(setterName(columnCode), type).invoke(edsCustomFieldsDouble, doubleValue);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static EdsCustomFields setStringCustomFields(EdsCustomFields edsCustomFields, CompanyCustomFieldItem customFieldItem) {
        if (edsCustomFields == null || customFieldItem == null) {
            return edsCustomFields;
        }
        if (Constants.UI_TYPE_ITEM_WITH_DESCRIPTION.equals(customFieldItem.getUiType()) && customFieldItem.getItem() != null) {
            Gson gson = new Gson();
            String json = gson.toJson(customFieldItem.getItem());
            customFieldItem.setFieldStringValue(json);
        }
        if ((Constants.TYPE_ENTITY_MULTI_LOOKUP.equals(customFieldItem.getUiType())
                || Constants.UI_TYPE_MULTI_LOOKUP.equals(customFieldItem.getUiType()))
                && customFieldItem.getSelectItems() != null
                && customFieldItem.getSelectItems().size() > 0) {
            Gson gson = new Gson();
            String json = gson.toJson(customFieldItem.getSelectItems());
            customFieldItem.setFieldStringValue(json);
        }
        if (Constants.UI_TYPE_AUTONUMBER.equals(customFieldItem.getUiType()) && edsCustomFields.getStringValue(customFieldItem.getColumnCode()) == null) {
            CommonServiceLocal commonServiceLocal = (CommonServiceLocal) ApplicationContextProvider.applicationContext.getBean("commonService");

            String maxValue = commonServiceLocal.getMaxValueOfAutoNumbering(customFieldItem);
            String currentValue = customFieldItem.getFieldStringValue();
            if (currentValue != null && !currentValue.equals(maxValue)) {
                customFieldItem.setFieldStringValue(maxValue);
            }
        }


        if (Constants.UI_TYPE_HTML_TEXTAREA.equals(customFieldItem.getUiType())) {
            String str = customFieldItem.getFieldStringValue();
            try {
                customFieldItem.setFieldStringValue(ServerUtils.decrypt(str));
            } catch (Exception e) {
                customFieldItem.setFieldStringValue(str);
            }
        }
        setValueCustomField(edsCustomFields, customFieldItem.getColumnCode(), customFieldItem.getFieldStringValue());
        if (Constants.TYPE_ENTITY_LOOKUP.equals(customFieldItem.getUiType()) && customFieldItem.getEntityType() != null && customFieldItem.getSelectedId() != null) {
            String entityId = customFieldItem.getEntityType().getId() + "";
            String selectedId = customFieldItem.getSelectedId() + "";
            String keyvalue = entityId + "=" + selectedId;
            if (edsCustomFields.getCustomEntMap().containsKey(customFieldItem.getColumnCode())) {
                edsCustomFields.getCustomEntMap().replace(customFieldItem.getColumnCode(), keyvalue);
            } else {
                edsCustomFields.getCustomEntMap().put(customFieldItem.getColumnCode(), keyvalue);
            }
        } else if ((Constants.UI_TYPE_LOOKUP.equals(customFieldItem.getUiType()) || Constants.UI_TYPE_CURRENCY.equals(customFieldItem.getUiType())) && customFieldItem.getSelectedId() != null) {
            if (edsCustomFields.getCustomEntMap().containsKey(customFieldItem.getColumnCode())) {
                edsCustomFields.getCustomEntMap().replace(customFieldItem.getColumnCode(), customFieldItem.getSelectedId() + "");
            } else {
                edsCustomFields.getCustomEntMap().put(customFieldItem.getColumnCode(), customFieldItem.getSelectedId() + "");
            }
        }
        if (Constants.TYPE_ENTITY_LOOKUP.equals(customFieldItem.getUiType()) || Constants.UI_TYPE_LOOKUP.equals(customFieldItem.getUiType()) || Constants.UI_TYPE_CURRENCY.equals(customFieldItem.getUiType())) {
            Gson gson = new Gson();
            String json = gson.toJson(edsCustomFields.getCustomEntMap());
            edsCustomFields.setJsonEntities(json);
        }
        return edsCustomFields;
    }

    private static String getterName(String columnCode) {
        return "get" + EdsCustomFields.map.get(columnCode);
    }

    private static String setterName(String columnCode) {
        return "set" + EdsCustomFields.map.get(columnCode);
    }

    private static void setCompanyCustomFieldItemStrings(EdsCustomFields edsCustomFields, CompanyCustomFieldItem fieldsItem) {
        if (edsCustomFields != null) {
            try {
                String stringValue;

                Object obj = getObjectValue(edsCustomFields, fieldsItem.getColumnCode());
                stringValue = obj == null ? null : "" + obj;

                if (Constants.TYPE_ENTITY_LOOKUP.equals(fieldsItem.getUiType()) && edsCustomFields.getJsonEntities() != null) {
                    Gson gson = new Gson();
                    Type dataType = new TypeToken<HashMap<String, String>>() {
                    }.getType();
                    HashMap<String, String> entMap = gson.fromJson(edsCustomFields.getJsonEntities(), dataType);
                    if (entMap.containsKey(fieldsItem.getColumnCode())) {
                        String kv = entMap.get(fieldsItem.getColumnCode());
                        String[] keyValue = kv.split("=");
                        fieldsItem.setEntityType(new SelectItem());
                        if (keyValue.length > 0) {
                            fieldsItem.setEntityType(new SelectItem(Integer.parseInt(keyValue[0])));
                        }
                        if (keyValue.length > 1) {
                            fieldsItem.setSelectedId(Integer.parseInt(keyValue[1]));
                        }
                    }
                } else if (Constants.UI_TYPE_LOOKUP.equals(fieldsItem.getUiType()) && edsCustomFields.getJsonEntities() != null) {
                    Gson gson = new Gson();
                    Type dataType = new TypeToken<HashMap<String, String>>() {
                    }.getType();
                    HashMap<String, String> map = gson.fromJson(edsCustomFields.getJsonEntities(), dataType);
                    if (map.containsKey(fieldsItem.getColumnCode())) {
                        String value = map.get(fieldsItem.getColumnCode());
                        if (value != null) {
                            Integer objectId = Integer.parseInt(value);
                            fieldsItem.setSelectedId(objectId);
                            String newValue = getLookUpStringValue(fieldsItem, objectId);
                            if (newValue != null && !"".equals(newValue)) {
                                stringValue = newValue;
                            }
                        }
                    }
                } else if (Constants.UI_TYPE_ITEM_WITH_DESCRIPTION.equals(fieldsItem.getUiType())) {
                    SelectItem item = new Gson().fromJson(stringValue, new TypeToken<SelectItem>() {
                    }.getType());
                    fieldsItem.setItem(item);
                } else if (Constants.TYPE_ENTITY_MULTI_LOOKUP.equals(fieldsItem.getUiType()) || Constants.UI_TYPE_MULTI_LOOKUP.equals(fieldsItem.getUiType())) {
                    if (stringValue != null && stringValue.startsWith("[{")) {
                        ArrayList<SelectItem> item = new Gson().fromJson(stringValue, new TypeToken<List<SelectItem>>() {
                        }.getType());
                        if (CustomFieldLookUpTypeEnum.DEPARTMENT.equals(fieldsItem.getLookUpTypeEnum()) && (item != null && item.size() > 0)) {
                            DepartmentManager departmentManager = StaticContextAccessor.getBean(DepartmentManager.class);
                            item.stream().peek(selectItem -> selectItem.setName(departmentManager.get(selectItem.getId()).getName())).collect(Collectors.toList());
                        }
                        if (CustomFieldLookUpTypeEnum.POSITION.equals(fieldsItem.getLookUpTypeEnum()) && (item != null && item.size() > 0)) {
                            PositionManager positionManager = StaticContextAccessor.getBean(PositionManager.class);
                            item.stream().peek(selectItem -> {
                                EdsPosition position = positionManager.get(selectItem.getId());
                                if (position != null) {
                                    selectItem.setName(position.getName());
                                }
                            }).collect(Collectors.toList());
                        }
                        fieldsItem.setSelectItems(item);
                    }
                } else if (Constants.UI_TYPE_CURRENCY.equals(fieldsItem.getUiType()) && edsCustomFields.getJsonEntities() != null) {
                    Gson gson = new Gson();
                    Type dataType = new TypeToken<HashMap<String, String>>() {
                    }.getType();
                    HashMap<String, String> map = gson.fromJson(edsCustomFields.getJsonEntities(), dataType);
                    if (map.containsKey(fieldsItem.getColumnCode())) {
                        String value = map.get(fieldsItem.getColumnCode());
                        if (value != null) {
                            Integer objectId = Integer.parseInt(value);
                            fieldsItem.setSelectedId(objectId);
                        }
                    }
                } else if (Constants.UI_TYPE_DROPDOWN.equals(fieldsItem.getUiType()) || Constants.UI_TYPE_RADIOBUTTON.equals(fieldsItem.getUiType()) || Constants.UI_TYPE_CHECKBOX.equals(fieldsItem.getUiType())) {
                    UserManager userManager = StaticContextAccessor.getBean(UserManager.class);
                    UserEmailSettingsManager userEmailSettingsManager = StaticContextAccessor.getBean(UserEmailSettingsManager.class);
                    EdsUser loggedUser = userManager.getUser();
                    EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(loggedUser);
                    if (fieldsItem.getLocalization() != null && fieldsItem.getLocalization().getChildren() != null) {
                        switch (userSettings.getInternationalization()) {
                            case "en" -> {
                                for (CustomFormLocalization localization : fieldsItem.getLocalization().getChildren()) {
                                    if (localization.getDefaultName() != null && stringValue != null) {
                                        if (stringValue.equals(localization.getDefaultName())) {
                                            stringValue = localization.getEnglishName();
                                        }
                                    }
                                }
                            }
                            case "ar" -> {
                                for (CustomFormLocalization localization : fieldsItem.getLocalization().getChildren()) {
                                    if (localization.getDefaultName() != null && stringValue != null) {
                                        if (stringValue.equals(localization.getDefaultName())) {
                                            stringValue = localization.getArabicName();
                                        }
                                    }

                                }
                            }
                            case "ru" -> {
                                for (CustomFormLocalization localization : fieldsItem.getLocalization().getChildren()) {
                                    if (localization.getDefaultName() != null && stringValue != null) {
                                        if (stringValue.equals(localization.getDefaultName())) {
                                            stringValue = localization.getRussianName();
                                        }
                                    }

                                }
                            }
                            case "uz" -> {
                                for (CustomFormLocalization localization : fieldsItem.getLocalization().getChildren()) {
                                    if (localization.getDefaultName() != null && stringValue != null) {
                                        if (stringValue.equals(localization.getDefaultName())) {
                                            stringValue = localization.getUzbekName();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                fieldsItem.setObjectId(edsCustomFields.getObjectID());
                fieldsItem.setFieldStringValue(stringValue);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    public static Object getObjectValue(EdsCustomFields edsCustomFields, String columnCode) {
        if (EdsCustomFields.map.get(columnCode) != null) {
            try {
                return EdsCustomFields.class.getDeclaredMethod(getterName(columnCode)).invoke(edsCustomFields);
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static void setCompanyCustomFieldItemDouble(EdsCustomFields edsCustomFields, CompanyCustomFieldItem fieldsItem) {
        BigDecimal value = null;
        int scale = fieldsItem.getScale() != null ? fieldsItem.getScale() : 3;
        if (edsCustomFields != null) {
            try {
                Object obj = getObjectValue(edsCustomFields, fieldsItem.getColumnCode());
                value = obj == null ? null : new BigDecimal(obj.toString()).setScale(scale, RoundingMode.HALF_DOWN);
                if (value != null && Constants.DATA_TYPE_PROFILE_IMAGE.equals(fieldsItem.getDataType())) {
                    fieldsItem.setProfielImageId(value.intValue());
                }
                fieldsItem.setObjectId(edsCustomFields.getObjectID());
                if (!fieldsItem.getUiType().equals(Constants.UI_TYPE_TEXTBOX) || (fieldsItem.getScale() != null && fieldsItem.getScale() == 0)) {
                    fieldsItem.setFieldStringValue(value != null ? value.toString() : null, true);
                } else {
                    fieldsItem.setFieldStringValue(value != null ? value.toString() : null);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    private static void setCompanyCustomFieldItemDouble2(EdsCustomFields edsCustomFields, CompanyCustomFieldItem fieldsItem) {
        BigDecimal value;
        if (edsCustomFields != null) {

            try {
                Object obj = getObjectValue(edsCustomFields, fieldsItem.getColumnCode());
                value = obj == null ? null : new BigDecimal(obj.toString()).setScale(3, RoundingMode.HALF_DOWN);
                fieldsItem.setObjectId(edsCustomFields.getObjectID());
                fieldsItem.setFieldStringValue(value != null ? value.toString() : null);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    private static void setCompanyCustomFieldItemDate(EdsCustomFields edsCustomFields, CompanyCustomFieldItem fieldsItem) {
        if (edsCustomFields != null) {
            try {
                Date dateValue;
                Object obj = getObjectValue(edsCustomFields, fieldsItem.getColumnCode());
                dateValue = (Date) obj;

                fieldsItem.setObjectId(edsCustomFields.getObjectID());
                fieldsItem.setFieldDateNonConvertedValue(new DateNonConvertable(dateValue));
                fieldsItem.setFieldDateNonConvertedValue(dateValue != null ? new DateNonConvertable(dateValue) : null);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * CompanyCustomFieldItem rpc object value set to EdsCustomFields domen object
     *
     * @param edsCustomFields
     * @param customFieldItems
     * @return EdsCustomFields
     */
    public static EdsCustomFields setDomenObjectCustomFields(EdsCustomFields edsCustomFields, List<CompanyCustomFieldItem> customFieldItems) {
        for (CompanyCustomFieldItem fieldItem : customFieldItems) {
            if (Constants.DATA_TYPE_DATE.equals(fieldItem.getDataType())) {
                setDateCustomFields(edsCustomFields, fieldItem);
            } else if (Constants.DATA_TYPE_FILE_UPLOAD.equals(fieldItem.getDataType())) {
                setFileUploadCustomFields(edsCustomFields, fieldItem);
            } else if (Constants.DATA_TYPE_NUMBER.equals(fieldItem.getDataType()) || Constants.DATA_TYPE_PROFILE_IMAGE.equals(fieldItem.getDataType())) {
                setDoubleCustomFields(edsCustomFields, fieldItem);
            } else {
                setStringCustomFields(edsCustomFields, fieldItem);
            }
        }
        return edsCustomFields;
    }

    public static void setFileUploadCustomFields(EdsCustomFields edsCustomFields, CompanyCustomFieldItem fieldItem) {
        if (edsCustomFields == null || fieldItem == null) {
            return;
        }
        Double doubleValue = null;
        if (fieldItem.getFileUploadFieldId() != null) {
            doubleValue = Double.valueOf(fieldItem.getFileUploadFieldId());
        }
        if (fieldItem.getAttachments() != null && fieldItem.getAttachments().length > 0) {
            try {
                AttachmentUtilsManager attachmentUtilsManager = (AttachmentUtilsManager) ApplicationContextProvider.applicationContext.getBean("attachmentUtilsManager");
                attachmentUtilsManager.saveAttachments(F_CUSTOM_FIELD_ITEMS, fieldItem.getFileUploadFieldId(), edsCustomFields.getObjectID(), fieldItem.getAttachments());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        setValueCustomField(edsCustomFields, fieldItem.getColumnCode(), doubleValue);

    }

    /**
     * CompanyCustomFieldItem rpc object value set to EdsCustomFields domen object
     *
     * @param edsCustomFields
     * @param customFieldItems
     * @return EdsCustomFields
     */
    public static EdsCustomFields setAccountingDomainObjectCustomFields(EdsCustomFields edsCustomFields, List<CompanyCustomFieldItem> customFieldItems) {
        for (CompanyCustomFieldItem fieldItem : customFieldItems) {
            if (Constants.DATA_TYPE_DATE.equals(fieldItem.getDataType())) {
                setDateCustomFields(edsCustomFields, fieldItem);
            } else if (Constants.DATA_TYPE_NUMBER.equals(fieldItem.getDataType()) || Constants.DATA_TYPE_PROFILE_IMAGE.equals(fieldItem.getDataType())) {
                setDoubleCustomFields(edsCustomFields, fieldItem);
            } else if (Constants.DATA_TYPE_FILE_UPLOAD.equals(fieldItem.getDataType())) {
                setFileUploadCustomFields(edsCustomFields, fieldItem);
            } else {
                setStringCustomFields(edsCustomFields, fieldItem);
            }
        }
        return edsCustomFields;
    }

    public static void setDomenObjectFieldChange(EdsCustomFields edsCustomFields, Map<String, Object> customFields, String columnCodeName) {
        if (edsCustomFields != null) {
            if (columnCodeName.contains("string")) {
                setStringCustomFields(edsCustomFields, customFields, columnCodeName);
            } else if (columnCodeName.contains("double")) {
                setDoubleCustomFields(edsCustomFields, customFields, columnCodeName);
            } else if (columnCodeName.contains("date")) {
                setDateCustomFields(edsCustomFields, customFields, columnCodeName);
            }
        }
    }

    /**
     * For String Value Cell Editor
     *
     * @param edsCustomFields
     * @param customFields
     * @param columnCodeName
     */
    private static void setStringCustomFields(EdsCustomFields edsCustomFields, Map<String, Object> customFields, String columnCodeName) {
        CompanyCustomFieldItem fieldItem = new CompanyCustomFieldItem();
        if (customFields.get(columnCodeName) != null) {
            fieldItem.setFieldStringValue(customFields.get(columnCodeName).toString());
        }
        fieldItem.setColumnCode(columnCodeName);
        setStringCustomFields(edsCustomFields, fieldItem);
    }

    /**
     * For Double Value Cell Editor
     *
     * @param edsCustomFields
     * @param customFields
     * @param columnCodeName
     */
    private static void setDoubleCustomFields(EdsCustomFields edsCustomFields, Map<String, Object> customFields, String columnCodeName) {
        CompanyCustomFieldItem fieldItem = new CompanyCustomFieldItem();
        if (customFields.get(columnCodeName) != null) {
            fieldItem.setFieldStringValue(customFields.get(columnCodeName).toString());
        }
        fieldItem.setColumnCode(columnCodeName);
        setDoubleCustomFields(edsCustomFields, fieldItem);
    }

    /**
     * For Date Value Cell Editor
     *
     * @param edsCustomFields
     * @param customFields
     * @param columnCodeName
     */
    private static void setDateCustomFields(EdsCustomFields edsCustomFields, Map<String, Object> customFields, String columnCodeName) {
        CompanyCustomFieldItem fieldItem = new CompanyCustomFieldItem();
        if (customFields.get(columnCodeName) instanceof Date) {
            fieldItem.setFieldDateNonConvertedValue(new DateNonConvertable((Date) customFields.get(columnCodeName)));
        } else {
            fieldItem.setFieldDateNonConvertedValue((DateNonConvertable) customFields.get(columnCodeName));
        }
        fieldItem.setColumnCode(columnCodeName);
        setDateCustomFields(edsCustomFields, fieldItem);
    }

    /**
     * at EdsCustomFields Domen Object set to CustomFieldItems rpc object
     *
     * @param edsCustomFields
     * @param companyCustomFieldItems
     * @return List<CompanyCustomFieldItem>
     */
    public static ArrayList<CompanyCustomFieldItem> setRPCCustomFieldItems(EdsCustomFields edsCustomFields, ArrayList<CompanyCustomFieldItem> companyCustomFieldItems) {
        if (companyCustomFieldItems != null && companyCustomFieldItems.size() > 0) {
            companyCustomFieldItems.removeIf(Objects::isNull);
            companyCustomFieldItems.forEach(fieldItem -> {
                fieldItem.setObjectId(null);
                switch (fieldItem.getDataType()) {
                    case Constants.DATA_TYPE_DATE -> setCompanyCustomFieldItemDate(edsCustomFields, fieldItem);
                    case Constants.DATA_TYPE_FILE_UPLOAD ->
                            setCompanyCustomFieldItemDouble2(edsCustomFields, fieldItem);
                    case Constants.DATA_TYPE_NUMBER, Constants.DATA_TYPE_PROFILE_IMAGE ->
                            setCompanyCustomFieldItemDouble(edsCustomFields, fieldItem);
                    default -> setCompanyCustomFieldItemStrings(edsCustomFields, fieldItem);
                }
            });
        }
        return companyCustomFieldItems;
    }

    public static HashMap<String, CompanyCustomFieldItem> setSystemCustomFieldItemMap(List<CompanyCustomFieldItem> companyCustomFieldItems) {
        if (companyCustomFieldItems != null && companyCustomFieldItems.size() > 0) {
            HashMap<String, CompanyCustomFieldItem> map = new HashMap<>();
            for (CompanyCustomFieldItem companyCustomFieldItem : companyCustomFieldItems) {
                map.put(companyCustomFieldItem.getColumnCode(), companyCustomFieldItem);
            }
            return map;
        }
        return new HashMap<>();
    }

    /**
     * Get In Solr Custom fields and they are sets to Map<ColumnCode,Value>
     */
    public static HashMap<String, Object> getInSolrCustomFields(SolrDocument relevantDoc, List<String> columnCodeName) {
        HashMap<String, Object> mapObject = new java.util.HashMap<>();
        for (int i = 1; i <= Constants.STRING_FIELD_LIMIT; i++) {
            if (columnCodeName != null) {
                if (i <= Constants.FIELD_LIMIT) {
                    try {
                        if (columnCodeName.contains(TaskListItem.DATE_VALUE + i) && relevantDoc.getFieldValueMap().containsKey(SolrTaskRepresenter.DYNAMIC_FIELD_CF_DATE + i)) {

                            if (relevantDoc.getFieldValue(SolrTaskRepresenter.DYNAMIC_FIELD_CF_DATE + i) instanceof String) {
                                DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
                                Date date = dateFormat.parse(relevantDoc.getFieldValue(SolrTaskRepresenter.DYNAMIC_FIELD_CF_DATE + i).toString());
                                DateNonConvertable dateNonConvertable = new DateNonConvertable();
                                dateNonConvertable.setDate(date);
                                mapObject.put(TaskListItem.DATE_VALUE + i, dateNonConvertable);
                            } else if (relevantDoc.getFieldValue(SolrTaskRepresenter.DYNAMIC_FIELD_CF_DATE + i) instanceof Date) {
                                Date date = (Date) relevantDoc.getFieldValue(SolrTaskRepresenter.DYNAMIC_FIELD_CF_DATE + i);
                                DateNonConvertable dateNonConvertable = new DateNonConvertable();
                                dateNonConvertable.setDate(date);
                                mapObject.put(TaskListItem.DATE_VALUE + i, dateNonConvertable);
                            } else {
                                mapObject.put(TaskListItem.DATE_VALUE + i, relevantDoc.getFieldValue(SolrTaskRepresenter.DYNAMIC_FIELD_CF_DATE + i));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (i <= Constants.DOULE_FIELD_LIMIT) {
                    try {
                        if (columnCodeName.contains(TaskListItem.NUMBER_VALUE + i) && relevantDoc.getFieldValueMap().containsKey(SolrTaskRepresenter.DYNAMIC_FIELD_CF_DOUBLE + i)) {
                            mapObject.put(TaskListItem.NUMBER_VALUE + i, relevantDoc.getFieldValue(SolrTaskRepresenter.DYNAMIC_FIELD_CF_DOUBLE + i));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (i <= Constants.STRING_FIELD_LIMIT) {
                    try {
                        if (columnCodeName.contains(TaskListItem.STRING_VALUE + i) && relevantDoc.getFieldValueMap().containsKey(SolrTaskRepresenter.DYNAMIC_FIELD_CF_STRING + i)) {
                            mapObject.put(TaskListItem.STRING_VALUE + i, relevantDoc.getFieldValue(SolrTaskRepresenter.DYNAMIC_FIELD_CF_STRING + i));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        return mapObject;
    }

    public static HashMap<String, Object> getBaseSolrDocDynamicFields(BaseSolrDoc baseSolrDoc, List<String> columnCodeName) {
        HashMap<String, Object> mapObject = new java.util.HashMap<>();
        for (int i = 1; i <= Constants.FIELD_LIMIT; i++) {
            if (columnCodeName != null) {
                try {
                    if (columnCodeName.contains(DATE_VALUE + i) && baseSolrDoc.getDateValueDynamic().containsKey("" + i)) {
                        mapObject.put(DATE_VALUE + i, new DateNonConvertable(baseSolrDoc.getDateValueDynamic().get("" + i)));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (columnCodeName.contains(DOUBLE_VALUE + i) && baseSolrDoc.getDoubleValueDynamic().containsKey("" + i)) {
                        mapObject.put(DOUBLE_VALUE + i, baseSolrDoc.getDoubleValueDynamic().get("" + i));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (columnCodeName.contains(STRING_VALUE + i) && baseSolrDoc.getStringValueDynamic().containsKey("" + i)) {
                        mapObject.put(STRING_VALUE + i, baseSolrDoc.getStringValueDynamic().get("" + i));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return mapObject;
    }

    public static HashMap<String, Object> getSolrDocDynamicFields(SolrDocument relevantDoc, List<String> columnCodeName) {
        HashMap<String, Object> mapObject = new java.util.HashMap<>();
        for (int i = 1; i <= Constants.FIELD_LIMIT; i++) {
            if (columnCodeName != null) {
                try {
                    if (columnCodeName.contains(TaskListItem.DATE_VALUE + i) && relevantDoc.getFieldValueMap().containsKey(SolrTaskRepresenter.DYNAMIC_FIELD_CF_DATE + i)) {

                        if (relevantDoc.getFieldValue(SolrTaskRepresenter.DYNAMIC_FIELD_CF_DATE + i) instanceof String) {
                            DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
                            Date date = dateFormat.parse(relevantDoc.getFieldValue(SolrTaskRepresenter.DYNAMIC_FIELD_CF_DATE + i).toString());
                            DateNonConvertable dateNonConvertable = new DateNonConvertable();
                            dateNonConvertable.setDate(date);
                            mapObject.put(TaskListItem.DATE_VALUE + i, dateNonConvertable);
                        } else if (relevantDoc.getFieldValue(SolrTaskRepresenter.DYNAMIC_FIELD_CF_DATE + i) instanceof Date) {
                            Date date = (Date) relevantDoc.getFieldValue(SolrTaskRepresenter.DYNAMIC_FIELD_CF_DATE + i);
                            DateNonConvertable dateNonConvertable = new DateNonConvertable();
                            dateNonConvertable.setDate(date);
                            mapObject.put(TaskListItem.DATE_VALUE + i, dateNonConvertable);
                        } else {
                            mapObject.put(TaskListItem.DATE_VALUE + i, relevantDoc.getFieldValue(SolrTaskRepresenter.DYNAMIC_FIELD_CF_DATE + i));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (columnCodeName.contains(DOUBLE_VALUE + i) && relevantDoc.getFieldValueMap().containsKey(SolrTaskRepresenter.DYNAMIC_FIELD_CF_DOUBLE + i)) {
                        mapObject.put(DOUBLE_VALUE + i, relevantDoc.getFieldValue(SolrTaskRepresenter.DYNAMIC_FIELD_CF_DOUBLE + i));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (columnCodeName.contains(TaskListItem.STRING_VALUE + i) && relevantDoc.getFieldValueMap().containsKey(SolrTaskRepresenter.DYNAMIC_FIELD_CF_STRING + i)) {
                        mapObject.put(TaskListItem.STRING_VALUE + i, relevantDoc.getFieldValue(SolrTaskRepresenter.DYNAMIC_FIELD_CF_STRING + i));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return mapObject;
    }

    /**
     * Get In RPC Custom fields and they are sets to Map<ColumnCode,Value>
     */
    public static HashMap<String, Object> getRPCCustomFields(EdsCustomFields customFields, List<String> columnCodeName) {
        HashMap<String, Object> mapObject = new java.util.HashMap<>();
        if (customFields != null && columnCodeName != null) {
            for (int i = 1; i <= Constants.FIELD_LIMIT; i++) {
                try {
                    if (columnCodeName.contains(DATE_VALUE + i) && customFields.getDateValue(DATE_VALUE + i) != null) {
                        mapObject.put(DATE_VALUE + i, customFields.getDateValue(DATE_VALUE + i));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for (int i = 1; i <= Constants.DOULE_FIELD_LIMIT; i++) {
                try {
                    if (columnCodeName.contains(DOUBLE_VALUE + i) && customFields.getDoubleValue(DOUBLE_VALUE + i) != null) {
                        mapObject.put(DOUBLE_VALUE + i, customFields.getDoubleValue(DOUBLE_VALUE + i));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for (int i = 1; i <= Constants.STRING_FIELD_LIMIT; i++) {
                try {
                    if (columnCodeName.contains(STRING_VALUE + i) && customFields.getStringValue(STRING_VALUE + i) != null) {
                        mapObject.put(STRING_VALUE + i, customFields.getStringValue(STRING_VALUE + i));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return mapObject;
    }

    /**
     * Set In Solr Custom fields
     */
    public static void setInSolrCustomFields(SolrInputDocument doc, EdsCustomFields customFields) {
        if (customFields != null) {
            try {
                for (int i = 1; i <= Constants.FIELD_LIMIT; i++) {
                    Date value = customFields.getDateValue((SolrTaskRepresenter.DYNAMIC_FIELD_CF_DATE + i).toLowerCase());
                    if (value != null) {
                        doc.addField(SolrTaskRepresenter.DYNAMIC_FIELD_CF_DATE + i, value);
                    }
                }

                for (int i = 1; i <= Constants.DOULE_FIELD_LIMIT; i++) {
                    Double value = customFields.getDoubleValue((SolrTaskRepresenter.DYNAMIC_FIELD_CF_DOUBLE + i).toLowerCase());
                    if (value != null) {
                        doc.addField(SolrTaskRepresenter.DYNAMIC_FIELD_CF_DOUBLE + i, value);
                    }
                }

                for (int i = 1; i <= Constants.DOULE_FIELD_LIMIT; i++) {
                    String value = customFields.getStringValue((SolrTaskRepresenter.DYNAMIC_FIELD_CF_STRING + i).toLowerCase());
                    if (value != null && value.length() <= 32766) {
                        doc.addField(SolrTaskRepresenter.DYNAMIC_FIELD_CF_STRING + i, value);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Dynamic set custom fields to Solr collection
     */
    public static void setSolrDocDynamicFields(BaseSolrDoc doc, EdsCustomFields customFields) {
        if (customFields != null) {
            try {
                for (int i = 1; i <= Constants.FIELD_LIMIT; i++) {
                    Date value = customFields.getDateValue((SolrTaskRepresenter.DYNAMIC_FIELD_CF_DATE + i).toLowerCase());
                    if (value != null) {
                        doc.getDateValueDynamic().computeIfAbsent("" + i, v -> value);
                    }
                }

                for (int i = 1; i <= Constants.FIELD_LIMIT; i++) {
                    Double value = customFields.getDoubleValue((SolrTaskRepresenter.DYNAMIC_FIELD_CF_DOUBLE + i).toLowerCase());
                    if (value != null) {
                        doc.getDoubleValueDynamic().computeIfAbsent("" + i, v -> value);
                    }
                }

                for (int i = 1; i <= Constants.FIELD_LIMIT; i++) {
                    String value = customFields.getStringValue((SolrTaskRepresenter.DYNAMIC_FIELD_CF_STRING + i).toLowerCase());
                    if (value != null) {
                        doc.getStringValueDynamic().computeIfAbsent("" + i, v -> value);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Itext Pdf Custom Fields set Pdf Header
     *
     * @param customfields
     * @param pdfHeader
     */
    public static void setCustomFieldsPdfHeaderMap(List<CompanyCustomFieldItem> customfields, Map<String, CellData> pdfHeader) {
        if (customfields != null) {
            for (CompanyCustomFieldItem field : customfields) {
                pdfHeader.put(field.getColumnCode(), new CellData(field.getFieldName(), Element.ALIGN_LEFT));
            }
        }
    }

    /**
     * Itext Pdf Custom Fields set Pdf Table row
     *
     * @param customfields
     * @param pdfTableRows
     * @param fieldColumnCode
     * @param customFieldData
     * @param edsCompany
     */
    public static void setCustomFieldsPdfTableRows(List<CompanyCustomFieldItem> customfields, Map<String, CellData> pdfTableRows, List<String> fieldColumnCode, ListingCustomFields customFieldData, EdsCompany edsCompany) {
        if (customFieldData != null && customfields != null) {
            for (CompanyCustomFieldItem field : customfields) {
                if (fieldColumnCode.contains(field.getColumnCode())) {
                    try {
                        if (Constants.DATA_TYPE_DATE.equals(field.getDataType())) {
                            Date data = null;
                            if (customFieldData.getCustomFieldsValue(field.getColumnCode()) instanceof DateNonConvertable) {
                                data = ((DateNonConvertable) customFieldData.getCustomFieldsValue(field.getColumnCode())).getDate();
                            } else {
                                data = (Date) customFieldData.getCustomFieldsValue(field.getColumnCode());
                            }
                            if (data != null) {
                                pdfTableRows.put(field.getColumnCode(), new CellData(Utils.formatDate(data, edsCompany)));
                            } else {
                                pdfTableRows.put(field.getColumnCode(), new CellData("—"));
                            }
                        } else if (Constants.DATA_TYPE_NUMBER.equals(field.getDataType())) {
                            Double data = (Double) customFieldData.getCustomFieldsValue(field.getColumnCode());
                            if (data != null) {
                                pdfTableRows.put(field.getColumnCode(), new CellData(Utils.formatDouble(data)));
                            } else {
                                pdfTableRows.put(field.getColumnCode(), new CellData("—"));
                            }
                        } else {
                            String data = (String) customFieldData.getCustomFieldsValue(field.getColumnCode());
                            if (data != null) {
                                pdfTableRows.put(field.getColumnCode(), new CellData(data));
                            } else {
                                pdfTableRows.put(field.getColumnCode(), new CellData("—"));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * POI excel header custom fields generate
     *
     * @param customfields
     * @param pdfHeader
     */
    public static void setCustomFieldsExcelHeaderMap(List<CompanyCustomFieldItem> customfields, Map<String, ExcelData> pdfHeader) {
        if (customfields != null) {
            for (CompanyCustomFieldItem field : customfields) {
                pdfHeader.put(field.getColumnCode(), new ExcelData(field.getFieldName(), ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            }
        }
    }

    public static void setCustomFieldsExcelHeaderMapWithoutBorder(List<CompanyCustomFieldItem> customfields, Map<String, ExcelData> pdfHeader) {
        if (customfields != null) {
            for (CompanyCustomFieldItem field : customfields) {
                pdfHeader.put(field.getColumnCode(), new ExcelData(field.getFieldName(), ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            }
        }
    }

    public static void setCustomFieldsExcelHeaderMapWithNormalBorder(List<CompanyCustomFieldItem> customfields, Map<String, ExcelData> pdfHeader) {
        if (customfields != null) {
            for (CompanyCustomFieldItem field : customfields) {
                pdfHeader.put(field.getColumnCode(), new ExcelData(field.getFieldName(), ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
            }
        }
    }

    /**
     * POI excel table columns custom fields generate
     *
     * @param customfields
     * @param pdfTableRows
     * @param fieldColumnCode
     * @param customFieldData
     * @param edsCompany
     */
    public static void setCustomFieldsExcelTableRows(List<CompanyCustomFieldItem> customfields, Map<String, ExcelData> pdfTableRows, List<String> fieldColumnCode, ListingCustomFields customFieldData, EdsCompany edsCompany) {
        if (customFieldData != null && customfields != null) {
            for (CompanyCustomFieldItem field : customfields) {
                if (fieldColumnCode.contains(field.getColumnCode())) {
                    try {
                        if (Constants.DATA_TYPE_DATE.equals(field.getDataType())) {
                            Date date = null;
                            if (customFieldData.getCustomFieldsValue(field.getColumnCode()) instanceof DateNonConvertable) {
                                date = ((DateNonConvertable) customFieldData.getCustomFieldsValue(field.getColumnCode())).getDate();
                            } else {
                                date = (Date) customFieldData.getCustomFieldsValue(field.getColumnCode());
                            }
                            if (date != null) {
                                pdfTableRows.put(field.getColumnCode(), new ExcelData(Utils.formatDate(date, edsCompany), ExcelData.STRING, 25, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                            } else {
                                pdfTableRows.put(field.getColumnCode(), new ExcelData("N/A", ExcelData.STRING, 25, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                            }
                        } else if (Constants.DATA_TYPE_NUMBER.equals(field.getDataType())) {
                            Double data = (Double) customFieldData.getCustomFieldsValue(field.getColumnCode());
                            if (data != null) {
                                pdfTableRows.put(field.getColumnCode(), new ExcelData(Utils.formatDouble(data), ExcelData.STRING, 25, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                            } else {
                                pdfTableRows.put(field.getColumnCode(), new ExcelData("N/A", ExcelData.STRING, 25, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                            }
                        } else {
                            String data = (String) customFieldData.getCustomFieldsValue(field.getColumnCode());
                            if (Constants.TYPE_ENTITY_LOOKUP.equals(field.getUiType())) {
                                if (field.getQueryItems() != null) {
                                    for (SelectItem item : field.getQueryItems()) {
                                        if (item.getId().equals(Integer.valueOf(data))) {
                                            data = item.getName();
                                            break;
                                        }
                                    }
                                }

                            }
                            if (data != null) {
                                pdfTableRows.put(field.getColumnCode(), new ExcelData(data, ExcelData.STRING, 25, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                            } else {
                                pdfTableRows.put(field.getColumnCode(), new ExcelData("N/A", ExcelData.STRING, 25, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void setCustomFieldsExcelTableRowsWithoutBorder(List<CompanyCustomFieldItem> customfields, Map<String, ExcelData> pdfTableRows, List<String> fieldColumnCode, ListingCustomFields customFieldData, EdsCompany edsCompany) {
        if (customFieldData != null && customfields != null) {
            for (CompanyCustomFieldItem field : customfields) {
                if (fieldColumnCode.contains(field.getColumnCode())) {
                    try {
                        if (Constants.DATA_TYPE_DATE.equals(field.getDataType())) {
                            Date date = null;
                            if (customFieldData.getCustomFieldsValue(field.getColumnCode()) instanceof DateNonConvertable) {
                                date = ((DateNonConvertable) customFieldData.getCustomFieldsValue(field.getColumnCode())).getDate();
                            } else {
                                date = (Date) customFieldData.getCustomFieldsValue(field.getColumnCode());
                            }
                            if (date != null) {
                                pdfTableRows.put(field.getColumnCode(), new ExcelData(Utils.formatDate(date, edsCompany), ExcelData.STRING, 25, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                            } else {
                                pdfTableRows.put(field.getColumnCode(), new ExcelData("N/A", ExcelData.STRING, 25, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                            }
                        } else if (Constants.DATA_TYPE_NUMBER.equals(field.getDataType())) {
                            Double data = (Double) customFieldData.getCustomFieldsValue(field.getColumnCode());
                            if (data != null) {
                                pdfTableRows.put(field.getColumnCode(), new ExcelData(Utils.formatDouble(data), ExcelData.STRING, 25, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                            } else {
                                pdfTableRows.put(field.getColumnCode(), new ExcelData("N/A", ExcelData.STRING, 25, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                            }
                        } else {
                            String data = (String) customFieldData.getCustomFieldsValue(field.getColumnCode());
                            if (Constants.TYPE_ENTITY_LOOKUP.equals(field.getUiType())) {
                                if (field.getQueryItems() != null) {
                                    for (SelectItem item : field.getQueryItems()) {
                                        if (item.getId().equals(Integer.valueOf(data))) {
                                            data = item.getName();
                                            break;
                                        }
                                    }
                                }

                            }
                            if (data != null) {
                                pdfTableRows.put(field.getColumnCode(), new ExcelData(data, ExcelData.STRING, 25, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                            } else {
                                pdfTableRows.put(field.getColumnCode(), new ExcelData("N/A", ExcelData.STRING, 25, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void setCustomFieldsSortableNameToSolr(String sortField, boolean desc, SolrQuery query) {
        setCustomFieldsSortableNameToSolr(sortField, desc, query, false);
    }

    /**
     * @param sortField
     * @param desc
     * @param query
     * @param isDynamicString
     * @TODO after all core move to new version SOLR our need remove this is method
     */

    public static void setCustomFieldsSortableNameToSolr(String sortField, boolean desc, SolrQuery query, boolean isDynamicString) {
        if (!isDynamicString) {
            if (sortField.contains(SolrTaskRepresenter.DYNAMIC_FIELD_CF_DATE.toLowerCase())
                    || sortField.contains(SolrTaskRepresenter.DYNAMIC_FIELD_CF_DOUBLE.toLowerCase())
                    || sortField.contains(SolrTaskRepresenter.DYNAMIC_FIELD_CF_STRING.toLowerCase())) {
                query.setSort(sortField.toUpperCase(), (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
            }
        } else {
            if (sortField.contains(SolrTaskRepresenter.DYNAMIC_FIELD_CF_DATE.toLowerCase())
                    || sortField.contains(SolrTaskRepresenter.DYNAMIC_FIELD_CF_DOUBLE.toLowerCase())) {
                query.setSort(sortField.toUpperCase(), (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
            } else if (sortField.contains(SolrTaskRepresenter.DYNAMIC_FIELD_CF_STRING.toLowerCase())) {
                query.setSort(SORTABLE_ + sortField.toUpperCase(), (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
            }
        }
    }

    public static Sort getSortCustomFieldsSortableNameToSolr(String sortField, boolean desc, boolean isDynamicString) {
        if (!isDynamicString) {
            if (sortField.contains(SolrTaskRepresenter.DYNAMIC_FIELD_CF_DATE.toLowerCase())
                    || sortField.contains(SolrTaskRepresenter.DYNAMIC_FIELD_CF_DOUBLE.toLowerCase())
                    || sortField.contains(SolrTaskRepresenter.DYNAMIC_FIELD_CF_STRING.toLowerCase())) {
                return Sort.by((desc ? Sort.Direction.DESC : Sort.Direction.ASC), sortField);
            }
        } else {
            if (sortField.contains(SolrTaskRepresenter.DYNAMIC_FIELD_CF_DATE.toLowerCase())
                    || sortField.contains(SolrTaskRepresenter.DYNAMIC_FIELD_CF_DOUBLE.toLowerCase())) {
                return Sort.by((desc ? Sort.Direction.DESC : Sort.Direction.ASC), sortField);
            } else if (sortField.contains(SolrTaskRepresenter.DYNAMIC_FIELD_CF_STRING.toLowerCase())) {
                return Sort.by((desc ? Sort.Direction.DESC : Sort.Direction.ASC), sortField);
            }
        }
        return Sort.by((desc ? Sort.Direction.DESC : Sort.Direction.ASC), sortField);
    }

    public static ArrayList<CompanyCustomFieldItem> cloneItemCustomFields(List<CompanyCustomFieldItem> customFields) {

        if (customFields != null && !customFields.isEmpty()) {
            ArrayList<CompanyCustomFieldItem> items = new ArrayList<>();

            for (CompanyCustomFieldItem item : customFields) {
                items.add(item.cloneObject());
            }

            return items;
        }

        return null;
    }

    public static String[] getDataType(String uiType, HashMap<Integer, String[]> map) {
        String[] result = new String[2];
        String dataType = switch (uiType) {
            case Constants.UI_TYPE_TEXTBOX, Constants.UI_TYPE_TEXTBOX_EMAIL, Constants.UI_TYPE_URL,
                 Constants.UI_TYPE_TEXTAREA, Constants.UI_TYPE_HTML_TEXTAREA, Constants.UI_TYPE_DROPDOWN,
                 Constants.UI_TYPE_ENTITY_DROPDOWN, Constants.UI_TYPE_CHECKBOX, Constants.UI_TYPE_RADIOBUTTON,
                 Constants.UI_TYPE_CURRENCY, Constants.UI_TYPE_AUTONUMBER -> Constants.DATA_TYPE_TEXT;
            case Constants.UI_TYPE_DATEPICKER, Constants.UI_TYPE_DATEPICKER_TIME -> Constants.DATA_TYPE_DATE;
            case Constants.UI_TYPE_PROFILE_IMAGE_WIDGET -> Constants.DATA_TYPE_PROFILE_IMAGE;
            case Constants.UI_TYPE_FILE_UPLOAD_ITEM, Constants.UI_TYPE_FILE_UPLOAD_WIDGET ->
                    Constants.DATA_TYPE_FILE_UPLOAD;
            case Constants.NUMBER, Constants.UI_TYPE_PERCENTAGE -> Constants.DATA_TYPE_NUMBER;
            default -> Constants.DATA_TYPE_TEXT;
        };

        result[0] = dataType;

        String[] allItems = null;
        String value = "";
        int LIMIT_COUNT = Constants.FIELD_LIMIT;
        if (dataType.equals(Constants.DATA_TYPE_TEXT)) {
            value = "string_value";
            if (map.get(0).length < Constants.STRING_FIELD_LIMIT) {
                allItems = map.get(0);
                LIMIT_COUNT = Constants.STRING_FIELD_LIMIT;
            }
        }
        if (dataType.equals(Constants.DATA_TYPE_NUMBER) || dataType.equals(Constants.DATA_TYPE_FILE_UPLOAD) || dataType.equals(Constants.DATA_TYPE_PROFILE_IMAGE)) {
            value = "double_value";
            if (map.get(1).length < Constants.DOULE_FIELD_LIMIT) {
                allItems = map.get(1);
                LIMIT_COUNT = Constants.DOULE_FIELD_LIMIT;
            }
        }
        if (dataType.equals(Constants.DATA_TYPE_DATE)) {
            value = "date_value";
            if (map.get(2).length < Constants.FIELD_LIMIT) {
                allItems = map.get(2);
                LIMIT_COUNT = Constants.FIELD_LIMIT;
            }
        }
        int k;
        if (allItems != null) {
            for (int i = 1; i <= LIMIT_COUNT; i++) {
                String fieldname = value + i;
                k = 0;
                for (String allItem : allItems) {
                    if (fieldname.equals(allItem)) {
                        k++;
                        break;
                    }
                }
                if (k == 0) {
                    result[1] = fieldname;
                    break;
                }
            }
        } else {
            result[1] = value + 1;
        }
        return result;
    }

    public static String getLookUpStringValue(CompanyCustomFieldItem item, Integer objectId) {
        if (item == null) {
            return null;
        }
        switch (item.getLookUpTypeEnum()) {
            case LOCATION:
                LocationManager locationManager = StaticContextAccessor.getBean(LocationManager.class);
                EdsLocation location = locationManager.get(objectId);
                if (location != null) {
                    return StringUtils.defaultString(location.getName(), item.getFieldStringValue());
                } else {
                    return item.getFieldStringValue();
                }
            case PURCHASE_INVOICE:
            case SALES_INVOICE:
                InvoiceManager invoiceManager = StaticContextAccessor.getBean(InvoiceManager.class);
                EdsInvoice edsInvoice = invoiceManager.get(objectId);
                if (edsInvoice != null) {
                    return StringUtils.defaultString(edsInvoice.getNumber(), item.getFieldStringValue());
                } else {
                    return item.getFieldStringValue();
                }
            case SALES_QUOTE:
                QuoteManager quoteManager = StaticContextAccessor.getBean(QuoteManager.class);
                EdsQuote edsQuote = quoteManager.getSaleQuote(objectId);
                if (edsQuote != null) {
                    return StringUtils.defaultString(edsQuote.getNumber(), item.getFieldStringValue());
                } else {
                    return item.getFieldStringValue();
                }
            case PURCHASE_ORDER:
                QuoteManager purchaseOrderManager = StaticContextAccessor.getBean(QuoteManager.class);
                EdsQuote edsPurchaseOrder = purchaseOrderManager.getPurchaseOrderByID(objectId);
                if (edsPurchaseOrder != null) {
                    return StringUtils.defaultString(edsPurchaseOrder.getNumber(), item.getFieldStringValue());
                } else {
                    return item.getFieldStringValue();
                }
            case CURRENCY:
                CurrencyManager currencyManager = StaticContextAccessor.getBean(CurrencyManager.class);
                EdsCurrency edsCurrency = currencyManager.get(objectId);
                if (edsCurrency != null) {
                    return StringUtils.defaultString(edsCurrency.getName(), item.getFieldStringValue());
                } else {
                    return item.getFieldStringValue();
                }
            case EMPLOYEE:
                EmployeeManager employeeManager = StaticContextAccessor.getBean(EmployeeManager.class);
                EdsEmployee edsEmployee = employeeManager.get(objectId);
                if (edsEmployee != null) {
                    return StringUtils.defaultString(edsEmployee.getName(), item.getFieldStringValue());
                } else {
                    return item.getFieldStringValue();
                }
            case COUNTRY:
                CountryManager countryManager = StaticContextAccessor.getBean(CountryManager.class);
                EdsCountry edsCountry = countryManager.get(objectId);
                if (edsCountry != null) {
                    return StringUtils.defaultString(edsCountry.getName(), item.getFieldStringValue());
                } else {
                    return item.getFieldStringValue();
                }
            case TERMS:
                InvoiceTermsManager invoiceTermsManager = StaticContextAccessor.getBean(InvoiceTermsManager.class);
                EdsInvoiceTerms edsInvoiceTerms = invoiceTermsManager.get(objectId);
                if (edsInvoiceTerms != null) {
                    return StringUtils.defaultString(edsInvoiceTerms.getName(), item.getFieldStringValue());
                } else {
                    return item.getFieldStringValue();
                }
            case UNIT_MEASUREMENT:
                UnitMeasurementManager unitMeasurementManager = StaticContextAccessor.getBean(UnitMeasurementManager.class);
                EdsUnitMeasurement edsMeasurement = unitMeasurementManager.get(objectId);
                if (edsMeasurement != null) {
                    return StringUtils.defaultString(edsMeasurement.getName(), item.getFieldStringValue());
                } else {
                    return item.getFieldStringValue();
                }
            case PROJECT:
                ProjectManager projectManager = StaticContextAccessor.getBean(ProjectManager.class);
                EdsProject edsProject = projectManager.get(objectId);
                if (edsProject != null) {
                    return StringUtils.defaultString(edsProject.getName(), item.getFieldStringValue());
                } else {
                    return item.getFieldStringValue();
                }
            case PRODUCT:
                ItemManager itemManager = StaticContextAccessor.getBean(ItemManager.class);
                EdsItem edsItem = itemManager.get(objectId);
                if (edsItem != null) {
                    return StringUtils.defaultString(edsItem.getName(), item.getFieldStringValue());
                } else {
                    return item.getFieldStringValue();
                }
            case CUSTOMER:
                CrmAccountManager crmAccountManager = StaticContextAccessor.getBean(CrmAccountManager.class);
                EdsCrmAccount edsCrmAccount = crmAccountManager.get(objectId);
                if (edsCrmAccount != null) {
                    return StringUtils.defaultString(edsCrmAccount.getName(), item.getFieldStringValue());
                } else {
                    return item.getFieldStringValue();
                }
            case SUPPLIER:
                crmAccountManager = StaticContextAccessor.getBean(CrmAccountManager.class);
                edsCrmAccount = crmAccountManager.get(objectId);
                if (edsCrmAccount != null) {
                    return StringUtils.defaultString(edsCrmAccount.getName(), item.getFieldStringValue());
                } else {
                    return item.getFieldStringValue();
                }
            case LEAD:
                crmAccountManager = StaticContextAccessor.getBean(CrmAccountManager.class);
                edsCrmAccount = crmAccountManager.get(objectId);
                if (edsCrmAccount != null) {
                    return StringUtils.defaultString(edsCrmAccount.getName(), item.getFieldStringValue());
                } else {
                    return item.getFieldStringValue();
                }
            case OPPORTUNITY:
                OpportunityManager opportunityManager = StaticContextAccessor.getBean(OpportunityManager.class);
                EdsOpportunity edsOpportunity = opportunityManager.get(objectId);
                if (edsOpportunity != null) {
                    return StringUtils.defaultString(edsOpportunity.getName(), item.getFieldStringValue());
                } else {
                    return item.getFieldStringValue();
                }
            case TASK:
                TaskManager taskManager = StaticContextAccessor.getBean(TaskManager.class);
                EdsTask edsTask = taskManager.get(objectId);
                if (edsTask != null) {
                    return StringUtils.defaultString(edsTask.getNumber() + "->" + edsTask.getName(), item.getFieldStringValue());
                } else {
                    return item.getFieldStringValue();
                }
            case CASE:
                CaseManager caseManager = StaticContextAccessor.getBean(CaseManager.class);
                EdsCase edsCase = caseManager.get(objectId);
                if (edsCase != null) {
                    return StringUtils.defaultString(edsCase.getSubject(), item.getFieldStringValue());
                } else {
                    return item.getFieldStringValue();
                }
            case PRODUCT_CATEGORY:
                ProductCategoryManager itemCategoryManager = StaticContextAccessor.getBean(ProductCategoryManager.class);
                EdsProductCategory edsItemCategory = itemCategoryManager.get(objectId);
                if (edsItemCategory != null) {
                    return StringUtils.defaultString(edsItemCategory.getName(), item.getFieldStringValue());
                } else {
                    return item.getFieldStringValue();
                }
            case REFERENCE:
                ReferenceManager referenceManager = StaticContextAccessor.getBean(ReferenceManager.class);
                EdsReference edsReference = referenceManager.get(objectId);
                if (edsReference != null) {
                    return StringUtils.defaultString(edsReference.getName(), item.getFieldStringValue());
                } else {
                    return item.getFieldStringValue();
                }

            case CONTACT:
            case CANDIDATE:
                CrmContactManager crmContactManager = StaticContextAccessor.getBean(CrmContactManager.class);
                EdsCrmContact edsCrmContact = crmContactManager.get(objectId);
                if (edsCrmContact != null) {
                    return StringUtils.defaultString(edsCrmContact.getNumber() + "->" + edsCrmContact.getName(), item.getFieldStringValue());
                } else {
                    return item.getFieldStringValue();
                }
            case PAYMENT_METHOD:
                PaymentMethodManager paymentMethodManager = StaticContextAccessor.getBean(PaymentMethodManager.class);
                EdsPaymentMethod edsPaymentMethod = paymentMethodManager.get(objectId);
                if (edsPaymentMethod != null) {
                    return StringUtils.defaultString(edsPaymentMethod.getName(), item.getFieldStringValue());
                } else {
                    return item.getFieldStringValue();
                }
            case DEPARTMENT:
                DepartmentManager departmentManager = StaticContextAccessor.getBean(DepartmentManager.class);
                EdsDepartment edsDepartment = departmentManager.get(objectId);
                if (edsDepartment != null) {
                    return StringUtils.defaultString(edsDepartment.getName(), item.getFieldStringValue());
                } else {
                    return item.getFieldStringValue();
                }
            case POSITION:
                PositionManager positionManager = StaticContextAccessor.getBean(PositionManager.class);
                EdsPosition edsPosition = positionManager.get(objectId);
                if (edsPosition != null) {
                    return StringUtils.defaultString(edsPosition.getName(), item.getFieldStringValue());
                } else {
                    return item.getFieldStringValue();
                }
        }
        return item.getFieldStringValue();
    }

    public static String formatAutoNumber(String prefix, Integer number) {
        return prefix + codeFormat.format(number);
    }

    public static CustomFieldDto getCustomFieldDto(CompanyCustomFieldItem fieldItem) {
        DocumentsServiceLocal documentsServiceLocal = (DocumentsServiceLocal) ApplicationContextProvider.applicationContext.getBean("documentsService");
        CommonServiceLocal commonServiceLocal = (CommonServiceLocal) ApplicationContextProvider.applicationContext.getBean("commonService");

        CustomFieldDto dto = new CustomFieldDto(fieldItem.getObjectId(), fieldItem.getColumnCode(), fieldItem.getFieldName(), fieldItem.getDataType(), fieldItem.getUiType());
        dto.setAlias(fieldItem.getAliasName());

        switch (fieldItem.getDataType()) {
            case Constants.DATA_TYPE_DATE -> {
                if (fieldItem.getFieldDateNonConvertedValue() != null) {
                    dto.setValue(ServerUtils.getDateAsString(fieldItem.getFieldDateNonConvertedValue().getNonConvertedDate()));
                }
            }
            case Constants.DATA_TYPE_FILE_UPLOAD -> {
                if (StringUtils.isNotBlank(fieldItem.getFieldStringValue())) {
                    ArrayList<FileResource> resources = documentsServiceLocal.getFileResources(Constants.F_CUSTOM_FIELD_ITEM, Double.valueOf(fieldItem.getFieldStringValue()).intValue(), fieldItem.getObjectId());
                    dto.setValue(resources.stream().map(r -> new HashMap<String, Object>() {{
                        put("id", r.getObjectId());
                        put("name", r.getFileName());
                        put("url", r.getDownloadUrl());
                    }}).collect(Collectors.toList()));
                }
            }
            case Constants.DATA_TYPE_NUMBER -> {
                if (StringUtils.isNotBlank(fieldItem.getFieldStringValue())) {
                    dto.setValue(fieldItem.getFieldStringValue().split("[.]").length > 1 ? Double.valueOf(fieldItem.getFieldStringValue()) : Integer.valueOf(fieldItem.getFieldStringValue()));
                }
            }
            case Constants.DATA_TYPE_PROFILE_IMAGE -> {
                if (fieldItem.getProfielImageId() != null) {
                    dto.setValue(commonServiceLocal.getImageUrl(fieldItem.getProfielImageId()));
                }
            }
            default -> {
                if (fieldItem.getUiType().equals(Constants.TYPE_ENTITY_LOOKUP) ) {
                    if (fieldItem.getFieldStringValue() != null && !"".equals(fieldItem.getFieldStringValue()) &&  fieldItem.getQueryItems() != null) {
                        SelectItem item = Arrays.stream(fieldItem.getQueryItems()).filter(i -> i.getId().equals(Integer.parseInt(fieldItem.getFieldStringValue()))).findFirst().orElse(null);
                        if (item != null) {
                            dto.setValue(new ItemDto(item.getId(), item.getName()));
                        }
                    }
                } else if (fieldItem.getItem() != null) {
                    dto.setValue(new ItemDto(fieldItem.getItem().getId(), fieldItem.getItem().getName()));
                } else if (!CollectionUtils.isEmpty(fieldItem.getSelectItems())) {
                    dto.setValue(fieldItem.getSelectItems().stream().map(i -> new ItemDto(i.getId(), i.getName())).collect(Collectors.toList()));
                } else if (fieldItem.getSelectedId() != null) {
                    dto.setValue(new ItemDto(fieldItem.getSelectedId(), fieldItem.getFieldStringValue()));
                } else {
                    dto.setValue(fieldItem.getFieldStringValue());
                }
            }
        }
        return dto;
    }

    public static CompanyCustomFieldItem applyCustomFieldValue(CompanyCustomFieldItem customFieldItem, Object value) {
        Gson JSONUtil = new Gson();
        if (Constants.UI_TYPE_DATEPICKER.equals(customFieldItem.getUiType())) {
            customFieldItem.setFieldDateNonConvertedValue(value != null ? new DateNonConvertable(DateDeserializer.convertToDate(String.valueOf(value))) : null);
        } else if (Constants.UI_TYPE_DATEPICKER_TIME.equals(customFieldItem.getUiType())) {
            customFieldItem.setFieldDateNonConvertedValue(value != null ? new DateNonConvertable(DateDeserializer.convertToDate(String.valueOf(value))) : null);
        } else if (Constants.UI_TYPE_FILE_UPLOAD_WIDGET.equals(customFieldItem.getUiType()) || Constants.UI_TYPE_FILE_UPLOAD_ITEM.equals(customFieldItem.getUiType())) {
            if (value != null && value instanceof ArrayList) {
                String valueJSON = JSONUtil.toJson(value);
                List<ItemDto> items = JSONUtil.fromJson(valueJSON, new TypeToken<ArrayList<ItemDto>>() {
                }.getType());
                customFieldItem.setAttachments(items.stream().map(item -> {
                    FileItem fItem = new FileItem();
                    fItem.setId(item.getId());
                    fItem.setFileName(item.getName());
                    return customFieldItem;
                }).collect(Collectors.toList()).toArray(new FileItem[]{}));
            }
        } else if (Constants.UI_TYPE_PROFILE_IMAGE_WIDGET.equals(customFieldItem.getUiType())) {
            customFieldItem.setProfielImageId((Integer) value);
            customFieldItem.setFieldStringValue(String.valueOf(value));
        } else if (Constants.UI_TYPE_LOOKUP.equals(customFieldItem.getUiType()) || Constants.UI_TYPE_CURRENCY.equals(customFieldItem.getUiType())) {
            if (value != null && !(value instanceof String)) {
                String valueJSON = JSONUtil.toJson(value);
                ItemDto selectItem = JSONUtil.fromJson(valueJSON, ItemDto.class);
                customFieldItem.setSelectedId(selectItem.getId());
                customFieldItem.setFieldStringValue(selectItem.getName());
            } else {
                String valueStr = String.valueOf(value);
                customFieldItem.setFieldStringValue(valueStr);
                // Try to parse the value as an ID if it's a numeric string
                if (StringUtils.isNotBlank(valueStr) && valueStr.matches("\\d+")) {
                    try {
                        customFieldItem.setSelectedId(Integer.parseInt(valueStr));
                    } catch (NumberFormatException e) {
                        // Not a valid ID, keep as string value
                    }
                }
            }
        } else if (Constants.TYPE_ENTITY_MULTI_LOOKUP.equals(customFieldItem.getUiType()) || Constants.UI_TYPE_MULTI_LOOKUP.equals(customFieldItem.getUiType())) {
            if (value instanceof ArrayList) {
                String valueJSON = JSONUtil.toJson(value);
                ArrayList<ItemDto> items = JSONUtil.fromJson(valueJSON, new TypeToken<ArrayList<ItemDto>>() {
                }.getType());
                ArrayList<SelectItem> selectItems = (ArrayList<SelectItem>) items.stream().map(item -> new SelectItem(item.getId(), item.getName())).collect(Collectors.toList());
                customFieldItem.setSelectItems(selectItems);
            }
        } else if (Constants.TYPE_ENTITY_LOOKUP.equals(customFieldItem.getUiType()) || Constants.UI_TYPE_ENTITY_DROPDOWN.equals(customFieldItem.getUiType())) {
            if (value != null && value instanceof ItemDto) {
                String valueJSON = JSONUtil.toJson(value);
                ItemDto selectItem = JSONUtil.fromJson(valueJSON, ItemDto.class);
                if (customFieldItem.getQueryItems() != null) {
                    customFieldItem.setFieldStringValue(String.valueOf(Arrays.stream(customFieldItem.getQueryItems()).filter(cf -> cf.getId().equals(selectItem.getId()) || cf.getName().equals(selectItem.getName())).map(SelectItem::getId).findFirst().orElse(null)));
                }
            } else {
                String valueStr = String.valueOf(value);
                customFieldItem.setFieldStringValue(valueStr);
                // For entity lookup fields, also try to set selectedId if the value is numeric
                if (StringUtils.isNotBlank(valueStr) && valueStr.matches("\\d+")) {
                    try {
                        customFieldItem.setSelectedId(Integer.parseInt(valueStr));
                    } catch (NumberFormatException e) {
                        // Not a valid ID, keep as string value
                    }
                }
            }
        } else {
            customFieldItem.setFieldStringValue(value != null ? String.valueOf(value) : null);
        }
        return customFieldItem;
    }

    /**
     * Aim of the method is to fill object custom field data with provided custom field values from API or other data sources
     *
     * @param customFieldRequestList is a provided data from API request body or anywere
     * @param customFieldSettings    is a custom field config list which is added to the selected object
     * @param existingData           is a already saved data to the object
     * @return
     */
    public static ArrayList<CompanyCustomFieldItem> convertCustomFields(List<? extends CustomFieldRequest> customFieldRequestList, ArrayList<CompanyCustomFieldItem> customFieldSettings, EdsCustomFields existingData) {
        if (CollectionUtils.isEmpty(customFieldSettings)) {
            return null;
        } else if (CollectionUtils.isEmpty(customFieldRequestList)) {
            return new ArrayList<>(CustomFieldsUtils.setRPCCustomFieldItems(existingData, customFieldSettings));
        }
        List<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(existingData, customFieldSettings);
        Map<String, CompanyCustomFieldItem> customFieldsMap = customFieldItems.stream().collect(Collectors.toMap(CompanyCustomFieldItem::getAliasName, Function.identity(), (oldOne, newOne) -> oldOne));
        Map<String, CustomFieldRequest> customFieldsFromRequestMap = customFieldRequestList.stream().filter(cf -> cf.getValue() != null).collect(Collectors.toMap(CustomFieldRequest::getAlias, Function.identity(), (oldOne, newOne) -> oldOne));
        ArrayList<CompanyCustomFieldItem> result = new ArrayList<>();
        customFieldsMap.keySet().forEach(alise -> {
            CompanyCustomFieldItem customFieldItem = customFieldsMap.get(alise);
            CustomFieldRequest cfRequest = customFieldsFromRequestMap.get(alise);
            if (cfRequest != null) {
                customFieldItem = applyCustomFieldValue(customFieldItem, cfRequest.getValue());
            }
            result.add(customFieldItem);
        });

        return result;
    }


    public static ArrayList<CompanyCustomFieldItem> convertCustomFieldsCanidate(List<? extends CustomFieldRequest> customFieldRequestList, ArrayList<CompanyCustomFieldItem> customFieldSettings, EdsCustomFields existingData) {
        if (CollectionUtils.isEmpty(customFieldSettings)) {
            return null;
        } else if (CollectionUtils.isEmpty(customFieldRequestList)) {
            return new ArrayList<>(CustomFieldsUtils.setRPCCustomFieldItems(existingData, customFieldSettings));
        }
        List<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(existingData, customFieldSettings);
        Map<String, CompanyCustomFieldItem> customFieldsMap = customFieldItems.stream().collect(Collectors.toMap(CompanyCustomFieldItem::getAliasName, Function.identity(), (oldOne, newOne) -> oldOne));
        Map<String, CustomFieldRequest> customFieldsFromRequestMap = customFieldRequestList.stream().filter(cf -> cf.getValue() != null).collect(Collectors.toMap(CustomFieldRequest::getAlias, Function.identity(), (oldOne, newOne) -> oldOne));
        ArrayList<CompanyCustomFieldItem> result = new ArrayList<>();

        customFieldsMap.keySet().forEach(alias -> {
            CompanyCustomFieldItem customFieldItem = customFieldsMap.get(alias);
            CustomFieldRequest cfRequest = customFieldsFromRequestMap.get(alias);

            if (cfRequest != null) {
                String rawValue = (String) cfRequest.getValue();
                String valueToApply = rawValue;

                // Check if the value contains the separator, indicating a lookup field
                if (rawValue != null && rawValue.contains(SEPARATOR)) {
                    String[] parts = rawValue.split(SEPARATOR);

                    // Handle multi-select lookup: "value1-:-id1-:-value2-:-id2"
//                    if (parts.length > 2 && parts.length % 2 == 0) {
//                        List<String> textParts = new ArrayList<>();
//                        List<Integer> idParts = new ArrayList<>();
//                        for (int i = 0; i < parts.length; i += 2) {
//                            textParts.add(parts[i]);
//                            try {
//                                idParts.add(Integer.parseInt(parts[i + 1]));
//                            } catch (NumberFormatException e) {
//                                // Malformed ID in string, skipping this part
//                            }
//                        }
//                        // Assumption: A method exists to set multiple IDs.
//                        customFieldItem.setSelectedId(idParts);
//                        valueToApply = String.join(", ", textParts); // Create a clean display value
//
//                    }
                    // Handle single-select lookup: "value-:-id"
                     if (parts.length == 2) {
                        valueToApply = parts[0];
                        try {
                            // Assumption: A method exists to set a single ID.
                            customFieldItem.setSelectedId(Integer.parseInt(parts[1]));
                        } catch (NumberFormatException e) {
                            // Malformed ID in string
                        }
                    }
                }
                // The value passed to this method is now the clean, text-only part.
                customFieldItem = applyCustomFieldValue(customFieldItem, valueToApply);
            }
            result.add(customFieldItem);
        });

        return result;
    }

    public static void setValueByDataType(final CompanyCustomFieldItem customField, String value) {
        String dataType = customField != null ? customField.getDataType() : null;
        if (dataType != null && !dataType.isEmpty()) {
            if (CompanyCustomFieldItem.TEXT.equals(dataType)) {
                customField.setFieldStringValue(value);
            } else if (CompanyCustomFieldItem.NUMBER.equals(dataType) && value.matches(Constants.REGEX_REAL_NUMBERS_WITH_DOT)) {
                try {
                    customField.setFieldStringValue(Double.valueOf(value));
                } catch (NumberFormatException e) {
                    customField.setFieldStringValue(value);
                }
            } else if (CompanyCustomFieldItem.DATE.equals(dataType) && value.matches("(\\d\\d?)\\/(\\d\\d?)\\/(\\d\\d\\d\\d)")) {
                SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_PATTERN);
                try {
                    Date parsedDate = format.parse(value);
//                    customField.setFieldDateValue(user != null ? user.getServerDateByUserDate(parsedDate) : parsedDate);
                    customField.setFieldDateNonConvertedValue(parsedDate != null ? new DateNonConvertable(parsedDate) : null);
                } catch (ParseException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
