package com.edatasite.workforce.gwt.core.server.app;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serial;

public class ImageLoader extends JPanel {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 1L;

    private BufferedImage image;
    private int imageWidth;
    private int imageHeight;

    public ImageLoader(String imageUrl) throws IOException {
        super();
        loadImage(imageUrl, null);
    }

    public ImageLoader(InputStream input) throws IOException {
        super();
        loadImage(null, input);
    }

    public void loadImage(String imageUrl, InputStream input) throws IOException {
        if (input == null) {
            image = ImageIO.read(new File(imageUrl));
        } else {
            image = ImageIO.read(input);
        }

        setImageWidth(image.getWidth(this));
        setImageHeight(image.getHeight(this));
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }
}
