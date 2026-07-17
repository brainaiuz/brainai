package com.edatasite.workforce.scheduler;

import com.edatasite.workforce.core.domain.accounting.EdsVatEFiling;
import com.edatasite.workforce.core.domain.payrolluk.EdsCompanyPayrollSettings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.server.app.efiling.VatReturnEFilingProvider;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.accounting.VatEFilingManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.CompanyPayrollSettingsManager;
import com.edatasite.workforce.mail.IBaseJob;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import govgateway.GovGatewayResponse;
import govgateway.PostAndGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar
 * Date: Mar 29, 2010
 * Time: 4:38:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class EfileSubmitterJob implements IBaseJob, Constants {
    private static final Logger log = LoggerFactory.getLogger(EfileSubmitterJob.class);

    @Autowired
    private CompanyPayrollSettingsManager companyPayrollSettingsManager;
    @Autowired
    private VatEFilingManager vatEFilingManager;

    private String econApplicable = "DEFGHKLNOSV";

    @Transactional
    public void execute() {
//        submitP35();
        submitVatReturn();
    }

    private void submitVatReturn() {
        final List<EdsVatEFiling> vatReturnReports = vatEFilingManager.getUnsubmittedVatReturnReports();
        for (EdsVatEFiling vr : vatReturnReports) {
            Integer companyID = vr.getCompanyID();
            ServerSecurityContext.getInstance().setCompanyId(companyID);
            final EdsCompanyPayrollSettings gatewayUserID = companyPayrollSettingsManager.getCompanySettingValue(GATEWAY_USER_ID);
            final EdsCompanyPayrollSettings gatewayUserPassword = companyPayrollSettingsManager.getCompanySettingValue(GATEWAY_USER_PASSWORD);
            final EdsCompanyPayrollSettings vatRegNo = companyPayrollSettingsManager.getCompanySettingValue(VAT_REGISTRATION_NUMBER);
            if (!isGatewayParamExists(gatewayUserID, gatewayUserPassword, companyID)) {
                return;
            }
            if (vatRegNo == null || vatRegNo.getValue() == null || "".equals(vatRegNo.getValue().trim())) {
                log.info("VAT Reg No is not provided");
                return;
            }

            final VatReturnEFilingProvider eFilingProvider = new VatReturnEFilingProvider(vr, vatRegNo.getValue(),
                    gatewayUserID.getValue(), gatewayUserPassword.getValue(), null);
            final Integer submissionStatus = eFilingProvider.processData();
            vr.setIrMarkXML(eFilingProvider.getIrMarkXML());
            vr.setIrMarkValue(eFilingProvider.getIrMarkValue());
            vr.setSubmissionXML(eFilingProvider.getSubmissionXML());
            vr.setResponseContent(eFilingProvider.getResponseContent());
            vr.setHmrcReference(getValue("<CorrelationID>", "</CorrelationID>", eFilingProvider.getResponseContent()));
            if (AccountingConstants.SUBMISSION_COMPLETED.equals(submissionStatus)) {
                vr.setStatus(AccountingConstants.SUBMISSION_COMPLETED);
            } else {
                String bodyXml = getValue("<Body>", "</Body>", eFilingProvider.getResponseContent());
                String errorContent = getValue("<Error>", "</Error>", bodyXml);
                String errorCode = getValue("<Number>", "</Number>", errorContent);
                String errorMessage = getValue("<Text>", "</Text>", errorContent);
                vr.setErrorCode(errorCode);
                vr.setMessage(errorMessage);
                vr.setStatus(VatReturnEFilingProvider.SUBMISSION_FAILED);
            }
        }
    }

    private boolean isGatewayParamExists(EdsCompanyPayrollSettings gatewayUserID, EdsCompanyPayrollSettings gatewayUserPassword, Integer companyID) {
        if (gatewayUserID == null || gatewayUserID.getValue() == null || "".equals(gatewayUserID.getValue().trim())
                || gatewayUserPassword == null || gatewayUserPassword.getValue() == null || "".equals(gatewayUserPassword.getValue().trim())) {
            log.info("Government Gateway User ID or Password does not exist. COMPANY_ID:" + companyID);
            return false;
        }
        return true;
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

    private static GovGatewayResponse submitPOLL(String correlationID, boolean isTestService) {
        //System.out.println("-------------------------SUBMISSION_POLL-----------------------------");
        String strXML = "<?xml version=\"1.0\"?>\n" +
                "<GovTalkMessage xmlns=\"http://www.govtalk.gov.uk/CM/envelope\">\n" +
                "      <EnvelopeVersion>2.0</EnvelopeVersion>\n" +
                "      <Header>\n" +
                "                <MessageDetails>\n" +
                "                                <Class>IR-PAYE-EOY</Class>\n" +
                "                                <Qualifier>poll</Qualifier>\n" +
                "                                <Function>submit</Function>\n" +
                "                                <CorrelationID>" + correlationID + "</CorrelationID>\n" +
                "                                <Transformation>XML</Transformation>\n" +
                "                                <GatewayTimestamp></GatewayTimestamp>\n" +
                "                </MessageDetails>\n" +
                "                <SenderDetails/>\n" +
                "      </Header>\n" +
                "      <GovTalkDetails>\n" +
                "                <Keys/>\n" +
                "      </GovTalkDetails>\n" +
                "      <Body/>\n" +
                "</GovTalkMessage>";
        return PostAndGet.doPostAndGetResponse(strXML, isTestService ? PostAndGet.POLL_TEST : PostAndGet.POLL);
    }

    private static GovGatewayResponse deleteRequest(String correlationID) {
        //System.out.println("-------------------------DELETE_REQUEST-----------------------------");
        String xml = "<?xml version=\"1.0\"?>\n" +
                "<GovTalkMessage xmlns=\"http://www.govtalk.gov.uk/CM/envelope\">\n" +
                "   <EnvelopeVersion>2.0</EnvelopeVersion>\n" +
                "   <Header>\n" +
                "       <MessageDetails>\n" +
                "           <Class>IR-PAYE-EOY</Class>\n" +
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

    private static GovGatewayResponse dataRequest() {
        System.out.println("-------------------------DATA_REQUEST-----------------------------");
        String xml = """
                <?xml version="1.0"?>
                <GovTalkMessage xmlns="http://www.govtalk.gov.uk/CM/envelope">
                   <EnvelopeVersion>2.0</EnvelopeVersion>
                   <Header>
                       <MessageDetails>
                           <Class>IR-PAYE-EOY</Class>
                           <Qualifier>request</Qualifier>
                           <Function>list</Function>
                           <CorrelationID/>
                           <Transformation>XML</Transformation>
                           <GatewayTimestamp/>
                       </MessageDetails>
                                <SenderDetails>
                                                <IDAuthentication>
                                                                <SenderID>APITEST12345</SenderID>
                                                                <Authentication>
                                                                                <Method>clear</Method>
                                                                                <Value>TeSt12345</Value>
                                                                </Authentication>
                                                </IDAuthentication>
                                                <EmailAddress>test1@api.co.uk</EmailAddress>
                                </SenderDetails>
                   </Header>
                <GovTalkDetails>
                   <Keys/>
                </GovTalkDetails>
                <Body>   <IncludeIdentifiers>1</IncludeIdentifiers>
                   <StartDate>13/11/2009</StartDate>
                   <StartTime>16:00:00</StartTime>
                   <EndDate>13/02/2011</EndDate>
                   <EndTime>16:30:00</EndTime></Body>
                </GovTalkMessage>""";
        return PostAndGet.doPostAndGetResponse(xml, null);
    }
}
