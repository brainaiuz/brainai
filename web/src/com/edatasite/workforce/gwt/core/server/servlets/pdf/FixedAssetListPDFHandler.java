package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset.FixedAssetItem;
import com.edatasite.workforce.gwt.accounting.server.app.FixedAssetServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfTemplateEvent;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 9/12/11
 * Time: 7:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class FixedAssetListPDFHandler extends AbstractITextPostPdfHandler {

    @Autowired
    private FixedAssetServiceLocal fixedAssetService;
    @Autowired
    private GenericSettingsManager genericSettingsManager;

    @Autowired
    @Qualifier("accountingLocalizer")
    protected WfmMessageSource accountingLocalizer;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    private CellData[] getTableHeaders(ListingFilterParameter fp) {

        EdsUser user = userManager.getUser();
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        boolean isDepartmentRelationEnabled = fs.getEnableAccountingDepartmentRelation();

        Map<String, CellData> mapColumnHeader = new HashMap<>();

        ListPanelToolRpc panelTools = fp.getListPanelTool();

        mapColumnHeader.put(FixedAssetItem.NAME, new CellData(accountingLocalizer.localize(PdfLocalizationName.name), Element.ALIGN_LEFT));
        mapColumnHeader.put(FixedAssetItem.DATE, new CellData(accountingLocalizer.localize(PdfLocalizationName.purchaseDate), Element.ALIGN_LEFT));
        mapColumnHeader.put(FixedAssetItem.COST, new CellData(commonLocalizer.localize(PdfLocalizationName.cost), Element.ALIGN_RIGHT));
        mapColumnHeader.put(FixedAssetItem.RESIDUALVALUE, new CellData(accountingLocalizer.localize(PdfLocalizationName.residualValue), Element.ALIGN_RIGHT));
        mapColumnHeader.put(FixedAssetItem.ASSETLIFE, new CellData(commonLocalizer.localize(PdfLocalizationName.useFulLife), Element.ALIGN_LEFT));
        mapColumnHeader.put(FixedAssetItem.CODE, new CellData(accountingLocalizer.localize(PdfLocalizationName.code), Element.ALIGN_LEFT));
        mapColumnHeader.put(FixedAssetItem.ACCOUNT, new CellData(accountingLocalizer.localize(PdfLocalizationName.account), Element.ALIGN_LEFT));
        mapColumnHeader.put(FixedAssetItem.CATEGORY, new CellData(commonLocalizer.localize(PdfLocalizationName.category), Element.ALIGN_LEFT));
        mapColumnHeader.put(FixedAssetItem.DESCRIPTION, new CellData(accountingLocalizer.localize(PdfLocalizationName.description), Element.ALIGN_LEFT));
        mapColumnHeader.put(FixedAssetItem.LOCATION, new CellData(propertManager.findByCode(Constants.LOCATION_PROPERTY_OBJECTNAME) != null ? propertManager.findByCode("LocListView").getSingular() : commonLocalizer.localize(PdfLocalizationName.location), Element.ALIGN_LEFT));
        mapColumnHeader.put(FixedAssetItem.OWNER, new CellData(commonLocalizer.localize(PdfLocalizationName.owner), Element.ALIGN_LEFT));
        mapColumnHeader.put(FixedAssetItem.STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
        mapColumnHeader.put(CustomFormConstants.FIXED_ASSET_ACCOUNT,new CellData(commonLocalizer.localize(PdfLocalizationName.accumulatedDepreciationAccount)));
        mapColumnHeader.put(CustomFormConstants.EXPENSE_ACCOUNT,new CellData(commonLocalizer.localize(PdfLocalizationName.depreciationExpenseAccount)));

        if (isDepartmentRelationEnabled) {
            mapColumnHeader.put(FixedAssetItem.DEPARTMENT, new CellData(commonLocalizer.localize(PdfLocalizationName.department), Element.ALIGN_LEFT));
        }


        CustomFieldsUtils.setCustomFieldsPdfHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);

        List<CellData> header = new ArrayList<>();
        header.add(new CellData(accountingLocalizer.localize(PdfLocalizationName.number), Element.ALIGN_LEFT));
        for (String columnCode : panelTools.getColumnCodeName()) {
            if (mapColumnHeader.containsKey(columnCode)) {
                header.add(mapColumnHeader.get(columnCode));
            }
        }
        return header.toArray(new CellData[0]);
    }


    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        CellData[] headers = getTableHeaders(filterParametrs);
        ITextTableList tableList = new ITextTableList(headers.length);

        EdsUser user = uploadManager.getUser();
        filterParametrs.setLimit(1000);

        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();

        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();

        ListResult<FixedAssetItem> fixedAssets = fixedAssetService.getFixedAssets(filterParametrs);

        List<FixedAssetItem> items = fixedAssets.getList();
        tableList.addPdfTableHeader(headers);
        tableList.addTableWidthPercentage(0.35f, 0.2f, 0.15f, 0.15f, 0.15f);
        int count = 1;
        SimpleDateFormat shortDateFormat = new SimpleDateFormat(ServerUtils.getShortDateFormat(user));


        DecimalFormat priceScaleFormat = getPriceScaleNumberFormat(fs);
        String disposed = commonLocalizer.localize(PdfLocalizationName.disposed);
        String active = commonLocalizer.localize(PdfLocalizationName.active);

        for (FixedAssetItem assetItem : items) {
            String counter = String.valueOf(count);
            Map<String, CellData> columnMap = new HashMap<>();
            if (panelTools.getColumnCodeName().contains(FixedAssetItem.NAME)) {
                columnMap.put(FixedAssetItem.NAME, new CellData(getResultOrLongDash(assetItem.getName()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(FixedAssetItem.DATE)) {
                columnMap.put(FixedAssetItem.DATE, new CellData(shortDateFormat.format(assetItem.getCreationDate().getNonConvertedDate()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(FixedAssetItem.COST)) {
                columnMap.put(FixedAssetItem.COST, new CellData(priceScaleFormat.format(assetItem.getCost()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(FixedAssetItem.RESIDUALVALUE)) {
                columnMap.put(FixedAssetItem.RESIDUALVALUE, new CellData(getResultOrLongDash(priceScaleFormat.format(assetItem.getResidualValue())), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(FixedAssetItem.ASSETLIFE)) {
                columnMap.put(FixedAssetItem.ASSETLIFE, assetItem.getUsefulLife() != null ? new CellData(String.valueOf(assetItem.getUsefulLife()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(FixedAssetItem.CODE)) {
                columnMap.put(FixedAssetItem.CODE, new CellData(getResultOrLongDash(assetItem.getCode()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(FixedAssetItem.ACCOUNT)) {
                columnMap.put(FixedAssetItem.ACCOUNT, assetItem.getFinancedByAccount() != null ? new CellData(getResultOrLongDash(assetItem.getFinancedByAccount().getName()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(FixedAssetItem.CATEGORY)) {
                columnMap.put(FixedAssetItem.CATEGORY, assetItem.getAccount() != null ? new CellData(getResultOrLongDash(assetItem.getAccount().getName()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(FixedAssetItem.DESCRIPTION)) {
                columnMap.put(FixedAssetItem.DESCRIPTION, new CellData(getResultOrLongDash(assetItem.getDescription()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(FixedAssetItem.LOCATION)) {
                columnMap.put(FixedAssetItem.LOCATION, new CellData(getResultOrLongDash(assetItem.getLocationName()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(FixedAssetItem.OWNER)) {
                columnMap.put(FixedAssetItem.OWNER, assetItem.getOwner() != null ? new CellData(getResultOrLongDash(assetItem.getOwner().getName()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(FixedAssetItem.DEPARTMENT)) {
                columnMap.put(FixedAssetItem.DEPARTMENT, assetItem.getDepartment() != null ? new CellData(getResultOrLongDash(assetItem.getDepartment().getName()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(FixedAssetItem.STATUS)) {
                columnMap.put(FixedAssetItem.STATUS, new CellData(getResultOrLongDash(assetItem.getDisposed() ? disposed : active), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(CustomFormConstants.FIXED_ASSET_ACCOUNT)) {
                columnMap.put(CustomFormConstants.FIXED_ASSET_ACCOUNT, new CellData(assetItem.getFixedAssetAccount() != null ? assetItem.getFixedAssetAccount().getName() != null ? assetItem.getFixedAssetAccount().getName() : "" : ""));
            }
            if (panelTools.getColumnCodeName().contains(CustomFormConstants.EXPENSE_ACCOUNT)) {
                columnMap.put(CustomFormConstants.EXPENSE_ACCOUNT, new CellData(assetItem.getExpenseAccount() != null ? assetItem.getExpenseAccount().getName() != null ? assetItem.getExpenseAccount().getName() : "" : ""));
            }


            CustomFieldsUtils.setCustomFieldsPdfTableRows(panelTools.getListViewCustomFields(), columnMap, panelTools.getColumnCodeName(), assetItem, company);

            List<CellData> column = new ArrayList<>();
            column.add(new CellData(counter, Element.ALIGN_LEFT));
            for (String columnCode : panelTools.getColumnCodeName()) {
                if (columnMap.containsKey(columnCode)) {
                    column.add(columnMap.get(columnCode));
                }
            }
            tableList.addPdfTableRows(column.toArray(new CellData[0]));

            count++;
        }

        pdfData.setListTable(tableList);
        return pdfData;
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        return property != null ? property.getPlural() : pdfWfmMessageSource.localize("fixedAssets");
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName("Fixed Asset Register");
    }

    @Override
    protected void initPagingAndStamper(PdfReader pdfReader, PdfStamper pdfStamper, Document document, ITextPdfTemplateEvent iTextPdfTemplateEvent, Object dataClass) throws DocumentException {
        audingPdfFooterSignature(pdfReader, pdfStamper, document);
        super.initPagingAndStamper(pdfReader, pdfStamper, document, iTextPdfTemplateEvent, dataClass);
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }
}
