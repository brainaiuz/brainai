package com.edatasite.workforce.gwt.core.client.rpc.module;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 11-May-2011
 * Time: 20:07:16
 * <p/>
 * <h1>... This is Enum constants Module settings doc file xml constants ...</h1>
 */
public enum WfmModuleSettingConstants {
    /**
     * This is constant name property uses read in doc file xml
     * <p/>
     * Excample:
     * <module>
     * ...
     * </module>
     */
    MODULE("module"),
    /**
     * This is constant name property uses in module xml tag attribue
     * <p/>
     * Excample:
     * <module name="modulename">
     * ...
     * </module>
     */
    MODULENAME("name"),
    /**
     * This is modile tag attribute
     * <p/>
     * Excample:
     * <module name="modulename" style="workforce">
     * ...
     * </module>
     */
    STYLE("style"),
    /**
     * This is constant name property uses in div tag id name
     * <p/>
     * Excample:
     * <div id="wfm-module-settings">
     * <module name="modulename">
     * ...
     * </module>
     * </div>
     */
    MUDULESETTING("wfm-module-settings"),
    /**
     * This is constant name property uses in module xml tag attribue
     * <p/>
     * Excample:
     * <module name="modulename" width="1000px">
     * ...
     * </module>
     */
    WIDTH("width"),
    /**
     * This is constant name property uses in module xml tag attribue
     * <p/>
     * Excample:
     * <module name="modulename" height="1000px">
     * ...
     * </module>
     */
    HEIGHT("height"),
    /**
     * This is constant name property uses in module xml tag attribute
     * By this is attribute value set entry point to div tag id value equals to rootid attribute value
     * <p/>
     * Excample:
     * <div id="wfm-module-settings">
     * <module name="modulename" rootid="wfp">
     * ...
     * </module>
     * </div>
     * <div id="wfp">
     * </dib>
     */
    ROOTDIVID("rootid"),
    /**
     * This is constant name property uses in module xml tag attribute
     * default value true
     * <p/>
     * Excample:
     * <div id="wfm-module-settings">
     * <module name="modulename" listlimit="-1">
     * ...
     * </module>
     * </div>
     */
    LISTPANEL_LIMIT("listlimit"),
     /**
     * This is constant name property uses in module xml tag attribute
     * default value true
     * <p/>
     * Excample:
     * <div id="wfm-module-settings">
     * <module name="modulename" listlimit="-1">
     * ...
     * </module>
     * </div>
     */
    LISTPANEL_SHOW_PAGING("listshowpaging"),
    /**
     * This is constant name property uses in module xml tag attribute
     * default value true
     * <p/>
     * Excample:
     * <div id="wfm-module-settings">
     * <module name="modulename" listshowpaging="false">
     * ...
     * </module>
     * </div>
     */
    SHOWHEADER("showheader"),
    /**
     * This is constant name property uses in container xml tag attribute
     * default value true
     * <p/>
     * Excample:
     * <div id="wfm-module-settings">
     * <module name="modulename" showheader="false">
     * <containers>
     * <container historyname="accountList_2" showleftmenu="false" sections="contactList_2"/>
     * <container historyname="gettingStarted" showleftmenu="true" sections="all"/>-
     * </containers>
     * </module>
     * </div>
     */
    SHOWLEFTMENU("showleftmenu"),
    /**
     * This is constant name property uses in module xml tag attribute
     * default value true
     * <p/>
     * Excample:
     * <div id="wfm-module-settings">
     * <module name="modulename" showheader="false" showallcontainer="true">
     * no write xml doc
     * </module>
     * </div>
     */
    SHOWALLCONTAINER("showallcontainer"),
    /**
     * This is constant name property uses in container xml attribute
     * default value true
     * <p/>
     * Excample:
     * <div id="wfm-module-settings">
     * <module name="modulename" showheader="false">
     * <containers>
     * <container historyname="accountList_2" showleftmenu="false" sections="contactList_2"/>
     * <container historyname="gettingStarted" showleftmenu="true" sections="all"/>-
     * </containers>
     * </module>
     * </div>
     */
    HISTORYNAME("historyname"),
    /**
     * This is constant name property uses in module xml tag inner xml doc tag
     * default value true
     * <p/>
     * Excample:
     * <div id="wfm-module-settings">
     * <module name="modulename" showheader="false">
     * <containers>
     * <container historyname="accountList_2" showleftmenu="false" sections="contactList_2"/>
     * <container historyname="gettingStarted" showleftmenu="true" sections="all"/>-
     * </containers>
     * </module>
     * </div>
     */
    CONTAINERS("containers"),
    /**
     * This is constant name property uses in containers xml tag inner xml doc tag
     * default value true
     * <p/>
     * Excample:
     * <div id="wfm-module-settings">
     * <module name="modulename" showheader="false">
     * <containers>
     * <container historyname="accountList_2" showleftmenu="false" sections="contactList_2"/>
     * <container historyname="gettingStarted" showleftmenu="true" sections="all"/>-
     * </containers>
     * </module>
     * </div>
     */
    CONTAINER("container"),
    /**
     * This is constant name property uses in containers xml tag inner xml doc tag
     * default value all
     * or View class history names
     * <p/>
     * Excample:
     * <div id="wfm-module-settings">
     * <module name="modulename" showheader="false">
     * <containers>
     * <container historyname="accountList_2" showleftmenu="false" sections="contactList_2"/>
     * <container historyname="gettingStarted" showleftmenu="true" sections="all"/>-
     * </containers>
     * </module>
     * </div>
     */
    SECTIONS("sections"),
    /**
     * This is module view params
     * Owner tag from param tag no attributes
     */
    PARAMS("params"),
    /**
     * This is module view param tag
     * attributes: name and value
     * <p/>
     * Example
     * <div id="wfm-module-settings">
     * <module name="modulename" showheader="false">
     * <containers>
     * <container historyname="accountList_2" showleftmenu="false" sections="contactList_2"/>
     * <container historyname="gettingStarted" showleftmenu="true" sections="all"/>-
     * </containers>
     * <params>
     * <param name="contactId" value="3018"/>
     * <param name="email" value="dilshod.toj@gmail.com"/>
     * ...
     * </params>
     * </module>
     * </div>
     */
    PARAM("param"),
    /**
     * If this param value true that when working close closed browser window tab
     */
    BROWSERWINDOWCLOSE("isCloseWindow"),

    SHOWSTEPS("showsteps"),

    SHOWBUTTONS("showbuttons"),

    ACTIVESTEPS("activesteps"),

    FIRSTSTEP("firststep"),

    ACTIVEPAGERS("activepagers"),

    CUSTOMDASHBOARDID("customdashboardid"),

    ENABLE_WFT_LISTING("enable_wft_listing");


    WfmModuleSettingConstants(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }
}
