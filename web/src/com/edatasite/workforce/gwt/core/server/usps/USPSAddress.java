package com.edatasite.workforce.gwt.core.server.usps;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 7/16/12
 * Time: 6:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class USPSAddress {
    private Integer ID = 0;
    private String firmName;
    private String contact;
    private String contactEmail;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zip;
    private String zipPlus4;

    private boolean isZipCodeLookUp;

    public USPSAddress(boolean isZipCodeLookUp) {
        this.isZipCodeLookUp = isZipCodeLookUp;
    }

    public String toXML() {
        StringBuilder sb = new StringBuilder();
        if (isZipCodeLookUp) {
            sb.append("<ZipCodeLookupRequest USERID=\"" + USPSWebService.USPS_USER_ID + "\">");
        } else {
            sb.append("<AddressValidateRequest USERID=\"" + USPSWebService.USPS_USER_ID + "\">");
        }
        sb.append("<Address ID=\"" + this.ID.toString() + "\">");
        sb.append("<Address1>" + this.address1 + "</Address1>");
        sb.append("<Address2>" + this.address2 + "</Address2>");
        sb.append("<City>" + this.city + "</City>");
        sb.append("<State>" + this.state + "</State>");
        sb.append("<Zip5>" + this.zip + "</Zip5>");
        sb.append("<Zip4>" + this.zipPlus4 + "</Zip4>");
        sb.append("</Address>");

        if (isZipCodeLookUp) {
            sb.append("</ZipCodeLookupRequest>");
        } else {
            sb.append("</AddressValidateRequest>");
        }
        return sb.toString();
    }

    public static USPSAddress getDataFromXML(String responseXML) {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

            InputStream inputStream = new ByteArrayInputStream(responseXML.getBytes());
            Document document = docBuilder.parse(inputStream);

            USPSAddress uspsAddress = new USPSAddress(false);
            NodeList firmName = document.getElementsByTagName("FirmName");
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        try {
            if (address1.length() > 38)
                throw new USPSWebServiceException("Address1 is limited to a maximum of 38 characters.");
            this.address1 = address1;
        } catch (USPSWebServiceException e) {
            e.printStackTrace();
        }
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        try {
            if (address2.length() > 38)
                throw new USPSWebServiceException("Address2 is limited to a maximum of 38 characters.");
            this.address2 = address2;
        } catch (USPSWebServiceException e) {
            e.printStackTrace();
        }
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        try {
            if (city.length() > 15)
                throw new USPSWebServiceException("City is limited to a maximum of 15 characters.");
            this.city = city;
        } catch (USPSWebServiceException e) {
            e.printStackTrace();
        }
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        try {
            if (state.length() > 2)
                throw new USPSWebServiceException("State is limited to a maximum of 2 characters.");
            this.state = state;
        } catch (USPSWebServiceException e) {
            e.printStackTrace();
        }
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        try {
            if (zip.length() > 5)
                throw new USPSWebServiceException("Zip is limited to a maximum of 5 characters.");
            this.zip = zip;
        } catch (USPSWebServiceException e) {
            e.printStackTrace();
        }
    }

    public String getZipPlus4() {
        return zipPlus4;
    }

    public void setZipPlus4(String zipPlus4) {
        try {
            if (zipPlus4.length() > 4)
                throw new USPSWebServiceException("ZipPlus4 is limited to a maximum of 4 characters.");
            this.zipPlus4 = zipPlus4;
        } catch (USPSWebServiceException e) {
            e.printStackTrace();
        }
    }
}
