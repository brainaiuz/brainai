package com.edatasite.workforce.gwt.accounting.server.app.efiling.irmark;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.xml.security.Init;
import org.apache.xml.security.signature.XMLSignatureInput;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Base64;
import org.w3c.dom.Document;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod Makhmudov
 * Date: 12/12/11
 * Time: 9:27 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class MarkCalculator {
    public static final String DEFAULT_SEC_HASH_ALGORITHM = "SHA";

    public String createMark(InputStream in)
            throws Exception {
        return toBase64(getMarkBytes(in));
    }

    protected byte[] getMarkBytes(InputStream in) throws Exception {
        Init.init();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new ByteArrayInputStream(getAlgorithm().getBytes()));

        Transforms transforms = new Transforms(doc.getDocumentElement(), null);

        XMLSignatureInput input = new XMLSignatureInput(in);
        XMLSignatureInput result = transforms.performTransforms(input);

        MessageDigest md = MessageDigest.getInstance(DEFAULT_SEC_HASH_ALGORITHM);
        md.update(result.getBytes());

        return md.digest();
    }

    protected abstract String getAlgorithm();

    public static String toBase64(byte[] irMarkBytes) {
        return Base64.encode(irMarkBytes);
    }
}
