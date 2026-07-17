<%@ page import="java.util.Date" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en" dir="ltr">
<head>
    <title>
        <tiles:getAsString name="title" ignore="true"/>
    </title>

    <link rel="stylesheet" href="/customisation/goodsystems/style.css"/>
    <link rel="stylesheet" href="/customisation/goodsystems/appFrame.css"/>
    <link rel="stylesheet" href="/customisation/goodsystems/master.css"/>
    <link href="/http://app.goodsystems.com.au/customisation/goodsystems/images/favicon.ico" rel="shortcut icon">
    <!--[if lt IE 9]>
    <script type="text/javascript" src="//html5shiv.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <!-- <meta http-equiv="X-UA-Compatible" content="chrome=1" /> -->
    <!-- for ie -->

    <!--[if lte IE 8]>
    <link rel="stylesheet" href="/customisation/goodsystems/fix-ie.css"/>
    <![endif]-->

    <script type="text/javascript" src="/customisation/goodsystems/scripts/jquery.js"></script>
    <script type="text/javascript" src="/customisation/goodsystems/scripts/common.js"></script>

    <SCRIPT type=text/javascript src="/customisation/goodsystems/jquery.placeholder.min.js"></SCRIPT>

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
        <a href="http://www.goodsystems.com.au" id="logo">
            <img style="margin-left: -28px;" src="/customisation/goodsystems/images/logo.png">
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
                <span>1300 85 75 60</span>
            </div>

            <div class="clearBox"></div>
        </hgroup>

        <%-- <div class="appActs">
             <a href="/signup/freeSignup.html" class="btn-1">Free Trial</a>
         </div>--%>
        <!-- end logo site -->

        <%--<nav class="menu-main-menu-container">--%>
        <%--<ul class="menu" id="menu-main-menu">--%>
        <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-605" id="menu-item-605">--%>
        <%--<a href="//vipworkspace.com/product-tour/">Products</a>--%>
        <%--<ul class="sub-menu">--%>
        <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-1802"--%>
        <%--id="menu-item-1802">--%>
        <%--<a href="//vipworkspace.com/all-in-one/">All In One</a>--%>
        <%--</li>--%>
        <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-551"--%>
        <%--id="menu-item-551">--%>
        <%--<a href="//vipworkspace.com/project-management/">Project and Resource Management</a>--%>
        <%--<ul class="sub-menu">--%>
        <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-1752"--%>
        <%--id="menu-item-1752"><a href="//vipworkspace.com/timesheet/">Timesheet</a></li>--%>
        <%--</ul>--%>
        <%--</li>--%>
        <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-552"--%>
        <%--id="menu-item-552">--%>
        <%--<a href="//vipworkspace.com/crm/">CRM and Sales</a>--%>
        <%--</li>--%>
        <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-553"--%>
        <%--id="menu-item-553">--%>
        <%--<a href="//vipworkspace.com/hrms/">HR Management &amp; Payroll</a>--%>
        <%--<ul class="sub-menu">--%>
        <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-1754"--%>
        <%--id="menu-item-1754"><a href="//vipworkspace.com/attendance-tracking/">Attendance Tracking</a>--%>
        <%--</li>--%>
        <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-597"--%>
        <%--id="menu-item-597"><a href="//vipworkspace.com/payroll/">Payroll</a></li>--%>
        <%--</ul>--%>
        <%--</li>--%>
        <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-554"--%>
        <%--id="menu-item-554">--%>
        <%--<a href="//vipworkspace.com/accounting/">Financials and Inventory</a>--%>
        <%--<ul class="sub-menu">--%>
        <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-506"--%>
        <%--id="menu-item-506"><a href="//vipworkspace.com/e-commerce/">E-commerce</a></li>--%>
        <%--</ul>--%>
        <%--</li>--%>
        <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-556"--%>
        <%--id="menu-item-556">--%>
        <%--<a href="//vipworkspace.com/reporting/">Reporting and Dashboard</a>--%>
        <%--</li>--%>
        <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-555"--%>
        <%--id="menu-item-555">--%>
        <%--<a href="//vipworkspace.com/document-management/">Document Management</a>--%>
        <%--</li>--%>
        <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-534"--%>
        <%--id="menu-item-534">--%>
        <%--<a href="//vipworkspace.com/add-on/">CMS and Add-Ons</a>--%>
        <%--</li>--%>
        <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-2069"--%>
        <%--id="menu-item-2069">--%>
        <%--<a href="//vipworkspace.com/templates/">Templates</a>--%>
        <%--<ul class="sub-menu">--%>
        <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-2075"--%>
        <%--id="menu-item-2075">--%>
        <%--<a href="//vipworkspace.com/websites-forms/">Websites forms</a>--%>
        <%--</li>--%>
        <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-2074"--%>
        <%--id="menu-item-2074">--%>
        <%--<a href="//vipworkspace.com/invoice-templates/">Invoice templates</a>--%>
        <%--</li>--%>
        <%--</ul>--%>
        <%--</li>--%>
        <%--</ul>--%>
        <%--</li>--%>
        <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-557" id="menu-item-557">--%>
        <%--<a href="//vipworkspace.com/pricing/">Pricing</a>--%>
        <%--<ul class="sub-menu">--%>
        <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-539"--%>
        <%--id="menu-item-539">--%>
        <%--<a href="//vipworkspace.com/pricing/">Pricing Packages</a>--%>
        <%--</li>--%>
        <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-538"--%>
        <%--id="menu-item-538">--%>
        <%--<a href="//vipworkspace.com/comparison/">Comparison</a>--%>
        <%--</li>--%>
        <%--</ul>--%>
        <%--</li>--%>
        <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-2079" id="menu-item-2079">--%>
        <%--<a href="//vipworkspace.com/support/">Support</a>--%>
        <%--<ul class="sub-menu">--%>
        <%--<li class="menu-item menu-item-type-custom menu-item-object-custom menu-item-2023"--%>
        <%--id="menu-item-2023">--%>
        <%--<a href="http://wiki.vipworkspace.com" target="_blank">Wiki</a>--%>
        <%--</li>--%>
        <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-577"--%>
        <%--id="menu-item-577">--%>
        <%--<a href="//vipworkspace.com/guidebooks/">Guidebooks</a>--%>
        <%--</li>--%>
        <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-587"--%>
        <%--id="menu-item-587">--%>
        <%--<a href="//vipworkspace.com/faq/">FAQ</a>--%>
        <%--</li>--%>
        <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-609"--%>
        <%--id="menu-item-609">--%>
        <%--<a href="//vipworkspace.com/help-video/">Help Video</a>--%>
        <%--</li>--%>
        <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-2065"--%>
        <%--id="menu-item-2065">--%>
        <%--<a href="//vipworkspace.com/partners-page/">Partners</a>--%>
        <%--</li>--%>
        <%--</ul>--%>
        <%--</li>--%>
        <%--<li class="menu-item menu-item-type-custom menu-item-object-custom menu-item-1977" id="menu-item-1977">--%>
        <%--<a href="http://blog.vipworkspace.com" target="_blank">Blog</a>--%>
        <%--</li>--%>
        <%--<li class="menu-item menu-item-type-post_type menu-item-object-page current-menu-ancestor current-menu-parent current_page_parent current_page_ancestor menu-item-2170"--%>
        <%--id="menu-item-2170">--%>
        <%--<a href="//vipworkspace.com/about/">About</a>--%>
        <%--<ul class="sub-menu">--%>
        <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-590"--%>
        <%--id="menu-item-590"><a href="//vipworkspace.com/about-kpi-com/">About vipworkspace.com</a></li>--%>
        <%--<li class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-643"--%>
        <%--id="menu-item-643"><a href="//vipworkspace.com/category/press-release/">Press Release</a></li>--%>
        <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-683"--%>
        <%--id="menu-item-683"><a href="//vipworkspace.com/customers/">Customers</a></li>--%>
        <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-689"--%>
        <%--id="menu-item-689"><a href="//vipworkspace.com/legal/">Legal</a></li>--%>
        <%--<li class="menu-item menu-item-type-post_type menu-item-object-page current-menu-item page_item page-item-2 current_page_item menu-item-559"--%>
        <%--id="menu-item-559"><a href="//vipworkspace.com/careers/">Careers</a></li>--%>
        <%--<li class="menu-item menu-item-type-custom menu-item-object-custom menu-item-1750"--%>
        <%--id="menu-item-1750"><a href="http://blog.vipworkspace.com/">Blog</a></li>--%>
        <%--</ul>--%>
        <%--</li>--%>
        <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-592" id="menu-item-592">--%>
        <%--<a href="//vipworkspace.com/contact-us/">Contact Us</a>--%>
        <%--</li>--%>
        <%--</ul>--%>
        <%--</nav>--%>

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
                    <a href="mailto:support@goodsystems.com.au" target="_blank">Email</a>
                </li>
                <li class="markTwitter socialBookMark menu-item menu-item-type-custom menu-item-object-custom menu-item-19"
                    id="menu-item-19">
                    <a href="https://www.twitter.com/good_systems" target="_blank">Twitter</a>
                </li>
                <li class="markLinkedin socialBookMark menu-item menu-item-type-custom menu-item-object-custom menu-item-20"
                    id="menu-item-20">
                    <a href="https://www.linkedin.com/company/good-systems-australia-" target="_blank">LinkedIn</a>
                </li>
                <li class="markFacebook socialBookMark menu-item menu-item-type-custom menu-item-object-custom menu-item-21"
                    id="menu-item-21">
                    <a href="http://www.facebook.com/goodsystems" target="_blank">Facebook</a>
                </li>
            </ul>

            <div class="sitePhone">
                <em>Need Help?</em>
                <span>1300 85 75 60</span>
            </div>

            <div class="footCaption">
                <a href="http://www.goodsystems.com.au" id="foot-logo">
                    <img alt="Logo" style="width:130px;height:36px;" src="/customisation/goodsystems/images/foot_logo.png">
                </a>
                <span>Good Systems Pty Ltd</span>

                <p>
                    2007 &ndash;
                    <%
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(new Date());
                        int year = cal.get(Calendar.YEAR);
                    %>
                    <%=year%> &copy; goodsystems.com All Rights Reserved.<br>
                    <a href="///legal">Terms of Use</a> | <a href="//goodsystems.com/legal">Privacy Policy</a>
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


</body>
</html>
