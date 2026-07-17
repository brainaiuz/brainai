<%@ page import="com.edatasite.workforce.appContext.SpringPropertiesUtil" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!doctype html>
<html class="no-js" lang="en" dir="ltr">
<head>

    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Account has been moved</title>
    <link rel="shortcut icon" href="/mainStyles/new-ui/login/img/favicon.ico?v=2" type="image/x-icon"/>
    <link rel="stylesheet" href="/mainStyles/new-ui/login/font-awesome/css/font-awesome.css"/>
    <link rel="stylesheet" href="/mainStyles/new-ui/login/slick/slick.css"/>
    <link rel="stylesheet" href="/mainStyles/new-ui/login/css/main.css?v=<%=SpringPropertiesUtil.getProperty("cssVersion")%>"/>

</head>
<body>
<div class="pg_landing">
<%--    <jsp:include page="newuiSlider.jsp"/>--%>

    <div class="pg_landing__container">
        <div class="pg_landing__header">
            <div class="pg_landing__logo">
                <a href="#">
                    <img src="../../mainStyles/new-ui/images/new-kpi-logo.svg?v=2" alt="logo"/>
                </a>
            </div>
        </div>
        <div class="pg_landing__main">
            <div class="pg_landing__main-innerbox">
                <div class="cp_login">
                    <div class="cp_login__content">
                        <c:if test="${empty param.domain}">
                            <div class="cp_login__textbox"><p class="title" id="domain">Sorry, your account has been moved to <a href="#"
                                                                                                      style="color:#0070f1;">${domain}</a>.
                                    You will be automatically redirected to corresponding login page after 10 seconds.
                                    If you want to access the login page now, please click<a href="#"
                                                                                             style="color:#0070f1;"
                                                                                             id="frw"
                                                                                             data-subdomain='${domain}'> ${domain}</a>
                                </p>
                                </c:if>
                                <c:if test="${empty domain}">
                                    <p class="title" id="domain">Sorry, your account has been moved to <a href="#"
                                                                                                          style="color:#0070f1;">${param.domain}</a>.
                                        You will be automatically redirected to corresponding login page after 10 seconds.
                                        If you want to access the login page now, please click<a href="#"
                                                                                                 style="color:#0070f1;"
                                                                                                 id="frw"
                                                                                                 data-subdomain='${param.domain}'> ${param.domain}</a>
                                    </p>
                                </c:if>

                                <p class="title" id="maintance"><fmt:message key="companymaintenance.companymaintenace"/></p>

                                <p><fmt:message key="companymaintenance.pleaseContactForMoreDetails"/></p>
                            </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="/mainStyles/new-ui/login/js/jquery.min.js"></script>
<script src="/mainStyles/new-ui/login/js/slick.min.js"></script>
<script src="/mainStyles/new-ui/login/js/main.js"></script>
</body>
</html>

<script type="text/javascript">

    var maintance = document.getElementById("maintance");
    var domain = document.getElementById("domain");
    var link = domain.lastElementChild;
    var domainhref = link.getAttribute("data-subdomain");
    if (domainhref == null || domainhref == "") {
        maintance.style.display = 'block';
        domain.style.display = 'none';
    } else {
        maintance.style.display = 'none';
        domain.style.display = 'block';
    }
    var forward = document.getElementById("frw");
    forward.onclick = function () {
        var domainport=window.location.port;
            window.location.href = window.location.protocol + "//"+domainhref+":"+domainport;
    }
    setTimeout("redirect()", 10000);
    function redirect() {
        var domainport=window.location.port;
        window.location.href = window.location.protocol + "//"+domainhref+":"+domainport;
    }

</script>