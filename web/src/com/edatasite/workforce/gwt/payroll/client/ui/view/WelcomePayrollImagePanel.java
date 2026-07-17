package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * Created by Khasan on 01.08.14.
 */
public class WelcomePayrollImagePanel extends Composite {


    private HTMLPanel imagePanel;


    public WelcomePayrollImagePanel() {
        getImagePanel();
        initWidget(imagePanel);
    }

    private void getImagePanel() {
        LoadingPanel.loading(true);
        String imageLink = null;
        if ("workforce".equals(Utils.getThemeStyle())) {  /*"Blue"*/
            imageLink = "payroll/images/welcomePayroll_scheme.png";
        } else if ("green".equals(Utils.getThemeStyle())) {   /*"Green"*/
            imageLink = "payroll/images/welcomePayroll_scheme_themeGreen.png";
        } else if ("atm".equals(Utils.getThemeStyle())) {    /*"Grey"*/
            imageLink = "payroll/images/welcomePayroll_scheme_themeGreey.png";
        } else if ("maroon".equals(Utils.getThemeStyle())) {   /*"Maroon"*/
            imageLink = "payroll/images/welcomePayroll_scheme_themeMaroon.png";
        } else if ("coo".equals(Utils.getThemeStyle())) {    /*"Violet"*/
            imageLink = "payroll/images/welcomePayroll_scheme_themeViolete.png";
        } else if ("mediacom".equals(Utils.getThemeStyle())) {   /*"Mediacom"*/
            imageLink = "payroll/images/welcomePayroll_scheme_themeMediacom.png";
        } else if ("mediacom2".equals(Utils.getThemeStyle())) {   /*"Mediacom2"*/
            imageLink = "payroll/images/welcomePayroll_scheme_themeMediacom.png";
        } else if ("orange".equals(Utils.getThemeStyle())) {   /*"orange"*/
            imageLink = "payroll/images/welcomePayroll_scheme_themeOrange.png";
        } else if ("tele".equals(Utils.getThemeStyle())) {    /*"Tele"*/
            imageLink = "payroll/images/welcomePayroll_scheme.png";
        }
        imagePanel = new HTMLPanel("<img src=" + imageLink + " class=\"scheme_img\" alt=\"image\"/>\n");
    }
}
