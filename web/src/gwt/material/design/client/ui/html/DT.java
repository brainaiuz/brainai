package gwt.material.design.client.ui.html;

import com.google.gwt.dom.client.Document;
import com.google.gwt.safehtml.shared.HtmlSanitizer;
import com.google.gwt.safehtml.shared.SafeHtml;
import gwt.material.design.client.base.HasSafeText;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.base.mixin.TextMixin;

public class DT extends MaterialWidget implements HasSafeText {
    private TextMixin<DT> textMixin;

    public DT() {
        super(Document.get().createElement("dt"));
    }
    public DT(String text) {
        this();
        setHtml((SafeHtml) () -> text);
    }

    public String getText() {
        return getTextMixin().getText();
    }

    public void setText(String text) {
        getTextMixin().setText(text);
    }

    public TextMixin<DT> getTextMixin() {
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
}
