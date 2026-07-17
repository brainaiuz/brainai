/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/4/27 2:8:35                                                                                             *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;

import java.util.List;
import java.util.Map;

public interface ClientContactManager extends Manager<EdsClientContact> {

    String CLIENT_CONTACT = "clientContact";
    String CLIENT = "client";
    String COMPANY = "company";

//    public List<EdsClientContact> getClientContacts(EdsObject object, boolean allInEntity);

    List<Object[]> getClientsByCompany(EdsCompany company);

    List<EdsClientContact> getAccessEnabledContacts(EdsCrmAccount client);

//    public List<EdsClientContact> getNotAssignedClientContacts();

//    public List getClientContactUsers(String username, Integer companyID);

//    public EdsClientContact getClientContact(Integer objectId);

//    public EdsClientContact getClientContactByUsername(String username);

    EdsCrmContact getPrimaryClientContact(Integer clientID);

    EdsCrmContact getPrimarySupplierContact(Integer supplierID);

    /*public void insertClientID(Integer clientID, Integer id) throws EdsDbException;*/
//    List getSupplierContactUsers(String email, Integer companyID);

    Integer getClientIdByClientContact(Integer clinetContactID);

    List<String> getClientContactEmailsByCompany(Integer companyID);

    Map<String, String[]> checkEmailExistence(Integer entityID, String[] email);

    List<EdsClientContact> getClientContactByImportFile(Integer importFileID);

//    List<EdsClientContact> getClientContactsByEntityID(Integer crmEntityID);

//    EdsCrmContact getCrmContactDetails(EdsClientContact clientContact);

    EdsClientContact getClientContactByCrmContact(Integer crmContactID);

    Map<Integer, Boolean> getMapIdAndIsActive(List<Integer> clientContactIDs);

    boolean isContactExist(Integer objectID, String type);

    List<EdsClientContact> getAccessEnabledContactList();

    Integer getClientContactsCount(String email);

    EdsClientContact getClientContactByEmail(String email);

    EdsClientContact getAccessEnabledContactByEmail(String email);

    EdsClientContact getClientByCrmContactAndByUserId(Integer crmContactID, Integer userID);
}
