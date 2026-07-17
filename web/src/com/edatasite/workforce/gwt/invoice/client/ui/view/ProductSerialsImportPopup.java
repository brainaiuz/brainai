package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.ui.DateFormatException;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmFormPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductSerialItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Dilshod Madrahimov on 8/1/15 1:17 PM
 */
public class ProductSerialsImportPopup extends KpiModal {
    
    private static final String SAMPLE_SERIAL_NUMBERS = "https://s3.amazonaws.com/workforcetrack/000000000000/public/Product_serial_numbers.csv";
    private static final String SAMPLE_ARTICLE_NUMBERS_TALAL = "https://s3.amazonaws.com/workforcetrack/000000000000/public/Product_article_numbers.csv";
    boolean isAlmadarSerials = Utils.hasGenericAccess(GenericSettingsEnum.ALMADAR_PRODUCT_SERIAL_ENABLED);
    protected NewInvoice invoiceData;
    protected int columntCount = 5;
    protected ArrayList<ProductSerialItem> serialsList;
    private FileUpload fileUpload;
    private WfmFormPanel uploadPanel;
    private Button uploadBtn;
    private Button cancelBtn;

    public ProductSerialsImportPopup() {
        initialize(false);
    }

    public ProductSerialsImportPopup(NewInvoice invoiceData) {
        this.invoiceData = invoiceData;
        initialize(false);
    }

    protected void initialize(boolean fromArticle) {
        fileUpload = new FileUpload();
        fileUpload.setName(CommandConstants.ATTACHMENT_PARAM_BASE + 0);
        uploadPanel = new WfmFormPanel("/ProductSerailsHandler");
        uploadPanel.addFormHandler(new FormHandler() {
            public void onSubmit(FormSubmitEvent event) {
            }

            public void onSubmitComplete(FormSubmitCompleteEvent event) {
                LoadingPanel.loading(false);
                close();
                if (uploadPanel.getErrorString() == null) {
                    if (validateAndSetData(uploadPanel.getReturnValue())) {
                        Info.show(wfmStrings.successfullyUploaded(), Info.Type.INFO);
                    } else {
                        Info.show("The CSV file items quantity more than the required quantity !", Info.Type.WARNING);
                    }
                } else {
                    Info.show(wfmStrings.parseError(), Info.Type.WARNING);
                }
            }
        });
        uploadPanel.setParameter("columnCount", String.valueOf(columntCount));

        HTML label = new HTML("<font size='2.5'><b>" + wfmStrings.messSelectFile() + "<b></font>");
        String descText = "To import your serial numbers into the system you need to download our Sample CSV template file first. Fill respective columns with numbers data, select this file on Choose File pop-up, then click Upload File button";
        Anchor downloadLink = new Anchor(wfmStrings.downloadSample(), false, GWT.getHostPageBaseURL() + "", "_blank");
        if (fromArticle && isAlmadarSerials) {
            downloadLink.setHref(SAMPLE_ARTICLE_NUMBERS_TALAL);
        } else {
            downloadLink.setHref(SAMPLE_SERIAL_NUMBERS);
        }
        uploadPanel.setWidget(fileUpload);

        uploadBtn = new Button(wfmStrings.uploadFile());
        cancelBtn = new Button(wfmStrings.cancel());

        uploadBtn.addClickHandler(clickEvent -> this.onFormSubmitClick());
        cancelBtn.addClickHandler(clickEvent -> this.close());
        setTitle("Import Serial Numbers");
        setSize(350, 200);

        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.setSpacing(7);
        buttonPanel.add(uploadBtn);
        buttonPanel.add(cancelBtn);

        VerticalPanel content = new VerticalPanel();
        content.setSpacing(7);
        content.add(label);
        content.add(uploadPanel);
        content.add(downloadLink);
        content.add(new Label(descText));
        content.add(buttonPanel);
        getContent().add(content);
        open();
    }

    protected void onFormSubmitClick() {
        final String filename = fileUpload.getFilename();

        if (filename == null || filename.isEmpty()) {
            Info.show(wfmStrings.messSelectFile(), Info.Type.WARNING);
            return;
        }
        if (!Objects.equals(".csv", filename.substring(filename.lastIndexOf(".")))) {
            Info.show(wfmStrings.messSelectCSVFile(), Info.Type.WARNING);
            return;
        }
        LoadingPanel.loading(true);

        this.uploadPanel.setParameter("url", "ProductSerailsHandler");
        this.uploadPanel.submit();
    }

    protected boolean validateAndSetData(String returnValue) {
        final Map<String, List<ProductSerialItem>> productNumberMap = new LinkedHashMap<>();
        serialsList = new ArrayList<>();
        String[] values = returnValue.split(";");
        if (invoiceData != null) {//PO serials
            for (String value : values) {
                if (value == null || value.trim().isEmpty()) {
                    continue;
                }
                String[] rowValues = value.split("=");

                String productNumber = rowValues[0];
                String serialNumber = rowValues[1];
                String expDateStr = rowValues[2];
                Date expDate;
                try {
                    expDate = DateUtils.parse(expDateStr, DateUtils.dateFormatWithSlash);
                } catch (DateFormatException e) {
                    e.printStackTrace();
                    expDate = new Date();
                }
                if (!productNumberMap.containsKey(productNumber)) {
                    serialsList = new ArrayList<>();
                    serialsList.add(new ProductSerialItem(serialNumber, expDate, null, null));
                    productNumberMap.put(productNumber, serialsList);
                } else {
                    productNumberMap.get(productNumber).add(new ProductSerialItem(serialNumber, expDate, null, null));
                }
            }

            int i = 0;
            for (String entry : productNumberMap.keySet()) {
                int receivedQty = 0;
                int newReceivedQty = 0;
                if (invoiceData.getItems()[i].getReceive() != null) {
                    receivedQty = invoiceData.getItems()[i].getReceive().intValue();
                }
                ProductSerialItem[] newProductSerials = productNumberMap.get(entry).toArray(new ProductSerialItem[]{});
                newReceivedQty = newProductSerials.length;
                if (newReceivedQty > receivedQty) {
                    return false;
                }
                invoiceData.getItems()[i].setAssignedSerials(newProductSerials);
                i++;
            }
            serialsList.clear();

        } else { //product serials
            for (String value : values) {
                if (value == null || value.trim().isEmpty()) {
                    continue;
                }
                String[] rowValues = value.split("=");
                String productNumber = rowValues[0];
                String serialNumber = rowValues[1];
                String expDateStr = rowValues[2];
                String lotNumber = null;
                if (rowValues.length >= 4) {
                    lotNumber = rowValues[3];
                }
                String refNumber = null;
                if (rowValues.length >= 5) {
                    refNumber = rowValues[4];
                }
                Date expDate = null;
                if (expDateStr != null) {
                    try {
                        expDate = DateUtils.parse(expDateStr, DateUtils.dateFormatWithSlash);
                    } catch (DateFormatException e) {
                        e.printStackTrace();
                        expDate = new Date();
                    }

                }
                ProductSerialItem serialItem = new ProductSerialItem();
                serialItem.setSerial(serialNumber);
                serialItem.setExpirationDate(expDate);
                serialItem.setLotNumber(lotNumber);
                serialItem.setRefNumber(refNumber);
                serialsList.add(serialItem);

            }
        }
        return true;
    }

    public ArrayList<ProductSerialItem> getProductSerialItems() {
        return serialsList;
    }
}
