package com.edatasite.workforce.gwt.hrms.client.rpc;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.VacancyItem;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.historyNote.HistoryNote;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

//import com.edatasite.workforce.gwt.crm.client.rpc.EventItem;



public interface RecruitmentServiceAsync {
    void getVacancyItem(Integer objectID, AsyncCallback<VacancyItem> callback);

    void getVacancyItem(Integer objectID, String formType, Integer convertedFormId, AsyncCallback<VacancyItem> callback);

    void saveVacancy(VacancyItem item, AsyncCallback<Integer> callback);

    Request getVacancyList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<VacancyItem>> callback);

    Request listCandidates(ListingFilterParameter filterParameter, AsyncCallback<ListResult<ContactListItem>> callback);

    void deleteVacancy(Integer vacancyID, AsyncCallback<Void> callback);

    void changeVacancyStatus(Integer vacancyID, Integer statusID, AsyncCallback<Void> callback);

    void generateVacancyNumber(Integer vacancyID, AsyncCallback<NumberData> callback);

    void generateCandidateNumber(Integer candidateID, AsyncCallback<NumberData> callback);

    void saveCandidate(ContactListItem item, AsyncCallback<Integer> abstractAsyncCallback);

    void getPermissions(Integer taskID, String context, AsyncCallback<HashSet<String>> async);

    void getCandidateList(AsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>> callback);

    //placement methods
    void savePlacement(PlacementItem placementItem, DateNonConvertable hireDate, AsyncCallback<Void> callback);

    void saveApprovedHiredPlacement(Integer placementID, boolean approved, DateNonConvertable hireDate, AsyncCallback<Void> callback);

    void getPlacementItem(Integer placementID, String formType, Integer convertFormId, AsyncCallback<PlacementItem> callback);

    void getPlacementVacancies(Integer placementID, Integer candidateID, AsyncCallback<ArrayList<SelectItem>> callback);

    void getPlacementList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<PlacementItem>> callback);

    void deletePlacement(Integer placementID, AsyncCallback<Boolean> callback);

    void getCandidateVacancies(Integer candidateID, AsyncCallback<ArrayList<SelectItem>> callback);

    void saveCandidateVacancies(ArrayList<SelectItem> vacancies, Integer candidateID, AsyncCallback<Void> callback);

    void changeStatus(String classEntity, ArrayList<Integer> entityIDs, String parentCode, String statusCode, AsyncCallback<String> abstractAsyncCallback);

    void getCurrentInterviews(AsyncCallback<ArrayList<Appointment>> callback);

    void  getCandidateData(Integer candidateID, AsyncCallback<PlacementItem> asyncCallback);

    void getProjectVacancyItem(Integer objectID, Integer projectID, AsyncCallback<ArrayList<SelectItem>> callback);

    void saveVacancyEditCellValue(VacancyItem rowValue, String columnCodeName, AsyncCallback<Void> callback);

    void savePlacementEditCellValue(PlacementItem rowValue, String columnCodeName, AsyncCallback<Void> callback);

    void hireCandidate(Integer candidateID, Integer placementId, AsyncCallback<Void> async);

    void getCandidateStatuses(AsyncCallback<SelectItem[]> callback);

    void getCandidateSources(AsyncCallback<SelectItem[]> callback);

    void getVacancyStatusListItem(AsyncCallback<SelectItem[]> callback);

    void getVacancyTypes(AsyncCallback<SelectItem[]> callback);

    void getVacancyReligions(AsyncCallback<SelectItem[]> callback);

    void getPlacementStatus(AsyncCallback<SelectItem[]> callback);

    void getCandidateProject(Integer candidateID, AsyncCallback<SelectItem> asyncCallback);

    void getOwners(AsyncCallback<SelectItem[]> callback);

    void getVacancyJobType(AsyncCallback<SelectItem[]> callback);

    void getVacancyJobFamily(AsyncCallback<SelectItem[]> callback);

    void getVacancyReqDegree(AsyncCallback<SelectItem[]> callback);

    void getPlacementPosition(AsyncCallback<SelectItem[]> callback);

    void getVacancyQuickData(AsyncCallback<VacancyItem> callback);

    void getVacancyLookUpItems(ListingFilterParameter filterParameter, AsyncCallback<SelectItem[]> async);

    void changeCandidateStatus(ArrayList<Integer> ids, Integer statusId, AsyncCallback<Void> callback);

    void updateVacancyStatus(Integer objectId, String statusCode, String note, AsyncCallback<Void> callback);

    void loadVacancyHistory(Integer objectId, AsyncCallback<List<HistoryNote>> callback);

    void getCandidateById(Integer id, AsyncCallback<ContactListItem> async);

    void createVacancyHistory(Integer certificateId, HistoryListItem hisItem, AsyncCallback<Integer> async);

    Request getNewKanbanCandidates(ListingFilterParameter filterParameter, SelectItem columnMetadata, AsyncCallback<ListResult<ContactListItem>> async);

    void changeCandidateKanbanOrder(SelectItem columnLayoutData, Integer itemId, Integer widgetIndex,
                                    Integer prevItemId, Integer afterItemId, AsyncCallback<Integer> async);

    void deleteVacancyComment(Integer commentID, AsyncCallback<Void> callback);

    void updateStatusPlacement(Integer objectID, String status, String rejectionReason, AsyncCallback<Void> async);

    void loadPlacementNoteAndHistory(Integer objectID, AsyncCallback<List<HistoryNote>> async);


}
