<%@ page import="com.edatasite.workforce.appContext.SpringPropertiesUtil" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!doctype html>
<html class="no-js" lang="en" dir="ltr">
<head>

    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Password Reminder</title>
    <link rel="shortcut icon" href="/mainStyles/new-ui/login/img/favicon.ico?v=2" type="image/x-icon"/>
    <link rel="stylesheet" href="/mainStyles/new-ui/login/font-awesome/css/font-awesome.css"/>
    <link rel="stylesheet" href="/mainStyles/new-ui/login/slick/slick.css"/>
    <link rel="stylesheet" href="/mainStyles/new-ui/login/css/main.css?v=<%=SpringPropertiesUtil.getProperty("cssVersion")%>"/>

</head>
<body>

<div class="pg_landing">
    <div class="pg_landing__container">
        <figure class="pg_landing__header">
            <div class="pg_landing__logo">
                <a href="#">
                    <img src="../../mainStyles/new-ui/images/new-kpi-logo.svg" alt="logo"/>
                </a>
            </div>
            <figcaption><fmt:message key="index.signInManageYourBusiness"/></figcaption>
        </figure>
        <div class="pg_landing__main">
            <div class="pg_landing__main-innerbox">
                <div class="cp_login cp_login--step-3">
                    <div class="cp_login__content">
                        <div class="cp_login__title"><fmt:message
                                key="successpassword.passwordReminderSuccess"/></div>
                        <div class="cp_login__main">
                            <p style="text-align: justify;"><fmt:message key="passwordHasBeenSent"/></p>

                            <div class="cp_login__pass-forgot">
                                <a href="/index.html" class="elm_btn--back"><fmt:message key="successpassword.goBackToSignInPage"/></a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div id="pg_landing-bg">

</div>

<script src="/mainStyles/new-ui/login/js/jquery.min.js"></script>
<script src="/mainStyles/new-ui/login/js/slick.min.js"></script>
<script src="/mainStyles/new-ui/login/js/main.js"></script>
</body>
</html>