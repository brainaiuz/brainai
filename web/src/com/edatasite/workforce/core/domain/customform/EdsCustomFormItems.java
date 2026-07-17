package com.edatasite.workforce.core.domain.customform;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.shared.db.ObjectIdentifier;
import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.EdsCompanyCustomFieldsSettings;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.approving.EdsApprovable;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.customfields.EdsCustomFormCustomFields;
import com.edatasite.workforce.core.domain.documents.EdsAuditInfo;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFormSolrRPC;
import com.edatasite.workforce.gwt.core.client.rpc.FormItems;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCustomFormConst;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.app.StaticContextAccessor;
import com.edatasite.workforce.gwt.core.server.db.CompanyCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.common.SolrInputDocument;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.CUSTOM_VIEW;
import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "custom_form_item")
public class EdsCustomFormItems extends EdsApprovable implements ObjectIdentifier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "objectKey", unique = true, updatable = false)
    private String objectKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id", referencedColumnName = "form_id")
    private EdsCustomForm customForm;

    @Column(columnDefinition = "boolean DEFAULT false")
    private boolean deleted = false;

    @Column(name = "duration_time")
    private String durationTime;

    @Embedded
    private EdsAuditInfo auditInfo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_customfieldsid", unique = true)
    private EdsCustomFormCustomFields formCustomFields;

    @OneToMany(mappedBy = "formItem", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<EdsCustomItemTable> itemTables = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entityID", fetch = FetchType.LAZY)
    @Where(clause = "stepEmployeeType = 'CUSTOM_FORM_ITEM'")
    @OrderBy(value = "approverOrder ASC")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private List<EdsApprover> approvers = new ArrayList<>();

    private Integer relationId;
    private String relationObjectKey;
    private String relationType;

    public EdsCustomFormItems() {
    }

    @Override
    public List<EdsApprover> getApprovers() {
        return approvers;
    }

    @Override
    public void setApprovers(List<EdsApprover> approvers) {
        this.approvers = approvers;
    }

    @Override
    public void setEntityStatus(EdsReference overallStatus) {
        if (this.getOverallStatus() != overallStatus) {
            addChange(CustomFormConstants.STATUS);
        }
        setOverallStatus(overallStatus);
    }

    @Override
    public boolean isCurrentApproverApproved() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && Constants.CUSTOM_FORM_ITEM_STATUS_APPROVED.equals(getCurrentApprover().getStatus().getCode());
    }

    @Override
    public boolean isCurrentApproverRejected() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && Constants.CUSTOM_FORM_ITEM_STATUS_REJECTED.equals(getCurrentApprover().getStatus().getCode());
    }

    @Override
    protected EdsReference getStatusByMarkedAction(Integer actionId) {
        if (!isOk(actionId)) {
            return null;
        }
        ReferenceManager referenceManager = StaticContextAccessor.getBean(ReferenceManager.class);
        if (actionId.equals(ApproverItem.MARK_AS_REJECTED)) {
            return referenceManager.findReference(Constants.CUSTOM_FORM_ITEM_STATUS, Constants.CUSTOM_FORM_ITEM_STATUS_REJECTED);
        } else if (actionId.equals(ApproverItem.MARK_AS_APPROVED)) {
            return referenceManager.findReference(Constants.CUSTOM_FORM_ITEM_STATUS, Constants.CUSTOM_FORM_ITEM_STATUS_APPROVED);
        } else if (actionId.equals(ApproverItem.SEND_TO_CREATOR)) {
            return referenceManager.findReference(Constants.CUSTOM_FORM_ITEM_STATUS, Constants.CUSTOM_FORM_ITEM_STATUS_REJECTED);
        } else if (actionId.equals(ApproverItem.SEND_TO_DIRECTORS)) {
            return referenceManager.findReference(Constants.CUSTOM_FORM_ITEM_STATUS, Constants.CUSTOM_FORM_ITEM_STATUS_REJECTED);
        }
        return null;
    }

    @Override
    public void updateRejectedStatus() {
        if (getOverallStatus() != null && Constants.CUSTOM_FORM_ITEM_STATUS_REJECTED.equals(getOverallStatus().getCode())) {
            ReferenceManager referenceManager = StaticContextAccessor.getBean(ReferenceManager.class);
            setEntityStatus(referenceManager.findReference(Constants.CUSTOM_FORM_ITEM_STATUS, Constants.CUSTOM_FORM_ITEM_STATUS_SUBMITTED));
        }
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsCustomForm getCustomForm() {
        return customForm;
    }

    public void setCustomForm(EdsCustomForm customForm) {
        this.customForm = customForm;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public EdsAuditInfo getAuditInfo() {
        if (auditInfo == null) {
            auditInfo = new EdsAuditInfo();
        }
        return auditInfo;
    }

    public void setAuditInfo(EdsAuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }

    public EdsCustomFormCustomFields getFormCustomFields() {
        return formCustomFields;
    }

    public void setFormCustomFields(EdsCustomFormCustomFields formCustomFields) {
        this.formCustomFields = formCustomFields;
    }

    public Set<EdsCustomItemTable> getItemTables() {
        return itemTables.stream().sorted(Comparator.comparing(EdsCustomItemTable::getSorder)).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public void setItemTables(Set<EdsCustomItemTable> itemTables) {
        this.itemTables = itemTables;
    }

    public String getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(String durationTime) {
        this.durationTime = durationTime;
    }

    public void addItemTable(EdsCustomItemTable itemTable) {
        itemTables.add(itemTable);
        itemTable.setFormItem(this);
    }

    public FormItems toRpc() {
        FormItems item = new FormItems();
        item.setFormID(getCustomForm().getFormID());
        item.setObjectID(getObjectID());
        item.setObjectKey(getObjectKey());
        item.setFormName(getCustomForm().getName() != null ? getCustomForm().getName() : getName());
        item.setModifiedData(getAuditInfo() != null ? getAuditInfo().getModificationDate() : null);
        item.setRelationId(getRelationId());
        item.setRelationObjectKey(getRelationObjectKey());
        item.setRelationType(getRelationType());
        item.setDurationTime(getDurationTime());
        if (this.getOverallStatus() != null) {
            item.setStatusCode(this.getOverallStatus().getCode());
            item.setStatus(this.getOverallStatus().getName());
        }
        if (this.getCurrentApprover() != null && this.getCurrentApprover().getExactEmployee() != null) {
            item.setCurrentApproverId(this.getCurrentApprover().getExactEmployee().getObjectID());
            item.setCurrentApproverName(this.getCurrentApprover().getExactEmployee().getFullName());
        }
        if (this.getPrevApprover() != null && this.getPrevApprover().getExactEmployee() != null) {
            item.setPrevApproverName(this.getPrevApprover().getExactEmployee().getFullName());
        }
        if (this.getAuditInfo() != null && this.getAuditInfo().getCreatedBy() != null && this.getAuditInfo().getCreatedBy().getFullName() != null) {
            item.setCreator(this.getAuditInfo().getCreatedBy().getFullName());
        }
        if (this.getAuditInfo() != null && this.getAuditInfo().getCreationDate() != null) {
            item.setCreatedDate(this.getAuditInfo().getCreationDate());
        }
        if (this.getAuditInfo() != null && this.getAuditInfo().getModifiedBy() != null && this.getAuditInfo().getModifiedBy().getFullName() != null) {
            item.setUpdater(this.getAuditInfo().getModifiedBy().getFullName());
        }
        return item;
    }

    public CustomFormSolrRPC getSolrRPC() {
        CustomFormSolrRPC solrRPC = new CustomFormSolrRPC();

        solrRPC.setObjectId(getObjectID());
        solrRPC.setDocType(SolrCustomFormConst.CUSTOM_FORM_SOLR_DOC);
        solrRPC.setItemId(getCustomForm().getObjectID());
        solrRPC.setFormId(getCustomForm().getFormID());
        solrRPC.setFormName(getCustomForm().getName());

        if (getOverallStatus() != null) {
            solrRPC.setStatus(getOverallStatus().getAsSelectItem());
        }

        if (getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null) {
            solrRPC.setCurrentApprover(getCurrentApprover().getExactEmployee().getAsSelectItem());
        }

        if (getAuditInfo() != null) {
            if (getAuditInfo().getCreatedBy() != null) {
                EdsUser creator = getAuditInfo().getCreatedBy();
                solrRPC.setCreator(creator.getAsSelectItem());
            }
            if (getAuditInfo().getModifiedBy() != null) {
                EdsUser modifier = getAuditInfo().getModifiedBy();
                solrRPC.setUpdater(modifier.getAsSelectItem());
            }
            solrRPC.setCreatedDate(getAuditInfo().getCreationDate());
            solrRPC.setUpdatedDate(getAuditInfo().getModificationDate());
        }

        return solrRPC;
    }

    public SolrInputDocument indexToSolr(Integer companyID) {

        SolrInputDocument doc = new SolrInputDocument();
        doc.setField(SolrCustomFormConst.FIELD_DOC_TYPE, SolrCustomFormConst.CUSTOM_FORM_SOLR_DOC);
        String compositID = companyID + "_" + (getCustomForm() != null ? getCustomForm().getObjectID() : null) + "_" + getObjectID();
        doc.addField(SolrCustomFormConst.FIELD_COMPOSITE_ID, compositID);
        doc.addField(SolrCustomFormConst.FIELD_COMPANY_ID, companyID);

        doc.addField(SolrCustomFormConst.FIELD_ITEM_ID, getCustomForm().getObjectID());
        doc.addField(SolrCustomFormConst.FIELD_FORM_ID, getCustomForm().getFormID());
        doc.addField(SolrCustomFormConst.FIELD_FORM_NAME, getCustomForm().getName());
        doc.addField(SolrCustomFormConst.FIELD_OBJECT_ID, getObjectID());

        if (getOverallStatus() != null) {
            doc.addField(SolrCustomFormConst.FIELD_STATUS_ID, getOverallStatus().getObjectID());
            doc.addField(SolrCustomFormConst.FIELD_STATUS_NAME, getOverallStatus().getName());
            doc.addField(SolrCustomFormConst.FIELD_STATUS_ID_NAME, getOverallStatus().getObjectID() + SolrCustomFormConst.SPLIT + getOverallStatus().getName());
        }
        if (getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null) {
            doc.addField(SolrCustomFormConst.FIELD_CURRENT_APPROVER_ID, getCurrentApprover().getExactEmployee().getObjectID());
            doc.addField(SolrCustomFormConst.FIELD_CURRENT_APPROVER_NAME, getCurrentApprover().getExactEmployee().getFullName());
            doc.addField(SolrCustomFormConst.FIELD_CURRENT_APPROVER_ID_NAME, getCurrentApprover().getExactEmployee().getObjectID() + SolrCustomFormConst.SPLIT + getCurrentApprover().getExactEmployee().getFullName());
        }

        if (getAuditInfo() != null) {

            if (getAuditInfo().getCreatedBy() != null) {

                EdsUser creator = getAuditInfo().getCreatedBy();

                doc.addField(SolrCustomFormConst.FIELD_CREATOR_ID, creator.getObjectID());
                doc.addField(SolrCustomFormConst.FIELD_CREATOR_NAME, creator.getName());
                doc.addField(SolrCustomFormConst.FIELD_CREATOR_ID_NAME, creator.getObjectID() + SolrCustomFormConst.SPLIT + creator.getName());
            }

            if (getAuditInfo().getModifiedBy() != null) {

                EdsUser modifier = getAuditInfo().getModifiedBy();

                doc.addField(SolrCustomFormConst.FIELD_UPDATER_ID, modifier.getObjectID());
                doc.addField(SolrCustomFormConst.FIELD_UPDATER_NAME, modifier.getName());
                doc.addField(SolrCustomFormConst.FIELD_UPDATER_ID_NAME, modifier.getObjectID() + SolrCustomFormConst.SPLIT + modifier.getName());
            }
            doc.addField(SolrCustomFormConst.FIELD_CREATED_DATE, getAuditInfo().getCreationDate());
            doc.addField(SolrCustomFormConst.FIELD_UPDATED_DATE, getAuditInfo().getModificationDate());
        }
        CustomFieldsUtils.setInSolrCustomFields(doc, getFormCustomFields());
        return doc;
    }

    @Override
    protected String getStringValueByFieldID(String realFieldID) {
        return super.getStringValueByFieldID(realFieldID);
    }

    @Override
    public void setValueForField(EdsModelField field, Object value) {
        if (field != null && field.getField_ID() != null) {
            String fieldID = field.getField_ID();
            if (field.isCustomField()) {
                Object ob = CustomFieldsUtils.getObjectValue(this.getFormCustomFields(), fieldID);
                if (ob != null) {
                    if (ob instanceof String) {
                        String text = (String) ob;
                        if (!text.equals(value)) {
                            addChange(fieldID);
                        }
                    } else if (ob instanceof Number) {
                        String text = String.valueOf(((Double) ob).intValue());
                        if (!text.equals(value)) {
                            addChange(fieldID);
                        }
                    } else if (ob instanceof Date) {
                        Date date = (Date) ob;
                        if (!date.equals(value)) {
                            addChange(fieldID);
                        }
                    }
                } else {
                    addChange(fieldID);
                }
                Map<String, Object> customFieldsMap = new HashMap<>();
                customFieldsMap.put(fieldID, value);
                CustomFieldsUtils.setDomenObjectFieldChange(this.getFormCustomFields(), customFieldsMap, fieldID);
            }
        }
        super.setValueForField(field, value);
    }

    @Override
    public Object getRealValue(String fieldID) {
        if (fieldID == null) {
            return null;
        } else if (fieldID.equals(CustomFormConstants.STATUS)) {
            return getOverallStatus();
        } else if (fieldID.equals(CustomFormConstants.CREATED_DATE)) {
            return getAuditInfo().getCreationDate();
        } else if (fieldID.equals(CustomFormConstants.UPDATED_DATE)) {
            return getAuditInfo().getModificationDate();
        } else if (fieldID.contains("string_value") || fieldID.contains("double_value") || fieldID.contains("date_value")) {
            CompanyCustomFieldsManager companyCustomFieldsManager = (CompanyCustomFieldsManager) ApplicationContextProvider.applicationContext.getBean("companyCFSettingsManager");
            String formId = this.getCustomForm().getFormID();
            EdsCompanyCustomFieldsSettings companyCustomFieldsSettings = companyCustomFieldsManager.getCompanyCustomField(ViewName.CustomFormItems.name(), CUSTOM_VIEW + formId.substring(0, formId.indexOf("_FORM")), fieldID);

            Object result = this.getFormCustomFields() != null ? CustomFieldsUtils.getObjectValue(this.getFormCustomFields(), fieldID) : "";

            if (result != "" && companyCustomFieldsSettings != null && Constants.TYPE_ENTITY_LOOKUP.equals(companyCustomFieldsSettings.getUiType())) {
                if (result instanceof String && StringUtils.isNotBlank((String) result)) {
                    String selectedAsString = (String) result;
                    Integer selectedId = Integer.valueOf(selectedAsString);
                    SelectItem selectedResult = Arrays.stream(companyCustomFieldsManager.getCustomFieldDataByQuery(SecurityContext.getCompanyID(), companyCustomFieldsSettings.getQuery())).filter(x -> x.getId().equals(selectedId)).findAny().orElse(new SelectItem());
                    return selectedResult.getName();
                }
            }
            return result;
        }
        return super.getRealValue(fieldID);
    }

    public Integer getRelationId() {
        return relationId;
    }

    public void setRelationId(Integer relationId) {
        this.relationId = relationId;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    @Override
    public String getObjectKey() {
        return objectKey;
    }

    @Override
    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    public String getRelationObjectKey() {
        return relationObjectKey;
    }

    public void setRelationObjectKey(String relationObjectKey) {
        this.relationObjectKey = relationObjectKey;
    }
}
