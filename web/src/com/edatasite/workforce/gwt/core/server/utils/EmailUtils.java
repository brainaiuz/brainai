package com.edatasite.workforce.gwt.core.server.utils;

import com.edatasite.workforce.core.domain.emailfetching.EdsEmailAttachment;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.utils.InputStreamDataSource;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.BodyPart;
import jakarta.mail.MessagingException;
import jakarta.mail.Part;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.internet.MimeUtility;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.angus.mail.imap.IMAPBodyPart;
import org.eclipse.angus.mail.util.BASE64DecoderStream;
import org.simplejavamail.converter.EmailConverter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailUtils {

    public static StringBuilder retrieveContent(Object content, String contentType, StringBuilder description, Map<String, String> inlineImages) {
        if (content == null || StringUtils.isEmpty(content.toString())) {
            return new StringBuilder();
        }
        if (inlineImages == null) {
            inlineImages = new HashMap<>();
        }
        if (content instanceof MimeMessage) {
            org.simplejavamail.api.email.Email email = EmailConverter.mimeMessageToEmail((MimeMessage) content);
            StringBuilder result = new StringBuilder();
            if (email == null) {
                return result;
            }
            if (email.getHTMLText() != null) {
                result.append(email.getHTMLText());
                return result;
            }
            result.append(email.getPlainText());
            return result;
        } else if (content instanceof MimeMultipart) {
            MimeMultipart mime = (MimeMultipart) content;
            try {
                if (mime.getCount() > 0) {
                    for (int i = 0; i < mime.getCount(); i++) {
                        Part bodyPart = mime.getBodyPart(i);
                        if (bodyPart.isMimeType("text/plain")) {
                            boolean hasHtml = false;
                            for (int j = i; j < mime.getCount(); j++) {
                                Part bodyPart_ = mime.getBodyPart(j);
                                if (bodyPart_ != null && bodyPart_.isMimeType("text/html")) {
                                    hasHtml = true;
                                    break;
                                }
                            }
                            if (hasHtml || (bodyPart.getContent() != null && bodyPart.getContent() instanceof String)) {
                                description = retrieveContent(bodyPart, bodyPart.getContentType(), description, inlineImages);
                            }
                        } else if (bodyPart.isMimeType("message/rfc822")) {
                            description = retrieveContent(bodyPart.getContent(), bodyPart.getContentType(), description, inlineImages);
                        } else if (contentType != null && contentType.toLowerCase().contains("alternative")) {
                            description = retrieveContent(bodyPart, bodyPart.getContentType(), description, inlineImages);
                        } else if ((contentType != null && contentType.toLowerCase().contains("image")) ||
                                (bodyPart.getContentType() != null && bodyPart.getContentType().toLowerCase().contains("image")) ||
                                (bodyPart.getContentType() != null && bodyPart.getContentType().toLowerCase().contains("application/octet-stream") && bodyPart.getContent() instanceof InputStream)) {
                            description = retrieveContent(bodyPart, bodyPart.getContentType(), description, inlineImages);
                        } else if (bodyPart.getContentType() == null || !bodyPart.getContentType().toLowerCase().contains("image")) {
                            description = retrieveContent(bodyPart, bodyPart.getContentType(), description, inlineImages);
                        }
                    }
                }
            } catch (MessagingException | IOException e) {
                e.printStackTrace();
            }
        } else if (content instanceof MimeBodyPart || content instanceof IMAPBodyPart) {
            try {
                BodyPart bodyPart = content instanceof MimeBodyPart ? (MimeBodyPart) content : (IMAPBodyPart) content;
                if (bodyPart != null && bodyPart.getContent() != null && !(bodyPart.getContent() instanceof String)) {
                    description = retrieveContent(bodyPart.getContent(), bodyPart.getContentType(), description, inlineImages);
                }
                String disposition = bodyPart.getDisposition();
                if (disposition == null || (Part.INLINE.equals(disposition.toLowerCase()) && !bodyPart.getContentType().equalsIgnoreCase("application/pdf")) || !Part.ATTACHMENT.equals(disposition.toLowerCase())) {
                    if (bodyPart.isMimeType("text/plain")) {
                        description = new StringBuilder();
                        description.append(getContentAsString(bodyPart).replace("\n", "<br>").replace("\r", ""));
                    } else if (bodyPart.isMimeType("text/html")) {
                        description = new StringBuilder();
                        description.append(getContentAsString(bodyPart));
                    } else if (bodyPart.getContent() instanceof String) {
                        description = new StringBuilder();
                        description.append(getContentAsString(bodyPart).replace("\n", "<br>").replace("\r", ""));
                    } else if ((disposition != null && Part.INLINE.equals(disposition.toLowerCase())) || bodyPart.getContent() instanceof InputStream) {
                        embededToString(bodyPart, inlineImages);
                    }
                }
            } catch (MessagingException | IOException e) {
                e.printStackTrace();
            }
        } else if (content instanceof String) {
            description = new StringBuilder();
            description.append(content.toString().replace("\n", "<br>").replace("\r", ""));
            if (contentType != null && contentType.contains("text/calendar")) {
                String temp = content.toString();
                //calendar get desc only.
                temp = temp.substring(temp.indexOf("text/", temp.indexOf("X-ALT-DESC")) + 10, temp.indexOf("X-MICROSOFT", temp.indexOf("text/", temp.indexOf("X-ALT-DESC"))));
                if (temp != null) {
                    description = new StringBuilder();
                    description.append(temp.replaceAll("(\\r\\n|\\n|\\t)", "").replaceAll("(\\\\.)", ""));
                }
            }
            try {
                StringBuilder temp = new StringBuilder();
                temp.append(MimeUtility.decodeText(description.toString()));
                description = temp;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if (inlineImages.size() > 0 && description != null && description.toString().contains("cid:")) {
            StringBuilder temp = new StringBuilder();
            temp.append(findAndReplaceImages(description.toString(), inlineImages));
            description = temp;
        }
        return description == null ? new StringBuilder() : description;
    }

    private static String getContentAsString(BodyPart part) throws IOException, MessagingException {
        if (part == null) {
            return null;
        }
        try {
            String encoding = ((MimeBodyPart) part).getEncoding();//text/plain; charset=KOI8-R
            String charSet = ServerUtils.extractCharsetFromContentType(part.getContentType());
            if (encoding != null && (encoding.equalsIgnoreCase("base64") || encoding.equalsIgnoreCase("quoted-printable") || encoding.contains("uue"))) {
                return ServerUtils.getInputStreamAsString(MimeUtility.decode(((MimeBodyPart) part).getRawInputStream(), encoding), charSet);
            }
        } catch (MessagingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return part.getContent().toString();
    }

    private static String findAndReplaceImages(String description, Map<String, String> inlineImages) {
        for (Map.Entry<String, String> entry : inlineImages.entrySet()) {
            if (description.contains(entry.getKey())) {
                String desc1 = description.substring(0, description.indexOf(entry.getKey()));
                if (desc1 != null && !"".equals(desc1) && desc1.toLowerCase().contains("cid:")) {
                    desc1 = desc1.substring(0, desc1.toLowerCase().lastIndexOf("cid:"));
                    String desc2 = description.substring(description.indexOf(entry.getKey()) + entry.getKey().length());
                    description = desc1 + entry.getValue() + desc2;
                }
            }
        }
        return description;
    }

    private static String embededToString(BodyPart bodyPart, final Map<String, String> inlineImages) throws MessagingException, IOException {
        if (bodyPart.getContentType() != null && (bodyPart.getContentType().toLowerCase().contains("image") || bodyPart.getContentType().toLowerCase().contains("application/octet-stream"))) {
            StringBuilder s = new StringBuilder();
            s.append("data:");
            if (bodyPart.getContentType() != null) {
                String contentType = bodyPart.getContentType();
                contentType = contentType.replace("\"", "");
                s.append(contentType);
            }
            s.append(";base64");
            s.append(",");
            if (bodyPart.getContent() instanceof BASE64DecoderStream base64DecoderStream) {
                byte[] byteArray = IOUtils.toByteArray(base64DecoderStream);
                byte[] encodeBase64 = Base64.encodeBase64(byteArray);
                s.append(new String(encodeBase64, StandardCharsets.UTF_8));
            }
            if (bodyPart instanceof MimeBodyPart mimeBodyPart) {
                String contentID = mimeBodyPart.getContentID();
                contentID = contentID == null ? new Date().toString().replace(" ", "") : contentID;
                if (contentID != null) {
                    contentID = contentID.replaceAll("<|>", "");
                    inlineImages.put(contentID, s.toString());
                }
            }
            return s.toString();
        }

        return null;
    }

    public static String replaceEmbeddedImages(final Map<String, MimeBodyPart> embeddedImage, String content) throws IOException, MessagingException {
        if (content != null) {
            Pattern pattern = Pattern.compile("(src\\s*=\\s*\"data:)(((?!;base64,).)*)(;base64,)([A-Za-z0-9+\\/=]*)(\")", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(content);
            while (matcher.find()) {
                if (matcher.groupCount() > 5) {
                    String image = matcher.group(matcher.groupCount() - 1);
                    String contentType = matcher.group(2);
                    if (image != null) {
                        String key = "kpi_" + (new Date()).getTime() + image.hashCode();
                        byte[] bytes = Base64.decodeBase64(image.getBytes());
                        InputStream value = new ByteArrayInputStream(bytes);
                        if (value != null && value.available() > 0) {
                            MimeBodyPart messageBodyPart = new MimeBodyPart();
                            String fileName = "noname.jpg";
                            if (contentType != null && contentType.contains(";")) {
                                fileName = contentType.substring(contentType.indexOf(";") + 1);
                                fileName = fileName != null && !"".equals(fileName.trim()) ? fileName.trim() : "noname.jpg";
                                if (fileName.contains("=")) {
                                    fileName = fileName.substring(fileName.indexOf("=") + 1);
                                }
                                contentType = contentType.substring(0, contentType.indexOf(";"));
                            }
                            DataSource source = new InputStreamDataSource(fileName, contentType, value);
                            messageBodyPart.setDataHandler(new DataHandler(source));
                            messageBodyPart.setContentID(key);
                            messageBodyPart.setDisposition(MimeMessage.INLINE);
                            messageBodyPart.setFileName(fileName);
                            messageBodyPart.addHeader("X-Attachment-Id", key);
                            embeddedImage.put(key, messageBodyPart);
                            content = content.replace(matcher.group(), "src=\"cid:" + key + "\"");
                        }
                    }
                }
            }
        }
        return content;
    }

    public static InputStream getInputStream(Object content, EdsEmailAttachment attachment) {
        return getInputStream(content, attachment, null);
    }

    private static InputStream getInputStream(Object content, EdsEmailAttachment attachment, InputStream inputStream) {
        if (inputStream != null) {
            return inputStream;
        }
        if (content == null) {
            return null;
        }
        if (content instanceof MimeMultipart) {
            MimeMultipart mime = (MimeMultipart) content;
            try {
                if (mime.getCount() > 0) {
                    inputStream = attachment.getContentID() != null ? getInputStream(mime.getBodyPart(attachment.getContentID()), attachment, inputStream) : inputStream;
                    if (inputStream == null) {
                        for (int i = 0; i < mime.getCount(); i++) {
                            Part bodyPart = mime.getBodyPart(i);
                            inputStream = getInputStream(bodyPart, attachment, inputStream);
                            if (inputStream != null) {
                                break;
                            }
                        }
                    }
                    return inputStream;
                }
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        } else if (content instanceof MimeBodyPart || content instanceof IMAPBodyPart) {
            try {
                BodyPart bodyPart = content instanceof MimeBodyPart ? (MimeBodyPart) content : (IMAPBodyPart) content;
                String disposition = bodyPart.getDisposition();
                if ((disposition != null && (Part.ATTACHMENT.equalsIgnoreCase(disposition.toLowerCase()) || (Part.INLINE.equalsIgnoreCase(disposition.toLowerCase()))) || bodyPart.getContentType().contains("application/pdf"))) {
                    String type = "";
                    try {
                        if (bodyPart.getDataHandler() != null) {
                            if (bodyPart.getDataHandler().getTransferDataFlavors() != null && bodyPart.getDataHandler().getTransferDataFlavors().length > 0) {
                                type = bodyPart.getDataHandler().getTransferDataFlavors()[0].getMimeType();
                            }
                        }
                        String fileName = bodyPart.getFileName();
                        InputStream stream = bodyPart.getDataHandler().getInputStream();
                        long fileSize = stream.available();
                        if (fileSize == attachment.getFilesize() && ((fileName != null && fileName.equalsIgnoreCase(attachment.getFileName())) || fileName == null || attachment.getFileName() == null || attachment.getFileName().equals("noname"))) {
                            inputStream = stream;
                            return stream;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (bodyPart != null && bodyPart.getContent() != null && !(bodyPart.getContent() instanceof String)) {
                        return getInputStream(bodyPart.getContent(), attachment, inputStream);
                    }
                }
            } catch (MessagingException | IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
