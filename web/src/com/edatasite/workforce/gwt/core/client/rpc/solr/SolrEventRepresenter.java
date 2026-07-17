package com.edatasite.workforce.gwt.core.client.rpc.solr;

import com.edatasite.workforce.gwt.crm.client.rpc.EventItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: HaveANiceDay
 * Date: 19.08.11
 * Time: 18:22
 * To change this template use File | Settings | File Templates.
 */
public class SolrEventRepresenter implements IsSerializable {
    public static final String SPLIT = "@";
    public static final String FIELD_COMPOSITE_ID = "oid";
    public static final String FIELD_COMPANY_ID = "companyId";

    public static final String FIELD_EVENT_ID = "eventId";
    public static final String FIELD_SUBJECT = "subject";
    public static final String FIELD_INBOUND = "inbound";
    public static final String FIELD_MISSED = "missed";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_CREATION_DATE = "creationDate";
    public static final String FIELD_START_DATE = "startDate";
    public static final String FIELD_END_DATE = "endDate";
    public static final String FIELD_LAST_UPDATE_DATE = "lastUpdateDate";
    public static final String FIELD_LOCATION = "location";

    public static final String FIELD_RECURRENCE_ID = "recurrenceId";

    public static final String FIELD_SHARED_USER_ID = "sharedUserId";
    public static final String FIELD_SHARED_USER_NAME = "sharedUserName";
    public static final String FIELD_SHARED_USER_ID_NAME = "sharedUserIdName";

    public static final String FIELD_CONTACT_ID = "contactId";

    public static final String FIELD_OWNER_ID = "ownerId";
    public static final String FIELD_OWNER_NAME = "ownerName";
    public static final String FIELD_OWNER_ID_NAME = "ownerIdName";

    public static final String FIELD_UPDATER_ID = "updaterId";
    public static final String FIELD_UPDATER_NAME = "updaterName";
    public static final String FIELD_UPDATER_ID_NAME = "updaterIdName";

    public static final String FIELD_DURATION_SEC = "duration";

    public static final String FIELD_GOOGLE_ID = "googleId";
    public static final String FIELD_ASTERISK_ID = "asteriskId";
    public static final String FIELD_TWILIO_ID = "twilioId";

    public static final String FIELD_FROM_RECORDER = "fromRecorder";
    public static final String FIELD_ALL_DAY = "allDay";
    public static final String FIELD_ACTIVITY_TYPE_ID = "activityTypeId";
    public static final String FIELD_CALL_TYPE = "callType";
    public static final String FIELD_ACTIVITY_TYPE_ID_NAME = "activityTypeIdName";
    public static final String FIELD_BOOKING = "booking";
    public static final String FIELD_MULTI_DAY = "multiDay";
    public static final String FIELD_EDS_LOCATION_ID = "edsLocationId";
    public static final String FIELD_CREATED_FROM_ID = "createdFromId";

    public static final String FIELD_CONTACT_RELATED_ID = "contactRelatedId";
    public static final String FIELD_CONTACT_RELATED_NAME = "contactRelatedName";
    public static final String FIELD_CONTACT_RELATED_ID_NAME = "contactRelatedIdName";

    public static final String FIELD_LEAD_RELATED_ID = "leadRelatedId";
    public static final String FIELD_LEAD_RELATED_NAME = "leadRelatedName";
    public static final String FIELD_LEAD_RELATED_ID_NAME = "leadRelatedIdName";

    public static final String FIELD_CRM_ACCOUNT_RELATED_ID = "crmAccountRelatedId";
    public static final String FIELD_CRM_ACCOUNT_RELATED_NAME = "crmAccountRelatedName";
    public static final String FIELD_CRM_ACCOUNT_RELATED_ID_NAME = "crmAccountRelatedIdName";

    public static final String FIELD_CANDIDATE_RELATED_ID = "candidateRelatedId";
    public static final String FIELD_CANDIDATE_RELATED_NAME = "candidateRelatedName";
    public static final String FIELD_CANDIDATE_RELATED_ID_NAME = "candidateRelatedIdName";

    public static final String FIELD_EMPLOYEE_RELATED_ID = "employeeRelatedId";
    public static final String FIELD_EMPLOYEE_RELATED_NAME = "employeeRelatedName";
    public static final String FIELD_EMPLOYEE_RELATED_ID_NAME = "employeeRelatedIdName";

    public static final String FIELD_PHONE_NUMBER = "phoneNumber";

    public static final String DYNAMIC_FIELD_RELATED_ID = "relatedId_";
    public static final String DYNAMIC_FIELD_RELATED_NAME = "relatedName_";
    public static final String DYNAMIC_FIELD_RELATED_ID_NAME = "relatedIdName_";
    // Solr sortable fields
    public static final String SORTABLE_SUBJECT = "sortableSubject";
    public static final String SORTABLE_DESCRIPTION = "sortableDescription";

    public static final String FIELD_COMPOSITE = "composite";

    public static String getSortField(String sortField) {
        if (sortField != null) {
            switch (sortField) {
                case EventItem.ID:
                    return SolrEventRepresenter.FIELD_EVENT_ID;
                case EventItem.SUBJECT:
                    return SolrEventRepresenter.SORTABLE_SUBJECT;
                case EventItem.START_DATE:
                    return SolrEventRepresenter.FIELD_START_DATE;
                case EventItem.END_DATE:
                    return SolrEventRepresenter.FIELD_END_DATE;
                case EventItem.VENUE:
                    return SolrEventRepresenter.FIELD_LOCATION;
                case EventItem.DESCRIPTION:
                    return SolrEventRepresenter.SORTABLE_DESCRIPTION;
                case SolrEventRepresenter.FIELD_CREATION_DATE:
                    return SolrEventRepresenter.FIELD_CREATION_DATE;
            }
        }
        return SolrEventRepresenter.FIELD_EVENT_ID;
    }
}
