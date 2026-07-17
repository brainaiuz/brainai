package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.ui.WfmCustomFieldsForm;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldBuilder;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFiledsOnLoad;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewAddFiledsCodeName;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 10/28/11
 * Time: 5:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class InvoiceCustomFieldsView extends Composite {

    private ViewAddFiledsCodeName viewName;
    private Integer limitCustomFields;
    private ArrayList<CompanyCustomFieldItem> customFields;
    private CustomFieldBuilder customFieldBuilder;

    private WfmCustomFieldsForm wfmCustomTable;

    private VerticalPanel pnlWraper;

    public InvoiceCustomFieldsView(ViewAddFiledsCodeName viewName, ArrayList<CompanyCustomFieldItem> customFields, VerticalPanel pnlWraper, Integer limitCustomFields) {
        this.viewName = viewName;
        this.customFields = customFields;
        this.pnlWraper = pnlWraper;
        this.limitCustomFields = limitCustomFields;

        onInitialize();
    }

    private void onInitialize() {
        wfmCustomTable = new WfmCustomFieldsForm();
        wfmCustomTable.setStyleName(AccountingCustomFormConstants.STYLE_CUSTOM_FIELD_TABLE);
        wfmCustomTable.addStyleName("WfmCustomFieldsForm file--InvoiceCustomFieldsView");

        if (viewName != null) {
            customFieldBuilder = new AccountingCustomFieldBuilder(viewName, wfmCustomTable, new CustomFiledsOnLoad() {
                @Override
                public void onLoad(List<String> showFieldCodeName) {
                    if (customFieldBuilder.getCustomFields() == null || customFieldBuilder.getCustomFields().size() == 0) {
                        if (pnlWraper != null)
                            pnlWraper.setVisible(false);
                    }
                }

                @Override
                public void setCustomFieldValues() {
                    if (customFields != null) {
                        customFieldBuilder.setValues(customFields);
                    }
                }
            }, limitCustomFields);
        } else {
            customFieldBuilder = new AccountingCustomFieldBuilder(wfmCustomTable, customFields);
        }

        initWidget(wfmCustomTable);
    }

    public ArrayList<CompanyCustomFieldItem> getData() {
        return customFieldBuilder.getValues();
    }

    public void setData(ArrayList<CompanyCustomFieldItem> values) {
        customFieldBuilder.setValues(values);
    }

    public boolean validateRequiredFields() {
        return customFieldBuilder.validateRequiredFields();
    }
}
