package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public abstract class ExcelHandler implements HttpRequestHandler, Constants {
    protected String filename;

    @Autowired
    @Qualifier("commonLocalizer")
    protected WfmMessageSource commonLocalizer;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            setFileName(filename + ".xls");
            filename = ServerUtils.normalizeFileNameT(filename);
            response.setHeader("content-disposition", "attachment; filename=" + filename + ".xls");
            response.setContentType(CONTENT_TYPE_EXCEL);
            HSSFWorkbook wb = getWorkBook(request);
            if (wb == null) {
                wb = getWorkBook(request, response);
            }

            OutputStream stream = response.getOutputStream();

            if (wb != null) {
                wb.write(stream);
            }
            stream.flush();
            stream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final String CONTENT_TYPE_EXCEL = "application/vnd.ms-excel";

    protected HSSFWorkbook getWorkBook(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    protected HSSFWorkbook getWorkBook(HttpServletRequest request) {
        return null;
    }

    public abstract void setFileName(String name);
}
