<%--
  Created by IntelliJ IDEA.
  User: Lochin copied form pricing.jsp
  Date: Feb 17, 2010
  Time: 6:04:14 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>WorkforceTrack Pricing</title>
    <link rel="shortcut icon" href="/customisation/${productNameLower}/images/favicon.ico" type="image/x-icon"/>
    <link rel="apple-touch-icon" href="/mobile_sources/images/apple-touch-icon.png">

    <%--Drupal Head Content--%>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <link type="text/css" rel="stylesheet" media="all" href="http://beta.workforcetrack.com/modules/book/book.css?3"/>
    <link type="text/css" rel="stylesheet" media="all" href="http://beta.workforcetrack.com/modules/node/node.css?3"/>
    <link type="text/css" rel="stylesheet" media="all" href="http://beta.workforcetrack.com/modules/poll/poll.css?3"/>
    <link type="text/css" rel="stylesheet" media="all"
          href="http://beta.workforcetrack.com/modules/system/defaults.css?3"/>
    <link type="text/css" rel="stylesheet" media="all"
          href="http://beta.workforcetrack.com/modules/system/system.css?3"/>
    <link type="text/css" rel="stylesheet" media="all"
          href="http://beta.workforcetrack.com/modules/system/system-menus.css?3"/>
    <link type="text/css" rel="stylesheet" media="all" href="http://beta.workforcetrack.com/modules/user/user.css?3"/>
    <link type="text/css" rel="stylesheet" media="all"
          href="http://beta.workforcetrack.com/sites/all/modules/cck/theme/content-module.css?3"/>
    <link type="text/css" rel="stylesheet" media="all"
          href="http://beta.workforcetrack.com/sites/all/modules/nice_menus/nice_menus.css?3"/>

    <link type="text/css" rel="stylesheet" media="all"
          href="http://beta.workforcetrack.com/sites/all/modules/nice_menus/nice_menus_default.css?3"/>
    <link type="text/css" rel="stylesheet" media="all" href="http://beta.workforcetrack.com/modules/forum/forum.css?3"/>
    <link type="text/css" rel="stylesheet" media="all"
          href="http://beta.workforcetrack.com/sites/all/themes/wft/style.css?3"/>
    <link type="text/css" rel="stylesheet" media="print"
          href="http://beta.workforcetrack.com/sites/all/themes/wft/print.css?3"/>
    <script type="text/javascript" src="http://beta.workforcetrack.com/misc/jquery.js?3"></script>
    <script type="text/javascript" src="http://beta.workforcetrack.com/misc/drupal.js?3"></script>
    <script type="text/javascript"
            src="http://beta.workforcetrack.com/sites/all/modules/nice_menus/nice_menus.js?3"></script>
    <script type="text/javascript">
        <!--//--><![CDATA[//><!--
        jQuery.extend(Drupal.settings, { "basePath": "http://beta.workforcetrack.com/", "googleanalytics": { "trackOutgoing": 1, "trackMailto": 1, "trackDownload": 1, "trackDownloadExtensions": "7z|aac|avi|csv|doc|exe|flv|gif|gz|jpe?g|js|mp(3|4|e?g)|mov|pdf|phps|png|ppt|rar|sit|tar|torrent|txt|wma|wmv|xls|xml|zip" } });
        //--><!]]>
    </script>
    <script type="text/javascript"
            src="//beta.workforcetrack.com/sites/all/themes/wft/highslide/highslide-with-gallery.js"></script>
    <link rel="stylesheet" type="text/css"
          href="//beta.workforcetrack.com/sites/all/themes/wft/highslide/highslide.css"/>
    <!--<script src="http://beta.workforcetrack.com/sites/all/themes/wft/jQuery.js"></script>-->
    <script src="//beta.workforcetrack.com/sites/all/themes/wft/jquery.cookie.js"></script>

    <script>
        $(document).ready(function() {

            var COOKIE_NAME = 'own_cookie';
            var val = $.cookie(COOKIE_NAME);
            if (val == null) {
                $("#cover").css("font-size", "13px");
                $("#font-size-all option:nth-child(2)").attr("selected", "selected");
            }
            else {
                $("#cover").css("font-size", val + "px");
                switch (val) {
                    case "11":
                        $("#font-size-all option:nth-child(1)").attr("selected", "selected");
                        break;
                    case "13":
                        $("#font-size-all option:nth-child(2)").attr("selected", "selected");
                        break;
                    case "15":
                        $("#font-size-all option:nth-child(3)").attr("selected", "selected");
                        break;
                    default:
                        $("#font-size-all option:nth-child(2)").attr("selected", "selected");
                        break;
                }
            }

            $("#font-size-all").change(function() {
                var obj = $("#font-size-all").val();

                $("#cover").css("font-size", obj + "px");
                $.cookie(COOKIE_NAME, null);
                $.cookie(COOKIE_NAME, obj, { path: '/', expires: 7 });
            });
        });
    </script>
    <script type="text/javascript">
        hs.graphicsDir = '/sites/all/themes/wft/highslide/graphics/';
        hs.align = 'center';
        hs.transitions = ['expand', 'crossfade'];
        hs.outlineType = 'rounded-white';
        hs.fadeInOut = true;
        hs.numberPosition = 'caption';
        hs.dimmingOpacity = 0.75;

        if (hs.addSlideshow) hs.addSlideshow({
                    //slideshowGroup: 'group1',
                    interval: 5000,
                    repeat: false,
                    useControls: true,
                    fixedControls: 'fit',
                    overlayOptions: {
                        opacity: .75,
                        position: 'bottom center',
                        hideOnMouseOut: true
                    }
                });
    </script>
    <!--END Galery-->
    <style type="text/css"
           media="print">@import "//beta.workforcetrack.com/sites/all/themes/wft/print.css";</style>
    <!--[if lt IE 7]>
    <style type="text/css" media="all">@import "//beta.workforcetrack.com/sites/all/themes/wft/fix-ie.css";</style>
    <![endif]-->
    <meta name="google-site-verification" content="jjw1ZnV5AryaQZs-KB64fCH3EpvkT94GKJAoFvLFzbU"/>
    <meta name="msvalidate.01" content="358DD608AB06D6ED2E842AF53CAFFB9E"/>
    <META name="y_key" content="d766be7156d8deef">
    <%--End of Drupal Head Content--%>


    <script language="javascript" src="pricing/pricing.nocache.js"></script>
</head>
<body>
<input type="hidden" id="isukclient" value="${isukclient}"/>
<iframe id="__gwt_historyFrame" style="width:0;height:0;border:0"></iframe>


<%--Start of Drupal Content--%>
<!--begin #wrapper-->
<div id="wrapper">

<!--begin #header-->
<div id="header">
<div id="head">
    <div id="block-block-9" class="clear-block block block-block">


        <div class="content"><a id="logo-site" href="http://beta.workforcetrack.com/">
            <% if (request.getHeader("host").toLowerCase().contains(".ru") || (request.getParameterMap().get("locale") != null && request.getParameter("locale").toLowerCase().contains("ru"))) {%>
            <img src="/workforcelogo_ru.png" alt="Site Name"/>
            <%} else {%>
            <img src="//www.workforcetrack.com/sites/all/themes/wft/images/logo.png" alt="Site Name"/>
            <%}%>

        </a>

            <ul class="extra-menu">
                <li>

                    <span class="m-mobile master-marks">Call Us: +44 (0) 844 774 2253</span>
                </li>
                <li>
                    <a href="http://app.workforcetrack.com" class="m-key master-marks">Customer Login</a>
                </li>
            </ul>
            <label class="font-size">Choose Text Size <select id="font-size-all">

                <option value="11">Small (11px)</option>
                <option value="13">Medium (13px)</option>
                <option value="15">High (15px)</option>
            </select>
            </label>

            <h2 class="custom-font light-font clear right">Your Simple Business Applications All-In-One!</h2></div>
    </div>

</div>

<!--begin #head-menu-->
<div id="head-menu">
<div id="block-nice_menus-1" class="clear-block block block-nice_menus">

<h2><span class="nice-menu-show-title">Primary links</span></h2>

<div class="content">
<div class="test-menu">
<ul class="nice-menu nice-menu-right" id="nice-menu-1">
<li id="menu-307" class="menu-path-front"><a href="http://beta.workforcetrack.com/"
                                             title=""><span>Home</span></a></li>

<li id="menu-1097" class="menu-path-node-46"><a
        href="http://beta.workforcetrack.com/content/about" title=""><span>About</span></a></li>
<li id="menu-1104" class="menu-path-node-266"><a
        href="http://beta.workforcetrack.com/content/features"
        title=""><span>Features</span></a></li>
<li id="menu-187" class="menu-path-node-265 act"><a
        href="http://beta.workforcetrack.com/content/pricing" title="Pricing"
        class="active"><span>Pricing</span></a></li>
<li id="menu-1105" class="menuparent menu-path-node-2"><a
        href="http://beta.workforcetrack.com/content/product-tours"
        title=""><span>Product Tour</span></a>

<div class="dd-menu">
<table>
<tr>
<td>
<ul>
<li id="menu-1113" class="menuparent menu-path-node-63"><a
        href="http://beta.workforcetrack.com/content/take-project-management-software-screenshot-tour"
        title=""><span>Project Management</span></a>

    <div class="dd-menu">
        <table>
            <tr>
                <td>
                    <ul>
                        <li id="menu-1167" class="menu-path-node-63"><a
                                href="http://beta.workforcetrack.com/content/take-project-management-software-screenshot-tour#project-management-overview"
                                title=""><span>Project Management Overview</span></a>
                        </li>
                        <li id="menu-1168" class="menu-path-node-63"><a
                                href="http://beta.workforcetrack.com/content/take-project-management-software-screenshot-tour#projects"
                                title=""><span>Projects</span></a></li>
                        <li id="menu-1169" class="menu-path-node-63"><a
                                href="http://beta.workforcetrack.com/content/take-project-management-software-screenshot-tour#tasks"
                                title=""><span>Tasks</span></a></li>
                        <li id="menu-1121" class="menu-path-node-63"><a
                                href="http://beta.workforcetrack.com/content/take-project-management-software-screenshot-tour#timesheet"
                                title=""><span>WorkforceTrack Timesheet</span></a>
                        </li>
                        <li id="menu-1170" class="menu-path-node-63"><a
                                href="http://beta.workforcetrack.com/content/take-project-management-software-screenshot-tour#timesheet-approval"
                                title=""><span>Timesheet Approval</span></a>
                        </li>

                        <li id="menu-1171" class="menu-path-node-63"><a
                                href="http://beta.workforcetrack.com/content/take-project-management-software-screenshot-tour#issues"
                                title=""><span>Issues</span></a></li>
                        <li id="menu-1172" class="menu-path-node-63"><a
                                href="http://beta.workforcetrack.com/content/take-project-management-software-screenshot-tour#events"
                                title=""><span>Projects integration with the Calendar</span></a>
                        </li>
                        <li id="menu-1120" class="menu-path-node-63"><a
                                href="http://beta.workforcetrack.com/content/take-project-management-software-screenshot-tour#gannt-chart"
                                title=""><span>Gannt Chart tool</span></a>
                        </li>
                        <li id="menu-1123" class="menu-path-node-63"><a
                                href="http://beta.workforcetrack.com/content/take-project-management-software-screenshot-tour#project"
                                title=""><span>Project Based Invoices</span></a>
                        </li>
                        <li id="menu-1122" class="menu-path-node-63"><a
                                href="http://beta.workforcetrack.com/content/take-project-management-software-screenshot-tour#reporting"
                                title=""><span>Project Management Custom Reports</span></a>
                        </li>
                    </ul>
                    <div class='dd-shadow'>
                        <div></div>
                    </div>
                </td>
            </tr>
        </table>
    </div>
</li>
<li id="menu-1115" class="menuparent menu-path-node-75"><a
        href="http://beta.workforcetrack.com/content/crm-campaign-contact-and-notes"
        title=""><span>Customer Relationship Management (CRM)</span></a>

    <div class="dd-menu">
        <table>
            <tr>
                <td>
                    <ul>
                        <li id="menu-1131" class="menu-path-node-75"><a
                                href="http://beta.workforcetrack.com/content/crm-campaign-contact-and-notes#leads"
                                title=""><span>Lead Management</span></a>
                        </li>
                        <li id="menu-1132" class="menu-path-node-75"><a
                                href="http://beta.workforcetrack.com/content/crm-campaign-contact-and-notes#marketing"
                                title=""><span>Marketing management</span></a>
                        </li>

                        <li id="menu-1133" class="menu-path-node-75"><a
                                href="http://beta.workforcetrack.com/content/crm-campaign-contact-and-notes#contacts"
                                title=""><span>Contact management</span></a>
                        </li>
                        <li id="menu-1134" class="menu-path-node-75"><a
                                href="http://beta.workforcetrack.com/content/crm-campaign-contact-and-notes#case-management"
                                title=""><span>Help Desk Ticketing/Case management</span></a>
                        </li>
                    </ul>
                    <div class='dd-shadow'>
                        <div></div>
                    </div>
                </td>
            </tr>
        </table>
    </div>
</li>
<li id="menu-1125" class="menuparent menu-path-node-276"><a
        href="http://beta.workforcetrack.com/content/accounting_%20and_finance"
        title=""><span>Accounting and Finance</span></a>

    <div class="dd-menu">
        <table>
            <tr>
                <td>
                    <ul>
                        <li id="menu-1135" class="menu-path-node-276"><a
                                href="http://beta.workforcetrack.com/content/accounting_%20and_finance#sales-quotes"
                                title=""><span>Sales quotes</span></a>
                        </li>
                        <li id="menu-1136" class="menu-path-node-276"><a
                                href="http://beta.workforcetrack.com/content/accounting_%20and_finance#invoicing"
                                title=""><span>Sales invoices</span></a>
                        </li>
                        <li id="menu-1137" class="menu-path-node-276"><a
                                href="http://beta.workforcetrack.com/content/accounting_%20and_finance#sales-order"
                                title=""><span>Sales orders</span></a>
                        </li>
                        <li id="menu-1138" class="menu-path-node-276"><a
                                href="http://beta.workforcetrack.com/content/accounting_%20and_finance#purchase-order"
                                title=""><span>Purchase orders</span></a>
                        </li>
                        <li id="menu-1139" class="menu-path-node-276"><a
                                href="http://beta.workforcetrack.com/content/accounting_%20and_finance#inventory"
                                title=""><span>Inventory management</span></a>
                        </li>

                        <li id="menu-1140" class="menu-path-node-276"><a
                                href="http://beta.workforcetrack.com/content/accounting_%20and_finance#expense"
                                title=""><span>Expense management</span></a>
                        </li>
                        <li id="menu-1141" class="menu-path-node-276"><a
                                href="http://beta.workforcetrack.com/content/accounting_%20and_finance#vat-reporting"
                                title=""><span>VAT reporting</span></a>
                        </li>
                        <li id="menu-1142" class="menu-path-node-276"><a
                                href="http://beta.workforcetrack.com/content/accounting_%20and_finance#general-ledger"
                                title=""><span>General Ledger</span></a>
                        </li>
                    </ul>
                    <div class='dd-shadow'>
                        <div></div>
                    </div>
                </td>
            </tr>
        </table>
    </div>
</li>
<li id="menu-1124" class="menuparent menu-path-node-275"><a
        href="http://beta.workforcetrack.com/content/hr_management%20"
        title=""><span>HR Management</span></a>

    <div class="dd-menu">
        <table>
            <tr>
                <td>
                    <ul>
                        <li id="menu-1143" class="menu-path-node-275"><a
                                href="http://beta.workforcetrack.com/content/hr_management%20#organization"
                                title=""><span>Organization Management</span></a>
                        </li>
                        <li id="menu-1144" class="menu-path-node-275"><a
                                href="http://beta.workforcetrack.com/content/hr_management%20#performance-appraisal"
                                title=""><span>Performance Appraisals</span></a>
                        </li>
                        <li id="menu-1145" class="menu-path-node-275"><a
                                href="http://beta.workforcetrack.com/content/hr_management%20#attendance-tracking"
                                title=""><span>Attendance Tracking</span></a>
                        </li>
                        <li id="menu-1146" class="menu-path-node-275"><a
                                href="http://beta.workforcetrack.com/content/hr_management%20#employee-profile"
                                title=""><span>Employee Profile</span></a>
                        </li>

                        <li id="menu-1147" class="menu-path-node-275"><a
                                href="http://beta.workforcetrack.com/content/hr_management%20#goal-management"
                                title=""><span>Goal Management</span></a>
                        </li>
                    </ul>
                    <div class='dd-shadow'>
                        <div></div>
                    </div>
                </td>
            </tr>
        </table>
    </div>
</li>
<li id="menu-1126" class="menuparent menu-path-node-277"><a
        href="http://beta.workforcetrack.com/content/payroll"
        title=""><span>Payroll</span></a>

    <div class="dd-menu">
        <table>
            <tr>
                <td>
                    <ul>
                        <li id="menu-1148" class="menu-path-node-277"><a
                                href="http://beta.workforcetrack.com/content/payroll#payslips"
                                title=""><span>Payslips</span></a></li>
                        <li id="menu-1149" class="menu-path-node-277"><a
                                href="http://beta.workforcetrack.com/content/payroll#payroll-reports"
                                title=""><span>Payroll Reports</span></a>
                        </li>
                        <li id="menu-1150" class="menu-path-node-277"><a
                                href="http://beta.workforcetrack.com/content/payroll#employee-payroll-accounts"
                                title=""><span>Employee Payroll Accounts</span></a>
                        </li>
                        <li id="menu-1151" class="menu-path-node-277"><a
                                href="http://beta.workforcetrack.com/content/payroll#efiling"
                                title=""><span>Efiling</span></a></li>
                    </ul>
                    <div class='dd-shadow'>
                        <div></div>
                    </div>
                </td>
            </tr>
        </table>
    </div>
</li>
<li id="menu-1128" class="menuparent menu-path-node-279"><a
        href="http://beta.workforcetrack.com/content/workspace"
        title=""><span>Workspace</span></a>

    <div class="dd-menu">
        <table>
            <tr>
                <td>
                    <ul>
                        <li id="menu-1152" class="menu-path-node-279"><a
                                href="http://beta.workforcetrack.com/content/workspace#calendar"
                                title=""><span>Calendar</span></a></li>

                        <li id="menu-1153" class="menu-path-node-279"><a
                                href="http://beta.workforcetrack.com/content/workspace#contacts"
                                title=""><span>Contacts</span></a></li>
                        <li id="menu-1154" class="menu-path-node-279"><a
                                href="http://beta.workforcetrack.com/content/workspace#notes"
                                title=""><span>Notes</span></a></li>
                        <li id="menu-1155" class="menu-path-node-279"><a
                                href="http://beta.workforcetrack.com/content/workspace#disussion-board"
                                title=""><span>Discussion Board</span></a>
                        </li>
                        <li id="menu-1156" class="menu-path-node-279"><a
                                href="http://beta.workforcetrack.com/content/workspace#news"
                                title=""><span>News</span></a></li>
                        <li id="menu-1157" class="menu-path-node-279"><a
                                href="http://beta.workforcetrack.com/content/workspace#message-centre"
                                title=""><span>Message Centre</span></a>
                        </li>
                        <li id="menu-1158" class="menu-path-node-279"><a
                                href="http://beta.workforcetrack.com/content/workspace#updates"
                                title=""><span>Updates</span></a></li>
                    </ul>
                    <div class='dd-shadow'>
                        <div></div>
                    </div>
                </td>
            </tr>
        </table>
    </div>
</li>
<li id="menu-1130" class="menuparent menu-path-node-281"><a
        href="http://beta.workforcetrack.com/content/ecommerce"
        title=""><span>Ecommerce</span></a>

    <div class="dd-menu">
        <table>
            <tr>
                <td>
                    <ul>
                        <li id="menu-1159" class="menu-path-node-281"><a
                                href="http://beta.workforcetrack.com/content/ecommerce#ecommerce"
                                title=""><span>Ecommerce</span></a></li>

                        <li id="menu-1160" class="menu-path-node-281"><a
                                href="http://beta.workforcetrack.com/content/ecommerce#websites"
                                title=""><span>Websites</span></a></li>
                        <li id="menu-1161" class="menu-path-node-281"><a
                                href="http://beta.workforcetrack.com/content/ecommerce#widgets"
                                title=""><span>Widgets</span></a></li>
                        <li id="menu-1162" class="menu-path-node-281"><a
                                href="http://beta.workforcetrack.com/content/ecommerce#storefront"
                                title=""><span>Storefront</span></a>
                        </li>
                        <li id="menu-1163" class="menu-path-node-281"><a
                                href="http://beta.workforcetrack.com/content/ecommerce#stock-control"
                                title=""><span>Stock Control</span></a>
                        </li>
                        <li id="menu-1166" class="menu-path-node-281"><a
                                href="http://beta.workforcetrack.com/content/ecommerce#payments"
                                title=""><span>Payments</span></a></li>
                    </ul>
                    <div class='dd-shadow'>
                        <div></div>
                    </div>
                </td>
            </tr>
        </table>
    </div>
</li>
<li id="menu-1129" class="menuparent menu-path-node-280"><a
        href="http://beta.workforcetrack.com/content/reporting"
        title=""><span>Reporting</span></a>

    <div class="dd-menu">
        <table>
            <tr>
                <td>
                    <ul>
                        <li id="menu-1164" class="menu-path-node-280"><a
                                href="http://beta.workforcetrack.com/content/reporting#import"
                                title=""><span>Import your own data</span></a>
                        </li>
                        <li id="menu-1165" class="menu-path-node-280"><a
                                href="http://beta.workforcetrack.com/content/reporting#reports"
                                title=""><span>WorkforceTrack reports</span></a>
                        </li>

                    </ul>
                    <div class='dd-shadow'>
                        <div></div>
                    </div>
                </td>
            </tr>
        </table>
    </div>
</li>
<li id="menu-1127" class="menu-path-node-278"><a
        href="http://beta.workforcetrack.com/content/documents"
        title=""><span>Documents</span></a></li>
</ul>
<div class='dd-shadow'>
    <div></div>
</div>
</td>
</tr>
</table>
</div>
</li>
<li id="menu-1109" class="menu-path-node-284"><a
        href="http://beta.workforcetrack.com/content/selected-customers" title=""><span>Customers</span></a>
</li>
<li id="menu-1107" class="menu-path-node-44"><a
        href="http://beta.workforcetrack.com/content/partners"
        title=""><span>Partners</span></a></li>
<li id="menu-1110" class="menu-path-node-270"><a
        href="http://beta.workforcetrack.com/content/faqs" title=""><span>FAQs</span></a></li>
<li id="menu-876" class="menu-path-node-214"><a
        href="http://beta.workforcetrack.com/content/help-support"
        title="Help &amp; Support"><span>Help</span></a></li>
<li id="menu-877" class="menu-path-content-blog"><a
        href="http://beta.workforcetrack.com/content/blog" title=""><span>Blogs</span></a></li>
<li id="menu-1173" class="menu-path-node-285"><a
        href="http://beta.workforcetrack.com/content/contact-us"
        title=""><span>Contact Us</span></a></li>

</ul>
</div>
</div>
</div>
</div>
<!--END #head-menu-->

</div>
<!--END #header-->


<!--begin content and ZoneIn-inner-->
<div id="cover">
    <div class="breadcrumb"><a href="http://beta.workforcetrack.com/">Home</a> › Pricing</div>
    <div id="sidebar-left" class="sidebar left-sidebar">

        <div id="block-block-11" class="clear-block block block-block">


            <div class="content"><br/><br/>
                <a href="http://app.workforcetrack.com/signup/showForm"><img
                        src="http://beta.workforcetrack.com/sites/all/themes/wft/images/art_btn_signin.png"/></a><br/><br/>
                <a href="http://beta.workforcetrack.com/content/request-demo"><img
                        src="http://beta.workforcetrack.com/sites/all/themes/wft/images/art_btn_request.png"/></a><br/><br/>
                <!-- http://www.LiveZilla.net Chat Button Link Code --><a
                        href="javascript:void(window.open('http://livehelp.workforcetracksupport.com/livezilla.php','','width=600,height=600,left=0,top=0,resizable=yes,menubar=no,location=yes,status=yes,scrollbars=yes'))"><img
                        src="http://livehelp.workforcetracksupport.com/image.php?id=02" width="227" height="59"
                        border="0" alt="LiveZilla Live Help"></a>
                <noscript>
                    <div><a href="http://livehelp.workforcetracksupport.com/livezilla.php" target="_blank">Start Live
                        Help Chat</a></div>
                </noscript>
                <!-- http://www.LiveZilla.net Chat Button Link Code --><!-- http://www.LiveZilla.net Tracking Code -->
                <div id="livezilla_tracking" style="display:none"></div>
                <script language="JavaScript" type="text/javascript">var script = document.createElement("script");
                script.type = "text/javascript";
                var src = "http://livehelp.workforcetracksupport.com/server.php?request=track&output=jcrpt&nse=" + Math.random();
                setTimeout("script.src=src;document.getElementById('livezilla_tracking').appendChild(script)", 1);</script>
                <!-- http://www.LiveZilla.net Tracking Code -->
                <br><br/>

            </div>
        </div>
        <div id="block-book-0" class="clear-block block block-book">

            <h2>Handbook</h2>

            <div class="content">
                <div id="book-block-menu-113" class="book-block-menu">
                    <ul class="menu">
                        <li class="collapsed last"><a
                                href="http://beta.workforcetrack.com/content/library/help-articles">Help Articles</a>
                        </li>
                    </ul>
                </div>
                <div id="book-block-menu-96" class="book-block-menu">
                    <ul class="menu">
                        <li class="leaf last"><a href="http://beta.workforcetrack.com/content/help">User Manuals, PDF
                            samples, Quick reference cards</a></li>
                    </ul>
                </div>
            </div>
        </div>

    </div>
    <!--begin #main-->
    <div id="main">

        <!--start Pricing GWT content-->
        <div id="contbody">
            <div id="container">


            </div>
            <!-- End wrapper content -->
        </div>
        <!--end Pricing GWT Content #contbody-->


    </div>
    <!--END #main-->

</div>
<!--END #ZoneIn-->


<!--begin #footer-->
<div id="footer">
    <div class="ZoneIn-1">

        <div id="block-block-10" class="clear-block block block-block">


            <div class="content">
                <ul class="cols">
                    <li class="parent">
                        <h2 class="title">Home</h2>
                        <ul>
                            <li><a href="http://beta.workforcetrack.com/content/workforcetrack-price-comparison">Compare
                                Us</a></li>
                            <li><a href="http://beta.workforcetrack.com/content/pricing">Pricing</a></li>

                            <li><a href="http://beta.workforcetrack.com/content/workforcetrack-products-list">Product
                                Services</a></li>
                            <li><a href="http://beta.workforcetrack.com/content/news">News</a></li>
                            <li><a href="http://beta.workforcetrack.com/content/blog">Blog</a></li>
                            <li><a href="http://beta.workforcetrack.com/content/help-support">Help/Support</a></li>
                        </ul>
                    </li>
                    <li class="parent">

                        <h2 class="title">Resources</h2>
                        <ul>
                            <li><a href="http://beta.workforcetrack.com/content/product-tours">Screenshots Tour</a></li>
                            <li>
                                <a href="http://beta.workforcetrack.com/sites/workforcetrack.com/files/slides/WFT_Leaflet_NEW.pdf">Download
                                    Brochures</a></li>
                            <li><a href="http://beta.workforcetrack.com/content/help">Download User Guides</a></li>
                            <li><a href="http://app.workforcetrack.com/shadowLogin?id=gfH%2FC8og8Q0%3D">Online Demo
                                Account</a></li>

                            <li><a href="#">Reviews</a></li>
                            <li>
                                <a href="javascript:void(window.open('http://livehelp.workforcetracksupport.com/livezilla.php','','width=600,height=600,left=0,top=0,resizable=yes,menubar=no,location=yes,status=yes,scrollbars=yes'))">Live
                                    Help</a></li>
                        </ul>
                    </li>
                    <li class="parent">
                        <h2 class="title">We Offer</h2>
                        <ul>

                            <li><a href="http://beta.workforcetrack.com/content/online-time-sheet-management-solution">Timesheet</a>
                            </li>
                            <li>
                                <a href="http://beta.workforcetrack.com/content/dashboards-and-report-generating-system">Dashboards</a>
                            </li>
                            <li><a href="http://beta.workforcetrack.com/content/web-based-payroll-system">Payroll
                                Solution</a></li>
                            <li>
                                <a href="http://beta.workforcetrack.com/content/online-employee-attendance-tracking-system">Attendance
                                    Tracking</a></li>
                            <li><a href="http://beta.workforcetrack.com/content/online-ecommerce-and-shopping-chart">StoreFront</a>
                            </li>
                            <li><a href="http://beta.workforcetrack.com/content/workforcetrack-products-list">much more
                                ...</a></li>

                        </ul>
                    </li>
                    <li class="parent">
                        <h2 class="title">Our Products</h2>
                        <ul>
                            <li><a href="http://beta.workforcetrack.com/content/online-project-management-software">Project
                                Management</a></li>
                            <li><a href="http://beta.workforcetrack.com/content/online-crm-system">Client Relation
                                Management</a></li>

                            <li>
                                <a href="http://beta.workforcetrack.com/content/hrms-goal-management-employee-self-service-system">Human
                                    Resources Management</a></li>
                            <li><a href="http://beta.workforcetrack.com/content/online-finance-and-accounting-system">Accounting
                                &amp; Finance</a></li>
                            <li><a href="http://beta.workforcetrack.com/content/online-ecommerce-and-shopping-chart">Ecommerce</a>
                            </li>
                            <li><a href="http://beta.workforcetrack.com/content/workforcetrack-products-list">much more
                                ...</a></li>
                        </ul>
                    </li>

                    <li class="parent">
                        <h2 class="title">Get connected</h2>
                        <ul>
                            <li><a href="http://www.facebook.com/home.php?#!/pages/Workforcetrack/209290605645?ref=ts">facebook</a>
                            </li>
                            <li><a href="http://www.twitter.com/workforcetrack">twitter</a></li>
                            <li><a href="http://www.linkedin.com/companies/finnet-limited">LinkedIn</a></li>
                            <li><a href="#">MySpace</a></li>

                            <li><a href="#">Delicious</a></li>
                            <li><a href="#">digg</a></li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
        <!-- end .cols -->

        <div id="foot-plate">

            <div class="BoxIn-1">
                <ul class="links secondary-links">
                    <li class="menu-1083 first"><a href="http://beta.workforcetrack.com/" title="">Home</a></li>
                    <li class="menu-1084"><a href="http://beta.workforcetrack.com/content/product-tours"
                                             title="">Tour</a></li>
                    <li class="menu-1086"><a href="http://beta.workforcetrack.com/" title="">Why WFT?</a></li>
                    <li class="menu-1092"><a href="http://beta.workforcetrack.com/content/privacy" title="">Privacy
                        Policy</a></li>
                    <li class="menu-1093"><a href="http://beta.workforcetrack.com/content/terms-of-use" title="">Terms
                        of Use</a></li>
                    <li class="menu-1094 last"><a href="http://beta.workforcetrack.com/content/contact-us" title="">Contact
                        Us</a></li>
                </ul>
                <div class="copyright">Copyright &copy; 2007 – 2010 <a target="_blanck"
                                                                       href="http://www.finnetlimited.com">Finnet
                    Limited</a> All Rights Reserved
                </div>

            </div>
        </div>

    </div>
</div>
<!--END #footer-->

</div>
<!--END #wrapper-->
<div style="position: fixed; bottom: 10px; right: 10px; z-index: 100;">
    <iframe src="http://www.google.com/talk/service/badge/Show?tk=z01q6amlqgdfsohfgdl0djsu8j2m56oigf5amugqbroubs7e5nv980eu6si6o4fqr7itggf06jf5ikrfm3tndpqad92eko0o7fbf3rmd614ip576ienu4itgfh74rrmtsecus5j5tvt8abvight8i8dmmacu8046jonsloa1f7us6qtvsospjqoe24bna8n4srjehgihk8fvgo2ccm9im3v8tlq0g&w=200&h=60"
            frameborder="0" allowtransparency="true" width="200" height="60"></iframe>
</div>
<script type="text/javascript">
    <!--//--><![CDATA[//><!--
    var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
    document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
    //--><!]]>

</script>
<script type="text/javascript">
    <!--//--><![CDATA[//><!--
    try {
        var pageTracker = _gat._getTracker("UA-355982-15");
        try {
            var pageTracker = _gat._getTracker("UA-355982-15");
            pageTracker._setDomainName(".workforcetrack.com");
            pageTracker._trackPageview();
        } catch(err) {
        }
    } catch(err) {
    }
    //--><!]]>
</script>


<%--End of Drupal Content--%>

</body>
</html>
