<%@ page import="com.edatasite.workforce.gwt.core.client.ui.Constants" %>
<%@ page import="com.edatasite.workforce.utils.EdsContextParams" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<%--
  Created by IntelliJ IDEA.
  User: Ilhombek
  Date: 5/13/11
  Time: 7:08 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String hostName = (request.getAttribute("hostName") != null) ? request.getAttribute("hostName").toString() : request.getServerName();
    boolean isCompanyDomain = EdsContextParams.getCompanyDomain(request.getServerName());

    if (hostName != null && hostName.contains("ezyadmin")) { %>

<c:set var="analytics_code" value="UA-59981695-14"/>

<%} else if (hostName != null && hostName.contains("smebu")) { %>

<c:set var="analytics_code" value="UA-59981695-3"/>

<%} else if (hostName != null && hostName.contains("tjilo")) { %>

<c:set var="analytics_code" value="UA-59981695-19"/>

<%} else if (hostName != null && hostName.contains("omandatapark.com")) { %>

<c:set var="analytics_code" value="UA-59981695-9"/>

<%} else if (hostName != null && hostName.contains("preprod.kpi.com")) { %>

<c:set var="analytics_code" value="UA-355982-15"/>

<%} else if (hostName != null && hostName.contains("kpi.com.ru")) { %>

<c:set var="analytics_code" value="UA-59981695-6"/>

<%} else if (hostName != null && hostName.contains("iisholding.com")) { %>

<c:set var="analytics_code" value="UA-59981695-15"/>

<%} else if (hostName != null && hostName.contains("appiev.com")) { %>

<c:set var="analytics_code" value="UA-59981695-13"/>

<%} else if (hostName != null && hostName.contains("kah.sa")) { %>

<c:set var="analytics_code" value="UA-59981695-4"/>

<%} else if (hostName != null && hostName.contains("unisyserp.com")) { %>

<c:set var="analytics_code" value="UA-59981695-17"/>

<%} else if (hostName != null && hostName.contains("goodsystems.com.au")) { %>

<c:set var="analytics_code" value="UA-59981695-22"/>

<%} else if (hostName != null && (hostName.contains("upshott.com") || hostName.contains("erp.com") || hostName.contains("erp.ae"))) { %>

<c:set var="analytics_code" value="UA-59981695-16"/>

<%} else if (hostName != null && hostName.contains("talibro")) { %>

<c:set var="analytics_code" value="UA-43168248-1"/>

<%} else if (isCompanyDomain) { %>

<c:set var="analytics_code" value="UA-355982-15"/>

<%} else if (hostName != null && hostName.contains("financialit")) { %>

<c:set var="analytics_code" value="UA-355982-15"/>

<%} else if (hostName != null && hostName.contains("technosolutions")) { %>

<c:set var="analytics_code" value="UA-59981695-5"/>

<%} else if (hostName != null && hostName.contains("zairzaidi")) { %>

<c:set var="analytics_code" value="UA-59981695-10"/>

<%} else if (hostName != null && hostName.contains("basilurarabia")) { %>

<c:set var="analytics_code" value="UA-59981695-8"/>

<%} else if (hostName != null && hostName.contains("mynfra")) { %>

<c:set var="analytics_code" value="UA-59981695-12"/>

<%} else if (hostName != null && hostName.contains("fairtradeengergy")) { %>

<c:set var="analytics_code" value="UA-355982-15"/>

<%} else if (hostName != null && hostName.contains("stefanodesigns")) { %>

<c:set var="analytics_code" value="UA-59981695-11"/>

<%} else if (hostName != null && hostName.contains("b2xcg.com")) { %>

<c:set var="analytics_code" value="UA-59981695-7"/>

<%} else if (hostName != null && hostName.contains("vipworkspace")) { %>

<c:set var="analytics_code" value="UA-59981695-18"/>

<%} else if (hostName != null && hostName.contains("mykidstale")) { %>

<c:set var="analytics_code" value="UA-355982-15"/>

<%} else if (hostName != null && hostName.contains("enfion.com")) { %>

<c:set var="analytics_code" value="UA-59981695-21"/>

<%} else if (hostName != null && hostName.contains("1erp")) { %>

<c:set var="analytics_code" value="UA-355982-15"/>

<%} else if (hostName != null && hostName.contains("idaaerpservices")) { %>

<c:set var="analytics_code" value="UA-59981695-20"/>

<%} else if (hostName != null && hostName.contains("kg.kpi")) { %>

<c:set var="analytics_code" value="UA-59981695-23"/>

<%} else { %>

<c:set var="analytics_code" value="UA-355982-15"/>

<% } %>
<head>
   
    <!-- Google Tag Manager -->
    <script>(function (w, d, s, l, i) {
        w[l] = w[l] || [];
        w[l].push({
            'gtm.start':
                    new Date().getTime(), event: 'gtm.js'
        });
        var f = d.getElementsByTagName(s)[0],
                j = d.createElement(s), dl = l != 'dataLayer' ? '&l=' + l : '';
        j.async = true;
        j.src =
                'https://www.googletagmanager.com/gtm.js?id=' + i + dl;
        f.parentNode.insertBefore(j, f);
    })(window, document, 'script', 'dataLayer', 'GTM-WDZSMK');</script>
    <!-- End Google Tag Manager -->

    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <title><fmt:message key="welcomepage.thankYouForSigningUp"></fmt:message></title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="x-ua-compatible" content="ie=edge">

    <link rel="shortcut icon" href="/mainStyles/new-ui/login/img/favicon2.svg?v=2" type="image/svg"/>
    <link rel="stylesheet" href="/mainStyles/new-ui/css/materialize.min.css">
    <link rel="stylesheet" href="/mainStyles/new-ui/login/font-awesome/css/font-awesome.css"/>
    <link rel="stylesheet" href="/mainStyles/new-ui/login/slick/slick.css"/>
    <%--<script type=text/javascript src="/customisation/preprod.kpi.com/scripts/jquery-3.1.1.min.js"></script>--%>
</head>
<script type="text/javascript">

    <c:if test="${signedFromSocial != null and signedFromSocial eq true}">
    setTimeout("redirect()", 10000);
    </c:if>

    $(function () {
        var bgimage = new Image();
        var url = location.protocol + '//' + location.hostname + ":" + location.port + "/customisation/postroom/img/photo_2017-10-03_10-28-58.jpg";
        $('<img/>').attr('src', url).on('load', function () {
            $(this).remove();
            $('.pg-welcome').css({
                "conent": "",
                "position": "absolute",
                "z-index": "-1",
                "top": "0",
                "bottom": "0",
                "left": "0",
                "right": "0",
                "background-size": "cover",
                "animation": "bgfade 1.3s",
                "background-image": "url(" + url + ")"
            })
        });
    });

    function getCookie(c_name) {
        var i, x, y, ARRcookies = document.cookie.split(";");
        for (i = 0; i < ARRcookies.length; i++) {
            x = ARRcookies[i].substr(0, ARRcookies[i].indexOf("="));
            y = ARRcookies[i].substr(ARRcookies[i].indexOf("=") + 1);
            x = x.replace(/^\s+|\s+$/g, "");
            if (x == c_name) {
                return unescape(y);
            }
        }
    }

    function redirect() {
        var section = getCookie('SECTION_HTML');
        var path = "<%=Constants.DEFAULT_SECTION%>.html";
        //alert(section);
        if (section != null && section.match("html") != null) {
            path = section;
        }
        window.location = '/' + path;
    }

    var isInIFrame = (window.location != window.parent.location) ? true : false;
    if (isInIFrame) {
        parent.location = document.location;
    }
</script>

<body class="signUpStep signUpStep--1" toast="bottom-right">
<!-- Google Tag Manager (noscript) -->
<noscript>
    <iframe src="https://www.googletagmanager.com/ns.html?id=GTM-WDZSMK"
            height="0" width="0" style="display:none;visibility:hidden"></iframe>
</noscript>
<!-- End Google Tag Manager (noscript) -->
<script type="text/javascript" src="/mainStyles/js/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="/mainStyles/new-ui/js/materialize-0.97.5.min.js"></script>

<div class="modal-holder active">
    <div class="modal modal--md sign-up-step sign-up-step--1">
        <div class="modal-wrapper">
            <div class="modal-header">
                <figure>
                    <div class="visual">
                        <img src="/mainStyles/new-ui/icons/sign-up-step-1.svg" alt="image">
                    </div>
                    <figcaption class="hgroup">
                        <h1 class="fs-4">
                            <strong><fmt:message key="welcomepage.YouArealmostthere"/></strong>
                        </h1>
                        <h2 class="sub-title fs-3">
                            <fmt:message key="welcomepage.Verifyyouremailtogetstarted"/>
                        </h2>
                    </figcaption>
                </figure>
            </div>
            <div class="modal-content">
                <p class="simple-text"><fmt:message key="welcomepage.Tokeepyourfinancialinformationsafeandsecure"/>
                    <br><fmt:message key="welcomepage.anEmailHasBeenSentTo"/>
                    <br>
                    <a href="mailto:<c:out value="${adminEmail}"/>">
                        <b>
                            <c:out value="${adminEmail}"></c:out>
                        </b>
                    </a>
                </p>
            </div>
            <div class="modal-footer">
                <div class="simple-text"><fmt:message key="welcomepage.Didnotreceivetheemail"/>
                    <br/><fmt:message key="welcomepage.Сheckyourjunkorspamfolderorcontact"/>
                    <a href="mailto:support@kpi.com"><fmt:message key="welcomepage.support"/></a>.
                </div>
            </div>
        </div>
    </div>
    <div class="lean-overlay file--welcomePaage" id="materialize-lean-overlay-1" style="z-index: 1002; display: block; opacity: 0.5;"></div>
</div>

<!--SCRIPTS-->
<script>
    jQuery(document).ready(function () {
        $('select').material_select();
    });
</script>
</body>

<noscript>
    <div style="display:inline;">
        <img height="1" width="1" style="border-style:none;" alt=""
             src="//www.googleadservices.com/pagead/conversion/1068037200/?value=10&label=e1PuCLD89AMQ0Oij_QM&guid=ON&script=0"/>
    </div>
</noscript>

<!-- Global site tag (gtag.js) - Google Ads: 1068037200 -->
<script async src="https://www.googletagmanager.com/gtag/js?id=AW-1068037200"></script>
<script>
    window.dataLayer = window.dataLayer || [];

    function gtag() {
        dataLayer.push(arguments);
    }

    gtag('js', new Date());
    gtag('config', 'AW-1068037200');
</script>

<!-- Event snippet for Jay - sign up conversion page -->
<script>
    gtag('event', 'conversion', {
        'send_to': 'AW-1068037200/e1PuCLD89AMQ0Oij_QM',
        'value': 1.0,
        'currency': 'GBP'
    });
</script>

<!--New Google Analytics script-->
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
<!--New Google Analytics script-->
<%--TODO: move to https, it was commented as we implemented https--%>
<%--<img src="http://ad.retargeter.com/seg?add=132528" width="1" height="1"/>--%>
