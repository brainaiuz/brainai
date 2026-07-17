<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html xmlns="http://www.w3.org/1999/xhtml"
        >
<head>
    <meta content="text/html; charset=utf-8" http-equiv="Content-Type">

    <title>
        <tiles:getAsString name="title" ignore="true"/>
    </title>
        <% if (hostName.contains("aws")){%>
    <meta name="robots" content="noindex,nofollow">
        <%}%>

    <link rel="shortcut icon" href="/customisation/ezyadmin/images/favicon.ico" type="image/x-icon"/>

    <link href="//www.${helpHost}/sites/all/themes/ezyadmin/style.css" media="all" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="/customisation/preprod.kpi.com/style.css"/>
    <link rel="stylesheet" href="/customisation/preprod.kpi.com/appFrame.css"/>
    <link rel="stylesheet" href="http://www.ezymedia.com/wp-content/themes/TheCorporation/style.css"/>
    <link rel="stylesheet" href="/customisation/preprod.kpi.com/master.css"/>
    <link type="text/css" rel="stylesheet" media="all" href="//www.${helpHost}/sites/all/modules/nice_menus/nice_menus.css"/>
    <link type="text/css" rel="stylesheet" media="all" href="//www.${helpHost}/sites/all/modules/nice_menus/nice_menus_default.css"/>
    <meta content="time and attendance software,editor management system,editor management online,simple erp,online editor management,workforce tracking,time tracking,personnel online tracking,business tracking,business online tracking,staff tracking,online timesheet and time tracking software,timesheet software,timesheet reporting,crm online software,human resources management system" name="keywords">


    <tiles:insertAttribute name="style" ignore="true"/>
    <link href="/loginpage/kpi/afterdeletedshell.css" media="all" rel="stylesheet" type="text/css"/>
    <%----------------------------------------%>
    <!--[if lte IE 7]>
    <link rel="stylesheet" href="/loginpage/kpi/ie7-andLeft.css" type="text/css"/>
    <![endif]-->
    <!--[if lte IE 8]>
    <link rel="stylesheet" href="/loginpage/kpi/ie8-andLeft.css" type="text/css"/>
    <![endif]-->
        <%
        //Recaptcha script
        Boolean enableCaptcha = request.getAttribute("captcha") != null ? ((Boolean) request.getAttribute("captcha")) : false;
        String captchaTheme = request.getAttribute("captchaTheme") != null ? ((String) request.getAttribute("captchaTheme")) : "white";
    if (enableCaptcha) {
    %>
    <script type="text/javascript" src="https://www.google.com/recaptcha/api/js/recaptcha_ajax.js"></script>
        <% } %>
<head>


        <%if (request.getRequestURI().endsWith("index.jsp")) {%>
<body id="index">
    <% } else { %>
<body id="">
<% } %>

<!--begin #header-->
<div id="header">
    <div class="container">
        <!-- LOGO -->
        <a href="http://www.ezymedia.com">
            <img id="logo" height="74px" alt="EzyMedia Publishing" src="http://www.ezymedia.com/wp-content/themes/TheCorporation/images/logo.png">
        </a>
        <!-- TOP MENU -->
        <div id="contacttoplink"><a href="http://www.ezymedia.com/contact-us">contact us</a></div>
        <div id="top-menu">
            <ul class="superfish nav clearfix sf-js-enabled" id="menu-top-menu">
                <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-3067" id="menu-item-3067">
                    <a href="http://www.ezymedia.com/what-it-costs/">What it costs</a></li>
                <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-3614" id="menu-item-3614">
                    <a href="http://www.ezymedia.com/our-team/">Your Team</a></li>
                <li class="menu-item menu-item-type-custom menu-item-object-custom menu-item-3781" id="menu-item-3781">
                    <a href="http://www.ezymagazines.com" target="_blank">DEMO</a></li>
            </ul>
        </div>
        <!-- end #top-menu -->

        <!-- end searchform -->
    </div>
</div>
<!--END #header-->

<div class="line-nav">
    <div class="container">
        <ul class="linemenu" id="menu-clients">
            <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-413 active index1"
                id="menu-item-413"><a href="http://www.ezyadmin.com/content/take-project-management-software-screenshot-tour">Edition Manager</a></li>
            <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-412 active index2"
                id="menu-item-412"><a href="http://www.ezyadmin.com/content/crm-campaign-contact-and-notes">CRM</a></li>
            <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-411 index3"
                id="menu-item-411"><a href="http://www.ezyadmin.com/content/accounting-and-finance">Basic Accounting</a></li>
            <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-417 active index4"
                id="menu-item-417"><a href="http://www.ezyadmin.com/content/hr-management">HR & Collaboration</a></li>
        </ul>
    </div>
    <div class="clear"></div>
</div>
<tiles:insertAttribute name="body" ignore="false"/>

<!--end #index-page-->


<!--begin #footer-->
<div id="footer_bottomB">
    <div class="ftLast">
        <div class="clear-block block block-block" id="block-block-4">


            <div class="content">
                <div class="ftLast">

                    <div class="footer_bottom_box" style="width: 145px;">
                        <div class="footer_bottom_txt">PRODUCTS</div>
                        <ul id="menu-products" class="ul_fotter">
                            <li id="menu-item-3439" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-3439"><a href="http://www.ezymedia.com/publishing-websites/">Websites</a></li>
                            <li id="menu-item-3450" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-3450"><a href="http://www.ezymedia.com/newsletters/">Newsletters</a></li>
                            <li id="menu-item-3444" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-3444"><a href="http://www.ezymedia.com/publishing-revenue/">Revenues</a></li>
                            <li id="menu-item-3441" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-3441"><a href="http://www.ezymedia.com/ezyadmin/">Administrator</a></li>
                            <li id="menu-item-3449" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-3449"><a href="http://www.ezymedia.com/training/">Professional Dev.</a></li>
                            <li id="menu-item-3775" class="menu-item menu-item-type-custom menu-item-object-custom menu-item-3775"><a href="http://www.ezymedia.com/training-videos/">Training Videos</a></li>
                            <li id="menu-item-3963" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-3963"><a href="http://www.ezymedia.com/terms-conditions/">Terms &amp; Conditions</a></li>
                        </ul>
                    </div>


                    <div class="footer_bottom_box" style="width: 145px;">
                        <div class="footer_bottom_txt">SERVICES</div>
                        <ul id="menu-services" class="ul_fotter">
                            <li id="menu-item-3433" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-3433"><a href="http://www.ezymedia.com/page-layout-design/">Page Layout</a></li>
                            <li id="menu-item-3447" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-3447"><a href="http://www.ezymedia.com/graphics/">Graphics</a></li>
                            <li id="menu-item-3434" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-3434"><a href="http://www.ezymedia.com/publishing-content/">Content</a></li>
                            <li id="menu-item-3436" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-3436"><a href="http://www.ezymedia.com/social-media/">Social Media</a></li>
                            <li id="menu-item-3437" class="menu-item menu-item-type-post_type menu-item-object-page current-menu-item page_item page-item-1108 current_page_item menu-item-3437"><a href="http://www.ezymedia.com/marketing/">Marketing</a></li>
                            <li id="menu-item-3948" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-3948"><a href="http://www.ezymedia.com/seo/">SEO</a></li>
                            <li id="menu-item-3699" class="menu-item menu-item-type-custom menu-item-object-custom menu-item-3699"><a href="https://ezymedia.zendesk.com">Support</a></li>
                        </ul>
                    </div>


                    <div class="footer_bottom_box" style="width: 395px;">
                        <div class="footer_bottom_txt">BLOGS</div>
                        <ul class="ul_fotter">
                            <li><a href="http://www.ezymedia.com/blogs/funding-your-newspaper-or-magazine-startup/" title="Funding your newspaper or magazine startup">Funding your
                                newspaper or magazine startup</a></li>
                            <li><a href="http://www.ezymedia.com/blogs/how-newspapers-can-win-back-the-trust/" title="How newspapers can win back the trust">How newspapers can win
                                back the trust</a></li>
                            <li><a href="http://www.ezymedia.com/blogs/newspaper-page-layout-for-print/" title="Newspaper Page Layout for Print">Newspaper Page Layout for
                                Print</a></li>
                            <li><a href="http://www.ezymedia.com/blogs/how-the-weeklies-will-inherit-the-earth-trends-and-21-money-ideas/" title="How the weeklies will inherit the earth &ndash; 21 money makers for publishers">How
                                the weeklies will inherit the earth &ndash; 21 money makers for publishers</a></li>
                            <li><a href="http://www.ezymedia.com/blogs/" title="More Blog Articles"> More &gt;&gt; </a></li>
                        </ul>
                    </div>


                    <div class="footer_bottom_box">

                        <div class="phone_number">(USA) +1 774-226-7602 <br> (AUS) +61 401-847-853 <br> (UK) &nbsp; +44 1733 564 906
                        </div>
                        <div class="mclearer"></div>

                        <a href="http://www.ezymedia.com/contact-us">
                            <div class="footer_right_btnB">Contact us</div>
                        </a>


                        <div class="footer_right_btnM" id="footer_right_btnBa" style="cursor:pointer;">
                            <a href="http://www.ezymedia.com/free-trial/" title="Free Trial!">
                                Free Trial!
                            </a>
                        </div>

                    </div>
                </div>
            </div>
        </div>
        <!-- end .cols -->

    </div>

</div>
<!--END #footer-->

<tiles:insertAttribute name="script" ignore="true"/>

<%
    if (enableCaptcha != null && enableCaptcha) {%>
<script type="text/javascript">
    Recaptcha.create("<%=EdsContextParams.getRecaptchaPublicKey()%>", "recaptcha", {
        theme: "<%=captchaTheme%>"});
</script>
<% } %>

<%if (request.getRequestURI().endsWith("index.jsp")) {%>
<%--Facebook Script Begin--%>
<div id="fb-root"></div>
<script src="//connect.facebook.net/en_US/all.js"></script>
<script>
    FB.init({appId: '<%=facebookAppID%>', status: true, cookie: true, xfbml: true});

</script>
<% } %>
<%if (request.getRequestURI().endsWith("pricing.jsp")) {%>
<div style="position: fixed; bottom: 10px; right: 10px; z-index: 100;">
    <iframe src="//www.google.com/talk/service/badge/Show?tk=z01q6amlqgdfsohfgdl0djsu8j2m56oigf5amugqbroubs7e5nv980eu6si6o4fqr7itggf06jf5ikrfm3tndpqad92eko0o7fbf3rmd614ip576ienu4itgfh74rrmtsecus5j5tvt8abvight8i8dmmacu8046jonsloa1f7us6qtvsospjqoe24bna8n4srjehgihk8fvgo2ccm9im3v8tlq0g&w=200&h=60"
            frameborder="0" allowtransparency="true" width="200" height="60"></iframe>
</div>
<% } %>

<%--<script type="text/javascript">--%>
<%--(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){--%>
<%--(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),--%>
<%--m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)--%>
<%--})(window,document,'script','//www.google-analytics.com/analytics.js','ga');--%>

<%--ga('create', 'UA-59981695-14', 'auto');--%>
<%--ga('send', 'pageview');--%>

<%--</script>--%>

</body>
</html>