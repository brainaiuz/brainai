package com.edatasite.workforce.gwt.invoice.client.rpc.usps;

import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 7/17/12
 * Time: 4:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class USPSPackage implements IsSerializable, AccountingConstants{
    private String serviceType;
    private String firstClassMailType;
    private String zipOrigination;
    private String zipDestination;
    private String pounds;
    private String ounces;
    private String container;
    private String size;
    private String machinable;
    private String width;
    private String length;
    private String height;
    private String girth;
    private String shipDate;//dd-MMM-yyyy

    private ArrayList<USPSPostage> postages;

    public USPSPackage() {
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getFirstClassMailType() {
        return firstClassMailType;
    }

    public void setFirstClassMailType(String firstClassMailType) {
        this.firstClassMailType = firstClassMailType;
    }

    public String getZipOrigination() {
        return zipOrigination;
    }

    public void setZipOrigination(String zipOrigination) {
        this.zipOrigination = zipOrigination;
    }

    public String getZipDestination() {
        return zipDestination;
    }

    public void setZipDestination(String zipDestination) {
        this.zipDestination = zipDestination;
    }

    public String getPounds() {
        return pounds;
    }

    public void setPounds(String pounds) {
        this.pounds = pounds;
    }

    public String getOunces() {
        return ounces;
    }

    public void setOunces(String ounces) {
        this.ounces = ounces;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMachinable() {
        return machinable;
    }

    public void setMachinable(String machinable) {
        this.machinable = machinable;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getGirth() {
        return girth;
    }

    public void setGirth(String girth) {
        this.girth = girth;
    }

    public String getShipDate() {
        return shipDate;
    }

    public void setShipDate(String shipDate) {
        this.shipDate = shipDate;
    }

    public ArrayList<USPSPostage> getPostages() {
        if (postages == null) {
            postages = new ArrayList<>();
        }
        return postages;
    }

    public void setPostages(ArrayList<USPSPostage> postages) {
        this.postages = postages;
    }

    public String toXML(Integer packageID) {
        StringBuilder sb = new StringBuilder();
        sb.append("<Package ID=\"" + packageID + "\">");
        sb.append("<Service>" + serviceType + "</Service>");
        if (serviceType.startsWith(SHIPPING_SERVICE.FIRST_CLASS) || serviceType.equals(SHIPPING_SERVICE.ALL)) {
            sb.append("<FirstClassMailType>" + firstClassMailType + "</FirstClassMailType>");
        }
        sb.append("<ZipOrigination>" + zipOrigination + "</ZipOrigination>");
        sb.append("<ZipDestination>" + zipDestination + "</ZipDestination>");
        sb.append("<Pounds>" + (pounds != null ? pounds : 0) + "</Pounds>");
        sb.append("<Ounces>" + (ounces != null ? ounces : 0) + "</Ounces>");
        if (container != null) {
            sb.append("<Container>" + container + "</Container>");
        } else {
            sb.append("<Container/>");
        }
        sb.append("<Size>" + size + "</Size>");
        if ("LARGE".equals(size) && width != null && length != null && height != null) {
            sb.append("<Width>" + width + "</Width>");
            sb.append("<Length>" + length + "</Length>");
            sb.append("<Height>" + height + "</Height>");
            if ("NONRECTANGULAR".equals(container)) {
                sb.append("<Girth>" + girth + "</Girth>");
            }
        }
        sb.append("<Machinable>" + machinable + "</Machinable>");
        if (shipDate != null) {
            sb.append("<ShipDate>" + shipDate + "</ShipDate>");
        }
        sb.append("</Package>");

        return sb.toString();
    }

    public static USPSPackage createUSPSPackage(String service, ShippingLabelData labelData, String size) {
        return createUSPSPackage(service, labelData, size, null);
    }

    public static USPSPackage createUSPSPackage(String service, ShippingLabelData labelData, String size, String firstClassMailType) {
        USPSPackage uspsPackage = new USPSPackage();
        uspsPackage.setServiceType(service);
        uspsPackage.setFirstClassMailType(firstClassMailType != null ? firstClassMailType : labelData.getFirstClassMailType());
        uspsPackage.setZipOrigination(labelData.getFromZip());
        uspsPackage.setZipDestination(labelData.getToZip());
        if (labelData.getPounds() != null) {
            uspsPackage.setPounds(labelData.getPounds().toString());
        }
        if (labelData.getOunces() != null) {
            uspsPackage.setOunces(labelData.getOunces().toString());
        }
        uspsPackage.setSize(size);

        if (AccountingConstants.LARGE_PACKAGE.equals(labelData.getServiceType())) {
            uspsPackage.setContainer(labelData.getContainer());
            if (labelData.getWidth() != null) {
                uspsPackage.setWidth(labelData.getWidth().toString());
            }
            if (labelData.getLength() != null) {
                uspsPackage.setLength(labelData.getLength().toString());
            }
            if (labelData.getHeight() != null) {
                uspsPackage.setHeight(labelData.getHeight().toString());
            }
            if (labelData.getGirth() != null) {
                uspsPackage.setGirth(labelData.getGirth().toString());
            }
        }
        uspsPackage.setMachinable("false");
        uspsPackage.setShipDate(labelData.getShipDate());

        return uspsPackage;
    }
}
