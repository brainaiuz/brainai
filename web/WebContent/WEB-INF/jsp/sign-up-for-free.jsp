<%--
  Created by IntelliJ IDEA.
  User: Fatxulla Nigmatjonov
  Date: 11/12/14
  Time: 6:04:14 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<tiles:insertDefinition name="frontEndLayoutNew">

<tiles:putAttribute name="title">
    <fmt:message key="freeTrial.title"/>
</tiles:putAttribute>

<tiles:putAttribute name="style">

    <meta http-equiv="X-UA-Compatible" content="IE=8"/>
    <script type="text/javascript" src="/jsscript/landing.js"></script>
    <script type="text/javascript" src="/jsscript/ajax.js"></script>
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
                $.cookie(COOKIE_NAME, obj, { path:'/', expires:7 });
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
            interval:5000,
            repeat:false,
            useControls:true,
            fixedControls:'fit',
            overlayOptions:{
                opacity:.75,
                position:'bottom center',
                hideOnMouseOut:true
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

</tiles:putAttribute>

<tiles:putAttribute name="body">
    <input type="hidden" id="isukclient" value="${isukclient}"/>
    <iframe id="__gwt_historyFrame" style="width:0;height:0;border:0"></iframe>

    <div class="breadcrumb">
        <ol class="container">
            <li><a href="http://new.kpi.com">Home</a></li>
            <li class="active">Try kpi.com for free</li>
        </ol>
    </div>

    <div class="container">

        <article class="post-9289 page type-page status-publish hentry" id="post-9289">
            <h1 class="page_title">Try kpi.com for free</h1>
            <div class="row products_review bottomField sign_up_for_free">
                <div class="col-md-6 left_content">
                    <div class="main-content">
                        <div class="row">
                            <div class="col-md-12"><h4 style="text-align: center;" class="free-trial"><fmt:message key="signup.freeTrialPay"/></h4>
                                <h5 style="text-align: center;" class="sign-up-work"><fmt:message key="signup.SignUpInTwoMinutes"/></h5>
                                <p style="text-align: center;">
                                </p>
                            </div>
                        </div>

                        <form:form id="appTrialForm" role="form" method="post" commandName="newCompany" action="/signup/sign-up-for-free.html">
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="adminFName"><fmt:message key="signup.firstName"/></label>
                                        <form:input path="adminFName"  cssClass="form-control"/>
                                        <form:errors path="adminFName" cssClass="error"/>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="adminLName"><fmt:message key="signup.lastName"/></label>
                                        <form:input path="adminLName" cssClass="form-control"/>
                                        <form:errors path="adminLName" cssClass="error"/>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="adminEmail"><fmt:message key="signup.email"/></label>
                                        <form:input path="adminEmail" cssClass="form-control" id="edit-submitted-e-mail"
                                                    readonly="${fromFederatedLogin}"
                                                    onblur="sendRequest('GET','/signup/handleAjaxRequest?adminEmail='+this.value,this.value)"/>
                                        <span id="emailValidate" class="error"><form:errors path="adminEmail" cssClass="error"/></span>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="name"><fmt:message key="signup.companyName"/></label>
                                        <form:input path="name" cssClass="form-control" />
                                        <form:errors path="name" cssClass="error"/>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="phone"><fmt:message key="signup.phone"/></label>
                                        <form:input path="phone" cssClass="form-control" onkeypress="return phoneValidation(event)"/>
                                        <form:errors path="phone" cssClass="error"/>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="countryID"><fmt:message key="signup.country"/></label>
                                        <form:select path="countryID" cssClass="form-control">
                                            <form:option value="">
                                                <fmt:message key="signup.pleaseSelect"/>
                                            </form:option>
                                            <form:options items="${countrys}" itemLabel="name" itemValue="objectID"/>
                                        </form:select>
                                        <form:errors path="countryID" cssClass="error"/>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="userPromotionalCode"><fmt:message key="signup.promutionalCode"/></label>
                                        <p class="definition"><fmt:message key="signup.ifapplicable"/></p>
                                        <input  value="" id="promotional-code" class="form-control" name="companySignedUpFrom"/>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="locale"><fmt:message key="signup.language"/></label>
                                        <p class="definition"><fmt:message key="signup.DefaultlanguagefortheSystem"/></p>
                                        <form:select id="localeSelector" path="locale" cssClass="form-control">
                                            <c:forEach var="localeItem" items="${locales}">
                                                <c:choose>
                                                    <c:when test="${pageContext.request.locale.language == localeItem.description}">
                                                        <option value="${localeItem.description}" selected = "selected">${localeItem.name}</option>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <form:option value="${localeItem.description}">${localeItem.name}</form:option>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:forEach>
                                        </form:select>
                                    </div>
                                </div>

                                <div class="col-md-12 agreement-checkbox">
                                    <form:checkbox path="agreeWithCondition" id="terms" cssStyle="width: 40px !important;float: left" label="${terms}"/>
                                    <span style="float: left;margin-right: 2px;"><fmt:message key="signup.iAgreeWitThe"/></span>
                                    <a style="float:left;" href="//${helpHost}/content/terms-of-use" target="_blank">
                                        <fmt:message key="signup.termsAndConditions"/>
                                    </a>
                                    <div style="text-transform:uppercase; height: 20px;clear: both;margin-left: 50px; "><form:errors path="agreeWithCondition" cssClass="error"/></div>
                                    <center>
                                        <div id="recaptcha"></div>
                                        <div style="text-transform:uppercase; height: 20px; ">${captchaError}</div>
                                    </center>
                                </div>
                                <div style="margin-top: -39px;" class="col-md-12">
                                    <button class="btn btn-block btn-primary btn-sign-up-submit" type="submit" ><fmt:message key="signup.startFreeTrial"/></button>
                                </div>
                            </div>
                            <input type="hidden" name="action" value="signUp"/>
                        </form:form>

                    </div>
                </div>

                <div class="col-md-6 right_content"> <!-- Begin of main content area -->
                    <div class="main-content">
                        <h3><fmt:message key="signup.benefits"/>:</h3>
                        <div class="textwidget">
                            <ul class="kpi-benefits">
                                <li> <fmt:message key="signup.PM"/></li>
                                <li> <fmt:message key="signup.accessAnywhereAnytime"/></li>
                                <li><fmt:message key="signup.automaticBackups"/></li>
                                <li><fmt:message key="signup.easilyExportYourData"/></li>
                                <li><fmt:message key="signup.mobileApplications"/></li>
                                <li><fmt:message key="signup.allData"/></li>
                                <li><fmt:message key="signup.trainingSupport"/></li>
                                <li><fmt:message key="signup.additionalStorage"/></li>
                                <li><fmt:message key="signup.multipleWarehouse"/></li>
                                <li>Customizable Dashboard</li>
                                <li>Reporting tool</li>
                            </ul>
                        </div>
                    </div>
                </div><!-- End of main content area -->
            </div><!-- #row products_review -->

        </article><!-- #post-## -->


    </div>
	
</tiles:putAttribute>

<tiles:putAttribute name="script">
    <script type="text/javascript">
        if (document.getElementById("adminFName")) document.getElementById("adminFName").focus(); //Initially focus on firstname textbox
        <!--//--><![CDATA[//><!--
        var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
        document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
        //--><!] ]>

    </script>
    <script type="text/javascript">
        <!--//--><![CDATA[//><!--
        try {
            var pageTracker = _gat._getTracker("UA-355982-15");
            try {
                var pageTracker = _gat._getTracker("UA-355982-15");
                pageTracker._trackPageview();
            } catch (err) {
            }
        } catch (err) {
        }
        //--><!] ]>
    </script>

    <%--End of Drupal Content--%>
</tiles:putAttribute>

</tiles:insertDefinition>