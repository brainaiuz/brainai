package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingMethod;
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
 * User: Java6
 * Date: 29.09.11
 * Time: 16:45
 * To change this template use File | Settings | File Templates.
 */
public class ShippingMethodsListExcelHandler extends BaseExcelHandler {

    private static final Logger log = LoggerFactory.getLogger(ShippingMethodsListExcelHandler.class);

    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;

    @Override
    protected void setFileName() {
        filename = "ShippingMethods";
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        filterParametrs.setLimit(1000);
        ListResult<ShippingMethod> shippinglist = invoiceService.getShippingMethodData(filterParametrs);
        List<ShippingMethod> solutionListItems = shippinglist.getList();
        EdsUser user = userManager.getUser();
        ExcelData[] cellDatas;
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        if (header.contains("Action")) {
            header.remove("Action");
        } else {
            header.remove("action");
        }
        header.remove(ShippingMethod.ACTION);
        Map<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(ShippingMethod.PRICE, excelReferenceMessageSource.localize("EPPrice", "Price"));
        mapColumnHeader.put(ShippingMethod.DESCRIPTION, excelReferenceMessageSource.localize("wokrspacedescriptionField", "Descripsion"));
        mapColumnHeader.put(ShippingMethod.NAME, excelReferenceMessageSource.localize("GeneralName2", "Name"));
        mapColumnHeader.put(ShippingMethod.TAXRATE, excelReferenceMessageSource.localize("EPTaxRate", "Tax Rate"));
        try {
            List<ExcelData[]> list = new LinkedList<>();
            cellDatas = new ExcelData[header.size()];

            for (int i = 0; i < header.size(); i++) {
                cellDatas[i] = new ExcelData(mapColumnHeader.get(header.get(i)), ExcelData.STRING, header.get(i).equals(ShippingMethod.PRICE) || header.get(i).equals(ShippingMethod.DESCRIPTION) ? 50 : 20, false, header.get(i).equals(ShippingMethod.NAME) || header.get(i).equals(ShippingMethod.PRICE) || header.get(i).equals(ShippingMethod.DESCRIPTION), ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            list.add(cellDatas);
            for (ShippingMethod pickList : solutionListItems) {
                String temp = "";
                cellDatas = new ExcelData[header.size()];
                for (int j = 0; j < header.size(); j++) {
                    temp = "";
                    if (ShippingMethod.PRICE.equals(header.get(j))) {
                        temp = pickList.getPrice() == null ? "" : pickList.getPrice().toString();
                    } else if (ShippingMethod.DESCRIPTION.equals(header.get(j))) {
                        temp = pickList.getDescription() == null ? "N/A" : pickList.getDescription();
                    } else if (ShippingMethod.NAME.equals(header.get(j))) {
                        temp = pickList.getName() == null ? "N/A" : pickList.getName();
                    } else if (ShippingMethod.TAXRATE.equals(header.get(j))) {
                        temp = pickList.getTaxItem() != null ? pickList.getTaxItem().getName() : "N/A";
                    }
                    cellDatas[j] = new ExcelData(temp, ExcelData.STRING, header.get(j).equals(ShippingMethod.PRICE) || header.get(j).equals(ShippingMethod.DESCRIPTION) ? 50 : 20, false, !header.get(j).equals(ShippingMethod.NAME), ExcelData.NO_BORDER, ExcelData.NORMAL);
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
