package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.core.domain.emailfetching.EdsEmailAttachment;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.db.emailfetching.EmailAttachmentManager;
import com.edatasite.workforce.gwt.messagecenter.server.MessageCenterServiceLocal;
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

@Transactional
public class DownloadFromEmailHandler implements HttpRequestHandler, Constants {

    @Qualifier("uploadManager")
    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private EmailAttachmentManager emailAttachmentManager;
    @Autowired
    private MessageCenterServiceLocal messageCenterService;

    protected static final int BUFFER_SIZE = 1024 * 2;
    protected static final String INPUTSTREAM = "INPUTSTREAM";
    protected static final String CONTENTTYPE = "CONTENT_TYPE";

    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String encodedURL = request.getParameter("link").trim();
        Integer objectID = decrypt(encodedURL);
        System.out.println("********************************** " + encodedURL + " **********************************");
        if (objectID == null) {
            doDefaultResponse(response);
            return;
        }
        EdsEmailAttachment attachment = emailAttachmentManager.get(objectID);
        InputStream inputStream = messageCenterService.getInputStream(attachment);
        if (inputStream == null) {
            doDefaultResponse(response);
            return;
        }
        response.setContentType(attachment.getContentType());
        response.setHeader("content-disposition", "attachment; filename=\"" + (attachment.getFileName() == null ? "noname" : attachment.getFileName()) + "\"");
        doSendContent(inputStream, response);
    }

    private static Integer decrypt(String encodedURL) {
        String result = EncryptionHelper.decrypt(encodedURL);
        if (result != null && !"".equals(result)) {
            try {
                return Integer.valueOf(result);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return null;
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
