package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsReferenceLocale;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.HasEdsObjectPermission;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.customform.EdsModelField;
import com.edatasite.workforce.core.domain.workflow.EdsTraceable;
import com.edatasite.workforce.core.domain.workflow.EdsWebHookResponse;
import com.edatasite.workforce.core.domain.workflow.EdsWorkflowRule;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactTo;
import com.edatasite.workforce.gwt.core.client.enums.WorkflowExecutionCriteriaEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomizeFormItem;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.InOutItem;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelForm;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;

import java.util.*;

/**
 * User: Xushnud
 * Date: 22.03.2010
 * Time: 20:37:25
 */
public interface AllInOneServiceLocal {

    HashMap<String, Integer> findIDsBy(ListingFilterParameter fp);

    ArrayList<RelationItem> getEmailRelations(long generatedGoogleID);

    Boolean deleteRelationByGeneratedGoogleID(long generatedGoogleID);

    EdsReferenceLocale saveEntityLocale(ReferenceLocale referenceLocale);

    EdsReferenceLocale saveEntityLocale(EdsReference reference, ReferenceLocale referenceLocale);

    ArrayList<RelationItem> saveRelations(String fromType, Integer fromID, String fromName, ArrayList<RelationItem> relationItems);

    HasEdsObjectPermission saveObjectPermission(HasEdsObjectPermission entity, HasObjectPermission rpc);

    SelectItem[] getParentAccountsTreeList(Integer objectID);

    String getRelationName(Integer relationID, String relationType);

    SelectItem[] getEmployeesAsSelectItem(ListLoadConfig listLoadConfig, ListingFilterParameter filterParametrs);

    ArrayList<SelectItem> getApproverEmployeesAsSelectItem(ListLoadConfig listLoadConfig, ListingFilterParameter filterParametrs);

    SelectItem[] getEmployeesAsSelectItem(ListingFilterParameter filterParametrs);

    void runRecurrenceForWorkflowAction(String actionRelationType, Integer actionObjectID, String relationType, Integer relationID, Integer userID);

    void runRecurringWorkflow(Integer wokrflowRuleID, Integer companyID, Integer userID);

    boolean checkCrmAccountRelations(Integer accountID, String accountType);

    void runWorkflows(Integer entityID, String relationType, String module, WorkflowExecutionCriteriaEnum[] actions, boolean fromWorkflow, boolean sendNotification);

    Integer saveGoogleGadgetMail(Email email);

    void removeCFsFromWorkflows(EdsModelField field);

    void approvedOrRejected(String entityType, Integer entityID, EdsBusinessEvent eventType);

    String getImageUrl(Integer id);

    void runWebHooks(EdsWorkflowRule rule, String relationType, EdsTraceable object, boolean isRetry, EdsWebHookResponse existingWebHook);

    EdsTraceable getTraceableDomainObject(Integer entityID, String relationType);

    Map<String, Object> getAdditionalFieldValuesAsMap(String relationType, EdsTraceable object, Map<String, Object> keyValues, EdsUser edsUser);

    Integer saveModelForm(ModelForm form);

    void runActionTrack(EdsWorkflowRule workflowRule, String typeEmployee, EdsTraceable employee);

    SelectItem[] getPaymentMethodList();

    SelectItem[] getProjectsAsSelectItem(ListingFilterParameter filterParametrs);

    Map<String, String> getLeaveReasonMap(String formId);

    ListResult<SelectItem> getCrmAccountAsSelectItem(int type, ListingFilterParameter filterParameter);

    SelectItem[] getDepartmentsForLookUp(ListingFilterParameter filterParameter);

    void relationsChangeTypesByType(Integer relationID);

    SelectItem[] getLookUpItems(ListingFilterParameter filterParameter, Integer type);
    SelectItem[] getLookUpItems(ListingFilterParameter filterParameter, Integer type, String query);
    SelectItem[] getLookUpItems(String type, String searchKey, String query);

    ApprovalListResult getApprovers(String formID, Integer entityID, boolean isLeaveRequestForm, Integer userID, boolean fromSettings);

    ApprovalListResult getApprovers(String formID, Integer entityID, boolean isLeaveRequestForm, Integer userID, boolean fromSettings, boolean fromApi);

    SelectItem[] getAccountsForPayment(ListingFilterParameter filterParametrs);

    Integer[] getAllEmployeesMaxCount();

    ArrayList<SelectItem> getAllRoles();

    InOutItem[] getInOutReport(Integer clientId, Integer projectId, Integer departmentId, Integer employeeId, Integer viewAsId,
                               String groupByName, Date t1, Date t2, boolean showDate, boolean showCheckIn, boolean showCheckOut,
                               boolean showActualIn, boolean showLeaveReq, boolean showLauchHour,
                               boolean showTimesheetHour, boolean showBudgetHour, boolean showMissingHours, boolean showFinImpact);

    NumberData generateCandidateNumber(Integer candidateID);

    HasObjectPermission getObjectPermission(HasEdsObjectPermission hasEdsObjectPermission, HasObjectPermission hasObjectPermission);

    ArrayList<SelectItem> getRoles();

    Map<String, LinkedList<CustomizeFormItem>> getCustomizeGridForm(String formID);

    SelectItem[] getCustomFieldLookUpData(ListingFilterParameter fp, CustomFieldLookUpTypeEnum typeEnum);

    Integer getTaxCalcTypeForInvoice();

    ArrayList<RelationItem> getAdditionalRelations(Integer relationID, String relationType, String relationName, Integer fromID, String fromType, String fromName);

    byte[]  getSipuniAudio(String callId,String userId,String secret);

    ContactTo createContactFromCalls(String phoneNumber,String contactFullName);

}
