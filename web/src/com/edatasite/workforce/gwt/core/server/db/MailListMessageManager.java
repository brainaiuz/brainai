/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/5/17 8:33:26                                                                                            *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.crm.EdsMailList;
import com.edatasite.workforce.core.domain.crm.EdsMailListMessage;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 25.01.2010
 * Time: 14:38:49
 * To change this template use File | Settings | File Templates.
 */
public interface MailListMessageManager extends Manager<EdsMailListMessage> {

    List<EdsMailList> getMailListsByMessage(Integer messageId);

    void deleteByMessage(Integer messageID);

    List<Object[]> getQueuedMessagesForContact(Integer mailMessageID, Integer companyID,/*, String type*/int loop);

}