/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/5/17 8:33:26                                                                                            *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.crm.EdsCrmEntitySendMessageStatus;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.core.client.enums.MessageStatusEnum;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 30-Jan-2010
 * Time: 18:53:36
 * To change this template use File | Settings | File Templates.
 */
public interface CrmEntitySendMessageStatusManager extends Manager<EdsCrmEntitySendMessageStatus> {
    List<EdsCrmContact> getBouncedEntitiesList(ListingFilterParameter fp);

    Long getBouncedEntitiesCount(ListingFilterParameter fp);

    EdsCrmEntitySendMessageStatus getEntity(Integer messageID, Integer entityID);

    Long getStatusCountByMessageID(Integer messageID, MessageStatusEnum status);
}
