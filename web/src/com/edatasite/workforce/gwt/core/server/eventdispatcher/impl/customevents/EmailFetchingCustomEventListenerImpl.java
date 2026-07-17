package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents;

import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.settings.EdsEmailSetting;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CaseManager;
import com.edatasite.workforce.gwt.core.server.db.EmailSettingsManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User: Abdulaziz
 * Date: Aug 11, 2010
 * Time: 2:28:55 PM
 */
@Transactional
public class EmailFetchingCustomEventListenerImpl extends CustomBusinessEventListenerAdapter {
    private static final Logger log = LoggerFactory.getLogger(EmailFetchingCustomEventListenerImpl.class);

    public static String EVENT_SEND_AUTORESPONSE = "SEND_AUTORESPONSE";
    public static WfmType<EdsEmailSetting> TYPE_EMAIL_FETCHING = new WfmType<>(EventTypes.emailFetchingCustomEventListener);

    @Autowired
    private CaseManager caseManager;
    @Autowired
    private CrmServiceLocal crmService;
    @Autowired
    private EmailSettingsManager emailSettingsManager;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        try {
            sendAutoResponseToCases(event);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    private void sendAutoResponseToCases(EdsBusinessEvent event) {
        if (event != null && event.getEntityID() != null && event.getCustomStringField() != null && !"".equals(event.getCustomStringField())) {
            EdsEmailSetting emailSettings = emailSettingsManager.get(event.getEntityID());
            if (emailSettings != null) {
                List<Integer> caseIDs = ServerUtils.getStringAsList(event.getCustomStringField(), ",");
                caseIDs = caseManager.getCaseIDsByIDs(caseIDs);
                if (caseIDs != null && caseIDs.size() > 0) {
                    for (Integer caseID : caseIDs) {
                        crmService.sendAutoResponseToCase(caseID, event.getEntityID(), null);
                    }
                }
                event.setStatus(EventStatus.COMPLETED.name());
            }
        }
    }

}