package com.finnetlimited.reportservice.core.server.utils;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: nodir
 * Date: 14.07.2010
 * Time: 13:58:33
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class ImportExcel implements Constants {

    private Workbook workbook = null;
    private DataFormatter formatter = null;
    private FormulaEvaluator evaluator = null;
    private String separator = null;

    /**
     * Identifies that the CSV file should obey Excel's formatting conventions
     * with regard to escaping certain embedded characters - the field separator,
     * speech mark and end of line (EOL) character
     */
    public static final int EXCEL_STYLE_ESCAPING = 0;
    /**
     * Identifies that the CSV file should obey UNIX formatting conventions
     * with regard to escaping certain embedded characters - the field separator
     * and end of line (EOL) character
     */
    public static final int UNIX_STYLE_ESCAPING = 1;

    private DataSource dataSource;


    public void excelFile(DataSource dataSource, String companyId, String viewName, String tableName, String strSource, int formattingConvention, String reportCategoryTemplateId)
            throws IOException,
            IllegalArgumentException, InvalidFormatException {
        this.dataSource = dataSource;
        File source = new File(strSource);
        File[] filesList;

        // Check that the source file/folder exists.
        if (!source.exists()) {
            throw new IllegalArgumentException("The source for the Excel " +
                    "file(s) cannot be found.");
        }

        // Ensure the value passed to the formattingConvention parameter is
        // within range.
        if (formattingConvention != ImportExcel.EXCEL_STYLE_ESCAPING &&
                formattingConvention != ImportExcel.UNIX_STYLE_ESCAPING) {
            throw new IllegalArgumentException("The value passed to the " +
                    "formattingConvention parameter is out of range.");
        }

        // Copy the spearator character and formatting convention into local
        // variables for use in other methods.

        // Check to see if the sourceFolder variable holds a reference to
        // a file or a folder full of files.
        if (source.isDirectory()) {
            // Get a list of all of the Excel spreadsheet files (workbooks) in
            // the source folder/directory
            filesList = source.listFiles(new ExcelFilenameFilter());
        } else {
            // Assume that it must be a file handle - although there are other
            // options the code should perhaps check - and store the reference
            // into the filesList variable.
            filesList = new File[]{source};
        }

        // Step through each of the files in the source folder and for each
        // open the workbook, convert it's contents to CSV format and then
        // save the resulting file away into the folder specified by the
        // contents of the destination variable. Note that the name of the
        // csv file will be created by taking the name of the Excel file,
        // removing the extension and replacing it with .csv. Note that there
        // is one drawback with this approach; if the folder holding the files
        // contains two workbooks whose names match but one is a binary file
        // (.xls) and the other a SpreadsheetML file (.xlsx), then the names
        // for both CSV files will be identical and one CSV file will,
        // therefore, over-write the other.
        for (File excelFile : filesList) {
            // Open the workbook
            this.openWorkbook(excelFile);

            // Convert it's contents into a CSV file
            //this.convertToCSV();
            this.loadFromExcel(viewName, tableName, companyId, reportCategoryTemplateId);
        }
    }

    /**
     * Open an Excel workbook ready for conversion.
     *
     * @param file An instance of the File class that encapsulates a handle
     *             to a valid Excel workbook. Note that the workbook can be in
     *             either binary (.xls) or SpreadsheetML (.xlsx) format.
     * @throws java.io.FileNotFoundException Thrown if the file cannot be located.
     * @throws java.io.IOException           Thrown if a problem occurs in the file system.
     * @throws org.apache.poi.openxml4j.exceptions.InvalidFormatException
     *                                       Thrown
     *                                       if invalid xml is found whilst parsing an input SpreadsheetML
     *                                       file.
     */
    private void openWorkbook(File file) throws
            IOException, InvalidFormatException {
        FileInputStream fis = null;
        try {
            System.out.println("Opening workbook [" + file.getName() + "]");

            fis = new FileInputStream(file);

            // Open the workbook and then create the FormulaEvaluator and
            // DataFormatter instances that will be needed to, respectively,
            // force evaluation of forumlae found in cells and create a
            // formatted String encapsulating the cells contents.
            this.workbook = WorkbookFactory.create(fis);
            this.evaluator = this.workbook.getCreationHelper().createFormulaEvaluator();
            this.formatter = new DataFormatter();
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }

    private void loadFromExcel(String viewName, String tableName, String companyId, String reportCategoryTemplateId) {
        int CELL_TYPE_DATE = 6;
        int CELL_TYPE_TIME = 7;
        int CELL_TYPE_TIMESTAMP = 8;
        boolean isRowBlank;
        Sheet sheet;
        Row row;
        Cell cell;
        int lastRowNum;
        int lastCellNum;
        ArrayList<Integer> cellTypes = new ArrayList<>();
        ArrayList<String> columns = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        String dbTableName = "x" + companyId + "_" + tableName;

        System.out.println("Converting files contents to CSV format.");

        int numSheets = this.workbook.getNumberOfSheets();
        numSheets = (Math.min(numSheets, 1));

        for (int i = 0; i < numSheets; i++) {
            sheet = workbook.getSheetAt(i);

            if (sheet.getPhysicalNumberOfRows() > 0) {
                StringBuilder fieldsStr = new StringBuilder();
                row = sheet.getRow(0);
                lastCellNum = row.getLastCellNum();
                for (int c = 0; c < lastCellNum; c++) {
                    cell = row.getCell(c);
                    if (cell == null) {
                        continue;
                    }
                    if (!cell.getStringCellValue().trim().isEmpty()) {
                        final String cellStringValue = cell.getStringCellValue().replaceAll("[^0-9a-zA-Z]", "").toLowerCase();
                        String fullcolumnname = cellStringValue;
                        if (cellStringValue.isEmpty() || cellStringValue == null) {
                            fullcolumnname = "column_" + c;
                        }
                        columns.add(fullcolumnname);
                        fieldsStr.append(fullcolumnname).append(",");
                        titles.add(cell.getStringCellValue());
                    }
                }
                fieldsStr = new StringBuilder(fieldsStr.substring(0, fieldsStr.length() - 1));
                int headerColumnsCount = columns.size();

                lastRowNum = sheet.getLastRowNum();
                lastRowNum = (Math.min(lastRowNum, 1000));
                boolean isFirst = true;
                for (int j = 1; j <= lastRowNum; j++) {
                    row = sheet.getRow(j);
                    if (row == null)
                        break;

                    StringBuilder dataRowStr = new StringBuilder();
                    isRowBlank = true;
                    for (int y = 0; y < headerColumnsCount; y++) {
                        cell = row.getCell(y);
                        if (isFirst && cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
                            if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    if (this.formatter.formatCellValue(cell).indexOf("/") > 0 && this.formatter.formatCellValue(cell).indexOf(":") > 0) {
                                        cellTypes.add(CELL_TYPE_TIMESTAMP);
                                    } else if (this.formatter.formatCellValue(cell).indexOf(":") > 0) {
                                        cellTypes.add(CELL_TYPE_TIME);
                                    } else {
                                        cellTypes.add(CELL_TYPE_DATE);
                                    }
                                } else {
                                    cellTypes.add(Cell.CELL_TYPE_NUMERIC);
                                }
                            } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
                                cellTypes.add(Cell.CELL_TYPE_NUMERIC);
                            } else {
                                cellTypes.add(Cell.CELL_TYPE_STRING);
                            }
                        }

                        if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
                            if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    //date
                                    dataRowStr.append("'").append(this.formatter.formatCellValue(cell).replace("'", "`")).append("',");
                                } else {
                                    String value = this.formatter.formatCellValue(cell, this.evaluator).replace(" ", "").replaceAll("[^0-9.,]", "");
                                    if (value == null || "".equals(value) || " ".equals(value))
                                        value = "0";
                                    //numeric
                                    dataRowStr.append("'").append(value).append("',");
                                }
                            } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
                                switch (cell.getCachedFormulaResultType()) {
                                    case Cell.CELL_TYPE_NUMERIC -> {
                                        if (DateUtil.isCellDateFormatted(cell)) {
                                            dataRowStr.append("'").append(cell.getDateCellValue()).append("',");
                                        } else {
                                            dataRowStr.append(cell.getNumericCellValue()).append(",");
                                        }
                                    }
                                    case Cell.CELL_TYPE_STRING ->
                                            dataRowStr.append(("'" + cell.getRichStringCellValue()).replace("'", "`").replace(",", "")).append("',");
                                }
                                //numeric(formula)
                            } else {
                                //string
                                dataRowStr.append("'").append(this.formatter.formatCellValue(cell).replace(",", "").replace("'", "`")).append("',");
                            }
                            isRowBlank = false;
                        } else {
                            cellTypes.add(Cell.CELL_TYPE_STRING);
                            dataRowStr.append("null,");
                        }


                    }
                    if (isFirst) {
                        createTempTable(columns, dbTableName);
                        createTempTable(columns, cellTypes, dbTableName);
                    }
                    isFirst = false;
                    if (!isRowBlank) {
                        dataRowStr = new StringBuilder(dataRowStr.substring(0, dataRowStr.length() - 1));
                        insertExcelData(fieldsStr.toString(), dataRowStr.toString(), dbTableName);
                    }

                }
            }
        }

        System.out.println("Inserting the data in excel into the SQL datasource.");
        moveExcelData(columns, cellTypes, dbTableName);
        System.out.println("Generating XML file.");

        generateXml(columns, titles, cellTypes, dbTableName, viewName, companyId, reportCategoryTemplateId);
        System.out.println("Load Excel is DONE");
    }

    /**
     * The main() method contains code that demonstrates how to use the class.
     *
     * @param args An array containing zero, one or more elements all of type
     *             String. Each element will encapsulate an argument specified by the
     *             user when running the program from the command prompt.
     */
    public static void main(String[] args) {
        try {
        } catch (Exception ex) {
            System.out.println("Caught an: " + ex.getClass().getName());
            System.out.println("Message: " + ex.getMessage());
            System.out.println("Stacktrace follows:.....");
            ex.printStackTrace(System.out);
        }
    }

    /**
     * An instance of this class can be used to control the files returned
     * be a call to the listFiles() method when made on an instance of the
     * File class and that object refers to a folder/directory
     */
    class ExcelFilenameFilter implements FilenameFilter {

        /**
         * Determine those files that will be returned by a call to the
         * listFiles() method. In this case, the name of the file must end with
         * either of the following two extension; '.xls' or '.xlsx'. For the
         * future, it is very possible to parameterise this and allow the
         * containing class to pass, for example, an array of Strings to this
         * class on instantiation. Each element in that array could encapsulate
         * a valid file extension - '.xls', '.xlsx', '.xlt', '.xlst', etc. These
         * could then be used to control which files were returned by the call
         * to the listFiles() method.
         *
         * @param file An instance of the File class that encapsulates a handle
         *             referring to the folder/directory that contains the file.
         * @param name An instance of the String class that encapsulates the
         *             name of the file.
         * @return A boolean value that indicates whether the file should be
         *         included in the array retirned by the call to the listFiles()
         *         method. In this case true will be returned if the name of the
         *         file ends with either '.xls' or '.xlsx' and false will be
         *         returned in all other instances.
         */
        public boolean accept(File file, String name) {
            return (name.endsWith(".xls") || name.endsWith(".xlsx"));
        }
    }

    private boolean deleteTempTable(String tableName) {
        boolean result = false;
        boolean isHaveTable = false;
        String selectQuery = " select count(*) from pg_tables where tablename='" + tableName + "'";
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement st = conn.prepareStatement(selectQuery);
            ResultSet resultSet = st.executeQuery();
            resultSet.next();
            isHaveTable = resultSet.getInt(1) > 0;
        } catch (Exception e) {
            System.out.println("Message: " + e.getMessage() + "; Query: " + selectQuery);
            e.printStackTrace(System.out);
        }
        if (isHaveTable) {
            String sqlQuery = "drop table " + tableName;
            try (Connection conn = dataSource.getConnection()) {
                PreparedStatement st = conn.prepareStatement(sqlQuery);
                result = st.execute();
            } catch (Exception e) {
                System.out.println("Message: " + e.getMessage() + "; Query: " + sqlQuery);
                e.printStackTrace(System.out);
            }
        }
        return result;
    }

    public boolean createTempTable(List<String> fields, String tableName) {
        boolean result = false;
        try {
            deleteTempTable(tableName);
        } finally {
            StringBuilder fieldsStr = new StringBuilder(tableName + "id serial NOT NULL,");

            for (String field : fields) {
                fieldsStr.append("\"").append(field).append("\" text,");
            }
            fieldsStr.append("CONSTRAINT ").append(tableName).append("_pkey PRIMARY KEY (").append(tableName).append("id)");
            String sqlQuery = "create table " + tableName + "(" + fieldsStr + ") WITH (OIDS=FALSE)";

            try (Connection conn = dataSource.getConnection()) {
                PreparedStatement st = conn.prepareStatement(sqlQuery);
                result = st.execute();
            } catch (Exception e) {
                System.out.println("Message: " + e.getMessage());
                e.printStackTrace(System.out);
            }
        }

        return result;
    }

    public boolean createTempTable(List<String> fields, List<Integer> fieldTypes, String tableName) {
        StringBuilder fieldsStr = new StringBuilder(tableName + "_tid serial NOT NULL,");
        boolean result = false;
        for (int i = 0; i < fields.size(); i++) {
            if (fieldTypes.get(i) == Cell.CELL_TYPE_NUMERIC) {
                fieldsStr.append("\"").append(fields.get(i)).append("\" numeric,");
            } else if (fieldTypes.get(i) == 6) {
                fieldsStr.append("\"").append(fields.get(i)).append("\" date,");
            } else {
                fieldsStr.append("\"").append(fields.get(i)).append("\" text,");
            }
        }
        fieldsStr.append("CONSTRAINT ").append(tableName).append("_t_pkey PRIMARY KEY (").append(tableName).append("_tid)");
        String sqlQuery = "create table " + tableName + "_t (" + fieldsStr + ") WITH (OIDS=FALSE)";

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement st = conn.prepareStatement(sqlQuery);
            result = st.execute();
        } catch (Exception e) {
            System.out.println("Message: " + e.getMessage());
            e.printStackTrace(System.out);
        }

        return result;
    }

    public boolean insertExcelData(String fieldsStr, String dataRowStr, String tableName) {
        String sqlQuery = "insert into " + tableName + " (" + fieldsStr + ") values (" + dataRowStr + ")";

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement st = conn.prepareStatement(sqlQuery);
        } catch (Exception e) {
            System.out.println("Message: " + e.getMessage() + "Query:" + sqlQuery);
            e.printStackTrace(System.out);
        }

        return true;
    }

    public boolean moveExcelData(List<String> fields, List<Integer> fieldTypes, String tableName) {
        boolean result = false;
        StringBuilder cols = new StringBuilder();
        for (int i = 0; i < fields.size(); i++) {
            switch (fieldTypes.get(i)) {
                case Cell.CELL_TYPE_NUMERIC ->
                        cols.append("case when position(',' in ").append(fields.get(i)).append(") > 0  and position('.' in ").append(fields.get(i)).append(") > 0").append(" then cast(replace(").append(fields.get(i)).append(",',','') as numeric) else cast(replace(").append(fields.get(i)).append(",',','.') as numeric) end,");
                case 6 ->//DATE
                        cols.append("cast(").append(fields.get(i)).append(" as date),");
                case 7 ->//TIME
                        cols.append("cast(").append(fields.get(i)).append(" as time),");
                case 8 ->//DATE
                        cols.append("cast(").append(fields.get(i)).append(" as timestamp),");
                default -> cols.append(fields.get(i)).append(",");
            }
        }
        cols = new StringBuilder(cols.substring(0, cols.length() - 1));
        String sqlQuery = "insert into " + tableName + "_t select " + tableName + "id," + cols + " from " + tableName;

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement st = conn.prepareStatement(sqlQuery);
            result = st.execute();
        } catch (Exception e) {
            System.out.println("Message: " + e.getMessage() + " Query: " + sqlQuery);
            e.printStackTrace(System.out);
        }

        if (result) {
            try {
                deleteTempTable(tableName);
            } finally {
                result = false;
            }
        }

        return result;
    }

    public boolean generateXml(List<String> fields, List<String> titles, List<Integer> types, String tableName, String viewName, String companyId, String reportCategoryTemplateId) {
        Integer reportTemplateCategoryId = Integer.parseInt(reportCategoryTemplateId);
        SelectItem templateItem = new SelectItem();
        StringBuilder columns = new StringBuilder();
        for (int i = 0; i < fields.size(); i++) {
            if (types.get(i) == Cell.CELL_TYPE_NUMERIC) {
                columns.append("<column name=\"").append(fields.get(i)).append("\" title=\"").append(titles.get(i)).append("\"  type=\"number\" formattype=\"number\" />\n");
            } else if (types.get(i) == 6) {
                columns.append("<column name=\"").append(fields.get(i)).append("\" title=\"").append(titles.get(i)).append("\"  type=\"date\" formattype=\"date\" />\n");
            } else {
                columns.append("<column name=\"").append(fields.get(i)).append("\" title=\"").append(titles.get(i)).append("\" type=\"string\" formattype=\"string\" />\n");
            }
        }
        String xml =
                "<report>\n" +
                        "\t<view>\n" +
                        "\t\t<title>" + viewName + "</title>\n" +
                        "\t\t<name>" + viewName + " Report</name>\n" +
                        "\t\t<category>" + viewName.toLowerCase() + "</category>\n" +
                        "\t\t<sqlquery>\n" +
                        "\t\t\t<select agregateFunc=\"distinct\">\n" +
                        "\t\t\t\t<table name=\"" + viewName + "\">\n" + columns +
                        "\t\t\t\t</table>\n" +
                        "\t\t\t</select>\n" +
                        "\t\t\t<from>\n" +
                        "\t\t\t\t" + tableName + "_t\n" +
                        "\t\t\t</from>\n" +
                        "\t\t\t<where>\n" +
                        "\t\t\t\t<terms id=\"base\" value=\"1=1\" />\n" +
                        "\t\t\t\t<terms id=\"userid\" userid=\"\" company=\"\" />\n" +
                        "\t\t\t\t<terms id=\"managerid\" userid=\"\" company=\"\" />\n" +
                        "\t\t\t\t<terms id=\"leaderid\" userid=\"\" company=\"\" />\n" +
                        "\t\t\t\t<terms id=\"admin\" company=\"\" />\n" +
                        "\t\t\t</where>\n" +
                        "\t\t</sqlquery>\n" +
                        "\t</view>\n" +
                        "</report>";
        Integer[] companyIDs = new Integer[1];
        companyIDs[0] = Integer.parseInt(companyId);
        templateItem.setName(viewName);
        templateItem.setDescription(xml);
        templateItem.setNewItem(true);
        CoreService coreService = (CoreService) ApplicationContextProvider.applicationContext.getBean("reportingCoreService");

        ArrayList<SelectItem> roles = coreService.getTemplateRoles(companyIDs[0], null);
        ListingFilterParameter filterParameter = new ListingFilterParameter();

        filterParameter.setColumnsOfListing(new ArrayList<>());
        for (SelectItem role : roles) {
            filterParameter.getColumnsOfListing().add(role.getDescription());
        }
        filterParameter.setSelected(true);
        filterParameter.setName(templateItem.getName());
        filterParameter.setDescription(templateItem.getDescription());
        filterParameter.setCategoryID(reportTemplateCategoryId);
        filterParameter.setCompaines(companyIDs);
        filterParameter.setObjectId(templateItem.getId());
        filterParameter.setLibrary(false);
        coreService.saveOrUpdateReportTemplate(filterParameter);
        return true;
    }
}
