package com.edatasite.workforce.gwt.ganttchart.server;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class HtmlToImage {

    protected static File generateOutput() throws Exception {
        // Create a temporary output file for the PNG image.
        File outputFile = new File("Reporte.png");
        outputFile.deleteOnExit();

        JEditorPane pane = new JEditorPane();
        pane.setContentType("text/html");
        pane.setPage("http://www.google.com");
        final JFrame frame = new JFrame();
        frame.pack();

        // Time Delay for the correct loading of the file.
        try {
            Thread.sleep(5000);
        } catch (NumberFormatException ignored) {
        }

        frame.add(pane);
        frame.pack();

        Dimension prefSize = pane.getPreferredSize();
        pane.setSize(prefSize);

        BufferedImage img = new BufferedImage(prefSize.width, prefSize.height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) img.getGraphics();

        SwingUtilities.paintComponent(g, pane, frame, 0, 0, prefSize.width, prefSize.height);

        ImageIO.write(img, "png", outputFile);

        return outputFile;
    }

    public static void main(String[] args) {
        try {
            generateOutput();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
