package com.workforcetrack.mobile.services;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.EdsEntity;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.emailfetching.mongo.EdsEmail;
import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.contact.server.app.ContactServiceLocal;
import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryList;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.LocalizationType;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetSolrField;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCaseRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrContactRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCrmAccountRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.EntityManager;
import com.edatasite.workforce.gwt.core.server.db.EventManager;
import com.edatasite.workforce.gwt.core.server.db.emailfetching.mongo.EmailRepository;
import com.edatasite.workforce.gwt.crm.client.rpc.ActivityItem;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.CaseList;
import com.edatasite.workforce.gwt.crm.client.rpc.CrmAccountList;
import com.edatasite.workforce.gwt.crm.client.rpc.LeadList;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityListItem;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.invoice.client.rpc.TypeItem;
import com.workforcetrack.mobile.rpc.accounting.MTypeItemList;
import com.workforcetrack.mobile.rpc.attachment.MFileResource;
import com.workforcetrack.mobile.rpc.base.MFacetFilter;
import com.workforcetrack.mobile.rpc.base.MIntegerList;
import com.workforcetrack.mobile.rpc.base.MSelectItemList;
import com.workforcetrack.mobile.rpc.base.WebServiceConstants;
import com.workforcetrack.mobile.rpc.base.WebServiceUtils;
import com.workforcetrack.mobile.rpc.calendar.MAppointment;
import com.workforcetrack.mobile.rpc.calendar.MAppointmentList;
import com.workforcetrack.mobile.rpc.calendar.MTaskList;
import com.workforcetrack.mobile.rpc.calendar.MTaskListItem;
import com.workforcetrack.mobile.rpc.client.MFilterParametrs;
import com.workforcetrack.mobile.rpc.contact.MContactData;
import com.workforcetrack.mobile.rpc.contact.MContactList;
import com.workforcetrack.mobile.rpc.contact.MContactListItem;
import com.workforcetrack.mobile.rpc.crm.MAuditInfo;
import com.workforcetrack.mobile.rpc.crm.MCaseItem;
import com.workforcetrack.mobile.rpc.crm.MCaseList;
import com.workforcetrack.mobile.rpc.crm.MCrmFilterData;
import com.workforcetrack.mobile.rpc.crm.MCrmUrlParam;
import com.workforcetrack.mobile.rpc.crm.MEmailItem;
import com.workforcetrack.mobile.rpc.crm.MEmailList;
import com.workforcetrack.mobile.rpc.crm.MHistoryList;
import com.workforcetrack.mobile.rpc.crm.MHistoryListItem;
import com.workforcetrack.mobile.rpc.crmAccount.MContactCompanyInfo;
import com.workforcetrack.mobile.rpc.crmAccount.MCrmAccountList;
import com.workforcetrack.mobile.rpc.crmAccount.MCrmAccountListItem;
import com.workforcetrack.mobile.rpc.opportunity.MOpportunityList;
import com.workforcetrack.mobile.rpc.opportunity.MOpportunityListItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: HAveANiceDay
 * Date: 08.07.11
 * Time: 20:10
 */
@Transactional
@Service("crmWebService")
public class CRMWebServiceImpl implements CRMWebService, WebServiceConstants {

    @Autowired
    private CRMService crmService;
    @Autowired
    ClientService clientService;
    @Autowired
    @Qualifier("crmService")
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private ContactService contactService;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private EventManager eventManager;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private RbacService rbacService;
    @Autowired
    private AllInOneService allInOneService;
    @Autowired
    private DocumentsService documentsService;

    @Override
    public MContactList getList(MFilterParametrs fp) {
        ListLoadConfig listLoadConfig = fp.convertToListLoadConfig();
        if (fp.getFilter() != null) {
            FacetFilterRpc facetFilterRpc = getFacetFilter(CrmConstants.CRM_LEAD);
            facetFilterRpc.setType(ListPanelType.LeadListPanel);
            facetFilterRpc.setOverallSearch(false);
            setFacetItems(fp.getFilter(), facetFilterRpc, CrmConstants.CRM_LEAD);
            if (fp.getCategoryType() != null) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                try {
                    facetFilterRpc.setStartDate(df.parse(fp.getStartDateStr()));
                    facetFilterRpc.setEndDate(df.parse(fp.getEndDateStr()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                facetFilterRpc.setSelectedDateSolrCodeName(fp.getCategoryType());
            }
            facetFilterRpc.setFilterChanges(true);
            fp.setFacetFilter(facetFilterRpc);
        }

        ListingFilterParameter filterListingParameters = fp.convertToListingFilterParameter(null);
        LeadList leadList = crmServiceLocal.getLeadList(filterListingParameters, listLoadConfig);
        MContactList resultList = new MContactList();
        if (leadList != null && leadList.getLeadListItems() != null && leadList.getLeadListItems().length > 0) {
            resultList.setContactListItems(new ArrayList<>());
            resultList.setTotalCount(leadList.getTotalCount());
            for (ContactListItem leadItem : leadList.getLeadListItems()) {
                resultList.getContactListItems().add(MContactListItem.convertToMobile(leadItem, true, true));
            }
        }
        return resultList;
    }

    @Override
    public MContactList getLeadListForExcel(MFilterParametrs fp) {
        ListLoadConfig listLoadConfig = fp.convertToListLoadConfig();
        ListingFilterParameter filterParameters = fp.convertToFilterParametrs();
        LeadList leadList = crmServiceLocal.getLeadList(filterParameters, listLoadConfig);
        MContactList resultList = new MContactList();
        if (leadList != null && leadList.getLeadListItems() != null && leadList.getLeadListItems().length > 0) {
            resultList.setTotalCount(leadList.getTotalCount());
            resultList.setContactListItems(new ArrayList<>());
            for (ContactListItem leadItem : leadList.getLeadListItems()) {
                ContactListItem lead = crmService.editLead(leadItem.getObjectId(), null);
                resultList.getContactListItems().add(MContactListItem.convertLeadToExcel(lead));
            }
        }
        return resultList;
    }

    @Override
    public MContactListItem get(Integer objectID) {
        if (objectID == null) {
            return null;
        }
        ContactListItem leadItem = crmService.getLead(objectID);
        return MContactListItem.convertToMobile(leadItem, true, false);
    }

    @Override
    public MContactListItem edit(Integer objectID) {

        ContactListItem leadItem = crmService.editLead(objectID, null);
        return MContactListItem.convertToMobile(leadItem, true, false);
    }

    @Override
    public MContactListItem edit() {
        return edit(null);
    }

    @Override
    public Integer save(MContactListItem leadtListItem) {
        if (leadtListItem == null) {
            return -1;
        }
        try {
            ContactListItem contactListItem = null;
            if (leadtListItem.getObjectID() != null && leadtListItem.getObjectID() != 0) {
                contactListItem = crmService.editLead(leadtListItem.getObjectID(), null);
            }
            contactListItem = leadtListItem.convertFromMobile(contactListItem, true);
            return crmService.saveLead(contactListItem, null);
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    @Override
    public Boolean delete(Integer objectID, Integer ownerID) {
        if (objectID == null || ownerID == null || objectID == 0) {
            return null;
        }
        try {
            ArrayList<Integer> contactIDs = new ArrayList<>();
            contactIDs.add(objectID);
            contactIDs = contactService.deleteContacts(contactIDs, ownerID, false);
            return (contactIDs.size() == 0);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }

    @Override
    public MSelectItemList lookUp(MFilterParametrs fp, String type) {
        if (fp == null || type == null || "".equals(type.trim())) {
            return null;
        }
        type = type.trim();
        int typeInt = -1;
        if (CrmConstants.CRM_ACCOUNT.equalsIgnoreCase(type)) {
            typeInt = CrmConstants.CRM_ACCOUNT_ID;
        } else if (CrmConstants.CRM_CONTACT.equalsIgnoreCase(type)) {
            typeInt = CrmConstants.CRM_CONTACT_ID;
        } else if (CrmConstants.CRM_OPPORTUNITY.equalsIgnoreCase(type)) {
            typeInt = CrmConstants.CRM_OPPORTUNITY_ID;
        } else if (CrmConstants.CRM_LEAD.equalsIgnoreCase(type)) {
            typeInt = CrmConstants.CRM_LEAD_ID;
        } else if (CrmConstants.CRM_ACCOUNT.equalsIgnoreCase(type)) {
            typeInt = CrmConstants.CRM_ACCOUNT_ID;
        } else if (CrmConstants.CLIENT.equalsIgnoreCase(type)) {
            typeInt = CrmConstants.CLIENT_ID;
        } else if (CrmConstants.SUPPLIER.equalsIgnoreCase(type)) {
            typeInt = CrmConstants.SUPPLIER_ID;
        } else if (CrmConstants.CRM_CASE.equalsIgnoreCase(type)) {
            typeInt = CrmConstants.CRM_CASE_ID;
        }

        if (typeInt == -1) {
            return null;
        }
        ListingFilterParameter lfp = fp.convertToListingFilterParameter(null);
        return new MSelectItemList(crmService.getLookUpItems(lfp, typeInt).getList().toArray(new SelectItem[]{}));
    }

    @Override
    public MCrmAccountList getCrmAccountList(MFilterParametrs fp) {
        if (fp.getFilter() != null) {
            FacetFilterRpc facetFilterRpc = getFacetFilter(CrmConstants.CRM_ACCOUNT);
            facetFilterRpc.setType(ListPanelType.CrmAccountListPanel);
            facetFilterRpc.setOverallSearch(false);
            setFacetItems(fp.getFilter(), facetFilterRpc, CrmConstants.CRM_ACCOUNT);
            if (fp.getCategoryType() != null) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                try {
                    facetFilterRpc.setStartDate(df.parse(fp.getStartDateStr()));
                    facetFilterRpc.setEndDate(df.parse(fp.getEndDateStr()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                facetFilterRpc.setSelectedDateSolrCodeName(fp.getCategoryType());
            }
            facetFilterRpc.setFilterChanges(true);
            fp.setFacetFilter(facetFilterRpc);
        }

        ListingFilterParameter filterListingParameters = fp.convertToListingFilterParameter(null);
        filterListingParameters.setSortField(null);
        CrmAccountList crmAccountList = crmServiceLocal.getCrmAccounts(filterListingParameters);
        MCrmAccountList resultList = new MCrmAccountList();
        if (crmAccountList != null && crmAccountList.getList() != null && crmAccountList.getList().size() > 0) {
            resultList.setCrmAccountListItems(new ArrayList<>());
            resultList.setTotalCount(crmAccountList.getTotal());
            for (CrmAccountItem accountItem : crmAccountList.getList()) {
                resultList.getCrmAccountListItems().add(MCrmAccountListItem.convertToMobile(accountItem, true));
            }
        }
        return resultList;
    }

    @Override
    public MCrmAccountListItem getCrmAccount(Integer objectID) {
        if (objectID == null) {
            return null;
        }
        MCrmAccountListItem mCrmAccountListItem = MCrmAccountListItem.convertToMobile(crmService.getAccount(objectID, null), false);
        ContactListItem contactListItem = clientService.getPrimaryContact(objectID);
        if (contactListItem != null) {
            mCrmAccountListItem.setContactID(contactListItem.getObjectId());
            mCrmAccountListItem.setContactFirstName(contactListItem.getFirstName());
            mCrmAccountListItem.setContactLastName(contactListItem.getLastName());
            mCrmAccountListItem.setContactEmail(contactListItem.getPrimaryEmail());
            mCrmAccountListItem.setContactPhone(contactListItem.getPrimaryPhone());
        }
        return mCrmAccountListItem;
    }

    @Override
    public MCrmAccountListItem editCrmAccount(Integer objectID) {
        if (objectID == null) {
            return null;
        }
        MCrmAccountListItem mCrmAccountListItem = MCrmAccountListItem.convertToMobile(crmService.editAccount(objectID, null), false);

        ContactListItem contactListItem = clientService.getPrimaryContact(objectID);
        if (contactListItem != null) {
            mCrmAccountListItem.setContactID(contactListItem.getObjectId());
            mCrmAccountListItem.setContactFirstName(contactListItem.getFirstName());
            mCrmAccountListItem.setContactLastName(contactListItem.getLastName());
            mCrmAccountListItem.setContactEmail(contactListItem.getPrimaryEmail());
            mCrmAccountListItem.setContactPhone(contactListItem.getPrimaryPhone());
        }
        return mCrmAccountListItem;
    }

    @Override
    public Boolean deleteCrmAccount(Integer objectID) {
        if (objectID == null || objectID == 0) {
            return false;
        }
        ArrayList<Integer> objectIDs = new ArrayList<>();
        objectIDs.add(objectID);

        try {
            crmService.deleteCrmAccount(objectIDs, false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Integer saveCrmAccount(MCrmAccountListItem mCrmAccountListItem) {
        CrmAccountItem crmAccountItem = null;
        ContactListItem contactItem = null;
        try {
            if (mCrmAccountListItem.getObjectID() != null && !mCrmAccountListItem.getObjectID().equals(0)) {
                crmAccountItem = crmService.editAccount(mCrmAccountListItem.getObjectID(), null);
                contactItem = clientService.getPrimaryContact(mCrmAccountListItem.getObjectID());
            }
            crmAccountItem = mCrmAccountListItem.convertFromMobile(crmAccountItem, contactItem);
            crmAccountItem.setFromMobile(true);
            Integer result = crmService.saveAccount(crmAccountItem, null, null, false, false, false, true);

            return result != null ? result : -1;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    @Override
    public MCrmAccountListItem addCrmAccount() {
        CrmAccountItem crmAccountItem = crmService.editAccount(null, null);
        crmAccountItem.setNumber(crmService.generateAccountNumber(null));
        return MCrmAccountListItem.convertToMobile(crmAccountItem, false);
    }


    @Override
    public MContactCompanyInfo getCrmAccount1(Integer objectID) {
        if (objectID == null) {
            return null;
        }
        MContactCompanyInfo companyInfo;
        companyInfo = new MContactCompanyInfo(crmService.editAccount(objectID, null));
        return companyInfo;
    }

    @Override
    public Integer saveLeadForExcel(MContactListItem item) {
        if (item == null) {
            return null;
        }
        try {
            ContactListItem contactListItem = null;
            if (item.getObjectID() != null && item.getObjectID() != 0) {
                contactListItem = contactService.editContact(ContactListItem.LEAD_CONTACT, item.getObjectID(), null, null, true);
            }
            contactListItem = item.convertLeadFromExcel(contactListItem);
            EdsCrmAccount edsCrmAccount = crmAccountManager.getCrmAccountByName(contactListItem.getCrmAccount().getName(), null);
            return crmService.saveLead(contactListItem, null);

        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }


    @Override
    public MOpportunityList getOpportunityList(MFilterParametrs fp) {
        ListingFilterParameter filterParameters = fp.convertToFilterParametrs();
        return new MOpportunityList(crmService.getOpportunityList(filterParameters));
    }

    @Override
    public Integer saveOpportunity(MOpportunityListItem mOpportunityListItem) {
        OpportunityListItem opportunityListItem = null;
        try {
            if (mOpportunityListItem.getObjectID() != null && mOpportunityListItem.getObjectID() != 0) {
                opportunityListItem = crmService.editOpportunity(mOpportunityListItem.getObjectID());
            }

            opportunityListItem = mOpportunityListItem.convertToNewOpportunity(opportunityListItem);
            SelectItem opportunityItem = crmService.saveOpportunity(opportunityListItem, null);
            return opportunityItem != null ? opportunityItem.getId() : -1;
        } catch (NumberExistingException e) {
            e.printStackTrace();
            return -2;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    @Override
    public MOpportunityListItem editOpportunity() {
        return editOpportunity(null);
    }

    @Override
    public MOpportunityListItem editOpportunity(Integer objectID) {

        OpportunityListItem opportunityListItem = crmService.editOpportunity(objectID);
        return new MOpportunityListItem(opportunityListItem);
    }

    @Override
    public Boolean deleteOpportunity(Integer objectID) {
        //To change body of implemented methods use File | Settings | File Templates.
        if (objectID == null) {
            return false;
        }
        ArrayList<Integer> objectIDs = new ArrayList<>();
        objectIDs.add(objectID);

        try {
            crmService.deleteOpportunity(objectIDs);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public MCrmFilterData getFilterData(String type, MFacetFilter facetFilter) {
        if (type == null || "".equals(type.trim()) || !(CrmConstants.CRM_LEAD.equalsIgnoreCase(type) ||
                CrmConstants.CRM_ACCOUNT.equalsIgnoreCase(type) || CrmConstants.CRM_CONTACT.equalsIgnoreCase(type))) {
            return null;
        }
        ListPanelType listPanelType = null;
        if (CrmConstants.CRM_LEAD.equalsIgnoreCase(type)) {
            type = CrmConstants.CRM_LEAD;
            listPanelType = ListPanelType.LeadListPanel;
        } else if (CrmConstants.CRM_ACCOUNT.equalsIgnoreCase(type)) {
            type = CrmConstants.CRM_ACCOUNT;
            listPanelType = ListPanelType.CrmAccountListPanel;
        } else if (CrmConstants.CRM_CONTACT.equalsIgnoreCase(type)) {
            type = CrmConstants.CRM_CONTACT;
            listPanelType = ListPanelType.ContactListPanel;
        }

        FacetFilterRpc facetFilterRpc = getFacetFilter(type);
        facetFilterRpc.setType(listPanelType);
        facetFilterRpc.setOverallSearch(false);
        setFacetItems(facetFilter, facetFilterRpc, type);

        facetFilterRpc.setFilterChanges(true);
        facetFilterRpc = rbacService.getCRMFacetFilterData(type, facetFilterRpc);

        return getMCrmFilterData(facetFilterRpc, type);
    }

    @Override
    public MCrmFilterData getFilterData(String type) {
        return getFilterData(type, null);
    }

    private MCrmFilterData getMCrmFilterData(FacetFilterRpc facetFilterRpc, String type) {
        MCrmFilterData filterData = new MCrmFilterData();
        //getFacetItems(facetFilterRpc, FacetContentType.TaskFacetFilter.getContentCode()[0])
        String[] contentCodes;
        if (CrmConstants.CRM_LEAD.equals(type)) {
            contentCodes = FacetContentType.LeadFacetFilter.getContentCode();
            filterData.setCampaign(MFacetFilter.getFacetItems(facetFilterRpc, contentCodes[0]));
            filterData.setLeadSource(MFacetFilter.getFacetItems(facetFilterRpc, contentCodes[1]));
            filterData.setStatus(MFacetFilter.getFacetItems(facetFilterRpc, contentCodes[2]));
            filterData.setCountry(MFacetFilter.getFacetItems(facetFilterRpc, contentCodes[3]));
            filterData.setAssignee(MFacetFilter.getFacetItems(facetFilterRpc, contentCodes[5]));
        }

        return filterData;
    }


    private void setFacetItems(MFacetFilter facetFilter, FacetFilterRpc facetFilterRpc, String type) {
        if (facetFilter != null) {
            FacetContentType facetContentType;
            if (CrmConstants.CRM_LEAD.equals(type)) {
                facetContentType = FacetContentType.LeadFacetFilter;
                WebServiceUtils.setFacetItems(facetFilter.getCampaignID(), true, facetFilterRpc, facetContentType, 0);
                WebServiceUtils.setFacetItems(facetFilter.getLeadSourceID(), true, facetFilterRpc, facetContentType, 1);
                WebServiceUtils.setFacetItems(facetFilter.getStatusID(), true, facetFilterRpc, facetContentType, 2);
                WebServiceUtils.setFacetItems(facetFilter.getCountryID(), true, facetFilterRpc, facetContentType, 3);
                WebServiceUtils.setFacetItems(facetFilter.getAssigneeID(), true, facetFilterRpc, facetContentType, 5);
            } else if (CrmConstants.CRM_ACCOUNT.equals(type)) {

            } else if (CrmConstants.CRM_CONTACT.equals(type)) {

            }
        }

    }

    private String getCrmType(String type) {
        String crmType = null;
        if (CrmConstants.CRM_LEAD.equalsIgnoreCase(type)) {
            crmType = CrmConstants.CRM_LEAD;
        } else if (CrmConstants.CRM_ACCOUNT.equalsIgnoreCase(type)) {
            crmType = CrmConstants.CRM_ACCOUNT;
        } else if (CrmConstants.CRM_CONTACT.equalsIgnoreCase(type)) {
            crmType = CrmConstants.CRM_CONTACT;
        }
        return crmType;
    }


    private FacetFilterRpc getFacetFilter(String type) {
        FacetFilterRpc facetFilterRpc = null;

        facetFilterRpc = new FacetFilterRpc(getColumnCode(type), getSolrField(type));
        return facetFilterRpc;
    }

    private ArrayList<String> getColumnCode(String type) {
        FacetContentType facetContentType = null;
        if (CrmConstants.CRM_LEAD.equals(type)) {
            facetContentType = FacetContentType.LeadFacetFilter;
        } else if (CrmConstants.CRM_CONTACT.equals(type)) {
            facetContentType = FacetContentType.ContactFacetFilter;
        } else if (CrmConstants.CRM_ACCOUNT.equals(type)) {
            facetContentType = FacetContentType.CrmAccountFacetFilter;
        }
        ArrayList<String> resultList = new ArrayList<>(Arrays.asList(facetContentType.getContentCode()));
        return resultList;
    }

    private HashMap<String, FacetSolrField> getSolrField(String type) {
        HashMap<String, FacetSolrField> resultSolrField = new HashMap<>();
        FacetSolrField solrField = null;
        String[] contentCodes = null;
        if (CrmConstants.CRM_LEAD.equals(type)) {
            contentCodes = FacetContentType.LeadFacetFilter.getContentCode();
            solrField = new FacetSolrField(SolrContactRepresenter.FIELD_CAMPAIGN_ID, SolrContactRepresenter.FIELD_CAMPAIGN_ID_NAME, LocalizationType.REFERENCE);
            resultSolrField.put(contentCodes[0], solrField);
            solrField = new FacetSolrField(SolrContactRepresenter.FIELD_LEAD_SOURCE_ID, SolrContactRepresenter.FIELD_LEAD_SOURCE_ID_CODE, LocalizationType.REFERENCE);
            resultSolrField.put(contentCodes[1], solrField);
            solrField = new FacetSolrField(SolrContactRepresenter.FIELD_LEAD_STATUS_ID, SolrContactRepresenter.FIELD_LEAD_STATUS_ID_CODE, LocalizationType.REFERENCE);
            resultSolrField.put(contentCodes[2], solrField);
            solrField = new FacetSolrField(SolrContactRepresenter.FIELD_COUNTRY_ID, SolrContactRepresenter.FIELD_COUNTRY_ID_CODE, LocalizationType.COUNTRY);
            resultSolrField.put(contentCodes[3], solrField);
            solrField = new FacetSolrField(SolrContactRepresenter.FIELD_JOB_TITLE, SolrContactRepresenter.FIELD_JOB_TITLE, LocalizationType.REFERENCE, false);
            resultSolrField.put(contentCodes[4], solrField);
            solrField = new FacetSolrField(SolrContactRepresenter.FIELD_LEAD_ASSIGNEE_ID, SolrContactRepresenter.FIELD_LEAD_ASSIGNEE_ID_NAME, LocalizationType.REFERENCE);
            resultSolrField.put(contentCodes[5], solrField);
        } else if (CrmConstants.CRM_CONTACT.equals(type)) {

        } else if (CrmConstants.CRM_ACCOUNT.equals(type)) {

        }
        return resultSolrField;
    }


    @Override
    public String generateURL(MCrmUrlParam urlParam) {
        EdsUser user = crmContactManager.getUser();
        if (urlParam == null || WebServiceUtils.isEmptyOrNull(urlParam.getAction(), urlParam.getEntityName(), user) ||
                (WebServiceUtils.isEmptyOrNull(urlParam.getEntityID()) && urlParam.getAction().equals(WebServiceConstants.EDIT)) ||
                (WebServiceUtils.isEmptyOrNull(urlParam.getAddParams()) && urlParam.getAction().equals(WebServiceConstants.ADD_WITH_PARAMS))) {

            return null;
        }

        Integer entityID = urlParam.getEntityID();
        String entityName = urlParam.getEntityName().toLowerCase();
        String addParams = urlParam.getAddParams();
        Integer actionType = urlParam.getAction();
        boolean isActionTypeRight = true;

        StringBuilder resultURL = new StringBuilder("Crm.html?link=");
        StringBuilder urlBuilder = new StringBuilder(entityName);
        switch (actionType) {
            case ADD -> urlBuilder.append("|add/add/");
            case VIEW -> urlBuilder.append("|summary/" + entityID);

            /*
            case EDIT:
                if (entityName.equalsIgnoreCase("case") || entityName.equalsIgnoreCase("opportunity")) {
                    urlBuilder.append("|add");
                } else {
                    urlBuilder.append("edit|edit");
                }
                urlBuilder.append(entityName + "/" + entityID);
                break;
            */
            case ADD_WITH_PARAMS -> urlBuilder.append("|add/add/").append(addParams + "/fromOutlook");
            case ADD_TASK -> urlBuilder.append("|summary/" + entityID).append("/" + Constants.OPEN_ADD_TASK);
            case ADD_EVENT -> urlBuilder.append("|summary/" + entityID).append("/" + Constants.OPEN_ADD_EVENT);
            case ADD_NOTE -> urlBuilder.append("|summary/" + entityID).append("/" + Constants.OPEN_WRITE_NOTE);
            case ADD_LOG_EVENT -> urlBuilder.append("|summary/" + entityID).append("/" + Constants.OPEN_LOG_CALL);
            default -> isActionTypeRight = false;
        }
        if (isActionTypeRight) {
            resultURL.append(EncryptionHelper.encryptURL(urlBuilder.toString()));
            resultURL.append("&" + Constants.U_ID + "=" + EncryptionHelper.encryptURL(user.getObjectID().toString()));
            resultURL.append("&" + Constants.C_ID + "=" + EncryptionHelper.encryptURL(user.getCompany().getObjectID().toString()));
            return resultURL.toString();
        }

        return null;

    }

    /*==== FOR OUTLOOK PLUGIN ====*/
    @Override
    public MTaskList getCrmTasks(MFilterParametrs fp) {
        ListingFilterParameter lfp = fp.convertToListingFilterParameter(null);
        lfp = fp.convertToListingFilterParameter(lfp);
        lfp.setCrmTaskList(true);
        ListResult<ActivityItem> contactTasks = crmService.getCrmTaskListForActivityTab(lfp);
        return new MTaskList(contactTasks);
    }

    @Override
    public MContactList getListForOutlook(MFilterParametrs fp) {
        ListingFilterParameter filterParameters = fp.convertToListingFilterParameter(null);
        filterParameters.setFromOutlook(true);
        ListResult<ContactListItem> contactList = contactService.getNewContactList(filterParameters);
        MContactList resultList = new MContactList();
        if (contactList != null) {
            resultList.setTotalCount(contactList.getTotal());
            resultList.setContactListItems(MContactList.getContactListItemsForOutlook(contactList.getList()));
        }

        return resultList;
    }

    private SelectItem[] getAsSelectItem(List listOfObject, final int type) {
        return ServerUtils.getAsSelectItem(listOfObject, type);
    }

    private ContactServiceLocal getContactServiceLocal() {
        return (ContactServiceLocal) ApplicationContextProvider.applicationContext.getBean("contactService");
    }

    @Override
    public MContactData getContactDataForOutlook(Integer contactID) {
        MContactData resultData = new MContactData();
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setContactID(contactID);
        fp.setRelationType(RelationItem.TYPE_CONTACT);
        fp.setRelationID(contactID);

        ListResult<ActivityItem> crmTaskEvent = crmService.getCrmTaskListForActivityTab(fp);
        if (crmTaskEvent != null && crmTaskEvent.getList() != null && crmTaskEvent.getList().size() > 0) {
            for (ActivityItem item : crmTaskEvent.getList()) {
                resultData.getTask().add(new MTaskListItem(item));
            }
        }
        crmTaskEvent = crmService.getNewActivityList(fp);
        if (crmTaskEvent != null && crmTaskEvent.getList() != null && crmTaskEvent.getList().size() > 0) {
            for (ActivityItem item : crmTaskEvent.getList()) {
                resultData.getEvent().add(new MAppointment(item));
            }
        }
        HistoryList historyList = crmServiceLocal.getCrmNoteHistory(fp);
        if (historyList != null && historyList.getResult() != null && historyList.getResult().length > 0) {
            for (HistoryListItem item : historyList.getResult()) {
                resultData.getNote().add(new MHistoryListItem(item));
            }
        }

        return resultData;
    }


    private List<MContactListItem> convertForOutlook(ContactListItem[] contacts) {
        List<MContactListItem> resultItems = new ArrayList<>();
        if (contacts != null & contacts.length > 0) {
            for (ContactListItem contact : contacts) {
                resultItems.add(MContactListItem.convertToOutlook(contact));
            }
        }
        return resultItems;
    }

    private ListingFilterParameter getListingFilterParamsForContactRelatedItems(Integer contactID, Integer type) {
        ListingFilterParameter fp = new ListingFilterParameter();
        if (type != null && type.equals(CrmConstants.TYPE_LEAD_CONTACT)) {
            fp.setLeadID(contactID);
            fp.setRelationType(RelationItem.TYPE_LEAD);
        } else {
            fp.setContactID(contactID);
            fp.setRelationType(RelationItem.TYPE_CONTACT);
        }
        fp.setRelationID(contactID);
        fp.setFromOutlook(true);
        return fp;
    }

    @Override
    public MTaskList getContactTasks(MFilterParametrs fp, Integer objectID) {
        ListingFilterParameter lfp = getListingFilterParamsForContactRelatedItems(objectID, fp.getType());
        lfp = fp.convertToListingFilterParameter(lfp);
        lfp.setFromOutlook(false);
        ListResult<ActivityItem> contactTasks = crmService.getCrmTaskListForActivityTab(lfp);

        return new MTaskList(contactTasks);
    }

    @Override
    public MAppointmentList getContactEvents(MFilterParametrs fp, Integer objectID) {
        ListingFilterParameter lfp = getListingFilterParamsForContactRelatedItems(objectID, fp.getType());
        lfp = fp.convertToListingFilterParameter(lfp);
        List<EdsEvent> contactEvents = eventManager.getList(lfp);
        Integer totalCount = eventManager.getListCount(lfp);
        MAppointmentList resultList = new MAppointmentList();
        if (contactEvents != null && contactEvents.size() > 0) {
            resultList.setAppointment(new ArrayList<>());
            resultList.setTotalCount(totalCount);
            for (EdsEvent edsEvent : contactEvents) {
                MAppointment appointment = new MAppointment();
                appointment.setLocation(edsEvent.getVenue());
                appointment.setObjectID(edsEvent.getObjectID());
                appointment.setTitle(edsEvent.getSubject());
                appointment.setStartDate(edsEvent.getStartDate());
                appointment.setDescription(edsEvent.getDescription());
                appointment.setEndDate(edsEvent.getEndDate());
                appointment.setAssignee(edsEvent.getAssignee() != null ? edsEvent.getAssignee().getFullName() : null);
                resultList.getAppointment().add(appointment);
            }
        }
        return resultList;
    }

    @Override
    public MHistoryList getContactNotes(MFilterParametrs fp, Integer objectID) {
        ListingFilterParameter lfp = getListingFilterParamsForContactRelatedItems(objectID, fp.getType());
        lfp = fp.convertToListingFilterParameter(lfp);
        HistoryList historyList = crmServiceLocal.getCrmNoteHistory(lfp);
        return new MHistoryList(historyList);
    }

    @Override
    public MOpportunityList getContactOpportunities(MFilterParametrs fp, Integer contactID) {
        ListingFilterParameter lfp = getListingFilterParamsForContactRelatedItems(contactID, fp.getType());
        lfp = fp.convertToListingFilterParameter(lfp);
        ListResult<OpportunityListItem> opportunityList = crmService.getOpportunityList(lfp);
        return new MOpportunityList(opportunityList);
    }

    @Override
    public MCaseList getContactCases(MFilterParametrs fp, Integer contactID) {
        ListingFilterParameter lfp = fp.convertToListingFilterParameter(null);
        if (fp.getType() != null && fp.getType().equals(CrmConstants.TYPE_LEAD_CONTACT)) {
            lfp.setCrmLeadId(contactID);
        } else {
            lfp.setCrmContactId(contactID);
        }

        lfp.setFromOutlook(true);
        CaseList caseList = crmService.getCases(lfp);
        MCaseList resultList = new MCaseList();
        if (caseList != null && caseList.getList() != null && caseList.getList().size() > 0) {
            resultList.setTotalCount(caseList.getTotal());
            resultList.setCaseItem(new ArrayList<>());
            for (CaseItem item : caseList.getList()) {
                item = crmService.getCase(item.getObjectId(), false);
                resultList.getCaseItem().add(new MCaseItem(item));
            }
        }
        return resultList;
    }

    @Override
    public MEmailList getContactEmails(MFilterParametrs fp, Integer objectID) {
        ListingFilterParameter lfp = getListingFilterParamsForContactRelatedItems(objectID, fp.getType());
        EdsCrmContact contact = crmContactManager.get(objectID);
        if (contact.getEntityID() != null) {
            lfp.setEntityID(contact.getEntityID());
        } else {
            EdsEntity entity = new EdsEntity();
            entityManager.create(entity);
            contact.setEntityID(entity.getObjectID());
            lfp.setEntityID(contact.getEntityID());
        }

        List<EdsEmail> caseEmails = emailRepository.getEmailList(lfp);
        Integer totalCount = emailRepository.getEmailCount(lfp);
        MEmailList resultList = new MEmailList();
        if (caseEmails != null && caseEmails.size() > 0) {
            List<MEmailItem> resultItems = new ArrayList<>();
            for (EdsEmail email : caseEmails) {
                MEmailItem emailItem = new MEmailItem();
                emailItem.setObjectID(email.getId());
                emailItem.setContent(email.getDescription());
                emailItem.setBcc(email.getToBCC());
                emailItem.setCc(email.getToCC());
                emailItem.setCreatedDate(email.getCreatedDate());
                emailItem.setFrom(email.getFrom());
                emailItem.setSubject(email.getSubject());
                emailItem.setReplyTo(email.getReplyTo());
                resultItems.add(emailItem);
            }
            resultList.setTotalCount(totalCount);
            resultList.setEmailItem(resultItems);
        }
        return resultList;
    }

    @Override
    public MTypeItemList getContactQuotes(MFilterParametrs fp, Integer objectID) {
        ListingFilterParameter lfp = fp.convertToListingFilterParameter(null);
        lfp.setContactID(objectID);
        lfp.setQuotesOnly(true);
        TypeItem[] quotesList = crmService.getInvoicesOrQuotes(lfp);
        return new MTypeItemList(quotesList);
    }

    @Override
    public MCaseList getAccountCases(MFilterParametrs fp, Integer objectID) {
        ListingFilterParameter lfp = fp.convertToListingFilterParameter(null);
        lfp.setCrmAccountId(objectID);
        CaseList caseList = crmService.getCases(lfp);
        return new MCaseList(caseList);
    }

    @Override
    public MContactList getAccountContacts(MFilterParametrs fp, Integer accountID) {
        ListingFilterParameter lfp = fp.convertToListingFilterParameter(null);

        FacetFilterRpc facetFilter = new FacetFilterRpc(getContactColumnCode(), getContactSolrField());
        facetFilter.setType(ListPanelType.ContactListPanel);
        SelectItem[] item = new SelectItem[]{new SelectItem(accountID)};
        facetFilter.getFacetContentMap().get(FacetContentType.ContactFacetFilter.getContentCode()[1]).setFacetItems(item);
        lfp.setFacetFilter(facetFilter);

        ListResult<ContactListItem> newContactList = contactService.getNewContactList(lfp);
        MContactList resultList = new MContactList();
        if (newContactList != null && newContactList.getList() != null && newContactList.getList().size() > 0) {
            resultList.setContactListItems(new ArrayList<>());
            resultList.setTotalCount(newContactList.getTotal());
            for (ContactListItem contact : newContactList.getList()) {
                resultList.getContactListItems().add(MContactListItem.convertToOutlook(contact));
            }
        }
        return resultList;

    }

    private HashMap<String, FacetSolrField> getContactSolrField() {
        HashMap<String, FacetSolrField> contactSolrField = new HashMap<>();
        contactSolrField.put(FacetContentType.ContactFacetFilter.getContentCode()[0], new FacetSolrField(SolrContactRepresenter.FIELD_COUNTRY_ID, SolrContactRepresenter.FIELD_COUNTRY_ID_CODE, LocalizationType.REFERENCE));
        contactSolrField.put(FacetContentType.ContactFacetFilter.getContentCode()[1], new FacetSolrField(SolrContactRepresenter.FIELD_CRM_ACCOUNT_ID, SolrContactRepresenter.FIELD_CRM_ACCOUNT_ID, LocalizationType.REFERENCE));
        contactSolrField.put(FacetContentType.ContactFacetFilter.getContentCode()[5], new FacetSolrField(SolrContactRepresenter.FIELD_CATEGORY_ID, SolrContactRepresenter.FIELD_CATEGORY_ID_NAME, LocalizationType.REFERENCE));
        return contactSolrField;
    }

    private ArrayList<String> getContactColumnCode() {
        ArrayList<String> resultList = new ArrayList<>(Arrays.asList(FacetContentType.ContactFacetFilter.getContentCode()));
        return resultList;
    }

    @Override
    public MOpportunityList getAccountOpportunities(MFilterParametrs fp, Integer objectID) {
        ListingFilterParameter lfp = fp.convertToListingFilterParameter(null);
        FacetFilterRpc facetFilterRpc = new FacetFilterRpc(getOpportunityColumnCode(), getOpportunitytSolrField());
        facetFilterRpc.setType(ListPanelType.OpportunitiesListPanel);
        SelectItem[] item = new SelectItem[]{new SelectItem(objectID)};
        facetFilterRpc.getFacetContentMap().get(FacetContentType.OpportunityFacetFilter.getContentCode()[2]).setFacetItems(item);
        lfp.setFacetFilter(facetFilterRpc);
        ListResult<OpportunityListItem> opportunityList = crmService.getOpportunityList(lfp);
        MOpportunityList resultList = new MOpportunityList();
        if (opportunityList != null && opportunityList.getList() != null && opportunityList.getList().size() > 0) {
            resultList.setTotalCount(opportunityList.getTotal());
            resultList.setOpportunityListItem(new ArrayList<>());
            for (OpportunityListItem opportunityItem : opportunityList.getList()) {
                resultList.getOpportunityListItem().add(new MOpportunityListItem(opportunityItem));
            }
        }
        return resultList;
    }

    // CASE
    @Override
    public MCaseList getCaseList(MFilterParametrs fp) {
        if (fp == null) {
            return null;
        }

        ListingFilterParameter lfp = fp.convertToListingFilterParameter(null);
        FacetFilterRpc facetFilter = new FacetFilterRpc(getCaseColumnCodes(), getCaseSolrFields());

        facetFilter.setType(ListPanelType.CaseListPanel);
        if (fp.getFilter() != null) {
            MFacetFilter filter = fp.getFilter();
            WebServiceUtils.setFacetItems(filter.getStatusID(), true, facetFilter, FacetContentType.CaseFacetFilter, 3);
            WebServiceUtils.setFacetItems(filter.getResolverID(), true, facetFilter, FacetContentType.CaseFacetFilter, 6);
            WebServiceUtils.setFacetItems(filter.getCaseOriginID(), true, facetFilter, FacetContentType.CaseFacetFilter, 4);
            WebServiceUtils.setFacetItems(filter.getAssigneeID(), true, facetFilter, FacetContentType.CaseFacetFilter, 5);
        }
        lfp.setFacetFilter(facetFilter);

        CaseList caseList = crmService.getCases(lfp);
        MCaseList resultList = new MCaseList();
        if (caseList != null && caseList.getList() != null && caseList.getList().size() > 0) {
            resultList.setTotalCount(caseList.getTotal());
            resultList.setCaseItem(new ArrayList<>());
            for (CaseItem caseItem : caseList.getList()) {
                //caseItem = crmService.getCase(caseItem.getObjectId());
                resultList.getCaseItem().add(MCaseItem.convertToMobile(caseItem, false));
            }
        }

        return resultList;
    }

    @Override
    public MCaseItem getCase(Integer objectID) {
        CaseItem caseItem = crmService.editCase(objectID, null, null);
        MCaseItem item = MCaseItem.convertToMobile(caseItem, true);
        if (caseItem != null && caseItem.getObjectId() != null && caseItem.getObjectId() > 0) {
            ArrayList<HistoryListItem> notes = allInOneService.getNotes(caseItem.getObjectId(), RelationItem.TYPE_CASE);
            item.setNotes(MHistoryList.convertToMobile(notes));

            ArrayList<FileResource> attachments = documentsService.getFileResources(Constants.F_CASE, item.getObjectID(), item.getObjectID());
            item.setAttachments(MFileResource.convertToMobile(attachments));

            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setRelationID(objectID);
            fp.setRelationType(RelationItem.TYPE_CASE);
            fp.setFromMobile(true);
            ListResult<ActivityItem> activityList = crmService.getNewActivityList(fp);
            item.setActivities(MAppointment.convertFromActivityList(activityList));

            CaseList statusHistoryList = crmService.getCaseChangeHistory(objectID);
            if (statusHistoryList != null && statusHistoryList.getList() != null && statusHistoryList.getList().size() > 0) {
                List<MAuditInfo> historyList = new ArrayList<>();
                for (CaseItem historyItem : statusHistoryList.getList()) {
                    historyList.add(MAuditInfo.convertToMobileCaseInfo(historyItem));
                }
                item.setStatusHistories(historyList);
            }
            Email caseEmail = crmService.getCaseEmail(caseItem.getEmailID(), caseItem.getTrackerID());
            item.setCaseEmail(MEmailItem.convertToMobileCaseInfo(caseEmail));
            item.getCaseEmail().setUserEmail(crmAccountManager.getUser().getEmail());
        }
        SelectItem[] employees = employeeService.getCompanyEmployeesAsSelectItems();
        item.setAssignees(WebServiceUtils.getAsMSelectItemList(employees));

        return item;
    }

    @Override
    public MCaseItem getCase() {
        return getCase(null);
    }

    @Override
    public Integer saveCase(MCaseItem caseItem) {
        Integer saveResult = -1;
        CaseItem item = null;
        try {
            if (caseItem.getObjectID() != null && !caseItem.getObjectID().equals(0)) {
                item = crmService.editCase(caseItem.getObjectID(), null, null);
            }
            item = caseItem.convertFromMobile(item);
            saveResult = crmService.saveCase(item, false).getId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return saveResult;
    }

    @Override
    public Boolean deleteCase(Integer objectID) {
        if (objectID == null || objectID.equals(0)) {
            return null;
        }
        Boolean result = null;
        try {
            crmService.deleteCase(objectID);
            result = Boolean.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
            result = Boolean.FALSE;
        }
        return result;
    }

    @Override
    public Boolean deleteNote(Integer objectID, String type) {
        if (objectID != null && type != null && objectID > 0 && !"".equals(type)) {
            try {
                String realEntityType = type;
                if (type.equalsIgnoreCase(CrmConstants.CRM_CASE)) {
                    realEntityType = CrmConstants.CRM_CASE;
                } else if (type.equalsIgnoreCase(CrmConstants.CRM_CONTACT)) {
                    realEntityType = CrmConstants.CRM_CONTACT;
                } else if (type.equalsIgnoreCase(CrmConstants.CRM_LEAD)) {
                    realEntityType = CrmConstants.CRM_LEAD;
                } else if (type.equalsIgnoreCase(CrmConstants.CRM_ACCOUNT)) {
                    realEntityType = CrmConstants.CRM_ACCOUNT;
                }
                allInOneService.deleteNote(objectID, realEntityType);
                return Boolean.TRUE;
            } catch (Exception e) {
                e.printStackTrace();
                return Boolean.FALSE;
            }
        }
        return Boolean.FALSE;
    }


    @Override
    public MCaseList getCasesForExcel(MFilterParametrs fp) {
        ListingFilterParameter lfp = fp.convertToListingFilterParameter(null);
        FacetFilterRpc facetFilter = new FacetFilterRpc(getCaseColumnCodes(), getCaseSolrFields());

        facetFilter.setType(ListPanelType.CaseListPanel);
        if (fp.getFilter() != null) {
            MFacetFilter filter = fp.getFilter();
            WebServiceUtils.setFacetItems(filter.getStatusID(), true, facetFilter, FacetContentType.CaseFacetFilter, 3);
            WebServiceUtils.setFacetItems(filter.getResolverID(), true, facetFilter, FacetContentType.CaseFacetFilter, 6);
            WebServiceUtils.setFacetItems(filter.getCaseOriginID(), true, facetFilter, FacetContentType.CaseFacetFilter, 4);
            WebServiceUtils.setFacetItems(filter.getAssigneeID(), true, facetFilter, FacetContentType.CaseFacetFilter, 5);
        }
        lfp.setFacetFilter(facetFilter);

        CaseList caseList = crmService.getCases(lfp);
        MCaseList resultList = new MCaseList();
        if (caseList != null && caseList.getList() != null && caseList.getList().size() > 0) {
            resultList.setTotalCount(caseList.getTotal());
            resultList.setCaseItem(new ArrayList<>());
            for (CaseItem caseItem : caseList.getList()) {
                caseItem = crmService.getCase(caseItem.getObjectId(), false);
                resultList.getCaseItem().add(MCaseItem.convertToExcel(caseItem, true));
            }
        }

        return resultList;
    }

    @Override
    public MCaseItem editCase(Integer objectID) {
        CaseItem caseItem = crmService.editCase(objectID, null, null);
        return MCaseItem.convertToExcel(caseItem, false);
    }

    @Override
    public MCaseItem editCase() {
        MCaseItem resultItem = editCase(null);
        SelectItem[] employees = employeeService.getCompanyEmployeesAsSelectItems();
        resultItem.setAssignees(WebServiceUtils.getAsMSelectItemList(employees));
        return resultItem;
    }

    @Override
    public Integer saveCaseForExcel(MCaseItem mCaseItem) {
        if (mCaseItem == null) {
            return null;
        }
        try {
            CaseItem caseItem = null;
            if (mCaseItem.getObjectID() != null && !mCaseItem.equals(0)) {
                caseItem = crmService.getCase(mCaseItem.getObjectID(), false);
            }
            caseItem = mCaseItem.convertFromExcel(caseItem);
            return crmService.saveCase(caseItem, false).getId();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public MIntegerList saveCaseListForExcel(MCaseList caseList) {
        if (caseList == null || caseList.getCaseItem() == null || caseList.getCaseItem().size() == 0) {
            return null;
        }

        MIntegerList resultList = new MIntegerList();
        List<Integer> results = new ArrayList<>();
        for (MCaseItem caseItem : caseList.getCaseItem()) {
            results.add(saveCaseForExcel(caseItem));
        }
        resultList.setResult(results);
        return resultList;
    }

    private HashMap<String, FacetSolrField> getCaseSolrFields() {
        //CaseFacetFilter("reportby", "type", "priority", "status", "caseorign", "assignees", "resolver"),
        HashMap<String, FacetSolrField> caseSolrFieldMap = new HashMap<>();
        caseSolrFieldMap.put(FacetContentType.CaseFacetFilter.getContentCode()[3], new FacetSolrField(SolrCaseRepresenter.STATUS_ID, SolrCaseRepresenter.STATUS_ID_NAME, LocalizationType.REFERENCE));
        caseSolrFieldMap.put(FacetContentType.CaseFacetFilter.getContentCode()[4], new FacetSolrField(SolrCaseRepresenter.CASE_ORIGIN_ID, SolrCaseRepresenter.CASE_ORIGIN_ID_NAME, LocalizationType.REFERENCE));
        caseSolrFieldMap.put(FacetContentType.CaseFacetFilter.getContentCode()[5], new FacetSolrField(SolrCaseRepresenter.CASE_ASSIGNEE_ID, SolrCaseRepresenter.CASE_ASSIGNEE_ID_NAME, LocalizationType.REFERENCE));
        caseSolrFieldMap.put(FacetContentType.CaseFacetFilter.getContentCode()[6], new FacetSolrField(SolrCaseRepresenter.RESOLVER_ID, SolrCaseRepresenter.RESOLVER_ID_NAME, LocalizationType.REFERENCE));
        return caseSolrFieldMap;
    }

    private ArrayList<String> getCaseColumnCodes() {
        ArrayList<String> resultList = new ArrayList<>(Arrays.asList(FacetContentType.CaseFacetFilter.getContentCode()));
        return resultList;
    }


    private HashMap<String, FacetSolrField> getOpportunitytSolrField() {
        HashMap<String, FacetSolrField> opportunitySolrField = new HashMap<>();
        opportunitySolrField.put(FacetContentType.OpportunityFacetFilter.getContentCode()[2], new FacetSolrField(SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_ID, SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_ID_NAME, LocalizationType.REFERENCE));
        return opportunitySolrField;
    }

    private ArrayList<String> getOpportunityColumnCode() {
        ArrayList<String> resultList = new ArrayList<>(Arrays.asList(FacetContentType.OpportunityFacetFilter.getContentCode()));
        return resultList;
    }
}
