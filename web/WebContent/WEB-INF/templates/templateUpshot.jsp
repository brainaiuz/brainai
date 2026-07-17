<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en" dir="ltr">
<head>
    <title>
        <tiles:getAsString name="title" ignore="true"/>
    </title>

    <link rel="stylesheet" href="/customisation/erp/style.css"/>
    <link rel="stylesheet" href="/customisation/erp/appFrame.css"/>
    <link rel="stylesheet" href="/customisation/erp/master.css"/>
    <link href="/customisation/erp/images/favicon.ico" rel="shortcut icon">
    <!--[if lt IE 9]>
    <script type="text/javascript" src="//html5shiv.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <!-- <meta http-equiv="X-UA-Compatible" content="chrome=1" /> -->
    <!-- for ie -->

    <!--[if lte IE 8]>
    <link rel="stylesheet" href="/customisation/erp/fix-ie.css"/>
    <![endif]-->

    <script type="text/javascript" src="/customisation/erp/scripts/jquery.js"></script>
    <%
        //Recaptcha script
        Boolean enableCaptcha = request.getAttribute("captcha") != null ? ((Boolean) request.getAttribute("captcha")) : false;
        String captchaTheme = request.getAttribute("captchaTheme") != null ? ((String) request.getAttribute("captchaTheme")) : "white";
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

            <!-- begin logo site -->
            <%--<a href="//www.erp.upshot.ae" id="logo">--%>
            <img src="/customisation/erp/images/logo.png">
            <%--</a>--%>

            <%--<hgroup>--%>
            <%--<div class="langBar">--%>

            <%--</div>--%>

            <%--<div class="sitePhone">--%>
            <%--<em>Need Help?</em>--%>
            <%--<span> +97 1 (0) 562175535 </span>--%>
            <%--<span> +97 1 (0) 502071199 </span>--%>
            <%--</div>--%>

            <%--<div class="clearBox"></div>--%>
            <%--</hgroup>--%>

            <div class="appActs">
                <%--<a href="/signup/freeSignup.html" class="btn-1"> <fmt:message key="freeTrial.title"/> </a>--%>
            </div>
            <!-- end logo site -->


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
            <%--<footer>--%>
            <%--<table class="footer-nav">--%>
            <%--<tbody>--%>
            <%--<tr>--%>
            <%--<td>--%>
            <%--<nav>--%>
            <%--<h2 class="footNavIcon-1 footNavIcon">--%>
            <%--<span> <fmt:message key="template.projectResourceManagement"/> </span>--%>
            <%--</h2>--%>
            <%--<ul class="menu" id="menu-project-management-nav">--%>
            <%--<li> <fmt:message key="template.projectAndTasks"/> </li>--%>
            <%--<li> <fmt:message key="template.ganttChartTool"/> </li>--%>
            <%--<li> <fmt:message key="template.timesheetTool"/> </li>--%>
            <%--<li> <fmt:message key="template.timesheetBilling"/> </li>--%>
            <%--<li> <fmt:message key="template.issueManagement"/> </li>--%>
            <%--<li> <fmt:message key="template.budgeting"/> </li>--%>
            <%--<li> <fmt:message key="template.resourceManagement"/> </li>--%>
            <%--</ul>--%>
            <%--</nav>--%>
            <%--</td>--%>
            <%--<td>--%>
            <%--<nav>--%>
            <%--<h2 class="footNavIcon-2 footNavIcon">--%>
            <%--<span> <fmt:message key="template.crmAndSales"/> </span>--%>
            <%--</h2>--%>
            <%--<ul class="menu" id="menu-cr-management-nav">--%>
            <%--<li> <fmt:message key="template.salesForceAutomation"/> </li>--%>
            <%--<li> <fmt:message key="template.marketingAutomation"/> </li>--%>
            <%--<li> <fmt:message key="template.caseManagement"/> </li>--%>
            <%--<li> <fmt:message key="template.massMailing"/> </li>--%>
            <%--<li> <fmt:message key="template.messageCenter"/> </li>--%>
            <%--<li> <fmt:message key="template.accountsContactsManagement"/> </li>--%>
            <%--</ul>--%>
            <%--</nav>--%>
            <%--</td>--%>
            <%--<td>--%>
            <%--<nav>--%>
            <%--<h2 class="footNavIcon-3 footNavIcon">--%>
            <%--<span> <fmt:message key="template.hrAndPayroll"/> </span>--%>
            <%--</h2>--%>
            <%--<ul class="menu" id="menu-hr-management-nav">--%>
            <%--<li> <fmt:message key="template.companySetup"/> </li>--%>
            <%--<li> <fmt:message key="template.goalManagement"/> </li>--%>
            <%--<li> <fmt:message key="template.performanceAppraisals"/> </li>--%>
            <%--<li> <fmt:message key="template.attendanceTracking"/> </li>--%>
            <%--<li> <fmt:message key="template.recruitment"/> </li>--%>
            <%--<li> <fmt:message key="template.employeeProfile"/> </li>--%>
            <%--<li> <fmt:message key="template.ukPayroll"/> </li>--%>
            <%--</ul>--%>
            <%--</nav>--%>
            <%--</td>--%>
            <%--<td>--%>
            <%--<nav>--%>
            <%--<h2 class="footNavIcon-4 footNavIcon">--%>
            <%--<span> <fmt:message key="template.financialsAndInventory"/> </span>--%>
            <%--</h2>--%>
            <%--<ul class="menu" id="menu-financials-and-payroll-nav">--%>
            <%--<li> <fmt:message key="template.sales"/></li>--%>
            <%--<li> <fmt:message key="template.purchases"/></li>--%>
            <%--<li> <fmt:message key="template.banking"/></li>--%>
            <%--<li> <fmt:message key="template.inventoryManagement"/></li>--%>
            <%--<li> <fmt:message key="template.expenseClaims"/></li>--%>
            <%--<li> <fmt:message key="template.storefront"/></li>--%>
            <%--<li> <fmt:message key="template.reports"/></li>--%>
            <%--</ul>--%>
            <%--</nav>--%>
            <%--</td>--%>
            <%--<td>--%>
            <%--<nav>--%>
            <%--<h2 class="footNavIcon-6 footNavIcon">--%>
            <%--<span> <fmt:message key="template.cmsAndAddOns"/> </span>--%>
            <%--</h2>--%>
            <%--<ul class="menu" id="menu-add-ons-nav">--%>
            <%--<li> <fmt:message key="template.websitesAndCatalogs"/> </li>--%>
            <%--<li> <fmt:message key="template.customWebsites"/> </li>--%>
            <%--<li> <fmt:message key="template.mobileApps"/> </li>--%>
            <%--<li> <fmt:message key="template.officePlugins"/> </li>--%>
            <%--<li> <fmt:message key="template.blogsWikis"/> </li>--%>
            <%--</ul>--%>
            <%--</nav>--%>
            <%--</td>--%>
            <%--<td>--%>
            <%--<nav>--%>
            <%--<h2 class="footNavIcon-5 footNavIcon">--%>
            <%--<span> <fmt:message key="template.reportingAndDashboard"/> </span>--%>
            <%--</h2>--%>
            <%--<ul class="menu" id="menu-reporting-and-dashboard-nav">--%>
            <%--<li> <fmt:message key="template.customReports"/> </li>--%>
            <%--<li> <fmt:message key="template.predefinedTemplates"/> </li>--%>
            <%--<li> <fmt:message key="template.customCharts"/> </li>--%>
            <%--<li> <fmt:message key="template.importingExporting"/> </li>--%>
            <%--<li> <fmt:message key="template.dashboards"/> </li>--%>
            <%--</ul>--%>
            <%--</nav>--%>
            <%--</td>--%>
            <%--</tr>--%>
            <%--</tbody>--%>
            <%--</table>--%>

            <%--<ul class="menu" id="menu-stay-connected">--%>
            <%--<li class="markEmail socialBookMark menu-item menu-item-type-custom menu-item-object-custom menu-item-18"--%>
            <%--id="menu-item-18">--%>
            <%--<a href="mailto:support@upshot.ae" target="_blank">Email</a>--%>
            <%--</li>--%>
            <%--<li class="markTwitter socialBookMark menu-item menu-item-type-custom menu-item-object-custom menu-item-19"--%>
            <%--id="menu-item-19">--%>
            <%--<a href="http://twitter.com/upshotuae" target="_blank">Twitter</a>--%>
            <%--</li>--%>
            <%--&lt;%&ndash;<li class="markLinkedin socialBookMark menu-item menu-item-type-custom menu-item-object-custom menu-item-20"--%>
            <%--id="menu-item-20">--%>
            <%--<a href="//www.linkedin.com/company/" target="_blank">LinkedIn</a>--%>
            <%--</li>&ndash;%&gt;--%>
            <%--<li class="markFacebook socialBookMark menu-item menu-item-type-custom menu-item-object-custom menu-item-21"--%>
            <%--id="menu-item-21">--%>
            <%--<a href="http://www.facebook.com/pages/Upshot/255991001168244" target="_blank">Facebook</a>--%>
            <%--</li>--%>
            <%--</ul>--%>

            <%--<div class="sitePhone">--%>
            <%--<em>Need Help?</em>--%>
            <%--<span> +97 1 (0) 562175535 </span>--%>
            <%--<span> +97 1 (0) 502071199 </span>--%>
            <%--</div>--%>


            <div class="footCaption" style="margin-left:75%;font-weight: bold;padding-bottom: 5px;">
                <%--<img alt="Upshot" src="/customisation/erp/images/logo.png">--%>

                <a href="http://www.afghanidhost.com" rel="nofollow">Powered by Upshot Technology Services</a>

                <%--<p>--%>
                <%--2007 &ndash;--%>
                <%--<%--%>
                <%--Calendar cal = Calendar.getInstance();--%>
                <%--cal.setTime(new Date());--%>
                <%--int year = cal.get(Calendar.YEAR);--%>
                <%--%>--%>
                <%--<%=year%> &copy; upshot.ae All Rights Reserved.<br>--%>
                <%--<a href="//upshot.ae/legal">Terms of Use</a> | <a href="//upshot.ae/legal">Privacy Policy</a>--%>
                <%--</p>--%>
            </div>

            <%--<div class="clearBox"></div>--%>
            <%--</footer>--%>

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

<script type="text/javascript">
    if (document.getElementById("breadCrump")) document.getElementById("breadCrump").style.display = "none";
    if (document.getElementById("special")) document.getElementById("special").style.display = "none";
</script>

<%--<script type="text/javascript">--%>
<%--(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){--%>
<%--(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),--%>
<%--m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)--%>
<%--})(window,document,'script','//www.google-analytics.com/analytics.js','ga');--%>

<%--ga('create', 'UA-59981695-16', 'auto');--%>
<%--ga('send', 'pageview');--%>

<%--</script>--%>

</body>
</html>
