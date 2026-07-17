package com.workforcetrack.mobile.rpc.client;


import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.ClientContact;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: HAveANiceDay
 * Date: 20.06.11
 * Time: 15:18
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "clientListItem")
public class MClientListItem {

    //ClientListItem
    private String code;
    private Integer objectID;
    private String ownerName;
    private Integer ownerID;
    @XmlElementWrapper(name = "ownerItems")
    @XmlElement(name = "ownerItem")
    private List<MSelectItem> ownerItems;
    private String name;
    private String currency;
    private Integer currencyID;
    private String email;
    private String phone;
    private String fax;
    private String vatNumber;
    private String registrationNumber;
    @XmlElement
    private String website;
    private MAdressData primaryBillAdress;
    private MAdressData primaryMailAdress;
    private String bill_address1;
    private Integer primaryBillAddressID;
    private List<MSelectItem> billAddresses;


//ClientContactList


    private String contact_objectID;

    private String contact_firstName;

    private String contact_lastName;

    private String contact_email;

    private String contact_phone;

    private String contact_primaryContact;

    private String contact_position;


    private MClientContactListItem clientContact;


    public MClientListItem() {
    }

    public MClientListItem(CrmAccountItem clientListItem) {
        if (clientListItem != null) {
            this.objectID = clientListItem.getObjectId();
            //this.ownerName = clientListItem.getOwnerName();
            //this.ownerID = clientListItem.getOwnerID();

            if (clientListItem.getOwnerItems() != null) {
                this.ownerItems = new ArrayList<>();
                for (SelectItem selectItem : clientListItem.getOwnerItems()) {
                    this.ownerItems.add(new MSelectItem(selectItem));
                }
            }
            this.name = clientListItem.getName();
            this.email = clientListItem.getEmail();
            this.phone = clientListItem.getPhone();
            this.fax = clientListItem.getFax();
            this.website = clientListItem.getWebsite();
            this.primaryBillAdress = new MAdressData(clientListItem.getBillAddresses());
            this.primaryMailAdress = new MAdressData(clientListItem.getMailAddresses());
            this.vatNumber = clientListItem.getVatNumber();
            this.registrationNumber = clientListItem.getRegistrationNumber();
            this.currency = clientListItem.getCurrency();
            this.currencyID = clientListItem.getCurrencyId();
            this.code = clientListItem.getCode();

        }
    }

    public CrmAccountItem convertToCrmAccountItem(CrmAccountItem crmAccountItem, ContactListItem contactListItem) {
        if (crmAccountItem == null) {
            crmAccountItem = new CrmAccountItem();
        }
        crmAccountItem.setObjectId(this.objectID);
        //crmAccountItem.setOwnerID(this.ownerID);
        //crmAccountItem.setOwnerName(this.ownerName);
        crmAccountItem.setName(this.name);
        crmAccountItem.setCurrency(this.currency);
        crmAccountItem.setCurrencyId(this.currencyID);
        crmAccountItem.setPhone(this.phone);
        crmAccountItem.setEmail(this.email);
        crmAccountItem.setWebsite(this.website);
        crmAccountItem.setFax(this.fax);
        crmAccountItem.setCode(this.code);
        crmAccountItem.setVatNumber(this.vatNumber);
        crmAccountItem.setRegistrationNumber(this.registrationNumber);
        MAdressData datav = this.primaryMailAdress;
        MAdressData datav2 = this.primaryBillAdress;
        Address[] datas = crmAccountItem.getBillAddresses();
        if (datas != null && datas.length > 0) {
            for (Address address : datas) {
                if (address.isPrimary()) {
                    address.setAddress(this.primaryBillAdress.getAddress());
                    address.setAddressb(this.primaryBillAdress.getAddressb());
                    address.setCity(this.primaryBillAdress.getCity());
                    address.setCountry(this.primaryBillAdress.getCountry());
                    address.setCountryId(this.primaryBillAdress.getCountryID());
                    address.setPrimary(true);
                    address.setState(this.primaryBillAdress.getState());
                    address.setStateId(this.primaryBillAdress.getStateID());
                    address.setName(this.primaryBillAdress.getName());
                    //addressData.setZipCode(this.primaryBillAdress.);
                }
            }
            crmAccountItem.setBillAddresses(datas);
        } else {
            Address data = new Address();
            data = this.primaryBillAdress.convertToAD(data);
            List<Address> list = new ArrayList<>();
            list.add(data);
            crmAccountItem.setBillAddresses(list.toArray(new Address[]{}));
        }

        Address[] datamail = crmAccountItem.getMailAddresses();
        if (datamail != null && datamail.length > 0) {
            for (Address address : datamail) {
                if (address.isPrimary()) {
                    address.setAddress(this.primaryMailAdress.getAddress());
                    address.setAddressb(this.primaryMailAdress.getAddressb());
                    address.setCity(this.primaryMailAdress.getCity());
                    address.setCountry(this.primaryMailAdress.getCountry());
                    address.setCountryId(this.primaryMailAdress.getCountryID());
                    address.setPrimary(true);
                    address.setState(this.primaryMailAdress.getState());
                    address.setStateId(this.primaryMailAdress.getStateID());
                    address.setName(this.primaryMailAdress.getName());
                    //addressData.setZipCode(this.primaryMailAdress);

                }
            }
            crmAccountItem.setMailAddresses(datamail);
        } else {
            Address datas2 = new Address();
            datas2 = this.primaryMailAdress.convertToAD(datas2);
            ArrayList<Address> list = new ArrayList<>();
            list.add(datas2);
            crmAccountItem.setMailAddresses(list.toArray(new Address[]{}));
        }
        if (contactListItem == null) {
            contactListItem = new ContactListItem();
        }
        contactListItem.getHomeEmail().add(this.contact_email);
        contactListItem.setPrimaryEmail(this.contact_email);
        contactListItem.getHomePhone().add(this.contact_phone);
        contactListItem.setPrimaryPhone(this.contact_phone);
        contactListItem.setFirstName(this.contact_firstName);
        contactListItem.setLastName(this.contact_lastName);
        contactListItem.setPrimaryContact(true);
        contactListItem.setCrmAccount(crmAccountItem);
        ArrayList<ContactListItem> contacts = new ArrayList<>();
        contacts.add(contactListItem);
        crmAccountItem.setContacts(contacts);


        return crmAccountItem;
    }

    public ContactListItem convertPrimaryContact(ContactListItem contactListItem) {
        if (contactListItem == null) {
            contactListItem = new ContactListItem();
        }
        contactListItem.setPrimaryEmail(this.contact_email);
        contactListItem.setPrimaryPhone(this.contact_phone);
        contactListItem.setFirstName(this.contact_firstName);
        contactListItem.setLastName(this.contact_lastName);
        contactListItem.setPrimaryContact(true);


        return contactListItem;

    }

    public ContactListItem convertFromContactListItem(CrmAccountItem crmAccountItem) {
        ContactListItem contactListItem = new ContactListItem();
        contactListItem.setPrimaryEmail(this.contact_email);
        contactListItem.setPrimaryPhone(this.contact_phone);
        contactListItem.setFirstName(this.contact_firstName);
        contactListItem.setLastName(this.contact_lastName);
        contactListItem.setPrimaryContact(true);
        contactListItem.setCrmAccount(crmAccountItem);

        return contactListItem;
    }


    public ClientContact convertToClientContact() {
        ClientContact clientContact = new ClientContact();

        clientContact.setEmail(this.contact_email);
        clientContact.setPhone(this.contact_phone);
        clientContact.setFirstName(this.contact_firstName);
        clientContact.setLastName(this.contact_lastName);
        clientContact.setPrimaryContact(true);
        clientContact.setClientId(this.objectID);
        return clientContact;

    }

    public String getBill_address1() {
        return bill_address1;
    }

    public void setBill_address1(String bill_address1) {
        this.bill_address1 = bill_address1;
    }


    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getContact_objectID() {
        return contact_objectID;
    }

    public void setContact_objectID(String contact_objectID) {
        this.contact_objectID = contact_objectID;
    }

    public String getContact_firstName() {
        return contact_firstName;
    }

    public void setContact_firstName(String contact_firstName) {
        this.contact_firstName = contact_firstName;
    }

    public String getContact_lastName() {
        return contact_lastName;
    }

    public void setContact_lastName(String contact_lastName) {
        this.contact_lastName = contact_lastName;
    }

    public String getContact_email() {
        return contact_email;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email;
    }

    public String getContact_phone() {
        return contact_phone;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
    }

    public String getContact_primaryContact() {
        return contact_primaryContact;
    }

    public void setContact_primaryContact(String contact_primaryContact) {
        this.contact_primaryContact = contact_primaryContact;
    }

    public String getContact_position() {
        return contact_position;
    }

    public void setContact_position(String contact_position) {
        this.contact_position = contact_position;
    }

    public String getClientCode() {
        return code;
    }

    public void setClientCode(String clientCode) {
        this.code = clientCode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Integer getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(Integer ownerID) {
        this.ownerID = ownerID;
    }

    public List<MSelectItem> getOwnerItems() {
        return ownerItems;
    }

    public void setOwnerItems(List<MSelectItem> ownerItems) {
        this.ownerItems = ownerItems;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }


    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }


    public MAdressData getPrimaryBillAdress() {
        return primaryBillAdress;
    }

    public void setPrimaryBillAdress(MAdressData primaryBillAdress) {
        this.primaryBillAdress = primaryBillAdress;
    }

    public MAdressData getPrimaryMailAdress() {
        return primaryMailAdress;
    }

    public void setPrimaryMailAdress(MAdressData primaryMailAdress) {
        this.primaryMailAdress = primaryMailAdress;
    }

    public MClientContactListItem getClientContact() {
        return clientContact;
    }

    public void setClientContact(MClientContactListItem clientContact) {
        this.clientContact = clientContact;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(Integer currencyID) {
        this.currencyID = currencyID;
    }

    public Integer getPrimaryBillAddressID() {
        return primaryBillAddressID;
    }

    public void setPrimaryBillAddressID(Integer primaryBillAddressID) {
        this.primaryBillAddressID = primaryBillAddressID;
    }

    public List<MSelectItem> getBillAddresses() {
        return billAddresses;
    }

    public void setBillAddresses(List<MSelectItem> billAddresses) {
        this.billAddresses = billAddresses;
    }
}
