<%--
  Created by IntelliJ IDEA.
  User: Bobur
  Date: 10/24/2022
  Time: 9:06 PM
  To change this template use File | Settings | File Templates.
--%>
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
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign in or Sign up to kpi.com</title>
    <link rel="shortcut icon" href="/customisation/praaktisgo/images/praaktisgofavicon.png" type="image/x-icon"/>
    <link rel="stylesheet" href="/mainStyles/new-ui/login/font-awesome/css/font-awesome.css"/>
    <link rel="stylesheet" href="/mainStyles/new-ui/login/slick/slick.css"/>
    <link rel="stylesheet"
          href="/mainStyles/new-ui/login/css/main.css?v=<%=SpringPropertiesUtil.getProperty("cssVersion")%>"/>
    <link rel="stylesheet" href="../WebContent/mainStyles/new-ui/login/css/templateBrain?v=<%=SpringPropertiesUtil.getProperty("cssVersion")%>"/>

    <script src="/mainStyles/new-ui/login/js/jquery.min.js"></script>
    <script src="https://apis.google.com/js/client :platform.js?onload=start" async defer></script>
    <script>
        function start() {
            gapi.load('auth2', function () {
                auth2 = gapi.auth2.init({
                    client_id: '${GOOGLE_CLIENT_ID}',
                });
            });
        }
    </script>
</head>
<body style="overflow: hidden; display: flex; align-items: center; justify-content: center;">
<div class="container">
    <div class="login-section">
        <div class="login-container">
            <figure class="login-header">
                <a href="#"><img src="../WebContent/customisation/brain/logo.png" alt="Logo" /></a>
                <figcaption>Manage multiple businesses from a single platform.</figcaption>
            </figure>

            <!-- Show error if exists -->
            <c:if test="${not empty error}">
                <div class="row expanded align-center">
                    <div class="error-message">${error}</div>
                </div>
            </c:if>

            <form id="mainLogin" class="login-form" action="/mainLogin" method="post">
                <% if (request.getParameter("redirect_uri") != null) { %>
                <input type="hidden" id="redirect_uri" name="redirect_uri"
                       value="<%=request.getParameter("redirect_uri")%>"/>
                <%}%>

                <h3 class="show--mobile">Enter the system with e-mail</h3>

                <div class="form-group i--email show--desktop">
                    <label for="login">Login or email</label>
                    <div class="control-group">
                        <input type="text" id="login" name="USER_NAME" class="form-control" placeholder="Enter your login">
                    </div>
                </div>

                <div class="form-group i--email show--mobile">
                    <label for="login">E-mail</label>
                    <div class="control-group">
                        <input type="email" id="login" name="USER_NAME" class="form-control" placeholder="Enter your email">
                    </div>
                </div>

                <div class="form-group i--password password">
                    <label for="pass">
                        <span>Password</span>
                        <a href="#" class="password__forgot">forgot password</a>
                    </label>
                    <div class="control-group">
                        <input type="password" id="pass" name="USER_PASSWORD" class="form-control" placeholder="Password">
                        <span class="password__view"
                              onclick="this.previousElementSibling.type = this.previousElementSibling.type === 'password' ? 'text' : 'password'; this.classList.toggle('active');">
                            <!-- Eye SVG Icon -->
                            <svg width="19" height="18" viewBox="0 0 19 18" fill="none" stroke="currentColor" xmlns="http://www.w3.org/2000/svg">
                                <path d="M0.995444 0.96967C1.28834 0.676777 1.76321 0.676777 2.0561 0.96967L17.5983 16.5119C17.8912 16.8048 17.8912 17.2796 17.5983 17.5725C17.3054 17.8654 16.8305 17.8654 16.5376 17.5725L0.995444 0.96967ZM4.65887 5.69376C3.18642 6.83336 2.16189 8.25948 1.65964 9.05453C1.62914 9.10281 1.60709 9.13777 1.58851 9.16835C1.57034 9.19825 1.56016 9.21639 1.55394 9.22836C1.5481 9.24028C1.54688 9.25853 1.54688 9.27107C1.54687 9.28366C1.54743 9.29442C1.5481 9.30195C1.55071 9.30764C1.55384 9.31366C1.55998 9.32548C1.57005 9.34345C1.58809 9.37316C1.60656 9.40356C1.62848 9.43832C1.65885 9.48641C2.12219 10.2201 3.03971 11.5064 4.3528 12.6023C5.66413 13.6967 7.32478 14.5653 9.29687 14.5653C10.5435 14.5653 11.6644 14.2188 12.6523 13.6872L11.0685 12.1033C10.555 12.425 9.94735 12.6115 9.29687 12.6115C7.45204 12.6115 5.95651 11.1159 5.95651 9.2711C5.95651 8.62062 6.14293 8.01297 6.46463 7.49951L4.65887 5.69376ZM7.57764 8.61253C7.49931 8.81693 7.45651 9.03887 7.45651 9.2711C7.45651 10.2875 8.28047 11.1115 9.29687 11.1115C9.5291 11.1115 9.75104 11.0687 9.95544 10.9903L7.57764 8.61253ZM9.29687 3.97691C8.96149 3.97691 8.63615 4.00193 8.3208 4.04856C7.91104 4.10914 7.52975 3.82608 7.46917 3.41632C7.40859 3.00656 7.69165 2.62527 8.10141 2.56469C8.48789 2.50755 8.88656 2.47691 9.29687 2.47691C11.7327 2.47691 13.7223 3.55323 15.2021 4.7883C16.6802 6.02191 17.6947 7.44981 18.2031 8.25482C18.2097 8.26518 18.2163 8.27566 18.2231 8.28629C18.325 8.44696 18.4472 8.63951 18.5094 8.91074C18.5594 9.1289 18.5594 9.41365 18.5093 9.6318C18.4471 9.90308 18.3244 10.0964 18.222 10.2577C18.2151 10.2685 18.2084 10.2791 18.2018 10.2896C17.929 10.7213 17.5149 11.3254 16.9661 11.9807C16.7002 12.2983 16.2272 12.3402 15.9096 12.0742C15.592 11.8083 15.5501 11.3353 15.8161 11.0177C16.3133 10.4239 16.6886 9.87619 16.9337 9.48836C16.9642 9.43997 16.9864 9.4049 17.005 9.37422C17.0232 9.34421 17.0335 9.32598 17.0398 9.31392C17.043 9.30777 17.0447 9.30394 17.0457 9.30189C17.0463 9.29437 17.0469 9.28366 17.0469 9.27114C17.0469 9.25855 17.0463 9.24779 17.0456 9.24026C17.0448 9.23829 17.043 9.23456 17.0399 9.22855C17.0338 9.21673 17.0237 9.19876 17.0057 9.16904C16.9872 9.13864 16.9653 9.10388 16.9349 9.05578C16.4716 8.32213 15.554 7.03583 14.2409 5.93991C12.9296 4.84546 11.269 3.97691 9.29687 3.97691ZM17.0463 9.2419C17.0461 9.24128 17.0461 9.24109 17.0463 9.24173C17.0464 9.24203 17.0464 9.24207 17.0463 9.2419Z"
                                      fill="#838383"/>
                            </svg>
                        </span>
                    </div>
                </div>

                <div class="form-group remember-me">
                    <input type="checkbox" id="remember">
                    <label for="remember">Remember me</label>
                </div>

                <button type="submit" class="btn btn-primary">Login</button>

                <div class="divider">
                    <span>or</span>
                </div>

                <div class="oauth-buttons">
                    <button type="button" class="btn btn-social google" onclick="google_login_new(event)">
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M21.8055 10.0415H21V10H12V14H17.6515C16.827 16.3285 14.6115 18 12 18C8.6865 18 6 15.3135 6 12C6 8.6865 8.6865 6 12 6C13.5295 6 14.921 6.577 15.9805 7.5195L18.809 4.691C17.023 3.0265 14.634 2 12 2C6.4775 2 2 6.4775 2 12C2 17.5225 6.4775 22 12 22C17.5225 22 22 17.5225 22 12C22 11.3295 21.931 10.675 21.8055 10.0415Z" fill="#FFC107"/>
                            <path d="M3.15308 7.3455L6.43858 9.755C7.32758 7.554 9.48058 6 12.0001 6C13.5296 6 14.9211 6.577 15.9806 7.5195L18.8091 4.691C17.0231 3.0265 14.6341 2 12.0001 2C8.15908 2 4.82808 4.1685 3.15308 7.3455Z" fill="#FF3D00"/>
                            <path d="M11.9999 22.0001C14.5829 22.0001 16.9299 21.0116 18.7044 19.4041L15.6094 16.7851C14.6054 17.5456 13.3574 18.0001 11.9999 18.0001C9.39891 18.0001 7.19041 16.3416 6.35841 14.0271L3.09741 16.5396C4.75241 19.7781 8.11341 22.0001 11.9999 22.0001Z" fill="#005EC4"/>
                            <path d="M21.8055 10.0415H21V10H12V14H17.6515C17.2555 15.1185 16.536 16.083 15.608 16.7855L15.6095 16.7845L18.7045 19.4035C18.4855 19.6025 22 17 22 12C22 11.3295 21.931 10.675 21.8055 10.0415Z" fill="#1976D2"/>
                        </svg>
                        Sign in with Google
                    </button>

                    <button type="button" class="btn btn-social apple" onclick="fb_login(event)">
                        <svg width="21" height="24" viewBox="0 0 21 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M14.4863 0.610985C13.125 0.70286 11.6682 1.51286 10.7663 2.60599C9.97691 3.56786 9.31503 4.97974 9.56628 6.44599C9.34128 6.37661 9.13691 6.36911 8.89128 6.28099C8.22191 6.04286 7.45691 5.78599 6.50628 5.78599C4.61816 5.78599 2.68503 6.90911 1.46628 8.78598C-0.307467 11.5122 0.0487823 16.3104 2.74128 20.411C3.21566 21.131 3.76878 21.9166 4.45128 22.541C5.13378 23.1654 5.95878 23.6416 6.92628 23.651C7.75316 23.6604 8.32878 23.3847 8.84628 23.156C9.36378 22.9272 9.84566 22.7247 10.7513 22.721C10.7569 22.721 10.7607 22.721 10.7663 22.721C11.6682 22.7135 12.135 22.9122 12.6413 23.141C13.1475 23.3697 13.7175 23.6585 14.5463 23.651C15.5325 23.6435 16.3707 23.1035 17.0663 22.436C17.7619 21.7685 18.3338 20.9491 18.8063 20.231C19.4832 19.1997 19.755 18.6372 20.2763 17.486C20.3307 17.366 20.3325 17.2272 20.28 17.1054C20.2294 16.9835 20.13 16.8879 20.0063 16.841C18.3 16.196 17.3269 14.6866 17.1863 13.091C17.0457 11.4954 17.7207 9.85286 19.4213 8.92099C19.5469 8.85349 19.635 8.73536 19.665 8.59661C19.6932 8.45786 19.6594 8.31161 19.5713 8.20099C18.3507 6.68411 16.6332 5.78599 14.9513 5.78599C13.8788 5.78599 13.0913 6.03911 12.4463 6.28099C12.3394 6.32224 12.2607 6.31849 12.1613 6.35599C12.8157 6.01099 13.3932 5.53286 13.8263 4.97599C14.6138 3.96536 15.2213 2.52724 14.9963 1.01599C14.9588 0.768485 14.7357 0.592235 14.4863 0.610985ZM13.9763 1.75099C13.9444 2.72036 13.6125 3.66911 13.0613 4.37599C12.4838 5.11849 11.5125 5.63224 10.5713 5.78599C10.5882 4.85411 10.9557 3.88849 11.5163 3.20599C12.105 2.49349 13.08 1.97974 13.9763 1.75099ZM6.50628 6.74599C6.92628 6.74599 7.90128 6.94661 8.56128 7.18099C9.22128 7.41536 9.91691 7.69099 10.7363 7.69099C11.5369 7.69099 12.1707 7.41349 12.7913 7.18099C13.4119 6.94849 14.0307 6.74599 14.9513 6.74599C16.1532 6.74599 17.4525 7.37036 18.4763 8.45599C16.8375 9.61286 16.0707 11.4166 16.2263 13.166C16.3819 14.9341 17.4469 16.6122 19.1963 17.486C19.1938 17.4914 19.1913 17.4969 19.1888 17.5023C18.8142 18.3182 18.5645 18.8622 18.0113 19.706C17.5519 20.4054 17.0063 21.1704 16.4063 21.746C15.8063 22.3216 15.1707 22.6854 14.5313 22.691C13.9144 22.6966 13.5638 22.5129 13.0313 22.271C12.4988 22.0291 11.8107 21.7516 10.7513 21.761C9.69378 21.7666 8.99628 22.0329 8.45628 22.271C7.91628 22.5091 7.56191 22.6966 6.94128 22.691C6.28503 22.6854 5.67566 22.3666 5.09628 21.836C4.51691 21.3054 3.99378 20.5816 3.53628 19.886C1.01628 16.046 0.813782 11.5347 2.26128 9.31099C3.32253 7.67786 6.08628 6.74599 6.50628 6.74599Z" fill="black"/>
                            <path d="M6.50628 6.74599C6.92628 6.74599 7.90128 6.94661 8.56128 7.18099C9.22128 7.41536 9.91691 7.69099 10.7363 7.69099C11.5369 7.69099 12.1707 7.41349 12.7913 7.18099C13.4119 6.94849 14.0307 6.74599 14.9513 6.74599C16.1532 6.74599 17.4525 7.37036 18.4763 8.45599C16.8375 9.61286 16.0707 11.4166 16.2263 13.166C16.3819 14.9341 17.4469 16.6122 19.1963 17.486L19.1888 17.5023C18.8142 18.3182 18.5645 18.8622 18.0113 19.706C17.5519 20.4054 17.0063 21.1704 16.4063 21.746C15.8063 22.3216 15.1707 22.6854 14.5313 22.691C13.9144 22.6966 13.5638 22.5129 13.0313 22.271C12.4988 22.0291 11.8107 21.7516 10.7513 21.761C9.69378 21.7666 8.99628 22.0329 8.45628 22.271C7.91628 22.5091 7.56191 22.6966 6.94128 22.691C6.28503 22.6854 5.67566 22.3666 5.09628 21.836C4.51691 21.3054 3.99378 20.5816 3.53628 19.886C1.01628 16.046 0.813782 11.5347 2.26128 9.31099C3.32253 7.67786 6.08628 6.74599 6.50628 6.74599Z" fill="black"/>
                            <path d="M13.9763 1.75099C13.9444 2.72036 13.6125 3.66911 13.0613 4.37599C12.4838 5.11849 11.5125 5.63224 10.5713 5.78599C10.5882 4.85411 10.9557 3.88849 11.5163 3.20599C12.105 2.49349 13.08 1.97974 13.9763 1.75099Z" fill="black"/>
                        </svg>
                        Sign in with Apple
                    </button>
                </div>

                <div class="signup-link">
                    Don't have an account? <a href="/auth/signup.html">Sign Up</a>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Scripts -->
<script src="/mainStyles/new-ui/login/js/slick.min.js"></script>
<script src="/mainStyles/new-ui/login/js/main.js"></script>
<script src="//connect.facebook.net/en_US/all.js#xfbml=1&appId=<%=EdsContextParams.getFacebookAppID(request.getServerName())%>" async defer></script>

<form id="google-signin-form" action="/google-oauth2-verify" method="post">
    <input type="hidden" name="code" id="googlecode"/>
</form>

<script>
    var auth2;

    function google_login_new(event) {
        if (event) event.preventDefault();
        auth2.grantOfflineAccess().then(signInCallback);
    }

    function signInCallback(authResult) {
        if (authResult['code']) {
            $('#googlecode').val(authResult['code']);
            $('#google-signin-form').submit();
        } else {
            console.log("There was an error");
        }
    }

    function fb_login(event) {
        if (event) event.preventDefault();
        FB.login(function (response) {
            if (response.authResponse) {
                var uid = response.authResponse.userID;
                var accessToken = response.authResponse.accessToken;
                window.location = 'facebookLogin?access_token=' + accessToken + '&uid=' + uid;
            } else {
                console.log('User cancelled login or did not fully authorize.');
            }
        }, {scope: 'email, public_profile'});
    }

    // Responsive visibility script from HTML
    class ResponsiveVisibility {
        constructor() {
            this.mobileBreakpoint = 768;
            this.currentState = this.getCurrentState();
            this.init();
        }
        getCurrentState() {
            return window.innerWidth < this.mobileBreakpoint ? 'mobile' : 'desktop';
        }
        init() {
            this.setupEventListeners();
            this.updateVisibility();
        }
        setupEventListeners() {
            window.addEventListener('resize', () => {
                const newState = this.getCurrentState();
                if (newState !== this.currentState) {
                    this.currentState = newState;
                    this.updateVisibility();
                }
            });
        }
        updateVisibility() {
            const isMobile = this.currentState === 'mobile';
            document.querySelectorAll('.show--mobile').forEach(el => el.style.display = isMobile ? '' : 'none');
            document.querySelectorAll('.show--desktop').forEach(el => el.style.display = isMobile ? 'none' : '');
            ['inline', 'inline-block', 'flex', 'grid'].forEach(type => {
                document.querySelectorAll(`.show--mobile--${type}`).forEach(el => el.style.display = isMobile ? type : 'none');
                document.querySelectorAll(`.show--desktop--${type}`).forEach(el => el.style.display = isMobile ? 'none' : type);
            });
        }
    }

    document.addEventListener('DOMContentLoaded', () => {
        new ResponsiveVisibility();
    });
</script>

</body>
</html>