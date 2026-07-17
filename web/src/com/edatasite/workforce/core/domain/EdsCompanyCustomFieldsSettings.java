package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormLocalization;
import com.edatasite.workforce.core.domain.documents.EdsAuditInfo;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.PredefinedValueItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * User: Ilxom Lutfullaev
 * Date: Mar 24, 2010
 * Time: 12:22:18 PM
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "companyCustomFieldsSettings")
@Where(clause = "(deleted is null or deleted = 'false')")
public class EdsCompanyCustomFieldsSettings extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "entityname")
    private String entityName;

    @Column(name = "aliasname", length = 1000)
    private String aliasName;

    @Column(name = "fieldname", length = 1000)
    private String fieldName;

    @Column(name = "datatype")
    private String dataType;

    @Column(name = "uitype")
    private String uiType;

    @Column(name = "query", length = 1000)
    private String query;

    @Column(name = "predefinedValues")
    @Type(type = "text")
    private String predefinedValues;

    @Column(name = "predefinedValuesWithSorting")
    @Type(type = "text")
    private String predefinedValuesWithSorting;

    @Column(name = "showinlisting")
    private boolean showInListing;

    @Column(name = "showinfiltergrouping")
    private boolean showInFilterGrouping;

    @Column(name = "disabled")
    private Boolean disabled;

    @Column(name = "columncode")
    private String columnCode;

    @Column(name = "prefix")
    private String prefix;

    @Column(name = "relationship")
    private Integer relationship;

    @Column(name = "isFacetable", columnDefinition = "boolean DEFAULT false")
    private Boolean isFacetable;

    @Column(name = "isRequired", columnDefinition = "boolean DEFAULT false")
    private Boolean isRequired;

    private Integer columnWidth;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "customfield")
    private List<EdsCustomFieldListener> listeners = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "customfield")
    private List<EdsCustomFieldValidation> validations = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entitytypeid")
    private EdsEntityType entityType;

    @Enumerated(EnumType.STRING)
    private CustomFieldLookUpTypeEnum lookUpType;

    @Column(name = "addTab")
    private Boolean addTab = false;

    @Column(name = "seeOwnPermission")
    private Boolean seeOwnPermission;

    /**
     * The audit information.
     */
    @Embedded
    private EdsAuditInfo auditInfo;

    @ManyToMany(cascade = {CascadeType.PERSIST/*, CascadeType.REMOVE*/})
    @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "customfield_permissions",
            joinColumns = {@JoinColumn(name = "customfield_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private Set<EdsRole> allowedRoles = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST/*, CascadeType.REMOVE*/})
    @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "customfield_edit_permissions",
            joinColumns = {@JoinColumn(name = "customfield_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private Set<EdsRole> editFieldRoles = new HashSet<>();
    private String entityCategoryName;
    private String entityCategoryAlias;

    @Column(name = "clickable", columnDefinition = "boolean DEFAULT false")
    private boolean clickable;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referenceId")
    EdsReference reference;

    @Column(name = "active", columnDefinition = "boolean DEFAULT true")
    private boolean active;

    private Integer scale;

    @Column(name = "min_height")
    private String minHeight;

    @Column(name = "relationFieldId")
    private Integer relationFieldId;

    @Column(name = "relationFieldValues")
    @Type(type = "text")
    private String relationFieldValues;

    @Column(name = "quizFormScoreValues")
    @Type(type = "text")
    private String quizFormScoreValues;

    @Column(name = "numberMinValue")
    private Double numberMinValue;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customFormLocalizationId")
    private EdsCustomFormLocalization customFormlocalization;

    private static final String SPLITTER = "-:-";

    @Column(name = "minChar")
    private String minChar;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(name = "useInPermission")
    private Boolean useInPermission = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "custom_logic_field_id")
    private EdsCompanyCustomFieldsSettings customLogicField;

    @Column(name = "custom_logic_value")
    private String customLogicValue;

    @OneToMany(mappedBy = "customField", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @Where(clause = "type = 'CHANGE'")
    private Set<EdsDropdownValueRole> predefinedValueChangeRoles = new HashSet<>();

    @OneToMany(mappedBy = "customField", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @Where(clause = "type = 'CHANGE'")
    private Set<EdsDropdownValueEmployee> predefinedValueChangeEmployees = new HashSet<>();

    @OneToMany(mappedBy = "customField", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @Where(clause = "type = 'VIEW'")
    private Set<EdsDropdownValueRole> predefinedValueViewRoles = new HashSet<>();

    @OneToMany(mappedBy = "customField", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @Where(clause = "type = 'VIEW'")
    private Set<EdsDropdownValueEmployee> predefinedValueViewEmployees = new HashSet<>();

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getUiType() {
        return uiType;
    }

    public void setUiType(String uiType) {
        this.uiType = uiType;
    }

    private String formatPredefinedValue(String value) {
        if (value.contains(SPLITTER)) {
            return value;
        } else {
            return value.replace(",", SPLITTER).replace(";", SPLITTER);
        }
    }

    public String[] getPredefinedValues() {
        if (predefinedValues != null) {
            Map<String, List<Integer>> roles = getPredefinedValueViewRoles() != null ? getPredefinedValueViewRoles().stream().collect(Collectors.groupingBy(EdsDropdownValueRole::getValue, Collectors.mapping(r -> r.getRole().getObjectID(), Collectors.toList()))) : new HashMap<>();
            if (getPredefinedValueChangeRoles() != null) {
                getPredefinedValueChangeRoles().stream()
                        .collect(Collectors.groupingBy(EdsDropdownValueRole::getValue, Collectors.mapping(r -> r.getRole().getObjectID(), Collectors.toList())))
                        .forEach((key, value) -> roles.merge(key, value, (existing, incoming) -> {
                            existing.addAll(incoming);
                            return existing;
                        }));
            }
            Map<String, List<Integer>> employees = getPredefinedValueViewEmployees() != null ? getPredefinedValueViewEmployees().stream().collect(Collectors.groupingBy(EdsDropdownValueEmployee::getValue, Collectors.mapping(r -> r.getEmployee().getObjectID(), Collectors.toList()))) : new HashMap<>();
            if (getPredefinedValueChangeEmployees() != null) {
                getPredefinedValueChangeEmployees().stream()
                        .collect(Collectors.groupingBy(EdsDropdownValueEmployee::getValue, Collectors.mapping(r -> r.getEmployee().getObjectID(), Collectors.toList())))
                        .forEach((key, value) -> employees.merge(key, value, (existing, incoming) -> {
                            existing.addAll(incoming);
                            return existing;
                        }));
            }
            if (roles.isEmpty() && employees.isEmpty()) {
                return formatPredefinedValue(predefinedValues).split(SPLITTER);
            }
            String[] values = formatPredefinedValue(predefinedValues).split(SPLITTER);
            List<String> result = new ArrayList<>();
            EdsUser user = (EdsUser) SecurityContext.getInstance().getUser();
            for (String value : values) {
                if (roles.get(value) != null && employees.get(value) != null) {
                    if (user.hasEitherRoles(roles.get(value).toArray(new Integer[]{})) || employees.get(value).contains(user.getObjectID())) {
                        result.add(value);
                    }
                } else if (roles.get(value) != null) {
                    if (user.hasEitherRoles(roles.get(value).toArray(new Integer[]{}))) {
                        result.add(value);
                    }
                } else if (employees.get(value) != null) {
                    if (employees.get(value).contains(user.getObjectID())) {
                        result.add(value);
                    }
                } else {
                    result.add(value);
                }
            }
            return result.toArray(new String[]{});
        }
        return null;
    }

    public void setPredefinedValues(String[] predefinedValues) {
        if (predefinedValues != null) {
            this.predefinedValues = String.join(SPLITTER, predefinedValues);
        } else {
            this.predefinedValues = null;
        }
    }

    public PredefinedValueItem[] getPredefinedValuesWithSorting() {
        if (predefinedValuesWithSorting != null && !"".equals(predefinedValuesWithSorting)) {
            List<PredefinedValueItem> valuesWithSorting = new ArrayList<>();
            String[] predefinedValue = formatPredefinedValue(predefinedValuesWithSorting).split(SPLITTER);
            Map<String, List<EdsDropdownValueRole>> viewRoles = getPredefinedValueViewRoles() != null ? getPredefinedValueViewRoles().stream().collect(Collectors.groupingBy(EdsDropdownValueRole::getValue)) : null;
            Map<String, List<EdsDropdownValueEmployee>> viewEmployees = getPredefinedValueViewEmployees() != null ? getPredefinedValueViewEmployees().stream().collect(Collectors.groupingBy(EdsDropdownValueEmployee::getValue)) : null;
            Map<String, List<EdsDropdownValueRole>> changeRoles = getPredefinedValueChangeRoles() != null ? getPredefinedValueChangeRoles().stream().collect(Collectors.groupingBy(EdsDropdownValueRole::getValue)) : null;
            Map<String, List<EdsDropdownValueEmployee>> changeEmployees = getPredefinedValueChangeEmployees() != null ? getPredefinedValueChangeEmployees().stream().collect(Collectors.groupingBy(EdsDropdownValueEmployee::getValue)) : null;
            for (String s : predefinedValue) {
                String[] value = s.split("=");
                PredefinedValueItem item = new PredefinedValueItem(value.length > 1 ? Integer.parseInt(value[1]) : predefinedValue.length, value[0]);
                if (viewRoles != null && viewRoles.get(value[0]) != null) {
                    item.setViewRoles(viewRoles.get(value[0]).stream().map(r -> new SelectItem(r.getRole().getObjectID(), r.getRole().getName())).collect(Collectors.toList()));
                }
                if (viewEmployees != null && viewEmployees.get(value[0]) != null) {
                    item.setViewEmployees(viewEmployees.get(value[0]).stream().map(r -> new SelectItem(r.getEmployee().getObjectID(), r.getEmployee().getFullName())).collect(Collectors.toList()));
                }
                if (changeRoles != null && changeRoles.get(value[0]) != null) {
                    item.setChangeRoles(changeRoles.get(value[0]).stream().map(r -> new SelectItem(r.getRole().getObjectID(), r.getRole().getName())).collect(Collectors.toList()));
                }
                if (changeEmployees != null && changeEmployees.get(value[0]) != null) {
                    item.setChangeEmployees(changeEmployees.get(value[0]).stream().map(r -> new SelectItem(r.getEmployee().getObjectID(), r.getEmployee().getFullName())).collect(Collectors.toList()));
                }
                valuesWithSorting.add(item);
            }
            return valuesWithSorting.toArray(new PredefinedValueItem[0]);
        }
        return null;
    }

    public void setPredefinedValuesWithSorting(SelectItem[] predefinedValuesWithSorting) {
        if (predefinedValuesWithSorting != null) {
            StringBuilder valueWithSorting = new StringBuilder();
            Arrays.stream(predefinedValuesWithSorting).forEach(s -> valueWithSorting.append(s.getName()).append("=").append(s.getId().toString()).append(SPLITTER));
            this.predefinedValuesWithSorting = valueWithSorting.substring(0, valueWithSorting.lastIndexOf(SPLITTER));
        } else {
            this.predefinedValuesWithSorting = null;
        }
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isShowInListing() {
        return showInListing;
    }

    public void setShowInListing(boolean showInListing) {
        this.showInListing = showInListing;
    }

    public boolean isShowInFilterGrouping() {
        return showInFilterGrouping;
    }

    public void setShowInFilterGrouping(boolean showInFilterGrouping) {
        this.showInFilterGrouping = showInFilterGrouping;
    }

    public String getColumnCode() {
        return columnCode;
    }

    public void setColumnCode(String columnCode) {
        this.columnCode = columnCode;
    }

    public Integer getRelationship() {
        return relationship;
    }

    public void setRelationship(Integer relationship) {
        this.relationship = relationship;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Boolean getFacetable() {
        return isFacetable != null ? isFacetable : false;
    }

    public void setFacetable(Boolean facetable) {
        isFacetable = facetable;
    }

    public Boolean getRequired() {
        return isRequired != null ? isRequired : false;
    }

    public void setRequired(Boolean required) {
        isRequired = required;
    }

    public Boolean getDisabled() {
        return disabled != null && disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Boolean isAddTab() {
        return this.addTab != null ? this.addTab : false;
    }

    public void setAddTab(final Boolean addTab) {
        this.addTab = addTab;
    }

    public List<EdsCustomFieldListener> getListeners() {
        return listeners;
    }

    public void setListeners(List<EdsCustomFieldListener> listeners) {
        this.listeners = listeners;
    }

    public Integer getColumnWidth() {
        return columnWidth;
    }

    public void setColumnWidth(Integer columnWidth) {
        this.columnWidth = columnWidth;
    }

    public List<EdsCustomFieldValidation> getValidations() {
        if (validations == null) {
            validations = new ArrayList<>();
        }
        return validations;
    }

    public void setValidations(List<EdsCustomFieldValidation> validations) {
        this.validations = validations;
    }

    public EdsAuditInfo getAuditInfo() {
        return auditInfo;
    }

    public void setAuditInfo(EdsAuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }

    public Set<EdsRole> getAllowedRoles() {
        if (allowedRoles == null) {
            allowedRoles = new HashSet<>();
        }
        return allowedRoles;
    }

    public ArrayList<String> getAllowedRoleCodes() {
        ArrayList<String> allowedRoleCodes = new ArrayList<>();
        if (allowedRoles != null) {
            for (EdsRole role : allowedRoles) {
                allowedRoleCodes.add(role.getCode());
            }
        }
        return allowedRoleCodes;
    }

    public ArrayList<Integer> getAllowedRoleIds() {
        ArrayList<Integer> allowedRoleCodes = new ArrayList<>();
        if (allowedRoles != null) {
            for (EdsRole role : allowedRoles) {
                allowedRoleCodes.add(role.getObjectID());
            }
        }
        return allowedRoleCodes;
    }

    public void setAllowedRoles(Set<EdsRole> accessedRoles) {
        this.allowedRoles = accessedRoles;
    }

    public Set<EdsRole> getEditFieldRoles() {
        if (editFieldRoles == null) {
            editFieldRoles = new HashSet<>();
        }
        return this.editFieldRoles;
    }

    public void setEditFieldRoles(final Set<EdsRole> editFieldRoles) {
        this.editFieldRoles = editFieldRoles;
    }

    public ReferenceItem getReference() {
        return reference != null ? reference.getRPC() : null;
    }

    public Integer getReferenceId() {
        return reference != null ? reference.getObjectID() : null;
    }

    public EdsReference getEdsReference() {
        return reference != null ? reference : null;
    }

    public void setReference(EdsReference reference) {
        this.reference = reference;
    }

    public EdsReference getReferenceEntity() {
        return reference;
    }

    public EdsCustomFormLocalization getCustomFormlocalization() {
        return customFormlocalization;
    }

    public void setCustomFormlocalization(EdsCustomFormLocalization customFormlocalization) {
        this.customFormlocalization = customFormlocalization;
    }

    public CompanyCustomFieldItem getRPC(CompanyCustomFieldItem item) {
        if (item == null) {
            item = new CompanyCustomFieldItem();
        }
        item.setObjectId(getObjectID());
        item.setColumnCode(getColumnCode());
        item.setEntityName(getEntityName());
        item.setFieldName(getFieldName());
        item.setAliasName(getAliasName());
        item.setDataType(getDataType());
        item.setUiType(getUiType());
        item.setEntityCategoryName(getEntityCategoryName());
        item.setEntityCategoryAlias(getEntityCategoryAlias());
        item.setQuery(getQuery());
        item.setPredefinedValues(getPredefinedValues());
        item.setCustomFieldSettingID(getObjectID());
        item.setRelationship(getRelationship());
        item.setRequired(getRequired());
        item.setMinChar(getMinChar());
        item.setShowInListing(isShowInListing());
        item.setShowInFilterGrouping(isShowInFilterGrouping());
        item.setDisabled(getDisabled());
        item.setAddTab(isAddTab());
        item.setSeeOwnPermission(getSeeOwnPermission());
        item.setLookUpTypeEnum(getLookUpType());
        item.setPrefix(getPrefix());
        item.setReferenceItem(getReference());
        item.setSystemField(getDataType() != null && getDataType().equals(Constants.SYSTEM));
        item.setActive(isActive());
        item.setScale(getScale());
        item.setRelationFieldId(getRelationFieldId());
        item.setRelationFieldValues(getRelationFieldValues());
        item.setQuizFormScoreValues(getQuizFormScoreValues() != null ? getQuizFormScoreValues() : "");
        item.setNumberMinValue(getNumberMinValue());
        item.setUseInPermission(isUseInPermission());
        EdsAuditInfo edsAuditInfo = getAuditInfo();
        if (edsAuditInfo != null) {
            item.setCreatedBy(edsAuditInfo.getCreatedBy() != null ? edsAuditInfo.getCreatedBy().getName() : "");
            item.setCreationDate(edsAuditInfo.getCreationDate());
            item.setLastUpdatedBy(edsAuditInfo.getModifiedBy() != null ? edsAuditInfo.getModifiedBy().getName() : "");
            item.setLastUpdatedDate(edsAuditInfo.getModificationDate());
        }
        HashSet<EdsRole> roles = new HashSet<>();
        if (!allowedRoles.isEmpty()) {
            roles.addAll(allowedRoles);
        }
        ArrayList<Integer> roleList = new ArrayList<>();
        for (EdsRole role : roles) {
            roleList.add(role.getObjectID());
        }
        item.setAllowedRoles(roleList);
        ArrayList<Integer> editRoleList = new ArrayList<>();
        if (editFieldRoles != null && !editFieldRoles.isEmpty()) {
            for (EdsRole role : editFieldRoles) {
                editRoleList.add(role.getObjectID());
            }
        }
        item.setRoleEdit(editRoleList);
        item.setCustomLogicValue(getCustomLogicValue());
        if (getCustomLogicField() != null) {
            item.setCustomLogicField(new SelectItem(getCustomLogicField().getObjectID(), getCustomLogicField().getFieldName(), SelectItem.asItems(getCustomLogicField().getPredefinedValues())));
        }
        return item;
    }

    public String getFieldNameLocalization(String internationalization) {
        if (internationalization != null && getCustomFormlocalization() != null) {
            return switch (internationalization) {
                case "en" -> getCustomFormlocalization().getEnglishName();
                case "ar" -> getCustomFormlocalization().getArabicName();
                case "ru" -> getCustomFormlocalization().getRussianName();
                case "uz" -> getCustomFormlocalization().getUzbekName();
                default -> getCustomFormlocalization().getDefaultName();
            };
        }
        return getFieldName();
    }

    public static Map getCustomFieldTables() {
        HashMap<String, String> customFieldTables = new HashMap<>();
        customFieldTables.put(ViewName.Lead.name(), "crmcustomfields");
        customFieldTables.put(ViewName.Opportunity.name(), "crmcustomfields");
        customFieldTables.put(ViewName.CrmAccount.name(), "crmcustomfields");
        customFieldTables.put(ViewName.Contact.name(), "crmcustomfields");
        customFieldTables.put(ViewName.CrmCase.name(), "crmcustomfields");
        customFieldTables.put(ViewName.Estimate.name(), "estimateOfContractCustomFields");
        customFieldTables.put(ViewName.Candidate.name(), "crmcustomfields");
        customFieldTables.put(ViewName.Employee.name(), "employeecustomfields");
        customFieldTables.put(ViewName.Dependent.name(), "dependentcustomfields");
        customFieldTables.put(ViewName.Task.name(), "taskcustomfields");
        customFieldTables.put(ViewName.Issues.name(), "taskcustomfields");
        customFieldTables.put(ViewName.PersonalGoal.name(), "goalcustomfields");     //Personal Goal
        customFieldTables.put(ViewName.DepartmentGoal.name(), "goalcustomfields");   //Department Goal
        customFieldTables.put(ViewName.ProjectGoal.name(), "goalcustomfields");      //Project Goal
        customFieldTables.put(ViewName.BusinessGoal.name(), "goalcustomfields");     //Business Goal
        customFieldTables.put(ViewName.CompanyGoal.name(), "goalcustomfields");      //Company Goal
        customFieldTables.put(ViewName.Project.name(), "projectcustomfields");
        customFieldTables.put(ViewName.ProductCategory.name(), "itemcustomfields");
        customFieldTables.put(ViewName.ProductServiceView.name(), "itemcustomfields");
        customFieldTables.put(ViewName.SaleInvoice.name(), "invoicecustomfields");
        customFieldTables.put(ViewName.PurchaseInvoice.name(), "invoicecustomfields");
        customFieldTables.put(ViewName.SaleQuote.name(), "invoicecustomfields");
        customFieldTables.put(ViewName.SaleOrder.name(), "invoicecustomfields");
        customFieldTables.put(ViewName.PurchaseOrder.name(), "invoicecustomfields");
        customFieldTables.put(ViewName.ExpenceReportView.name(), "invoicecustomfields");
        customFieldTables.put(ViewName.OnboardingStep.name(), "onboardingstepcustomfields");
        customFieldTables.put(ViewName.CompanySettings.name(), "companySettingscustomfields");
        customFieldTables.put(ViewName.Vacancy.name(), "vacancycustomfields");
        customFieldTables.put(ViewName.LeaveRequest.name(), "sickrequestcustomfields");
        customFieldTables.put(ViewName.RequestForQuote.name(), "rfqcustomfields");
        customFieldTables.put(ViewName.RequestForPurchase.name(), "rfpcustomfields");
        customFieldTables.put(ViewName.Prepayment.name(), "prepaymentCustomfields");
        customFieldTables.put(ViewName.Supplier.name(), "prepaymentCustomfields");
        customFieldTables.put(ViewName.RentalOrdersView.name(), "rentalOrdercustomfields");
        customFieldTables.put(ViewName.Brand.name(), "brandcustomfields");
        customFieldTables.put(ViewName.BenefitRequestList.name(),"benefitrequestcustomfields");

        return customFieldTables;
    }

    public void setEntityCategoryName(String entityCategoryName) {
        this.entityCategoryName = entityCategoryName;
    }

    public String getEntityCategoryName() {
        return entityCategoryName;
    }

    public void setEntityCategoryAlias(String entityCategoryAlias) {
        this.entityCategoryAlias = entityCategoryAlias;
    }

    public String getEntityCategoryAlias() {
        return entityCategoryAlias;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public boolean isClickable() {
        return clickable;
    }

    public EdsEntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EdsEntityType entityType) {
        this.entityType = entityType;
    }

    public CustomFieldLookUpTypeEnum getLookUpType() {
        return lookUpType;
    }

    public void setLookUpType(CustomFieldLookUpTypeEnum lookUpType) {
        this.lookUpType = lookUpType;
    }

    public String getPrefix() {
        return prefix != null ? prefix : "";
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Integer getScale() {
        return scale;
    }

    public void setScale(Integer scale) {
        this.scale = scale;
    }


    public String getMinHeight() {
        return this.minHeight;
    }

    public void setMinHeight(final String minHeight) {
        this.minHeight = minHeight;
    }

    public Boolean getSeeOwnPermission() {
        return this.seeOwnPermission != null ? this.seeOwnPermission : false;
    }

    public void setSeeOwnPermission(Boolean seeOwnPermission) {
        this.seeOwnPermission = seeOwnPermission;
    }

    public Integer getRelationFieldId() {
        return relationFieldId;
    }

    public void setRelationFieldId(Integer relationFieldId) {
        this.relationFieldId = relationFieldId;
    }

    public String getRelationFieldValues() {
        return relationFieldValues;
    }

    public void setRelationFieldValues(String relationFieldValues) {
        this.relationFieldValues = relationFieldValues;
    }

    public String getQuizFormScoreValues() {
        return quizFormScoreValues;
    }

    public void setQuizFormScoreValues(String quizFormScoreValues) {
        this.quizFormScoreValues = quizFormScoreValues;
    }

    public Double getNumberMinValue() {
        return numberMinValue;
    }

    public void setNumberMinValue(Double numberMinValue) {
        this.numberMinValue = numberMinValue;
    }

    public String getMinChar() {
        return minChar;
    }

    public void setMinChar(String minChar) {
        this.minChar = minChar;
    }

    public Boolean isUseInPermission() {
        return useInPermission != null ? useInPermission : false;
    }

    public void setUseInPermission(Boolean useInPermission) {
        this.useInPermission = useInPermission;
    }

    public EdsCompanyCustomFieldsSettings getCustomLogicField() {
        return customLogicField;
    }

    public void setCustomLogicField(EdsCompanyCustomFieldsSettings customLogicField) {
        this.customLogicField = customLogicField;
    }

    public String getCustomLogicValue() {
        return customLogicValue;
    }

    public void setCustomLogicValue(String customLogicValue) {
        this.customLogicValue = customLogicValue;
    }

    public Set<EdsDropdownValueEmployee> getPredefinedValueChangeEmployees() {
        return predefinedValueChangeEmployees;
    }

    public void setPredefinedValueChangeEmployees(Set<EdsDropdownValueEmployee> predefinedValueChangeEmployees) {
        this.predefinedValueChangeEmployees = predefinedValueChangeEmployees;
    }

    public Set<EdsDropdownValueEmployee> getPredefinedValueViewEmployees() {
        return predefinedValueViewEmployees;
    }

    public void setPredefinedValueViewEmployees(Set<EdsDropdownValueEmployee> predefinedValueViewEmployees) {
        this.predefinedValueViewEmployees = predefinedValueViewEmployees;
    }

    public Set<EdsDropdownValueRole> getPredefinedValueChangeRoles() {
        return predefinedValueChangeRoles;
    }

    public void setPredefinedValueChangeRoles(Set<EdsDropdownValueRole> predefinedValueChangeRoles) {
        this.predefinedValueChangeRoles = predefinedValueChangeRoles;
    }

    public Set<EdsDropdownValueRole> getPredefinedValueViewRoles() {
        return predefinedValueViewRoles;
    }

    public void setPredefinedValueViewRoles(Set<EdsDropdownValueRole> predefinedValueViewRoles) {
        this.predefinedValueViewRoles = predefinedValueViewRoles;
    }
}
