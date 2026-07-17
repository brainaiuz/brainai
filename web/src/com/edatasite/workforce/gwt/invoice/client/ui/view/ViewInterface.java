package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.ui.view.widgets.placeofsupply.PlaceOfSupplyWidget;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.CurrencyWidget;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ProjectLookUp;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceNumberData;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.Params;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.ReceiptTable;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.ProductsTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 1/26/12
 * Time: 3:15 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ViewInterface {

    boolean isEditForm();

    Params getFormParameters();

    Integer getObjectID();

    void setObjectID(Integer objectID);

    LookUp getCrmAccountLookUp();

    CrmAccountWidgets getCrmAccountWidgets();

    ProjectLookUp getProjectLookUp();

    CurrencyWidget getCurrencyWidget();

    DatePicker getDatePicker();

    DatePicker getDueDatePicker();

    TextBox getNumberTxtBox();

    DataListBox getTaxCalcListBox();

    ReceiptTable getTotalTable();

    ProductsTable getProductTable();

    void setConversionDate(Date date);

    Date getConversionDate();

    View getView();

    void setEditValues(NewInvoice result);

    void setFormData(NewInvoice result);

    void initCustomFields(NewInvoice result);

    void initSystemCustomFields(NewInvoice result);

    void generateForm(String layoutHTML);

    NewInvoice getFormData(String invoiceStatus, boolean calculate);

    Date getCurrentDate();

    InvoiceNumberData getNumberData();

    void setNumberData(InvoiceNumberData numberData);

    SplitButton getSplitButtonPdf();


    WfmButton2 getSaveButton();

    WfmButton2 getApproveButton();

    WfmButton2 getApproveAndSendButton();

    boolean validateCustomFields();

    boolean validateProjectMandatory();

    boolean validateSystemCustomFields();

    HTMLPanel getHTMLPanel();

    void initProductsTableData(NewInvoice result);

    void initWidgetMap();

    PdfTemplatePanel getPdfTemplateBox();

    void initPdfTemplates(NewInvoice result);

    PlaceOfSupplyWidget getPlaceOfSupplyWidget();

    DataListBox getPriceLevelDropdown();
}
