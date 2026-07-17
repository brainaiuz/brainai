package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.ui.WfmCustomFieldsForm;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldBuilder;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFiledsOnLoad;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewAddFiledsCodeName;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 2/16/12
 * Time: 3:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class AccountingCustomFieldBuilder extends CustomFieldBuilder {
    public AccountingCustomFieldBuilder(ViewAddFiledsCodeName addViewName, WfmCustomFieldsForm wfmForm, CustomFiledsOnLoad customFiledOnLoad, Integer limitCustomFields) {
        super(addViewName, wfmForm, customFiledOnLoad, limitCustomFields);
    }

    public AccountingCustomFieldBuilder(WfmCustomFieldsForm wfmForm, ArrayList<CompanyCustomFieldItem> customFields) {
        super(wfmForm, customFields);
    }

    @Override
    protected String getCustomWidgetStyle() {
        return AccountingCustomFormConstants.STYLE_CUSTOM_WIDGET;
    }

    @Override
    protected boolean isNonConvertedDate() {
        return true;
    }
}
