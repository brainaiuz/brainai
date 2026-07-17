package com.edatasite.workforce.gwt.core.client.ui.listpanel;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.CoreMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.RootPanel;
import gwt.material.design.client.ui.html.Anchor;
import gwt.material.design.client.ui.html.DD;
import gwt.material.design.client.ui.html.DL;
import gwt.material.design.client.ui.html.DT;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.FigCaption;
import gwt.material.design.client.ui.html.FigureWidget;
import gwt.material.design.client.ui.html.IFrame;
import gwt.material.design.client.ui.html.Span;

public class ListingGuidePanel extends Div implements Constants {
    private static final CoreMessages coreMessages = CoreMessages.App.get();

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private DT instanceDT;
    private DD dd;
    private ListPanelGuideSettingsRPC settingsData;
    private KpiModal videoModal;
    private Command addCommand;
    private Command uploadCommand;

    public ListingGuidePanel(ListPanelGuideSettingsRPC settingsData, Command addCommand, Command uploadCommand) {
        super("guide");
        this.addCommand = addCommand;
        this.settingsData = settingsData;
        this.uploadCommand = uploadCommand;
        add(createHeader());
        if (settingsData != null) {
            add(createBody(settingsData));
        }
        setInstanceName(settingsData.getInstanceName());
    }

    private void setInstanceName(String instanceName) {
        if (instanceName == null) {
            instanceDT.getElement().setInnerHTML("");
            return;
        }
        instanceDT.getElement().setInnerHTML(Property.get(instanceName, coreMessages.tapButtonToCreate(), instanceName.toLowerCase()));
    }

    private Div createHeader() {
        Div header = new Div("guide-header");
        DL dl = new DL();
        dd = new DD();
        instanceDT = new DT();
        dl.add(instanceDT);
        Anchor addButton = new Anchor();
        addButton.addStyleName("btn--circle btn--success btn-large");
        SvgIcon iconAdd = new SvgIcon(SvgEnum.plus);
        addButton.add(iconAdd);
        addButton.addClickHandler(e -> {
            if (addCommand != null) {
                addCommand.execute();
            } else {
                Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
            }
        });
        dd.add(addButton);

        if (uploadCommand != null) {
            Span orSpan = new Span(wfmStrings.or());
            dd.add(orSpan);
            Anchor uploadButton = new Anchor();
            uploadButton.addStyleName("btn--circle btn--primary btn-large");
            SvgIcon iconUpload = new SvgIcon(SvgEnum.uploadCloud);
            uploadButton.add(iconUpload);
            uploadButton.addClickHandler(e -> {
                uploadCommand.execute();
            });
            dd.add(uploadButton);
        }
        dl.add(dd);
        header.add(dl);
        return header;
    }

    private Div createBody(ListPanelGuideSettingsRPC settingsData) {
        Div body = new Div("guide-body");
        Div guideLine = new Div("guide-line");
        if (settingsData.isShowScheduleDemo() && settingsData.getYoutubeUrl() != null && !Utils.isNullOrEmpty(settingsData.getYoutubeUrl())) {
            Div demoPanel = createYoutubePanel(settingsData.getYoutubeUrl());
            guideLine.add(demoPanel);
        }

        if (settingsData.isShowPhoneNumber() && settingsData.getPhoneNumber() != null && !Utils.isNullOrEmpty(settingsData.getPhoneNumber())) {
            Div phonePanel = createPhonePanel(settingsData.getPhoneNumber());
            guideLine.add(phonePanel);
        }
        if (settingsData.isShowWiki() && settingsData.getWikiUrl() != null && !Utils.isNullOrEmpty(settingsData.getWikiUrl())) {
            Div wikiPanel = createWikiPanel(settingsData.getWikiUrl());
            guideLine.add(wikiPanel);
        }

        body.add(guideLine);
        return body;
    }

    private Div createPhonePanel(String phoneNumber) {
        Div phonePanel = new Div("guide-line__phone");
        Div panel = new Div("panel");
        FigureWidget headingFigure = new FigureWidget();
        headingFigure.addStyleName("panel__heading");
        Div figureIconDiv = new Div("figure-icon");
        SvgIcon phoneIcon = new SvgIcon(SvgEnum.phone);
        figureIconDiv.add(phoneIcon);
        headingFigure.add(figureIconDiv);

        FigCaption figCaption = new FigCaption();
        figCaption.getElement().setInnerHTML("<h2 class=\"fs-3\"><strong>" + wfmStrings.phone() + "</strong><small>" + wfmStrings.giveUsACall() + "</small></h2>");
        headingFigure.add(figCaption);

        panel.add(headingFigure);

        Anchor anchor = new Anchor();
        anchor.getElement().setAttribute("href", "tel:" + phoneNumber);
        anchor.addStyleName("btn-small btn--primary");
        anchor.getElement().setInnerHTML(phoneNumber);
        Div panelBody = new Div("panel__body");
        Div contactDiv = new Div();
        contactDiv.getElement().setInnerHTML(wfmStrings.youCanContactTo());
        contactDiv.add(anchor);
        Div phoneDiv = new Div();
        Span useChatSpan = new Span();
        useChatSpan.getElement().setInnerHTML(" " + wfmStrings.orUseChat());
        phoneDiv.add(contactDiv);
        phoneDiv.add(useChatSpan);
        panelBody.add(phoneDiv);

        phonePanel.add(panel);
        panel.add(panelBody);
        return phonePanel;
    }

    private Div createYoutubePanel(String youtubeUrl) {
        Div demoPanel = new Div("guide-line__demo");

        FigureWidget figure = new FigureWidget();
        Div videoWrapper = new Div("videoWrapper");
        videoWrapper.addStyleName("videoWrapper--poster");
        videoWrapper.addClickHandler(e -> {
            RootPanel.get().addStyleName("has-guide-modal");
            if (videoModal == null) {
                videoModal = new KpiModal(false);
                videoModal.setDismissible(true);
                /**
                 * a.btn-small btn--close btn--circle
                 * svg#
                 */
                Anchor buttonClose = new Anchor();
                buttonClose.addStyleName("btn-small btn--close btn--circle");
                SvgIcon iconClose = new SvgIcon(SvgEnum.x);
                buttonClose.add(iconClose);
                buttonClose.addClickHandler(closeEvent -> {
                    videoModal.close();
                });
                videoModal.addToWrapper(buttonClose);
                videoModal.addOpenHandler(event -> {
                    RootPanel.get().addStyleName("has-guide-modal");
                });
                videoModal.addCloseHandler(closeEvent -> {
                    RootPanel.get().removeStyleName("has-guide-modal");
                });
                IFrame iFrame = new IFrame();
                iFrame.getElement().setAttribute("src", youtubeUrl);
                iFrame.getElement().setAttribute("sandbox", "allow-forms allow-scripts allow-same-origin allow-presentation");
                iFrame.getElement().setAttribute("allowfullscreen", "allowfullscreen");
                videoModal.add(iFrame);
            }
            videoModal.open();
        });
        figure.add(videoWrapper);

        FigCaption figCaption = new FigCaption();
        if (settingsData.isShowScheduleDemo()) {
            WfmButton2 demoButton = new WfmButton2(wfmStrings.arrangeADemo(), "btn-large btn--primary");
            demoButton.addClickHandler(e -> {
                Utils.openURL(settingsData.getDemoURL());
            });
            figCaption.add(demoButton);
        }
        figure.add(figCaption);

        demoPanel.add(figure);
        return demoPanel;
    }


    private Div createWikiPanel(String wikiUrl) {
        Div wikiDiv = new Div("guide-line__wiki");
        Anchor anchor = new Anchor();
        anchor.addStyleName("panel");
        anchor.getElement().setAttribute("href", wikiUrl);
        anchor.getElement().setAttribute("target", "_blank");
        FigureWidget headingFigure = new FigureWidget();
        headingFigure.addStyleName("panel__heading");
        Div iconDiv = new Div("figure-icon");
        SvgIcon wikiIcon = new SvgIcon(SvgEnum.wiki);
        iconDiv.add(wikiIcon);
        headingFigure.add(iconDiv);
        FigCaption figCaption = new FigCaption();
        figCaption.getElement().setInnerHTML("<h2 class=\"fs-3\"><strong>" + wfmStrings.wiki() + "</strong><small>" + wfmStrings.visitTheWiki() + "</small></h2>");
        headingFigure.add(figCaption);
        anchor.add(headingFigure);

        Div body = new Div("panel__body");
        body.getElement().setInnerHTML(wfmStrings.wikiIsHelpful());

        anchor.add(body);
        wikiDiv.add(anchor);
        return wikiDiv;
    }

    @Override
    protected void onDetach() {
//        MainLayout.get().considerBodyHasFittedContent(true);
//        MainLayout.get().considerBodyHasOperPanel(true);
//        MainLayout.get().considerBodyHasFittedContent(false);
//        MainLayout.get().considerPagerOpersEmptiness(false);

        super.onDetach();
    }

    @Override
    protected void onAttach() {
        MainLayout.get().considerBodyHasFittedContent(false);
        MainLayout.get().considerBodyHasOperPanel(false);
        MainLayout.get().considerBodyHasFittedContent(true);
//        MainLayout.get().considerPagerOpersEmptiness(true);

        super.onAttach();
    }

}
