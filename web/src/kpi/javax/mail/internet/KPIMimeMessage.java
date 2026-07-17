package kpi.javax.mail.internet;

import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetHeaders;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.SharedByteArrayInputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 * User: hayot
 * Date: 9/17/12
 * Time: 10:04 AM
 * To change this template use File | Settings | File Templates.
 */
public class KPIMimeMessage extends MimeMessage implements Serializable {
    private String messageID;

    public KPIMimeMessage(Session session) {
        super(session);
    }

    public KPIMimeMessage(Session session, InputStream is) throws MessagingException {
        super(session, is);
    }

    protected KPIMimeMessage(MimeMessage source) throws MessagingException {
        super(source);
    }

    protected KPIMimeMessage(Folder folder, int msgnum) {
        super(folder, msgnum);
    }

    protected KPIMimeMessage(Folder folder, InputStream is, int msgnum) throws MessagingException {
        super(folder, is, msgnum);
    }

    protected KPIMimeMessage(Folder folder, InternetHeaders headers, byte[] content, int msgnum) throws MessagingException {
        super(folder, headers, content, msgnum);
    }

    public static KPIMimeMessage convert(Session emailSession, Message message) throws MessagingException, IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        message.writeTo(bos);
        bos.close();
        SharedByteArrayInputStream bis = new SharedByteArrayInputStream(bos.toByteArray());
        KPIMimeMessage mimeMessage = new KPIMimeMessage(emailSession, bis);
        bis.close();
        return mimeMessage;
    }

    public static String[] extractTrackerCodefromSubject(String subject) {
        HashSet<String> strTrackerIDs = new HashSet<>();
        if (subject != null && !subject.isEmpty()) {
            if (subject.matches(".*\\[.*\\d{5}\\].*")) {
                String[] results = subject.split("\\]");
                if (results.length > 0) {
                    for (String result : results) {
                        result = result.substring(result.lastIndexOf("[") + 1);
                        if (result.matches(".*\\d{5}$")) {
                            strTrackerIDs.add(result);
                        }
                    }
                }
            }
        }

        return strTrackerIDs.toArray(new String[]{});
    }

    @Override
    public Object getContent() throws IOException, MessagingException {
        initEncoding();
        return super.getContent();
    }

    private void initEncoding() throws MessagingException {
        String encoding = getEncoding();
        boolean encodingFound = false;
        if (encoding == null) {
            encodingFound = false;
        } else if (encoding.equalsIgnoreCase("base64")) {
            encodingFound = true;
        } else if (encoding.equalsIgnoreCase("quoted-printable")) {
            encodingFound = true;
        } else if (encoding.equalsIgnoreCase("uuencode") ||
                encoding.equalsIgnoreCase("x-uuencode") ||
                encoding.equalsIgnoreCase("x-uue")) {
            encodingFound = true;
        } else if (encoding.equalsIgnoreCase("binary") ||
                encoding.equalsIgnoreCase("7bit") ||
                encoding.equalsIgnoreCase("8bit")) {
            encodingFound = true;
        }
        if (!encodingFound) {
            removeHeader("Content-Transfer-Encoding");
        }
    }

    @Override
    public String getMessageID() throws MessagingException {
        if (messageID != null) {
            return messageID;
        }
        String messageId = Optional.ofNullable(super.getMessageID()).orElse(getMessageXID());
        if (messageId != null) {
            return messageID = messageId.replaceAll("[<>]", "");
        } else {
            return null;
        }
    }

    private String getMessageXID() throws MessagingException {
        return getHeader("Message-XID", null);
    }

    public String[] getTrackerIDs() throws MessagingException {
        String[] headers = getHeader("X-TRACKER-IDS");
        String existingHeader = null;
        if (headers != null && headers.length > 0 && headers[0] != null) {
            existingHeader = headers[0];
        }
        HashSet<String> result = new HashSet<>(Arrays.asList(extractTrackerCodefromSubject(getSubject())));
        if (existingHeader != null) {
            result.addAll(Arrays.asList(extractTrackerCodefromSubject(existingHeader)));
        }
        return result.toArray(new String[]{});
    }

    public void initTracker(String code) throws MessagingException {
        String subject = getSubject();
        String[] trackerIDs = getTrackerIDs();
        StringBuilder trackerIDs_ = new StringBuilder(code != null ? "[" + code + "]" : "");
        if (trackerIDs != null && trackerIDs.length > 0) {
            for (String trackerID : trackerIDs) {
                if (!trackerIDs_.toString().toLowerCase().contains(trackerID.toLowerCase())) {
                    trackerIDs_.append(",[").append(trackerID).append("]");
                }
                subject = subject.replaceAll("\\[" + trackerID + "\\]", "");
            }
        }
        setSubject(subject, "utf-8");
        addHeader("X-TRACKER-IDS", trackerIDs_.toString());
    }

    public void initReferences(String... messageIDs) throws MessagingException {
        List<String> references = new ArrayList<>();
        if (messageIDs != null && messageIDs.length > 0) {
            StringBuilder result = new StringBuilder();
            for (String messageID : messageIDs) {
                if (messageID != null && !references.contains(messageID)) {
                    String res = messageID.trim();
                    result.append(!res.startsWith("<") ? "<" + res + ">\t" : res + "\t");
                    references.add(messageID);
                }
            }
            addHeader("References", result.toString());
        }
    }

    public void setTrackerInvisibleSubject(String subject) throws MessagingException {
        String[] trackerIDs = extractTrackerCodefromSubject(subject);
        StringBuilder trackerIDs_ = new StringBuilder();
        if (trackerIDs != null && trackerIDs.length > 0) {
            for (String trackerID : trackerIDs) {
                if (!trackerIDs_.toString().toLowerCase().contains(trackerID.toLowerCase())) {
                    trackerIDs_.append(",[").append(trackerID).append("]");
                }
                subject = subject.replaceAll("\\[" + trackerID + "\\]", "");
            }
        }
        setSubject(subject, "utf-8");
    }
}
