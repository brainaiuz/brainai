package com.edatasite.workforce.gwt.core.server.funnel.chars;

import com.edatasite.workforce.gwt.core.client.rpc.funnel.Color;
import com.edatasite.workforce.gwt.core.client.rpc.funnel.FunnelItem;
import com.edatasite.workforce.gwt.core.client.rpc.funnel.tags.PathBase;
import com.edatasite.workforce.gwt.core.client.rpc.funnel.tags.PathC;
import com.edatasite.workforce.gwt.core.client.rpc.funnel.tags.PathL;
import com.edatasite.workforce.gwt.core.client.rpc.funnel.tags.PathM;
import com.edatasite.workforce.gwt.core.client.rpc.funnel.tags.PathZ;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Lochin
 * Date: 7/29/12
 * Time: 7:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class FunnelGenerator {
    public static void main(String[] args) throws Exception {

        FunnelGenerator gen = new FunnelGenerator(450, 450);
        gen.fillTemp();
        System.out.println(gen.renderJS());

        ImageIO.write(gen.renderImage(), "jpg", new File("C:/test.jpg"));
    }

    public String renderJS() {
        return render(ChartRenderType.JS);
    }

    public void fillTemp() {
//        for (int i = 1; i < 5; i++) {
        add((Math.random() * 1000), "qwert");
        add((Math.random() * 1000), "asdgsdfsd");
        add((Math.random() * 1000), "safsadfsdfy");
        add((Math.random() * 1000), "098765redsack");
        add((Math.random() * 1000), "qwasdfghjklzcvbr");
        add((Math.random() * 1000), "lkjhgfdaxcvbnmpoiuy345678dfgh");
        add((Math.random() * 1000), "67yhj");
//        }
    }

    public FunnelGenerator(int width, int height) {
        this.width = width;
        this.height = height;
    }

    private double max = 0f;
    private double sum = 0f;

    private double width;
    private double height;

    public void add(double value, String title) {
        FunnelItem item = new FunnelItem(value, title + "(" + (int) value + ")");
        insert(item);
        if (max < value) {
            max = value;
        }
        sum += value;
    }

    private int insert(FunnelItem value) {
//        for (int i = 0; i < datas.size(); i++) {
//            FunnelItem item = datas.get(i);
//            if (value.getValue() == item.getValue()
//                    || value.getValue() > item.getValue()) {
//                datas.add(i, value);
//                return i;
//            }
//        }
        datas.add(value);
        return datas.size() - 1;
    }

    List<FunnelItem> datas = new ArrayList<>();

    public BufferedImage renderImage() throws IOException {
        //Подготовка объектов
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);
        UserAgent userAgent = new UserAgentAdapter();
        DocumentLoader loader = new DocumentLoader(userAgent);
        BridgeContext ctx = new BridgeContext(userAgent, loader);
        GVTBuilder builder = new GVTBuilder();

        //Создание картинки
        BufferedImage image = new BufferedImage((int) getWidth(), (int) getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        //Получение текста Html 5
        String text = render(ChartRenderType.Image);
        InputStream is = new ByteArrayInputStream(text.getBytes());
        SVGDocument city = factory.createSVGDocument(new File("temp.temp").toURI().toString(), is);

        //РИсование на полотно
        GraphicsNode mapGraphics = builder.build(ctx, city);
        mapGraphics.paint(g2d);
        g2d.dispose();
        return image;
    }

    public String render(ChartRenderType renderType) {

        double xr = .1d;
        double yr = .1d;

        double h = height;
        double w = width;
        double w2 = w / (datas.size());
        double sy = (w) / 2 * .9d * yr;// (w * (datas.get(0).getValue() / max) * yr);
        double sy2 = (w * .1d) / 2 * .9d * yr;//(w * (datas.get(datas.size() - 1).getValue() / max) * yr);
        h -= (sy + sy2);
        double mx = w / 2;

        double yy = -sy * .7d;

        for (int i = datas.size() - 1; i >= 0; i--) {
            FunnelItem f1 = datas.get(i);
            double r1 = (w - w2 * i) / 2 * .9d;// (w * (f1.getValue() / max)) / 2 * .8d;
            double r2 = (w - w2 * (i + 1)) / 2 * .9d;//(w * (f2.getValue() / max)) / 2 * .8d;
            double h1 = (h * (f1.getValue() / sum));
            f1.setHeight(h1);
            f1.setTopRadius(r1);
            f1.setBottomRadius(r2);
            yy += h1;

            double x, y;
            f1.getPath().clear();
            x = mx - r1;
            y = h - yy;
            //Нижний полукруг
            f1.getPath().add(new PathM(x, y));
            f1.getPath().add(new PathC((x + (r1 * xr)), (y + (r1 * yr))));
            f1.getPath().add(new PathBase((mx + r1 - (r1 * xr)), (y + (r1 * yr))));
            f1.getPath().add(new PathBase((mx + r1), y));
            //верхний полукруг
            f1.getPath().add(new PathC((mx + r1 - (r1 * xr)), (y - (r1 * yr))));
            f1.getPath().add(new PathBase((x + (r1 * xr)), (y - (r1 * yr))));
            f1.getPath().add(new PathBase(x, y));
            f1.getPath().add(new PathZ());
            f1.getPath().add(new PathM(x, y));

            x = mx - r2;
            y = y + h1;
            f1.setCenterX(x);
            f1.setCenterY(y);
            //полоса в низ
            f1.getPath().add(new PathL(x, y));
            //нижний полукруг нижнего круга
            f1.getPath().add(new PathC((x + (r2 * xr)), (y + (r2 * yr))));
            f1.getPath().add(new PathBase((mx + r2 - (r2 * xr)), (y + (r2 * yr))));
            f1.getPath().add(new PathBase((mx + r2), y));
            //полоса вверх
            f1.getPath().add(new PathL((mx + r1), (y - h1)));
            x = mx - r1;
            y = h - yy;
            //нижний полукруг верхнего круга
            f1.getPath().add(new PathC((mx + r1 - (r1 * xr) + 1), (y + (r1 * yr))));
            f1.getPath().add(new PathBase((x + (r1 * xr)), (y + (r1 * yr))));
            f1.getPath().add(new PathBase(x - 1, y - 1));
            f1.getPath().add(new PathZ());
        }
        StringBuilder sb = new StringBuilder((datas.size() + 1) * 250);
        switch (renderType) {
            case JS -> privateRenderJS(sb);
            case Image -> privateRenderImage(sb);
        }
        return sb.toString();
    }

    private String privateRenderImage(StringBuilder sb) {
        sb.append("<?xml version=\"1.0\" standalone=\"no\"?>");
        sb.append("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">");
        sb.append("<svg  viewBox=\"0 0 " + width + " " + height + "\""
                + " height=\"" + height + "px\" width=\"" + width + "px\""
                + " xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" style=\"overflow: hidden; position: relative; left: 0px; top: 0px;\">");
        sb.append("\n");
        StringBuilder defs = new StringBuilder(datas.size() * 100);
        StringBuilder paths = new StringBuilder(datas.size() * 75);
        Random random = new Random();
        defs.append("<defs>");
        for (int i = datas.size() - 1; i >= 0; i--) {
            FunnelItem funnelItem = datas.get(i);
            String name = "f" + random.nextInt();
            //Заливка
            defs.append("<linearGradient id=\"" + name + "\" x1=\"0\" y1=\"0\" x2=\"1\" y2=\"0\" gradientTransform=\"matrix(1, 0, 0, 1, 0, 0)\">");
            defs.append("<stop offset=\"0%\" stop-color=\"#ffffff\"></stop>");
            defs.append("<stop offset=\"100%\" stop-color=\"" + funnelItem.getColor() + "\"></stop>");
            defs.append("</linearGradient>");
            defs.append("\n");
            //Путь
            paths.append("<path style=\"opacity: 1; fill-opacity: 1; stroke-opacity: 0.3;\" fill=\"url(#" + name + ")\"");
            paths.append(" stroke=\"" + funnelItem.getColor2() + "\"");
            paths.append(" d=\"" + funnelItem.getPathString(0, 0) + "\" opacity=\"1\" fill-opacity=\"1\" stroke-width=\"1\" stroke-opacity=\"0.3\"/>");
            paths.append("\n");
        }

        defs.append("</defs>");
        sb.append(defs);
        sb.append("<rect x=\"0\" y=\"0\" width=\"" + width + "\" height=\"" + height + "\" r=\"0\" rx=\"0\" ry=\"0\" fill=\"#ffffff\" stroke=\"none\" style=\"\"/>");
        sb.append(paths);
        sb.append("</svg>");
        System.out.println(sb.toString());
        return sb.toString();
    }

    private String privateRenderJS(StringBuilder sb) {

        StringBuilder text = new StringBuilder(datas.size() * 150);
        for (int i = datas.size() - 1; i >= 0; i--) {
            FunnelItem funnelItem = datas.get(i);
            String name = "f" + i;
            //Коллекция
            String path = funnelItem.getPathString(-width / 2, height / 2);
            String pathStart = funnelItem.getPathString(0, 0);

            text.append("var " + name + "=paper.path(\"" + path + "\");");
            text.append("var color = \"rgb(255, 255, 255)\", color2 = \"" + funnelItem.getColor() + "\";");
            text.append(name + ".attr({ fill: [0, color, color2].join(\"-\"), stroke: \"" + funnelItem.getColor2() + "\", \"stroke-width\": 1, \"stroke-opacity\": 0.3});");
            text.append(name + ".mouseover(function () {" + name + ".attr({ \"fill-opacity\":.5});});");
            text.append(name + ".mouseout(function () {" + name + ".attr({\"fill-opacity\":1});});");

            text.append(" var fattr" + i + " = [{path:\"" + pathStart + "\"}], now" + i + "=1; ");
            text.append("f" + i + ".stop().animate(fattr" + i + "[+(now" + i + " = !now" + i + ")]," + (700 + (2 * i) * 100) + ",\"backOut\");\n");

        }
        sb.append("window.onload = function () {");
        sb.append(" var paper = Raphael(\"canvas\", 640, 480);");

        sb.append(" paper.clear();");
        sb.append(" paper.rect(0, 0, 450, 450, 0).attr({fill: \"#fff\", stroke: \"rgb(0,0,0)\"});");
        sb.append(text);
        sb.append("};");
        System.out.println(sb.toString());
        return sb.toString();
    }

    private String color(Color color) {
        return "rgb(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ")";
    }

    private String colorHEX(Color color) {
        return "rgb(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ")";
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public List<FunnelItem> getDatas() {
        return datas;
    }
}
