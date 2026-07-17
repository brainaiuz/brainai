package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.BankAccount;
import com.edatasite.workforce.gwt.accounting.client.rpc.BankCheckData;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Mirjalol
 * Date: 30.11.12
 * Time: 11:16
 * To change this template use File | Settings | File Templates.
 */
public class BankCheckListExcelHandler extends BaseExcelHandler {

    private static final Logger log = LoggerFactory.getLogger(BankCheckListExcelHandler.class);

    @Autowired
    private AccountingService accountingService;
    @Autowired
    private PropertManager propertManager;
    private String sheetName;
    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;

    @Override
    protected void setFileName() {
        filename = "Bank Cheques";
    }

    @Override
    protected HSSFWorkbook getWorkBook(Object object) {
        EdsCompany company = userManager.getUser().getCompany();
        EdsCompanySettings companySettings = company.getCompanySettings();
        String shortDateFormat = "MMM dd, yyyy";
        if (companySettings != null) {
            shortDateFormat = companySettings.getShortDateFormat();
        }
        ListingFilterParameter filterParameter = (ListingFilterParameter) object;
        EdsProperty property = propertManager.findByCode(filterParameter.getPropertyCode());
        sheetName = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.writeChecks);
        filterParameter.setLimit(1000);
        ListResult<BankCheckData> solutionList = accountingService.getBankCheckList(filterParameter);
        List<BankCheckData> holListItem = solutionList.getList();
        EdsUser user = userManager.getUser();
        ExcelData[] cellDatas;
        ListPanelToolRpc panelTools = filterParameter.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        header.remove(BankCheckData.ACTION);
        Map<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(BankCheckData.NUMBER, commonLocalizer.localize(PdfLocalizationName.number));
        mapColumnHeader.put(BankCheckData.BANK_ACCOUNT, commonLocalizer.localize(PdfLocalizationName.bank));
        mapColumnHeader.put(BankCheckData.PAY_TO, commonLocalizer.localize(PdfLocalizationName.payTo));
        mapColumnHeader.put(BankCheckData.DATE, commonLocalizer.localize(PdfLocalizationName.date));
        mapColumnHeader.put(BankCheckData.AMOUNT, commonLocalizer.localize(PdfLocalizationName.amount));
        mapColumnHeader.put(BankCheckData.ADDRESS, commonLocalizer.localize(PdfLocalizationName.address));
        mapColumnHeader.put(BankCheckData.MEMO, commonLocalizer.localize(PdfLocalizationName.description));
        mapColumnHeader.put(BankCheckData.STATUS, commonLocalizer.localize(PdfLocalizationName.status));
        mapColumnHeader.put(BankCheckData.CREATOR, commonLocalizer.localize(PdfLocalizationName.createdBy));
        mapColumnHeader.put(BankCheckData.PROJECT, commonLocalizer.localize(PdfLocalizationName.project));

        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);
            List<ExcelData[]> list = new LinkedList<>();
            cellDatas = new ExcelData[header.size()];

            list.add(generateOneRowWithValue(header.size() + 1, user.getCompany().getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(header.size() + 1, sheetName, workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(header.size() + 1, excelReferenceMessageSource.localize("EPAsOf", " As Of") + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            for (int i = 0; i < header.size(); i++) {
                cellDatas[i] = new ExcelData(mapColumnHeader.get(header.get(i)), ExcelData.STRING, header.get(i).equals(BankCheckData.ADDRESS) ? 50 : 20, true, header.get(i).equals(BankCheckData.ADDRESS), ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            list.add(cellDatas);

            for (BankCheckData checkData : holListItem) {
                String temp = "";
                cellDatas = new ExcelData[header.size()];
                for (int j = 0; j < header.size(); j++) {
                    temp = "";
                    if (BankCheckData.NUMBER.equals(header.get(j))) {
//                        temp = checkData.getNumberData() == null ? "" : checkData.getNumberData().getNumberString();
                        cellDatas[j] = new ExcelData(checkData.getNumberData() == null ? "" : checkData.getNumberData().getNumberString(), ExcelData.STRING, 12, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        continue;
                    } else if (BankCheckData.BANK_ACCOUNT.equals(header.get(j))) {
                        temp = (checkData.getBankAccount() == null || checkData.getBankAccount().getName() == null) ? "" : checkData.getBankAccount().getName();
                    } else if (BankCheckData.PAY_TO.equals(header.get(j))) {
                        temp = checkData.getPayTo() == null ? "" : checkData.getPayTo();
                    } else if (BankCheckData.DATE.equals(header.get(j))) {
//                        temp = checkData.getDate() == null ? "" : simpleDateFormat.format(checkData.getDate().getNonConvertedDate());
                        cellDatas[j] = new ExcelData(ServerUtils.dateFormat(checkData.getDate() != null ? checkData.getDate().getNonConvertedDate() : null, shortDateFormat), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        continue;
                    } else if (BankCheckData.AMOUNT.equals(header.get(j))) {
//                        temp = checkData.getAmount() == null ? "" : decimalFormat.format(checkData.getAmount());
                        cellDatas[j] = new ExcelData(checkData.getAmount(), ExcelData.CURRENCY, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        continue;
                    } else if (BankCheckData.ADDRESS.equals(header.get(j))) {
                        temp = checkData.getAddress() == null ? "" : "" + checkData.getAddress();
                    } else if (BankCheckData.MEMO.equals(header.get(j))) {
                        temp = checkData.getMemo() == null ? "" : "" + checkData.getMemo();
                    } else if (BankCheckData.STATUS.equals(header.get(j))) {
                        temp = checkData.isPostDatedTransaction() ? accountingLocalizer.localizeAccounting(PdfLocalizationName.postedDate) : "Posted";
                    } else if (BankCheckData.CREATOR.equals(header.get(j))) {
                        temp = (checkData.getCreator() == null) ? "" : "" + checkData.getCreator();
                    } else if (BankCheckData.PROJECT.equals(header.get(j))) {
                        temp = (checkData.getProject() == null) ? "" : "" + checkData.getProject();
                    }
                    cellDatas[j] = new ExcelData(temp, ExcelData.STRING, 25, false, header.get(j).equals(BankAccount.NAME_COLUMN) || header.get(j).equals(BankAccount.AMOUNT_COLUMN) || header.get(j).equals(BankAccount.CODE_COLUMN), ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                list.add(cellDatas);
            }
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, header.size());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate bank check list excel report, exception: " + e);
        }

        return null;
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

    public void setExcelReferenceMessageSource(WfmResourceBundleMessageSource excelReferenceMessageSource) {
        this.excelReferenceMessageSource = excelReferenceMessageSource;
    }

}
