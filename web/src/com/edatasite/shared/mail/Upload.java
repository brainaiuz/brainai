package com.edatasite.shared.mail;

import com.google.gwt.user.client.rpc.IsSerializable;
import jakarta.activation.DataHandler;

import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: May 28, 2010
 * Time: 5:20:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class Upload implements IsSerializable {

    private String fileName;
    private String contentType;
    private InputStream inputStream;
    private DataHandler dataHandler;

    public DataHandler getDataHandler() {
        return dataHandler;
    }

    public void setDataHandler(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
