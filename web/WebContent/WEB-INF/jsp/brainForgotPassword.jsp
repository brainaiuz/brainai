<%@ page import="com.edatasite.workforce.appContext.SpringPropertiesUtil" %>
<%@ page import="com.edatasite.workforce.utils.EdsContextParams" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="title" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<c:set var="language"
       value="${not empty param.language ? param.language : (not empty sessionScope.language ? sessionScope.language : 'uz')}"
       scope="session"/>
<fmt:setLocale value="${language}" />

<c:choose>
    <c:when test="${language == 'ru'}">
        <c:set var="forgotTitle" value="Забыли пароль" />
        <c:set var="forgotDesc" value="Введите email для подтверждения, мы отправим 4-значный код." />
        <c:set var="emailLabel" value="Эл. почта" />
        <c:set var="emailPlaceholder" value="Введите email" />
        <c:set var="continueBtn" value="Продолжить" />

        <c:set var="emailRequiredMsg" value="Email обязателен" />
        <c:set var="emailInvalidMsg" value="Введите корректный email адрес" />
        <c:set var="emailNotFoundMsg" value="Электронная почта не найдена!" />
    </c:when>

    <c:when test="${language == 'en'}">
        <c:set var="forgotTitle" value="Forgot password" />
        <c:set var="forgotDesc" value="Enter your email for verification, we will send a 4-digit code." />
        <c:set var="emailLabel" value="Email address" />
        <c:set var="emailPlaceholder" value="Enter your email" />
        <c:set var="continueBtn" value="Continue" />

        <c:set var="emailRequiredMsg" value="Email is required" />
        <c:set var="emailInvalidMsg" value="Enter a valid email address" />
        <c:set var="emailNotFoundMsg" value="Email address not found!" />
    </c:when>

    <c:otherwise>
        <c:set var="forgotTitle" value="Parolni unutdingizmi" />
        <c:set var="forgotDesc" value="Tasdiqlash uchun emailingizni kiriting, biz sizga 4 xonali kod yuboramiz." />
        <c:set var="emailLabel" value="Email manzil" />
        <c:set var="emailPlaceholder" value="Emailingizni kiriting" />
        <c:set var="continueBtn" value="Davom etish" />

        <c:set var="emailRequiredMsg" value="Email majburiy" />
        <c:set var="emailInvalidMsg" value="To'g'ri email kiriting" />
        <c:set var="emailNotFoundMsg" value="Email manzili topilmadi!" />
    </c:otherwise>
</c:choose>

<!doctype html>
<html class="no-js" lang="${language}" dir="ltr">
<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BRAIN - ${forgotTitle}</title>
    <link rel="shortcut icon" href="/customisation/brain/favicon.png" type="image/x-icon"/>
    <link rel="stylesheet" href="/mainStyles/new-ui/login/font-awesome/css/font-awesome.css"/>
    <link rel="stylesheet"
          href="/mainStyles/new-ui/login/css/templateBrain.css?v=<%=SpringPropertiesUtil.getProperty("cssVersion")%>"/>
    <!--phoneInput-->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/intl-tel-input@25.3.1/build/css/intlTelInput.css">

    <script src="/mainStyles/new-ui/login/js/jquery.min.js"></script>
    <script type="text/javascript" src="/mainStyles/js/jquery-3.2.1.min.js"></script>
    <script type=text/javascript src="/customisation/preprod.kpi.com/scripts/jquery.select2.js"></script>
    <script src="https://apis.google.com/js/client:platform.js?onload=start" async defer>
    </script>
    <script src="/mainStyles/new-ui/js/frame_affix.js"></script>
    <style>
        .progress-panel--remove-sample-data {
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            background-color: white;
            padding: 20px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            z-index: 1000;
            width: 600px;
            height: 400px;
        }

        .progress-panel__header {
            font-size: 1.3rem;
            text-align: center;
            margin: 15px auto 35px;
            max-width: 420px;
        }

        .progress-panel__steps,
        .progress-panel__current-desc,
        .cs-loader {
            margin-top: 10px;
        }

        .modal-overlay {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
            z-index: 999;
        }
    </style>

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
                                onclick="toggleLocaleLangOptions()">
                            ${not empty param.language ? param.language : 'uz'}
                        </button>
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
                <figcaption style="text-align: left;">${forgotTitle}
                    <p style="font-size: 16px; color: #828282">${forgotDesc}</p>
                </figcaption>
            </figure>

            <c:if test="${not empty message or errorMessageKey == 'not_found'}">
                <div class="alert alert-danger" style="color: #721c24; background-color: #f8d7da; border: 1px solid #f5c6cb; padding: 10px; border-radius: 4px; margin-bottom: 15px;">
                    <i class="fa fa-exclamation-circle"></i> ${not empty message ? message : emailNotFoundMsg}
                </div>
            </c:if>

            <form action="/auth/forgotPassword.html"
                  method="post"
                  onsubmit="validateForgotPassword(event)"
                  novalidate>
                <input type="hidden" name="language" value="${language}"/>


                <div class="form-group">
                    <label for="adminEmail">${emailLabel}</label>

                    <div class="control-group">
                        <input type="email"
                               id="adminEmail"
                               name="email"
                               class="form-control"
                               placeholder="${emailPlaceholder}" />
                    </div>

                    <div id="emailError" class="error-message" style="display:none;"></div>
                </div>

                <input type="submit" class="btn btn-primary" value="${continueBtn}"/>
            </form>

        </div>
    </div>
</div>

<!--phoneInput-->
<script src="https://cdn.jsdelivr.net/npm/intl-tel-input@25.3.1/build/js/intlTelInput.min.js"></script>
<script>
    const phoneInput = document.getElementById("phone");
    const iti = window.intlTelInput(phoneInput, {
        preferredCountries: ['uz', 'ru', 'kz', 'ae'],
        initialCountry: "uz",
        separateDialCode: false,
        autoPlaceholder: "aggressive",
        formatOnDisplay: true,
        utilsScript: "https://cdnjs.cloudflare.com/ajax/libs/intl-tel-input/25.3.1/js/utils.js"
    });

    // Запрет ввода букв
    phoneInput.addEventListener('input', function (e) {
        this.value = this.value.replace(/[^\d+]/g, '');
    });

    // Форсируем обновление placeholder после инициализации
    setTimeout(() => {
        const countryData = iti.getSelectedCountryData();
        phoneInput.placeholder = `+${countryData.dialCode} • `;
    }, 100);


    async function validateAndSubmit(event) {
        event.preventDefault(); // Prevent the default form submission

        var adminFName = document.getElementById("signUpFree_firstName").value;
        var adminEmail = document.getElementById("signUpFree_email").value;
        var phone = document.getElementById("signUpFree_phone").value;
        var dataToHash = adminFName + "/" + adminEmail + "/" + phone;

        const encoder = new TextEncoder();
        const data = encoder.encode(dataToHash);
        const hashBuffer = await crypto.subtle.digest('SHA-256', data);
        const hashArray = Array.from(new Uint8Array(hashBuffer));
        const hashHex = hashArray.map(byte => byte.toString(16).padStart(2, '0')).join('');

        // Set the hash value to the hidden input field
        document.getElementById("encodedData").value = hashHex;
        // Submit the form
        initializeProgressBar("/mainStyles/new-ui/css/materialize.min.css?v=<%=SpringPropertiesUtil.getProperty("cssVersion")%>");
        removeProgressPanel();
        // reCAPTCHA completed, proceed with form submission
        document.getElementById("signUpFree").submit();
    }
</script>

<script>
    function validateForgotPassword(event) {
        event.preventDefault();

        const emailInput = document.getElementById("adminEmail");
        const errorBox = document.getElementById("emailError");

        const email = emailInput.value.trim();

        errorBox.style.display = "none";
        emailInput.classList.remove("field-invalid");

        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        if (!email) {
            showError(EMAIL_REQUIRED_MSG);
            return;
        }

        if (!emailRegex.test(email)) {
            showError(EMAIL_INVALID_MSG);
            return;
        }

        event.target.submit();

        function showError(msg) {
            errorBox.innerText = msg;
            errorBox.style.display = "block";
            emailInput.classList.add("field-invalid");
        }
    }
</script>

<script>
    const EMAIL_REQUIRED_MSG = "${emailRequiredMsg}";
    const EMAIL_INVALID_MSG = "${emailInvalidMsg}";
</script>

<script>
    function formatState(state) {
        if (!state.id) {
            return state.text;
        }
        var $state = $(
            '<span><img class="flag flag-' + state.element.id.toLowerCase() + '" /> ' + state.text + '</span>'
        );
        return $state;
    };

    function selectedFormatState(state) {
        if (!state.id) return state.text;
        return $('<span><img class="flag flag-' + state.element.id.toLowerCase() + '" /></span>');
    };
    $("#signUpFree_country").select2({
        templateResult: formatState,
        templateSelection: selectedFormatState,
        placeholder: 'Country'
    }).on("select2:close", function (event) {
        $('#signUpFree_country').get(0).focus();
    }).on("change", function (changeEvent) {
        var title = $(this).children(":selected").attr("title");
        $('#signUpFree_phone').val('+' + title);
    });

    $('#signUpFree_email').on('keypress keydown', function (event) {
        if (event.keyCode === 9) {                      // 9 - tabkey when user blurs from name field he/she supposed to
            $('#signUpFree_country').select2('open');   // focus on country code field. so open event triggers
        }
    });

    function customPhoneValidation(e) {
        var keynum;
        var keychar;
        var num = '0123456789'
        if (e.keyCode) //IE
        {
            keynum = e.keyCode;
        } else if (e.which)// Netscape/Firefox/Opera
        {
            keynum = e.which;
        }
        keychar = String.fromCharCode(keynum);
        if ((num.indexOf(keychar) !== -1) || keynum === 8 || keynum === 9) {
            return true;
        } else return false;
    }

    function customPasteValidator(e) {
        // Get the pasted text
        const pastedData = event.clipboardData.getData('text');
        // You can modify the pasted text if needed
        const sanitizedData = pastedData.replace(/[^0-9]/g, ''); // Only allow numbers
        event.target.value = "+" + sanitizedData; // Set the sanitized value back to the input
        // Prevent default paste behavior
        event.preventDefault();
    }
</script>

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
    function updateLocale(lang) {
        window.location.search = '?language=' + lang;
    }
    function toggleLocaleLangOptions() {
        const localLangOptions = document.getElementById("locale-lang-options");
        localLangOptions.classList.toggle('active');
    }

    function updateLocale(lang) {
        document.documentElement.lang = lang;
        window.location.search = '?language=' + lang;
    }
</script>
<!--Mobile - Desktop visibility classes Switch-->
<script>
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

            document.querySelectorAll('.show--mobile').forEach(el => {
                el.style.display = isMobile ? '' : 'none';
            });

            document.querySelectorAll('.show--desktop').forEach(el => {
                el.style.display = isMobile ? 'none' : '';
            });

            const types = ['inline', 'inline-block', 'flex', 'grid'];
            types.forEach(type => {
                document.querySelectorAll(`.show--mobile--${type}`).forEach(el => {
                    el.style.display = isMobile ? type : 'none';
                });

                document.querySelectorAll(`.show--desktop--${type}`).forEach(el => {
                    el.style.display = isMobile ? 'none' : type;
                });
            });
        }
    }

    document.addEventListener('DOMContentLoaded', () => {
        new ResponsiveVisibility();
    });
</script>
</body>
</html>