package com.edatasite.workforce.gwt.core.server.db.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsCrmCustomFields;
import com.edatasite.workforce.gwt.core.server.db.Manager;

/**
 * User: Hayot
 * Email: hayot.rahimov@gmail.com
 * Date: 03-Feb-2011
 * Time: 17:16:50
 */
public interface CrmCustomFieldsManager extends Manager<EdsCrmCustomFields> {
    EdsCrmCustomFields getCustomFieldByLead(Integer leadID);
    EdsCrmCustomFields getCustomFieldByCandidate(Integer candidateID);
}
