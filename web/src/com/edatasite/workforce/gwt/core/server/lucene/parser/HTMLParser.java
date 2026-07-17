package com.edatasite.workforce.gwt.core.server.lucene.parser;

import org.apache.html.dom.HTMLDocumentImpl;
import org.cyberneko.html.parsers.DOMFragmentParser;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLDocument;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;


public class HTMLParser {
    ArrayList messages;

    public String performParse(String htmlFragment) throws SAXException, IOException {
        messages = new ArrayList();
        DOMFragmentParser parser = new DOMFragmentParser();
        HTMLDocument document = new HTMLDocumentImpl();
        StringReader sr = new StringReader(htmlFragment);
        DocumentFragment fragment = document.createDocumentFragment();
        InputSource ins = new InputSource(sr);
        parser.parse(ins, fragment);
        print(fragment, "");
        StringBuilder returnResult = new StringBuilder();
        for (Object message : messages) {
            if (message != null) {
                returnResult.append((String) message);
            }
        }
//            String returnResult = this.messages.toString().substring(1,messages.toString().length()-1);
        return returnResult.toString();

    }

    private void print(Node node, String indent) {
        if (node.getFirstChild() == null) {
            messages.add(node.getTextContent());
        }
        Node child = node.getFirstChild();
        while (child != null) {
            print(child, indent + " ");
            child = child.getNextSibling();
        }

    }


}
