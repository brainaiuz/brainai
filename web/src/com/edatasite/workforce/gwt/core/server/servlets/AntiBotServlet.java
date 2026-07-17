package com.edatasite.workforce.gwt.core.server.servlets;

import com.edatasite.shared.components.EncryptionHelper;
import org.jfree.chart.ChartUtilities;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class AntiBotServlet extends HttpServlet {
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {
        String encrypted = request.getParameter("id");
        String original = EncryptionHelper.decrypt(encrypted);
        int width = 67;
        int height = 28;
        int y = 14;

        // Array of difference colors for CAPTCHA
        int[][] botColors = {{0xc0, 0x15, 0x11}, {0x05, 0x11, 0xaf}, {0x10, 0x56, 0x0b}, {0x0a, 0x9f, 0xa8}, {0x0a, 0x63, 0xa8}, {0x0a, 0x20, 0xa8}, {0x91, 0x0c, 0xc9}, {0xc9, 0x0c, 0xc7}, {0xc9, 0x0c, 0x77}, {0x57, 0x86, 0x9c}};
        // Array of difference font sizes for CAPTCHA
        int[] botFontSizes = {14, 15, 16, 17};
        // Array of difference rotations for CAPTCHA
        float[] botRotations = {0.1f, 0.2f, 0.23f, 0.22f};
        int bColor;
        int bFontSize;
        float bRotation;
        boolean neg = false;
        float old = 0;

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = (Graphics2D) img.getGraphics();
        // Select colors for random lines
        float[] hsb = Color.RGBtoHSB(0xc5, 0xdb, 0xdf, null);
        g.setColor(Color.getHSBColor(hsb[0], hsb[1], hsb[2]));
        // Draw random lines on background
        for (int i = 0; i < 20; i++) {
            g.drawLine((int) Math.round(Math.random() * 66), (int) Math.round(Math.random() * 27), (int) Math.round(Math.random() * 66), (int) Math.round(Math.random() * 27));
        }
        // Select color for border
        hsb = Color.RGBtoHSB(127, 157, 185, null);
        g.setColor(Color.getHSBColor(hsb[0], hsb[1], hsb[2]));
        // Draw border
        g.drawRect(0, 0, 66, 27);
//        g.setColor(Color.GRAY);


        Font font;
        TextLayout textLayout;
        AffineTransform at = new AffineTransform();
        /*
        at.setToRotation(0.2);
        g.transform(at);        
        */
        // Setup rendering method for draw chars with antialiasing
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Main cycle for draw CAPTCHA chars
        for (int i = 0; i < original.length(); i++) {
            // Select random color
            bColor = (int) Math.round(Math.random() * 9);
            // Select random font size
            bFontSize = botFontSizes[(int) Math.round((Math.random()) * 3)];
            // Select random rotation
            bRotation = botRotations[(int) Math.round((Math.random()) * 3)];

            // To control the direction of the rotation
            if (bRotation > 0) {
                if (!neg) {
                    neg = true;
                    bRotation = -old;
                } else {
                    neg = false;
                }
            } else {
                if (neg) {
                    neg = false;
                    bRotation = -old;
                } else {
                    neg = true;
                }
            }
            old = bRotation; // Old rotation

            // Select color for the next symbol CAPTCHA
            hsb = Color.RGBtoHSB(botColors[bColor][0], botColors[bColor][1], botColors[bColor][2], null);
            g.setColor(Color.getHSBColor(hsb[0], hsb[1], hsb[2]));
            font = new Font("Arial", Font.BOLD, bFontSize);
            at.setToRotation(bRotation);
            g.transform(at);

            textLayout = new TextLayout(original.substring(i, i + 1), font, g.getFontRenderContext());
            textLayout.draw(g, 5 + i * 12, y);
        }
        //g.drawString(original, 5, 21);
        if (img != null) {
            response.setContentType("image/png");
            ChartUtilities.writeBufferedImageAsPNG(response.getOutputStream(), img);
        }
    }

}

