package com.edatasite.workforce.gwt.core.server.servlets.pdf.template;

import org.w3c.dom.Element;
import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.pdf.ITextOutputDevice;
import org.xhtmlrenderer.pdf.ITextReplacedElementFactory;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.simple.extend.FormSubmissionListener;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 05.04.13
 * Time: 17:51
 * To change this template use File | Settings | File Templates.
 */
public class RTLTextReplacedElementFactory implements ReplacedElementFactory {

    private String cssClassName;
    private String cssHeaderClassName;

    private ITextReplacedElementFactory defaultFactory;

    public RTLTextReplacedElementFactory(ITextOutputDevice outputDevice, String cssClassName) {
        defaultFactory = new ITextReplacedElementFactory(outputDevice);
        String[] sptlitCssName = cssClassName.split(";");
        this.cssClassName = sptlitCssName[0];
        this.cssHeaderClassName = sptlitCssName[1];
    }

    @Override
    public ReplacedElement createReplacedElement(LayoutContext c, BlockBox box, UserAgentCallback uac, int cssWidth, int cssHeight) {
        Element element = box.getElement();
        if (element == null) {
            return null;
        }
        if (!"".equals(cssClassName) && (element.getAttribute("class").contains(cssClassName) || element.getAttribute("class").contains(cssHeaderClassName))) {
            String text = element.getTextContent().replaceAll("(?m)\\s+ ", "");
            if (element.getAttribute("class").contains(cssClassName)) {
                return new RTLText(c, box, uac, cssWidth, cssHeight, text);
            } else {
                return new RTLText(c, box, uac, cssWidth, cssHeight, text, true);
            }
        } else {
            return defaultFactory.createReplacedElement(c, box, uac, cssWidth, cssHeight);
        }
    }


    @Override
    public void reset() {
    }

    @Override
    public void remove(Element e) {
    }

    @Override
    public void setFormSubmissionListener(FormSubmissionListener listener) {
    }
}
