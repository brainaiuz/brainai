<%@ page import="com.edatasite.workforce.appContext.SpringPropertiesUtil" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="language" value="${not empty param.language ? param.language : 'en'}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="com.edatasite.workforce.gwt.core.client.localization.WfmStrings" />

<c:choose>
    <c:when test="${language == 'ru'}">
        <c:set var="tfSitTight" value="Подождите! Мы создаём ваш аккаунт. Вы будете перенаправлены через несколько секунд." />
        <c:set var="tfStep1"    value="Токен" />
        <c:set var="tfStep2"    value="Компания" />
        <c:set var="tfStep3"    value="Настройка" />
        <c:set var="tfStatus1"  value="Компания создаётся..." />
        <c:set var="tfStatus2"  value="Настройки сохраняются..." />
        <c:set var="tfStatus3"  value="Почти готово..." />
    </c:when>
    <c:when test="${language == 'uz'}">
        <c:set var="tfSitTight" value="Kuting! Hisobingiz yaratilmoqda. Bir necha soniyada yo'naltirilasiz." />
        <c:set var="tfStep1"    value="Token" />
        <c:set var="tfStep2"    value="Kompaniya" />
        <c:set var="tfStep3"    value="Sozlash" />
        <c:set var="tfStatus1"  value="Kompaniya yaratilmoqda..." />
        <c:set var="tfStatus2"  value="Sozlamalar saqlanmoqda..." />
        <c:set var="tfStatus3"  value="Deyarli tayyor..." />
    </c:when>
    <c:otherwise>
        <c:set var="tfSitTight" value="Sit tight! We are creating your account. You will be redirected in a few seconds." />
        <c:set var="tfStep1"    value="Token" />
        <c:set var="tfStep2"    value="Company" />
        <c:set var="tfStep3"    value="Set up" />
        <c:set var="tfStatus1"  value="Creating company..." />
        <c:set var="tfStatus2"  value="Saving settings..." />
        <c:set var="tfStatus3"  value="Almost ready..." />
    </c:otherwise>
</c:choose>

<!doctype html>
<html lang="${language}">
<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>TextileFinds</title>
    <link rel="shortcut icon" href="/customisation/textilefinds/images/favicon.ico" type="image/x-icon"/>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }

        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
            background: #f3f4f6;
            display: flex;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
        }

        .modal {
            background: #ffffff;
            border-radius: 16px;
            padding: 2.5rem 2rem;
            width: 460px;
            text-align: center;
            box-shadow: 0 4px 32px rgba(0, 0, 0, 0.10);
        }

        .logo {
            margin-bottom: 1.5rem;
        }

        .logo img {
            width: 180px;
        }

        .banner {
            background: #3b82f6;
            color: #ffffff;
            border-radius: 10px;
            padding: 14px 18px;
            font-size: 14px;
            font-weight: 500;
            line-height: 1.5;
            margin-bottom: 2rem;
        }

        .steps {
            display: flex;
            align-items: center;
            justify-content: center;
            margin-bottom: 1.5rem;
        }

        .step-wrap {
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 8px;
        }

        .step-circle {
            width: 48px;
            height: 48px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            transition: background 0.4s ease;
        }

        .step-circle.done    { background: #22c55e; }
        .step-circle.pending { background: #e5e7eb; }

        .step-circle.done svg stroke    { stroke: #ffffff; }
        .step-circle.pending svg stroke { stroke: #9ca3af; }

        .step-line {
            width: 70px;
            height: 2px;
            margin-bottom: 24px;
            transition: background 0.4s ease;
        }

        .step-line.done    { background: #22c55e; }
        .step-line.pending { background: #e5e7eb; }

        .step-label {
            font-size: 12px;
            color: #6b7280;
        }

        .status-text {
            font-size: 15px;
            color: #374151;
            font-weight: 500;
            margin-bottom: 1.5rem;
            min-height: 24px;
        }

        .dots {
            display: flex;
            justify-content: center;
            gap: 8px;
        }

        .dot {
            width: 10px;
            height: 10px;
            border-radius: 50%;
            background: #3b82f6;
        }

        @keyframes bounce {
            0%, 80%, 100% { transform: translateY(0); }
            40%           { transform: translateY(-9px); }
        }
    </style>
</head>
<body>

<div class="modal">

    <div class="logo">
        <img src="/customisation/textilefinds/images/logo.png" alt="TextileFinds" />
    </div>

    <div class="banner">${tfSitTight}</div>

    <div class="steps">

        <div class="step-wrap">
            <div class="step-circle done" id="s1">
                <svg width="22" height="22" viewBox="0 0 24 24" fill="none"
                     stroke="white" stroke-width="3"
                     stroke-linecap="round" stroke-linejoin="round">
                    <polyline points="20 6 9 17 4 12"/>
                </svg>
            </div>
            <span class="step-label">${tfStep1}</span>
        </div>

        <div class="step-line pending" id="l1"></div>

        <div class="step-wrap">
            <div class="step-circle pending" id="s2">
                <svg width="22" height="22" viewBox="0 0 24 24" fill="none"
                     stroke="#9ca3af" stroke-width="3"
                     stroke-linecap="round" stroke-linejoin="round">
                    <polyline points="20 6 9 17 4 12"/>
                </svg>
            </div>
            <span class="step-label">${tfStep2}</span>
        </div>

        <div class="step-line pending" id="l2"></div>

        <div class="step-wrap">
            <div class="step-circle pending" id="s3">
                <svg width="22" height="22" viewBox="0 0 24 24" fill="none"
                     stroke="#9ca3af" stroke-width="3"
                     stroke-linecap="round" stroke-linejoin="round">
                    <polyline points="20 6 9 17 4 12"/>
                </svg>
            </div>
            <span class="step-label">${tfStep3}</span>
        </div>

    </div>

    <div class="status-text" id="status-text">${tfStatus1}</div>

    <div class="dots">
        <div class="dot" style="animation: bounce 1.2s infinite 0.0s"></div>
        <div class="dot" style="animation: bounce 1.2s infinite 0.2s"></div>
        <div class="dot" style="animation: bounce 1.2s infinite 0.4s"></div>
        <div class="dot" style="animation: bounce 1.2s infinite 0.6s"></div>
        <div class="dot" style="animation: bounce 1.2s infinite 0.8s"></div>
    </div>

</div>

<form id="processForm" action="/textilefinds-connect.html" method="post" style="display:none">
    <input type="hidden" name="token" value="${token}"/>
</form>

<script>
    function markStep(stepId, lineId, statusText, svgStrokeColor) {
        var circle = document.getElementById(stepId);
        circle.classList.remove('pending');
        circle.classList.add('done');
        circle.querySelector('svg').setAttribute('stroke', svgStrokeColor || 'white');

        if (lineId) {
            var line = document.getElementById(lineId);
            line.classList.remove('pending');
            line.classList.add('done');
        }

        document.getElementById('status-text').textContent = statusText;
    }

    setTimeout(function () {
        markStep('s2', 'l1', '${tfStatus2}');
    }, 5000);

    setTimeout(function () {
        markStep('s3', 'l2', '${tfStatus3}');
    }, 10000);

    document.getElementById('processForm').submit();
</script>

</body>
</html>
