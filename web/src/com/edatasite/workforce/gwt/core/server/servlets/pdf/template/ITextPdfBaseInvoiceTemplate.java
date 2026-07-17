package com.edatasite.workforce.gwt.core.server.servlets.pdf.template;

import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextBaseInvoice;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.lowagie.text.Anchor;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.*;
import java.io.IOException;
import java.util.Map;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 24-Jun-2010
 * Time: 18:12:33
 * All Invoice pdf generate Pdf template
 */
public class ITextPdfBaseInvoiceTemplate extends PdfPTable implements ITextPdfTemplate {

    public static final String TC_CONSOLIDATED_INVOICE = "TC_CONSOLIDATED_INVOICE";

    private ITextBaseInvoice baseInvoice;
    private Document document;

    public ITextPdfBaseInvoiceTemplate(ITextBaseInvoice baseInvoice) {
        super(2);
        this.baseInvoice = baseInvoice;
        initialization();
    }

    /**
     * set Default properties
     */
    private void initialization() {
        this.setWidthPercentage(100);
        this.setHorizontalAlignment(Element.ALIGN_LEFT);
        this.getDefaultCell().setBorder(0);
        this.getDefaultCell().setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
    }

    /**
     * Generater Accounting Tempalte method
     *
     * @return PdfPTable pdf object
     * @throws DocumentException
     * @throws IOException
     */
    public Element generatePdf(Document doc) throws DocumentException, IOException {
        this.document = doc;

        String fontName = baseInvoice.getFontName() != null ? baseInvoice.getFontName() : ITextFontTypeEnum.TIMES_NEW_ROMAN.getName();

        if (baseInvoice.isPackingSlip()) {
            if (baseInvoice.getClientSupplierData() != null) {
                Font font12 = FontFactory.getFont(fontName, BaseFont.IDENTITY_H, 12);
                Font font12Bold = FontFactory.getFont(fontName, BaseFont.IDENTITY_H, 12, Font.BOLD);

                PdfPTable addressTable = new PdfPTable(2);
                addressTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
                addressTable.setWidths(new float[]{20f, 100f});

                Map<String, String> addressDataAsMap = baseInvoice.getClientSupplierData();
                boolean containsAddressData = false;
                if (isValid(addressDataAsMap.get(PDFConstants.COMP_BILL_ADDRESS)) || isValid(addressDataAsMap.get(PDFConstants.COMP_BILL_ADDRESS2))
                        || isValid(addressDataAsMap.get(PDFConstants.COMP_BILL_COUNTRY)) || isValid(addressDataAsMap.get(PDFConstants.COMP_BILL_CITY))
                        || isValid(addressDataAsMap.get(PDFConstants.COMP_BILL_STATE)) || isValid(addressDataAsMap.get(PDFConstants.COMP_BILL_ZIPCODE))) {

                    containsAddressData = true;

                    PdfPCell fromPDFCell = new PdfPCell(new Phrase("From", font12Bold));
                    PdfPCell fromAddressDataCell = new PdfPCell();
                    fromPDFCell.setBorder(0);
                    fromAddressDataCell.setBorder(0);
                    fromAddressDataCell.setPaddingBottom(30f);

                    PdfPTable dataTable = new PdfPTable(1);
                    dataTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
                    dataTable.getDefaultCell().setBorder(0);
                    if (isValid(addressDataAsMap.get(PDFConstants.COMP_BILL_ADDRESS))) {
                        dataTable.addCell(new Phrase(addressDataAsMap.get(PDFConstants.COMP_BILL_ADDRESS), font12));
                    }
                    if (isValid(addressDataAsMap.get(PDFConstants.COMP_BILL_ADDRESS2))) {
                        dataTable.addCell(new Phrase(addressDataAsMap.get(PDFConstants.COMP_BILL_ADDRESS2), font12));
                    }
                    if (isValid(addressDataAsMap.get(PDFConstants.COMP_BILL_COUNTRY))) {
                        dataTable.addCell(new Phrase(addressDataAsMap.get(PDFConstants.COMP_BILL_COUNTRY), font12));
                    }
                    if (isValid(addressDataAsMap.get(PDFConstants.COMP_BILL_CITY))) {
                        dataTable.addCell(new Phrase(addressDataAsMap.get(PDFConstants.COMP_BILL_CITY), font12));
                    }
                    if (isValid(addressDataAsMap.get(PDFConstants.COMP_BILL_STATE))) {
                        dataTable.addCell(new Phrase(addressDataAsMap.get(PDFConstants.COMP_BILL_STATE), font12));
                    }
                    if (isValid(addressDataAsMap.get(PDFConstants.COMP_BILL_ZIPCODE))) {
                        dataTable.addCell(new Phrase(addressDataAsMap.get(PDFConstants.COMP_BILL_ZIPCODE), font12));
                    }
                    fromAddressDataCell.addElement(dataTable);

                    addressTable.addCell(fromPDFCell);
                    addressTable.addCell(fromAddressDataCell);
                }
                if (isValid(addressDataAsMap.get(PDFConstants.MAIL_ADDRESS)) || isValid(addressDataAsMap.get(PDFConstants.MAIL_ADDRESS2))
                        || isValid(addressDataAsMap.get(PDFConstants.MAIL_COUNTRY)) || isValid(addressDataAsMap.get(PDFConstants.MAIL_CITY))
                        || isValid(addressDataAsMap.get(PDFConstants.MAIL_STATE)) || isValid(addressDataAsMap.get(PDFConstants.MAIL_ZIPCODE))) {

                    containsAddressData = true;

                    PdfPCell toPDFCell = new PdfPCell(new Phrase("To", font12Bold));
                    PdfPCell toAddressDataCell = new PdfPCell();
                    toPDFCell.setBorder(0);
                    toAddressDataCell.setBorder(0);

                    PdfPTable dataTable = new PdfPTable(1);
                    dataTable.getDefaultCell().setBorder(0);
                    dataTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
                    if (isValid(addressDataAsMap.get(PDFConstants.MAIL_ADDRESS))) {
                        dataTable.addCell(new Phrase(addressDataAsMap.get(PDFConstants.MAIL_ADDRESS), font12));
                    }
                    if (isValid(addressDataAsMap.get(PDFConstants.MAIL_ADDRESS2))) {
                        dataTable.addCell(new Phrase(addressDataAsMap.get(PDFConstants.MAIL_ADDRESS2), font12));
                    }
                    if (isValid(addressDataAsMap.get(PDFConstants.MAIL_COUNTRY))) {
                        dataTable.addCell(new Phrase(addressDataAsMap.get(PDFConstants.MAIL_COUNTRY), font12));
                    }
                    if (isValid(addressDataAsMap.get(PDFConstants.MAIL_CITY))) {
                        dataTable.addCell(new Phrase(addressDataAsMap.get(PDFConstants.MAIL_CITY), font12));
                    }
                    if (isValid(addressDataAsMap.get(PDFConstants.MAIL_STATE))) {
                        dataTable.addCell(new Phrase(addressDataAsMap.get(PDFConstants.MAIL_STATE), font12));
                    }
                    if (isValid(addressDataAsMap.get(PDFConstants.MAIL_ZIPCODE))) {
                        dataTable.addCell(new Phrase(addressDataAsMap.get(PDFConstants.MAIL_ZIPCODE), font12));
                    }
                    toAddressDataCell.addElement(dataTable);

                    addressTable.addCell(toPDFCell);
                    addressTable.addCell(toAddressDataCell);
                }

                if (containsAddressData) {
                    PdfPTable pSlipAddressTable = new PdfPTable(1);
                    pSlipAddressTable.setWidthPercentage(100);
                    PdfPCell addressPCell = new PdfPCell();
                    addressPCell.setBorder(0);
                    addressPCell.addElement(addressTable);
                    pSlipAddressTable.addCell(addressPCell);
                    this.document.add(pSlipAddressTable);
                    document.newPage(); // Page Break. It is like ctrl+Enter on MSWord
                }
            }

            if (baseInvoice.getPackingSlipTitle() != null) {
                PdfPTable themaTable = new PdfPTable(1);
                themaTable.setTotalWidth(document.getPageSize().getWidth());
                themaTable.getDefaultCell().setBorder(0);
                themaTable.getDefaultCell().setPaddingBottom(15);
                themaTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                themaTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
                themaTable.addCell(new Phrase(baseInvoice.getPackingSlipTitle(), FontFactory.getFont(fontName, BaseFont.IDENTITY_H, 12, Font.BOLD)));
                document.add(themaTable);
            }
        }

        if (baseInvoice.getClientSupplierData() != null || baseInvoice.getNumberAndDatesTable() != null) {
            PdfPTable topTables = new PdfPTable(2);
            topTables.setHorizontalAlignment(Element.ALIGN_LEFT);
            topTables.getDefaultCell().setBorder(0);
            topTables.getDefaultCell().setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
            topTables.setWidthPercentage(100);

            topTables.addCell(drawAddressTable(baseInvoice, fontName));

            if (baseInvoice.getNumberAndDatesTable() != null) {
                topTables.addCell(drawNumberAndDates(baseInvoice.getNumberAndDatesTable(), baseInvoice.getNumberAndDatesTableName(), fontName));
            } else {
                topTables.addCell("");
            }
            this.document.add(topTables);
        }
        if (baseInvoice.getPoDataTable() != null) {
            document.add(drawPoTable(baseInvoice.getPoDataTable()));
        }
        if (baseInvoice.getIntroduction() != null) {
            document.add(drawIntroductions(baseInvoice.getIntroduction(), null, fontName)); //this is by Normurod
        }
        if (baseInvoice.getProductTable() != null) {
            document.add(drawProductItems(baseInvoice.getProductTable(), baseInvoice.getProductTableName()));
        }
        if (baseInvoice.getExpenseTable() != null) {
            document.add(drawExpenseItems(baseInvoice.getExpenseTable(), baseInvoice.getExpenseTableName()));
        }
        if (baseInvoice.getInvoiceTotalTable() != null) {
            document.add(drawTotals(baseInvoice.getInvoiceTotalTable(), baseInvoice.getInvoiceTotalTableName(), fontName));
        }
        if (baseInvoice.getPaypallData() != null) {
            document.add(drawPayPallTable(fontName));
        }
        if (baseInvoice.getGoogleData() != null) {
            document.add(drawGoogleTable(fontName));
        }
        if (baseInvoice.getMasterCardData() != null) {
            document.add(drawMastercardTable(fontName));
        }
        if (baseInvoice.getElavonData() != null) {
            document.add(drawElavonTable(fontName));
        }
        if (baseInvoice.getTermsConditions() != null) {
            document.add(drawTermsConditaions(baseInvoice.getTermsConditions(), baseInvoice.getTermsConditionsName(), fontName));
        }
        if (baseInvoice.getBank() != null || baseInvoice.getAccount() != null) {
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.getDefaultCell().setBorderWidth(0);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);
            if (baseInvoice.getBank() != null) {
                table.addCell(drawBankAcccount(baseInvoice.getBank(), fontName));
            } else {
                table.addCell("");
            }
            if (baseInvoice.getAccount() != null) {
                table.addCell(drawAccount(baseInvoice.getAccount(), fontName));
            } else {
                table.addCell("");
            }
            document.add(table);
        }
        return this;
    }

    private Element drawExpenseItems(ITextTableList expenseTable, String expenseTableName) throws IOException, DocumentException {
        expenseTable.setName(expenseTableName);
        ITextPdfTableListTemplate expenseItemTable = new ITextPdfTableListTemplate(expenseTable);
        return expenseItemTable.generatePdf(document);
    }

    /**
     * @param account
     * @return
     */
    private PdfPTable drawAccount(ITextTableList account, String fontName) {
        PdfPTable bankTable = new PdfPTable(2);
        bankTable.getDefaultCell().setPadding(3);
        bankTable.getDefaultCell().setBorderWidth(.5f);
        bankTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
        bankTable.setTotalWidth(200);
        bankTable.setLockedWidth(true);
        bankTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

        Font font8 = FontFactory.getFont(fontName, BaseFont.IDENTITY_H, 8);
        for (int i = 0; i < account.getTableRows().size(); i++) {
            PdfPCell bankTitleCell = new PdfPCell(new Phrase(account.getTableRows().get(i)[0].getText(), font8));
            bankTitleCell.setBorderWidth(0.5f);
            bankTitleCell.setPadding(3);
            bankTitleCell.setBackgroundColor(Color.LIGHT_GRAY);
            bankTitleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            bankTable.addCell(bankTitleCell);
//            bankTable.addCell(new Phrase(account.getTableRows().get(i)[1].getText(), font8));

            PdfPCell value = new PdfPCell(new Phrase(account.getTableRows().get(i)[1].getText(), font8));
            String text = account.getTableRows().get(i)[1].getText();
            Boolean rtl = Utils.isRTL(text);
            if (rtl) {
                value.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                value.setHorizontalAlignment(ALIGN_LEFT);
            } else {
                value.setHorizontalAlignment(ALIGN_RIGHT);
            }
            bankTable.addCell(value);
        }

        return bankTable;
    }


    /**
     * Draw Purchese Order Table
     *
     * @param poDataTable
     * @throws IOException
     * @throws DocumentException return PdfPTable
     */
    private Element drawPoTable(ITextTableList poDataTable) throws IOException, DocumentException {
        poDataTable.setBeforSpacing(10);
        ITextPdfTableListTemplate poTable = new ITextPdfTableListTemplate(poDataTable);
        return poTable.generatePdf(document);
    }

    /**
     * Cretae Bank data Table
     *
     * @param bankAccount
     */
    private PdfPTable drawBankAcccount(ITextTableList bankAccount, String fontName) {
        PdfPTable bankTable = new PdfPTable(2);
        bankTable.getDefaultCell().setPadding(3);
        bankTable.getDefaultCell().setBorderWidth(.5f);
        bankTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
        bankTable.setTotalWidth(200);
        bankTable.setLockedWidth(true);
        bankTable.setHorizontalAlignment(Element.ALIGN_LEFT);

        Font font8 = FontFactory.getFont(fontName, BaseFont.IDENTITY_H, 8);

        for (int i = 0; i < bankAccount.getTableRows().size(); i++) {
            PdfPCell bankTitleCell = new PdfPCell(new Phrase(bankAccount.getTableRows().get(i)[0].getText(), font8));
            bankTitleCell.setBorderWidth(0.5f);
            bankTitleCell.setPadding(3);
            bankTitleCell.setBackgroundColor(Color.LIGHT_GRAY);
            bankTitleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            bankTable.addCell(bankTitleCell);

            PdfPCell value = new PdfPCell(new Phrase(bankAccount.getTableRows().get(i)[1].getText(), font8));
            String text = bankAccount.getTableRows().get(i)[1].getText();
            Boolean rtl = Utils.isRTL(text);
            if (rtl) {
                value.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                value.setHorizontalAlignment(ALIGN_LEFT);
            } else {
                value.setHorizontalAlignment(ALIGN_RIGHT);
            }
            bankTable.addCell(value);
        }
        return bankTable;
    }

    /**
     * Create Google Table
     */
    private PdfPTable drawGoogleTable(String fontName) throws IOException, DocumentException {
        PdfPTable googleTable = new PdfPTable(2);
        googleTable.setSpacingAfter(10);
        googleTable.getDefaultCell().setBorderWidth(0.0f);
        googleTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        googleTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
        googleTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        googleTable.setTotalWidth(200);
        googleTable.setWidths(new float[]{0.7f, 0.3f});
        googleTable.setLockedWidth(true);
        googleTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

        java.util.List<String> linkImg = baseInvoice.getGoogleData();


        Anchor anchor = new Anchor("Pay By Google Checkout", FontFactory.getFont(fontName, 9, Font.NORMAL, Color.BLUE));
        anchor.setReference(linkImg.get(0));
        googleTable.addCell(anchor);

        Image googleImg = Image.getInstance(linkImg.get(1));
        googleImg.scalePercent(80);
        Chunk imgChunk = new Chunk(googleImg, 0, 0);
        PdfPCell cell = new PdfPCell(new Phrase(imgChunk));
        cell.setBorderWidth(0);
        cell.setPaddingRight(20);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        googleTable.addCell(cell);

        return googleTable;
    }

    /**
     * Create Mastercard Table
     */
    private PdfPTable drawMastercardTable(String fontName) throws IOException, DocumentException {
        PdfPTable googleTable = new PdfPTable(2);
        googleTable.setSpacingAfter(10);
        googleTable.getDefaultCell().setBorderWidth(0.0f);
        googleTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        googleTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
        googleTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        googleTable.setTotalWidth(200);
        googleTable.setWidths(new float[]{0.7f, 0.3f});
        googleTable.setLockedWidth(true);
        googleTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

        java.util.List<String> linkImg = baseInvoice.getMasterCardData();


        Anchor anchor = new Anchor("Pay By Mastercard", FontFactory.getFont(fontName, 9, Font.NORMAL, Color.BLUE));
        anchor.setReference(linkImg.get(0));
        googleTable.addCell(anchor);

        Image googleImg = Image.getInstance(linkImg.get(1));
        googleImg.scalePercent(80);
        Chunk imgChunk = new Chunk(googleImg, 0, 0);
        PdfPCell cell = new PdfPCell(new Phrase(imgChunk));
        cell.setBorderWidth(0);
        cell.setPaddingRight(20);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        googleTable.addCell(cell);

        return googleTable;
    }

    /**
     * Create Elavon Table
     */
    private PdfPTable drawElavonTable(String fontName) throws IOException, DocumentException {
        PdfPTable elavonTable = new PdfPTable(1);
        elavonTable.setSpacingAfter(10);
        elavonTable.getDefaultCell().setBorderWidth(0.0f);
        elavonTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        elavonTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
        elavonTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        elavonTable.setTotalWidth(200);
        elavonTable.setLockedWidth(true);
        elavonTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

        java.util.List<String> linkImg = baseInvoice.getElavonData();


        Anchor anchor = new Anchor("Pay By Elavon", FontFactory.getFont(fontName, 9, Font.NORMAL, Color.BLUE));
        anchor.setReference(linkImg.get(0));
        elavonTable.addCell(anchor);

        return elavonTable;
    }

    /**
     * Create Paypall Table
     */
    private PdfPTable drawPayPallTable(String fontName) throws IOException, DocumentException {
        PdfPTable paypallTable = new PdfPTable(2);
        paypallTable.getDefaultCell().setBorderWidth(0.0f);
        paypallTable.setSpacingAfter(20);
        paypallTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        paypallTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
        paypallTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        paypallTable.setTotalWidth(200);
        paypallTable.setWidths(new float[]{0.7f, 0.3f});
        paypallTable.setLockedWidth(true);
        paypallTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

        java.util.List<String> linkImg = baseInvoice.getPaypallData();
        Anchor anchor = new Anchor("Pay By Paypal", FontFactory.getFont(fontName, 9, Font.NORMAL, Color.BLUE));
        anchor.setReference(linkImg.get(0));
        paypallTable.addCell(anchor);
        Image paypallImg = Image.getInstance(linkImg.get(1));
        paypallImg.scalePercent(80);
        Chunk imgChunk = new Chunk(paypallImg, 0, 0);
        PdfPCell cell = new PdfPCell(new Phrase(imgChunk));
        cell.setBorderWidth(0);
        cell.setPaddingRight(20);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        paypallTable.addCell(cell);

        return paypallTable;
    }


    private PdfPCell drawAddressTable(ITextBaseInvoice baseInvoice, String fontName) {

        PdfPTable clientSupplierTable = new PdfPTable(1);
        clientSupplierTable.getDefaultCell().setPaddingLeft(10);
        clientSupplierTable.getDefaultCell().setBorder(0);
        clientSupplierTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        clientSupplierTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        clientSupplierTable.setWidthPercentage(100);

        clientSupplierTable.addCell(drawClientSupplierTable(baseInvoice.getClientSupplierData(), fontName));

        PdfPCell billCell = new PdfPCell(clientSupplierTable);
        billCell.setPadding(5);
        billCell.setBorder(0);
        billCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        billCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return billCell;
    }

    /**
     * @param data
     */
    private PdfPTable drawClientSupplierTable(Map<String, String> data, String fontName) {
        PdfPTable dataTable = createTable(1);
        dataTable.setTotalWidth(100);

        /*TC CONSOLIDATED INVOICE START*/
        String dataType = data.get(PDFConstants.TYPE);
        if (TC_CONSOLIDATED_INVOICE.equals(dataType)) {
            if (isValid(data.get(PDFConstants.TC_REFERENCE_NUMBER))) {
                addCell("Ref #: " + data.get(PDFConstants.TC_REFERENCE_NUMBER), dataTable, false, fontName);
            }
            if (isValid(data.get(PDFConstants.TC_CUSTOMER_NAME))) {
                addCell("Customer: " + data.get(PDFConstants.TC_CUSTOMER_NAME), dataTable, false, fontName);
            }
            if (isValid(data.get(PDFConstants.TC_PERIOD))) {
                addCell("Period: " + data.get(PDFConstants.TC_PERIOD), dataTable, false, fontName);
            }
            if (isValid(data.get(PDFConstants.TC_LOCATION))) {
                addCell("Location: " + data.get(PDFConstants.TC_LOCATION), dataTable, false, fontName);
            }
            return dataTable;
        }
        /*TC CONSOLIDATED INVOICE END*/

        boolean isReceivable = Constants.RECEIVABLE.equals(dataType);

        if (isValid(data.get(PDFConstants.HEADER))) {
            addCell(data.get(PDFConstants.HEADER), dataTable, true, fontName);
        }
        addCell(data.get(PDFConstants.NAME), dataTable, false, fontName);

        if (isValid(data.get(PDFConstants.REPORTER))) {
            addCell(data.get(PDFConstants.REPORTER), dataTable, false, fontName);
        }
        if (isValid(data.get(PDFConstants.APPROVER))) {
            addCell(data.get(PDFConstants.APPROVER), dataTable, false, fontName);
        }

        if (isValid(data.get(PDFConstants.CLIENT_CONTACT))) {
            addCell(data.get(PDFConstants.CLIENT_CONTACT), dataTable, false, fontName);
        }
        PdfPTable billToTable = null, shipToTable = null;
        if (isReceivable) {
            if (isValid(data.get(PDFConstants.BILL_ADDRESS)) || isValid(data.get(PDFConstants.BILL_CITYSTATEZIP)) || isValid(data.get(PDFConstants.BILL_COUNTRY))) {
                billToTable = createTable(1);
                addCell(data.get(PDFConstants.BILL_TO_HEADER), billToTable, true, fontName);
                if (isValid(data.get(PDFConstants.BILL_ADDRESS))) {
                    addCell(data.get(PDFConstants.BILL_ADDRESS), billToTable, false, fontName);
                }
                if (isValid(data.get(PDFConstants.BILL_ADDRESS2))) {
                    addCell(data.get(PDFConstants.BILL_ADDRESS2), billToTable, false, fontName);
                }

                if (isValid(data.get(PDFConstants.BILL_CITYSTATEZIP))) {
                    addCell(data.get(PDFConstants.BILL_CITYSTATEZIP), billToTable, false, fontName);
                }
                if (isValid(data.get(PDFConstants.BILL_COUNTRY))) {
                    addCell(data.get(PDFConstants.BILL_COUNTRY), billToTable, false, fontName);
                }
                if (isValid(data.get(PDFConstants.CLIENT_VAT_NUMBER))) {
                    addCell(data.get(PDFConstants.CLIENT_VAT_NUMBER), billToTable, false, fontName);
                }
            }
            if (isValid(data.get(PDFConstants.MAIL_ADDRESS)) || isValid(data.get(PDFConstants.MAIL_CITYSTATEZIP)) || isValid(data.get(PDFConstants.MAIL_COUNTRY))) {
                shipToTable = createTable(1);
                addCell(data.get(PDFConstants.SHIP_TO_HEADER), shipToTable, true, fontName);
                if (isValid(data.get(PDFConstants.MAIL_ADDRESS))) {
                    addCell(data.get(PDFConstants.MAIL_ADDRESS), shipToTable, false, fontName);
                }
                if (isValid(data.get(PDFConstants.MAIL_ADDRESS2))) {
                    addCell(data.get(PDFConstants.MAIL_ADDRESS2), shipToTable, false, fontName);
                }
                if (isValid(data.get(PDFConstants.MAIL_CITYSTATEZIP))) {
                    addCell(data.get(PDFConstants.MAIL_CITYSTATEZIP), shipToTable, false, fontName);
                }
                if (isValid(data.get(PDFConstants.MAIL_COUNTRY))) {
                    addCell(data.get(PDFConstants.MAIL_COUNTRY), shipToTable, false, fontName);
                }
            }
        } else {
            if (isValid(data.get(PDFConstants.BILL_ADDRESS)) || isValid(data.get(PDFConstants.BILL_ADDRESS2))
                    || isValid(data.get(PDFConstants.BILL_CITYSTATEZIP)) || isValid(data.get(PDFConstants.BILL_COUNTRY))) {
                PdfPTable supplierTable = createTable(1);
                if (isValid(data.get(PDFConstants.BILL_ADDRESS))) {
                    addCell(data.get(PDFConstants.BILL_ADDRESS), supplierTable, false, fontName);
                }
                if (isValid(data.get(PDFConstants.BILL_ADDRESS2))) {
                    addCell(data.get(PDFConstants.BILL_ADDRESS2), supplierTable, false, fontName);
                }
                if (isValid(data.get(PDFConstants.BILL_CITYSTATEZIP))) {
                    addCell(data.get(PDFConstants.BILL_CITYSTATEZIP), supplierTable, false, fontName);
                }
                if (isValid(data.get(PDFConstants.BILL_COUNTRY))) {
                    addCell(data.get(PDFConstants.BILL_COUNTRY), supplierTable, false, fontName);
                }
                dataTable.addCell(supplierTable);
            }

            if (isValid(data.get(PDFConstants.PURCHASE_CLIENT_NAME))) {
                if (isValid(data.get(PDFConstants.PURCHASE_CLIENT_HEADER))) {
                    addCell(data.get(PDFConstants.PURCHASE_CLIENT_HEADER), dataTable, true, fontName);
                }
                addCell(data.get(PDFConstants.PURCHASE_CLIENT_NAME), dataTable, false, fontName);
            }
            if (isValid(data.get(PDFConstants.COMP_BILL_ADDRESS)) || isValid(data.get(PDFConstants.COMP_BILL_ADDRESS2))
                    || isValid(data.get(PDFConstants.COMP_BILL_CITYSTATEZIP)) || isValid(data.get(PDFConstants.COMP_BILL_COUNTRY))) {
                billToTable = createTable(1);
                addCell(data.get(PDFConstants.BILL_TO_HEADER), billToTable, true, fontName);
                if (isValid(data.get(PDFConstants.COMP_BILL_ADDRESS))) {
                    addCell(data.get(PDFConstants.COMP_BILL_ADDRESS), billToTable, false, fontName);
                }
                if (isValid(data.get(PDFConstants.COMP_BILL_ADDRESS2))) {
                    addCell(data.get(PDFConstants.COMP_BILL_ADDRESS2), billToTable, false, fontName);
                }
                if (isValid(data.get(PDFConstants.COMP_BILL_CITYSTATEZIP))) {
                    addCell(data.get(PDFConstants.COMP_BILL_CITYSTATEZIP), billToTable, false, fontName);
                }
                if (isValid(data.get(PDFConstants.COMP_BILL_COUNTRY))) {
                    addCell(data.get(PDFConstants.COMP_BILL_COUNTRY), billToTable, false, fontName);
                }
            }
            if (isValid(data.get(PDFConstants.COMP_MAIL_ADDRESS)) || isValid(data.get(PDFConstants.COMP_MAIL_ADDRESS2))
                    || isValid(data.get(PDFConstants.COMP_MAIL_CITYSTATEZIP)) || isValid(data.get(PDFConstants.COMP_MAIL_COUNTRY))) {
                shipToTable = createTable(1);
                addCell(data.get(PDFConstants.SHIP_TO_HEADER), shipToTable, true, fontName);
                if (isValid(data.get(PDFConstants.COMP_MAIL_ADDRESS))) {
                    addCell(data.get(PDFConstants.COMP_MAIL_ADDRESS), shipToTable, false, fontName);
                }
                if (isValid(data.get(PDFConstants.COMP_MAIL_ADDRESS2))) {
                    addCell(data.get(PDFConstants.COMP_MAIL_ADDRESS2), shipToTable, false, fontName);
                }
                if (isValid(data.get(PDFConstants.COMP_MAIL_CITYSTATEZIP))) {
                    addCell(data.get(PDFConstants.COMP_MAIL_CITYSTATEZIP), shipToTable, false, fontName);
                }
                if (isValid(data.get(PDFConstants.COMP_MAIL_COUNTRY))) {
                    addCell(data.get(PDFConstants.COMP_MAIL_COUNTRY), shipToTable, false, fontName);
                }
            }
        }
        if (billToTable != null && shipToTable != null) {
            PdfPTable billShipTable = createTable(2);
            billShipTable.addCell(billToTable);
            billShipTable.addCell(shipToTable);
            dataTable.addCell(billShipTable);
        } else if (billToTable != null) {
            dataTable.addCell(billToTable);
        } else if (shipToTable != null) {
            dataTable.addCell(shipToTable);
        }
        return dataTable;
    }

    private PdfPTable createTable(int columns) {
        PdfPTable table = new PdfPTable(columns);
        table.getDefaultCell().setBorder(0);
        table.getDefaultCell().setPadding(0);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        return table;
    }

    private boolean isValid(String text) {
        return text != null && !"".equals(text.trim());
    }

    private void addCell(String text, PdfPTable table, boolean bold, String fontName) {
        PdfPCell contact = new PdfPCell(new Phrase(text, FontFactory.getFont(fontName, BaseFont.IDENTITY_H, 8, bold ? Font.BOLD : Font.NORMAL)));
        contact.setBorder(0);
        contact.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(contact);
    }

    /**
     * Number And Dates Table
     *
     * @param numberAndDates
     * @param numberAndDatesTableName
     */
    private PdfPCell drawNumberAndDates(ITextTableList numberAndDates, String numberAndDatesTableName, String fontName) throws DocumentException {
        PdfPTable numberDate = new PdfPTable(numberAndDates.getNumColumns());
        numberDate.getDefaultCell().setBorderWidth(0.5f);
        numberDate.getDefaultCell().setPadding(3);
        numberDate.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
        numberDate.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        numberDate.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        numberDate.setHorizontalAlignment(Element.ALIGN_RIGHT);
        numberDate.setTotalWidth(200);
        numberDate.setLockedWidth(true);

        if (numberAndDates.getTotalWidth() != null) {
            numberDate.setTotalWidth(numberAndDates.getTotalWidth());
        }
        if (numberAndDates.getColWidthPercentage() != null && numberAndDates.getColWidthPercentage().length != 0) {
            numberDate.setWidths(numberAndDates.getColWidthPercentage());
        }

        Font font8 = FontFactory.getFont(fontName, BaseFont.IDENTITY_H, 8);
        Font font8Bold = FontFactory.getFont(fontName, BaseFont.IDENTITY_H, 8, Font.BOLD);

        if (numberAndDatesTableName != null && !"".equals(numberAndDatesTableName)) {
            PdfPCell tableName = new PdfPCell(new Phrase(numberAndDatesTableName, font8Bold));
            tableName.setBorder(0);
            tableName.setPadding(3);
            tableName.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableName.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tableName.setColspan(numberAndDates.getNumColumns());
            numberDate.addCell(tableName);
        }
        if (numberAndDates.getTableRows().size() != 0) {
            for (CellData[] data : numberAndDates.getTableRows()) {
                for (int i = 0; i < data.length; i++) {
                    if (i == 0) {
                        PdfPCell header = new PdfPCell(new Phrase(data[0].getText(), font8Bold));
                        header.setBorderWidth(0.5f);
                        header.setPadding(3);
                        header.setBackgroundColor(Color.LIGHT_GRAY);
                        numberDate.addCell(header);
                    } else {
                        numberDate.addCell(new Phrase(data[i].getText(), font8));
                    }
                }
            }
        }
        PdfPCell numDateCell = new PdfPCell(numberDate);
        numDateCell.setPadding(5);
        numDateCell.setBorder(0);
        numDateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        numDateCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return numDateCell;
    }

    /**
     * Product items table
     *
     * @param productTable
     * @param productTableName
     */
    private Element drawProductItems(ITextTableList productTable, String productTableName) throws DocumentException, IOException {
        productTable.setName(productTableName);
        ITextPdfTableListTemplate productItemTable = new ITextPdfTableListTemplate(productTable);
        return productItemTable.generatePdf(document);
    }

    /**
     * All Totals table
     *
     * @param invoiceTotalData
     * @param invoiceTotalTableName
     */
    private PdfPTable drawTotals(ITextTableList invoiceTotalData, String invoiceTotalTableName, String fontName) throws DocumentException {
        PdfPTable invoiceTable = new PdfPTable(invoiceTotalData.getNumColumns());
        invoiceTable.getDefaultCell().setBorderWidth(0.5f);
        invoiceTable.getDefaultCell().setPadding(3);
        invoiceTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
        invoiceTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoiceTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        invoiceTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
        invoiceTable.setTotalWidth(200);
        invoiceTable.setLockedWidth(true);

        if (invoiceTotalData.getTotalWidth() != null) {
            invoiceTable.setTotalWidth(invoiceTotalData.getTotalWidth());
        }
        if (invoiceTotalData.getColWidthPercentage() != null && invoiceTotalData.getColWidthPercentage().length != 0) {
            invoiceTable.setWidths(invoiceTotalData.getColWidthPercentage());
        }

        if (invoiceTotalTableName != null && !"".equals(invoiceTotalTableName)) {
            PdfPCell tableName = new PdfPCell(new Phrase(invoiceTotalTableName, FontFactory.getFont(fontName, BaseFont.IDENTITY_H, 10, Font.BOLD)));
            tableName.setBorder(0);
            tableName.setPadding(5);
            tableName.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableName.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tableName.setColspan(invoiceTotalData.getNumColumns());
            invoiceTable.addCell(tableName);
        }
        Font font8 = FontFactory.getFont(fontName, BaseFont.IDENTITY_H, 8);
        Font font8Bold = FontFactory.getFont(fontName, BaseFont.IDENTITY_H, 8, Font.BOLD);
        if (invoiceTotalData.getTableRows().size() != 0) {
            int step = 0;
            for (CellData[] data : invoiceTotalData.getTableRows()) {
                for (int i = 0; i < data.length; i++) {
                    if (i == 0) {
                        PdfPCell header = new PdfPCell(new Phrase(data[0].getText(), font8Bold));
                        header.setBorderWidth(0.5f);
                        header.setPadding(3);
                        header.setBackgroundColor(Color.LIGHT_GRAY);
                        invoiceTable.addCell(header);
                    } else {
                        invoiceTable.addCell(new Phrase(data[i].getText(), font8));
                    }
                }
            }
        }
        PdfPTable totalTable = new PdfPTable(2);
        totalTable.setWidthPercentage(100);
        totalTable.setSpacingAfter(10);
        totalTable.setSpacingBefore(20);
        totalTable.getDefaultCell().setBorder(0);
        totalTable.getDefaultCell().setPadding(3);
        totalTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalTable.addCell("");
        totalTable.addCell(invoiceTable);
        return totalTable;


    }

    /**
     * Terms and Condations table
     *
     * @param termsConditions
     * @param termsConditionsName
     */
    private PdfPTable drawTermsConditaions(ITextTableList termsConditions, String termsConditionsName, String fontName) throws DocumentException {
        PdfPTable termsConTable = new PdfPTable(termsConditions.getNumColumns());
        termsConTable.setSpacingAfter(20);
        termsConTable.setSpacingBefore(20);
        termsConTable.getDefaultCell().setBorderWidth(0.5f);
        termsConTable.getDefaultCell().setPadding(5);
        termsConTable.getDefaultCell().setPaddingLeft(20);
        termsConTable.getDefaultCell().setPaddingBottom(20);
        termsConTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        termsConTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        termsConTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        termsConTable.setWidthPercentage(100);

        if (termsConditions.getTotalWidth() != null) {
            termsConTable.setTotalWidth(termsConditions.getTotalWidth());
        }
        if (termsConditions.getColWidthPercentage() != null && termsConditions.getColWidthPercentage().length != 0) {
            termsConTable.setWidths(termsConditions.getColWidthPercentage());
        }

        if (termsConditionsName != null && !"".equals(termsConditionsName)) {
            PdfPCell tableName = new PdfPCell(new Phrase(termsConditionsName, FontFactory.getFont(fontName, 10, Font.BOLD)));
            tableName.setBorder(0);
            tableName.setPadding(7);
            tableName.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableName.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tableName.setColspan(termsConditions.getNumColumns());
            termsConTable.addCell(tableName);
        }
        // table header
        if (termsConditions.getTableHeader().size() != 0) {
            for (CellData data : termsConditions.getTableHeader()) {
                PdfPCell header = new PdfPCell(new Phrase(data.getText(), FontFactory.getFont(fontName, 8, Font.BOLD)));
                header.setBorderWidth(0.5f);
                header.setPadding(5);
                header.setBackgroundColor(Color.LIGHT_GRAY);
                header.setHorizontalAlignment(Element.ALIGN_CENTER);
                termsConTable.addCell(header);
            }
            termsConTable.setHeaderRows(termsConTable.size());
        }

        // table content
        Font font8 = FontFactory.getFont(fontName, BaseFont.IDENTITY_H, 8);
        Font font8Link = FontFactory.getFont(fontName, BaseFont.IDENTITY_H, 8);
        font8Link.setColor(Color.BLUE);
        if (termsConditions.getTableRows().size() != 0) {
            for (CellData[] data : termsConditions.getTableRows()) {
                for (CellData aData : data) {
                    PdfPCell value = aData.createPdfCell(font8, font8Link);
                    String text = aData.getText();
                    Boolean rtl = Utils.isRTL(text);
                    if (rtl) {
                        value.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        value.setHorizontalAlignment(ALIGN_LEFT);
                    }
                    termsConTable.addCell(value);
                }
            }
        }
        // table footer
        if (termsConditions.getTableFooter().size() != 0) {
            for (String data : termsConditions.getTableFooter()) {
                PdfPCell header = new PdfPCell(new Phrase(data, FontFactory.getFont(fontName, 8, Font.BOLD)));
                header.setBorder(1);
                header.setPadding(3);
                header.setBackgroundColor(Color.LIGHT_GRAY);
                termsConTable.addCell(header);
            }
            termsConTable.setFooterRows(termsConTable.size());
        }

        return termsConTable;
    }

    private PdfPTable drawIntroductions(ITextTableList introductions, String introductionTableName, String fontName) throws DocumentException {
        PdfPTable introductionTable = new PdfPTable(introductions.getNumColumns());
        introductionTable.setSpacingAfter(20);
        introductionTable.setSpacingBefore(20);
        introductionTable.getDefaultCell().setBorderWidth(0.5f);
        introductionTable.getDefaultCell().setPadding(5);
        introductionTable.getDefaultCell().setPaddingLeft(20);
        introductionTable.getDefaultCell().setPaddingBottom(20);
        introductionTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        introductionTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        introductionTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        introductionTable.setWidthPercentage(100);

        if (introductions.getTotalWidth() != null) {
            introductionTable.setTotalWidth(introductions.getTotalWidth());
        }
        if (introductions.getColWidthPercentage() != null && introductions.getColWidthPercentage().length != 0) {
            introductionTable.setWidths(introductions.getColWidthPercentage());
        }

        if (introductionTableName != null && !"".equals(introductionTableName)) {
            PdfPCell tableName = new PdfPCell(new Phrase(introductionTableName, FontFactory.getFont(fontName, 10, Font.BOLD)));
            tableName.setBorder(0);
            tableName.setPadding(7);
            tableName.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableName.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tableName.setColspan(introductions.getNumColumns());
            introductionTable.addCell(tableName);
        }
        // table header
        if (introductions.getTableHeader().size() != 0) {
            for (CellData data : introductions.getTableHeader()) {
                PdfPCell header = new PdfPCell(new Phrase(data.getText(), FontFactory.getFont(fontName, 8, Font.BOLD)));
                header.setBorderWidth(0.5f);
                header.setPadding(5);
                header.setBackgroundColor(Color.LIGHT_GRAY);
                header.setHorizontalAlignment(Element.ALIGN_CENTER);
                introductionTable.addCell(header);
            }
            introductionTable.setHeaderRows(introductionTable.size());
        }

        // table content
        Font font8 = FontFactory.getFont(fontName, BaseFont.IDENTITY_H, 8);
        Font font8Link = FontFactory.getFont(fontName, BaseFont.IDENTITY_H, 8);
        font8Link.setColor(Color.BLUE);
        if (introductions.getTableRows().size() != 0) {
            for (CellData[] data : introductions.getTableRows()) {
                for (CellData aData : data) {
                    PdfPCell value = new PdfPCell(new Phrase(aData.getText(), font8));
                    String text = aData.getText();
                    Boolean rtl = Utils.isRTL(text);
                    if (rtl) {
                        value.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        value.setHorizontalAlignment(ALIGN_LEFT);
                    }
                    introductionTable.addCell(value);
//                    introductionTable.addCell(data[i].createPdfCell(font8, font8Link));
                }
            }
        }
        // table footer
        if (introductions.getTableFooter().size() != 0) {
            for (String data : introductions.getTableFooter()) {
                PdfPCell header = new PdfPCell(new Phrase(data, FontFactory.getFont(fontName, 8, Font.BOLD)));
                header.setBorder(1);
                header.setPadding(3);
                header.setBackgroundColor(Color.LIGHT_GRAY);
                introductionTable.addCell(header);
            }
            introductionTable.setFooterRows(introductionTable.size());
        }

        return introductionTable;
    }
}
