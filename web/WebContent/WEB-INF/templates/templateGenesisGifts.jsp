<%@ page import="java.util.Date" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en" dir="ltr">
<head>
    <title>
        <tiles:getAsString name="title" ignore="true"/>
    </title>

    <link rel="stylesheet" href="/customisation/preprod.kpi.com/style.css"/>
    <link rel="stylesheet" href="/customisation/preprod.kpi.com/appFrame.css"/>
    <link rel="stylesheet" href="/customisation/preprod.kpi.com/master.css"/>
    <link rel="shortcut icon" href="/customisation/genesis/images/favicon.ico" type="image/x-icon"/>
    <!--[if lt IE 9]>
    <script type="text/javascript" src="//html5shiv.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <!-- <meta http-equiv="X-UA-Compatible" content="chrome=1" /> -->
    <!-- for ie -->

    <!--[if lte IE 8]>
    <link rel="stylesheet" href="/customisation/preprod.kpi.com/fix-ie.css"/>
    <![endif]-->

    <script type="text/javascript" src="/customisation/preprod.kpi.com/scripts/jquery.js"></script>
    <script type="text/javascript" src="/customisation/preprod.kpi.com/scripts/common.js"></script>

    <SCRIPT type=text/javascript src="/customisation/kpi.com/jquery.placeholder.min.js"></SCRIPT>

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
            <a href="//www.genesis-gifts.com" id="logo">
                <img style="width: 210px; height: 50px; padding-top: 5px;" src="/customisation/genesis/images/logo.png">
            </a>

            <hgroup>
                <div class="langBar">
                    <%--<ul>--%>
                    <%--<li><a href="/" class="langEN">English</a></li>--%>
                    <%--<li><a href="//kpi.com/o-que-o-workforcetrack-representa-e-por-que-precisamos-dele"--%>
                    <%--class="langPo">Portuguese</a></li>--%>
                    <%--<li><a href="http://www.kpi.com.ru/" class="langRU">Russian</a></li>--%>
                    <%--<li><a href="//kpi.com/kpi-com-nedir-ve-neden-ihtiyacim-var" class="langTU">Turkey</a></li>--%>
                    <%--</ul>--%>
                </div>

                <div class="sitePhone">
                    <%--<em>Need Help?</em>--%>
                    <%--<span>(UK) +44 (0) 2070961245</span>--%>
                    <%--<span>(US) +1 646 844 3330</span>--%>
                    <%--<span>(UAE) +971 4 424 3033</span>--%>
                </div>

                <div class="clearBox"></div>
            </hgroup>

            <div class="appActs" style="height: 20px;">
                <%--<a href="/signup/freeSignup.html" class="btn-1">Free Trial</a>--%>
            </div>
            <!-- end logo site -->

            <nav class="menu-main-menu-container">
                <ul class="menu" id="menu-main-menu">
                    <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-605" id="menu-item-605">--%>
                    <%--<a href="//kpi.com/product-tour/">Products</a>--%>
                    <%--<ul class="sub-menu">--%>
                    <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-1802"--%>
                    <%--id="menu-item-1802">--%>
                    <%--<a href="//kpi.com/all-in-one/">All In One</a>--%>
                    <%--</li>--%>
                    <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-551"--%>
                    <%--id="menu-item-551">--%>
                    <%--<a href="//kpi.com/project-management/">Project and Resource Management</a>--%>
                    <%--<ul class="sub-menu">--%>
                    <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-1752"--%>
                    <%--id="menu-item-1752"><a href="//kpi.com/timesheet/">Timesheet</a></li>--%>
                    <%--</ul>--%>
                    <%--</li>--%>
                    <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-552"--%>
                    <%--id="menu-item-552">--%>
                    <%--<a href="//kpi.com/crm/">CRM and Sales</a>--%>
                    <%--</li>--%>
                    <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-553"--%>
                    <%--id="menu-item-553">--%>
                    <%--<a href="//kpi.com/hrms/">HR Management &amp; Payroll</a>--%>
                    <%--<ul class="sub-menu">--%>
                    <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-1754"--%>
                    <%--id="menu-item-1754"><a href="//kpi.com/attendance-tracking/">Attendance Tracking</a>--%>
                    <%--</li>--%>
                    <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-597"--%>
                    <%--id="menu-item-597"><a href="//kpi.com/payroll/">Payroll</a></li>--%>
                    <%--</ul>--%>
                    <%--</li>--%>
                    <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-554"--%>
                    <%--id="menu-item-554">--%>
                    <%--<a href="//kpi.com/accounting/">Financials and Inventory</a>--%>
                    <%--<ul class="sub-menu">--%>
                    <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-506"--%>
                    <%--id="menu-item-506"><a href="//kpi.com/e-commerce/">E-commerce</a></li>--%>
                    <%--</ul>--%>
                    <%--</li>--%>
                    <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-556"--%>
                    <%--id="menu-item-556">--%>
                    <%--<a href="//kpi.com/reporting/">Reporting and Dashboard</a>--%>
                    <%--</li>--%>
                    <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-555"--%>
                    <%--id="menu-item-555">--%>
                    <%--<a href="//kpi.com/document-management/">Document Management</a>--%>
                    <%--</li>--%>
                    <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-534"--%>
                    <%--id="menu-item-534">--%>
                    <%--<a href="//kpi.com/add-on/">CMS and Add-Ons</a>--%>
                    <%--</li>--%>
                    <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-2069"--%>
                    <%--id="menu-item-2069">--%>
                    <%--<a href="//kpi.com/templates/">Templates</a>--%>
                    <%--<ul class="sub-menu">--%>
                    <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-2075"--%>
                    <%--id="menu-item-2075">--%>
                    <%--<a href="//kpi.com/websites-forms/">Websites forms</a>--%>
                    <%--</li>--%>
                    <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-2074"--%>
                    <%--id="menu-item-2074">--%>
                    <%--<a href="//kpi.com/invoice-templates/">Invoice templates</a>--%>
                    <%--</li>--%>
                    <%--</ul>--%>
                    <%--</li>--%>
                    <%--</ul>--%>
                    <%--</li>--%>
                    <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-557" id="menu-item-557">--%>
                    <%--<a href="//kpi.com/pricing/">Pricing</a>--%>
                    <%--<ul class="sub-menu">--%>
                    <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-539"--%>
                    <%--id="menu-item-539">--%>
                    <%--<a href="//kpi.com/pricing/">Pricing Packages</a>--%>
                    <%--</li>--%>
                    <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-538"--%>
                    <%--id="menu-item-538">--%>
                    <%--<a href="//kpi.com/comparison/">Comparison</a>--%>
                    <%--</li>--%>
                    <%--</ul>--%>
                    <%--</li>--%>
                    <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-2079" id="menu-item-2079">--%>
                    <%--<a href="//kpi.com/support/">Support</a>--%>
                    <%--<ul class="sub-menu">--%>
                    <%--<li class="menu-item menu-item-type-custom menu-item-object-custom menu-item-2023"--%>
                    <%--id="menu-item-2023">--%>
                    <%--<a href="http://wiki.kpi.com" target="_blank">Wiki</a>--%>
                    <%--</li>--%>
                    <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-577"--%>
                    <%--id="menu-item-577">--%>
                    <%--<a href="//kpi.com/guidebooks/">Guidebooks</a>--%>
                    <%--</li>--%>
                    <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-587"--%>
                    <%--id="menu-item-587">--%>
                    <%--<a href="//kpi.com/faq/">FAQ</a>--%>
                    <%--</li>--%>
                    <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-609"--%>
                    <%--id="menu-item-609">--%>
                    <%--<a href="//kpi.com/help-video/">Help Video</a>--%>
                    <%--</li>--%>
                    <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-2065"--%>
                    <%--id="menu-item-2065">--%>
                    <%--<a href="//kpi.com/partners-page/">Partners</a>--%>
                    <%--</li>--%>
                    <%--</ul>--%>
                    <%--</li>--%>
                    <%--<li class="menu-item menu-item-type-custom menu-item-object-custom menu-item-1977" id="menu-item-1977">--%>
                    <%--<a href="http://blog.kpi.com" target="_blank">Blog</a>--%>
                    <%--</li>--%>
                    <%--<li class="menu-item menu-item-type-post_type menu-item-object-page current-menu-ancestor current-menu-parent current_page_parent current_page_ancestor menu-item-2170"--%>
                    <%--id="menu-item-2170">--%>
                    <%--<a href="//kpi.com/about/">About</a>--%>
                    <%--<ul class="sub-menu">--%>
                    <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-590"--%>
                    <%--id="menu-item-590"><a href="//kpi.com/about-kpi-com/">About kpi.com</a></li>--%>
                    <%--<li class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-643"--%>
                    <%--id="menu-item-643"><a href="//kpi.com/category/press-release/">Press Release</a></li>--%>
                    <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-683"--%>
                    <%--id="menu-item-683"><a href="//kpi.com/customers/">Customers</a></li>--%>
                    <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-689"--%>
                    <%--id="menu-item-689"><a href="//kpi.com/legal/">Legal</a></li>--%>
                    <%--<li class="menu-item menu-item-type-post_type menu-item-object-page current-menu-item page_item page-item-2 current_page_item menu-item-559"--%>
                    <%--id="menu-item-559"><a href="//kpi.com/careers/">Careers</a></li>--%>
                    <%--<li class="menu-item menu-item-type-custom menu-item-object-custom menu-item-1750"--%>
                    <%--id="menu-item-1750"><a href="http://blog.kpi.com/">Blog</a></li>--%>
                    <%--</ul>--%>
                    <%--</li>--%>
                    <%--<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-592" id="menu-item-592">--%>
                    <%--<a href="//kpi.com/contact-us/">Contact Us</a>--%>
                    <%--</li>--%>
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
                                    <a>Project/Resource<br> Management</a>
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
                                    <a>CRM and<br> Sales</a>
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
                                    <a>HR &amp; Payroll</a>
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
                                    <a>Financials<br> and Inventory</a>
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
                                    <a>CMS and<br> Add-Ons</a>
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
                                    <a>Reporting<br> and Dashboard</a>
                                </h2>
                                <ul class="menu" id="menu-reporting-and-dashboard-nav">
                                    <li>Predefined templates</li>
                                    <li>Custom Reports/charts</li>
                                    <li>Importing /Exporting</li>
                                    <li>Dashboards</li>
                                    <!--- Secure Site Seal - DO NOT EDIT --->
                                    <%--<span id="ss_img_wrapper_115-55_image_en">--%>
                                    <%--<a href="http://www.alphassl.com/ssl-certificates/wildcard-ssl.html" target="_blank" title="SSL Certificates">--%>
                                    <%--<img alt="Wildcard SSL Certificates" border=0 id="ss_img"--%>
                                    <%--src="//seal.alphassl.com/SiteSeal/images/alpha_noscript_115-55_en.gif" title="SSL Certificate">--%>
                                    <%--</a>--%>
                                    <%--</span>--%>
                                    <script type="text/javascript"
                                            src="//seal.alphassl.com/SiteSeal/alpha_image_115-55_en.js">
                                    </script>
                                    <!--- Secure Site Seal - DO NOT EDIT --->
                                </ul>
                            </nav>
                        </td>
                    </tr>
                    </tbody>
                </table>

                <%--<ul class="menu" id="menu-stay-connected">--%>
                <%--<li class="markEmail socialBookMark menu-item menu-item-type-custom menu-item-object-custom menu-item-18"--%>
                <%--id="menu-item-18">--%>
                <%--<a>Email</a>--%>
                <%--</li>--%>
                <%--<li class="markTwitter socialBookMark menu-item menu-item-type-custom menu-item-object-custom menu-item-19"--%>
                <%--id="menu-item-19">--%>
                <%--<a>Twitter</a>--%>
                <%--</li>--%>
                <%--<li class="markLinkedin socialBookMark menu-item menu-item-type-custom menu-item-object-custom menu-item-20"--%>
                <%--id="menu-item-20">--%>
                <%--<a>LinkedIn</a>--%>
                <%--</li>--%>
                <%--<li class="markFacebook socialBookMark menu-item menu-item-type-custom menu-item-object-custom menu-item-21"--%>
                <%--id="menu-item-21">--%>
                <%--<a>Facebook</a>--%>
                <%--</li>--%>
                <%--</ul>--%>

                <div class="sitePhone">
                    <%--<em>Need Help?</em>--%>
                    <%--<span>(UK) +44 (0) 2070961245</span>--%>
                    <%--<span>(US) +1 646 844 3330</span>--%>
                </div>

                <%--<div class="footCaption">--%>
                <%--<a id="foot-logo">--%>
                <%--<img alt="Logo" src="/customisation/preprod.kpi.com/images/foot_logo.png">--%>
                <%--</a>--%>
                <%--<span>Powered by Finnet Limited</span>--%>

                <%--<p>--%>
                <%--2007 &ndash;--%>
                <%--<%--%>
                <%--Calendar cal = Calendar.getInstance();--%>
                <%--cal.setTime(new Date());--%>
                <%--int year = cal.get(Calendar.YEAR);--%>
                <%--%>--%>
                <%--<%=year%> &copy; kpi.com All Rights Reserved.<br>--%>
                <%--<a>Terms of Use</a> | <a>Privacy Policy</a>--%>
                <%--</p>--%>
                <%--</div>--%>

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
        theme:"<%=captchaTheme%>"});
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
