package com.edatasite.workforce.gwt.core.server.servlets.csv;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCrmContactItemParams;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactList;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.server.app.ContactServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.settings.CompanySettingsManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.WfmJsonUtils;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Jul 10, 2010
 * Time: 12:38:38 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class CrmContactListCSVHandler extends AbstractBaseCSVHandler {
    //personal Information titles
    private static final String FIRST_NAME = "First Name";
    private static final String MIDDLE_NAME = "Middle Name";
    private static final String LAST_NAME = "Last Name";
    private static final String OTHER_NAME = "Other Name";
    private static final String TITLE = "Title";
    private static final String DATE_OF_BIRTH = "Date Of Birth";
    //company Information titles;
    private static final String COMPANY_NAME = "Company Name";
    private static final String POSITION = "Position";
    private static final String DEPARTMENT = "Department";
    //contact Information Titles
    private static final String EMAIL_HOME = "Email - Home";                        //emails
    private static final String EMAIL_WORK = "Email - Work";                        //emails
    private static final String EMAIL_OTHER = "Email - Other";                        //emails
    private static final String PHONE_HOME = "Phone - Home";                        //Phones
    private static final String PHONE_WORK = "Phone - Work";                        //Phones
    private static final String PHONE_MOBILE = "Mobile";                            //Phones
    private static final String PHONE_HOME_FAX = "Fax - Home";                        //Phones
    private static final String PHONE_WORK_FAX = "Fax - Work";                        //Phones
    private static final String PHONE_PAGER = "Pager";                                //Phones
    private static final String PHONE_OTHER = "Phone - Other";                        //Phones
    private static final String IM_GTALK = "Google talk";                            //IM Address
    private static final String IM_AIM = "AIM";                                        //IM Address
    private static final String IM_YAHOO = "Yahoo";                                    //IM Address
    private static final String IM_SKYPE = "Skype";                                    //IM Address
    private static final String IM_QQ = "QQ";                                        //IM Address
    private static final String IM_MSN = "MSN";                                        //IM Address
    private static final String IM_ICQ = "ICQ";                                        //IM Address
    private static final String IM_JABBER = "Jabber";                                //IM Address
    private static final String WEB_HOME = "WEB address - Home";                    //Web Address
    private static final String WEB_WORK = "WEB address - Home";                    //Web Address
    private static final String WEB_HOME_PAGE = "Home Page";                        //Web Address
    private static final String WEB_FTP = "FTP";                                    //Web Address
    private static final String WEB_BLOG = "Blog";                                    //Web Address
    private static final String WEB_PROFILE = "Profile";                            //Web Address
    private static final String WEB_OTHER = "Web address - Other";                    //Web Address
    private static final String ADDRESS_STREET_HOME = "Street - Home";                //Address Street
    private static final String ADDRESS_STREET2_HOME = "Street 2 - Home";                //Address Street
    private static final String ADDRESS_STREET_WORK = "Street - Work";                //Address Street
    private static final String ADDRESS_STREET2_WORK = "Street 2- Work";                //Address Street
    private static final String ADDRESS_STREET_OTHER = "Street - Other";            //Address Street
    private static final String ADDRESS_STREET2_OTHER = "Street 2 - Other";            //Address Street
    private static final String ADDRESS_CITY_HOME = "City - Home";                    //Address City
    private static final String ADDRESS_CITY_WORK = "City - Work";                    //Address City
    private static final String ADDRESS_CITY_OTHER = "City - Other";                //Address City
    private static final String ADDRESS_COUNTRY_HOME = "Country - Home";            //Address Country
    private static final String ADDRESS_COUNTRY_WORK = "Country - Work";            //Address Country
    private static final String ADDRESS_COUNTRY_OTHER = "Country - Other";            //Address Country
    private static final String ADDRESS_STATE_HOME = "State - Home";                //Address State
    private static final String ADDRESS_STATE_WORK = "State - Work";                //Address State
    private static final String ADDRESS_STATE_OTHER = "State - Other";                //Address State
    private static final String ADDRESS_POSTCODE_HOME = "Post Code - Home";            //Address Post Code
    private static final String ADDRESS_POSTCODE_WORK = "Post Code - Work";            //Address Post Code
    private static final String ADDRESS_POSTCODE_OTHER = "Post Code - Other";        //Address Post Code

    @Autowired
    private ContactServiceLocal contactServiceLocal;
    @Autowired
    @Qualifier("crmService")
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private CompanySettingsManager companySettingsManager;

    @Override
    protected Object prepareRequest(HttpServletRequest request) {
        Map filterMap = request.getParameterMap();
        ListingFilterParameter fp = new ListingFilterParameter();
        HashMap<String, String> paramsMap = fp.getRequestParams();
        Iterator<Map> entries = filterMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            if (paramsMap.containsKey(entry.getKey())) {
                String[] value = (String[]) entry.getValue();
                paramsMap.put((String) entry.getKey(), value[0]);
            }
        }
        fp.setRequestParams(paramsMap);
        fp.setFacetFilter(WfmJsonUtils.jsonConvertToFacetFilterRpc(fp.getFacetFilterJson()));
        fp.setListPanelTool(WfmJsonUtils.jsonConvertToListPanelToolRpc(fp.getListPanelToolJson()));
        return fp;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CSVTransferObject buildCSV(CSVTransferObject transferObject, Object dataClass) {
        if (transferObject == null) {
            transferObject = new CSVTransferObject();
        }

        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        if (filterParametrs == null) {
            filterParametrs = new ListingFilterParameter();
        }
        if (!transferObject.isTitlesSet()) {
            Object limit_ = companySettingsManager.getColumnValue(SecurityContext.getCompanyID(), "excellimit");
            Integer limit = null;
            if (limit_ != null && limit_.toString().matches(Constants.REGEX_INTEGER)) {
                limit = Integer.parseInt(limit_.toString());
            }
            if (limit != null) {
                filterParametrs.setLimit(limit);
            } else {
                filterParametrs.setLimit(1000);
            }
            setTitles(filterParametrs, transferObject);
        }
        filterParametrs.setForCSVonly(true);
        ContactList contactList = contactServiceLocal.getContactsByIDsFromDBForExport(filterParametrs, getLessObjectIDs(true, transferObject));
//        getRows().clear();
        List<Map<String, String>> rows = new ArrayList<>();
        if (contactList != null && contactList.getContactListItems() != null && contactList.getContactListItems().length > 0) {
            for (ContactListItem contact : contactList.getContactListItems()) {
                Map<String, String> row = new HashMap<>();
                addRow(row, FIRST_NAME, contact.getFirstName(), 1);
                addRow(row, MIDDLE_NAME, contact.getMiddleName(), 1);
                addRow(row, LAST_NAME, contact.getLastName(), 1);
                addRow(row, OTHER_NAME, contact.getOtherName(), 1);
                addRow(row, TITLE, contact.getTitle(), 1);
                if (contact.getBirthDate() != null) {
                    addRow(row, DATE_OF_BIRTH, ServerUtils.getDateShortFormat(contact.getBirthDate().getNonConvertedDate()), 1);
                }


                addRow(row, COMPANY_NAME, contact.getCrmAccount().getName(), 1);
                addRow(row, POSITION, contact.getJobTitle(), 1);
                addRow(row, DEPARTMENT, contact.getDepartment(), 1);
                //emails
                int i = 1;
                for (String email : contact.getHomeEmail()) {
                    i = addRow(row, EMAIL_HOME, email, i);
                }
                i = 1;
                for (String email : contact.getWorkEmail()) {
                    i = addRow(row, EMAIL_WORK, email, i);
                }
                i = 1;
                for (String email : contact.getOtherEmail()) {
                    i = addRow(row, EMAIL_OTHER, email, i);
                }
                //phones
                i = 1;
                for (String phone : contact.getHomePhone()) {
                    i = addRow(row, PHONE_HOME, Utils.formatPhoneNumber(phone), i);
                }
                i = 1;
                for (String phone : contact.getWorkPhone()) {
                    i = addRow(row, PHONE_WORK, Utils.formatPhoneNumber(phone), i);
                }
                i = 1;
                for (String phone : contact.getMobile()) {
                    i = addRow(row, PHONE_MOBILE, Utils.formatPhoneNumber(phone), i);
                }
                i = 1;
                for (String phone : contact.getHomeFax()) {
                    i = addRow(row, PHONE_HOME_FAX, Utils.formatPhoneNumber(phone), i);
                }
                i = 1;
                for (String phone : contact.getWorkFax()) {
                    i = addRow(row, PHONE_WORK_FAX, Utils.formatPhoneNumber(phone), i);
                }
                i = 1;
                for (String phone : contact.getPager()) {
                    i = addRow(row, PHONE_PAGER, Utils.formatPhoneNumber(phone), i);
                }
                i = 1;
                for (String phone : contact.getOtherPhone()) {
                    i = addRow(row, PHONE_OTHER, Utils.formatPhoneNumber(phone), i);
                }
                //IM Addresses
                i = 1;
                for (String imAddress : contact.getgTalk()) {
                    i = addRow(row, IM_GTALK, imAddress, i);
                }
                i = 1;
                for (String imAddress : contact.getAIM()) {
                    i = addRow(row, IM_AIM, imAddress, i);
                }
                i = 1;
                for (String imAddress : contact.getYahoo()) {
                    i = addRow(row, IM_YAHOO, imAddress, i);
                }
                i = 1;
                for (String imAddress : contact.getSkype()) {
                    i = addRow(row, IM_SKYPE, imAddress, i);
                }
                i = 1;
                for (String imAddress : contact.getQQ()) {
                    i = addRow(row, IM_QQ, imAddress, i);
                }
                i = 1;
                for (String imAddress : contact.getMSN()) {
                    i = addRow(row, IM_MSN, imAddress, i);
                }
                i = 1;
                for (String imAddress : contact.getICQ()) {
                    i = addRow(row, IM_ICQ, imAddress, i);
                }
                i = 1;
                for (String imAddress : contact.getJabber()) {
                    i = addRow(row, IM_JABBER, imAddress, i);
                }
                //Web Addresses
                i = 1;
                for (String webAddress : contact.getHomeWebSite()) {
                    i = addRow(row, WEB_HOME, webAddress, i);
                }
                i = 1;
                for (String webAddress : contact.getWorkWebSite()) {
                    i = addRow(row, WEB_WORK, webAddress, i);
                }
                i = 1;
                for (String webAddress : contact.getHomePage()) {
                    i = addRow(row, WEB_HOME_PAGE, webAddress, i);
                }
                i = 1;
                for (String webAddress : contact.getFtp()) {
                    i = addRow(row, WEB_FTP, webAddress, i);
                }
                i = 1;
                for (String webAddress : contact.getBlog()) {
                    i = addRow(row, WEB_BLOG, webAddress, i);
                }
                i = 1;
                for (String webAddress : contact.getProfileWebSite()) {
                    i = addRow(row, WEB_PROFILE, webAddress, i);
                }
                i = 1;
                for (String webAddress : contact.getOtherWebSite()) {
                    i = addRow(row, WEB_OTHER, webAddress, i);
                }
                i = 1;
                for (Address address : contact.getAddresses()) {
                    if (address != null) {
                        if (address.getRelationType() != null) {
                            final int relation = address.getRelationType();
                            switch (relation) {
                                case EdsCrmContactItemParams.HOME -> {
                                    addRow(row, ADDRESS_STREET_HOME, address.getAddress(), i);
                                    addRow(row, ADDRESS_STREET2_HOME, address.getAddressb(), i);
                                    addRow(row, ADDRESS_CITY_HOME, address.getCity(), i);
                                    addRow(row, ADDRESS_COUNTRY_HOME, address.getCountry(), i);
                                    addRow(row, ADDRESS_STATE_HOME, address.getState(), i);
                                    addRow(row, ADDRESS_POSTCODE_HOME, address.getZipCode(), i);
                                }
                                case EdsCrmContactItemParams.WORK -> {
                                    addRow(row, ADDRESS_STREET_WORK, address.getAddress(), i);
                                    addRow(row, ADDRESS_STREET2_WORK, address.getAddressb(), i);
                                    addRow(row, ADDRESS_CITY_WORK, address.getCity(), i);
                                    addRow(row, ADDRESS_COUNTRY_WORK, address.getCountry(), i);
                                    addRow(row, ADDRESS_STATE_WORK, address.getState(), i);
                                    addRow(row, ADDRESS_POSTCODE_WORK, address.getZipCode(), i);
                                }
                                case EdsCrmContactItemParams.OTHER -> {
                                    addRow(row, ADDRESS_STREET_OTHER, address.getAddress(), i);
                                    addRow(row, ADDRESS_STREET2_OTHER, address.getAddressb(), i);
                                    addRow(row, ADDRESS_CITY_OTHER, address.getCity(), i);
                                    addRow(row, ADDRESS_COUNTRY_OTHER, address.getCountry(), i);
                                    addRow(row, ADDRESS_STATE_OTHER, address.getState(), i);
                                    addRow(row, ADDRESS_POSTCODE_OTHER, address.getZipCode(), i);
                                }
                            }
                            i++;
                        }
                    }
                }
                rows.add(row);
            }
        }
        transferObject.setFilterParameters(filterParametrs);
        return init(rows, transferObject);
    }

    private ArrayList<Integer> getLessObjectIDs(boolean forDB, CSVTransferObject transferObject) {
        ArrayList<Integer> ids = transferObject.getObjectIDs();
        int limit = forDB ? CSVTransferObject.limitDB : CSVTransferObject.limitSOLR;
        int step = transferObject.nextStep();
        if (ids.size() > limit) {
            int fromIndex = step * limit;
            boolean lastOnes = fromIndex + limit > ids.size();
            int toIndex = lastOnes ? ids.size() : fromIndex + limit;
            transferObject.setToBeContinued(!lastOnes);
            return new ArrayList<>(ids.subList(fromIndex, toIndex));
        } else {
            transferObject.setToBeContinued(false);
        }
        return ids;
    }

    private void setTitles(ListingFilterParameter filterParametrs, CSVTransferObject transferObject) {
        transferObject.setTitlesSet(true);
        Map<String, List<String>> titles = new LinkedHashMap<>();
        addTitle(FIRST_NAME, titles);
        addTitle(MIDDLE_NAME, titles);
        addTitle(LAST_NAME, titles);
        addTitle(OTHER_NAME, titles);
        addTitle(TITLE, titles);
        addTitle(DATE_OF_BIRTH, titles);

        addTitle(COMPANY_NAME, titles);
        addTitle(POSITION, titles);
        addTitle(DEPARTMENT, titles);

        addTitle(EMAIL_HOME, titles);
        addTitle(EMAIL_WORK, titles);
        addTitle(EMAIL_OTHER, titles);

        addTitle(PHONE_HOME, titles);
        addTitle(PHONE_WORK, titles);
        addTitle(PHONE_MOBILE, titles);
        addTitle(PHONE_HOME_FAX, titles);
        addTitle(PHONE_WORK_FAX, titles);
        addTitle(PHONE_PAGER, titles);
        addTitle(PHONE_OTHER, titles);

        addTitle(IM_GTALK, titles);
        addTitle(IM_AIM, titles);
        addTitle(IM_YAHOO, titles);
        addTitle(IM_SKYPE, titles);
        addTitle(IM_QQ, titles);
        addTitle(IM_MSN, titles);
        addTitle(IM_ICQ, titles);
        addTitle(IM_JABBER, titles);

        addTitle(WEB_HOME, titles);
        addTitle(WEB_WORK, titles);
        addTitle(WEB_HOME_PAGE, titles);
        addTitle(WEB_FTP, titles);
        addTitle(WEB_BLOG, titles);
        addTitle(WEB_PROFILE, titles);
        addTitle(WEB_OTHER, titles);

        addTitle(ADDRESS_STREET_HOME, titles);
        addTitle(ADDRESS_STREET_WORK, titles);
        addTitle(ADDRESS_STREET_OTHER, titles);
        addTitle(ADDRESS_CITY_HOME, titles);
        addTitle(ADDRESS_CITY_WORK, titles);
        addTitle(ADDRESS_CITY_OTHER, titles);
        addTitle(ADDRESS_COUNTRY_HOME, titles);
        addTitle(ADDRESS_COUNTRY_WORK, titles);
        addTitle(ADDRESS_COUNTRY_OTHER, titles);
        addTitle(ADDRESS_STATE_HOME, titles);
        addTitle(ADDRESS_STATE_WORK, titles);
        addTitle(ADDRESS_STATE_OTHER, titles);
        addTitle(ADDRESS_POSTCODE_HOME, titles);
        addTitle(ADDRESS_POSTCODE_WORK, titles);
        addTitle(ADDRESS_POSTCODE_OTHER, titles);

        filterParametrs.setIDsOnly(true);
        List<Integer> idList = crmServiceLocal.getCRMEntityIDs(CrmConstants.CRM_CONTACT, filterParametrs);
        transferObject.setObjectIDs(new ArrayList<>(idList));
        StringBuilder ids = new StringBuilder("0");
        if (idList != null && idList.size() > 0) {
            for (Integer id : idList) {
                ids.append(",").append(id.toString());
            }
        }

        List<Object> countOfColumns = crmContactManager.getColumnNumbersForCSV(ids.toString(), EdsCrmContactItemParams.EMAIL, EdsCrmContactItemParams.HOME, EdsCrmContactItemParams.WORK, EdsCrmContactItemParams.OTHER);
        addAdditionalTitles(titles, countOfColumns, EMAIL_HOME, EMAIL_WORK, EMAIL_OTHER);

        countOfColumns = crmContactManager.getColumnNumbersForCSV(ids.toString(), EdsCrmContactItemParams.PHONE, EdsCrmContactItemParams.HOME, EdsCrmContactItemParams.WORK, EdsCrmContactItemParams.MOBILE, EdsCrmContactItemParams.HOME_FAX, EdsCrmContactItemParams.WORK_FAX, EdsCrmContactItemParams.PAGER, EdsCrmContactItemParams.OTHER);
        addAdditionalTitles(titles, countOfColumns, PHONE_HOME, PHONE_WORK, PHONE_MOBILE, PHONE_HOME_FAX, PHONE_WORK_FAX, PHONE_PAGER, PHONE_OTHER);

        countOfColumns = crmContactManager.getColumnNumbersForCSV(ids.toString(), EdsCrmContactItemParams.IMADDRESS, EdsCrmContactItemParams.GOOGLE_TALK, EdsCrmContactItemParams.AIM, EdsCrmContactItemParams.YAHOO, EdsCrmContactItemParams.SKYPE, EdsCrmContactItemParams.QQ, EdsCrmContactItemParams.MSN, EdsCrmContactItemParams.ICQ, EdsCrmContactItemParams.JABBER);
        addAdditionalTitles(titles, countOfColumns, IM_GTALK, IM_AIM, IM_YAHOO, IM_SKYPE, IM_QQ, IM_MSN, IM_ICQ, IM_JABBER);
        transferObject.setTitles(titles);
    }

    private void addAdditionalTitles(Map<String, List<String>> titles, List<Object> countOfColumns, String... sTitles) {
        if (sTitles != null && sTitles.length > 0) {
            if (countOfColumns != null && countOfColumns.size() > 0) {
                for (Object countOfColumn : countOfColumns) {
                    if (countOfColumn != null && countOfColumn instanceof Object[]) {
                        if (((Object[]) countOfColumn).length > 0 && (((Object[]) countOfColumn)[1] instanceof Integer || ((Object[]) countOfColumn)[1] instanceof BigInteger)) {
                            BigInteger count = (BigInteger) ((Object[]) countOfColumn)[1];
                            if (count != null && count.intValue() > 1) {
                                if (sTitles.length >= countOfColumns.indexOf(countOfColumn)) {
                                    String title = sTitles[countOfColumns.indexOf(countOfColumn)];
                                    if (title != null && !"".equals(title)) {
                                        addTitle(title, titles);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private CSVTransferObject init(List<Map<String, String>> rows, CSVTransferObject transferObject) {
        boolean writeTitleOnlyOnes = true;
        for (Map<String, String> row : rows) {
            List<String> rowList = new ArrayList<>();
            List<String> title = new ArrayList<>();
            for (String key : transferObject.getTitles()) {
                if (transferObject.isFirstStep() && writeTitleOnlyOnes) {
                    title.add(key);
                }
                String value = row.get(key);
                rowList.add(value == null || value.equals("null") ? "" : value);
            }
            if (transferObject.isFirstStep() && writeTitleOnlyOnes) {
                transferObject.getRows().add(title.toArray(new String[]{}));
                writeTitleOnlyOnes = false;
            }
            transferObject.getRows().add(rowList.toArray(new String[]{}));
        }
        return transferObject;
    }

    private int addRow(final Map<String, String> row, String key, String value, int level) {
        if (value == null) {
            value = "";
        }
        key = level > 1 && !"".equals(value) ? key + " (" + level + ")" : key;
        row.put(key, value);
        return value.equals("") ? level : level + 1;
    }

    private void addTitle(String title, Map<String, List<String>> titles) {
        if (!titles.containsKey(title)) {
            List<String> titleList = new ArrayList<>();
            titleList.add(title);
            titles.put(title, titleList);
        } else {
            titles.get(title).add(title + " (" + (titles.get(title).size() + 1) + ")");
        }
    }

    @Override
    String getFileName() {
        EdsUser user = crmContactManager.getUser();
        String firstName = user.getFirstName() != null ? user.getFirstName().replace(" ", "") : "";
        String lastName = user.getLastName() != null ? user.getLastName().replace(" ", "") : "";
        String date = ServerUtils.dateFormat(user.getUserDate(), "MM_dd_yyyy");
        return firstName + "_" + lastName + "_Contacts_" + date;
    }

}
