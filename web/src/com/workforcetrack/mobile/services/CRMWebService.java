package com.workforcetrack.mobile.services;

import com.workforcetrack.mobile.rpc.accounting.MTypeItemList;
import com.workforcetrack.mobile.rpc.base.MFacetFilter;
import com.workforcetrack.mobile.rpc.base.MIntegerList;
import com.workforcetrack.mobile.rpc.base.MSelectItemList;
import com.workforcetrack.mobile.rpc.calendar.MAppointmentList;
import com.workforcetrack.mobile.rpc.calendar.MTaskList;
import com.workforcetrack.mobile.rpc.client.MFilterParametrs;
import com.workforcetrack.mobile.rpc.contact.MContactData;
import com.workforcetrack.mobile.rpc.contact.MContactList;
import com.workforcetrack.mobile.rpc.contact.MContactListItem;
import com.workforcetrack.mobile.rpc.crm.MCaseItem;
import com.workforcetrack.mobile.rpc.crm.MCaseList;
import com.workforcetrack.mobile.rpc.crm.MCrmFilterData;
import com.workforcetrack.mobile.rpc.crm.MCrmUrlParam;
import com.workforcetrack.mobile.rpc.crm.MEmailList;
import com.workforcetrack.mobile.rpc.crm.MHistoryList;
import com.workforcetrack.mobile.rpc.crmAccount.MContactCompanyInfo;
import com.workforcetrack.mobile.rpc.crmAccount.MCrmAccountList;
import com.workforcetrack.mobile.rpc.crmAccount.MCrmAccountListItem;
import com.workforcetrack.mobile.rpc.opportunity.MOpportunityList;
import com.workforcetrack.mobile.rpc.opportunity.MOpportunityListItem;

/**
 * Created by IntelliJ IDEA.
 * User: HAveANiceDay
 * Date: 08.07.11
 * Time: 17:34
 * To change this template use File | Settings | File Templates.
 */
public interface CRMWebService {

    MContactList getList(MFilterParametrs fp);

    MContactList getLeadListForExcel(MFilterParametrs fp);

    MContactListItem get(Integer objectID);

    MContactListItem edit(Integer objectID);

    MContactListItem edit();

    Integer save(MContactListItem leadtListItem);

    Boolean delete(Integer objectID, Integer ownerID);

    MSelectItemList lookUp(MFilterParametrs fp, String type);

    //CRM Account CRUD Starts

    MCrmAccountList getCrmAccountList(MFilterParametrs fp);

    MCrmAccountListItem getCrmAccount(Integer objectID);

    MCrmAccountListItem editCrmAccount(Integer objectID);

    Boolean deleteCrmAccount(Integer objectID);

    Integer saveCrmAccount(MCrmAccountListItem mCrmAccountListItem);

    MCrmAccountListItem addCrmAccount();

    MContactCompanyInfo getCrmAccount1(Integer objectID);

    Integer saveLeadForExcel(MContactListItem item);

    MOpportunityList getOpportunityList(MFilterParametrs fp);

    Integer saveOpportunity(MOpportunityListItem mOpportunityListItem);

    MOpportunityListItem editOpportunity();

    MOpportunityListItem editOpportunity(Integer objectID);

    Boolean deleteOpportunity(Integer objectID);

    //FILTER
    MCrmFilterData getFilterData(String type, MFacetFilter facetFilter);

    MCrmFilterData getFilterData(String type);

    // FOR OUTLOOK PLUGIN

    String generateURL(MCrmUrlParam urlParam);

    MTaskList getCrmTasks(MFilterParametrs fp);

    MContactList getListForOutlook(MFilterParametrs fp);

    MContactData getContactDataForOutlook(Integer contactID);

    MTaskList getContactTasks(MFilterParametrs fp, Integer objectID);

    MAppointmentList getContactEvents(MFilterParametrs fp, Integer objectID);

    MHistoryList getContactNotes(MFilterParametrs fp, Integer objectID);

    MOpportunityList getContactOpportunities(MFilterParametrs fp, Integer contactID);

    MCaseList getContactCases(MFilterParametrs fp, Integer contactID);

    MEmailList getContactEmails(MFilterParametrs fp, Integer objectID);

    MTypeItemList getContactQuotes(MFilterParametrs fp, Integer objectID);

    MCaseList getAccountCases(MFilterParametrs fp, Integer objectID);

    MContactList getAccountContacts(MFilterParametrs fp, Integer accountID);

    MOpportunityList getAccountOpportunities(MFilterParametrs fp, Integer objectID);

    // CASE MANAGEMENT
    MCaseList getCaseList(MFilterParametrs fp);

    MCaseItem getCase();

    MCaseItem getCase(Integer objectID);

    Integer saveCase(MCaseItem caseItem);

    Boolean deleteCase(Integer objectID);

    Boolean deleteNote(Integer objectID, String type);

    // FOR EXCEL PLUGIN
    MCaseList getCasesForExcel(MFilterParametrs fp);

    MCaseItem editCase(Integer objectID);

    MCaseItem editCase();

    Integer saveCaseForExcel(MCaseItem caseItem);

    MIntegerList saveCaseListForExcel(MCaseList caseList);
}
