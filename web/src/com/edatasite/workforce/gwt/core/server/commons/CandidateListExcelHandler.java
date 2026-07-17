package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.hrms.client.rpc.RecruitmentService;
import com.lowagie.text.Element;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * User: Babayev xushnud
 * Date: 7/26/12
 * Time: 2:57 PM
 */
public class CandidateListExcelHandler extends BaseExcelHandler {

    private static final Logger log = LoggerFactory.getLogger(CandidateListExcelHandler.class.getName());

    @Autowired
    private RecruitmentService recruitmentService;
    @Autowired
    private PropertManager propertManager;
    private String sheetName;
    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }

    protected HSSFWorkbook getWorkBook(Object dataClass) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        ListResult<ContactListItem> ContactListItemListResult = recruitmentService.listCandidates(filterParametrs);
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        ExcelData[] cellDatas;
        Map<String, ExcelData> mapColumnHeader = new HashMap<>();
        EdsUser user = userManager.getUser();
        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);
            EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
            sheetName = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.candidates);
            List<ExcelData[]> list = new LinkedList<>();
            mapColumnHeader.put(ContactListItem.CONTACT_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.name), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ContactListItem.CONTACT_ID, new ExcelData(commonLocalizer.localize(PdfLocalizationName.number), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ContactListItem.LEAD_STATUS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.status), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ContactListItem.DATE_OF_BIRTH, new ExcelData(commonLocalizer.localize(PdfLocalizationName.dateOfBirth), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ContactListItem.EMAIL, new ExcelData(commonLocalizer.localize(PdfLocalizationName.email), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ContactListItem.PHONE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.phone), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ContactListItem.OWNER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.owner), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ContactListItem.PROJECT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.project), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ContactListItem.LEAD_SOURCE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.source), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ContactListItem.CANDIDATE_SKILLS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.skills), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ContactListItem.CREATED_BY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.createdBy), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ContactListItem.LAST_MODIFIED, new ExcelData(commonLocalizer.localize(PdfLocalizationName.modifiedDate), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ContactListItem.CREATION_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.createdDate), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ContactListItem.VACANCIES, new ExcelData(commonLocalizer.localize(PdfLocalizationName.vacancy), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ContactListItem.UPDATED_BY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.modifiedBy), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));

            CustomFieldsUtils.setCustomFieldsExcelHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);

            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), user.getCompany().getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), sheetName, workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + " Xolatiga ko'ra" : excelReferenceMessageSource.localize("EPAsOf", " As Of") + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            List<ExcelData> excelDataList = new ArrayList<>();
            for (String columnName : panelTools.getColumnCodeName()) {
                if (mapColumnHeader.containsKey(columnName)) {
                    excelDataList.add(mapColumnHeader.get(columnName));
                }
            }
            cellDatas = new ExcelData[excelDataList.size()];
            excelDataList.toArray(cellDatas);
            list.add(cellDatas);

            for (ContactListItem item : ContactListItemListResult.getList()) {
                Map<String, ExcelData> mapColumns = new HashMap<>();

                if (panelTools.getColumnCodeName().contains(ContactListItem.CONTACT_NAME)) {
                    mapColumns.put(ContactListItem.CONTACT_NAME, new ExcelData(item.getName() != null ? item.getName() : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                if (panelTools.getColumnCodeName().contains(ContactListItem.CONTACT_ID)) {
                    mapColumns.put(ContactListItem.CONTACT_ID, new ExcelData(item.getNumberData() != null ? item.getNumberData().getNumberString() : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                if (panelTools.getColumnCodeName().contains(ContactListItem.LEAD_STATUS)) {
                    mapColumns.put(ContactListItem.LEAD_STATUS, new ExcelData(item.getCandidateStatus() != null ? item.getCandidateStatus().getName() : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                if (panelTools.getColumnCodeName().contains(ContactListItem.DATE_OF_BIRTH)) {
                    mapColumns.put(ContactListItem.DATE_OF_BIRTH, new ExcelData(item.getBirthDate() != null ? (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat(item.getBirthDate().getDate())) : dateFormat(item.getBirthDate().getDate())) : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                if (panelTools.getColumnCodeName().contains(ContactListItem.EMAIL)) {
                    mapColumns.put(ContactListItem.EMAIL, new ExcelData(item.getPrimaryEmail() != null ? item.getPrimaryEmail() : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                if (panelTools.getColumnCodeName().contains(ContactListItem.PHONE)) {
                    mapColumns.put(ContactListItem.PHONE, new ExcelData(item.getPrimaryPhone() != null ? getPhoneCallFormat(item.getPrimaryPhone()) : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                if (panelTools.getColumnCodeName().contains(ContactListItem.OWNER)) {
                    mapColumns.put(ContactListItem.OWNER, new ExcelData(item.getOwner() != null ? item.getOwner() : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ContactListItem.PROJECT)) {
                    mapColumns.put(ContactListItem.PROJECT, new ExcelData(item.getProjectItem() != null ? item.getProjectItem().getName() : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                if (panelTools.getColumnCodeName().contains(ContactListItem.LEAD_SOURCE)) {
                    mapColumns.put(ContactListItem.LEAD_SOURCE, new ExcelData(item.getCandidateSource() != null ? item.getCandidateSource().getName() : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ContactListItem.CANDIDATE_SKILLS)) {
                    mapColumns.put(ContactListItem.CANDIDATE_SKILLS, new ExcelData(item.getSkills() != null ? item.getSkills() : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (mapColumnHeader.containsKey(ContactListItem.CREATED_BY)) {
                    mapColumns.put(ContactListItem.CREATED_BY, new ExcelData(item.getCreator() != null ? item.getCreator() : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (mapColumnHeader.containsKey(ContactListItem.LAST_MODIFIED)) {
                    mapColumns.put(ContactListItem.LAST_MODIFIED, new ExcelData(item.getUpdatedDate() != null ? (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(longDateFormat(item.getUpdatedDate())) : longDateFormat(item.getUpdatedDate())) : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (mapColumnHeader.containsKey(ContactListItem.CREATION_DATE)) {
                    mapColumns.put(ContactListItem.CREATION_DATE, new ExcelData(item.getCreatedDate() != null ? (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(longDateFormat(item.getCreatedDate())) : longDateFormat(item.getCreatedDate())) : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (mapColumnHeader.containsKey(ContactListItem.UPDATED_BY)) {
                    mapColumns.put(ContactListItem.UPDATED_BY, new ExcelData((item.getUpdater()) != null ? (item.getUpdater()) : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ContactListItem.VACANCIES)) {
                    if (item.getVacancies() != null) {
                        StringBuilder vacancies = new StringBuilder();
                        int i = 0;
                        for (SelectItem selectItem : item.getVacancies()) {
                            vacancies.append(selectItem.getName());
                            if (i != item.getVacancies().size() - 1) {
                                vacancies.append(",\n");
                                i++;
                            }
                        }
                        mapColumns.put(ContactListItem.VACANCIES, new ExcelData(vacancies.toString(), ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else {
                        mapColumns.put(ContactListItem.VACANCIES, new ExcelData("", Element.ALIGN_LEFT));
                    }
                }

                CustomFieldsUtils.setCustomFieldsExcelTableRows(panelTools.getListViewCustomFields(), mapColumns, panelTools.getColumnCodeName(), item, user.getCompany());

                excelDataList = new ArrayList<>();
                for (String columnName : panelTools.getColumnCodeName()) {
                    if (mapColumns.containsKey(columnName)) {
                        excelDataList.add(mapColumns.get(columnName));
                    }
                }
                cellDatas = new ExcelData[excelDataList.size()];
                excelDataList.toArray(cellDatas);
                list.add(cellDatas);
            }
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, mapColumnHeader.size());


        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate " + "CandidateList excel report, exception: " + e);
        }
        return null;
    }

    protected EdsUser getUser() {
        return userManager.getUser();
    }

    @Override
    protected void setFileName() {
        this.filename = "CandidateList";
    }

    public static String getPhoneCallFormat(String phNumber) {
        if (phNumber != null && !"".equals(phNumber)) {
            if ("n/a".equals(phNumber.toLowerCase().trim())) {
                phNumber = "";
            }
        }
        String s = cleanPhoneNumber(phNumber);
        if (phNumber != null && s != null) {
            phNumber = phNumber.replace("|", " ");
            if (!phNumber.startsWith("+")) {
                phNumber = "+" + phNumber;
            }
            return phNumber;
        } else {
            return "";
        }
    }

    public static String cleanPhoneNumber(String phone) {

        if (phone == null || "".equals(phone)) {
            return null;
        }
        phone = phone.replace("(", "");
        phone = phone.replace(")", "");
        phone = phone.replace("-", "");
        phone = phone.replace(" ", "");
        phone = phone.replace("|", "");
        phone = phone.replaceAll("||", "");
        phone = phone.replace("+", "");
        phone = "+" + phone;

        return phone;
    }
}
