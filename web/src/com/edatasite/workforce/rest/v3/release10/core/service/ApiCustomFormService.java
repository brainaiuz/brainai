package com.edatasite.workforce.rest.v3.release10.core.service;

import com.edatasite.workforce.core.domain.EdsCompanyCustomFieldsSettings;
import com.edatasite.workforce.core.domain.EdsCustomQuizFormScore;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.customform.EdsCustomForm;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormItems;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormSection;
import com.edatasite.workforce.gwt.core.client.form.CustomizeFormItem;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFormLocalization;
import com.edatasite.workforce.gwt.core.client.rpc.FormItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelForm;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.db.CompanyCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.CustomFormItemManager;
import com.edatasite.workforce.gwt.core.server.db.CustomFormManager;
import com.edatasite.workforce.gwt.core.server.db.CustomFormSectionManager;
import com.edatasite.workforce.gwt.core.server.db.CustomQuizFormManager;
import com.edatasite.workforce.gwt.core.server.db.ModelManager;
import com.edatasite.workforce.gwt.core.server.db.UserEmailSettingsManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.rest.base.helpers.ConvertUtils;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.to.CFItemExistanceDto;
import com.edatasite.workforce.rest.v3.release10.core.to.CheckExistanceRequestDto;
import com.edatasite.workforce.rest.v3.release10.core.to.CustomFieldTo;
import com.edatasite.workforce.rest.v3.release10.core.to.CustomFormDto;
import com.edatasite.workforce.rest.v3.release10.core.to.DynamicDto;
import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;
import com.edatasite.workforce.rest.v3.release10.core.to.LocaleDto;
import com.edatasite.workforce.rest.v3.release10.core.to.QuizScoreDto;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.rpc.RelationItem.TYPE_CUSTOM_FORM_ITEM;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.GENERAL_ERROR_MESSAGE;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.NOT_FOUND;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.REQUIRED;

@Service
public class ApiCustomFormService implements Constants {
    private final CommonService commonService;
    private final CustomFormManager customFormManager;
    private final AllInOneService allInOneService;
    private final CustomFormItemManager customFormItemManager;
    private final ModelManager modelManager;
    private final CustomQuizFormManager customQuizFormManager;
    private final CompanyCustomFieldsManager companyCFSettingsManager;
    private final UserEmailSettingsManager userEmailSettingsManager;
    private final CustomFormSectionManager customFormSectionManager;
    private final CustomPredefinedValueService customPredefinedValueService;
    private final Map<String, List<String>> fields = new HashMap<>();

    @Autowired
    public ApiCustomFormService(CommonService commonService, CustomFormManager customFormManager, AllInOneService allInOneService, CustomFormItemManager customFormItemManager, ModelManager modelManager, CustomQuizFormManager customQuizFormManager, CompanyCustomFieldsManager companyCFSettingsManager, UserEmailSettingsManager userEmailSettingsManager, CustomPredefinedValueService customPredefinedValueService, CustomFormSectionManager customFormSectionManager) {
        this.commonService = commonService;
        this.customFormManager = customFormManager;
        this.allInOneService = allInOneService;
        this.customFormItemManager = customFormItemManager;
        this.modelManager = modelManager;
        this.customQuizFormManager = customQuizFormManager;
        this.companyCFSettingsManager = companyCFSettingsManager;
        this.userEmailSettingsManager = userEmailSettingsManager;
        this.customPredefinedValueService = customPredefinedValueService;
        this.customFormSectionManager = customFormSectionManager;
        initFormFields();
    }

    private void initFormFields() {
        fields.put(LayoutRPC.TASK_MAX_FORM, Arrays.asList(CustomFormConstants.TASK.PROJECT, CustomFormConstants.TASK.CLIENT, CustomFormConstants.NUMBER, CustomFormConstants.NAME, CustomFormConstants.DESCRIPTION, CustomFormConstants.START_DATE, CustomFormConstants.DUE_DATE, CustomFormConstants.WORKFLOW_DATE, CustomFormConstants.WORKFLOW_TIME_BASED, CustomFormConstants.ASSIGNEE, CustomFormConstants.TASK.BILLIBLE, CustomFormConstants.PRIORITY, CustomFormConstants.TASK.STOPWATCH, CustomFormConstants.TASK.TIME_SPENT, CustomFormConstants.TASK.TASK_AMOUNT, CustomFormConstants.ATTACHMENTS, CustomFormConstants.TASK.PARENT_WORKSTREAM, CustomFormConstants.TASK.PREDECESSOR_TASK, CustomFormConstants.TASK.SUCCESSOR_TASK, CustomFormConstants.TASK.DUE_DATE_REMINDER, CustomFormConstants.TASK.RECURRENING, CustomFormConstants.TASK.TASK_NOTE, CustomFormConstants.STATUS, CustomFormConstants.ASSIGNEES));
        fields.put(LayoutRPC.PROJECT_FORM, Arrays.asList(CustomFormConstants.NUMBER, CustomFormConstants.NAME, CustomFormConstants.DESCRIPTION, CustomFormConstants.START_DATE, CustomFormConstants.DUE_DATE, CustomFormConstants.STATUS, CustomFormConstants.PARENT, CustomFormConstants.PROJECT.LOCATION, CustomFormConstants.PROJECT.MANAGER, CustomFormConstants.PROJECT.PROJECT_NOTE, CustomFormConstants.ATTACHMENTS, CustomFormConstants.PROJECT.INVOLVED_EMPLOYEE, CustomFormConstants.PROJECT.BACKUP_MANAGER));
        fields.put(LayoutRPC.LEAVE_REQUEST_FORM, Arrays.asList(CustomFormConstants.EMPLOYEES, CustomFormConstants.REASON, CustomFormConstants.DATE_PERIOD, CustomFormConstants.DESCRIPTION, CustomFormConstants.APPROVER, CustomFormConstants.TAKE_LIVE_TYPE, CustomFormConstants.TYPE));
        fields.put(LayoutRPC.OPPORTUNITY_FORM, Arrays.asList(CustomFormConstants.CRM_OPPORTUNITY_STAGE, CustomFormConstants.CRM_NOTE, CustomFormConstants.CRM_OPPORTUNITY_ASSIGNEE, CustomFormConstants.CRM_OPPORTUNITY_NAME, CustomFormConstants.CRM_OPPORTUNITY_LEAD_SOURCE, CustomFormConstants.CRM_OPPORTUNITY_CLOSING_DATE, CustomFormConstants.CRM_OPPORTUNITY_AMOUNT, CustomFormConstants.CRM_OPPORTUNITY_ATTACHMENTS, CustomFormConstants.CURRENCY, CustomFormConstants.CRM_OPPORTUNITY_CONTACT_NAME, CustomFormConstants.CRM_OPPORTUNITY_CONTACT_PRIMARY_PHONE, CustomFormConstants.CRM_OPPORTUNITY_CONTACT_PRIMARY_EMAIL, CustomFormConstants.CRM_OPPORTUNITY_ATTACHMENTS, CustomFormConstants.CRM_OPPORTUNITY_CONTACT_NAME));
        fields.put(LayoutRPC.LEAD_FORM, Arrays.asList(CustomFormConstants.FIRST_NAME, CustomFormConstants.LAST_NAME, CustomFormConstants.MIDDLE_NAME, CustomFormConstants.OTHER_NAME, CustomFormConstants.BIRTH_DAY, CustomFormConstants.CRM_ACCOUNT_NAME, CustomFormConstants.CRM_ACCOUNT_TYPE, CustomFormConstants.JOB_TITLE, CustomFormConstants.DEPARTMENT, CustomFormConstants.REF_IND_NUMBER, CustomFormConstants.ASSETS, CustomFormConstants.CRM_ACCOUNT_INDUSTRY, CustomFormConstants.EMAIL, CustomFormConstants.TELEGRAM, CustomFormConstants.PHONE, CustomFormConstants.IM_ADDRESS, CustomFormConstants.WEB_ADDRESS, CustomFormConstants.RELATIONSHIP, CustomFormConstants.ADDRESS, CustomFormConstants.PARENT_ADDRESSES, CustomFormConstants.CATEGORY, CustomFormConstants.REPORTS_TO, CustomFormConstants.OWNER, CustomFormConstants.CRM_CAMPAIGN_NAME, CustomFormConstants.EMAIL_OPT_OUT, CustomFormConstants.SUBSCRIPTION_LIST, CustomFormConstants.CRM_ACTIVITIES, CustomFormConstants.CRM_NOTE, CustomFormConstants.ATTACHMENTS, CustomFormConstants.ATTACHMENTS_MINI,CustomFormConstants.ASSIGNEE,CustomFormConstants.LEAD_SOURCE));
        fields.put(LayoutRPC.CONTACT_FORM, Arrays.asList(CustomFormConstants.FIRST_NAME, CustomFormConstants.LAST_NAME, CustomFormConstants.MIDDLE_NAME, CustomFormConstants.PHONE, CustomFormConstants.COMPANY, CustomFormConstants.JOB_TITLE, CustomFormConstants.EMAIL, CustomFormConstants.CRM_NOTE, CustomFormConstants.CONTACT_INFORMATION, CustomFormConstants.ATTACHMENTS, CustomFormConstants.CATEGORY, CustomFormConstants.CRM_ACCOUNT_TYPE, CustomFormConstants.PROFILE_PICTURE, CustomFormConstants.CRM_ACCOUNT_NAME, CustomFormConstants.CRM_ACCOUNT_INDUSTRY, CustomFormConstants.CRM_CAMPAIGN_NAME));
        fields.put(LayoutRPC.ACTIVITY_FORM, Arrays.asList(CustomFormConstants.SUBJECT, CustomFormConstants.CUSTOM_HTML_TEMPLATE, CustomFormConstants.WHEN, CustomFormConstants.DESCRIPTION, CustomFormConstants.SHARED_WITH, CustomFormConstants.GUESTS, CustomFormConstants.RECURRING_WIDGET, CustomFormConstants.ENABLE_REMINDER, CustomFormConstants.CALL_TYPE, CustomFormConstants.CALL_DETAILS, CustomFormConstants.CALL_CLONE));
    }

    public ListResultTO<CustomFormDto> getCustomFormItemsList(ListingFilterParameter fp) {
        ListResult<FormItems> items = commonService.getCustomFormItems(fp);

        ListResultTO<CustomFormDto> result = new ListResultTO<>();
        ArrayList<CustomFormDto> dtos = new ArrayList<>();
        if (items != null && items.getList() != null) {
            items.getList().forEach(i ->
                    dtos.add(ConvertUtils.toDto(i, allInOneService.getNotes(i.getObjectID(), TYPE_CUSTOM_FORM_ITEM), null)));
        }
        result.setItems(dtos);
        result.setTotalNumber(dtos.size());
        return result;
    }

    @Transactional(readOnly = true)
    public CustomFormDto getCustomFormItemById(IdCode dto) throws RestException {
        EdsCustomFormItems items = getItem(dto.getId(), dto.getObjectKey());
        FormItems customFormItem = commonService.getCustomFormItem(items.getObjectID(), items.getCustomForm().getObjectID(), items.getCustomForm().getFormID(), false, null, null, null, null);
        ArrayList<HistoryListItem> notes = allInOneService.getNotes(items.getObjectID(), TYPE_CUSTOM_FORM_ITEM);
        EdsUser currentUser = modelManager.getUser();
        return ConvertUtils.toDto(customFormItem, notes, currentUser);
    }

    @Transactional
    public IdCode save(CustomFormDto dto, boolean isNew) throws RestException {
        FormItems item;
        EdsCustomFormItems edsCustomFormItems = null;
        if (!isNew && !dto.isNew()) {
            EdsCustomFormItems items = getItem(dto.getId(), dto.getObjectKey());
            item = commonService.getCustomFormItem(items.getObjectID(), items.getCustomForm().getObjectID(), items.getCustomForm().getFormID(), false, null, null, null, null);
        } else {
            item = new FormItems();
        }
        EdsCustomForm form = null;
        if (dto.getForm().getId() != null) {
            form = customFormManager.get(dto.getForm().getId());
        }
        if (dto.getForm().getCode() != null && form == null) {
            form = customFormManager.findByFormID(dto.getForm().getCode());
        }
        if (form == null) {
            throw new RestException("Form is not found", "Form is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        item.setFormID(form.getFormID());
        item.setAttempt(dto.getAttempt());
        item.setDurationTime(dto.getDuration());
        item.setRelationId(dto.getRelationId());
        item.setRelationObjectKey(dto.getRelationObjectKey());
        item.setRelationType(dto.getRelationType());

        item.setCustomFieldItems(CustomFieldsUtils.convertCustomFields(dto.getCustomFields(), commonService.getCompanyCategoryCustomFields(form.getObjectID()),
                !isNew ? edsCustomFormItems.getFormCustomFields() : null));
        Integer id = commonService.saveCustomFormItem(item);
        EdsCustomFormItems items = customFormItemManager.get(id);
        return new IdCode(id, null, items.getObjectKey());
    }

    @Transactional
    public IdCode patchUpdate(CustomFormDto dto) throws RestException {
        EdsCustomFormItems edsCustomFormItems = getItem(dto.getId(), dto.getObjectKey());
        FormItems item = commonService.getCustomFormItem(edsCustomFormItems.getObjectID(), edsCustomFormItems.getCustomForm().getObjectID(), edsCustomFormItems.getCustomForm().getFormID(), false, null, null, null, null);
        if (edsCustomFormItems.getCustomForm().getTimer() != null) {
            String[] estimatedTime = edsCustomFormItems.getCustomForm().getTimer().split(",");
            long duration = (Long.parseLong(estimatedTime[0]) * 60 * 60 * 1000) + (Long.parseLong(estimatedTime[1]) * 60 * 1000);
            if (new Date().getTime() - edsCustomFormItems.getAuditInfo().getCreationDate().getTime() > duration) {
                item.setStatusCode("TIME_IS_UP");
                item.setDurationTime(getDuration(edsCustomFormItems.getAuditInfo().getCreationDate().getTime(), edsCustomFormItems.getAuditInfo().getCreationDate().getTime() + duration));
                commonService.saveCustomFormItem(item);
                return new IdCode(edsCustomFormItems.getObjectID(), "TIME_IS_UP", edsCustomFormItems.getObjectKey());
            }
        }
        Optional.ofNullable(dto.getCustomFields()).ifPresent(cfs -> {
            ArrayList<CompanyCustomFieldItem> customFields = CustomFieldsUtils.convertCustomFields(cfs, commonService.getCompanyCategoryCustomFields(customFormManager.findByFormID(item.getFormID()).getObjectID()),
                    edsCustomFormItems.getFormCustomFields());
            if (item.getCustomFieldItems() != null) {
                customFields.forEach(cf -> {
                    item.getCustomFieldItems().removeIf(cfItem -> cf.getObjectId() != null && cf.getObjectId().equals(cfItem.getObjectId()));
                });
                item.getCustomFieldItems().addAll(customFields);
            } else {
                item.setCustomFieldItems(customFields);
            }
        });
        item.setDurationTime(getDuration(edsCustomFormItems.getAuditInfo().getCreationDate().getTime(), new Date().getTime()));
        commonService.saveCustomFormItem(item);
        return new IdCode(edsCustomFormItems.getObjectID(), null, edsCustomFormItems.getObjectKey());
    }

    public CFItemExistanceDto checkExistance(CheckExistanceRequestDto dto) {
        CFItemExistanceDto result = new CFItemExistanceDto();
        EdsCustomFormItems items = customFormItemManager.findByRelation(dto.getFormId(), dto.getRelationType(), dto.getRelationId(), dto.getRelationObjectKey());
        if (items != null) {
            result.setExists(true);
            CustomFormDto item = new CustomFormDto();

            if (items.getDurationTime() != null) {
                String[] duration = items.getDurationTime().split(":");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(items.getAuditInfo().getCreationDate());

                calendar.add(Calendar.HOUR_OF_DAY, Integer.valueOf(duration[0]));
                calendar.add(Calendar.MINUTE, Integer.valueOf(duration[1]));
                calendar.add(Calendar.SECOND, Integer.valueOf(duration[2]));

                item.setEndAt(modelManager.getUser().getUserDate(calendar.getTime()));
            }

            EdsCustomQuizFormScore score = customQuizFormManager.getQuizFormScore(dto.getFormId(), items.getObjectID());
            item.setScore(score != null ? score.getTotalScore() : BigDecimal.ZERO);
            result.setItem(item);
        }
        return result;
    }

    public void saveScores(CustomFieldTo fieldTo) throws RestException {
        String aliasName = fieldTo.getAliasName();
        String formId = fieldTo.getCustomFormId();
        CompanyCustomFieldItem fieldItem = commonService.getCustomFieldByAlias(ViewName.CustomFormItems.name(), aliasName);
        boolean item = commonService.customFormIsQuizForm(formId);
        if (!item) {
            throw new RestException("This custom form is not Quiz form ", "Bad Request", NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
        if (!(UI_TYPE_DROPDOWN.equals(fieldItem.getUiType())
                || UI_TYPE_CHECKBOX.equals(fieldItem.getUiType())
                || UI_TYPE_RADIOBUTTON.equals(fieldItem.getUiType()))) {
            throw new RestException("you can not add score value to this custom field ", "Bad Request", NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
        List<QuizScoreDto> map = fieldTo.getCustomFieldScoreValues();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(map);
        } catch (Exception ex) {
            jsonObject = new JSONObject();
        }
        fieldItem.setQuizFormScoreValues(jsonObject.toString());
        commonService.saveCustomFields(fieldItem, false);
    }

    public List<CustomFieldTo> getScores(DynamicDto dto) throws RestException {
        String customFormId = Optional.of(dto)
                .map(DynamicDto::getProperties)
                .map(d -> d.get("customFormId"))
                .map(d -> ((String) d))
                .orElseThrow(() -> new RestException(GENERAL_ERROR_MESSAGE, "data is required", REQUIRED, HttpStatus.BAD_REQUEST));

        if (StringUtils.isEmpty(customFormId)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "formId is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        boolean isQuizForm = commonService.customFormIsQuizForm(customFormId);
        if (!isQuizForm) {
            throw new RestException("This custom form is not Quiz form ", "Bad Request", NOT_FOUND, HttpStatus.BAD_REQUEST);
        }

        EdsCustomForm customForm = Optional.ofNullable(customFormManager.findByFormID(customFormId))
                .orElseThrow(() -> new RestException("Custom Form not found", "Bad Request", NOT_FOUND, HttpStatus.BAD_REQUEST));
        List<CustomFieldTo> result = new ArrayList<>();
        ArrayList<CompanyCustomFieldItem> itemList = commonService.getCompanyCategoryCustomFields(customForm.getObjectID());
        for (CompanyCustomFieldItem item : itemList) {
            CustomFieldTo fieldTo = new CustomFieldTo();
            fieldTo.setAliasName(item.getAliasName());
            fieldTo.setCustomFormId(customFormId);
            fieldTo.setObjectId(item.getObjectId());
            if (!(UI_TYPE_DROPDOWN.equals(item.getUiType()) ||
                    UI_TYPE_CHECKBOX.equals(item.getUiType()) ||
                    UI_TYPE_RADIOBUTTON.equals(item.getUiType()))) {
                continue;
            }
            if (item.getLocalization() != null) {
                fieldTo.setLocale(new LocaleDto(item.getLocalization().getRussianName(), item.getLocalization().getEnglishName(), item.getLocalization().getUzbekName(), item.getLocalization().getArabicName()));
            }
            fieldTo.setCustomFieldScoreValues(getScoreValuesMap(item.getQuizFormScoreValues(), item.getLocalization() != null && item.getLocalization().getChildren() != null ? item.getLocalization().getChildren().stream().collect(Collectors.toMap(CustomFormLocalization::getDefaultName, l -> l)) : null));
            result.add(fieldTo);
        }
        return result;
    }

    private List<QuizScoreDto> getScoreValuesMap(String quizStringValues, Map<String, CustomFormLocalization> localeMap) {
        List<QuizScoreDto> scores = new ArrayList<>();
        if (quizStringValues != null) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject = new JSONObject(quizStringValues);
                for (Iterator it = jsonObject.keys(); it.hasNext(); ) {
                    String key = it.next().toString();
                    QuizScoreDto score = new QuizScoreDto();
                    score.setName(key);
                    score.setScore(jsonObject.getDouble(key));
                    CustomFormLocalization locale = localeMap.get(key);
                    if (locale != null) {
                        score.setLocale(new LocaleDto(locale.getRussianName(), locale.getEnglishName(), locale.getUzbekName(), locale.getArabicName()));
                    }
                    scores.add(score);
                }
            } catch (Exception ex) {
                ex.getStackTrace();
            }
        }
        return scores;
    }

    private EdsCustomFormItems getItem(Integer id, String objectKey) throws RestException {
        return Optional.ofNullable(id)
                .map(customFormItemManager::get)
                .or(() -> Optional.ofNullable(customFormItemManager.getByObjectKey(objectKey)))
                .orElseThrow(() -> new RestException(GENERAL_ERROR_MESSAGE, "Custom form item with this id or object key is not found", NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    private String getDuration(long startedAt, long finishedAt) {
        long totalTime = finishedAt - startedAt;
        int hh, mm, ss;
        hh = (int) ((totalTime / 1000) / 3600);
        mm = (int) (((totalTime / 1000) / 60) % 60);
        ss = (int) ((totalTime / 1000) % 60);
        return hh + ":" + mm + ":" + ss;
    }

    public List<List<CustomizeFormItem>> getFormProperties(String formId) {
        ModelForm modelForm = allInOneService.getModelForm(formId);

        List<CustomizeFormItem> customizeFormItems = modelForm.getColumnMap().values().stream()
                .map(HashMap::values)
                .flatMap(Collection::stream)
                .flatMap(Collection::stream)
                .toList();

        LinkedList<CustomizeFormItem> requiredList = customizeFormItems.stream()
                .filter(ApiCustomFormService::isRequired)
                .collect(Collectors.toCollection(LinkedList::new));

        LinkedList<CustomizeFormItem> optionalList = customizeFormItems.stream()
                .filter(ApiCustomFormService::isOptional)
                .collect(Collectors.toCollection(LinkedList::new));

        List<EdsCompanyCustomFieldsSettings> companyCustomFieldsSettings = companyCFSettingsManager.getCompanyCustomFieldsByEntityName(modelForm.getViewName());
        String userLocale = userEmailSettingsManager.getUserSettings(userEmailSettingsManager.getUser()).getInternationalization();
        final List<EdsCustomFormSection> sectionList = this.customFormSectionManager.getSections(formId);
        Map<String, EdsCustomFormSection> sectionMap = sectionList.stream()
                .collect(Collectors.toMap(EdsCustomFormSection::getSection, Function.identity()));

        List<List<CustomizeFormItem>> list = new ArrayList<>();
        list.add(sortFields(requiredList, companyCustomFieldsSettings, userLocale, sectionMap));
        list.add(sortFields(optionalList, companyCustomFieldsSettings, userLocale, sectionMap));
        return list;
    }

    private static boolean isRequired(CustomizeFormItem customizeFormItem) {
        if (isRequiredCustom(customizeFormItem)) return true;
        boolean isCustomRequired = customizeFormItem.isCustomField() && customizeFormItem.isSystemMandatory();
        boolean isSystemRequired = customizeFormItem.getFormProperty() != null && customizeFormItem.getFormProperty().isRequired();
        return ((!isOptionalCustom(customizeFormItem)) && (isCustomRequired || isSystemRequired));
    }

    private static boolean isOptional(CustomizeFormItem customizeFormItem) {
        return !isRequired(customizeFormItem);
    }

    private static boolean isRequiredCustom(CustomizeFormItem customizeFormItem) {
        if (customizeFormItem.getFormID().equals(LayoutRPC.OPPORTUNITY_FORM)
                && customizeFormItem.getName().equals(CustomFormConstants.CRM_OPPORTUNITY_ASSIGNEE)) {
            customizeFormItem.setSystemMandatory(true);
            if (customizeFormItem.getFormProperty() != null) {
                customizeFormItem.getFormProperty().setRequired(true);
                customizeFormItem.getFormProperty().setSystemRequired(true);
            }
            return true;
        }
        if (customizeFormItem.getFormID().equals(LayoutRPC.OPPORTUNITY_FORM)
                && customizeFormItem.getName().equals(CustomFormConstants.CRM_OPPORTUNITY_ACCOUNT_NAME)) {
            customizeFormItem.setSystemMandatory(true);
            if (customizeFormItem.getFormProperty() != null) {
                customizeFormItem.getFormProperty().setRequired(true);
                customizeFormItem.getFormProperty().setSystemRequired(true);
            }
            return true;
        }
        if (customizeFormItem.getFormID().equals(LayoutRPC.TASK_MAX_FORM)
                && customizeFormItem.getName().equals(CustomFormConstants.TASK.PROJECT)) {
            customizeFormItem.setSystemMandatory(true);
            if (customizeFormItem.getFormProperty() != null) {
                customizeFormItem.getFormProperty().setRequired(true);
                customizeFormItem.getFormProperty().setSystemRequired(true);
            }
            return true;
        }
        return false;
    }


    private static boolean isOptionalCustom(CustomizeFormItem customizeFormItem) {
        if (customizeFormItem.getFormID().equals(LayoutRPC.TASK_MAX_FORM)
                && customizeFormItem.getName().equals(CustomFormConstants.ASSIGNEE)) {
            customizeFormItem.setSystemMandatory(false);
            if (customizeFormItem.getFormProperty() != null) {
                customizeFormItem.getFormProperty().setRequired(false);
                customizeFormItem.getFormProperty().setSystemRequired(false);
            }
            return true;
        }
        if (customizeFormItem.getFormID().equals(LayoutRPC.TASK_MAX_FORM)
                && customizeFormItem.getName().equals(CustomFormConstants.TASK.TASK_NOTE)) {
            customizeFormItem.setSystemMandatory(false);
            if (customizeFormItem.getFormProperty() != null) {
                customizeFormItem.getFormProperty().setRequired(false);
                customizeFormItem.getFormProperty().setSystemRequired(false);
            }
            return true;
        }
        return false;

    }

    private List<CustomizeFormItem> sortFields(LinkedList<CustomizeFormItem> fields, List<EdsCompanyCustomFieldsSettings> companyCustomFieldsSettings, String userLocale,  Map<String, EdsCustomFormSection> sectionMap) {
        List<CustomizeFormItem> toSort = new ArrayList<>(fields);
        toSort.sort(Comparator
                .comparingInt((CustomizeFormItem e) -> {
                    EdsCustomFormSection section = sectionMap.get(e.getSection());
                    return section != null ? section.getSorder() : Integer.MAX_VALUE;
                })
                .thenComparingInt(CustomizeFormItem::getForder)
        );
        List<CustomizeFormItem> list = new ArrayList<>();
        for (CustomizeFormItem e : toSort) {
            if (!e.isActive() && !(e.getFormID().equals("TASK_MAX_FORM") && "PROJECT".equals(e.getName()))) {
                continue;
            }
            Optional<FormProperty> formPropertyOptional = companyCustomFieldsSettings.stream()
                    .filter(c -> Objects.equals(e.getName(), c.getColumnCode()))
                    .map(ApiCustomFormService::getFormProperty)
                    .peek(formProperty -> formProperty.setLang(userLocale))
                    .peek(formProperty -> formProperty.setSystemRequired(e.isSystemMandatory()))
                    .findFirst();
            formPropertyOptional.ifPresent(e::setFormProperty);

            Optional.ofNullable(e.getFormProperty())
                    .map(ApiCustomFormService::getFieldLabel)
                    .ifPresent(e::setLabel);

            if (e.getFormProperty() != null) {
                e.getFormProperty().setAllRoles(new ArrayList<>());
            }
            if (e.getFormProperty() != null && e.getFormProperty().getPredefinedValues() == null) {
                SelectItem[] systemPredefinedValues = customPredefinedValueService.predefinedValue(e.getName(), e.getFormID());
                e.getFormProperty().setSystemPredefinedValues(systemPredefinedValues);
            }
            e.setSaveVisible(isSaveVisible(e));
            if ("LOOKUP".equals(e.getUiType())) {
                e.setDefaultValue(formatDefaultValue(e.getDefaultValue()));
            }
            list.add(e);
        }
        return list;
    }

    private String formatDefaultValue(String defaultValue) {
        if (StringUtils.isEmpty(defaultValue))
            return defaultValue;
        String[] parts = defaultValue.split("#_#");
        return parts.length > 1 ? parts[1] : defaultValue;
    }

    private boolean isSaveVisible(CustomizeFormItem e) {
        boolean customSaveVisible = this.fields.containsKey(e.getFormID()) && this.fields.get(e.getFormID()).contains(e.getName());
        return isRequired(e) || e.isCustomField() || customSaveVisible;
    }

    private static String getFieldLabel(FormProperty formProperty) {
        return switch (formProperty.getLang()) {
            case "uz" -> formProperty.getTitleUz();
            case "en" -> formProperty.getTitleEn();
            case "ru" -> formProperty.getTitleRu();
            case "ar" -> formProperty.getTitleAr();
            default -> formProperty.getTitle();
        };
    }

    private static FormProperty getFormProperty(EdsCompanyCustomFieldsSettings companyCustomField) {
        FormProperty formProperty = new FormProperty();
        formProperty.setAliasName(companyCustomField.getAliasName());
        Optional.ofNullable(companyCustomField.getRequired())
                .ifPresent(formProperty::setRequired);
        Optional.ofNullable(companyCustomField.getDisabled())
                .ifPresent(formProperty::setDisabled);
        formProperty.setPredefinedValues(companyCustomField.getPredefinedValues());
        formProperty.setLookUpType(companyCustomField.getLookUpType());
        formProperty.setQuery(companyCustomField.getQuery());
        if (companyCustomField.getCustomFormlocalization() != null) {
            formProperty.setTitle(companyCustomField.getCustomFormlocalization().getDefaultName());
            formProperty.setTitleUz(companyCustomField.getCustomFormlocalization().getUzbekName());
            formProperty.setTitleEn(companyCustomField.getCustomFormlocalization().getEnglishName());
            formProperty.setTitleRu(companyCustomField.getCustomFormlocalization().getRussianName());
            formProperty.setTitleAr(companyCustomField.getCustomFormlocalization().getArabicName());
        }
        return formProperty;
    }


}
