<%@ page import="com.edatasite.workforce.utils.EdsContextParams" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<link rel="stylesheet" href="/customisation/preprod.kpi.com/style.css"/>
<SCRIPT type=text/javascript src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.4/jquery.min.js"></SCRIPT>
<%
    //Recaptcha script
    Boolean enableCaptcha = request.getAttribute("captcha") != null ? ((Boolean) request.getAttribute("captcha")) : false;
    String captchaTheme = request.getAttribute("captchaTheme") != null ? ((String) request.getAttribute("captchaTheme")) : "white";
    if (enableCaptcha) {
%>
<script type="text/javascript" src="https://www.google.com/recaptcha/api/js/recaptcha_ajax.js"></script>
<% } %>

<div id="contentPlace" class="clear-block">

    <!-- Start MAIN -->
    <div id="main">

        <!-- START LOGIN FORM -->

        <section>
            <form action="/mainLogin" method="post">

                <h1 class="title"><fmt:message key="index.login"/></h1>
                <input id="BEST_SIGNIN" type="hidden" name="BEST_SIGNIN" value="BEST_SIGNIN">
                <p class="c req">${error}</p>

                <div class="loginBox">
                    <div class="boxIn">
                        <label for="login">
                            <input id="login" type="text" name="USER_NAME" value=""
                                   placeholder="<fmt:message key="index.userName"/>" class="txt" >
                        </label>

                        <label for="pass">
                            <input id="pass" type="password" name="USER_PASSWORD" value="" placeholder="<fmt:message key="index.password"/>" class="txt" >
                        </label>
                        <div id="recaptcha"></div>

                        <input style="text-indent: 1px;" type="submit" value="<fmt:message key="index.login"/>" class="btnLogIn btn">

                        <div class="alterLogin"> <fmt:message key="index.or"/> </div>

                        <ul class="useSocialBookMarking">
                            <li><a id = "liveid" href="/liveidauth" class="mini-icons mini-livemessager" style="display:none">Live Messanger</a></li>
                            <li><a href="/check" class="mini-icons mini-google">Google</a></li>
                            <li><a href="/check?ID_PROVIDER=https://me.yahoo.com" class="mini-icons mini-yahoo">Yahoo</a></li>
                            <li><a href="/enterGoogleDomain.html" class="mini-icons mini-google-apps">Google Apps</a></li>
                        </ul>

                        <div class="c">
                            <a href="/forgot/forgotPassword.html"> <fmt:message key="index.forgotPassword"/>  </a>
                            |
                            <a href="/signup/freeSignup.html"><fmt:message key="index.signUp"/> </a>
                        </div>
                    </div>
                </div>
                <div class="clearBox"></div>
            </form>
        </section>
        <!-- END LOGIN FORM -->

    </div>
    <!--End #main -->


</div>


<% if (enableCaptcha != null && enableCaptcha) {%>
    <script type="text/javascript">
        Recaptcha.create("<%=EdsContextParams.getRecaptchaPublicKey()%>", "recaptcha", {
            theme:"<%=captchaTheme%>"});
    </script>
<% } %>

