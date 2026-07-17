package com.edatasite.workforce.gwt.core.server.commons;

import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Dec 10, 2009
 * Time: 5:15:44 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ExcelBAOSHandler {

    ByteArrayOutputStream getExcelStream(Object object);

}
