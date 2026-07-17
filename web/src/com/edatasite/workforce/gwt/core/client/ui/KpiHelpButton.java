package com.edatasite.workforce.gwt.core.client.ui;


import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.form.HelpDocumentItem;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.ButtonExt;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * Created with IntelliJ IDEA.
 * User: Hasan Xo'janazarov
 * Date: 28.11.12
 * Time: 17:55
 * To change this template use File | Settings | File Templates.
 */
public class KpiHelpButton extends ButtonExt {


    public static final WfmStrings wfmStrings = WfmStrings.App.get();
    private HTMLPanel textSpan;
    private String code;
    private String wikiUrl;

    public KpiHelpButton(String code) {
        this.code = code;
        getButton();
    }

    private void getButton() {
        setVisible(false);
        if (code != null && !"".equals(code)) {
            removeStyleName("gwt-Button");
            addStyleName("optBtn2 right");
            textSpan = new HTMLPanel("span", wfmStrings.help());
            setHTML(textSpan.getElement().getString());
            ensureDebugId("help");
            textSpan.setStyleName("markHelp");
            setHTML(textSpan.getElement().getString());
            AllInOneService.App.get().getWikiUrl(code, new AbstractAsyncCallback<HelpDocumentItem>() {
                @Override
                public void success(HelpDocumentItem item) {
                    if (item != null && !"".equals(item)) {
                        wikiUrl = item.getLink();
                        setVisible(true);
                        setTitle(item.getTitle());
                    }
                }
            });

            addClickHandler(clickEvent -> Window.open(wikiUrl, "_blank", ""));
        }
    }
}
