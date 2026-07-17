<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en" dir="ltr">
<head>

    <!-- Yandex.Metrika counter -->
    <%--<script type="text/javascript"> (function (d, w, c) {
        (w[c] = w[c] || []).push(function () {
            try {
                w.yaCounter45824055 = new Ya.Metrika2({id: 45824055, clickmap: true, trackLinks: true, accurateTrackBounce: true, webvisor: true, trackHash: true});
            } catch (e) {
            }
        });
        var n = d.getElementsByTagName("script")[0], s = d.createElement("script"), f = function () {
            n.parentNode.insertBefore(s, n);
        };
        s.type = "text/javascript";
        s.async = true;
        s.src = "https://mc.yandex.ru/metrika/tag.js";
        if (w.opera == "[object Opera]") {
            d.addEventListener("DOMContentLoaded", f, false);
        } else {
            f();
        }
    })(document, window, "yandex_metrika_callbacks2"); </script>
    <noscript>
        <div><img src="https://mc.yandex.ru/watch/45824055" style="position:absolute; left:-9999px;" alt=""/></div>
    </noscript>--%>
    <!-- /Yandex.Metrika counter -->

    <title>
        <tiles:getAsString name="title" ignore="true"/>
    </title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="/customisation/preprod.kpi.com/styleKPI.css"/>
    <link rel="stylesheet" href="/customisation/preprod.kpi.com/appFrame.css"/>
    <link rel="stylesheet" href="/customisation/preprod.kpi.com/master.css"/>
    <link rel="stylesheet" href="/customisation/preprod.kpi.com/styleJF.css">
    <link href="//app.kpi.com/customisation/kpi.com/images/favicon.ico" rel="shortcut icon">
    <link rel="stylesheet" href="/customisation/preprod.kpi.com/bootstrap.min.css">
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
    <link href="/customisation/preprod.kpi.com/customize.css" rel="stylesheet">
    <link href="/customisation/preprod.kpi.com/font-awesome.min.css" rel="stylesheet">

    <tiles:insertAttribute name="style" ignore="true"/>

    <% String hostName1 = (request.getAttribute("hostName") != null) ? request.getAttribute("hostName").toString() : request.getServerName();
        if (hostName1 != null && hostName1.contains("kg.kpi.com")) { %>
    <c:set var="analytics_code" value="UA-59981695-23"/>
    <%} else { %>
    <c:set var="analytics_code" value="UA-355982-15"/>
    <% } %>

    <script type="text/javascript">
        (function (i, s, o, g, r, a, m) {
            i['GoogleAnalyticsObject'] = r;
            i[r] = i[r] || function () {
                (i[r].q = i[r].q || []).push(arguments)
            }, i[r].l = 1 * new Date();
            a = s.createElement(o), m = s.getElementsByTagName(o)[0];
            a.async = 1;
            a.src = g;
            m.parentNode.insertBefore(a, m)
        })(window, document, 'script', '//www.google-analytics.com/analytics.js', 'ga');
        ga('create', '<c:out value="${analytics_code}"/>', 'auto');
        ga('send', 'pageview');
    </script>
    <% if (hostName1 != null && (hostName1.contains("app.kpi.com")) || hostName1.contains("aws.kpi.com")) { %>
    <script type="text/javascript">
        setTimeout(function () {
            var a = document.createElement("script");
            var b = document.getElementsByTagName("script")[0];
            a.src = document.location.protocol + "//script.crazyegg.com/pages/scripts/0043/1807.js?" + Math.floor(new Date().getTime() / 3600000);
            a.async = true;
            a.type = "text/javascript";
            b.parentNode.insertBefore(a, b)
        }, 1);
    </script>
    <% } %>
</head>

<body>

<!-- begin wrapper -->
<div id="wrapper">

<!--begin header-->
<header id="header">
<div class="topHeader">
    <div class="container">
        <div class="sitePhone">
            <em>Contact Us:</em>
            <span>(UK) +44 (0) 173 261 7967</span>
            <span>(US) +1 844 726 84 46</span>
            <span>(UAE) +971 4 424 3033</span>
        </div>
        <div class="socials">
            <a href="https://www.facebook.com/KpiServices?ref=tn_tnmn" class="fa fa-facebook" target="_blank"></a>
            <a href="https://twitter.com/kpi_online" class="fa fa-twitter" target="_blank"></a>
            <a href="https://www.linkedin.com/company/260375?trk=tyah&trkInfo=idx%3A1-1-1%2CtarId%3A1424337700540%2Ctas%3Akpi.com"
               class="fa fa-linkedin" target="_blank"></a>
            <a href="https://plus.google.com/u/0/111235257208481299823/posts" class="fa fa-google-plus"
               target="_blank"></a>
            <a href="https://www.youtube.com/channel/UC611go2FxyIh50rVzhgghMg" class="fa fa-youtube"
               target="_blank"></a>
        </div>
    </div>
</div>
<div class="container midHeader">
<div class="ZoneIn">
    <!-- begin logo site -->


    <div class="langBar" id="langbar" style="display: none;">
        <ul>
            <li><a href="/" class="langEN">English</a></li>
            <li><a href="//kpi.com/o-que-o-workforcetrack-representa-e-por-que-precisamos-dele"
                   class="langPo">Portuguese</a></li>
            <li><a href="http://www.kpi.com.ru/" class="langRU">Russian</a></li>
            <li><a href="//kpi.com/kpi-com-nedir-ve-neden-ihtiyacim-var" class="langTU">Turkey</a></li>
        </ul>
    </div>
    <div class="appActs" id="signIn">
        <%if (request.getRequestURI().endsWith("freeSignup.jsp")) {%>
        <a href="/index.html" class="btn-1 login-btn">Login</a>
        <%} else {%>
        <a href="/auth/signup.html" class="btn-1">Free Trial</a>
        <%}%>
    </div>

    <a href="//www.kpi.com" id="logo">
        <img src="/customisation/preprod.kpi.com/images/logo.png">
    </a>
    <!-- end logo site -->

    <nav class="navbar" role="navigation">
        <div class="navbar-header">
            <button class="navbar-toggle" type="button" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="fa fa-bars"></span>
            </button>
        </div>

        <div class="collapse navbar-collapse" role="navigation">
            <ul class="nav navbar-nav">
                <li class="dropdown">
                    <div title="dropdown menu" data-toggle="dropdown"><b class="caret"></b></div>
                    <a href="https://www.kpi.com/solutions/" class="dropdown-toggle">Solutions</a>
                    <ul class="dropdown-menu">
                        <li><a href="https://www.kpi.com/integrated-one-cloud-erp-software/">Integrated All-in-One
                            Cloud ERP Software</a></li>
                        <li class="dropdown dropdown-submenu">
                            <a href="https://www.kpi.com/project-management/">Project and Resource Management</a>
                            <ul class="dropdown-menu">
                                <li><a href="https://www.kpi.com/timesheet/">Timesheet</a></li>
                            </ul>
                        </li>
                        <li><a href="https://www.kpi.com/crm/">CRM and Sales</a></li>
                        <li class="dropdown dropdown-submenu">
                            <a href="https://www.kpi.com/hrms/">HR Management &amp; Payroll</a>
                            <ul class="dropdown-menu">
                                <li><a href="https://www.kpi.com/payroll/">Payroll</a></li>
                                <li><a href="https://www.kpi.com/employee-management/">Employee Management</a></li>
                                <li><a href="https://www.kpi.com/attendance-tracking/">Attendance Tracking</a></li>
                                <li><a href="https://www.kpi.com/performance-appraisals/">Performance Appraisals</a>
                                </li>
                                <li><a href="https://www.kpi.com/goal-management/">Goal Management</a></li>
                                <li><a href="https://www.kpi.com/recruitment/">Recruitment</a></li>
                                <li><a href="https://www.kpi.com/departments/">Departments</a></li>
                                <li><a href="https://www.kpi.com/locations/">Locations</a></li>
                                <li><a href="https://www.kpi.com/positions/">Positions</a></li>
                                <li><a href="https://www.kpi.com/salary-grades/">Salary Grades</a></li>
                                <li><a href="https://www.kpi.com/employee-profile/">Employee Profile</a></li>
                            </ul>
                        </li>
                        <li class="dropdown dropdown-submenu">
                            <a href="https://www.kpi.com/accounting/">Financials and Inventory</a>
                            <ul class="dropdown-menu">
                                <li><a href="https://www.kpi.com/online-accounting-solution/">Online Accounting
                                    Solution</a></li>
                                <li><a href="https://www.kpi.com/sales-and-accounts-receivable/">Sales and Accounts
                                    Receivable</a></li>
                                <li><a href="https://www.kpi.com/procurement/">Procurement</a></li>
                                <li><a href="https://www.kpi.com/inventory-management-3/">Inventory Management</a>
                                </li>
                                <li><a href="https://www.kpi.com/expense-management/">Expense Management</a></li>
                                <li><a href="https://www.kpi.com/fixed-asset-management-3/">Fixed Asset
                                    Management</a></li>
                                <li><a href="https://www.kpi.com/bank-account-management/">Bank Account
                                    Management</a></li>
                                <li><a href="https://www.kpi.com/departmentscost-and-profit-centres/">Departments/Cost
                                    and Profit Centres</a></li>
                                <li><a href="https://www.kpi.com/chart-of-accounts/">Chart of Accounts</a></li>
                                <li><a href="https://www.kpi.com/e-commerce/">E-commerce</a></li>
                            </ul>
                        </li>
                        <li class="dropdown dropdown-submenu">
                            <a href="https://www.kpi.com/reporting/">Reporting and Dashboard</a>
                            <ul class="dropdown-menu">
                                <li><a href="https://www.kpi.com/custom-reports/">Custom Reports</a></li>
                            </ul>
                        </li>
                        <li><a href="https://www.kpi.com/document-management/">Document Management</a></li>
                        <li><a href="https://www.kpi.com/add-on/">CMS and Add-Ons</a></li>
                        <li><a href="https://www.kpi.com/training-management-module/">Training Management Module</a>
                        </li>
                        <li><a href="https://www.kpi.com/website-management-solution/">Website Management
                            Solution</a></li>
                        <li class="dropdown dropdown-submenu">
                            <a href="https://www.kpi.com/templates/">Templates</a>
                            <ul class="dropdown-menu">
                                <li><a href="https://www.kpi.com/website-templates/">Website Templates</a></li>
                                <li><a href="https://www.kpi.com/invoice-templates/">Invoice templates</a></li>
                            </ul>
                        </li>
                    </ul>
                </li>
                <li class="dropdown">
                    <div title="dropdown menu" data-toggle="dropdown"><b class="caret"></b></div>
                    <a href="https://www.kpi.com/pricing/" class="dropdown-toggle">Pricing</a>
                    <ul class="dropdown-menu">
                        <li><a href="https://www.kpi.com/pricing/">Pricing Packages</a></li>
                        <li><a href="https://www.kpi.com/comparison/">Comparison</a></li>
                    </ul>
                </li>
                <li class="dropdown">
                    <div title="dropdown menu" data-toggle="dropdown"><b class="caret"></b></div>
                    <a href="https://www.kpi.com/support/" class="dropdown-toggle">Support</a>
                    <ul class="dropdown-menu">
                        <li><a href="https://www.kpi.com/help-video/">Help Videos</a></li>
                        <li><a target="_blank" href="http://wiki.kpi.com/s/">Wiki</a></li>
                        <li><a href="https://www.kpi.com/brochures">Brochures</a></li>
                        <li><a href="https://www.kpi.com/guidebooks/">Guidebooks</a></li>
                        <li><a href="https://www.kpi.com/faq/">Frequently Asked Questions</a></li>
                        <li><a href="https://www.kpi.com/blog">Blog</a></li>
                    </ul>
                </li>
                <li><a target="_blank" href="https://www.kpi.com/blog">Blog</a></li>
                <li class="dropdown">
                    <div title="dropdown menu" data-toggle="dropdown"><b class="caret"></b></div>
                    <a href="https://www.kpi.com/about/" class="dropdown-toggle">About</a>
                    <ul class="dropdown-menu">
                        <li><a href="https://www.kpi.com/about-kpi-com/">About kpi.com</a></li>
                        <li><a href="https://www.kpi.com/category/press-release/">Press Release</a></li>
                        <li><a href="https://www.kpi.com/customers/">Customers</a></li>
                        <li><a href="https://www.kpi.com/legal/">Legal</a></li>
                        <li><a href="https://www.kpi.com/careers/">Careers</a></li>
                        <li><a href="https://www.kpi.com/partners-page/">Partners</a></li>
                    </ul>
                </li>
                <li><a href="https://www.kpi.com/contact-us/">Contact Us</a></li>
            </ul>
        </div>
    </nav>
</div>
<style>
    /* Start "Style" */
    hgroup {
        border-bottom: 1px solid #dcdcdc;
        background: #eff4f7;
        color: #b1b4b6;
        padding: 10px 0;
    }

    hgroup > div {
        position: relative;
        width: 964px;
        margin: 0 auto;
    }

    header .ZoneIn {
        padding: 25px 0;
    }

    a#logo {
        top: 10px;
    }

    .ZoneIn .appActs .btn-1 {
        background-image: none;
        border: 1px solid transparent;
        white-space: nowrap;
        padding: 6px 12px;
        font-size: 14px;
        line-height: 1.42857143;
        border-radius: 4px;
        color: #fff;
        background-color: #00acee;
        border-color: #009ad5;
        height: auto;
        box-shadow: none;
        font-family: 'Roboto', sans-serif;
        min-width: 116px;
        text-align: center;
        vertical-align: middle;
        -webkit-box-sizing: border-box;
        -moz-box-sizing: border-box;
        box-sizing: border-box;
    }

    .ZoneIn .appActs .btn-1:hover {
        color: #fff;
        background-color: #0087bb;
        border-color: #006d97;
    }

    .ZoneIn .appActs {
        margin: 0 0 0 35px;
        clear: left;
    }

    .ZoneIn nav.menu-main-menu-container {
        margin: 5px 0 0;
    }

    header nav > ul > li > a {
        font-family: 'Roboto', sans-serif;
        font-size: 14px;
        text-transform: uppercase;
        padding: 5px 0;
    }

    header .sitePhone span {
        color: #b1b4b6;
        font-family: 'Roboto', sans-serif;
        font-size: 14px;
        font-weight: normal;
        margin-right: 25px;
    }

    .langBar {
        height: 30px;
        font-size: 12px;
        line-height: 1.5;
        border-radius: 3px;
    }

    .langBar ul {
        padding: 10px 10px 9px;
        font-size: 12px;
        line-height: 1.5;
        border-radius: 3px;
        background-position: 100% 15px;
        background-size: 11px 5px;
    }

    .langBar ul li {
        margin: 0 0 15px;
    }

    .langBar:hover ul {
        height: auto;
    }

    .langBar ul li:last-child {
        margin: 0;
    }

    /* End "Style" */
</style>
</div>
</header>
<!--END header-->

<!--begin #cover-->
<div id="cover">

    <!--begin #content-->
    <div id="content" class="full-main">


        <tiles:insertAttribute name="body" ignore="false"/>

        <!--begin footer-->
        <footer id="footer">
            <div class="topFooter">
                <div class="container">
                    <ul class="footer_menu row">
                        <li class="col-md-2 col-sm-6 col-xs-6">
                            <%--<a href="//kpi.com/project-management">--%>
                            <h3>Project Management</h3>
                            <ul class="list-unstyled">
                                <li><a href="https://www.kpi.com/brochures/projects-and-tasks/">Projects and Tasks</a></li>
                                <li><a href="https://www.kpi.com/brochures/timesheet/">Timesheet</a></li>
                                <li><a href="https://www.kpi.com/brochures/gantt-chart/">Gantt Chart</a></li>
                                <li><a href="https://www.kpi.com/brochures/budget-sheet/">Budget Sheet</a></li>
                                <li><a href="https://www.kpi.com/brochures/resource-utilization/">Resource Utilization</a></li>
                                <li><a href="https://www.kpi.com/brochures/issue-management/">Issue Management</a></li>
                                <li><a href="https://www.kpi.com/brochures/booking-items/">Booking Items</a></li>
                            </ul>
                        </li>

                        <li class="col-md-2 col-sm-6 col-xs-6">
                            <h3>CRM</h3>
                            <ul class="list-unstyled">
                                <li><a href="https://www.kpi.com/brochures/sales-force-automation/">Sales Force Automation</a></li>
                                <li><a href="https://www.kpi.com/brochures/customer-service/">Customer Service</a></li>
                                <li><a href="https://www.kpi.com/brochures/email-configuration/">Email Configuration</a></li>
                                <li><a href="https://www.kpi.com/brochures/email-marketing/">Email Marketing</a></li>
                                <li><a href="https://www.kpi.com/brochures/calendar/">Calendar</a></li>
                                <li><a href="https://www.kpi.com/brochures/message-center/">Message Center</a></li>
                            </ul>
                        </li>

                        <li class="col-md-2 col-sm-6 col-xs-6">
                            <h3>HR &amp; Payroll</h3>
                            <ul class="list-unstyled">
                                <li><a href="https://www.kpi.com/brochures/employee-profile/">Employee Profile</a></li>
                                <li><a href="https://www.kpi.com/brochures/attendance-tracking/">Attendance Tracking</a></li>
                                <li><a href="https://www.kpi.com/brochures/leave-requests/">Leave Requests</a></li>
                                <li><a href="https://www.kpi.com/brochures/performance-appraisals/">Performance Appraisals</a></li>
                                <li><a href="https://www.kpi.com/brochures/recruitment/">Recruitment</a></li>
                                <li><a href="https://www.kpi.com/brochures/payroll/">Payroll</a></li>
                            </ul>
                        </li>

                        <li class="col-md-2 col-sm-6 col-xs-6">
                            <h3>Accounting</h3>
                            <ul class="list-unstyled">
                                <li><a href="https://www.kpi.com/brochures/sales-transactions/">Sales Transactions</a></li>
                                <li><a href="https://www.kpi.com/brochures/purchases/">Purchases</a></li>
                                <li><a href="https://www.kpi.com/brochures/products-and-services/">Products and Services</a></li>
                                <li><a href="https://www.kpi.com/brochures/customersupplier-center/">Customer&amp;Supplier Center</a></li>
                                <li><a href="https://www.kpi.com/brochures/storefront/">Storefront</a></li>
                            </ul>
                        </li>

                        <li class="col-md-2 col-sm-6 col-xs-6">
                            <h3>CMS &amp; E-commerce</h3>
                            <ul class="list-unstyled">
                                <li><a href="https://www.kpi.com/brochures/custom-websites/">Custom Websites</a></li>
                                <li><a href="https://www.kpi.com/brochures/mobile-applications/">Mobile Applications</a></li>
                                <li><a href="https://www.kpi.com/brochures/office-plugins/">Office Plugins</a></li>
                                <li><a href="https://www.kpi.com/brochures/add-ons/">Add-ons</a></li>
                                <li><a href="https://www.kpi.com/brochures/custom-pdf-templates/">Custom PDF Templates</a></li>
                            </ul>
                        </li>

                        <li class="col-md-2 col-sm-6 col-xs-6">
                            <h3>Reporting</h3>
                            <ul class="list-unstyled">
                                <li><a href="https://www.kpi.com/brochures/reporting-engine/">Reporting Engine</a></li>
                                <li><a href="https://www.kpi.com/brochures/custom-reports/">Custom Reports</a></li>
                                <li><a href="https://www.kpi.com/brochures/dashboard/">Dashboard</a></li>
                                <li><a href="https://www.kpi.com/brochures/beta-workspace/">Beta Workspace</a></li>
                                <li><a href="https://www.kpi.com/brochures/accounting-reports/">Accounting Reports</a></li>
                                <!--- Secure Site Seal - DO NOT EDIT --->
                                <span id="ss_img_wrapper_115-55_image_en">
	                                <a href="http://www.alphassl.com/ssl-certificates/wildcard-ssl.html" target="_blank" title="SSL Certificates">
                                        <img alt="Wildcard SSL Certificates" border=0 id="ss_img"
                                             src="//seal.alphassl.com/SiteSeal/images/alpha_noscript_115-55_en.gif" title="SSL Certificate">
                                    </a>
                                </span>
                                <script type="text/javascript"
                                        src="//seal.alphassl.com/SiteSeal/alpha_image_115-55_en.js">
                                </script>
                                <!--- Secure Site Seal - DO NOT EDIT --->
                            </ul>
                        </li>
                    </ul>
                </div>
            </div>

            <div class="bottomFooter">
                <div class="container">
                    <div class="row">
                        <div class="col-md-2 col-sm-6">
                            <a href="https://www.kpi.com" id="logo2" rel="home">
                                <img alt="Finnet Technologies logo" src="/customisation/preprod.kpi.com/images/logo_site_2.png">
                            </a>
                        </div>
                        <div class="col-md-4 col-sm-6">
                            <address>
                                <strong class="title">Powered by Finnet Limited</strong>

                                <div class="address">
                                    <strong>Registration No:</strong> 03929942 <br/>
                                    <strong>Registered Address:</strong> 137 Blackstock Road, London, N4 2JW, United
                                    Kingdom<br/>
                                    <strong>VAT Reg. Number:</strong> GB-848480002
                                </div>
                            </address>
                        </div>

                        <div class="col-md-3">
                            <ul class="phones list-unstyled">
                                <li><i class="icon-mobile-phone"></i> (UK) +44 (0) 173 261 7967
                                </li>
                                <li><i class="icon-mobile-phone"></i> (US) +1 844 726 84 46
                                </li>
                                <li><i class="icon-mobile-phone"></i> (UAE) +971 4 424 3033
                                </li>
                            </ul>
                        </div>

                        <div class="col-md-3">
                            <div class="socials">
                                <div class="clearfix">
                                    <a class="icon-envelope-o" href="mailto:support@kpi.com">Email</a>
                                    <a class="icon-twitter" href="https://twitter.com/kpi_online">Twitter</a>
                                </div>
                                <div class="clearfix">
                                    <a class="icon-linkedin"
                                       href="https://www.linkedin.com/company/260375?trk=tyah&trkInfo=idx%3A1-1-1%2CtarId%3A1424337700540%2Ctas%3Akpi.com">Linkedn</a>
                                    <a class="icon-facebook"
                                       href="https://www.facebook.com/KpiServices?ref=tn_tnmn">Facebook</a>
                                </div>
                                <div class="clearfix">
                                    <a class="icon-google-plus" href="https://plus.google.com/u/0/111235257208481299823/posts">Google+</a>
                                </div>
                            </div>
                        </div>

                        <div class="col-xs-12">
                            <hr/>
                        </div>
                        <div class="col-xs-12 col-md-10 col-md-offset-2">
                            <div class="copy">
                                2007
                                &#8211; 2017 &copy; kpi.com
                                All Rights Reserved.
                                <div class="copyLinks">
                                    <a href="https://www.kpi.com/legal/">Terms of Use</a> | <a
                                        href="https://www.kpi.com/legal/">Privacy Policy</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </footer>

        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="/customisation/preprod.kpi.com/scripts/jquery.min.js"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="/customisation/preprod.kpi.com/scripts/bootstrap.min.js"></script>

        <!--END footer-->

    </div>
    <!--END #content-->

</div>
<!--END #cover-->


</div>
<!-- END #wrapper -->

<tiles:insertAttribute name="script" ignore="true"/>

<%
    //Recaptcha script
    Boolean enableCaptcha = request.getAttribute("captcha") != null ? ((Boolean) request.getAttribute("captcha")) : false;
    String captchaTheme = request.getAttribute("captchaTheme") != null ? ((String) request.getAttribute("captchaTheme")) : "white";

    if (enableCaptcha) {%>
<script type="text/javascript">
    var recaptchaCallback = function () {
        //console.log('recaptcha is ready'); // not showing
        grecaptcha.render("recaptcha", {
            sitekey: '<%=EdsContextParams.getRecaptchaPublicKey()%>',
            callback: function () {
                console.log('recaptcha callback');
            }
        });
    }
</script>
<script src="https://www.google.com/recaptcha/api.js?onload=recaptchaCallback&render=explicit" async defer></script>

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
