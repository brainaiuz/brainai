package com.edatasite.workforce.gwt.gettingstarted.client.ui.view;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.gettingstarted.client.rpc.GettingStartedService;
import com.edatasite.workforce.gwt.gettingstarted.client.rpc.GettingStartedServiceAsync;
import com.edatasite.workforce.gwt.pm.client.factory.PMSinksContainerFactory;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: 16.06.2009
 * Time: 18:09:49
 * To change this template use File | Settings | File Templates.
 */
public class FinishingGuideView extends GettingStartedMainView {
    private final GettingStartedServiceAsync gettingStartedService = GettingStartedService.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public FinishingGuideView() {
        super();
        VerticalPanel hop = new VerticalPanel();
        hop.add(new HTML("<span class=introTitle>" + wfmStrings.finished().toUpperCase() + "</span>"));
        hop.add(new HTML(""));
        hop.add(new HTML("<span class=introText>" + wfmStrings.completedSuccessfullyGettingStarted() + "</span>"));
        hop.add(new HTML("<span class=introText>" + wfmStrings.clickToAddInformation() + "</span>"));
        hop.add(new HTML("<span class=introText>" + wfmStrings.clickToFinish() + "</span>"));
//        hop.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//        hop.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
//        hop.setHeight("150px");
//        hop.setWidth("300px");
        hop.setSpacing(10);
        HorizontalPanel vp = new HorizontalPanel();
        hop.setHeight("150px");
        hop.setWidth("500px");
        vp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        vp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        vp.add(hop);
        container.add(vp);
        //container.layout(true);
        backButton = new WfmButton2("  " + wfmStrings.back() + "  ");
        backButton.addClickHandler(sender -> listener.onBackButtonClick());
        buttonPanel.add(backButton);
        buttonPanel.setCellHorizontalAlignment(backButton, HasHorizontalAlignment.ALIGN_LEFT);
        HorizontalPanel hp = new HorizontalPanel();
        hp.setWidth("700px");
        buttonPanel.add(hp);
        nextButton = new WfmButton2("  " + wfmStrings.finish() + "  ");
        nextButton.addClickHandler(sender -> {
            gettingStartedService.activateCompany(new AbstractAsyncCallback() {
                public void failure(Throwable caught) {

                }

                public void success(Object result) {
                    ((PMSinksContainerFactory) SinksContainerFactory.entryPoint.getContainerFactory()).finishGettingStarted();
                }
            });
//                MessageBox messageBox;
//                messageBox = new MessageBox(Style.ICON_INFO, Style.MODAL | Style.OK);
//                messageBox.setText("Success");
//                messageBox.setMessage("Successfully finished");
//                messageBox.addShellListener(new ShellListener(){
//                    public void shellActivated(BaseEvent be) {
//                        //To change body of implemented methods use File | Settings | File Templates.
//                    }
//
//                    public void shellClosed(BaseEvent be) {
//                        ((PMSinksContainerFactory) SinksContainerFactory.entryPoint.getContainerFactory()).finishGettingStarted();
//                    }
//
//                    public void shellDeactivated(BaseEvent be) {
//                        //To change body of implemented methods use File | Settings | File Templates.
//                    }
//                });
//                messageBox.open();
//
//
        });
        buttonPanel.add(nextButton);
        buttonPanel.setCellHorizontalAlignment(nextButton, HasHorizontalAlignment.ALIGN_RIGHT);
    }

    public void showView() {

    }

    public void refresh() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    protected void saveAddAnother() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    protected void skipThisStep() {

    }

    protected boolean saveAndNext() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
