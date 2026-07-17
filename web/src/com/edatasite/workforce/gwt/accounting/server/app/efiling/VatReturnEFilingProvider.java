package com.edatasite.workforce.gwt.accounting.server.app.efiling;

import com.edatasite.workforce.core.domain.accounting.EdsVatEFiling;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.server.app.efiling.irmark.HMRCMarkCalculator;
import govgateway.GovGatewayResponse;
import govgateway.PostAndGet;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 02.03.2010
 * Time: 13:04:11
 * To change this template use File | Settings | File Templates.
 */
public class VatReturnEFilingProvider implements AccountingConstants {
    public static String SUBMIT_LIVE = "https://secure.gateway.gov.uk/submission";
    public static String POLL_LIVE = "https://secure.gateway.gov.uk/poll";
    public static String SUBMIT_TEST = "https://secure.dev.gateway.gov.uk/submission";
    public static String POLL_TEST = "https://secure.dev.gateway.gov.uk/poll";
    public static String SUBMIT_TEST_LOCAL = "http://localhost:5665/LTS/LTSPostServlet";

    public static String SUBMISSION_URL = "SUBMISSION_URL";
    public static String POLL_URL = "POLL_URL";

    private DecimalFormat decimalFormat = new DecimalFormat("##0.00");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");

    private EdsVatEFiling vatReturn;

    private String senderID = "VATDEC059a01";
    private String password = "testing1";
    private String method = "clear";
    private String vatRegNo = "999900001";

    private Date toDate;
    private String irMarkXML;
    private String irMarkValue;
    private String submissionXML;
    private String responseContent;
    private String urlType;

    public VatReturnEFilingProvider(EdsVatEFiling vatReturn, String vatRegNo, String gatewayUserID, String gatewayUserPassword, String urlType) {
        this.vatReturn = vatReturn;
        this.senderID = gatewayUserID;
        this.password = gatewayUserPassword;
        this.toDate = vatReturn.getToDate();
        this.vatRegNo = vatRegNo;
        this.urlType = urlType;
    }

    public Integer processData() {
        try {
            GovGatewayResponse response = submitRequest();
            if (response == null || response.getCode() != 200) {
                responseContent = "Connection Failed";
                return SUBMISSION_PENDING;
            }
            responseContent = response.getContent();
            System.out.println("Status: " + response.getQualifier());
            System.out.println("Content: " + responseContent);
            if ("error".equals(response.getQualifier())) {
                return SUBMISSION_FAILED;
            }
            String correlationID = getValue("<CorrelationID>", "</CorrelationID>", response.getContent());
            System.out.println(correlationID);
            if ("response".equals(response.getQualifier())){
                return SUBMISSION_COMPLETED;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUBMISSION_PENDING;
    }

    private GovGatewayResponse submitRequest() {
        try {
            irMarkXML = getIRMarkXML();
            irMarkValue = new HMRCMarkCalculator().generateIRMark(irMarkXML);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("IR Mark: " + irMarkValue);

        System.out.println("-------------------------SUBMISSION_REQUEST_START-----------------------------");
        submissionXML = getSubmissionXML(irMarkValue);
        System.out.println("REQUEST_XML: " + submissionXML);
        GovGatewayResponse response = PostAndGet.doPostAndGetResponse(submissionXML, SUBMIT_TEST);
        System.out.println("-------------------------SUBMISSION_REQUEST_END-----------------------------");
        return response;
    }

    public String getIrMarkXML() {
        return irMarkXML;
    }

    public String getIrMarkValue() {
        return irMarkValue;
    }

    public String getSubmissionXML() {
        return submissionXML;
    }

    public String getResponseContent() {
        return responseContent;
    }

    private String format(BigDecimal amount) {
        return decimalFormat.format(amount).replace(",", ".");
    }

    private String formatAsIntValue(BigDecimal amount) {
        return String.valueOf(amount.intValue());
    }

    public static String getValue(String startTag, String endTag, String content) {
        if (content != null && content.contains(startTag)) {
            return content.substring(content.indexOf(startTag) + startTag.length(), content.indexOf(endTag));
        } else {
            return "";
        }
    }

    public static String getAttValue(String startTag, String endTag, String content) {
        if (content != null && content.contains(startTag)) {
            return content.substring(content.indexOf(startTag) + startTag.length(), content.indexOf(endTag, content.indexOf(startTag) + startTag.length()));
        } else {
            return "";
        }
    }

    public static void waiting(int n) {
        System.out.println("Wait " + n + " seconds");
        long t0, t1;

        t0 = System.currentTimeMillis();

        do {
            t1 = System.currentTimeMillis();
        }
        while ((t1 - t0) < (n * 1000L));
    }

    private String getSubmissionXML(String irMark) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<GovTalkMessage xmlns=\"http://www.govtalk.gov.uk/CM/envelope\">" +
                "<EnvelopeVersion>2.0</EnvelopeVersion>" +
                "<Header>" +
                "<MessageDetails>" +
                "<Class>HMRC-VAT-DEC</Class>" +
                "<Qualifier>request</Qualifier>" +
                "<Function>submit</Function>" +
                "<CorrelationID/>" +
                "<Transformation>XML</Transformation>" +
                "</MessageDetails>" +
                "<SenderDetails>" +
                "<IDAuthentication>" +
                "<SenderID>" + senderID + "</SenderID>" +
                "<Authentication>" +
                "<Method>clear</Method>" +
                "<Value>testing1</Value>" +
                "</Authentication>" +
                "</IDAuthentication>" +
                "</SenderDetails>" +
                "</Header>" +
                "<GovTalkDetails>" +
                "<Keys>" +
                "<Key Type=\"VATRegNo\">"+vatRegNo+"</Key>" +
                "</Keys>" +
                "<ChannelRouting>" +
                "<Channel>" +
                "<URI>0147</URI>" +
                "<Product>SDS</Product>" +
                "<Version>2.02</Version>" +
                "</Channel>" +
                "</ChannelRouting>" +
                "</GovTalkDetails>" +
                "<Body>" +
                "<vat:IRenvelope xmlns:vat=\"http://www.govtalk.gov.uk/taxation/vat/vatdeclaration/2\">" +
                "<vat:IRheader>" +
                "<vat:Keys>" +
                "<vat:Key Type=\"VATRegNo\">"+vatRegNo+"</vat:Key>" +
                "</vat:Keys>" +
                "<vat:PeriodID>" + dateFormat.format(toDate) + "</vat:PeriodID>" +
                "<vat:IRmark Type=\"generic\">"+irMark+"</vat:IRmark>" +
                "<vat:Sender>Employer</vat:Sender>" +
                "</vat:IRheader>" +
                "<vat:VATDeclarationRequest>" +
                "<vat:VATDueOnOutputs>" + format(vatReturn.getVatOnSalesAndOutputs()) + "</vat:VATDueOnOutputs>" +
                "<vat:VATDueOnECAcquisitions>" + format(vatReturn.getVatFromECMemberStates()) + "</vat:VATDueOnECAcquisitions>" +
                "<vat:TotalVAT>" + format(vatReturn.getTotalVatDue()) + "</vat:TotalVAT>" +
                "<vat:VATReclaimedOnInputs>" + format(vatReturn.getVatOnPurchaseAndInputs()) + "</vat:VATReclaimedOnInputs>" +
                "<vat:NetVAT>" + format(vatReturn.getVatToReclaimFromCustoms()) + "</vat:NetVAT>" +
                "<vat:NetSalesAndOutputs>" + formatAsIntValue(vatReturn.getTotalSalesAndOutputs()) + "</vat:NetSalesAndOutputs>" +
                "<vat:NetPurchasesAndInputs>" + formatAsIntValue(vatReturn.getTotalPurchasesAndInputs()) + "</vat:NetPurchasesAndInputs>" +
                "<vat:NetECSupplies>" + formatAsIntValue(vatReturn.getTotalSupplies()) + "</vat:NetECSupplies>" +
                "<vat:NetECAcquisitions>" + formatAsIntValue(vatReturn.getTotalAcquisitions()) + "</vat:NetECAcquisitions>" +
                "</vat:VATDeclarationRequest>" +
                "</vat:IRenvelope>" +
                "</Body>" +
                "</GovTalkMessage>";
    }

    private String getIRMarkXML() {
        return "<Body xmlns=\"http://www.govtalk.gov.uk/CM/envelope\">" +
                "<vat:IRenvelope xmlns:vat=\"http://www.govtalk.gov.uk/taxation/vat/vatdeclaration/2\">" +
                "<vat:IRheader>" +
                "<vat:Keys>" +
                "<vat:Key Type=\"VATRegNo\">" + vatRegNo + "</vat:Key>" +
                "</vat:Keys>" +
                "<vat:PeriodID>" + dateFormat.format(toDate) + "</vat:PeriodID>" +
                "<vat:Sender>Employer</vat:Sender>" +
                "</vat:IRheader>" +
                "<vat:VATDeclarationRequest>" +
                "<vat:VATDueOnOutputs>" + format(vatReturn.getVatOnSalesAndOutputs()) + "</vat:VATDueOnOutputs>" +
                "<vat:VATDueOnECAcquisitions>" + format(vatReturn.getVatFromECMemberStates()) + "</vat:VATDueOnECAcquisitions>" +
                "<vat:TotalVAT>" + format(vatReturn.getTotalVatDue()) + "</vat:TotalVAT>" +
                "<vat:VATReclaimedOnInputs>" + format(vatReturn.getVatOnPurchaseAndInputs()) + "</vat:VATReclaimedOnInputs>" +
                "<vat:NetVAT>" + format(vatReturn.getVatToReclaimFromCustoms()) + "</vat:NetVAT>" +
                "<vat:NetSalesAndOutputs>" + formatAsIntValue(vatReturn.getTotalSalesAndOutputs()) + "</vat:NetSalesAndOutputs>" +
                "<vat:NetPurchasesAndInputs>" + formatAsIntValue(vatReturn.getTotalPurchasesAndInputs()) + "</vat:NetPurchasesAndInputs>" +
                "<vat:NetECSupplies>" + formatAsIntValue(vatReturn.getTotalSupplies()) + "</vat:NetECSupplies>" +
                "<vat:NetECAcquisitions>" + formatAsIntValue(vatReturn.getTotalAcquisitions()) + "</vat:NetECAcquisitions>" +
                "</vat:VATDeclarationRequest>" +
                "</vat:IRenvelope>" +
                "</Body>";
    }

    private String getPollXML(String correlationID) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<GovTalkMessage xmlns=\"http://www.govtalk.gov.uk/CM/envelope\">\n" +
                "      <EnvelopeVersion>2.0</EnvelopeVersion>\n" +
                "      <Header>\n" +
                "                <MessageDetails>\n" +
                "                                <Class>HMRC-VAT-DEC</Class>\n" +
                "                                <Qualifier>poll</Qualifier>\n" +
                "                                <Function>submit</Function>\n" +
//              "                                <TransactionID>20021202ABC</TransactionID>\n" +
                "                                <CorrelationID>" + correlationID + "</CorrelationID>\n" +
                "                                <Transformation>XML</Transformation>\n" +
                //"                                <GatewayTest>1</GatewayTest>\n" +
                "                                <GatewayTimestamp></GatewayTimestamp>\n" +
                "                </MessageDetails>\n" +
                "                <SenderDetails/>\n" +
                "      </Header>\n" +
                "      <GovTalkDetails>\n" +
                "                <Keys/>\n" +
                "      </GovTalkDetails>\n" +
                "      <Body/>\n" +
                "</GovTalkMessage>";
    }

    private GovGatewayResponse submitPOLL(String correlationID) {
        System.out.println("-------------------------SUBMISSION_POLL-----------------------------");
        String strXML = getPollXML(correlationID);
        System.out.println("POLL XML: " + strXML);
        return PostAndGet.doPostAndGetResponse(strXML, POLL_TEST);
    }

    private static GovGatewayResponse deleteRequest(String correlationID) {
        System.out.println("-------------------------DELETE_REQUEST-----------------------------");
        String xml = "<?xml version=\"1.0\"?>\n" +
                "<GovTalkMessage xmlns=\"http://www.govtalk.gov.uk/CM/envelope\">\n" +
                "   <EnvelopeVersion>2.0</EnvelopeVersion>\n" +
                "   <Header>\n" +
                "       <MessageDetails>\n" +
                "           <Class>HMRC-VAT-DEC</Class>\n" +
                "           <Qualifier>request</Qualifier>\n" +
                "           <Function>delete</Function>\n" +
                "           <CorrelationID>" + correlationID + "</CorrelationID>\n" +
                "           <Transformation>XML</Transformation>\n" +
                "           <GatewayTimestamp/>\n" +
                "       </MessageDetails>\n" +
                "       <SenderDetails/>\n" +
                "   </Header>\n" +
                "<GovTalkDetails>\n" +
                "   <Keys/>\n" +
                "</GovTalkDetails>\n" +
                "<Body></Body>\n" +
                "</GovTalkMessage>";
        return PostAndGet.doPostAndGetResponse(xml, null);
    }

    private GovGatewayResponse dataRequest() {
        System.out.println("-------------------------DATA_REQUEST-----------------------------");
        String xml = "<?xml version=\"1.0\"?>\n" +
                "<GovTalkMessage xmlns=\"http://www.govtalk.gov.uk/CM/envelope\">\n" +
                "   <EnvelopeVersion>2.0</EnvelopeVersion>\n" +
                "   <Header>\n" +
                "       <MessageDetails>\n" +
                "           <Class>HMRC-VAT-DEC</Class>\n" +
                "           <Qualifier>request</Qualifier>\n" +
                "           <Function>list</Function>\n" +
                "           <CorrelationID/>\n" +
                "           <Transformation>XML</Transformation>\n" +
                "           <GatewayTimestamp/>\n" +
                "       </MessageDetails>\n" +
                "                <SenderDetails>\n" +
                "                                <IDAuthentication>\n" +
                "                                                <SenderID>" + senderID + "</SenderID>\n" +
                "                                                <Authentication>\n" +
                "                                                                <Method>" + method + "</Method>\n" +
                "                                                                <Value>" + password + "</Value>\n" +
                "                                                </Authentication>\n" +
                "                                </IDAuthentication>\n" +
                "                                <EmailAddress>test1@api.co.uk</EmailAddress>\n" +
                "                </SenderDetails>\n" +
                "   </Header>\n" +
                "<GovTalkDetails>\n" +
                "   <Keys/>\n" +
                "</GovTalkDetails>\n" +
                "<Body>" +
                "   <IncludeIdentifiers>1</IncludeIdentifiers>\n" +
                "   <StartDate>13/11/2009</StartDate>\n" +
                "   <StartTime>16:00:00</StartTime>\n" +
                "   <EndDate>13/02/2010</EndDate>\n" +
                "   <EndTime>16:30:00</EndTime>" +
                "</Body>\n" +
                "</GovTalkMessage>";
        return PostAndGet.doPostAndGetResponse(xml, null);
    }
}
