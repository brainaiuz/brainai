package com.edatasite.workforce.gwt.hrms.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomTableRpc;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.Relational;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.VacancyItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * User: Ilhombek
 * Date: 7/3/12
 * Time: 4:30 PM
 */
public class PlacementItem extends Relational implements ListingCustomFields {

    public static final String PLACEMENT_ACTION = "PLACEMENT_ACTION";
    public static final String PLACEMENT_CANDIDATE_NAME = "PLACEMENT_CANDIDATE_NAME";
    public static final String PLACEMENT_POSITION_OFFERED = "PLACEMENT_POSITION_OFFERED";
    public static final String PLACEMENT_DATE_OFFERED = "PLACEMENT_DATE_OFFERED";
    public static final String PLACEMENT_STATUS_OFFER = "PLACEMENT_STATUS_OFFER";
    public static final String PLACEMENT_CODE = "PLACEMENT_CODE";

    private Integer objectID;                        //placement ID

    private FileItem[] attachments;                  //Attachments
    private Integer candidateID;                     //Candidate ID
    private String candidateName;                    //Candidate Name
    private SelectItem[] candidates;                 //Candidate List

    private Integer departmentID;                    //Department ID
    private String departmentName;                   //Department Name
    private SelectItem[] departments;                //Department List

    private DateNonConvertable dateNonConvertable;   //Non convertable date

    private boolean editable;                        //Editable option (if placement current status == Hired status, this placement not editable)

    private Integer locationID;                      //Location ID
    private String locationName;                     //Location Name
    private SelectItem[] locations;                  //Location List


    private Integer positionID;                      //Position offered ID
    private String positionName;                     //Position offered name
    private SelectItem[] positions;                  //Position List

    private Integer projectID;                      //Project ID
    private String projectName;                     //Project Name
    private SelectItem[] projects;                  //Project List
    private SelectItem[] templates;
    private ArrayList<VacancyItem> matchedVacancies;

    private Integer statusID;                       //offer status ID
    private String statusName;                      //offer status name
    private String statusCode;                      //offer status code

    private boolean showApproveButton;              //show approve button
    private boolean showHireButton;                 //show hire button
    private boolean approveProcessEnabled;
    private ArrayList<ApproverItemMini> approvers = new ArrayList<>();
    private SelectItem creator;
    private SelectItem approver;
    private ArrayList<SelectItem> vacancies;        //Vacancies
    private ArrayList<HistoryListItem> notes;       //Placement notes
    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private HashMap<String, Object> customFieldsMap;
    private Integer employeeProfileId;
    private HashMap<String, ArrayList<CustomTableRpc>> customTableItems = new HashMap<>();
    private Integer candidateType;
    private NumberData numberData;
    private String placementCode;
    private Integer groupPlacementId;

    private SelectItem candidate;
    private SelectItem department;
    private SelectItem location;
    private SelectItem position;

    private String plannedPlaceCount;
    private String headCount;

    public HashMap<String, ArrayList<CustomTableRpc>> getCustomTableItems() {
        return customTableItems;
    }

    public void setCustomTableItems(HashMap<String, ArrayList<CustomTableRpc>> customTableItems) {
        this.customTableItems = customTableItems;
    }

    private boolean approved;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
    }

    public Integer getCandidateID() {
        return candidateID;
    }

    public void setCandidateID(Integer candidateID) {
        this.candidateID = candidateID;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public SelectItem[] getCandidates() {
        return candidates;
    }

    public SelectItem[] getTemplates() {
        return templates;
    }

    public void setTemplates(SelectItem[] templates) {
        this.templates = templates;
    }

    public ArrayList<VacancyItem> getMatchedVacancies() {
        return matchedVacancies;
    }

    public void setMatchedVacancies(ArrayList<VacancyItem> matchedVacancies) {
        this.matchedVacancies = matchedVacancies;
    }

    public void setCandidates(SelectItem[] candidates) {
        this.candidates = candidates;
    }

    public Integer getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(Integer departmentID) {
        this.departmentID = departmentID;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public SelectItem[] getDepartments() {
        return departments;
    }

    public void setDepartments(SelectItem[] departments) {
        this.departments = departments;
    }

    public Date getDateOffed() {
        return getDateNonConvertable() != null ? getDateNonConvertable().getNonConvertedDate() : null;
    }

    public void setDateOffed(Date dateOffed) {
        this.dateNonConvertable = new DateNonConvertable();
        this.dateNonConvertable.setDate(dateOffed);
    }

    public DateNonConvertable getDateNonConvertable() {
        return /*dateNonConvertable == null ? new DateNonConvertable() : */dateNonConvertable;
    }

    public void setDateNonConvertable(DateNonConvertable dateNonConvertable) {
        this.dateNonConvertable = dateNonConvertable;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public Integer getLocationID() {
        return locationID;
    }

    public void setLocationID(Integer locationID) {
        this.locationID = locationID;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public SelectItem[] getLocations() {
        return locations;
    }

    public void setLocations(SelectItem[] locations) {
        this.locations = locations;
    }

    public Integer getPositionID() {
        return positionID;
    }

    public void setPositionID(Integer positionID) {
        this.positionID = positionID;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public SelectItem[] getPositions() {
        return positions;
    }

    public void setPositions(SelectItem[] positions) {
        this.positions = positions;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public SelectItem[] getProjects() {
        return projects;
    }

    public void setProjects(SelectItem[] projects) {
        this.projects = projects;
    }

    public Integer getStatusID() {
        return statusID;
    }

    public void setStatusID(Integer statusID) {
        this.statusID = statusID;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isShowApproveButton() {
        return showApproveButton;
    }

    public void setShowApproveButton(boolean showApproveButton) {
        this.showApproveButton = showApproveButton;
    }

    public boolean isShowHireButton() {
        return showHireButton;
    }

    public void setShowHireButton(boolean showHireButton) {
        this.showHireButton = showHireButton;
    }

    public ArrayList<SelectItem> getVacancies() {
        return this.vacancies == null ? new ArrayList<>() : vacancies;
    }

    public void setVacancies(ArrayList<SelectItem> vacancies) {
        this.vacancies = vacancies;
    }

    public ArrayList<HistoryListItem> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<HistoryListItem> notes) {
        this.notes = notes;
    }

    public boolean getApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isApproveProcessEnabled() {
        return this.approveProcessEnabled;
    }

    public void setApproveProcessEnabled(final boolean approveProcessEnabled) {
        this.approveProcessEnabled = approveProcessEnabled;
    }

    public ArrayList<ApproverItemMini> getApprovers() {
        return this.approvers;
    }

    public void setApprovers(final ArrayList<ApproverItemMini> approvers) {
        this.approvers = approvers;
    }

    public SelectItem getApprover() {
        return this.approver;
    }

    public void setApprover(final SelectItem approver) {
        this.approver = approver;
    }

    public SelectItem getCreator() {
        return this.creator;
    }

    public void setCreator(final SelectItem creator) {
        this.creator = creator;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }

    @Override
    public Integer getRelationID() {
        return null;
    }

    @Override
    public String getRelationType() {
        return RelationItem.TYPE_PLACEMENT;
    }

    @Override
    public String getRelationName() {
        return null;
    }

    @Override
    public Object getCustomFieldsValue(String columnCodeKey) {
        return getCustomFieldsMap().get(columnCodeKey);
    }

    @Override
    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {
        getCustomFieldsMap().put(columnCodeKey, cellValue);
    }

    public HashMap<String, Object> getCustomFieldsMap() {
        if (customFieldsMap == null) {
            customFieldsMap = new HashMap<>();
        }
        return customFieldsMap;
    }

    public void setCustomFieldsMap(HashMap<String, Object> customFieldsMap) {
        this.customFieldsMap = customFieldsMap;
    }

    public Integer getEmployeeProfileId() {
        return employeeProfileId;
    }

    public void setEmployeeProfileId(Integer employeeProfileId) {
        this.employeeProfileId = employeeProfileId;
    }

    public Integer getCandidateType() {
        return candidateType;
    }

    public void setCandidateType(Integer candidateType) {
        this.candidateType = candidateType;
    }

    public NumberData getNumberData() {
        return this.numberData;
    }

    public void setNumberData(final NumberData numberData) {
        this.numberData = numberData;
    }

    public String getPlacementCode() {
        return this.placementCode;
    }

    public void setPlacementCode(final String placementCode) {
        this.placementCode = placementCode;
    }

    public Integer getGroupPlacementId() {
        return groupPlacementId;
    }


    public void setGroupPlacementId(Integer groupPlacementId) {
        this.groupPlacementId = groupPlacementId;
    }

    public SelectItem getCandidate() {
        return candidate;
    }

    public void setCandidate(SelectItem candidate) {
        this.candidate = candidate;
    }

    public SelectItem getDepartment() {
        return department;
    }

    public void setDepartment(SelectItem department) {
        this.department = department;
    }

    public SelectItem getLocation() {
        return location;
    }

    public void setLocation(SelectItem location) {
        this.location = location;
    }

    public SelectItem getPosition() {
        return position;
    }

    public void setPosition(SelectItem position) {
        this.position = position;
    }

    public String getHeadCount() {
        return headCount;
    }

    public void setHeadCount(String headCount) {
        this.headCount = headCount;
    }

    public String getPlannedPlaceCount() {
        return plannedPlaceCount;
    }

    public void setPlannedPlaceCount(String plannedPlaceCount) {
        this.plannedPlaceCount = plannedPlaceCount;
    }
}