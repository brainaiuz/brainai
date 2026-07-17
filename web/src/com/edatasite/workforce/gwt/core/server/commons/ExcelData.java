package com.edatasite.workforce.gwt.core.server.commons;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.LinkedList;
import java.util.List;

public class ExcelData {
    private Object value;
    private int dataType = 0;
    public static final int STRING = 0;
    public static final int INTEGER = 1;
    public static final int NUMBER_FORMAT_PERCENTAGE = 2;
    public static final int DOUBLE = 3;
    public static final int BIG_DECIMAL = 4;
    public static final int DATE = 5;
    public static final int BOOLEAN = 6;
    public static final int NUMBER_FORMAT_0_00 = 7;
    public static final int TIME_FORMAT_Short_time_HH_mm = 8;
    public static final int DATE_LONG = 9;
    public static final int CURRENCY = 10;

    private int fontSize = 0;
    private int fontColor = 0;
    private short bgcolor = 0;
    private short borderColor = HSSFColor.BLACK.index;
    private int cellSize = 0;
    private double columnSize;
    private boolean autSize = false;
    private boolean wrapped = false;
    private Integer cellIndex;

    private int borderPosiontion = 0;
    private int[] borderPosiontions;
    public static final int ALL_BORDER = 1;
    public static final int TOP_BORDER = 2;
    public static final int BOTTOM_BORDER = 3;
    public static final int LEFT_BORDER = 4;
    public static final int RIGHT_BORDER = 5;
    public static final int NO_BORDER = 6;
    private int dataPosiontionInPage = 0;
    public static final int HEADER = 1;
    public static final int HEADER2 = 3;
    public static final int HEADER3 = 4;
    public static final int HEADER_LEFT = 5;
    public static final int HEADER_RIGHT = 6;
    public static final int HEADER_CENTER = 9;
    public static final int NORMAL_LEFT = 7;
    public static final int NORMAL_RIGHT = 8;
    public static final int NORMAL = 2;

    private boolean merged = false;
    private int fromRow = 0;
    private int fromCell = 0;
    private int toRow = 0;
    private int toCell = 0;
    private int rotation = 0;
    private boolean style = false;
    private boolean bold = false;
    private String currencySymbol = "USD";

    private short horizontalAlignment = CellStyle.ALIGN_LEFT;
    private short verticalAlignment = CellStyle.VERTICAL_CENTER;

    private boolean isGroupCellBorder;

    private String commentString;

    public static final short BLACK = HSSFColor.BLACK.index;
    public String CustomDateFormat;
    private List<ExcelData[]> items = new LinkedList<>();

    public ExcelData() {
    }

    public ExcelData(Object value, int dataType) {
        this.value = value;
        this.dataType = dataType;
    }

    public ExcelData(Object value, int dataType, int cellSize,
                     boolean autSize, boolean wrapped,
                     int borderPosiontion, int dataPosiontionInPage) {
        this.value = value;
        this.dataType = dataType;
        this.cellSize = cellSize;
        this.autSize = autSize;
        this.wrapped = wrapped;
        this.borderPosiontion = borderPosiontion; // All, Top, Bottom, Left, Right
        this.dataPosiontionInPage = dataPosiontionInPage; // Header, Footer
    }

    public ExcelData(Object value, int dataType, int cellSize,
                     boolean autSize, boolean wrapped,
                     int[] borderPosiontions, int dataPosiontionInPage) {
        this.value = value;
        this.dataType = dataType;
        this.cellSize = cellSize;
        this.autSize = autSize;
        this.wrapped = wrapped;
        this.borderPosiontions = borderPosiontions; // All, Top, Bottom, Left, Right
        this.dataPosiontionInPage = dataPosiontionInPage; // Header, Footer
    }

    public ExcelData(Object value, int dataType, double columnSize,
                     boolean autSize, boolean wrapped,
                     int borderPositions, int dataPosiontionInPage) {
        this.value = value;
        this.dataType = dataType;
        this.columnSize = columnSize;
        this.autSize = autSize;
        this.wrapped = wrapped;
        this.borderPosiontion = borderPositions; // All, Top, Bottom, Left, Right
        this.dataPosiontionInPage = dataPosiontionInPage; // Header, Footer
    }


    public ExcelData(Object value, int dataType, int cellSize,
                     boolean autSize, boolean wrapped,
                     int borderPosiontion, int dataPosiontionInPage, Integer cellIndex, boolean isGroupCellBorder) {
        this.value = value;
        this.dataType = dataType;
        this.cellSize = cellSize;
        this.autSize = autSize;
        this.wrapped = wrapped;
        this.borderPosiontion = borderPosiontion; // All, Top, Bottom, Left, Right
        this.dataPosiontionInPage = dataPosiontionInPage; // Header, Footer
        this.cellIndex = cellIndex;
        this.isGroupCellBorder = isGroupCellBorder;
    }

    public ExcelData(Object value, int dataType, int cellSize,
                     boolean autSize, boolean wrapped, boolean merged,
                     int borderPosiontion, int dataPosiontionInPage) {
        this.value = value;
        this.dataType = dataType;
        this.cellSize = cellSize;
        this.autSize = autSize;
        this.wrapped = wrapped;
        this.merged = merged;
        this.borderPosiontion = borderPosiontion; // All, Top, Bottom, Left, Right
        this.dataPosiontionInPage = dataPosiontionInPage; // Header, Footer
    }

    public ExcelData(Object value, int dataType, int cellSize, int dataPosiontionInPage) {
        this.value = value;
        this.dataType = dataType;
        this.cellSize = cellSize;
        this.dataPosiontionInPage = dataPosiontionInPage;
    }


    public static ExcelData getReportNameData(Object value, int cellSize, int lastColumnInMerged) {
        ExcelData result = ExcelData.getReportNameChildData(value, cellSize, lastColumnInMerged);
        result.setFontSize(12);

        return result;
    }

    public static ExcelData getReportNameChildData(Object value, int cellSize, int lastColumnInMerged) {
        ExcelData result = new ExcelData(value, ExcelData.STRING, cellSize, lastColumnInMerged);
        result.setDataPosiontionInPage(ExcelData.HEADER_CENTER);
        result.setMerged(true);
        result.setFromRow(0);
        result.setToRow(0);
        result.setFromCell(0);
        result.setToCell(lastColumnInMerged);

        result.setHorizontalAlignment(CellStyle.ALIGN_CENTER);
        result.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        return result;
    }

    public static ExcelData getReportNameChildDataWithOutBorderInStart(Object value, int cellSize, int lastColumnInMerged) {
        ExcelData result = new ExcelData(value, ExcelData.STRING, cellSize, lastColumnInMerged);
        result.setDataPosiontionInPage(ExcelData.HEADER_CENTER);
        result.setMerged(true);
        result.setFromRow(0);
        result.setToRow(0);
        result.setFromCell(0);
        result.setToCell(lastColumnInMerged);

        result.setHorizontalAlignment(CellStyle.ALIGN_CENTER);
        result.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        return result;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getFontColor() {
        return fontColor;
    }

    public void setFontColor(int fontColor) {
        this.fontColor = fontColor;
    }

    public short getBgcolor() {
        return bgcolor;
    }

    public void setBgcolor(short bgcolor) {
        this.bgcolor = bgcolor;
    }

    public int getCellSize() {
        return cellSize * 256;
    }

    public void setCellSize(int cellSize) {
        this.cellSize = cellSize;
    }

    public Double getColumnSize() {
        return columnSize * 256d;
    }


    public int getDataPosiontionInPage() {
        return dataPosiontionInPage;
    }

    public void setDataPosiontionInPage(int dataPosiontionInPage) {
        this.dataPosiontionInPage = dataPosiontionInPage;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isWrapped() {
        return wrapped;
    }

    public void setWrapped(boolean wrapped) {
        this.wrapped = wrapped;
    }

    public void setFont(HSSFFont font) {
        if (fontColor > 0) {
            font.setColor((short) fontColor);
        }
        if (fontSize > 0) {
            font.setFontHeightInPoints((short) fontSize);
        }
        font.setBoldweight(bold ? Font.BOLDWEIGHT_BOLD : Font.BOLDWEIGHT_NORMAL);
    }

    public void setStyle(HSSFCellStyle style, HSSFFont font) {
        setFont(font);
        style.setFont(font);
        if (wrapped) {
            style.setWrapText(wrapped);
        }
        style.setVerticalAlignment(verticalAlignment);
        if (horizontalAlignment != CellStyle.ALIGN_LEFT) {
            style.setAlignment(horizontalAlignment);
        }
        if (getDataPosiontionInPage() == HEADER) {
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        } else if (getDataPosiontionInPage() == HEADER2) {
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        } else if (getDataPosiontionInPage() == HEADER3) {
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        } else if (getDataPosiontionInPage() == HEADER_LEFT) {
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            style.setAlignment(CellStyle.ALIGN_LEFT);
        } else if (getDataPosiontionInPage() == HEADER_RIGHT) {
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            style.setAlignment(CellStyle.ALIGN_RIGHT);
        } else if (getDataPosiontionInPage() == HEADER_CENTER) {
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            style.setAlignment(CellStyle.ALIGN_CENTER);
        } else if (getDataPosiontionInPage() == NORMAL_LEFT) {
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            style.setAlignment(CellStyle.ALIGN_LEFT);
        } else if (getDataPosiontionInPage() == NORMAL_RIGHT) {
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            style.setAlignment(CellStyle.ALIGN_RIGHT);
        }


        if (bgcolor != 0) {
            style.setFillForegroundColor(bgcolor);
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);//FillPatternType.SOLID_FOREGROUND
        }
        if (rotation != 0) {
            style.setRotation((short) rotation);
        }
        setBorderStyle(style, HSSFCellStyle.BORDER_THIN, HSSFColor.BLACK.index);

    }

    public void setBorderStyle(HSSFCellStyle style, short borderIndex, short borderColorIndex) {
        //Munir said
        if (borderPosiontions != null) {
            for (int position : borderPosiontions) {
                applyBorderPosition(style, borderIndex, borderColorIndex, position);
            }
        } else {
            applyBorderPosition(style, borderIndex, borderColorIndex, borderPosiontion);
        }
    }

    public void applyBorderPosition(HSSFCellStyle style, short borderIndex, short borderColorIndex, int position) {
        if (position == ALL_BORDER || position == TOP_BORDER) {
            style.setBorderTop(borderIndex);
            style.setTopBorderColor(borderColorIndex);
        }

        if (position == ALL_BORDER || position == LEFT_BORDER) {
            style.setBorderLeft(borderIndex);
            style.setLeftBorderColor(borderColorIndex);
        }

        if (position == ALL_BORDER || position == BOTTOM_BORDER) {
            style.setBorderBottom(borderIndex);
            style.setBottomBorderColor(borderColorIndex);
        }

        if (position == ALL_BORDER || position == RIGHT_BORDER) {
            style.setBorderRight(borderIndex);
            style.setRightBorderColor(borderColorIndex);
        }
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public boolean isAutSize() {
        return autSize;
    }

    public boolean isMerged() {
        return merged;
    }

    public void setMerged(boolean merged) {
        this.merged = merged;
    }

    public int getFromRow() {
        return fromRow;
    }

    public void setFromRow(int fromRow) {
        this.fromRow = fromRow;
    }

    public int getFromCell() {
        return fromCell;
    }

    public void setFromCell(int fromCell) {
        this.fromCell = fromCell;
    }

    public int getToRow() {
        return toRow;
    }

    public void setToRow(int toRow) {
        this.toRow = toRow;
    }

    public int getToCell() {
        return toCell;
    }

    public void setToCell(int toCell) {
        this.toCell = toCell - 1;
    }

    public void addMerging(HSSFSheet sheet, int fromRow, int fromCell, int toRow, int toCell) {
        if (fromCell != toCell) {
            sheet.addMergedRegion(new CellRangeAddress(fromRow, toRow, (short) fromCell, (short) toCell));
        }
    }

    public List<ExcelData[]> getItems() {
        return items;
    }

    public void setItems(List<ExcelData[]> items) {
        this.items = items;
    }

    public Integer getCellIndex() {
        return cellIndex;
    }

    public void setCellIndex(Integer cellIndex) {
        this.cellIndex = cellIndex;
    }

    public boolean isGroupCellBorder() {
        return isGroupCellBorder;
    }

    public void setGroupCellBorder(boolean groupCellBorder) {
        isGroupCellBorder = groupCellBorder;
    }

    public boolean isStyle() {
        return style;
    }

    public void setStyle(boolean style) {
        this.style = style;
    }

    public boolean isCenterAligned() {
        return CellStyle.ALIGN_CENTER == horizontalAlignment;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public short getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(short borderColor) {
        this.borderColor = borderColor;
    }

    public void setHorizontalAlignment(short horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }

    public void setVerticalAlignment(short verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
    }

    public String getCustomDateFormat() {
        return CustomDateFormat;
    }

    public void setCustomDateFormat(String customDateFormat) {
        CustomDateFormat = customDateFormat;
    }

    public String getCurrencySymbol() {
        return this.currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getCommentString() {
        return commentString;
    }

    public void setCommentString(String commentString) {
        this.commentString = commentString;
    }
}
