<%@ page import="com.edatasite.workforce.appContext.SpringPropertiesUtil" %>
<%@ page import="com.edatasite.workforce.utils.EdsContextParams" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="title" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<% String hostName = (request.getAttribute("hostName") != null) ? request.getAttribute("hostName").toString() : request.getServerName(); %>

<!doctype html>
<html class="no-js" lang="en" dir="ltr">
<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign in or Sign up to kpi.com</title>
    <c:if test="${faviconEnable}">
        <link rel="shortcut icon" href="${faviconUrl}" type="image/x-icon"/>
    </c:if>
    <link rel="stylesheet" href="/mainStyles/new-ui/login/font-awesome/css/font-awesome.css"/>
    <link rel="stylesheet" href="/mainStyles/new-ui/login/slick/slick.css"/>
    <link rel="stylesheet"
          href="/mainStyles/new-ui/login/css/main.css?v=<%=SpringPropertiesUtil.getProperty("cssVersion")%>"/>

    <script src="/mainStyles/new-ui/login/js/jquery.min.js"></script>
    <script src="https://apis.google.com/js/client:platform.js?onload=start" async defer>
    </script>
    <script>
        function start() {
            gapi.load('auth2', function () {
                auth2 = gapi.auth2.init({
                    client_id: '${GOOGLE_CLIENT_ID}',//'YOUR_CLIENT_ID.apps.googleusercontent.com',
                    // Scopes to request in addition to 'profile' and 'email'
                    //scope: 'additional_scope'
                });
            });
        }
    </script>
    <!-- Google tag (gtag.js) -->
    <script async src="https://www.googletagmanager.com/gtag/js?id=G-JHFJELYCR7"></script>
    <script>
        window.dataLayer = window.dataLayer || [];

        function gtag() {
            dataLayer.push(arguments);
        }

        gtag('js', new Date());

        gtag('config', 'G-JHFJELYCR7');
    </script>

    <!-- Google tag (gtag.js) -->
    <script async src="https://www.googletagmanager.com/gtag/js?id=AW-1068037200"></script>
    <script>
        window.dataLayer = window.dataLayer || [];

        function gtag() {
            dataLayer.push(arguments);
        }

        gtag('js', new Date());

        gtag('config', 'AW-1068037200');
    </script>
    <script src="https://www.google.com/recaptcha/api.js" async defer></script>
    <script>
        async function validateAndSubmit(event) {
            event.preventDefault();
            var captchaEnabled = <%= (Boolean) request.getAttribute("captcha") %>;
            if (captchaEnabled != null) {
                var response = grecaptcha.getResponse();

                if (response.length === 0) {
                    alert("Please complete the reCAPTCHA.");
                    return;
                }
            }
            document.getElementById("mainLogin").submit();
        }
    </script>
</head>
<body>
<div class="pg_landing">
    <div class="pg_landing__container">
        <figure class="pg_landing__header">
            <c:if test="${logoEnable}">
                <div class="pg_landing__logo">
                    <a href="#">
                        <img src="${logoUrl}" alt="logo" style="height:80px !important;width: 250px !important; "/>
                    </a>
                </div>
            </c:if>
            <c:if test="${descriptionEnable}">
                <figcaption>${description}</figcaption>
            </c:if>
        </figure>
        <div class="pg_landing__main">
            <div class="pg_landing__main-innerbox">
                <div class="${not empty error ? 'cp_login cp_login--step-1 cp_login--error' : 'cp_login cp_login--step-1'}">
                    <div class="cp_login__content">
                        <c:if test="${not empty error}">
                            <div class="row expanded align-center">
                                <div class="cp_login__message--error">
                                        ${error}
                                </div>
                            </div>
                        </c:if>
                        <div class="cp_login__title"><fmt:message key="index.signInToKpi"/></div>
                        <form id="mainLogin" class="cp_login__main" action="/mainLogin" method="post">
                            <% if (request.getParameter("redirect_uri") != null) { %>
                            <input type="hidden" id="redirect_uri" name="redirect_uri"
                                   value="<%=request.getParameter("redirect_uri")%>"/>
                            <%}%>

                            <div class="cp_login__field">
                                <input id="login" name="USER_NAME" value="" type="text" autofocus
                                       placeholder="<fmt:message key="index.loginOrEmail"/>"/>
                                <div class="cp_login__field-underline"></div>
                            </div>
                            <div class="cp_login__field cp_login__field--password">
                                <div class="password-wrapper">
                                    <input type="password" id="pass" name="USER_PASSWORD"
                                           placeholder="<fmt:message key="index.password"/>"/>
                                    <span class="toggle-password" onclick="togglePassword()">
            <i class="fa fa-eye" id="eyeIcon"></i>
        </span>
                                </div>

                                <div class="cp_login__field-underline"></div>
                            </div>
                            <div class="cp_login__form-item">
                                <c:if test="${captcha}">
                                    <div class="cp_login__field">
                                        <div class="g-recaptcha" data-sitekey="6Lc-FRMqAAAAAPzkqZwvH7ttAY0-2pW0_ekA0V5N"
                                             data-callback="checkRecaptcha"></div>
                                    </div>
                                </c:if>

                                <input type="submit" class="cp_login__submit elm_btn--blue"
                                       value="<fmt:message key="index.login"/>" onclick="validateAndSubmit(event)">
                                <c:if test="${forgotPasswordEnable}">
                                    <span class="cp_login__pass-forgot">
                                            <a href="/auth/forgotPassword.html">
                                                <fmt:message key="index.forgotPassword"/></a>
                                    </span>
                                </c:if>
                                <c:if test="${socialLoginEnable}">
                                    <ul class="cp_login__options">
                                        <li class="cp_login__option cp_login__option--google">
                                            <a class="fa fa-google-plus" href="#" onclick="google_login_new(event)"></a>
                                        </li>
                                        <li class="cp_login__option cp_login__option--facebook facebook">
                                            <a class="fa fa-facebook-f " data-tooltip="Coming Soon" href="#"
                                               onclick="fb_login(event)"></a>
                                        </li>

                                        <li class="cp_login__option cp_login__option--linkedin">
                                            <a class="fa fa-linkedin" href="#" onclick="linkedin_login(event)"></a>
                                        </li>
                                        <li class="cp_login__option cp_login__option--office365">
                                            <a class="ficon--office365" href="#" onclick="office_login(event)"></a>
                                        </li>
                                    </ul>
                                </c:if>
                            </div>
                        </form>
                    </div>
                    <c:if test="${signUpEnable}">
                        <div class="cp_login__footer">
                            <dl>
                                <dt>
                                    <fmt:message key="index.doNotHaveAnAccount"/>
                                </dt>
                                <dd>
                                                        <span class="pg_landing__start-btn">
                                                            <a class="btn-login-sign-up" href="/auth/signup.html">
                                                                <fmt:message key="index.signUpNow"/>
                                                            </a>
                                                        </span>
                                </dd>
                            </dl>
                        </div>
                    </c:if>
                </div>

            </div>
        </div>
    </div>
</div>
<form id="google-signin-form" action="/google-oauth2-verify" method="post">
    <input type="hidden" name="code" id="googlecode"/>
</form>


<div id="pg_landing-bg">

</div>


<script src="/mainStyles/new-ui/login/js/slick.min.js"></script>
<script src="/mainStyles/new-ui/login/js/main.js"></script>

<script type="text/javascript">
    function google_login(event) {
        if (event) {
            event.preventDefault();
        }
        window.location = '/check';

    }

    function google_login_new(event) {
        if (event) {
            event.preventDefault();
        }
        window.location = "https://accounts.google.com/o/oauth2/v2/auth?"
            + "redirect_uri=" + '${fullHost}' + "google-oauth2-verify"
            + "&response_type=code"
            + "&client_id=" + '${GOOGLE_CLIENT_ID}'
            + "&scope=https://www.googleapis.com/auth/userinfo.email+https://www.googleapis.com/auth/userinfo.profile+openid"
            + "&access_type=offline";
    }

    function togglePassword() {
        const passInput = document.getElementById('pass');
        const eyeIcon = document.getElementById('eyeIcon');
        if (passInput.type === 'password') {
            passInput.type = 'text';
            eyeIcon.classList.remove('fa-eye');
            eyeIcon.classList.add('fa-eye-slash');
        } else {
            passInput.type = 'password';
            eyeIcon.classList.remove('fa-eye-slash');
            eyeIcon.classList.add('fa-eye');
        }
    }

    function signInCallback(authResult) {
        if (authResult['code']) {
            // Send the code to the server
            $('#googlecode').val(authResult['code']);
            $('#google-signin-form').submit();
        } else {
            // There was an error.
            console.log("There was an error")
        }
    }


    function linkedin_login(event) {
        if (event) {
            event.preventDefault();
        }
        window.location = '/auth/sendtolinkedinauthorization';
    }

    function office_login(event) {
        if (event) {
            event.preventDefault();
        }
        window.location = '/auth/office365authorization';

    }

    function fb_login(event) {
        if (event) {
            event.preventDefault();
        }
        FB.login(function (response) {
            console.log(response);

            if (response.authResponse) {
                console.log('Welcome!  Fetching your information.... ');
                var uid = response.authResponse.userID;
                var accessToken = response.authResponse.accessToken;
                window.location = 'facebookLogin?access_token=' + accessToken + '&uid=' + uid;
            } else {
                console.log('User cancelled login or did not fully authorize.');

            }
        }, {
            scope: 'email, public_profile',
            return_scopes: false
        });
    }
</script>

<script type="text/javascript">
    (function (d, s, id) {
        var js, fjs = d.getElementsByTagName(s)[0];
        if (d.getElementById(id)) return;
        js = d.createElement(s);
        js.id = id;
        js.src = "//connect.facebook.net/en_US/all.js#xfbml=1&appId=<%=EdsContextParams.getFacebookAppID(request.getServerName())%>";
        fjs.parentNode.insertBefore(js, fjs);
    }(document, 'script', 'facebook-jssdk'));</script>
</body>
</html>

