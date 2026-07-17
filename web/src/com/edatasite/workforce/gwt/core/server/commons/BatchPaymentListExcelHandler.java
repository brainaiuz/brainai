package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.invoice.client.rpc.BatchPaymentListItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by dilshod on 1/17/2016.
 */
public class BatchPaymentListExcelHandler extends BaseExcelHandler {
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private UserManager userManager;
    @Autowired
    private PropertManager propertManager;
    private String sheetName;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;

    private static final Logger logger = LoggerFactory.getLogger(BatchPaymentListExcelHandler.class);

    @Override
    protected void setFileName() {
        filename = "Payments";
    }

    @Override
    protected void setFileName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        filename = Constants.RECEIVABLE.equals(fp.getDataType()) ? "Receive Payments" : "Pay Invoices";
    }

    @Override
    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter fp = (ListingFilterParameter) object;
        fp.setLimit(LIMIT_EXCEL_ROW);
        String shortDateFormat = "MMM dd, yyyy";
        boolean isReceivable = Constants.RECEIVABLE.equals(fp.getDataType());
        EdsUser user = userManager.getUser();
        EdsCompanySettings companySettings = user.getCompany().getCompanySettings();
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        Integer calculationScale = getCalculationScale(financialSettings);
        if (companySettings != null) {
            shortDateFormat = companySettings.getShortDateFormat();
            if (companySettings.getExcelLimit() != null && !"".equals(companySettings.getExcelLimit())) {
                fp.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
            }
        }
        ListResult<BatchPaymentListItem> result = invoiceService.getBatchPayments(fp);

        ExcelData[] cellExcelDatas;
        ListPanelToolRpc panelTools = fp.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        header.remove(BatchPaymentListItem.ACTION);

        Map<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(BatchPaymentListItem.NUMBER, commonLocalizer.localize(PdfLocalizationName.number));
        mapColumnHeader.put(BatchPaymentListItem.CRM_ACCOUNT, isReceivable ? commonLocalizer.localize(PdfLocalizationName.customer) : commonLocalizer.localize(PdfLocalizationName.supplier));
        mapColumnHeader.put(BatchPaymentListItem.DATE, commonLocalizer.localize(PdfLocalizationName.date));
        mapColumnHeader.put(BatchPaymentListItem.REFERENCE, commonLocalizer.localize(PdfLocalizationName.reference));
        mapColumnHeader.put(BatchPaymentListItem.ACCOUNT, commonLocalizer.localize(PdfLocalizationName.account));
        mapColumnHeader.put(BatchPaymentListItem.AMOUNT, commonLocalizer.localize(PdfLocalizationName.amount));
        mapColumnHeader.put(BatchPaymentListItem.PAYMENT_TYPE, commonLocalizer.localize(PdfLocalizationName.paymentType));
        mapColumnHeader.put(BatchPaymentListItem.CURRENCY, commonLocalizer.localize(PdfLocalizationName.currency));
        mapColumnHeader.put(BatchPaymentListItem.PROJECT, commonLocalizer.localize(PdfLocalizationName.project));
        mapColumnHeader.put(BatchPaymentListItem.CREATOR, commonLocalizer.localize(PdfLocalizationName.createdBy));

        setCustomFieldsPdfHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);
        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);
            EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
            if (fp.getPropertyCode().equals("BATCH_RECEIVE_PAYMENT")) {
                sheetName = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.recivePayments, "Receive Payments");
            } else if (fp.getPropertyCode().equals("payBillsList")) {
                sheetName = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.payInvoices);
            } else {
                return null;
            }
            List<ExcelData[]> list = new LinkedList<>();
            cellExcelDatas = new ExcelData[header.size()];

            list.add(generateOneRowWithValue(header.size() + 1, user.getCompany().getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(header.size() + 1, sheetName, workBook.getSheet(), 1));
            if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                list.add(generateOneRowWithValue(header.size() + 1, ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + " " + commonLocalizer.localize(PdfLocalizationName.asOF), workBook.getSheet(), 2));
            } else {
                list.add(generateOneRowWithValue(header.size() + 1, commonLocalizer.localize(PdfLocalizationName.asOF) + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));
            }
//            list.add(generateOneRowWithValue(header.size() + 1, "As of  + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            for (int i = 0; i < header.size(); i++) {
                cellExcelDatas[i] = new ExcelData(mapColumnHeader.get(header.get(i)), ExcelData.STRING, 30, false, false, ExcelData.NO_BORDER, ExcelData.HEADER);
            }

            list.add(cellExcelDatas);
            for (BatchPaymentListItem item : result.getList()) {
                String temp = "";
                cellExcelDatas = new ExcelData[header.size()];
                for (int j = 0; j < header.size(); j++) {
                    temp = "";
                    switch (header.get(j)) {
                        case BatchPaymentListItem.NUMBER -> {
                            temp = item.getNumber();
                            cellExcelDatas[j] = new ExcelData(temp, ExcelData.STRING, 30, false, false, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        }
                        case BatchPaymentListItem.CRM_ACCOUNT -> {
                            temp = item.getCrmAccount() != null ? item.getCrmAccount().getName() : "";
                            cellExcelDatas[j] = new ExcelData(temp, ExcelData.STRING, 30, false, false, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        }
                        case BatchPaymentListItem.DATE -> {
                            temp = ServerUtils.dateFormat(item.getDate() != null ? item.getDate().getNonConvertedDate() : null, shortDateFormat);
                            cellExcelDatas[j] = new ExcelData(temp, ExcelData.STRING, 30, false, false, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        }
                        case BatchPaymentListItem.REFERENCE -> {
                            temp = item.getReference() != null ? item.getReference() : "";
                            cellExcelDatas[j] = new ExcelData(temp, ExcelData.STRING, 30, false, false, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        }
                        case BatchPaymentListItem.ACCOUNT -> {
                            temp = item.getAccount() != null ? item.getAccount().getName() : "";
                            cellExcelDatas[j] = new ExcelData(temp, ExcelData.STRING, 30, false, false, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        }
                        case BatchPaymentListItem.AMOUNT -> {
                            BigDecimal amount = (item.getTotalAmount() != null ? item.getTotalAmount() : BigDecimal.ZERO).setScale(calculationScale, RoundingMode.HALF_UP);
                            temp = "" + amount;
                            cellExcelDatas[j] = new ExcelData(temp, ExcelData.STRING, 30, false, false, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        }
                        case BatchPaymentListItem.PAYMENT_TYPE -> {
                            temp = item.getPaymentMethod() != null ? item.getPaymentMethod().getName() : "";
                            cellExcelDatas[j] = new ExcelData(temp, ExcelData.STRING, 30, false, false, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        }
                        case BatchPaymentListItem.CURRENCY -> {
                            temp = item.getCurrency() != null ? item.getCurrency().getName() : "";
                            cellExcelDatas[j] = new ExcelData(temp, ExcelData.STRING, 30, false, false, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        }
                        case BatchPaymentListItem.PROJECT -> {
                            temp = item.getProject() != null ? item.getProject() : "";
                            cellExcelDatas[j] = new ExcelData(temp, ExcelData.STRING, 30, false, false, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        }
                        case BatchPaymentListItem.CREATOR -> {
                            temp = item.getCreator() != null ? item.getCreator() : "";
                            cellExcelDatas[j] = new ExcelData(temp, ExcelData.STRING, 30, false, false, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        }
                        default -> {
                            if (item.getCustomFieldsMap() != null && item.getCustomFieldsMap().get(header.get(j)) != null) {
                                if (item.getCustomFieldsMap().get(header.get(j)) instanceof Date) {
                                    temp = dateFormat((Date) item.getCustomFieldsMap().get(header.get(j)));
                                } else {
                                    temp = item.getCustomFieldsMap().get(header.get(j)) != null ? item.getCustomFieldsMap().get(header.get(j)).toString() : "";
                                }
                                cellExcelDatas[j] = new ExcelData(temp, ExcelData.STRING, 30, false, false, ExcelData.NO_BORDER, ExcelData.NORMAL);
                            }
                        }
                    }
                }
                list.add(cellExcelDatas);
            }
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, header.size());

        } catch (Exception exp) {
            exp.printStackTrace();
            logger.error("Cannot generate " + filename + " excel report, exception: " + exp);
        }
        return null;
    }

    public void setCustomFieldsPdfHeaderMap(List<CompanyCustomFieldItem> customfields, Map<String, String> pdfHeader) {
        if (customfields != null) {
            for (CompanyCustomFieldItem field : customfields) {
                pdfHeader.put(field.getColumnCode(), field.getFieldName());
            }
        }
    }

}
