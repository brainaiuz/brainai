package com.edatasite.workforce.gwt.core.server.servlets.pdf.payroll;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.payrolluk.EdsEmployeePayrollSettings;
import com.edatasite.workforce.core.domain.payrolluk.EdsPaymentDeduction;
import com.edatasite.workforce.core.domain.payrolluk.P11;
import com.edatasite.workforce.core.domain.pdf.EdsPdfFonts;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.P11Manager;
import com.edatasite.workforce.gwt.core.server.db.payroll.EmployeePayrollSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PaymentDeductionManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayrollCategoryManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.IPostPDFHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextFontTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextSummaryView;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayslipRequestObject;
import com.edatasite.workforce.utils.EdsContextParams;
import com.lowagie.text.Anchor;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 7/18/11
 * Time: 4:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class PayslipPDFHandler extends AbstractITextPostPdfHandler implements IPostPDFHandler, PDFConstants {

    private EmployeeManager employeeManager;
    private PayrollCategoryManager categoryManager;
    private EmployeePayrollSettingsManager employeePayrollSettingsManager;
    private P11Manager p11Manager;
    private PaymentDeductionManager paymentDeductionManager;

    DecimalFormat numFormat = new DecimalFormat(",##0.00");

    public void setEmployeeManager(EmployeeManager employeeManager) {
        this.employeeManager = employeeManager;
    }

    public void setCategoryManager(PayrollCategoryManager categoryManager) {
        this.categoryManager = categoryManager;
    }

    public void setEmployeePayrollSettingsManager(EmployeePayrollSettingsManager employeePayrollSettingsManager) {
        this.employeePayrollSettingsManager = employeePayrollSettingsManager;
    }

    public void setP11Manager(P11Manager p11Manager) {
        this.p11Manager = p11Manager;
    }

    public void setPaymentDeductionManager(PaymentDeductionManager paymentDeductionManager) {
        this.paymentDeductionManager = paymentDeductionManager;
    }

    protected boolean getPagingOnTop() {
        return true;
    }

    @Override
    protected PdfPTable getPageHeader(Object object, EdsCompany edsCompany, PdfWriter pdfWriter, Document document, String fontName) throws DocumentException {
        //Header table for new design
        PdfPTable header = new PdfPTable(1);
        setShownPaging(true);
        float width = document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin();
        header.setWidthPercentage(100);
        header.getDefaultCell().setBorder(0);
        header.setTotalWidth(width);

        Integer pdfFontID = edsCompany.getCompanySettings().getPdfFontID();
        EdsPdfFonts pdfFonts = pdfFontID != null ? companyPdfFontsManager.getPdfFontByID(pdfFontID) : null;
        String default_font = ITextFontTypeEnum.DEJAVUSANS.getName();

        PdfPTable pageInfo = new PdfPTable(1);
        pageInfo.setWidthPercentage(100);
        pageInfo.setTotalWidth(width);
        PdfPCell pdfPCell = new PdfPCell(new Phrase(""));
        pdfPCell.setPaddingTop(65f);
        pdfPCell.setBorder(Rectangle.NO_BORDER);
        pageInfo.addCell(pdfPCell);
        header.addCell(pageInfo);

        //Company info contains company address and logo
        PdfPTable companyInfo = new PdfPTable(2);
        companyInfo.getDefaultCell().setBorder(0);
        companyInfo.setWidthPercentage(50);
        companyInfo.setTotalWidth(width);

        String companyName = escapeHtml(edsCompany.getName());
        String address = edsCompany.getAddress1() != null ? escapeHtml(edsCompany.getAddress1()) : "";
        String city = edsCompany.getCity() != null ? escapeHtml(edsCompany.getCity()) : "";
        String postCode = (edsCompany.getPostCode() != null && !"".equals(edsCompany.getPostCode())) ? escapeHtml(edsCompany.getPostCode()) : "";
        EdsCountry edsCountry = (edsCompany.getCountryZone() != null && edsCompany.getCountryZone().getCountry() != null) ? edsCompany.getCountryZone().getCountry() : null;
        String country = "";
        if (edsCountry != null) {
            country = countryLocalizer.localize(edsCountry.getCode(), edsCountry.getName());
        }
        String cityPostCode = (!"".equals(city) && !"".equals(postCode) ? (city + ", " + postCode) : (!"".equals(city) ? city : postCode));

        PdfPTable leftHeader = new PdfPTable(1);
        leftHeader.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        leftHeader.getDefaultCell().setBorder(0);
        leftHeader.setTotalWidth((document.getPageSize().getWidth() / 2) - document.leftMargin() - 10);

        Color textColor = null;
        String color = edsCompany.getCompanySettings().getPdfStyleColor();
        if(color != null && !"".equals(color) && color.length() == 6) {
            textColor = Utils.hexToRGB(color);
        } else {
            textColor = Utils.hexToRGB(DEFAULT_FONT_COLOR);
        }

        leftHeader.addCell(new Phrase(companyName, FontFactory.getFont(default_font, BaseFont.IDENTITY_H, 14, Font.BOLD, textColor)));
        if (edsCompany.getAddress2() != null && !"".equals(edsCompany.getAddress2()) && !address.equals(edsCompany.getAddress2())) {
            leftHeader.addCell(new Phrase(address, FontFactory.getFont(default_font, BaseFont.IDENTITY_H, 8)));
            leftHeader.addCell(new Phrase(city, FontFactory.getFont(default_font, BaseFont.IDENTITY_H, 8)));
        } else {
            leftHeader.addCell(new Phrase(address, FontFactory.getFont(default_font, BaseFont.IDENTITY_H, 8)));
            leftHeader.addCell(new Phrase(cityPostCode, FontFactory.getFont(default_font, BaseFont.IDENTITY_H, 8)));
        }
        leftHeader.addCell(new Phrase(escapeHtml(country), FontFactory.getFont(default_font, BaseFont.IDENTITY_H, 8)));
        companyInfo.addCell(leftHeader);

        //logo
        PdfPTable rightTable = new PdfPTable(1);
        rightTable.getDefaultCell().setBorder(0);
        rightTable.setTotalWidth(width / 2);
        rightTable.setHorizontalAlignment(PdfPTable.ALIGN_RIGHT);
        rightTable.getDefaultCell().setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        rightTable.getDefaultCell().setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        String imageUrl = null;
        try {
            imageUrl = getPdfLogoUrl(edsCompany, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (imageUrl != null) {
            try {
                Image image = Image.getInstance(imageUrl);
                EdsCompanySettings cs = edsCompany.getCompanySettings();

                if (cs != null && cs.getPdfLogoHeight() != null && cs.getPdfLogoWidth() != null) {
                    image.scaleAbsolute(cs.getPdfLogoWidth(), cs.getPdfLogoHeight());
                } else {
                    if (image.getWidth() > 240 && image.getHeight() > 60) {
                        float widthScale = image.getWidth() / 240;
                        float heightScale = image.getHeight() / 60;
                        if (widthScale > heightScale) {
                            image.scaleAbsoluteWidth(240);
                            image.scaleAbsoluteHeight(image.getHeight() / widthScale);
                        } else {
                            image.scaleAbsoluteHeight(60);
                            image.scaleAbsoluteWidth(image.getWidth() / heightScale);
                        }
                    } else if (image.getWidth() > 240) {
                        image.scaleAbsoluteWidth(240);
                        image.scaleAbsoluteHeight(image.getHeight() * 240 / image.getWidth());
                    } else if (image.getHeight() > 60) {
                        image.scaleAbsoluteHeight(60);
                        image.scaleAbsoluteWidth(image.getWidth() * 60 / image.getHeight());
                    } else {
                        image.scaleAbsolute((int) (image.getWidth() * 0.8), (int) (image.getHeight() * 0.8));
                    }
                }
                Chunk a = new Chunk(image, 0, 0);
                rightTable.addCell(new Phrase(a));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        companyInfo.addCell(rightTable);
        header.addCell(companyInfo);
        return header;
    }

    @Override
    protected PdfPTable getPageFooter(Object object, EdsCompany edsCompany, PdfWriter pdfWriter, Document document, String fontName) throws DocumentException {
        PdfPTable footer;
        if (edsCompany.getShowCertificatePdfFooter()) {
            footer = new PdfPTable(3);
            footer.setWidths(new float[]{commonLocalizer.initializeUserLocale().getLanguage().contains("ru") ? 1.0f : 0.35f, 0.35f, 0.30f});
        } else {
            footer = new PdfPTable(2);
            footer.setWidths(new float[]{commonLocalizer.initializeUserLocale().getLanguage().contains("ru") ? 1.0f : 0.35f, 0.65f});
        }
        footer.getDefaultCell().setNoWrap(true);
        footer.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        footer.getDefaultCell().setPadding(3);
        footer.getDefaultCell().setBorder(0);
        footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        footer.setTotalWidth(235);


        if (isShownWFTFooter) {
            String url = "http://www." + EdsContextParams.getHelpHost();
            Phrase poweredBy = new Phrase(commonLocalizer.localize(PdfLocalizationName.poweredBy), FontFactory.getFont(ITextFontTypeEnum.DEJAVUSANS.getName(), BaseFont.IDENTITY_H, 8, Font.BOLD));
            Anchor anchor = new Anchor(url, FontFactory.getFont(ITextFontTypeEnum.DEJAVUSANS.getName(), BaseFont.IDENTITY_H, 8));
            anchor.setReference(url);
            footer.addCell(poweredBy);
            footer.addCell(anchor);
        }
        return footer;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        pdfData.setPdfViewType(ITextPdfViewTypeEnum.SUMMARYVIEW);
        ITextSummaryView summaryView = new ITextSummaryView();
        summaryView.setFontName(ITextFontTypeEnum.DEJAVUSANS_BOLD.getName());
        pdfData.setSummaryView(summaryView);

        ITextTableList space = new ITextTableList(1);
        ITextTableList space1 = new ITextTableList(1);
        ITextTableList space3 = new ITextTableList(1);
        space.setBorderWidth(0);
        summaryView.addTable(space);
        summaryView.addTable(space1);
        summaryView.addTable(space3);

        DateFormat dateFormat = SimpleDateFormat.getDateInstance();
        PayslipRequestObject payslip = (PayslipRequestObject) dataClass;
        EdsEmployee employee = employeeManager.get(payslip.getEmployeeId());

        //Header
        ITextTableList title = new ITextTableList(1);
        CellData cellData = new CellData("PAYSLIP", Element.ALIGN_LEFT);
        cellData.setPadding(3, 50, 0, 20);
        cellData.setFont(FontFactory.getFont(ITextFontTypeEnum.DEJAVUSANS.getName(), BaseFont.IDENTITY_H, 11, Font.BOLD));
        title.addPdfTableRows(cellData);
        title.setBorderWidth(0);

        //Employee Data Table
        ITextTableList employeeTable = new ITextTableList(3);
        employeeTable.setBorderWidth(0);
        employeeTable.setTableAlignment(Element.ALIGN_RIGHT);
        employeeTable.setCellPadding(1.5f);
        employeeTable.setTotalWidth(300);
        employeeTable.addTableWidthPercentage(10f, 10f, 10f);

        ITextTableList[] headerTable = new ITextTableList[]{title, employeeTable};
        float[] percent = new float[]{50f, 300f};
        summaryView.addTable(headerTable, percent);

        ITextTableList paymentTable = new ITextTableList(2);
        paymentTable.setTotalWidth(240);
        paymentTable.setTableAlignment(Element.ALIGN_LEFT);
        paymentTable.addTableWidthPercentage(6f, 4f);

        ITextTableList deductionTable = new ITextTableList(2);
        deductionTable.setTotalWidth(240);
        deductionTable.setTableAlignment(Element.ALIGN_RIGHT);
        deductionTable.addTableWidthPercentage(6f, 4f);

        ITextTableList ytdTable = new ITextTableList(2);
        ytdTable.setTotalWidth(240);
        ytdTable.setTableAlignment(Element.ALIGN_RIGHT);
        ytdTable.addTableWidthPercentage(6f, 4f);

        ITextTableList totalTable = new ITextTableList(2);
        totalTable.setTotalWidth(240);
        totalTable.setTableAlignment(Element.ALIGN_LEFT);
        totalTable.addTableWidthPercentage(6f, 4f);

        ITextTableList[] payDeductionTables = new ITextTableList[]{paymentTable, deductionTable};
        ITextTableList[] payTotal = new ITextTableList[]{totalTable, ytdTable};
        float[] widthPercentage = new float[]{0.5f, 0.5f};
        summaryView.addTable(payDeductionTables, widthPercentage);
        summaryView.addTable(payTotal, widthPercentage);

        ITextTableList advanceTable = new ITextTableList(5);
        summaryView.addTable(advanceTable);

        ITextTableList additionDataTable = new ITextTableList(1);
        summaryView.addTable(additionDataTable);
        //Tax, NI Number ... Table

        EdsUser edsUser = userManager.getUser();

        String curSymbol = getCompanyCurrencySymbol() + " ";

        //For Payslip List
        P11 p11 = null;
        Date pDate = null;
        final Calendar c = Calendar.getInstance();
        StringBuilder ref = new StringBuilder();
        if (PayslipRequestObject.FROM_GENERATE_PAYSLIP.equals(payslip.getFromView())) {
            pDate = edsUser.getUserDate(new Date(payslip.getDate()));
        } else if (PayslipRequestObject.FROM_PAYSLIP_LIST.equals(payslip.getFromView())) {
            if (p11Manager.get(payslip.getObjectID()).getParent() != null) {
                p11 = p11Manager.get(payslip.getObjectID()).getParent();
            } else {
                p11 = p11Manager.get(payslip.getObjectID());
            }
            pDate = new Date(p11.getDate().getTime());
        } else {
            pDate = new Date();
        }
        c.setTime(pDate);
        ref.append("PS");
        if (c.getTime() != null) {
            ref.append(c.get(Calendar.YEAR)).append(pDate.getMonth() < 10 ? "0" + pDate.getMonth() : pDate.getMonth());
        }
        if (employee != null) {
            ref.append(employee.getProfile().getEmployeeCode() != null ? employee.getProfile().getEmployeeCode() : "");
        }
        String reference = ref.toString();
        paymentTable.addPdfTableHeader(createHeader("Payments"), createHeader("Amount", null, Element.ALIGN_RIGHT));
        deductionTable.addPdfTableHeader(createHeader("Deductions"), createHeader("Amount", null, Element.ALIGN_RIGHT));
        ytdTable.addPdfTableHeader(createHeader("Totals Year To Date", 2, Element.ALIGN_LEFT));
        totalTable.addPdfTableHeader(createHeader("Totals This Period", 2, Element.ALIGN_LEFT));
        advanceTable.addPdfTableHeader(createHeader("Advance Payments"), createHeader("From Date"), createHeader("To Date"), createHeader("No. Periods"), createHeader("Amount"));

        if (PayslipRequestObject.FROM_GENERATE_PAYSLIP.equals(payslip.getFromView())) {
            ITextTableList bottomTable = new ITextTableList(4);
            bottomTable.setTotalWidth(532);
            if ("true".equals(payslip.getFromCompanyUK())) {
                summaryView.addTable(bottomTable);
            }
            bottomTable.addPdfTableHeader(createHeader("Tax Code"), createHeader("Dept"), createHeader("N.I.Number"), createHeader("Payment Method"));

            employeeTable.addPdfTableRows(createHeader("Employee Name", null, Rectangle.NO_BORDER), createHeader("Ref.", null, Rectangle.NO_BORDER), createHeader("Period", null, Rectangle.NO_BORDER));
            employeeTable.addPdfTableRows(createCell(employee.getName()), createCell(reference), createCell(getWeekMonth(payslip.getWeekOrMonthNo())));
            employeeTable.addPdfTableRows(createCell(""), createCell(""), createCell(""));
            employeeTable.addPdfTableRows(createCell(""), createCell(""), createCell(""));
            employeeTable.addPdfTableRows(createCell(""), createCell(""), createCell(""));

            employeeTable.addPdfTableRows(createHeader("Process Date", null, Rectangle.NO_BORDER), createHeader("Pay Date", null, Rectangle.NO_BORDER), createHeader("Payment Method", null, Rectangle.NO_BORDER));
            employeeTable.addPdfTableRows(createCell(dateFormat.format(edsUser.getUserDate(new Date()))), createCell(dateFormat.format(edsUser.getUserDate(new Date(payslip.getDate())))), createCell(payslip.getPaymentMethod()));

            employeeTable.setAfterSpacing(15f);

            String[] payments = payslip.getPayments().split(PayslipRequestObject.ROW_SPLITTER);
            for (String payment : payments) {
                String[] items = payment.split(PayslipRequestObject.CELL_SPLITTER);
                paymentTable.addPdfTableRows(createCell(categoryManager.get(Integer.parseInt(items[0])).getName()),
                        createRACell((items.length > 1 && items[1] != null) ? curSymbol + items[1] : formatCurrency(BigDecimal.ZERO, curSymbol)));
            }
            String[] deductions = payslip.getDeductions().length() > 0
                    ? payslip.getDeductions().split(PayslipRequestObject.ROW_SPLITTER)
                    : new String[]{PayslipRequestObject.CELL_SPLITTER + " "};
            for (String deduction : deductions) {
                String[] items = deduction.split(PayslipRequestObject.CELL_SPLITTER);
                deductionTable.addPdfTableRows(createCell(items[0] != null && !"".equals(items[0].trim()) ? categoryManager.get(Integer.parseInt(items[0])).getName() : ""),
                        createRACell((items.length > 1 && items[1] != null) ? curSymbol + "(" + items[1] + ")" : curSymbol + "(0.00)"));
            }
            if (deductionTable.getTableRows() == null || deductionTable.getTableRows().size() == 0) {
                deductionTable.addPdfTableRows(createCell(" "), createRACell(" "));
            }
            totalTable.addPdfTableRows(createCell("Total Bonus"), createRACell(curSymbol + payslip.getTotalBonus()));
            totalTable.addPdfTableRows(createCell("Total Gross Pay"), createRACell(curSymbol + payslip.getTotalGrossPay()));
            totalTable.addPdfTableRows(createCell("Deductions"), createRACell(curSymbol + "(" + payslip.getTotalDeductions() + ")"));
            if ("true".equals(payslip.getFromCompanyUK())) {
                totalTable.addPdfTableRows(createCell("Total Taxable Pay"), createRACell(payslip.getTotalTaxablePay() != null ? curSymbol + payslip.getTotalTaxablePay() : payslip.getTotalGrossPay() != null ? curSymbol + payslip.getTotalGrossPay() : "0.00"));
            }
            totalTable.addPdfTableRows(createCell("Total"), createRACell(curSymbol + payslip.getNetPay()));

            ytdTable.addPdfTableRows(createCell("Total Pay to date"), createRACell(!payslip.getTotalPayToDate().equals("0.00") ? curSymbol + payslip.getTotalPayToDate() : payslip.getTotalGrossPay() != null ? curSymbol + payslip.getTotalGrossPay() : "0.00"));


            if (payslip.getAdvancePayments() != null && !"".equals(payslip.getAdvancePayments())) {
                String[] rows = payslip.getAdvancePayments().split(PayslipRequestObject.ROW_SPLITTER);
                for (String row : rows) {
                    String[] cells = row.split(PayslipRequestObject.CELL_SPLITTER);
                    advanceTable.addPdfTableRows(createCell(cells[0]), createCell(dateFormat.format(new Date(Long.parseLong(cells[1])))),
                            createCell(dateFormat.format(new Date(Long.parseLong(cells[2])))), createCell(cells[3]), createRACell(cells[4]));
                }
            }

            String department = "";
            if (employee.getEmployeeTeam() != null && employee.getEmployeeTeam().getTeam() != null) {
                department = employee.getEmployeeTeam().getTeam().getName();
            }
            bottomTable.addPdfTableRows(createCell(payslip.getTaxCode() != null ? payslip.getTaxCode() : ""), createCell(department),
                    createCell(payslip.getNiNumber()), createCell(payslip.getPaymentMethod() != null ? payslip.getPaymentMethod() : ""));
            String str;
            Date date = edsUser.getUserDate(new Date(payslip.getDate()));
            Integer endDay = date.getDate();
            Integer startDay = DateUtil.getMonthFirstDay(date).getDate();
            SimpleDateFormat shortDateFormat = new SimpleDateFormat("MMMM");
            String month = shortDateFormat.format(date);

            additionDataTable.addPdfTableRows(createCellWithStyle("Payment is reported for the period of " + startDay + " to " + endDay + " " + month, com.lowagie.text.Font.ITALIC));
        } else if (PayslipRequestObject.FROM_PAYSLIP_LIST.equals(payslip.getFromView())) {
            ITextTableList bottomTable = new ITextTableList(3);
            bottomTable.setTotalWidth(532);
            if ("true".equals(payslip.getFromCompanyUK())) {
                summaryView.addTable(bottomTable);
            }

            String processDate = p11.getProcessDate() != null ? dateFormat.format(new Date(p11.getProcessDate().getTime())) : "";
            String payDate = dateFormat.format(new Date(p11.getDate().getTime()));
            String periodType = employeePayrollSettingsManager.getEmployeeSettingValue(payslip.getEmployeeId(), PAY_FREQUENCY).getValue();
            String period = ("Weekly".equals(periodType) ? "Week " : "Month ") + p11.getPayPeriod();
            String paymentMethod = p11.getPaymentmethod() != null ? p11.getPaymentmethod() : "";
//            employeeTable.addPdfTableRows(createCell(reference), createCell(employee.getName()), createCell(processDate), createCell(payDate), createCell(period), createCell(paymentMethod));

            employeeTable.addPdfTableRows(createHeader("Employee Name", null, Rectangle.NO_BORDER), createHeader("Ref.", null, Rectangle.NO_BORDER), createHeader("Period", null, Rectangle.NO_BORDER));
            employeeTable.addPdfTableRows(createCell(employee.getName()), createCell(reference), createCell(period));
            employeeTable.addPdfTableRows(createCell(""), createCell(""), createCell(""));
            employeeTable.addPdfTableRows(createCell(""), createCell(""), createCell(""));
            employeeTable.addPdfTableRows(createCell(""), createCell(""), createCell(""));

            employeeTable.addPdfTableRows(createHeader("Process Date", null, Rectangle.NO_BORDER), createHeader("Pay Date", null, Rectangle.NO_BORDER), createHeader("Payment Method", null, Rectangle.NO_BORDER));
            employeeTable.addPdfTableRows(createCell(processDate), createCell(payDate), createCell(paymentMethod));
            employeeTable.setAfterSpacing(15f);

            List<EdsPaymentDeduction> paymentDeductions = paymentDeductionManager.getPayslipPaymentDeductions(p11.getObjectID(), null);
            for (EdsPaymentDeduction pd : paymentDeductions) {
                if ("Payment".equals(pd.getCategory().getType())) {
                    paymentTable.addPdfTableRows(createCell(pd.getCategory().getName()), createRACell(formatCurrency(pd.getPaymentAmount(), curSymbol)));
                } else if ("Deduction".equals(pd.getCategory().getType())) {
                    deductionTable.addPdfTableRows(createCell(pd.getCategory().getName()), createRACell(curSymbol + "(" + numFormat.format(pd.getPaymentAmount() != null ? pd.getPaymentAmount() : BigDecimal.ZERO) + ")"));
                }
            }
            if (deductionTable.getTableRows() == null || deductionTable.getTableRows().size() == 0) {
                deductionTable.addPdfTableRows(createCell(" "), createRACell(" "));
            }

            List<EdsPaymentDeduction> advancePayments = paymentDeductionManager.getPayslipAdvancePayments(p11.getObjectID());
            if (advancePayments != null && advancePayments.size() > 0) {
                for (EdsPaymentDeduction ap : advancePayments) {
                    advanceTable.addPdfTableRows(createCell(ap.getCategory().getName()),
                            createCell(ap.getStartDate() != null ? dateFormat.format(ap.getStartDate()) : ""),
                            createCell(ap.getEndDate() != null ? dateFormat.format(ap.getEndDate()) : ""),
                            createCell(ap.getNumberOfPeriods() != null ? ap.getNumberOfPeriods().toString() : ""),
                            createRACell(formatCurrency(ap.getPaymentAmount() != null ? ap.getPaymentAmount() : BigDecimal.ZERO, curSymbol)));
                }
            }

            final BigDecimal payAdjustment = p11.getTotalFreePay() != null ? p11.getTotalFreePay() : p11.getTotalAdditionalPay() != null ? p11.getTotalAdditionalPay() : BigDecimal.ZERO;
            totalTable.addPdfTableRows(createCell("Total Bonus"), createRACell(p11.getTotalBonus() != null ? formatCurrency(p11.getTotalBonus(), curSymbol) : curSymbol + "0.00"));
            totalTable.addPdfTableRows(createCell("Total Gross Pay"), createRACell(p11.getGrossPayInPeriod() != null ? formatCurrency(p11.getGrossPayInPeriod(), curSymbol) : curSymbol + "0.00"));
            totalTable.addPdfTableRows(createCell("Deductions"), createRACell(curSymbol + "(" + numFormat.format(payslip.getTotalDeductions() != null ? new BigDecimal(Double.valueOf(payslip.getTotalDeductions())) : BigDecimal.ZERO) + ")"));
            if ("true".equals(payslip.getFromCompanyUK())) {
                totalTable.addPdfTableRows(createCell("Total Taxable Pay"), createRACell(formatCurrency((p11.getTotalTaxablePayToDate() != null ? p11.getTotalTaxablePayToDate() : p11.getGrossPayInPeriod()), curSymbol)));
            }
            totalTable.addPdfTableRows(createCell("Total"), createRACell(p11.getNetPay() != null ? formatCurrency(p11.getNetPay(), curSymbol) : curSymbol + "0.00"));

            ytdTable.addPdfTableRows(createCell("Total Pay to date"), createRACell(formatCurrency((p11.getTotalPayToDate() != null ? p11.getTotalPayToDate() : p11.getGrossPayInPeriod()), curSymbol)));

            List<EdsEmployeePayrollSettings> empSettings = employeePayrollSettingsManager.getEmployeeSettings(payslip.getEmployeeId());
            String niNumber = "", taxCode = "", department = "";
            if (empSettings != null && empSettings.size() > 0) {
                for (EdsEmployeePayrollSettings s : empSettings) {
                    if (NI_NUMBER.equals(s.getKey())) {
                        niNumber = s.getValue();
                    } else if (TAX_CODE.equals(s.getKey())) {
                        taxCode = s.getValue();
                    }
                }
            }
            if (p11.getEmployee().getEmployeeTeam() != null && p11.getEmployee().getEmployeeTeam().getTeam() != null) {
                department = p11.getEmployee().getEmployeeTeam().getTeam().getName();
            }
            bottomTable.addPdfTableHeader(createHeader("Tax Code"), createHeader("Dept"), createHeader("N.I.Number"));
            bottomTable.addPdfTableRows(createCell(taxCode), createCell(department), createCell(niNumber));
            String str;
            Date date = edsUser.getUserDate(p11.getDate());
            Integer endDay = date.getDate();
            Integer startDay = DateUtil.getMonthFirstDay(date).getDate();
            SimpleDateFormat shortDateFormat = new SimpleDateFormat("MMMM");
            String month = shortDateFormat.format(date);

            additionDataTable.addPdfTableRows(createCellWithStyle("Payment is reported for the period of " + startDay + " to " + endDay + " " + month, com.lowagie.text.Font.ITALIC));
        }
        return pdfData;
    }

    public static final String PAYMENT_TABLE = "PAYMENT_TABLE";
    public static final String DEDUCTION_TABLE = "DEDUCTION_TABLE";
    public static final String YTD_TABLE = "YTD_TABLE";
    public static final String ADVANCE_TABLE = "ADVANCE_TABLE";
    public static final String BOTTOM_TABLE = "BOTTOM_TABLE";

    public static final String PROCESS_DATE = "PROCESS_DATE";
    public static final String PAYSLIP_DATE = "PAYSLIP_DATE";
    public static final String CATEGORY_NAME = "CATEGORY_NAME";
    public static final String START_DATE = "START_DATE";
    public static final String END_DATE = "END_DATE";
    public static final String PERIOD_NUMBER = "PERIOD_NUMBER";
    public static final String PAYMENT_AMOUNT = "PAYMENT_AMOUNT";
    public static final String TAX_CODE = "TAX_CODE";
    public static final String DEPARTMENT = "DEPARTMENT";
    public static final String NI_NUMBER = "NI_NUMBER";
    public static final String PAYMENT_METHOD = "PAYMENT_METHOD";

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {

        ITextGenericPdfData pdfData = new ITextGenericPdfData();

        DateFormat dateFormat = SimpleDateFormat.getDateInstance();
        PayslipRequestObject payslip = (PayslipRequestObject) dataClass;
        EdsEmployee employee = employeeManager.get(payslip.getEmployeeId());
        String curSymbol = getCompanyCurrencySymbol();

        CustomisedITextTable employeeTable = new CustomisedITextTable();
        CustomisedITextTable paymentTable = new CustomisedITextTable();
        CustomisedITextTable deductionTable = new CustomisedITextTable();
        CustomisedITextTable totalTable = new CustomisedITextTable();
        CustomisedITextTable ytdTable = new CustomisedITextTable();
        CustomisedITextTable advanceTable = new CustomisedITextTable();
        CustomisedITextTable bottomTable = new CustomisedITextTable();

        employeeTable.addColumnOrder(REFERENCE, EMPLOYEE_NAME, PROCESS_DATE, PAYSLIP_DATE, PERIOD);
        paymentTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        deductionTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        totalTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        ytdTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        advanceTable.addColumnOrder(CATEGORY_NAME, START_DATE, END_DATE, PERIOD_NUMBER, PAYMENT_AMOUNT);
        bottomTable.addColumnOrder(TAX_CODE, DEPARTMENT, NI_NUMBER, PAYMENT_METHOD);

        String reference = "PS - " + String.valueOf(payslip.getObjectID() != null ? payslip.getObjectID() : 0);
        if (PayslipRequestObject.FROM_GENERATE_PAYSLIP.equals(payslip.getFromView())) {
            employeeTable.addRow(reference, employee.getName(), dateFormat.format(employee.getCompany().getCompanyDate()),
                    dateFormat.format(new Date(payslip.getDate())), getWeekMonth(payslip.getWeekOrMonthNo()));

            String[] payments = payslip.getPayments().split(PayslipRequestObject.ROW_SPLITTER);
            for (String payment : payments) {
                String[] items = payment.split(PayslipRequestObject.CELL_SPLITTER);
                paymentTable.addRow(categoryManager.get(Integer.parseInt(items[0])).getName(),
                        (items.length > 1 && items[1] != null) ? items[1] : "");
            }
            String[] deductions = payslip.getDeductions().length() > 0
                    ? payslip.getDeductions().split(PayslipRequestObject.ROW_SPLITTER)
                    : new String[]{PayslipRequestObject.CELL_SPLITTER + " "};
            for (String deduction : deductions) {
                String[] items = deduction.split(PayslipRequestObject.CELL_SPLITTER);
                deductionTable.addRow(items[0] != null && !"".equals(items[0].trim()) ? categoryManager.get(Integer.parseInt(items[0])).getName() : "",
                        (items.length > 1 && items[1] != null) ? items[1] : "");
            }

            totalTable.addRow("Total Gross Pay", payslip.getTotalGrossPay());
            totalTable.addRow("Deductions", payslip.getTotalDeductions());
            totalTable.addRow("Pay Adjustment", payslip.getPayAdjustment());
            totalTable.addRow("Total Taxable Pay", payslip.getTotalTaxablePay() != null ? payslip.getTotalTaxablePay() : payslip.getTotalGrossPay());
            totalTable.addRow("NET PAY", payslip.getNetPay());

            ytdTable.addRow("Total Pay to date", payslip.getTotalPayToDate() != null ? payslip.getTotalPayToDate() : payslip.getTotalGrossPay());


            if (payslip.getAdvancePayments() != null && !"".equals(payslip.getAdvancePayments())) {
                String[] rows = payslip.getAdvancePayments().split(PayslipRequestObject.ROW_SPLITTER);
                for (String row : rows) {
                    String[] cells = row.split(PayslipRequestObject.CELL_SPLITTER);
                    advanceTable.addRow(cells[0], dateFormat.format(new Date(Long.parseLong(cells[1]))),
                            dateFormat.format(new Date(Long.parseLong(cells[2]))), cells[3], cells[4]);
                }
            }

            String department = "";
            if (employee.getEmployeeTeam() != null && employee.getEmployeeTeam().getTeam() != null) {
                department = employee.getEmployeeTeam().getTeam().getName();
            }
            bottomTable.addRow((payslip.getTaxCode() != null ? payslip.getTaxCode() : ""), department, payslip.getNiNumber(),
                    (payslip.getPaymentMethod() != null ? payslip.getPaymentMethod() : ""));
        } else if (PayslipRequestObject.FROM_PAYSLIP_LIST.equals(payslip.getFromView())) {
            //For Payslip List
            P11 p11;
            if (p11Manager.get(payslip.getObjectID()).getParent() != null) {
                p11 = p11Manager.get(payslip.getObjectID()).getParent();
            } else {
                p11 = p11Manager.get(payslip.getObjectID());
            }

            String processDate = p11.getProcessDate() != null ? dateFormat.format(new Date(p11.getProcessDate().getTime())) : "";
            String payDate = dateFormat.format(new Date(p11.getDate().getTime()));
            String periodType = employeePayrollSettingsManager.getEmployeeSettingValue(payslip.getEmployeeId(), PAY_FREQUENCY).getValue();
            String period = ("Weekly".equals(periodType) ? "Week " : "Month ") + p11.getPayPeriod();
            employeeTable.addRow(reference, employee.getName(), processDate, payDate, period);

            List<EdsPaymentDeduction> paymentDeductions = paymentDeductionManager.getPayslipPaymentDeductions(p11.getObjectID(), null);
            for (EdsPaymentDeduction pd : paymentDeductions) {
                if ("Payment".equals(pd.getCategory().getType())) {
                    paymentTable.addRow(pd.getCategory().getName(), formatCurrency(pd.getPaymentAmount(), curSymbol));
                } else if ("Deduction".equals(pd.getCategory().getType())) {
                    deductionTable.addRow(pd.getCategory().getName(), formatCurrency(pd.getPaymentAmount(), curSymbol));
                }
            }

            List<EdsPaymentDeduction> advancePayments = paymentDeductionManager.getPayslipAdvancePayments(p11.getObjectID());
            if (advancePayments != null && advancePayments.size() > 0) {
                for (EdsPaymentDeduction ap : advancePayments) {
                    advanceTable.addRow(ap.getCategory().getName(),
                            (ap.getStartDate() != null ? dateFormat.format(ap.getStartDate()) : ""),
                            (ap.getEndDate() != null ? dateFormat.format(ap.getEndDate()) : ""),
                            (ap.getNumberOfPeriods() != null ? ap.getNumberOfPeriods().toString() : ""),
                            formatCurrency(ap.getPaymentAmount() != null ? ap.getPaymentAmount() : BigDecimal.ZERO, curSymbol));
                }
            }

            final BigDecimal payAdjustment = p11.getTotalFreePay() != null ? p11.getTotalFreePay() : p11.getTotalAdditionalPay() != null ? p11.getTotalAdditionalPay() : BigDecimal.ZERO;
            totalTable.addRow("Total Gross Pay", p11.getGrossPayInPeriod() != null ? formatCurrency(p11.getGrossPayInPeriod(), curSymbol) : "");
            totalTable.addRow("Deductions", formatCurrency((payslip.getTotalDeductions() != null ? new BigDecimal(Double.valueOf(payslip.getTotalDeductions())) : BigDecimal.ZERO), curSymbol));
            totalTable.addRow("Pay Adjustment", formatCurrency(payAdjustment, curSymbol));
            totalTable.addRow("Total Taxable Pay", formatCurrency((p11.getTotalTaxablePayToDate() != null ? p11.getTotalTaxablePayToDate() : p11.getGrossPayInPeriod()), curSymbol));
            totalTable.addRow("NET PAY", p11.getNetPay() != null ? formatCurrency(p11.getNetPay(), curSymbol) : "");

            ytdTable.addRow("Total Pay to date", formatCurrency((p11.getTotalPayToDate() != null ? p11.getTotalPayToDate() : p11.getGrossPayInPeriod()), curSymbol));

            List<EdsEmployeePayrollSettings> empSettings = employeePayrollSettingsManager.getEmployeeSettings(payslip.getEmployeeId());
            String niNumber = "", taxCode = "", paymentMethod = "", department = "";
            if (empSettings != null && empSettings.size() > 0) {
                for (EdsEmployeePayrollSettings s : empSettings) {
                    if (NI_NUMBER.equals(s.getKey())) {
                        niNumber = s.getValue();
                    } else if (TAX_CODE.equals(s.getKey())) {
                        taxCode = s.getValue();
                    } else if (PAY_METHOD.equals(s.getKey())) {
                        paymentMethod = s.getValue();
                    }
                }
            }
            if (p11.getEmployee().getEmployeeTeam() != null && p11.getEmployee().getEmployeeTeam().getTeam() != null) {
                department = p11.getEmployee().getEmployeeTeam().getTeam().getName();
            }
            bottomTable.addRow(taxCode, department, niNumber, paymentMethod);
        }

        // Company Data
        pdfData.setCompanyData(getCompanyData(employee.getCompany(), true, hasPhantom));
        //Table Data
        HashMap<String, CustomisedITextTable> customData = new HashMap<>();
        customData.put(EMPLOYEE_TABLE, employeeTable);
        customData.put(PAYMENT_TABLE, paymentTable);
        customData.put(DEDUCTION_TABLE, deductionTable);
        customData.put(TOTAL_TABLE, totalTable);
        customData.put(YTD_TABLE, ytdTable);
        customData.put(ADVANCE_TABLE, advanceTable);
        if ("true".equals(payslip.getFromCompanyUK())) {
            customData.put(BOTTOM_TABLE, bottomTable);
        }
        pdfData.setCustomData(customData);
        return pdfData;
    }

    private String formatCurrency(BigDecimal amount, String currencySymbol) {
        return (currencySymbol != null ? currencySymbol + " " : "") + numFormat.format(amount);
    }

    private String getFileName(String weekMonth, String employeeName, Integer year) {
        String fileName = year != null ? (String.valueOf(year) + "_") : "";
        fileName += escapeHtml(weekMonth) + "_";
        fileName += escapeHtml(employeeName);
        return fileName;
    }

    private String getCompanyCurrencySymbol() {
        String symbol = "$";
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        EdsCurrency currency = null;
        if (financialSettings != null) {
            currency = financialSettings.getCurrency();
        }
        if (currency != null) {
            symbol = currency.getSymbol() != null ? currency.getSymbol() : currency.getName();
        }
        return symbol;
    }

    private String getWeekMonth(String weekMonthNumber) {
        if (weekMonthNumber != null && (weekMonthNumber.toLowerCase().startsWith("week") || weekMonthNumber.toLowerCase().startsWith("month"))) {
            return weekMonthNumber;
        }
        String wm = "";
        if (weekMonthNumber != null) {
            if (weekMonthNumber.startsWith("W")) {
                wm = "Week " + weekMonthNumber.substring(1);
            } else if (weekMonthNumber.startsWith("M")) {
                wm = "Month " + weekMonthNumber.substring(1);
            }
        }
        return wm;
    }

    private static final Color HEADER_COLOR = new Color(17, 17, 17);
    private static final Color HEADER_BG_COLOR = new Color(255, 255, 255);
    private static final Color BORDER_COLOR = new Color(51, 51, 51);

    private CellData createHeader(String text) {
        return createHeader(text, null, Element.ALIGN_LEFT);
    }

    private CellData createHeader(String text, Integer colspan, Integer align) {
        CellData cellData = new CellData(text.toUpperCase());
        cellData.setBorder(Rectangle.BOTTOM);
        cellData.setFont(FontFactory.getFont(ITextFontTypeEnum.DEJAVUSANS.getName(), BaseFont.IDENTITY_H, 8, Font.BOLD));
        cellData.setBgColor(HEADER_BG_COLOR);
        cellData.setBorderColor(BORDER_COLOR);
        cellData.setAlignment(align);
        if (colspan != null) {
            cellData.setColspan(colspan);
        }
        return cellData;
    }

    private CellData createCellWithStyle(String text, Integer style) {
        CellData cellData = createCell(text);
        cellData.getFont().setStyle(style);
        return cellData;
    }

    private CellData createCell(String text) {
        CellData cellData = new CellData(escapeHtml(text));
        cellData.setFont(FontFactory.getFont(ITextFontTypeEnum.DEJAVUSANS.getName(), BaseFont.IDENTITY_H, 8));
        cellData.setBorder(Rectangle.NO_BORDER);
        cellData.setBorderColor(BORDER_COLOR);
        return cellData;
    }

    private CellData createRACell(String text) {
        CellData cellData = new CellData(escapeHtml(text), Element.ALIGN_RIGHT);
        cellData.setFont(FontFactory.getFont(ITextFontTypeEnum.DEJAVUSANS.getName(), BaseFont.IDENTITY_H, 8));
        cellData.setBorderColor(BORDER_COLOR);
        cellData.setBorder(Rectangle.NO_BORDER);
        return cellData;
    }


    @Override
    protected Object getDataClass(HttpServletRequest request) {
        return new PayslipRequestObject();
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        PayslipRequestObject payslip = (PayslipRequestObject) dataClass;
        EdsEmployee employee = employeeManager.get(payslip.getEmployeeId());
        Calendar cc = Calendar.getInstance();
        if (PayslipRequestObject.FROM_GENERATE_PAYSLIP.equals(payslip.getFromView())) {
            cc.setTime(new Date(payslip.getDate()));
            setFileName(getFileName(getWeekMonth(payslip.getWeekOrMonthNo()), employee.getName(), cc.get(Calendar.YEAR)));
        } else if (PayslipRequestObject.FROM_PAYSLIP_LIST.equals(payslip.getFromView())) {
            P11 p11;
            if (p11Manager.get(payslip.getObjectID()).getParent() != null) {
                p11 = p11Manager.get(payslip.getObjectID()).getParent();
            } else {
                p11 = p11Manager.get(payslip.getObjectID());
            }
            cc.setTime(p11.getDate());
            String periodType = employeePayrollSettingsManager.getEmployeeSettingValue(payslip.getEmployeeId(), PAY_FREQUENCY).getValue();
            String period = ("Weekly".equals(periodType) ? "Week " : "Month ") + p11.getPayPeriod();
            setFileName(getFileName(period, employee.getName(), cc.get(Calendar.YEAR)));
        } else {
            setFileName("Payslip ");
        }
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.PAYSLIP;
    }
}
