package com.edatasite.workforce.gwt.accounting.server.app.efiling.irmark;

import org.apache.tools.ant.filters.StringInputStream;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod Makhmudov
 * Date: 12/12/11
 * Time: 9:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class HMRCMarkCalculator extends MarkCalculator{
    public String getAlgorithm() {
        return "<?xml version='1.0'?>\n<dsig:Transforms xmlns:dsig='http://www.w3.org/2000/09/xmldsig#' xmlns:soap='http://www.w3.org/2003/05/soap-envelope'>\n<dsig:Transform Algorithm='http://www.w3.org/TR/1999/REC-xpath-19991116'>\n<dsig:XPath>\ncount(ancestor-or-self::node()|/soap:Envelope/soap:Body)=count(ancestor-or-self::node())\n</dsig:XPath>\n</dsig:Transform>\n<dsig:Transform Algorithm='http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments'/>\n</dsig:Transforms>\n";
    }

    /**
     * Generate and print the mark.
     *
     * @throws Exception
     */
    public String generateIRMark(String xmlData) throws Exception {
        InputStream is = new StringInputStream(xmlData, "UTF-8");
        String irMark = createMark(is);
        System.out.println("IRMark --> " + irMark);

        return irMark;
    }
}
