/*
package com.edatasite.workforce.components;

import com.edatasite.workforce.utils.EdsContextParams;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.SmtpAppender;
import org.apache.logging.log4j.core.net.SmtpManager;

import jakarta.mail.Authenticator;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import java.io.Serializable;
import java.util.Properties;


*/
/**
 * User: Aziz
 * Date: 18.09.2009 17:22:33
 *//*

public class EdsSMTPAppender extends SmtpAppender {

    private int timeout;

    private EdsSMTPAppender(String name, Filter filter, Layout<? extends Serializable> layout, SmtpManager manager, boolean ignoreExceptions) {
        super(name, filter, layout, manager, ignoreExceptions);
    }

    public void append(LogEvent event) {
        try {
            */
/**
             * Stack overflow occurs, when the content of messages that contain "ERROR: column" or "ERROR: schema"
 *//*

            if (!(event.getMessage().toString().contains("ERROR: column") || event.getMessage().toString().contains("ERROR: schema"))) {
                if (EdsContextParams.getContextHost() != null && !(EdsContextParams.getContextHost().contains("localhost") || EdsContextParams.getContextHost().contains("127.0.0.1"))) {
                    msg.setSubject(EdsContextParams.getContextHost() + "-> SessionID:" + ServerSecurityContext.getInstance().getSessionId() + "; CompanyId: " + ServerSecurityContext.getInstance().getCompanyId() + "; " + event.getLevel().toString() + ":" + event.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.append(event);
    }

    @Override
    protected Session createSession() {
        Properties props;
        try {
            props = new Properties(System.getProperties());
        } catch (SecurityException ex) {
            props = new Properties();
        }
        if (timeout > 0) {
            String timeoutStr = Integer.toString(timeout);
            props.setProperty("mail.smtp.connectiontimeout", timeoutStr);
            props.setProperty("mail.smtp.timeout", timeoutStr);
        }
        if (getSMTPHost() != null) {
            props.put("mail.smtp.host", getSMTPHost());
        }
        Authenticator auth = null;
        if (getSMTPPassword() != null && getSMTPUsername() != null) {
            props.put("mail.smtp.auth", "true");
            auth = new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(getSMTPUsername(), getSMTPPassword());
                }
            };
        }
        Session session = Session.getInstance(props, auth);
        if (getSMTPDebug()) {
            session.setDebug(getSMTPDebug());
        }
        return session;
    }

    public int getTimeout() {
        return timeout;
    }
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
*/
