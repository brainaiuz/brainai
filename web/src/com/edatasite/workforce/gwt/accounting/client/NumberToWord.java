package com.edatasite.workforce.gwt.accounting.client;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 5/17/12
 * Time: 6:30 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class NumberToWord {

//    public static NumberToWord getConvertClass(String localeCode) {
//        NumberToWord classWithLocale = null;
//        String name = "com.edatasite.workforce.gwt.accounting.client.NumberToWord_" + localeCode;
//        try {
//            Class cl = Class.forName(name);
//            java.lang.reflect.Constructor co = cl.getConstructor(new Class[]{});
//            classWithLocale = (NumberToWord) co.newInstance(new Object[]{});
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//
//        return classWithLocale;
//    }

    public abstract String toWord(Number paramNumber);
}
