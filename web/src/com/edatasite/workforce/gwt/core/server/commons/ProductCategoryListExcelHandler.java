package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.ManualJournalListItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductCategoryItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class ProductCategoryListExcelHandler extends BaseExcelHandler {

    @Autowired
    private AccountingService accountingService;

    private static final Logger log = LoggerFactory.getLogger(ProductCategoryListExcelHandler.class);
    @Autowired
    private PropertManager propertManager;

    @Autowired
    private UserManager userManager;

    private String sheetName;

    @Override
    protected void setFileName() {
        filename = "Product Category List";
    }

    protected HSSFWorkbook getWorkBook(Object object) {

        EdsUser user = userManager.getUser();
        EdsCompany edsCompany = user.getCompany();
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        EdsCompanySettings companySettings = userManager.getUser().getCompany().getCompanySettings();
        String shortDateFormat = (companySettings != null && companySettings.getShortDateFormat() != null) ? companySettings.getShortDateFormat() : "MMM dd, yyyy";

        filterParametrs.setFromExcelPDF(true);


        ListResult<ProductCategoryItem> productCategoryList = accountingService.getProductCategoriesList(filterParametrs);
        ArrayList<ProductCategoryItem> result = productCategoryList.getList();
        ExcelData[] cellDatas;
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<String> header2 = new ArrayList<>();
        List<String> header = panelTools.getColumnCodeName();
        if (header.contains("Action")) {
            header.remove("Action");
        } else {
            header.remove("action");
        }
        header.remove(ManualJournalListItem.ACTION);

        Set<String> columnsForExport = new HashSet<>();
        for (ProductCategoryItem items : result) {
            if (items.getParentCategoryName() != null && header.contains(ProductCategoryItem.PARENT))
                columnsForExport.add(ProductCategoryItem.PARENT);
            if (items.getName() != null && header.contains(ProductCategoryItem.NAME))
                columnsForExport.add(ProductCategoryItem.NAME);
            if (items.getDescription() != null && header.contains(ProductCategoryItem.DESCRIPTION))
                columnsForExport.add(ProductCategoryItem.DESCRIPTION);
            if (header.contains(ProductCategoryItem.STATUS)) columnsForExport.add(ProductCategoryItem.STATUS);
        }


        Map<String, ExcelData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(ProductCategoryItem.PARENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.parent), ExcelData.STRING, 25, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(ProductCategoryItem.NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.name), ExcelData.STRING, 25, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(ProductCategoryItem.DESCRIPTION, new ExcelData(commonLocalizer.localize(PdfLocalizationName.description), ExcelData.STRING, 25, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(ProductCategoryItem.STATUS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.status), ExcelData.STRING, 25, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        CustomFieldsUtils.setCustomFieldsExcelHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);

        List<ExcelData> excelDataList = new ArrayList<>();
        for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
            if (mapColumnHeader.containsKey(panelTools.getColumnCodeName().get(i))) {
                excelDataList.add(mapColumnHeader.get(panelTools.getColumnCodeName().get(i)));
            }
        }

        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);
            EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
            sheetName = property != null ? property.getPlural() : "Product Category List";
            List<ExcelData[]> list = new LinkedList<>();
            cellDatas = new ExcelData[columnsForExport.size()];

            list.add(generateOneRowWithValue(columnsForExport.size() + 1, user.getCompany().getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(columnsForExport.size() + 1, sheetName, workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(columnsForExport.size() + 1, ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + " Xolatiga ko'ra" : " " + commonLocalizer.localize(PdfLocalizationName.asOF) + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));


            cellDatas = new ExcelData[excelDataList.size()];
            excelDataList.toArray(cellDatas);
            list.add(cellDatas);


            for (ProductCategoryItem items : result) {

                Map<String, ExcelData> mapColumn = new HashMap<>();

                if (panelTools.getColumnCodeName().contains(ProductCategoryItem.PARENT)) {
                    mapColumn.put(ProductCategoryItem.PARENT, new ExcelData(items.getParentCategoryName() == null ? "N/A" : items.getParentCategoryName(), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProductCategoryItem.NAME)) {
                    mapColumn.put(ProductCategoryItem.NAME, new ExcelData(items.getName() == null ? "N/A" : items.getName(), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProductCategoryItem.DESCRIPTION)) {
                    mapColumn.put(ProductCategoryItem.DESCRIPTION, new ExcelData(items.getDescription() == null ? "N/A" : items.getDescription(), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProductCategoryItem.STATUS)) {
                    mapColumn.put(ProductCategoryItem.STATUS, new ExcelData(items.isActive() ? accountingLocalizer.localize(PdfLocalizationName.active) : commonLocalizer.localize(PdfLocalizationName.deactivate), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                CustomFieldsUtils.setCustomFieldsExcelTableRows(panelTools.getListViewCustomFields(), mapColumn, panelTools.getColumnCodeName(), items, edsCompany);
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
            return workBook.getWorkBook(filename, 0, 0, 0, header.size());

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate product category excel list, exception: " + e);
        }
        return null;
    }
}


