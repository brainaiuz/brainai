package com.edatasite.workforce.gwt.core.client.form.formbuild;

import com.edatasite.workforce.gwt.core.client.enums.ModuleEnum;
import com.edatasite.workforce.gwt.core.client.rpc.ConvertItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFormLocalization;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class CustomFormItem implements IsSerializable {

    private Integer objectId;
    private Integer propertyID;
    private String name;
    private String plural;
    private String shortName;
    private ModuleEnum module;
    private String type;
    private List<Integer> selectedRoleIds = new ArrayList<>();
    private List<SelectItem> roles;
    private String context;
    private SelectItem[] tableArray;
    private ConvertItem[] convertItems;
    private boolean isCustom;
    private String oldFormID;
    private LinkedHashMap<String, LinkedList<SelectItem>> section;
    private SelectItem container;
    private Integer containerItemId;
    private Integer quotaPerUser;
    private Integer quotaPerForm;
    private CustomFormRuleItem ruleItem;
    private int[] timer;
    private String welcomeMessage;
    private String endTimeMessage;
    private boolean isCopy;
    private boolean isQuizForm;
    private boolean isAnonymousForm;
    private String entity;
    private String relation;
    private CustomFormLocalization lName;
    private CustomFormLocalization lPlural;
    private CustomFormLocalization lShort;
    private HashMap<String, HashMap<Integer, SelectItem>> cfItemTableEntityMap = new HashMap<>();
    private String formId;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getPropertyID() {
        return propertyID;
    }

    public void setPropertyID(Integer propertyID) {
        this.propertyID = propertyID;
    }

    public CustomFormRuleItem getRuleItem() {
        return ruleItem;
    }

    public void setRuleItem(CustomFormRuleItem ruleItem) {
        this.ruleItem = ruleItem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlural() {
        return plural;
    }

    public void setPlural(String plural) {
        this.plural = plural;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public ModuleEnum getModule() {
        return module;
    }

    public void setModule(ModuleEnum module) {
        this.module = module;
    }

    public List<Integer> getSelectedRoleIds() {
        return selectedRoleIds;
    }

    public void setSelectedRoleIds(List<Integer> selectedRoleIds) {
        this.selectedRoleIds = selectedRoleIds;
    }

    public List<SelectItem> getRoles() {
        return roles;
    }

    public void setRoles(List<SelectItem> roles) {
        this.roles = roles;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public SelectItem[] getTableArray() {
        return tableArray;
    }

    public void setTableArray(SelectItem[] tableArray) {
        this.tableArray = tableArray;
    }

    public boolean isCustom() {
        return isCustom;
    }

    public void setCustom(boolean custom) {
        isCustom = custom;
    }

    public String getOldFormID() {
        return this.oldFormID;
    }

    public void setOldFormID(final String oldFormID) {
        this.oldFormID = oldFormID;
    }

    public ConvertItem[] getConvertItems() {
        return this.convertItems;
    }

    public void setConvertItems(final ConvertItem[] convertItems) {
        this.convertItems = convertItems;
    }

    public LinkedHashMap<String, LinkedList<SelectItem>> getSection() {
        return this.section;
    }

    public void setSection(final LinkedHashMap<String, LinkedList<SelectItem>> section) {
        this.section = section;
    }

    public SelectItem getContainer() {
        return this.container;
    }

    public void setContainer(final SelectItem container) {
        this.container = container;
    }

    public Integer getContainerItemId() {
        return this.containerItemId;
    }

    public void setContainerItemId(final Integer containerItemId) {
        this.containerItemId = containerItemId;
    }

    public String getType() {
        return this.type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public Integer getQuotaPerUser() {
        return quotaPerUser;
    }

    public void setQuotaPerUser(Integer quotaPerUser) {
        this.quotaPerUser = quotaPerUser;
    }

    public Integer getQuotaPerForm() {
        return quotaPerForm;
    }

    public void setQuotaPerForm(Integer quotaPerForm) {
        this.quotaPerForm = quotaPerForm;
    }

    public int[] getTimer() {
        return timer;
    }

    public void setTimer(int[] timer) {
        this.timer = timer;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public void setWelcomeMessage(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    public String getEndTimeMessage() {
        return endTimeMessage;
    }

    public void setEndTimeMessage(String endTimeMessage) {
        this.endTimeMessage = endTimeMessage;
    }

    public boolean isCopy() {
        return isCopy;
    }

    public void setCopy(boolean copy) {
        isCopy = copy;
    }

    public boolean isQuizForm() {
        return isQuizForm;
    }

    public void setQuizForm(boolean quizForm) {
        isQuizForm = quizForm;
    }

    public boolean isAnonymousForm() {
        return isAnonymousForm;
    }

    public void setAnonymousForm(boolean anonymousForm) {
        isAnonymousForm = anonymousForm;
    }

    public CustomFormLocalization getlName() {
        return lName;
    }

    public void setlName(CustomFormLocalization lName) {
        this.lName = lName;
    }

    public CustomFormLocalization getlPlural() {
        return lPlural;
    }

    public void setlPlural(CustomFormLocalization lPlural) {
        this.lPlural = lPlural;
    }

    public CustomFormLocalization getlShort() {
        return lShort;
    }

    public void setlShort(CustomFormLocalization lShort) {
        this.lShort = lShort;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public HashMap<String, HashMap<Integer, SelectItem>> getCfItemTableEntityMap() {
        return cfItemTableEntityMap;
    }

    public void setCfItemTableEntityMap(HashMap<String, HashMap<Integer, SelectItem>> cfItemTableEntityMap) {
        this.cfItemTableEntityMap = cfItemTableEntityMap;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }
}
