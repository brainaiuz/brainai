
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
        <c:set var="successTitle" value="Письмо отправлено" />
        <c:set var="successMsg" value="На ваш зарегистрированный email было отправлено письмо со ссылкой для восстановления." />
        <c:set var="junkMsg" value="Если вы не получите письмо в ближайшее время, проверьте папки 'Спам' или 'Рассылки'. Чтобы гарантированно получать письма от BRAIN.UZ в будущем, добавьте домен BRAIN.UZ в список доверенных отправителей." />
    </c:when>

    <c:when test="${language == 'en'}">
        <c:set var="successTitle" value="Email Sent" />
        <c:set var="successMsg" value="An email has been sent to your registered email with a recovery link." />
        <c:set var="junkMsg" value="If you do not receive an email shortly, check your 'bulk email' or 'junk email' folders. To make sure you receive email from BRAIN.UZ in the future, add the BRAIN.UZ domain to your email safe list." />
    </c:when>

    <c:otherwise>
        <c:set var="successTitle" value="Email yuborildi" />
        <c:set var="successMsg" value="Ro'yxatdan o'tgan emailingizga parolni tiklash havolasi yuborildi." />
        <c:set var="junkMsg" value="Agar tez orada xat olmasangiz, 'spam' yoki 'keraksiz xatlar' papkasini tekshiring. Kelajakda BRAIN.UZ'dan xat olishingizga ishonch hosil qilish uchun BRAIN.UZ domenini xavfsiz ro'yxatga qo'shing." />
    </c:otherwise>
</c:choose>


<!doctype html>
<html class="no-js" lang="en" dir="ltr">
<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BRAIN - Signup</title>
    <link rel="shortcut icon" href="/customisation/brain/favicon.png.png" type="image/x-icon"/>
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
            <figure class="login-header">
                <a href="#"><img src="/customisation/brain/logo.png" alt="Brain Logo"/></a>
            </figure>
            <br/>
            <p style="font-size: 20px;">${successMsg}</p>
            <p style="font-size: 14px; color: #828282;">${junkMsg}</p>
        </div>
    </div>
</div>

<form id="google-signin-form" action="/google-oauth2-verify" method="post">
    <input type="hidden" name="code" id="googlecode"/>
</form>

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