<%@ page import="java.util.Date" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en" dir="ltr">
<head>
    <title>
        <tiles:getAsString name="title" ignore="true"/>
    </title>

    <link rel="stylesheet" href="/customisation/alkawader/style.css"/>
    <link rel="stylesheet" href="/customisation/alkawader/appFrame.css"/>
    <link rel="stylesheet" href="/customisation/alkawader/master.css"/>
    <link href="//apps.alkawader.com/customisation/alkawader/images/favicon.ico" rel="shortcut icon">
    <!--[if lt IE 9]>
    <script type="text/javascript" src="//html5shiv.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <!-- <meta http-equiv="X-UA-Compatible" content="chrome=1" /> -->
    <!-- for ie -->

    <!--[if lte IE 8]>
    <link rel="stylesheet" href="/customisation/alkawader/fix-ie.css"/>
    <![endif]-->

    <script type="text/javascript" src="/customisation/alkawader/scripts/jquery.js"></script>
    <script type="text/javascript" src="/customisation/alkawader/scripts/common.js"></script>

    <SCRIPT type=text/javascript src="/customisation/alkawader/jquery.placeholder.min.js"></SCRIPT>

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
        <a href="http://www.alkawader.com" id="logo">
            <img style="width: 215px; height: 50px; padding-top: 15px;" src="/customisation/alkawader/images/logo.png">
        </a>

        <hgroup>
            <%--<div class="langBar">--%>
            <%--<ul>--%>
            <%--<li><a href="/" class="langEN">English</a></li>--%>
            <%--<li><a href="//vipworkspace.com/o-que-o-workforcetrack-representa-e-por-que-precisamos-dele" class="langPo">Portuguese</a></li>--%>
            <%--<li><a href="http://www.vipworkspace.com.ru/" class="langRU">Russian</a></li>--%>
            <%--<li><a href="//vipworkspace.com/kpi-com-nedir-ve-neden-ihtiyacim-var" class="langTU">Turkey</a></li>--%>
            <%--</ul>--%>
            <%--</div>--%>

            <div class="sitePhone">
                <em>Need Help?</em>
                <span>+966 12 653 3334</span>
                <span>+966 92 002 3235</span>
            </div>

            <div class="clearBox"></div>
        </hgroup>

        <div class="appActs">
            <a href="/signup/freeSignup.html" class="btn-1">Free Trial</a>
        </div>

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
                                Project/Resource<br> Management
                            </h2>
                            <ul class="menu" id="menu-project-management-nav">
                                <li>Projects and Tasks</li>
                                <li>Gantt Chart Tool</li>
                                <li>Timesheet Tool</li>
                                <li>Timesheet Billing</li>
                                <li>Issue Management</li>
                                <li>Budgeting</li>
                                <li>Resource Management</li>
                            </ul>
                        </nav>
                    </td>
                    <td>
                        <nav>
                            <h2 class="footNavIcon-2 footNavIcon">
                                CRM and<br> Sales
                            </h2>
                            <ul class="menu" id="menu-cr-management-nav">
                                <li>Sales Force Automation</li>
                                <li>Marketing Automation</li>
                                <li>Case Management</li>
                                <li>Mass Mailing</li>
                                <li>Message Center/Emails</li>
                                <li>Accounts/ Contacts Management</li>
                            </ul>
                        </nav>
                    </td>
                    <td>
                        <nav>
                            <h2 class="footNavIcon-3 footNavIcon">
                                HR &amp; Payroll
                            </h2>
                            <ul class="menu" id="menu-hr-management-nav">
                                <li>Company Setup</li>
                                <li>Goal Management</li>
                                <li>Performance Appraisals</li>
                                <li>Attendance Tracking</li>
                                <li>Recruitment</li>
                                <li>Employee Profile</li>
                                <li>Payroll</li>
                            </ul>
                        </nav>
                    </td>
                    <td>
                        <nav>
                            <h2 class="footNavIcon-4 footNavIcon">
                                Financials<br> and Inventory
                            </h2>
                            <ul class="menu" id="menu-financials-and-payroll-nav">
                                <li>Sales</li>
                                <li>Purchases</li>
                                <li>Banking</li>
                                <li>Inventory Management</li>
                                <li>Expense Claims</li>
                                <li>Storefront</li>
                                <li>Reports</li>
                            </ul>
                        </nav>
                    </td>
                    <td>
                        <nav>
                            <h2 class="footNavIcon-6 footNavIcon">
                                CMS and<br> Add-Ons
                            </h2>
                            <ul class="menu" id="menu-add-ons-nav">
                                <li>Websites and Catalogs</li>
                                <li>Custom Websites</li>
                                <li>Mobile Apps</li>
                                <li>Office plugins</li>
                                <li>Blogs/Wikis</li>
                            </ul>
                        </nav>
                    </td>
                    <td>
                        <nav>
                            <h2 class="footNavIcon-5 footNavIcon">
                                Reporting<br> and Dashboard
                            </h2>
                            <ul class="menu" id="menu-reporting-and-dashboard-nav">
                                <li>Custom Reports</li>
                                <li>Predefined templates</li>
                                <li>Custom charts</li>
                                <li>Importing /Exporting</li>
                                <li>Dashboards</li>
                            </ul>
                        </nav>
                    </td>
                </tr>
                </tbody>
            </table>

            <ul class="menu" id="menu-stay-connected">
                <li class="markEmail socialBookMark menu-item menu-item-type-custom menu-item-object-custom menu-item-18"
                    id="menu-item-18">
                    <a href="mailto:support@taqneen.com" target="_blank">Email</a>
                </li>
                <li class="markTwitter socialBookMark menu-item menu-item-type-custom menu-item-object-custom menu-item-19"
                    id="menu-item-19">
                    <a href="https://www.twitter.com/" target="_blank">Twitter</a>
                </li>
                <li class="markLinkedin socialBookMark menu-item menu-item-type-custom menu-item-object-custom menu-item-20"
                    id="menu-item-20">
                    <a href="https://www.linkedin.com/" target="_blank">LinkedIn</a>
                </li>
                <li class="markFacebook socialBookMark menu-item menu-item-type-custom menu-item-object-custom menu-item-21"
                    id="menu-item-21">
                    <a href="http://www.facebook.com/" target="_blank">Facebook</a>
                </li>
            </ul>

            <div class="sitePhone">
                <em>Need Help?</em>
                <span>+966 12 653 3334</span>
                <span>+966 92 002 3235</span>
            </div>

            <div class="footCaption">
                <a href="http://www.alkawader.com" id="foot-logo">
                    <img alt="Logo" style="width:130px;height:36px;" src="/customisation/alkawader/images/logo.png">
                </a>
                <span>Taqneen Solutions</span>

                <p>
                    2007 &ndash;
                    <%
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(new Date());
                        int year = cal.get(Calendar.YEAR);
                    %>
                    <%=year%> &copy; alkawader.com All Rights Reserved.<br>
                    <a href="//alkawader.com/legal">Terms of Use</a> | <a href="//alkawader.com/legal">Privacy Policy</a>
                </p>
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
<%--a = s.createElement(o), m = s.getElementsByTagName(o)[0];--%>
<%--a.async = 1;--%>
<%--a.src = g;--%>
<%--m.parentNode.insertBefore(a, m)--%>
<%--})(window, document, 'script', '//www.google-analytics.com/analytics.js', 'ga');--%>
<%--ga('create', '<c:out value="${analytics_code}"/>', 'auto');--%>
<%--ga('send', 'pageview');--%>
<%--if (document.getElementById("signupLink")) document.getElementById("signupLink").remove();--%>
<%--if (document.getElementById("forgot")) document.getElementById("forgot").remove();--%>
<%--</script>--%>

</body>
</html>
