package com.edatasite.workforce.gwt.core.server.utils;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by IntelliJ IDEA.
 * User: iskan
 * Date: Jan 2, 2008
 * Time: 10:01:55 PM
 * To change this template use File | Settings | File Templates.
 */

public class SpringBeanLocator {

    private static BeanFactory factory;

    static {
        factory = new ClassPathXmlApplicationContext(
                new String[]{"beans.xml"});
    }

    public static Object getBean(String name) {
        return factory.getBean(name);
    }

    public static Object getBean(String name, Class clazz) {
        return factory.getBean(name, clazz);
    }

    public static boolean containsBean(String name) {
        return factory.containsBean(name);
    }

}
