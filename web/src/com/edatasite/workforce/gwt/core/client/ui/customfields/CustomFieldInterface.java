package com.edatasite.workforce.gwt.core.client.ui.customfields;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;

public interface CustomFieldInterface {
    CompanyCustomFieldItem getFieldItem();

    void setFieldItem(CompanyCustomFieldItem fieldItem);
}
