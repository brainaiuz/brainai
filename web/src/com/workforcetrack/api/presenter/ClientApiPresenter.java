package com.workforcetrack.api.presenter;

import com.edatasite.workforce.gwt.client.client.rpc.NewClientList;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactList;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.workforcetrack.api.base.RestServiceUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: shahob
 * Date: 12/09/12
 * Time: 14:36
 * To change this template use File | Settings | File Templates.
 */
public class ClientApiPresenter extends BaseApiPresenter {

    public Map<String, Object> convertToMapListing(NewClientList items) {
        Map<String, Object> map = new LinkedHashMap<>();
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        if (items != null && items.getList().size() > 0) {
            for (CrmAccountItem item : items.getList()) {
                list.add(convertToMapListing(item));
            }
        }
        map.put(TOTAL_COUNT, items.getTotal());
        map.put(ITEMS, list);
        return map;
    }

    public Map<String, Object> convertToMapListing(CrmAccountItem item) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(OBJECT_ID, item.getObjectId());
        map.put(NUMBER, item.getNumber());
        map.put(NAME, item.getName());
        if (item.getBillAddresses() != null && item.getBillAddresses().length > 0) {
            Address address = item.getBillAddresses()[0];
            map.put(BILL_ADDRESSES, address.getAddress());
            map.put(COUNTRY, address.getCountry());
            map.put(STATE, address.getState());
        }
        map.put(PRIMARY_CONTACT, item.getPrimaryContact() != null ? item.getPrimaryContact().getContactName() : "N/A");
        map.put(PHONE, item.getPhone());
        return map;
    }

    public Map<String, Object> convertToMap(CrmAccountItem item) {
        Map<String, Object> map = new LinkedHashMap<>();

        map.put(OBJECT_ID, item.getObjectId());
        map.put(OWNER_NAME, item.getOwnerItems() != null && item.getOwnerItems().length > 0 ? item.getOwnerItems()[0].getName() : "");
        map.put(OWNER_ID, item.getOwnerItems() != null && item.getOwnerItems().length > 0 ? item.getOwnerItems()[0].getId() : "");
        map.put(OWNER_ITEMS, item.getOwnerItems());
        map.put(NAME, item.getName());
        map.put(NAME_ID, item.getNameId());
        map.put(NUMBER, item.getNumber());
        map.put(NUMBER_ID, item.getNumberId());
        map.put(ACCOUNT_TYPES, item.getAccountTypes());
        map.put(EMAIL, item.getEmail());
        map.put(EMAIL_ID, item.getEmailId());
        map.put(PHONE, item.getPhone());
        map.put(PHONE_ID, item.getPhoneId());
        map.put(FAX, item.getFax());
        map.put(FAX_ID, item.getFaxId());
        map.put(WEBSITE, item.getWebsite());
        map.put(WEBSITE_ID, item.getWebsiteId());
        map.put(BILL_ADDRESSES, item.getDefaultAddress(true));
        map.put(MAIL_ADDRESSES, item.getDefaultAddress(false));
        map.put(COUNTRYS, item.getCountrys());
        map.put(STATES, item.getStates());
        map.put(NOTE, item.getNote());
        map.put(IMPORT_FILE_ID, item.getImportFileID());
        map.put(TITLE, item.getTitle());
        map.put(TITLE_ID, item.getTitleID());
        map.put(INDUSTRIES, item.getIndustries());
        map.put(INDUSTRY_ID, item.getIndustryID());
        map.put(INDUSTRY, item.getIndustry());
        map.put(INDUSTRY_CODE, item.getIndustryCode());
        map.put(CURRENCIES, item.getCurrencies());
        map.put(CURRENCY_ID, item.getCurrencyId());
        map.put(CURRENCY, item.getCurrency());
        map.put(PAYMENT_METHOD, item.getPaymentMethod());
        map.put(PAYMENT_METHOD_ID, item.getPaymentMethodId());
        map.put(PAYMENT_METHODS, item.getPaymentMethods());
        map.put(VAT_NUMBER, item.getVatNumber());
        map.put(VAT_NUMBER_ID, item.getVatNumberId());
        map.put(LOGO_ID, item.getLogoId());
        map.put(LOGO_URL, item.getLogoUrl());
        map.put(PRIMARY_CONTACT, item.getPrimaryContact());
        map.put(PARENT, item.getParent());
        map.put(CREATED_DATE, item.getCreatedDate());
        map.put(LAST_UPDATE_DATE, item.getLastUpdatedDate());
        map.put(CREDIT_LIMIT, item.getCreditLimit());
        map.put(ATTACHMENTS, item.getAttachments());
        map.put(FROM_SIGNUP, item.isFromSignUp());
        map.put(FROM_SAASU, item.isFromSaasu());

        return map;
    }

    public Map<String, Object> convertToMap(ContactList items) {
        Map<String, Object> map = new LinkedHashMap<>();
        ArrayList<Map<String, Object>> list = new ArrayList<>();

        for (ContactListItem item : items.getContactListItems()) {
            list.add(convertToMap(item));
        }

        map.put(TOTAL_COUNT, items.getTotalCount());
        map.put(ITEMS, list);
        return map;
    }

    public Map<String, Object> convertToMap(ContactListItem item) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(OBJECT_ID, item.getObjectId());
        map.put(CONTACT_NAME, item.getContactName());
        map.put(COMPANY_NAME, item.getCrmAccount() != null ? item.getCrmAccount().getName() : "N/A");
        map.put(PHONE, item.getPrimaryPhone());
        String mobile = "N/A";
        if (item.getMobile() != null && item.getMobile().size() > 0 && !"n/a".equals(item.getMobile().get(0).toLowerCase())) {
            mobile = RestServiceUtils.cleanPhoneNumber(item.getMobile().get(0));
        }
        map.put(MOBILE_PHONE, mobile);
        map.put(EMAIL, item.getPrimaryEmail());
        map.put(TITLE, item.getTitle());
        Address address = item.getPrimaryAddress();
        map.put(STREET, address != null && address.getAddress() != null ? address.getAddress() : "N/A");
        map.put(STATE, address != null && address.getAddress() != null ? address.getState() : "N/A");
        map.put(CITY, address != null && address.getCity() != null ? address.getCity() : "N/A");
        map.put(COUNTRY, address != null && address.getCountryId() != null ? address.getCountry() : "N/A");
        map.put(POST_CODE, address != null && address.getZipCode() != null ? address.getZipCode() : "N/A");
        map.put(OWNER_NAME, item.getOwner());
        map.put(OWNER_ID, item.getOwnerId());
        map.put(DEPARTMENT, item.getDepartment());
        map.put(POSITION, item.getJobTitle());
        return map;
    }


    public CrmAccountItem convertToItem(Map<String, Object> map, CrmAccountItem item) throws ParseException, ClassCastException {
        item.setObjectId((Integer) map.get(OBJECT_ID));
        SelectItem ownerItems[] = new SelectItem[1];
        ownerItems[0].setId((Integer) map.get(OWNER_ID));
        item.setOwnerItems(ownerItems);
        item.setNameId((Integer) map.get(NAME_ID));
        item.setName((String) map.get(NAME));
        item.setAccountTypes(convertToSelectItemList((List<Map<String, Object>>) map.get(ACCOUNT_TYPES)));
        item.setEmail((String) map.get(EMAIL));
        item.setPhone((String) map.get(PHONE));
        item.setFax((String) map.get(FAX));
        item.setWebsite((String) map.get(WEBSITE));
        item.setIndustryID((Integer) map.get(INDUSTRY_ID));

        Address billAddress = convertToAddressItem((Map<String, Object>) map.get(BILL_ADDRESSES));
        if (item.getBillAddresses() != null && item.getBillAddresses().length > 0) {
            for (Address address : item.getBillAddresses()) {
                if (address.isPrimary()) {
                    address.setName(billAddress.getName());
                    address.setAddress(billAddress.getAddress());
                    address.setAddressb(billAddress.getAddressb());
                    address.setCountryId(billAddress.getCountryId());
                    address.setStateId(billAddress.getStateId());
                    address.setCity(billAddress.getCity());
                    address.setCityId(billAddress.getCityId());
                    address.setZipCode(billAddress.getZipCode());
                    break;
                }
            }
        } else {
            Address[] billAddressList = new Address[1];
            billAddressList[0] = billAddress;
            item.setBillAddresses(billAddressList);
        }

        Address mailAddress = convertToAddressItem((Map<String, Object>) map.get(MAIL_ADDRESSES));
        if (item.getMailAddresses() != null && item.getMailAddresses().length > 0) {
            for (Address address : item.getMailAddresses()) {
                if (address.isPrimary()) {
                    address.setName(mailAddress.getName());
                    address.setAddress(mailAddress.getAddress());
                    address.setAddressb(mailAddress.getAddressb());
                    address.setCountryId(mailAddress.getCountryId());
                    address.setStateId(mailAddress.getStateId());
                    address.setCity(mailAddress.getCity());
                    address.setCityId(mailAddress.getCityId());
                    address.setZipCode(mailAddress.getZipCode());
                    break;
                }
            }
        } else {
            Address[] mailAddressList = new Address[1];
            mailAddressList[0] = mailAddress;
            item.setMailAddresses(mailAddressList);
        }

        item.setNote((String) map.get(NOTE));
        item.setTitleID((Integer) map.get(TITLE_ID));
        item.setCurrencyId((Integer) map.get(CURRENCY_ID));
        item.setPaymentMethodId((Integer) map.get(PAYMENT_METHOD_ID));
        item.setVatNumber((String) map.get(VAT_NUMBER));
        item.setVatNumberId((Integer) map.get(VAT_NUMBER_ID));
        item.setLogoId((Integer) map.get(LOGO_ID));
        item.setLogoUrl((String) map.get(LOGO_URL));

        return item;
    }
}
