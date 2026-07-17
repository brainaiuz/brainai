package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.client.client.rpc.CrmAccountBalance;
import com.edatasite.workforce.gwt.client.client.rpc.CrmAccountBalanceItem;
import com.edatasite.workforce.gwt.client.client.rpc.CrmAccountCurrencyBalance;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Atabek Boboyev
 * Date: 19.03.12
 * Time: 19:41
 * To change this template use File | Settings | File Templates.
 */
public class CrmAccountBalanceExcelHandler extends BaseExcelHandler {
    private final int aCellSize = 23;
    private final int bCellSize = 35;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    private List<ExcelData[]> list;
    private final int cCellSize = 20;
    private final int dCellSize = 20;
    @Autowired
    @Qualifier("accountingLocalizer")
    protected WfmMessageSource accountingLocalizer;
    @Autowired
    private ClientService clientService;

    @Override
    protected void setFileName() {
        filename = "";
    }

    @Override
    protected void setFileName(Object dataClass) {
        ListingFilterParameter filterParameter = (ListingFilterParameter) dataClass;
        filename = CrmAccountItem.CUSTOMER.equals(filterParameter.getAccountType()) ? "Customer_Balance" : "Supplier_Balance";
    }

    @Override
    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter fp = (ListingFilterParameter) object;
        EdsUser user = crmAccountManager.getUser();
        EdsCompany company = crmAccountManager.getUser().getCompany();
        DateFormat dateFormat = getCompanyShortDateFormat(company);

        Date fromDate = parseFilterParameterDate(fp.getStartDateNC());
        Date toDate = parseFilterParameterDate(fp.getEndDateNC());
        CrmAccountBalance balance = clientService.getCrmAccountBalanceReport(new DateNonConvertable(fromDate), new DateNonConvertable(toDate), fp);

        list = new LinkedList<>();

        String accountTitle;
        if (CrmAccountItem.CUSTOMER.equals(fp.getAccountType())) {
            accountTitle = commonLocalizer.localize(PdfLocalizationName.customerBalance) + " - " + balance.getCrmAccountItem().getName();
        } else {
            accountTitle = commonLocalizer.localize(PdfLocalizationName.supplierBalance) + " - " + balance.getCrmAccountItem().getName();
        }
        int lastColumnIndex = fp.isShowSubAccountTransaction() ? 7 : 6;
        list.add(new ExcelData[]{
                new ExcelData("", ExcelData.STRING, aCellSize, lastColumnIndex)
        });


        ExcelData titleData = ExcelData.getReportNameData(accountTitle, aCellSize, lastColumnIndex);
        ExcelData dateData = null;
        ExcelData companyData = ExcelData.getReportNameChildDataWithOutBorderInStart(user.getCompany().getName(), aCellSize, lastColumnIndex);
        if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
            dateData = ExcelData.getReportNameChildDataWithOutBorderInStart(ServerUtils.convertToUzbDateFormat(ServerUtils.dateFormat(fromDate, "dd MMM, yyyy")) + " dan " + " " + ServerUtils.convertToUzbDateFormat(ServerUtils.dateFormat(toDate, "dd MMM, yyyy")) + " gacha", aCellSize, lastColumnIndex);

        } else {
            dateData = ExcelData.getReportNameChildDataWithOutBorderInStart(commonLocalizer.localize("from") + " "
                    + ServerUtils.dateFormat(fromDate, "dd MMM, yyyy") + " " + accountingLocalizer.localize("to") + " " + ServerUtils.dateFormat(toDate, "dd MMM, yyyy"), aCellSize, lastColumnIndex);
        }

        list.add(new ExcelData[]{
                titleData
        });
        list.add(new ExcelData[]{
                companyData
        });
        list.add(new ExcelData[]{
                dateData
        });


        ExcelData emptyData = new ExcelData("", ExcelData.STRING, aCellSize, lastColumnIndex);
        ExcelData[] cellEmptyHeader = new ExcelData[]{
                emptyData
        };
        list.add(cellEmptyHeader);


        ExcelData dateData1 = new ExcelData(accountingLocalizer.localize("date", "Date"), ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
        ExcelData customerSupplierData = new ExcelData(CrmAccountItem.CUSTOMER.equals(fp.getAccountType()) ? accountingLocalizer.localize("customer", "Customer") : accountingLocalizer.localize("supplier", "Supplier"), ExcelData.STRING, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
        ExcelData transactionData = new ExcelData(accountingLocalizer.localize("transaction", "Transaction"), ExcelData.STRING, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
        ExcelData referenceData = new ExcelData(accountingLocalizer.localize("reference", "Reference"), ExcelData.STRING, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
        ExcelData debitData = new ExcelData(accountingLocalizer.localize("debit", "Debit"), ExcelData.STRING, cCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
        ExcelData creditData = new ExcelData(accountingLocalizer.localize("credit", "Credit"), ExcelData.STRING, dCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
        ExcelData balancetData = new ExcelData(accountingLocalizer.localize("balance", "Balance"), ExcelData.STRING, dCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);

        ExcelData[] cellHeader = new ExcelData[]{dateData1, transactionData, referenceData, debitData, creditData, balancetData};
        if (fp.isShowSubAccountTransaction()) {
            cellHeader = new ExcelData[]{dateData1, customerSupplierData, transactionData, referenceData, debitData, creditData, balancetData};
        }

        ArrayList<CrmAccountCurrencyBalance> currencyBalanceList = balance.getCurrencyBalances();

        for (CrmAccountCurrencyBalance currencyBalance : currencyBalanceList) {

            list.add(cellEmptyHeader);

            list.add(cellHeader);
            ExcelData currencyData = ExcelData.getReportNameChildData(commonLocalizer.localize("figuresIn", "Figures in") + " " + (currencyBalance.getCurrency().getSymbol() != null ? currencyBalance.getCurrency().getSymbol() : "") + "(" + currencyBalance.getCurrency().getName() + ")", aCellSize, lastColumnIndex);

            list.add(new ExcelData[]{
                    currencyData
            });

            CrmAccountBalanceItem[] items = currencyBalance.getItems();

            ExcelData beginData = new ExcelData(accountingLocalizer.localize("beginningBalance", "Beginning Balance"), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT);
            ExcelData beginBalanceLabel = new ExcelData(currencyBalance.getEarlyBalance(), ExcelData.CURRENCY, 0, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
            beginBalanceLabel.setBold(true);
            ExcelData[] cellBegin = new ExcelData[]{
                    beginData, emptyData, emptyData, emptyData, emptyData, beginBalanceLabel
            };
            if (fp.isShowSubAccountTransaction()) {
                cellBegin = new ExcelData[]{
                        beginData, emptyData, emptyData, emptyData, emptyData, emptyData, beginBalanceLabel
                };
            }
            list.add(cellBegin);

            for (CrmAccountBalanceItem item : items) {
                ExcelData userDateCell = null;

                if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                    userDateCell = new ExcelData(ServerUtils.convertToUzbDateFormat(ServerUtils.dateFormat(item.getDate_nc().getNonConvertedDate(), "dd MMM, yyyy")), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);
                } else {
                    userDateCell = new ExcelData(ServerUtils.dateFormat(item.getDate_nc().getNonConvertedDate(), "dd MMM, yyyy"), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);
                }
                ExcelData cusSupLabelCell = new ExcelData(item.getClientSupplierName() != null ? item.getClientSupplierName() : "", ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);
                ExcelData transactionLabelCell = new ExcelData(item.getTransactionLabel() != null ? item.getTransactionLabel() : "", ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);
                ExcelData referenceCell = new ExcelData(item.getReference() != null ? item.getReference() : "", ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);
                ExcelData debitCell = new ExcelData(item.getDebit() != null && item.getDebit().compareTo(BigDecimal.ZERO) > 0 ? item.getDebit() : 0, ExcelData.CURRENCY, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
                ExcelData creditCell = new ExcelData(item.getCredit() != null && item.getCredit().compareTo(BigDecimal.ZERO) > 0 ? item.getCredit() : 0, ExcelData.CURRENCY, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
                ExcelData balanceCell = new ExcelData(item.getBalance(), ExcelData.CURRENCY, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
                ExcelData[] cellBody = new ExcelData[]{
                        userDateCell, transactionLabelCell, referenceCell, debitCell, creditCell, balanceCell
                };
                if (fp.isShowSubAccountTransaction()) {
                    cellBody = new ExcelData[]{
                            userDateCell, cusSupLabelCell, transactionLabelCell, referenceCell, debitCell, creditCell, balanceCell
                    };
                }
                list.add(cellBody);
            }
            ExcelData totalData = new ExcelData(accountingLocalizer.localize("endingBalance", "Ending Balance"), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT);
            ExcelData totalAmountLabel = new ExcelData(currencyBalance.getEndingBalance(), ExcelData.CURRENCY, 0, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
            ExcelData[] cellTotal = new ExcelData[]{
                    totalData, emptyData, emptyData, emptyData, emptyData, totalAmountLabel
            };
            if (fp.isShowSubAccountTransaction()) {
                cellTotal = new ExcelData[]{
                        totalData, emptyData, emptyData, emptyData, emptyData, emptyData, totalAmountLabel
                };
            }

            list.add(cellTotal);
        }
        HSSFWorkbook wb = new WorkBook(list).getWorkBook(filename, 0, 0, 0, lastColumnIndex - 1);
        // Set the columns to repeat from column 0 to 2 on the first sheet
        wb.setRepeatingRowsAndColumns(0, 0, lastColumnIndex, 0, 6);

        return wb;
    }

    private SimpleDateFormat getCompanyShortDateFormat(EdsCompany company) {
        SimpleDateFormat shortDateFormat;
        if (company.getCompanySettings() != null && company.getCompanySettings().getShortDateFormat() != null) {
            shortDateFormat = new SimpleDateFormat(company.getCompanySettings().getShortDateFormat());
        } else {
            shortDateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        }
        return shortDateFormat;
    }

    private String getValueAsString(BigDecimal value, DecimalFormat numberFormat) {
        return value.compareTo(AccountingConstants.ZERO) >= 0 ? numberFormat.format(value) : "(" + numberFormat.format(value.abs()) + ")";
    }
}
