package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsNews;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.crm.EdsCase;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.documents.EdsFolder;

/**
 * User: Abdulaziz
 * Date: Aug 21, 2010
 * Time: 3:06:28 PM
 */
public class SolrEvent<H extends EdsObject> {
    public static final WfmType<EdsTask> TASK_ADD = new WfmType<>("TASK_ADD");
    public static final WfmType<EdsProject> PROJECT_ADD = new WfmType<>("PROJECT_ADD");
    public static final WfmType<EdsFolder> FOLDER_ADD = new WfmType<>("FOLDER_ADD");
    public static final WfmType<EdsCrmContact> LEAD_ADD = new WfmType<>("LEAD_ADD");
    public static final WfmType<EdsCrmContact> LEAD_REMOVE = new WfmType<>("LEAD_REMOVE");
    public static final WfmType<EdsNews> NEWS_ADD = new WfmType<>("NEWS_ADD");
    public static final WfmType<EdsNews> NEWS_REMOVE = new WfmType<>("NEWS_REMOVE");
    public static final WfmType<EdsCrmContact> CRM_CONTACT_ADD = new WfmType<>("CRM_CONTACT_ADD");
    public static final WfmType<EdsCrmAccount> CRM_ACCOUNT_ADD = new WfmType<>("CRM_CRMACCOUNT_ADD");
    public static final WfmType<EdsCase> CRM_CASE_ADD = new WfmType<>("CRM_CASE_ADD");
    public static final WfmType<EdsEvent> EVENT_ADD = new WfmType<>("EVENT_ADD");


    private WfmType<H> eventType;
    private Integer entityID;
    private Integer companyID;

    public SolrEvent(WfmType<H> eventType, H entity, EdsCompany company) {
        this.eventType = eventType;
        this.entityID = entity.getObjectID();
        this.companyID = company.getObjectID();
    }

    public WfmType<H> getEventType() {
        return eventType;
    }

    public Integer getEntityID() {
        return entityID;
    }

    public Integer getCompanyID() {
        return companyID;
    }
}
