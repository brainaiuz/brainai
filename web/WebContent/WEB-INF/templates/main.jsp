<%--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
~ LAST CHANGE                                                                                                      ~
~ User: Ruslan Muhammadov                                                                                                      ~
~ Time: 2011/3/13 15:55:30                                                                                          ~
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~--%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ page import="com.edatasite.workforce.appContext.ApplicationContextProvider" %>
<%@ page import="com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum" %>
<%@ page import="com.edatasite.workforce.gwt.core.client.rpc.module.WfmModuleSettingConstants" %>
<%@ page import="com.edatasite.workforce.gwt.core.client.ui.Constants" %>
<%@ page import="com.edatasite.workforce.gwt.core.client.ui.UiSettings" %>
<%@ page import="com.edatasite.workforce.gwt.core.server.app.ServerUtils" %>
<%@ page import="org.springframework.context.support.WfmMessageSource" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: Jonibek
  Date: May 5, 2009
  Time: 6:29:14 PM
  To change this template use File | Settings | File Templates.
--%>
<%
    String showHeaderParam = request.getParameter(WfmModuleSettingConstants.SHOWHEADER.getName());
    if (showHeaderParam == null) {
        showHeaderParam = "true";
    }
    Boolean isShowHeader = Boolean.valueOf(showHeaderParam);
    Boolean showGoogleTalkChat = Boolean.valueOf(String.valueOf(request.getAttribute(Constants.SHOW_GOOGLE_TALK_CHAT)));
    String parentStyle = request.getParameter(WfmModuleSettingConstants.STYLE.getName());
    if (parentStyle == null || parentStyle.equals("null")) {
        parentStyle = (String) request.getAttribute(Constants.THEME_FOR_SYSTEM);
    }
    if ("".equals(parentStyle) || parentStyle == null || parentStyle.equals("null")) {
        parentStyle = UiSettings.BLUE_THEME;
    }
    String hostLanguageForUser = (String) request.getAttribute(Constants.LANGUAGE_FOR_USER);
    String activeMenu = (String) request.getAttribute(Constants.ACTIVE_MENU);
    String faviconPath = (String) request.getAttribute("productName");
    String logoLink = (String) request.getAttribute("logoLink");
    String favIconLink = (String) request.getAttribute("favIcon");
    String hostName = (String) request.getAttribute("hostName");
    faviconPath = faviconPath != null ? faviconPath.toLowerCase() : "kpi";
    String accounting = (String) request.getAttribute("accounting");
    String crm = (String) request.getAttribute("crm");
    String hrms = (String) request.getAttribute("hrms");
    String pm = (String) request.getAttribute("pm");
    String payroll = (String) request.getAttribute("payroll");
    String reportingsystem = (String) request.getAttribute("reportingsystem");
    String documents = (String) request.getAttribute("documents");
    String tc = (String) request.getAttribute("tc");
    boolean workspaceEnabled = (boolean) request.getAttribute("WORKSPACE_MAIN_MENU");
    boolean accountingEnabled = (boolean) request.getAttribute("ACCOUNTING_MAIN_MENU");
    boolean crmEnabled = (boolean) request.getAttribute("CRM_MAIN_MENU");
    boolean hrmsEnabled = (boolean) request.getAttribute("HRMS_MAIN_MENU");
    boolean pmEnabled = (boolean) request.getAttribute("PM_MAIN_MENU");
    boolean payrollEnabled = (boolean) request.getAttribute("PAYROLL_MAIN_MENU");
    boolean reportingEnabled = (boolean) request.getAttribute("REPORTING_MAIN_MENU");
    boolean documentsEnabled = (boolean) request.getAttribute("DOCUMENTS_MAIN_MENU");
    boolean tcEnabled = (boolean) request.getAttribute("TC_MAIN_MENU");
    String imageUrl = (String) request.getAttribute("image");

    boolean isArabic = "ar".equals(request.getAttribute("LANGUAGE_FOR_USER")) || "he".equals(request.getAttribute("LANGUAGE_FOR_USER"));
%>
<html xmlns="http://www.w3.org/1999/xhtml" <%if(isArabic){%>dir="rtl"<%}%>>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta content="text/html; charset=utf-8" http-equiv="Content-Type">
    <meta name="gwt:property" content="locale=<%=request.getAttribute(Constants.LANGUAGE_FOR_USER)%>">
    <meta http-equiv="P3P" content='policyref="/w3c/p3p.xml", CP="NOI DSP COR PSA PSAa OUR IND COM NAV STA"'>
    <%--<% if (BrowserSupportUtils.isSupportedIE8(request.getHeader("user-agent"))) {%>--%>
    <%--<%}%>--%>
    <script type="text/javascript" src="/mainStyles/js/jquery-3.2.1.min.js"></script>
    <script src="/mainStyles/js/kpi-chat.js" data-widget-size="large" data-name="kpi-chatbot" defer></script>
    <%--<script type="text/javascript" src="/mainStyles/js/svgxuse.min.js"></script> <!-- to fix svg icons problem in IE-11 -->--%>
    <script type="text/javascript">
        document.elementsByName = function (aName) {
            var listOfElements = document.getElementsByName(aName); // Replace aName with the name you're looking for
            // IE hack, because it doesn't properly support getElementsByName
            if (listOfElements.length == 0) { // If IE, which hasn't returned any elements
                var listOfElements = [];
                var spanList = document.getElementsByTagName('*'); // If all the elements are the same type of tag, enter it here (e.g.: SPAN)
                for (var i = 0; i < spanList.length; i++) {
                    if (spanList[i].getAttribute('name') == aName) {
                        listOfElements.push(spanList[i]);
                    }
                }
            }
            return listOfElements;
        }
    </script>

    <% if (Constants.PROJECT_MANAGEMENT_PAGE.equals(activeMenu)) {%>
    <title>
        <fmt:message key="main.projectManagment"/> - ${productName}
    </title>
    <% } else if (Constants.HRMS_PAGE.equals(activeMenu)) { %>
    <title>
        <fmt:message key="main.HRMS"/> - ${productName}
    </title>
    <% } else if (Constants.CRM_PAGE.equals(activeMenu)) { %>
    <title>
        <fmt:message key="main.CRM"/> - ${productName}
    </title>
    <% } else if (Constants.ACCOUNTING_PAGE.equals(activeMenu)) { %>
    <title>
        <fmt:message key="main.accountingFinance"/> - ${productName}
    </title>
    <% } else if (Constants.PAYROLL_PAGE.equals(activeMenu) && (!hostName.contains("app.tjilo.com"))) { %>
    <title>
        <fmt:message key="main.payroll"/> - ${productName}
    </title>
    <% } else if (Constants.DOC_MY_FOLDER.equals(activeMenu)) { %>
    <title>
        <fmt:message key="main.documents"/> - ${productName}
    </title>
    <% } else if (Constants.REPORTING_PAGE.equals(activeMenu)) { %>
    <title>
        <fmt:message key="main.reporting"/> - ${productName}
    </title>
    <% } else if (Constants.DASHBOARD_PAGE.equals(activeMenu)) { %>
    <title>
        <fmt:message key="main.dashboard"/> - ${productName}
    </title>
    <% } else if (Constants.MYACCOUNT_PAGE.equals(activeMenu)) { %>
    <title><fmt:message key="main.myBilling"/> - ${productName}</title>
    <% } else if (Constants.SETTINGS_PAGE.equals(activeMenu)) { %>
    <title><fmt:message key="main.settings"/> - ${productName}</title>
    <% } else if (Constants.TRAINING_CENTER_PAGE.equals(activeMenu)) { %>
    <title><fmt:message key="main.trainingCenter"/> - ${productName}</title>
    <%} else if (Constants.LOGISTICS_PAGE.equals(activeMenu)) { %>
    <title><fmt:message key="main.logistics"/> - ${productName}</title>
    <%} else if (Constants.MC_PAGE.equals(activeMenu)) { %>
    <title><fmt:message key="main.messageCentre"/> - ${productName}</title>
    <%} else if (Constants.MY_WORKSPACE_PAGE.equals(activeMenu)) { %>
    <title><fmt:message key="main.myWorkspace"/> - ${productName}</title>
    <%} else {%>
    <title> ${productName}</title>
    <%}%>

    <tiles:insertAttribute name="script" ignore="false"/>

    <% if (favIconLink != null) { %>
    <link rel="icon" href="<%= favIconLink %>" type="image/x-icon"/>
    <% } else { %>
    <link rel="shortcut icon" href="/customisation/<%=faviconPath%>/images/favicon.ico" type="image/x-icon"/>
    <% } %>
    <%--<link rel=stylesheet type=text/css href="/mainStyles/<%=request.getAttribute("theme")%>.css?v=<%=version%>" type="text/css">--%>
    <link rel="P3Pv1" href="/w3c/p3p.xml">
    <link rel="apple-touch-icon" href="/mobile_sources/images/apple-touch-icon.png" type="image/x-icon">
    <%
        String enableAsterisk = (String) request.getAttribute(GenericSettingsEnum.ENABLE_ASTERISK.name());
        if ("YES".equalsIgnoreCase(enableAsterisk)) {
    %>
    <script type="text/javascript" src="/mainStyles/js/sip-0.17.1.min.js"></script>
    <script type="text/javascript" src="/mainStyles/js/kpiAsterisk.js"></script>
    <%
        }

        if (Constants.LOGISTICS_PAGE.equals(activeMenu)
                || Constants.REPORTING_PAGE.equals(activeMenu)
                || Constants.ACCOUNTING_PAGE.equals(activeMenu)
                || Constants.MYACCOUNT_PAGE.equals(activeMenu)
                || Constants.PAYROLL_PAGE.equals(activeMenu)
                || Constants.HRMS_PAGE.equals(activeMenu)
                || Constants.PROJECT_MANAGEMENT_PAGE.equals(activeMenu)) { %>


    <!-- ######################### START JS FOR CURRENT PAGE! ######################### -->
    <%--<script src="mainStyles/bootstrap/js/vendor/jquery.min.js" type="application/javascript"></script>--%>
    <script src="mainStyles/bootstrap/js/transition.js" type="application/javascript"></script>
    <script src="mainStyles/bootstrap/js/tab.js" type="application/javascript"></script>
    <script src="mainStyles/bootstrap/js/collapse.js" type="application/javascript"></script>

    <script src="mainStyles/bootstrap/js/switch_stage.js" type="application/javascript"></script>
    <script src="mainStyles/bootstrap/js/anti_scroll_x.js" type="application/javascript"></script>

    <%--<script src="mainStyles/bootstrap/js/btn_toggle.js" type="application/javascript"></script>--%>


    <script src="mainStyles/bootstrap/js/scrollArea_pageControls.js" type="application/javascript"></script>
    <script src="mainStyles/bootstrap/js/dropdown.js" type="application/javascript"></script>
    <script src="mainStyles/bootstrap/js/jquery.vibrate.min.js" type="application/javascript"></script>

    <!-- ######################### End JS FOR CURRENT PAGE! ######################### -->


    <%}%>

    <% if (showGoogleTalkChat
            || ServerUtils.isCrm()
            || hostName.contains("telemanaged.com")) { %>
    <%--<script type="text/javascript" src="/mainStyles/js/jquery-1.8.2.min.js"></script>--%>
    <script type="text/javascript" src="/mainStyles/js/jappixChat.js"></script>
    <script type="text/javascript" src="/mainStyles/js/tool-tip.js"></script>

    <script type="text/javascript">
        function getAatribut() {
            $("a").easyTooltip();
            $('a').each(function () {
                $('[title]').each(function () {
                    $this = $(this);
                    $.data(this, 'title', $this.attr('title'));
                    $this.removeAttr('title');
                });
            });
        }
    </script>
    <% }
        String version = (String) request.getAttribute("cssVersion");
    %>

    <%--<link rel="stylesheet" href="/mainStyles/js/grid/css/gridstack.min.css" type="text/css"/>--%>
    <link rel="stylesheet" href="/mainStyles/new-ui/login/slick/slick.css?v=<%=version%>" type="text/css"/>
    <link rel="stylesheet" href="/googlegadget/select2.min.css?v=<%=version%>" type="text/css"/>
    <script src="/mainStyles/js/jquery-ui.min.js" type="application/javascript"></script>
    <!-- Google tag (gtag.js) -->
    <script async src="https://www.googletagmanager.com/gtag/js?id=G-JHFJELYCR7"></script>
    <script>
        window.dataLayer = window.dataLayer || [];
        function gtag(){dataLayer.push(arguments);}
        gtag('js', new Date());

        gtag('config', 'G-JHFJELYCR7');
    </script>
    <script src="/mainStyles/js/grid/lodash.min.js" type="application/javascript"></script>
    <script src="/mainStyles/js/grid/gridstack.min.js" type="application/javascript"></script>
    <script src="/mainStyles/js/grid/gridstack.jQueryUI.min.js" type="application/javascript"></script>
    <script src="/mainStyles/new-ui/login/js/slick.min.js" type="application/javascript"></script>
    <script src="/googlegadget/select2.min.js" type="application/javascript"></script>
    <script src="/mainStyles/new-ui/tooltipster/tooltipster.bundle.min.js" type="application/javascript"></script>
    <script defer src="/mainStyles/new-ui/js/svgxuse.js" type="application/javascript"></script>
    <%--<%--%>
    <%--String serverName = request.getServerName();--%>
    <%--if (serverName.contains("localhost") || serverName.contains("newui.kpi.com")) { %>--%>
    <link rel="stylesheet" href="/mainStyles/new-ui/css/materialize.css?v=<%=version%>">
    <%--<%} else {%>--%>
    <%--<link rel="stylesheet" href="/mainStyles/new-ui/css/materialize.min.css?v=<%=version%>">--%>
    <%--<%}%>--%>

    <%if (isArabic) {%>
    <link rel=stylesheet type=text/css href="/mainStyles/rtl.css?v=<%=version%>">
    <%} else {%>
    <%--<link rel=stylesheet type=text/css href="/mainStyles/ltr.css">--%>
    <style>.left {
        float: left !important
    }

    .right {
        float: right !important
    }</style>
    <%}%>

    <%--<link rel="stylesheet" href="/mainStyles/new-ui/css/kpitables.css?v=<%=version%>">--%>
    <%--<link rel="stylesheet" href="/mainStyles/new-ui/css/temp.css?v=<%=version%>">--%>
    <%--<link rel="stylesheet" href="/mainStyles/new-ui/css/theme.css?v=<%=version%>">--%>
    <%--<link rel="stylesheet" href="/mainStyles/new-ui/css/invoice.css?v=<%=version%>">--%>
    <%--<link rel="stylesheet" href="/mainStyles/new-ui/css/flags.min.css?v=<%=version%>">--%>
    <%--<link rel="stylesheet" href="/mainStyles/new-ui/css/KanbanDND.css?v=<%=version%>">--%>
    <%--<link rel="stylesheet" href="/mainStyles/new-ui/css/dynamictable.css?v=<%=version%>">--%>
    <%--<link rel="stylesheet" href="/mainStyles/reporting/reporting.css" type="text/css"/>--%>
    <%--<link rel="stylesheet" href="/mainStyles/new-ui/css/transition.css">--%>
    <%--<link rel="stylesheet" href="/mainStyles/new-ui/css/jquery.scrollbar.css">--%> <%--Added to materialize.css--%>
    <%--<link rel="stylesheet" href="/mainStyles/new-ui/css/dashboard.grid.css?v=<%=version%>">--%>
    <%--<link rel="stylesheet" href="/mainStyles/new-ui/css/recurringWidget.css?v=<%=version%>">--%>
    <%--<link rel="stylesheet" href="/mainStyles/bootstrap/css/bootstrap-timepicker.css?v=<%=version%>">--%>


    <style>
        .pg-loading {
            position: fixed;
            left: 0;
            right: 0;
            top: 0;
            bottom: 0;
            display: flex;
            flex-direction: column;
            justify-content: center;
            user-select: none;
        }

        .pg-loading__logo {
            text-align: center;
        }
    </style>

</head>

<body class="frame__body <%=request.getAttribute("SNS") != null ? request.getAttribute(Constants.SIDE_NAV_STYLE) : "left-menu-open"%>">

<iframe id="__gwt_historyFrame" style="position: absolute; left: -1000px; top: 0; right: 0; bottom: 0;"></iframe>


<div id="Loading-Message">
    <div class="pg-loading">
        <div class="frame__modules">
            <div class="main-modules">
                <div>
                    <div class="user-profile">
                        <a class="user-profile__menu dropdown-button" style="cursor: pointer;">
                <span class="user-profile-img" style="background-image: url(<%=imageUrl%>);">
                    <img <%--src="https://wfmtest.s3.amazonaws.com/000000000000/public/23039/static/87a7e0a6-c4d3-4cfa-81f2-fe9e029dba7b?AWSAccessKeyId=AKIAJJMKLWOMZUSCJLUQ&amp;Expires=1696848025&amp;Signature=o6bGDg2Do7vyHh%2BsauJePTlBslg%3D"--%>>
                </span>
                        </a>
                    </div>
                </div>
                <ul>
<%--                    <% if (workspaceEnabled) { %>--%>
<%--                    <li class="main-modules__item main-modules__item--workspace">--%>
<%--                        <a href="Workspace.html" style="cursor: pointer;">--%>
<%--                            <svg class="icon--home">--%>
<%--                                <use href="mainStyles/new-ui/icons/sprite__panels.svg?v=3.1.0#home"></use>--%>
<%--                            </svg>--%>
<%--                            <span></span>--%>
<%--                        </a>--%>
<%--                    </li>--%>
<%--                    <%}%>--%>
                    <% if (accountingEnabled) { %>
                    <li class="main-modules__item  main-modules__item--accounting">
                        <a style="cursor: pointer;">
                            <svg class="icon--accounting">
                                <use href="mainStyles/new-ui/icons/sprite__panels.svg?v=3.1.0#accounting"></use>
                            </svg>
                            <span><%=accounting%></span>
                        </a>
                    </li>
                    <%}%>

                    <% if (crmEnabled) { %>
                    <li class="main-modules__item main-modules__item--sales">
                        <a style="cursor: pointer;">
                            <svg class="icon--sales">
                                <use href="mainStyles/new-ui/icons/sprite__panels.svg?v=3.1.0#sales"></use>
                            </svg>
                            <span><%=crm%></span>
                        </a>
                    </li>
                    <%}%>
                    <% if (hrmsEnabled) { %>
                    <li class="main-modules__item main-modules__item--humans">
                        <a style="cursor: pointer;">
                            <svg class="icon--humans">
                                <use href="mainStyles/new-ui/icons/sprite__panels.svg?v=3.1.0#humans"></use>
                            </svg>
                            <span><%=hrms%></span>
                        </a>
                    </li>
                    <%}%>
                    <% if (pmEnabled) { %>
                    <li class="main-modules__item main-modules__item--projects">
                        <a style="cursor: pointer;">
                            <svg class="icon--projects">
                                <use href="mainStyles/new-ui/icons/sprite__panels.svg?v=3.1.0#projects"></use>
                            </svg>
                            <span><%=pm%></span>
                        </a>
                    </li>
                    <%}%>
                    <% if (payrollEnabled) { %>
                    <li class="main-modules__item main-modules__item--payroll">
                        <a style="cursor: pointer;">
                            <svg class="icon--payroll">
                                <use href="mainStyles/new-ui/icons/sprite__panels.svg?v=3.1.0#payroll"></use>
                            </svg>
                            <span><%=payroll%></span>
                        </a>
                    </li>
                    <%}%>
                    <% if (reportingEnabled) { %>
                    <li class="main-modules__item main-modules__item--reports">
                        <a style="cursor: pointer;">
                            <svg class="icon--reporting">
                                <use href="mainStyles/new-ui/icons/sprite__panels.svg?v=3.1.0#reporting"></use>
                            </svg>
                            <span><%=reportingsystem%></span>
                        </a>
                    </li>
                    <%}%>
                    <% if (documentsEnabled) { %>
                    <li class="main-modules__item main-modules__item--docs">
                        <a style="cursor: pointer;">
                            <svg class="icon--documents">
                                <use href="mainStyles/new-ui/icons/sprite__panels.svg?v=3.1.0#documents"></use>
                            </svg>
                            <span><%=documents%></span>
                        </a>
                    </li>
                    <%}%>
                    <% if (tcEnabled) { %>
                    <li class="main-modules__item main-modules__item--projects">
                        <a style="cursor: pointer;">
                            <svg class="icon--projects">
                                <use href="mainStyles/new-ui/icons/sprite__panels.svg?v=3.1.0#projects"></use>
                            </svg>
                            <span><%=tc%></span>
                        </a>
                    </li>
                    <%}%>
                </ul>
                <div class="kpi-modules-overlay" style="display: none;"></div>
            </div>
        </div>

        <div class="pg-loading__logo">
            <% if (logoLink != null) { %>
            <% if (hostName.contains("brainbm") || hostName.contains("brain")) { %>
            <img width="200" src="<%= logoLink %>" alt="logo" style="height:200px !important;width: 180px !important;"/>
            <% } else { %>
            <img width="100" src="<%= logoLink %>" alt="logo" style="height:100px !important;width: 90px !important;"/>
            <% } %>
            <% } else { %>
            <img width="100" src="/mainStyles/new-ui/images/new-kpi-logo.svg?v=2" alt="logo"/>
            <% } %>
        </div>
    </div>
</div>
<%
    if ("YES".equalsIgnoreCase(enableAsterisk)) {
%>
<video id="remoteVideo" hidden></video>
<video id="localVideo" hidden muted="muted"></video>
<%
    }
%>

<script type="text/javascript">
    function itemonmouseover(id, page) {
        var pa = document.getElementById(id);

        if (pa == null) return;
        pa.onmouseover = function () {
            if (pa.className.indexOf("wfm-my-toolitem-selected wfm-my-toolitem-disabled") == -1)
                pa.className = "wfm-my-toolitem-over";
        };
        pa.onmouseout = function () {
            if (pa.className.indexOf("wfm-my-toolitem-selected wfm-my-toolitem-disabled") == -1)
                pa.className = "wfm-my-toolitem wfm-my-no-selection";
        };
        pa.onclick = function () {
            if (pa.className.indexOf("wfm-my-toolitem-selected wfm-my-toolitem-disabled") == -1)
                document.location = page;
        };
    }

    itemonmouseover("ws-item", "Workspace.html");
    itemonmouseover("pm-item", "ProjectManagement.html");
    itemonmouseover("hrms-item", "Hrms.html");
    itemonmouseover("acc-item", "Accounting.html");
    /*itemonmouseover("website-item", "Website.html");*/
    itemonmouseover("payr-item", "Payroll.html");
    itemonmouseover("doc-item", "Documents.html");
    itemonmouseover("crm-item", "Crm.html");
    itemonmouseover("dash-item", "Dashboard.html");
    itemonmouseover("set-item", "Settings.html");
    itemonmouseover("myacc-item", "Myaccount.html");
    itemonmouseover("my-22", "logout.html");

    setTimeout("showRefreshMessage()", 60000);

    function showRefreshMessage() {
        var pageLoadVar = document.getElementById("pageload");
        var loaderAnim = document.getElementById("loaderAnim");

        if (pageLoadVar != undefined)
            pageLoadVar.style.display = 'block';
        if (loaderAnim != undefined)
            loaderAnim.style.display = 'none;';
    }

    function contentScroll(contentStyleName, parentStyle) {
        $(parentStyle + ' ' + contentStyleName).scrollbar({
            "autoUpdate": true,
            "autoScrollSize": true,
            "scrollx": $(parentStyle + ' .external-scroll_x'),
            "scrolly": $(parentStyle + ' .external-scroll_y')
        });
    }

    //Editor include styles problem fix T87909 //https://i.imgur.com/JIA4NUP.png
    function removeProblemStyles() {
        const styleSheets = document.styleSheets; // Target all stylesheets in the document

        Array.from(styleSheets).forEach((styleSheet) => {
            try {
                const rules = styleSheet.cssRules || styleSheet.rules; // Get all CSS rules

                // Iterate through all rules
                for (let i = 0; i < rules.length; i++) {
                    const rule = rules[i];

                    // Ensure we are dealing with a CSSStyleRule and not something like @media
                    if (rule.type === CSSRule.STYLE_RULE) {

                        // Проверяем селектор: только тег <div> без классов и id
                        if (rule.selectorText === 'div') {

                            // Проверяем, если свойства width, padding или margin присутствуют
                            const { width, padding, margin } = rule.style;
                            if (width || padding || margin) {
                                styleSheet.deleteRule(i);
                                i--; // Корректируем индекс после удаления правила
                            }
                        }
                    }
                }
            } catch (e) {
                console.warn('Не удалось обработать один из стилей (вероятно, cross-origin):', e);
            }
        });
    }
    // Запускаем функцию при загрузке страницы
    window.addEventListener('load', removeProblemStyles);

</script>


<%-- Common GWT related data--%>
<jsp:include page="userSpecificDatas.jsp"/>

<script type="text/javascript" src="/mainStyles/new-ui/js/frame_affix.min.js?v=<%=version%>"></script>
<%--<script type="text/javascript" src="/mainStyles/new-ui/js/jquery.scrollbar.min.js"></script>--%>
<script type="text/javascript" src="/mainStyles/new-ui/js/highcharts.js"></script>
<script type="text/javascript" src="/mainStyles/new-ui/js/modules/drilldown.js"></script>
<script type="text/javascript" src="/mainStyles/new-ui/js/modules/exporting.js"></script>
<script type="text/javascript" src="/mainStyles/new-ui/js/highcharts-3d.js"></script>
<script type="text/javascript" src="/mainStyles/new-ui/js/highcharts-more.js"></script>
<script type="text/javascript" src="/mainStyles/new-ui/js/cylinder.js"></script>
<script type="text/javascript" src="/mainStyles/new-ui/js/modules/exporting.js"></script>
<%--<script type="text/javascript" src="/mainStyles/new-ui/js/modules/offline-exporting.js"></script>--%>
<script type="text/javascript" src="/mainStyles/new-ui/js/modules/gantt.js"></script>
<script type="text/javascript" src="/mainStyles/new-ui/js/modules/funnel.js"></script>
<script type="text/javascript" src="/mainStyles/new-ui/js/exportAllCharts.js"></script>
<script type="text/javascript" src="/mainStyles/new-ui/js/modules/solid-gauge.js"></script>
<script type="text/javascript" src="/mainStyles/bootstrap/js/bootstrap-timepicker.js"></script>
<script type="text/javascript" src="/mainStyles/js/twilio.min.js"></script>
<div id="tawk_5b2e498aeba8cd3125e31c0d" class="tawkto-css"></div>
</body>

<tiles:insertAttribute name="jsjacScript" ignore="true"/>
<tiles:insertAttribute name="facebookScript" ignore="true"/>

</html>
