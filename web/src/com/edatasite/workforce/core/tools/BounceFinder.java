package com.edatasite.workforce.core.tools;

import com.edatasite.workforce.gwt.core.client.ui.Constants;
import jakarta.mail.Address;
import jakarta.mail.Flags;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.search.AndTerm;
import jakarta.mail.search.FlagTerm;
import jakarta.mail.search.ReceivedDateTerm;
import jakarta.mail.search.SearchTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Dec 16, 2010
 * Time: 8:52:36 PM
 * To change this template use File | Settings | File Templates.
 */
public final class BounceFinder {
    static final Logger logger = LoggerFactory.getLogger(BounceFinder.class);
    static final boolean isDebugEnabled = false;//logger.isDebugEnabled();

    private final SmtpScanner rfcScan;

    static final String TEN_DASHES = "----------";
    static final String ORIGMSG_SEPARATOR = "-----Original Message-----";
    static final String REPLY_SEPARATOR = "---------Reply Separator---------";
    static final String LF = System.getProperty("line.separator", "\n");

    public final static String VERP_BOUNCE_ADDR_XHEADER = "X-VERP_Bounce_Addr";

    /**
     * default constructor
     */
    public BounceFinder() throws IOException {
        rfcScan = SmtpScanner.getInstance();
    }

    /**
     * Scans email properties to find out the bounce type. It also checks VERP
     * headers to get original recipient.
     *
     * @param msgBean a MessageBean instance
     */
    public String parse(MessageBean msgBean) {
        if (isDebugEnabled)
            logger.debug("Entering parse() method...");
        String bounceType = null;

        // retrieve attachments into an array, it also gathers rfc822/Delivery Status.
        BodypartUtil.retrieveAttachments(msgBean);

        // scan message for Enhanced Mail System Status Code (rfc1893/rfc3464)
        BodypartBean aNode = null;
        if (msgBean.getReport() != null) {
            /*
            * multipart/report mime type is present, retrieve DSN/MDN report.
            */
            MessageNode mNode = msgBean.getReport();
            // locate message/delivery-status section
            aNode = BodypartUtil.retrieveDlvrStatus(mNode.getBodypartNode(), mNode.getLevel());
            if (aNode != null) {
                // first scan message/delivery-status
                byte[] attchValue = aNode.getValue();
                if (attchValue != null) {
                    if (isDebugEnabled) {
                        logger.debug("parse() - scan message/report status -----<" + LF + new String(attchValue) + ">-----");
                    }
                    if (bounceType == null) {
                        bounceType = rfcScan.scanBody(new String(attchValue));
                    }
                    parseDsn(attchValue, msgBean);
                    msgBean.setDsnDlvrStat(new String(attchValue));
                }
            } else if ((aNode = BodypartUtil.retrieveMDNReceipt(mNode.getBodypartNode(), mNode.getLevel())) != null) {
                // got message/disposition-notification
                byte[] attchValue = aNode.getValue();
                if (attchValue != null) {
                    if (isDebugEnabled) {
                        logger.debug("parse() - display message/report status -----<" + LF + new String(attchValue) + ">-----");
                    }
                    if (bounceType == null) {
                        bounceType = SmtpScanner.BOUNCE_TYPES.MDN_RECEIPT.toString();
                    }
                    // MDN comes with original and final recipients
                    parseDsn(attchValue, msgBean);
                    msgBean.setDsnDlvrStat(new String(attchValue));
                }
            } else {
                // missing message/* section, try text/plain
                List<BodypartBean> nodes = BodypartUtil.retrieveReportText(mNode.getBodypartNode(), mNode.getLevel());
                if (!nodes.isEmpty()) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    for (BodypartBean bodyPart : nodes) {
                        byte[] attchValue = bodyPart.getValue();
                        try {
                            baos.write(attchValue);
                        }
                        catch (IOException e) {
                            //logger.error("IOException caught", e);
                        }
                    }
                    try {
                        baos.close();
                    }
                    catch (IOException e) {
                    }
                    byte[] attchValue = baos.toByteArray();
                    if (attchValue != null) {
                        if (isDebugEnabled) {
                            logger.debug("parse() - scan message/report text -----<" + LF + new String(attchValue) + ">-----");
                        }
                        if (bounceType == null) {
                            bounceType = rfcScan.scanBody(new String(attchValue));
                        }
                        parseDsn(attchValue, msgBean);
                        msgBean.setDsnText(new String(attchValue));
                    }
                }
            }
            // locate possible message/rfc822 section under multipart/report
            aNode = BodypartUtil.retrieveMessageRfc822(mNode.getBodypartNode(), mNode.getLevel());
            if (aNode != null && msgBean.getRfc822() == null) {
                msgBean.setRfc822(new MessageNode(aNode, mNode.getLevel()));
            }
            // locate possible text/rfc822-headers section under multipart/report
            aNode = BodypartUtil.retrieveRfc822Headers(mNode.getBodypartNode(), mNode.getLevel());
            if (aNode != null && msgBean.getRfc822() == null) {
                msgBean.setRfc822(new MessageNode(aNode, mNode.getLevel()));
            }
        }

        if (msgBean.getRfc822() != null) {
            /*
            * message/rfc822 is present, retrieve RFC report.
            */
            MessageNode mNode = msgBean.getRfc822();
            aNode = BodypartUtil.retrieveRfc822Text(mNode.getBodypartNode(), mNode.getLevel());
            if (aNode != null) {
                StringBuilder sb = new StringBuilder();
                // get original message headers
                List<MsgHeader> vheader = aNode.getHeaders();
                for (int i = 0; vheader != null && i < vheader.size(); i++) {
                    MsgHeader header = vheader.get(i);
                    sb.append(header.getName() + ": " + header.getValue() + LF);
                }
                boolean foundAll = false;
                String rfcHeaders = sb.toString();
                if (!StringUtil.isEmpty(rfcHeaders)) {
                    // rfc822 headers
                    if (isDebugEnabled) {
                        logger.debug("parse() - scan rfc822 headers -----<" + LF + rfcHeaders + ">-----");
                    }
                    foundAll = parseRfc(rfcHeaders, msgBean);
                    msgBean.setDsnRfc822(rfcHeaders);
                }
                byte[] attchValue = aNode.getValue();
                if (attchValue != null) {
                    // rfc822 text
                    String rfcText = new String(attchValue);
                    sb.append(rfcText);
                    String mtype = aNode.getMimeType();
                    if (mtype.startsWith("text/") || mtype.startsWith("message/")) {
                        if (!foundAll) {
                            if (isDebugEnabled) {
                                logger.debug("parse() - scan rfc822 text -----<" + LF + rfcText + ">-----");
                            }
                            parseRfc(rfcText, msgBean);
                            msgBean.setDsnRfc822(sb.toString());
                        }
                    }
                    if (msgBean.getDsnText() == null) {
                        msgBean.setDsnText(rfcText);
                    } else {
                        msgBean.setDsnText(msgBean.getDsnText() + LF + LF + "RFC822 Text:" + LF + rfcText);
                    }
                }
                if (bounceType == null) {
                    bounceType = rfcScan.scanBody(sb.toString());
                }
            }
        } // end of RFC Scan

        String body = msgBean.getBody();
        if (msgBean.getRfc822() != null && bounceType == null) {
            // message/rfc822 is present, scan message body for rfc1893 status code
            if (isDebugEnabled)
                logger.debug("parse() - scan body text -----<" + LF + body + ">-----");
            bounceType = rfcScan.scanBody(body);
        }

        // check CC/BCC
        if (bounceType == null) {
            // if the "real_to" address is not found in envelope, but is
            // included in CC or BCC: set bounceType to CC_USER
            for (int i = 0; msgBean.getTo() != null && i < msgBean.getTo().length; i++) {
                Address to = msgBean.getTo()[i];
                if (containsNoAddress(msgBean.getToEnvelope(), to)) {
                    if (containsAddress(msgBean.getCc(), to)
                            || containsAddress(msgBean.getBcc(), to)) {
                        bounceType = SmtpScanner.BOUNCE_TYPES.CC_USER.toString();
                        break;
                    }
                }
            }
        }

        // check VERP bounce address, set bounce type to SOFT_BOUNCE if VERP recipient found
        List<MsgHeader> headers = msgBean.getHeaders();
        for (MsgHeader header : headers) {
            if (VERP_BOUNCE_ADDR_XHEADER.equals(header.getName())) {
                //logger.info("parse() - VERP Recipient found: ==>" + header.getValue() + "<==");
                if (msgBean.getOrigRcpt() != null && !StringUtil.isEmpty(header.getValue())
                        && !msgBean.getOrigRcpt().equalsIgnoreCase(header.getValue())) {
                    logger.warn("parse() - replace original recipient: " + msgBean.getOrigRcpt()
                            + " with VERP recipient: " + header.getValue());
                }
                if (!StringUtil.isEmpty(header.getValue())) {
                    // VERP Bounce - always override
                    msgBean.setOrigRcpt(header.getValue());
                } else {
                    logger.warn("parse() - " + VERP_BOUNCE_ADDR_XHEADER + " Header found, but it has no value.");
                }
                if (bounceType == null) {
                    // a bounced mail shouldn't have Return-Path
                    String rPath = msgBean.getReturnPath() == null ? "" : msgBean.getReturnPath();
                    if (StringUtil.isEmpty(rPath) || "<>".equals(rPath.trim())) {
                        bounceType = SmtpScanner.BOUNCE_TYPES.SOFT_BOUNCE.toString();
                    }
                }
                break;
            }
        }

        // if it's hard or soft bounce and no final recipient was found, scan
        // message body for final recipient using known patterns.
        if (SmtpScanner.BOUNCE_TYPES.HARD_BOUNCE.toString().equals(bounceType)
                || SmtpScanner.BOUNCE_TYPES.SOFT_BOUNCE.toString().equals(bounceType)) {
            if (StringUtil.isEmpty(msgBean.getFinalRcpt())
                    && StringUtil.isEmpty(msgBean.getOrigRcpt())) {
                String finalRcpt = BounceAddressFinder.getInstance().find(body);
                if (!StringUtil.isEmpty(finalRcpt)) {
                    //logger.info("parse() - Final Recipient found from message body: " + finalRcpt);
                    msgBean.setFinalRcpt(finalRcpt);
                }
            }
        }

        if (bounceType == null) { // use default
            bounceType = SmtpScanner.BOUNCETYPE.GENERIC.toString();
        }

        //logger.info("parse() - bounceType: " + bounceType);

        return bounceType;
    }

    private boolean containsAddress(Address[] addrs, Address to) {
        if (to != null && addrs != null && addrs.length > 0) {
            for (Address addr : addrs) {
                if (to.equals(addr)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean containsNoAddress(Address[] addrs, Address to) {
        if (to != null && addrs != null && addrs.length > 0) {
            for (Address addr : addrs) {
                if (to.equals(addr)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Parse the message/delivery-status to retrieve DSN fields. Also used by
     * message/disposition-notification to retrieve final recipient.
     *
     * @param attchValue -
     *                   delivery status text
     * @param msgBean    -
     *                   MessageBean object
     */
    private void parseDsn(byte[] attchValue, MessageBean msgBean) {
        // retrieve Final-Recipient, Action, and Status
        ByteArrayInputStream bais = new ByteArrayInputStream(attchValue);
        BufferedReader br = new BufferedReader(new InputStreamReader(bais));
        String line = null;
        try {
            while ((line = br.readLine()) != null) {
                if (isDebugEnabled)
                    logger.debug("parseDsn() - Line: " + line);
                line = line.trim();
                if (line.toLowerCase().startsWith("final-recipient:")) {
                    // "Final-Recipient" ":" address-type ";" generic-address
                    // address-type = rfc822 / unknown
                    StringTokenizer st = new StringTokenizer(line, " ;");
                    while (st.hasMoreTokens()) {
                        String token = st.nextToken().trim();
                        if (token.indexOf("@") > 0) {
                            msgBean.setFinalRcpt(token);
                            //logger.info("parseDsn() - Final_Recipient found: ==>" + token + "<==");
                            break;
                        }
                    }
                } else if (line.toLowerCase().startsWith("original-recipient:")) {
                    // "Original-Recipient" ":" address-type ";" generic-address
                    StringTokenizer st = new StringTokenizer(line, " ;");
                    while (st.hasMoreTokens()) {
                        String token = st.nextToken().trim();
                        if (token.indexOf("@") > 0) {
                            msgBean.setOrigRcpt(token);
                            //logger.info("parseDsn() - Original_Recipient found: ==>" + token + "<==");
                            break;
                        }
                    }
                } else if (line.toLowerCase().startsWith("action:")) {
                    /**
                     * "Action" ":" action-value =
                     * 1) failed - could not be delivered to the recipient.
                     * 2) delayed - the reporting MTA has so far been unable to deliver
                     *  or relay the message.
                     * 3) delivered - the message was successfully delivered.
                     * 4) relayed - the message has been relayed or gatewayed.
                     * 5) expanded - delivered and forwarded by reporting MTA to multiple
                     *  additional recipient addresses.
                     */
                    String action = line.substring(7).trim();
                    msgBean.setDsnAction(action);
                    if (isDebugEnabled)
                        logger.debug("parseDsn() - Action found: ==>" + action + "<==");
                } else if (line.toLowerCase().startsWith("status:")) {
                    // "Status" ":" status-code (digit "." 1*3digit "." 1*3 digit)
                    String status = line.substring(7).trim();
                    if (status.indexOf(" ") > 0) {
                        status = status.substring(0, status.indexOf(" "));
                    }
                    msgBean.setDsnStatus(status);
                    if (isDebugEnabled)
                        logger.debug("parseDsn() - Status found: ==>" + status + "<==");
                } else if (line.toLowerCase().startsWith("diagnostic-code:")) {
                    // "Diagnostic-Code" ":" diagnostic-code
                    String diagcode = line.substring(16).trim();
                    msgBean.setDiagnosticCode(diagcode);
                    if (isDebugEnabled)
                        logger.debug("parseDsn() - Diagnostic-Code: found: ==>" + diagcode + "<==");
                }
            }
        }
        catch (IOException e) {
            //logger.error("IOException caught during parseDsn()", e);
        }
    }

    /**
     * parse message/rfc822 to retrieve original email properties: final
     * recipient, original subject and original SMTP message-id.
     *
     * @param rfc_text -
     *                 rfc822 text
     * @param msgBean  -
     *                 MessageBean object
     * @return true if all three properties were found
     */
    private boolean parseRfc(String rfc_text, MessageBean msgBean) {
        // retrieve original To address
        ByteArrayInputStream bais = new ByteArrayInputStream(rfc_text.getBytes());
        BufferedReader br = new BufferedReader(new InputStreamReader(bais));
        int lineCount = 0;
        boolean gotToAddr = false, gotSubj = false, gotSmtpId = false;
        boolean gotWFMDatabaseType = false, gotWFMCompanyID = false, gotWFMMessageID = false, gotWFMEntityID = false;
        // allows to quit scan once all three headers are found
        String line = null;
        try {
            while ((line = br.readLine()) != null) {
                if (isDebugEnabled)
                    logger.debug("parseRfc() - Line: " + line);
                line = line.trim();
                if (line.toLowerCase().startsWith("to:")) {
                    // "To" ":" generic-address
                    String token = line.substring(3).trim();
                    if (StringUtil.isEmpty(msgBean.getFinalRcpt())) {
                        msgBean.setFinalRcpt(token);
                    } else if (StringUtil.compareEmailAddrs(msgBean.getFinalRcpt(), token) != 0) {
                        logger.error("parseRfc() - Final_Rcpt from RFC822: " + token + " is different from DSN's: " + msgBean.getFinalRcpt());
                    }
                    //logger.info("parseRfc() - Final_Recipient(RFC822 To) found: ==>" + token + "<==");
                    gotToAddr = true;
                } else if (line.toLowerCase().startsWith("subject:")) {
                    // "Subject" ":" subject text
                    String token = line.substring(8).trim();
                    if (StringUtil.isEmpty(msgBean.getOrigSubject())) {
                        msgBean.setOrigSubject(token);
                    }
                    //logger.info("parseRfc() - Original_Subject(RFC822 To) found: ==>" + token + "<==");
                    gotSubj = true;
                } else if (line.toLowerCase().startsWith("message-id:")) {
                    // "Message-Id" ":" SMTP message id
                    String token = line.substring(11).trim();
                    if (StringUtil.isEmpty(msgBean.getSmtpMessageId())) {
                        msgBean.setRfcMessageId(token);
                    }
                    //logger.info("parseRfc() - Smtp Message-Id(RFC822 To) found: ==>" + token + "<==");
                    gotSmtpId = true;
                } else if (line.startsWith(Constants.MASS_MAIL_HEADER_DATABASE_TYPE)) {
                    String token = line.substring(Constants.MASS_MAIL_HEADER_DATABASE_TYPE.length() + 1).trim();
                    if (StringUtil.isEmpty(msgBean.getWfmClusterType())) {
                        msgBean.setWfmClusterType(token);
                    }
                    gotWFMCompanyID = true;
                } else if(line.startsWith(Constants.MASS_MAIL_HEADER_COMPANYID)) {
                    String token = line.substring(Constants.MASS_MAIL_HEADER_COMPANYID.length()+1).trim();
                    if (StringUtil.isEmpty(msgBean.getWfmCompanyID())) {
                        msgBean.setWfmCompanyID(token);
                    }
                    gotWFMCompanyID = true;
                } else if(line.startsWith(Constants.MASS_MAIL_HEADER_MESSAGEID)) {
                    String token = line.substring(Constants.MASS_MAIL_HEADER_MESSAGEID.length()+1).trim();
                    if (StringUtil.isEmpty(msgBean.getWfmMessageID())) {
                        msgBean.setWfmMessageID(token);
                    }
                    gotWFMMessageID = true;
                } else if(line.startsWith(Constants.MASS_MAIL_HEADER_ENTITYID)) {
                    String token = line.substring(Constants.MASS_MAIL_HEADER_ENTITYID.length()+1).trim();
                    if (StringUtil.isEmpty(msgBean.getWfmEntityID())) {
                        msgBean.setWfmEntityID(token);
                    }
                    gotWFMEntityID = true;
                }
                if (gotToAddr && gotSubj && gotSmtpId && gotWFMDatabaseType && gotWFMCompanyID && gotWFMMessageID && gotWFMEntityID) {
                    return true;
                }
                if (++lineCount > 100 && !line.contains(":")) {
                    break; // check if it's a header after 100 lines
                }
            } // end of while
        }
        catch (IOException e) {

        }
        return false;
    }

    public static void main(String[] args) {
        try {
            Properties properties = new Properties();
            properties.put("mail.pop3.host", "mail.mailforcetrack.com");
            properties.setProperty("mail.pop3s.connectiontimeout", "5000");
            properties.setProperty("mail.pop3s.timeout", "5000");

            Session emailSession = Session.getDefaultInstance(properties);
            Store store = emailSession.getStore("pop3");
            store.connect("mail.mailforcetrack.com", 110, "admin@mailforcetrack.com", "admin4wfm");

            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_WRITE);
            SearchTerm searchTerm = null;
            FlagTerm flagTerm = new FlagTerm(new Flags(Flags.Flag.SEEN), false);

            ReceivedDateTerm receivedDate = null;

            if (receivedDate != null) {
                searchTerm = new AndTerm(receivedDate, flagTerm);
            } else {
                searchTerm = flagTerm;
            }
            Message[] messages = emailFolder.search(searchTerm);
            BounceFinder parser = new BounceFinder();
            if (messages != null) {
                for (Message message1 : messages) {
                    Message message = null;//= messages[i];
                    MimeMessage msg = (MimeMessage) message1;
                    MessageBean mBean = MessageBeanUtil.mimeToBean(msg);
                    String bType = parser.parse(mBean);
                    if (mBean.getWfmCompanyID() != null) {
                        System.out.println("### Bounce Type: " + bType);
                    }
                }
            }
        } catch (Exception e) {

        }
    }
}
