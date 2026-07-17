package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.PrePaymentListItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.FixedAssetRegisterListView;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.PrepaymentService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Atabek Boboyev
 * Date: 24.01.2012
 * Time: 11:38
 * To change this template use File | Settings | File Templates.
 */
public class PrePaymentListExcelHandler extends BaseExcelHandler {
    private static final Logger log = LoggerFactory.getLogger(FixedAssetRegisterListView.class);
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;
    @Autowired
    private PrepaymentService prepaymentService;
    @Autowired
    private PropertManager propertManager;
    private String sheetName;

    public void setExcelReferenceMessageSource(WfmResourceBundleMessageSource excelReferenceMessageSource) {
        this.excelReferenceMessageSource = excelReferenceMessageSource;
    }

    @Override
    protected void setFileName() {
        filename = "Prepayments";
    }

    @Override
    protected void setFileName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        if (AccountingConstants.RECEIVABLE_PREPAYMENT.equals(fp.getViewType())) {
            filename = excelReferenceMessageSource.localizeAccounting("prepaymentList", "Customer Prepayments");
        } else {
            filename = excelReferenceMessageSource.localizeAccounting("supplierCredits", "Supplier Prepayments");
        }
    }

    @Override
    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter fp = (ListingFilterParameter) object;
        String shortDateFormat = "MMM dd, yyyy";
        Integer calScale = 2;
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        if (financialSettings != null && financialSettings.getCalculationScale() != null) {
            calScale = financialSettings.getCalculationScale();
        }
        DecimalFormat numberFormat = ServerUtils.getDecimalFormat(calScale);
        EdsUser user = userManager.getUser();
        EdsCompany edsCompany = user.getCompany();
        EdsCompanySettings companySettings = edsCompany.getCompanySettings();
        if (companySettings != null) {
            shortDateFormat = companySettings.getShortDateFormat();
        }
        if (companySettings.getExcelLimit() != null && !"".equals(companySettings.getExcelLimit())) {
            fp.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
        } else {
            fp.setLimit(LIMIT_EXCEL_ROW);
        }
        boolean isReceivable = AccountingConstants.RECEIVABLE_PREPAYMENT.equals(fp.getViewType());
        fp.setFromExcelPDF(true);
        ListResult<PrePaymentListItem> result = prepaymentService.getPrePaymentList(fp);
        ExcelData[] cellExcelDatas;
        ListPanelToolRpc panelTools = fp.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        if (header.contains("Action")) {
            header.remove("Action");
        } else {
            header.remove("action");
        }
        Map<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(PrePaymentListItem.CODE, commonLocalizer.localize(PdfLocalizationName.number));
        mapColumnHeader.put(PrePaymentListItem.CUSTOMER, commonLocalizer.localize("EPtaskClient", "Customer"));
        mapColumnHeader.put(PrePaymentListItem.SUPPLIER, commonLocalizer.localize("supplier", "Supplier"));
        mapColumnHeader.put(PrePaymentListItem.PAY_ACCOUNT, commonLocalizer.localize("paidTo", isReceivable ? PdfLocalizationName.paidTo : PdfLocalizationName.paidFrom));
        mapColumnHeader.put(PrePaymentListItem.NOTE, commonLocalizer.localize("note", "Note"));
        mapColumnHeader.put(PrePaymentListItem.REFERENCE, commonLocalizer.localize("accountingReference", "Reference"));
        mapColumnHeader.put(PrePaymentListItem.AMOUNT, commonLocalizer.localize("amount", "Amount"));
        mapColumnHeader.put(PrePaymentListItem.PROJECT, commonLocalizer.localize("project", "Project"));
        mapColumnHeader.put(PrePaymentListItem.REMAIN, commonLocalizer.localize("remain", "Remain"));
        mapColumnHeader.put(PrePaymentListItem.STATUS, commonLocalizer.localize("status", "Status"));
        mapColumnHeader.put(PrePaymentListItem.DATE, excelReferenceMessageSource.localize("date", "Date"));
        mapColumnHeader.put(PrePaymentListItem.CURRENCY, excelReferenceMessageSource.localize("currency", "Currency"));
        mapColumnHeader.put(PrePaymentListItem.CREATOR, commonLocalizer.localize("createdBy", "Created By"));
        mapColumnHeader.put(PrePaymentListItem.DEPARTMENT, commonLocalizer.localize("department", "Department"));
        mapColumnHeader.put(PrePaymentListItem.SALE_QUOTE, commonLocalizer.localize("salesQuote", "Sales Quote"));
        mapColumnHeader.put(PrePaymentListItem.SALE_INVOICE, commonLocalizer.localize("saleinvoice", "Sales Invoice"));
        mapColumnHeader.put(PrePaymentListItem.REMAINING_BALANCE, accountingLocalizer.localize("remainingBalance", "Remaining Balance"));
        mapColumnHeader.put(PrePaymentListItem.PURCHASE_ORDER, commonLocalizer.localize("purchaseorder", "Purchase Order"));
        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);
            EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
            if (fp.getPropertyCode().equals("supplierPrepayment")) {
                sheetName = property != null ? property.getPlural() : "Supplier Prepayments";
            } else if (fp.getPropertyCode().equals("customerPrepayment")) {
                sheetName = property != null ? property.getPlural() : "Customer Prepayments";
            } else {
                return null;
            }
            List<ExcelData[]> list = new LinkedList<>();
            cellExcelDatas = new ExcelData[header.size()];

            list.add(generateOneRowWithValue(header.size() + 1, edsCompany.getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(header.size() + 1, sheetName, workBook.getSheet(), 1));
            if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                list.add(generateOneRowWithValue(header.size() + 1, ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + " Xolatiga ko'ra", workBook.getSheet(), 2));
            } else {
                list.add(generateOneRowWithValue(header.size() + 1, commonLocalizer.localize(PdfLocalizationName.asOF) + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));
            }
//            list.add(generateOneRowWithValue(header.size() + 1, ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)+" "+commonLocalizer.localize(PdfLocalizationName.asOF), workBook.getSheet(), 2));

            for (int i = 0; i < header.size(); i++) {
                cellExcelDatas[i] = new ExcelData(mapColumnHeader.get(header.get(i)), ExcelData.STRING, header.get(i).equals(PrePaymentListItem.NOTE) || header.get(i).equals(PrePaymentListItem.PAY_ACCOUNT) ? 50 : 20, false, header.get(i).equals(PrePaymentListItem.NOTE) || header.get(i).equals(PrePaymentListItem.REFERENCE), ExcelData.NO_BORDER, ExcelData.HEADER);
            }

            list.add(cellExcelDatas);

            for (PrePaymentListItem assetItem : result.getList()) {
                String temp = "";
                cellExcelDatas = new ExcelData[header.size()];
                for (int j = 0; j < header.size(); j++) {
                    temp = "";
                    if (PrePaymentListItem.CODE.equals(header.get(j))) {
                        temp = assetItem.getNumber() != null ? assetItem.getNumber() : "";
                    } else if (PrePaymentListItem.CUSTOMER.equals(header.get(j)) || PrePaymentListItem.SUPPLIER.equals(header.get(j))) {
                        temp = assetItem.getCustomerName() != null ? assetItem.getCustomerName() : "";
                    } else if (PrePaymentListItem.PAY_ACCOUNT.equals(header.get(j))) {
                        temp = assetItem.getPayAccount() != null ? assetItem.getPayAccount() : "";
                    } else if (PrePaymentListItem.NOTE.equals(header.get(j))) {
                        temp = assetItem.getNote() != null ? assetItem.getNote() : "";
                    } else if (PrePaymentListItem.REFERENCE.equals(header.get(j))) {
                        temp = assetItem.getReference() != null ? assetItem.getReference() : "";
                    } else if (PrePaymentListItem.AMOUNT.equals(header.get(j))) {
                        temp = numberFormat.format(assetItem.getAmount());
                    } else if (PrePaymentListItem.PROJECT.equals(header.get(j))) {
                        temp = assetItem.getProject() != null ? assetItem.getProject() : "";
                    } else if (PrePaymentListItem.REMAIN.equals(header.get(j))) {
                        temp = assetItem.getRemainingBalance() != null ? getMoneyFormat(assetItem.getRemainingBalance()) : "";
                    } else if (PrePaymentListItem.DATE.equals(header.get(j))) {
                        if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                            temp = ServerUtils.convertToUzbDateFormat(ServerUtils.dateFormat(assetItem.getDate() != null ? assetItem.getDate().getNonConvertedDate() : null, shortDateFormat));
                        } else {
                            temp = ServerUtils.dateFormat(assetItem.getDate() != null ? assetItem.getDate().getNonConvertedDate() : null, shortDateFormat);
                        }
//                        temp = ServerUtils.dateFormat(assetItem.getDate() != null ? assetItem.getDate().getNonConvertedDate() : null, shortDateFormat);
                    } else if (PrePaymentListItem.CURRENCY.equals(header.get(j))) {
                        temp = assetItem.getCurrency() != null ? assetItem.getCurrency() : "";
                    } else if (PrePaymentListItem.STATUS.equals(header.get(j))) {
                        temp = AccountingConstants.PRE_PAYMENT_APPLIED_STATUS.equals(assetItem.getStatus()) ? commonLocalizer.localize(PdfLocalizationName.applied)
                                : AccountingConstants.PRE_PAYMENT_OPEN_STATUS.equals(assetItem.getStatus()) ? commonLocalizer.localize(PdfLocalizationName.open)
                                : AccountingConstants.PRE_PAYMENT_PARTIAL_APPLIED_STATUS.equals(assetItem.getStatus()) ? commonLocalizer.localize(PdfLocalizationName.partialApplied) : commonLocalizer.localize(PdfLocalizationName.open);
                    } else if (PrePaymentListItem.CREATOR.equals(header.get(j))) {
                        temp = assetItem.getCreator() != null ? assetItem.getCreator() : "";
                    } else if (PrePaymentListItem.DEPARTMENT.equals(header.get(j))) {
                        temp = assetItem.getDepartment() != null ? assetItem.getDepartment() : "";
                    } else if (PrePaymentListItem.SALE_QUOTE.equals(header.get(j))) {
                        temp = assetItem.getSaleQuote() != null ? assetItem.getSaleQuote().getName() : "";
                    } else if (PrePaymentListItem.SALE_INVOICE.equals(header.get(j))) {
                        temp = assetItem.getSaleInvoice() != null ? assetItem.getSaleInvoice().getName() : "";
                    } else if (PrePaymentListItem.REMAINING_BALANCE.equals(header.get(j))) {
                        temp = assetItem.getRemainingBalance() != null ? numberFormat.format(assetItem.getRemainingBalance()) : "";
                    } else if (PrePaymentListItem.PURCHASE_ORDER.equals(header.get(j))) {
                        temp = assetItem.getPurchaseOrder() != null ? assetItem.getPurchaseOrder().getName() : "";
                    }

                    cellExcelDatas[j] = new ExcelData(temp, ExcelData.STRING, header.get(j).equals(PrePaymentListItem.NOTE) || header.get(j).equals(PrePaymentListItem.PAY_ACCOUNT) ? 50 : 20, false, header.get(j).equals(PrePaymentListItem.NOTE) || header.get(j).equals(PrePaymentListItem.REFERENCE), ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                list.add(cellExcelDatas);
            }
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, header.size());

        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Cannot generate " + filename + " report, exception: " + ex);
        }
        return null;
    }
}