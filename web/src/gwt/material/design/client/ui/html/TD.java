package gwt.material.design.client.ui.html;

import com.google.gwt.dom.client.Document;
import com.google.gwt.safehtml.client.HasSafeHtml;
import com.google.gwt.safehtml.shared.HtmlSanitizer;
import com.google.gwt.safehtml.shared.SafeHtml;
import gwt.material.design.client.base.HasSafeText;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.base.mixin.TextMixin;

/**
 * Created By : Dilsh0d Madrahimov on 10/4/2019 10:54 AM
 */
public class TD extends MaterialWidget implements HasSafeText, HasSafeHtml {
    private TextMixin<TD> textMixin;

    public TD() {
        super(Document.get().createElement("td"));
    }

    public TD(String text) {
        this();
        setHTML((SafeHtml) () -> text);
    }

    public String getText() {
        return getTextMixin().getText();
    }

    public void setText(String text) {
        getTextMixin().setText(text);
    }

    public TextMixin<TD> getTextMixin() {
        if (textMixin == null) {
            textMixin = new TextMixin<>(this);
        }
        return textMixin;
    }

    @Override
    public void setHtml(SafeHtml html) {
        getTextMixin().setHtml(html);
    }

    @Override
    public void setSanitizer(HtmlSanitizer sanitizer) {
        getTextMixin().setSanitizer(sanitizer);
    }

    @Override
    public HtmlSanitizer getSanitizer() {
        return getTextMixin().getSanitizer();
    }

    @Override
    public void setHTML(SafeHtml safeHtml) {
        getTextMixin().setHtml(safeHtml);
    }
}

