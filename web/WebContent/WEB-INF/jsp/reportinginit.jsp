<%@ page import="com.edatasite.workforce.appContext.SpringPropertiesUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!doctype html>
<html class="no-js" lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><fmt:message key="reportinginit.connectyourowndatasourse"> </fmt:message></title>

    <link rel="stylesheet" href="/mainStyles/new-ui/css/materialize.css?v=<%=SpringPropertiesUtil.getProperty("cssVersion")%>"/>
    <link rel="stylesheet" href="/mainStyles/new-ui/css/jquery-ui.min.css">

    <link rel="shortcut icon" href="/mainStyles/new-ui/login/img/favicon.ico?v=2" type="image/x-icon">

    <style>
        .invalid {
            border-bottom: 1px solid #F44336;
            box-shadow: 0 1px 0 0 #F44336;
        }
    </style>
</head>

<%--<body class="" toast="bottom-right" >--%>
<body toast="bottom-right" class="frame__body left-menu-open has-page-opers fitted-content has-guide-modal" data-gr-c-s-loaded="true" style="overflow: hidden;">
<script type="text/javascript" src="/mainStyles/js/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="/mainStyles/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="/mainStyles/new-ui/js/materialize-0.97.5.min.js"></script>

<div id="waiting" class="valign-wrapper loader-wrapper content-back-drop" style="width: auto; position: none;">
    <div id="waitingicon" style="margin: auto; display: none " class="material-preloader">
        <div class="lds-message">
            <div></div>
            <div></div>
            <div></div>
        </div>
    </div>
</div>

<form action="/reportinginit.html" method="post" content="application/json" id="importdata-form" class="features-list features-list--reporting">
    <aside class="features-aside">
        <div class="features-list__heading">
            <a class="features-aside__title">
                <svg class="icon--reporting">
                    <use href="/mainStyles/new-ui/icons/sprite__panels.svg#reporting"></use>
                </svg>
                <span><fmt:message key="reportinginit.report"> </fmt:message></span>
            </a>
        </div>

        <dl class="features-aside__welcome">
            <dd><fmt:message key="reportinginit.connectyourowndatasourse"> </fmt:message></dd>
        </dl>

        <ul class="features-aside__steps">
            <li id="connetyourdata" class="status--current">
                <span><fmt:message key="reportinginit.connectyourdata"> </fmt:message></span>
            </li>
            <li id="mapping" class="status--not-passed">
                <span><fmt:message key="reportinginit.mapping"> </fmt:message></span>
            </li>
        </ul>
    </aside>

    <div id="firstpart" class="content-holder book-layout">
        <div class="book-layout__left">
            <div class="features-list__heading">
                <c:if test="${errormsg != null}">
                    <h2 class="features-list__title">${errormsg}</h2>
                </c:if>
                <h3 class="features-list__sub-title"><fmt:message key="reportinginit.CreateMeaningfulChartsAndTablesFromYourDataUsingOurIntuitiveDragDropInterface"> </fmt:message></h3>
            </div>

            <div class="features-list__main-body">
                <div class="tiles">
                    <figure class="config-paid-option runClickClass">
                        <span class="figure-icon">
                            <img src="/mainStyles/new-ui/icons/excel.svg" alt="image">
                        </span>
                        <figcaption>
                            <strong><fmt:message key="reportininit.XLS"/></strong> <fmt:message key="reportinginit.fromLocalDrive" />
                        </figcaption>
                    </figure>

                    <figure class="config-paid-option runClickClass">
                        <span class="figure-icon">
                            <img src="/mainStyles/new-ui/icons/excel.svg" alt="image">
                        </span>
                        <figcaption>
                            <strong><fmt:message key="reportininit.XLSX"/></strong> <fmt:message key="reportinginit.fromLocalDrive" />
                        </figcaption>
                    </figure>

                    <figure class="config-paid-option runClickClass">
                        <span class="figure-icon">
                            <img src="/mainStyles/new-ui/icons/csv.svg" alt="image">
                        </span>
                        <figcaption>
                            <strong><fmt:message key="reportininit.CSV"/></strong> <fmt:message key="reportinginit.fromLocalDrive" />
                        </figcaption>
                    </figure>

                    <figure class="config-paid-option" title="Coming Soon">
                       <span class="figure-icon">
                            <img src="/mainStyles/new-ui/icons/gsheet.svg" alt="image">
                        </span>
                        <figcaption>
                            <strong alt="Coming Soon">Google Spreadsheet</strong>
                        </figcaption>
                    </figure>

                    <input type="file" id="file" onchange="handleFilesCustom(this.files)">
                </div>
            </div>

            <div class="features-list__main-footer">
                <div class="btns-group">
                    <button type="button" class="btn btn--default btn--outline" onclick="return submitForm(true);">
                        <span><fmt:message key="accountinginit.Cancel"> </fmt:message></span></button>
                </div>

            </div>

        </div>

        <div class="book-layout__right">
            <figure class="setup-note-reporting">
                <div class="figure__img">
                    <img src="/mainStyles/new-ui/images/reporting-setup-figure.png" alt="image"/>
                </div>

                <figcaption>
                    <h3 class="features-list__sub-title"><fmt:message key="reportinginit.meesageone"> </fmt:message></h3>
                    <h3 class="features-list__sub-title"><fmt:message key="reportinginit.meesagetwo"> </fmt:message></h3>
                    <h3 class="features-list__sub-title"><fmt:message key="reportinginit.meesagethree"> </fmt:message></h3>

                    <a class="whatch-youtube" href="#" id="showModal">
                        <h3 class="features-list__sub-title"><fmt:message key="reportinginit.meesagefour"> </fmt:message></h3>
                    </a>
                    <div id="shade" class="modal" style="z-index: 1003; display: none; opacity: 1; transform: scaleX(1); top: 10%;">
                        <div class="modal-wrapper convert-lead form-compressed">
                            <div id="modal">
                                <iframe src="https://www.youtube.com/embed?v=QbQR2ICYv6w&list=PLkQMm7X2VSS3K3p7eAZigY2BEy884S8UH"
                                        sandbox="allow-forms allow-scripts allow-same-origin allow-presentation"
                                        allowfullscreen="allowfullscreen"></iframe>
                            </div>
                            <a href="#" class="btn-small btn--close btn--circle" id="modalClose">
                                <svg class="icon--x">
                                    <use href="/mainStyles/new-ui/icons/sprite__panels.svg?v=<%=SpringPropertiesUtil.getProperty("cssVersion")%>#x"></use>
                                </svg>
                            </a>
                        </div>
                    </div>
                </figcaption>
            </figure>
        </div>
    </div>

    <div id="secondpart" class="table-cols__main">
        <div class="features-list__heading">
            <h2 class="features-list__title"><fmt:message key="reportinginit.adjustColumnHeadersAndAataFormat"></fmt:message></h2>
        </div>

        <div class="table-cols__wrapper">
            <table class="table-cols" name="dataTable">
                <thead>
                <tr name="tbHeadTr">
                </tr>
                </thead>
                <tbody>
                <tr class="table-cols__check" name="includeTr"></tr>
                <tr class="table-cols__options" name="typeTr"></tr>
                </tbody>
            </table>
        </div>

        <div class="features-list__main-footer">
            <div class="btns-group">
                <button class="btn btn--default btn--outline" onclick="return openFirstPart();"><span><fmt:message key="reportinginit.previous"> </fmt:message></span></button>
                <button type="button" class="btn btn--primary" onclick="return submitForm(false);"><span><fmt:message key="reportinginit.import"> </fmt:message></span></button>
            </div>

        </div>
    </div>

    <input type="hidden" name="importfileId" value="0">
    <input type="hidden" name="categoryId" value="${reportcategoryid}">
</form>


<!--SCRIPTS-->
<script>

    $("#secondpart").hide();

    var importFileId = 0;

    jQuery(document).ready(function () {
        $('select').material_select();
    });

    $("#file").css('opacity', '0');

    $(".runClickClass").click(function (e) {
        e.preventDefault();
        $("#file").trigger('click');
        importFileId = 0;
    });

    $("#showModal").click(function (e) {
        e.preventDefault();
        $("#modal").addClass("modal-content");
        $("#shade").css("display", "block");
    });
    $("#modalClose").click(function (e) {
        e.preventDefault();
        $("#modal").removeClass("modal-content");
        $("#shade").css("display", "none");
    });

    function openFirstPart() {
        $("#secondpart").hide();
        $("#connetyourdata").addClass("status--current");
        $("#connetyourdata").removeClass("status--passed");

        $("#mapping").addClass("status--not-passed");
        $("#mapping").removeClass("status--current");
        $("#firstpart").show();
        return false;
    }

    function openSecondPart() {
        $("#firstpart").hide();
        $("#connetyourdata").removeClass("status--current");
        $("#connetyourdata").addClass("status--passed");

        $("#mapping").removeClass("status--not-passed");
        $("#mapping").addClass("status--current");
        $("#secondpart").show();
    }

    function handleFilesCustom(files) {
        $.each(files, function (i, file) {
            if (!(file.name.endsWith(".csv", file.name.length)
                    || file.name.endsWith(".xls", file.name.length)
                    || file.name.endsWith(".xlsx", file.name.length))) {
                window.alert(<fmt:message key="reportinginit.mapping"> </fmt:message>);
            }
            else {
                uploadFileAndGetHeaders();
            }
        });
    }

    function uploadFileAndGetHeaders() {

        var errors = 0;


        if ($('#file')[0].files.length == 0) {
            $(".runClickClass").addClass("invalid");
            errors += 1;
        } else {
            $(".runClickClass").removeClass("invalid");
        }

        if (errors > 0) {
            return false;
        }

        loadOrHideLoadingPanel(true);
        if (importFileId == 0) {
            var formData = new FormData();
            $.each($('#file')[0].files, function (i, file) {
                formData.append('uploadFile', file, file.name);
            });
            $.ajax({
                url: '/services/api/v2/uploadAttachment',
                type: 'POST',
                data: formData,
                contentType: false,
                processData: false,
                success: function (response) {
                    importFileId = response;
                    $("input[name='importfileId']").attr("value", importFileId);
                    getImportFileHeaders();
                },
                beforeSend: setHeader
            });
        } else {
            getImportFileHeaders();
        }
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

    function getImportFileHeaders() {
        $.ajax({
            url: "/services/api/v2/headers/" + importFileId + "/7",
            type: 'GET',
            success: function (response) {
                loadOrHideLoadingPanel(false);
                openSecondPart();
                createHtmlTableForData(response);
                return false;
            },
            error: function (error) {
                loadOrHideLoadingPanel(false);
                alert(error);
                return false;
            },
            beforeSend: setHeader
        });
    }

    function createHtmlTableForData(response) {

        var $dataTable = $("table[name='dataTable']");

        var $tbHeadTr = $("tr[name='tbHeadTr']");
        $tbHeadTr.find('td').each(function () {
            $(this).remove();
        });

        var $includeTr = $("tr[name='includeTr']");
        $includeTr.find('td').each(function () {
            $(this).remove();
        });

        var $typeTr = $("tr[name='typeTr']");
        $typeTr.find('td').each(function () {
            $(this).remove();
        });

        $("tr[name='tbDataTr']").remove();

        var columnIndex = 0;
        var childItems = response.data.list[0];
        $.each(childItems, function (item) {

            var $newtd = $("<td>");
            var $newstrong = $("<strong>" + this.name + "</strong><input type='hidden' name='columnName' value='" + this.name + "'>");
            $newtd.append($newstrong);
            $tbHeadTr.append($newtd);

            var $newtd2 = $("<td>\n" +
                    "            <label class='control control--checkbox'>\n" +
                    "                <input id='onofcheck" + columnIndex + "' type='checkbox' checked onclick='changeInputValue(" + columnIndex + ");' />\n" +
                    "                <span class='control__indicator'></span>\n" +
                    "                <span class='control__description'>Include</span>\n" +
                    "                <input type='hidden' name='onOfValue' value='on' id='onof" + columnIndex + "'>\n" +
                    "            </label>\n" +
                    "        </td>");
            $includeTr.append($newtd2);

            var $newtd3 = $("<td>\n" +
                    "             <div class='form-control input-field listbox-wrapper' autocomplete='off'>\n" +
                    "                 <div class='gwt-ListBox'>\n" +
                    "                     <select class='gwt-ListBox initialized' name='fortmatType' inside-origin='false'>\n" +
                    "                         <option value='1'>Text/Mixed</option>\n" +
                    "                         <option value='2'>Numeric</option>\n" +
                    "                         <option value='3'>Money</option>\n" +
                    "                         <option value='4'>Short Date</option>\n" +
                    "                         <option value='5'>Long Date</option>\n" +
                    "                     </select>\n" +
                    "                 </div>\n" +
                    "                 <span class='material-label'></span>\n" +
                    "             </div>\n" +
                    "         </td>");
            $typeTr.append($newtd3);

            columnIndex++;
        });

        var i = 0;
        $.each(response.data.list, function (pitem) {
            if (i > 0) {
                var $tbDataTd = "";
                var bodyitems = response.data.list[i];
                $.each(bodyitems, function (childITem) {
                    $tbDataTd = $tbDataTd.concat("<td><span class='text-cover'>" + this.name + "</span></td>");
                });
                $dataTable.append("<tr name='tbDataTr'>" + $tbDataTd + "</tr>");
            }
            i++;
        });
    }

    function loadOrHideLoadingPanel(isLoading) {
        if (isLoading) {
            $("#waiting").css("display", "block");
            $("#waitingicon").css("display", "block");
            $("#waiting").css("position", "fixed");
        } else {
            $("#waiting").css("display", "none");
            $("#waitingicon").css("display", "none");
            $("#waiting").css("position", "unset");
        }

    }

    function changeInputValue(columnIndex) {
        var chbValue = $('#onofcheck' + columnIndex).is(":checked");
        if (chbValue) {
            $('#onof' + columnIndex).attr("value", 'on');
        } else {
            $('#onof' + columnIndex).attr("value", 'off');
        }
    }

    function submitForm(isCancel) {
        loadOrHideLoadingPanel(true);
        var cancelInput = document.createElement('input');
        cancelInput.type = 'hidden';
        cancelInput.name = 'cancel';
        cancelInput.value = isCancel;
        var form = document.getElementById('importdata-form');
        form.appendChild(cancelInput);
        form.submit();
        return false;
    }

</script>
</body>

</html>