<%@ page import="com.edatasite.workforce.appContext.SpringPropertiesUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!doctype html>
<html class="no-js" lang="">
<head>
  <meta charset="utf-8">
  <meta http-equiv="x-ua-compatible" content="ie=edge">
  <title>Sign in or Sign up to kpi.com</title>
  <meta name="description" content="">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <!--CSS-->
  <link rel="stylesheet"
        href="/mainStyles/new-ui/css/materialize.css?v=<%=SpringPropertiesUtil.getProperty("cssVersion")%>">
  <link rel="shortcut icon" href="/mainStyles/new-ui/login/img/praaktisgofavicon.png?v=2" type="image/png"/>
  <link rel="stylesheet" href="/mainStyles/new-ui/login/css/main.css">
  <link href="/customisation/kpi.com/select2.css" rel="stylesheet">
  <link rel="stylesheet" href="/mainStyles/new-ui/login/font-awesome/css/font-awesome.min.css">
  <link rel="stylesheet" href="/customisation/preprod.kpi.com/materialSIgnup/css/flags.min.css">

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
      font-size: 1.5em;
      margin-bottom: 10px;
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

  <script src="https://www.google.com/recaptcha/api.js" async defer></script>
  <script>
    async function validateAndSubmit(event) {
      event.preventDefault(); // Prevent the default form submission

      var response = grecaptcha.getResponse();

      if (response.length === 0) {
        alert("Please complete the reCAPTCHA.");
        return; // Do not proceed with form submission
      }

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
      initializeProgressBar();
      removeProgressPanel();
      // reCAPTCHA completed, proceed with form submission
      document.getElementById("signUpFree").submit();
    }
  </script>

  <style type="text/css"></style>
</head>
<body>

<c:set var="nameHasBindError">
  <form:errors path="adminFName"/>
</c:set>
<c:set var="phoneHasBindError">
  <form:errors path="phone"/>
</c:set>
<c:set var="emailHasBindError">
  <form:errors path="adminEmail"/>
</c:set>

<script type="text/javascript" src="/mainStyles/js/jquery-3.2.1.min.js"></script>
<script type=text/javascript src="/customisation/preprod.kpi.com/scripts/jquery.select2.js"></script>
<script type="text/javascript">
  $(document).ready(function() {
    document.querySelector('#external_url').value = btoa(document.referrer);
    document.querySelector('#company_signed_up_from').value = window.location.href
  });
</script>
<div class="pg_landing pg_sign-up">
  <div class="pg_landing__container">
    <figure class="pg_landing__header">
      <img src="/customisation/praaktisgo/images/newpraaktisgo.svg" alt="logo">
      <figcaption>Sign up now and try the trial <br> period for 30 days.</figcaption>
    </figure>
    <div class="pg_landing__main">
      <div class="pg_landing__main-innerbox">
        <div class="${not empty errorMessage ? 'cp_login cp_login--error' : 'cp_login'}">
          <c:if test="${not empty errorMessage}">
            <div class="cp_login__message--error">
                ${errorMessage}
            </div>
          </c:if>

          <div class="cp_login__content">
            <div class="cp_signup__caption"><h3>Sign up</h3>
                                          <ul class="cp_login__options">
                                              <li class="cp_login__option cp_login__option--google">
                                                  <a class="fa fa-google-plus" href="#" onclick="google_login_new(event)"></a>
                                              </li>
              <%--                                <li class="cp_login__option cp_login__option--facebook facebook">--%>
              <%--                                    <a class="fa fa-facebook-f " href="#" onclick="fb_login(event)"></a>--%>
              <%--                                </li>--%>

              <%--                                <li class="cp_login__option cp_login__option--linkedin">--%>
              <%--                                    <a class="fa fa-linkedin" href="#" onclick="linkedin_login(event)"></a>--%>
              <%--                                </li>--%>
              <%--                                <li class="cp_login__option  cp_login__option--office365">--%>
              <%--                                    <a class="ficon--office365" href="#" onclick="office_login(event)"></a>--%>
              <%--                                </li>--%>
                                          </ul>
            </div>
            <form:form method="post" modelAttribute="newCompany" id="signUpFree" cssClass="cp_login__main">
              <input type="hidden" name="referrer" value="" id="external_url"/>
              <input type="hidden" id="encodedData" name="kpi_code"/>
              <input type="hidden" name="companySignedUpFrom" value="" id="company_signed_up_from"/>

              <div class="cp_login__field">
                <form:input path="name" required="required" id="companyName" cssClass="${not empty nameHasBindError ? 'cp_login__field-invalid' : ''}"
                            placeholder="Enter your company name"/>
                <div class="cp_login__field-underline "></div>
              </div>

              <div class="cp_login__field">
                <form:input path="adminFName" required="required" id="signUpFree_firstName" cssClass="${not empty nameHasBindError ? 'cp_login__field-invalid' : ''}"
                            placeholder="Name"/>
                <div class="cp_login__field-underline "></div>
              </div>
              <div class="cp_login__field">
                <form:input path="adminEmail" id="signUpFree_email" required="required"  type="email" placeholder="Email" cssClass="${not empty emailHasBindError ? 'cp_login__field-invalid' : ''}"/>
                <div class="cp_login__field-underline">
                </div>
              </div>

              <div class="cp_login__field">
                <select id="currencyDropdown" name="currencyID" class="cp_login__dropdown">
                  <option value="" disabled selected>Choose your currency</option>
                  <c:forEach var="currency" items="${currencies}">
                    <option value="${currency.id}">
                        ${currency.name} (${currency.properties['symbol']})
                    </option>
                  </c:forEach>
                </select>
                <div class="cp_login__field-underline"></div>
              </div>

              <div class="cp_login__field signUp-phone">
                <div class="signUp-phone__prefix">
                  <form:select path="countryCode" id="signUpFree_country">
                    <option></option>
                    <c:forEach items="${countryCallCodes}" var="ccc">
                      <c:choose>
                        <c:when test="${ccc.countryCode == currentCountry}">
                          <option value="${ccc.countryCode}" title="${ccc.callCode}" id="${ccc.countryCode}"
                                  selected="selected">
                              ${ccc.name}
                          </option>
                        </c:when>
                        <c:otherwise>
                          <option value="${ccc.countryCode}" title="${ccc.callCode}" id="${ccc.countryCode}">
                              ${ccc.name}
                          </option>
                        </c:otherwise>
                      </c:choose>
                    </c:forEach>
                  </form:select>
                </div>
                <div class="signUp-phone__phone">
                  <form:input path="phone" id="signUpFree_phone" placeholder="Phone" value="${currentCallCode}"
                              onkeypress="return customPhoneValidation(event)"
                              onpaste="return customPasteValidator(event)"
                              cssClass="${not empty phoneHasBindError ? 'cp_login__field-invalid' : ''}"
                  />
                </div>
                <div class="cp_login__field-underline">
                </div>
              </div>

              <div class="cp_login__field cp_login__field--password">
                <input type="text"  id="promoCode" name="promoCode" placeholder="Promo code">
                <div class="cp_login__field-underline"></div>
              </div>

              <div class="cp_login__field controls-stack">
                <label class="control control--checkbox">
                  <input type="checkbox" name="agreeWithTerms" id="agreeWithTerms" required="">
                  <span class="control__indicator"></span>
                  <span class="control__description" for="agreeWithTerms">
                                        I agree to the
                                        <a href="https://praaktis.com/terms_of_use.html">Terms of Service</a>
                                        and
                                        <a href="https://praaktis.com/terms_of_use.html">Privacy Policy</a>.
                                    </span>
                </label>
              </div>

              <div class="cp_login__field">
                <div class="g-recaptcha" data-sitekey="6Lc-FRMqAAAAAPzkqZwvH7ttAY0-2pW0_ekA0V5N" data-callback="checkRecaptcha"></div>
              </div>

              <div class="cp_login__form-item" id="signUp">
                <input type="submit" class="cp_login__submit elm_btn--blue"
                       style="background-color: #6980F9; color: white !important;"
                       color="#fff;"
                       value="Sign up"
                       onclick="validateAndSubmit(event)" />
              </div>
            </form:form>
          </div>
          <%--                    <div class="cp_login__footer">--%>
          <%--                        <dl>--%>
          <%--                            <dt>I already have an account</dt>--%>
          <%--                            <dd>--%>
          <%--                                <span class="pg_landing__start-btn">--%>
          <%--                                    <a class="btn-login-sign-up" style="color: #6980F9" href="/index.html">Login</a>--%>
          <%--                                </span>--%>
          <%--                            </dd>--%>
          <%--                        </dl>--%>
          <%--                    </div>--%>
        </div>
      </div>
    </div>
  </div>
</div>

<form id="google-signin-form" action="/google-oauth2-verify" method="post">
  <input type="hidden" name="code" id="googlecode">
</form>


<script src="/mainStyles/new-ui/login/js/slick.min.js"></script>
<script src="/mainStyles/new-ui/login/js/main.js"></script>
<script src="https://apis.google.com/js/client:platform.js?onload=start" async defer></script>
<script src="/mainStyles/new-ui/js/frame_affix.js"></script>
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
  }).on("change", function(changeEvent) {
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
    if ((num.indexOf(keychar) != -1) || keynum === 8 || keynum === 9) {
      return true;
    } else return false;
  }
</script>

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
  function customPasteValidator(e) {
    // Get the pasted text
    const pastedData = event.clipboardData.getData('text');
    // You can modify the pasted text if needed
    const sanitizedData = pastedData.replace(/[^0-9]/g, ''); // Only allow numbers
    event.target.value = "+" + sanitizedData; // Set the sanitized value back to the input
    // Prevent default paste behavior
    event.preventDefault();
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
    js.src = "//connect.facebook.net/en_US/all.js#xfbml=1&appId=860786822123441";
    fjs.parentNode.insertBefore(js, fjs);
  }(document, 'script', 'facebook-jssdk'));</script>


<!--INTERNATIONSL TEL-->

<div></div>
<iframe id="ssIFrame_google" sandbox="allow-scripts allow-same-origin" aria-hidden="true" frame-border="0"
        src="https://accounts.google.com/o/oauth2/iframe#origin=https%3A%2F%2Fdev.kpi.com&amp;rpcToken=336232152.19868475&amp;clearCache=1"
        style="position: absolute; width: 1px; height: 1px; left: -9999px; top: -9999px; right: -9999px; bottom: -9999px; display: none;"></iframe>
<div id="fb-root" class=" fb_reset">
  <div style="position: absolute; top: -10000px; width: 0px; height: 0px;">
    <div>
      <iframe name="fb_xdm_frame_https" frameborder="0" allowtransparency="true" allowfullscreen="true"
              scrolling="no" allow="encrypted-media" id="fb_xdm_frame_https" aria-hidden="true"
              title="Facebook Cross Domain Communication Frame" tabindex="-1"
              src="https://staticxx.facebook.com/connect/xd_arbiter/r/d_vbiawPdxB.js?version=44#channel=f352e44c03b2ad&amp;origin=https%3A%2F%2Fdev.kpi.com"
              style="border: none;"></iframe>
    </div>
    <div>
      <iframe name="f2d4a1801e676b4" frameborder="0" allowtransparency="true" allowfullscreen="true"
              scrolling="no" allow="encrypted-media"
              src="https://www.facebook.com/connect/ping?client_id=860786822123441&amp;domain=dev.kpi.com&amp;origin=1&amp;redirect_uri=https%3A%2F%2Fstaticxx.facebook.com%2Fconnect%2Fxd_arbiter%2Fr%2Fd_vbiawPdxB.js%3Fversion%3D44%23cb%3Df30b3a865bc3994%26domain%3Ddev.kpi.com%26origin%3Dhttps%253A%252F%252Fdev.kpi.com%252Ff352e44c03b2ad%26relation%3Dparent&amp;response_type=token%2Csigned_request&amp;sdk=joey"
              style="display: none;"></iframe>
    </div>
  </div>
</div>
</body>
</html>
