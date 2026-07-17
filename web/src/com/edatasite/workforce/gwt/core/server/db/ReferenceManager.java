package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA. User: iskan Date: Jan 14, 2008 Time: 3:58:03 PM To
 * change this template use File | Settings | File Templates.
 */

public interface ReferenceManager extends Manager<EdsReference> {

    String _SICK_STATUS = "_SICK_STATUS";

    String _UPLOAD_TYPE = "_UPLOAD_TYPE";
    String LOCAL = "LOCAL";
    String AMAZON = "AMAZON";
    String GOOGLE = "GOOGLE";
    String _TIME_SHEET_ENTRY_STATUS = "_TIME_SHEET_ENTRY_STATUS";
    String _ISSUE_STATUS = "_ISSUE_STATUS";
    String _ISSUE_STATUS_RESOLVED = "_RESOLVED";

    String _PRODUCT_TYPE = "_PRODUCT_TYPE";

    List<EdsReference> listReferences(ListingFilterParameter filterParameter);

    int countReferences(ListingFilterParameter filterParameter);

    List<EdsReference> listReferences(String parentCode);

    List<EdsReference> listReferencesByLimit(String parentCode, int limit);

    List<EdsReference> listReferences(String parentCode, boolean isSystemReference);

    Map<String, List<ReferenceItem>> listReferences(List<String> parentCodes, boolean isSystemReference);

    EdsReference findReference(String parentCode, String code);

    EdsReference findByParentCodeAndName(String parentCode, String name);

    EdsReference findReferenceByDescription(String parentCode, String description);

    EdsReference getReference(Integer objectID);

    EdsUser getUser();

    EdsReference getOriginal(Integer objectID);

    EdsReference get(Integer objectID);

    List<EdsReference> getTimeSheetEntryStatuses();

    List<EdsReference> getIssueStatuses(boolean... isResolver);

    EdsReference findReferenceByCode(String code);

    List<EdsReference> getParents();

    void deleteChildren(EdsReference reference);

    EdsReference getByCode(String stageCode);

    SelectItem[] getAsSelectItems(String parentCode);

    Integer getLastSorder(String workflowModule);

    EdsReference getReferenceByParentCode(String parentCode);

    void copyStepStatuses(Integer oldParentID, Integer newParentID, Integer fromCompanyID, Integer toCompanyID);

    EdsReference getByName(String name);

    Integer findReferenceId(String parentCode, String code);

    EdsReference findReferenceForCrmAccount(String crmAccountType, String supplier);

    List<String> getFieldNamesByCode(String code);

    Map<Integer, EdsReference> getRefernceByIds(List<Integer> ids);

    Set<EdsReference> getReferenceSetByParentCode(String parentCode);
}
