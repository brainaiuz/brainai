package gwt.material.design.client.ui.html;

import com.google.gwt.dom.client.Document;
import com.google.gwt.safehtml.shared.HtmlSanitizer;
import com.google.gwt.safehtml.shared.SafeHtml;
import gwt.material.design.client.base.HasSafeText;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.base.mixin.TextMixin;

public class FigCaption extends MaterialWidget implements HasSafeText {
    private TextMixin<FigCaption> textMixin;

    public FigCaption() {
        super(Document.get().createElement("figcaption"));
    }

    public FigCaption(String text) {
        this();
        setHtml((SafeHtml) () -> text);
    }

    public String getText() {
        return getTextMixin().getText();
    }

    public void setText(String text) {
        getTextMixin().setText(text);
    }

    public TextMixin<FigCaption> getTextMixin() {
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
    public HtmlSanitizer getSanitizer() {
        return getTextMixin().getSanitizer();
    }

    @Override
    public void setSanitizer(HtmlSanitizer sanitizer) {
        getTextMixin().setSanitizer(sanitizer);
    }
}
