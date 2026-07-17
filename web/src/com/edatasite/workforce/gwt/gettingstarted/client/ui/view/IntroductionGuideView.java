package com.edatasite.workforce.gwt.gettingstarted.client.ui.view;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: 16.06.2009
 * Time: 18:08:49
 * To change this template use File | Settings | File Templates.
 */
public class IntroductionGuideView extends GettingStartedMainView {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private boolean populateDemoData;

    public IntroductionGuideView() {
        super();
        final VerticalPanel vp = new VerticalPanel();
        vp.add(new HTML("<span class=introTitle>" + wfmStrings.introduction().toUpperCase() + "</span>"));
//        vp.add(new HTML(""));
//        vp.add(new HTML(""));
        vp.add(new HTML(wfmStrings.vpHTML1()));
        vp.add(new HTML(wfmStrings.vpHTML2() + " Project Management tool " + wfmStrings.vpHTML21()));
        vp.add(new HTML(wfmStrings.vpHTML3()));
        vp.add(new HTML(wfmStrings.vpHTML4()));
        vp.add(new HTML(""));
        vp.add(new HTML(wfmStrings.vpHTML5()));
        vp.add(new HTML(wfmStrings.vpHTML6()));
        vp.add(new HTML(wfmStrings.vpHTML7()));
        vp.add(new HTML(wfmStrings.vpHTML8()));
        vp.add(new HTML(wfmStrings.vpHTML9()));
        vp.add(new HTML(""));
        vp.add(new HTML(wfmStrings.vpHTML10()));

//        StatusService.App.get().getCompanyStat(new AsyncCallback() {
//            public void onFailure(Throwable caught) {
//                //To change body of implemented methods use File | Settings | File Templates.
//            }
//
//            public void onSuccess(Object result) {
//                final CheckIfTrialCompanyItem item = (CheckIfTrialCompanyItem) result;
//                if (item.isIfTrial() && !item.isDemoDataDeleted()) {
//                    if (!item.isDemoDataPopulated()) {
//                        RadioButton startNew = new RadioButton("start", "<b><font color=red>I want to start with completely New account and will set it up myself</font></b>", true);
//                        startNew.setChecked(true);
//                        startNew.addClickListener(new ClickListener() {
//                            public void onClick(Widget sender) {
//                                populateDemoData = false;
//                            }
//                        });
//                        RadioButton startWithDemo = new RadioButton("start", "<b><font color=red>I want to start with demo data populated account for review</font></b></br>(Note: demo data can be removed after your trial period, and is for demonstration purposes only)", true);
//                        startWithDemo.addClickListener(new ClickListener() {
//                            public void onClick(Widget sender) {
//                                populateDemoData = true;
//                            }
//                        });
//                        vp.add(startNew);
//                        vp.add(startWithDemo);
//                        vp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
//                        vp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
//                        container.layout(true);
//
//                    }
//
//                }
//                vp.add(new HTML("<span class=introText>Please, click <b>Next</b> button below to start.</span>"));
//                vp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
//                vp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
//
//            }
//        });


        vp.setSpacing(5);
        vp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        vp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        HorizontalPanel hp = new HorizontalPanel();
        hp.setSpacing(10);
        hp.setWidth("600px");
        hp.setHeight("200px");
        hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        hp.add(vp);
        container.add(hp);
        //container.layout(true);

        HorizontalPanel space = new HorizontalPanel();
        space.setWidth("600px");
        buttonPanel.add(space);
        buttonPanel.setCellHorizontalAlignment(space, HasHorizontalAlignment.ALIGN_LEFT);
        nextButton = new WfmButton2(wfmStrings.next());
        nextButton.addClickHandler(sender -> {
//                if (populateDemoData) {
//                    SinksContainerFactory.entryPoint.setPopulateDemoData(new WorkforceEntryPoint.PopulateDemoData() {
//                        public void populate(boolean populated) {
//                            LoadingPanel.loading(false);
//                            listener.onNextButtonClick();
//                        }
//                    });
//                    LoadingPanel.loading(true);
//                    SinksContainerFactory.entryPoint.addDemoData();
//                }else{
            listener.onNextButtonClick();
//                }
        });
        buttonPanel.add(nextButton);
        buttonPanel.setCellHorizontalAlignment(nextButton, HorizontalPanel.ALIGN_RIGHT);

//        super(false);
    }

    public void showView() {

    }

    public void refresh() {

    }

    protected void saveAddAnother() {

    }

    protected void skipThisStep() {

    }

    protected boolean saveAndNext() {
        return true;
    }
}
