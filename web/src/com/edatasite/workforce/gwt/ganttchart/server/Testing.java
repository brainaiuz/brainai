package com.edatasite.workforce.scheduler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 11/28/12
 * Time: 4:29 PM
 * To change this template use File | Settings | File Templates.
 */

public class Testing {
    public static void main(String[] args) {
        Robot r= null;
        try {
            r = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle rct=new Rectangle(0,0,d.width,d.height);
        BufferedImage bimg=r.createScreenCapture(rct);
        try {
            ImageIO.write(bimg, "jpeg", new File("D:/a.jpg"));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
