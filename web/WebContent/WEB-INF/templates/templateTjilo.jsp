<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
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

    <script type="text/javascript" src="/customisation/preprod.kpi.com/scripts/jquery.js"></script>
    <link rel="stylesheet" href="/customisation/tjilo.com/appFrame.css"/>
    <link rel="stylesheet" href="/customisation/activira/style.css"/>


    <link rel="shortcut icon" href="/customisation/tjilo.com/images/favicon.ico" type="image/x-icon"/>
    <link type="text/css" rel="stylesheet" media="all" href="../../customisation/tjilo.com/content-module.css"/>
    <link type="text/css" rel="stylesheet" media="all" href="../../customisation/tjilo.com/filefield.css"/>
    <link type="text/css" rel="stylesheet" media="all" href="../../customisation/tjilo.com/nice_menus.css"/>
    <link type="text/css" rel="stylesheet" media="all" href="../../customisation/tjilo.com/nice_menus_default.css"/>
    <link type="text/css" rel="stylesheet" media="all" href="../../customisation/tjilo.com/fieldgroup.css"/>
    <link type="text/css" rel="stylesheet" media="all" href="../../customisation/tjilo.com/style.css"/>
    <link type="text/css" rel="stylesheet" media="print" href="../../customisation/tjilo.com/print.css"/>

    <tiles:insertAttribute name="style" ignore="true"/>
    <link href="/customisation/${productNameLower}/loginpage.css" rel="stylesheet" type="text/css"/>
    <%----------------------------------------%>
    <!--[if lte IE 7]>
    <link rel="stylesheet" href="/loginpage/kpi/ie7-andLeft.css" type="text/css"/>
    <![endif]-->
    <!--[if lte IE 8]>
    <link rel="stylesheet" href="/loginpage/kpi/ie8-andLeft.css" type="text/css"/>
    <![endif]-->
    <style type="text/css">
        #appLoginForm table {
            margin: 0 0 0 130px !important;
        }
    </style>
    <%
        //Recaptcha script
        Boolean enableCaptcha = request.getAttribute("captcha") != null ? ((Boolean) request.getAttribute("captcha")) : false;
        String captchaTheme = request.getAttribute("captchaTheme") != null ? ((String) request.getAttribute("captchaTheme")) : "white";
        if (enableCaptcha) {
    %>
    <script type="text/javascript" src="https://www.google.com/recaptcha/api/js/recaptcha_ajax.js"></script>
    <% } %>
</head>
<body>
<div id="header" class="clear-block">
    <div class="c-box">
        <h1>
            <a href="//www.tjilo.com" title="Simple to use and quick to implement">
                <!--<img id="logo" alt="Tjilo.com" src="/sites/all/themes/tjilo/images/logo.png" />-->
                <span>Tjilo.com - Direct operationeel en simpel te gebruiken</span>
            </a>
        </h1>
        <ul id="nice-menu-1" class="nice-menu nice-menu-right">
            <li class="menu-path-front_page" id="menu-332"><a title="" href="//${helpHost}">Home</a></li>
            <li class="menu-path-node-27" id="menu-333"><a title="" href="//www.${helpHost}/content/product-tour">Moduleoverzicht</a></li>
            <li class="menu-path-node-28" id="menu-334"><a title="" href="//www.${helpHost}/pricing">Prijzen</a></li>
            <li class="menu-path-node-29" id="menu-335"><a title="" href="//www.${helpHost}/content/support">Support</a></li>
            <li class="menu-path-node-31" id="menu-337"><a title="" href="//www.${helpHost}/content/nieuws">Nieuws</a></li>
            <li class="menu-path-node-30" id="menu-336"><a title="" href="//www.${helpHost}/content/over-ons">Over ons</a></li>
            <li class="menu-path-node-32 act" id="menu-338"><a class="active" title="" href="//www.${helpHost}/content/dienstverlening">Dienstverlening</a></li>
            <li class="menu-path-node-33" id="menu-339"><a title="" href="//www.${helpHost}/content/contacts">Contact</a></li>
        </ul>
        <div id="userOptions">
            <ul class="userMenu">
                <li><a href="http://app.tjilo.com">Log in</a></li>
                <li><a href="http://app.tjilo.com/signup/freeSignup.html">Sign Up</a></li>
            </ul>
        </div>
        <p><a class="btn btn_getFree"
              style="text-indent: -9999px !important; height: 75px; left: 484px; position: absolute; top: 262px; width: 436px;"
              href="/signup/freeSignup.html">Get Free Trial</a></p>
        <a class="btnDemo" href="/signup/freeSignup.html">Aanvraag demo</a>
        <%--<a class="btnChat" href="/">Live chat</a>--%>
    </div>
</div>
<div id="cover">

    <!--begin #content-->
    <div id="content" class="full-main">
        <tiles:insertAttribute name="body" ignore="false"/>

    </div>
</div>
<div id="footer">
    <!-- Start 	footer primary links -->
    <!-- Start 	footer primary links -->
    <ul class="cols">
        <li class="parent">
            <h2 class="title">Urenstaat</h2>
            <ul>
                <li><a href="//www.${helpHost}/content/product-tour">Productrondleiding</a></li>
                <li><a href="//www.${helpHost}/pricing">Prijsstelling</a></li>
                <li><a href="//www.${helpHost}/content/support">Ondersteuning</a></li>
                <li><a href="//www.${helpHost}/content/about">Informatie</a></li>
                <li><a href="//www.${helpHost}/content/sitemap">Sitemap</a></li>
            </ul>
        </li>


        <li class="parent">
            <h2 class="title">Onze producten</h2>
            <ul>
                <li><a href="//www.${helpHost}/content/take-project-management-software-screenshot-tour">Projectmanagement</a></li>
                <li><a href="//www.${helpHost}/content/crm-campaign-contact-and-notes">Relatiebeheer (CRM)</a></li>
                <li><a href="//www.${helpHost}/content/hr-management">Personeelsmanagement (HRM)</a></li>
                <li><a href="//www.${helpHost}/content/accounting-and-finance">Financiële administratie</a></li>
                <li><a href="//www.${helpHost}/content/ecommerce">E-commerce</a></li>

            </ul>
        </li>
        <li class="parent">
            <h2 class="title">Verbinden</h2>
            <ul>
                <li><a href="#&quot;">Facebook</a></li>
                <li><a href="#">Twitter</a></li>
                <li><a href="#">LinkedIn</a></li>
            </ul>
        </li>


    </ul>
</div>
<%
    if (enableCaptcha != null && enableCaptcha) {%>
<script type="text/javascript">
    Recaptcha.create("<%=EdsContextParams.getRecaptchaPublicKey()%>", "recaptcha", {
        theme: "<%=captchaTheme%>"});
</script>
<% } %>
<tiles:insertAttribute name="script" ignore="true"/>

<%--<script type="text/javascript">--%>
<%--(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){--%>
<%--(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),--%>
<%--m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)--%>
<%--})(window,document,'script','//www.google-analytics.com/analytics.js','ga');--%>

<%--ga('create', 'UA-59981695-19', 'auto');--%>
<%--ga('send', 'pageview');--%>

<%--</script>--%>
</body>
</html>
