package com.edatasite.workforce.gwt.core.server.usps;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.gwt.invoice.client.rpc.usps.USPSPackage;
import com.edatasite.workforce.gwt.invoice.client.rpc.usps.USPSPostage;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.xerces.dom.DeferredElementImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 6/29/12
 * Time: 3:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class USPSWebService {
    public static final String USPS_LIVE_API_URL = "http://production.shippingapis.com/ShippingAPI.dll";
    public static final String USPS_SECURE_LIVE_API_URL = "https://secure.shippingapis.com/ShippingAPI.dll";

    public static final String USPS_USER_ID = "927SHERM7403";
    public static final String USPS_PASSWORD = "764IM82HQ257";

    private String apiName;
    private String uspsSubmissionURL;
    private String uspsUserID;
    private String uspsUserPassword;

    public USPSWebService(String apiName, boolean isTestServer, String uspsUserID, String uspsUserPassword) {
        this.apiName = apiName;
        this.uspsSubmissionURL = ("DeliveryConfirmationV3".equals(apiName) || "ExpressMailLabel".equals(apiName) || "ExpressMailLabelCertify".equals(apiName)) ? USPS_SECURE_LIVE_API_URL : USPS_LIVE_API_URL;
        this.uspsUserID = (uspsUserID != null ? uspsUserID : USPS_USER_ID);
        this.uspsUserPassword = (uspsUserPassword != null ? uspsUserPassword : USPS_PASSWORD);
    }

    public static USPSPackage[] parseRateAPIResponse(String responseXML) {

        USPSPackage[] packageArray = null;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new ByteArrayInputStream(responseXML.getBytes(StandardCharsets.UTF_8)));
            doc.getDocumentElement().normalize();

            NodeList packagesList = doc.getElementsByTagName("Package");
            if (packagesList != null && packagesList.getLength() > 0) {
                packageArray = new USPSPackage[packagesList.getLength()];
                for (int i = 0; i < packagesList.getLength(); i++) {
                    Node packageItem = packagesList.item(i);
                    packageArray[i] = new USPSPackage();

                    NodeList fcmTypeNodeList = ((DeferredElementImpl) packageItem).getElementsByTagName("FirstClassMailType");
                    NodeList sizeNodeList = ((DeferredElementImpl) packageItem).getElementsByTagName("Size");

                    if (fcmTypeNodeList != null && fcmTypeNodeList.getLength() > 0) {
                        packageArray[i].setFirstClassMailType(fcmTypeNodeList.item(0).getTextContent());
                    }
                    if (sizeNodeList != null && sizeNodeList.getLength() > 0) {
                        packageArray[i].setSize(sizeNodeList.item(0).getTextContent());
                    }

                    NodeList postageNodeList = ((DeferredElementImpl) packageItem).getElementsByTagName("Postage");

                    for (int j = 0; j < postageNodeList.getLength(); j++) {
                        NodeList mailService = postageNodeList.item(j).getChildNodes();
                        USPSPostage postage = new USPSPostage();
                        postage.setMailService(mailService.item(0).getTextContent().replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&amp;", "&"));
                        postage.setAmount(new BigDecimal(mailService.item(1).getTextContent()));
                        postage.setClassID(((DeferredElementImpl) mailService).getAttribute("CLASSID"));
                        packageArray[i].getPostages().add(postage);
                    }
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }

        return packageArray;
    }

    public String submitRequestAndGetResponse(String requestXML) {
        String requestURL = getRequestURL(requestXML);
        // Prepare HTTP post
        HttpGet method = new HttpGet(requestURL);
        method.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        method.setHeader("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.3");
        method.setHeader("Accept-Encoding", "gzip,deflate,sdch");
        String responseXML = null;
        // Get HTTP client
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            // Execute request
            HttpResponse httpResponse = httpclient.execute(method);
            int responseStatusCode = httpResponse.getStatusLine().getStatusCode();
            // Display status code
            System.out.println("Response status code: " + responseStatusCode);
            if (responseStatusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + httpResponse.getStatusLine());
//                return null;
            }
            responseXML = EntityUtils.toString(httpResponse.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Release current connection to the connection pool
            // once you are done
            method.releaseConnection();
        }

        return responseXML;
    }

    private String getRequestURL(String requestXML) {
        return uspsSubmissionURL + "?API=" + apiName + "&XML=" + EncryptionHelper.encodeURL(requestXML);
    }
}
