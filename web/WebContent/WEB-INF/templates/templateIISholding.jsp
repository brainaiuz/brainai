<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en" dir="ltr">
<head>
    <title>
        <tiles:getAsString name="title" ignore="true"/>
    </title>

    <link rel="stylesheet" href="/customisation/iisholding/style.css"/>
    <link rel="stylesheet" href="/customisation/iisholding/appFrame.css"/>
    <link rel="stylesheet" href="/customisation/iisholding/master.css"/>
    <link rel="shortcut icon" href="/customisation/iisholding/images/favicon.ico">
    <!--[if lt IE 9]>
    <script type="text/javascript" src="//html5shiv.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <!-- <meta http-equiv="X-UA-Compatible" content="chrome=1" /> -->
    <!-- for ie -->

    <!--[if lte IE 8]>
    <link rel="stylesheet" href="/customisation/iisholding/fix-ie.css"/>
    <![endif]-->

    <script type="text/javascript" src="/customisation/iisholding/scripts/jquery.js"></script>
    <script type="text/javascript" src="/customisation/iisholding/scripts/common.js"></script>

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
        <a href="/" style=" position: absolute; left: 0; top: 20px; margin-left: 20px !important;">
            <img src="/customisation/iisholding/images/logo.png">
        </a>

        <hgroup>
            <div class="langBar">

            </div>

            <div class="sitePhone">

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
                    <ul class="sub-menu">
                        <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-1802"
                            id="menu-item-1802">
                            <a href="/">All In One</a>
                        </li>
                        <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-551"
                            id="menu-item-551">
                            <a href="/">Project and Resource Management</a>
                            <ul class="sub-menu">
                                <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-1752"
                                    id="menu-item-1752"><a href="/">Timesheet</a></li>
                            </ul>
                        </li>
                        <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-552"
                            id="menu-item-552">
                            <a href="/">CRM and Sales</a>
                        </li>
                        <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-553"
                            id="menu-item-553">
                            <a href="/">HR Management &amp; Payroll</a>
                            <ul class="sub-menu">
                                <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-1754"
                                    id="menu-item-1754"><a href="/">Attendance Tracking</a>
                                </li>
                                <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-597"
                                    id="menu-item-597"><a href="/">Payroll</a></li>
                            </ul>
                        </li>
                        <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-554"
                            id="menu-item-554">
                            <a href="/">Financials and Inventory</a>
                            <ul class="sub-menu">
                                <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-506"
                                    id="menu-item-506"><a href="/">E-commerce</a></li>
                            </ul>
                        </li>
                        <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-556"
                            id="menu-item-556">
                            <a href="/">Reporting and Dashboard</a>
                        </li>
                        <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-555"
                            id="menu-item-555">
                            <a href="/">Document Management</a>
                        </li>
                        <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-534"
                            id="menu-item-534">
                            <a href="/">CMS and Add-Ons</a>
                        </li>
                        <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-2069"
                            id="menu-item-2069">
                            <a href="/">Templates</a>
                            <ul class="sub-menu">
                                <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-2075"
                                    id="menu-item-2075">
                                    <a href="/">Websites forms</a>
                                </li>
                                <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-2074"
                                    id="menu-item-2074">
                                    <a href="/">Invoice templates</a>
                                </li>
                            </ul>
                        </li>
                    </ul>
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
                    <ul class="sub-menu">

                        <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-577"
                            id="menu-item-577">
                            <a href="/">Guidebooks</a>
                        </li>
                        <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-587"
                            id="menu-item-587">
                            <a href="/">FAQ</a>
                        </li>
                        <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-609"
                            id="menu-item-609">
                            <a href="/">Help Video</a>
                        </li>
                        <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-2065"
                            id="menu-item-2065">
                            <a href="/">Partners</a>
                        </li>
                    </ul>
                </li>
                <li class="menu-item menu-item-type-custom menu-item-object-custom menu-item-1977" id="menu-item-1977">
                    <a href="/" target="_blank">Blog</a>
                </li>
                <li class="menu-item menu-item-type-post_type menu-item-object-page current-menu-ancestor current-menu-parent current_page_parent current_page_ancestor menu-item-2170"
                    id="menu-item-2170">
                    <a href="/">About</a>
                    <ul class="sub-menu">
                        <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-590"
                            id="menu-item-590"><a href="/">About iisholding.com</a></li>
                        <li class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-643"
                            id="menu-item-643"><a href="/">Press Release</a></li>
                        <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-683"
                            id="menu-item-683"><a href="/">Customers</a></li>
                        <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-689"
                            id="menu-item-689"><a href="/">Legal</a></li>
                        <li class="menu-item menu-item-type-post_type menu-item-object-page current-menu-item page_item page-item-2 current_page_item menu-item-559"
                            id="menu-item-559"><a href="/">Careers</a></li>
                        <li class="menu-item menu-item-type-custom menu-item-object-custom menu-item-1750"
                            id="menu-item-1750"><a href="/">Blog</a></li>
                    </ul>
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
                                <a href="/">Project/Resource<br> Management</a>
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
                                <a href="/">CRM and<br> Sales</a>
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
                                <a href="/">HR &amp; Payroll</a>
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
                                <a href="/">Financials<br> and Inventory</a>
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
                                <a href="/">CMS and<br> Add-Ons</a>
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
                                <a href="/">Reporting<br> and Dashboard</a>
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
<%--(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){--%>
<%--(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),--%>
<%--m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)--%>
<%--})(window,document,'script','//www.google-analytics.com/analytics.js','ga');--%>

<%--ga('create', 'UA-59981695-15', 'auto');--%>
<%--ga('send', 'pageview');--%>

<%--</script>--%>

</body>
</html>
