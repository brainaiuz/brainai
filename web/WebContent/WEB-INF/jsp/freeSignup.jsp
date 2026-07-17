<%--
  Created by IntelliJ IDEA.
  User: Babayev Xushnud
  Date: Feb 17, 2010
  Time: 6:04:14 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<% String hostName = (request.getAttribute("hostName") != null) ? request.getAttribute("hostName").toString() : request.getServerName(); %>
<tiles:insertDefinition name="frontEndLayoutNew">

<tiles:putAttribute name="title">
    <fmt:message key="freeTrial.title"/>
</tiles:putAttribute>

<tiles:putAttribute name="style">

    <meta http-equiv="X-UA-Compatible" content="IE=8"/>
    <script type="text/javascript" src="/jsscript/landing.js"></script>
    <script type="text/javascript" src="/jsscript/ajax.js"></script>
    <c:if test="${!fn:containsIgnoreCase(hostName,'alfursanrecruitment.ae') && !fn:containsIgnoreCase(hostName,'alfursanrecruitment.ae') && !fn:containsIgnoreCase(hostName,'aws.alkawader.com')
                                && !fn:containsIgnoreCase(hostName,'genesis-gifts.com') && !fn:containsIgnoreCase(hostName,'genesis-gifts.com') && !fn:containsIgnoreCase(hostName,'activira.com') && !fn:containsIgnoreCase(hostName,'passionerp.com')
                                && !fn:containsIgnoreCase(hostName,'vipworkspace') && !fn:containsIgnoreCase(hostName,'tjilo')
                                 && !fn:containsIgnoreCase(hostName,'1erp.sa') && !fn:containsIgnoreCase(hostName,'Basilurarabia.com')
                                  && !fn:containsIgnoreCase(hostName,'ebmconsultant.com')
                                  && !fn:containsIgnoreCase(hostName,'kpi.developmentlogix.com')
                                  && !fn:containsIgnoreCase(hostName,'upshott.com')}">
        <link rel="stylesheet" href="/customisation/preprod.kpi.com/bootstrap.min.css">
        <link href="/customisation/preprod.kpi.com/customize.css" rel="stylesheet">
        <link href="/customisation/preprod.kpi.com/font-awesome.min.css" rel="stylesheet">
        <script src="/customisation/preprod.kpi.com/scripts/bootstrap.min.js" type="javascript"></script>
    </c:if>

    <script>
        $(document).ready(function () {

            var COOKIE_NAME = 'own_cookie';
            var val = $.cookie(COOKIE_NAME);
            if (val == null) {
                $("#cover").css("font-size", "13px");
                $("#font-size-all option:nth-child(2)").attr("selected", "selected");
            }
            else {
                $("#cover").css("font-size", val + "px");
                switch (val) {
                    case "11":
                        $("#font-size-all option:nth-child(1)").attr("selected", "selected");
                        break;
                    case "13":
                        $("#font-size-all option:nth-child(2)").attr("selected", "selected");
                        break;
                    case "15":
                        $("#font-size-all option:nth-child(3)").attr("selected", "selected");
                        break;
                    default:
                        $("#font-size-all option:nth-child(2)").attr("selected", "selected");
                        break;
                }
            }

            $("#font-size-all").change(function () {
                var obj = $("#font-size-all").val();

                $("#cover").css("font-size", obj + "px");
                $.cookie(COOKIE_NAME, null);
                $.cookie(COOKIE_NAME, obj, { path: '/', expires: 7 });
            });
        });
    </script>
    <script type="text/javascript">
        hs.graphicsDir = '/sites/all/themes/wft/highslide/graphics/';
        hs.align = 'center';
        hs.transitions = ['expand', 'crossfade'];
        hs.outlineType = 'rounded-white';
        hs.fadeInOut = true;
        hs.numberPosition = 'caption';
        hs.dimmingOpacity = 0.75;

        if (hs.addSlideshow) hs.addSlideshow({
            //slideshowGroup: 'group1',
            interval: 5000,
            repeat: false,
            useControls: true,
            fixedControls: 'fit',
            overlayOptions: {
                opacity: .75,
                position: 'bottom center',
                hideOnMouseOut: true
            }
        });
        var isInIFrame = (window.location != window.parent.location) ? true : false;
        if (isInIFrame) {
            parent.location = document.location;
        }
    </script>

    <meta name="google-site-verification" content="jjw1ZnV5AryaQZs-KB64fCH3EpvkT94GKJAoFvLFzbU"/>
    <meta name="msvalidate.01" content="358DD608AB06D6ED2E842AF53CAFFB9E"/>
    <META name="y_key" content="d766be7156d8deef">
    <SCRIPT type=text/javascript src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.4/jquery.min.js"></SCRIPT>


</tiles:putAttribute>

<tiles:putAttribute name="body">

<c:if test="${fn:containsIgnoreCase(hostName,'aws.alfursanrecruitment.ae') || fn:containsIgnoreCase(hostName,'login.alfursanrecruitment.ae') || fn:containsIgnoreCase(hostName,'alkawader.com')
                                || fn:containsIgnoreCase(hostName,'app.genesis-gifts.com') || fn:containsIgnoreCase(hostName,'aws.genesis-gifts.com') || fn:containsIgnoreCase(hostName,'activira.com') || fn:containsIgnoreCase(hostName,'passionerp.com')
                                || fn:containsIgnoreCase(hostName,'vipworkspace') || fn:containsIgnoreCase(hostName,'tjilo')
                                 || fn:containsIgnoreCase(hostName,'1erp.sa') || fn:containsIgnoreCase(hostName,'Basilurarabia.com')
                                  || fn:containsIgnoreCase(hostName,'ebmconsultant.com')
                                  || fn:containsIgnoreCase(hostName,'kpi.developmentlogix.com')
                                  || fn:containsIgnoreCase(hostName,'upshott.com')}">
<input type="hidden" id="isukclient" value="${isukclient}"/>
<iframe id="__gwt_historyFrame" style="width:0;height:0;border:0"></iframe>

<!-- BEGIN contentPlace -->
<div id="contentPlace" class="clear-block">

<!-- Start MAIN -->
<div id="main">

<div id="trailPage">

<form:form id="appTrialForm" method="post" commandName="newCompany" action="/signup/freeSignup.html">

    <!--begin #main-->
    <%--<div id="main">--%>

    <%--<ul class="breadCrump">--%>
    <%--<li><a href="http://${helpHost}/">kpi.com</a></li>--%>
    <%--<li>› Trial</li>--%>
    <%--</ul>--%>

    <section>
        <h1 style="margin:0 0 13px" class="title c">
            <fmt:message key="signup.try"/> ${productName} <fmt:message key="signup.forFree"/></h1>

        <h2 class="sub-title c"><fmt:message key="signup.freeTrialPay"/></h2>

        <h3 class="sub-title c"><fmt:message key="signup.SignUpInTwoMinutes"/></h3>

        <div class="sdbr-1 right">
            <h2><fmt:message key="signup.benefits"/> :</h2>
            <ul class="tickList">
                <li>
                    <fmt:message key="signup.PM"/>
                </li>
                <li>
                    <fmt:message key="signup.accessAnywhereAnytime"/>
                </li>
                <li>
                    <fmt:message key="signup.automaticBackups"/>
                </li>
                <li>
                    <fmt:message key="signup.easilyExportYourData"/>
                </li>
                <li>
                    <fmt:message key="signup.mobileApplications"/>
                </li>
                <li>
                    <fmt:message key="signup.allData"/>
                </li>
                <li>
                    <fmt:message key="signup.trainingSupport"/>
                </li>
                <li>
                    <fmt:message key="signup.additionalStorage"/>
                </li>
                <li>
                    <fmt:message key="signup.multipleWarehouse"/>
                </li>
            </ul>
            <c:set var="hostName" scope="session" value="${hostName}"/>
            <div class="c">
                <c:choose>
                    <c:when test="${not fn:containsIgnoreCase('1erpsdsdsd', '1erp')}">
                        <a target="_blank" id="special" class="btn-1 mDownload" href="http://helpuserguides.s3.amazonaws.com/${productNameLower}/special.pdf"><em></em>
                            <span><fmt:message key="signup.getMoreeBenefits"/></span>
                        </a>
                    </c:when>
                    <c:otherwise>
                        <a id="special" class="btn-1 mDownload"><em></em>
                            <span><fmt:message key="signup.getMoreeBenefits"/></span>
                        </a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        <div class="overhide">
            <div class="trialFormFull">
                <div class="BoxIn">

                    <label>
                        <form:hidden path="utm_campaign" value="${param.utm_campaign}"/>
                        <form:hidden path="utm_source" value="${param.utm_source}"/>
                        <form:hidden path="utm_medium" value="${param.utm_medium}"/>
                        <form:hidden path="utm_keyword" value="${param.utm_keyword}"/>
                        <form:hidden path="redirected" value="${param.redirected}"/>
                        <span><fmt:message key="signup.firstName"/> * </span>
                        <form:input path="adminFName"/>
                        <form:errors path="adminFName" cssClass="error"/>
                    </label>

                    <label>
                        <span><fmt:message key="signup.lastName"/> *</span>
                        <form:input path="adminLName"/>
                        <form:errors path="adminLName" cssClass="error"/>
                    </label>

                    <div class="clearBox"></div>

                    <label>
                        <span><fmt:message key="signup.email"/> *</span>
                        <form:input path="adminEmail" cssClass="form-text required" id="edit-submitted-e-mail"
                                    readonly="${fromFederatedLogin}"
                                    onblur="sendRequest('GET','/signup/handleAjaxRequest?adminEmail='+this.value,this.value)"/>
                        <span id="emailValidate" class="error"><form:errors path="adminEmail" cssClass="error"/></span>
                    </label>
                    <label>
                        <span><fmt:message key="signup.companyName"/> *</span>
                        <form:input path="name"/>
                        <form:errors path="name" cssClass="error"/>
                    </label>

                    <div class="clearBox"></div>

                    <label>
                        <span><fmt:message key="signup.phone"/> *</span>
                        <form:input path="phone" onkeypress="return phoneValidation(event)"/>
                        <form:errors path="phone" cssClass="error"/>
                    </label>

                    <c:if test="${!fn:containsIgnoreCase(hostName,'1erp.sa')}">
                    <label>
                        <span><fmt:message key="signup.country"/> *</span>
                        <form:select path="countryID">
                            <form:option value="">
                                <fmt:message key="signup.pleaseSelect"/>
                            </form:option>
                            <form:options items="${countrys}" itemLabel="name" itemValue="objectID"/>
                        </form:select>
                        <form:errors path="countryID" cssClass="error"/>
                    </label>
                    </c:if>

                    <c:if test="${fn:containsIgnoreCase(hostName,'1erp.sa')}">
                        <label>
                            <span><fmt:message key="signup.country"/> *</span>
                            <form:select path="countryID" onchange="getStateWithCountryId(this.value)">
                                <c:forEach var="countryItem" items="${countrys}">
                                    <c:choose>
                                        <c:when test="${'Saudi Arabia' == countryItem.name}">
                                            <option value="${countryItem.objectID}"
                                                    selected="selected">${countryItem.name}</option>
                                        </c:when>
                                        <c:otherwise>
                                            <form:option
                                                    value="${countryItem.objectID}">${countryItem.name}</form:option>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </form:select>
                            <form:errors path="countryID" cssClass="error"/>
                        </label>

                        <script type="text/javascript">
                            $(document).ready(function () {
                                var country = jQuery('#countryID option:selected').text();
                                if (country && country == 'Saudi Arabia') {
                                    jQuery("#edit-submitted-state-wrapper").show();
                                } else {
                                    jQuery("#edit-submitted-state-wrapper").hide();
                                }
                            });
                        </script>

                        <script type="text/javascript">
                            function getStateWithCountryId(countryID) {
                                if (countryID) {
                                    var country = jQuery('#countryID option:selected').text();
                                    if (country && country == 'Saudi Arabia') {
                                        jQuery("#edit-submitted-state-wrapper").show();
                                    } else {
                                        jQuery("#edit-submitted-state-wrapper").hide();
                                    }
                                }
                            }
                        </script>

                        <label id="edit-submitted-state-wrapper">
                            <span><fmt:message key="signup.province"/> </span>
                            <form:select path="stateID">
                                <form:option value=""> <fmt:message key="signup.pleaseSelect"/></form:option>
                                <form:options items="${regions}" itemLabel="name" itemValue="objectID"/>
                            </form:select>
                        </label>
                    </c:if>

                    <div class="clearBox"></div>

                    <label>
                        <span><fmt:message key="signup.promutionalCode"/></span>
                        <em class="note"><fmt:message key="signup.ifapplicable"/> </em>
                        <input value="" name="companySignedUpFrom" id="freeSignupPromutionalCode"/>
                    </label>
                        <%--Locale Selector--%>
                    <label class="form-item odd" title="<fmt:message key="signup.languageDisclaimer"/>">
                                            <span><fmt:message key="signup.language"/>
                                            </span>
                        <em class="note"> <fmt:message key="signup.DefaultlanguagefortheSystem"/> </em>
                        <form:select id="localeSelector" path="locale" cssClass="form-text required">
                            <c:forEach var="localeItem" items="${locales}">
                                <c:choose>
                                    <c:when test="${pageContext.request.locale.language == localeItem.description}">
                                        <option value="${localeItem.description}" selected="selected">${localeItem.name}</option>
                                    </c:when>
                                    <c:otherwise>
                                        <form:option value="${localeItem.description}">${localeItem.name}</form:option>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </form:select>
                            <%--<input type="text" class="form-text required" value="" id="edit-submitted-country" name="submitted[phone]" />--%>
                        <div style="margin-left: 32%;font-size: 10px;"><form:errors path="locale"
                                                                                    cssClass="error"/></div>
                    </label>

                    <div class="clearBox"></div>

                    <div for="terms" id="agree" style="width: 100% !important;">
                        <div style="height: 25px;">
                            <fmt:message key="signup.iAgreeWitThe" var="terms"/>
                            <form:checkbox path="agreeWithCondition" id="terms" cssStyle="width: 40px !important;float: left" label="${terms}"/>


                            <a style="float:left;" href="//${helpHost}/content/terms-of-use" target="_blank">
                                <fmt:message key="signup.termsAndConditions"/>
                            </a>
                        </div>

                        <div style="text-transform:uppercase; height: 20px; "><form:errors path="agreeWithCondition" cssClass="error"/></div>
                    </div>

                    <div class="clearBox"></div>
                    <center>
                        <div id="recaptcha"></div>
                        <div style="text-transform:uppercase; height: 20px; ">${captchaError}</div>
                    </center>
                                        <span class="submitCover">
                                            <input class="btn-1" type="submit" value="<fmt:message key="signup.startFreeTrial"/>"/>
                                        </span>

                    <div class="clearBox"></div>
                </div>
            </div>

            <input type="hidden" name="action" value="signUp"/>
        </div>

        <div class="clearBox"></div>

    </section>

    <div class="clearBox"></div>
    <%--</div>--%>
    <!--END #main-->

</form:form>
<script type="text/javascript">
    jQuery("#appTrialForm").submit(function (event) {
        var firstName = jQuery("#adminFName").val();
        var secondName = jQuery("#adminLName").val();
        var email = jQuery("#edit-submitted-e-mail").val();
        var company = jQuery("#name").val();
        var phone = jQuery("#phone").val();
        var countryId = jQuery("#countryID").val();
        var country = jQuery('#countryID option:selected').text();
        var promotionalCode = jQuery("#freeSignupPromutionalCode").val();
        var languageId = jQuery("#localeSelector").val();
        var language = jQuery('#localeSelector option:selected').text();
        var checkItem = jQuery("#terms").val();
        if (firstName != "" && secondName != "" && phone != "" && email != "" && company != ""
                && countryId != "" && languageId != "" && checkItem != "" && checkItem == "true") {
            ga('send', 'event', 'Signup from www.kpi.com ', 'Free trial signup - any page',
                    'First Name=' + firstName + ', Last Name=' + secondName + ', Phone=' +
                            phone + ', E-mail=' + email + ', Company=' + company + ', Country=' +
                            country + ', Promotional Code=' + promotionalCode + ', Language=' + language);
        }
    });
</script>

</div>

</div>
<!--End #main -->

</div>
<!-- END contentPlace -->
</c:if>

<c:if test="${!fn:containsIgnoreCase(hostName,'aws.alfursanrecruitment.ae') && !fn:containsIgnoreCase(hostName,'login.alfursanrecruitment.ae') && !fn:containsIgnoreCase(hostName,'alkawader.com')
                                && !fn:containsIgnoreCase(hostName,'app.genesis-gifts.com') && !fn:containsIgnoreCase(hostName,'aws.genesis-gifts.com')  && !fn:containsIgnoreCase(hostName,'activira.com') && !fn:containsIgnoreCase(hostName,'passionerp.com')
                                && !fn:containsIgnoreCase(hostName,'vipworkspace') && !fn:containsIgnoreCase(hostName,'tjilo')
                                && !fn:containsIgnoreCase(hostName,'1erp.sa') && !fn:containsIgnoreCase(hostName,'Basilurarabia.com')
                                && !fn:containsIgnoreCase(hostName,'ebmconsultant.com')
                                && !fn:containsIgnoreCase(hostName,'kpi.developmentlogix.com')
                                && !fn:containsIgnoreCase(hostName,'upshott.com')}">
<input type="hidden" id="isukclient" value="${isukclient}"/>
<iframe id="__gwt_historyFrame" style="width:0;height:0;border:0"></iframe>

<!-- BEGIN contentPlace -->
<div id="contentPlace" class="clear-block">

<!-- Start MAIN -->
<form:form id="appTrialForm" method="post" commandName="newCompany" action="/signup/freeSignup.html">

<!--begin #main-->
<%--<div id="main">--%>

<%--<ul class="breadCrump">--%>
<%--<li><a href="http://${helpHost}/">kpi.com</a></li>--%>
<%--<li>› Trial</li>--%>
<%--</ul>--%>


<section id="mainContent" class="section-signUP">

    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <h4 class="text-center color-red margin-bottom15">Start your free account now and get instant access to all features.</h4>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 col-md-10 col-md-offset-1">
                <div class="row">
                    <div class="col-md-6 col-xs-12">
                        <div class="form-group">
                            <label for="adminFName" class="control-label">
                                <form:hidden path="utm_campaign" value="${param.utm_campaign}"/>
                                <form:hidden path="utm_source" value="${param.utm_source}"/>
                                <form:hidden path="utm_medium" value="${param.utm_medium}"/>
                                <form:hidden path="utm_keyword" value="${param.keyword}"/>
                                <form:hidden path="redirected" value="${param.redirected}"/>
                                <span><fmt:message key="signup.firstName"/> * </span>
                                <form:errors path="adminFName" cssClass="error"/>
                            </label>
                            <form:input path="adminFName" cssClass="form-control"/>
                        </div>
                    </div>
                    <div class="col-md-6 col-xs-12">
                        <div class="form-group">
                            <label for="lname" class="control-label">
                                <span><fmt:message key="signup.lastName"/> *</span>
                                <form:errors path="adminLName" cssClass="error"/>
                            </label>
                            <form:input path="adminLName" cssClass="form-control"/>
                        </div>
                    </div>
                    <div class="col-md-6 col-xs-12">
                        <div class="form-group">
                            <label for="edit-submitted-e-mail" class="control-label">
                                <span><fmt:message key="signup.email"/> *</span>
                                <span id="emailValidate" class="error"><form:errors path="adminEmail" cssClass="error"/></span>
                            </label>
                            <form:input path="adminEmail" cssClass="form-text required" id="edit-submitted-e-mail"
                                        readonly="${fromFederatedLogin}"
                                        onblur="sendRequest('GET','/signup/handleAjaxRequest?adminEmail='+this.value,this.value)"/>
                        </div>
                    </div>
                    <div class="col-md-6 col-xs-12">
                        <div class="form-group">
                            <label for="name" class="control-label">
                                <span><fmt:message key="signup.companyName"/> *</span>
                                <form:errors path="name" cssClass="error"/>
                            </label>
                            <form:input path="name" cssClass="form-control"/>
                        </div>
                    </div>
                    <div class="col-md-6 col-xs-12">
                        <div class="form-group">
                            <label for="phone" class="control-label">
                                <span><fmt:message key="signup.phone"/> *</span>
                                <form:errors path="phone" cssClass="error"/>
                            </label>
                            <form:input cssClass="form-control" path="phone" onkeypress="return phoneValidation(event)"/>
                        </div>
                    </div>
                    <div class="col-md-6 col-xs-12">
                        <div class="form-group">
                            <label for="countryID" class="control-label">
                                <span><fmt:message key="signup.country"/> *</span>
                                <form:errors path="countryID" cssClass="error"/>
                            </label>
                            <form:select path="countryID" cssClass="form-control">
                                <form:option value="">
                                    <fmt:message key="signup.pleaseSelect"/>
                                </form:option>
                                <form:options items="${countrys}" itemLabel="name" itemValue="objectID"/>
                            </form:select>
                        </div>
                    </div>
                    <div class="col-md-6 col-xs-12">
                        <div class="form-group">
                            <label for="promutionalCode" class="control-label">
                                <span><fmt:message key="signup.promutionalCode"/></span>
                                <em class="note"><fmt:message key="signup.ifapplicable"/> </em>
                            </label>
                            <form:input value="" path="companySignedUpFrom" id="freeSignupPromutionalCode" cssClass="form-control"/>
                        </div>
                    </div>
                    <div class="col-md-6 col-xs-12">
                        <div class="form-group">
                            <label for="localeSelector form-item odd" class="control-label" title="<fmt:message key="signup.languageDisclaimer"/>">
                                                        <span><fmt:message key="signup.language"/>
                                                        </span>
                                <em class="note"> <fmt:message key="signup.DefaultlanguagefortheSystem"/> </em>
                                    <%--<input type="text" class="form-text required" value="" id="edit-submitted-country" name="submitted[phone]" />--%>
                                <div style="margin-left: 32%;font-size: 10px;"><form:errors path="locale"
                                                                                            cssClass="error"/></div>
                            </label>
                            <form:select id="localeSelector" path="locale" cssClass="form-text required">
                                <c:forEach var="localeItem" items="${locales}">
                                    <c:choose>
                                        <c:when test="${pageContext.request.locale.language == localeItem.description}">
                                            <option value="${localeItem.description}" selected="selected">${localeItem.name}</option>
                                        </c:when>
                                        <c:otherwise>
                                            <form:option value="${localeItem.description}">${localeItem.name}</form:option>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </form:select>
                        </div>
                    </div>
                    <div class="col-xs-12">
                        <div class="form-group">
                            <div class="checkbox">
                                <label>
                                    <fmt:message key="signup.iAgreeWitThe" var="terms"/>
                                </label>
                                <form:checkbox path="agreeWithCondition" id="terms" label="${terms}"/>
                                <a href="//${helpHost}/content/terms-of-use" target="_blank">
                                    <fmt:message key="signup.termsAndConditions"/>
                                </a>
                            </div>
                        </div>
                        <div style="text-transform:uppercase; height: 20px; "><form:errors path="agreeWithCondition" cssClass="error"/></div>

                        <div class="clearBox"></div>
                        <div id="recaptcha"></div>
                        <center>
                            <div style="text-transform:uppercase; height: 20px; ">${captchaError}</div>
                        </center>

                        <div class="col-xs-12 text-center">
                            <input class="btn btn-primary btn-submit" type="submit" value="<fmt:message key="signup.start"/>"/>
                            <span class="below-button"><fmt:message key="signup.cancelanytime"/></span>
                        </div>
                    </div>
                </div>

                <input type="hidden" name="action" value="signUp"/>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 col-md-4 col-md-offset-4 text-center">
                <h4 class="bg-line text-center margin-top10"><label>OR</label></h4>
                    <%--<a href="javascript:" onclick="fbRedirect()" class="btn btn-default btn-socian-sign-up"><i class="fa fa-facebook">--%>
                    <%--&lt;%&ndash;<div class="fb-login-button" data-max-rows="1" data-label="Facebook" data-size="large" data-show-faces="false" data-auto-logout-link="false" onlogin="fbRedirect()" data-scope="basic_info, read_stream, email"></div>&ndash;%&gt;--%>
                    <%--</i></a>--%>
                <div class="fb-login-button" data-max-rows="1"
                     data-label="Facebook" data-size="large"
                     data-show-faces="false" data-auto-logout-link="false"
                     onlogin="fbRedirect()"
                     data-scope="basic_info, read_stream, email"></div>
                <a href="/check?ID_PROVIDER=https://me.yahoo.com" class="btn btn-default btn-socian-sign-up"><i class="fa fa-yahoo"></i></a>
                <a href="/check" class="btn btn-default btn-socian-sign-up"><i class="fa fa-google"></i></a>
                <a href="/enterGoogleDomain.html" class="btn btn-default btn-socian-sign-up"><i class="fa fa-google-plus"></i></a>
                <a href="/sendtolinkedinauthorization" class="btn btn-default btn-socian-sign-up"><i class="fa fa-linkedin"></i></a>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 col-md-10 col-md-offset-1">
                <hr/>
            </div>
        </div>
    </div>
</section>

<section id="sectionBenefits" class="section-benefits">
    <div class="container">
        <div class="row">
            <div class="col-xs-12 col-md-10 col-md-offset-1">
                <h2 class="color-blue text-center text-uppercase"><fmt:message key="signup.benefits"/></h2>

                <div class="col-xs-12 col-md-4">
                    <ul class="benefits-list">
                        <li>
                            <fmt:message key="signup.PM"/>
                        </li>
                        <li>
                            <fmt:message key="signup.accessAnywhereAnytime"/>
                        </li>
                        <li>
                            <fmt:message key="signup.automaticBackups"/>
                        </li>
                    </ul>
                </div>
                <div class="col-xs-12 col-md-4">
                    <ul class="benefits-list">
                        <li>
                            <fmt:message key="signup.easilyExportYourData"/>
                        </li>
                        <li>
                            <fmt:message key="signup.mobileApplications"/>
                        </li>
                        <li>
                            <fmt:message key="signup.allData"/>
                        </li>
                    </ul>
                </div>
                <div class="col-xs-12 col-md-4">
                    <ul class="benefits-list">
                        <li>
                            <fmt:message key="signup.trainingSupport"/>
                        </li>
                        <li>
                            <fmt:message key="signup.additionalStorage"/>
                        </li>
                        <li>
                            <fmt:message key="signup.multipleWarehouse"/>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
    <script>(function (d, s, id) {
        var js, fjs = d.getElementsByTagName(s)[0];
        if (d.getElementById(id)) return;
        js = d.createElement(s);
        js.id = id;
        js.src = "//connect.facebook.net/en_US/all.js#xfbml=1&appId=262235000465385";
        fjs.parentNode.insertBefore(js, fjs);
    }(document, 'script', 'facebook-jssdk'));
    </script>
</section>
</form:form>
<script type="text/javascript">
    jQuery("#appTrialForm").submit(function (event) {
        var firstName = jQuery("#adminFName").val();
        var secondName = jQuery("#adminLName").val();
        var email = jQuery("#edit-submitted-e-mail").val();
        var company = jQuery("#name").val();
        var phone = jQuery("#phone").val();
        var countryId = jQuery("#countryID").val();
        var country = jQuery('#countryID option:selected').text();
        var promotionalCode = jQuery("#freeSignupPromutionalCode").val();
        var languageId = jQuery("#localeSelector").val();
        var language = jQuery('#localeSelector option:selected').text();
        var checkItem = jQuery("#terms").val();
        if (firstName != "" && secondName != "" && phone != "" && email != "" && company != ""
                && countryId != "" && languageId != "" && checkItem != "" && checkItem == "true") {
            ga('send', 'event', 'Signup from www.kpi.com ', 'Free trial signup - any page',
                    'First Name=' + firstName + ', Last Name=' + secondName + ', Phone=' +
                            phone + ', E-mail=' + email + ', Company=' + company + ', Country=' +
                            country + ', Promotional Code=' + promotionalCode + ', Language=' + language);
        }
    });
</script>
<script type="text/javascript">
    function fbRedirect() {
        FB.getLoginStatus(function (response) {
            if (response.status === 'connected') {
                // the user is logged in and has authenticated your
                // app, and response.authResponse supplies
                // the user's ID, a valid access token, a signed
                // request, and the time the access token
                // and signed request each expire
                var uid = response.authResponse.userID;
                var accessToken = response.authResponse.accessToken;
                window.location = 'facebookLogin?access_token=' + accessToken + '&uid=' + uid;
            } else if (response.status === 'not_authorized') {
                // the user is logged in to Facebook,
                // but has not authenticated your app
            } else {
                // the user isn't logged in to Facebook.
            }
        });
    }
</script>
    <%--<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->--%>
<script src="/customisation/preprod.kpi.com/scripts/jquery.min.js"></script>
    <%--<!-- Include all compiled plugins (below), or include individual files as needed -->--%>
<script src="/customisation/preprod.kpi.com/scripts/bootstrap.min.js"></script>
    <%--<script type="text/javascript">--%>
    <%--var recaptchaCallback = function () {--%>
    <%--//console.log('recaptcha is ready'); // not showing--%>
    <%--grecaptcha.render("recaptcha", {--%>
    <%--sitekey: '6LfkCBkTAAAAAEBJBmYjxEEszN2FQDJt40k9_DXb',--%>
    <%--callback: function () {--%>
    <%--console.log('recaptcha callback');--%>
    <%--}--%>
    <%--});--%>
    <%--}--%>
    <%--</script>--%>
    <%--<script src="https://www.google.com/recaptcha/api.js?onload=recaptchaCallback&amp;render=explicit" async="" defer=""></script>--%>
</div>
<!--End #main -->
<!-- END contentPlace -->
</c:if>
</tiles:putAttribute>

<%--<tiles:putAttribute name="script">--%>
<%--<script type="text/javascript">--%>
<%--if (document.getElementById("adminFName")) document.getElementById("adminFName").focus(); //Initially focus on firstname textbox--%>
<%--<!--//--><![CDATA[//><!----%>
<%--var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");--%>
<%--document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));--%>
<%--//--><!] ]>--%>

<%--</script>--%>
<%--<script type="text/javascript">--%>
<%--<!--//--><![CDATA[//><!----%>
<%--try {--%>
<%--var pageTracker = _gat._getTracker("UA-355982-15");--%>
<%--try {--%>
<%--var pageTracker = _gat._getTracker("UA-355982-15");--%>
<%--pageTracker._trackPageview();--%>
<%--} catch (err) {--%>
<%--}--%>
<%--} catch (err) {--%>
<%--}--%>
<%--//--><!] ]>--%>
<%--</script>--%>

<%--&lt;%&ndash;End of Drupal Content&ndash;%&gt;--%>
<%--</tiles:putAttribute>--%>

</tiles:insertDefinition>