package com.edatasite.workforce.gwt.accounting.client;

import com.edatasite.workforce.gwt.core.client.ui.WfmCustomFieldsForm;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldBuilder;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFiledsOnLoad;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewAddFiledsCodeName;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 2/16/13
 * Time: 2:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductCustomFieldBuilder extends CustomFieldBuilder {

    public ProductCustomFieldBuilder(ViewAddFiledsCodeName addViewName, WfmCustomFieldsForm wfmForm, final CustomFiledsOnLoad customFiledOnLoad, Integer limitCustomFields) {
        super(addViewName, wfmForm, customFiledOnLoad, limitCustomFields);
    }

    public ProductCustomFieldBuilder(ViewAddFiledsCodeName addViewName, WfmCustomFieldsForm wfmForm, final CustomFiledsOnLoad customFiledOnLoad, String localeCode) {
        super(addViewName, wfmForm, customFiledOnLoad, localeCode);
    }

    public ProductCustomFieldBuilder(ViewAddFiledsCodeName addViewName, WfmCustomFieldsForm wfmForm, CustomFiledsOnLoad customFiledOnLoad, boolean isUsesShowField, Integer relationship) {
        super(addViewName, wfmForm, customFiledOnLoad, isUsesShowField, relationship, null, null);
    }

    @Override
    protected String getCustomWidgetStyle() {
        return AccountingCustomFormConstants.STYLE_CUSTOM_WIDGET;
    }
}
