package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.core.client.rpc.Address;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 15.09.2010
 * Time: 15:45:07
 * To change this template use File | Settings | File Templates.
 */
public interface AddressManager extends Manager<EdsAddress> {
    List<EdsAddress> getAddressesByEntityIdAndType(Integer entityID, Integer relationType, String entityType);

    List<EdsAddress> getAddressesByCrmAccount(List<Integer> accountIds, String entityType);

    List<EdsAddress> getContactAddresses(Integer contactID);

    Map<Integer, Map<Integer, ArrayList<Address>>> getAddressesByContactIDs(List<Integer> lessObjectIDs);

    void updateLinkedAddresses(EdsCrmAccount account);

    void removeLinkedAddresses(Integer crmAccountID);

    void createAddressesForSubs(EdsCrmAccount account, List<EdsAddress> newAddresses);
}
