package com.edatasite.workforce.appContext;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 07.03.2009
 * Time: 18:35:49
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationContextProvider implements ApplicationContextAware {

    public static ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextProvider.applicationContext = applicationContext;
    }
}
