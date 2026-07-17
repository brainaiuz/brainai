/*
package com.edatasite.shared.log;

import org.apache.logging.log4j.LoggingException;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.AbstractManager;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.net.MimeMessageBuilder;
import org.apache.logging.log4j.core.util.CyclicBuffer;
import org.apache.logging.log4j.core.util.NameUtil;
import org.apache.logging.log4j.core.util.NetUtils;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.util.Strings;

import jakarta.activation.DataSource;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetHeaders;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.internet.MimeUtility;
import jakarta.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Properties;

*/
/**
 * Manager for sending SMTP events.
 *//*

public class KpiSmtpManager extends AbstractManager {
    private static final SMTPManagerFactory FACTORY = new SMTPManagerFactory();

    private final Session session;

    private final CyclicBuffer<LogEvent> buffer;

    protected volatile MimeMessage message;

    private final FactoryData data;

    protected KpiSmtpManager(final String name, final Session session, final MimeMessage message,
                             final FactoryData data) {
        super(LoggerContext.getContext(), name);
        this.session = session;
        this.message = message;
        this.data = data;
        this.buffer = new CyclicBuffer<>(LogEvent.class, data.numElements);
    }

    public void add(final LogEvent event) {
        buffer.add(event);
    }

    public static KpiSmtpManager getSMTPManager(final String to, final String cc, final String bcc,
                                                final String from, final String replyTo,
                                                final String subject, String protocol, final String host,
                                                final int port, final String username, final String password,
                                                final boolean isDebug, final String filterName, final int numElements) {
        if (Strings.isEmpty(protocol)) {
            protocol = "smtp";
        }

        final StringBuilder sb = new StringBuilder();
        if (to != null) {
            sb.append(to);
        }
        sb.append(':');
        if (cc != null) {
            sb.append(cc);
        }
        sb.append(':');
        if (bcc != null) {
            sb.append(bcc);
        }
        sb.append(':');
        if (from != null) {
            sb.append(from);
        }
        sb.append(':');
        if (replyTo != null) {
            sb.append(replyTo);
        }
        sb.append(':');
        if (subject != null) {
            sb.append(subject);
        }
        sb.append(':');
        sb.append(protocol).append(':').append(host).append(':').append("port").append(':');
        if (username != null) {
            sb.append(username);
        }
        sb.append(':');
        if (password != null) {
            sb.append(password);
        }
        sb.append(isDebug ? ":debug:" : "::");
        sb.append(filterName);

        final String name = "SMTP:" + NameUtil.md5(sb.toString());

        return getManager(name, FACTORY, new FactoryData(to, cc, bcc, from, replyTo, subject,
                protocol, host, port, username, password, isDebug, numElements));
    }

    */
/**
     * Send the contents of the cyclic buffer as an e-mail message.
     *
     * @param layout      The layout for formatting the events.
     * @param appendEvent The event that triggered the send.
     *//*

    public void sendEvents(final Layout<?> layout, final LogEvent appendEvent) {
        if (message == null) {
            connect();
        }
        try {
            final LogEvent[] priorEvents = buffer.removeAll();
            // LOG4J-310: log appendEvent even if priorEvents is empty

            final byte[] rawBytes = formatContentToBytes(priorEvents, appendEvent, layout);

            final String contentType = layout.getContentType();
            final String encoding = getEncoding(rawBytes, contentType);
            final byte[] encodedBytes = encodeContentToBytes(rawBytes, encoding);

            final InternetHeaders headers = getHeaders(contentType, encoding);
            final MimeMultipart mp = getMimeMultipart(encodedBytes, headers);

            sendMultipartMessage(message, mp);
        } catch (final MessagingException | IOException | RuntimeException e) {
            logError("caught exception while sending e-mail notification.", e);
            throw new LoggingException("Error occurred while sending email", e);
        }
    }

    protected byte[] formatContentToBytes(final LogEvent[] priorEvents, final LogEvent appendEvent,
                                          final Layout<?> layout) throws IOException {
        final ByteArrayOutputStream raw = new ByteArrayOutputStream();
        writeContent(priorEvents, appendEvent, layout, raw);
        return raw.toByteArray();
    }

    private void writeContent(final LogEvent[] priorEvents, final LogEvent appendEvent, final Layout<?> layout,
                              final ByteArrayOutputStream out)
            throws IOException {
        writeHeader(layout, out);
        writeBuffer(priorEvents, appendEvent, layout, out);
        writeFooter(layout, out);
    }

    protected void writeHeader(final Layout<?> layout, final OutputStream out) throws IOException {
        final byte[] header = layout.getHeader();
        if (header != null) {
            out.write(header);
        }
    }

    protected void writeBuffer(final LogEvent[] priorEvents, final LogEvent appendEvent, final Layout<?> layout,
                               final OutputStream out) throws IOException {
        for (final LogEvent priorEvent : priorEvents) {
            final byte[] bytes = layout.toByteArray(priorEvent);
            out.write(bytes);
        }

        final byte[] bytes = layout.toByteArray(appendEvent);
        out.write(bytes);
    }

    protected void writeFooter(final Layout<?> layout, final OutputStream out) throws IOException {
        final byte[] footer = layout.getFooter();
        if (footer != null) {
            out.write(footer);
        }
    }

    protected String getEncoding(final byte[] rawBytes, final String contentType) {
        final DataSource dataSource = new ByteArrayDataSource(rawBytes, contentType);
        return MimeUtility.getEncoding(dataSource);
    }

    protected byte[] encodeContentToBytes(final byte[] rawBytes, final String encoding)
            throws MessagingException, IOException {
        final ByteArrayOutputStream encoded = new ByteArrayOutputStream();
        encodeContent(rawBytes, encoding, encoded);
        return encoded.toByteArray();
    }

    protected void encodeContent(final byte[] bytes, final String encoding, final ByteArrayOutputStream out)
            throws MessagingException, IOException {
        try (final OutputStream encoder = MimeUtility.encode(out, encoding)) {
            encoder.write(bytes);
        }
    }

    protected InternetHeaders getHeaders(final String contentType, final String encoding) {
        final InternetHeaders headers = new InternetHeaders();
        headers.setHeader("Content-Type", contentType + "; charset=UTF-8");
        headers.setHeader("Content-Transfer-Encoding", encoding);
        return headers;
    }

    protected MimeMultipart getMimeMultipart(final byte[] encodedBytes, final InternetHeaders headers)
            throws MessagingException {
        final MimeMultipart mp = new MimeMultipart();
        final MimeBodyPart part = new MimeBodyPart(headers, encodedBytes);
        mp.addBodyPart(part);
        return mp;
    }

    protected void sendMultipartMessage(final MimeMessage msg, final MimeMultipart mp) throws MessagingException {
        synchronized (msg) {
            msg.setContent(mp);
            msg.setSentDate(new Date());
            Transport.send(msg);
        }
    }

    */
/**
     * Factory data.
     *//*

    private static class FactoryData {
        private final String to;
        private final String cc;
        private final String bcc;
        private final String from;
        private final String replyto;
        private final String subject;
        private final String protocol;
        private final String host;
        private final int port;
        private final String username;
        private final String password;
        private final boolean isDebug;
        private final int numElements;

        public FactoryData(final String to, final String cc, final String bcc, final String from, final String replyTo,
                           final String subject, final String protocol, final String host, final int port,
                           final String username, final String password, final boolean isDebug, final int numElements) {
            this.to = to;
            this.cc = cc;
            this.bcc = bcc;
            this.from = from;
            this.replyto = replyTo;
            this.subject = subject;
            this.protocol = protocol;
            this.host = host;
            this.port = port;
            this.username = username;
            this.password = password;
            this.isDebug = isDebug;
            this.numElements = numElements;
        }
    }

    private synchronized void connect() {
        if (message != null) {
            return;
        }
        try {
            message = new MimeMessageBuilder(session).setFrom(data.from).setReplyTo(data.replyto)
                    .setRecipients(Message.RecipientType.TO, data.to).setRecipients(Message.RecipientType.CC, data.cc)
                    .setRecipients(Message.RecipientType.BCC, data.bcc).setSubject(data.subject).getMimeMessage();
        } catch (final MessagingException e) {
            logError("could not set SmtpAppender message options", e);
            message = null;
        }
    }

    */
/**
     * Factory to create the SMTP Manager.
     *//*

    private static class SMTPManagerFactory implements ManagerFactory<KpiSmtpManager, FactoryData> {

        @Override
        public KpiSmtpManager createManager(final String name, final FactoryData data) {
            final String prefix = "mail." + data.protocol;

            final Properties properties = PropertiesUtil.getSystemProperties();
            properties.put("mail.transport.protocol", data.protocol);
            if (properties.getProperty("mail.host") == null) {
                // Prevent an UnknownHostException in Java 7
                properties.put("mail.host", NetUtils.getLocalHostname());
            }

            if (null != data.host) {
                properties.put(prefix + ".host", data.host);
            }
            if (data.port > 0) {
                properties.put(prefix + ".port", String.valueOf(data.port));
            }

            final Authenticator authenticator = buildAuthenticator(data.username, data.password);
            if (null != authenticator) {
                properties.put(prefix + ".auth", "true");
            }

            final Session session = Session.getInstance(properties, authenticator);
            session.setProtocolForAddress("rfc822", data.protocol);
            session.setDebug(data.isDebug);
            MimeMessage message;

            try {
                message = new MimeMessageBuilder(session).setFrom(data.from).setReplyTo(data.replyto)
                        .setRecipients(Message.RecipientType.TO, data.to).setRecipients(Message.RecipientType.CC, data.cc)
                        .setRecipients(Message.RecipientType.BCC, data.bcc).setSubject(data.subject).getMimeMessage();
            } catch (final MessagingException e) {
                LOGGER.error("Could not set SmtpAppender message options.", e);
                message = null;
            }

            return new KpiSmtpManager(name, session, message, data);
        }

        private Authenticator buildAuthenticator(final String username, final String password) {
            if (null != password && null != username) {
                return new Authenticator() {
                    private final PasswordAuthentication passwordAuthentication =
                            new PasswordAuthentication(username, password);

                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return passwordAuthentication;
                    }
                };
            }
            return null;
        }
    }
}
*/
