package com.edatasite.workforce.gwt.core.client.ui.components.fileUpload;

import java.util.HashMap;

/**
 * Created by Anvar Akramov on 12/14/17.
 */
public class KpiFileUtils {
    public static HashMap<String, String> FILE_ICONS = new HashMap<>();

    static {
        FILE_ICONS.put("xls", "ficon--file-excel");
        FILE_ICONS.put("xlsx", "ficon--file-excel");
        FILE_ICONS.put("pdf", "ficon--file-pdf");
        FILE_ICONS.put("doc", "ficon--file-word");
        FILE_ICONS.put("docx", "ficon--file-word");
    }

    public static String getFileIconByExtension(String fileExtension) {
        return FILE_ICONS.get(fileExtension);
    }
}
