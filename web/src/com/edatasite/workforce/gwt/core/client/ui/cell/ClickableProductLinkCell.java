package com.edatasite.workforce.gwt.core.client.ui.cell;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.google.gwt.cell.client.AbstractSafeHtmlCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SimpleHtmlSanitizer;
import com.google.gwt.text.shared.SafeHtmlRenderer;

/**
 * A cell that renders a product name as a clickable link.
 * When clicked, it will navigate to the product summary if the user has permission.
 * The value is expected to be in format: "productName|productID"
 */
public class ClickableProductLinkCell extends AbstractSafeHtmlCell<String> {

    private static final ProductSafeHtmlRenderer RENDERER = new ProductSafeHtmlRenderer();
    private final boolean checkPermission;
    private final String productName;
    private final Integer productId;

    public ClickableProductLinkCell() {
        this(RENDERER);
    }

    public ClickableProductLinkCell(SafeHtmlRenderer<String> renderer) {
        super(renderer, "click", "keydown");
        this.checkPermission = true;
        this.productName = null;
        this.productId = null;
    }

    public ClickableProductLinkCell(String productName, Integer productId) {
        this(RENDERER, productName, productId);
    }

    public ClickableProductLinkCell(SafeHtmlRenderer<String> renderer, String productName, Integer productId) {
        super(renderer, "click", "keydown");
        this.checkPermission = true;
        this.productName = productName;
        this.productId = productId;
    }

    public ClickableProductLinkCell withPermissionCheck(boolean checkPermission) {
        return new ClickableProductLinkCell(RENDERER, checkPermission, productName, productId);
    }

    private ClickableProductLinkCell(SafeHtmlRenderer<String> renderer, boolean checkPermission, String productName, Integer productId) {
        super(renderer, "click", "keydown");
        this.checkPermission = checkPermission;
        this.productName = productName;
        this.productId = productId;
    }

    @Override
    public void onBrowserEvent(Context context, Element parent, String value,
                               NativeEvent event, ValueUpdater<String> valueUpdater) {
        super.onBrowserEvent(context, parent, value, event, valueUpdater);
        if ("click".equals(event.getType())) {
            EventTarget eventTarget = event.getEventTarget();
            if (!Element.is(eventTarget)) {
                return;
            }
            if (parent.getFirstChildElement().isOrHasChild(Element.as(eventTarget))) {
                handleProductClick(value);
            }
        }
    }

    private void handleProductClick(String value) {
        if (!hasPermission()) {
            return;
        }
        if (value == null || value.isEmpty()) {
            return;
        }
        String[] parts = value.split("\\|");
        if (parts.length >= 2) {
            try {
                Integer productId = Integer.parseInt(parts[1]);
                SinksContainerFactory.entryPoint.onHistoryChanged("product|summary/" + productId);
            } catch (NumberFormatException e) {
                GWT.log("Invalid product ID: " + parts[1], e);
            }
        }
    }

    private boolean hasPermission() {
        if (!checkPermission) {
            return true;
        }
        return Utils.hasPermission(PermissionConstants.ACCOUNTING_PRODUCT_SUMMARY);
    }

    @Override
    public void render(Context context, SafeHtml data, SafeHtmlBuilder sb) {
        if (data != null) {
            String dataString = data.asString();
            String[] parts = dataString.split("\\|");
            String productName = parts[0];
            String productId = parts.length >= 2 ? parts[1] : "";
            boolean hasPermission = !checkPermission || hasPermission();
            String cssClass = hasPermission ? "product-link-cell clickable" : "product-link-cell";
            sb.appendHtmlConstant("<a href=\"javascript:;\" class=\"" + cssClass + "\" data-product-id=\"" + productId + "\">");
            sb.append(SimpleHtmlSanitizer.sanitizeHtml(productName));
            sb.appendHtmlConstant("</a>");
        }
    }

    @Override
    protected void onEnterKeyDown(Context context, Element parent, String value,
                                   NativeEvent event, ValueUpdater<String> valueUpdater) {
        handleProductClick(value);
    }

    private static class ProductSafeHtmlRenderer implements SafeHtmlRenderer<String> {
        @Override
        public SafeHtml render(String object) {
            return SimpleHtmlSanitizer.sanitizeHtml(object != null ? object : "");
        }

        @Override
        public void render(String object, SafeHtmlBuilder builder) {

        }
    }
}
