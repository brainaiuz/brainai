package com.edatasite.workforce.gwt.core.server.commons;


import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.utils.EdsContextParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;


//
//CommonService.getFileUrl()
@Transactional

/**
 * Created by Omonullo on 07.04.16.
 */
public class DownloadLocalHandler implements HttpRequestHandler, Constants{
    @Qualifier("userManager")
    @Autowired
    UserManager userManager;

    protected static final int BUFFER_SIZE = 1024 * 2;

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fileName = request.getParameter("file");
        if(fileName != null && fileName != ""){
            String uploadDir = EdsContextParams.getUploadDir() != null ? EdsContextParams.getUploadDir() : "";
            String location =  uploadDir + "/backups/" + userManager.getUser().getCompany().getObjectID() + "/" + fileName;
            File file = new File(location);
            if (file != null && file.exists()) {
                InputStream is = new FileInputStream(file);
                response.setHeader("content-disposition", "attachment; filename=\"" + file.getName() + "\"");
                doSendContent(is, response);
            } else {
                doDefaultResponse(response);
            }
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
