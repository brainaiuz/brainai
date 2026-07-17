/*
package com.edatasite.shared.log;

*/
/**
 * Created by Sherali on 3/29/2016.
 * Project web
 *//*


import com.edatasite.workforce.appContext.SpringPropertiesUtil;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Filter.Result;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.filter.ThresholdFilter;
import org.apache.logging.log4j.core.layout.HtmlLayout;
import org.apache.logging.log4j.core.util.Booleans;

import jakarta.mail.MessagingException;
import java.io.Serializable;

@Plugin(
        name = "KpiSMTP",
        category = "Core",
        elementType = "appender",
        printObject = true
)
public final class KpiSmtpAppender extends AbstractAppender {
    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_BUFFER_SIZE = 512;
    private final KpiSmtpManager manager;

    private KpiSmtpAppender(String name, Filter filter, Layout<? extends Serializable> layout, KpiSmtpManager manager, boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
        this.manager = manager;
    }


    @PluginFactory
    public static KpiSmtpAppender createAppender(@PluginAttribute("name") String name, @PluginAttribute("to") String to, @PluginAttribute("cc") String cc, @PluginAttribute("bcc") String bcc, @PluginAttribute("from") String from, @PluginAttribute("replyTo") String replyTo, @PluginAttribute("subject") String subject, @PluginAttribute("smtpProtocol") String smtpProtocol, @PluginAttribute("smtpHost") String smtpHost, @PluginAttribute("smtpPort") String smtpPortStr, @PluginAttribute("smtpUsername") String smtpUsername, @PluginAttribute("smtpPassword") String smtpPassword, @PluginAttribute("smtpDebug") String smtpDebug, @PluginAttribute("bufferSize") String bufferSizeStr, @PluginElement("Layout") Layout<? extends Serializable> layout, @PluginElement("Filter") Filter filter, @PluginAttribute("ignoreExceptions") String ignore) {
        if (name == null) {
            LOGGER.error("No name provided for KpiSmtpAppender");
            return null;
        } else {
            boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
            int smtpPort = AbstractAppender.parseInt(smtpPortStr, 0);
            boolean isSmtpDebug = Boolean.parseBoolean(smtpDebug);
            int bufferSize = bufferSizeStr == null ? 512 : Integer.parseInt(bufferSizeStr);
            if (layout == null) {
                layout = HtmlLayout.createDefaultLayout();
            }

            if (filter == null) {
                filter = ThresholdFilter.createFilter(null, null, null);
            }

            KpiSmtpManager manager = KpiSmtpManager.getSMTPManager(to, cc, bcc, from, replyTo, subject, smtpProtocol, smtpHost, smtpPort, smtpUsername, smtpPassword, isSmtpDebug, filter.toString(), bufferSize);
            return manager == null ? null : new KpiSmtpAppender(name, filter, layout, manager, ignoreExceptions);
        }
    }

    public boolean isFiltered(LogEvent event) {
        boolean filtered = super.isFiltered(event);
        if (filtered) {
            this.manager.add(event);
        }

        return filtered;
    }

    public void append(LogEvent event) {
        if (Boolean.valueOf(SpringPropertiesUtil.getProperty("bg_error_email_enabled"))) {
            try {
                if (!(event.getMessage().toString().contains("ERROR: column") || event.getMessage().toString().contains("ERROR: schema"))) {
                    if (!(EdsContextParams.getContextHost().contains("localhost") || EdsContextParams.getContextHost().contains("127.0.0.1"))) {
                        this.manager.message.setSubject(EdsContextParams.getContextHost() + "-> SessionID:" + ServerSecurityContext.getInstance().getSessionId() + "; CompanyId: " + ServerSecurityContext.getInstance().getCompanyId() + "; " + event.getLevel().toString());
                    }
                }
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            this.manager.sendEvents(this.getLayout(), event);
        }
    }
}
*/
