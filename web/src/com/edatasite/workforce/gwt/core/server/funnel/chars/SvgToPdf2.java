/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */
package com.edatasite.workforce.gwt.core.server.funnel.chars;

import com.lowagie.text.DocumentException;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.svg.SVGDocument;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class SvgToPdf2 {

    /**
     * The resulting PDF.
     */
    public static final String RESULT = "c:/test.jpg";
    /**
     * The map (shapes).
     */
    public static final String CITY = "resources/xml/svgtest.svg";

    /**
     * The SVG document factory.
     */
    protected SAXSVGDocumentFactory factory;
    /**
     * The SVG bridge context.
     */
    protected BridgeContext ctx;
    /**
     * The GVT builder
     */
    protected GVTBuilder builder;

    /**
     * Creates an SvgToPdf object.
     */
    public SvgToPdf2() {
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        factory = new SAXSVGDocumentFactory(parser);

        UserAgent userAgent = new UserAgentAdapter();
        DocumentLoader loader = new DocumentLoader(userAgent);
        ctx = new BridgeContext(userAgent, loader);
        builder = new GVTBuilder();
    }

    /**
     * Draws an SVG file to a PdfTemplate.
     *
     * @param resource the SVG content.
     * @throws java.io.IOException
     */
    public void drawSvg(String resource) throws IOException {

        FunnelGenerator gen = new FunnelGenerator(450, 450);
        gen.fillTemp();

        String text = gen.render(ChartRenderType.Image);

        InputStream is = new ByteArrayInputStream(text.getBytes());

        BufferedImage image = new BufferedImage(450, 450, BufferedImage.TYPE_3BYTE_BGR);

        Graphics2D g2d = image.createGraphics();

        SVGDocument city = factory.createSVGDocument(new File("temp.temp").toURI().toString(), is);
        GraphicsNode mapGraphics = builder.build(ctx, city);
        mapGraphics.paint(g2d);
        g2d.dispose();

        File file = new File(RESULT);
        ImageIO.write(image, "jpg", file);
        System.out.println(file.getAbsolutePath());


    }

    /**
     * Creates a PDF document.
     *
     * @param filename the path to the new PDF document
     * @throws com.lowagie.text.DocumentException
     *
     * @throws java.io.IOException
     */
    public void createPdf(String filename) throws IOException, DocumentException {
        drawSvg(CITY);
    }

    /**
     * Main method.
     *
     * @param args no arguments needed
     * @throws com.lowagie.text.DocumentException
     *
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException, DocumentException {
        new SvgToPdf2().createPdf(RESULT);
    }
}
