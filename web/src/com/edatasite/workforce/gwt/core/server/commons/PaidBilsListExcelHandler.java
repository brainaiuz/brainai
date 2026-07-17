package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.invoice.client.rpc.BatchPaymentListItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 25.07.2009
 * Time: 16:15:12
 * To change this template use File | Settings | File Templates.
 */
public class PaidBilsListExcelHandler extends BaseExcelHandler {

    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private UserManager userManager;


    @Autowired
    @Qualifier("commonLocalizer")
    protected WfmMessageSource commonLocalizer;

    private static final Logger log = LoggerFactory.getLogger(PaidBilsListExcelHandler.class);
    private static final DecimalFormat numberFormat = new DecimalFormat("#,##0.00");
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }

    @Override
    protected void setFileName() {
        filename = "Paid_Bils_List";
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        String shortDateFormat = "MMM dd, yyyy";
        EdsUser user = userManager.getUser();
        EdsCompany company = user.getCompany();
        EdsCompanySettings companySettings = company.getCompanySettings();
        if (companySettings != null) {
            shortDateFormat = companySettings.getShortDateFormat();
        }
        ListingFilterParameter filterParameters = (ListingFilterParameter) object;
        filterParameters.setDataType(Constants.PAYABLE);

        if (companySettings.getExcelLimit() != null && !"".equals(companySettings.getExcelLimit())) {
            filterParameters.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
        } else {
            filterParameters.setLimit(LIMIT_EXCEL_ROW);
        }

        ListResult<BatchPaymentListItem> result = invoiceService.getBatchPayments(filterParameters);
        List<BatchPaymentListItem> batchPaymentList = result.getList();
        ExcelData[] cellDatas;
        ListPanelToolRpc panelTools = filterParameters.getListPanelTool();
        Map<String, ExcelData> mapColumnHeader = new HashMap<>();
        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);

            List<ExcelData[]> list = new LinkedList<>();
            mapColumnHeader.put(BatchPaymentListItem.NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.number), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(BatchPaymentListItem.CRM_ACCOUNT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.supplier), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(BatchPaymentListItem.DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.date), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(BatchPaymentListItem.REFERENCE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.reference), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(BatchPaymentListItem.ACCOUNT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.account), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(BatchPaymentListItem.AMOUNT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.amount), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(BatchPaymentListItem.PAYMENT_TYPE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.paymentType), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(BatchPaymentListItem.CURRENCY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.currency), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            List<ExcelData> excelDataList = new ArrayList<>();
            for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
                if (mapColumnHeader.containsKey(panelTools.getColumnCodeName().get(i))) {
                    excelDataList.add(mapColumnHeader.get(panelTools.getColumnCodeName().get(i)));
                }
            }
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), company.getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), "Paid Bils List", workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), excelReferenceMessageSource.localize("EPAsOf", " As Of") + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            cellDatas = new ExcelData[excelDataList.size()];
            excelDataList.toArray(cellDatas);
            EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
            DecimalFormat priceScaleNumberFormat = getPriceScaleNumberFormat(financialSettings);
            list.add(cellDatas);
            for (BatchPaymentListItem item : batchPaymentList) {

                Map<String, ExcelData> mapColumn = new HashMap<>();
                if (panelTools.getColumnCodeName().contains(BatchPaymentListItem.NUMBER)) {
                    mapColumn.put(BatchPaymentListItem.NUMBER, new ExcelData(item.getNumber() != null ? item.getNumber() : "N/A", ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(BatchPaymentListItem.CRM_ACCOUNT)) {
                    mapColumn.put(BatchPaymentListItem.CRM_ACCOUNT, new ExcelData(item.getCrmAccount() != null ? item.getCrmAccount().getName() : "N/A",
                            ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(BatchPaymentListItem.DATE))
                    mapColumn.put(BatchPaymentListItem.DATE, new ExcelData(item.getDate() != null ? dateFormat(item.getDate().getDate()) : "N/A",
                            ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                if (panelTools.getColumnCodeName().contains(BatchPaymentListItem.REFERENCE)) {
                    mapColumn.put(BatchPaymentListItem.REFERENCE, new ExcelData(item.getReference() != null ? item.getReference() : "N/A",
                            ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(BatchPaymentListItem.ACCOUNT)) {
                    mapColumn.put(BatchPaymentListItem.ACCOUNT, new ExcelData(item.getAccount() != null ? item.getAccount().getName() : "N/A", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(BatchPaymentListItem.AMOUNT)) {
                    mapColumn.put(BatchPaymentListItem.AMOUNT, new ExcelData(item.getTotalAmount() != null ? priceScaleNumberFormat.format(item.getTotalAmount()) : "N/A", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(BatchPaymentListItem.PAYMENT_TYPE)) {
                    mapColumn.put(BatchPaymentListItem.PAYMENT_TYPE, new ExcelData(item.getPaymentMethod() != null ? item.getPaymentMethod().getName() : "N/A", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(BatchPaymentListItem.CURRENCY)) {
                    mapColumn.put(BatchPaymentListItem.CURRENCY, new ExcelData(item.getCurrency() != null ? item.getCurrency().getName() : "N/A", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                excelDataList = new ArrayList<>();
                for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
                    if (mapColumn.containsKey(panelTools.getColumnCodeName().get(i))) {
                        excelDataList.add(mapColumn.get(panelTools.getColumnCodeName().get(i)));
                    }
                }
                cellDatas = new ExcelData[excelDataList.size()];
                excelDataList.toArray(cellDatas);
                list.add(cellDatas);
            }
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, 6);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate paid bils list excel report, exception: " + e.getMessage());
        }
        return null;
    }

    public void setExcelReferenceMessageSource(WfmResourceBundleMessageSource excelReferenceMessageSource) {
        this.excelReferenceMessageSource = excelReferenceMessageSource;
    }
}
