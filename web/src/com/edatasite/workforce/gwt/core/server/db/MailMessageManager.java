package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.crm.EdsMailMessage;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 27.01.2010
 * Time: 18:44:53
 * To change this template use File | Settings | File Templates.
 */

public interface MailMessageManager extends Manager<EdsMailMessage> {

    List<Object[]> getListForLead(ListingFilterParameter fp);

    List<EdsMailMessage> getListOfMailMessages(ListingFilterParameter fp);

    Integer getCountOfMailMessages(ListingFilterParameter fp);
}
