package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: Aug 5, 2009
 * Time: 7:50:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImportClientView extends ImportCrmAccountView {
    private DataListBox creditLimit;
    private DataListBox clientType;
    private DataListBox balanceAmount;
    protected Date conversionDate;
    private final String importClientView = "import_client_view_";

    public ImportClientView(Integer objectId, Date conversionDate) {
        super("addimportclient", wfmStrings.importClients(), objectId);
        viewName = wfmStrings.importClients();
        successMessage = wfmMessages.messItemSucImported(wfmStrings.customers());
        errorMessage = wfmMessages.messImportItemError(wfmStrings.customers());
        this.conversionDate = conversionDate;

    }

    public ImportClientView(String viewName, String viewDescription, Integer objectId) {
        super(viewName, viewDescription, objectId);
    }

    @Override
    public ImportTypeEnum getImportType() {
        return ImportTypeEnum.CUSTOMER;
    }

    public void initialize() {
        super.initialize();


        balanceAmount = new DataListBox();
        balanceAmount.ensureDebugId(importClientView + "balanceAmount");
        balanceAmount.addStyleName(DEFAULT_WIDTH);

        creditLimit = new DataListBox();
        creditLimit.ensureDebugId(importClientView + "creditLimit");
        creditLimit.addStyleName(DEFAULT_WIDTH);

        clientType = new DataListBox();
        clientType.ensureDebugId(importClientView + "creditLimit");
        clientType.addStyleName(DEFAULT_WIDTH);
    }

    @Override
    public CrmAccountItem getValuesIntoItem(CrmAccountItem item) {
        item = super.getValuesIntoItem(item);
        item.setBalanceAmountId(balanceAmount.getSelectedId());
        if (ImportTypeEnum.CUSTOMER.equals(getImportType())) {
            item.setCreditLimitId(creditLimit.getSelectedId());
            item.setClientTypeId(clientType.getSelectedId());
        }
        if (conversionDate != null) {
            item.setConversionDate(conversionDate);
        }
        return item;
    }

    @Override
    protected CustomForm getCustomForm() {
        return this;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.IMPORT_CLIENT_FORM;
    }

    @Override
    protected void drawForm() {
        super.drawForm();
        addField(OPENING_BALANCE, balanceAmount, getTitle(wfmStrings.openingBalance()));
        if (ImportTypeEnum.CUSTOMER.equals(getImportType())) {
            addField(CREDIT_LIMIT, creditLimit, getTitle(wfmStrings.creditLimit()));
            addField(CLIENT_TYPE, clientType, getTitle(Property.get(Constants.CLIENT_LIST, wfmStrings.clientType(), wfmStrings.customer())));
        }
    }

    @Override
    public void setItems() {
        super.setItems();
        balanceAmount.setItems(items, wfmStrings.openingBalance());
        if (ImportTypeEnum.CUSTOMER.equals(getImportType())) {
            creditLimit.setItems(items, wfmStrings.creditLimit());
            clientType.setItems(items, Property.get(Constants.CLIENT_LIST, wfmStrings.clientType(), wfmStrings.customer()));
        }
    }
}