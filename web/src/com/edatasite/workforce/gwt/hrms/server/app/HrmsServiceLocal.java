package com.edatasite.workforce.gwt.hrms.server.app;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsShift;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.customfields.EdsEmployeeCustomFields;
import com.edatasite.workforce.core.domain.recruitment.EdsGroupPlacement;
import com.edatasite.workforce.core.domain.recruitment.EdsRotation;
import com.edatasite.workforce.gwt.contact.client.rpc.AnnualLeaveItem;
import com.edatasite.workforce.gwt.contact.client.rpc.DependentItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.historyNote.HistoryNote;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.hrms.client.rpc.OnboardingItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.PerformanceNoteItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.ShiftItem;

import java.util.*;

/**
 * User: Ilhombek
 * Date: 4/6/13
 * Time: 2:37 PM
 */
public interface HrmsServiceLocal {

    void updateHolidayDetails(EdsEmployee employee, EdsLocation oldLocation, EdsLocation newLocation);

    void sendEmployeeVisaExpirationDateEmailNotification(Integer recurrenceID, Integer employeeProfileID);

    void sendHrReminders(Integer typeID, Integer userId, Integer companyId);

    void createEmployeeLeaveAllowance(EdsEmployee empl, Map<Integer, AnnualLeaveItem> leaveitems, Integer selectedYear);

    EdsEmployeeCustomFields saveEmployeeCustomFields(EdsEmployeeCustomFields edsEmployeeCustomField, List<CompanyCustomFieldItem> customFieldItems);

    void saveEmployeeDocumentReminder(FileResource item);

    SelectItem[] getSalaryGradeListItems();

    Integer updateProfile(ProfileItem item);

    ProfileItem editProfile(Integer objectID);

    ProfileItem editProfile(Integer objectID, String from);

    ProfileItem editProfile(Integer objectID, String from, boolean isView);

    ProfileItem editProfile(Integer objectID, String from, boolean isView, Integer placementId, String fromType, Integer convertedFormId);

    ProfileItem getProfile(Integer employeeId);

    FileResource[] getRelatedFiles(Integer employeeId);

    LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getEmployeesByTeamsList();

    LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getEmployeesWithTeams(Integer timeslotID);

    SelectItem[] getPositionsListForEmployeeEdit(ListingFilterParameter fp);

    DependentItem[] getDependents(Integer employeeId);

    String getEmployeeImageURL(Integer employeeId);

    ArrayList<OnboardingItem> getOnboardingStepsForListing();

    ListResult<PositionItem> getPositionList(ListingFilterParameter filterParametrs);

    SelectItem[] getLookUpItems(ListingFilterParameter filterParametrs, Integer type);

    String getUserImageUrl(EdsUser user);

    GoalItem editGoal(Integer objectId, String type);

    PerformanceNoteItem getPerformanceNote(Integer int_objectID);

    ListResult<GoalItem> getPersonalGoalList(ListingFilterParameter filterParametrs);

    ListResult<PerformanceNoteItem> getPerformanceNoteList(ListingFilterParameter fp);

    SelectItem[] getGoalLookUpItems(ListingFilterParameter fp);

//    String getCertificateFacetQuery(final ListingFilterParameter fp, final EdsUser user);

    String getPositionFacetQuery(final ListingFilterParameter fp, final EdsUser user);

    String replaceVelocity(String defaultHTML, Integer employeeId, ArrayList<FileResource> relatedFiles, Integer certificateId);

//    SolrQuery getCertificateSolrQuery(ListingFilterParameter fp);

    void createLabourPeriodToEmployee(EdsEmployee employee, Date startDate);

    void insertEmployeePresentTime(EdsShift shift);

    void updateApprove(ShiftItem shiftItem);

    void deleteTimeRecordsByShiftId(Integer shiftId);

    void updateEmployeeByRotation(EdsRotation rotation);

    void convertPlacementItems(EdsGroupPlacement placement);

    Integer saveEmployeeLeaveAllowance(ProfileItem item, EdsUser modifiedBy);

    void deleteLaborPeriodHistory(Integer periodHistoryID);

    ArrayList<HistoryNote> loadLaborPeriodHistory(Integer periodID);

    Integer createlaborPeriodHistory(Integer periodId, HistoryListItem hisItem);

    void updateCandidateStatusOnApproval(Integer candidateId,String status);
}
