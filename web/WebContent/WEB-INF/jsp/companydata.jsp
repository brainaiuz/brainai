<%@ page import="com.edatasite.workforce.appContext.SpringPropertiesUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.edatasite.workforce.gwt.core.server.app.Utils" %>
<%@ page import="javax.servlet.http.HttpServletRequest" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
    boolean isPraaktis = Utils.isPraaktis(request);
    String defaultOrgType = isPraaktis ? "GYM" : "e.g. plumbing, real estate";
    String hostName = (request.getAttribute("hostName") != null) ? request.getAttribute("hostName").toString() : request.getServerName();
%>

<!doctype html>
<html lang="${locale}">
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
    <link rel="stylesheet" href="/mainStyles/new-ui/login/font-awesome/css/font-awesome.css"/>
    <link rel="stylesheet" href="/mainStyles/new-ui/login/slick/slick.css"/>
    <link rel="stylesheet"
          href="/mainStyles/new-ui/css/materialize.css?v=<%=SpringPropertiesUtil.getProperty("cssVersion")%>">
    <link rel="stylesheet"
          href="/mainStyles/new-ui/css/pass-validation.css?v=<%=SpringPropertiesUtil.getProperty("cssVersion")%>">
    <link rel="stylesheet" href="/mainStyles/new-ui/css/transition.css">
    <link rel="shortcut icon" href="/mainStyles/new-ui/login/img/favicon.ico?v=2" type="image/x-icon">
</head>
<body class="wizard-page wizard-page--org signUpStep signUpStep--4">

<div class="modal-holder active">
    <div class="modal modal--md sign-up-step sign-up-step--3">
        <div class="modal-wrapper">
            <div class="modal-header">
                <div class="hgroup">
                    <h1 class="fs-4"><fmt:message key="companydata.Addyourorganisationtostartusingkpinow"/>
                    </h1>
                </div>
            </div>
            <div class="modal-content">
                <form action="/companyinit.html" method="post">
                    <div class="form-group">
                        <div class="form-group__label"><fmt:message key="companydata.WHATISTHENAMEOFYOURORGANIZATION"/>
                        </div>
                        <div class="form-group__content">
                            <input type="text" class="form-control" id="login" name="name" value="${name}"
                                   placeholder="Your Company Name" required/>
                        </div>
                    </div>
                    <c:forEach items="${modules}" var="module">
                        <input type="hidden" name="modules" value="${module}">
                    </c:forEach>

                    <div class="form-group">
                        <div class="form-group__label"><fmt:message key="companydata.WhatDoesYourOrganisationDo"/>
                        </div>
                        <div class="form-group__content">
                            <input id="type" class="form-control" name="orgType" type="text"
                                   placeholder="e.g. plumbing, real estate"
                                   value="<%= defaultOrgType %>"/>
                        </div>
                    </div>
                    <%--Country Field--%>
                    <div class="form-group">
                        <div class="form-group__label"><fmt:message key="companydata.WHEREISYOURORGANIZATIONBASED"/>
                        </div>
                        <div class="form-group__content">
                            <div class="form-control input-field listbox-wrapper">
                                <div class="select-wrapper gwt-ListBox"><span class="caret">▼</span>
                                    <select id="country" name="country" class="gwt-ListBox initialized" inside-origin="false" onchange="onChangeCountry()">
                                        <option value="-1" disabled selected>Please Select</option>
                                        <c:forEach items="${countries}" var="country">
                                            <option value="${country.id}" ${country.id == companyCountryId ? 'selected' : ''}>${country.name}</option>
                                        </c:forEach>
                                    </select></div>
                                <label></label><span class="material-label"></span>
                            </div>
                        </div>
                    </div>

                    <%--Timezone Field--%>
                    <div class="form-group">
                        <div class="form-group__label"><fmt:message key="companydata.WhatIsYourTimeZone"/>
                        </div>
                        <div class="form-group__content">
                            <div class="form-control input-field listbox-wrapper">
                                <div class="select-wrapper gwt-ListBox"><span class="caret">▼</span>
                                    <select id="timezone" name="timezone" class="gwt-ListBox initialized" inside-origin="false">
                                        <option value="-1" disabled selected>Please Select</option>
                                        <c:choose>
                                            <c:when test="${fn:length(timezones) == 1}">
                                                <option value="${timezones[0].id}" selected>${timezones[0].name}</option>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach items="${timezones}" var="timezone">
                                                    <option value="${timezone.id}" ${timezone.id == timeZoneId ? 'selected':''}>${timezone.name}</option>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                    </select></div>
                                <label></label><span class="material-label"></span>
                            </div>
                        </div>
                    </div>

                    <%--Language Field--%>
                    <div class="form-group">
                        <div class="form-group__label"><fmt:message key="companydata.SelectLanguage"/>
                        </div>
                        <div class="form-group__content">
                            <div class="form-control input-field listbox-wrapper">
                                <div class="select-wrapper gwt-ListBox"><span class="caret">▼</span>
                                    <select id="lang" name="language" class="gwt-ListBox initialized" inside-origin="false"
                                            required>
                                        <option value="-1">Please Select</option>
                                        <c:forEach items="${languages}" var="language">
                                            <option value="${language.description}" ${userLanguage != null ? (userLanguage == language.name ? 'selected' : '') : (language.name == 'English' ? 'selected' : '')}>${language.name}</option>
                                        </c:forEach>
                                    </select></div>
                                <label></label><span class="material-label"></span>
                            </div>
                        </div>
                    </div>
                    <button type="submit" class="btn btn--primary"><fmt:message key="companydata.LetsGetStarted"/> </button>
                </form>
            </div>
        </div>
    </div>
    <div class="lean-overlay file--companydata" id="materialize-lean-overlay-1" style="opacity: 0.5;"></div>
</div>
<!--SCRIPTS-->
<script type="text/javascript" src="/mainStyles/new-ui/js/jquery.min.js"></script>
<script type="text/javascript" src="/mainStyles/new-ui/js/materialize-0.97.5.min.js"></script>

</body>
<script type="text/javascript">
    (function ($) {
        $(document).ready(function () {
            $('select').material_select();
            $('span.caret').on('click', function (event) {
                this.nextSibling.focus();
                // $('input.select-dropdown').focus();
            });
        });
    })(jQuery);

    function onChangeCountry() {
        $.ajax({
            url: "/services/api/v2/countries/" + $("select#country option").filter(":selected").val() + "/timezones",
            type: 'GET',
            success: function (response) {

                if (response.data.list != 0) {
                    var $timezone = $("select[name='timezone']");
                    $timezone.empty();

                    var newOpt = $("<option>").attr("value", "-1").text("Please Select");
                    $timezone.append(newOpt);
                    $.each(response.data.list, function (item) {
                        var newOpt = $("<option>").attr("value", this.id).text(this.title);
                        $timezone.append(newOpt);
                    });
                    $timezone.material_select();
                }
            },
            error: function (error) {
                alert(error);
            },
            beforeSend: setHeader
        });
    }
    function setHeader(xhr) {
        xhr.setRequestHeader('accessToken', '22cfd8ef-2678-47ea-b750-738c59615598');
        xhr.setRequestHeader('x-auth', readCookie("SESSION_ID"));
    }
    function readCookie(name) {
        var nameEQ = name + "=";
        var ca = document.cookie.split(';');
        for (var i = 0; i < ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0) == ' ') c = c.substring(1, c.length);
            if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length, c.length);
        }
        return null;
    }
</script>
</html>