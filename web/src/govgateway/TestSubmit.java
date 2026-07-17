package govgateway;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 02.03.2010
 * Time: 13:04:11
 * To change this template use File | Settings | File Templates.
 */
public class TestSubmit {
    public static void main(String[] args) {
        try {
            GovGatewayResponse response = submitRequest();
            if (response.getCode() != 200) {
                System.exit(1);
            }
            System.out.println("Status: " + response.getQualifier());
            String correlationID = getValue("<CorrelationID>", "</CorrelationID>", response.getContent());
            System.out.println(correlationID);

            //SUBMISSION_POLL
            do {
                System.out.println("Quolifier:" + response.getQualifier());
                waiting(Integer.parseInt(getAttValue("PollInterval=\"", "\"", response.getContent())));
                response = submitPOLL(correlationID);
            }
            while (response.getQualifier() != null && response.getQualifier().equals("acknowledgement"));

            if (!response.getQualifier().equals("response")) {
                System.exit(1);
            }

            response = deleteRequest(response.getCorrelationID());

            if (!response.getQualifier().equals("response")) {
                System.exit(1);
            }

            response = dataRequest();

            if (!response.getQualifier().equals("response")) {
                System.exit(1);
            } else {
                System.out.println(getValue("<Body>", "</Body>", response.getContent()));
            }


            /*DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setNamespaceAware(true); // never forget this!
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(response.getContent().getBytes("UTF-8")));

            XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = factory.newXPath();
            XPathExpression expr
                    = xpath.compile("//Qualifier");//xpath.compile("//book[author='Neal Stephenson']/title/text()");

            Object result = expr.evaluate(doc, XPathConstants.NODESET);
            NodeList nodes = (NodeList) result;
            for (int i = 0; i < nodes.getLength(); i++) {
                System.out.println(nodes.item(i).getNodeValue());
            }*/

            //System.out.println(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private static GovGatewayResponse submitRequest() {
        System.out.println("-------------------------SUBMISSION_REQUEST-----------------------------");
        String strXML = "<?xml version=\"1.0\"?>\n" +
                "<GovTalkMessage xmlns=\"http://www.govtalk.gov.uk/CM/envelope\">\n" +
                "      <EnvelopeVersion>2.0</EnvelopeVersion>\n" +
                "      <Header>\n" +
                "                <MessageDetails>\n" +
                "                                <Class>IR-PAYE-EOY</Class>\n" +
                "                                <Qualifier>request</Qualifier>\n" +
                "                                <Function>submit</Function>\n" +
                "                                <TransactionID>20021202ABC</TransactionID>\n" +
                "                                <CorrelationID></CorrelationID>\n" +
                "                                <Transformation>XML</Transformation>\n" +
                "                                <GatewayTest>1</GatewayTest>\n" +
                "                                <GatewayTimestamp></GatewayTimestamp>\n" +
                "                </MessageDetails>\n" +
                "                <SenderDetails>\n" +
                "                                <IDAuthentication>\n" +
                "                                                <SenderID>APITEST12345</SenderID>\n" +
                "                                                <Authentication>\n" +
                "                                                                <Method>clear</Method>\n" +
                "                                                                <Value>TeSt12345</Value>\n" +
                "                                                </Authentication>\n" +
                "                                </IDAuthentication>\n" +
                "                                <EmailAddress>test1@api.co.uk</EmailAddress>\n" +
                "                </SenderDetails>\n" +
                "      </Header>\n" +
                "      <GovTalkDetails>\n" +
                "                <Keys>\n" +
                "                    <Key Type=\"TaxOfficeNumber\">105</Key>\n" +
                "                    <Key Type=\"TaxOfficeReference\">Z888</Key>\n" +
                "                </Keys>\n" +
                "      <ChannelRouting><Channel><URI>1558</URI></Channel></ChannelRouting>\n" +
                "      </GovTalkDetails>\n" +
                "      <Body>\n" +
                "           <IRenvelope xmlns=\"http://www.govtalk.gov.uk/taxation/EOY/09-10/1\">" +
                "               <IRheader>" +
                "               <TestMessage>0</TestMessage>\n" +
                "               <Keys>\n" +
                "                   <Key Type=\"TaxOfficeNumber\">105</Key>  \n" +
                "                   <Key Type=\"TaxOfficeReference\">Z888</Key>\n" +
                "               </Keys> \n" +
                "              <PeriodEnd>2010-04-05</PeriodEnd>\n" +
                "              <DefaultCurrency>GBP</DefaultCurrency>\n" +
                "              <Sender>Employer</Sender>\n" +
                "           </IRheader>  " +
                "           <EndOfYearReturn> " +
                "               <ReturnType>original</ReturnType> \n" +
                "               <SubmissionType>P14Part</SubmissionType>\n" +
                "               <P14>\n" +
                "                   <Name>" +
                "                       <Ttl>Miss</Ttl>" +
                "                       <Fore>Louise</Fore>" +
                "                       <Fore>Helen</Fore>" +
                "                       <Sur>O'Leary</Sur>" +
                "                   </Name>" +
                "                   <NINO>SE987654D</NINO>" +
                "                   <NICs Tab=\"X\">" +
                "                       <Earn>" +
                "                           <AtLEL>0.00</AtLEL>" +
                "                           <LELtoET>0.00</LELtoET>" +
                "                           <ETtoUAP>0.00</ETtoUAP>" +
                "                           <UAPtoUEL>0.00</UAPtoUEL>" +
                "                       </Earn>" +
                "                       <Both>0.00</Both>" +
                "                       <Emp>0.00</Emp>" +
                "                   </NICs>" +
                "                   <SSP>0.00</SSP>" +
                "                   <SMP>0.00</SMP>" +
                "                   <SPP>0.00</SPP>" +
                "                   <SAP>0.00</SAP>" +
                "                   <PayAndTax>" +
                "                       <PrevEmp>" +
                "                           <TaxablePay>999.99</TaxablePay>" +
                "                           <Tax>10.00</Tax>" +
                "                       </PrevEmp>" +
                "                       <ThisEmp>" +
                "                       <TaxablePay>100.00</TaxablePay>" +
                "                           <Tax>10.00</Tax>" +
                "                           <Start>2009-08-11</Start>" +
                "                           <EndDate>2010-02-28</EndDate>" +
                "                           <StLoan>0.00</StLoan>" +
                "                           <Code>456L</Code>" +
                "                           <WidOrph>0.00</WidOrph>" +
                "                       </ThisEmp>" +
                "                   </PayAndTax>" +
                "               </P14>" +
                "               <PartTotals>" +
                "                   <P14Count>1</P14Count>\n" +
                "                   <TotalContributions>0.00</TotalContributions>\n" +
                "                   <TotalTax>10.00</TotalTax>\n" +
                "                   <TotalSSP>0.00</TotalSSP>\n" +
                "                   <TotalSMP>0.00</TotalSMP>\n" +
                "                   <TotalSPP>0.00</TotalSPP>\n" +
                "                   <TotalSAP>0.00</TotalSAP>\n" +
                "                   <TotalStudentLoanDeductions>0.00</TotalStudentLoanDeductions>\n" +
                "               </PartTotals>\n" +
                "               <UniqueID>example1</UniqueID>\n" +
                "           </EndOfYearReturn>\n" +
                "       </IRenvelope>\n" +
                "       <!-- A valid Body payload with a namespace declaration on the first element -->\n" +
                "   </Body>\n" +
                "</GovTalkMessage>";
        return PostAndGet.doPostAndGetResponse(strXML, PostAndGet.REQUEST);
    }

    private static GovGatewayResponse submitPOLL(String correlationID) {
        System.out.println("-------------------------SUBMISSION_POLL-----------------------------");
        String strXML = "<?xml version=\"1.0\"?>\n" +
                "<GovTalkMessage xmlns=\"http://www.govtalk.gov.uk/CM/envelope\">\n" +
                "      <EnvelopeVersion>2.0</EnvelopeVersion>\n" +
                "      <Header>\n" +
                "                <MessageDetails>\n" +
                "                                <Class>IR-PAYE-EOY</Class>\n" +
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
                //"                <ChannelRouting>" +
                //"                   <Channel>" +
                //"                       <URI>1558</URI>" +
                //"                   </Channel>" +
                //"                </ChannelRouting>\n" +
                "      </GovTalkDetails>\n" +
                "      <Body/>\n" +
                "</GovTalkMessage>";
        //System.out.println("POOL REQUIES: "+strXML);
        return PostAndGet.doPostAndGetResponse(strXML, PostAndGet.POLL);
    }

    private static GovGatewayResponse deleteRequest(String correlationID) {
        System.out.println("-------------------------DELETE_REQUEST-----------------------------");
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
        String xml = "<?xml version=\"1.0\"?>\n" +
                "<GovTalkMessage xmlns=\"http://www.govtalk.gov.uk/CM/envelope\">\n" +
                "   <EnvelopeVersion>2.0</EnvelopeVersion>\n" +
                "   <Header>\n" +
                "       <MessageDetails>\n" +
                "           <Class>IR-PAYE-EOY</Class>\n" +
                "           <Qualifier>request</Qualifier>\n" +
                "           <Function>list</Function>\n" +
                "           <CorrelationID/>\n" +
                "           <Transformation>XML</Transformation>\n" +
                "           <GatewayTimestamp/>\n" +
                "       </MessageDetails>\n" +
                "                <SenderDetails>\n" +
                "                                <IDAuthentication>\n" +
                "                                                <SenderID>APITEST12345</SenderID>\n" +
                "                                                <Authentication>\n" +
                "                                                                <Method>clear</Method>\n" +
                "                                                                <Value>TeSt12345</Value>\n" +
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
