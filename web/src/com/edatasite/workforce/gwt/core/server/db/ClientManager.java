package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mansur
 * Date: Jan 8, 2008
 * Time: 4:47:18 PM
 * To change this template use File | Settings | File Templates.
 */

public interface ClientManager extends Manager<EdsCrmAccount> {
    List<EdsCrmAccount> list();

    SelectItem[] list(ListingFilterParameter fp);

    List<EdsCrmAccount> customerList(Integer currencyId);

    List<EdsCrmContact> getContacts(Integer clientId);

    List<EdsCrmAccount> getClientsForInvoice(String searchKey);

    List<EdsCrmAccount> getClientsWithInvoice(EdsCompany company);

    List<EdsCrmAccount> getClientsByRegDate(Date sTime, Date eTime, EdsCompany company);

    List<EdsCrmAccount> getClientByName(String name);

    Boolean deleteClient(EdsCrmAccount client);

    Integer getClientIdByQBCustomerId(String qbCustomerId);

    List<EdsCrmAccount> getClientsByIds(String Ids);

    List<String> getNames(Integer companyID);

    List<Integer> getClientIdsByIds(List<Integer> list);

    void create(EdsCrmAccount client, boolean solrUpdateAlso);

    void update(EdsCrmAccount client, boolean solrUpdateAlso);

    HashMap<String, Integer> getNimbleCrmAccountsMap();

    HashMap<String, Integer> getNimbleUniqueIDsMap();

    Integer checkGWDCustomerForExists(String customerName);

    ArrayList<Integer> getEmployeeClients(Integer employeeId);
}
