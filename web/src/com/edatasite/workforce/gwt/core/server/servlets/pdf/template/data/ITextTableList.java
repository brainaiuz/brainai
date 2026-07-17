package com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data;

import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfHeaderPosition;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextRowProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 22-Jun-2010
 * Time: 16:55:53
 * <p/>
 * This is List Table tempalte repository class
 */
public class ITextTableList {

    public static final Integer CELL_TEXT = 0;
    public static final Integer CELL_LINK = 1;
    public static final Integer CELL_IMAGE = 2;
    public static final Integer CELL_HTML = 3;
    public static final Integer CELL_HTML_TEXT = 4;

    private int numColumns;// not equals 0
    private Integer totalWidth;
    private Integer tableAlignment;// Uses com.lowagie.text.Element constants
    private float borderWidth = -1; // border width began in ZERO
    private float cellPadding = -1;// Padding began in 0
    private float beforSpacing = -1;// Value began in 0
    private float afterSpacing = -1;// Value began in 0
    private List<Float> colWidthPercentage;
    private String name;// table name
    private String namePositionRight;// table name
    private String namePositionCenter;// table name
    private List<CellData> tableHeader;
    private List<CellData[]> tableRows;
    private Map<Integer, Map<Integer, ITextRowProperty>> rowProperties;
    private List<String> tableFooter;
    private ITextPdfHeaderPosition headerPosition = ITextPdfHeaderPosition.FIRST_HORIZONTAL_ROW;
    private boolean isLastCellAdd = true;

    private String fontName;

    public ITextTableList(int numColumns) {
        this.numColumns = numColumns;
        tableRows = new ArrayList<>();
        rowProperties = new HashMap<>();
    }


    public int getNumColumns() {
        return numColumns != 0 ? numColumns : 1;
    }

    public Integer getTotalWidth() {
        return totalWidth;
    }

    public void setTotalWidth(Integer totalWidth) {
        this.totalWidth = totalWidth;
    }

    public Integer getTableAlignment() {
        return tableAlignment;
    }

    /**
     * Uses com.lowagie.text.Element
     *
     * @param tableAlignment {
     *                       Element.ALIGN_LEFT,
     *                       Element.ALIGN_CENTER,
     *                       Element.ALIGN_RIGHT,
     *                       }
     */
    public void setTableAlignment(Integer tableAlignment) {
        this.tableAlignment = tableAlignment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamePositionRight() {
        return namePositionRight;
    }

    public void setNamePositionRight(String namePositionRight) {
        this.namePositionRight = namePositionRight;
    }

    public String getNamePositionCenter() {
        return namePositionCenter;
    }

    public void setNamePositionCenter(String namePositionCenter) {
        this.namePositionCenter = namePositionCenter;
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
    }

    public float getCellPadding() {
        return cellPadding;
    }

    public void setCellPadding(float cellPadding) {
        this.cellPadding = cellPadding;
    }

    public float getBeforSpacing() {
        return beforSpacing;
    }

    public void setBeforSpacing(float beforSpacing) {
        this.beforSpacing = beforSpacing;
    }

    public float getAfterSpacing() {
        return afterSpacing;
    }

    public void setAfterSpacing(float afterSpacing) {
        this.afterSpacing = afterSpacing;
    }

    /**
     * Set Column width by percent
     * <p/>
     * Column width count equals to column counts
     *
     * @param colWidth
     */
    public void addTableWidthPercentage(float... colWidth) {
        if (colWidth.length != 0) {
            this.colWidthPercentage = new ArrayList<>();
            for (float aColWidth : colWidth) {
                this.colWidthPercentage.add(aColWidth);
            }
        }
    }

    /**
     * set One rows table
     *
     * @param columns
     */
    public void addPdfTableRows(Integer[] columnsAligments, String... columns) {
        if (columns != null && columns.length != 0) {
            CellData[] row = new CellData[columns.length];
            for (int i = 0; i < columns.length; i++) {
                if (columnsAligments != null && columnsAligments[i] != null) {
                    row[i] = new CellData(columns[i], columnsAligments[i]);
                } else {
                    row[i] = new CellData(columns[i]);
                }
            }
            tableRows.add(row);
        }
    }

    public void addPdfTableRows(String... columns) {
        if (columns != null && columns.length != 0) {
            CellData[] row = new CellData[columns.length];
            for (int i = 0; i < columns.length; i++) {
                row[i] = new CellData(columns[i]);
            }
            tableRows.add(row);
        }
    }

    public void addPdfTableRows(CellData... columns) {
        if (columns != null && columns.length != 0) {
            tableRows.add(columns);
        }
    }

    /**
     * set Row properties
     *
     * @param property
     */
    public void addRowProperty(Integer rowId, Map<Integer, ITextRowProperty> property) {
        rowProperties.put(rowId, property);
    }

    public Map<Integer, Map<Integer, ITextRowProperty>> getRowProperties() {
        return rowProperties;
    }

    /**
     * Add table header
     *
     * @param header
     */
    public void addPdfTableHeader(String... header) {
        if (header != null && header.length != 0) {
            if (this.tableHeader == null) {
                this.tableHeader = new ArrayList<>();
            }
            for (String aHeader : header) {
                this.tableHeader.add(new CellData(aHeader));
            }
        }
    }

    /**
     * Add table header
     *
     * @param header
     */
    public void addPdfTableHeader(CellData... header) {
        if (header != null && header.length != 0) {
            if (this.tableHeader == null) {
                this.tableHeader = new ArrayList<>();
            }
            this.tableHeader.addAll(Arrays.asList(header));
        }
    }

    /**
     * Add Table footer
     *
     * @param footer
     */
    public void addPdfTablerFooter(String... footer) {
        if (footer != null && footer.length != 0) {
            if (this.tableFooter == null) {
                this.tableFooter = new ArrayList<>();
            }
            this.tableFooter.addAll(Arrays.asList(footer));
        }
    }

    public float[] getColWidthPercentage() {
        if (colWidthPercentage != null) {
            float[] arrayOfFloat = new float[colWidthPercentage.size()];
            int i = 0;
            for (Float widthOfColumn : colWidthPercentage) {
                arrayOfFloat[i++] = widthOfColumn;
            }
            return arrayOfFloat;
        }
        return new float[]{};
    }

    public List<CellData> getTableHeader() {
        if (tableHeader == null) {
            return new ArrayList<>();
        }
        return tableHeader;
    }

    public List<String> getHeadersAsStrList() {
        List<String> headers = new ArrayList<>();
        if (tableHeader != null) {
            for (CellData cd : tableHeader) {
                headers.add(cd.getText());
            }
        }
        return headers;
    }

    public List<CellData[]> getTableRows() {
        if (tableRows == null) {
            return new ArrayList<>();
        }
        return tableRows;
    }

    public List<String> getTableFooter() {
        if (tableFooter == null) {
            return new ArrayList<>();
        }
        return tableFooter;
    }

//    public Map<Integer, Map<Integer, String>> getAnchorRowCol() {
//        if (anchorRowCol == null) {
//            anchorRowCol = new HashMap<Integer, Map<Integer, String>>();
//        }
//        return anchorRowCol;
//    }
//
//    public void setAnchorRowCol(Map<Integer, Map<Integer, String>> anchorRowCol) {
//        this.anchorRowCol = anchorRowCol;
//    }
//
//    public Map<Integer, Integer> getImageRowCol() {
//        if (imageRowCol == null) {
//            imageRowCol = new HashMap<Integer, Integer>();
//        }
//        return imageRowCol;
//    }

    /**
     * Set Map<Row,Col> image location matrisa row column
     *
     * @param imageRowCol
     */
//    public void setImageRowCol(Map<Integer, Integer> imageRowCol) {
//        this.imageRowCol = imageRowCol;
//    }
//
//    public Map<Integer, Map<Integer, ITextImageProperty>> getImageRowColByProperty() {
//        if (imageRowColByProperty == null) {
//            imageRowColByProperty = new HashMap<Integer, Map<Integer, ITextImageProperty>>();
//        }
//        return imageRowColByProperty;
//    }

    /**
     * Map put image row and column and image properties
     *
     * @param imageRowColByProperty
     */
//    public void setImageRowColByProperty(Map<Integer, Map<Integer, ITextImageProperty>> imageRowColByProperty) {
//        this.imageRowColByProperty = imageRowColByProperty;
//    }
//
//    public Map<Integer, Integer> getAlignmentCells() {
//        if (alignmentCells == null) {
//            return alignmentCells = new HashMap<Integer, Integer>();
//        }
//        return alignmentCells;
//    }

    /**
     * Set Cells  order Horizontal Alignment
     * Uses please for Cells Horizontal Alignments
     * <b> com.lowagie.text.Element </b> properties
     * <p/>
     * alignmentCells        <br/>
     * <p/>
     * <b> {
     * com.lowagie.text.Element.ALIGN_LEFT,
     * com.lowagie.text.Element.ALIGN_CENTER,
     * com.lowagie.text.Element.ALIGN_RIGHT;
     * } </b>
     * <p/>
     * Map <Column Number,com.lowagie.text.Element.ALIGN_CENTER>
     * Column number begin in Zero
     */
//    public void setAlignmentCells(Map<Integer, Integer> alignmentCells) {
//        this.alignmentCells = alignmentCells;
//    }
    public ITextPdfHeaderPosition getHeaderPosition() {
        return headerPosition;
    }

    public void setHeaderPosition(ITextPdfHeaderPosition headerPosition) {
        this.headerPosition = headerPosition;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public boolean isLastCellAdd() {
        return isLastCellAdd;
    }

    public void setLastCellAdd(boolean isLastCellAdd) {
        this.isLastCellAdd = isLastCellAdd;
    }
}
