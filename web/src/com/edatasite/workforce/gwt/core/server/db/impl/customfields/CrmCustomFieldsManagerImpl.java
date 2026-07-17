package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customfields.EdsCrmCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.CrmCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * User: Hayot
 * Email: hayot.rahimov@gmail.com
 * Date: 03-Feb-2011
 * Time: 17:23:19
 */
@Repository("crmCustomFieldsManager")
public class CrmCustomFieldsManagerImpl extends BaseManager<EdsCrmCustomFields> implements CrmCustomFieldsManager {
    public CrmCustomFieldsManagerImpl() {
        super(EdsCrmCustomFields.class);
    }

    @Override
    public EdsCrmCustomFields getCustomFieldByLead(Integer leadID) {
        return (EdsCrmCustomFields) findSingle("select customField from EdsCrmCustomFields customField where objectID in (select lead.customFields from EdsCrmContact lead where lead.contactType = " + EdsCrmContact.LEAD_CONTACT + "and lead.objectID = " + leadID + ")");
    }

    @Override
    public EdsCrmCustomFields getCustomFieldByCandidate(Integer leadID) {
        return (EdsCrmCustomFields) findSingle("select customField from EdsCrmCustomFields customField where objectID in (select lead.customFields from EdsCrmContact lead where lead.contactType = " + EdsCrmContact.CANDIDATE + "and lead.objectID = " + leadID + ")");
    }
}
