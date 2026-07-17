<%@ page import="com.edatasite.workforce.appContext.SpringPropertiesUtil" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!doctype html>
<html class="no-js" lang="en" dir="ltr">
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
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Forgot password</title>

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
                <div class="cp_login cp_login--step-2">
                    <div class="cp_login__content">
                        <div class="cp_login__title">
                            <fmt:message key="index.passwordRecovery"/>
                        </div>
                        <div class="cp_login__main">
                            <div class="cp_login__subtitle">
                                <fmt:message key="index.passwordRecoveryDescription"/>
                            </div>
                            <form action="/auth/forgotPassword.html" method="post">
                                <c:if test="${not empty message}">
                                    <div class="cp_login__message cp_login__message--error">
                                        <span class="desc">${message}</span>
                                    </div>
                                </c:if>
                                <div class="cp_login__field">
                                    <input id="login" name="email" type="text" placeholder="<fmt:message key="forgetpassword.email" />">
                                    <div class="cp_login__field-underline"></div>
                                </div>
                                <div class="cp_login__form-item">
                                    <input class="cp_login__submit elm_btn" type="submit" value="<fmt:message key="sendPassword" />"/>
                                </div>
                                <div class="cp_login__pass-forgot">
                                    <a href="/mainLogin" class="elm_btn--back"><span><fmt:message key="successpassword.goBackToSignInPage"/> </span></a>
                                </div>
                            </form>
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