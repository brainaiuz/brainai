package com.edatasite.workforce.gwt.core.server.rpc;

import org.mozilla.universalchardet.UniversalDetector;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Created with IntelliJ IDEA.
 * User: Hayot
 * Date: 1/31/14
 * Time: 4:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class FindEncodeInputStream extends InputStream {
    private InputStream is;
    private byte[] sampleData = new byte[8192];
    private int sampleLen;
    private int sampleIndex = 0;

    public FindEncodeInputStream(InputStream is) {
        this.is = is;
        // pre-read the data
        try {
            sampleLen = is.read(sampleData);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public InputStream getIs() {
        return is;
    }

    public void setIs(InputStream is) {
        this.is = is;
    }

    public Charset getCharset() {
        // detect the charset
        UniversalDetector detector = new UniversalDetector(null);
        detector.handleData(sampleData, 0, sampleLen);
        detector.dataEnd();
        String encoding = detector.getDetectedCharset();
        if (encoding != null) {
            System.out.println("Detected encoding = " + encoding);
            return Charset.forName(detector.getDetectedCharset());
        }
        System.out.println("No encoding detected.");
        return Charset.defaultCharset();
    }

    @Override
    public int read() throws IOException {
        return is.read();
    }

}
