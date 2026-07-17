package com.workforcetrack.mobile.rpc.client;

import com.edatasite.workforce.gwt.client.client.rpc.BillingData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: HAveANiceDay
 * Date: 20.06.11
 * Time: 15:23
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "mBillingData")
public class MBillingData {
    @XmlElement
    private Integer clientID;
    @XmlElement
	private String address;
    @XmlElement
	private String city;
    @XmlElement
	private String zipCode;
    @XmlElement
	private MSelectItem country;
    @XmlElement
	private MSelectItem state;
    @XmlElementWrapper(name = "countries")
    @XmlElement(name = "country")
	private ArrayList<MSelectItem> countries = new ArrayList<>();
    @XmlElementWrapper(name = "states")
    @XmlElement(name = "state")
	private ArrayList<MSelectItem> states = new ArrayList<>();
    @XmlElement
	private Integer currencyId;
    @XmlElement
	private boolean nullInstance;

    public MBillingData() {

    }

    public MBillingData (BillingData billingData) {
        if (billingData != null) {
            this.clientID = billingData.getClientID();
            this.address = billingData.getAddress();
            this.city = billingData.getCity();
            this.zipCode = billingData.getZipCode();
            this.country = new MSelectItem(billingData.getCountry());
            this.state = new MSelectItem(billingData.getState());
            MSelectItemList mSelectItemList = new MSelectItemList();
            this.countries = mSelectItemList.convert(billingData.getCountries());
            this.states = mSelectItemList.convert(billingData.getStates());
            this.currencyId = billingData.getCurrencyId();
            this.nullInstance = billingData.isNullInstance();
        }
    }

    public Integer getClientID() {
        return clientID;
    }

    public void setClientID(Integer clientID) {
        this.clientID = clientID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public MSelectItem getCountry() {
        return country;
    }

    public void setCountry(MSelectItem country) {
        this.country = country;
    }

    public MSelectItem getState() {
        return state;
    }

    public void setState(MSelectItem state) {
        this.state = state;
    }

    public ArrayList<MSelectItem> getCountries() {
        return countries;
    }

    public void setCountries(ArrayList<MSelectItem> countries) {
        this.countries = countries;
    }

    public ArrayList<MSelectItem> getStates() {
        return states;
    }

    public void setStates(ArrayList<MSelectItem> states) {
        this.states = states;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public boolean isNullInstance() {
        return nullInstance;
    }

    public void setNullInstance(boolean nullInstance) {
        this.nullInstance = nullInstance;
    }
}
