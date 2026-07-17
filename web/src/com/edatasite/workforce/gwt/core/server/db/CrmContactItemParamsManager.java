package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.crm.EdsCrmContactItemParams;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Jul 4, 2010
 * Time: 6:44:11 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CrmContactItemParamsManager extends Manager<EdsCrmContactItemParams> {

    List<EdsCrmContactItemParams> getContactParams(Integer contactId);

    List<EdsCrmContactItemParams> getContactParams(Integer contactId, Integer paramType);

    List<EdsCrmContactItemParams> getContactParams(Integer contactId, Integer paramType, Integer entityId);

    void deleteContactItemParams(EdsCrmContact contact, int paramType);

    void deleteContactItemParams(Integer contactID, Integer paramType);

    void deleteAllContactItemParams(Integer objectID);

    void deleteAllAccountItemParams(Integer objectID);

    HashMap<Integer, HashMap<Integer, HashMap<Integer, ArrayList<String>>>> getItemParamsByContactIDs(List<Integer> lessObjectIDs);

    Map<Integer, Map<Integer, Map<Integer, ArrayList<String>>>> getAllContactParams();
}
