package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCompanyCustomFieldsSettings;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CompanyCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Aug 10, 2009
 * Time: 8:08:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrmAccountsExcelHandler extends BaseExcelHandler {

    private static final Logger log = LoggerFactory.getLogger(CrmAccountsExcelHandler.class);
    @Autowired
    private CRMService crmService;
    @Autowired
    private UserManager userManager;
    @Qualifier("companyCFSettingsManager")
    @Autowired
    private CompanyCustomFieldsManager companyCFSettingsManager;
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

    protected void setFileName() {
        EdsUser user = getUser();
        filename = user.getFirstName() + "_" + user.getLastName() + "_CrmAccountList_" + dateFormat(user.getUserDate());
        filename = filename.replace("/", "_");
        if (filename.length() > 31) {
            filename = filename.substring(0, 31);}
    }


    protected EdsUser getUser() {
        return userManager.getUser();
    }

    protected String dateFormat(Date date) {
        return ServerUtils.shortDateFormat(date, userManager.getUser());
    }

    protected String longDateFormat(Date date) {
        return ServerUtils.longDateFormat(date, userManager.getUser());
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
        if (filterParametrs.getPropertyCode().equals("supplierList")) {
            sheetName = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.supplierCenter);
        } else if (filterParametrs.getPropertyCode().equals("clientList")) {
            sheetName = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.customersCenter);
        } else if (filterParametrs.getPropertyCode().equals("accountList")) {
            sheetName = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.companies);
        }
        EdsUser user = getUser();
        EdsCompany edsCompany = user.getCompany();
        EdsCompanySettings companySettings = edsCompany.getCompanySettings();

        if (companySettings.getExcelLimit() != null && !"".equals(companySettings.getExcelLimit())) {
            filterParametrs.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
        } else {
            filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        }

        ListResult<CrmAccountItem> accountList = getList(filterParametrs);
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        ExcelData[] cellDatas;

        List<ExcelData[]> list = new LinkedList<>();
        Map<String, ExcelData> mapColumnHeader = getColumnHeaderMap();
        if (panelTools.isCustomFieldsShown()) {
            CustomFieldsUtils.setCustomFieldsExcelHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);
        }

        WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
        workBook.setSheetName(filename);

        list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), edsCompany.getName(), workBook.getSheet(), 0));
        list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), sheetName, workBook.getSheet(), 1));
        list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + " Xolatiga ko'ra" : " " + commonLocalizer.localize(PdfLocalizationName.asOF) + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

        List<ExcelData> excelDataList = new ArrayList<>();
        for (String columnName : panelTools.getColumnCodeName()) {
            if (mapColumnHeader.containsKey(columnName)) {
                excelDataList.add(getExcelDataHeader(mapColumnHeader.get(columnName)));
            }
        }
        cellDatas = new ExcelData[excelDataList.size()];
        excelDataList.toArray(cellDatas);
        list.add(cellDatas);

        try {
            for (CrmAccountItem item : accountList.getList()) {
                Map<String, ExcelData> mapColumns = new HashMap<>();
                BigDecimal clientBalance = BigDecimal.ZERO;
                BigDecimal supplierBalance = BigDecimal.ZERO;
                BigDecimal creditLimit = BigDecimal.ZERO;
                String vatNumber = !ServerUtils.isNullOrEmpty(item.getTrn()) ? item.getTrn() : item.getVatNumber();

                if (item.getClientBalance() != null) {
//                    if (item.getClientBalance() >= 0) {
//                        clientBalance = numberFormat.format(item.getClientBalance());
//                    } else {
//                        clientBalance = "(" + numberFormat.format((-1) * item.getClientBalance()) + ")";
//                    }
                    clientBalance = BigDecimal.valueOf(item.getClientBalance());
                }
                if (item.getSupplierBalance() != null) {
//                    if (item.getSupplierBalance() >= 0) {
//                        supplierBalance = numberFormat.format(item.getSupplierBalance());
//                    } else {
//                        supplierBalance = "(" + numberFormat.format((-1) * item.getSupplierBalance()) + ")";
//                    }
                    supplierBalance = BigDecimal.valueOf(item.getSupplierBalance());
                }
                if (item.getCreditLimit() != null) {
                    creditLimit = item.getCreditLimit();
                }
                if (panelTools.getColumnCodeName().contains(CrmAccountItem.OWNER)) {
                    //mapColumns.put(CrmAccountItem.OWNER, refactor(item.getOwnerName()));
                    mapColumns.put(CrmAccountItem.OWNER, refactor(item.getOwnerNames()));
                }
                if (panelTools.getColumnCodeName().contains(CrmAccountItem.PARENT_ACCOUNT_NAME)) {
                    mapColumns.put(CrmAccountItem.PARENT_ACCOUNT_NAME, refactor(item.getParent() != null ? item.getParent().getName() : null));
                }
                if (panelTools.getColumnCodeName().contains(CrmAccountItem.ACCOUNT_NAME)) {
                    mapColumns.put(CrmAccountItem.ACCOUNT_NAME, refactor(item.getName()));
                }
                if (panelTools.getColumnCodeName().contains(CrmAccountItem.ACCOUNT_NUMBER)) {
                    mapColumns.put(CrmAccountItem.ACCOUNT_NUMBER, refactor(item.getNumber()));
                }
                if (panelTools.getColumnCodeName().contains(CrmAccountItem.PHONE)) {
                    mapColumns.put(CrmAccountItem.PHONE, refactor(Objects.nonNull(item.getPhone()) ? item.getPhone().replace("|", "") : null));
                }
                if (panelTools.getColumnCodeName().contains(CrmAccountItem.FAX)) {
                    mapColumns.put(CrmAccountItem.FAX, refactor(item.getFax()));
                }
                if (panelTools.getColumnCodeName().contains(CrmAccountItem.WEBSITE)) {
                    mapColumns.put(CrmAccountItem.WEBSITE, refactor(item.getWebsite()));
                }
                if (panelTools.getColumnCodeName().contains(CrmAccountItem.EMAIL)) {
                    mapColumns.put(CrmAccountItem.EMAIL, refactor(item.getEmail()));
                }
                if (panelTools.getColumnCodeName().contains(CrmAccountItem.ACCOUNT_TYPE)) {
                    mapColumns.put(CrmAccountItem.ACCOUNT_TYPE, refactor(ServerUtils.getSelectItemsAsCommaDelimeted(item.getAccountTypes(), true)));
                }
                if (panelTools.getColumnCodeName().contains(CrmAccountItem.BANK_ACCOUNT)) {
                    mapColumns.put(CrmAccountItem.BANK_ACCOUNT, refactor(item.getBankAccount()));
                }
                if (panelTools.getColumnCodeName().contains(CrmAccountItem.CONTACT_NAME)) {
                    mapColumns.put(CrmAccountItem.CONTACT_EMAIL, refactor(item.getPrimaryContactEmail()));
                }

                Address billAddress = item.getDefaultAddress(true);
                Address mailAddress = item.getDefaultAddress(false);
                if (panelTools.getColumnCodeName().contains(CrmAccountItem.BILLING_ADDRESS)) {
                    mapColumns.put(CrmAccountItem.BILLING_ADDRESS, refactor(billAddress.getAddress()));
                }
                if (panelTools.getColumnCodeName().contains(CrmAccountItem.BILLING_ADDRESS2)) {
                    mapColumns.put(CrmAccountItem.BILLING_ADDRESS2, refactor(billAddress.getAddressb()));
                }
                if (panelTools.getColumnCodeName().contains(CrmAccountItem.CITY)) {
                    mapColumns.put(CrmAccountItem.CITY, refactor(billAddress.getCity()));
                }
                if (panelTools.getColumnCodeName().contains(CrmAccountItem.COUNTRY)) {
                    mapColumns.put(CrmAccountItem.COUNTRY, refactor(billAddress.getCountry()));
                }
                if (panelTools.getColumnCodeName().contains(CrmAccountItem.STATE)) {
                    mapColumns.put(CrmAccountItem.STATE, refactor(billAddress.getState()));
                }
                if (panelTools.getColumnCodeName().contains(CrmAccountItem.POST_CODE)) {
                    mapColumns.put(CrmAccountItem.POST_CODE, refactor(billAddress.getZipCode()));
                }
                if (panelTools.getColumnCodeName().contains(CrmAccountItem.MAILING_ADDRESS)) {
                    mapColumns.put(CrmAccountItem.MAILING_ADDRESS, refactor(mailAddress.getAddress()));
                }
                if (panelTools.getColumnCodeName().contains(CrmAccountItem.MAILING_ADDRESS2)) {
                    mapColumns.put(CrmAccountItem.MAILING_ADDRESS2, refactor(mailAddress.getAddressb()));
                }
                if (panelTools.getColumnCodeName().contains(CrmAccountItem.CITY2)) {
                    mapColumns.put(CrmAccountItem.CITY2, refactor(mailAddress.getCity()));
                }
                if (panelTools.getColumnCodeName().contains(CrmAccountItem.COUNTRY2)) {
                    mapColumns.put(CrmAccountItem.COUNTRY2, refactor(mailAddress.getCountry()));
                }
                if (panelTools.getColumnCodeName().contains(CrmAccountItem.STATE2)) {
                    mapColumns.put(CrmAccountItem.STATE2, refactor(mailAddress.getState()));
                }
                if (panelTools.getColumnCodeName().contains(CrmAccountItem.POST_CODE2)) {
                    mapColumns.put(CrmAccountItem.POST_CODE2, refactor(mailAddress.getZipCode()));
                }
                if (panelTools.getColumnCodeName().contains(CrmAccountItem.INDUSTRY)) {
                    mapColumns.put(CrmAccountItem.INDUSTRY, refactor(item.getIndustry()));
                }
                if (panelTools.getColumnCodeName().contains(CrmAccountItem.CURRENCY)) {
                    mapColumns.put(CrmAccountItem.CURRENCY, refactor(item.getCurrency()));
                }
                if (panelTools.getColumnCodeName().contains(CrmAccountItem.VAT_NUMBER)) {
                    mapColumns.put(CrmAccountItem.VAT_NUMBER, refactor(vatNumber));
                }
                if (panelTools.getColumnCodeName().contains(CrmAccountItem.TAX)) {
                    mapColumns.put(CrmAccountItem.TAX, refactor(item.getTaxName()));
                }
                if (panelTools.getColumnCodeName().contains(CrmAccountItem.PAYMENT_METHOD)) {
                    mapColumns.put(CrmAccountItem.PAYMENT_METHOD, refactor(item.getPaymentMethod()));
                }
                if (panelTools.getColumnCodeName().contains(CrmAccountItem.LAST_MODIFIED)) {
                    mapColumns.put(CrmAccountItem.LAST_MODIFIED, refactor(dateFormat(item.getLastUpdatedDate())));
                }
                if (panelTools.getColumnCodeName().contains(CrmAccountItem.STATUS)) {
                    mapColumns.put(CrmAccountItem.STATUS, refactor(item.isBlocked() ? commonLocalizer.localize(PdfLocalizationName.blocked) : commonLocalizer.localize(PdfLocalizationName.active)));
                }
                if (panelTools.getColumnCodeName().contains(CrmAccountItem.CREATION_DATE)) {
                    if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                        mapColumns.put(CrmAccountItem.CREATION_DATE, refactor(ServerUtils.convertToUzbDateFormat(dateFormat(item.getCreatedDate()))));
                    } else {
                        mapColumns.put(CrmAccountItem.CREATION_DATE, refactor(dateFormat(item.getCreatedDate())));
                    }
//                    mapColumns.put(CrmAccountItem.CREATION_DATE, refactor(dateFormat(item.getCreatedDate())));
                }
                if (panelTools.getColumnCodeName().contains(CrmAccountItem.CONTACT_NAME)) {
                    mapColumns.put(CrmAccountItem.CONTACT_NAME, refactor(item.getPrimaryContact() != null ? item.getPrimaryContact().getName() : null));
                }
                if (panelTools.getColumnCodeName().contains(CrmAccountItem.CLIENT_BALANCE)) {
                    mapColumns.put(CrmAccountItem.CLIENT_BALANCE, new ExcelData(clientBalance, ExcelData.CURRENCY, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CrmAccountItem.SUPPLIER_BALANCE)) {
                    mapColumns.put(CrmAccountItem.SUPPLIER_BALANCE, new ExcelData(supplierBalance, ExcelData.CURRENCY, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CrmAccountItem.CREDIT_LIMIT)) {
                    mapColumns.put(CrmAccountItem.CREDIT_LIMIT, new ExcelData(creditLimit, ExcelData.CURRENCY, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CrmAccountItem.TERMS)) {
                    mapColumns.put(CrmAccountItem.TERMS, new ExcelData(item.getTermName() != null ? item.getTermName() : "N/A", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CrmAccountItem.BLOCKED)) {
                    mapColumns.put(CrmAccountItem.BLOCKED, new ExcelData(item.isBlocked() ?
                            commonLocalizer.localize(PdfLocalizationName.yes) :
                                commonLocalizer.localize(PdfLocalizationName.no), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                ArrayList<CompanyCustomFieldItem> companyCustomFieldItems = new ArrayList<>();

                for (CompanyCustomFieldItem customFieldItem : panelTools.getListViewCustomFields())
                    if (customFieldItem != null) {
                        EdsCompanyCustomFieldsSettings setting = companyCFSettingsManager.getCompanyCustomField(customFieldItem.getEntityName(), customFieldItem.getColumnCode());
                        if (setting != null) {
                            CompanyCustomFieldItem fieldsItem = new CompanyCustomFieldItem();
                            fieldsItem.setObjectId(setting.getObjectID());
                            fieldsItem.setEntityId(setting.getObjectID());
                            fieldsItem.setColumnCode(setting.getColumnCode());
                            fieldsItem.setFieldName(setting.getFieldName());
                            fieldsItem.setAliasName(setting.getAliasName());
                            fieldsItem.setDataType(setting.getDataType());
                            fieldsItem.setUiType(setting.getUiType());
                            fieldsItem.setColumnWidth(setting.getColumnWidth());
                            fieldsItem.setEntityName(setting.getEntityName());
                            fieldsItem.setEntityCategoryAlias(setting.getEntityCategoryAlias());


                            if (Constants.UI_TYPE_ENTITY_DROPDOWN.equals(fieldsItem.getUiType())
                                    || Constants.TYPE_ENTITY_LOOKUP.equals(fieldsItem.getUiType())
                                    || Constants.TYPE_ENTITY_MULTI_LOOKUP.equals(fieldsItem.getUiType())) {
                                fieldsItem.setQueryItems(companyCFSettingsManager.getCustomFieldDataByQuery(SecurityContext.getCompanyID(), setting.getQuery()));
                            }

                            companyCustomFieldItems.add(fieldsItem);
                        }

                    }


                CustomFieldsUtils.setCustomFieldsExcelTableRows(companyCustomFieldItems, mapColumns, panelTools.getColumnCodeName(), item, user.getCompany());
//                if (panelTools.isCustomFieldsShown()) {
//                    for (String key : item.getCustomFieldsMap().keySet()) {
//                        if (item.getCustomFieldsMap().get(key) != null) {
//                            if (item.getCustomFieldsMap().get(key) instanceof Date) {
//                                mapColumns.put(key, new ExcelData(dateFormat((Date) item.getCustomFieldsMap().get(key)), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
//                            } else if (item.getCustomFieldsMap().get(key) instanceof Double) {
//                                mapColumns.put(key, new ExcelData(NumberFormat.getNumberInstance().format(item.getCustomFieldsMap().get(key)), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
//                            } else {
//                                mapColumns.put(key, new ExcelData(item.getCustomFieldsMap().get(key).toString(), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
//                            }
//                        } else {
//                            mapColumns.put(key, new ExcelData("", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
//                        }
//                    }
//                }
                excelDataList = new ArrayList<>();
                for (String columnName : panelTools.getColumnCodeName()) {
                    if (mapColumns.containsKey(columnName)) {
                        excelDataList.add(getExcelRows(mapColumns.get(columnName)));
                    }
                }
                cellDatas = new ExcelData[excelDataList.size()];
                excelDataList.toArray(cellDatas);
                list.add(cellDatas);
            }
//             workBook = new WorkBook(list, true, 0, 1, 0, 1);
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, 6);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate CRM Account list excel report, exception: " + e);
        }
        return null;
    }

    private ExcelData refactor(String value) {
        value = value != null && !"".equals(value) ? value : "N/A";
        return new ExcelData(value, ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL);
    }

    public ListResult<CrmAccountItem> getList(ListingFilterParameter filterParametrs) {
        return crmService.getCrmAccounts(filterParametrs);
    }

    private Map<String, ExcelData> getColumnHeaderMap() {
        Map<String, ExcelData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(CrmAccountItem.OWNER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.accountEmployee), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CrmAccountItem.PARENT_ACCOUNT_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.parentaccount), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CrmAccountItem.ACCOUNT_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.name), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CrmAccountItem.ACCOUNT_NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.number), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CrmAccountItem.PHONE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.phone), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CrmAccountItem.FAX, new ExcelData(commonLocalizer.localize(PdfLocalizationName.fax), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CrmAccountItem.WEBSITE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.website), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CrmAccountItem.EMAIL, new ExcelData(commonLocalizer.localize(PdfLocalizationName.email), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CrmAccountItem.ACCOUNT_TYPE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.accountType), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CrmAccountItem.BILLING_ADDRESS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.billingStreet1), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CrmAccountItem.BILLING_ADDRESS2, new ExcelData(commonLocalizer.localize(PdfLocalizationName.billingStreet2), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CrmAccountItem.CITY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.billingCity), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CrmAccountItem.COUNTRY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.country), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CrmAccountItem.STATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.billingState), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CrmAccountItem.POST_CODE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.billingPostcode), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CrmAccountItem.MAILING_ADDRESS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.mailingStreet1), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CrmAccountItem.MAILING_ADDRESS2, new ExcelData(commonLocalizer.localize(PdfLocalizationName.mailingStreet2), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CrmAccountItem.CITY2, new ExcelData(commonLocalizer.localize(PdfLocalizationName.mailingCity), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CrmAccountItem.COUNTRY2, new ExcelData(commonLocalizer.localize(PdfLocalizationName.mailingCountry), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CrmAccountItem.STATE2, new ExcelData(commonLocalizer.localize(PdfLocalizationName.mailingState), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CrmAccountItem.POST_CODE2, new ExcelData(commonLocalizer.localize(PdfLocalizationName.mailingPostcode), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CrmAccountItem.INDUSTRY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.industry), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CrmAccountItem.CURRENCY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.currency), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CrmAccountItem.VAT_NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.vatNumber), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CrmAccountItem.TAX, new ExcelData(commonLocalizer.localize(PdfLocalizationName.taxRate), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CrmAccountItem.PAYMENT_METHOD, new ExcelData(commonLocalizer.localize(PdfLocalizationName.paymentMethod), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CrmAccountItem.LAST_MODIFIED, new ExcelData(commonLocalizer.localize(PdfLocalizationName.modifiedBy), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CrmAccountItem.CREATION_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.createdDate), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CrmAccountItem.STATUS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.status), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CrmAccountItem.CONTACT_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.contactName), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CrmAccountItem.CLIENT_BALANCE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.balance), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CrmAccountItem.SUPPLIER_BALANCE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.balance), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CrmAccountItem.CREDIT_LIMIT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.creditLimit), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CrmAccountItem.TERMS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.terms), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CrmAccountItem.BANK_ACCOUNT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.bankAccount), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CrmAccountItem.CONTACT_EMAIL, new ExcelData(commonLocalizer.localize(PdfLocalizationName.email), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CrmAccountItem.BLOCKED, new ExcelData(commonLocalizer.localize(PdfLocalizationName.blocked), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        return mapColumnHeader;
    }

    public void setExcelReferenceMessageSource(WfmResourceBundleMessageSource excelReferenceMessageSource) {
        this.excelReferenceMessageSource = excelReferenceMessageSource;
    }
}
