package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AddressManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 15.09.2010
 * Time: 15:45:46
 * To change this template use File | Settings | File Templates.
 */
@Repository("addressManager")
public class AddressManagerImpl extends BaseManager<EdsAddress> implements AddressManager {
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private CrmContactManager crmContactManager;

    public AddressManagerImpl() {
        super(EdsAddress.class);
    }

    @Override
    public List<EdsAddress> getAddressesByEntityIdAndType(Integer entityID, Integer relationType, String entityType) {
        return (List<EdsAddress>) find("select addr from EdsAddress addr where addr.entityID = ? and addr.relationType = ? and addr.entityType = ? and (addr.deleted <> true or addr.deleted is null)", entityID, relationType, entityType);
    }

    @Override
    public List<EdsAddress> getAddressesByCrmAccount(List<Integer> accountIds, String entityType) {
        return find("select i from EdsAddress i where i.entityType = '" + entityType + "' and  i.accountID IN (" + ServerUtils.getAsCommoDelimited(accountIds, "0") + ")  order by i.name ");
    }

    @Override
    public List<EdsAddress> getContactAddresses(Integer contactID) {
        return find("select address from EdsAddress address where address.deleted = false " +
                "   and address.entityID = ? " +
                "   and address.entityType = '" + EdsAddress.ENTITY_TYPE_CONTACT + "'", contactID);
    }

    @Override
    public Map<Integer, Map<Integer, ArrayList<Address>>> getAddressesByContactIDs(List<Integer> lessObjectIDs) {
        Map<Integer, Map<Integer, ArrayList<Address>>> map = new HashMap<>();
        List<Object[]> addresses = findNative("select " +
                "add.entityid as contactID, " +
                "add.relationtype as relationID, " +
                "add.address as street, " +
                "add.addressb as street2, " +
                "add.city as city, " +
                "add.countryId as countryID, " +
                "country.name as countryName, " +
                "add.stateid as stateID, " +
                "state.name as stateName, " +
                "add.zipCode as zipCode" +
                " from " + getCompanyId() + ".address add " +
                "left join " + getPublic() + ".country country on country.id = add.countryid " +
                "left join " + getPublic() + ".region state on state.id = add.stateid " +
                "where add.deleted is not true and add.entitytype ='" + EdsAddress.ENTITY_TYPE_CONTACT + "' and add.entityid in (" + ServerUtils.getAsCommoDelimited(lessObjectIDs, "0", ",") + ")");
        for (Object[] address : addresses) {
            Integer contactID = address[0] != null ? Integer.parseInt(address[0].toString()) : null;
            Integer relationID = address[1] != null ? Integer.parseInt(address[1].toString()) : null;
            String street = address[2] != null ? String.valueOf(address[2]) : null;
            String street2 = address[3] != null ? String.valueOf(address[3]) : null;
            String city = address[4] != null ? String.valueOf(address[4]) : null;
            Integer countryID = address[5] != null ? Integer.parseInt(address[5].toString()) : null;
            String country = address[6] != null ? String.valueOf(address[6]) : null;
            Integer stateID = address[7] != null ? Integer.parseInt(address[7].toString()) : null;
            String state = address[8] != null ? String.valueOf(address[8]) : null;
            String zipCode = address[9] != null ? String.valueOf(address[9]) : null;
            if (!map.containsKey(contactID)) {
                map.put(contactID, new HashMap<>());
            }
            if (!map.get(contactID).containsKey(relationID)) {
                map.get(contactID).put(relationID, new ArrayList<>());
            }
            map.get(contactID).get(relationID).add(new Address(relationID, street, street2, city, countryID, country, stateID, state, zipCode));
        }
        return map;
    }

    /**
     * hamma linked bulgan addresslarni o'chirish kerak, update qilishdan oldin. bundan keyingi methodda ularni deleted=false qilib ketish kerak.
     *
     * @param crmAccountID
     */
    @Override
    public void removeLinkedAddresses(Integer crmAccountID) {
        updateNative("update " + getCompanyId() + ".address set deleted = true where isLinkedAddress is true and linkedAddressID in (select s.id from " + getCompanyId() + ".address s where s.entityType = '" + EdsAddress.ENTITY_TYPE_CRM_ACCOUNT + "' and s.entityid = " + crmAccountID + ")");
    }

    @Override
    public void createAddressesForSubs(EdsCrmAccount account, List<EdsAddress> newAddresses) {
        if (newAddresses != null && newAddresses.size() > 0) {
            List<EdsCrmAccount> subAccounts = crmAccountManager.getAllSubAccounts(account, true);
            List<Integer> accountIDs = EdsObject.getObjectIDs(subAccounts);
            accountIDs.add(account.getObjectID());
            List<EdsCrmContact> subContacts = crmContactManager.getContactsByCrmAccounts(accountIDs);
            accountIDs.remove(account.getObjectID());
            List<Integer> contactIDs = EdsObject.getObjectIDs(subContacts);
            for (EdsAddress newAddress : newAddresses) {
                String address = quote(newAddress.getAddress());
                String addressB = quote(newAddress.getAddressb());
                String city = quote(newAddress.getCity());
                String zipCode = quote(newAddress.getZipCode());
                Integer stateID = newAddress.getState() != null ? newAddress.getState().getObjectID() : null;
                Integer countryID = newAddress.getCountry() != null ? newAddress.getCountry().getObjectID() : null;
                if (!EdsAddress.MAILING_ADDRESS.equals(newAddress.getRelationType())) {//contact uchun
                    if (contactIDs != null && contactIDs.size() > 0) {
                        StringBuilder sql = new StringBuilder("insert into " + getCompanyId() + ".address(address, addressb, city, zipCode, stateid, countryid,entitytype, entityID, contactid,islinkedaddress, linkedaddressid, relationType) values");
                        boolean vergul = false;
                        for (Integer contactID : contactIDs) {
                            sql.append(vergul ? "," : "").append("(").append(address).append(",").append(addressB).append(",").append(city).append(",").append(zipCode).append(",").append(stateID).append(",").append(countryID).append(",").append(quote(EdsAddress.ENTITY_TYPE_CONTACT)).append(",").append(contactID).append(",").append(contactID).append(",").append("true").append(",").append(newAddress.getObjectID()).append(",").append(EdsAddress.WORK).append(")");
                            vergul = true;
                        }
                        updateNative(sql.toString());
                    }
                }
                if (accountIDs != null && accountIDs.size() > 0) {
                    StringBuilder sql = new StringBuilder("insert into " + getCompanyId() + ".address(address, addressb, city, zipCode, stateid, countryid,entitytype, entityID, accountid,islinkedaddress, linkedaddressid, relationType) values");
                    boolean vergul = false;
                    for (Integer accountID : accountIDs) {
                        sql.append(vergul ? "," : "").append("(").append(address).append(",").append(addressB).append(",").append(city).append(",").append(zipCode).append(",").append(stateID).append(",").append(countryID).append(",").append(quote(newAddress.getEntityType())).append(",").append(accountID).append(",").append(accountID).append(",").append("true").append(",").append(newAddress.getObjectID()).append(",").append(newAddress.getRelationType()).append(")");
                        vergul = true;
                    }
                    updateNative(sql.toString());
                }
            }
        }
    }

    private String quote(String address) {
        return address == null ? "''" : "'" + address.replaceAll("'", "`") + "'";
    }

    @Override
    public void updateLinkedAddresses(EdsCrmAccount account) {
        removeLinkedAddresses(account.getObjectID());
        List<EdsAddress> newAddresses = new ArrayList<>();
        List<EdsAddress> addresses = account.getAddresses();
        if (addresses != null && addresses.size() > 0) {
            for (EdsAddress edsAddress : addresses) {
                BigInteger count = (BigInteger) findNativeSingle("select count(id) from " + getCompanyId() + ".address where isLinkedAddress is true and linkedaddressid = " + edsAddress.getObjectID());
                if (count != null && count.intValue() == 0 && !edsAddress.isDeleted()) {
                    newAddresses.add(edsAddress);
                } else {
                    String address = edsAddress.getAddress();
                    String addressB = edsAddress.getAddressb();
                    String city = edsAddress.getCity();
                    String zipCode = edsAddress.getZipCode();
                    Integer stateID = edsAddress.getState() != null ? edsAddress.getState().getObjectID() : null;
                    Integer countryID = edsAddress.getCountry() != null ? edsAddress.getCountry().getObjectID() : null;
                    updateNativeByParams("update " + getCompanyId() + ".address set deleted = ?, address=?, addressb=?, city=?, zipCode=?, stateid=" + stateID + ", countryid = " + countryID + " where linkedAddressID = ?", edsAddress.isDeleted(), address, addressB, city, zipCode, edsAddress.getObjectID());
                }
            }
        }
        if (newAddresses != null && newAddresses.size() > 0) {
            createAddressesForSubs(account, newAddresses);
        }
    }
}
