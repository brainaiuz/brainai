package com.edatasite.workforce.gwt.invoice.client.rpc.usps;

import com.edatasite.workforce.gwt.core.server.usps.USPSWebService;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 7/3/12
 * Time: 4:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class USPSRates {

    private USPSPackage[] packages;

    public USPSRates(USPSPackage... packages) {
        this.packages = packages;
    }

    public String toXML() {
        StringBuilder sb = new StringBuilder();
        sb.append("<RateV4Request USERID=\"" + USPSWebService.USPS_USER_ID + "\">");
        sb.append("<Revision>2</Revision>");

        int packageID = 1;
        for (USPSPackage uspsPackage : packages) {
            sb.append(uspsPackage.toXML(packageID++));
        }

        sb.append("</RateV4Request>");

        return sb.toString();
    }
}
