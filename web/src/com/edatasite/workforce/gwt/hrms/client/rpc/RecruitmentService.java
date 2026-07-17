package com.edatasite.workforce.gwt.hrms.client.rpc;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.Utils;
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
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * User: Ilxom Lutfullaev
 * Date: 6/22/12
 * Time: 4:38 PM
 */

public interface RecruitmentService extends RemoteService {

    VacancyItem getVacancyItem(Integer objectID);

    VacancyItem getVacancyItem(Integer objectID, String formType, Integer convertedFormId);

    Integer saveVacancy(VacancyItem item);

    Integer saveCandidate(ContactListItem item);

    ListResult<VacancyItem> getVacancyList(ListingFilterParameter filterParameter);

    ListResult<ContactListItem> listCandidates(ListingFilterParameter filterParameter);

    void deleteVacancy(Integer vacancyID);

    void changeVacancyStatus(Integer vacancyID, Integer statusID);

    NumberData generateVacancyNumber(Integer vacancyID);

    NumberData generateCandidateNumber(Integer candidateID);

    ContactListItem getCandidateById(Integer id);

    HashSet<String> getPermissions(Integer taskID, String context);

    LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getCandidateList();

    //placement methods
    void savePlacement(PlacementItem placementItem, DateNonConvertable hireDate);

    void saveApprovedHiredPlacement(Integer placementID, boolean approved, DateNonConvertable hireDate);

    PlacementItem getPlacementItem(Integer placementID, String formType, Integer converFormId);

    ArrayList<SelectItem> getPlacementVacancies(Integer placementID, Integer candidateID);

    ListResult<PlacementItem> getPlacementList(ListingFilterParameter filterParameter);

    Boolean deletePlacement(Integer placementID);

    ArrayList<SelectItem> getCandidateVacancies(Integer candidateID);

    void saveCandidateVacancies(ArrayList<SelectItem> vacancies, Integer candidateID);

    String changeStatus(String classEntity, ArrayList<Integer> entityIDs, String parentCode, String statusCode);

    ArrayList<Appointment> getCurrentInterviews();

    PlacementItem getCandidateData(Integer candidateID);

    void saveVacancyEditCellValue(VacancyItem rowValue, String columnCodeName);

    void savePlacementEditCellValue(PlacementItem rowValue, String columnCodeName);

    ArrayList<SelectItem> getProjectVacancyItem(Integer objectID, Integer projectID);

    void hireCandidate(Integer candidateID, Integer placementId);

    SelectItem[] getCandidateStatuses();

    SelectItem[] getCandidateSources();

    SelectItem[] getVacancyStatusListItem();

    SelectItem[] getVacancyTypes();

    SelectItem[] getVacancyReligions();

    SelectItem[] getPlacementStatus();

    SelectItem getCandidateProject(Integer candidateID);

    SelectItem[] getOwners();

    SelectItem[] getVacancyJobType();

    SelectItem[] getVacancyJobFamily();

    SelectItem[] getVacancyReqDegree();

    SelectItem[] getPlacementPosition();

    VacancyItem getVacancyQuickData();

    SelectItem[] getVacancyLookUpItems(ListingFilterParameter filterParameter);

    void changeCandidateStatus(ArrayList<Integer> candidateIDs, Integer statusID);

    void updateVacancyStatus(Integer objectId, String statusCode, String note);

    List<HistoryNote> loadVacancyHistory(Integer objectId);

    Integer createVacancyHistory(Integer certificateId, HistoryListItem hisItem);

    ListResult<ContactListItem> getNewKanbanCandidates(ListingFilterParameter filterParameter, SelectItem columnMetadata);

    Integer changeCandidateKanbanOrder(SelectItem columnLayoutData, Integer item, Integer widgetIndex, Integer prevItem, Integer afterItem);

    void deleteVacancyComment(Integer commentID);

    void updateStatusPlacement(Integer objectID, String status, String rejectionReason);

    List<HistoryNote> loadPlacementNoteAndHistory(Integer objectID);


    class App {
        public static RecruitmentServiceAsync get() {
            ServiceDefTarget target = GWT.create(RecruitmentService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/recruitment");
            return (RecruitmentServiceAsync) target;
        }
    }
}
