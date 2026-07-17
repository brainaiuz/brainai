package com.edatasite.workforce.rest.v2.release10.core;

import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsRelation;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.crm.EdsCase;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductItem;
import com.edatasite.workforce.gwt.accounting.server.app.ProductServiceLocal;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.contact.server.app.ContactServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.CampaignItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.server.app.ListUtils;
import com.edatasite.workforce.gwt.core.server.db.CaseManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EventManager;
import com.edatasite.workforce.gwt.core.server.db.OpportunityManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.RelationManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.crm.client.rpc.CaseList;
import com.edatasite.workforce.gwt.crm.client.rpc.CrmAccountList;
import com.edatasite.workforce.gwt.crm.client.rpc.EventItem;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityListItem;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;
import com.edatasite.workforce.gwt.employee.server.app.EmployeeServiceLocal;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceList;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.server.app.QuoteServiceLocal;
import com.edatasite.workforce.gwt.issue.client.rpc.IssueListItem;
import com.edatasite.workforce.gwt.issue.server.app.IssueServiceLocal;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectListItem;
import com.edatasite.workforce.gwt.project.server.actions.ProjectServiceLocal;
import com.edatasite.workforce.gwt.task.client.rpc.TaskList;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.edatasite.workforce.gwt.task.server.app.TaskServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseListData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.link.LinkListTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.link.LinkTO;
import com.edatasite.workforce.rest.v2.release10.enums.EntityTypeEnum;
import com.edatasite.workforce.rest.v2.release10.enums.LinkTypeEnum;
import com.edatasite.workforce.rest.v2.release10.enums.OrderByEnum;
import com.edatasite.workforce.rest.v2.release10.enums.OrderFieldEnum;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Dilsh0d Madrahimov on 17/03/2018.
 */

@Tag(name = "Link", description = "Link API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiLinkControllerV2 extends BaseApiControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ApiLinkControllerV2.class);

    @Autowired
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private ContactServiceLocal contactServiceLocal;
    @Autowired
    private TaskServiceLocal taskServiceLocal;
    @Autowired
    private IssueServiceLocal issueServiceLocal;
    @Autowired
    private ProjectServiceLocal projectServiceLocal;
    @Autowired
    private EmployeeServiceLocal employeeServiceLocal;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private QuoteServiceLocal quoteServiceLocal;
    @Autowired
    private ProductServiceLocal productServiceLocal;
    @Autowired
    private RelationManager relationManager;
    @Autowired
    private OpportunityManager opportunityManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private EventManager eventManager;
    @Autowired
    private CaseManager caseManager;
    @Autowired
    private ProjectManager projectManager;


    @Operation(summary = "Search Link by query or/and date")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/links/{link_path_name}/search", method = RequestMethod.GET)
    public Object searchLink(
            @PathVariable(value = "link_path_name") String link_path_name,
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "date", required = false) String dateStr) throws RestException {

        if (StringUtils.isBlank(link_path_name)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "link_path_name is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        Date date = null;
        if (StringUtils.isNotBlank(dateStr)) {
            SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);
            try {
                date = longDateTimezoneFormat.parse(dateStr);
            } catch (ParseException e) {
                log.error("", e);
                throw new RestException("Invalid date format", "Invalid date format. Acceptable format is " + longDateTimezoneFormat.toPattern(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }

        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setSearchKey(query);
        filterParameter.setStart(offset != null ? offset : 0);
        filterParameter.setLimit(limit != null ? limit : MAX_LIMIT);
        filterParameter.setSearchButton(true);

        ArrayList<LinkTO> result = new ArrayList<>();

        if (EntityTypeEnum.PROJECTS.name().equalsIgnoreCase(link_path_name)) {

            filterParameter.setLookUp(true);

            ListResult<ProjectListItem> projectListResult;
            try {
                projectListResult = projectServiceLocal.getProjectList(filterParameter);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (projectListResult != null && projectListResult.getList() != null && projectListResult.getList().size() > 0) {
                for (ProjectListItem item : projectListResult.getList()) {
                    result.add(new LinkTO(item.getObjectId(), item.getName(), LinkTypeEnum.PROJECT.name()));
                }
            }

            return successResponse(new ResponseListData<>(result));
        }

        if (EntityTypeEnum.CONTACTS.name().equalsIgnoreCase(link_path_name)) {

            filterParameter.setDetectDuplicates(false);
            filterParameter.setFromMobile(true);
            filterParameter.setLookUp(true);

            ListResult<ContactListItem> contactList;
            try {
                contactList = contactServiceLocal.getNewContactList(filterParameter);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (contactList != null && contactList.getList() != null && contactList.getList().size() > 0) {
                for (ContactListItem item : contactList.getList()) {
                    result.add(new LinkTO(item.getObjectId(), item.getName(), LinkTypeEnum.CONTACT.name()));
                }
            }

            return successResponse(new ResponseListData<>(result));
        }

        if (EntityTypeEnum.LEADS.name().equalsIgnoreCase(link_path_name)) {

            filterParameter.setDetectDuplicates(false);
            filterParameter.setFromMobile(true);

            filterParameter.setContactType(CrmConstants.TYPE_LEAD_CONTACT);
            ListResult<ContactListItem> leadList;
            try {
                leadList = crmServiceLocal.getNewLeads(filterParameter);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (leadList != null && leadList.getList() != null && leadList.getList().size() > 0) {
                for (ContactListItem item : leadList.getList()) {
                    result.add(new LinkTO(item.getObjectId(), item.getName(), LinkTypeEnum.LEAD.name()));
                }
            }

            return successResponse(new ResponseListData<>(result));
        }

        if (EntityTypeEnum.CAMPAIGNS.name().equalsIgnoreCase(link_path_name)) {

            filterParameter.setLookUp(true);

            ListResult<CampaignItem> campaignListResult;
            try {
                campaignListResult = crmServiceLocal.getCampaigns(filterParameter);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (campaignListResult != null && campaignListResult.getList() != null && campaignListResult.getList().size() > 0) {
                for (CampaignItem item : campaignListResult.getList()) {
                    result.add(new LinkTO(item.getObjectId(), item.getName(), LinkTypeEnum.CAMPAIGNS.name()));
                }
            }

            return successResponse(new ResponseListData<>(result));
        }

        if (EntityTypeEnum.OPPORTUNITIES.name().equalsIgnoreCase(link_path_name)) {

            filterParameter.setDetectDuplicates(false);
            filterParameter.setFromMobile(true);

            ListResult<OpportunityListItem> opportunityList;
            try {
                opportunityList = crmServiceLocal.getOpportunityList(filterParameter);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (opportunityList != null && opportunityList.getList() != null && opportunityList.getList().size() > 0) {
                for (OpportunityListItem item : opportunityList.getList()) {
                    result.add(new LinkTO(item.getObjectId(), item.getOpportunityName(), LinkTypeEnum.OPPORTUNITY.name()));
                }
            }

            return successResponse(new ResponseListData<>(result));
        }

        if (EntityTypeEnum.CASES.name().equalsIgnoreCase(link_path_name)) {

            filterParameter.setLookUp(true);

            CaseList caseList;
            try {
                caseList = crmServiceLocal.getCases(filterParameter);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (caseList != null && caseList.getList() != null && caseList.getList().size() > 0) {
                for (CaseItem item : caseList.getList()) {
                    result.add(new LinkTO(item.getObjectId(), item.getSubject(), LinkTypeEnum.CASE.name()));
                }
            }

            return successResponse(new ResponseListData<>(result));
        }

        if (EntityTypeEnum.TASKS.name().equalsIgnoreCase(link_path_name)) {

            filterParameter.setDetectDuplicates(false);
            filterParameter.setFromMobile(true);

            TaskList taskList;
            try {
                taskList = taskServiceLocal.getTaskList(filterParameter);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (taskList != null && taskList.getList() != null && taskList.getList().size() > 0) {
                for (TaskListItem item : taskList.getList()) {
                    result.add(new LinkTO(item.getObjectID(), item.getName(), LinkTypeEnum.TASK.name()));
                }
            }

            return successResponse(new ResponseListData<>(result));
        }

        if (EntityTypeEnum.ISSUES.name().equalsIgnoreCase(link_path_name)) {

            filterParameter.setFromMobile(true);

            ListResult<IssueListItem> issueListResult;
            try {
                issueListResult = issueServiceLocal.getIssuesList(filterParameter);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (issueListResult != null && issueListResult.getList() != null && issueListResult.getList().size() > 0) {
                for (IssueListItem item : issueListResult.getList()) {
                    result.add(new LinkTO(item.getObjectID(), item.getName(), LinkTypeEnum.ISSUE.name()));
                }
            }

            return successResponse(new ResponseListData<>(result));
        }

        if (EntityTypeEnum.EVENTS.name().equalsIgnoreCase(link_path_name)) {

            if (date != null) {
                ArrayList<Date> dates = new ArrayList<>();
                dates.add(date);
                filterParameter.setDates(dates);
            }

            ListResult<EventItem> eventListResult;
            try {
                eventListResult = crmServiceLocal.getEventList(filterParameter);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (eventListResult != null && eventListResult.getList() != null && eventListResult.getList().size() > 0) {
                for (EventItem item : eventListResult.getList()) {
                    result.add(new LinkTO(item.getObjectID(), item.getSubject(), LinkTypeEnum.EVENTS.name()));
                }
            }

            return successResponse(new ResponseListData<>(result));
        }

        if (EntityTypeEnum.CRM_ACCOUNTS.name().equalsIgnoreCase(link_path_name) || EntityTypeEnum.COMPANIES.name().equalsIgnoreCase(link_path_name)) {

            filterParameter.setDetectDuplicates(false);
            filterParameter.setFromMobile(true);
            filterParameter.setCRM(true);

            CrmAccountList crmAccountList;
            try {
                crmAccountList = crmServiceLocal.getCrmAccounts(filterParameter);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (crmAccountList != null && crmAccountList.getList() != null && crmAccountList.getList().size() > 0) {
                for (CrmAccountItem item : crmAccountList.getList()) {
                    result.add(new LinkTO(item.getObjectId(), item.getName(), LinkTypeEnum.CRM_ACCOUNT.name()));
                }
            }

            return successResponse(new ResponseListData<>(result));
        }

        if (EntityTypeEnum.SUPPLIERS.name().equalsIgnoreCase(link_path_name) || EntityTypeEnum.CLIENTS_CUSTOMERS.name().equalsIgnoreCase(link_path_name)) {

            filterParameter.setDetectDuplicates(false);
            filterParameter.setFromMobile(true);
            filterParameter.setCRM(true);
            filterParameter.setAccountType(EntityTypeEnum.SUPPLIERS.name().equalsIgnoreCase(link_path_name) ? EdsCrmAccount.SUPPLIER : EdsCrmAccount.CUSTOMER);

            CrmAccountList crmAccountList;
            try {
                crmAccountList = crmServiceLocal.getCrmAccounts(filterParameter);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (crmAccountList != null && crmAccountList.getList() != null && crmAccountList.getList().size() > 0) {
                String linkType = EntityTypeEnum.SUPPLIERS.name().equalsIgnoreCase(link_path_name) ? LinkTypeEnum.SUPPLIER.name() : LinkTypeEnum.CLIENTS_CUSTOMERS.name();
                for (CrmAccountItem item : crmAccountList.getList()) {
                    result.add(new LinkTO(item.getObjectId(), item.getName(), linkType));
                }
            }

            return successResponse(new ResponseListData<>(result));
        }

        if (EntityTypeEnum.EMPLOYEES.name().equalsIgnoreCase(link_path_name)) {

            ListResult<EmployeeListItem> employeeListResult;
            try {
                employeeListResult = employeeServiceLocal.getEmployeeList(filterParameter);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (employeeListResult != null && employeeListResult.getList() != null && employeeListResult.getList().size() > 0) {
                for (EmployeeListItem item : employeeListResult.getList()) {
                    result.add(new LinkTO(item.getObjectID(), item.getFullName(), LinkTypeEnum.EMPLOYEE.name()));
                }
            }

            return successResponse(new ResponseListData<>(result));
        }

        if (EntityTypeEnum.DEPARTMENTS.name().equalsIgnoreCase(link_path_name)) {

            ArrayList<EdsDepartment> departmentList;
            try {
                departmentList = (ArrayList<EdsDepartment>) departmentManager.list(filterParameter);
                departmentList = ListUtils.getSublistSmart(departmentList, filterParameter.getStart(), filterParameter.getLimit());
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            for (EdsDepartment item : departmentList) {
                result.add(new LinkTO(item.getObjectID(), item.getName(), LinkTypeEnum.DEPARTMENT.name()));
            }

            return successResponse(new ResponseListData<>(result));
        }

        if (EntityTypeEnum.SALES.name().equalsIgnoreCase(link_path_name)) {

            InvoiceList invoiceList;
            try {
                invoiceList = quoteServiceLocal.getSaleQuoteData(filterParameter);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            for (NewInvoice item : invoiceList.getList()) {
                result.add(new LinkTO(item.getID(), item.getInvoiceNumber(), LinkTypeEnum.QUOTE.name()));
            }

            return successResponse(new ResponseListData<>(result));
        }

        if (EntityTypeEnum.PRODUCTS.name().equalsIgnoreCase(link_path_name)) {
            filterParameter.setFromMobile(true);
            ListResult<ProductItem> productListResult;
            try {
                productListResult = productServiceLocal.getProductsListFromSolr(filterParameter);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            for (ProductItem item : productListResult.getList()) {
                result.add(new LinkTO(item.getObjectId(), item.getName(), LinkTypeEnum.PRODUCT.name()));
            }

            return successResponse(new ResponseListData<>(result));
        }

        if (EntityTypeEnum.PURCHASES.name().equalsIgnoreCase(link_path_name)) {

            filterParameter.setFromMobile(true);

            InvoiceList purchaseOrderListResult;
            try {
                purchaseOrderListResult = quoteServiceLocal.getPurchaseOrderData(filterParameter);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            for (NewInvoice item : purchaseOrderListResult.getList()) {
                result.add(new LinkTO(item.getID(), item.getInvoiceNumber(), LinkTypeEnum.PURCHASE_ORDER.name()));
            }

            return successResponse(new ResponseListData<>(result));
        }

        throw new RestException(GENERAL_ERROR_MESSAGE, "link_path_name should be one of leads,opportunities,tasks,contacts,crm_accounts,suppliers,clients_customers,issues,projects,campaigns,cases,events,employees,departments,sales,products,purchases", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);

    }

    @Operation(summary = "Search Link by project")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/links/projects/{id}/{link_path_name}/search", method = RequestMethod.GET)
    public Object searchLinkByProject(@PathVariable(value = "id") Integer id,
                                      @PathVariable(value = "link_path_name") String link_path_name,
                                      @RequestParam(value = "query", required = false) String query,
                                      @RequestParam(value = "limit", required = false) Integer limit,
                                      @RequestParam(value = "offset", required = false) Integer offset) throws RestException {

        if (id == null || id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (StringUtils.isBlank(link_path_name)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "link_path_name is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setSearchKey(query);
        filterParameter.setStart(offset != null ? offset : 0);
        filterParameter.setLimit(limit != null ? limit : MAX_LIMIT);

        ArrayList<LinkTO> result = new ArrayList<>();

        if (EntityTypeEnum.ISSUES.name().equalsIgnoreCase(link_path_name)) {
            filterParameter.setProjectId(id);
            filterParameter.setFromMobile(true);

            ListResult<IssueListItem> issueListResult;
            try {
                issueListResult = issueServiceLocal.getIssuesList(filterParameter);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (issueListResult != null && issueListResult.getList() != null && issueListResult.getList().size() > 0) {
                for (IssueListItem item : issueListResult.getList()) {
                    result.add(new LinkTO(item.getObjectID(), item.getName(), LinkTypeEnum.ISSUE.name()));
                }
            }

            return successResponse(new ResponseListData<>(result));
        }

        if (EntityTypeEnum.TASKS.name().equalsIgnoreCase(link_path_name)) {

            filterParameter.setDetectDuplicates(false);
            filterParameter.setFromMobile(true);
            filterParameter.setProjectId(id);

            TaskList taskList;
            try {
                taskList = taskServiceLocal.getTaskList(filterParameter);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (taskList != null && taskList.getList() != null && taskList.getList().size() > 0) {
                for (TaskListItem item : taskList.getList()) {
                    result.add(new LinkTO(item.getObjectID(), item.getName(), LinkTypeEnum.TASK.name()));
                }
            }

            return successResponse(new ResponseListData<>(result));
        }

        throw new RestException(GENERAL_ERROR_MESSAGE, "link_path_name should be one of tasks,issues", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);

    }

    @Operation(summary = "Update or add links to particular entity")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/{entity_path_name}/{entity_id}/links", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Transactional
    public Object updateLink(@PathVariable(value = "entity_path_name") String entity_path_name,
                             @PathVariable(value = "entity_id") Integer entity_id,
                             @RequestBody LinkListTO linkListTO) throws RestException {

        if (StringUtils.isBlank(entity_path_name)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "entity_path_name is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (entity_id == null || entity_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (EntityTypeEnum.TASKS.name().equalsIgnoreCase(entity_path_name)) {

            EdsTask edsTask = taskManager.get(entity_id);
            if (edsTask == null || edsTask.getDeleted()) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Task with id ".concat(entity_id.toString()).concat(" is not found"), NOT_FOUND, HttpStatus.NOT_FOUND);
            }

            try {
                relationManager.deleteAllRelations(RelationItem.TYPE_TASK, edsTask.getObjectID());
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            ArrayList<RelationItem> relationItems = new ArrayList<>();
            for (LinkTO linkTO : linkListTO.getList()) {
                RelationItem relationItem = new RelationItem();
                relationItem.setFromID(entity_id);
                relationItem.setFromType(RelationItem.TYPE_TASK);
                relationItem.setFromName(edsTask.getName());
                relationItem.setToID(linkTO.getItem_id());
                relationItem.setToType(getEntityRelation(linkTO.getLink_type()));
                relationItem.setToName(linkTO.getName());
                relationItem.setCreatedDate(new Date());
                relationItem.setLastModifiedDate(new Date());

                relationItems.add(relationItem);
            }

            try {
                allInOneServiceLocal.saveRelations(RelationItem.TYPE_TASK, edsTask.getObjectID(), edsTask.getName(), relationItems);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else if (EntityTypeEnum.ISSUES.name().equalsIgnoreCase(entity_path_name)) {

            EdsTask edsTask = taskManager.get(entity_id);
            if (edsTask == null || edsTask.getDeleted()) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Issue with id ".concat(entity_id.toString()).concat(" is not found"), NOT_FOUND, HttpStatus.NOT_FOUND);
            }

            try {
                relationManager.deleteAllRelations(RelationItem.TYPE_ISSUE, edsTask.getObjectID());
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            ArrayList<RelationItem> relationItems = new ArrayList<>();
            for (LinkTO linkTO : linkListTO.getList()) {
                RelationItem relationItem = new RelationItem();
                relationItem.setFromID(entity_id);
                relationItem.setFromType(RelationItem.TYPE_ISSUE);
                relationItem.setFromName(edsTask.getName());
                relationItem.setToID(linkTO.getItem_id());
                relationItem.setToType(getEntityRelation(linkTO.getLink_type()));
                relationItem.setToName(linkTO.getName());
                relationItem.setCreatedDate(new Date());
                relationItem.setLastModifiedDate(new Date());

                relationItems.add(relationItem);
            }

            try {
                allInOneServiceLocal.saveRelations(RelationItem.TYPE_ISSUE, edsTask.getObjectID(), edsTask.getName(), relationItems);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else if (EntityTypeEnum.EVENTS.name().equalsIgnoreCase(entity_path_name) || EntityTypeEnum.CALLS.name().equalsIgnoreCase(entity_path_name)) {

            EdsEvent edsEvent = eventManager.get(entity_id);
            if (edsEvent == null || edsEvent.isDeleted()) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Event/Call with id ".concat(entity_id.toString()).concat(" is not found"), NOT_FOUND, HttpStatus.NOT_FOUND);
            }

            try {
                relationManager.deleteAllRelations(RelationItem.TYPE_EVENT, edsEvent.getObjectID());
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            ArrayList<RelationItem> relationItems = new ArrayList<>();
            for (LinkTO linkTO : linkListTO.getList()) {
                RelationItem relationItem = new RelationItem();
                relationItem.setFromID(entity_id);
                relationItem.setFromType(RelationItem.TYPE_EVENT);
                relationItem.setFromName(edsEvent.getSubject());
                relationItem.setToID(linkTO.getItem_id());
                relationItem.setToType(getEntityRelation(linkTO.getLink_type()));
                relationItem.setToName(linkTO.getName());
                relationItem.setCreatedDate(new Date());
                relationItem.setLastModifiedDate(new Date());

                relationItems.add(relationItem);
            }

            try {
                allInOneServiceLocal.saveRelations(RelationItem.TYPE_EVENT, edsEvent.getObjectID(), edsEvent.getSubject(), relationItems);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else if (EntityTypeEnum.OPPORTUNITIES.name().equalsIgnoreCase(entity_path_name)) {

            EdsOpportunity edsOpportunity = opportunityManager.get(entity_id);
            if (edsOpportunity == null || edsOpportunity.isDeleted()) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Opportunity with id ".concat(entity_id.toString()).concat(" is not found"), NOT_FOUND, HttpStatus.NOT_FOUND);
            }

            try {
                relationManager.deleteAllRelations(RelationItem.TYPE_OPPORTUNITY, edsOpportunity.getObjectID());
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            ArrayList<RelationItem> relationItems = new ArrayList<>();
            for (LinkTO linkTO : linkListTO.getList()) {
                RelationItem relationItem = new RelationItem();
                relationItem.setFromID(entity_id);
                relationItem.setFromType(RelationItem.TYPE_OPPORTUNITY);
                relationItem.setFromName(edsOpportunity.getName());
                relationItem.setToID(linkTO.getItem_id());
                relationItem.setToType(getEntityRelation(linkTO.getLink_type()));
                relationItem.setToName(linkTO.getName());
                relationItem.setCreatedDate(new Date());
                relationItem.setLastModifiedDate(new Date());

                relationItems.add(relationItem);
            }

            try {
                allInOneServiceLocal.saveRelations(RelationItem.TYPE_OPPORTUNITY, edsOpportunity.getObjectID(), edsOpportunity.getName(), relationItems);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else if (EntityTypeEnum.CASES.name().equalsIgnoreCase(entity_path_name)) {

            EdsCase edsCase = caseManager.get(entity_id);
            if (edsCase == null || edsCase.getDeleted()) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Case with id ".concat(entity_id.toString()).concat(" is not found"), NOT_FOUND, HttpStatus.NOT_FOUND);
            }

            try {
                relationManager.deleteAllRelations(RelationItem.TYPE_CASE, edsCase.getObjectID());
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            ArrayList<RelationItem> relationItems = new ArrayList<>();
            for (LinkTO linkTO : linkListTO.getList()) {
                RelationItem relationItem = new RelationItem();
                relationItem.setFromID(entity_id);
                relationItem.setFromType(RelationItem.TYPE_CASE);
                relationItem.setFromName(edsCase.getSubject());
                relationItem.setToID(linkTO.getItem_id());
                relationItem.setToType(getEntityRelation(linkTO.getLink_type()));
                relationItem.setToName(linkTO.getName());
                relationItem.setCreatedDate(new Date());
                relationItem.setLastModifiedDate(new Date());

                relationItems.add(relationItem);
            }

            try {
                allInOneServiceLocal.saveRelations(RelationItem.TYPE_CASE, edsCase.getObjectID(), edsCase.getSubject(), relationItems);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else if (EntityTypeEnum.PROJECTS.name().equalsIgnoreCase(entity_path_name)) {

            EdsProject edsProject = projectManager.get(entity_id);
            if (edsProject == null || edsProject.getDeleted()) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Project with id ".concat(entity_id.toString()).concat(" is not found"), NOT_FOUND, HttpStatus.NOT_FOUND);
            }

            try {
                relationManager.deleteAllRelations(RelationItem.TYPE_PROJECT, edsProject.getObjectID());
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            ArrayList<RelationItem> relationItems = new ArrayList<>();
            for (LinkTO linkTO : linkListTO.getList()) {
                RelationItem relationItem = new RelationItem();
                relationItem.setFromID(entity_id);
                relationItem.setFromType(RelationItem.TYPE_PRODUCT);
                relationItem.setFromName(edsProject.getName());
                relationItem.setToID(linkTO.getItem_id());
                relationItem.setToType(getEntityRelation(linkTO.getLink_type()));
                relationItem.setToName(linkTO.getName());
                relationItem.setCreatedDate(new Date());
                relationItem.setLastModifiedDate(new Date());

                relationItems.add(relationItem);
            }

            try {
                allInOneServiceLocal.saveRelations(RelationItem.TYPE_PROJECT, edsProject.getObjectID(), edsProject.getName(), relationItems);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "entity_path_name should be one of tasks, issues, events, calls, opportunities, cases, projects", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }


        return successResponse(new ResponseData());
    }


    @Operation(summary = "Get Links belongs to particular entity")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/{main_entity_path}/{entity_id}/links", method = RequestMethod.GET)
    public Object getLinksByEntity(
            @PathVariable(value = "main_entity_path") String main_entity_path,
            @PathVariable(value = "entity_id") Integer entity_id,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "sort_type", required = false) String sort_type,
            @RequestParam(value = "direction", required = false) String direction) throws RestException {

        if (StringUtils.isBlank(main_entity_path)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "main_entity_path is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (entity_id == null || entity_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "entity_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        ArrayList<LinkTO> result = new ArrayList<>();

        List<EdsRelation> relationList;

        String entityType = getEntityRelation(main_entity_path);

        if (StringUtils.isBlank(entityType)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "link_path_name should be one of event, call, task, lead, opportunity, company, contact", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setRelationType(entityType);
        filterParameter.setRelationID(entity_id);
        if (OrderFieldEnum.DATE.getField().equalsIgnoreCase(sort_type)) {
            filterParameter.setSortField(OrderFieldEnum.DATE.getField());
        } else if (OrderFieldEnum.NAME.getField().equalsIgnoreCase(sort_type)) {
            filterParameter.setSortField(OrderFieldEnum.NAME.getField());
        } else {
            filterParameter.setSortField(OrderFieldEnum.ID.getField());
        }

        filterParameter.setAscending(OrderByEnum.ASC.getDirection().equalsIgnoreCase(direction));
        filterParameter.setSortDir(OrderByEnum.ASC.getDirection().equalsIgnoreCase(direction) ? OrderByEnum.ASC.getId() : OrderByEnum.DESC.getId());
        filterParameter.setStart(offset != null ? offset : 0);
        filterParameter.setLimit((limit != null && limit > 0) ? limit : MAX_LIMIT);
        filterParameter.setFromMobile(true);

        try {
            relationList = relationManager.getAllRelations(filterParameter);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        for (EdsRelation relation : relationList) {
            String relationType;
            String relationToName;
            Integer id;
            if ((entityType.equals(relation.getFromType()) && entity_id.equals(relation.getFromID()))) {
                relationType = relation.getToType();
                relationToName = relation.getToName();
                id = relation.getToID();
                if (getLinkType(relationType) != null) {
                    result.add(new LinkTO(id, relationToName, getLinkType(relationType)));
                }
            } else if (entity_id.equals(relation.getToID()) && entityType.equals(relation.getToType())) {
                relationType = relation.getFromType();
                relationToName = relation.getFromName();
                id = relation.getFromID();
                if (getLinkType(relationType) != null) {
                    result.add(new LinkTO(id, relationToName, getLinkType(relationType)));
                }
            }
        }

        return successResponse(new ResponseListData<>(result));
    }


}

