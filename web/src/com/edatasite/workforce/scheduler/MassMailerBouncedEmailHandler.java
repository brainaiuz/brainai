package com.edatasite.workforce.scheduler;

import com.edatasite.workforce.appContext.SpringPropertiesUtil;
import com.edatasite.workforce.core.tools.BounceFinder;
import com.edatasite.workforce.core.tools.MessageBean;
import com.edatasite.workforce.core.tools.MessageBeanUtil;
import com.edatasite.workforce.core.tools.SmtpScanner;
import jakarta.mail.Flags;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.URLName;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.search.FlagTerm;
import jakarta.mail.search.SearchTerm;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Dec 17, 2010
 * Time: 10:45:02 PM
 * To change this template use File | Settings | File Templates.
 */

public class MassMailerBouncedEmailHandler extends BaseRecurrenceJob {
    private static boolean running = false;

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if (running) return;
        running = true;
        super.execute(jobExecutionContext);
//        getLogger().info("MassMailerBouncedEmailHandler started");
        String host = SpringPropertiesUtil.getProperty("bounced_host");
        String email = SpringPropertiesUtil.getProperty("bounced_email");
        String password = SpringPropertiesUtil.getProperty("bounced_password");
        if (host != null && email != null && password != null) {
            Store store = null;
            Folder folder = null;
            try {
                Properties props = new Properties();
                props.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.setProperty("mail.pop3.socketFactory.fallback", "false");
                props.setProperty("mail.pop3.port", "995");
                props.setProperty("mail.pop3.socketFactory.port", "995");
                props.setProperty("mail.pop3.ssl.trust", "*"); //Trust all Servers
                props.setProperty("mail.pop3.ssl.trust", "localhost");
                URLName urln = new URLName("pop3", host, 995, null, email, password);

                Session session = Session.getInstance(props, null);
                store = session.getStore(urln);
                store.connect();

                folder = store.getFolder("INBOX");
                folder.open(Folder.READ_WRITE);
                SearchTerm searchTerm = new FlagTerm(new Flags(Flags.Flag.SEEN), false);

                Message[] messages = folder.search(searchTerm);
                getLogger().info("Messages Count: " + (messages != null ? messages.length : 0));
                BounceFinder parser = new BounceFinder();
                if (messages != null && messages.length > 0) {
                    for (int i = 0; i < 20; i++) {
                        MimeMessage msg = (MimeMessage) messages[i];
                        try {
                            MessageBean mBean = MessageBeanUtil.mimeToBean(msg);
                            String bType = parser.parse(mBean);
                            if (mBean.getWfmCompanyID() != null && !bType.equals(SmtpScanner.BOUNCETYPE.GENERIC.toString())) {
                                try {
                                    massMailServiceLocal.sendCrmEntityMessageBounce(mBean.getWfmClusterType()
                                            , mBean.getWfmCompanyID()
                                            , Integer.parseInt(mBean.getWfmEntityID())
                                            , Integer.parseInt(mBean.getWfmMessageID()));
                                } catch (Exception e) {
                                    getLogger().info("PROBABLY SCHEMA DOESN'T EXIST " + e.getMessage());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        msg.setFlag(Flags.Flag.SEEN, true);
                        msg.setFlag(Flags.Flag.DELETED, true);
                    }
                }
            } catch (Exception e) {
//                getLogger().info(" BOUNCE CONNECTION ERROR " + e.getMessage());
            } finally {
                try {
                    if (folder != null && folder.isOpen()) {
                        folder.close(true);
                    }
                    if (store != null) {
                        store.close();
                    }
                } catch (MessagingException e) {
                    getLogger().error("while closing folder or store throwed****************:\n" + e.getMessage());
                }
            }
        }
        running = false;
//        getLogger().info("MassMailerBouncedEmailHandler ended");
    }
}
