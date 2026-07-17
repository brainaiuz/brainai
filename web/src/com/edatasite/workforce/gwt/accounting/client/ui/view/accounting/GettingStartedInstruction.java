package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 12/22/11
 * Time: 7:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class GettingStartedInstruction {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private FlexTable topTable;
    private FlexTable.FlexCellFormatter topFormatter;
    private WfmButton2 viewDemo;
    private WfmButton2 viewUserGuide;


    public GettingStartedInstruction() {
        initalize();
    }

    private void initalize(){
        topTable = new FlexTable();
        topTable.setCellPadding(0);
        topTable.setCellSpacing(0);
        topTable.setStyleName("topTableStyle");
        topFormatter = topTable.getFlexCellFormatter();

        topTable.setHTML(0, 0, " ");
        topFormatter.setStyleName(0, 0, "top-l");

        topFormatter.setStyleName(0, 1, "top-c");
        VerticalPanel topCenter = new VerticalPanel();
        topCenter.setStyleName("topCenter");
        topCenter.setSpacing(4);

        topCenter.add(new HTML("<b class=welcomeTitle>" + accountingStrings.welcome10() + "</b>"));
        topCenter.add(new HTML("<span class=topText>"+accountingStrings.pleaceFillOutMessage()+ "</span> "));
//        topCenter.add(new HTML("<span class=topText>" + accountingStrings.welcome21() + " " + "<b>" + accountingStrings.welcome211() + "</b>.</span> "));
//        topCenter.add(new HTML("<span class=topText>" + accountingStrings.welcome22() + "</span>"));
        // Button
        HorizontalPanel buttonBar = new HorizontalPanel();

        viewDemo = new WfmButton2("  " + accountingStrings.viewDemo() + "  ");
        viewDemo.removeStyleName("wfm-button");
        viewDemo.setStyleName("wfm-blue-button");
        viewDemo.addClickHandler(widget -> Window.open("http://www." + Utils.getHelpHost() + "/content/invoicing-introduction-video", "_blank", ""));
        viewDemo.setVisible(false);

        viewUserGuide = new WfmButton2(accountingStrings.viewUserGuide());
        viewUserGuide.removeStyleName("wfm-button");
        viewUserGuide.setStyleName("wfm-blue-button");
        viewUserGuide.addClickHandler(widget -> Window.open(GWT.getHostPageBaseURL() + "customisation/"+Utils.getProductName().toLowerCase()+"/docs/accounting-quick.pdf", "_blank", ""));
        viewUserGuide.setVisible(false);

        buttonBar.setSpacing(4);
        topCenter.add(buttonBar);
        buttonBar.add(viewDemo);
        buttonBar.add(viewUserGuide);

        topTable.setWidget(0, 1, topCenter);

        topTable.setHTML(0, 2, " ");
        topFormatter.setStyleName(0, 2, "top-r");
    }

    public FlexTable getTable() {
        return topTable;
    }
}
