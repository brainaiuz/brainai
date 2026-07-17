package com.edatasite.workforce.appContext;

import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * Created by Sherali on 2/23/2016.
 * Project web
 */
public class CustomXmlWebApplicationContext extends XmlWebApplicationContext {
    protected void initBeanDefinitionReader(XmlBeanDefinitionReader beanDefinitionReader) {
        super.initBeanDefinitionReader(beanDefinitionReader);
    }
}
