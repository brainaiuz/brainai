package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 07.11.2008
 * Time: 13:23:57
 * To change this template use File | Settings | File Templates.
 */
public interface IPostPDFHandler {

    ByteArrayOutputStream getPDFStream(Object object);

    String velocityReplaceContentAttributes(Object object);

    String getFileName();

}
