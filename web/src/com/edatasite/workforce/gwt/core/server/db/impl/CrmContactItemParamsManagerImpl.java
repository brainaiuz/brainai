package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.crm.EdsCrmContactItemParams;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CrmContactItemParamsManager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: Jul 4, 2010
 * Time: 6:46:02 PM
 * To change this template use File | Settings | File Templates.
 */

@Repository("crmContactItemParamsManager")
public class CrmContactItemParamsManagerImpl extends BaseManager<EdsCrmContactItemParams> implements CrmContactItemParamsManager {
    public CrmContactItemParamsManagerImpl() {
        super(EdsCrmContactItemParams.class);
    }

    public List<EdsCrmContactItemParams> getContactParams(Integer contactId) {
        return find("select params from EdsCrmContactItemParams params where params.contact.objectID = ?", contactId);
    }

    public List<EdsCrmContactItemParams> getContactParams(Integer contactId, Integer paramType) {
        return find("select params from EdsCrmContactItemParams params where params.contact.objectID = ? and params.param = ? order by params.lastUpdateTime desc ", contactId, paramType);
    }

    public List<EdsCrmContactItemParams> getContactParams(Integer contactId, Integer paramType, Integer entityId) {
        return find("select params from EdsCrmContactItemParams params where params.contact.objectID = ? and params.param = ? and params.relation = ?", contactId, paramType, entityId);
    }

    @Override
    public void deleteContactItemParams(final EdsCrmContact contact, int paramType) {
        EdsCrmContact.removeContactItemParams(contact, paramType);
        updateNative("delete from " + getCompanyId() + ".crmcontactitemparams where contactId = " + contact.getObjectID() + " and paramid = " + paramType);
    }

    @Override
    public void deleteContactItemParams(Integer contactID, Integer paramType) {
        updateNative("delete from " + getCompanyId() + ".crmcontactitemparams where contactId = " + contactID + " and paramid = " + paramType);
    }

    public void deleteAllContactItemParams(Integer objectID) {
        updateNative("delete from " + getCompanyId() + ".crmcontactitemparams where contactId = " + objectID);
    }

    public void deleteAllAccountItemParams(Integer objectID) {
        updateNative("delete from " + getCompanyId() + ".crmcontactitemparams where accountId = " + objectID);
    }

    @Override
    public HashMap<Integer, HashMap<Integer, HashMap<Integer, ArrayList<String>>>> getItemParamsByContactIDs(List<Integer> lessObjectIDs) {
        HashMap<Integer, HashMap<Integer, HashMap<Integer, ArrayList<String>>>> map = new HashMap<>();
        List<Object[]> listOfValues = findNative("select contactid, paramid, relationid, value from " + getCompanyId() + ".crmcontactitemparams params where contactid in (" + (ServerUtils.getAsCommoDelimited(lessObjectIDs, "0", ",") + ")"));
        if (listOfValues != null && listOfValues.size() > 0) {
            for (Object[] columns : listOfValues) {
                Integer contactID = Integer.parseInt(columns[0].toString());
                Integer paramID = Integer.parseInt(columns[1].toString());
                Integer relationID = Integer.parseInt(columns[2].toString());
                String value = columns[3] != null ? columns[3].toString() : null;
                if (value != null && !"".equals(value.trim())) {
                    if (!map.containsKey(contactID)) {
                        map.put(contactID, new HashMap<>());
                    }
                    if (!map.get(contactID).containsKey(paramID)) {
                        map.get(contactID).put(paramID, new HashMap<>());
                    }
                    if (!map.get(contactID).get(paramID).containsKey(relationID)) {
                        map.get(contactID).get(paramID).put(relationID, new ArrayList<>());
                    }
                    map.get(contactID).get(paramID).get(relationID).add(value);
                }
            }
        }
        return map;
    }

    public Map<Integer, Map<Integer, Map<Integer, ArrayList<String>>>> getAllContactParams() {
        Map<Integer, Map<Integer, Map<Integer, ArrayList<String>>>> map = new HashMap<>();
        List<Object[]> listOfValues = findNative("select contactid, paramid, relationid, value from " + getCompanyId() + ".crmcontactitemparams param where param.contactid in (select c.id from " + getCompanyId() + ".crmContact c where c.deleted is not true)");
        if (listOfValues != null && listOfValues.size() > 0) {
            for (Object[] columns : listOfValues) {
                Integer contactID = Integer.parseInt(columns[0].toString());
                Integer paramID = Integer.parseInt(columns[1].toString());
                Integer relationID = Integer.parseInt(columns[2].toString());
                String value = columns[3] != null ? columns[3].toString() : null;
                if (value != null && !"".equals(value.trim())) {
                    if (!map.containsKey(contactID)) {
                        map.put(contactID, new HashMap<>());
                    }
                    if (!map.get(contactID).containsKey(paramID)) {
                        map.get(contactID).put(paramID, new HashMap<>());
                    }
                    if (!map.get(contactID).get(paramID).containsKey(relationID)) {
                        map.get(contactID).get(paramID).put(relationID, new ArrayList<>());
                    }
                    map.get(contactID).get(paramID).get(relationID).add(value);
                }
            }
        }
        return map;
    }
}
