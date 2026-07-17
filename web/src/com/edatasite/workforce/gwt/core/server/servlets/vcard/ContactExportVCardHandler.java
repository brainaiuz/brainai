package com.edatasite.workforce.gwt.core.server.servlets.vcard;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCrmContactItemParams;
import com.edatasite.workforce.core.tools.StringUtil;
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
import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.parameter.AddressType;
import ezvcard.parameter.EmailType;
import ezvcard.parameter.TelephoneType;
import ezvcard.property.Birthday;
import ezvcard.property.Email;
import ezvcard.property.FormattedName;
import ezvcard.property.Organization;
import ezvcard.property.StructuredName;
import ezvcard.property.Telephone;
import ezvcard.property.Url;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Sher
 * Date: Mar 10, 2016
 * Time: 9:37:12 PM
 */
public class ContactExportVCardHandler implements HttpRequestHandler {
    @Autowired
    @Qualifier("commonLocalizer")
    protected WfmMessageSource commonLocalizer;
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
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object dataClass = prepareRequest(request);
        try {
            System.out.println("VCard EXPORTING STARTED..." + new Date());

            buildMetadataStepFirst(response);
            ByteArrayOutputStream baos = getData(dataClass);
            buildMetadataStepSecond(response, baos.size());
            ServletOutputStream out = response.getOutputStream();
            baos.writeTo(out);
            try {
                baos.flush();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("VCard EXPORTING ENDED..." + new Date());
    }

    private ByteArrayOutputStream getData(Object dataClass) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        VCardTransferObject transferObject = new VCardTransferObject();
        transferObject = recursivelyWriteToStream(dataClass, transferObject);
        Ezvcard.write(transferObject.getAll()).version(VCardVersion.V3_0).go(outputStream);
        return outputStream;
    }

    private VCardTransferObject recursivelyWriteToStream(Object dataClass, VCardTransferObject transferObject) {
        transferObject = buildVCards(transferObject, dataClass);
        if (transferObject.isToBeContinued()) {
//            transferObject.getRows().clear();
            return recursivelyWriteToStream(transferObject.getFilterParameters(), transferObject);
        }
        return transferObject;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public VCardTransferObject buildVCards(VCardTransferObject transferObject, Object dataClass) {
        if (transferObject == null) {
            transferObject = new VCardTransferObject();
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
        List<VCard> rows = new ArrayList<>();
        if (contactList != null && contactList.getContactListItems() != null && contactList.getContactListItems().length > 0) {
            for (ContactListItem contact : contactList.getContactListItems()) {
                VCard vCard = new VCard();

                vCard.setNickname(contact.getName());

                FormattedName formattedName = new FormattedName(contact.getContactName());
                vCard.setFormattedName(formattedName);

                StructuredName n = new StructuredName();
                if (!StringUtil.isEmpty(contact.getFirstName())) {
                    n.setGiven(contact.getFirstName());
                }
                if (!StringUtil.isEmpty(contact.getLastName())) {
                    n.setFamily(contact.getLastName());
                }
                if (!StringUtil.isEmpty(contact.getMiddleName())) {
                    n.addSuffix(contact.getMiddleName());
                }
                if (!StringUtil.isEmpty(contact.getTitle())) {
                    n.addPrefix(contact.getTitle());
                }
                vCard.setStructuredName(n);
                if (!StringUtil.isEmpty(contact.getJobTitle())) {
                    vCard.addExtendedProperty("TITLE", contact.getJobTitle());
                }

                if (contact.getCrmAccount() != null) {
                    Organization organization = new Organization();
                    organization.addValue(contact.getCrmAccount().getName());
                    vCard.setOrganization(organization);
                }

                if (contact.getBirthDate() != null) {
                    Birthday birthday = new Birthday(contact.getBirthDate().getNonConvertedDate());
                    vCard.setBirthday(birthday);
                }

//                addRow(row, POSITION, contact.getJobTitle(), 1);
//                addRow(row, DEPARTMENT, contact.getDepartment(), 1);
                //emails
                for (String email : contact.getHomeEmail()) {
                    Email email1 = new Email(email);
                    email1.addType(EmailType.HOME);
                    vCard.addEmail(email1);
                }
                for (String email : contact.getWorkEmail()) {
                    Email email1 = new Email(email);
                    email1.addType(EmailType.WORK);
                    vCard.addEmail(email1);
                }
                for (String email : contact.getOtherEmail()) {
                    Email email1 = new Email(email);
                    email1.addType(EmailType.INTERNET);
                    vCard.addEmail(email1);
                }

                //phones
                for (String phone : contact.getHomePhone()) {
                    Telephone telephone = new Telephone(Utils.formatPhoneNumber(phone));
                    telephone.addType(TelephoneType.HOME);
                    vCard.addTelephoneNumber(telephone);
                }

                for (String phone : contact.getWorkPhone()) {
                    Telephone telephone = new Telephone(Utils.formatPhoneNumber(phone));
                    telephone.addType(TelephoneType.WORK);
                    vCard.addTelephoneNumber(telephone);
                }
                for (String phone : contact.getMobile()) {
                    Telephone telephone = new Telephone(Utils.formatPhoneNumber(phone));
                    telephone.addType(TelephoneType.CELL);
                    vCard.addTelephoneNumber(telephone);
                }
                for (String phone : contact.getHomeFax()) {
                    Telephone telephone = new Telephone(Utils.formatPhoneNumber(phone));
                    telephone.addType(TelephoneType.FAX);
                    vCard.addTelephoneNumber(telephone);
                }
                for (String phone : contact.getWorkFax()) {
                    Telephone telephone = new Telephone(Utils.formatPhoneNumber(phone));
                    telephone.addType(TelephoneType.FAX);
                    vCard.addTelephoneNumber(telephone);
                }
                for (String phone : contact.getPager()) {
                    Telephone telephone = new Telephone(Utils.formatPhoneNumber(phone));
                    telephone.addType(TelephoneType.PAGER);
                    vCard.addTelephoneNumber(telephone);
                }
                for (String phone : contact.getOtherPhone()) {
                    Telephone telephone = new Telephone(Utils.formatPhoneNumber(phone));
                    telephone.addType(TelephoneType.WORK);
                    vCard.addTelephoneNumber(telephone);
                }
                //IM Addresses

                for (String imAddress : contact.getgTalk()) {
                    vCard.addExtendedProperty("X-GTALK", imAddress);
                }
                for (String imAddress : contact.getAIM()) {
                    vCard.addExtendedProperty("X-AIM", imAddress);
                }
                for (String imAddress : contact.getYahoo()) {
                    vCard.addExtendedProperty("X-YAHOO", imAddress);
                }
                for (String imAddress : contact.getSkype()) {
                    vCard.addExtendedProperty("X-SKYPE", imAddress);
                }
//                for (String imAddress : contact.getQQ()) {
//                    i = addRow(row, IM_QQ, imAddress, i);
//                    vCard.addExtendedProperty("X-SKYPE", imAddress);
//                }
                for (String imAddress : contact.getMSN()) {
                    vCard.addExtendedProperty("X-MSN", imAddress);
                }
                for (String imAddress : contact.getICQ()) {
                    vCard.addExtendedProperty("X-ICQ", imAddress);
                }
                for (String imAddress : contact.getJabber()) {
                    vCard.addExtendedProperty("X-JABBER", imAddress);
                }
                //Web Addresses

                for (String webAddress : contact.getHomeWebSite()) {
                    Url url = new Url(webAddress);
                    url.setType("home");
                    vCard.addUrl(url);
                }
                for (String webAddress : contact.getWorkWebSite()) {
                    Url url = new Url(webAddress);
                    url.setType("work");
                    vCard.addUrl(url);
                }
                for (String webAddress : contact.getHomePage()) {
                    Url url = new Url(webAddress);
                    url.setType("home_page");
                    vCard.addUrl(url);
                }
                for (String webAddress : contact.getFtp()) {
                    Url url = new Url(webAddress);
                    url.setType("ftp");
                    vCard.addUrl(url);
                }
                for (String webAddress : contact.getBlog()) {
                    Url url = new Url(webAddress);
                    url.setType("blog");
                    vCard.addUrl(url);
                }
                for (String webAddress : contact.getProfileWebSite()) {
                    Url url = new Url(webAddress);
                    url.setType("profile");
                    vCard.addUrl(url);
                }
                for (String webAddress : contact.getOtherWebSite()) {
                    Url url = new Url(webAddress);
                    url.setType("other");
                    vCard.addUrl(url);
                }
                for (Address address : contact.getAddresses()) {
                    if (address != null) {
                        if (address.getRelationType() != null) {
                            final int relation = address.getRelationType();
                            ezvcard.property.Address add = new ezvcard.property.Address();
                            if (!StringUtil.isEmpty(address.getAddress())) {
                                add.setLabel(address.getAddress());
                            }
                            if (!StringUtil.isEmpty(address.getCountry())) {
                                add.setCountry(address.getCountry());
                            }
                            if (!StringUtil.isEmpty(address.getState())) {
                                add.setRegion(address.getState());
                            }
                            if (!StringUtil.isEmpty(address.getZipCode())) {
                                add.getPostalCodes().add(address.getZipCode());
                            }
                            switch (relation) {
                                case EdsCrmContactItemParams.HOME -> {
                                    add.addType(AddressType.HOME);
                                }
                                case EdsCrmContactItemParams.WORK -> {
                                    add.addType(AddressType.WORK);
                                }
                                default -> add.addType(AddressType.PREF);
                            }
                            vCard.addAddress(add);
                        }
                    }
                }
                rows.add(vCard);
            }
        }
        transferObject.setFilterParameters(filterParametrs);
        return init(rows, transferObject);
    }

    private ArrayList<Integer> getLessObjectIDs(boolean forDB, VCardTransferObject transferObject) {
        ArrayList<Integer> ids = transferObject.getObjectIDs();
        int limit = forDB ? VCardTransferObject.limitDB : VCardTransferObject.limitSOLR;
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

    private void setTitles(ListingFilterParameter filterParametrs, VCardTransferObject transferObject) {
        transferObject.setTitlesSet(true);

        filterParametrs.setIDsOnly(true);
        List<Integer> idList = crmServiceLocal.getCRMEntityIDs(CrmConstants.CRM_CONTACT, filterParametrs);
        transferObject.setObjectIDs(new ArrayList<>(idList));
//        StringBuffer ids = new StringBuffer("0");
//        if (idList.size() > 0) {
//            for (Integer id : idList) {
//                ids.append(",").append(id.toString());
//            }
//        }

//        List<Object> countOfColumns = crmContactManager.getColumnNumbersForCSV(ids.toString(), EdsCrmContactItemParams.EMAIL, EdsCrmContactItemParams.HOME, EdsCrmContactItemParams.WORK, EdsCrmContactItemParams.OTHER);
//        addAdditionalTitles(titles, countOfColumns, EMAIL_HOME, EMAIL_WORK, EMAIL_OTHER);

//        countOfColumns = crmContactManager.getColumnNumbersForCSV(ids.toString(), EdsCrmContactItemParams.PHONE, EdsCrmContactItemParams.HOME, EdsCrmContactItemParams.WORK, EdsCrmContactItemParams.MOBILE, EdsCrmContactItemParams.HOME_FAX, EdsCrmContactItemParams.WORK_FAX, EdsCrmContactItemParams.PAGER, EdsCrmContactItemParams.OTHER);
//        addAdditionalTitles(titles, countOfColumns, PHONE_HOME, PHONE_WORK, PHONE_MOBILE, PHONE_HOME_FAX, PHONE_WORK_FAX, PHONE_PAGER, PHONE_OTHER);

//        countOfColumns = crmContactManager.getColumnNumbersForCSV(ids.toString(), EdsCrmContactItemParams.IMADDRESS, EdsCrmContactItemParams.GOOGLE_TALK, EdsCrmContactItemParams.AIM, EdsCrmContactItemParams.YAHOO, EdsCrmContactItemParams.SKYPE, EdsCrmContactItemParams.QQ, EdsCrmContactItemParams.MSN, EdsCrmContactItemParams.ICQ, EdsCrmContactItemParams.JABBER);
//        addAdditionalTitles(titles, countOfColumns, IM_GTALK, IM_AIM, IM_YAHOO, IM_SKYPE, IM_QQ, IM_MSN, IM_ICQ, IM_JABBER);
//        transferObject.setTitles(titles);
    }

    private VCardTransferObject init(List<VCard> rows, VCardTransferObject transferObject) {
        transferObject.getRows().addAll(rows);
        return transferObject;
    }

    String getFileName() {
        EdsUser user = crmContactManager.getUser();
        String firstName = user.getFirstName() != null ? user.getFirstName().replace(" ", "") : "";
        String lastName = user.getLastName() != null ? user.getLastName().replace(" ", "") : "";
        String date = ServerUtils.dateFormat(user.getUserDate(), "MM_dd_yyyy");
        return firstName + "_" + lastName + "_Contacts_" + date;
    }

    /**
     * Set CSV Meta Data
     *
     * @param response
     */
    protected void buildMetadataStepFirst(HttpServletResponse response) {
        try {
            String fileName = "";
            if (getFileName() != null) {
                fileName = getFileName();
            }

            if (fileName.contains(" ")) {
                fileName = fileName.replace(" ", "");
            }
            if (fileName.contains("/")) {
                fileName = fileName.replace("\\/", "_");
            }
            fileName = ServerUtils.normalizeFileNameT(fileName);
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".vcf");
            response.setHeader("Content-Type", "text/vcard; charset=utf-8");
            response.setContentType("text/vcard");
            response.setCharacterEncoding("UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Set CSV Meta Data
     *
     * @param response
     */
    protected void buildMetadataStepSecond(HttpServletResponse response, int contentLength) {
        try {
            response.setContentLength(contentLength);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * You can rewrite this method
     * if you want to parse request to take some parametrs.
     *
     * @param request
     * @return return true if you want PostPDFHandler to parse and bind your request.
     */
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
}
