<%@ page import="com.edatasite.workforce.appContext.SpringPropertiesUtil" %>
<%@ page session="true" contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.edatasite.workforce.gwt.core.server.app.Utils" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    boolean isBrain = Utils.isBrain(request);
    String greeting = isBrain ? "Salom" : "Hello";
    String plsSelectCompany = isBrain ? "Iltimos, kompaniyangizni tanlang" : "Please, choose your company";
    String backToLogin = isBrain ? "Kirish sahifasiga qaytish" : "<fmt:message key=\"companies.logOut\"/>";
    String favicon = isBrain ? "faviconBrain.png" : "favicon.ico";
%>



<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Choose Your Company</title>
    <meta name="viewport" content="width=device-width,initial-scale=1.0">
    <link rel="shortcut icon" href="/mainStyles/new-ui/login/img/<%= favicon %>?v=2" type="image/x-icon"/>
    <link rel="stylesheet" href="/mainStyles/new-ui/login/font-awesome/css/font-awesome.css"/>
    <link rel="stylesheet" href="/mainStyles/new-ui/login/slick/slick.css"/>
    <link rel="stylesheet" href="/mainStyles/new-ui/login/css/main.css?v=<%=SpringPropertiesUtil.getProperty("cssVersion")%>"/>
</head>
<body>
<div class="pg_landing pg_company-list">
    <div class="pg_landing__container">
        <div class="pg_landing__start-btn">
            <a class="elm_link--array" href="/index.html">
                <svg class="icon--chevronLeft">
                    <use href="/mainStyles/new-ui/icons/sprite__panels.svg#chevronLeft"></use>
                </svg>
                <span>
    <c:choose>
        <c:when test="<%= isBrain %>">
            Kirish sahifasiga qaytish
        </c:when>
        <c:otherwise>
            <fmt:message key="companies.logOut"/>
        </c:otherwise>
    </c:choose>
</span>
            </a>
        </div>

        <div class="pg_landing__main">
            <div class="pg_landing__main-innerbox">
                <div class="cp_company">
                    <div class="cp_company__header">
                        <div class="cp_company__header-user"><%= greeting %>, ${fullName}!</div>
                        <div class="cp_company__list-title"><%= plsSelectCompany %></div>
                    </div>
                    <div class="cp_company__list">
                        <ul class="cp_company__list-content">
                            <c:set var="companySize" value="${fn:length(companyList)}"/>
                            <c:forEach var="companyItem" items="${companyList}">
                                <li class="${'cp_company__list-item company-status--'}${companyItem.status}">
                                    <a href="${companyItem.clusterURL}&domain=${companyItem.subdomainCompany}&ACCOUNT_TYPE=${ACCOUNT_TYPE}&IS_MULTI_COMPANY=${IS_MULTI_COMPANY}&redirect_uri=${redirect_uri}">
                                        <span>
                                            <figure>
                                                <svg class="icon--company">
                                                    <use href="/mainStyles/new-ui/icons/sprite__panels.svg#company"></use>
                                                </svg>
                                                <figcaption>${companyItem.statusName}</figcaption>
                                            </figure>

                                        </span>

                                        <dl>
                                            <dt>${companyItem.companyName}</dt>
                                            <dd>ID: ${companyItem.companyID}</dd>
                                        </dl>

                                        <span>
                                            <svg class="icon--chevronRight">
                                                <use href="/mainStyles/new-ui/icons/sprite__panels.svg#chevronRight"></use>
                                            </svg>
                                        </span>
                                    </a>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>

<script src="/mainStyles/new-ui/login/js/jquery.min.js"></script>
<script src="/mainStyles/new-ui/login/js/slick.min.js"></script>
<script src="/mainStyles/new-ui/login/js/main.js"></script>
</body>
</html>