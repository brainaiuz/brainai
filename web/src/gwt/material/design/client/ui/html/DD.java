package gwt.material.design.client.ui.html;

import com.google.gwt.dom.client.Document;
import com.google.gwt.safehtml.shared.HtmlSanitizer;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.base.HasSafeText;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.base.mixin.TextMixin;

public class DD extends MaterialWidget implements HasSafeText {
    private TextMixin<DD> textMixin;

    public DD() {
        super(Document.get().createElement("dd"));
    }

    public DD(String text) {
        this();
        setHtml((SafeHtml) () -> text);
    }

    public DD(Widget widget) {
        this();
        add(widget);
    }

    public String getText() {
        return getTextMixin().getText();
    }

    public void setText(String text) {
        getTextMixin().setText(text);
    }

    public TextMixin<DD> getTextMixin() {
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
