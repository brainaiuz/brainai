package com.edatasite.shared.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar
 * Date: Jul 6, 2010
 * Time: 9:06:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class EdsPOIUtils {

    public static String getCellStringValue(HSSFCell cell) {
        final Object value = getCellValue(cell);
        if (value != null) {
            return String.valueOf(value);
        }
        return null;
    }

    public static Object getCellValue(HSSFCell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING -> {
                return cell.getStringCellValue();
            }
            case HSSFCell.CELL_TYPE_NUMERIC -> {
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    return cell.getNumericCellValue();
                }
            }
            case HSSFCell.CELL_TYPE_BOOLEAN -> {
                return cell.getBooleanCellValue();
            }
        }
        return null;
    }
}
