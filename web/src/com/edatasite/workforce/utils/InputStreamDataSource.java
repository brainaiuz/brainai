package com.edatasite.workforce.utils;

import jakarta.activation.DataSource;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 26.03.2008
 * Time: 10:17:05
 * To change this template use File | Settings | File Templates.
 */
public class InputStreamDataSource implements DataSource {

    private String name;
    private String contentType;
    private ByteArrayOutputStream baos;

    public InputStreamDataSource(String name, String contentType, InputStream inputStream) throws IOException {
        this.name = name;
        this.contentType = contentType;

        baos = new ByteArrayOutputStream();

        int read;
        byte[] buff = new byte[256];
        while ((read = inputStream.read(buff)) != -1) {
            baos.write(buff, 0, read);
        }
    }

    public String getContentType() {
        return contentType;
    }

    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(baos.toByteArray());
    }

    public String getName() {
        return name;
    }

    public OutputStream getOutputStream() throws IOException {
        throw new IOException("Cannot write to this read-only resource");
    }
}
