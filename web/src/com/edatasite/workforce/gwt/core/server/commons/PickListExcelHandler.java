package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.invoice.client.rpc.PickList;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.math.RoundingMode;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Hasan
 * Date: 28.09.11
 * Time: 14:58
 * To change this template use File | Settings | File Templates.
 */
public class PickListExcelHandler extends BaseExcelHandler {

    private static final Logger log = LoggerFactory.getLogger(PickListExcelHandler.class);

    @Autowired
    private QuoteService quoteService;
    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;

    @Override
    protected void setFileName() {
        filename = "Pick List";
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        filterParametrs.setLimit(1000);
        ListResult<PickList> solutionList = quoteService.getPickListData(filterParametrs);
        List<PickList> solutionListItems = solutionList.getList();
        EdsUser user = userManager.getUser();
        ExcelData[] cellDatas;
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        if (header.contains("Action")) {
            header.remove("Action");
        } else {
            header.remove("action");
        }
        header.remove(PickList.ACTION);
        Map<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(PickList.STATUS, excelReferenceMessageSource.localizeAccounting("workspaceStatus", "Status"));
        mapColumnHeader.put(PickList.SHIP_DATE, excelReferenceMessageSource.localizeAccounting("EPShipDate", "ShipDate"));
        mapColumnHeader.put(PickList.EXPECTED_DATE, excelReferenceMessageSource.localizeAccounting("EPExpectedDate", "ExpectedDate"));
        mapColumnHeader.put(PickList.TOTAL, excelReferenceMessageSource.localizeAccounting("EPTotal", "Total"));
        mapColumnHeader.put(PickList.DISCOUNT, excelReferenceMessageSource.localizeAccounting("EPDiscount", "Discount"));
        mapColumnHeader.put(PickList.DUE_DATE, excelReferenceMessageSource.localizeAccounting("EPtaskDuedate", "DueDate"));
        mapColumnHeader.put(PickList.CLIENT, excelReferenceMessageSource.localize("EPtaskClient", "Client"));


        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        Integer calculationScale = getCalculationScale(fs);

        try {
            List<ExcelData[]> list = new LinkedList<>();
            cellDatas = new ExcelData[header.size()];

            for (int i = 0; i < header.size(); i++) {
                cellDatas[i] = new ExcelData(mapColumnHeader.get(header.get(i)), ExcelData.STRING, header.get(i).equals(PickList.STATUS) || header.get(i).equals(PickList.SHIP_DATE) ? 50 : 20, false, header.get(i).equals(PickList.EXPECTED_DATE) || header.get(i).equals(PickList.TOTAL) || header.get(i).equals(PickList.DISCOUNT), ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            list.add(cellDatas);
            for (PickList pickList : solutionListItems) {
                String temp = "";
                cellDatas = new ExcelData[header.size()];
                for (int j = 0; j < header.size(); j++) {
                    temp = "";
                    if (PickList.STATUS.equals(header.get(j))) {
                        temp = pickList.getStatus() == null ? "" : pickList.getStatus();
                    } else if (PickList.SHIP_DATE.equals(header.get(j))) {
                        temp = pickList.getShipDate() == null ? "N/A" : pickList.getShipDate().toString();
                    } else if (PickList.EXPECTED_DATE.equals(header.get(j))) {
                        temp = pickList.getExpectedDate() == null ? "N/A" : pickList.getExpectedDate().getNonConvertedDate().toString();
                    } else if (PickList.TOTAL.equals(header.get(j))) {
                        temp = pickList.getTotal() == null ? "" : pickList.getTotal().setScale(calculationScale, RoundingMode.HALF_UP).toString();
                    } else if (PickList.DISCOUNT.equals(header.get(j))) {
                        temp = pickList.getDiscount() == null ? "" : pickList.getDiscount().setScale(calculationScale, RoundingMode.HALF_UP).toString();
                    } else if (PickList.DUE_DATE.equals(header.get(j))) {
                        temp = pickList.getDueDate() == null ? "" : pickList.getDueDate().toString();
                    } else if (PickList.CLIENT.equals(header.get(j))) {
                        temp = pickList.getClientName() == null ? "" : pickList.getClientName();
                    }
                    cellDatas[j] = new ExcelData(temp, ExcelData.STRING, header.get(j).equals(PickList.STATUS) || header.get(j).equals(PickList.SHIP_DATE) ? 50 : 20, false, !header.get(j).equals(PickList.EXPECTED_DATE), ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                list.add(cellDatas);

            }
            return new WorkBook(list).getWorkBook(filename, 0, 0, 0, header.size());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate Pick List list excel report, exception: " + e);
        }

        return null;
    }

    public void setExcelReferenceMessageSource(WfmResourceBundleMessageSource excelReferenceMessageSource) {
        this.excelReferenceMessageSource = excelReferenceMessageSource;
    }
}
