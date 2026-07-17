/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/5/5 0:47:32                                                                                             *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterParametrs implements IsSerializable {

    private Integer clientId;
    private Integer clientContactId;
    private Integer contactType;
    private Integer supplierContactId;
    private Integer caseID;
    private String email;
    private Integer projectId;
    private Integer departmentId;
    private Integer employeeId;
    private Integer issueId;
    private Integer viewAsId;
    private Integer taskStatusId;
    private Integer workstreamID;
    private String workstreamName;
    private Integer projectStatusId;
    private Integer invoiceStatusId;
    private Integer backendUsersId;
    private Integer invoiceClientId;
    private Integer bugStatusId;
    private Integer bugPriorityId;
    private Integer timeSheetApprovalSessionStatusId;
    private Integer bugAssigneeId;
    private Integer groupById;
    private String groupByName;
    private Integer timeSlotID;
    private Integer contactID;
    private Integer companyID;
    private Integer leadID;
    private Integer accountID;
    private Integer entityID;
    private Integer opportunityID;
    private int searchType;
    private Integer locationId;
    private Integer countryId;
    private Integer campaignID;
    private Integer messageId;
    private Integer mailListID;
    private String cfColumnCode;    // for custom fields
    private String cfColumnValue;
    private boolean briefly = true;
    private boolean IDsOnly;
    private Boolean allByFilter = false;
    private Boolean forCSVonly = false;
    private Boolean asSelectItem = false;
    private Integer storefrontID;
    private Integer categoryID;
    private Integer[] categories;
    private Boolean doNotSearch = false;
    private Boolean doNotExportToQB = false;
    private ListLoadConfig listLoadConfig;
    private Integer userID;
    private boolean CRM;
    private String clientName;
    private boolean withImage = false;
    private boolean lookUp;
    private String lookUpBy;
    private boolean forChanging = false;
    private boolean invoicesOnly = false;
    private boolean quotesOnly = false;
    private boolean withEncryptedLink = false;
    private Integer statusID;
    private Integer[] StatusIDs;
    private Integer ignoreID;
    private Boolean customFieldsShown = Boolean.FALSE;
    private List<String> columnsOfListing;
    private boolean fromMobile = false;
    private Integer currencyID;
    private HashMap<String, String> customFields;
    private Boolean showVariations;
    private boolean searchByParent;
    private boolean featured;
    private boolean specialOffer;
    //    private Boolean showOnOpportunity;
    private Integer itemId;

    private boolean showWebsiteProducts = false;
    private Integer websiteID;
    private Integer relationID;
    private String relationType;
    private List<Integer> directories;
    private List<Integer> clothestAddresses;
    private Double latitude;
    private Double longitude;


    public FilterParametrs(boolean briefly) {
        this.briefly = briefly;
    }

    /**
     * In the Filter UI it is used as Between startDate and endDate
     */
    private Date startDate;
    private Date endDate;
    private boolean plannedStart;
    private boolean plannedDue;
    private boolean actualStart;
    private boolean actualDue;
    private boolean goalsFlag;
    private Integer taskPriorityId;
    private Integer type;
    private Integer withoutType;
    private String quantityStartValue;
    private String quantityEndValue;
    private String priceStartValue;
    private String priceEndValue;
    private String params;
    private String searchKey;
    private boolean proEmp;
    private boolean cleanTheList = false;
    //For List Config
    private Integer start;
    private Integer limit;
    private String sortField;
    private Integer sortDir;
    private String invoiceType;
    private String statusValues;

    private String accountType;
    private String accountCode;

    private String issueRelatedTo;

    // Especially for accounting
    private long dueDate;
    private long fromDate;
    private long toDate;
    private boolean showBudget;
    private boolean showYTD;

    private Date sickRequestStartDate;
    private Date sickRequestEndDate;
    private String departmentIds;
    private String projectIds;
    private String productNumber;
    private boolean newType = false;
    private boolean checkNumber = false;

    // Dashboard params
    private Integer leaveDayCategory;
    private Integer leaveReqCategory;
    private boolean showProject = false;
    private boolean showEmployee = false;
    private boolean showDepartment = false;
    private boolean showActive = false;
    private boolean showEvent = false;
    private boolean showTasks = false;
    private boolean showIssues = false;
    private boolean showLeaveRequest = false;
    private boolean showPA = false;
    private boolean showHolidays = false;

    //Issue params
    private Integer issueStatusId;
    private Integer issuePriorityId;
    //Resource param
    private boolean resourceIdNull = false;
    private boolean fromCoo;

    //Inventory
    private Integer warehouseID;
    private String facetFilterJSON;
    private FacetFilterRpc facetFilter;

    public Map getRequestParams() {

        Map<String, String> parametersMap = new HashMap<>();

        parametersMap.put("backendUsersId", getAsString(backendUsersId));
        parametersMap.put("bugPriorityId", getAsString(bugPriorityId));
        parametersMap.put("bugStatusId", getAsString(bugStatusId));
        parametersMap.put("bugAssigneeId", getAsString(bugAssigneeId));
        parametersMap.put("clientId", getAsString(clientId));
        parametersMap.put("departmentId", getAsString(departmentId));
        parametersMap.put("employeeId", getAsString(employeeId));
        parametersMap.put("issueId", getAsString(issueId));
        parametersMap.put("endDate", getAsString(endDate));
        parametersMap.put("invoiceClientId", getAsString(invoiceClientId));
        parametersMap.put("invoiceStatusId", getAsString(invoiceStatusId));
        parametersMap.put("priceStartValue", getAsString(priceStartValue));
        parametersMap.put("projectId", getAsString(projectId));
        parametersMap.put("projectStatusId", getAsString(projectStatusId));
        parametersMap.put("quantityEndValue", getAsString(quantityEndValue));
        parametersMap.put("quantityStartValue", getAsString(quantityStartValue));
        parametersMap.put("searchKey", getAsString(searchKey));
        parametersMap.put("params", getAsString(params));
        parametersMap.put("startDate", getAsString(startDate));
        parametersMap.put("taskPriorityId", getAsString(taskPriorityId));
        parametersMap.put("taskStatusId", getAsString(taskStatusId));
        parametersMap.put("timeSheetApprovalSessionStatusId", getAsString(timeSheetApprovalSessionStatusId));
        parametersMap.put("type", getAsString(type));
        parametersMap.put("viewAsId", getAsString(viewAsId));
        parametersMap.put("actualDue", getAsString(actualDue));
        parametersMap.put("actualStart", getAsString(actualStart));
        parametersMap.put("plannedDue", getAsString(plannedDue));
        parametersMap.put("plannedStart", getAsString(plannedStart));
        parametersMap.put("priceEndValue", getAsString(priceEndValue));
        parametersMap.put("start", getAsString(start));
        parametersMap.put("limit", getAsString(limit));
        parametersMap.put("sortField", getAsString(sortField));
        parametersMap.put("sortDir", getAsString(sortDir));
        parametersMap.put("invoiceType", getAsString(invoiceType));
        parametersMap.put("dueDate", getAsString(dueDate));
        parametersMap.put("fromDate", getAsString(fromDate));
        parametersMap.put("toDate", getAsString(toDate));
        parametersMap.put("showBudget", getAsString(showBudget));
        parametersMap.put("showYTD", getAsString(showYTD));
        parametersMap.put("sickRequestStartDate", getAsString(sickRequestStartDate));
        parametersMap.put("sickRequestEndDate", getAsString(sickRequestEndDate));
        parametersMap.put("departmentIds", getAsString(departmentIds));
        parametersMap.put("projectIds", getAsString(projectIds));
        parametersMap.put("leaveDayCategory", getAsString(leaveDayCategory));
        parametersMap.put("leaveReqCategory", getAsString(leaveReqCategory));
        parametersMap.put("showProject", getAsString(showProject));
        parametersMap.put("accountType", getAsString(accountType));
        parametersMap.put("showEvent", getAsString(showEvent));
        parametersMap.put("showTasks", getAsString(showTasks));
        parametersMap.put("showIssues", getAsString(showIssues));
        parametersMap.put("showLeaveRequest", getAsString(showLeaveRequest));
        parametersMap.put("showPA", getAsString(showPA));
        parametersMap.put("showHolidays", getAsString(showHolidays));
        parametersMap.put("issueStatusId", getAsString(issueStatusId));
        parametersMap.put("issuePriorityId", getAsString(issuePriorityId));
        parametersMap.put("cfColumnCode", getAsString(cfColumnCode));
        parametersMap.put("cfColumnValue", getAsString(cfColumnValue));
        parametersMap.put("groupByName", getAsString(groupByName));
        parametersMap.put("searchType", getAsString(searchType));
        parametersMap.put("statusValues", getAsString(statusValues));
        parametersMap.put("accountID", getAsString(accountID));
        parametersMap.put("allByFilter", getAsString(allByFilter));
        parametersMap.put("clientName", getAsString(clientName));
        parametersMap.put("forCSVonly", getAsString(forCSVonly));
        parametersMap.put("asSelectItem", getAsString(asSelectItem));
        parametersMap.put("doNotSearch", getAsString(doNotSearch));
        parametersMap.put("facetFilterJSON", getAsString(facetFilterJSON));
        parametersMap.put("customFieldsShown", getAsString(customFieldsShown));
        parametersMap.put("messageID", getAsString(messageId));
        parametersMap.put("goalsFlag", getAsString(goalsFlag));
        return parametersMap;
    }

    private String getAsString(Object value) {
        if (value == null) {
            return null;
        } else {
            return value.toString();
        }
    }

    public void setRequestParams(Map parametersMap) {
        for (Map map : (Iterable<Map>) parametersMap.entrySet()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) map;

            if (entry.getKey().equals("backendUsersId")) {
                if (entry.getValue() != null) {
                    try {
                        setBackendUsersId(Integer.valueOf(entry.getValue()));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("bugPriorityId")) {
                if (entry.getValue() != null) {
                    try {
                        setBugPriorityId(Integer.valueOf(entry.getValue()));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("bugStatusId")) {
                if (entry.getValue() != null) {
                    try {
                        setBugStatusId(Integer.valueOf(entry.getValue()));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("bugAssigneeId")) {
                if (entry.getValue() != null) {
                    try {
                        setBugAssigneeId(Integer.valueOf(entry.getValue()));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("clientId")) {
                if (entry.getValue() != null) {
                    try {
                        setClientId(Integer.valueOf(entry.getValue()));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("departmentId")) {
                if (entry.getValue() != null) {
                    try {
                        setDepartmentId(Integer.valueOf(entry.getValue()));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("employeeId")) {
                if (entry.getValue() != null) {
                    try {
                        setEmployeeId(Integer.valueOf(entry.getValue()));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("issueId")) {
                if (entry.getValue() != null) {
                    try {
                        setIssueId(Integer.valueOf(entry.getValue()));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("endDate")) {
                if (entry.getValue() != null) {
                    try {
                        setEndDate(new Date(entry.getValue()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("invoiceClientId")) {
                if (entry.getValue() != null) {
                    try {
                        setInvoiceClientId(Integer.valueOf(entry.getValue()));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (entry.getKey().equals("invoiceStatusId")) {
                if (entry.getValue() != null) {
                    try {
                        setInvoiceStatusId(Integer.valueOf(entry.getValue()));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("priceStartValue")) {
                if (entry.getValue() != null) {
                    setPriceStartValue(entry.getValue());
                }
            }
            if (entry.getKey().equals("projectId")) {
                if (entry.getValue() != null) {
                    try {
                        setProjectId(Integer.valueOf(entry.getValue()));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("projectStatusId")) {
                if (entry.getValue() != null) {
                    try {
                        setProjectStatusId(Integer.valueOf(entry.getValue()));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("quantityEndValue")) {
                if (entry.getValue() != null) {
                    setQuantityEndValue(entry.getValue());
                }
            }
            if (entry.getKey().equals("quantityStartValue")) {
                if (entry.getValue() != null) {
                    setQuantityStartValue(entry.getValue());
                }
            }
            if (entry.getKey().equals("searchKey")) {
                if (entry.getValue() != null) {
                    setSearchKey(entry.getValue());
                }
            }
            if (entry.getKey().equals("params")) {
                if (entry.getValue() != null) {
                    setParams(entry.getValue());
                }
            }
            if (entry.getKey().equals("startDate")) {
                if (entry.getValue() != null) {
                    try {
                        setStartDate(new Date(entry.getValue()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("taskPriorityId")) {
                if (entry.getValue() != null) {
                    try {
                        setTaskPriorityId(Integer.valueOf(entry.getValue()));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("taskStatusId")) {
                if (entry.getValue() != null) {
                    try {
                        setTaskStatusId(Integer.valueOf(entry.getValue()));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (entry.getKey().equals("timeSheetApprovalSessionStatusId")) {
                if (entry.getValue() != null) {
                    try {
                        setTimeSheetApprovalSessionStatusId(Integer.valueOf(entry.getValue()));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("type")) {
                if (entry.getValue() != null) {
                    try {
                        setType(Integer.valueOf(entry.getValue()));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("viewAsId")) {
                if (entry.getValue() != null) {
                    try {
                        setViewAsId(Integer.valueOf(entry.getValue()));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("actualDue")) {
                if (entry.getValue() != null) {
                    try {
                        setActualDue(Boolean.parseBoolean(entry.getValue()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("actualStart")) {
                if (entry.getValue() != null) {
                    try {
                        setActualStart(Boolean.parseBoolean(entry.getValue()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("plannedDue")) {
                if (entry.getValue() != null) {
                    try {
                        setPlannedDue(Boolean.parseBoolean(entry.getValue()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("plannedStart")) {
                if (entry.getValue() != null) {
                    try {
                        setPlannedStart(Boolean.parseBoolean(entry.getValue()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("priceEndValue")) {
                if (entry.getValue() != null) {
                    setPriceEndValue(entry.getValue());
                }
            }
            if (entry.getKey().equals("start")) {
                if (entry.getValue() != null) {
                    try {
                        setStart(Integer.valueOf(entry.getValue()));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("limit")) {
                if (entry.getValue() != null) {
                    try {
                        setLimit(Integer.valueOf(entry.getValue()));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (entry.getKey().equals("sortField")) {
                if (entry.getValue() != null) {
                    setSortField(entry.getValue());
                }
            }
            if (entry.getKey().equals("sortDir")) {
                if (entry.getValue() != null) {
                    try {
                        setSortDir(Integer.valueOf(entry.getValue()));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("invoiceType")) {
                if (entry.getValue() != null) {
                    setInvoiceType(entry.getValue());
                }
            }
            if (entry.getKey().equals("dueDate")) {
                if (entry.getValue() != null) {
                    try {
                        setDueDate(Long.valueOf(entry.getValue()));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("fromDate")) {
                if (entry.getValue() != null) {
                    try {
                        setFromDate(Long.valueOf(entry.getValue()));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("toDate")) {
                if (entry.getValue() != null) {
                    try {
                        setToDate(Long.valueOf(entry.getValue()));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("showBudget")) {
                if (entry.getValue() != null) {
                    try {
                        setShowBudget(Boolean.parseBoolean(entry.getValue()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("showYTD")) {
                if (entry.getValue() != null) {
                    try {
                        setShowYTD(Boolean.parseBoolean(entry.getValue()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("sickRequestStartDate")) {
                if (entry.getValue() != null) {
                    try {
                        setSickRequestStartDate(new Date(entry.getValue()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("sickRequestEndDate")) {
                if (entry.getValue() != null) {
                    try {
                        setSickRequestEndDate(new Date(entry.getValue()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            if (entry.getKey().equals("departmentIds")) {
                if (entry.getValue() != null) {
                    setDepartmentIds(entry.getValue());
                }
            }
            if (entry.getKey().equals("projectIds")) {
                if (entry.getValue() != null) {
                    setProjectIds(entry.getValue());
                }
            }
            if (entry.getKey().equals("leaveDayCategory")) {
                if (entry.getValue() != null) {
                    try {
                        setLeaveDayCategory(Integer.valueOf(entry.getValue()));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("leaveReqCategory")) {
                if (entry.getValue() != null) {
                    try {
                        setLeaveReqCategory(Integer.valueOf(entry.getValue()));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("showProject")) {
                if (entry.getValue() != null) {
                    try {
                        setShowProject(Boolean.parseBoolean(entry.getValue()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("accountType")) {
                if (entry.getValue() != null) {
                    setAccountType(entry.getValue());
                }
            }
            if (entry.getKey().equals("showEvent")) {
                if (entry.getValue() != null) {
                    try {
                        setShowEvent(Boolean.parseBoolean(entry.getValue()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("showTasks")) {
                if (entry.getValue() != null) {
                    try {
                        setShowTasks(Boolean.parseBoolean(entry.getValue()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("showIssues")) {
                if (entry.getValue() != null) {
                    try {
                        setShowIssues(Boolean.parseBoolean(entry.getValue()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("showLeaveRequest")) {
                if (entry.getValue() != null) {
                    try {
                        setShowLeaveRequest(Boolean.parseBoolean(entry.getValue()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            if (entry.getKey().equals("showPA")) {
                if (entry.getValue() != null) {
                    try {
                        setShowPA(Boolean.parseBoolean(entry.getValue()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("showHolidays")) {
                if (entry.getValue() != null) {
                    try {
                        setShowHolidays(Boolean.parseBoolean(entry.getValue()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("issueStatusId")) {
                if (entry.getValue() != null) {
                    try {
                        setIssueStatusId((Integer.valueOf(entry.getValue())));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("issuePriorityId")) {
                if (entry.getValue() != null) {
                    try {
                        setIssuePriorityId(Integer.valueOf(entry.getValue()));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("cfColumnCode")) {
                if (entry.getValue() != null) {
                    setCfColumnCode(entry.getValue());
                }
            }
            if (entry.getKey().equals("cfColumnValue")) {
                if (entry.getValue() != null) {
                    setCfColumnValue(entry.getValue());
                }
            }
            if (entry.getKey().equals("groupByName")) {
                if (entry.getValue() != null) {
                    setGroupByName(entry.getValue());
                }
            }
            if (entry.getKey().equals("searchType")) {
                if (entry.getValue() != null) {
                    try {
                        setSearchType(Integer.valueOf(entry.getValue()));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("statusValues")) {
                if (entry.getValue() != null) {
                    setStatusValues(entry.getValue());
                }
            }
            if (entry.getKey().equals("allByFilter")) {
                if (entry.getValue() != null) {
                    setAllByFilter(Boolean.valueOf(entry.getValue()).booleanValue());
                }
            }
            if (entry.getKey().equals("clientName")) {
                if (entry.getValue() != null) {
                    setClientName(entry.getValue());
                }
            }
            if (entry.getKey().equals("forCSVonly")) {
                if (entry.getValue() != null) {
                    setForCSVonly(Boolean.valueOf(entry.getValue()));
                }
            }
            if (entry.getKey().equals("asSelectItem")) {
                if (entry.getValue() != null) {
                    setAsSelectItem(Boolean.valueOf(entry.getValue()));
                }
            }
            if (entry.getKey().equals("doNotSearch")) {
                if (entry.getValue() != null) {
                    setDoNotSearch(Boolean.valueOf(entry.getValue()));
                }
            }
            if (entry.getKey().equals("accountID")) {
                if (entry.getValue() != null) {
                    try {
                        setAccountID(Integer.valueOf(entry.getValue()));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (entry.getKey().equals("customFieldsShown")) {
                if (entry.getValue() != null) {
                    setCustomFieldsShown(Boolean.valueOf(entry.getValue()).booleanValue());
                }
            }
            if (entry.getKey().equals("goalsFlag")) {
                if (entry.getValue() != null) {
                    try {
                        setAllGoals(Boolean.parseBoolean(entry.getValue()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public FilterParametrs(Integer clientId, Integer projectId,
                           Integer departmentId, Integer employeeId, Integer viewAsId) {
        this.clientId = clientId;
        this.projectId = projectId;
        this.departmentId = departmentId;
        this.employeeId = employeeId;
        this.viewAsId = viewAsId;
    }

    public boolean isValidSearchKey() {
        if (this.searchKey == null || "".equals(this.searchKey)) {
            return false;
        }
        if (this.searchKey.trim().length() > 20) {
            searchKey = searchKey.substring(0, 20);
        }
        if (this.searchKey.trim().split(" ").length > 3) {
            String[] searchKeys = this.searchKey.trim().split(" ");
            this.searchKey = "";
            String delimitr = "";
            for (String miniSearchKey : searchKeys) {
                this.searchKey += delimitr + miniSearchKey;
                delimitr = " ";
            }
        }
        return (
                this.searchKey != null
                        && !this.searchKey.trim().equals("")
                        && this.searchKey.trim().split(" ").length < 4 && this.searchKey.trim().length() <= 20
        );
    }

    public String getSearchKeyPrepared() {
        if (isValidSearchKey()) {
            return ((isLookUp() ? "" : " ") + this.searchKey.trim() + " ").replace("'", "''").replace(" ", "%").toLowerCase();
        } else {
            return null;
        }
    }

    public String getLookUpSearchKey() {
        if (isValidSearchKey()) {
            return (this.searchKey.trim() + " ").replace("'", "''").replace(" ", "%").toLowerCase();
        } else {
            return null;
        }
    }

    public Integer getTimeSlotID() {
        return timeSlotID;
    }

    public void setTimeSlotID(Integer timeSlotID) {
        this.timeSlotID = timeSlotID;
    }

    public Integer getGroupById() {
        return groupById;
    }

    public void setGroupById(Integer groupById) {
        this.groupById = groupById;
    }

    public String getGroupByName() {
        return groupByName;
    }

    public void setGroupByName(String groupByName) {
        this.groupByName = groupByName;
    }

    public boolean isProEmp() {
        return proEmp;
    }

    public void setProEmp(boolean proEmp) {
        this.proEmp = proEmp;
    }

    public boolean isPlannedStart() {
        return plannedStart;
    }

    public void setPlannedStart(boolean plannedStart) {
        this.plannedStart = plannedStart;
    }

    public boolean isPlannedDue() {
        return plannedDue;
    }

    public void setPlannedDue(boolean plannedDue) {
        this.plannedDue = plannedDue;
    }

    public boolean isActualStart() {
        return actualStart;
    }

    public void setActualStart(boolean actualStart) {
        this.actualStart = actualStart;
    }

    public boolean isActualDue() {
        return actualDue;
    }

    public void setActualDue(boolean actualDue) {
        this.actualDue = actualDue;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getTaskStatusId() {
        return taskStatusId;
    }

    public void setTaskStatusId(Integer taskStatusId) {
        this.taskStatusId = taskStatusId;
    }

    public Integer getTimeSheetApprovalSessionStatusId() {
        return timeSheetApprovalSessionStatusId;
    }

    public void setTimeSheetApprovalSessionStatusId(Integer timeSheetApprovalSessionStatusId) {
        this.timeSheetApprovalSessionStatusId = timeSheetApprovalSessionStatusId;
    }

    public Integer getProjectStatusId() {
        return projectStatusId;
    }

    public void setProjectStatusId(Integer projectStatusId) {
        this.projectStatusId = projectStatusId;
    }

    public FilterParametrs() {
    }

    public Integer getViewAsId() {
        return viewAsId;
    }

    public void setViewAsId(Integer viewAsId) {
        this.viewAsId = viewAsId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getClientContactId() {
        return clientContactId;
    }

    public void setClientContactId(Integer clientContactId) {
        this.clientContactId = clientContactId;
    }

    public Integer getContactType() {
        return contactType;
    }

    public void setContactType(Integer contactType) {
        this.contactType = contactType;
    }

    public Integer getSupplierContactId() {
        return supplierContactId;
    }

    public void setSupplierContactId(Integer supplierContactId) {
        this.supplierContactId = supplierContactId;
    }

    public Integer getCaseID() {
        return caseID;
    }

    public void setCaseID(Integer caseID) {
        this.caseID = caseID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getIssueId() {
        return issueId;
    }

    public void setIssueId(Integer issueId) {
        this.issueId = issueId;
    }

    public void setInvoiceStatusId(Integer invoiceStatusId) {
        this.invoiceStatusId = invoiceStatusId;
    }

    public Integer getInvoiceStatusId() {
        return invoiceStatusId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setDueDate(Date dueDate) {
        this.endDate = dueDate;
    }

    public void setInvoiceClientId(Integer invoiceClientId) {
        this.invoiceClientId = invoiceClientId;
    }

    public Integer getInvoiceClientId() {
        return invoiceClientId;
    }

    public Integer getTaskPriorityId() {
        return taskPriorityId;
    }

    public void setTaskPriorityId(Integer taskPriorityId) {
        this.taskPriorityId = taskPriorityId;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public String getQuantityStartValue() {
        return quantityStartValue;
    }

    public void setQuantityStartValue(String quantityStartValue) {
        this.quantityStartValue = quantityStartValue;
    }

    public String getQuantityEndValue() {
        return quantityEndValue;
    }

    public void setQuantityEndValue(String quantityEndValue) {
        this.quantityEndValue = quantityEndValue;
    }

    public String getPriceStartValue() {
        return priceStartValue;
    }

    public void setPriceStartValue(String priceStartValue) {
        this.priceStartValue = priceStartValue;
    }

    public String getPriceEndValue() {
        return priceEndValue;
    }

    public void setPriceEndValue(String priceEndValue) {
        this.priceEndValue = priceEndValue;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public Integer getBackendUsersId() {
        return backendUsersId;
    }

    public void setBackendUsersId(Integer backendUsersId) {
        this.backendUsersId = backendUsersId;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public Integer getBugPriorityId() {
        return bugPriorityId;
    }

    public void setBugPriorityId(Integer bugPriorityId) {
        this.bugPriorityId = bugPriorityId;
    }

    public Integer getBugStatusId() {
        return bugStatusId;
    }

    public void setBugStatusId(Integer bugStatusId) {
        this.bugStatusId = bugStatusId;
    }

    public Integer getBugAssigneeId() {
        return bugAssigneeId;
    }

    public void setBugAssigneeId(Integer bugAssigneeId) {
        this.bugAssigneeId = bugAssigneeId;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public Integer getSortDir() {
        return sortDir;
    }

    public void setSortDir(Integer sortDir) {
        this.sortDir = sortDir;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getIssueRelatedTo() {
        return issueRelatedTo;
    }

    public void setIssueRelatedTo(String issueRelatedTo) {
        this.issueRelatedTo = issueRelatedTo;
    }

    public long getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    public long getFromDate() {
        return fromDate;
    }

    public void setFromDate(long fromDate) {
        this.fromDate = fromDate;
    }

    public long getToDate() {
        return toDate;
    }

    public void setToDate(long toDate) {
        this.toDate = toDate;
    }

    public boolean isShowBudget() {
        return showBudget;
    }

    public void setShowBudget(boolean showBudget) {
        this.showBudget = showBudget;
    }

    public boolean isShowYTD() {
        return showYTD;
    }

    public void setShowYTD(boolean showYTD) {
        this.showYTD = showYTD;
    }

    public Date getSickRequestStartDate() {
        return sickRequestStartDate;
    }

    public void setSickRequestStartDate(Date sickRequestStartDate) {
        this.sickRequestStartDate = sickRequestStartDate;
    }

    public Date getSickRequestEndDate() {
        return sickRequestEndDate;
    }

    public void setSickRequestEndDate(Date sickRequestEndDate) {
        this.sickRequestEndDate = sickRequestEndDate;
    }

    public String getDepartmentIds() {
        return departmentIds;
    }

    public void setDepartmentIds(String departmentIds) {
        this.departmentIds = departmentIds;
    }

    public String getProjectIds() {
        return projectIds;
    }

    public void setProjectIds(String projectIds) {
        this.projectIds = projectIds;
    }

    public Integer getLeaveDayCategory() {
        return leaveDayCategory;
    }

    public void setLeaveDayCategory(Integer leaveDayCategory) {
        this.leaveDayCategory = leaveDayCategory;
    }

    public Integer getLeaveReqCategory() {
        return leaveReqCategory;
    }

    public void setLeaveReqCategory(Integer leaveReqCategory) {
        this.leaveReqCategory = leaveReqCategory;
    }

    public boolean isShowProject() {
        return showProject;
    }

    public void setShowProject(boolean showProject) {
        this.showProject = showProject;
    }

    public Integer getContactID() {
        return contactID;
    }

    public void setContactID(Integer contactID) {
        this.contactID = contactID;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public Integer getLeadID() {
        return leadID;
    }

    public void setLeadID(Integer leadID) {
        this.leadID = leadID;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public Integer getMailListID() {
        return mailListID;
    }

    public void setMailListID(Integer mailListID) {
        this.mailListID = mailListID;
    }

    public Integer getAccountID() {
        return accountID;
    }

    public void setAccountID(Integer accountID) {
        this.accountID = accountID;
    }

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public Integer getOpportunityID() {
        return opportunityID;
    }

    public void setOpportunityID(Integer opportunityID) {
        this.opportunityID = opportunityID;
    }

    public String getStatusValues() {
        return statusValues;
    }

    public void setStatusValues(String statusValues) {
        this.statusValues = statusValues;
    }

    public boolean isShowEmployee() {
        return showEmployee;
    }

    public void setShowEmployee(boolean showEmployee) {
        this.showEmployee = showEmployee;
    }

    public boolean isShowDepartment() {
        return showDepartment;
    }

    public void setShowDepartment(boolean showDepartment) {
        this.showDepartment = showDepartment;
    }

    public int getSearchType() {
        return searchType;
    }

    public void setSearchType(int searchType) {
        this.searchType = searchType;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public boolean isShowActive() {
        return showActive;
    }

    public void setShowActive(boolean showActive) {
        this.showActive = showActive;
    }

    public boolean isShowEvent() {
        return showEvent;
    }

    public void setShowEvent(boolean showEvent) {
        this.showEvent = showEvent;
    }

    public boolean isShowIssues() {
        return showIssues;
    }

    public void setShowIssues(boolean showIssues) {
        this.showIssues = showIssues;
    }

    public boolean isShowTasks() {
        return showTasks;
    }

    public void setShowTasks(boolean showTasks) {
        this.showTasks = showTasks;
    }

    public boolean isShowLeaveRequest() {
        return showLeaveRequest;
    }

    public void setShowLeaveRequest(boolean showLeaveRequest) {
        this.showLeaveRequest = showLeaveRequest;
    }

    public boolean isShowPA() {
        return showPA;
    }

    public void setShowPA(boolean showPA) {
        this.showPA = showPA;
    }

    public boolean isShowHolidays() {
        return showHolidays;
    }

    public void setShowHolidays(boolean showHolidays) {
        this.showHolidays = showHolidays;
    }

    public Integer getCampaignID() {
        return campaignID;
    }

    public void setCampaignID(Integer campaignID) {
        this.campaignID = campaignID;
    }

    public Integer getIssueStatusId() {
        return issueStatusId;
    }

    public void setIssueStatusId(Integer issueStatusId) {
        this.issueStatusId = issueStatusId;
    }

    public Integer getIssuePriorityId() {
        return issuePriorityId;
    }

    public void setIssuePriorityId(Integer issuePriorityId) {
        this.issuePriorityId = issuePriorityId;
    }

    public boolean isResourceIdNull() {
        return resourceIdNull;
    }

    public void setResourceIdNull(boolean resourceIdNull) {
        this.resourceIdNull = resourceIdNull;
    }

    public String getCfColumnCode() {
        return cfColumnCode;
    }

    public void setCfColumnCode(String cfColumnCode) {
        this.cfColumnCode = cfColumnCode;
    }

    public String getCfColumnValue() {
        return cfColumnValue;
    }

    public void setCfColumnValue(String cfColumnValue) {
        this.cfColumnValue = cfColumnValue;
    }

    public Integer getWorkstreamID() {
        return workstreamID;
    }

    public void setWorkstreamID(Integer workstreamID) {
        this.workstreamID = workstreamID;
    }

    public String getWorkstreamName() {
        return workstreamName;
    }

    public void setWorkstreamName(String workstreamName) {
        this.workstreamName = workstreamName;
    }

    public Integer getWarehouseID() {
        return warehouseID;
    }

    public void setWarehouseID(Integer warehouseID) {
        this.warehouseID = warehouseID;
    }

    public String getFacetFilterJSON() {
        return facetFilterJSON;
    }

    public void setFacetFilterJSON(String facetFilterJSON) {
        this.facetFilterJSON = facetFilterJSON;
    }

    public FacetFilterRpc getFacetFilter() {
        return facetFilter;
    }

    public void setFacetFilter(FacetFilterRpc facetFilter) {
        this.facetFilter = facetFilter;
    }

    public boolean isBriefly() {
        return briefly;
    }

    public void setBriefly(boolean briefly) {
        this.briefly = briefly;
    }

    public void setIDsOnly(boolean IDsOnly) {
        this.IDsOnly = IDsOnly;
    }

    public boolean isIDsOnly() {
        return IDsOnly;
    }

    public boolean isFromCoo() {
        return fromCoo;
    }

    public void setFromCoo(boolean fromCoo) {
        this.fromCoo = fromCoo;
    }

    public Boolean isAllByFilter() {
        return allByFilter;
    }

    public void setAllByFilter(boolean allByFilter) {
        this.allByFilter = allByFilter;
    }

    public Boolean isForCSVonly() {
        if (forCSVonly == null) {
            forCSVonly = Boolean.FALSE;
        }
        return forCSVonly;
    }

    public void setForCSVonly(Boolean forCSVonly) {
        this.forCSVonly = forCSVonly;
    }

    public void setAsSelectItem(Boolean asSelectItem) {
        this.asSelectItem = asSelectItem;
    }

    public Boolean isAsSelectItem() {
        return asSelectItem;
    }

    public Integer getStorefrontID() {
        return storefrontID;
    }

    public void setStorefrontID(Integer storefrontID) {
        this.storefrontID = storefrontID;
    }

    public Integer getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }

    public Integer[] getCategories() {
        return categories;
    }

    public void setCategories(Integer[] categories) {
        this.categories = categories;
    }

    public void setDoNotSearch(Boolean doNotSearch) {
        this.doNotSearch = doNotSearch;
    }

    public Boolean isDoNotSearch() {
        return doNotSearch;
    }

    public Boolean isDoNotExportToQB() {
        return doNotExportToQB;
    }

    public void setDoNotExportToQB(Boolean doNotExportToQB) {
        this.doNotExportToQB = doNotExportToQB;
    }

    public Boolean getAllByFilter() {
        return allByFilter;
    }

    public void setAllByFilter(Boolean allByFilter) {
        this.allByFilter = allByFilter;
    }

    public boolean isCleanTheList() {
        return cleanTheList;
    }

    public void setCleanTheList(boolean cleanTheList) {
        this.cleanTheList = cleanTheList;
    }

    public void setListLoadConfig(ListLoadConfig listLoadConfig) {
        this.listLoadConfig = listLoadConfig;
    }

    public ListLoadConfig getListLoadConfig() {
        return listLoadConfig;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setCRM(boolean CRM) {
        this.CRM = CRM;
    }

    public boolean isCRM() {
        return CRM;
    }

    public void setWithImage(boolean withImage) {
        this.withImage = withImage;
    }

    public boolean isWithImage() {
        return withImage;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public boolean isLookUp() {
        return lookUp;
    }

    public void setLookUp(boolean lookUp) {
        this.lookUp = lookUp;
    }

    public String getLookUpBy() {
        return lookUpBy;
    }

    public void setLookUpBy(String lookUpBy) {
        this.lookUpBy = lookUpBy;
    }

    public Boolean getCustomFieldsShown() {
        return customFieldsShown;
    }

    public void setCustomFieldsShown(Boolean customFieldsShown) {
        this.customFieldsShown = customFieldsShown;
    }

    public boolean isForChanging() {
        return forChanging;
    }

    public void setForChanging(boolean forChanging) {
        this.forChanging = forChanging;
    }

    public boolean isInvoicesOnly() {
        return invoicesOnly;
    }

    public void setInvoicesOnly(boolean invoicesOnly) {
        this.invoicesOnly = invoicesOnly;
    }

    public boolean isQuotesOnly() {
        return quotesOnly;
    }

    public void setQuotesOnly(boolean quotesOnly) {
        this.quotesOnly = quotesOnly;
    }

    public boolean isWithEncryptedLink() {
        return withEncryptedLink;
    }

    public void setWithEncryptedLink(boolean withEncryptedLink) {
        this.withEncryptedLink = withEncryptedLink;
    }

    public Integer getStatusID() {
        return statusID;
    }

    public void setStatusID(Integer statusID) {
        this.statusID = statusID;
    }

    public Integer[] getStatusIDs() {
        return StatusIDs;
    }

    public void setStatusIDs(Integer[] statusIDs) {
        StatusIDs = statusIDs;
    }

    public Integer getIgnoreID() {
        return ignoreID;
    }

    public void setIgnoreID(Integer ignoreID) {
        this.ignoreID = ignoreID;
    }

    public Boolean isCustomFieldsShown() {
        return customFieldsShown;
    }

    public void setCustomFieldsShown(boolean customFieldsShown) {
        this.customFieldsShown = customFieldsShown;
    }

    public List<String> getColumnsOfListing() {
        return columnsOfListing;
    }

    public void setColumnsOfListing(List<String> columnsOfListing) {
        this.columnsOfListing = columnsOfListing;
    }

    public boolean isFromMobile() {
        return fromMobile;
    }

    public void setFromMobile(boolean fromMobile) {
        this.fromMobile = fromMobile;
    }

    public Integer getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(Integer currencyID) {
        this.currencyID = currencyID;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public HashMap<String, String> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(HashMap<String, String> customFields) {
        this.customFields = customFields;
    }

    public Integer getWithoutType() {
        return withoutType;
    }

    public void setWithoutType(Integer withoutType) {
        this.withoutType = withoutType;
    }

    public Boolean getShowVariations() {
        return showVariations;
    }

    public void setShowVariations(Boolean showVariations) {
        this.showVariations = showVariations;
    }

    public boolean isSearchByParent() {
        return searchByParent;
    }

    public void setSearchByParent(boolean searchByParent) {
        this.searchByParent = searchByParent;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public boolean isSpecialOffer() {
        return specialOffer;
    }

    public void setSpecialOffer(boolean specialOffer) {
        this.specialOffer = specialOffer;
    }

//    public Boolean isShowOnOpportunity() {
//        return showOnOpportunity;
//    }

//    public void setShowOnOpportunity(Boolean showOnOpportunity) {
//        this.showOnOpportunity = showOnOpportunity;
//    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public boolean isShowWebsiteProducts() {
        return showWebsiteProducts;
    }

    public void setShowWebsiteProducts(boolean showWebsiteProducts) {
        this.showWebsiteProducts = showWebsiteProducts;
    }

    public Integer getWebsiteID() {
        return websiteID;
    }

    public void setWebsiteID(Integer websiteID) {
        this.websiteID = websiteID;
    }

    public Integer getRelationID() {
        return relationID;
    }

    public void setRelationID(Integer relationID) {
        this.relationID = relationID;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public boolean getAllGoals() {
        return goalsFlag;
    }

    public void setAllGoals(boolean goalsFlag) {
        this.goalsFlag = goalsFlag;
    }

    public List<Integer> getDirectories() {
        return directories;
    }

    public void setDirectories(List<Integer> directories) {
        this.directories = directories;
    }

    public List<Integer> getClothestAddresses() {
        return clothestAddresses;
    }

    public void setClothestAddresses(List<Integer> clothestAddresses) {
        this.clothestAddresses = clothestAddresses;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public boolean isNewType() {
        return newType;
    }

    public void setNewType(boolean newType) {
        this.newType = newType;
    }

    public boolean isCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(boolean checkNumber) {
        this.checkNumber = checkNumber;
    }
}

