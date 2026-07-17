package com.edatasite.workforce.rest.v2.release10.core;

import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * Created by Anvar Akramov on 1/10/18.
 */
public class APIMultipartResolver extends CommonsMultipartResolver {
    @Override
    public boolean isMultipart(HttpServletRequest request) {
        if (!Arrays.asList("patch", "post", "put").contains(request.getMethod().toLowerCase())) {
            return false;
        } else {
            return FileUploadBase.isMultipartContent(new ServletRequestContext(request));
        }
    }
}
