package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductCategoryItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.accounting.ProductCategoryManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Jun 26, 2010
 * Time: 4:04:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductCategoriesListPDFHandler extends AbstractITextPostPdfHandler {
    private AccountingService accountingService;
    @Autowired
    @Qualifier("accountingLocalizer")
    protected WfmMessageSource accountingLocalizer;
    @Autowired
    protected ProductCategoryManager productCategoryManager;

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        EdsUser user = uploadManager.getUser();
        filterParametrs.setFromExcelPDF(true);


        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        filterParametrs.setLimit(1000);
        List<String> header = panelTools.getColumnCodeName();
        List<CellData> header2 = new ArrayList<>();
        header.remove(ProductCategoryItem.ACTION);
        ITextTableList tableList = new ITextTableList(header.size());
        pdfData.setListTable(tableList);
        Map<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(ProductCategoryItem.NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.name), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductCategoryItem.DESCRIPTION, new CellData(commonLocalizer.localize(PdfLocalizationName.description), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductCategoryItem.PARENT, new CellData(commonLocalizer.localize(PdfLocalizationName.parent), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductCategoryItem.STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
        CustomFieldsUtils.setCustomFieldsPdfHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);
        for (String aHeader : header) {
            header2.add(mapColumnHeader.get(aHeader));
        }
        tableList.addPdfTableHeader(header2.toArray(new CellData[]{}));

        ListResult<ProductCategoryItem> list = accountingService.getProductCategoriesList(filterParametrs);

        if (list != null && list.getList().size() > 0) {
            for (ProductCategoryItem item : list.getList()) {
                String[] temp = new String[header.size()];
                for (int j = 0; j < header.size(); j++) {
                    if (ProductCategoryItem.NAME.equals(header.get(j))) {
                        temp[j] = getResultOrLongDash(item.getName());
                    } else if (ProductCategoryItem.DESCRIPTION.equals(header.get(j))) {
                        temp[j] = getResultOrLongDash(item.getDescription());
                    } else if (ProductCategoryItem.PARENT.equals(header.get(j))) {
                        temp[j] = getResultOrLongDash(item.getParentCategoryName());
                    } else if (ProductCategoryItem.STATUS.equals(header.get(j))) {
                        temp[j] = getResultOrLongDash(item.isActive() ? accountingLocalizer.localize(PdfLocalizationName.active) : commonLocalizer.localize(PdfLocalizationName.deactivate));
                    } else {
                        if (item.getCustomFieldsMap() != null && item.getCustomFieldsMap().get(header.get(j)) != null) {
                            if (item.getCustomFieldsMap().get(header.get(j)) instanceof Date) {
                                temp[j] = dateFormat((Date) item.getCustomFieldsMap().get(header.get(j)));
                            } else {
                                temp[j] = item.getCustomFieldsMap().get(header.get(j)) != null ? item.getCustomFieldsMap().get(header.get(j)).toString() : "";
                            }
                        }
                    }
                }
                tableList.addPdfTableRows(temp);
            }
        }
        pdfData.setListTable(tableList);
        return pdfData;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    public AccountingService getAccountingService() {
        return accountingService;
    }

    @Override
    protected String getTableName(Object dataClass) {
        return pdfWfmMessageSource.localize("productCategoriesList");
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    public void setAccountingService(AccountingService accountingService) {
        this.accountingService = accountingService;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFullName() + "_" + "Product_Categories_List" + dateFormat(new Date()));
    }
}
