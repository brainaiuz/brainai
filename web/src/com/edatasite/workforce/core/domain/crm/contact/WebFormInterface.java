package com.edatasite.workforce.core.domain.crm.contact;

import com.edatasite.workforce.core.domain.customfields.EdsCrmCustomFields;

/**
 * Created with IntelliJ IDEA.
 * User: hayot
 * Date: 1/18/13
 * Time: 2:58 PM
 * To change this template use File | Settings | File Templates.
 */
public interface WebFormInterface {
    String getFieldAsString(Integer savingField);

    void setNote(String note);

    String getFieldLabelByCode(String fieldID);

    String getFieldValueByCode(String fieldID);

    EdsCrmCustomFields getCustomFields();
}
