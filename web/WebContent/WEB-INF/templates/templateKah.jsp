<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en" dir="ltr">
<head>
    <title>
        <tiles:getAsString name="title" ignore="true"/>
    </title>

    <link rel="stylesheet" href="/customisation/kah+erp/style.css"/>
    <link rel="stylesheet" href="/customisation/kah+erp/appFrame.css"/>
    <link rel="stylesheet" href="/customisation/kah+erp/master.css"/>
    <link rel="shortcut icon" href="/customisation/kah+erp/images/favicon.ico">
    <!--[if lt IE 9]>
    <script type="text/javascript" src="//html5shiv.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <!-- <meta http-equiv="X-UA-Compatible" content="chrome=1" /> -->
    <!-- for ie -->

    <!--[if lte IE 8]>
    <link rel="stylesheet" href="/customisation/kah+erp/fix-ie.css"/>
    <![endif]-->

    <script type="text/javascript" src="/customisation/kah+erp/scripts/jquery.js"></script>
    <script type="text/javascript" src="/customisation/kah+erp/scripts/common.js"></script>

    <%
        //Recaptcha script
        Boolean enableCaptcha = request.getAttribute("captcha") != null
                ? ((Boolean) request.getAttribute("captcha"))
                : false;
        String captchaTheme = request.getAttribute("captchaTheme") != null
                ? ((String) request.getAttribute("captchaTheme"))
                : "white";
        if (enableCaptcha) {
    %>
    <script type="text/javascript" src="https://www.google.com/recaptcha/api/js/recaptcha_ajax.js"></script>
    <% } %>
    <tiles:insertAttribute name="style" ignore="true"/>

</head>

<body>

<!-- begin wrapper -->
<div id="wrapper">

<!--begin header-->
<header>
<div class="ZoneIn">

<div class="langBar">
    <ul>
        <li><a href="?locale=ar"><fmt:message key="template.header.Arabic"/> </a></li>
        <li><a href="?locale=en"> <fmt:message key="template.header.English"/> </a></li>
    </ul>
</div>

<!-- begin logo site -->
<a href="/" style=" position: absolute; left: 0; top: 20px; margin-left: 100px !important;">
    <img src="/customisation/kah+erp/images/logo.png">
</a>

<hgroup>
    <div class="langBar">

    </div>

    <div class="sitePhone">

    </div>

    <div class="clearBox"></div>
</hgroup>

<div class="appActs">
    <a href="/signup/freeSignup.html" class="btn-1"> <fmt:message key="freeTrial.title"/> </a>
</div>
<!-- end logo site -->

<nav class="menu-main-menu-container">
    <ul class="menu" id="menu-main-menu">
        <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-605" id="menu-item-605">
            <a href="//${helpHost}/product-tour/"> <fmt:message key="productOnly"/> </a>
            <ul class="sub-menu">
                <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-1802"
                    id="menu-item-1802">
                    <a href="//${helpHost}/all-in-one/"> <fmt:message key="template.footer.allInOne"/> </a>
                </li>
                <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-551"
                    id="menu-item-551">
                    <a href="//${helpHost}/project-management/">
                        <fmt:message key="template.footer.projectAndResourceManagement"/>
                    </a>
                    <ul class="sub-menu">
                        <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-1752"
                            id="menu-item-1752"><a href="//${helpHost}/timesheet/">
                            <fmt:message key="frontendmain.workforceTrackTimesheet"/>
                        </a></li>
                    </ul>
                </li>
                <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-552"
                    id="menu-item-552">
                    <a href="//${helpHost}/crm/">
                        <fmt:message key="template.footer.crmAndSales"/>
                    </a>
                </li>
                <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-553"
                    id="menu-item-553">
                    <a href="//${helpHost}/hrms/">
                        <fmt:message key="template.footer.hrManagement"/>
                    </a>
                    <ul class="sub-menu">
                        <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-1754"
                            id="menu-item-1754"><a href="//${helpHost}/attendance-tracking/">
                            <fmt:message key="frontendmain.attendanceTracking"/>
                        </a>
                        </li>
                        <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-597"
                            id="menu-item-597"><a href="//${helpHost}/payroll/">
                            <fmt:message key="frontendmain.payroll"/>
                        </a></li>
                    </ul>
                </li>
                <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-554"
                    id="menu-item-554">
                    <a href="//${helpHost}/accounting/">
                        <fmt:message key="template.footer.financialsAndInventory"/>
                    </a>
                    <ul class="sub-menu">
                        <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-506"
                            id="menu-item-506"><a href="//${helpHost}/e-commerce/">
                            <fmt:message key="template.footer.e-commerce"/>
                        </a>
                        </li>
                    </ul>
                </li>
                <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-556"
                    id="menu-item-556">
                    <a href="//${helpHost}/reporting/">
                        <fmt:message key="template.footer.reportingAndDashboard"/>
                    </a>
                </li>
                <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-555"
                    id="menu-item-555">
                    <a href="//${helpHost}/document-management/">
                        <fmt:message key="frontendmain.documentsManagement"/>
                    </a>
                </li>
                <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-534"
                    id="menu-item-534">
                    <a href="//${helpHost}/add-on/">
                        <fmt:message key="template.footer.cmsAndAdd-Ons"/>
                    </a>
                </li>
                <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-2069"
                    id="menu-item-2069">
                    <a href="//${helpHost}/templates/">
                        <fmt:message key="template.footer.templates"/>
                    </a>
                    <ul class="sub-menu">
                        <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-2075"
                            id="menu-item-2075">
                            <a href="//${helpHost}/website-templates/">
                                <fmt:message key="template.footer.websitesForms"/>
                            </a>
                        </li>
                        <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-2074"
                            id="menu-item-2074">
                            <a href="//${helpHost}/invoice-templates/">
                                <fmt:message key="template.footer.invoiceTemplates"/>
                            </a>
                        </li>
                    </ul>
                </li>
            </ul>
        </li>

        <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-557" id="menu-item-557">
            <a href="//${helpHost}/pricing/"> <fmt:message key="frontendmain.pricing"/> </a>
            <ul class="sub-menu">
                <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-539"
                    id="menu-item-539">
                    <a href="//${helpHost}/pricing/"> <fmt:message key="template.footer.pricingPackages"/> </a>
                </li>
                <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-538"
                    id="menu-item-538">
                    <a href="//${helpHost}/comparison/"> <fmt:message key="template.footer.comparison"/> </a>
                </li>
            </ul>
        </li>

        <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-2079" id="menu-item-2079">
            <a href="//${helpHost}/support/"> <fmt:message key="frontendmain.helpSupport"/> </a>
            <ul class="sub-menu">

                <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-577"
                    id="menu-item-577">
                    <a href="//${helpHost}/guidebooks/"> <fmt:message key="frontendmain.guidebooks"/> </a>
                </li>
                <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-587"
                    id="menu-item-587">
                    <a href="//${helpHost}/faq/"> <fmt:message key="template.footer.FAQ"/> </a>
                </li>
                <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-609"
                    id="menu-item-609">
                    <a href="//${helpHost}/help-video/"> <fmt:message key="helpVideo"/> </a>
                </li>
                <%-- <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-2065"
                     id="menu-item-2065">
                     <a href="/"> <fmt:message key="frontendmain.partners"/> </a>&ndash;%&gt;

                 </li>--%>
            </ul>
        </li>

        <li class="menu-item menu-item-type-custom menu-item-object-custom menu-item-1977" id="menu-item-1977">
            <a href="http://blog.kpi.com/" target="_blank"> <fmt:message key="frontendmain.blog"/> </a>
        </li>

        <li class="menu-item menu-item-type-post_type menu-item-object-page current-menu-ancestor current-menu-parent current_page_parent current_page_ancestor menu-item-2170"
            id="menu-item-2170">
            <a href="//${helpHost}/about/"> <fmt:message key="frontendmain.about"/> </a>
            <ul class="sub-menu">
                <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-590" id="menu-item-590">
                    <a href="//${helpHost}/about-kpi-com/">
                        <fmt:message key="frontendmain.about"/> kah.sa
                    </a>
                </li>
                <li class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-643"
                    id="menu-item-643"><a href="//${helpHost}/category/press-release/">
                    <fmt:message key="template.footer.pressRelease"/>
                </a></li>
                <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-683"
                    id="menu-item-683"><a href="//${helpHost}/customers/">
                    <fmt:message key="frontendmain.customers"/>
                </a></li>
                <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-689"
                    id="menu-item-689"><a href="//${helpHost}/legal/">
                    <fmt:message key="template.footer.legal"/>
                </a></li>
                <li class="menu-item menu-item-type-post_type menu-item-object-page current-menu-item page_item page-item-2 current_page_item menu-item-559"
                    id="menu-item-559"><a href="//${helpHost}/careers/">
                    <fmt:message key="frontendmain.Careers"/>
                </a></li>
                <li class="menu-item menu-item-type-custom menu-item-object-custom menu-item-1750"
                    id="menu-item-1750"><a href="http://blog.kpi.com/">
                    <fmt:message key="frontendmain.blog"/>
                </a></li>
            </ul>
        </li>

        <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-592" id="menu-item-592">
            <a href="//${helpHost}/contact-us/"> <fmt:message key="frontendmain.contactUsOnly"/> </a>
        </li>
    </ul>
</nav>

<div class="clearBox"></div>
</div>
</header>
<!--END header-->

<!--begin #cover-->
<div id="cover">

    <!--begin #content-->
    <div id="content" class="full-main">


        <tiles:insertAttribute name="body" ignore="false"/>

        <!--begin footer-->
        <footer>
            <table class="footer-nav">
                <tbody>
                <tr>
                    <td>
                        <nav>
                            <h2 class="footNavIcon-1 footNavIcon">
                                <a href="#"> <fmt:message key="template.projectResourceManagement"/> </a>
                            </h2>
                            <ul class="menu" id="menu-project-management-nav">
                                <li><fmt:message key="template.projectAndTasks"/></li>
                                <li><fmt:message key="template.ganttChartTool"/></li>
                                <li><fmt:message key="template.timesheetTool"/></li>
                                <li><fmt:message key="template.timesheetBilling"/></li>
                                <li><fmt:message key="template.issueManagement"/></li>
                                <li><fmt:message key="template.budgeting"/></li>
                                <li><fmt:message key="template.resourceManagement"/></li>
                            </ul>
                        </nav>
                    </td>
                    <td>
                        <nav>
                            <h2 class="footNavIcon-2 footNavIcon">
                                <a href="#"> <fmt:message key="template.crmAndSales"/> </a>
                            </h2>
                            <ul class="menu" id="menu-cr-management-nav">
                                <li><fmt:message key="template.salesForceAutomation"/></li>
                                <li><fmt:message key="template.marketingAutomation"/></li>
                                <li><fmt:message key="template.caseManagement"/></li>
                                <li><fmt:message key="template.massMailing"/></li>
                                <li><fmt:message key="template.messageCenter"/></li>
                                <li><fmt:message key="template.accountsContactsManagement"/></li>
                            </ul>
                        </nav>
                    </td>
                    <td>
                        <nav>
                            <h2 class="footNavIcon-3 footNavIcon">
                                <a href="#"> <fmt:message key="template.hrAndPayroll"/> </a>
                            </h2>
                            <ul class="menu" id="menu-hr-management-nav">
                                <li><fmt:message key="template.companySetup"/></li>
                                <li><fmt:message key="template.goalManagement"/></li>
                                <li><fmt:message key="template.performanceAppraisals"/></li>
                                <li><fmt:message key="template.attendanceTracking"/></li>
                                <li><fmt:message key="template.recruitment"/></li>
                                <li><fmt:message key="template.employeeProfile"/></li>
                                <li><fmt:message key="template.ukPayroll"/></li>
                            </ul>
                        </nav>
                    </td>
                    <td>
                        <nav>
                            <h2 class="footNavIcon-4 footNavIcon">
                                <a href="#"> <fmt:message key="template.financialsAndInventory"/> </a>
                            </h2>
                            <ul class="menu" id="menu-financials-and-payroll-nav">
                                <li><fmt:message key="template.sales"/></li>
                                <li><fmt:message key="template.purchases"/></li>
                                <li><fmt:message key="template.banking"/></li>
                                <li><fmt:message key="template.inventoryManagement"/></li>
                                <li><fmt:message key="template.expenseClaims"/></li>
                                <li><fmt:message key="template.storefront"/></li>
                                <li><fmt:message key="template.reports"/></li>
                            </ul>
                        </nav>
                    </td>
                    <td>
                        <nav>
                            <h2 class="footNavIcon-6 footNavIcon">
                                <a href="#"> <fmt:message key="template.cmsAndAddOns"/> </a>
                            </h2>
                            <ul class="menu" id="menu-add-ons-nav">
                                <li><fmt:message key="template.websitesAndCatalogs"/></li>
                                <li><fmt:message key="template.customWebsites"/></li>
                                <li><fmt:message key="template.mobileApps"/></li>
                                <li><fmt:message key="template.officePlugins"/></li>
                                <li><fmt:message key="template.blogsWikis"/></li>
                            </ul>
                        </nav>
                    </td>
                    <td>
                        <nav>
                            <h2 class="footNavIcon-5 footNavIcon">
                                <a href="#"> <fmt:message key="template.reportingAndDashboard"/> </a>
                            </h2>
                            <ul class="menu" id="menu-reporting-and-dashboard-nav">
                                <li><fmt:message key="template.customReports"/></li>
                                <li><fmt:message key="template.predefinedTemplates"/></li>
                                <li><fmt:message key="template.customCharts"/></li>
                                <li><fmt:message key="template.importingExporting"/></li>
                                <li><fmt:message key="template.dashboards"/></li>
                            </ul>
                        </nav>
                    </td>
                </tr>
                </tbody>
            </table>


            <div class="sitePhone">

            </div>

            <div class="footCaption">


            </div>

            <div class="clearBox"></div>
        </footer>

        <!--END footer-->

    </div>
    <!--END #content-->

</div>
<!--END #cover-->


</div>
<!-- END #wrapper -->

<tiles:insertAttribute name="script" ignore="true"/>

<%
    if (enableCaptcha != null && enableCaptcha) {%>
<script type="text/javascript">
    Recaptcha.create("<%=EdsContextParams.getRecaptchaPublicKey()%>", "recaptcha", {
        theme: "<%=captchaTheme%>"});
</script>
<% } %>

<%if ((request.getRequestURI().endsWith("welcomePage.jsp") || (request.getRequestURI().endsWith("freeTrial.jsp"))) && isRussian) {%>
<!-- Google Code for Free Trial Sign up Conversion Page-->
<script type="text/javascript">
    /* <![CDATA[ */
    var google_conversion_id = 952099144;
    var google_conversion_language = "en";
    var google_conversion_format = "2";
    var google_conversion_color = "ffffff";
    var google_conversion_label = "LheeCLj90QMQyML_xQM";
    var google_conversion_value = 0;
    /*] ]> */
</script>
<script type="text/javascript" src="//www.googleadservices.com/pagead/conversion.js">
</script>
<noscript>
    <div style="display:inline;">
        <img height="1" width="1" style="border-style:none;" alt=""
             src="//www.googleadservices.com/pagead/conversion/952099144/?label=LheeCLj90QMQyML_xQM&guid=ON&script=0"/>
    </div>
</noscript>
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

<%--ga('create', 'UA-59981695-4', 'auto');--%>
<%--ga('send', 'pageview');--%>

<%--</script>--%>

</body>
</html>
