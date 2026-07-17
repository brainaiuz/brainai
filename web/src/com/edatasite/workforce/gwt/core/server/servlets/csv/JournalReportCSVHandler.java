package com.edatasite.workforce.gwt.core.server.servlets.csv;

import com.csvreader.CsvWriter;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.ListingResult;
import com.edatasite.workforce.gwt.accounting.client.rpc.Transaction;
import com.edatasite.workforce.gwt.accounting.client.rpc.TransactionItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.TransactionManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import org.gwtwidgets.server.spring.ServletUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by FARRUH ATABAEV on 20-Nov-14.
 */
public class JournalReportCSVHandler implements HttpRequestHandler {
    @Autowired
    private AccountingService accountingService;
    @Autowired
    private UserManager userManager;
    @Autowired
    private TransactionManager transactionManager;
    @Autowired
    @Qualifier("accountingLocalizer")
    protected WfmMessageSource accountingLocalizer;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);

    private static final SimpleDateFormat fpDateParseFormat = new SimpleDateFormat("ddMMyyyy HH:mm:ss");

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ListingFilterParameter fp = new ListingFilterParameter();
        String start = (request.getParameter("startDate_nc"));
        Date startDate = parseFilterParameterDate(start);
        String end = (request.getParameter("endDate_nc"));
        Date endDate = parseFilterParameterDate(end);
        fp.setFromExcelPDF(true);
        fp.setStartDate(startDate);
        fp.setEndDate(endDate);

        String orderby = (request.getParameter("sortField"));
        fp.setSortField(orderby);

        if (request.getParameter("departmentId") != null) {
            String departmentId = (request.getParameter("departmentId"));
            Integer departmentIdToInteger = Integer.parseInt(departmentId);
            fp.setDepartmentId(departmentIdToInteger);
        } else {
            fp.setDepartmentId(null);
        }
        String journalID = (request.getParameter("journalID"));
        fp.setJournalID(journalID != null ? Integer.valueOf(journalID) : null);
        String objectId = (request.getParameter("objectID"));
        fp.setObjectId(objectId != null ? Integer.valueOf(objectId) : null);

        EdsUser user = userManager.getUser();
        String[] companyDetails = accountingService.getCompanyDetails(user.getObjectID());
        String shortDateFormat = companyDetails[0];
        SimpleDateFormat format = new SimpleDateFormat(shortDateFormat != null
                                                       ? shortDateFormat
                                                       : "MMM dd yyyy", Locale.ENGLISH);

        String accountName = accountingLocalizer.localizeAccounting(PdfLocalizationName.account);
        String department = "Department";
        String debit = accountingLocalizer.localizeAccounting(PdfLocalizationName.debit);
        String credit = accountingLocalizer.localizeAccounting(PdfLocalizationName.credit);
        String from = accountingLocalizer.localizeAccounting(PdfLocalizationName.from);
        String to = accountingLocalizer.localizeAccounting(PdfLocalizationName.to);
        String journalReport = accountingLocalizer.localizeAccounting(PdfLocalizationName.journalReport);
        String figureIn = accountingLocalizer.localizeAccounting(PdfLocalizationName.figuresIn);

        CsvWriter csvWriter = createWriter(response, request, getFileName());

        fp.setLimit(5000);
        fp.setStart(0 * 5000);

        ListingResult<Transaction> transactions = accountingService.getJournalReportWithPaging(new DateNonConvertable(startDate), new DateNonConvertable(endDate), orderby, fp.getJournalID(), fp);

        if (transactions != null) {
            try {
                //header
                String date = from + " " + format.format(startDate) + " " + to + " " + format.format(endDate);

                String currencySymbol = companyDetails[1];
                String currencyCode = companyDetails[2];
                currencySymbol = currencySymbol != null ? currencySymbol : "";

                if (0 == 0) {
                    csvWriter.write("");
                    csvWriter.endRecord();

                    csvWriter.write("");
                    csvWriter.write(journalReport);
                    csvWriter.endRecord();

                    csvWriter.write("");
                    csvWriter.write(companyDetails[3]);
                    csvWriter.endRecord();

                    csvWriter.write("");
                    csvWriter.write(date);
                    csvWriter.endRecord();

                    csvWriter.write("");
                    csvWriter.write(figureIn + " " + currencySymbol + "(" + currencyCode + ")");
                    csvWriter.endRecord();

                }

                Integer calculationScale = getCalculationScale(companyDetails[4]);
                for (Transaction transaction : transactions.getList()) {
                    String id = transaction.getJournalId() != null
                                ? "ID " + String.valueOf(transaction.getJournalId())
                                : "";
                    String type = " " + transaction.getJournalName();
                    String postedDate = transaction.getPostedDate() != null
                                        ? dateFormat.format(transaction.getPostedDate().getNonConvertedDate())
                                        : "";
                    String poster = accountingLocalizer.localizeWithParamAccounting(PdfLocalizationName.postedByOn, transaction.getPostedBy(), postedDate);
                    String journalDate = transaction.getJournalDate() != null
                                         ? dateFormat.format(transaction.getJournalDate().getNonConvertedDate())
                                         : "";
                    String tableName = id + type + poster;

                    csvWriter.write(tableName);
                    csvWriter.write("");
                    csvWriter.write(journalDate);
                    csvWriter.endRecord();

                    csvWriter.write(accountName);
                    csvWriter.write(department);
                    csvWriter.write(debit);
                    csvWriter.write(credit);
                    csvWriter.endRecord();

                    for (TransactionItem transactionItem : transaction.getTransactionItems()) {
                        String name = transactionItem.getAccountName() + "(" + transactionItem.getAccountCode() + ")";
                        BigDecimal debitCellData = new BigDecimal("0.00");
                        BigDecimal creditCellData = new BigDecimal("0.00");
                        if (transactionItem.getDebit() != null) {
                            debitCellData = transactionItem.getDebit().setScale(calculationScale, BigDecimal.ROUND_HALF_UP);
                        }
                        if (transactionItem.getCredit() != null) {
                            creditCellData = transactionItem.getCredit().setScale(calculationScale, BigDecimal.ROUND_HALF_UP);
                        }
                        csvWriter.write(name);
                        csvWriter.write("");
                        csvWriter.write(debitCellData.toString());
                        csvWriter.write(creditCellData.toString());
                        csvWriter.endRecord();
                    }

                    BigDecimal total_debit = transaction.getTotalDebit() != null
                                             ? transaction.getTotalDebit().setScale(calculationScale, BigDecimal.ROUND_HALF_UP)
                                             : new BigDecimal("0.00");
                    BigDecimal total_credit = transaction.getTotalCredit() != null
                                              ? transaction.getTotalCredit().setScale(calculationScale, BigDecimal.ROUND_HALF_UP)
                                              : new BigDecimal("0.00");

                    String totalDebit = total_debit.toString();
                    String totalCredit = total_credit.toString();
                    csvWriter.write("");
                    csvWriter.write("");
                    csvWriter.write(totalDebit);
                    csvWriter.write(totalCredit);
                    csvWriter.endRecord();
                    csvWriter.write("");
                    csvWriter.endRecord();
                    csvWriter.write("");
                    csvWriter.endRecord();

                    csvWriter.flush();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        closeFile(csvWriter);
    }

    public Integer getCalculationScale(String calculationScale) {
        if (calculationScale != null && !calculationScale.equals("") && Integer.valueOf(calculationScale) > 0) {
            return Integer.valueOf(calculationScale);
        } else {
            return 2;
        }
    }

    public Date parseFilterParameterDate(String dateAsString) {
        try {
            return (dateAsString != null && !dateAsString.trim().isEmpty())
                   ? fpDateParseFormat.parse(dateAsString)
                   : null;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private CsvWriter createWriter(HttpServletResponse response, HttpServletRequest request, String filename) throws IOException {
        response.setHeader("Content-Type", "text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        response.addHeader("Cache-Control", "no-cache");
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Expires", "0");

//        ServletUtils.setRequest(request);
        ServletUtils.setResponse(response);

        return new CsvWriter(response.getOutputStream(), ',', Charset.forName("UTF-8"));
    }

    private void closeFile(CsvWriter csvWriter) {
        csvWriter.flush();
        csvWriter.close();
    }

    String getFileName() {
        EdsUser user = userManager.getUser();
        return user.getCompany().getName() + "_" + "Journal_Report.csv";
    }
}
