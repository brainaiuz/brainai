<%@ page import="com.edatasite.workforce.appContext.SpringPropertiesUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!doctype html>
<html class="no-js" lang="">
<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Welcome to
        <c:choose>
            <c:when test="${fn:containsIgnoreCase(hostName,'1erp.sa')}">
                1erp.sa!
            </c:when>
            <c:otherwise>
                kpi.com!
            </c:otherwise>
        </c:choose>
    </title>
    <link rel="shortcut icon" href="/mainStyles/new-ui/login/img/favicon.ico?v=2" type="image/x-icon"/>
    <link rel="stylesheet" href="/mainStyles/new-ui/login/font-awesome/css/font-awesome.css"/>
    <link rel="stylesheet" href="/mainStyles/new-ui/login/slick/slick.css"/>
    <link rel="stylesheet"
          href="/mainStyles/new-ui/css/pass-validation.css?v=<%=SpringPropertiesUtil.getProperty("cssVersion")%>">
    <link rel="stylesheet" href="/mainStyles/new-ui/css/materialize.css">

    <script>
        var adPassEnabled = <%= request.getParameter("adPassEnabled") %>;

        function checkForPassEnter(value) {
            document.getElementById('error').style.display = 'none';
            if (value.length === 0) {
                document.getElementById('step1').innerHTML = '';
                document.getElementById('passStrength').className = 'pass-validation';
                document.getElementById('step1').className = '';
                document.getElementById('passWeakHint').style.display = 'none';
            }
            if (value.length > 0) {
                if (adPassEnabled) {
                    code2word(passStrength(value, 15, 20, 1, 1, 1, 1, 11))
                } else {
                    code2word(passStrength(value, 8, 12, 1, 1, 1, 1, 4))
                }
            }
        }

        function checkForPassMatch(value) {
            passwordsMatch(document.getElementById('pass1').value, value) ? document.getElementById('error2').style.display = 'none' : "";
        }

        function submitnewPassword() {
            var error = 0;

            var passwordStrength = document.getElementById('step1').textContent;

            if (passwordStrength === '<fmt:message key="changepassword.Weak"/>') {
                document.getElementById('error').style.display = '';
                document.getElementById('error').style.color = 'red';
                document.getElementById('error').innerHTML = "Password is weak or too short (Min. " + (adPassEnabled ? 15 : 8) + " characters and must contain at least one uppercase, " +
                    "lowercase letters, numbers, and special symbols such as $ % & ( @ # § = ')' , : ; - _ + ^ )";
                error++;
            }

            var passwordMatches = passwordsMatch(document.getElementById('pass1').value, document.getElementById('pass2').value);
            if (!passwordMatches) {
                document.getElementById('error').innerHTML = "Make sure that the password matches!";
                document.getElementById('error2').style.display = '';
                error++;
            }

            if (error === 0) {
                document.getElementById('formSubmit').submit();
            }
        }
    </script>

</head>
<body class="signUpStep signUpStep--2">
<script type="text/javascript" src="/mainStyles/js/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="/mainStyles/new-ui/js/materialize-0.97.5.min.js"></script>

<div class="modal-holder active">
    <div class="modal modal--md sign-up-step sign-up-step--2"> <%--this is the last, correct version--%>
        <div class="modal-wrapper">
            <div class="modal-header">
                <div class="hgroup">
                    <h1 class="fs-4">
                        <strong><fmt:message key="changepassword.SetYourPassword"/></strong>
                    </h1>
                    <h2 class="sub-title fs-3">
                        <fmt:message key="changepassword.PleaseEnterAPasswordForYourAccount"/>
                    </h2>
                </div>
            </div>
            <form action='<c:url value="/auth/changePassword.html"/>' method="post" id="formSubmit">
                <input type="hidden" name="checkUser" value="<c:out value="${checkUser}"/>">

                <div class="modal-content">
                    <div class="sign-up-step__password">
                        <div class="form-group">
                            <div class="form-group__label">
                                <fmt:message key="changepassword.Password"/>
                            </div>
                            <div class="form-group__content" style="position: relative;">
                                <input placeholder="Password"
                                       type="password"
                                       class="form-control"
                                       name="password"
                                       id='pass1'
                                       onkeyup="checkForPassEnter(this.value)"
                                       style="padding-right: 40px;" />

                                <i class="fa fa-eye toggle-password"
                                   toggle="#pass1"
                                   style="position:absolute;margin-right: 70px; right: 10px; top: 8px; cursor: pointer;"></i>

                                <div id="passStrength" class="pass-validation pass-validation--state-1">
                                    <div class="pass-validation__view">
                                        <span class="pass-validation__view__step-1"></span>
                                        <span class="pass-validation__view__step-2"></span>
                                        <span class="pass-validation__view__step-3"></span>
                                    </div>
                                    <div class="pass-validation__text">
                                        <span id='step1'></span>
                                    </div>
                                </div>
                                <div id="passWeakHint"
                                     style="display:none; color:#e74c3c; font-size:0.82rem; margin-top:6px; line-height:1.4;"></div>
                            </div>

                            <span id="error" style="color:red;">
                            &nbsp; <c:out value="${message}"/>
                            </span>
                        </div>

                        <div class="form-group">
                            <div class="form-group__label">
                                <fmt:message key="changepassword.ConfirmYourPassword"/>
                            </div>
                            <div class="form-group__content" style="position: relative;">
                                <input type="password" class="form-control"
                                       placeholder="Confirm Your Password"
                                       name="cPassword"
                                       id="pass2"
                                       onkeyup="checkForPassMatch(this.value)" />
                                <i class="fa fa-eye toggle-password"
                                   toggle="#pass2"
                                   style="position:absolute; right: 10px; top: 10px; cursor: pointer;"></i>
                            </div>

                            <span id="error2" style="display:none; color:red;">
                            &nbsp;<fmt:message key="changepassword.match"/>
                            </span>
                        </div>
                        <input type='hidden' id='strength' name='strength' value="">
                        <button type="button" class="btn btn--primary" onclick="submitnewPassword()"><fmt:message
                                key="changepassword.Continue"/></button>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <div class="lean-overlay file--changePassword" id="materialize-lean-overlay-1"
         style="z-index: 1002; display: block; opacity: 0.5;"></div>
</div>
<div style="display: none">
    <span>Test block</span>
</div>

<script>
    jQuery(document).ready(function () {
        $('select').material_select();
    });
</script>

<script type=text/javascript src="/passwordStrength.js"></script>
<script type="text/javascript">
    function code2word(code) {
        document.querySelector('#strength').value = code;
        var hintEl = document.getElementById('passWeakHint');
        var minLen = adPassEnabled ? 15 : 8;
        if (code === 'WEAK') {
            document.getElementById('step1').innerHTML = 'Weak';
            hintEl.innerHTML = 'Password is too weak. Min. ' + minLen + ' characters and must contain at least one uppercase letter, lowercase letter, number, and special symbol ($ % & ( @ # § = \' ) , : ; - _ + ^ ).';
            hintEl.style.display = '';
        }
        if (code === 'MEDIUM') {
            document.getElementById('step1').innerHTML = 'Medium';
            hintEl.style.display = 'none';
        }
        if (code === 'STRONG') {
            document.getElementById('step1').innerHTML = 'Strong';
            hintEl.style.display = 'none';
        }
        document.getElementById('passStrength').className = 'pass-validation';
        document.getElementById('step1').className = '';
        switch (code) {
            case 'WEAK':
                document.getElementById('passStrength').classList.add('pass-validation--state-1');
                document.getElementById('step1').classList.add('pass-validation__text__step-1');
                break;
            case 'MEDIUM':
                document.getElementById('passStrength').classList.add('pass-validation--state-2');
                document.getElementById('step1').classList.add('pass-validation__text__step-2');
                break;
            case 'STRONG':
                document.getElementById('passStrength').classList.add('pass-validation--state-3');
                document.getElementById('step1').classList.add('pass-validation__text__step-3');
                break;
            default:
        }
    }
    $(document).ready(function () {
        $(".toggle-password").click(function () {
            var input = $($(this).attr("toggle"));
            var icon = $(this);
            if (input.attr("type") === "password") {
                input.attr("type", "text");
                icon.removeClass("fa-eye").addClass("fa-eye-slash");
            } else {
                input.attr("type", "password");
                icon.removeClass("fa-eye-slash").addClass("fa-eye");
            }
        });
    });
</script>
</body>
</html>

