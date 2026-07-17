package com.edatasite.workforce.gwt.core.server.utils;

import com.edatasite.workforce.core.domain.EdsLocalization;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.core.client.rpc.LocalizationService;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;

/**
 * Created by Virus on 2016/11/28.
 */
public class LocalizationUtils {
    public LinkedHashMap<String, LocalizationInfo> propertiesMap;
    private LocalizationService localizationService;
    private Properties props;
    private String root;
    private HashMap<String, List<EdsLocalization>> resourceDataMap = new HashMap<>();
    private HashMap<String, LinkedHashMap<String, EdsLocalization>> valueMap = new HashMap<>();
    private ArrayList<String> notFoundFileList;

    /*public static void synchranization() {
        LocalizationUtils utils = new LocalizationUtils();
        utils.init();

        for (Map.Entry<String, LocalizationInfo> properties : propertiesMap.entrySet()) {
            utils.updateDataBase(properties.getValue());
        }
    }*/

    public LocalizationUtils() {
        //try retrieve data from file]
        props = new Properties();
        String profile = System.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, Constants.DEFAULT_SPRING_PROFILES);
        if (StringUtil.isEmpty(profile)) {
            System.out.println("--------------------------");
            System.out.println("Please set spring.profiles.active!!!");
            System.out.println("VM options add this -Dspring.profiles.active=local or app");
            System.out.println("--------------------------");
            System.exit(0);
        }
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/" + profile + "/localization.properties");
        if (stream != null) {
            try {
                props.load(stream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        propertiesMap = new LinkedHashMap<>();
        notFoundFileList = new ArrayList<>();
    }

    public void setLocalizationService(LocalizationService localizationService) {
        this.localizationService = localizationService;
    }

    public void updateDataBase(LocalizationInfo info) {
        if (!valueMap.containsKey(info.name)) valueMap.put(info.name, new LinkedHashMap<>());
        HashMap<String, String> map = new HashMap<>();

        for (String name : info.properties.stringPropertyNames()) {
            String value = info.properties.getProperty(name);
            map.put(name, value);
        }
        System.out.printf("Update DataBase: Name = %s;  Language = %S; Size =  %d, path = %s%n", info.name, info.language, map.keySet().size(), info.path);
        localizationService.update(info.name, info.language, info.path, map, valueMap.get(info.name), false);
    }

    private void getProperty(String resourceName, String path, String language) {
        InputStreamReader stream = null;
        String fullPath;
        if (StringUtils.isEmpty(language) || "defaultText".equals(language)) {
            fullPath = String.format("%s/%s.properties", root, path);
        } else {
            fullPath = String.format("%s/%s_%s.properties", root, path, language);
        }
        propertiesMap.put(fullPath, new LocalizationInfo(language, new Properties(), fullPath, resourceName));
        try {
            stream = new InputStreamReader(new FileInputStream(fullPath), "UTF8");
            if (stream != null) {
                propertiesMap.get(fullPath).properties.load(stream);
            } else {
                notFoundFileList.add(fullPath);
            }
        } catch (FileNotFoundException e) {
//            e.printStackTrace();
            notFoundFileList.add(fullPath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }
    }

    public void init() {
        String locale = props.getProperty("locale");
        root = props.getProperty("root");
        for (String resourceName : props.stringPropertyNames()) {

            if (Arrays.asList("locale", "root").contains(resourceName)) continue;
            String resourcePath = props.getProperty(resourceName);
            for (String language : locale.split(",")) {

                getProperty(resourceName, resourcePath, language);
            }
        }

    }

    public void updateResoure(LocalizationInfo info) {

        List<EdsLocalization> list = resourceDataMap.get(info.name);
        if (list == null) {
            list = localizationService.getResourceData(info.name);
        }
        System.out.printf("Update Resource: Name = %s;  Language = %S; Size =  %d%n", info.name, info.language, list.size(), info.path);
        for (EdsLocalization localization : list) {
            String value = null;

            try {
                Field field = EdsLocalization.class.getDeclaredField(info.language);
                field.setAccessible(true);
                if (field.get(localization) != null)
                    value = "" + field.get(localization);
            } catch (NoSuchFieldException e) {
//                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (Exception e) {
            }
            if (!StringUtil.isEmpty(value))
                info.properties.setProperty(localization.getCode(), value);
        }
        OutputStream out = null;
        try {
            File file = new File(info.path);

            out = new FileOutputStream(file);
            OutputStreamWriter outputStream = new OutputStreamWriter(out, "UTF-8");
            info.properties.store(outputStream, null);
            file.setReadable(true);
            file.setExecutable(true);
            file.setWritable(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class LocalizationInfo {
        String language;
        Properties properties;
        String path;
        String name;

        public LocalizationInfo() {
        }

        public LocalizationInfo(String language, Properties properties, String path, String name) {
            this.language = language;
            this.properties = properties;
            this.path = path;
            this.name = name;
        }
    }

    public ArrayList<String> getNotFoundFileList() {
        return notFoundFileList;
    }
}
