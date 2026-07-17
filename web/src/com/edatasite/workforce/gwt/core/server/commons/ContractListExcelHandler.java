package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.project.client.rpc.ContractListItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Muhammad on 20.02.2016.
 */
public class ContractListExcelHandler extends BaseExcelHandler {

    @Autowired
    private ProjectService projectService;

    private static final Logger log = LoggerFactory.getLogger(ContractListExcelHandler.class.getName());
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;

    @Autowired
    private UserManager userManager;

    @Override
    protected void setFileName() {
        EdsUser user = userManager.getUser();
        filename = user.getFirstName() + "_" + user.getLastName() + "_ContractList_" + dateFormat(user.getUserDate());
        filename = filename.replace("/","_");
    }

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }


    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter fp = (ListingFilterParameter) object;
        fp.setAllByFilter(false);
        EdsUser user = userManager.getUser();
        ListLoadConfig config = new ListLoadConfig();
        config.setSortField(fp.getSortField());

        EdsCompany company = user.getCompany();
        EdsCompanySettings companySettings = company.getCompanySettings();

        if (companySettings.getExcelLimit() != null && !"".equals(companySettings.getExcelLimit())) {
            fp.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
            config.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
        } else {
            fp.setLimit(LIMIT_EXCEL_ROW);
            config.setLimit(LIMIT_EXCEL_ROW);
        }

        ListResult<ContractListItem> contractList = projectService.getContractList(fp);
        ListPanelToolRpc panelTools = fp.getListPanelTool();

        ExcelData[] cellDatas;
        Map<String, ExcelData> mapColumnData = new HashMap<>();
        try{
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);

            List<ExcelData[]> list = new LinkedList<>();

            mapColumnData.put(ContractListItem.NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.number),ExcelData.STRING, 20, true, true, ExcelData.NO_BORDER,ExcelData.HEADER));
            mapColumnData.put(ContractListItem.CLIENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.customer),ExcelData.STRING, 35, true, true, ExcelData.NO_BORDER,ExcelData.HEADER));
            mapColumnData.put(ContractListItem.PROJECT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.project),ExcelData.STRING, 35, true, true, ExcelData.NO_BORDER,ExcelData.HEADER));
            mapColumnData.put(ContractListItem.LAST_NOTE_COMMENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.notes),ExcelData.STRING, 40, true, true, ExcelData.NO_BORDER,ExcelData.HEADER));
            mapColumnData.put(ContractListItem.CONTRACT_START_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.contractStart),ExcelData.STRING, 20, true, true, ExcelData.NO_BORDER,ExcelData.HEADER));
            mapColumnData.put(ContractListItem.CONTRACT_END_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.contractEnd), ExcelData.STRING, 20, true, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(ContractListItem.CONTRACT_REGISTRATION_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.dateOfRegistration), ExcelData.STRING, 20, true, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            CustomFieldsUtils.setCustomFieldsExcelHeaderMap(panelTools.getListViewCustomFields(), mapColumnData);

            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), company.getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), commonLocalizer.localize(PdfLocalizationName.contract), workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertDateFormatFromEngToUzb(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + " Xolatiga ko'ra" : commonLocalizer.localize(PdfLocalizationName.asOF) + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            List<ExcelData> excelDataList = new ArrayList<>();

            for (String coulmnName : panelTools.getColumnCodeName()){
                if(mapColumnData.containsKey(coulmnName)){
                    excelDataList.add(mapColumnData.get(coulmnName));
                }
            }
            cellDatas = new ExcelData[excelDataList.size()];
            excelDataList.toArray(cellDatas);
            list.add(cellDatas);

            for (ContractListItem contract : contractList.getList()){
                Map<String, ExcelData> mapColumn = new HashMap<>();
                if (panelTools.getColumnCodeName().contains(ContractListItem.NUMBER)) {
                    mapColumn.put(ContractListItem.NUMBER, new ExcelData(contract.getNumber() != null ? contract.getNumber() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ContractListItem.CLIENT)) {
                    mapColumn.put(ContractListItem.CLIENT, new ExcelData(contract.getClient() != null ? contract.getClient() : "", ExcelData.STRING, 35, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));

                }
                if (panelTools.getColumnCodeName().contains(ContractListItem.PROJECT)) {
                    mapColumn.put(ContractListItem.PROJECT, new ExcelData(contract.getProject() != null ? contract.getProject() : "", ExcelData.STRING, 35, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));

                }
                if (panelTools.getColumnCodeName().contains(ContractListItem.LAST_NOTE_COMMENT)) {
                    mapColumn.put(ContractListItem.LAST_NOTE_COMMENT, new ExcelData(contract.getLastNoteComment() != null ? contract.getLastNoteComment() : "", ExcelData.STRING, 40, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));

                }
                if (panelTools.getColumnCodeName().contains(ContractListItem.CONTRACT_START_DATE)) {
                    mapColumn.put(ContractListItem.CONTRACT_START_DATE, new ExcelData(contract.getContractBeginDate() != null ? dateFormat(user.getUserDate(contract.getContractBeginDate().getNonConvertedDate()), true) : "N/A", ExcelData.STRING, 20, true, true, ExcelData.NO_BORDER, ExcelData.NORMAL));

                }
                if (panelTools.getColumnCodeName().contains(ContractListItem.CONTRACT_END_DATE)) {
                    mapColumn.put(ContractListItem.CONTRACT_END_DATE, new ExcelData(contract.getContractEndDate() != null ? dateFormat(user.getUserDate(contract.getContractEndDate().getNonConvertedDate()), true) : "N/A", ExcelData.STRING, 20, true, true, ExcelData.NO_BORDER, ExcelData.NORMAL));

                }
                if (panelTools.getColumnCodeName().contains(ContractListItem.CONTRACT_REGISTRATION_DATE)) {
                    mapColumn.put(ContractListItem.CONTRACT_REGISTRATION_DATE, new ExcelData(contract.getCreationTime() != null ? dateFormat(user.getUserDate(contract.getCreationTime().getNonConvertedDate()), true) : "N/A", ExcelData.STRING, 20, true, true, ExcelData.NO_BORDER, ExcelData.NORMAL));

                }
                CustomFieldsUtils.setCustomFieldsExcelTableRows(panelTools.getListViewCustomFields(), mapColumn, panelTools.getColumnCodeName(), contract, company);
                excelDataList = new ArrayList<>();
                for (String columnName : panelTools.getColumnCodeName()){
                    if(mapColumn.containsKey(columnName)){
                        excelDataList.add(mapColumn.get(columnName));
                    }
                }
                cellDatas = new ExcelData[excelDataList.size()];
                excelDataList.toArray(cellDatas);
                list.add(cellDatas);
            }
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, 6);
        }catch (Exception ex) {
            ex.printStackTrace();
            log.error("Cannot generate contract list excel report, exception: " + ex);
        }

        return null;
    }

    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    public void setExcelReferenceMessageSource(WfmResourceBundleMessageSource excelReferenceMessageSource) {
        this.excelReferenceMessageSource = excelReferenceMessageSource;
    }
}
