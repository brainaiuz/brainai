package com.finnetlimited.reportservice.core.client.enumtype;

/**
 * User: ${Dilsh0d}
 * Date: 15-Mar-2010
 * Time: 16:57:37
 * <p/>
 * <br/> Please enter id name uniq
 */
public enum IdType {

    BODY("reportSystem"),
    STEP_BODY("stepbodybystep"),
    STEP_BODY_OWNER("stepbodyowner"),
    REPORT_BODY("full-main-inner"),
    MESSAGE_PANEL("message-panel"),
    MESSAGE_PANEL1("message-panel1"),
    BOTTOM_PANEL("button-panel-inner"),
    STEP_PANEL("steppanel"),
    HELP_PANEL("helppanel"),
    FOLDER_TABLE("foldertable"),
    SELECTED_TABLE("selectedtable"),
    SUMMARIES_TABLE("summariestable"),
    REPORT_TABLE("reporttable"),
    ORDER_TABLE("ordertable"),
    LIST_BOX("listbox"),
    ORDER_LIST_BOX("orderlbox"),
    BODY_PANEL("bodyPanel"),
    LOADING("loading"),
    DATE_PICKER("datepicker");


    IdType(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }
}
