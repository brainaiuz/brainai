package com.edatasite.workforce.components;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * User: Dilsh0d
 * Date: 12-May-2010
 * Time: 15:33:21
 */
public class ImageScaleDown {

    private String type;
    private byte[] bytes;
    private InputStream inputStream;

    public ImageScaleDown(InputStream inputStream, String type) {
        this.type = type;
        this.inputStream = inputStream;
    }

    public InputStream getImageScaleDownInputStream() {
        try {
            BufferedImage changeImg = ImageIO.read(inputStream);
            double width = changeImg.getWidth(), height = changeImg.getHeight();
            if (width < height) {
                double hDevided = height / 90;
                if (width / hDevided < 76) {
                    width = width / hDevided;
                    height = height / hDevided;
                } else {
                    hDevided = width / 75;
                    width = width / hDevided;
                    height = height / hDevided;
                }
            } else {
                double hDevided = width / 75;
                width = width / hDevided;
                height = height / hDevided;
            }
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            Image img = changeImg.getScaledInstance((int) width, (int) height, Image.SCALE_AREA_AVERAGING);
            BufferedImage r = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = r.createGraphics();
            g.drawImage(img, 0, 0, (int) width, (int) height, null);
            g.dispose();
            ImageIO.write(r, type, byteArray);
            bytes = byteArray.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bytes != null && bytes.length != 0) {
            return new ByteArrayInputStream(bytes);
        } else {
            return inputStream;
        }
    }

    public Object[] getAdvancedImageScaleDownInputStream(double maxWidth, double maxHeight) {
        double width = 0, height = 0;
        try {
            BufferedImage changeImg = ImageIO.read(inputStream);
            width = changeImg.getWidth();
            height = changeImg.getHeight();
            if (width <= maxWidth && height <= maxHeight) {

            } else if (width <= maxWidth && height > maxHeight) {
                double hDevided = height / maxHeight;
                height = maxHeight;
                width = width / hDevided;
            } else if (width > maxWidth && height <= maxHeight) {
                double wDevided = width / maxWidth;
                width = maxWidth;
                height = height / wDevided;

            } else if (width > maxWidth && height > maxHeight) {
                if (maxWidth < maxHeight) {
                    double wDevided = width / maxWidth;
                    width = maxWidth;
                    height = height / wDevided;
                } else {
                    double hDevided = height / maxHeight;
                    height = maxHeight;
                    width = width / hDevided;
                }
            }

            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            Image img = changeImg.getScaledInstance((int) width, (int) height, Image.SCALE_AREA_AVERAGING);
            int bufferedImageType = BufferedImage.TYPE_INT_ARGB;
            if (type.equals("jpeg") || type.equals("jpg")) {
                bufferedImageType = BufferedImage.TYPE_INT_RGB;
            }
            BufferedImage r = new BufferedImage((int) width, (int) height, bufferedImageType);
            Graphics2D g = r.createGraphics();
            g.setComposite(AlphaComposite.Src);
            g.drawImage(img, 0, 0, (int) width, (int) height, null);
            g.dispose();
            ImageIO.write(r, type, byteArray);
            bytes = byteArray.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Object[] result = new Object[3];
        result[1] = String.valueOf(Math.ceil(width)).replace(".0", "");
        result[2] = String.valueOf(Math.ceil(height)).replace(".0", "");
        if (bytes != null && bytes.length != 0) {
            result[0] = new ByteArrayInputStream(bytes);
            return result;
        } else {
            result[0] = inputStream;
            return result;
        }
    }

    public InputStream createResizedCopy(int scaledWidth, int scaledHeight, boolean preserveAlpha) {
        BufferedImage changeImg = null;
        try {
            changeImg = ImageIO.read(inputStream);
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            double width = changeImg.getWidth(), height = changeImg.getHeight();
            int imageType = preserveAlpha ? BufferedImage.SCALE_FAST : BufferedImage.SCALE_DEFAULT;
            BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
            Graphics2D g = scaledBI.createGraphics();
            if (preserveAlpha) {
                g.setComposite(AlphaComposite.Src);
            }
            g.drawImage(changeImg, 0, 0, scaledWidth, scaledHeight, null);
            g.dispose();
            ImageIO.write(scaledBI, type, byteArray);
            bytes = byteArray.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bytes != null && bytes.length != 0) {
            return new ByteArrayInputStream(bytes);
        } else {
            return inputStream;
        }
    }
}
