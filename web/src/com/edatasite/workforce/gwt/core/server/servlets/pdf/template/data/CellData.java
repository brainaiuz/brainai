package com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data;

import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextImageProperty;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfTableListTemplate;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.*;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.html.simpleparser.StyleSheet;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.*;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 6/27/11
 * Time: 9:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class CellData {
    private BigDecimal dueAmount;
    private Integer type = ITextTableList.CELL_TEXT; //CELL_TEXT = 0, CELL_LINK = 1, CELL_IMAGE = 2;
    private String text;
    private Integer alignment;
    private ITextImageProperty imageProperty;
    private String link;
    private Font font;
    private Padding padding;
    private Color bgColor;
    private Color borderColor;
    private Integer colspan;
    private byte[] stream;
    private Integer border;
    private Integer borderRight;
    private Integer borderLeft;
    private Integer width;
    private String backgroundColor;
    private String bold;

    private String textColor;

    private HashMap<String, Integer> reasonVal;

    public CellData(HashMap<String, Integer> reasonVal) {
        this.reasonVal = reasonVal;
    }

    public CellData(String text, String backgroundColor) {
        this.text = text;
        this.backgroundColor = backgroundColor;
    }

    public CellData(Integer type) {
        this.type = type;
    }

    public CellData(String text) {
        this.text = text;
    }

    public CellData(String text, Integer alignment) {
        this.text = text;
        this.alignment = alignment;
    }

    public CellData(String text, Integer alignment, String backgroundColor) {
        this.text = text;
        this.alignment = alignment;
        this.backgroundColor = backgroundColor;
    }

    public CellData(String text, Integer alignment, Integer width) {
        this.text = text;
        this.alignment = alignment;
        this.width = width;
    }

    public CellData(BigDecimal dueAmount, int alignment) {
        this.dueAmount = dueAmount;
        this.alignment = alignment;
    }

    public Integer getBorder() {
        return border;
    }

    public void setBorder(Integer border) {
        this.border = border;
    }

    public Integer getBorderRight() {
        return borderRight;
    }

    public void setBorderRight(Integer borderRight) {
        this.borderRight = borderRight;
    }

    public Integer getBorderLeft() {
        return borderLeft;
    }

    public void setBorderLeft(Integer borderLeft) {
        this.borderLeft = borderLeft;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getText() {
        return text != null ? text : "";
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getAlignment() {
        return alignment;
    }

    public void setAlignment(Integer alignment) {
        this.alignment = alignment;
    }

    public ITextImageProperty getImageProperty() {
        return imageProperty;
    }

    public void setImageProperty(ITextImageProperty imageProperty) {
        this.imageProperty = imageProperty;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Padding getPadding() {
        return padding;
    }

    public void setPadding(Integer left, Integer right, Integer top, Integer bottom) {
        this.padding = new Padding(left, right, top, bottom);
    }

    public Color getBgColor() {
        return bgColor;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public Integer getColspan() {
        return colspan;
    }

    public void setColspan(Integer colspan) {
        this.colspan = colspan;
    }

    public byte[] getStream() {
        return stream;
    }

    public void setStream(byte[] stream) {
        this.stream = stream;
    }

    public String getBackgroundColor() {
        return backgroundColor == null ? "" : backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getBold() {
        return bold;
    }

    public void setBold(String bold) {
        this.bold = bold;
    }

    public PdfPCell createPdfCell(Font font8, Font font8Link) {
        PdfPCell cell;
        if (ITextTableList.CELL_LINK.equals(type)) {
            Anchor anchor = new Anchor(text, font8Link);
            anchor.setReference(link);
            cell = new PdfPCell(anchor);
        } else if (ITextTableList.CELL_IMAGE.equals(type)) {
            Image image = null;
            try {
                if (stream != null) {
                    image = Image.getInstance(stream);
                } else {
                    image = Image.getInstance(link);
                }
            } catch (IOException | BadElementException e) {
                e.printStackTrace();
            }
            if (image != null) {
                if (imageProperty != null) {
                    if (imageProperty.getWidth() != null && imageProperty.getHeight() != null) {
                        if (image.getWidth() > imageProperty.getWidth() && image.getHeight() > imageProperty.getHeight()) {
                            float widthScale = image.getWidth() / imageProperty.getWidth();
                            float heightScale = image.getHeight() / imageProperty.getHeight();
                            if (widthScale > heightScale) {
                                image.scaleAbsoluteWidth(imageProperty.getWidth());
                                image.scaleAbsoluteHeight(image.getHeight() / widthScale);
                            } else {
                                image.scaleAbsoluteHeight(imageProperty.getHeight());
                                image.scaleAbsoluteWidth(image.getWidth() / heightScale);
                            }
                        } else if (image.getWidth() > imageProperty.getWidth()) {
                            image.scaleAbsoluteWidth(imageProperty.getWidth());
                            image.scaleAbsoluteHeight(image.getHeight() * imageProperty.getWidth() / image.getWidth());
                        } else if (image.getHeight() > imageProperty.getHeight()) {
                            image.scaleAbsoluteHeight(image.getHeight());
                            image.scaleAbsoluteWidth(image.getWidth() * imageProperty.getHeight() / image.getHeight());
                        }
                    } else if (imageProperty.getWidth() != null) {
                        image.scaleAbsoluteWidth(imageProperty.getWidth());
                        image.scaleAbsoluteHeight(image.getHeight() * (imageProperty.getWidth() / image.getWidth()));
                    } else if (imageProperty.getHeight() != null) {
                        image.scaleAbsoluteHeight(imageProperty.getHeight());
                        image.scaleAbsoluteHeight(image.getHeight() * (imageProperty.getHeight() / image.getHeight()));
                    }
                }
                Chunk chunk = new Chunk(image, 0, 0);
                cell = new PdfPCell(new Phrase(chunk));
            } else if (text != null && link != null) {
                Anchor anchor = new Anchor(text, font8Link);
                anchor.setReference(link);
                cell = new PdfPCell(anchor);
            } else {
                cell = new PdfPCell(new Phrase("", font8));
            }
        } else if (ITextTableList.CELL_HTML.equals(type)) {
            try {
                cell = new PdfPCell(getParseHtmlElem(text));
            } catch (IOException e) {
                cell = new PdfPCell(new Phrase("", font8));
                e.printStackTrace();
            }
        } else if (ITextTableList.CELL_HTML_TEXT.equals(type)) {
            cell = new PdfPCell(new Phrase(text, font8));
            if (Utils.isRTL(text)) {
                cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            }
        } else {
            if (font != null) {
                cell = new PdfPCell(new Phrase(text, font));
            } else {
                cell = new PdfPCell(new Phrase(text, font8));
            }
        }
        return cell;
    }

    private Paragraph getParseHtmlElem(String s) throws IOException {
        Paragraph paragraph = new Paragraph();
        if (s == null) {
            return paragraph;
        }
        List<Element> objects = HTMLWorker.parseToList(new StringReader(ITextPdfTableListTemplate.ELEMENT_STYLE + s + "</span>"), new StyleSheet());
        paragraph.addAll(objects);
        return paragraph;
    }

    public static class Padding {
        private Integer left;
        private Integer right;
        private Integer top;
        private Integer bottom;

        public Padding(Integer left, Integer right, Integer top, Integer bottom) {
            this.left = left;
            this.right = right;
            this.top = top;
            this.bottom = bottom;
        }

        public Integer getLeft() {
            return left;
        }

        public void setLeft(Integer left) {
            this.left = left;
        }

        public Integer getRight() {
            return right;
        }

        public void setRight(Integer right) {
            this.right = right;
        }

        public Integer getTop() {
            return top;
        }

        public void setTop(Integer top) {
            this.top = top;
        }

        public Integer getBottom() {
            return bottom;
        }

        public void setBottom(Integer bottom) {
            this.bottom = bottom;
        }
    }


    public String getReasonVal(String val) {
        return reasonVal.get(val) == null ? "" : reasonVal.get(val) != 0 ? reasonVal.get(val) + "" : "";
    }

    public void setReasonVal(HashMap<String, Integer> reasonVal) {
        this.reasonVal = reasonVal;
    }

    public String getTextColor() {
        return textColor != null ? textColor : "";
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }
}
