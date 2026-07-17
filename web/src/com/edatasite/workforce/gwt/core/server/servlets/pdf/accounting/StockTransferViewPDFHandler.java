package com.edatasite.workforce.gwt.core.server.servlets.pdf.accounting;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.StockTransferItem;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceQuoteRequestObject;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductTrackBatchItem;
import com.google.common.collect.Lists;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dilshod Madrahimov on 3/17/15.
 */
public class StockTransferViewPDFHandler extends AbstractITextPostPdfHandler implements PDFConstants {

    @Autowired
    private ProductService productService;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        InvoiceQuoteRequestObject requestObject = (InvoiceQuoteRequestObject) dataClass;
        StockTransferItem item = productService.getStockTransferSummaryData(requestObject.getObjectID());

        EdsUser user = uploadManager.getUser();
        String creatorName = user != null ? escapeHtml(user.getFullName()) : "";
        HashMap<String, CustomisedITextTable> customData = new HashMap<>();
        CustomisedITextTable viewTable = new CustomisedITextTable();
        viewTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        viewTable.addRowWithCode(NARRATION, commonLocalizer.localize(PdfLocalizationName.narration), escapeHtml(item.getTransferName()));
        viewTable.addRowWithCode(DATE, commonLocalizer.localize(PdfLocalizationName.date), item.getDate() != null ? dateFormat(item.getDate().getDate()) : "");
        viewTable.addRowWithCode(NUMBER, commonLocalizer.localize(PdfLocalizationName.number), escapeHtml(item.getNumber()));
        viewTable.addRowWithCode(CREATOR, "", creatorName);
        customData.put("VIEW_TABLE", viewTable);

        CustomisedITextTable productTable = new CustomisedITextTable();
        productTable.addColumn(FROM_WAREHOUSE, "From Warehouse");
        productTable.addColumn(TO_WAREHOUSE, "To Warehouse");
        productTable.addColumn(PRODUCT_NAME, "Product Name");
        productTable.addColumn(ITEM_QTY, "Quantity");
        productTable.addColumn(ITEM_UNIT_MEASUREMENT, "U/M");
        productTable.addColumn(ITEM_NO, "Number");

        List<String> columnsValue = Lists.newArrayList();

        for (int i = 0; i < item.getAdjustmentItemList().size(); i++) {
            if (item.getAdjustmentItemList().get(i).getProductItems() == null
                    || item.getAdjustmentItemList().get(i).getProductItems().length <= 0) {
                continue;
            }

            if (item.getAdjustmentItemList().get(i).getProductItems()[0].getAssignedBatchItems() != null
                    && !item.getAdjustmentItemList().get(i).getProductItems()[0].getAssignedBatchItems().isEmpty()) {
                productTable.addColumn(ITEM_SERIAL_NUMBER, "Serial Number");
                productTable.addColumn(ITEM_EXPIRATION_DATE, "Expiry Date");
                List<ProductTrackBatchItem> lists = item.getAdjustmentItemList().get(i).getProductItems()[0].getAssignedBatchItems();
                for (ProductTrackBatchItem list : lists) {
                    columnsValue.clear();

                    columnsValue.add(escapeHtml(item.getAdjustmentItemList().get(i).getProductItems()[0].getFromWarehouseName()));
                    i++;
                    columnsValue.add(escapeHtml(item.getAdjustmentItemList().get(i).getProductItems()[0].getToWarehouseName()));
                    columnsValue.add(escapeHtml(item.getAdjustmentItemList().get(i).getProductItems()[0].getName()));

                    columnsValue.add(list.getQty() != null ? escapeHtml(list.getQty().toString()) : "");
                    columnsValue.add(escapeHtml(item.getAdjustmentItemList().get(i).getProductItems()[0].getUnitMeasurementName()));
                    columnsValue.add(escapeHtml(item.getAdjustmentItemList().get(i).getProductItems()[0].getProductNumber()));

                    columnsValue.add(escapeHtml(list.getSerial()));
                    String expireDate =  dateFormat(list.getExpirationDate(), false);
                    columnsValue.add(expireDate);

                    productTable.addRow(columnsValue.toArray(new String[]{}));
                    i--;
                }
                i++;
            } else {

                columnsValue.clear();
                columnsValue.add(escapeHtml(item.getAdjustmentItemList().get(i).getProductItems()[0].getFromWarehouseName()));
                i++;
                columnsValue.add(escapeHtml(item.getAdjustmentItemList().get(i).getProductItems()[0].getToWarehouseName()));
                columnsValue.add(escapeHtml(item.getAdjustmentItemList().get(i).getProductItems()[0].getName()));
                columnsValue.add(item.getAdjustmentItemList().get(i).getProductItems()[0].getQty() != null
                        ? escapeHtml(item.getAdjustmentItemList().get(i).getProductItems()[0].getQty().toString())
                        : "");
                columnsValue.add(escapeHtml(item.getAdjustmentItemList().get(i).getProductItems()[0].getUnitMeasurementName()));
                columnsValue.add(escapeHtml(item.getAdjustmentItemList().get(i).getProductItems()[0].getProductNumber()));
                productTable.addRow(columnsValue.toArray(new String[]{}));
            }
        }

        customData.put("PRODUCT_TABLE", productTable);

        ITextGenericPdfData pdf = new ITextGenericPdfData();
        pdf.setCustomData(customData);
        return pdf;
    }

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        InvoiceQuoteRequestObject requestObject = new InvoiceQuoteRequestObject();
        requestObject.setObjectID(Integer.valueOf(request.getParameter("objectID")));
        return requestObject;
    }

    @Override
    protected Integer getCustomisedPDFTemplateId(Object object) {
        if (object instanceof InvoiceQuoteRequestObject) {
            return ((InvoiceQuoteRequestObject) object).getTemplateID();
        }
        return null;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName("StockTransferView" + dateFormat(user.getUserDate()));
    }

    @Override
    protected String getTableName(Object dataClass) {
        return commonLocalizer.localize(PdfLocalizationName.stockTransfer);
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.STOCK_TRANSFER;
    }
}
