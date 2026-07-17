package com.edatasite.shared.mail;

import java.lang.reflect.Field;

/**
 * User: mansur
 * Date: 04.07.2007
 * Time: 0:49:31
 */

public class EdsMailContent {

    public static String getMailContent(Object transientObject, String mailContent) {

        if (transientObject == null) {
            throw new NullPointerException("'object' - parameter is null.");
        }

        if (mailContent == null) {
            throw new NullPointerException("'mailContent' - parameter is null.");
        }

        Class cls = transientObject.getClass();
        Field[] fields = cls.getDeclaredFields();
        Field[] sFields = cls.getSuperclass().getDeclaredFields();

        try {
            for (Field f : fields) {
                if (!f.isAccessible()) {
                    f.setAccessible(true);
                }
                Object fieldValue = f.get(transientObject);
                if (fieldValue != null) {
                    String par = "#" + f.getName().toUpperCase();
                    if (mailContent.contains(par)) {
                        mailContent = mailContent.replaceAll(par, (String) fieldValue);
                    }
                }
            }
            for (Field f : sFields) {
                if (!f.isAccessible()) {
                    f.setAccessible(true);
                }
                Object fieldValue = f.get(transientObject);
                if (fieldValue != null) {
                    String par = "#" + f.getName().toUpperCase();
                    if (mailContent.contains(par)) {
                        mailContent = mailContent.replaceAll(par, (String) fieldValue);
                    }
                }
            }
            return mailContent;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

}
