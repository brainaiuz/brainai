package com.edatasite.workforce.gwt.core.server.servlets;

import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.gwt.core.server.db.impl.UploadManagerImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class UploadServlet extends HttpServlet {

    private UploadManagerImpl<EdsUpload> uploadManager = new UploadManagerImpl<>();
    protected static final int BUFFER_SIZE = 1024 * 2;

    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String id = httpServletRequest.getParameter("id");
        Integer uploadId = null;
        try {
            uploadId = Integer.parseInt(id);
        } catch (Throwable t) {
            doDefaultResponse(httpServletResponse);
            return;
        }
        EdsUpload upload = uploadManager.get(uploadId);
        InputStream result = uploadManager.getInputStream(upload);
        if (result == null) {
            doDefaultResponse(httpServletResponse);
            return;
        }
        httpServletResponse.setContentType(upload.getContentType());
        httpServletResponse.setHeader("content-disposition", "attachment; filename=\"" + upload.getOriginalName() + "\"");
        doSendContent(result, httpServletResponse);
    }

    private void doSendContent(InputStream inputStream, HttpServletResponse response) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        OutputStream ois = response.getOutputStream();
        int count = 0;
        while ((count = inputStream.read(buffer)) > 0) {
            ois.write(buffer, 0, count);
        }
    }

    protected void doDefaultResponse(HttpServletResponse response) throws IOException {
        response.setStatus(404);
    }

}
