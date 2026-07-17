<%--
  Created by IntelliJ IDEA.
  User: Bobur
  Date: 10/24/2022
  Time: 9:06 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.edatasite.workforce.gwt.core.client.ui.Constants" %>
<%@ page import="com.edatasite.workforce.appContext.SpringPropertiesUtil" %>
<%@ page import="com.edatasite.workforce.utils.EdsContextParams" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="title" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="language" value="${not empty param.language ? param.language : 'uz'}" scope="session"/>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="com.edatasite.workforce.gwt.core.client.localization.WfmStrings"/>
<c:choose>
    <c:when test="${language == 'ru'}">
        <c:set var="brainPageTitle" value="Вход или регистрация в BRAIN"/>
        <c:set var="brainLoginPlaceholder" value="Введите логин или email"/>
        <c:set var="brainPasswordPlaceholder" value="Введите пароль"/>
        <c:set var="brainLoginBtn" value="Войти"/>
    </c:when>
    <c:when test="${language == 'en'}">
        <c:set var="brainPageTitle" value="Sign in or Sign up to BRAIN"/>
        <c:set var="brainLoginPlaceholder" value="Enter your login or email"/>
        <c:set var="brainPasswordPlaceholder" value="Enter your password"/>
        <c:set var="brainLoginBtn" value="Sign in"/>
    </c:when>
    <c:otherwise>
        <c:set var="brainPageTitle" value="BRAIN tizimiga kirish yoki ro'yxatdan o'tish"/>
        <c:set var="brainLoginPlaceholder" value="Login yoki emailni kiriting"/>
        <c:set var="brainPasswordPlaceholder" value="Parolni kiriting"/>
        <c:set var="brainLoginBtn" value="Kirish"/>
    </c:otherwise>
</c:choose>

<!doctype html>
<html class="no-js" lang="${language}" dir="ltr">
<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${brainPageTitle}</title>
    <link rel="shortcut icon" href="/customisation/brain/favicon.png" type="image/x-icon"/>
    <link rel="stylesheet" href="/mainStyles/new-ui/login/font-awesome/css/font-awesome.css"/>
    <link rel="stylesheet"
          href="/mainStyles/new-ui/login/css/templateBrain.css?v=<%=SpringPropertiesUtil.getProperty("cssVersion")%>"/>

    <script src="/mainStyles/new-ui/login/js/jquery.min.js"></script>
    <script src="https://apis.google.com/js/client:platform.js?onload=start" async defer>
    </script>
    <script>
        function start() {
            gapi.load('auth2', function () {
                auth2 = gapi.auth2.init({
                    client_id: '${GOOGLE_CLIENT_ID}',
                    // Scopes to request in addition to 'profile' and 'email'
                    //scope: 'additional_scope'
                });
            });
        }
    </script>

</head>
<body>
<div class="container">
    <div class="login-section">
        <div class="login-container">
            <div class="locale-wrapper">
                <div></div>
                <div class="locale-container">
                    <div class="locale-button">
                        <button class="btn-locale"
                                onclick="toggleLocaleLangOptions()">${not empty param.language ? param.language : 'uz'}</button>
                    </div>
                    <div class="locale-langs" id="locale-lang-options">
                        <button class="locale-lang-option" onclick="updateLocale('uz')">uz</button>
                        <button class="locale-lang-option" onclick="updateLocale('ru')">ru</button>
                        <button class="locale-lang-option" onclick="updateLocale('en')">en</button>
                    </div>
                </div>
            </div>
            <figure class="login-header">
                <a href="#"><img src="/customisation/brain/logo.png" alt="Brain Logo"/></a>
                <figcaption><fmt:message key="brainLoginTitle"/></figcaption>
            </figure>

            <form id="mainLogin" class="login-form" action="/mainLogin" method="post">
                <% if (request.getParameter("redirect_uri") != null) { %>
                <input type="hidden" id="redirect_uri" name="redirect_uri"
                       value="<%=request.getParameter("redirect_uri")%>"/>
                <%}%>

                <div class="form-group">
                    <label for="login"><fmt:message key="brainUsername"/></label>
                    <input type="text" id="login" name="USER_NAME" class="form-control"
                           placeholder="${brainLoginPlaceholder}" autofocus>
                </div>

                <div class="form-group i--password password ${not empty error ? 'has-error' : ''}">
                    <label for="password">
                        <span><fmt:message key="brainPassword"/></span>
                        <a href="/auth/forgotPassword.html" class="password__forgot"><fmt:message
                                key="brainForgotPassword"/></a>
                    </label>
                    <div class="control-group">
                        <input type="password" name="USER_PASSWORD" id="password" class="form-control"
                               placeholder="${brainPasswordPlaceholder}">
                        <span class="password__view"
                              onclick="this.previousElementSibling.type = this.previousElementSibling.type === 'password' ? 'text' : 'password'; this.classList.toggle('active');">
                                <svg width="19" height="18" viewBox="0 0 19 18" fill="none" stroke="currentColor"
                                     xmlns="http://www.w3.org/2000/svg">
                                    <path fill-rule="evenodd" clip-rule="evenodd"
                                          d="M0.995444 0.96967C1.28834 0.676777 1.76321 0.676777 2.0561 0.96967L5.20291 4.11647C5.24999 4.15325 5.29327 4.19619 5.33149 4.24506L7.9954 6.90896C7.99544 6.90901 7.99549 6.90906 7.99554 6.9091C7.99558 6.90915 7.99563 6.9092 7.99568 6.90924L11.6581 10.5717C11.6584 10.5719 11.6586 10.5722 11.6589 10.5724C11.6591 10.5727 11.6594 10.5729 11.6596 10.5732L14.314 13.2276C14.3688 13.2688 14.4187 13.3182 14.4618 13.3754L17.5983 16.5119C17.8912 16.8048 17.8912 17.2796 17.5983 17.5725C17.3054 17.8654 16.8305 17.8654 16.5376 17.5725L13.7523 14.7872C12.5031 15.5325 11.0095 16.0653 9.29687 16.0653C6.86102 16.0653 4.87147 14.989 3.39165 13.7539C1.91359 12.5203 0.899003 11.0924 0.390602 10.2874C0.384058 10.277 0.377402 10.2665 0.370658 10.2559C0.268701 10.0952 0.146513 9.9027 0.0843593 9.63149C0.0343649 9.41334 0.0343816 9.12862 0.0844012 8.91048C0.146595 8.63924 0.269138 8.4462 0.37138 8.28514C0.37818 8.27443 0.38489 8.26386 0.391487 8.25342L0.835085 8.53364L0.391487 8.25342C0.91947 7.41762 2.00166 5.89661 3.59075 4.62564L0.995444 2.03033C0.702551 1.73744 0.702551 1.26256 0.995444 0.96967ZM4.65887 5.69376C3.18642 6.83336 2.16189 8.25948 1.65964 9.05453C1.62914 9.10281 1.60709 9.13777 1.58851 9.16835C1.57034 9.19825 1.56016 9.21639 1.55394 9.22836C1.55077 9.23446 1.54899 9.23826 1.5481 9.24028C1.54743 9.2478 1.54688 9.25853 1.54688 9.27107C1.54687 9.28366 1.54743 9.29442 1.5481 9.30195C1.54897 9.30391 1.55071 9.30764 1.55384 9.31366C1.55998 9.32548 1.57005 9.34345 1.58809 9.37316C1.60656 9.40356 1.62848 9.43832 1.65885 9.48641C2.12219 10.2201 3.03971 11.5064 4.3528 12.6023C5.66413 13.6967 7.32478 14.5653 9.29687 14.5653C10.5435 14.5653 11.6644 14.2188 12.6523 13.6872L11.0685 12.1033C10.555 12.425 9.94735 12.6115 9.29687 12.6115C7.45204 12.6115 5.95651 11.1159 5.95651 9.2711C5.95651 8.62062 6.14293 8.01297 6.46463 7.49951L4.65887 5.69376ZM7.57764 8.61253C7.49931 8.81693 7.45651 9.03887 7.45651 9.2711C7.45651 10.2875 8.28047 11.1115 9.29687 11.1115C9.5291 11.1115 9.75104 11.0687 9.95544 10.9903L7.57764 8.61253ZM9.29687 3.97691C8.96149 3.97691 8.63615 4.00193 8.3208 4.04856C7.91104 4.10914 7.52975 3.82608 7.46917 3.41632C7.40859 3.00656 7.69165 2.62527 8.10141 2.56469C8.48789 2.50755 8.88656 2.47691 9.29687 2.47691C11.7327 2.47691 13.7223 3.55323 15.2021 4.7883C16.6802 6.02191 17.6947 7.44981 18.2031 8.25482C18.2097 8.26518 18.2163 8.27566 18.2231 8.28629C18.325 8.44696 18.4472 8.63951 18.5094 8.91074C18.5594 9.1289 18.5594 9.41365 18.5093 9.6318C18.4471 9.90308 18.3244 10.0964 18.222 10.2577C18.2151 10.2685 18.2084 10.2791 18.2018 10.2896C17.929 10.7213 17.5149 11.3254 16.9661 11.9807C16.7002 12.2983 16.2272 12.3402 15.9096 12.0742C15.592 11.8083 15.5501 11.3353 15.8161 11.0177C16.3133 10.4239 16.6886 9.87619 16.9337 9.48836C16.9642 9.43997 16.9864 9.4049 17.005 9.37422C17.0232 9.34421 17.0335 9.32598 17.0398 9.31392C17.043 9.30777 17.0447 9.30394 17.0457 9.30189C17.0463 9.29437 17.0469 9.28366 17.0469 9.27114C17.0469 9.25855 17.0463 9.24779 17.0456 9.24026C17.0448 9.23829 17.043 9.23456 17.0399 9.22855C17.0338 9.21673 17.0237 9.19876 17.0057 9.16904C16.9872 9.13864 16.9653 9.10388 16.9349 9.05578C16.4716 8.32213 15.554 7.03583 14.2409 5.93991C12.9296 4.84546 11.269 3.97691 9.29687 3.97691ZM17.0463 9.2419C17.0461 9.24128 17.0461 9.24109 17.0463 9.24173C17.0464 9.24203 17.0464 9.24207 17.0463 9.2419Z"
                                          fill="#838383"/>
                                </svg>
                        </span>
                    </div>
                    <c:if test="${not empty error}">
                        <div class="error-message">
                                ${error}
                        </div>
                    </c:if>
                </div>

                <div class="form-group remember-me">
                    <input type="checkbox" id="remember">
                    <label for="remember"><fmt:message key="brainRememberMe"/></label>
                </div>

                <button type="submit" class="btn btn-primary">${brainLoginBtn}</button>
                <div class="signup-link">
                    <fmt:message key="brainNoAccount"/> <a href="/auth/signup.html"><fmt:message key="brainSignUp"/></a>
                </div>

                <div class="locale-wrapper-m">
                    <div class="locale-container-m">
                        <select class="locale-langs-m" id="locale-lang-options-mobile"
                                onchange="updateLocale(this.value)">
                            <option class="locale-lang-option-m"
                                    value="uz" ${language == 'uz' ? 'selected="selected"' : ''}>O'zbekcha
                            </option>
                            <option class="locale-lang-option-m"
                                    value="ru" ${language == 'ru' ? 'selected="selected"' : ''}>Русский
                            </option>
                            <option class="locale-lang-option-m"
                                    value="en" ${language == 'en' ? 'selected="selected"' : ''}>English
                            </option>
                        </select>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <div class="hero-section">
        <div class="hero-content">
        </div>
    </div>
</div>

<form id="google-signin-form" action="/google-oauth2-verify" method="post">
    <input type="hidden" name="code" id="googlecode"/>
</form>

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
    }(document, 'script', 'facebook-jssdk'));
</script>

<script>
    function toggleLocaleLangOptions() {
        const localLangOptions = document.getElementById("locale-lang-options");
        localLangOptions.classList.toggle('active');
    }

    function updateLocale(lang) {
        document.documentElement.lang = lang;
        window.location.search = '?language=' + lang;
    }
</script>
</body>
</html>
