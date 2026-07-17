package com.edatasite.workforce.gwt.core.server.servlets.pdf.barcodes;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductItem;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.google.gson.Gson;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;

public class ProductsBarcodePDFHandler extends AbstractITextPostPdfHandler {

    private final String barcodeApi = "https://mobiledemand-barcode.azurewebsites.net/barcode/image?content=";

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ProductItem[] productItems = (ProductItem[]) dataClass;
        DecimalFormat priceScaleFormat = new DecimalFormat(",##0");
        for (int i = 0; i < productItems.length; i++) {
            String number = productItems[i].getProductNumber();
            String barcodeImageUrl = barcodeApi + number + "&amp;size=30&amp;symbology=CODE_128&amp;format=png&amp;text=false";
            productItems[i].setBarCodeString(barcodeImageUrl);
            productItems[i].setDescription(productItems[i].getDescription());
            productItems[i].setUnitPrice(priceScaleFormat.format(productItems[i].getUnitpPrice()));
            if (company.getObjectID().equals(67497)) {
                String name = "";
                if (productItems[i].getName().trim().length() > 20) {
                    char[] nameChar = productItems[i].getName().trim().toCharArray();
                    StringBuilder productName = new StringBuilder();
                    int count = 0;
                    for (char element : nameChar) {
                        if (count == 20) {
                            productName.append(element).append(" <br/> ");
                        } else {
                            productName.append(element);
                        }
                        count = count + 1;
                    }
                    name = productName.toString();
                } else {
                    name = productItems[i].getName();
                }
                productItems[i].setName(name);
            }
        }
        ITextGenericPdfData iTextGenericPdfData = new ITextGenericPdfData();
        iTextGenericPdfData.setProductItems(productItems);
        return iTextGenericPdfData;
    }

    private char parseSeparator(String separator, char def) {
        return (separator != null && !"".equals(separator.trim())) ? separator.trim().charAt(0) : def;
    }

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        Gson gson = new Gson();
        return gson.fromJson(request.getParameter("products"), ProductItem[].class);
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.PRODUCTS_BARCODE;
    }

    @Override
    protected Integer getCustomisedPDFTemplateId(Object object) {
        return companyPdfTemplateManager.getCompanyBarcodePDFTemplateId();
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName("PRODUCTS_BARCODE_" + dateFormat(user.getUserDate()));
    }

    @Override
    public String getDownloadType() {
        return PDFDownloadType.INLINE;
    }
}
