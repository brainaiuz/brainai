package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CurrencyManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by dilshod on 23-Mar-16.
 */
public class ManualTransactionSampleGeneratorExcelHandler extends ExcelHandler {

    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private AccountingManager accountingManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private CurrencyManager currencyManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;

    private static final boolean number = true;
    private static final boolean date = true;
    private static final boolean narration = true;
    private static final boolean exchangeRate = true;
    private static final boolean foreignCurrency = true;
    private static final boolean department = true;
    private static final boolean accountCode = true;
    private static final boolean debit = true;
    private static final boolean credit = true;
    private static final boolean reference = true;
    private static final boolean description = true;
    private static final boolean name = true;
    private static final boolean projectCode = true;
    private static final int limit = 10000;

    @Override
    protected HSSFWorkbook getWorkBook(HttpServletRequest request) {
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        Integer baseCurrencyID =  fs.getCurrency().getObjectID();
        Integer currencyID = (request.getParameter("currencyID") == null || "null".equals(request.getParameter("currencyID"))) ? baseCurrencyID : Integer.parseInt(request.getParameter("currencyID"));
        boolean isSameCurrency = baseCurrencyID.equals(currencyID);

        WorkBook wb = new WorkBook(true, 0, 1, 0, 1);
        ExcelData[] cellData;
        List<ExcelData[]> list = new LinkedList<>();

        HashMap<Integer, String[]> listValidatedColumns = new LinkedHashMap<>();
        List<Integer> doubleValidatedColumns = new LinkedList<>();
        List<Integer> dateValidatedColumns = new LinkedList<>();

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setCurrencyID(currencyID);

        Map<String, Integer> accountMap = accountingManager.getAccountAsMapByCode(fp);
        Map<String, Integer> projectMap = projectManager.getProjectAsMapByNumber();
        Map<String, Integer> costCenterMap = departmentManager.getDepartmentAsMap();
        List<EdsCurrency> currencyList = currencyManager.getAllCurrency();
        Map<String, EdsCrmAccount> crmAccountMap = crmAccountManager.getAllCrmAccountsMap();


        int colCount = 0;

        colCount += number ? 1 : 0;
        colCount += date ? 1 : 0;
        colCount += narration ? 1 : 0;
        colCount += reference ? 1 : 0;
        colCount += accountCode ? 1 : 0;
        colCount += debit ? 1 : 0;
        colCount += credit ? 1 : 0;
        colCount += description ? 1 : 0;
        colCount += name ? 1 : 0;
        colCount += projectCode ? 1 : 0;
        colCount += department ? 1 : 0;
        colCount += (exchangeRate && !isSameCurrency) ? 1 : 0;
        colCount += (foreignCurrency && !isSameCurrency) ? 1 : 0;

        cellData = new ExcelData[colCount];

        int i = 0;

        if (number) {
            cellData[i++] = new ExcelData("Number", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        }
        if (date) {
            dateValidatedColumns.add(i);
            cellData[i++] = new ExcelData("Date", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        }
        if (narration) {
            cellData[i++] = new ExcelData("Narration", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        }
        if (reference) {
            cellData[i++] = new ExcelData("Reference", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        }
        if (accountCode) {
            listValidatedColumns.put(i, accountMap != null ? accountMap.keySet().toArray(new String[]{}) : new String[]{});
            cellData[i++] = new ExcelData("Account Code", ExcelData.STRING, 12, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        }
        if (debit) {
            doubleValidatedColumns.add(i);
            cellData[i++] = new ExcelData("Debit", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        }
        if (credit) {
            doubleValidatedColumns.add(i);
            cellData[i++] = new ExcelData("Credit", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        }
        if (description) {
            cellData[i++] = new ExcelData("Description", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        }
        if (name) {
            addCrmAccountsToList(listValidatedColumns, i, crmAccountMap.values());
            cellData[i++] = new ExcelData("Name", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        }
        if (projectCode) {
            listValidatedColumns.put(i, projectMap != null ? projectMap.keySet().toArray(new String[]{}) : new String[]{});
            cellData[i++] = new ExcelData("Project Code", ExcelData.STRING, 12, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        }
        if (department) {
            listValidatedColumns.put(i, costCenterMap != null ? costCenterMap.keySet().toArray(new String[]{}) : new String[]{});
            cellData[i++] = new ExcelData("Department", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        }
        if (exchangeRate && !isSameCurrency) {
            doubleValidatedColumns.add(i);
            cellData[i++] = new ExcelData("Exchange Rate", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        }
        if (foreignCurrency && !isSameCurrency) {
            addCurrenciesToList(listValidatedColumns, i, currencyList);
            cellData[i++] = new ExcelData("Foreign Currency", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        }
        list.add(cellData);
        wb.setList(list);
        HSSFWorkbook workbook = wb.getWorkBook("Manual_Entries_Sample", 0, 0, 0, 1);
        HSSFSheet sheet = workbook.getSheet("Manual_Entries_Sample");
        HSSFSheet validations = workbook.createSheet("validations");

        populateValidationsSheet(listValidatedColumns, validations);

        Character c = 'A';
        for (Map.Entry<Integer, String[]> entry : listValidatedColumns.entrySet()) {
            addListDataValidation(sheet, 1, entry.getKey(), limit, entry.getKey(), entry.getValue(), c++);
        }

        for (Integer j : doubleValidatedColumns) {
            addDoubleNumericConstraint(sheet, 1, j, limit, j);
        }

//        for (Integer j : dateValidatedColumns) {
//            addDateValidation(sheet, 1, j, limit, j);
//        }

        workbook.setSheetHidden(1, true);
        return workbook;
    }


    private void addCurrenciesToList(HashMap<Integer, String[]> listValidatedColumns, Integer i, List<EdsCurrency> currencies) {
        String[] listItems = new String[currencies.size()];

        int j = 0;
        for (EdsCurrency cur : currencies) {
            listItems[j++] = cur.getName();
        }

        listValidatedColumns.put(i, listItems);
    }

    private void addCrmAccountsToList(HashMap<Integer, String[]> listValidatedColumns, Integer i, Collection<EdsCrmAccount> accounts) {
        String[] listItems = new String[accounts.size()];

        int j = 0;
        for (EdsCrmAccount cur : accounts) {
            listItems[j++] = cur.getName();
        }

        listValidatedColumns.put(i, listItems);
    }

    private void populateValidationsSheet(HashMap<Integer, String[]> dataMap, HSSFSheet validations) {
        boolean isAddedData = true;
        for (int i = 0; isAddedData; i++) {
            isAddedData = false;
            HSSFRow row = validations.createRow(i);
            int j = 0;
            for (Map.Entry<Integer, String[]> entry : dataMap.entrySet()) {
                HSSFCell cell = row.createCell(j++);
                if (entry.getValue().length > i) {
                    cell.setCellValue(entry.getValue()[i]);
                    isAddedData = true;
                }
            }
        }
    }

    private void addListDataValidation(Sheet sheet, int firstRow, int firstCol, int lastRow, int lastCol, String[] list, Character c) {
        if (sheet != null && list != null) {
            CellRangeAddressList addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
            DVConstraint dvConstraint = DVConstraint
                    .createFormulaListConstraint("validations!$" + c + "$1:$" + c + "$" + (list.length > 0 ? list.length : 1));
            DataValidation dataValidation = new HSSFDataValidation(addressList,
                    dvConstraint);
            dataValidation.setSuppressDropDownArrow(false);
            sheet.addValidationData(dataValidation);
        }
    }

    private void addDoubleNumericConstraint(Sheet sheet, int firstRow, int firstCol, int lastRow, int lastCol) {
        if (sheet != null) {
            CellRangeAddressList addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
            DVConstraint dvConstraint = DVConstraint.createNumericConstraint(DataValidationConstraint.ValidationType.DECIMAL, DataValidationConstraint.OperatorType.GREATER_OR_EQUAL, "0.0", null);
            DataValidation dataValidation = new HSSFDataValidation(addressList,
                    dvConstraint);
            sheet.addValidationData(dataValidation);
        }
    }

    private void addDateValidation(Sheet sheet, int firstRow, Integer firstCol, int lastRow, Integer lastCol) {
        if (sheet != null && firstCol != null) {
            CellRangeAddressList addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
            DVConstraint dvConstraint = DVConstraint.createDateConstraint(DataValidationConstraint.OperatorType.BETWEEN, "1990/01/01", "2099/12/31", "yyyy/MM/dd");
            DataValidation dataValidation = new HSSFDataValidation(addressList,
                    dvConstraint);
            sheet.addValidationData(dataValidation);
        }
    }

    @Override
    public void setFileName(String name) {
        filename = "Manual_Entries_Sample_XLS";
    }
}
