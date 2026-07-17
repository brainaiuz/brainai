package com.workforcetrack.mobile.services;

import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import org.apache.commons.beanutils.PropertyUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 5/20/11
 * Time: 3:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class WebServiceHelper {

    public static String convertToXML(Object object) throws JAXBException {

        if (object == null) {
            return null;
        }

        Class clazz = object.getClass();
        StringWriter resultXML = new StringWriter();

        JAXBContext jc = JAXBContext.newInstance(clazz);
        Marshaller marshaller = jc.createMarshaller();

        //JAXBElement<object.class> jaxbElement = new JAXBElement<clazz>(new QName(clazz.getSimpleName()), clazz, object);


        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(object, resultXML);

        return resultXML.toString();
    }

    public static Object convertToObject(String xmlStr, Class clazz) throws JAXBException {

        if (xmlStr == null || "".equals(xmlStr) || clazz == null)
            return null;

        Object resObj = null;

        JAXBContext jc = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = jc.createUnmarshaller();


        resObj = unmarshaller.unmarshal(new ByteArrayInputStream(xmlStr.getBytes()));
        return resObj;

    }

    public static Object getObjectByList(Object object, List<String> fieldList) throws InvocationTargetException, IllegalAccessException, InstantiationException {

        if (object == null || fieldList == null || fieldList.size() == 0) {
            return object;
        }
        Object clazz = object.getClass();
        Object newObject = null;
        try {
            newObject = object.getClass().getConstructor().newInstance();
            for (String fieldName : fieldList) {
                Object fieldValue = PropertyUtils.getProperty(object, fieldName);
                if (fieldValue != null) {
                    PropertyUtils.setProperty(newObject, fieldName, fieldValue);
                }
            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return newObject;
    }


     private ArrayList<String> getContactColumnCodeForFacetFilterRpc() {
         ArrayList<String> resultList = new ArrayList<>(Arrays.asList(FacetContentType.ContactFacetFilter.getContentCode()));
        return  resultList;
        //return (ArrayList<String>)Arrays.asList(FacetContentType.ContactFacetFilter.getContentCode());
    }


}
