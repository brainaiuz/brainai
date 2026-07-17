package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents;

import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.emailfetching.EdsEmailFolder;
import com.edatasite.workforce.core.domain.emailfetching.mongo.EdsEmail;
import com.edatasite.workforce.core.domain.settings.EdsEmailSetting;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.server.db.EmailFolderManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.messagecenter.server.MessageCenterServiceLocal;
import com.edatasite.workforce.gwt.messagecenter.server.app.MailServices;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Azazello on 1/6/16.
 */
@Transactional
public class MessageCenterCustomEventListenerImpl extends CustomBusinessEventListenerAdapter {
    public static WfmType<EdsEmailSetting> TYPE = new WfmType<>(EventTypes.messageCenterCustomEventListener);
    @Autowired
    private MessageCenterServiceLocal messageCenterService;
    @Autowired
    private MailServices mailServices;
    @Autowired
    private EmailFolderManager emailFolderManager;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (!StringUtils.isEmpty(event.getCustomStringField())) {
            String[] IDs = event.getCustomStringField().split(",");
            List<String> emailIDs = new ArrayList<>(Arrays.asList(IDs));

            List<EdsEmail> emails = messageCenterService.getEmailsByIDs(emailIDs);
            Set<Email> es = new HashSet<>();
            for (EdsEmail email : emails) {
                Email e = new Email();
                EdsEmailFolder folder = emailFolderManager.get(email.getFolderId());
                e.setMessageUIDHex(Long.toHexString(email.getMessageUID()));
                e.setMessageId(email.getMessageId());
                e.setFolderName(folder.getFullName());
                e.setType(folder.getType());
                es.add(e);
            }
            mailServices.getService(event.getEntityID()).setFlags(es, event.getEntityID(), event.getEventType());
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }
}
