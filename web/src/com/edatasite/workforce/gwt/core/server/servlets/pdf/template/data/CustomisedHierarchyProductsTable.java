package com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data;

import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Omonullo on 4/27/2017.
 */
public class CustomisedHierarchyProductsTable implements PDFConstants {
    private LinkedHashMap<String, LinkedHashMap<String, List<HashMap<String, String>>>> hierarchy;
    public LinkedHashMap<String, LinkedHashMap<String, List<HashMap<String, String>>>> getHierarchy() {
        if (hierarchy == null) {
            hierarchy = new LinkedHashMap<>();
        }
        return hierarchy;
    }

    public void setHierarchy(LinkedHashMap<String, LinkedHashMap<String, List<HashMap<String, String>>>> hierarchy) {
        this.hierarchy = hierarchy;
    }

    public void buildHierarchy(String description, HashMap<String, String> valueMap) {
        Matcher m = Pattern.compile("\\{\\{(.*?)\\}\\}").matcher(description); // if description contains value between { { } }
        valueMap.replace(ITEM_DESCRIPTION, valueMap.get(ITEM_DESCRIPTION).replaceAll("\\{\\{(.*?)\\}\\}", ""));
//        BigDecimal totalSalary = new BigDecimal(valueMap.get(ITEM_NET_WITHOUT_DISCOUNT).replaceAll(",", ""));
        while(m.find()) {
            String hierar = m.group(1) != null ? m.group(1) : "";
            if (hierar.contains("_")) {
                String[] parentChild = hierar.split("_");
                String parent = parentChild[0];
                String child = parentChild[1];
                if (parent != null && !"".equals(parent) && child != null && !"".equals(child)) {
                    if (getHierarchy().containsKey(parent)) {
                        HashMap<String, List<HashMap<String, String>>> parentNode = getHierarchy().get(parent);
                        if (parentNode.containsKey(child)) {
                            parentNode.get(child).add(valueMap);
                        } else {
                            ArrayList<HashMap<String, String>> entryList = new ArrayList<>();
                            entryList.add(valueMap);
                            parentNode.put(child, entryList);
                        }
                    } else {
                        LinkedHashMap<String, List<HashMap<String, String>>> parentNode = new LinkedHashMap<>();
                        ArrayList<HashMap<String, String>> entryList = new ArrayList<>();
                        entryList.add(valueMap);
                        parentNode.put(child, entryList);
                        getHierarchy().put(parent, parentNode);
                    }
                }
            }
        }
    }

    public BigDecimal getParentTotal(String parent) {
        BigDecimal result = BigDecimal.ZERO;
            LinkedHashMap<String, List<HashMap<String, String>>> parentNode = getHierarchy().get(parent);
            for (String ck : parentNode.keySet()) {
                for (HashMap<String, String> value : parentNode.get(ck)) {
                    BigDecimal totalSalary = new BigDecimal(value.get(ITEM_NET_WITHOUT_DISCOUNT).replace(",", ""));
                    result = result.add(totalSalary);
                }
            }
        return result;
    }

    public BigDecimal getChildTotal(String child, String parent) {
        BigDecimal result = BigDecimal.ZERO;
        LinkedHashMap<String, List<HashMap<String, String>>> parentNode = getHierarchy().get(parent);
            for (HashMap<String, String> value : parentNode.get(child)) {
                BigDecimal totalSalary = new BigDecimal(value.get(ITEM_NET_WITHOUT_DISCOUNT).replace(",", ""));
                result = result.add(totalSalary);
            }
        return result;
    }

}
