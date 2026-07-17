<%@ page import="com.edatasite.workforce.appContext.SpringPropertiesUtil" %>
<%@ page import="com.edatasite.workforce.utils.EdsContextParams" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="title" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="language" value="${not empty param.language ? param.language : 'uz'}" scope="session"/>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="com.edatasite.workforce.gwt.core.client.localization.WfmStrings"/>
<fmt:message key="brainSignUp" var="signUpMessage"/>

<!doctype html>
<html class="no-js" lang="en" dir="ltr">
<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BRAIN - Signup</title>
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
    <script>
        function start() {
            gapi.load('auth2', function () {
                auth2 = gapi.auth2.init({
                    client_id: '${GOOGLE_CLIENT_ID}',
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

        .modal-overlay__backdrop {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: #fff;
            z-index: 998;
        }

        .field-invalid {
            border: 1px solid #e74c3c !important;
        }

        .error-message {
            color: #e74c3c;
            font-size: 0.85rem;
            margin-top: 6px;
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

                <c:if test="${param.language == null || param.language == 'uz'}">
                    <figcaption>Ro'yxatdan o'ting va bepul sinab ko'ring</figcaption>
                </c:if>
                <c:if test="${param.language == 'ru'}">
                    <figcaption>Зарегистрируйтесь и попробуйте бесплатно</figcaption>
                </c:if>
                <c:if test="${param.language == 'en'}">
                    <figcaption>Register and try it for free</figcaption>
                </c:if>
            </figure>

            <form:form id="signUpFree" class="login-form" method="post" modelAttribute="newCompany">
                <% if (request.getParameter("redirect_uri") != null) { %>
                <input type="hidden" id="redirect_uri" name="redirect_uri"
                       value="<%=request.getParameter("redirect_uri")%>"/>
                <%}%>
                <input type="hidden" id="encodedData" name="kpi_code"/>

                <c:if test="${not empty errorMessage}">
                    <div class="error-message">
                            ${errorMessage}
                    </div>
                </c:if>

                <div class="form-group ${errors.getFieldError("adminFName") != null ? 'has-error' : ''}">
                    <label for="signUpFree_firstName"><fmt:message key="name"/></label>
                    <div class="control-group">
                        <form:input path="adminFName" type="text" id="signUpFree_firstName" name="USER_NAME"
                                    class="form-control" placeholder="Enter your name"/>
                    </div>
                    <div class="error-message">${errors.getFieldError("adminFName").defaultMessage}</div>
                </div>

                <div class="form-group ${errors.getFieldError("adminEmail") != null ? 'has-error' : ''}">
                    <label for="adminEmail"><fmt:message key="emailAddress"/></label>
                    <div class="control-group">
                        <form:input path="adminEmail" type="email" id="signUpFree_email" name="USER_EMAIL"
                                    class="form-control" placeholder="Enter your email"/>
                    </div>
                    <div class="error-message">${errors.getFieldError("adminEmail").defaultMessage}</div>
                </div>

                <div class="form-group">
                    <label for="industry">
                        <c:if test="${param.language == null || param.language == 'uz'}">Soha</c:if>
                        <c:if test="${param.language == 'ru'}">Отрасль</c:if>
                        <c:if test="${param.language == 'en'}">Industry</c:if>
                    </label>

                    <div class="control-group">
                        <form:select path="industry" name="industry" id="signUpFree_industry" class="form-control">
                            <option value="">
                                <c:if test="${param.language == null || param.language == 'uz'}">Soha tanlang</c:if>
                                <c:if test="${param.language == 'ru'}">Выберите отрасль</c:if>
                                <c:if test="${param.language == 'en'}">Select industry</c:if>
                            </option>

                            <c:forEach items="${industries}" var="ind">
                                <option value="${ind.code}">
                                    <c:choose>
                                        <c:when test="${param.language == 'ru'}">
                                            ${ind.category}
                                        </c:when>
                                        <c:when test="${param.language == 'en'}">
                                            ${ind.name}
                                        </c:when>
                                        <c:otherwise>
                                            ${ind.description}
                                        </c:otherwise>
                                    </c:choose>
                                </option>
                            </c:forEach>
                        </form:select>
                    </div>
                </div>

                <div class="form-group">
                    <label for="employeeCount">
                        <c:if test="${param.language == null || param.language == 'uz'}">Xodimlar soni</c:if>
                        <c:if test="${param.language == 'ru'}">Количество сотрудников</c:if>
                        <c:if test="${param.language == 'en'}">Employees count</c:if>
                    </label>

                    <div class="control-group">
                        <form:input path="employeeCount" id="signUpFree_employeeCount" class="form-control"
                                    inputmode="numeric" pattern="[0-9]*"
                                    placeholder="${param.language == 'ru' ? 'Введите количество сотрудников' : param.language == 'en' ? 'Enter number of employees' : 'Xodimlar sonini kiriting'}"
                                    oninput="this.value = this.value.replace(/[^0-9]/g, '')"
                                    onkeypress="return /[0-9]/.test(String.fromCharCode(event.charCode))"/>
                    </div>
                </div>


                <div class="form-group">
                    <label for="country"><fmt:message key="country"/></label>
                    <div class="control-group">
                        <form:select path="countryCode" id="signUpFree_country" class="form-control">
                            <option></option>
                            <c:forEach items="${countryCallCodes}" var="ccc">
                                <c:choose>
                                    <c:when test="${ccc.countryCode == currentCountry}">
                                        <option value="${ccc.countryCode}" title="${ccc.callCode}"
                                                id="${ccc.countryCode}"
                                                selected="selected">
                                                ${ccc.name}
                                        </option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${ccc.countryCode}" title="${ccc.callCode}"
                                                id="${ccc.countryCode}">
                                                ${ccc.name}
                                        </option>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </form:select>
                    </div>
                </div>

                <div class="form-group ${errors.getFieldError("phone") != null ? 'has-error' : ''}">
                    <label for="phone"><fmt:message key="phoneNumber"/></label>
                    <div class="control-group">
                        <form:input path="phone" id="signUpFree_phone" placeholder="Phone"
                                    value="${currentCallCode}"
                                    onkeypress="return customPhoneValidation(event)"
                                    onpaste="return customPasteValidator(event)"
                                    class="form-control"
                                    cssClass="${not empty phoneHasBindError ? 'cp_login__field-invalid' : ''}"
                        />
                    </div>
                    <div class="error-message">${errors.getFieldError("phone").defaultMessage}</div>
                </div>

                <div class="form-group remember-me">
                    <input type="checkbox" id="remember" name="TERMS_AGREED">
                    <c:if test="${param.language == null || param.language == 'uz'}">
                        <label for="remember"><span>Men <a href="#">shartlar va siyosatga</a> roziman</span></label>
                    </c:if>
                    <c:if test="${param.language == 'ru'}">
                        <label for="remember"><span>Я согласен с</span> <a href="#"> условиями и политикой</a></label>
                    </c:if>
                    <c:if test="${param.language == 'en'}">
                        <label for="remember"><span>I agree to the</span> <a href="#"> terms &amp; policy</a></label>
                    </c:if>
                </div>

                <c:choose>
                    <c:when test="${param.language == 'ru'}">
                        <input type="submit" class="btn btn-primary" value="Зарегистрироваться" onclick="validateAndSubmit(event)"/>
                    </c:when>
                    <c:when test="${param.language == 'en'}">
                        <input type="submit" class="btn btn-primary" value="Sign up" onclick="validateAndSubmit(event)"/>
                    </c:when>
                    <c:otherwise>
                        <input type="submit" class="btn btn-primary" value="Ro'yxatdan o'tish" onclick="validateAndSubmit(event)"/>
                    </c:otherwise>
                </c:choose>

                <div class="signup-link">
                    <fmt:message key="brainHaveAccount"/> <a href="/auth/login.html"><fmt:message key="signIn"/></a>
                </div>
            </form:form>
        </div>
    </div>
</div>

<form id="google-signin-form" action="/google-oauth2-verify" method="post">
    <input type="hidden" name="code" id="googlecode"/>
</form>

<script src="https://cdn.jsdelivr.net/npm/intl-tel-input@25.3.1/build/js/intlTelInput.min.js"></script>
<script>
    const phoneInput = document.getElementById("signUpFree_phone");
    const iti = window.intlTelInput(phoneInput, {
        preferredCountries: ['uz', 'ru', 'kz', 'ae'],
        initialCountry: "uz",
        separateDialCode: false,
        autoPlaceholder: "aggressive",
        formatOnDisplay: true,
        utilsScript: "https://cdnjs.cloudflare.com/ajax/libs/intl-tel-input/25.3.1/js/utils.js"
    });
    // Expose on window so the select2 change handler (in a separate <script> block) can reach it reliably.
    window.iti = iti;

    phoneInput.addEventListener('input', function (e) {
        this.value = this.value.replace(/[^\d+]/g, '');
    });

    setTimeout(() => {
        var dialCode = (window.iti && window.iti.getSelectedCountryData && window.iti.getSelectedCountryData().dialCode) || '';
        phoneInput.placeholder = `+${dialCode} • `;
    }, 100);

    function setFieldError(el, message) {
        if (!el) return;

        el.classList.add('field-invalid');

        const formGroup = el.closest('.form-group') || el.parentElement;
        if (!formGroup) return;

        let err = formGroup.querySelector('.client-error-message');
        if (!err) {
            err = document.createElement('div');
            err.className = 'error-message client-error-message';
            formGroup.appendChild(err);
        }
        err.textContent = message || 'This field is required';
    }

    function clearFieldError(el) {
        if (!el) return;

        if (el.classList) el.classList.remove('field-invalid');

        if (window.jQuery && el.tagName === 'SELECT') {
            const $selection = $(el).next('.select2').find('.select2-selection');
            $selection.removeClass('field-invalid');
        }

        const formGroup = el.closest ? (el.closest('.form-group') || el.parentElement) : el.parentElement;
        if (!formGroup) return;

        const err = formGroup.querySelector ? formGroup.querySelector('.client-error-message') : null;
        if (err) err.remove();
    }


    function isValidEmail(email) {
        return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
    }

    function normalizePhoneDigits(value) {
        return (value || '').replace(/\D/g, '');
    }

    async function validateAndSubmit(event) {
        event.preventDefault(); // Prevent the default form submission

        const firstNameEl = document.getElementById("signUpFree_firstName");
        const emailEl = document.getElementById("signUpFree_email");
        const industryEl = document.getElementById("signUpFree_industry");
        const employeeCountEl = document.getElementById("signUpFree_employeeCount");
        const countryEl = document.getElementById("signUpFree_country");
        const phoneEl = document.getElementById("signUpFree_phone");
        const termsEl = document.getElementById("remember");

        [firstNameEl, emailEl, industryEl, employeeCountEl, countryEl, phoneEl].forEach(clearFieldError);

        let hasError = false;

        const firstName = (firstNameEl?.value || '').trim();
        const email = (emailEl?.value || '').trim();
        const industry = (industryEl?.value || '').trim();
        const employeeCount = (employeeCountEl?.value || '').trim();
        const country = (countryEl?.value || '').trim();
        const phoneRaw = (phoneEl?.value || '').trim();
        const phoneDigits = normalizePhoneDigits(phoneRaw);

        if (!firstName) {
            setFieldError(firstNameEl, "Name is required");
            hasError = true;
        }

        if (!email) {
            setFieldError(emailEl, "Email is required");
            hasError = true;
        } else if (!isValidEmail(email)) {
            setFieldError(emailEl, "Enter a valid email address");
            hasError = true;
        }

        if (!industry) {
            setFieldError(industryEl, "Industry is required");
            hasError = true;
        }

        if (!employeeCount) {
            setFieldError(employeeCountEl, "Employee count is required");
            hasError = true;
        }

        if (!country) {
            setFieldError(countryEl, "Country is required");
            const s2 = document.querySelector('#signUpFree_country + .select2 .select2-selection');
            if (s2) s2.classList.add('field-invalid');
            hasError = true;
        } else {
            const s2 = document.querySelector('#signUpFree_country + .select2 .select2-selection');
            if (s2) s2.classList.remove('field-invalid');
        }

        if (!phoneRaw || phoneDigits.length < 7) {
            setFieldError(phoneEl, "Phone number is required");
            hasError = true;
        }

        if (!termsEl.checked) {
            setFieldError(termsEl, "You must agree to the terms");
            hasError = true;
        }

        if (hasError) return;

        var dataToHash = firstName + "/" + email + "/" + phoneRaw;

        const encoder = new TextEncoder();
        const data = encoder.encode(dataToHash);
        const hashBuffer = await crypto.subtle.digest('SHA-256', data);
        const hashArray = Array.from(new Uint8Array(hashBuffer));
        const hashHex = hashArray.map(byte => byte.toString(16).padStart(2, '0')).join('');

        document.getElementById("encodedData").value = hashHex;
        const backdrop = document.createElement('div');
        backdrop.className = 'modal-overlay__backdrop';
        document.body.appendChild(backdrop);
        initializeProgressBar("/mainStyles/new-ui/css/materialize.min.css?v=<%=SpringPropertiesUtil.getProperty("cssVersion")%>");
        removeProgressPanel();
        document.getElementById("signUpFree").submit();
    }

    document.addEventListener('DOMContentLoaded', () => {
        function bindClear(el) {
            if (!el) return;
            el.addEventListener('input', () => clearFieldError(el));
            el.addEventListener('change', () => clearFieldError(el));
            el.addEventListener('blur', () => clearFieldError(el));
        }

        bindClear(document.getElementById("signUpFree_firstName"));
        bindClear(document.getElementById("signUpFree_email"));
        bindClear(document.getElementById("signUpFree_industry"));
        bindClear(document.getElementById("signUpFree_employeeCount"));
        bindClear(document.getElementById("signUpFree_phone"));

        if (window.jQuery && $("#signUpFree_country").length) {
            $("#signUpFree_country").on("change select2:select select2:clear", function () {
                clearFieldError(this);
            });
        }
    });
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
    // Keeps the phone field (intl-tel-input flag + dial code) in sync with the
    // country chosen in the select2 dropdown. The flag is owned by `iti`, which
    // is independent of the <select>, so it must be updated explicitly via setCountry.
    function syncCountryToPhone() {
        var $selected = $("#signUpFree_country").children(":selected");
        var callCode = $selected.attr("title");               // dial code, e.g. "998"
        var iso = ($selected.attr("id") || "").toLowerCase(); // ISO code, e.g. "uz"
        if (!callCode) return;                                 // placeholder / nothing selected
        // Update the intl-tel-input flag to match. window.iti is set in the first <script> block.
        if (iso && window.iti && typeof window.iti.setCountry === 'function') {
            try { window.iti.setCountry(iso); } catch (e) { /* ignore non-standard ISO codes iti doesn't know */ }
        }
        $('#signUpFree_phone').val('+' + callCode);
    }

    $("#signUpFree_country").select2({
        templateResult: formatState,
        templateSelection: selectedFormatState,
        placeholder: 'Country'
    }).on("select2:close", function (event) {
        $('#signUpFree_country').get(0).focus();
    }).on("change", function (changeEvent) {
        syncCountryToPhone();
    });

    // First paint: make the flag + dial code match the preselected country.
    syncCountryToPhone();

    $('#signUpFree_email').on('keypress keydown', function (event) {
        if (event.keyCode === 9) {
            $('#signUpFree_country').select2('open');
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
        const pastedData = event.clipboardData.getData('text');
        const sanitizedData = pastedData.replace(/[^0-9]/g, '');
        event.target.value = "+" + sanitizedData;
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
            $('#googlecode').val(authResult['code']);
            $('#google-signin-form').submit();
        } else {
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