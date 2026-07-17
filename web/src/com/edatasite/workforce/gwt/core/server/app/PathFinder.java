package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.core.domain.EdsLayout;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.server.db.LayoutManager;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/***
 * Omonullo, [06.06.18 11:49]
 * WEB-INF/classes/layout - html templatelar joylashgan papka
 *
 * layout.properties - layout papkada joylashgan html qaysi CustomForm yoki Viewga tegishliligi yozilgan fayl
 *
 * ======
 *
 * CustomForm bo'lsa va tipi addForm, editForm, viewForm lardan biri bo'lsa,
 * unda layout.properties file'ni ichiga
 * formId.addForm=FORM_1.html
 *  yoki
 *  formId.editForm=FORM_2.html deb yoziladi va FORM_1.html  file'ni WEB-INF/classes/layout degan papkani ichiga olib o'tib qo'yamiz.
 *
 *
 * Agar ViewForm bo'lsa xuddi CustomForm uchun qilingan ishni amalga oshiramiz, lekin qo'shimchasiga
 * layoutManager.getLayoutHTML(String formId) methodni PathFinder.getLayoutHTML(String formId) ga o'zgartirib qo'yamiz
 */

public class PathFinder {
    private static LayoutManager layoutManager = StaticContextAccessor.getBean(LayoutManager.class);

    private static Logger logger = LoggerFactory.getLogger(PathFinder.class);


    public static String getLayoutPath(String formID, String type) {
        Properties props = new Properties();
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("./layout.properties");
        if (stream != null) {
            try {
                props.load(stream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String pName = formID;
        if (!StringUtils.isEmpty(type)) {
            pName += "." + type;
        }
        String path = props.getProperty(pName);
        return path;
    }

    public static String getFileContent(String path) {
        if (!StringUtil.isEmpty(path)) {
            InputStream is = PathFinder.class.getClassLoader().getResourceAsStream(path);
            String content = null;
            if (is != null) {
                try {
                    content = IOUtils.toString(is, "UTF-8");
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            logger.info("Returning Layout Content for path: " + path + " Content is " + (content == null ? "null" : "NOT null"));
            return content;
        }
        logger.info("Path was empty");
        return null;
    }

    public static String getLayoutContent(String formID, String type) {
        return getFileContent(File.separator + "layout" + File.separator + getLayoutPath(formID, type));
    }

    public static String getLayoutHTML(String formID) {
        String layout = layoutManager.getCustomLayout(formID);
        if (layout == null) {
            layout = getLayoutContent(formID, null);
            if (!StringUtils.isEmpty(layout)) {
                logger.info("Layout from HTML file is NOT null. FormID: " + formID);
                return layout;
            }
        }
        if (layout == null) {
            logger.info("Retrieving default layout. FormID: " + formID);
            layout = layoutManager.getDefaultLayout(formID);
        }
        return layout;
    }

    public static LayoutRPC getLayout(String formID, String type) {
        EdsLayout layout = layoutManager.getCustomLayout(formID, type);
        if (layout != null) {
            return layout.getRPC();
        }
        String template = getLayoutContent(formID, type);
        if (!StringUtils.isEmpty(template)) {
            logger.info("Layout from HTML file is NOT null. FormID: " + formID + " | Type: " + type);
            LayoutRPC result = new LayoutRPC();
            result.setLayout(template);
            return result;
        }
        return null;
    }

}