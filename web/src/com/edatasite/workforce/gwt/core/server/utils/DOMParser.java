package com.edatasite.workforce.gwt.core.server.utils;

import org.apache.tools.ant.filters.StringInputStream;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Mansur
 * Date: 09.06.2009
 * Time: 16:35:16
 * To change this template use File | Settings | File Templates.
 */

public class DOMParser {
    private Document doc = null;

    public DOMParser(String xmlString) {
        try {
            doc = parserXML(new StringInputStream(xmlString));

            visit(doc, 0);
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public DOMParser(File file) {
        try {
            doc = parserXML(file);

            visit(doc, 0);
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public void visit(Node node, int level) {
        NodeList nl = node.getChildNodes();

        for (int i = 0, cnt = nl.getLength(); i < cnt; i++) {
            System.out.println("[" + nl.item(i) + "]");

            visit(nl.item(i), level + 1);
        }
    }

    public Document parserXML(File file) throws SAXException, IOException, ParserConfigurationException {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
    }

    public Document parserXML(InputStream inputStream) throws SAXException, IOException, ParserConfigurationException {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
    }

}
