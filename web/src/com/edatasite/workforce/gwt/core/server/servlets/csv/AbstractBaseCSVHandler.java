package com.edatasite.workforce.gwt.core.server.servlets.csv;

import au.com.bytecode.opencsv.CSVWriter;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Jul 9, 2010
 * Time: 9:37:12 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractBaseCSVHandler implements HttpRequestHandler {
    @Autowired
    @Qualifier("commonLocalizer")
    protected WfmMessageSource commonLocalizer;

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object dataClass = prepareRequest(request);
        try {
            System.out.println("CSV EXPORTING STARTED..." + new Date());
            buildMetadataStepFirst(response, dataClass);
            ByteArrayOutputStream baos = getData(dataClass);
            buildMetadataStepSecond(response, baos.size());
            ServletOutputStream out = response.getOutputStream();
            baos.writeTo(out);
            try {
                baos.flush();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("CSV EXPORTING ENDED..." + new Date());
    }

    private ByteArrayOutputStream getData(Object dataClass) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);
        CSVWriter writer = new CSVWriter(streamWriter);
        recursivelyWriteToStream(writer, dataClass, null);
        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream;
    }

    private void recursivelyWriteToStream(CSVWriter writer, Object dataClass, CSVTransferObject transferObject) {
        transferObject = buildCSV(transferObject, dataClass);
        for (String[] row : transferObject.getRows()) {
            writer.writeNext(row);
        }
        if (transferObject.isToBeContinued()) {
            transferObject.getRows().clear();
            recursivelyWriteToStream(writer, transferObject.getFilterParameters(), transferObject);
        }
    }

    protected abstract CSVTransferObject buildCSV(CSVTransferObject transferObject, Object dataClass);

    /**
     * Set CSV Meta Data
     *
     * @param response
     */
    protected void buildMetadataStepFirst(HttpServletResponse response, Object dataClass) {
        try {
            String fileName = "";
            if (getFileName() != null) {
                fileName = getFileName();
            } else if (getDinamicFilename(dataClass) != null) {
                fileName = getDinamicFilename(dataClass);
            }

            if (fileName.contains(" ")) {
                fileName = fileName.replace(" ", "");
            }
            if (fileName.contains("/")) {
                fileName = fileName.replace("\\/", "_");
            }
            fileName = ServerUtils.normalizeFileNameT(fileName);
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".csv");
            response.setHeader("Content-Type", "text/html; charset=utf-8");
            response.setContentType("application/csv");
            response.setCharacterEncoding("UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Set CSV Meta Data
     *
     * @param response
     */
    protected void buildMetadataStepSecond(HttpServletResponse response, int contentLength) {
        try {
            response.setContentLength(contentLength);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    abstract String getFileName();

    String getDinamicFilename(Object ob) {
        return null;
    }

    /**
     * You can rewrite this method
     * if you want to parse request to take some parametrs.
     *
     * @param request
     * @return return true if you want PostPDFHandler to parse and bind your request.
     */
    protected Object prepareRequest(HttpServletRequest request) {
        Map filterMap = request.getParameterMap();
        ListingFilterParameter fp = new ListingFilterParameter();
        HashMap<String, String> paramsMap = fp.getRequestParams();

        for (Map map : (Iterable<Map>) filterMap.entrySet()) {
            Map.Entry entry = (Map.Entry) map;
            if (paramsMap.containsKey(entry.getKey())) {
                String[] value = (String[]) entry.getValue();
                paramsMap.put((String) entry.getKey(), value[0]);
            }
        }
        fp.setRequestParams(paramsMap);
        return fp;
    }
}
