package com.edatasite.workforce.gwt.core.client.ui.landing;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.form.HelpDocumentItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Fatkhulla
 * Date: 1/16/12
 * Time: 4:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class HelpPanelGenerator extends Widget implements Constants {

    public static FlowPanel getHelpPanel(String section, String view) {
        String language = Utils.userSettings.get(LANGUAGE_FOR_USER);
        if ("null".equals(language) || language == null || "en".equals(language)) {
            if (!Utils.isNullOrEmpty(section) && !Utils.isNullOrEmpty(view) && Utils.hasGenericAccess(GenericSettingsEnum.SHOW_USER_GUIDE)) {
                final WestPanelHelp2 westpanel = new WestPanelHelp2();
                AllInOneService.App.get().getHelpDocumentBySectionView(section, view, new AsyncCallback<ArrayList<HelpDocumentItem>>() {
                    @Override
                    public void onFailure(Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(ArrayList<HelpDocumentItem> helpDocumentItems) {
                        if (helpDocumentItems != null && helpDocumentItems.size() > 0) {
                            for (HelpDocumentItem item : helpDocumentItems) {
                                westpanel.addHelpItem(item.getTitle(), item.getDescription(), item.getLink());
                            }
                        }
                    }
                });
                return generatePanel(westpanel, section);
            }
        }
        return null;
    }

    private static FlowPanel generatePanel(WestPanelHelp2 westPanel, String section) {
        FlowPanel wc = new FlowPanel();
        VerticalPanel vp = new VerticalPanel();
        vp.setSpacing(3);
        if(section.equals("PM")){
            vp.add(PMUserGUIde());
        }
        if(section.equals("HRMS")){
            vp.add(HRMSUserGUIde());
        }

        vp.add(westPanel);
        vp.setWidth("100%");
        wc.add(vp);
        return wc;
    }

    private static ActionButton PMUserGUIde(){
        final String pdfPath = "https://s3.amazonaws.com/helpuserguides/quick";
        ActionButton guideLink = new ActionButton("Quick user guide", "markPDF");

        guideLink.getElement().getStyle().setFontSize(10d, Style.Unit.PX);
        guideLink.getElement().getStyle().setMarginLeft(7d, Style.Unit.PX);
        guideLink.getElement().getStyle().setMarginTop(-15d, Style.Unit.PX);

        guideLink.addClickHandler(clickEvent -> Utils.openURL(pdfPath + "/" + "PM_Quick_User_Guide.pdf"));
        return guideLink;

    }
    private static ActionButton HRMSUserGUIde(){
        final String pdfPath = "https://s3.amazonaws.com/helpuserguides/quick";
        ActionButton guideLink = new ActionButton("Quick user guide", "markPDF");

        guideLink.getElement().getStyle().setFontSize(10d, Style.Unit.PX);
        guideLink.getElement().getStyle().setMarginLeft(7d, Style.Unit.PX);
        guideLink.getElement().getStyle().setMarginTop(-15d, Style.Unit.PX);

        guideLink.addClickHandler(clickEvent -> Utils.openURL(pdfPath + "/" + "HRMS_Quick_User_Guide.pdf"));
        return guideLink;

    }

}
