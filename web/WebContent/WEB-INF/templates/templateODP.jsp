<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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


    <link rel="shortcut icon" href="/customisation/${productNameLower}/images/favicon.ico" type="image/x-icon"/>

    <link type="text/css" rel="stylesheet" media="all" href="/customisation/${productNameLower}/allstyle.css"/>
    <link rel="stylesheet" href="/customisation/${productNameLower}/style.css"/>
    <link rel="stylesheet" href="/customisation/${productNameLower}/appFrame.css"/>

    <%
        //Recaptcha script
        Boolean enableCaptcha = request.getAttribute("captcha") != null ? ((Boolean) request.getAttribute("captcha")) : false;
        String captchaTheme = request.getAttribute("captchaTheme") != null ? ((String) request.getAttribute("captchaTheme")) : "white";

        if (enableCaptcha) {
    %>
    <script type="text/javascript" src="https://www.google.com/recaptcha/api/js/recaptcha_ajax.js"></script>
    <% } %>

    <tiles:insertAttribute name="style" ignore="true"/>
    <link href="/loginpage/kpi/afterdeletedshell.css" media="all" rel="stylesheet" type="text/css"/>
    <%----------------------------------------%>
    <!--[if lte IE 7]>
    <link rel="stylesheet" href="/loginpage/kpi/ie7-andLeft.css" type="text/css"/>
    <![endif]-->
    <!--[if lte IE 8]>
    <link rel="stylesheet" href="/loginpage/kpi/ie8-andLeft.css" type="text/css"/>
    <![endif]-->


</head>
<body id="body-index">


<!--begin #wrapper-->
<div id="wrapper">

    <!--begin #header-->
    <%--<div id="header">--%>
    <%--<div class="zoneIn">--%>
    <%--<div class="siteWidth">--%>
    <%--<div class="langBar">--%>
    <%--<ul>--%>
    <%--<li><a href="?locale=ar"><fmt:message key="template.header.Arabic"/> </a></li>--%>
    <%--<li><a href="?locale=en"> <fmt:message key="template.header.English"/> </a></li>--%>
    <%--</ul>--%>
    <%--</div>--%>
    <%--<!--begin #headMenu-->--%>
    <%--<div class="headMenu" id="headMenu">--%>
    <%--<ul class="menu" id="menu-primary-menu">--%>
    <%--<li class="menu-item menu-item-type-custom menu-item-object-custom current-menu-item current_page_item menu-item-49"--%>
    <%--id="menu-item-49"><a href="http://${helpHost}"><span><fmt:message key="frontendmain.home"/> </span></a></li>--%>
    <%--<li class="menu-item-parent menu-item menu-item-type-post_type menu-item-object-page menu-item-50"--%>
    <%--id="menu-item-50"><a href="http://${helpHost}/product-tour/"><span> <fmt:message key="template.header.productTour"/>--%>
    <%--</span></a>--%>
    <%--<ul class="sub-menu">--%>
    <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-55"--%>
    <%--id="menu-item-55"><a--%>
    <%--href="http://${helpHost}/project-and-resource-management/"><span>Project and Resource Management</span></a>--%>
    <%--</li>--%>
    <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-77"--%>
    <%--id="menu-item-77"><a--%>
    <%--href="http://${helpHost}/crm-and-sales/"><span>CRM and Sales</span></a></li>--%>
    <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-76"--%>
    <%--id="menu-item-76"><a href="http://${helpHost}/hr-management-and-payroll/"><span>HR Management and Payroll</span></a>--%>
    <%--</li>--%>
    <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-75"--%>
    <%--id="menu-item-75"><a href="http://${helpHost}/financials-and-inventory/"><span>Financials and Inventory</span></a>--%>
    <%--</li>--%>
    <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-74"--%>
    <%--id="menu-item-74"><a href="http://${helpHost}/reporting-and-dashboard/"><span>Reporting and Dashboard</span></a>--%>
    <%--</li>--%>
    <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-73"--%>
    <%--id="menu-item-73"><a href="http://${helpHost}/document-management/"><span>Document Management</span></a>--%>
    <%--</li>--%>
    <%--</ul>--%>
    <%--</li>--%>
    <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-83"--%>
    <%--id="menu-item-83"><a href="http://${helpHost}/prices/"><span> <fmt:message key="template.header.prices"/> </span></a></li>--%>
    <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-86"--%>
    <%--id="menu-item-86"><a href="http://${helpHost}/support/"><span> <fmt:message key="frontendmain.helpSupport"/> </span></a></li>--%>
    <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-103"--%>
    <%--id="menu-item-103"><a href="http://${helpHost}/about/"><span> <fmt:message key="frontendmain.about"/> </span></a></li>--%>
    <%--<li class="menu-item menu-item-type-custom menu-item-object-custom menu-item-105"--%>
    <%--id="menu-item-105"><a href="http://omandatapark.com/"><span> <fmt:message key="frontendmain.contacts"/> </span></a></li>--%>
    <%--<li class="btnParent-1 menu-item menu-item-type-custom menu-item-object-custom menu-item-106"--%>
    <%--id="menu-item-106"><a href="/signup/freeSignup.html"--%>
    <%--target="_blank"><span> <fmt:message key="template.header.createAccount"/> </span></a></li>--%>
    <%--<li class="btnParent-2 menu-item menu-item-type-custom menu-item-object-custom menu-item-107"--%>
    <%--id="menu-item-107"><a href="/"><span> <fmt:message key="index.signIn"/> </span></a></li>--%>
    <%--</ul>--%>
    <%--</div>--%>
    <%--<!--END #headMenu-->--%>

    <%--<a href="http://${helpHost}/" id="logo" style="margin-left: 105px;">--%>
    <%--<img alt="" src="${logoImage}">--%>
    <%--</a>--%>


    <%--<div class="clearBox"></div>--%>

    <%--</div>--%>
    <%--</div>--%>
    <%--</div>--%>
    <!--END #header-->

    <tiles:insertAttribute name="body" ignore="false"/>

    <!--begin #footer-->
    <%--<div id="footer">--%>
    <%--<div class="siteWidth">--%>
    <%--<div class="zoneIn">--%>

    <%--<div id="footMenu">--%>
    <%--<table>--%>
    <%--<tbody><tr>--%>
    <%--<td>--%>
    <%--<h2><fmt:message key="template.footer.Home"/> </h2>--%>
    <%--<ul>--%>
    <%--<li><a href="#"> <fmt:message key="template.footer.ProductServices"/> </a></li>--%>
    <%--<li><a href="#"> <fmt:message key="template.footer.NewsBlog"/></a></li>--%>
    <%--<li><a href="#"> <fmt:message key="template.footer.Aboutus"/> </a></li>--%>
    <%--<li><a href="#"> <fmt:message key="template.footer.Sitemap"/> </a></li>--%>
    <%--<li><a href="#"> <fmt:message key="template.footer.Forum"/> </a></li>--%>
    <%--</ul>--%>
    <%--</td>--%>
    <%--<td>--%>
    <%--<h2><fmt:message key="template.footer.Resources"/></h2>--%>
    <%--<ul>--%>
    <%--<li><a href="#"><fmt:message key="template.footer.OnlineDemoAccount"/></a></li>--%>
    <%--<li><a href="#"><fmt:message key="template.footer.DownloadBrochures"/></a></li>--%>
    <%--<li><a href="#"><fmt:message key="template.footer.PriceComparison"/></a></li>--%>
    <%--<li><a href="#"><fmt:message key="template.footer.LiveHelp"/></a></li>--%>
    <%--</ul>--%>
    <%--</td>--%>
    <%--<td>--%>
    <%--<h2><fmt:message key="template.footer.WeOffer"/></h2>--%>
    <%--<ul>--%>
    <%--<li><a href="#"><fmt:message key="template.footer.AttendanceTracking"/></a></li>--%>
    <%--<li><a href="#"><fmt:message key="template.footer.PayrollSolution"/></a></li>--%>
    <%--<li><a href="#"><fmt:message key="template.footer.Dashboards"/></a></li>--%>
    <%--<li><a href="#"><fmt:message key="template.footer.Timesheet"/></a></li>--%>
    <%--</ul>--%>
    <%--</td>--%>
    <%--<td>--%>
    <%--<h2><fmt:message key="template.footer.OurProducts"/></h2>--%>
    <%--<ul>--%>
    <%--<li><a href="#"><fmt:message key="template.footer.HumanResourcesManagement"/></a></li>--%>
    <%--<li><a href="#"><fmt:message key="template.footer.ClientRelationManagement"/></a></li>--%>
    <%--<li><a href="#"><fmt:message key="template.footer.AccountingFinance"/></a></li>--%>
    <%--<li><a href="#"><fmt:message key="template.footer.ProjectManagement"/></a></li>--%>
    <%--<li><a href="#"><fmt:message key="template.footer.Ecommerce"/></a></li>--%>
    <%--</ul>--%>
    <%--</td>--%>
    <%--<td>--%>
    <%--<h2><fmt:message key="template.footer.GetConnected"/></h2>--%>
    <%--<ul>--%>
    <%--<li><a href="#"><fmt:message key="template.footer.Facebook"/></a></li>--%>
    <%--<li><a href="#"><fmt:message key="template.footer.LinkedIn"/></a></li>--%>
    <%--<li><a href="#"><fmt:message key="template.footer.Twitter"/></a></li>--%>
    <%--</ul>--%>
    <%--</td>--%>
    <%--<td class="last">--%>
    <%--<img alt="Google APP" src="/customisation/${productNameLower}/images/art_google_app.png">--%>
    <%--</td>--%>
    <%--</tr>--%>
    <%--</tbody></table>--%>
    <%--</div>--%>


    <%--<div class="copyright">2013 &copy;  Oman Datapark | All Rights Reserved</div>--%>

    <%--</div>--%>
    <%--</div>--%>
    <%--<!--END #footer-->--%>

    <%--</div>--%>
    <!--END #footer-->

    <tiles:insertAttribute name="script" ignore="true"/>

    <%
        if (enableCaptcha != null && enableCaptcha) {%>
    <script type="text/javascript">
        Recaptcha.create("<%=EdsContextParams.getRecaptchaPublicKey()%>", "recaptcha", {
            theme: "<%=captchaTheme%>",
            lang: "ar"});
    </script>
    <% } %>
    <%--<script type="text/javascript">--%>
    <%--(function (i, s, o, g, r, a, m) {--%>
    <%--i['GoogleAnalyticsObject'] = r;--%>
    <%--i[r] = i[r] || function () {--%>
    <%--(i[r].q = i[r].q || []).push(arguments)--%>
    <%--}, i[r].l = 1 * new Date();--%>
    <%--a = s.createElement(o),--%>
    <%--m = s.getElementsByTagName(o)[0];--%>
    <%--a.async = 1;--%>
    <%--a.src = g;--%>
    <%--m.parentNode.insertBefore(a, m)--%>
    <%--})(window, document, 'script', '//www.google-analytics.com/analytics.js', 'ga');--%>

    <%--ga('create', 'UA-59981695-9', 'auto');--%>
    <%--ga('send', 'pageview');--%>

    <%--</script>--%>

</div>
<!--END #wrapper-->
</body>
</html>