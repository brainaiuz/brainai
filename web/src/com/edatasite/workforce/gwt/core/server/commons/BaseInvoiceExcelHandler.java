package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 6/3/13
 * Time: 10:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class BaseInvoiceExcelHandler implements HttpRequestHandler {

    public static final String CONTENT_TYPE_EXCEL = "application/vnd.ms-excel";
    private ByteArrayOutputStream baos;
    private Map<String, CellStyle> styles;
    private SimpleDateFormat shortDateFormat;
    private Integer calculationScale, unitPriceScale;

    @Autowired
    private QuoteService quoteService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private UserManager userManager;

    @Autowired
    private CompanyAttachmentManager companyAttachmentManager;

    @Autowired
    @Qualifier("pdfWfmMessageSource")
    private WfmMessageSource pdfWfmMessageSource;

    @Autowired
    @Qualifier("commonLocalizer")
    protected WfmMessageSource commonLocalizer;

    @Autowired
    private AddressManager addressManager;

    @Autowired
    private CurrencyManager currencyManager;

    @Autowired
    private PaymentMethodManager paymentMethodManager;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        NewInvoice data;
        Integer objectID = Integer.parseInt(request.getParameter("objectID"));
        String isPackingStr = request.getParameter("isPacking");
        boolean isPackingSlip = isPackingStr.equals("true");

        if (isPackingSlip) {
            data = invoiceService.getInvoiceSummaryData(objectID);
        } else {
            data = quoteService.getQuoteSummaryData(objectID);
        }

        writeHeader(data, response, isPackingSlip);
        prepareOutputStream(data, isPackingSlip);
        returnResponse(response);
    }

    private void writeHeader(NewInvoice data, HttpServletResponse response, boolean isPacking) {
        String fileName;
        if (isPacking) {
            fileName = data.getInvoiceNumber() + "_Packing_Slip";
        } else {
            fileName = data.getInvoiceNumber() + "_Purchase_Order";
        }
        response.setHeader("content-disposition", "attachment; filename=\"" + fileName + ".xls\"");
        response.setContentType(CONTENT_TYPE_EXCEL);
        response.setCharacterEncoding("UTF8");
    }


    private void prepareOutputStream(NewInvoice data, boolean isPackingSlip) {

        baos = new ByteArrayOutputStream();

        try {
            HSSFWorkbook wb = generateWorkBook(data, isPackingSlip);
            if (wb != null) {
                wb.write(baos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void returnResponse(HttpServletResponse response) {
        try {
            byte[] data = baos.toByteArray();
            response.setContentLength(data.length);
            response.getOutputStream().write(data);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void createStyles(HSSFWorkbook wb) {
        styles = new HashMap<>();
        CellStyle style;
        Font companyFont = wb.createFont();
        companyFont.setFontHeightInPoints((short) 12);
        companyFont.setColor(setColor(wb, (byte) 84, (byte) 140, (byte) 231).getIndex());
        companyFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style = wb.createCellStyle();
        //style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFont(companyFont);
        styles.put("company", style);

        Font titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short) 12);
        titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFont(titleFont);

        styles.put("title", style);
        Font titleBoldFont = wb.createFont();
        titleFont.setFontHeightInPoints((short) 12);
        titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFont(titleFont);
        styles.put("titleBold", style);

        Font monthFont = wb.createFont();
        monthFont.setFontHeightInPoints((short) 11);
        monthFont.setColor(IndexedColors.BLACK.getIndex());
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setFillForegroundColor(setColor(wb, (byte) 192, (byte) 192, (byte) 192).getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFont(monthFont);
        style.setWrapText(false);
        styles.put("header", style);

        style = wb.createCellStyle();
        //style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setWrapText(true);
        styles.put("cell", style);

        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setWrapText(true);
        style.setBorderRight(CellStyle.BORDER_NONE);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_NONE);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_NONE);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_NONE);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("address", style);

        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setWrapText(true);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFillForegroundColor(setColor(wb, (byte) 192, (byte) 192, (byte) 192).getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setBorderRight(CellStyle.BORDER_NONE);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_NONE);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_NONE);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_NONE);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("label", style);
    }

    public HSSFColor setColor(HSSFWorkbook workbook, byte r, byte g, byte b) {
        HSSFPalette palette = workbook.getCustomPalette();
        HSSFColor hssfColor = null;
        try {
            hssfColor = palette.findColor(r, g, b);
            if (hssfColor == null) {
                palette.setColorAtIndex(HSSFColor.LAVENDER.index, r, g, b);
                hssfColor = palette.getColor(HSSFColor.LAVENDER.index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hssfColor;
    }

    private HSSFWorkbook generateWorkBook(NewInvoice data, boolean isPackingSlip) {
        EdsUser edsUser;
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        sheet.getPrintSetup().setLandscape(true);
        sheet.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        createStyles(workbook);
        if (data.getUserID() != null) {
            edsUser = userManager.get(data.getUserID());
        } else {
            edsUser = userManager.getUser();
        }
        drawHeader(sheet, edsUser, data, isPackingSlip);
        drawCompanyLogo(workbook, sheet, edsUser);

        return workbook;
    }

    private void drawCompanyLogo(HSSFWorkbook workbook, HSSFSheet sheet, EdsUser edsUser) {
        String urlStr = companyAttachmentManager.getCompanyLogoUrl(edsUser.getCompany(), "FOR_INVOICEPDF");
        URL url = null;
        InputStream inputStream = null;
        byte[] bytes = new byte[0];
        try {
            url = new URL(urlStr);
            inputStream = url.openStream();
            bytes = IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (inputStream != null) {
            int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            CreationHelper helper = workbook.getCreationHelper();
            Drawing drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setCol1(11);
            anchor.setRow1(1);
            Picture pict = drawing.createPicture(anchor, pictureIdx);
            pict.getPreferredSize();
            pict.resize();
        }
    }

    private void drawHeader(HSSFSheet sheet, EdsUser user, NewInvoice data, boolean isPackingSlip) {
        int row = 0;

        EdsCompany company = user.getCompany();
        setCompanyShortDateFormat(company);
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        setCalculationScale(fs);
        setUnitPriceScale(fs);
        createHeaderCell(company.getName(), sheet, styles.get("company"), row, 0, 3);
        EdsAddress defaultBillAddress = company.getBillingAddress();
        if (defaultBillAddress != null) {
            if (defaultBillAddress.getAddress() != null && !defaultBillAddress.getAddress().isEmpty()) {
                createHeaderCell(defaultBillAddress.getAddress(), sheet, styles.get("address"), ++row, 0, 3);
            }
            if (defaultBillAddress.getAddressb() != null && !defaultBillAddress.getAddressb().isEmpty()) {
                createHeaderCell(defaultBillAddress.getAddressb(), sheet, styles.get("address"), ++row, 0, 3);
                createHeaderCell("China:", sheet, styles.get("address"), row, 4, 6);
            }
            if (defaultBillAddress.getCity() != null && !defaultBillAddress.getCity().isEmpty()) {
                createHeaderCell(defaultBillAddress.getCity(), sheet, styles.get("address"), ++row, 0, 3);
                createHeaderCell("511, Huideguoji building", sheet, styles.get("address"), row, 4, 6);
            }
            if (defaultBillAddress.getCountryName() != null && !defaultBillAddress.getCountryName().isEmpty()) {
//                EdsCountry country = company.getCountryZone().getCountry();
                createHeaderCell(defaultBillAddress.getCountryName(), sheet, styles.get("address"), ++row, 0, 3);
                createHeaderCell("102  Haoxian Road", sheet, styles.get("address"), row, 4, 6);
            }
            if (company.getPhone() != null && !company.getPhone().isEmpty()) {
                createHeaderCell(company.getPhone(), sheet, styles.get("address"), ++row, 0, 3);
                createHeaderCell("Guangzhou, China 510055", sheet, styles.get("address"), row, 4, 6);
            }
        }
        createHeaderCell("www.hulpfjewelry.com", sheet, styles.get("address"), ++row, 0, 3);
        createHeaderCell("Phone: +86 20 83813665", sheet, styles.get("address"), row, 4, 6);
        row += 4;
        Row titleRow = sheet.createRow(row);
        Cell cell = titleRow.createCell(0);
        if (isPackingSlip) {
            cell.setCellValue("PACKING SLIP");
        } else {
            cell.setCellValue("PURCHASE ORDER");
        }
        cell.setCellStyle(styles.get("title"));
        sheet.addMergedRegion(new CellRangeAddress(row, row, 0, 13));

        row += 3;

        createHeaderCell(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.supplier), sheet, styles.get("titleBold"), row, 0, 3);
        createHeaderCell(data.getClientName(), sheet, styles.get("cell"), ++row, 0, 2);
        EdsAddress billAddress = data.getBillAddressID() != null ? addressManager.get(data.getBillAddressID()) : null;
        EdsAddress mailAddress = data.getMailAddressID() != null ? addressManager.get(data.getMailAddressID()) : null;
        drawBillTo(billAddress, sheet, ++row);
        if (mailAddress != null) {
            drawShipTo(mailAddress, sheet, row);
        } else {
            drawShipToFromCompany(company, sheet, row);
        }
        drawNumberData(data, sheet, row - 2, isPackingSlip);
        row += 8;
        if (!isPackingSlip) {
            drawPoTable(data, sheet, row);
            row += 5;
            row = drawItems(data, sheet, row, isPackingSlip);
            drawTotalTable(data, sheet, row + 4);
        } else {
            drawItems(data, sheet, row, isPackingSlip);
        }


    }


    private void drawPoTable(NewInvoice data, HSSFSheet sheet, int rowIndex) {
        int i = 1;
        Cell cell;
        Row row;
        EdsCurrency edsCurrency = currencyManager.getCurrency(data.getCurrencyID());
        EdsPaymentMethod paymentMethod = data.getPaymentMethodID() != null ? paymentMethodManager.get(data.getPaymentMethodID()) : null;
        String paymentType = paymentMethod != null ? commonLocalizer.localize(paymentMethod.getCode(), paymentMethod.getName()) : "";
        String paymentTerms = data.getPaymentTerms() != null ? data.getPaymentTerms() : "";
        String shippingTerm = data.getShippingTerms() != null ? data.getShippingTerms() : "";
        String poCurrency = edsCurrency != null ? edsCurrency.getFullName() : "";
        ExcelItem[] headerItems = new ExcelItem[]{
                new ExcelItem(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.poCurrency), 2),
                new ExcelItem(pdfWfmMessageSource.localize(PdfLocalizationName.paymentType), 2),
                new ExcelItem(pdfWfmMessageSource.localize(PdfLocalizationName.paymentTerms), 2),
                new ExcelItem(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.shippingTerms), 2),

        };
        ExcelItem[] excelItems = new ExcelItem[]{
                new ExcelItem(poCurrency, 2),
                new ExcelItem(paymentType, 2),
                new ExcelItem(paymentTerms, 2),
                new ExcelItem(shippingTerm, 2),

        };
        row = sheet.createRow(rowIndex);
        row.setHeightInPoints(30);
        for (ExcelItem header : headerItems) {
            cell = row.createCell(i);
            cell.setCellValue(header.getValue());
            cell.setCellStyle(styles.get("header"));
            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, i, i + header.getMergeCount()));
            i += header.getMergeCount() + 1;
        }

        row = sheet.createRow(++rowIndex);
        i = 1;
        for (ExcelItem item : excelItems) {
            cell = row.createCell(i);
            cell.setCellValue(item.getValue());
            cell.setCellStyle(styles.get("cell"));
            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, i, i + item.getMergeCount()));
            i += item.getMergeCount() + 1;
        }

    }

    private void drawTotalTable(NewInvoice data, HSSFSheet sheet, int rowIndex) {
        Row row;
        Cell cell;
        CellStyle styleLabel = styles.get("label");
        CellStyle styleCell = styles.get("address");
        row = sheet.createRow(rowIndex);
        cell = row.createCell(11);
        cell.setCellValue(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.subTotal));
        cell.setCellStyle(styleLabel);
        cell = row.createCell(12);
        cell.setCellValue(data.getSubtotal().setScale(calculationScale).toString());
        cell.setCellStyle(styleCell);
        row = sheet.createRow(++rowIndex);
        cell = row.createCell(11);
        cell.setCellValue(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.total));
        cell.setCellStyle(styleLabel);
        cell = row.createCell(12);
        cell.setCellValue(data.getTotal().setScale(calculationScale).toString());
        cell.setCellStyle(styleCell);
    }

    private Integer drawItems(NewInvoice data, HSSFSheet sheet, int rowIndex, boolean isPackingSlip) {
        Integer j = 1;
        ExcelItem[] tableHeader;
        List<ExcelItem[]> rowItemList = new ArrayList<>();
        ExcelItem[] excelItem;
        Cell cell = null;
        Row row = sheet.createRow(rowIndex);
        row.setHeightInPoints(30);
        //Cell cell;

        if (isPackingSlip) {
            tableHeader = new ExcelItem[]{
                    new ExcelItem(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.number), 0),
                    new ExcelItem(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.name), 2),
                    new ExcelItem(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.description), 2),
                    new ExcelItem(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.qty), 1)
            };

            for (NewInvoiceItem item : data.getItems()) {
                excelItem = new ExcelItem[]{
                        new ExcelItem(j.toString(), 0),
                        new ExcelItem(item.getItemName(), 2),
                        new ExcelItem(item.getDescription(), 2),
                        new ExcelItem(item.getQuantity().toString(), 1)
                };
                rowItemList.add(excelItem);
                j++;
            }
        } else {
            tableHeader = new ExcelItem[]{
                    new ExcelItem(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.number), 0),
                    new ExcelItem(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.name), 2),
                    new ExcelItem(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.description), 2),
                    new ExcelItem(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.qty), 0),
                    new ExcelItem(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.unitPrice), 1),
                    new ExcelItem(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.discount), 1),
                    new ExcelItem(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.totalAmount), 1)
            };
            j = 1;
            for (NewInvoiceItem item : data.getItems()) {
                excelItem = new ExcelItem[]{
                        new ExcelItem(j.toString(), 0),
                        new ExcelItem(item.getItemName(), 2),
                        new ExcelItem(item.getDescription(), 2),
                        new ExcelItem(item.getQuantity().setScale(calculationScale).toString(), 0),
                        new ExcelItem(item.getUnitPrice().setScale(unitPriceScale).toString(), 1),
                        new ExcelItem(item.getDiscountPercent().setScale(calculationScale).toString(), 1),
                        new ExcelItem(item.getTotalAmount().setScale(calculationScale).toString(), 1)

                };
                rowItemList.add(excelItem);
                j++;
            }
        }
        int i = isPackingSlip ? 2 : 0;

        for (ExcelItem header : tableHeader) {
            cell = row.createCell(i);
            cell.setCellValue(header.getValue());
            cell.setCellStyle(styles.get("header"));
            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, i, i + header.getMergeCount()));
            i += header.getMergeCount() + 1;
        }

        for (ExcelItem[] rowItem : rowItemList) {
            row = sheet.createRow(++rowIndex);
            i = isPackingSlip ? 2 : 0;
            for (ExcelItem cellItem : rowItem) {
                cell = row.createCell(i);
                cell.setCellValue(cellItem.getValue());
                cell.setCellStyle(styles.get("cell"));
                sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, i, i + cellItem.getMergeCount()));
                i += cellItem.getMergeCount() + 1;
            }
        }

        return rowIndex;
    }

    private void drawNumberData(NewInvoice data, HSSFSheet sheet, int row, boolean isPackingSlip) {
        String invoiceDate = data.getInvoiceDate() == null ? "" : shortDateFormat.format(data.getInvoiceDate().getNonConvertedDate());
        String invoiceDueDate = data.getDueDate() == null ? "" : shortDateFormat.format(data.getDueDate().getNonConvertedDate());
        String invoiceNumber = data.getInvoiceNumber() == null ? "N/A" : data.getInvoiceNumber();
        String quoteNumberValue = data.getQuoteNumber() != null ? data.getQuoteNumber().trim() : "";
        String poNumberValue = data.getPoNumber() != null ? data.getPoNumber().trim() : "";
        CellStyle styleLabel = styles.get("label");
        CellStyle styleCell = styles.get("address");
        if (isPackingSlip) {
            createNumberCell(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.invoiceNo), sheet, styleLabel, ++row, 9);
            createNumberCell(invoiceNumber, sheet, styleCell, row, 11);
            createNumberCell(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.quoteNumber), sheet, styleLabel, ++row, 9);
            createNumberCell(quoteNumberValue, sheet, styleCell, row, 11);
            createNumberCell(commonLocalizer.localize(PdfLocalizationName.invoiceDate), sheet, styleLabel, ++row, 9);
            createNumberCell(invoiceDate, sheet, styleCell, row, 11);
            createNumberCell(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.dueDate), sheet, styleLabel, ++row, 9);
            createNumberCell(invoiceDueDate, sheet, styleCell, row, 11);
        } else {
            createNumberCell(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.poNumber), sheet, styleLabel, ++row, 9);
            createNumberCell(invoiceNumber, sheet, styleCell, row, 11);
            createNumberCell(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.purchaseOrderDate), sheet, styleLabel, ++row, 9);
            createNumberCell(invoiceDate, sheet, styleCell, row, 11);
            createNumberCell(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.shipDate), sheet, styleLabel, ++row, 9);
            createNumberCell(invoiceDueDate, sheet, styleCell, row, 11);
        }
    }

    private void createNumberCell(String value, HSSFSheet sheet, CellStyle style, int rowIndex, int cellIndex) {
        Row row = sheet.getRow(rowIndex) != null ? sheet.getRow(rowIndex) : sheet.createRow(rowIndex);
        Cell cell = row.createCell(cellIndex);
        cell.setCellValue(value);
        cell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, cellIndex, cellIndex + 1));
    }

    private void drawShipTo(EdsAddress mailAddress, HSSFSheet sheet, int rowIndex) {
        Row row;
        Cell cell;
        CellStyle style = styles.get("address");
        row = sheet.getRow(rowIndex);
        cell = row.createCell(4);
        cell.setCellValue("Ship To");
        cell.setCellStyle(styles.get("titleBold"));
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 5, 7));
        if (mailAddress.getAddress() != null && !mailAddress.getAddress().isEmpty()) {
            createShipCell(mailAddress.getAddress(), sheet, style, ++rowIndex, 7);
        }
        if (mailAddress.getAddressb() != null && !mailAddress.getAddressb().isEmpty()) {
            createShipCell(mailAddress.getAddressb(), sheet, style, ++rowIndex, 7);
        }
        if (mailAddress.getCountry() != null && !mailAddress.getCountry().getName().isEmpty()) {
            createShipCell(mailAddress.getCountry().getName(), sheet, style, ++rowIndex, 7);
        }
        if (mailAddress.getCity() != null && !mailAddress.getCity().isEmpty()) {
            createShipCell(mailAddress.getCity(), sheet, style, ++rowIndex, 7);
        }
    }

    private void drawShipToFromCompany(EdsCompany company, HSSFSheet sheet, int rowIndex) {
        Row row;
        Cell cell;
        CellStyle style = styles.get("address");
        row = sheet.getRow(rowIndex);
        cell = row.createCell(4);
        cell.setCellValue("Ship To");
        cell.setCellStyle(styles.get("titleBold"));
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 5, 7));
        EdsAddress mailingAddress = company.getMailingAddress();
        if (mailingAddress != null) {
            if (mailingAddress.getAddress() != null && !mailingAddress.getAddress().isEmpty()) {
                createShipCell(mailingAddress.getAddress(), sheet, style, ++rowIndex, 7);
            }
            if (mailingAddress.getAddressb() != null && !mailingAddress.getAddressb().isEmpty()) {
                createShipCell(mailingAddress.getAddressb(), sheet, style, ++rowIndex, 7);
            }
            if (mailingAddress.getCountryName() != null && !mailingAddress.getCountryName().isEmpty()) {
                createShipCell(mailingAddress.getCountryName(), sheet, style, ++rowIndex, 7);
            }
            if (mailingAddress.getCity() != null && !mailingAddress.getCity().isEmpty()) {
                createShipCell(mailingAddress.getCity(), sheet, style, ++rowIndex, 7);
            }
        }
    }

    private void createShipCell(String value, HSSFSheet sheet, CellStyle style, int rowIndex, int mergeIndex) {
        Row row = sheet.getRow(rowIndex) != null ? sheet.getRow(rowIndex) : sheet.createRow(rowIndex);
        Cell cell = row.createCell(4);
        cell.setCellValue(value);
        cell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 4, mergeIndex));
    }

    private void createBillCell(String value, HSSFSheet sheet, CellStyle style, int rowIndex, int mergeIndex) {
        Row row = sheet.createRow(rowIndex);
        Cell cell = row.createCell(0);
        cell.setCellValue(value);
        cell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, mergeIndex));
    }

    private void drawBillTo(EdsAddress billAddress, HSSFSheet sheet, int rowIndex) {
        CellStyle style = styles.get("address");
        createHeaderCell("Bill To", sheet, styles.get("titleBold"), rowIndex, 0, 2);
        if (billAddress.getAddress() != null && !billAddress.getAddress().isEmpty()) {
            createBillCell(billAddress.getAddress(), sheet, style, ++rowIndex, 2);
        }
        if (billAddress.getAddressb() != null && !billAddress.getAddressb().isEmpty()) {
            createBillCell(billAddress.getAddressb(), sheet, style, ++rowIndex, 2);
        }
        if (billAddress.getCountry() != null && !billAddress.getCountry().getName().isEmpty()) {
            createBillCell(billAddress.getCountry().getName(), sheet, style, ++rowIndex, 2);
        }
        if (billAddress.getCity() != null && !billAddress.getCity().isEmpty()) {
            createBillCell(billAddress.getCity(), sheet, style, ++rowIndex, 2);
        }
    }


    private void createHeaderCell(String value, HSSFSheet sheet, CellStyle style, int rowIndex, int cellIndex, int mergeIndex) {

        Row row = sheet.getRow(rowIndex) != null ? sheet.getRow(rowIndex) : sheet.createRow(rowIndex);
        Cell cell = row.createCell(cellIndex);
        cell.setCellValue(value);
        cell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, cellIndex, mergeIndex));

    }

    public void setCompanyShortDateFormat(EdsCompany company) {
        if (company.getCompanySettings() != null && company.getCompanySettings().getShortDateFormat() != null) {
            shortDateFormat = new SimpleDateFormat(company.getCompanySettings().getShortDateFormat());
        } else {
            shortDateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        }
    }

    public void setCalculationScale(EdsFinancialSettings fs) {
        if (fs != null && fs.getCalculationScale() != null && fs.getCalculationScale() > 0) {
            calculationScale = fs.getCalculationScale();
        } else {
            calculationScale = 2;
        }
    }

    public void setUnitPriceScale(EdsFinancialSettings fs) {
        if (fs != null && fs.getProductPriceScale() != null) {
            unitPriceScale = fs.getProductPriceScale();
        } else {
            unitPriceScale = 4;
        }
    }

    public class ExcelItem {
        String value;
        Integer mergeCount;

        public ExcelItem(String value, Integer mergeCount) {
            this.value = value;
            this.mergeCount = mergeCount;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Integer getMergeCount() {
            return mergeCount;
        }

        public void setMergeCount(Integer mergeCount) {
            this.mergeCount = mergeCount;
        }
    }
}
