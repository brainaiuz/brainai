<%@ page import="com.edatasite.workforce.appContext.SpringPropertiesUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<% String hostName = (request.getAttribute("hostName") != null) ? request.getAttribute("hostName").toString() : request.getServerName(); %>


<!doctype html>
<html>
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
    <link rel="stylesheet" href="/mainStyles/new-ui/css/transition.css">
    <link rel="shortcut icon" href="/mainStyles/new-ui/login/img/favicon.ico?v=2" type="image/x-icon">
</head>
<body class="wizard-page wizard-page--blur1 wizard-page--apps signUpStep signUpStep--3">


<div class="modal-holder active">
    <div class="modal modal--md sign-up-step sign-up-step--4" style="width: 823px;">
        <div class="modal-wrapper">
            <div class="modal-header">
                <div class="hgroup">
                    <h1 class="fs-4">
                        <fmt:message key="main.Whatwouldyouliketodoinkpicom"/><br><fmt:message key="main.ThisIsJustToGetYouStarted"/>
                    </h1>
                </div>
            </div>
            <form id="gettingStartedForm" action="/gettingStarted.html" method="post">
                <div class="modal-content">

                    <div id="cp_modules-switch__error" hidden style="color:red;"><fmt:message key="main.PleaseSelectAtLeastOneApp"/> </div>
                    <div class="sign-up-step__section">
                        <div class="cp_modules-switch cp_modules-switch--content">
                            <div class="cp_modules-switch__item">
                                <div class="cp_modules-switch__item-content">
                                    <div class="cp_modules-switch__logo"><i class="ficon--calc"></i></div>
                                    <div class="cp_modules-switch__title"><fmt:message key="main.Accounts"/></div>
                                    <div class="cp_modules-switch__switcher">
                                        <div class="switch">
                                            <label>
                                                <span></span>
                                                <input type="checkbox" name="modules" value="accounts"
                                                       onchange="onChange()">
                                                <span class="lever"></span><span></span>
                                            </label>
                                            <span class="material-label" style="margin-top: 16px;"></span>
                                        </div>
                                    </div>
                                </div>
                                <div class="cp_modules-switch__notes"><fmt:message key="main.accountsNote"/></div>
                            </div>
                            <div class="cp_modules-switch__item">
                                <div class="cp_modules-switch__item-content">
                                    <div class="cp_modules-switch__logo"><i class="ficon--bar-chart-bold"></i></div>
                                    <div class="cp_modules-switch__title"><fmt:message key="main.Sales"/></div>
                                    <div class="cp_modules-switch__switcher">
                                        <div class="switch">
                                            <label>
                                                <span></span>
                                                <input type="checkbox" name="modules" value="sales" onchange="onChange()">
                                                <span class="lever"></span><span></span>
                                            </label>
                                            <span class="material-label" style="margin-top: 16px;"></span>
                                        </div>
                                    </div>
                                </div>
                                <div class="cp_modules-switch__notes"><fmt:message key="main.salesNote"/></div>
                            </div>
                            <div class="cp_modules-switch__item">
                                <div class="cp_modules-switch__item-content">
                                    <div class="cp_modules-switch__logo"><i class="ficon--users-bold"></i></div>
                                    <div class="cp_modules-switch__title"><fmt:message key="main.Humans"/></div>
                                    <div class="cp_modules-switch__switcher">
                                        <div class="switch">
                                            <label>
                                                <span></span>
                                                <input type="checkbox" name="modules" value="humans" onchange="onChange()">
                                                <span class="lever"></span><span></span>
                                            </label>
                                            <span class="material-label" style="margin-top: 16px;"></span>
                                        </div>
                                    </div>
                                </div>
                                <div class="cp_modules-switch__notes"><fmt:message key="main.humansNote"/></div>
                            </div>
                            <div class="cp_modules-switch__item">
                                <div class="cp_modules-switch__item-content">
                                    <div class="cp_modules-switch__logo"><i class="ficon--server-bold"></i></div>
                                    <div class="cp_modules-switch__title"><fmt:message key="main.Projects"/></div>
                                    <div class="cp_modules-switch__switcher">
                                        <div class="switch">
                                            <label>
                                                <span></span>
                                                <input type="checkbox" name="modules" value="projects"
                                                       onchange="onChange()">
                                                <span class="lever"></span><span></span>
                                            </label>
                                            <span class="material-label" style="margin-top: 16px;"></span>
                                        </div>
                                    </div>
                                </div>
                                <div class="cp_modules-switch__notes"><fmt:message key="main.projectsNote"/></div>
                            </div>
                            <div class="cp_modules-switch__item">
                                <div class="cp_modules-switch__item-content">
                                    <div class="cp_modules-switch__logo"><i class="ficon--wallet-bold"></i></div>
                                    <div class="cp_modules-switch__title"><fmt:message key="main.Payroll"/></div>
                                    <div class="cp_modules-switch__switcher">
                                        <div class="switch">
                                            <label>
                                                <span></span>
                                                <input type="checkbox" name="modules" value="payroll" onchange="onChange()">
                                                <span class="lever"></span><span></span>
                                            </label>
                                            <span class="material-label" style="margin-top: 16px;"></span>
                                        </div>
                                    </div>
                                </div>
                                <div class="cp_modules-switch__notes"><fmt:message key="main.payrollNote"/></div>
                            </div>
                            <div class="cp_modules-switch__notes cp_modules-switch__notes--original"><fmt:message key="main.kpicomoffers"/>
                            </div>
                        </div>
                    </div>

                </div>

                <div class="modal-footer text-left">
                    <button type="submit" class="btn btn--primary"><fmt:message key="main.Continue"/></button>
                    <div class="footnote">
                        <svg class="icon--info">
                            <use href="/mainStyles/new-ui/icons/sprite__panels.svg#info"></use>
                        </svg>
                        <span><fmt:message key="main.YoucanalwayschangeyourpreferenceslaterinSettings"/></span>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <div class="lean-overlay file--gettingStarted" id="materialize-lean-overlay-1" style="opacity: 0.5;"></div>
</div>


<!--SCRIPTS-->
<script>
    jQuery(document).ready(function () {
        $('select').material_select();
    });
</script>
<script type="text/javascript" src="/mainStyles/new-ui/js/jquery.min.js"></script>
<script type="text/javascript" src="/mainStyles/new-ui/js/materialize-0.97.5.min.js"></script>

<script type="text/javascript">
    //init active checkboxes
    $(".cp_modules-switch__item input:checked").parents(".cp_modules-switch__item").addClass("cp_modules-switch__item--active");

    //add/remove active class on change
    $(".cp_modules-switch__item input[type='checkbox']").change(function () {
        $(this).parents(".cp_modules-switch__item").toggleClass("cp_modules-switch__item--active");
        var module_check = $(this).find('input[type="checkbox"]');
        module_check.prop('checked', !module_check.prop("checked"));

        if ($(".cp_modules-switch__item input:checked").length >= 0) {
            $('#cp_modules-switch__error').hide();
        }
    });

    function onSubmit() {
        var error_field = $('#cp_modules-switch__error');
        var fields = $("input[name='modules']").serializeArray();

        error_field.hide();
        if (fields.length === 0) {
            error_field.show();
            // cancel submit
            return false;
        }
    }

    // register event on form, not submit button
    $('#gettingStartedForm').submit(onSubmit)
</script>
</body>
</html>