/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/4/8 6:20:43                                                                                             *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 08.04.2010
 * Time: 17:52:16
 * To change this template use File | Settings | File Templates.
 */
public class TaxRatesSupplierListExcelHandler extends BaseExcelHandler {

    private static final Logger log = LoggerFactory.getLogger(TaxRatesSupplierListExcelHandler.class);

    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;

    @Override
    protected void setFileName() {
        filename = "Tax Rates";
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        filterParametrs.setStart(0);
        filterParametrs.setLimit(1000);
        ListResult<TaxListItem> solutionList = invoiceService.getAccountingTaxList(filterParametrs).getTaxList();
        List<TaxListItem> solutionListItems = solutionList.getList();
        EdsUser user = userManager.getUser();
        ExcelData[] cellDatas;
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        if (header.contains("Action")) {
            header.remove("Action");
        } else {
            header.remove("action");
        }
        header.remove(TaxListItem.ACTION);
        Map<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(TaxListItem.NAME, excelReferenceMessageSource.localize("GeneralName2", "Name"));
        mapColumnHeader.put(TaxListItem.TAXRATE, excelReferenceMessageSource.localize("EPTaxRate2", "Taxrate"));
        try {
            List<ExcelData[]> list = new LinkedList<>();
            cellDatas = new ExcelData[header.size()];

            for (int i = 0; i < header.size(); i++) {
                cellDatas[i] = new ExcelData(mapColumnHeader.get(header.get(i)), ExcelData.STRING, header.get(i).equals(TaxListItem.NAME) || header.get(i).equals(TaxListItem.TAXRATE) ? 50 : 20, false, header.get(i).equals(TaxListItem.NAME) || header.get(i).equals(TaxListItem.TAXRATE) || header.get(i).equals(TaxListItem.NAME), ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            list.add(cellDatas);
            for (TaxListItem tax : solutionListItems) {
                String temp = "";
                cellDatas = new ExcelData[header.size()];
                for (int j = 0; j < header.size(); j++) {
                    temp = "";
                    if (TaxListItem.NAME.equals(header.get(j))) {
                        temp = tax.getName() == null ? "" : tax.getName();
                    } else if (TaxListItem.TAXRATE.equals(header.get(j))) {
                        temp = tax.getPercent() == null ? "" : "" + tax.getPercent();
                    }
                    cellDatas[j] = new ExcelData(temp, ExcelData.STRING, header.get(j).equals(TaxListItem.NAME) || header.get(j).equals(TaxListItem.TAXRATE) ? 50 : 20, false, !header.get(j).equals(TaxListItem.NAME), ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                list.add(cellDatas);

            }
            return new WorkBook(list).getWorkBook(filename, 0, 0, 0, header.size());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate holidays list excel report, exception: " + e);
        }

        return null;
    }

    public void setExcelReferenceMessageSource(WfmResourceBundleMessageSource excelReferenceMessageSource) {
        this.excelReferenceMessageSource = excelReferenceMessageSource;
    }
}