package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.crm.EdsMailList;
import com.edatasite.workforce.core.domain.crm.EdsMailListMessage;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.MailListMessageManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 25.01.2010
 * Time: 14:43:14
 * To change this template use File | Settings | File Templates.
 */
@Repository("mailListMessageManager")
public class MailListMessageManagerImpl extends BaseManager<EdsMailListMessage> implements MailListMessageManager {

    public MailListMessageManagerImpl() {
        super(EdsMailListMessage.class);
    }

    public List<Object[]> getQueuedMessagesForContact(Integer mailMessageID, Integer companyID, int loop) {
        StringBuilder sql = new StringBuilder();
        String company = "\"" + companyID + "\"";
        sql.append("SELECT distinct l.id as leadid, \n");//0
        sql.append("l.firstName, \n");//1
        sql.append("l.lastName, \n");//2
        sql.append("l.primaryEmail, \n");//3
        sql.append("l.title, \n");//4
        sql.append("a.name as companyname, \n");//5
        sql.append("ml.id as maillistId, \n");//6
        sql.append("c.id as campaignID, \n");//7
        sql.append("c.name as campaignName, \n");//8
        sql.append("l.primaryphone as primaryphone, \n");//9
        sql.append("itemparams.value as mobile \n");//10

        sql.append("FROM ").append(company).append(".crmContact l \n");
        sql.append("LEFT OUTER JOIN ").append(company).append(".crmaccount a on a.id = l.crmaccount \n");
        sql.append("LEFT OUTER JOIN ").append(company).append(".leadmaillist lml on lml.entity_id = l.id \n");
        sql.append("LEFT OUTER JOIN ").append(company).append(".maillist ml on ml.id = lml.maillistid \n");
        sql.append("LEFT OUTER JOIN ").append(company).append(".maillistmessage mlm on mlm.maillistid=ml.id \n");
        sql.append("LEFT OUTER JOIN ").append(company).append(".mailmessage m on m.id = mlm.mailmessageid \n");
        sql.append("LEFT OUTER JOIN ").append(company).append(".campaign c on m.campaignID = c.id \n");
        sql.append("LEFT OUTER JOIN ").append(company).append(".crmcontactitemparams itemparams on itemparams.contactid = l.id and itemparams.paramid = 1 and itemparams.relationid=3 \n");

        sql.append("WHERE (l.deleted is null OR l.deleted <> true) \n");
        sql.append("AND (m.deleted is null OR m.deleted <> true) \n");
        sql.append("AND (lml.deleted is null OR lml.deleted <> true) \n");
        sql.append("AND (l.primaryEmail is not null AND l.primaryEmail <> '') \n");
        sql.append("AND (l.emailOptOut is null OR l.emailOptOut <> true) \n");
        sql.append("AND l.primaryEmail not in (select blackemail from \"public\".blacklist) \n");
        sql.append("AND m.id = ").append(mailMessageID);
        return slaveEntityManager.createNativeQuery(sql.toString()).setFirstResult(loop * Constants.MASSMAIL_LIMIT).setMaxResults(Constants.MASSMAIL_LIMIT).getResultList();

    }

    @Override
    public List<EdsMailList> getMailListsByMessage(Integer messageId) {
        return slaveEntityManager.createQuery("select mlm.mailList from EdsMailListMessage mlm where mlm.mailMessage.objectID =:messageId", EdsMailList.class)
                .setParameter("messageId", messageId).getResultList();
    }

    @Override
    public void deleteByMessage(Integer messageID) {
        masterEntityManager.createQuery("delete from EdsMailListMessage where mailMessage.objectID =:messageID").setParameter("messageID", messageID).executeUpdate();
    }
}
