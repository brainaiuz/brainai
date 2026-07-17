package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.invoice.client.rpc.AgingSummaryInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.AgingSummaryItem;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 8/14/12
 * Time: 4:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class AgingSummaryExcelHandler extends BaseExcelHandler implements Constants {

    @Autowired
    private InvoiceServiceLocal invoiceServiceLocal;
    @Qualifier("uploadManager")
    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private PropertManager propertManager;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private AccountingManager accountingManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private CommonService commonService;
    @Autowired
    private InvoiceManager invoiceManager;

    private final int aCellSize = 40;
    private final int bCellSize = 15;

    private Integer interval;
    private Integer intervalLimit;
    private String type;
    private Boolean isDetailView;
    private List<AgingSummaryItem> result;
    private Integer columnCount;
    private Map<Integer, BigDecimal> balanceByColumn;
    private BigDecimal fullTotal;
    private List<ExcelData[]> list;
    private Date startDate;
    private String sheetName;

    @Override
    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParameter = (ListingFilterParameter) object;
        EdsProperty propertyReceivable = propertManager.findByCode("arAgingSummary");
        EdsProperty propertyPayable = propertManager.findByCode("apAgingSummary");
        String sheetName = "";
        if (RECEIVABLE.equals(filterParameter.getAccountType())) {
            sheetName = propertyReceivable != null ? propertyReceivable.getPlural() : commonLocalizer.localize(PdfLocalizationName.arAgingSummary1);
        } else {
            sheetName = propertyPayable != null ? propertyPayable.getPlural() : commonLocalizer.localize(PdfLocalizationName.apAgingSummary1);
        }
        interval = filterParameter.getInterval();
        intervalLimit = filterParameter.getIntervalLimit();
        type = filterParameter.getAccountType();
        isDetailView = filterParameter.isShowBudget();
        startDate = parseFilterParameterDate(filterParameter.getStartDateNC());

        result = invoiceServiceLocal.getOverdueInvoiceByCrmAccount(filterParameter).getList();
        list = new LinkedList<>();
        EdsUser user = uploadManager.getUser();
        String title;
        if (intervalLimit % interval == 0) {
            columnCount = intervalLimit / interval + 4;
        } else {
            columnCount = intervalLimit / interval + 5;
        }

        String shortDateFormat = "MMM dd, yyyy";
        EdsCompanySettings companySettings = user.getCompany().getCompanySettings();
        if (companySettings != null && companySettings.getShortDateFormat() != null && !"".equals(companySettings.getShortDateFormat())) {
            shortDateFormat = companySettings.getShortDateFormat();
        }
        SimpleDateFormat format = new SimpleDateFormat(shortDateFormat, Locale.ENGLISH);
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();

        if (type.equals(RECEIVABLE)) {
            title = commonLocalizer.localize(PdfLocalizationName.receivables);
        } else {
            title = commonLocalizer.localize(PdfLocalizationName.agedPayable);
        }

        String currencySymbol = financialSettings.getCurrency().getSymbol();
        String currencyCode = financialSettings.getCurrency().getName();
        currencySymbol = currencySymbol != null ? currencySymbol : "";
        int lastColumnIndex = columnCount;
        list.add(new ExcelData[]{
                new ExcelData("", ExcelData.STRING, aCellSize, lastColumnIndex)
        });
        ExcelData titleData = ExcelData.getReportNameData(sheetName, aCellSize, lastColumnIndex);

        ExcelData companyData = ExcelData.getReportNameChildData(user.getCompany().getName(), aCellSize, lastColumnIndex);
        ExcelData dateData;
        if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
            dateData = ExcelData.getReportNameChildData(ServerUtils.convertToUzbDateFormat(format.format(startDate)) + "  " + commonLocalizer.localize(PdfLocalizationName.asOF), aCellSize, lastColumnIndex);
        } else {
            dateData = ExcelData.getReportNameChildData(commonLocalizer.localize(PdfLocalizationName.asOF) + " "
                    + format.format(startDate), aCellSize, lastColumnIndex);
        }

        ExcelData currencyData = ExcelData.getReportNameChildData(commonLocalizer.localize(PdfLocalizationName.figuresIn) + " " + currencySymbol + "(" + currencyCode + ")", aCellSize, lastColumnIndex);

        list.add(new ExcelData[]{
                titleData
        });
        list.add(new ExcelData[]{
                companyData
        });
        list.add(new ExcelData[]{
                dateData
        });
        list.add(new ExcelData[]{
                currencyData
        });

        ExcelData emptyData = new ExcelData("", ExcelData.STRING, aCellSize, lastColumnIndex);
        ExcelData[] cellEmptyHeader = new ExcelData[]{
                emptyData
        };
        list.add(cellEmptyHeader);

        if (isDetailView) {
            if (Objects.equals(ServerSecurityContext.getInstance().getCompanyId(), "56895")) {
                drawDetailTableForKendah(format);
            } else {
                drawDetailTable(format);
            }
        } else {
            drawTable();
        }

        HSSFWorkbook wb = new WorkBook(list).getWorkBook(filename, 0, 0, 0, 3);
        // Set the columns to repeat from column 0 to 2 on the first sheet
        wb.setRepeatingRowsAndColumns(0, 0, columnCount, 0, 5);

        return wb;
    }

    private void drawTable() {
        ExcelData[] excelDatas, excelPercentDatas;
        Integer startLimit, endLimit, in, startt;

        ExcelData[] headers;
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_ClIENT_OR_SUPPLIER_OWNER)) {
            headers = new ExcelData[columnCount+1];
        } else {
            headers = new ExcelData[columnCount];
        }
        headers[0] = new ExcelData(commonLocalizer.localize(PdfLocalizationName.supplier), ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT);
        headers[0].setBold(true);
        headers[1] = new ExcelData(commonLocalizer.localize(PdfLocalizationName.current), ExcelData.STRING, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
        int i = 2, j = 0;
        for (; i < columnCount - 1; i++, j++) {
            startLimit = j * interval + 1;
            endLimit = (j + 1) * interval;
            if (startLimit >= intervalLimit) {
                headers[i] = new ExcelData(" > " + intervalLimit, ExcelData.STRING, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
            } else {
                if (endLimit >= intervalLimit) {
                    headers[i] = new ExcelData(startLimit + " - " + intervalLimit, ExcelData.STRING, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
                } else {
                    headers[i] = new ExcelData(startLimit + " - " + endLimit, ExcelData.STRING, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
                }
            }
        }
        headers[i] = new ExcelData(commonLocalizer.localize(PdfLocalizationName.total), ExcelData.STRING, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_ClIENT_OR_SUPPLIER_OWNER)) {
            headers[i + 1] = new ExcelData(commonLocalizer.localize(PdfLocalizationName.owner), ExcelData.STRING, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);
            headers[i + 1].setBold(true);
        }
        list.add(headers);

        excelDatas = new ExcelData[1];
        excelDatas[0] = new ExcelData(RECEIVABLE.equals(type) ? commonLocalizer.localize(PdfLocalizationName.receivables) : commonLocalizer.localize(PdfLocalizationName.payable), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);
        list.add(excelDatas);

        balanceByColumn = new HashMap<>();
        for (AgingSummaryItem item : result) {
            BigDecimal total = BigDecimal.ZERO;
            in = interval;
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_ClIENT_OR_SUPPLIER_OWNER)) {
                excelDatas = new ExcelData[columnCount+1];
            } else {
                excelDatas = new ExcelData[columnCount];
            }

            excelDatas[0] = new ExcelData(item.getCustomerOrSupplier(), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);

            for (i = 1, j = -1; i < columnCount - 1; i++, j++) {
                BigDecimal balance = BigDecimal.ZERO;
                startt = j * interval;
                in = (j+1)*interval;

                if (CollectionUtils.isNotEmpty(item.getInvoiceList())) {
                    for (AgingSummaryInvoiceItem inv : item.getInvoiceList()) {

                        if (startt > intervalLimit) {
                            startt = intervalLimit;
                        }
                        if (in > intervalLimit) {
                            in = intervalLimit;
                        }
                        if ((inv.getAging() > startt && inv.getAging() <= in) || (inv.getAging() > intervalLimit && i == columnCount - 2) || (i == 1 && inv.getAging() == 0)) {
                            balance = balance.add(inv.getAmount());
                        }
                    }
                }

                excelDatas[i] = new ExcelData(balance, ExcelData.CURRENCY, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
                balanceByColumn.put(i, getColumnBalance(i).add(balance));
                total = total.add(balance);
            }
            excelDatas[columnCount - 1] = new ExcelData(total, ExcelData.CURRENCY, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
            excelDatas[columnCount - 1].setBold(true);
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_ClIENT_OR_SUPPLIER_OWNER)) {
                String ownerName = accountingManager.getOwnerName(item.getCustomerOrSupplierObjectId());
                excelDatas[columnCount] = new ExcelData(ownerName, ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);
            }
            balanceByColumn.put(columnCount - 1, getColumnBalance(columnCount - 1).add(total));
            list.add(excelDatas);
        }
        if (result != null && result.size() > 0) {

            excelPercentDatas = new ExcelData[columnCount];
            excelPercentDatas[0] = new ExcelData(commonLocalizer.localize(PdfLocalizationName.ageingPercentage), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);
            excelPercentDatas[0].setBold(true);

            excelDatas = new ExcelData[columnCount];
            excelDatas[0] = new ExcelData(commonLocalizer.localize(PdfLocalizationName.total) + " " + (type.equals(RECEIVABLE) ? commonLocalizer.localize(PdfLocalizationName.receivables) : commonLocalizer.localize(PdfLocalizationName.payable)), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);
            excelDatas[0].setBold(true);

            for (i = 1; i < columnCount; i++) {
                excelDatas[i] = new ExcelData(balanceByColumn.get(i), ExcelData.CURRENCY, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
                excelDatas[i].setBold(true);

                double d = 0d;

                if (balanceByColumn.get(columnCount -1).compareTo(BigDecimal.ZERO) != 0) {
                    d = balanceByColumn.get(i).divide(balanceByColumn.get(columnCount - 1), RoundingMode.HALF_UP).doubleValue();
                }

                excelPercentDatas[i] = new ExcelData(d, ExcelData.NUMBER_FORMAT_PERCENTAGE, 0, true, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
                excelPercentDatas[i].setBold(true);
            }
            if (!Objects.equals(ServerSecurityContext.getInstance().getCompanyId(), "93943")) {
                list.add(excelPercentDatas);
            }
            list.add(excelDatas);
        } else {
            excelDatas = new ExcelData[columnCount];
            excelDatas[0] = new ExcelData(commonLocalizer.localize(PdfLocalizationName.total) + " " + (type.equals(RECEIVABLE) ? commonLocalizer.localize(PdfLocalizationName.receivables) : commonLocalizer.localize(PdfLocalizationName.payable)), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);
            excelDatas[0].setBold(true);
            for (i = 1; i < columnCount; i++) {
                excelDatas[i] = new ExcelData(BigDecimal.ZERO, ExcelData.CURRENCY, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
                excelDatas[i].setBold(true);
            }
            list.add(excelDatas);
        }
    }

    private void drawDetailTable(SimpleDateFormat format) {
        Integer startLimit, endLimit, in, startt;

        ExcelData[] headers = new ExcelData[columnCount+2];
        headers[0] = new ExcelData(commonLocalizer.localize(PdfLocalizationName.invoiceDate), ExcelData.STRING, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT);
        headers[1] = new ExcelData(commonLocalizer.localize(PdfLocalizationName.dueDate), ExcelData.STRING, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT);
        headers[2] = new ExcelData(commonLocalizer.localize(PdfLocalizationName.reference), ExcelData.STRING, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT);

        headers[3] = new ExcelData(commonLocalizer.localize(PdfLocalizationName.current), ExcelData.STRING, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
        int i = 4, j = 0;
        for (; i < columnCount + 1; i++, j++) {
            startLimit = j * interval + 1;
            endLimit = (j + 1) * interval;
            if (startLimit >= intervalLimit) {
                headers[i] = new ExcelData(" > " + intervalLimit, ExcelData.STRING, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
            } else {
                if (endLimit >= intervalLimit) {
                    headers[i] = new ExcelData(startLimit + " - " + intervalLimit, ExcelData.STRING, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
                } else {
                    headers[i] = new ExcelData(startLimit + " - " + endLimit, ExcelData.STRING, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
                }
            }
        }
        headers[i] = new ExcelData(commonLocalizer.localize(PdfLocalizationName.total), ExcelData.STRING, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
        list.add(headers);

        balanceByColumn = new HashMap<>();
        for (AgingSummaryItem item : result) {
            BigDecimal total = BigDecimal.ZERO;
            list.add(new ExcelData[]{ExcelData.getReportNameData("", 0, columnCount + 2)});
            list.add(new ExcelData[]{ExcelData.getReportNameData(item.getCustomerOrSupplier(), 0, columnCount + 2)});
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_ClIENT_OR_SUPPLIER_OWNER)) {
                String ownerName = accountingManager.getOwnerName(item.getCustomerOrSupplierObjectId());
                list.add(new ExcelData[]{ExcelData.getReportNameData(commonLocalizer.localize(PdfLocalizationName.owner) + ": " + ownerName, 0, columnCount + 2)});
            }

            for (i = 1, j = -1; i < columnCount - 1; i++, j++) {
                BigDecimal balance = BigDecimal.ZERO;
                startt = j * interval;
                in = (j + 1) * interval;

                if (item.getInvoiceList() != null && !item.getInvoiceList().isEmpty()) {
                    for (AgingSummaryInvoiceItem inv : item.getInvoiceList()) {
                        if (startt > intervalLimit) {
                            startt = intervalLimit;
                        }
                        if (in > intervalLimit) {
                            in = intervalLimit;
                        }
                        if ((inv.getAging() > startt && inv.getAging() <= in) || (inv.getAging() > intervalLimit && i == columnCount - 2) || (i == 1 && inv.getAging() == 0)) {
                            ExcelData[] invoice = new ExcelData[columnCount+2];
                            int x = 0;
                            invoice[x++] = new ExcelData(format.format(inv.getInvoiceDate().getNonConvertedDate()), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
                            invoice[x++] = new ExcelData(format.format(inv.getDueDate().getNonConvertedDate()), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
                            invoice[x++] = new ExcelData(inv.getInvoiceNumber(), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
                            for (; x < i + 2; x++) {
                                invoice[x] = new ExcelData("", ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
                            }
                            invoice[x++] = new ExcelData(inv.getAmount(), ExcelData.CURRENCY, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
                            for(; x < columnCount - 1; x++){
                                invoice[x] = new ExcelData("", ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
                            }
                            balance = balance.add(inv.getAmount());
                            total = total.add(inv.getAmount());
                            list.add(invoice);
                        }
                    }
                }
                balanceByColumn.put(i, getColumnBalance(i).add(balance));
            }
            balanceByColumn.put(columnCount - 1, getColumnBalance(columnCount - 1).add(total));
            ExcelData[] customerFooter = new ExcelData[columnCount+2];
            customerFooter[0] = ExcelData.getReportNameData(commonLocalizer.localize(PdfLocalizationName.total), 0, columnCount + 1);
            customerFooter[columnCount + 1] = new ExcelData(total, ExcelData.CURRENCY, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
            customerFooter[columnCount + 1].setBold(true);
            list.add(customerFooter);
        }

        list.add(new ExcelData[]{ExcelData.getReportNameData("", 0, columnCount + 2)});
        if (result != null && result.size() > 0) {
            ExcelData[] excelPercentDatas = new ExcelData[columnCount+2];
            excelPercentDatas[2] = new ExcelData(commonLocalizer.localize(PdfLocalizationName.ageingPercentage), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);
            excelPercentDatas[2].setBold(true);
            excelPercentDatas[2].setFontSize(13);

            ExcelData[] excelDatas = new ExcelData[columnCount+2];
            excelDatas[2] = new ExcelData(commonLocalizer.localize(PdfLocalizationName.total), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);
            excelDatas[2].setBold(true);
            excelDatas[2].setFontSize(13);

            for (i = 1; i < columnCount; i++) {
                excelDatas[i + 2] = new ExcelData(balanceByColumn.get(i), ExcelData.CURRENCY, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
                excelDatas[i + 2].setBold(true);
                excelDatas[i + 2].setFontSize(13);

                double d = 0d;

                if (balanceByColumn.get(columnCount - 1).compareTo(BigDecimal.ZERO) != 0) {
                    d = balanceByColumn.get(i).divide(balanceByColumn.get(columnCount - 1), RoundingMode.HALF_UP).doubleValue();
                }

                excelPercentDatas[i + 2] = new ExcelData(d, ExcelData.NUMBER_FORMAT_PERCENTAGE, 0, true, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
                excelPercentDatas[i + 2].setBold(true);
                excelPercentDatas[i + 2].setFontSize(13);
            }
            if (!Objects.equals(ServerSecurityContext.getInstance().getCompanyId(), "93943")) {
                list.add(excelPercentDatas);
            }
            list.add(excelDatas);
        } else {
            ExcelData[] excelDatas = new ExcelData[columnCount+2];
            excelDatas[2] = new ExcelData(commonLocalizer.localize(PdfLocalizationName.total) + " " + (type.equals(RECEIVABLE) ? commonLocalizer.localize(PdfLocalizationName.receivables) : commonLocalizer.localize(PdfLocalizationName.payable)), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);
            excelDatas[2].setBold(true);
            excelDatas[2].setFontSize(13);
            for (i = 1; i < columnCount; i++) {
                excelDatas[i + 2] = new ExcelData(BigDecimal.ZERO, ExcelData.CURRENCY, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
                excelDatas[i + 2].setBold(true);
                excelDatas[i + 2].setFontSize(13);
            }
            list.add(excelDatas);
        }
    }

    private void drawDetailTableForKendah(SimpleDateFormat format) {
        Integer startLimit, endLimit, in, startt;

        ExcelData[] headers = new ExcelData[columnCount + 3];
        headers[0] = new ExcelData(commonLocalizer.localize(PdfLocalizationName.invoiceDate), ExcelData.STRING, bCellSize, false, true, ExcelData.ALL_BORDER, ExcelData.HEADER_LEFT);
        headers[1] = new ExcelData(commonLocalizer.localize(PdfLocalizationName.dueDate), ExcelData.STRING, bCellSize, false, true, ExcelData.ALL_BORDER, ExcelData.HEADER_LEFT);
        headers[2] = new ExcelData(commonLocalizer.localize(PdfLocalizationName.reference), ExcelData.STRING, bCellSize, false, true, ExcelData.ALL_BORDER, ExcelData.HEADER_LEFT);
        headers[3] = new ExcelData(commonLocalizer.localize(PdfLocalizationName.poNumber), ExcelData.STRING, bCellSize, false, true, ExcelData.ALL_BORDER, ExcelData.HEADER_LEFT);

        headers[4] = new ExcelData(commonLocalizer.localize(PdfLocalizationName.current), ExcelData.STRING, bCellSize, false, true, ExcelData.ALL_BORDER, ExcelData.HEADER_RIGHT);
        int i = 5, j = 0;
        for (; i < columnCount + 2; i++, j++) {
            startLimit = j * interval + 1;
            endLimit = (j + 1) * interval;
            if (startLimit >= intervalLimit) {
                headers[i] = new ExcelData(" > " + intervalLimit, ExcelData.STRING, bCellSize, false, true, ExcelData.ALL_BORDER, ExcelData.HEADER_RIGHT);
            } else {
                if (endLimit >= intervalLimit) {
                    headers[i] = new ExcelData(startLimit + " - " + intervalLimit, ExcelData.STRING, bCellSize, false, true, ExcelData.ALL_BORDER, ExcelData.HEADER_RIGHT);
                } else {
                    headers[i] = new ExcelData(startLimit + " - " + endLimit, ExcelData.STRING, bCellSize, false, true, ExcelData.ALL_BORDER, ExcelData.HEADER_RIGHT);
                }
            }
        }
        headers[i] = new ExcelData(commonLocalizer.localize(PdfLocalizationName.total), ExcelData.STRING, bCellSize, false, true, ExcelData.ALL_BORDER, ExcelData.HEADER_RIGHT);
        list.add(headers);

        balanceByColumn = new HashMap<>();
        for (AgingSummaryItem item : result) {
            BigDecimal total = BigDecimal.ZERO;
            list.add(new ExcelData[]{ExcelData.getReportNameData("", 0, columnCount + 3)});
            list.add(new ExcelData[]{ExcelData.getReportNameData(item.getCustomerOrSupplier(), 0, columnCount + 3)});
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_ClIENT_OR_SUPPLIER_OWNER)) {
                String ownerName = accountingManager.getOwnerName(item.getCustomerOrSupplierObjectId());
                list.add(new ExcelData[]{ExcelData.getReportNameData(commonLocalizer.localize(PdfLocalizationName.owner) + ": " + ownerName, 0, columnCount + 2)});
            }

            for (i = 1, j = -1; i < columnCount - 1; i++, j++) {
                BigDecimal balance = BigDecimal.ZERO;
                startt = j * interval;
                in = (j + 1) * interval;

                if (item.getInvoiceList() != null && !item.getInvoiceList().isEmpty()) {
                    for (AgingSummaryInvoiceItem inv : item.getInvoiceList()) {
                        if (startt > intervalLimit) {
                            startt = intervalLimit;
                        }
                        if (in > intervalLimit) {
                            in = intervalLimit;
                        }
                        if ((inv.getAging() > startt && inv.getAging() <= in) || (inv.getAging() > intervalLimit && i == columnCount - 2) || (i == 1 && inv.getAging() == 0)) {
                            ExcelData[] invoice = new ExcelData[columnCount + 3];
                            int x = 0;
                            String poNumber = "";
                            if ("Invoice".equals(inv.getTypeName())) {
                                EdsInvoice edsInvoice = invoiceManager.get(inv.getObjectID());
                                if (edsInvoice != null && edsInvoice.getCustomFields() != null) {
                                    poNumber = invoiceManager.get(inv.getObjectID()).getCustomFields().getStringValue3();
                                }
                            }
                            invoice[x++] = new ExcelData(format.format(inv.getInvoiceDate().getNonConvertedDate()), ExcelData.STRING, 0, false, true, ExcelData.ALL_BORDER, ExcelData.NORMAL_RIGHT);
                            invoice[x++] = new ExcelData(format.format(inv.getDueDate().getNonConvertedDate()), ExcelData.STRING, 0, false, true, ExcelData.ALL_BORDER, ExcelData.NORMAL_RIGHT);
                            invoice[x++] = new ExcelData(inv.getInvoiceNumber(), ExcelData.STRING, 0, false, true, ExcelData.ALL_BORDER, ExcelData.NORMAL_RIGHT);
                            invoice[x++] = new ExcelData(poNumber, ExcelData.STRING, 0, false, true, ExcelData.ALL_BORDER, ExcelData.NORMAL_RIGHT);
                            for (; x < i + 3; x++) {
                                invoice[x] = new ExcelData("", ExcelData.STRING, 0, false, true, ExcelData.ALL_BORDER, ExcelData.NORMAL_RIGHT);
                            }
                            invoice[x++] = new ExcelData(inv.getAmount(), ExcelData.CURRENCY, 0, false, true, ExcelData.ALL_BORDER, ExcelData.NORMAL_RIGHT);
                            for (; x < columnCount - 1; x++) {
                                invoice[x] = new ExcelData("", ExcelData.STRING, 0, false, true, ExcelData.ALL_BORDER, ExcelData.NORMAL_RIGHT);
                            }
                            balance = balance.add(inv.getAmount());
                            total = total.add(inv.getAmount());
                            list.add(invoice);
                        }
                    }
                }
                balanceByColumn.put(i, getColumnBalance(i).add(balance));
            }
            balanceByColumn.put(columnCount - 1, getColumnBalance(columnCount - 1).add(total));
            ExcelData[] customerFooter = new ExcelData[columnCount + 3];
            customerFooter[0] = ExcelData.getReportNameData(commonLocalizer.localize(PdfLocalizationName.total), 0, columnCount + 1);
            customerFooter[columnCount + 2] = new ExcelData(total, ExcelData.CURRENCY, 0, false, true, ExcelData.ALL_BORDER, ExcelData.NORMAL_RIGHT);
            customerFooter[columnCount + 2].setBold(true);
            list.add(customerFooter);
        }

        list.add(new ExcelData[]{ExcelData.getReportNameData("", 0, columnCount + 3)});
        if (result != null && result.size() > 0) {
            ExcelData[] excelPercentDatas = new ExcelData[columnCount + 3];
            excelPercentDatas[3] = new ExcelData(commonLocalizer.localize(PdfLocalizationName.ageingPercentage), ExcelData.STRING, 0, false, true, ExcelData.ALL_BORDER, ExcelData.NORMAL_LEFT);
            excelPercentDatas[3].setBold(true);
            excelPercentDatas[3].setFontSize(13);

            ExcelData[] excelDatas = new ExcelData[columnCount + 3];
            excelDatas[3] = new ExcelData(commonLocalizer.localize(PdfLocalizationName.total), ExcelData.STRING, 0, false, true, ExcelData.ALL_BORDER, ExcelData.NORMAL_LEFT);
            excelDatas[3].setBold(true);
            excelDatas[3].setFontSize(13);

            for (i = 1; i < columnCount; i++) {
                excelDatas[i + 3] = new ExcelData(balanceByColumn.get(i), ExcelData.CURRENCY, 0, false, true, ExcelData.ALL_BORDER, ExcelData.NORMAL_RIGHT);
                excelDatas[i + 3].setBold(true);
                excelDatas[i + 3].setFontSize(13);

                double d = 0d;

                if (balanceByColumn.get(columnCount - 1).compareTo(BigDecimal.ZERO) != 0) {
                    d = balanceByColumn.get(i).divide(balanceByColumn.get(columnCount - 1), 4).doubleValue();
                }

                excelPercentDatas[i + 3] = new ExcelData(d, ExcelData.NUMBER_FORMAT_PERCENTAGE, 0, true, true, ExcelData.ALL_BORDER, ExcelData.NORMAL_RIGHT);
                excelPercentDatas[i + 3].setBold(true);
                excelPercentDatas[i + 3].setFontSize(13);
            }
            if (!Objects.equals(ServerSecurityContext.getInstance().getCompanyId(), "93943")) {
                list.add(excelPercentDatas);
            }
            list.add(excelDatas);
        } else {
            ExcelData[] excelDatas = new ExcelData[columnCount + 3];
            excelDatas[3] = new ExcelData(commonLocalizer.localize(PdfLocalizationName.total) + " " + (type.equals(RECEIVABLE) ? commonLocalizer.localize(PdfLocalizationName.receivables) : commonLocalizer.localize(PdfLocalizationName.payable)), ExcelData.STRING, 0, false, true, ExcelData.ALL_BORDER, ExcelData.NORMAL_LEFT);
            excelDatas[3].setBold(true);
            excelDatas[3].setFontSize(13);
            for (i = 1; i < columnCount; i++) {
                excelDatas[i + 3] = new ExcelData(BigDecimal.ZERO, ExcelData.CURRENCY, 0, false, true, ExcelData.ALL_BORDER, ExcelData.NORMAL_RIGHT);
                excelDatas[i + 3].setBold(true);
                excelDatas[i + 3].setFontSize(13);
            }
            list.add(excelDatas);
        }
    }

    private void setTableFooterWithColspan(String str, BigDecimal periodAmount) {
        ExcelData hdr = new ExcelData(str, ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        hdr.setBold(true);
        ExcelData emptyColumn = new ExcelData("", ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        ExcelData amount = new ExcelData(periodAmount, ExcelData.CURRENCY, 0, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
        ExcelData[] datas = new ExcelData[]{
                hdr, emptyColumn, emptyColumn, emptyColumn, emptyColumn, emptyColumn, emptyColumn, amount
        };
        list.add(datas);
        datas = new ExcelData[]{
                emptyColumn
        };
        list.add(datas);

        fullTotal = fullTotal.add(periodAmount);
    }

    private BigDecimal getColumnBalance(Integer columnIndex) {
        BigDecimal balance;
        if (balanceByColumn.get(columnIndex) == null) {
            balance = BigDecimal.ZERO;
        } else {
            balance = balanceByColumn.get(columnIndex);
        }
        return balance;
    }

    @Override
    protected void setFileName() {
        filename = "Aging Summary As Of_" + dateFormat(uploadManager.getUser().getUserDate());
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
