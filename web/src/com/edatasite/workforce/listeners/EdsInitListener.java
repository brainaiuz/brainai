package com.edatasite.workforce.listeners;
//

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.util.StatusPrinter;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.utils.EdsContextParams;
import com.lowagie.text.FontFactory;
import org.apache.catalina.core.ApplicationContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.AbstractEnvironment;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * User: Anvarbek
 * Date: 26.07.2009
 * Time: 17:13:25
 */

public class EdsInitListener implements ServletContextListener {

    private static final Logger log = LoggerFactory.getLogger(EdsInitListener.class);

    public void contextInitialized(ServletContextEvent servletContextEvent) {

        excuteMessage();

        System.out.println("Server starting...");
        String activeProfile = System.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME);
        if (StringUtil.isEmpty(activeProfile)) {
            activeProfile = Constants.DEFAULT_SPRING_PROFILES;
            System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, activeProfile);
        }
        try {
            InputStream stream0 = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/" + activeProfile + "/hibernate.properties");
            InputStream stream1 = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/" + activeProfile + "/application.properties");
            InputStream stream2 = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/" + activeProfile + "/bgconfig.properties");
            InputStream stream3 = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/" + activeProfile + "/massmailbounce.properties");
            InputStream version = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/version.properties");
            Properties props = new Properties();
            if (stream0 != null) {
                props.load(stream0);
            }
            if (stream1 != null) {
                props.load(stream1);
            }
            if (stream2 != null) {
                props.load(stream2);
            }
            if (stream3 != null) {
                props.load(stream3);
            }
            if (version != null) {
                props.load(version);
            }
            System.getProperties().putAll(props);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Load logback configuration
        try {
            String logbackConfigPath = "config/" + activeProfile + "/logback.xml";
            System.setProperty("logback.configurationFile", logbackConfigPath);

            LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
            context.reset();
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(context);

            InputStream logbackStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(logbackConfigPath);
            if (logbackStream != null) {
                configurator.doConfigure(logbackStream);
                System.out.println("Loaded logback configuration for profile: " + activeProfile);
            } else {
                System.err.println("Logback configuration file not found for profile: " + activeProfile);
            }

            StatusPrinter.printInCaseOfErrorsOrWarnings(context);

        } catch (Exception e) {
            e.printStackTrace();
        }
        // End logback configuration


        ServletContext context = servletContextEvent.getServletContext();
        Class classFacade = context.getClass();
        try {
            FontFactory.registerDirectory(context.getRealPath("/fonts"));// registration font in catologs fonts{arial.ttf,..,times.ttf} after uses pdf
            Field applicationField = classFacade.getDeclaredField("context");
            applicationField.setAccessible(true);
            ApplicationContext applicationContext = (ApplicationContext) applicationField.get(context);
            System.out.println("Server info: " + applicationContext.getServerInfo());
            System.out.println("Application host: http://" + System.getProperty("bg_hostName"));
            System.out.println("This is javax.servlet.context.tempdir: " + applicationContext.getAttribute("javax.servlet.context.tempdir") +"\n");

//            Class classApplication = applicationContext.getClass();
//            Field standardField = classApplication.getDeclaredField("context");
//            standardField.setAccessible(true);
//            StandardContext standardContext = (StandardContext) standardField.get(applicationContext);
            //log.info("!!!!This is taken from Standard Context = " + standardContext.getHostname());
            //set host to context variable
//            EdsContextParams.setContextHost(standardContext.getHostname());
            EdsContextParams.setGeoIPRealPath(applicationContext.getRealPath("/WEB-INF/defs"));


        } catch (Exception e) {
            e.printStackTrace();
        }

        //Initialize hazelcast for application cache upon server start
//        ApplicationCache.getInstance();

        initVelocity(servletContextEvent.getServletContext());
    }

    private void excuteMessage() {
        System.out.println();
        int width = 150;
        int height = 40;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 24));

        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.drawString("kpi.com", 12, 24);

        char[] gradientChars = {' ', '$'};

        for (int y = 0; y < height; y++) {
            StringBuilder sb = new StringBuilder();
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                char gradientChar = getGradientChar(rgb, gradientChars);
                sb.append(gradientChar);
            }

            if (sb.toString().trim().isEmpty()) {
                continue;
            }
            System.out.println(sb);
        }
        System.out.println();
    }

    private static char getGradientChar(int rgb, char[] gradientChars) {
        int index = Math.min((rgb & 0xFF) * gradientChars.length / 255, gradientChars.length - 1);
        return gradientChars[index];
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        //BaseEventsPostProcessorImpl.execSvc.shutdown();
    }

    public void initVelocity(ServletContext context) {
        Properties pr = new Properties();
        try {
            pr.load(context.getResourceAsStream("/WEB-INF/velocity.properties"));
        } catch (IOException ioe) {
            log.error("Failed to load Velocity configuration file - /WEB-INF/velocity.properties:", ioe);
        }
        try {
            pr.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, context.getRealPath(File.separator + "WEB-INF" + File.separator + "mail_templates"));
            Velocity.init(pr);
        } catch (Exception e) {
            log.error("Failed initialize Velocity:", e);
        }
    }

}
