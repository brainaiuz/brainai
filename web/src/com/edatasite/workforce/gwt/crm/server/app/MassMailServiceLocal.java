package com.edatasite.workforce.gwt.crm.server.app;

import com.edatasite.workforce.gwt.core.client.enums.MessageStatusEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Azazello on 3/29/2017.
 */
public interface MassMailServiceLocal {
    void sendCrmEntityMessageBounce(String clusterType, String companyID, Integer entityId, Integer msgId);

    void unsubscribeFromMailList(Integer mailListID, boolean subscribe, ArrayList<Integer> subscriberID);

    void registerMessageOpening(Integer entityID, Integer mailMessageID, String IPAddress);

    void sendMassMail(Integer mailMessageID, Integer companyID, List<Object[]> entities);

    List<Object[]> getEntitiesForMassMail(Integer mailMessageID, Integer companyID, int loop);

    SelectItem[] getSubscribedListsByCrmEntityId(Integer crmEntityID);

    boolean trackLink(String kpiUrl, Integer entityID, Integer messageID, String ipAddress);

    void unsubscribeFromMessage(Integer mailListID, Integer msgID, Integer subscriberID);

    void updateMessageStatusToSent(Integer mailMessageID);

    void updateSentEntityMessageStatus(String companyID, Integer msgId, Integer entityID, MessageStatusEnum status);
}
