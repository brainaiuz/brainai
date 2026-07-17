package com.edatasite.workforce.gwt.documents.server;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.google.gson.Gson;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GwtUploadServlet extends HttpServlet implements CommandConstants {
    public static String realPath;

    protected UploadedFile saveMultipartFile(FileItem item, HttpServletRequest request) throws Exception {
        final String filename = URLDecoder.decode(item.getName(), StandardCharsets.UTF_8);
        File file = new File(realPath + request.getParameter("uuid") + "_upld_" + filename.substring(filename.replace("\\", "/").lastIndexOf("/") + 1));
        file.getParentFile().mkdirs();
        item.write(file);
        return new UploadedFile(file.getName(), (int) item.getSize(), item.getContentType(), file.getName());
    }

    protected UploadedFile saveXhrFile(InputStream is, HttpServletRequest request) throws IOException {
        String filename = URLDecoder.decode(request.getHeader("X-File-Name"), StandardCharsets.UTF_8);
        String storage = URLDecoder.decode(request.getHeader(UPLOAD_TYPE_PARAM_NAME), StandardCharsets.UTF_8);
        String url = realPath + filename;
        final File file = new File(url);
        file.getParentFile().mkdirs();
        try (FileOutputStream os = new FileOutputStream(file)) {
            IOUtils.copy(is, os);
            is.close();
            os.flush();
        }

        return new UploadedFile(filename, (int) file.length(), storage, url);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        realPath = getServletContext().getRealPath("uploads") + "/";
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setAllowedHeaders(resp);
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Content-Type", "text/html");
        log("CONTENT TYPE: " + req.getContentType());

        try (final PrintWriter writer = resp.getWriter()) {
            if (req.getContentType().equals("application/octet-stream")) {
                log("->SAVE OCTET STREAM");
                try {
                    List<UploadedFile> files = new ArrayList<>();
                    saveOctetStream(req, resp, files);

                    String strJson = new Gson().toJson(files);
                    log("->LENGTH JSON: " + strJson.length());
                    writer.write(strJson);
                    writer.flush();
                    log("->WRITED FILE END!!!");
                } catch (Exception e) {
                    log("EXCEPTION: " + e.getMessage());
                    e.printStackTrace();

                }
            } else if (!ServletFileUpload.isMultipartContent(req)) {
                throw new IllegalArgumentException("Request is not multipart, please 'multipart/form-data' enctype for your form.");
            } else {
                ServletFileUpload sfu = new ServletFileUpload(new DiskFileItemFactory());
                try {
                    List<FileItem> items = sfu.parseRequest(req);
                    List<UploadedFile> files = new ArrayList<>();
                    for (FileItem item : items) {
                        if (!item.isFormField()) {
                            files.add(saveMultipartFile(item, req));
                        }
                    }
                    writer.write(new Gson().toJson(files));
                    writer.flush();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveOctetStream(HttpServletRequest request, HttpServletResponse response, List<UploadedFile> files) {
        InputStream is = null;
        log("-> START -- SAVE OCTET STREAM --");
        try {
            is = request.getInputStream();
            files.add(saveXhrFile(is, request));
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            System.out.println(GwtUploadServlet.class.getName() + "has thrown an exception: " + ex.getMessage());
            log(GwtUploadServlet.class.getName() + "has thrown an exception: " + ex.getMessage());
            ex.printStackTrace();
        } catch (Exception ex) {
            System.out.println(GwtUploadServlet.class.getName() + "has thrown an exception: " + ex.getMessage());
            log(GwtUploadServlet.class.getName() + "has thrown an exception: " + ex.getMessage());
            ex.printStackTrace();
        }
        log("-> END -- SAVE OCTET STREAM --");
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setAllowedHeaders(resp);
        resp.getWriter().write("`GET` is not supported");
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setAllowedHeaders(resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }

    private void setAllowedHeaders(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Headers", "X-File-Name,X-File-Type,X-File-Size,X-Requested-With,Content-Type");
    }

    private static class UploadedFile {
        private String name;
        private int size;
        private String type;
        private String url;

        public UploadedFile(String name, int size, String type, String url) {
            this.name = name;
            this.size = size;
            this.type = type;
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public int getSize() {
            return size;
        }

        public String getType() {
            return type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
