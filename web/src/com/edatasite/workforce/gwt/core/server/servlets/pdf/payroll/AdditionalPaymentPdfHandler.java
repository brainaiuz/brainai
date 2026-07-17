package com.edatasite.workforce.gwt.core.server.servlets.pdf.payroll;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.payrolluk.EdsAdditionalPayment;
import com.edatasite.workforce.gwt.core.client.rpc.AdditionalPaymentRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.AdditionalPaymentManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.IPostPDFHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextFontTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.*;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_en;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_ru;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_uz_lotin;
import com.edatasite.workforce.gwt.payroll.client.rpc.AdditionalPayment;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Shohruh on 10 Nov 2016.
 */
public class AdditionalPaymentPdfHandler extends AbstractITextPostPdfHandler implements IPostPDFHandler, Constants {

    @Autowired
    protected PayrollService payrollService;
    @Autowired
    @Qualifier("payrollLocalizer")
    protected WfmMessageSource payrollLocalizer;
    @Autowired
    protected AdditionalPaymentManager additionalPaymentManager;
    @Autowired
    protected DepartmentManager departmentManager;


    private static final Color HEADER_BG_COLOR = new Color(255, 255, 255);
    private static final Color BORDER_COLOR = new Color(51, 51, 51);
    protected DecimalFormat format = new DecimalFormat(",##0.00");

    protected Integer topColumns;
    protected Integer centerColumns;
    protected Integer bottomColumns;

    protected Integer TABLE_WIDTH = 350;

    protected String currencySymbol;
    protected boolean byCommission;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        /*RequestObject requestObject = (RequestObject) dataClass;
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setObjectId(requestObject.getObjectID());
        fp.setEmployeeId(requestObject.getUserID());
        AdditionalPayment additionalPayment = payrollService.getAdditionalPaymentData(fp);
        byCommission = additionalPayment.getByCommission();
        currencySymbol = additionalPayment.getCurrency() != null ? additionalPayment.getCurrency().getName() : getCompanyCurrencySymbol();

        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        pdfData.setPdfViewType(ITextPdfViewTypeEnum.SUMMARYVIEW);

        ITextSummaryView summaryView = new ITextSummaryView();
        summaryView.setFontName(ITextFontTypeEnum.DEJAVUSANS_BOLD.getName());

        addSpace(summaryView);
        initTableColumns();

        ITextTableList title = new ITextTableList(1);
        title.setBorderWidth(0);

        ITextTableList top = new ITextTableList(topColumns);
        top.setTableAlignment(Element.ALIGN_RIGHT);
        top.setTotalWidth(350);
        top.setCellPadding(1.5f);
        top.setBorderWidth(0);

        ITextTableList center = new ITextTableList(centerColumns);
        center.setTableAlignment(Element.ALIGN_RIGHT);
        center.setTotalWidth(TABLE_WIDTH);

        ITextTableList bottom = new ITextTableList(bottomColumns);
        bottom.setTableAlignment(Element.ALIGN_RIGHT);
        bottom.setTotalWidth(TABLE_WIDTH);

        titlePanel(title, additionalPayment);
        topPanel(top, additionalPayment);
        float[] widthPercentage = new float[]{0.35f, 0.65f};
        summaryView.addTable(title, top);

        centerPanel(center, additionalPayment);
        summaryView.addTable(center);

        bottomPanel(bottom, additionalPayment);
        summaryView.addTable(bottom);

        pdfData.setSummaryView(summaryView);

        return pdfData;*/
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        AdditionalPaymentRequestObject requestObject = (AdditionalPaymentRequestObject) dataClass;
        ListingFilterParameter fp = new ListingFilterParameter();
        if (requestObject.getObjectID() != null) {
            fp.setObjectId(requestObject.getObjectID());
        }
        if (requestObject.getUserID() != null) {
            fp.setEmployeeId(requestObject.getUserID());
        }
        AdditionalPayment additionalPayment = payrollService.getAdditionalPaymentData(fp);
        currencySymbol = additionalPayment.getCurrency() != null ? additionalPayment.getCurrency().getName() : getCompanyCurrencySymbol();
        baseInvoice.setCurrency(currencySymbol);

        EdsUser user = userManager.getUser();
        pdfData.setCompanyData(getCompanyData(user.getCompany(), true, hasPhantom));
        pdfData.setPdfViewType(ITextPdfViewTypeEnum.BASEINVOICE);
        pdfData.setBaseInvoice(baseInvoice);
        baseInvoice.setCustomNumberAndDatesTable(getCustomNumberAndDatesTable(additionalPayment));
        baseInvoice.setCustomProductTable(getCustomProductTable(additionalPayment));
        baseInvoice.setCustomTotalTable(getCustomTotalTable(additionalPayment));

        return pdfData;
    }

    protected CustomisedITextTable getCustomNumberAndDatesTable(AdditionalPayment additionalPayment) {
        CustomisedITextTable numAndDates = new CustomisedITextTable();
        String typeValue = "";
        String typeName = "";
        String departmentLeader = "";
        if (additionalPayment.getLocation() != null) {
            typeValue = additionalPayment.getLocation().getName();
            typeName = commonLocalizer.localize("location");
        } else if (additionalPayment.getEmployee() != null) {
            typeValue = additionalPayment.getEmployee().getName();
            typeName = commonLocalizer.localize("employee");
        } else if (additionalPayment.getDepartment() != null && additionalPayment.getDepartment().getId() != null) {
            EdsDepartment edsDepartment = departmentManager.get(additionalPayment.getDepartment().getId());
            departmentLeader = edsDepartment != null && edsDepartment.getLeader() != null ? edsDepartment.getLeader().getFullName() : "";
            typeValue = additionalPayment.getDepartment().getName();
            typeName = commonLocalizer.localize("department");
        } else if (additionalPayment.getPayrollBatch() != null) {
            typeValue = additionalPayment.getPayrollBatch().getName();
            if (Objects.equals(typeValue, "All Employees")) {
                typeValue = commonLocalizer.localize("allEmployees");
            }
            typeName = commonLocalizer.localize(PdfLocalizationName.group);
        }
        numAndDates.addColumnOrder(PDFConstants.COLUMN_NAME, PDFConstants.COLUMN_VALUE);
        numAndDates.addRowWithCode(PDFConstants.REFERENCE, commonLocalizer.localize(PdfLocalizationName.reference), escapeHtml(additionalPayment.getReference()));
        numAndDates.addRowWithCode(PDFConstants.PERIOD, commonLocalizer.localize(PdfLocalizationName.period), additionalPayment.getMonth() != null ? escapeHtml(ServerUtils.convertMonthToInterfaceLanguage(additionalPayment.getMonth()) + ", " + additionalPayment.getYear()) : "");
        numAndDates.addRowWithCode(PDFConstants.GROUP, typeName, typeValue);
        numAndDates.addRowWithCode(PDFConstants.APPROVER, commonLocalizer.localize(PdfLocalizationName.approver), additionalPayment.getCurrentApprover() != null ? escapeHtml(additionalPayment.getCurrentApprover().toString()) : "");
        numAndDates.addRowWithCode(PDFConstants.TYPE, commonLocalizer.localize(PdfLocalizationName.type), additionalPayment.getType() != null && additionalPayment.getType().equals("BY_COMMISION") ? commonLocalizer.localize(PdfLocalizationName.byCommission) : commonLocalizer.localize(PdfLocalizationName.additionalPayment));
        numAndDates.addRowWithCode(PDFConstants.CATEGORY_TYPE, commonLocalizer.localize(PdfLocalizationName.category), commonLocalizer.localize(ServerUtils.makeCamelCase(escapeHtml(additionalPayment.getCategoryType()))));
        numAndDates.addRowWithCode(PDFConstants.DEPARTMENT_LEADER, "Department Leader", escapeHtml(departmentLeader));

        return numAndDates;
    }

    protected CustomisedITextTable getCustomProductTable(AdditionalPayment additionalPayment) {
        CustomisedITextTable productItemTable = new CustomisedITextTable();
        boolean byCommission = additionalPayment.getByCommission();
        productItemTable.addColumn(PDFConstants.ITEM_NAME, commonLocalizer.localize(PdfLocalizationName.name));
        if (additionalPayment.getPaymentType() != null && additionalPayment.getPaymentType().equals("BASIC_SALARY_ALLOWANCE")) {
            productItemTable.addColumn(PDFConstants.ITEM_BASIC_AMOUNT_PAY, payrollLocalizer.localize(PdfLocalizationName.basicAllowancePay));
            productItemTable.addColumn(PDFConstants.ITEM_PERCENTAGE, commonLocalizer.localize(PdfLocalizationName.percentage));
        } else if (additionalPayment.getPaymentType() != null && additionalPayment.getPaymentType().equals("BASIC_SALARY")) {
            productItemTable.addColumn(PDFConstants.ITEM_BASIC_SALARY, payrollLocalizer.localize(PdfLocalizationName.basicSalary));
            productItemTable.addColumn(PDFConstants.ITEM_PERCENTAGE, commonLocalizer.localize(PdfLocalizationName.percentage));
        }
        productItemTable.addColumn(PDFConstants.ITEM_CATEGORY, commonLocalizer.localize(PdfLocalizationName.category));
        productItemTable.addColumn(PDFConstants.ITEM_DATE, commonLocalizer.localize(PdfLocalizationName.paymentDate));
        productItemTable.addColumn(PDFConstants.ITEM_AMOUNT, commonLocalizer.localize(PdfLocalizationName.amount));
        if (byCommission) {
            productItemTable.addColumn(PDFConstants.ITEM_TOTAL_SALES_PRICE, commonLocalizer.localize(PdfLocalizationName.salesAmount));
            productItemTable.addColumn(PDFConstants.ITEM_COMISSION, commonLocalizer.localize(PdfLocalizationName.commission));
        }
        productItemTable.addColumn(PDFConstants.ITEM_JUSTIFICATION, "Justification");
        productItemTable.addColumn(PDFConstants.ITEM_INCIDENT, "Incident");

        if (additionalPayment.getItems() != null) {
            for (PaymentDeductionObject item : additionalPayment.getItems()) {
                if (item != null) {
                    ArrayList<String> row = new ArrayList<>();
                    String employeeName = "";
                    if (item.getEmployee() != null) {
                        if (item.getEmployee().getDescription() != null && !"".equals(item.getEmployee().getDescription())) {
                            employeeName = escapeHtml(item.getEmployee().getDescription()) + " → " + escapeHtml(item.getEmployee().getName());
                        } else {
                            employeeName = escapeHtml(item.getEmployee().getName());
                        }
                    }
                    String basicSalary = item.getEmployeeBasicSalary() != null ? getMoneyFormat(item.getEmployeeBasicSalary()) : "";
                    String basicSalaryPlusAllowence = item.getBasicPlusAllowance() != null ? item.getBasicPlusAllowance().toString() : "";
                    String percentage = item.getPercentage() != null ? item.getPercentage().toString() : "";

                    String categoryName = item.getCategoryItem() != null ? escapeHtml(item.getCategoryItem().getName()) : "";
                    String paymentDate;
                    if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                        paymentDate = item.getAdditionalPaymentDate() != null ? ServerUtils.convertToUzbDateFormat(dateFormat(item.getAdditionalPaymentDate().getNonConvertedDate())) : "";
                    } else {
                        paymentDate = item.getAdditionalPaymentDate() != null ? dateFormat(item.getAdditionalPaymentDate().getNonConvertedDate()) : "";
                    }
                    String paymentAmount = item.getPaymentAmount() != null ? formatCurrency(item.getPaymentAmount(), null) : "";
                    String salesAmount = "";
                    String commission = "";
                    if (byCommission) {
                        salesAmount = item.getTotalAmount() != null ? formatCurrency(item.getTotalAmount(), currencySymbol) : "";
                        commission = item.getCommission() != null ? formatCurrency(item.getCommission(), null) : "";
                    }
                    row.add(employeeName);
                    if (additionalPayment.getPaymentType()!=null&&additionalPayment.getPaymentType().equals("BASIC_SALARY")) {
                        row.add(basicSalary);
                        row.add(percentage);
                    } else if (additionalPayment.getPaymentType()!=null&&additionalPayment.getPaymentType().equals("BASIC_SALARY_ALLOWANCE")) {
                        row.add(basicSalaryPlusAllowence);
                        row.add(percentage);
                    }
                    row.add(categoryName);
                    row.add(paymentDate);
                    row.add(paymentAmount);
                    if (byCommission) {
                        row.add(salesAmount);
                        row.add(commission);
                    }


                    List<CompanyCustomFieldItem> companyCustomFieldsItemList = item.getItemCustomFields();
                    if (companyCustomFieldsItemList != null) {
                        for (CompanyCustomFieldItem companyCustomFieldItem : companyCustomFieldsItemList) {
                            String stringFieldNAme = companyCustomFieldItem.getFieldStringValue() != null ? companyCustomFieldItem.getFieldStringValue() : "";
                            row.add(stringFieldNAme);
                        }
                    }

                    productItemTable.addRow(row.toArray(new String[]{}));
                }
            }
        }

        return productItemTable;
    }

    protected CustomisedITextTable getCustomTotalTable(AdditionalPayment additionalPayment) {
        NumberToWord numberToWordConverter = new NumberToWord_en();
        NumberToWord numberToWordUzConverter = new NumberToWord_uz_lotin();
        NumberToWord numberToWordRuConverter = new NumberToWord_ru();

        CustomisedITextTable totalTable = new CustomisedITextTable();
        totalTable.addColumnOrder(PDFConstants.COLUMN_NAME, PDFConstants.COLUMN_VALUE);
        totalTable.addRowWithCode(TOTAL_AMOUNT, commonLocalizer.localize(PdfLocalizationName.total), formatCurrency(additionalPayment.getTotal(), null));
        if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
            totalTable.addRowWithCode(PDFConstants.TOTAL_IN_WORDS, commonLocalizer.localize(PdfLocalizationName.total), additionalPayment.getTotal() != null ? numberToWordUzConverter.toWord(additionalPayment.getTotal()) : "");
        } else if (ServerUtils.getUserLocale().getLanguage().equals("ru")) {
            totalTable.addRowWithCode(PDFConstants.TOTAL_IN_WORDS, commonLocalizer.localize(PdfLocalizationName.total), additionalPayment.getTotal() != null ? numberToWordRuConverter.toWord(additionalPayment.getTotal()) : "");
        } else {
            totalTable.addRowWithCode(PDFConstants.TOTAL_IN_WORDS, commonLocalizer.localize(PdfLocalizationName.total), additionalPayment.getTotal() != null ? numberToWordConverter.toWord(additionalPayment.getTotal()) : "");
        }
        return totalTable;
    }

    protected void initTableColumns() {
        topColumns = 4;
        centerColumns = byCommission ? 5 : 3;
        bottomColumns = 2;
        TABLE_WIDTH = byCommission ? 550 : 350;
    }

    protected void addSpace(ITextSummaryView summaryView) {
        ITextTableList space = new ITextTableList(1);
        ITextTableList space1 = new ITextTableList(1);
        ITextTableList space2 = new ITextTableList(1);
        summaryView.addTable(space);
        summaryView.addTable(space1);
        summaryView.addTable(space2);
    }

    protected void titlePanel(ITextTableList title, AdditionalPayment additionalPayment) {
        CellData cellData = new CellData(commonLocalizer.localize(PayrollConstants.CATEGORY_PAYMENT.equals(additionalPayment.getCategoryType()) ? PdfLocalizationName.additionalPayment : PdfLocalizationName.additionalDeduction), Element.ALIGN_LEFT);
        cellData.setFont(FontFactory.getFont(ITextFontTypeEnum.DEJAVUSANS.getName(), BaseFont.IDENTITY_H, 11, Font.BOLD));
        title.setTableAlignment(Element.ALIGN_LEFT);
        title.addPdfTableRows(cellData);
    }

    protected void topPanel(ITextTableList top, AdditionalPayment additionalPayment) {
        CellData[] topHeaders = new CellData[topColumns];
        topHeaders[0] = createHeader(commonLocalizer.localize(PdfLocalizationName.reference));
        topHeaders[1] = createHeader(commonLocalizer.localize(PdfLocalizationName.period));
        topHeaders[2] = createHeader(commonLocalizer.localize(PdfLocalizationName.group));
        topHeaders[3] = createHeader(commonLocalizer.localize(PdfLocalizationName.approver));
        top.addPdfTableRows(topHeaders);

        CellData[] topRows = new CellData[topColumns];
        topRows[0] = createCell(additionalPayment.getReference());
        topRows[1] = createCell(additionalPayment.getPeriod());
        topRows[2] = createCell(additionalPayment.getPayrollBatch() != null ? additionalPayment.getPayrollBatch().getName() : "All Employees");
        topRows[3] = createCell(additionalPayment.getCurrentApprover().toString());
        top.addPdfTableRows(topRows);
    }

    protected void centerPanel(ITextTableList center, AdditionalPayment additionalPayment) {
        boolean byCommission = additionalPayment.getByCommission();
        if (byCommission) {
            center.addPdfTableHeader(
                    createHeader(commonLocalizer.localize(PdfLocalizationName.employee)),
                    createHeader(commonLocalizer.localize(PdfLocalizationName.category)),
                    createNumberHeader(commonLocalizer.localize(PdfLocalizationName.salesAmount)),
                    createNumberHeader(commonLocalizer.localize(PdfLocalizationName.commission)),
                    createNumberHeader(commonLocalizer.localize(PdfLocalizationName.amount)));

        } else {
            center.addPdfTableHeader(
                    createHeader(commonLocalizer.localize(PdfLocalizationName.employee)),
                    createHeader(commonLocalizer.localize(PdfLocalizationName.category)),
                    createNumberHeader(commonLocalizer.localize(PdfLocalizationName.amount)));

        }
        for (PaymentDeductionObject item : additionalPayment.getItems()) {
            if (item != null) {
                CellData[] datas = new CellData[centerColumns];
                if (byCommission) {
                    if (item.getEmployee() != null) {
                        /*if (item.getEmployee().getDescription() != null && !"".equals(item.getEmployee().getDescription())) {
                            datas[0] = createCell(item.getEmployee().getDescription() + " -> " + item.getEmployee().getName());
                        } else {
                        }*/
                        datas[0] = createCell(item.getEmployee().getName());
                    }
                    datas[1] = createCell(item.getCategoryItem().getName());
                    datas[2] = createNumberCell(formatCurrency(item.getTotalAmount(), currencySymbol));
                    datas[3] = createNumberCell(formatCurrency(item.getCommission(), null));
                    datas[4] = createNumberCell(formatCurrency(item.getPaymentAmount(), currencySymbol));
                } else {
                    if (item.getEmployee() != null) {
                        /*if (item.getEmployee().getDescription() != null && !"".equals(item.getEmployee().getDescription())) {
                            datas[0] = createCell(item.getEmployee().getDescription() + " -> " + item.getEmployee().getName());
                        } else {
                        }*/
                        datas[0] = createCell(item.getEmployee().getName());
                    }
                    datas[1] = createCell(item.getCategoryItem().getName());
                    datas[2] = createNumberCell(formatCurrency(item.getPaymentAmount(), currencySymbol));
                }
                center.addPdfTableRows(datas);
            }
        }
    }

    protected void bottomPanel(ITextTableList bottom, AdditionalPayment additionalPayment) {
        CellData[] total = new CellData[bottomColumns];
        total[0] = createCellWithStyle(commonLocalizer.localize(PdfLocalizationName.total), com.lowagie.text.Font.BOLD);
        total[1] = createNumberCell(formatCurrency(additionalPayment.getTotal(), currencySymbol));
        bottom.addPdfTableRows(total);
    }

    protected CellData createNumberHeader(String text) {
        CellData cellData = createHeader(text);
        cellData.setAlignment(Element.ALIGN_RIGHT);
        return cellData;
    }

    protected CellData createHeader(String text) {
        CellData cellData = new CellData(text.toUpperCase());
        cellData.setBorder(com.lowagie.text.Rectangle.BOTTOM);
        cellData.setFont(com.lowagie.text.FontFactory.getFont(ITextFontTypeEnum.DEJAVUSANS.getName(), BaseFont.IDENTITY_H, 8, com.lowagie.text.Font.BOLD));
        cellData.setAlignment(Element.ALIGN_LEFT);
        cellData.setBgColor(HEADER_BG_COLOR);
        cellData.setBorderColor(BORDER_COLOR);
        return cellData;
    }

    protected CellData createCell(String text) {
        CellData cellData = new CellData(escapeHtml(text));
        cellData.setFont(FontFactory.getFont(ITextFontTypeEnum.DEJAVUSANS.getName(), BaseFont.IDENTITY_H, 8));
        cellData.setBorder(Rectangle.NO_BORDER);
        cellData.setBorderColor(BORDER_COLOR);
        return cellData;
    }

    protected CellData createNumberCell(String text) {
        CellData cellData = createCell(text);
        cellData.setAlignment(Element.ALIGN_RIGHT);
        return cellData;
    }

    protected CellData createCellWithStyle(String text, Integer style) {
        CellData cellData = createCell(text.toUpperCase());
        cellData.getFont().setStyle(style);
        return cellData;
    }

    protected String formatCurrency(BigDecimal amount, String currencySymbol) {
        if (amount == null) return "";
        return (currencySymbol != null ? currencySymbol + " " : "") + format.format(amount.setScale(2, RoundingMode.HALF_UP));
    }

    protected boolean getPagingOnTop() {
        return true;
    }

    /*@Override
    protected PdfPTable getPageHeader(Object object, EdsCompany edsCompany, PdfWriter pdfWriter, Document document, String fontName) throws DocumentException {
        String default_font = ITextFontTypeEnum.DEJAVUSANS.getName();

        PdfPTable header = new PdfPTable(1);
        setShownPaging(true);
        float width = document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin();
        header.setWidthPercentage(100);
        header.getDefaultCell().setBorder(0);
        header.setTotalWidth(width);


        PdfPTable pageInfo = new PdfPTable(1);
        pageInfo.setWidthPercentage(100);
        pageInfo.setTotalWidth(width);
        PdfPCell pdfPCell = new PdfPCell(new Phrase(""));
        pdfPCell.setPaddingTop(65f);
        pdfPCell.setBorder(Rectangle.NO_BORDER);
        pageInfo.addCell(pdfPCell);
        header.addCell(pageInfo);


        PdfPTable companyInfo = new PdfPTable(2);
        companyInfo.getDefaultCell().setBorder(0);
        companyInfo.setWidthPercentage(50);
        companyInfo.setTotalWidth(width);

        String companyName = edsCompany.getName();
        String address = edsCompany.getAddress1() != null ? escapeHtml(edsCompany.getAddress1()) : "";
        String address2 = edsCompany.getBillAddress2() != null ? edsCompany.getBillAddress2() : "";
        String city = edsCompany.getCity() != null ? escapeHtml(edsCompany.getCity()) : "";
        String state = edsCompany.getCountryRegion() != null ? edsCompany.getCountryRegion().getName() : "";
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
        if (color != null && !"".equals(color) && color.length() == 6) {
            textColor = Utils.hexToRGB(color);
        } else {
            textColor = Utils.hexToRGB(DEFAULT_FONT_COLOR);
        }

        leftHeader.addCell(new Phrase(companyName, FontFactory.getFont(default_font, BaseFont.IDENTITY_H, 14, com.lowagie.text.Font.BOLD, textColor)));
        if (edsCompany.getBillAddress2() != null && !"".equals(edsCompany.getBillAddress2()) && !address.equals(edsCompany.getBillAddress2())) {
            leftHeader.addCell(new Phrase(address, FontFactory.getFont(default_font, BaseFont.IDENTITY_H, 8)));
            leftHeader.addCell(new Phrase(address2, FontFactory.getFont(default_font, BaseFont.IDENTITY_H, 8)));
            leftHeader.addCell(new Phrase(city, FontFactory.getFont(default_font, BaseFont.IDENTITY_H, 8)));
        } else {
            leftHeader.addCell(new Phrase(address, FontFactory.getFont(default_font, BaseFont.IDENTITY_H, 8)));
            leftHeader.addCell(new Phrase(cityPostCode, FontFactory.getFont(default_font, BaseFont.IDENTITY_H, 8)));
        }
        leftHeader.addCell(new Phrase(state, FontFactory.getFont(default_font, BaseFont.IDENTITY_H, 8)));
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
                com.lowagie.text.Image image = com.lowagie.text.Image.getInstance(imageUrl);
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
    }*/

    /*@Override
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
            Phrase poweredBy = new Phrase(commonLocalizer.localize(PdfLocalizationName.poweredBy), FontFactory.getFont(ITextFontTypeEnum.DEJAVUSANS.getName(), BaseFont.IDENTITY_H, 8, com.lowagie.text.Font.BOLD));
            Anchor anchor = new Anchor(url, FontFactory.getFont(ITextFontTypeEnum.DEJAVUSANS.getName(), BaseFont.IDENTITY_H, 8));
            anchor.setReference(url);
            footer.addCell(poweredBy);
            footer.addCell(anchor);
        }
        return footer;
    }*/

    protected String getCompanyCurrencySymbol() {
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

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        AdditionalPaymentRequestObject requestObject = new AdditionalPaymentRequestObject();
        if (StringUtils.isNotBlank(request.getParameter("objectID"))) {
            requestObject.setObjectID(Integer.valueOf(request.getParameter("objectID")));
        }
        if (StringUtils.isNotBlank(request.getParameter("userID"))) {
            requestObject.setUserID(Integer.valueOf(request.getParameter("userID")));
        }
        if (StringUtils.isNotBlank(request.getParameter("pdfTemplateID"))) {
            requestObject.setPdfTemplateID(Integer.valueOf(request.getParameter("pdfTemplateID")));
        }
        return requestObject;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        RequestObject requestObject = (RequestObject) dataClass;
        EdsAdditionalPayment additionalPayment = additionalPaymentManager.get(requestObject.getObjectID());
        setFileName("Additional_" + additionalPayment.getReference());
    }

    @Override
    protected Integer getCustomisedPDFTemplateId(Object object) {
        if (object instanceof AdditionalPaymentRequestObject) {
            return ((AdditionalPaymentRequestObject) object).getPdfTemplateID();
        }
        return null;
    }

    @Override
    protected PdfParams.Orientation getOrientation(Object dataClass) {
        return ((RequestObject) dataClass).getIS_LANDSCAPE() ? PdfParams.Orientation.landscape : null;
    }

    @Override
    protected String getTableName(Object dataClass) {
        return commonLocalizer.localize(PdfLocalizationName.additionalPayment);
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.ADDITIONAL_PAYMENT;
    }

}
