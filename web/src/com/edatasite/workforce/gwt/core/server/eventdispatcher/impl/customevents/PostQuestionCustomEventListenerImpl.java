package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents;

import com.edatasite.shared.db.EdsDbException;
import com.edatasite.workforce.core.domain.EdsChatRoom;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: HaveANiceDay
 * Date: 29.10.11
 * Time: 19:07
 * To change this template use File | Settings | File Templates.
 */
public class PostQuestionCustomEventListenerImpl extends CustomBusinessEventListenerAdapter {
    public static WfmType<EdsChatRoom> TYPE = new WfmType<>(EventTypes.postQuestionCustomEventListener);
    public static final String POST_QUESTION_ADD = "POST_QUESTION_ADD";

    @Autowired
    private MessageManager messageManager;

    @Autowired
    private UserManager userManager;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (POST_QUESTION_ADD.equals(event.getEventType())) {
            sendNotificationNewQuestionCreateForCOO(event);
        }
    }

    private void sendNotificationNewQuestionCreateForCOO(EdsBusinessEvent event) {

        String topic = event.getCustomStringField();
        EdsUser creator = userManager.get(event.getSourceID());
        if (topic != null && creator != null) {
            try {
                Integer companyID = creator.getCompany().getObjectID();
                List<EdsEmployee> companyMembers = userManager.getUsersByROLE(companyID, EdsRole.MEM);
                for (EdsEmployee member : companyMembers) {
                    messageManager.sendTopicCreatedToExpertNotificationForCOO(member, topic, companyID);
                }
                event.setStatus(EventStatus.COMPLETED.name());
            } catch (EdsDbException e) {
                event.setStatus(EventStatus.FAILED.name());
            }
        }
    }
}
