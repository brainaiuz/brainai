package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
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

/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: Nov 22, 2009
 * Time: 2:11:20 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class DownloadSecureHandler implements HttpRequestHandler {

    @Qualifier("uploadManager")
    @Autowired
    private UploadManager uploadManager;

    protected static final int BUFFER_SIZE = 1024 * 2;

    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id = request.getParameter("id");
        id = EncryptionHelper.decrypt(EncryptionHelper.decodeURL(id));
        Integer uploadId = null;
        try {
            uploadId = Integer.parseInt(id);
        } catch (Throwable t) {
            doDefaultResponse(response);
            return;
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
            response.setHeader("content-disposition", "attachment; filename=\"" + upload.getOriginalName() + "\"");
            doSendContent(result, response);
        } else {
            doDefaultResponse(response);
        }
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
