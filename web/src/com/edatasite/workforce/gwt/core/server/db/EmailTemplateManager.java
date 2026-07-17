package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.settings.EdsEmailTemplate;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: muratov
 * Date: Mar 19, 2010
 * Time: 4:53:29 PM
 * To change this template use File | Settings | File Templates.
 */
public interface EmailTemplateManager extends Manager<EdsEmailTemplate> {
    List<EdsEmailTemplate> getCompanyEmailTemplates(ListingFilterParameter fp);

    void updateDefaultTemplate(Integer objectId, Integer categoryId);

    Long getCountNonDeletedEmailTemplates(Integer categoryID);

    List<EdsEmailTemplate> getEmailTemplatesByCategory(String categorycCode);

    List<EdsEmailTemplate> getEmailTemplatesForMessageCenter(ArrayList<String> moduleCode);

    List<EdsEmailTemplate> getEmailTemplates(ArrayList<String> moduleCode);

    EdsEmailTemplate getCompanyDefaultEmailTemplatesByCategory(String categoryCode);

//    public EdsEmailTemplate getCompanyEmailTemplatesByCategory(String categoryCode);

    EdsEmailTemplate getDefaultEmailTemplateByCategory(String categoryCode);

    EdsEmailTemplate getEmailTemplateByCategory(String categoryCode);

    List<EdsEmailTemplate> getCompanyAutoResponseTemplates();

    List<EdsEmailTemplate> getEmailTemplatesByCategory(String messageCenterCategory, com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter params);

    List<EdsEmailTemplate> getTemplatesByCustomEntity(Integer customEntityID);

    List<EdsEmailTemplate> getWfpEmailTemplates();

    void updateFromEmail(String email, String activeEmails);
}
