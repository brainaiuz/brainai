<%@ page import="java.util.Date" %>
<%@ page import="com.edatasite.workforce.utils.EdsContextParams" %>
<%@ page import="java.util.Calendar" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en" dir="ltr">
<head>
    <title>
        <tiles:getAsString name="title" ignore="true"/>
    </title>
    <% if (hostName.contains("aws")){%>
    <meta name="robots" content="noindex,nofollow">
    <%}%>

    <link rel="stylesheet" href="/customisation/preprod.kpi.com/style.css"/>
    <link rel="stylesheet" href="/customisation/preprod.kpi.com/appFrame.css"/>
    <link rel="stylesheet" href="/customisation/preprod.kpi.com/master.css"/>
    <!--[if lt IE 9]>
    <script type="text/javascript" src="//html5shiv.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <meta http-equiv="X-UA-Compatible" content="chrome=1" />
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
            <a href="//www.${helpHost}" id="logo">
                <img src="/customisation/talibro/images/new_logo.png">
            </a>

            <hgroup>
                <div class="langBar">
                    <ul>
                        <li><a href="/" class="langEN">English</a></li>
                    </ul>
                </div>

                <div class="sitePhone">
                    <em>Need Help?</em>
                    <span>(UK) +44 207 148 4280</span>
                    <span>(US) +1 646 844 3330</span>
                </div>

                <div class="clearBox"></div>
            </hgroup>

            <div class="appActs">
                <a href="/signup/freeSignup.html" class="btn-1"><fmt:message key="freeTrial.title"/></a>
            </div>
            <!-- end logo site -->

            <nav class="menu-main-menu-container">
                <ul class="menu" id="menu-main-menu">

                    <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-557" id="menu-item-557">
                        <a href="http://www.${helpHost}/pricing.html"> <fmt:message key="frontendmain.pricing"/></a>
                        <ul class="sub-menu">
                            <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-538"
                                id="menu-item-538">
                                <a href="http://www.${helpHost}/comparison.html"><fmt:message key="template.footer.comparison"/></a>
                            </li>
                        </ul>
                    </li>
                    <li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-592" id="menu-item-592">
                        <a href="http://www.${helpHost}/contact-us.html"><fmt:message key="frontendmain.contactUsOnly"/></a>
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
                <ul class="menu" id="menu-stay-connected">
                    <li class="markEmail socialBookMark menu-item menu-item-type-custom menu-item-object-custom menu-item-18"
                        id="menu-item-18">
                        <a href="mailto:support@kpi.com" target="_blank">Email</a>
                    </li>
                    <li class="markTwitter socialBookMark menu-item menu-item-type-custom menu-item-object-custom menu-item-19"
                        id="menu-item-19">
                        <a href="//twitter.com/#!/KPI_Online" target="_blank">Twitter</a>
                    </li>
                    <li class="markLinkedin socialBookMark menu-item menu-item-type-custom menu-item-object-custom menu-item-20"
                        id="menu-item-20">
                        <a href="//www.linkedin.com/company/kpi-com---finnet-limited" target="_blank">LinkedIn</a>
                    </li>
                    <li class="markFacebook socialBookMark menu-item menu-item-type-custom menu-item-object-custom menu-item-21"
                        id="menu-item-21">
                        <a href="//www.facebook.com/pages/kpicom-Simply-Manage-Your-Business/364492530235669"
                           target="_blank">Facebook</a>
                    </li>
                </ul>

                <div class="sitePhone">
                    <em>Need Help?</em>
                    <span>(UK) +44 207 148 4280</span>
                    <span>(US) +1 646 844 3330</span>
                </div>

                <div class="footCaption">
                    <a href="//www.${helpHost}/" id="foot-logo">
                        <img alt="Logo" src="/customisation/preprod.kpi.com/images/foot_logo.png">
                    </a>
                    <span>Powered by Finnet Limited</span>

                    <p>
                        2007 &ndash;
                        <%
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(new Date());
                            int year = cal.get(Calendar.YEAR);
                        %>
                        <%=year%> &copy; ${helpHost}  All Rights Reserved.<br>
                        <a href="#">Terms of Use</a> | <a href="#">Privacy Policy</a>
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

<script type="text/javascript">

    var _gaq = _gaq || [];
    _gaq.push(['_setAccount', 'UA-43168248-1']);
    _gaq.push(['_trackPageview']);

    (function() {
        var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
        ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
        var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
    })();

</script>

</body>
</html>
