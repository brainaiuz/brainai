<%@ page import="com.edatasite.workforce.appContext.SpringPropertiesUtil" %>
<%@ page import="com.edatasite.workforce.utils.EdsContextParams" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="title" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!doctype html>
<html class="no-js" lang="en" dir="ltr">
<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign in or Sign up to kpi.com</title>
    <link rel="shortcut icon" href="/customisation/citynet/images/favicon.jpg" type="image/x-icon"/>
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

</head>
<body>
<div class="pg_landing">
    <div class="pg_landing__container">
        <figure class="pg_landing__header" style="margin-top: 50px;">
            <div class="pg_landing__logo">
                <a href="#">
                    <img src="/customisation/citynet/images/logo.png" alt="logo" style="width: 350px !important;"/>
                </a>
            </div>
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
                        <div class="cp_login__title">Авторизуйтесь</div>
                        <form id="mainLogin" class="cp_login__main" action="/mainLogin" method="post">
                            <% if (request.getParameter("redirect_uri") != null) { %>
                            <input type="hidden" id="redirect_uri" name="redirect_uri"
                                   value="<%=request.getParameter("redirect_uri")%>"/>
                            <%}%>

                            <div class="cp_login__field">
                                <input id="login" name="USER_NAME" value="" type="text" autofocus
                                       placeholder="Логин"/>
                                <div class="cp_login__field-underline"></div>
                            </div>
                            <div class="cp_login__field cp_login__field--password">
                                <input type="password" id="pass" name="USER_PASSWORD"
                                       placeholder="Пароль">
                                <div class="cp_login__field-underline"></div>
                            </div>
                            <div class="cp_login__form-item">
                                <input type="submit" class="cp_login__submit elm_btn--blue"
                                       value="Продолжить">
                                <span class="cp_login__pass-forgot">
                                    </span>
                            </div>
                        </form>
                    </div>
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
        auth2.grantOfflineAccess().then(signInCallback);

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

