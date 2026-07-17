package com.finnetlimited.reportservice.core.server.handler;

import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.utils.EdsContextParams;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.finnetlimited.reportservice.core.server.utils.ImportExcel;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: nodir
 * Date: 17.07.2010
 * Time: 14:04:11
 * To change this template use File | Settings | File Templates.
 */
public final class ExcelImportHandler implements HttpRequestHandler {

    private DataSource dataSourceXLS;

    private CoreService coreService;

    public void setDataSourceXLS(DataSource dataSource) {
        this.dataSourceXLS = dataSource;
    }

    public void setCoreService(CoreService coreService) {
        this.coreService = coreService;
    }

    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Create a factory for disk-based file items
        FileItemFactory factory = new DiskFileItemFactory();

        // Set factory constraints
        //factory.setSizeThreshold(yourMaxMemorySize);
        //factory.setRepository(yourTempDirectory);

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);

        // Set overall request size constraint
        //upload.setSizeMax(yourMaxRequestSize);

        // Parse the request
        try {
            List /* FileItem */ items = upload.parseRequest(request);
            // Process the uploaded items
            Iterator iter = items.iterator();
            String viewName = "";
            String reportCategoryId = "";
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();

                if (item.isFormField()) {
                    if ("reportCategoriesFormElementViewName".equals(item.getFieldName())) {
                        reportCategoryId = item.getString();
                    } else {
                        viewName = item.getString();
                    }
                } else {
                    if (!viewName.contains("(xls)")) {
                        viewName = viewName + " (xls)";
                    }
                    Integer companyId = SecurityContext.getCompanyID();
                    processUploadedFile(item, companyId, viewName, reportCategoryId);
                    response.getWriter().write(item.getName() + "|" + viewName);
                    //response.getWriter().write(new String(item.getName() + " - file successfully uploaded!"));
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void processUploadedFile(FileItem item, Integer companyId, String viewName, String reportCategoryTemplateId) {
        //To change body of created methods use File | Settings | File Templates.
        String tempFilePath = "";
        boolean isInMemory = item.isInMemory();
        long sizeInBytes = item.getSize();
        if (!isInMemory && sizeInBytes < 10240000) {
            try {
                if (EdsContextParams.isLocal()) {
                    tempFilePath = "" + "\\xmls\\excel\\" + companyId;
                } else {
                    tempFilePath = "" + "/xmls/excel/" + companyId;
                }

                File userDir = new File(tempFilePath);
                if (!userDir.exists()) {
                    try {
                        // Create a directory; all ancestor directories must exist
                        boolean success = (new File(tempFilePath)).mkdirs();
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }

                String hashedFileName = generateFileHashKey(item.getName());
                String viewName2 = (viewName == null ? hashedFileName : viewName);
                String tempFileName = "";
                if (EdsContextParams.isLocal()) {
                    tempFileName = tempFilePath + "\\" + hashedFileName;
                } else {
                    tempFileName = tempFilePath + "/" + hashedFileName;
                }
                File uploadedFile = new File(tempFileName);
                item.write(uploadedFile);

                ImportExcel converter = null;
                try {
                    converter = new ImportExcel();
                    converter.excelFile(dataSourceXLS, companyId.toString(), viewName2, hashedFileName, tempFileName, ImportExcel.EXCEL_STYLE_ESCAPING, reportCategoryTemplateId);
                } catch (Exception ex) {
                    System.out.println("Caught an: " + ex.getClass().getName());
                    System.out.println("Message: " + ex.getMessage());
                    System.out.println("Stacktrace follows:.....");
                    ex.printStackTrace(System.out);
                }
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }

    private static String generateFileHashKey(String content) throws Exception {
        String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat time = new SimpleDateFormat(DATE_FORMAT_NOW);

        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.reset();
        digest.update((content + time.format(cal.getTime())).getBytes());
        byte[] updatedData = digest.digest();

        StringBuilder result = new StringBuilder();
        for (byte anUpdatedData : updatedData) {
            result.append(Integer.toHexString(0xFF & anUpdatedData));
        }
        return result.toString();
    }
}
