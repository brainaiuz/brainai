<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en" dir="ltr">
<head>
    <meta content="text/html; charset=utf-8" http-equiv="Content-Type">
    <% if (isSupportedIE8) {%>
    <meta http-equiv="X-UA-Compatible" content="IE=8;"/>
    <%}%>
    <title>
        <tiles:getAsString name="title" ignore="true"/>
    </title>
    <% if (hostName.contains("aws")) {%>
    <meta name="robots" content="noindex,nofollow">
    <%}%>


    <link rel="shortcut icon" href="/customisation/smebu/images/favicon.ico" type="image/x-icon"/>
    <link type="text/css" rel="stylesheet" media="all" href="//www.${helpHost}/sites/all/modules/nice_menus/nice_menus.css"/>
    <link type="text/css" rel="stylesheet" media="all" href="//www.${helpHost}/sites/all/modules/nice_menus/nice_menus_default.css"/>
    <link type="text/css" rel="stylesheet" media="all" href="//www.${helpHost}/sites/all/themes/whitelabel/style.css"/>
    <link rel="stylesheet" href="/customisation/preprod.kpi.com/style.css"/>
    <link rel="stylesheet" href="/customisation/preprod.kpi.com/appFrame.css"/>
    <link rel="stylesheet" href="/customisation/preprod.kpi.com/master.css"/>

    <tiles:insertAttribute name="style" ignore="true"/>
    <link href="/loginpage/kpi/afterdeletedshell.css" media="all" rel="stylesheet" type="text/css"/>
    <%----------------------------------------%>
    <!--[if lte IE 7]>
    <link rel="stylesheet" href="/loginpage/kpi/ie7-andLeft.css" type="text/css"/>
    <![endif]-->
    <!--[if lte IE 8]>
    <link rel="stylesheet" href="/loginpage/kpi/ie8-andLeft.css" type="text/css"/>
    <![endif]-->

    <%
        //Recaptcha script
        Boolean enableCaptcha = request.getAttribute("captcha") != null ? ((Boolean) request.getAttribute("captcha")) : false;
        String captchaTheme = request.getAttribute("captchaTheme") != null ? ((String) request.getAttribute("captchaTheme")) : "white";
        if (enableCaptcha) {
    %>
    <script type="text/javascript" src="https://www.google.com/recaptcha/api/js/recaptcha_ajax.js"></script>
    <% } %>
</head>
<body id="body-index">


<!--begin #wrapper-->
<div id="wrapper">

<!--begin #header-->
<div id="header">
    <div id="head">

        <div id="block-block-4" class="clear-block block block-block">


            <div class="content"><a id="logo-site" href="/">
                <img src="/customisation/smebu/images/smebu-logo.png" alt="Site Name"/>
            </a>


                <strong class="headPhone">
                    <span>+34 902 94 62 95</span>
                </strong>

                <ul class="userMenu">
                    <li><a href="/index.html"><fmt:message key="index.login"/></a></li>
                    <li><a href="/signup/freeSignup.html">
                        <fmt:message key="freeTrial.title"/> </a></li>
                    <li><a href="//www.${helpHost}/content/contact-us"><fmt:message key="frontendmain.contactUsOnly"/> </a></li>
                    <li id="followUs">

                        <a href="http://www.facebook.com/pages/Smebu/177668568947857"
                           target="_blank"> <img src="//www.${helpHost}/sites/default/files/facebook.png"/>
                        </a>

                        <a href="https://twitter.com/#!/smebu_" target="_blank">
                            <img src="//www.${helpHost}/sites/default/files/twitter.png"/>
                        </a>

                        <a href="http://www.linkedin.com/company/smebu" target="_blank">
                            <img src="//www.${helpHost}/sites/default/files/linkedin.png"/>
                        </a>

                    </li>
                </ul>


                <ul class="languages">
                    <li class="lang-En active"><a title="English" href="/">En</a></li>
                    <li class="lang-Es"><a title="Spanish" href="/">Es</a></li>
                </ul>
            </div>
        </div>
    </div>

    <!--begin #navigation-->
    <div id="navigation">
        <div id="block-nice_menus-1" class="clear-block block block-nice_menus">

            <div class="content">
                <ul class="nice-menu nice-menu-right" id="nice-menu-1">
                    <li id="menu-255" class="menu-path-front_page act">
                        <a href="//www.${helpHost}/front_page" title="" class="active">
                            <fmt:message key="frontendmain.home"/>
                        </a>
                    </li>

                    <li id="menu-392" class="menuparent menu-path-node-71">
                        <a href="//www.${helpHost}/content/product-tour" title="">
                            <fmt:message key="frontendmain.productTour"/>
                        </a>

                        <div class="dd-menu">
                            <ul>
                                <li id="menu-394" class="menu-path-node-21">
                                    <a href="//www.${helpHost}/content/take-project-management-software-screenshot-tour" title="">
                                        <fmt:message key="frontendmain.projectManagement"/>
                                    </a>
                                </li>

                                <li id="menu-395" class="menu-path-node-40">
                                    <a href="//www.${helpHost}/content/crm-campaign-contact-and-notes" title="">
                                        <fmt:message key="frontendmain.customerRelationshipManagement"/>
                                    </a>
                                </li>

                                <li id="menu-396" class="menu-path-node-74">
                                    <a href="//www.${helpHost}/content/hr-management" title="HR Management">
                                        <fmt:message key="frontendmain.hrManagement"/>
                                    </a>
                                </li>
                                <li id="menu-397" class="menu-path-node-75">
                                    <a href="//www.${helpHost}/content/accounting-and-finance" title="Accounting and Finance">
                                        <fmt:message key="frontendmain.accountingFinance"/>
                                    </a>
                                </li>

                                <li id="menu-398" class="menu-path-node-76">
                                    <a href="//www.${helpHost}/content/payroll" title="Payroll">
                                        <fmt:message key="frontendmain.payroll"/>
                                    </a>
                                </li>

                                <li id="menu-400" class="menu-path-node-78">
                                    <a href="//www.${helpHost}/content/reporting" title="Reporting">
                                        <fmt:message key="frontendmain.reporting"/>
                                    </a>
                                </li>

                                <li id="menu-399" class="menu-path-node-77">
                                    <a href="//www.${helpHost}/content/workspace" title="Workspace">
                                        <fmt:message key="frontendmain.workspace"/>
                                    </a>
                                </li>

                                <li id="menu-402" class="menu-path-node-80">
                                    <a href="//www.${helpHost}/content/ecommerce" title="e-Commerce">
                                        <fmt:message key="frontendmain.ecommerce"/>
                                    </a>
                                </li>

                                <li id="menu-401" class="menu-path-node-79">
                                    <a href="//www.${helpHost}/content/online-document-management-use" title="Online Document Management in Use">
                                        <fmt:message key="frontendmain.documents"/>
                                    </a>
                                </li>
                            </ul>
                            <div class='dd-shadow'>
                                <div></div>
                            </div>
                        </div>
                    </li>

                    <li id="menu-393" class="menu-path-node-73">
                        <a href="/Pricing.html" title="Pricing">
                            <fmt:message key="frontendmain.pricing"/>
                        </a>
                    </li>

                    <li id="menu-280" class="menu-path-node-1">
                        <a href="//www.${helpHost}/content/support" title="Help and Support">
                            <fmt:message key="frontendmain.helpSupport"/>
                        </a>
                    </li>

                    <li id="menu-300" class="menu-path-node-20">
                        <a href="//www.${helpHost}/content/about-us" title="About Us">
                            <fmt:message key="frontendmain.about"/>
                        </a>
                    </li>
                </ul>
            </div>

        </div>
    </div>
    <!--END #navigation-->

</div>
<!--END #header-->

<!--begin content and ZoneIn-inner-->
<tiles:insertAttribute name="body" ignore="false"/>

<%--<div id="cover">--%>
<%--<!--begin #main-->--%>
<%--<div id="main">--%>

<%--<div style="">--%>


<%--<div class="overhide content-block">--%>
<%--<div id="index-page">--%>


<%--</div>--%>
<%--</div>--%>


<%--</div>--%>
<%--</div>--%>

<%--<!--END #main-->--%>

<%--</div>--%>
<!--END #ZoneIn-->

<!--begin #footer-->
<div id="footer">
    <div class="footNav">
        <div id="block-block-1" class="clear-block block block-block">


            <div class="content">
                <ul class="cols">
                    <li class="parent">
                        <h2 class="title"><fmt:message key="frontendmain.home"/></h2>
                        <ul>
                            <li>
                                <a href="//www.${helpHost}/content/product-tour">
                                    <fmt:message key="frontendmain.productTour"/>
                                </a>
                            </li>

                            <li>
                                <a href="//www.${helpHost}/Pricing.html">
                                    <fmt:message key="frontendmain.pricing"/>
                                </a>
                            </li>

                            <li>
                                <a href="//www.${helpHost}/content/support">
                                    <fmt:message key="supportOnly"/>
                                </a>
                            </li>

                            <li>
                                <a href="//www.${helpHost}/content/about">
                                    <fmt:message key="frontendmain.about"/>
                                </a>
                            </li>

                            <li>
                                <a href="//www.${helpHost}/content/sitemap">
                                    <fmt:message key="frontendmain.sitemap"/>
                                </a>
                            </li>
                        </ul>
                    </li>


                    <li class="parent">
                        <h2 class="title"><fmt:message key="frontendmain.ourProducts"/></h2>
                        <ul>
                            <li>
                                <a href="//www.${helpHost}/content/take-project-management-software-screenshot-tour">
                                    <fmt:message key="frontendmain.projectManagement"/>
                                </a>
                            </li>

                            <li>
                                <a href="//www.${helpHost}/content/crm-campaign-contact-and-notes">
                                    <fmt:message key="frontendmain.clientRelationManagement"/>
                                </a>
                            </li>

                            <li>
                                <a href="//www.${helpHost}/content/hr-management">
                                    <fmt:message key="frontendmain.humanResourcesManagement"/>
                                </a>
                            </li>

                            <li>
                                <a href="//www.${helpHost}/content/accounting-and-finance">
                                    <fmt:message key="frontendmain.accountingFinance"/>
                                </a>
                            </li>

                            <li>
                                <a href="//www.${helpHost}/content/ecommerce">
                                    <fmt:message
                                            key="frontendmain.ecommerce"/>
                                </a>
                            </li>

                        </ul>
                    </li>

                    <li class="parent">
                        <h2 class="title"><fmt:message key="frontendmain.getconnected"/></h2>
                        <ul>
                            <li><a href="http://www.facebook.com/pages/Smebu/177668568947857">Facebook</a></li>
                            <li><a href="https://twitter.com/#!/smebu_">Twitter</a></li>
                            <li><a href="http://www.linkedin.com/company/smebu">LinkedIn</a></li>
                        </ul>
                    </li>

                    <li class="parent signs">
                        <a href="https://www.google.com/enterprise/marketplace/search?categoryId=6&amp;orderBy=RATING&amp;offset=10/"
                           class="key-GoogleApp">Google apps</a>

                    </li>
                </ul>
            </div>
        </div>
        <!-- end .cols -->

    </div>
    <div class="clear c-box">

        <div class="copyright">2007 - 2012 &copy;
            <a href="/">Smebu</a> <fmt:message key="frontendmain.allRightsReserved"/>
        </div>
    </div>
</div>
<!--END #footer-->

<tiles:insertAttribute name="script" ignore="true"/>
<%
    if (enableCaptcha != null && enableCaptcha) {%>
<script type="text/javascript">
    Recaptcha.create("<%=EdsContextParams.getRecaptchaPublicKey()%>", "recaptcha", {
        theme: "<%=captchaTheme%>"});
</script>
<% } %>

<%if (request.getRequestURI().endsWith("pricing.jsp")) {%>
<script type="text/javascript">
    window.$zopim || (function (d, s) {
        var z = $zopim = function (c) {
            z._.push(c)
        }, $ = z.s =
                d.createElement(s), e = d.getElementsByTagName(s)[0];
        z.set = function (o) {
            z.set.
                    _.push(o)
        };
        z._ = [];
        z.set._ = [];
        $.async = !0;
        $.setAttribute('charset', 'utf-8');
        $.src = '//cdn.zopim.com/?z32bt6AkgEJuarqyBbKCnMBB0wUOd3OK';
        z.t = +new Date;
        $.
                type = 'text/javascript';
        e.parentNode.insertBefore($, e)
    })(document, 'script');
</script>
<% } %>
</div>

<%--<script type="text/javascript">--%>
<%--(function (i, s, o, g, r, a, m) {--%>
<%--i['GoogleAnalyticsObject'] = r;--%>
<%--i[r] = i[r] || function () {--%>
<%--(i[r].q = i[r].q || []).push(arguments)--%>
<%--}, i[r].l = 1 * new Date();--%>
<%--a = s.createElement(o), m = s.getElementsByTagName(o)[0];--%>
<%--a.async = 1;--%>
<%--a.src = g;--%>
<%--m.parentNode.insertBefore(a, m)--%>
<%--})(window, document, 'script', '//www.google-analytics.com/analytics.js', 'ga');--%>
<%--ga('create', 'UA-59981695-3', 'auto');--%>
<%--ga('send', 'pageview');--%>
<%--</script>--%>

<!--END #wrapper-->
</body>
</html>

