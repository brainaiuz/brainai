package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsWhatsAppMessage;
import com.edatasite.workforce.gwt.core.server.db.WhatsAppMessageManager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository("whatsAppMessageManager")
public class WhatsAppMessageManagerImpl extends BaseManager<EdsWhatsAppMessage> implements WhatsAppMessageManager {
    public WhatsAppMessageManagerImpl() {
        super(EdsWhatsAppMessage.class);
    }

    @Override
    public ArrayList<EdsWhatsAppMessage> getAllMessagesList() {
        StringBuilder query = new StringBuilder();
        query.append("select * from ").append(getCompanyId()).append(".whatsapp_message");
        query.append(" ORDER BY  message_date");

        return (ArrayList<EdsWhatsAppMessage>) findNative(query.toString(), EdsWhatsAppMessage.class);
    }

    @Override
    public HashMap<String, ArrayList<Integer>> getMessageOwnersDataMap() {
        HashMap<String, ArrayList<Integer>> resultMap = new HashMap<>();

        StringBuilder query = new StringBuilder();
        query.append("SELECT DISTINCT ");
        query.append("CASE ");
        query.append("WHEN crm_contact_id IS NOT NULL THEN crm_contact_id ");
        query.append("WHEN crm_account_id IS NOT NULL THEN crm_account_id ");
        query.append("WHEN employee_id IS NOT NULL THEN employee_id ");
        query.append("END AS id, ");
        query.append("CASE ");
        query.append("WHEN crm_contact_id IS NOT NULL THEN 'contact' ");
        query.append("WHEN crm_account_id IS NOT NULL THEN 'account' ");
        query.append("WHEN employee_id IS NOT NULL THEN 'employee' ");
        query.append("END AS type ");
        query.append("FROM ").append(getCompanyId()).append(".whatsapp_message");

        List<Object> list = findNative(query.toString());

        for (Object item : list) {
            Object[] it = (Object[]) item;
            int id = (int) it[0];
            String type = (String) it[1];

            // Check if the map contains the key, if not, add a new ArrayList
            resultMap.putIfAbsent(type, new ArrayList<>());
            // Add the id to the ArrayList corresponding to the type
            resultMap.get(type).add(id);
        }

        return resultMap;
    }
}
