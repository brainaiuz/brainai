package com.edatasite.shared.xml;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;

/**
 * Created by IntelliJ IDEA.
 * User: Iskandar
 * Date: 08-Aug-2007
 * Time: 19:50:02
 * To change this template use File | Settings | File Templates.
 */


public class EdsErrorListener implements ErrorListener {

    private TransformerException exception;

    public void warning(TransformerException exception) throws TransformerException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void error(TransformerException exception) throws TransformerException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void fatalError(TransformerException exception) throws TransformerException {
        this.exception = exception;
        throw exception;
    }

}
