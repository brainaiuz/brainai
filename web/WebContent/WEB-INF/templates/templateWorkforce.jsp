<%--<%@ page import="com.edatasite.workforce.utils.EdsContextParams" %>--%>
<%--<%@ page import="org.springframework.web.servlet.support.RequestContext" %>--%>
<%--<%@ page import="java.util.Calendar" %>--%>
<%--<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>--%>
<%--<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html xmlns="http://www.w3.org/1999/xhtml">
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
    <link rel="shortcut icon" href="/customisation/kpi/images/favicon.ico" type="image/x-icon"/>

    <meta name="description"
          content="Online project management software helping customers to control and track business, all projects, collaborator&#039;s time and attendance (timesheet tracking), accounting. The project management tools are equipped with Massmailing, Dashboards &amp;amp; Reporting , Goal tracking, Performance Appraisal and Finance bookkeeping tools."/>
    <meta name="keywords"
          content="time and attendance software,project management system,project management online,simple erp,online project management,workforce tracking,time tracking,personnel online tracking,business tracking,business online tracking,staff tracking,online timesheet and time tracking software,timesheet software,timesheet reporting,crm online software,human resources management system"/>
    <%----------------------------------------%>
    <link type="text/css" rel="stylesheet" media="all" href="//www.${helpHost}/modules/system/defaults.css"/>

    <link type="text/css" rel="stylesheet" media="all"
          href="//www.${helpHost}/sites/all/modules/nice_menus/nice_menus.css"/>

    <link type="text/css" rel="stylesheet" media="all" href="//www.${helpHost}/sites/all/themes/wft/style.css"/>


    <script type="text/javascript" src="//www.${helpHost}/sites/all/modules/nice_menus/nice_menus.js"></script>
    <tiles:insertAttribute name="style" ignore="true"/>
    <link href="/loginpage/kpi/afterdeletedshell.css" media="all" rel="stylesheet" type="text/css"/>


    <%----------------------------------------%>
    <!--[if lte IE 7]>
    <link rel="stylesheet" href="/loginpage/kpi/ie7-andLeft.css" type="text/css"/>
    <![endif]-->
    <!--[if lte IE 8]>
    <link rel="stylesheet" href="/loginpage/kpi/ie8-andLeft.css" type="text/css"/>
    <![endif]-->
    <!--END new styles for KPI-->

    <meta name="msvalidate.01" content="358DD608AB06D6ED2E842AF53CAFFB9E"/>
    <META name="y_key" content="d766be7156d8deef"/>

    <meta name="google-site-verification" content="gmYe4Vbo_xjB_KKkqBF4Zs0p8GK6l3rV0M7n5Ql7wIM"/>
    <meta name="google-site-verification" content="jjw1ZnV5AryaQZs-KB64fCH3EpvkT94GKJAoFvLFzbU"/>
</head>
<body class="body-index">
<!-- Igors part -->
<!-- End Of Igors part -->
<!--begin #wrapper-->
<div id="wrapper">


<div id="header">
    <div id="head">

        <div id="block-block-9" class="clear-block block block-block">


            <div class="content"><a id="logo-site" href="/">
                <img src="${logoImage}" alt="Site Name"/>
            </a>


                <ul class="userMenu">
                    <li><a href="/index.html"> <fmt:message key="index.login"/> </a></li>
                    <li><a href="/signup/freeSignup.html"><fmt:message key="freeTrial.title"/></a></li>
                    <li><a href="//${helpHost}/content/contact-us"><fmt:message key="frontendmain.contactUsOnly"/></a>
                    </li>

                </ul>

                <ul class="languages">

                    <li class="lang-En active"><a title="English" href="//www.${hostName}">En</a></li>
                    <li class="lang-Ru"><a title="Russian" href="//www.kpi.com.ru">Ru</a></li>

                    <li class="lang-Pt"><a title="Portuguese"
                                           href="//www.${helpHost}/content/o-que-o-workforcetrack-representa-e-por-que-precisamos-dele">Pt</a>
                    </li>

                </ul>
            </div>
        </div>


    </div>

    <!--begin #navigation-->
    <div id="navigation">
        <div id="block-nice_menus-1" class="clear-block block block-nice_menus">


            <div class="content">
                <ul class="nice-menu nice-menu-right" id="nice-menu-1">
                    <li id="menu-307" class="menu-path-front act"><a href="/" title="" class="active"><fmt:message
                            key="frontendmain.home"/></a></li>
                    <li id="menu-1105" class="menuparent menu-path-node-2"><a href="//${helpHost}/content/product-tour"
                                                                              title=""><fmt:message
                            key="frontendmain.productTour"/></a>

                        <div class="dd-menu">
                            <ul>
                                <li id="menu-3680" class="menuparent menu-path-node-6686"><a
                                        href="//${helpHost}/content/screenshots" title=""><fmt:message
                                        key="frontendmain.productSnapshots"/></a>

                                    <div class="dd-menu">
                                        <ul>
                                            <li id="menu-3683" class="menu-path-node-6687"><a
                                                    href="//${helpHost}/content/project-management-screenshots"
                                                    title=""><fmt:message key="frontendmain.projectManagement"/></a>
                                            </li>
                                            <li id="menu-3682" class="menu-path-node-6688"><a
                                                    href="//${helpHost}/content/crm-screenshots" title=""><fmt:message
                                                    key="main.CRM"/></a></li>
                                            <li id="menu-3684" class="menu-path-node-6690"><a
                                                    href="//${helpHost}/content/accounting-and-finance-screenshots"
                                                    title=""><fmt:message key="frontendmain.accountingAndFinance"/></a>
                                            </li>
                                            <li id="menu-3685" class="menu-path-node-6691"><a
                                                    href="//${helpHost}/content/hrms-screenshots" title=""><fmt:message
                                                    key="main.HRMS"/></a></li>
                                            <li id="menu-3685" class="menu-path-node-6691"><a
                                                    href="//${helpHost}/content/dashboard-screenshots"
                                                    title=""><fmt:message key="frontendmain.dashboards"/></a></li>
                                            <li id="menu-3685" class="menu-path-node-6691"><a
                                                    href="//${helpHost}/content/document-screenshots"
                                                    title=""><fmt:message key="frontendmain.documents"/></a></li>
                                            <li id="menu-3685" class="menu-path-node-6691"><a
                                                    href="//${helpHost}/content/reporting-screenshots"
                                                    title=""><fmt:message key="frontendmain.reporting"/></a></li>
                                        </ul>
                                        <div class="dd-shadow">
                                            <div></div>
                                        </div>
                                    </div>
                                </li>
                                <li id="menu-1121" class="menu-path-node-63"><a
                                        href="//${helpHost}/content/take-project-management-software-screenshot-tour"
                                        title=""><fmt:message key="frontendmain.projectManagement"/></a></li>
                                <li id="menu-1115" class="menu-path-node-75"><a
                                        href="//${helpHost}/content/crm-campaign-contact-and-notes"
                                        title=""><fmt:message key="frontendmain.customerRelationshipManagement"/></a>
                                </li>
                                <li id="menu-1124" class="menu-path-node-275"><a
                                        href="//${helpHost}/content/hr-management" title=""><fmt:message
                                        key="frontendmain.hrManagement"/></a></li>
                                <li id="menu-1125" class="menu-path-node-276"><a
                                        href="//${helpHost}/content/accounting-and-finance-0" title=""><fmt:message
                                        key="frontendmain.accountingAndFinance"/></a>
                                </li>
                                <li id="menu-1127" class="menu-path-node-278"><a
                                        href="//${helpHost}/content/online-document-management-use"
                                        title=""><fmt:message key="frontendmain.documentsManagement"/></a>
                                </li>
                                <li id="menu-1129" class="menu-path-node-280"><a href="//${helpHost}/content/reporting"
                                                                                 title=""><fmt:message
                                        key="frontendmain.reporting"/></a>
                                </li>
                            </ul>
                            <div class="dd-shadow">
                                <div></div>
                            </div>
                        </div>
                    </li>
                    <li id="menu-2407" class="menuparent menu-path-node-6495">
                        <a href="//${helpHost}/pricing.html" title="Microsoft Project Alternative | CRM Software | CRM Online">
                            <fmt:message key="frontendmain.pricing"/>
                        </a>

                        <div class="dd-menu">
                            <ul>
                                <li id="menu-3726" class="menu-path-node-6704">
                                    <a href="//${helpHost}/content/whykpi" title="<fmt:message key="frontendmain.whyKpi"/>">
                                        <fmt:message key="frontendmain.whyKpi"/>
                                    </a>
                                </li>
                                <li id="menu-3727" class="menu-path-node-6705">
                                    <a href="//${helpHost}/content/price-and-packages" title="<fmt:message key="frontendmain.pricingAndPackages"/>">
                                        <fmt:message key="frontendmain.pricingAndPackages"/>
                                    </a>
                                </li>
                            </ul>
                            <div class="dd-shadow">
                                <div></div>
                            </div>
                        </div>
                    </li>
                    <li id="menu-3046" class="menuparent menu-path-node-214"><a
                            href="//${helpHost}/support"
                            title=""><fmt:message key="frontendmain.helpSupport"/></a>

                        <div class="dd-menu">
                            <ul>
                                <li id="menu-876" class="menuparent menu-path-node-214"><a
                                        href="//${helpHost}/support"
                                        title="Help &amp; Support"><fmt:message key="frontendmain.help"/></a>

                                    <div class="dd-menu">
                                        <ul>
                                            <li id="menu-2539" class="menu-path-node-214"><a
                                                    href="//${helpHost}/support" title=""><fmt:message
                                                    key="frontendmain.helpArticles"/></a></li>
                                            <li id="menu-2478" class="menu-path-forum"><a href="//${helpHost}/forum"
                                                                                          title=""><fmt:message
                                                    key="frontendmain.Forum"/></a></li>
                                        </ul>
                                        <div class="dd-shadow">
                                            <div></div>
                                        </div>
                                    </div>
                                </li>
                                <li id="menu-1110" class="menu-path-node-270"><a href="//${helpHost}/content/faqs"
                                                                                 title=""><fmt:message
                                        key="frontendmain.faqs"/></a>
                                </li>
                            </ul>
                            <div class="dd-shadow">
                                <div></div>
                            </div>
                        </div>
                    </li>
                    <li id="menu-3686" class="menuparent menu-path-node-6662"><a href="//${helpHost}/content/resources"
                                                                                 title=""><fmt:message
                            key="frontendmain.resources"/></a>

                        <div class="dd-menu">
                            <ul>
                                <li id="menu-3687" class="menu-path-node-6693"><a
                                        href="//${helpHost}/content/guidebooks" title=""><fmt:message
                                        key="frontendmain.guidebooks"/></a>
                                </li>
                                <li id="menu-2443" class="menuparent menu-path-node-6662"><a
                                        href="//${helpHost}/content/help-videos"
                                        title=""><fmt:message key="frontendmain.helpVideos"/></a>

                                    <div class="dd-menu">
                                        <ul>
                                            <li id="menu-3110" class="menu-path-node-6648"><a
                                                    href="//${helpHost}/content/project-management-help-videos"
                                                    title="Project Management"><fmt:message
                                                    key="frontendmain.projectManagement"/></a></li>
                                            <li id="menu-3111" class="menu-path-node-6643"><a
                                                    href="//${helpHost}/content/crm-help-videos"
                                                    title="CRM"><fmt:message key="main.CRM"/></a></li>
                                            <li id="menu-3114" class="menu-path-node-6642"><a
                                                    href="//${helpHost}/content/accounting-and-finance-help-videos"
                                                    title="Accounting and Finance"><fmt:message
                                                    key="frontendmain.accountingAndFinance"/></a></li>
                                            <li id="menu-3112" class="menu-path-node-6645"><a
                                                    href="//${helpHost}/content/hrms-help-videos"
                                                    title="HRMS"><fmt:message key="main.HRMS"/></a></li>
                                            <li id="menu-3113" class="menu-path-node-6647"><a
                                                    href="//${helpHost}/content/payroll-help-videos"
                                                    title="Payroll"><fmt:message key="frontendmain.payroll"/></a></li>
                                        </ul>
                                        <div class="dd-shadow">
                                            <div></div>
                                        </div>
                                    </div>
                                </li>
                                <li id="menu-3108" class="menu-path-node-6652"><a
                                        href="//${helpHost}/content/case-studies"
                                        title="Case studies"><fmt:message key="frontendmain.caseStudies"/></a>
                                </li>
                            </ul>
                            <div class="dd-shadow">
                                <div></div>
                            </div>
                        </div>
                    </li>
                    <li id="menu-1097" class="menuparent menu-path-node-46"><a href="//${helpHost}/content/about" title=""><fmt:message key="frontendmain.about"/></a>

                        <div class="dd-menu">
                            <ul>
                                <li id="menu-3047" class="menu-path-testimonials"><a href="//${helpHost}/content/about" key="frontendmain.about"/> </a> </li>
                                <li id="menu-3047" class="menu-path-testimonials"><a href="//${helpHost}/testimonials" key="frontendmain.Testimonials"/> </a> </li>
                                <li id="menu-3676" class="menu-path-node-6685"><a href="//${helpHost}/content/careers" title=""><fmt:message key="frontendmain.Careers"/></a></li>
                                <li id="menu-3677" class="menu-path-content-customers"><a
                                        href="//${helpHost}/content/customers"
                                        title=""><fmt:message key="frontendmain.ourClients"/></a></li>
                                <li id="menu-3678" class="menu-path-node-221"><a href="//${helpHost}/content/sitemap"
                                                                                 title=""><fmt:message
                                        key="frontendmain.sitemap"/></a>
                                </li>
                                <li id="menu-3679" class="menu-path-node-50"><a
                                        href="//${helpHost}/content/terms-of-use" title=""><fmt:message
                                        key="frontendmain.termsofUse"/></a></li>
                            </ul>
                            <div class="dd-shadow">
                                <div></div>
                            </div>
                        </div>
                    </li>
                    <li id="menu-877" class="menu-path-content-blog"><a href="//${helpHost}/content/blog"
                                                                        title=""><fmt:message
                            key="frontendmain.blog"/></a></li>
                    <li id="menu-3675" class="menu-path-node-6680"><a href="//${helpHost}/services"
                                                                      title=""><fmt:message
                            key="frontendmain.Services"/></a></li>
                </ul>
            </div>
        </div>
    </div>
    <!--END #navigation-->


</div>
<!--END #header-->

<!-- <div id="index-page"> -->
<tiles:insertAttribute name="body" ignore="false"/>


<!--begin #footer-->
<div id="footer">
    <div class="footNav">
        <div id="block-block-10" class="clear-block block block-block">


            <div class="content">
                <ul class="cols">
                    <li class="parent">
                        <h2 class="title"><fmt:message key="frontendmain.home"/></h2>
                        <ul>
                            <li><a href="//${helpHost}/content/workforcetrack-products-list"><fmt:message
                                    key="frontendmain.productServices"/></a></li>
                            <li><a href="//${helpHost}/content/blog"><fmt:message key="frontendmain.newsOrBlog"/></a>
                            </li>
                            <li><a href="//${helpHost}/content/sitemap"><fmt:message key="frontendmain.sitemap"/></a>
                            </li>
                            <li><a href="//${helpHost}/content/about"><fmt:message key="frontendmain.aboutUs"/></a></li>
                            <li><a href="//${helpHost}/forum"><fmt:message key="frontendmain.Forum"/></a></li>

                        </ul>
                    </li>

                    <li class="parent">
                        <h2 class="title"><fmt:message key="frontendmain.weOffer"/></h2>
                        <ul>
                            <li><a href="//${helpHost}/content/online-time-sheet-management-solution"><fmt:message
                                    key="frontendmain.timesheet"/></a></li>
                            <li><a href="//${helpHost}/content/dashboards-and-report-generating-system"><fmt:message
                                    key="frontendmain.dashboards"/></a></li>
                            <li><a href="//${helpHost}/content/web-based-payroll-solution"><fmt:message
                                    key="frontendmain.payrollSolution"/></a></li>
                            <li><a href="//${helpHost}/content/online-employee-attendance-tracking-system"><fmt:message
                                    key="frontendmain.attendanceTracking"/></a>
                            </li>
                            <li><a href="//${helpHost}/content/project-management-google-apps"><fmt:message
                                    key="frontendmain.projectManagementforGoogleApps"/></a>
                            </li>

                        </ul>
                    </li>
                    <li class="parent">
                        <h2 class="title"><fmt:message key="frontendmain.ourProducts"/></h2>
                        <ul>
                            <li><a href="//${helpHost}/content/online-project-management-software"><fmt:message
                                    key="frontendmain.projectManagement"/></a></li>
                            <li><a href="//${helpHost}/content/online-crm-system"><fmt:message
                                    key="frontendmain.clientRelationManagement"/></a></li>
                            <li>
                                <a href="//${helpHost}/content/hrms-goal-management-employee-self-service-system"><fmt:message
                                        key="frontendmain.humanResourcesManagement"/></a></li>
                            <li><a href="//${helpHost}/content/online-finance-and-accounting-system"><fmt:message
                                    key="frontendmain.accountingFinance"/></a>
                            </li>
                            <li><a href="//${helpHost}/content/online-ecommerce-and-shopping-chart"><fmt:message
                                    key="frontendmain.ecommerce"/></a></li>

                        </ul>
                    </li>
                    <li class="parent">
                        <h2 class="title"><fmt:message key="frontendmain.getconnected"/></h2>
                        <ul>
                            <li><a href="https://www.facebook.com/pages/KPI/107864749330549?sk=wall">Facebook</a></li>
                            <li><a href="https://twitter.com/#!/KPI_Online">Twitter</a></li>
                            <li><a href="http://www.linkedin.com/company/kpi-com---finnet-limited">LinkedIn</a></li>

                        </ul>
                    </li>

                    <li class="parent signs">
                        <a class="key-GoogleApp"
                           href="https://www.google.com/enterprise/marketplace/viewVendorListings?vendorId=3964">Google
                            apps</a>
                        <br>
                        <a href="http://www.saasdir.co.uk/search/profile.aspx?spid=20180"><img
                                src="//www.${helpHost}/sites/all/themes/wft/images/art_medal-1.png"
                                alt="Most Popular, July 2011 - saasdir.co.uk"></a>
                    </li>
                </ul>
            </div>
        </div>
        <!-- end .cols -->

    </div>
    <div class="clear c-box">
        <div class="copyright">2007 &ndash; <%=Calendar.getInstance().get(Calendar.YEAR)%> &copy;
            <a href="/index.html"> ${helpHost} </a> <br/> <fmt:message
                    key="frontendmain.allRightsReserved"/>
        </div>

        <a class="right" target="_blank" href="http://www.finnetlimited.com"><fmt:message
                key="frontendmain.poweredBy"/> <img
                src="//www.${helpHost}/sites/all/themes/wft/images/logo_finnet.png"
                alt="Finnet Limited "/></a>
    </div>
</div>
<!--END #footer-->

</div>

<!--END #wrapper-->

<tiles:insertAttribute name="script" ignore="true"/>
<%if ((request.getRequestURI().endsWith("welcomePage.jsp") || (request.getRequestURI().endsWith("freeTrial.jsp"))) && isRussian) {%>
<!-- Google Code for Free Trial Sign up Conversion Page halim change -->
<script type="text/javascript">
    /* <![CDATA[ */
    var google_conversion_id = 952099144;
    var google_conversion_language = "en";
    var google_conversion_format = "2";
    var google_conversion_color = "ffffff";
    var google_conversion_label = "LheeCLj90QMQyML_xQM";
    var google_conversion_value = 0;
    /* ]]> */
</script>
<script type="text/javascript" src="http://www.googleadservices.com/pagead/conversion.js">
</script>
<noscript>
    <div style="display:inline;">
        <img height="1" width="1" style="border-style:none;" alt=""
             src="http://www.googleadservices.com/pagead/conversion/952099144/?label=LheeCLj90QMQyML_xQM&guid=ON&script=0"/>
    </div>
</noscript>
<% } %>


<%--<%if (request.getRequestURI().endsWith("index.jsp")) {%>--%>

<%--<div id="fb-root"></div>--%>
<%--<script src="//connect.facebook.net/en_US/all.js"></script>--%>
<%--<script>--%>
    <%--FB.init({appId: <%=facebookAppID%>, status:true, cookie:true, xfbml:true});--%>

<%--</script>--%>
<%--<% } %>--%>

<!-- Abdulla Turdialiev script -->
<!--Start of Zopim Live Chat Script-->
<script type="text/javascript">
    window.$zopim||(function(d,s){var z=$zopim=function(c){z._.push(c)},$=z.s=
            d.createElement(s),e=d.getElementsByTagName(s)[0];z.set=function(o){z.set.
            _.push(o)};z._=[];z.set._=[];$.async=!0;$.setAttribute('charset','utf-8');
        $.src='//cdn.zopim.com/?VFo2s9nYuh7nzXzmhXUzKIIeTLJ07PBu';z.t=+new Date;$.
                type='text/javascript';e.parentNode.insertBefore($,e)})(document,'script');
</script>
<!--End of Zopim Live Chat Script-->
<!-- Abdulla Turdialiev script end-->
<%if (request.getRequestURI().endsWith("pricing.jsp")) {%>
        <div style="position: fixed; bottom: 10px; right: 10px; z-index: 100;">
            <iframe src="//www.google.com/talk/service/badge/Show?tk=z01q6amlqgdfsohfgdl0djsu8j2m56oigf5amugqbroubs7e5nv980eu6si6o4fqr7itggf06jf5ikrfm3tndpqad92eko0o7fbf3rmd614ip576ienu4itgfh74rrmtsecus5j5tvt8abvight8i8dmmacu8046jonsloa1f7us6qtvsospjqoe24bna8n4srjehgihk8fvgo2ccm9im3v8tlq0g&w=200&h=60"
                    frameborder="0" allowtransparency="true" width="200" height="60"></iframe>
        </div>
<% } %>

<script type="text/javascript">

    var _gaq = _gaq || [];
    _gaq.push(['_setAccount', 'UA-355982-15']);
    _gaq.push(['_setDomainName', '.kpi.com']);
    _gaq.push(['_trackPageview']);

    (function () {
        var ga = document.createElement('script');
        ga.type = 'text/javascript';
        ga.async = true;
        ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
        var s = document.getElementsByTagName('script')[0];
        s.parentNode.insertBefore(ga, s);
    })();

</script>


</body>
</html>