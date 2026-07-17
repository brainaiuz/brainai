package com.edatasite.workforce.gwt.core.server.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by Sherali on 2/22/2016.
 * Project web
 */
@Component
public class StaticContextAccessor {

    private static StaticContextAccessor instance;

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void registerInstance() {
        instance = this;
    }

    public static <T> T getBean(Class<T> clazz) {
        return instance.applicationContext.getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return instance.applicationContext.getBean(name, clazz);
    }

}