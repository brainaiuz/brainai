package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset.FixedAssetItem;
import com.edatasite.workforce.gwt.accounting.server.app.FixedAssetServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Atabek Boboyev
 * Date: 17.01.12
 * Time: 11:22
 * To change this template use File | Settings | File Templates.
 */
public class FixedAssetListExcelHandler extends BaseExcelHandler {
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;

    private static final Logger log = LoggerFactory.getLogger(FixedAssetListExcelHandler.class);

    @Autowired
    private FixedAssetServiceLocal fixedAssetService;
    @Autowired
    private UserManager userManager2;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private PropertManager propertManager;
    private String sheetname;

    @Autowired
    @Qualifier("accountingLocalizer")
    protected WfmMessageSource accountingLocalizer;

    public void setExcelReferenceMessageSource(WfmResourceBundleMessageSource excelReferenceMessageSource) {
        this.excelReferenceMessageSource = excelReferenceMessageSource;
    }

    @Override
    protected void setFileName() {
        filename = "Fixed Assets";
    }

    @Override
    protected HSSFWorkbook getWorkBook(Object object) {

        String shortDateFormat = "MMM dd, yyyy";
        EdsUser user = userManager.getUser();
        EdsCompany edsCompany = user.getCompany();
        EdsCompanySettings companySettings = edsCompany.getCompanySettings();
        if (companySettings != null) {
            shortDateFormat = companySettings.getShortDateFormat();
        }
        ListingFilterParameter fp = (ListingFilterParameter) object;
        if (companySettings.getExcelLimit() != null && !"".equals(companySettings.getExcelLimit().trim()) && !"null".equals(companySettings.getExcelLimit().trim())) {
            fp.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
        } else {
            fp.setLimit(LIMIT_EXCEL_ROW);
        }
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        boolean isDepartmentRelationEnabled = financialSettings.getEnableAccountingDepartmentRelation();
        ListResult<FixedAssetItem> data = fixedAssetService.getFixedAssets(fp);

        List<FixedAssetItem> items = data.getList();
        ExcelData[] cellExcelDatas;
        ListPanelToolRpc panelTools = fp.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        if (header.contains("Action")) {
            header.remove("Action");
        } else {
            header.remove("action");
        }

        header.remove(FixedAssetItem.ACTION);
        Map<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(FixedAssetItem.NAME, accountingLocalizer.localize(PdfLocalizationName.name));
        mapColumnHeader.put(FixedAssetItem.DATE, accountingLocalizer.localize(PdfLocalizationName.purchaseDate));
        mapColumnHeader.put(FixedAssetItem.COST, commonLocalizer.localize(PdfLocalizationName.cost));
        mapColumnHeader.put(FixedAssetItem.RESIDUALVALUE, accountingLocalizer.localize(PdfLocalizationName.residualValue));
        mapColumnHeader.put(FixedAssetItem.ASSETLIFE, commonLocalizer.localize(PdfLocalizationName.useFulLife));
        mapColumnHeader.put(FixedAssetItem.CODE, commonLocalizer.localize(PdfLocalizationName.number));
        mapColumnHeader.put(FixedAssetItem.ACCOUNT, accountingLocalizer.localize(PdfLocalizationName.account));
        mapColumnHeader.put(FixedAssetItem.CATEGORY, commonLocalizer.localize(PdfLocalizationName.category));
        mapColumnHeader.put(FixedAssetItem.DESCRIPTION, accountingLocalizer.localize(PdfLocalizationName.description));
        mapColumnHeader.put(FixedAssetItem.LOCATION,propertManager.findByCode(Constants.LOCATION_PROPERTY_OBJECTNAME) != null ? propertManager.findByCode("LocListView").getSingular() : commonLocalizer.localize(PdfLocalizationName.location));
        mapColumnHeader.put(FixedAssetItem.OWNER, commonLocalizer.localize(PdfLocalizationName.owner));
        mapColumnHeader.put(FixedAssetItem.STATUS, commonLocalizer.localize(PdfLocalizationName.status));
        mapColumnHeader.put(FixedAssetItem.CALCULATE_DEPRECIATION,commonLocalizer.localize(PdfLocalizationName.calculateDepreciation));
        mapColumnHeader.put(CustomFormConstants.FIXED_ASSET_ACCOUNT,commonLocalizer.localize(PdfLocalizationName.accumulatedDepreciationAccount));
        mapColumnHeader.put(CustomFormConstants.EXPENSE_ACCOUNT,commonLocalizer.localize(PdfLocalizationName.depreciationExpenseAccount));

        if (isDepartmentRelationEnabled) {
            mapColumnHeader.put(FixedAssetItem.DEPARTMENT, commonLocalizer.localize(PdfLocalizationName.department));
        }

        setCustomFieldsPdfHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);

        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);
            EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
            sheetname = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.fixedAsset);

            List<ExcelData[]> list = new LinkedList<>();
            cellExcelDatas = new ExcelData[header.size()];

            list.add(generateOneRowWithValue(header.size() + 1, edsCompany.getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(header.size() + 1, sheetname, workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(header.size() + 1, ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.shortDateFormat(user.getUserDate(new Date()), user) + " Xolatiga ko'ra" : commonLocalizer.localize(PdfLocalizationName.asOF) + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            for (int i = 0; i < header.size(); i++) {
                cellExcelDatas[i] = new ExcelData(mapColumnHeader.get(header.get(i)), ExcelData.STRING, header.get(i).equals(FixedAssetItem.NAME) || header.get(i).equals(FixedAssetItem.DATE) ? 50 : 20, false, header.get(i).equals(FixedAssetItem.COST) || header.get(i).equals(FixedAssetItem.RESIDUALVALUE) || header.get(i).equals(FixedAssetItem.ASSETLIFE), ExcelData.NO_BORDER, ExcelData.HEADER);
            }

            Integer calculationScale = getCalculationScale(financialSettings);

            list.add(cellExcelDatas);
            String disposed = commonLocalizer.localize(PdfLocalizationName.disposed);
            String active = commonLocalizer.localize(PdfLocalizationName.active);
            String yes = commonLocalizer.localize(PdfLocalizationName.yes);
            String no = commonLocalizer.localize(PdfLocalizationName.no);
            for (FixedAssetItem assetItem : items) {
                String temp = "";
                cellExcelDatas = new ExcelData[header.size()];
                for (int j = 0; j < header.size(); j++) {
                    temp = "";
                    if (FixedAssetItem.NAME.equals(header.get(j))) {
                        cellExcelDatas[j] = new ExcelData(assetItem.getName() != null ? assetItem.getName() : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        continue;
                    } else if (FixedAssetItem.DATE.equals(header.get(j))) {
                        cellExcelDatas[j] = new ExcelData(ServerUtils.dateFormat(assetItem.getCreationDate() != null ? assetItem.getCreationDate().getNonConvertedDate() : null, shortDateFormat), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        continue;
                    } else if (FixedAssetItem.COST.equals(header.get(j))) {
                        cellExcelDatas[j] = new ExcelData(assetItem.getCost().setScale(calculationScale, RoundingMode.HALF_UP), ExcelData.CURRENCY, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        continue;
                    } else if (FixedAssetItem.RESIDUALVALUE.equals(header.get(j))) {
                        cellExcelDatas[j] = new ExcelData(assetItem.getResidualValue().setScale(calculationScale, RoundingMode.HALF_UP), ExcelData.CURRENCY, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        continue;
                    } else if (FixedAssetItem.ASSETLIFE.equals(header.get(j))) {
                        temp = assetItem.getUsefulLife() != null ? String.valueOf(assetItem.getUsefulLife()) : "";
                    } else if (FixedAssetItem.CODE.equals(header.get(j))) {
                        cellExcelDatas[j] = new ExcelData(assetItem.getCode() != null ? assetItem.getCode() : "", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        continue;
                    } else if (FixedAssetItem.ACCOUNT.equals(header.get(j))) {
                        temp = assetItem.getFinancedByAccount().getName() != null ? assetItem.getFinancedByAccount().getName() : "";
                    } else if (FixedAssetItem.CATEGORY.equals(header.get(j))) {
                        temp = assetItem.getAccount().getName() != null ? assetItem.getAccount().getName() : "";
                    } else if (FixedAssetItem.DESCRIPTION.equals(header.get(j))) {
                        temp = assetItem.getDescription() != null ? assetItem.getDescription() : "";
                    } else if (FixedAssetItem.LOCATION.equals(header.get(j))) {
                        temp = assetItem.getLocationName() != null ? assetItem.getLocationName() : "";
                    } else if (FixedAssetItem.OWNER.equals(header.get(j))) {
                        temp = assetItem.getOwner() != null ? assetItem.getOwner().getName() : "";
                    } else if (FixedAssetItem.DEPARTMENT.equals(header.get(j))) {
                        temp = assetItem.getDepartment() != null ? assetItem.getDepartment().getName() : "";
                    } else if (FixedAssetItem.STATUS.equals(header.get(j))) {
                        temp = assetItem.getDisposed() ? disposed : active;
                    } else if (FixedAssetItem.CALCULATE_DEPRECIATION.equals(header.get(j))) {
                        temp = assetItem.isCalculateDepreciation() ? yes : no;
                    } else if (CustomFormConstants.FIXED_ASSET_ACCOUNT.equals(header.get(j))) {
                        temp = assetItem.getFixedAssetAccount() != null ? assetItem.getFixedAssetAccount().getName() != null ? assetItem.getFixedAssetAccount().getName() : "" : "";
                    } else if (CustomFormConstants.EXPENSE_ACCOUNT.equals(header.get(j))) {
                        temp = assetItem.getExpenseAccount() != null ? assetItem.getExpenseAccount().getName() != null ? assetItem.getExpenseAccount().getName() : "" : "";
                    } else {
                        if (assetItem.getCustomFieldsMap() != null && assetItem.getCustomFieldsMap().get(header.get(j)) != null) {
                            if (assetItem.getCustomFieldsMap().get(header.get(j)) instanceof Date) {
                                temp = dateFormat((Date) assetItem.getCustomFieldsMap().get(header.get(j)));
                            } else {
                                temp = assetItem.getCustomFieldsMap().get(header.get(j)) != null ? assetItem.getCustomFieldsMap().get(header.get(j)).toString() : "";
                            }
                        }
                    }
                    cellExcelDatas[j] = new ExcelData(temp, ExcelData.STRING, 20, false, header.get(j).equals(FixedAssetItem.DESCRIPTION), ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                list.add(cellExcelDatas);
            }
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, header.size());

        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Cannot generate Fixed Asset List report, exception: " + ex);
        }
        return null;
    }

    protected String dateFormat(Date date) {
        Date newDate = (Date) date.clone();
        newDate.setMinutes(newDate.getMinutes() + userManager2.getUser().getUserTimezone().getRawOffset() / 60000);
        return ServerUtils.shortDateFormat(newDate, userManager2.getUser());
    }

    public void setCustomFieldsPdfHeaderMap(List<CompanyCustomFieldItem> customfields, Map<String, String> pdfHeader) {
        if (customfields != null) {
            for (CompanyCustomFieldItem field : customfields) {
                pdfHeader.put(field.getColumnCode(), field.getFieldName());
            }
        }
    }


}
