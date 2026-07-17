<%@ page import="java.util.Date" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en" dir="ltr">
<head>
    <title>
        <tiles:getAsString name="title" ignore="true"/>
    </title>

    <link rel="stylesheet" href="/customisation/mynfra/style.css"/>
    <link rel="stylesheet" href="/customisation/mynfra/appFrame.css"/>
    <link rel="stylesheet" href="/customisation/mynfra/master.css"/>
    <link href="/customisation/mynfra/images/favicon.ico" rel="shortcut icon">
    <!--[if lt IE 9]>
    <script type="text/javascript" src="//html5shiv.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <!-- <meta http-equiv="X-UA-Compatible" content="chrome=1" /> -->
    <!-- for ie -->

    <!--[if lte IE 8]>
    <link rel="stylesheet" href="/customisation/mynfra/fix-ie.css"/>
    <![endif]-->

    <script type="text/javascript" src="/customisation/mynfra/scripts/jquery.js"></script>
    <script type="text/javascript" src="/customisation/mynfra/scripts/common.js"></script>

    <script type=text/javascript src="/customisation/mynfra/scripts/jquery.placeholder.min.js"></script>
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
        <a href="//www.mynfra.com" id="logo">
            <img src="/customisation/mynfra/images/logo.png">
        </a>

        <hgroup>
            <%--<div class="langBar">--%>
            <%--<ul>--%>
            <%--<li><a href="/" class="langEN">English</a></li>--%>
            <%--<li><a href="//kpi.com/o-que-o-workforcetrack-representa-e-por-que-precisamos-dele"--%>
            <%--class="langPo">Portuguese</a></li>--%>
            <%--<li><a href="http://www.kpi.com.ru/" class="langRU">Russian</a></li>--%>
            <%--<li><a href="//kpi.com/kpi-com-nedir-ve-neden-ihtiyacim-var" class="langTU">Turkey</a></li>--%>
            <%--</ul>--%>
            <%--</div>--%>

            <div class="sitePhone">
                <em>Need Help?</em>
                <span>+3113 82 00 966</span>
            </div>

            <div class="clearBox"></div>
        </hgroup>

        <div class="appActs">
            <a href="/signup/freeSignup.html" class="btn-1">Free Trial</a>
        </div>
        <!-- end logo site -->

        <nav class="menu-main-menu-container">
            <ul class="menu" id="menu-main-menu">
                <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-605" id="menu-item-605">
                    <a href="/">Products</a>
                </li>
                <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-557" id="menu-item-557">
                    <a href="/">Pricing</a>
                    <ul class="sub-menu">
                        <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-539"
                            id="menu-item-539">
                            <a href="/">Pricing Packages</a>
                        </li>
                        <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-538"
                            id="menu-item-538">
                            <a href="/">Comparison</a>
                        </li>
                    </ul>
                </li>
                <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-2079" id="menu-item-2079">
                    <a href="/">Support</a>
                </li>
                <li class="menu-item menu-item-type-custom menu-item-object-custom menu-item-1977" id="menu-item-1977">
                    <a href="/" target="_blank">Blog</a>
                </li>
                <li class="menu-item menu-item-type-post_type menu-item-object-page current-menu-ancestor current-menu-parent current_page_parent current_page_ancestor menu-item-2170"
                    id="menu-item-2170">
                    <a href="/">About</a>
                </li>
                <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-592" id="menu-item-592">
                    <a href="/">Contact Us</a>
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
                                <span>Project/Resource<br> Management</span>
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
                                <span>CRM and<br> Sales</span>
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
                                <span>HR &amp; Payroll</span>
                            </h2>
                            <ul class="menu" id="menu-hr-management-nav">
                                <li>Company Setup</li>
                                <li>Goal Management</li>
                                <li>Performance Appraisals</li>
                                <li>Attendance Tracking</li>
                                <li>Recruitment</li>
                                <li>Employee Profile</li>
                                <li>UK Payroll</li>
                            </ul>
                        </nav>
                    </td>
                    <td>
                        <nav>
                            <h2 class="footNavIcon-4 footNavIcon">
                                <span>Financials<br> and Inventory</span>
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
                                <span>CMS and<br> Add-Ons</span>
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
                                <span>Reporting<br> and Dashboard</span>
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
                    <a href="mailto:support@mynfra.com" target="_blank">Email</a>
                </li>
                <li class="markTwitter socialBookMark menu-item menu-item-type-custom menu-item-object-custom menu-item-19"
                    id="menu-item-19">
                    <a href="//twitter.com/" target="_blank">Twitter</a>
                </li>
                <li class="markLinkedin socialBookMark menu-item menu-item-type-custom menu-item-object-custom menu-item-20"
                    id="menu-item-20">
                    <a href="//www.linkedin.com/" target="_blank">LinkedIn</a>
                </li>
                <li class="markFacebook socialBookMark menu-item menu-item-type-custom menu-item-object-custom menu-item-21"
                    id="menu-item-21">
                    <a href="//www.facebook.com/"
                       target="_blank">Facebook</a>
                </li>
            </ul>

            <div class="sitePhone">
                <em>Need Help?</em>
                <span>+3113 82 00 966 </span>
            </div>

            <div class="footCaption">
                <a href="//mynfra.com/" id="foot-logo">
                    <img alt="Logo" src="/customisation/mynfra/images/logo.png">
                </a>
                <span>Powered by Keles Group B.V.</span>

                <p>
                    2007 &ndash;
                    <%
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(new Date());
                        int year = cal.get(Calendar.YEAR);
                    %>
                    <%=year%> &copy; mynfra.com All Rights Reserved.<br>
                    <a href="//mynfra.com/legal">Terms of Use</a> | <a href="//mynfra.com/legal">Privacy Policy</a>
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

<script type="text/javascript">
    //    if (document.getElementById("liveid")) document.getElementById("liveid").style.display = "block";
</script>
<%
    if (enableCaptcha != null && enableCaptcha) {%>
<script type="text/javascript">
    Recaptcha.create("<%=EdsContextParams.getRecaptchaPublicKey()%>", "recaptcha", {
        theme: "<%=captchaTheme%>"});
</script>
<% } %>

<%--<script type="text/javascript">--%>
<%--(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){--%>
<%--(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),--%>
<%--m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)--%>
<%--})(window,document,'script','//www.google-analytics.com/analytics.js','ga');--%>

<%--ga('create', 'UA-59981695-12', 'auto');--%>
<%--ga('send', 'pageview');--%>

<%--</script>--%>

</body>
</html>
