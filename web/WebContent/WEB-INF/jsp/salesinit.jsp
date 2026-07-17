<%@ page import="com.edatasite.workforce.appContext.SpringPropertiesUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<% String hostName = (request.getAttribute("hostName") != null) ? request.getAttribute("hostName").toString() : request.getServerName(); %>
<!doctype html>
<html class="no-js" lang="">
<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>
        <fmt:message key="salesinit.salesSetup"></fmt:message>
    </title>

    <link rel="stylesheet" href="/mainStyles/new-ui/css/materialize.css?v=<%=SpringPropertiesUtil.getProperty("cssVersion")%>">

    <link rel="shortcut icon" href="/mainStyles/new-ui/login/img/favicon.ico?v=2" type="image/x-icon">
</head>

<body toast="bottom-right" class="features-list-wrapper">
<!--Import jQuery before materialize.js-->
<script type="text/javascript" src="/mainStyles/js/jquery-3.2.1.min.js"></script>
<%--<!--<script type="text/javascript" src="js/jquery.easing.1.3.js"></script>-->--%>
<script type="text/javascript" src="/mainStyles/new-ui/js/materialize-0.97.5.min.js"></script>


<form action="/salesinit.html" method="post" class="features-list features-list--crm">
    <aside class="features-aside">
        <div class="features-list__heading">
            <a class="features-aside__title">
                <i class="ficon--bar-chart-bold main-modules__module-icon"></i>
                <span>Sales</span>
            </a>
        </div>

        <dl class="features-aside__welcome">
            <dt><fmt:message key="salesinit.Welcome"> </fmt:message></dt>
            <dd>${fullname}!</dd>
        </dl>

        <ul class="features-aside__steps">
            <li id="orgdetails" class="status--current">
                <span><fmt:message key="salesinit.organizationDetails"> </fmt:message></span>
            </li>
            <li id="invite" class="status--not-passed">
                <span><fmt:message key="salesinit.inviteUser"> </fmt:message></span>
            </li>
            <li id="email-settings" class="status--not-passed">
                <span><fmt:message key="salesinit.emailSettings"> </fmt:message></span>
            </li>
        </ul>
    </aside>
    <main id="firstpart">
        <div class="features-list__heading">
            <h2 class="features-list__title">
                <fmt:message key="salesinit.salesSetup"> </fmt:message>
            </h2>
            <h3 class="features-list__sub-title">
                <fmt:message key="salesinit.giveUsSomeBasicDetails"> </fmt:message>
            </h3>
        </div>

        <div class="features-list__main-body">
            <div class="content-holder">
                <fieldset>
                    <div class="grid-row">
                        <div class="col-6">
                            <div class="form-group">
                                <label for="industry" class="form-group__label">
                                    <fmt:message key="salesinit.INDUSTRY"/>
                                </label>

                                <div class="form-group__content">
                                    <div class="form-control input-field listbox-wrapper" id="industryWrapper">

                                        <select name="industry" id="industry" class="select-wrapper gwt-ListBox"
                                        ${industryid != null ? 'disabled="disabled"' : ''}>

                                            <option value="-1">
                                                <fmt:message key="salesinit.pleaseSelect"/>
                                            </option>

                                            <c:forEach items="${industries}" var="industry">
                                                <option value="${industry.id}"
                                                    ${industry.id == industryid ? 'selected="selected"' : ''}>
                                                        ${industry.name}
                                                </option>
                                            </c:forEach>

                                        </select>

                                        <!-- Disabled bo‘lsa ham qiymat backendga ketishi uchun -->
                                        <c:if test="${industryid != null}">
                                            <input type="hidden" name="industry" value="${industryid}" />
                                        </c:if>

                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </fieldset>

                <fieldset>
                    <h4 class="fs-3"><fmt:message key="salesinit.companyAddress"> </fmt:message></h4>

                    <div class="grid-row">
                        <div class="col-6">
                            <div class="form-group">
                                <div class="form-group__content">
                                    <input type="text" class="form-control" placeholder="<fmt:message key="salesinit.street"> </fmt:message>"
                                           name="comp_addr_1" value="${address1}"/>
                                </div>
                            </div>
                        </div>
                        <div class="col-6">
                            <div class="form-group">
                                <div class="form-group__content">
                                    <input type="text" class="form-control" placeholder="<fmt:message key="salesinit.addressLine2"> </fmt:message>"
                                           name="comp_addr_2" value="${address2}"/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="grid-row">
                        <div class="col">
                            <div class="form-group">
                                <div class="form-group__content">
                                    <input type="text" class="form-control" placeholder="<fmt:message key="salesinit.city"> </fmt:message>"
                                           name="comp_addr_city" value="${city}"/>
                                </div>
                            </div>
                        </div>
                        <div class="col-4">
                            <div class="form-group">
                                <div class="form-group__content">
                                    <%--<input type="text" class="form-control" placeholder="State/Province" />--%>
                                    <div class="form-control input-field listbox-wrapper">
                                        <select class="select-wrapper gwt-ListBox" name="comp_addr_country"
                                                id="comp_addr_country" onchange="updateStates();">
                                            <option value="-1">Country</option>
                                            <c:forEach items="${countries}" var="country">
                                                <option value="${country.id}" ${country.id == countryid ? 'selected' : ''}>${country.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col" id="state-wrapper">
                            <div class="form-group">
                                <div class="form-group__content">
                                    <%--<input type="text" class="form-control" placeholder="State/Province" />--%>
                                    <%--<div class="form-control input-field " id="state-wrapper">class="select-wrapper gwt-ListBox" --%>
                                    <div class="input-field">
                                        <select class="form-control listbox-wrapper" id="comp_addr_state"
                                                name="comp_addr_state">
                                            <option value="-1"><fmt:message key="salesinit.state/province"> </fmt:message></option>
                                            <c:forEach items="${states}" var="state">
                                                <option value="${state.id}" ${state.id == stateid ? 'selected' : ''}>${state.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col">
                            <div class="form-group">
                                <div class="form-group__content">
                                    <input type="text" class="form-control" placeholder="<fmt:message key="salesinit.zip"> </fmt:message>"
                                           name="comp_addr_zip" value="${zip}"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </fieldset>

            </div>
        </div>

        <div class="features-list__main-footer">
            <div class="btns-group">
                <%--<button class="btn btn--default btn--outline"><span>Cancel</span></button>--%>
                <button class="btn btn--primary" onclick="return openSecondPart();"><span><fmt:message key="salesinit.save&continue"> </fmt:message></span>
                </button>
            </div>
            <div class="footnote">
                <svg class="icon--info">
                    <use href="/mainStyles/new-ui/icons/sprite__panels.svg#info"></use>
                </svg>
                <span><fmt:message key="salesinit.youCanAlwaysChangeYourPreferencesLaterInSettings"> </fmt:message></span>
            </div>
        </div>
    </main>

    <main id="secondpart">

        <div class="features-list__heading">
            <h2 class="features-list__title">
                <fmt:message key="salesinit.inviteUsers"> </fmt:message>
            </h2>
            <h3 class="features-list__sub-title">
                <fmt:message key="salesinit.giveUsSomeBasicDetails"> </fmt:message>
            </h3>
        </div>

        <div class="features-list__main-body">
            <div class="content-holder">
                <div id="inviteUsersCnt">
                    <div id="invite_0" class="form-row form-group">
                        <div class="col-3">
                            <label class="form-group__label">
                                <fmt:message key="salesinit.name"> </fmt:message>
                            </label>
                            <div class="form-group__content">
                                <input type="text" name="invited_user_name" class="form-control"
                                       placeholder="<fmt:message key="salesinit.TypeHere"></fmt:message>"/>
                            </div>
                        </div>

                        <div class="col-6">
                            <label class="form-group__label"><fmt:message key="salesinit.coworkersEmail"> </fmt:message></label>
                            <div class="form-group__content">
                                <input type="text" name="invited_user_email" class="form-control"
                                       placeholder="<fmt:message key="salesinit.TypeHere"></fmt:message>"/>
                            </div>
                        </div>

                        <div class="col-3">
                            <label class="form-group__label"><fmt:message key="salesinit.role"> </fmt:message></label>
                            <div class="form-group__content">
                                <div class="form-control input-field listbox-wrapper">
                                    <select name="invited_user_role" id="userrole_0" class="select-wrapper gwt-ListBox">
                                        <option value="0">Please select</option>
                                        <%--<option value="2">option</option>
                                        <option value="-1">Country</option>--%>
                                        <c:forEach items="${roles}" var="role">
                                            <option value="${role.id}">${role.name}</option><%-- ${role.id == roleid ? 'selected' : ''}--%>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>

                <button class="btn btn--lightgrey btn-block" onclick="return addInviteRow()"><fmt:message key="salesinit.AddRow"></fmt:message> </button>

            </div>
        </div>

        <div class="features-list__main-footer">
            <div class="grid-row">
                <div class="btns-group col">
                    <button class="btn btn--default btn--outline" onclick="return openFirstPart();"><span><fmt:message key="salesinit.Back"></fmt:message> </span>
                    </button>
                    <button class="btn btn--primary" onclick="return openThirdPart();"><span>
                        <fmt:message key="salesinit.save&close"> </fmt:message></span>
                    </button>
                </div>
                <div class="btns-group col-auto">
                    <button class="btn btn--default btn--outline" onclick="return openThirdPart();">
                        <span><fmt:message key="salesinit.skipSetup"> </fmt:message></span></button>
                </div>
            </div>
            <div class="footnote">
                <svg class="icon--info">
                    <use href="/mainStyles/new-ui/icons/sprite__panels.svg#info"></use>
                </svg>
                <span><fmt:message key="salesinit.youCanAlwaysChangeYourPreferencesLaterInSettings"> </fmt:message></span>
            </div>
        </div>
    </main>

    <main id="thirdpart">

        <div class="features-list__heading">
            <h2 class="features-list__title">
                <fmt:message key="salesinit.EmailSettings"></fmt:message>
            </h2>
            <h3 class="features-list__sub-title">
                <fmt:message key="salesinit.setupYourEmailAccountsWithSMTP"> </fmt:message>
            </h3>
        </div>

        <div class="features-list__main-body">
            <div class="content-holder">
                <h4 class="fs-3">
                    <fmt:message key="salesinit.addMailInboxes"> </fmt:message>
                </h4>

                <div class="email-settings">
                    <a href="#" onclick="return showAddEmail('gmail');">
                        <span class="email-settings__icon">
                            <img src="/mainStyles/images/logo_gmail.svg" alt="icon">
                        </span>
                        <span class="email-settings__caption">
                            Gmail
                        </span>
                    </a>

                    <a href="#" onclick="return showAddEmail('outlook');">
                        <span class="email-settings__icon">
                            <img src="/mainStyles/images/logo_outlook.svg" alt="icon">
                        </span>
                        <span class="email-settings__caption">
                            Outlook
                        </span>
                    </a>

                    <a href="#" onclick="return showAddEmail('yandex');">
                        <span class="email-settings__icon">
                            <img src="/mainStyles/images/logo_ymail.svg" alt="icon">
                        </span>
                        <span class="email-settings__caption">
                            Yandex Mail
                        </span>
                    </a>

                    <a href="#" onclick="return showAddEmail('');">
                        <span class="email-settings__icon">
                        <svg class="icon--settings"><use
                                href="/mainStyles/new-ui/icons/sprite__panels.svg#settings"></use></svg>
                        </span>
                        <span class="email-settings__caption">
                            <fmt:message key="salesinit.manualSetup"> </fmt:message>
                        </span>
                    </a>


                </div>
            </div>

        </div>

        <div class="features-list__main-footer">
            <div class="grid-row">
                <div class="btns-group col">
                    <button class="btn btn--default btn--outline" onclick="return openSecondPart();"><span><fmt:message key="salesinit.Back"></fmt:message></span>
                    </button>
                    <button class="btn btn--primary" onclick="$('form').submit();"><span><fmt:message key="salesinit.save&close"> </fmt:message></span>
                    </button>
                </div>
                <%--<div class="btns-group col-auto">
                    <button class="btn btn--default btn--outline"><span>Skip setup</span></button>
                </div>--%>
            </div>
            <div class="footnote">
                <svg class="icon--info">
                    <use href="/mainStyles/new-ui/icons/sprite__panels.svg#info"></use>
                </svg>
                <span><fmt:message key="salesinit.youCanAlwaysChangeYourPreferencesLaterInSettings"> </fmt:message></span>
            </div>
        </div>
    </main>

</form>


<div id="add-email-sidenav">
    <div class="kpi-sidenav-overlay" style="display: flex;"></div>
    <div tabindex="0" style="height: 100%;">
        <input type="text" tabindex="-1" aria-hidden="true"
               style="opacity: 0; height: 1px; width: 1px; z-index: -1; overflow: hidden; position: absolute;">
        <ul class="quick-add side-nav drawer right-aligned" id="98B357B0-DC11-45D3-BCBA-22DD9F0571A1" style="width: 900px; right: 0px;">
            <div class="sidenav-content" style="display: flex;">
                <div class="side-nav__heading">
                    <div class="side-nav__title"><h1><fmt:message key="salesinit.addEmailAccount"> </fmt:message></h1></div>
                </div>
                <div class="side-nav__body">
                    <div class="scroll-offset">
                        <div>
                            <div class="grid-row mb-3">
                                <div class="col-4">
                                    <a style="cursor: pointer;" onclick="return showAddEmail('gmail')"><span>↓</span><span><fmt:message key="salesinit.PrefillGmail"> </fmt:message></span></a>
                                </div>
                                <div class="col-4">
                                    <a style="cursor: pointer;" onclick="return showAddEmail('outlook')"><span>↓</span><span><fmt:message key="salesinit.PrefillOffice365"> </fmt:message></span></a>
                                </div>
                                <div class="col-4">
                                    <a style="cursor: pointer;"><span>↓</span><span><fmt:message key="salesinit.PrefillYandex"> </fmt:message></span></a> <%--ToDo static--%>
                                </div>
                            </div>

                            <div class="grid-row">
                                <div class="col-4">
                                    <div class="form-group">
                                        <label class="form-group__label"> <fmt:message key="salesinit.username"></fmt:message> </label>
                                        <div class="form-group__content">
                                            <input id="username" type="text" maxlength="255" class="form-control" />
                                        </div>
                                    </div>
                                </div>
                                <div class="col-4">
                                    <div class="form-group">
                                        <label class="form-group__label"> <fmt:message key="salesinit.emailAddress"></fmt:message> </label>
                                        <div class="form-group__content">
                                            <input id="emailaddress" type="text" maxlength="255" class="form-control" />
                                        </div>
                                    </div>
                                </div>
                                <div class="col-4">
                                    <div class="form-group">
                                        <label class="form-group__label"> <fmt:message key="salesinit.fromName"></fmt:message> </label>
                                        <div class="form-group__content">
                                            <input id="fromname" type="text" maxlength="255" class="form-control" />
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="grid-row">
                                <div class="col-4">
                                    <div class="form-group">
                                        <label class="form-group__label active"><fmt:message key="index.password"></fmt:message></label>
                                        <div class="form-group__content">
                                            <input id="password" type="password" maxlength="255" class="form-control" dir="ltr" />
                                        </div>
                                    </div>
                                </div>
                                <div class="col-4">
                                    <div class="form-group">
                                        <label class="form-group__label"> <fmt:message key="salesinit.confirmPassword"></fmt:message> </label>
                                        <div class="form-group__content">
                                            <input id="confirmpassword" type="password" maxlength="255" class="form-control" />
                                        </div>
                                    </div>
                                </div>
                                <div class="col-4">
                                    <div class="form-group">
                                        <label class="form-group__label"> <fmt:message key="salesinit.corporateEmail"></fmt:message></label>
                                        <div class="form-group__content switch">
                                            <label>
                                                <span class="switch__label--left"></span>
                                                <input type="checkbox" id="corporateemail">
                                                <span class="lever"></span>
                                                <span class="switch__label--right"></span>
                                            </label>
                                            <span class="material-label" style="margin-top: 16px;"></span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="grid-row">
                                <div class="col-4">
                                    <div class="form-group">
                                        <label class="form-group__label"> <fmt:message key="salesinit.incomingServerProtocol"></fmt:message> </label>
                                        <div class="form-group__content listbox-wrapper">
                                            <div class="form-control input-field listbox-wrapper">
                                                <select class="select-wrapper gwt-ListBox" id="imap" name="imap">
                                                    <option value="0">imap</option>
                                                    <option value="1">imaps</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-4">
                                    <div class="form-group">
                                        <label class="form-group__label"> <fmt:message key="salesinit.incomingServerHost"></fmt:message> </label>
                                        <div class="form-group__content">
                                            <input id="incominghost" type="text" maxlength="255" class="form-control" placeholder="e.g. imap.gmail.com" dir="ltr" />
                                        </div>
                                    </div>
                                </div>
                                <div class="col-4">
                                    <div class="form-group">
                                        <label class="form-group__label"> <fmt:message key="salesinit.incomingServerPort"></fmt:message> </label>
                                        <div class="form-group__content">
                                            <input id="incomingport" type="text" maxlength="255" class="form-control" dir="ltr" />
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="grid-row">
                                <div class="col-4">
                                    <div class="form-group">
                                        <label class="form-group__label"> <fmt:message key="salesinit.outgoingServerProtocal"></fmt:message> </label>
                                        <div class="form-group__content">
                                            <div class="form-control input-field listbox-wrapper">
                                                <select class="select-wrapper" id="smtp" name="smtp">
                                                    <option value="0">smtp</option>
                                                    <option value="1">smtps</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-4">
                                    <div class="form-group">
                                        <label class="form-group__label"> <fmt:message key="salesinit.outgoingServerHost"></fmt:message> </label>
                                        <div class="form-group__content">
                                            <input id="outgoinghost" type="text" maxlength="255" class="form-control" placeholder="e.g. smtp.gmail.com" />
                                        </div>
                                    </div>
                                </div>
                                <div class="col-4">
                                    <div class="form-group">
                                        <label class="form-group__label"> <fmt:message key="salesinit.outgoingServerPort"></fmt:message> </label>
                                        <div class="form-group__content">
                                            <input id="outgoingport" type="text" name="outgoing-server-port" maxlength="255" class="form-control" dir="ltr" />
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="grid-row">
                                <div class="col-3">
                                    <div class="form-group">
                                        <label class="form-group__label"> <fmt:message key="salesinit.performSMTPAuth"></fmt:message> </label>
                                        <div class="form-group__content switch">
                                            <label>
                                                <span class="switch__label--left"></span>
                                                <input type="checkbox" id="smtpauth" name="smtp-auth" checked="true">
                                                <span class="lever"></span>
                                                <span class="switch__label--right"></span>
                                            </label>
                                            <span class="material-label" style="margin-top: 16px;"></span>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-3">
                                    <div class="form-group">
                                        <label class="form-group__label"> <fmt:message key="salesinit.saveCopyToSentFolder"></fmt:message> </label>
                                        <div class="form-group__content switch">
                                            <label>
                                                <span class="switch__label--left"></span>
                                                <input id="savecopy" type="checkbox">
                                                <span class="lever"></span>
                                                <span class="switch__label--right"></span>
                                            </label>
                                            <span class="material-label" style="margin-top: 16px;"></span>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-3">
                                    <div class="form-group">
                                        <label class="form-group__label"> <fmt:message key="salesinit.defaultEmailAccount"></fmt:message> </label>
                                        <div class="form-group__content switch">
                                            <label>
                                                <span class="switch__label--left"></span>
                                                <input id="isdefaultemail" type="checkbox" checked="true">
                                                <span class="lever"></span>
                                                <span class="switch__label--right"></span>
                                            </label>
                                            <span class="material-label" style="margin-top: 16px;"></span>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-3">
                                    <div class="form-group">
                                        <label class="form-group__label"> <fmt:message key="salesinit.active"></fmt:message> </label>
                                        <div class="form-group__content switch">
                                            <label>
                                                <span class="switch__label--left"></span>
                                                <input type="checkbox" id="isactiveemailaccount">
                                                <span class="lever"></span>
                                                <span class="switch__label--right"></span>
                                            </label>
                                            <span class="material-label" style="margin-top: 16px;"></span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="side-nav__footer">
                    <button class="btn btn--primary" onclick="return testEmailConnection();"><span> <fmt:message key="salesinit.save"></fmt:message> </span></button>
                    <button class="btn btn--default" onclick="return hideAddEmail();"><span> <fmt:message key="salesinit.cancel"></fmt:message> </span></button>
                </div>
            </div>
        </ul>
    </div>
</div>
<!--SCRIPTS-->
<script>
    $("#secondpart").hide();//toggleClass('d-none');
    $("#thirdpart").hide();//toggleClass('d-none');
    $("#add-email-sidenav").hide();

    jQuery(document).ready(function () {
        $('select').material_select();
    });

    function openFirstPart() {
        $("#secondpart").hide();
        $("#orgdetails").addClass("status--current");
        $("#orgdetails").removeClass("status--passed");

        $("#invite").addClass("status--not-passed");
        $("#invite").removeClass("status--passed");
        $("#invite").removeClass("status--current");
        $("#firstpart").show();
        return false;
    }

    function openSecondPart() {

        $("#firstpart").hide();
        $("#thirdpart").hide();
        $("#orgdetails").removeClass("status--current");
        $("#email-settings").removeClass("status--current");
        $("#email-settings").addClass("status--not-passed");
        $("#orgdetails").addClass("status--passed");

        $("#invite").removeClass("status--passed");
        $("#invite").removeClass("status--not-passed");
        $("#invite").addClass("status--current");
        $("#secondpart").show();
        return false;

    }

    function openThirdPart() {

        /*if ($("select#conversion_date_year option").filter(":selected").val() == '-1' || $("select#conversion_date_month option").filter(":selected").val() == '-1') {
            // alert('Please set BOOKS START DATE.');
            if( $("select#conversion_date_year option").filter(":selected").val() == '-1' ) {
                $("#yearWrapper").addClass('x-form-invalid');
            }
            if($("select#conversion_date_month option").filter(":selected").val() == '-1') {
                $("#monthWrapper").addClass('x-form-invalid');
            }
            // Materialize.toast({html: '<i class=\'tick-toast__icon ficon--check\'></i>', classes:'tick-toast tick-toast--error'});
            Materialize.toast( 'Please set BOOKS START DATE.', 3000, 'tick-toast tick-toast--error' );
            return false;
        } else {

            $("#yearWrapper").removeClass('x-form-invalid');
            $("#monthWrapper").removeClass('x-form-invalid');*/

        $("#firstpart").hide();
        $("#secondpart").hide();
        $("#invite").removeClass("status--current");
        $("#invite").addClass("status--passed");

        $("#email-settings").removeClass("status--not-passed");
        $("#email-settings").addClass("status--current");
        $("#thirdpart").show();
        return false;
        // }
    }

    /*$('#comp_addr_state').on('contentChanged', function() {
        $(this).material_select();
    });*/

    function updateStates() {
        $.ajax({
            url: "/services/api/v2/countries/" + $("select#comp_addr_country option").filter(":selected").val() + "/states",
            type: 'GET',
            success: function (response) {
                /*alert(response.data.list.length);
                $.each(response.data.list, function (item) {
                    console.log(this.id );
                });*/
                if (response.data.list == 0) {
                    $('#state-wrapper').hide();
                } else {
                    $('#state-wrapper').show();
                    var $states = $("select[name='comp_addr_state']");
                    // $states.material_select("destroy");
                    $states.material_select('destroy');

                    /*$("#comp_addr_state option")*/
                    $states.find('option').each(function () {
                        $(this).remove();
                    });

                    /*
                    $states.find('option')
                        .remove()
                        .end()
                        .append('<option value="-1">Please Select</option>')
                        .val('whatever');*/

                    var newOpt = $("<option>").attr("value", "-1").text("State/Province");
                    $states.append(newOpt);
                    $.each(response.data.list, function (item) {
                        // console.log(this.id + " " + this.title);
                        // $("#comp_addr_state").append('<option value="option6">option6</option>');
                        // $("#comp_addr_state").append("<option value='item" + this.id + "'>" + this.title + "</option>").val(this.title);
                        var newOpt = $("<option>").attr("value", this.id).text(this.title);
                        $states.append(newOpt);
                        // append($("<option></option>").val().text());
                    });
                    /*$states.find('option')
                        .remove()
                        .end()
                        .append('<option></option>')
                        .val('-1').text("Please Select");*/

                    // $('select')
                    // $("#comp_addr_state").trigger('contentChanged');
                    $states.material_select('destroy');
                    $states.material_select();
                    //$states.closest('.input-field').children('span.caret').remove();
                    // $("select").material_select();

                }
            },
            error: function (error) {
                alert(error);
            },
            beforeSend: setHeader
        });

        function setHeader(xhr) {
            xhr.setRequestHeader('accessToken', '22cfd8ef-2678-47ea-b750-738c59615598');
            xhr.setRequestHeader('x-auth', readCookie("SESSION_ID"));
        }

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
    var incr = 0;

    function addInviteRow() {
        /*$("#myID").click(
            function () {*/
        incr++;
        var rowid = 'invite_' + incr;

        var newDiv = $("<div id=\"" + rowid + "\" class=\"form-row form-group\">\n" +
            "<div class=\"col-3\">" +
            "   <label class=\"form-group__label\">Name</label>" +
            "   <div class=\"form-group__content\">" +
            "       <input type=\"text\" name=\"invited_user_name\" class=\"form-control\" placeholder=\"Type here\" />" +
            "   </div>" +
            "</div>" +
            "<div class=\"col-6\">" +
            "   <label class=\"form-group__label\">Coworkers Email</label>" +
            "   <div class=\"form-group__content\">" +
            "       <input type=\"text\" name=\"invited_user_email\" class=\"form-control\" placeholder=\"Type here\" />" +
            "   </div>" +
            "</div>" +
            "<div class=\"col-3\">" +
            "   <label class=\"form-group__label\">Role</label>" +
            "   <div class=\"form-group__content\">" +
            "       <div class=\"form-control input-field listbox-wrapper\">" +
            "           <select name=\"invited_user_role\" id=\"userrole_" + incr + "\" class=\"select-wrapper gwt-ListBox\">" +
            "               <option value=\"0\">Please select</option>" +

            <c:forEach items="${roles}" var="role">
            "<option value=\"${role.id}\">${role.name}</option>" +
            </c:forEach>

            "           </select>" +
            "       </div>" +
            "   </div>" +
            "</div>" +
            "</div>");

        $("#inviteUsersCnt").append(newDiv);
        $('select').material_select();
        return false;
        /*}
    )*/
    }

    function removeInviteRow(rowId) {
        $(rowId).remove();
    }

    function showAddEmail(accounttype) {
        var username = '';
        var email = '';
        var fromname = '';
        var password = '';
        var confirmpassword = '';
        var corporateemail = false;
        var incomingserverprotocol = '';
        var incominserverhost = '';
        var incomingserverport = '';
        var outgoingserverprotocol = '';
        var outgoingserverhost = '';
        var outgoingserverport = '';
        var smtpauth = true;
        var savecopy = false;
        var defaultemail = true;
        var active = false;

        if (accounttype) {
            $("#imap").val('imaps').change();
            $("#smtp").val('smtps').change();
            /*$("select#imap option[value='1']").attr("selected", true);
            $("select#smtp option[value='1']").attr("selected", true);*/

            if (accounttype === 'gmail') {

                incominserverhost = 'imap.gmail.com';
                incomingserverport = '993';
                outgoingserverhost = 'smtp.gmail.com';
                outgoingserverport = '465';
            } else if (accounttype === 'outlook') {

                incominserverhost = 'outlook.office365.com';
                incomingserverport = '993';
                outgoingserverhost = 'smtp.office365.com';
                outgoingserverport = '587';
            } else if (accounttype === 'yandex') {

                incominserverhost = 'imap.yandex.com';
                incomingserverport = '993';
                outgoingserverhost = 'smtp.yandex.com';
                outgoingserverport = '465';
            }

            $("#incominghost").val(incominserverhost);
            $("#incomingport").val(incomingserverport);
            $("#outgoinghost").val(outgoingserverhost);
            $("#outgoingport").val(outgoingserverport);

        }
        $("#add-email-sidenav").show();
    }

    function hideAddEmail() {
        $("#add-email-sidenav").hide();
    }


    function testEmailConnection() {
        var data = {
            "active": $("#isactiveemailaccount").checked,
            "defaultEmail": $("#isdefaultemail").checked,
            "email": $("#emailaddress").val(),
            "companyEmail": $("#corporateemail").checked,
            "fromName": $("#fromname").val(),
            "gmail": $("#incominghost").val().indexOf("gmail")!==-1 || $("#outgoinghost").val().indexOf("gmail")!==-1,
            "imapHost": $("#incominghost").val(),
            "imapPort": $("#incomingport").val(),
            "imapProtocol": $("select#imap option").filter(":selected").text(),
            "password": $("#password").val(),
            "saveCopyToSentFolder": $("#savecopy").checked,
            "smtpConnectionNotAuth": $("#smtpauth").checked,
            "smtpHost": $("#outgoinghost").val(),
            "smtpPort": $("#outgoingport").val(),
            "smtpProtocol": $("select#smtp option").filter(":selected").text(),
            "userName": $("#username").val()
        };

        $.ajax({
            url: "/services/api/v2/test_connection",
            type: 'POST',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify( data ),
            success: function (response) {
                console.log(JSON.stringify(response));

                // if (response.data.list == 0) {
                    saveEmailAccount();
                /*} else {

                }*/
            },
            error: function (error) {
                // console.log(JSON.stringify(error.responseJSON.error));
                if(error.responseJSON.error) {
                    Materialize.toast(error.responseJSON.error.user_msg, 3000, 'tick-toast tick-toast--error');
                }
                // alert(error);
            },
            beforeSend: setHeader
        });

        function setHeader(xhr) {
            xhr.setRequestHeader('accessToken', '22cfd8ef-2678-47ea-b750-738c59615598');
            xhr.setRequestHeader('x-auth', readCookie("SESSION_ID"));
        }

    }

    function saveEmailAccount() {
        var data = {
            "active": $("#isactiveemailaccount").checked,
            "defaultEmail": $("#isdefaultemail").checked,
            "email": $("#emailaddress").val(),
            "companyEmail": $("#corporateemail").checked,
            "fromName": $("#fromname").val(),
            "gmail": $("#incominghost").val().indexOf("gmail")!==-1 || $("#outgoinghost").val().indexOf("gmail")!==-1,
            "imapHost": $("#incominghost").val(),
            "imapPort": $("#incomingport").val(),
            "imapProtocol": $("select#imap option").filter(":selected").text(),
            "password": $("#password").val(),
            "saveCopyToSentFolder": $("#savecopy").checked,
            "smtpConnectionNotAuth": $("#smtpauth").checked,
            "smtpHost": $("#outgoinghost").val(),
            "smtpPort": $("#outgoingport").val(),
            "smtpProtocol": $("select#smtp option").filter(":selected").text(),
            "userName": $("#username").val()
        };

        $.ajax({
            url: "/services/api/v2/email_account",
            type: 'POST',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify( data ),
            success: function (response) {
                console.log(JSON.stringify(response));
                $("#add-email-sidenav").hide();
                Materialize.toast(error.responseJSON.error.user_msg, 3000, 'tick-toast');
            },
            error: function (error) {
                // console.log(JSON.stringify(error.responseJSON.error));
                if(error.responseJSON.error) {
                    Materialize.toast(error.responseJSON.error.user_msg, 3000, 'tick-toast tick-toast--error');
                }
            },
            beforeSend: setHeader
        });

        function setHeader(xhr) {
            xhr.setRequestHeader('accessToken', '22cfd8ef-2678-47ea-b750-738c59615598');
            xhr.setRequestHeader('x-auth', readCookie("SESSION_ID"));
        }

    }
</script>
</body>

</html>