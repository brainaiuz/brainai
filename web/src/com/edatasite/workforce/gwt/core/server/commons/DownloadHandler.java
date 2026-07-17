package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//
//CommonService.getFileUrl()
@Transactional

public class DownloadHandler implements HttpRequestHandler, Constants {

    @Qualifier("uploadManager")
    @Autowired
    private UploadManager uploadManager;

    protected static final int BUFFER_SIZE = 1048 * 2;

    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id = request.getParameter("id");
        Integer uploadId = null;
        try {
            uploadId = Integer.parseInt(id);
        } catch (Throwable t) {
            doDefaultResponse(response);
            return;
        }
        String companyID = request.getParameter(C_ID);
        String dbName = request.getParameter("dbname");
        if (companyID != null && !"".equals(companyID)) {
            ServerSecurityContext.getInstance().setCompanyId(companyID);
            ServerSecurityContext.getInstance().setDatabase(dbName);
        } else {
            if (ServerSecurityContext.getInstance().getCompanyId() == null) {
                ServerSecurityContext.getInstance().setCompanyId(uploadManager.getUser().getCompany().getObjectID());
            }
        }
        EdsUpload upload = (EdsUpload) uploadManager.get(uploadId);

        InputStream result;


        if (upload != null) {
            result = uploadManager.getInputStream(upload);
            if (result == null) {
                doDefaultResponse(response);
                return;
            }
            response.setContentType(upload.getContentType());
            response.setContentLength(upload.getSize().intValue());
            response.setHeader("content-disposition", "inline; filename=\"" + upload.getOriginalName() + "\"");
            response.setHeader("Accept-Ranges", "bytes");

            String range = request.getHeader("Range");
            doSendContent(result, response, parseRangeStart(range));
        } else {
            doDefaultResponse(response);
        }
    }

    private void doSendContent(InputStream inputStream, HttpServletResponse response, int offset) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        OutputStream ois = response.getOutputStream();
        inputStream.skip(offset);
        int count = 0;
        while ((count = inputStream.read(buffer)) > 0) {
            ois.write(buffer, 0, count);
        }
    }

    private int parseRangeStart(String range) {
        if(StringUtils.isNotBlank(range)){
            return Integer.parseInt(range.substring(6).split("-")[0]);
        } else {
            return 0;
        }
    }

    protected void doDefaultResponse(HttpServletResponse response) throws IOException {
        response.setStatus(404);
    }

}
