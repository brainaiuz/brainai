package com.edatasite.workforce.gwt.core.server.servlets.pdf.template;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.w3c.dom.Element;
import org.xhtmlrenderer.css.parser.FSColor;
import org.xhtmlrenderer.css.parser.FSRGBColor;
import org.xhtmlrenderer.css.style.CalculatedStyle;
import org.xhtmlrenderer.css.style.derived.RectPropertySet;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.pdf.ITextFSFont;
import org.xhtmlrenderer.pdf.ITextOutputDevice;
import org.xhtmlrenderer.pdf.ITextReplacedElement;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.render.PageBox;
import org.xhtmlrenderer.render.RenderingContext;

import java.awt.*;


/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 05.04.13
 * Time: 17:57
 * To change this template use File | Settings | File Templates.
 */
public class RTLText implements ITextReplacedElement {
    private static final Logger logger = LoggerFactory.getLogger(RTLText.class.getName());

    private int width;
    private int height;
    private String text;
    private int align;

    private Color color;
    private float fontSize = -1;
    private int direction = PdfWriter.RUN_DIRECTION_LTR;
    private boolean isHeaderRTLText = false;

    public RTLText(LayoutContext c, BlockBox box, UserAgentCallback uac, int cssWidth, int cssHeight, String text) {
        this(c, box, uac, cssWidth, cssHeight, text, false);
    }

    public RTLText(LayoutContext c, BlockBox box, UserAgentCallback uac, int cssWidth, int cssHeight, String text, boolean isHeaderRTLText) {
        this.text = text;
        this.isHeaderRTLText = isHeaderRTLText;
        initDimensions(c, box, cssWidth, cssHeight);
        float fontSize = box.getStyle().getFSFont(c).getSize2D();

        align = com.lowagie.text.Element.ALIGN_LEFT;
        Element element = box.getElement();
        String as = element.getAttribute("align");
        {
            if (as.equalsIgnoreCase("left")) {
                align = com.lowagie.text.Element.ALIGN_LEFT;
            } else if (as.equalsIgnoreCase("center")) {
                align = com.lowagie.text.Element.ALIGN_CENTER;
            } else if (as.equalsIgnoreCase("right")) {
                align = com.lowagie.text.Element.ALIGN_RIGHT;
            }
        }
        as = element.getAttribute("direction");
        {
            if (as.equalsIgnoreCase("ltr")) {
                direction = PdfWriter.RUN_DIRECTION_LTR;
            } else if (as.equals("default")) {
                direction = PdfWriter.RUN_DIRECTION_DEFAULT;
            } else if (as.equals("no-bidi")) {
                direction = PdfWriter.RUN_DIRECTION_NO_BIDI;
            } else if (as.equals("rtl")) {
                direction = PdfWriter.RUN_DIRECTION_RTL;
            }
        }

        String ecolor = element.getAttribute("color");
        if (!ecolor.isEmpty()) {
//            color = HtmlUtilities.decodeColor(ecolor);
            color = Color.decode(ecolor);
        }

        String efontSize = element.getAttribute("font-size");
        if (!efontSize.isEmpty()) {
            this.fontSize = Float.parseFloat(efontSize);
        }
    }

    @Override
    public int getIntrinsicWidth() {
        return width;
    }

    @Override
    public int getIntrinsicHeight() {
        return height;
    }

    private Point location = new Point();

    @Override
    public Point getLocation() {
        return location;
    }

    @Override
    public void setLocation(int x, int y) {
        location.x = x;
        location.y = y;
    }

    @Override
    public void detach(LayoutContext c) {
    }

    @Override
    public boolean isRequiresInteractivePaint() {
        return false;
    }

    @Override
    public boolean hasBaseline() {
        return false;
    }

    @Override
    public int getBaseline() {
        return 0;
    }

    @Override
    public void paint(RenderingContext c, ITextOutputDevice outputDevice, BlockBox box) {
        try {
            PdfWriter writer = outputDevice.getWriter();
            PdfContentByte cb = writer.getDirectContent();

            ITextFSFont font = (ITextFSFont) box.getStyle().getFSFont(c);
            float pdfFontSize =
                    outputDevice.getDeviceLength(font.getSize2D());
            if (fontSize != -1) {
                pdfFontSize = fontSize;
            }
            FSColor color = box.getStyle().getColor();
            Color bc = null;
            if (this.color != null) {
                bc = this.color;
            } else if (color instanceof FSRGBColor) {
                FSRGBColor cc = (FSRGBColor) color;
                bc = new Color(cc.getRed(), cc.getGreen(), cc.getBlue());
            }
            ColumnText ct = new ColumnText(cb);
            setupColumnCoordinates(c, outputDevice, box);
            ct.setSimpleColumn(llx, lly, urx, ury);
            ct.setSpaceCharRatio(PdfWriter.NO_SPACE_CHAR_RATIO);
            ct.setLeading(0, 1);
            ct.setRunDirection(direction);
            ct.setAlignment(align);

            if (bc == null) {
                ct.addText(new Phrase(text, new Font(font.getFontDescription().getFont(), pdfFontSize)));
            } else {
                ct.addText(new Phrase(text, new Font(font.getFontDescription().getFont(), pdfFontSize, 0, new Color(bc.getRed(), bc.getGreen(), bc.getBlue()))));
            }
            ct.go();

        } catch (DocumentException e) {
            logger.warn("error while processing rtl text", e);
            e.printStackTrace();
        }
    }

    private int llx, lly, urx, ury;

    private void setupColumnCoordinates(RenderingContext c, ITextOutputDevice outputDevice, BlockBox box) {
        PageBox page = c.getPage();
        float dotsPerPoint = outputDevice.getDotsPerPoint();
        float marginBorderPaddingLeft = page.getMarginBorderPadding(c, CalculatedStyle.LEFT);
        float marginBorderPaddingBottom = page.getMarginBorderPadding(c, CalculatedStyle.BOTTOM);

        RectPropertySet margin = box.getMargin(c);
        RectPropertySet padding = box.getPadding(c);

        float dist = (page.getBottom() - box.getAbsY() +
                marginBorderPaddingBottom); //from box top to page bottom

        llx = (int) ((margin.left() + padding.left() + box.getAbsX() + marginBorderPaddingLeft) / dotsPerPoint);
        if (isHeaderRTLText) {
            lly = (int) ((dist - box.getHeight()) / dotsPerPoint) + 133;
        } else {
            lly = (int) ((dist - box.getHeight()) / dotsPerPoint);
        }

        urx = (int) ((box.getAbsX() + box.getWidth() + marginBorderPaddingLeft) / dotsPerPoint);
        if (isHeaderRTLText) {
            ury = (int) ((dist /*+ margin.bottom() + padding.bottom()*/) / dotsPerPoint) + 133;
        } else {
            ury = (int) ((dist /*+ margin.bottom() + padding.bottom()*/) / dotsPerPoint);
        }
    }

    protected void initDimensions(LayoutContext c, BlockBox box, int cssWidth, int cssHeight) {

        CalculatedStyle style = box.getStyle();

        Element element = box.getElement();
        float scalex = 0.1f;
        float scaley = 0.06f;
        int lines = 1;
        {
            String lines1 = element.getAttribute("lines");
            if (!lines1.isEmpty()) {
                lines = Integer.parseInt(lines1);
            }
        }

        String sx = element.getAttribute("scale-x");
        if (!sx.isEmpty()) {
            try {
                scalex = Float.parseFloat(sx);
            } catch (Exception e) {
                System.err.println("Bad scale-x attribute value: " +
                        sx);
                // do nothing
            }
        }

        String sy = element.getAttribute("scale-y");
        if (!sy.isEmpty()) {
            try {
                scaley = Float.parseFloat(sy);
            } catch (Exception e) {
                System.err.println("Bad scale-y attribute value: " +
                        sx);
                // do nothing
            }
        }
        String ewidth = element.getAttribute("width");
        if (!ewidth.isEmpty()) {
            width = Integer.parseInt(ewidth) * c.getDotsPerPixel();
        } else if (cssWidth != -1) {
            width = cssWidth;
        } else {
            width = (c.getTextRenderer().getWidth(
                    c.getFontContext(),
                    style.getFSFont(c),
                    text) / 2);
        }

        String eheight = element.getAttribute("height");
        if (!eheight.isEmpty()) {
            height = Integer.parseInt(eheight) * c.getDotsPerPixel();
        } else if (cssHeight != -1) {
            height = cssHeight;
        } else {
            height = ((int) (style.getLineHeight(c) * lines));
        }

        width *= c.getDotsPerPixel() * scalex;
        height *= c.getDotsPerPixel() * scaley;
    }
}
