package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.*;
import com.edatasite.workforce.gwt.invoice.client.rpc.AgingSummaryInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.AgingSummaryItem;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 8/13/12
 * Time: 12:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class AgingSummaryPdfHandler extends AbstractITextPostPdfHandler {
    private Integer columnCount;

    @Autowired
    private InvoiceServiceLocal invoiceServiceLocal;
    @Autowired
    private PropertManager propertManager;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    public GenericSettingsManager genericSettingsManager;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParameter = (ListingFilterParameter) dataClass;
        Integer interval = filterParameter.getInterval();
        Integer intervalLimit = filterParameter.getIntervalLimit();
        String type = filterParameter.getAccountType();
        boolean detailView = filterParameter.isShowBudget();
        Date startDate = parseFilterParameterDate(filterParameter.getStartDateNC());

        SimpleDateFormat format;
        if (company.getCompanySettings() != null && StringUtils.isNotEmpty(company.getCompanySettings().getShortDateFormat())) {
            format = new SimpleDateFormat(company.getCompanySettings().getShortDateFormat(), Locale.ENGLISH);
        } else {
            format = new SimpleDateFormat("MMM d yyyy", Locale.ENGLISH);
        }

        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
            pdfData.setCurrentDate(ServerUtils.convertToUzbDateFormat(format.format(startDate)) + "  " + commonLocalizer.localize(PdfLocalizationName.asOF));
        } else {
            pdfData.setCurrentDate(commonLocalizer.localize(PdfLocalizationName.asOF) + " " + format.format(startDate));
        }
        ITextCompanyData companyData = new ITextCompanyData();
        companyData.setCompanyName(escapeHtml(company.getName()));
        pdfData.setCompanyData(companyData);

        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        String currencySymbol = fs.getCurrency().getSymbol();
        String currencyCode = fs.getCurrency().getName();
        currencySymbol = currencySymbol != null ? currencySymbol : "";
        pdfData.setExtraData(StringUtils.join(commonLocalizer.localize(PdfLocalizationName.figuresIn), " ", currencyCode));

        pdfData.setListTable(drawHeader(interval, intervalLimit, type, detailView));

        DecimalFormat priceScaleFormat = getPriceScaleNumberFormat(fs);
        ListResult<AgingSummaryItem> items = invoiceServiceLocal.getOverdueInvoiceByCrmAccount(filterParameter);
        if (detailView) {
            pdfData.setCustomData(getDetailRows(items.getList(), interval, intervalLimit, type, format, priceScaleFormat));
        } else {
            HashMap<String, CustomisedITextTable> customData = new LinkedHashMap<>();
            CustomisedITextTable table = getRows(items.getList(), interval, intervalLimit, type, priceScaleFormat);
            if(table != null){
                customData.put("CUSTOM_DATA", table);
            }
            pdfData.setCustomData(customData);
        }

        return pdfData;
    }

    private ITextTableList drawHeader(final Integer days, final Integer limit, String type, boolean detailView) {
        if (limit % days == 0) {
            columnCount = limit / days + 4;
        } else {
            columnCount = limit / days + 5;
        }
        ITextTableList table = new ITextTableList(columnCount);
        if (detailView) {
            table.addPdfTableHeader(commonLocalizer.localize(PdfLocalizationName.invoiceDate));
            table.addPdfTableHeader(commonLocalizer.localize(PdfLocalizationName.dueDate));
            table.addPdfTableHeader(commonLocalizer.localize(PdfLocalizationName.reference));
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_REFERENCE_OR_PONUMBER_AGED_RECEIVABLES_PDF)) {
                table.addPdfTableHeader(commonLocalizer.localize(PdfLocalizationName.poNumber));
            }
        } else {
            table.addPdfTableHeader(commonLocalizer.localize(RECEIVABLE.equals(type) ? PdfLocalizationName.customer : PdfLocalizationName.supplier));
        }
        table.addPdfTableHeader(commonLocalizer.localize(PdfLocalizationName.current));

        Integer i, j, startLimit, endLimit;
        for (i = 1, j = 0; i < columnCount - 2; i++, j++) {
            startLimit = j * days + 1;
            endLimit = (j + 1) * days;

            if (startLimit > limit) {
                table.addPdfTableHeader(" > " + limit);
            } else {
                if (endLimit >= limit) {
                    table.addPdfTableHeader(startLimit + " - " + limit);
                } else {
                    table.addPdfTableHeader(startLimit + " - " + endLimit);
                }
            }
        }
        table.addPdfTableHeader(commonLocalizer.localize(PdfLocalizationName.total));

        return table;
    }

    private HashMap<String, CustomisedITextTable> getDetailRows(List<AgingSummaryItem> items, Integer interval, Integer intervalLimit, String type, SimpleDateFormat dateFormat, DecimalFormat format) {
        if (CollectionUtils.isNotEmpty(items)) {
            int tableCount = 0;
            HashMap<String, CustomisedITextTable> customData = new LinkedHashMap<>();
            HashMap<Integer, BigDecimal> balanceByColumn = new LinkedHashMap<>();

            for (AgingSummaryItem item : items) {
                int rowCount = 0;
                CustomisedITextTable table = new CustomisedITextTable();
                BigDecimal total = BigDecimal.ZERO;

                table.setName(StringUtils.isNotEmpty(item.getCustomerOrSupplier()) ? item.getCustomerOrSupplier() : commonLocalizer.localize(PdfLocalizationName.na));

                for (int i = 1, j = -1; i < columnCount - 1; i++, j++) {
                    BigDecimal balance = BigDecimal.ZERO;
                    Integer start = j * interval;
                    Integer in = (j + 1) * interval;

                    if (CollectionUtils.isNotEmpty(item.getInvoiceList()))
                        for (AgingSummaryInvoiceItem inv : item.getInvoiceList()) {
                            if (start > intervalLimit) {
                                start = intervalLimit;
                            }
                            if (in > intervalLimit) {
                                in = intervalLimit;
                            }
                            if ((inv.getAging() > j * interval && inv.getAging() <= in) || (inv.getAging() > intervalLimit && i == columnCount - 2) || (i == 1 && inv.getAging() == 0)) {
                                LinkedList<String> rows = new LinkedList<>();
                                rows.add(dateFormat.format(inv.getInvoiceDate().getNonConvertedDate()));
                                rows.add(dateFormat.format(inv.getDueDate().getNonConvertedDate()));
                                rows.add(inv.getInvoiceNumber());
                                if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_REFERENCE_OR_PONUMBER_AGED_RECEIVABLES_PDF)) {
                                    EdsInvoice invoice = invoiceManager.get(inv.getObjectID());
                                    rows.add(invoice != null ? invoice.getReference() : "");
                                }

                                for (int x = 0; x < i - 1; x++) {
                                    rows.add("");
                                }

                                rows.add(getValueAsString(inv.getAmount(), format));

                                for (int x = i; x < columnCount - 1; x++) {
                                    rows.add("");
                                }

                                total = total.add(inv.getAmount());
                                balance = balance.add(inv.getAmount());
                                table.addTotalRow("" + rowCount++, rows);
                            }
                        }
                    if (balanceByColumn.get(i) == null) {
                        balanceByColumn.put(i, new BigDecimal("0.00").add(balance));
                    } else {
                        balanceByColumn.put(i, balanceByColumn.get(i).add(balance));
                    }
                }

                if (balanceByColumn.get(columnCount - 1) == null) {
                    balanceByColumn.put(columnCount - 1, new BigDecimal("0.00").add(total));
                } else {
                    balanceByColumn.put(columnCount - 1, balanceByColumn.get(columnCount - 1).add(total));
                }
                table.addColumn(PDFConstants.COLUMN_NAME, commonLocalizer.localize(PdfLocalizationName.total));
                table.addColumn(PDFConstants.COLUMN_VALUE, getValueAsString(total, format));
                customData.put("" + tableCount++, table);
            }

            CustomisedITextTable percentageTable = new CustomisedITextTable();
            percentageTable.setName(commonLocalizer.localize(PdfLocalizationName.agingPercentage));
            LinkedList<String> percentageRows = new LinkedList<>();

            CustomisedITextTable totalTable = new CustomisedITextTable();
            totalTable.setName(commonLocalizer.localize(type.equals(RECEIVABLE) ? PdfLocalizationName.totalReceivables : PdfLocalizationName.totalPayables));
            LinkedList<String> totalRows = new LinkedList<>();
            for (int i = 1; i < columnCount; i++) {
                String percentage = "";
                if (balanceByColumn.get(columnCount - 1).compareTo(BigDecimal.ZERO) == 0) {
                    percentage = "0.00 %";
                } else {
                    percentage = getValueAsString(balanceByColumn.get(i).divide(balanceByColumn.get(columnCount - 1), 4).multiply(new BigDecimal(100)), format) + " %";
                }
                percentageRows.add(percentage);
                totalRows.add(getValueAsString(balanceByColumn.get(i), format));
            }
            if (!Objects.equals(ServerSecurityContext.getInstance().getCompanyId(), "93943")) {
                percentageTable.addTotalRow("PERCENTAGE_TOTAL", percentageRows);
                customData.put("PERCENTAGE_TOTAL", percentageTable);
            }
            totalTable.addTotalRow("AGING_TOTAL", totalRows);

            customData.put("AGING_TOTAL", totalTable);

            return customData;
        }
        return null;
    }

    public CustomisedITextTable getRows(List<AgingSummaryItem> items, Integer interval, Integer intervalLimit, String type, DecimalFormat format) {
        if (CollectionUtils.isNotEmpty(items)) {
            CustomisedITextTable table = new CustomisedITextTable();
            table.setName(type.equals(RECEIVABLE) ? commonLocalizer.localize(PdfLocalizationName.receivables) : commonLocalizer.localize(PdfLocalizationName.payable));

            HashMap<Integer, BigDecimal> balanceByColumn = new LinkedHashMap<>();
            int rowCount = 0;
            for (AgingSummaryItem item : items) {
                BigDecimal total = new BigDecimal("0.00");

                LinkedList<String> rows = new LinkedList<>();
                rows.add(StringUtils.isNotEmpty(item.getCustomerOrSupplier()) ? item.getCustomerOrSupplier() : commonLocalizer.localize(PdfLocalizationName.na));
                for (int i = 1, j = -1; i < columnCount - 1; i++, j++) {
                    BigDecimal balance = new BigDecimal("0.00");
                    Integer start = j * interval;
                    Integer in = (j + 1) * interval;

                    if (CollectionUtils.isNotEmpty(item.getInvoiceList()))
                        for (AgingSummaryInvoiceItem inv : item.getInvoiceList()) {
                            if (start > intervalLimit) {
                                start = intervalLimit;
                            }
                            if (in > intervalLimit) {
                                in = intervalLimit;
                            }
                            if ((inv.getAging() > j * interval && inv.getAging() <= in) || (inv.getAging() > intervalLimit && i == columnCount - 2) || (i == 1 && inv.getAging() == 0)) {
                                balance = balance.add(inv.getAmount());
                            }
                        }
                    if (balanceByColumn.get(i) == null) {
                        balanceByColumn.put(i, new BigDecimal("0.00").add(balance));
                    } else {
                        balanceByColumn.put(i, balanceByColumn.get(i).add(balance));
                    }
                    total = total.add(balance);
                    rows.add(getValueAsString(balance, format));
                }

                if (total.compareTo(new BigDecimal("0.00")) != 0) {
                    if (balanceByColumn.get(columnCount - 1) == null) {
                        balanceByColumn.put(columnCount - 1, new BigDecimal("0.00").add(total));
                    } else {
                        balanceByColumn.put(columnCount - 1, balanceByColumn.get(columnCount - 1).add(total));
                    }
                }
                rows.add(getValueAsString(total, format));
                table.addTotalRow("" + rowCount++, rows);
            }

            LinkedList<String> percentageRows = new LinkedList<>();
            percentageRows.add(commonLocalizer.localize(PdfLocalizationName.agingPercentage));

            LinkedList<String> totalRows = new LinkedList<>();
            totalRows.add(commonLocalizer.localize(type.equals(RECEIVABLE) ? PdfLocalizationName.totalReceivables : PdfLocalizationName.totalPayables));
            for (int i = 1; i < columnCount; i++) {
                String percentage = "";
                if (balanceByColumn.get(columnCount - 1).compareTo(BigDecimal.ZERO) == 0) {
                    percentage = "0.00 %";
                } else {
                    percentage = getValueAsString(balanceByColumn.get(i).divide(balanceByColumn.get(columnCount - 1), 4).multiply(new BigDecimal(100)), format) + " %";
                }
                percentageRows.add(percentage);
                totalRows.add(getValueAsString(balanceByColumn.get(i), format));
            }
            if (!Objects.equals(ServerSecurityContext.getInstance().getCompanyId(), "93943")) {
                table.addTotalRow("PERCENTAGE_TOTAL", percentageRows);
            }
            table.addTotalRow("AGING_TOTAL", totalRows);

            return table;
        }
        return null;
    }

    private String getValueAsString(BigDecimal value, DecimalFormat format) {
        if (value.compareTo(BigDecimal.ZERO) >= 0) {
            return format.format(value);
        } else {
            return "(" + format.format(value.abs()) + ")";
        }
    }

    @Override
    protected Integer getCustomisedPDFTemplateId(Object object) {
        if (object instanceof ListingFilterParameter) {
            return ((ListingFilterParameter) object).getTemplateID();
        }
        return null;
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter filterParameter = (ListingFilterParameter) dataClass;
        EdsProperty propertyReceivable = propertManager.findByCode("arAgingSummary");
        EdsProperty propertyPayable = propertManager.findByCode("apAgingSummary");
        String agedReceivableAndPayable = "";
        if (RECEIVABLE.equals(filterParameter.getAccountType())) {
            agedReceivableAndPayable = propertyReceivable != null && propertyReceivable.getPlural() != null ? propertyReceivable.getPlural() : commonLocalizer.localize(PdfLocalizationName.arAgingSummary1);
        } else {
            agedReceivableAndPayable = propertyPayable != null && propertyPayable.getPlural() != null ? propertyPayable.getPlural() : commonLocalizer.localize(PdfLocalizationName.apAgingSummary1);
        }

        return agedReceivableAndPayable;
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.AGING_SUMMARY;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName("AgingSummaryReport_AsOf_" + dateFormat(user.getUserDate()));
    }

    @Override
    protected PdfParams.Orientation getOrientation(Object dataClass) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        return filterParametrs.isLandscape() ? PdfParams.Orientation.landscape : null;
    }
}
