<%@ page import="com.edatasite.workforce.appContext.SpringPropertiesUtil" %>
<%--  User: jamshid's  Date: Nov 20, 2010--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!doctype html>
<html class="no-js" lang="en" dir="ltr">
<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <tiles:putAttribute name="title">
        <fmt:message key="pagenotfound.pageNotFound"/>
    </tiles:putAttribute>
    <link rel="shortcut icon" href="/mainStyles/new-ui/login/img/favicon.ico?v=2" type="image/x-icon"/>
    <link rel="stylesheet" href="/mainStyles/new-ui/login/font-awesome/css/font-awesome.css"/>
    <link rel="stylesheet" href="/mainStyles/new-ui/login/slick/slick.css"/>
    <link rel="stylesheet" href="/mainStyles/new-ui/login/css/main.css?v=<%=SpringPropertiesUtil.getProperty("cssVersion")%>"/>
</head>
<body class="pg_landing pg_404">

<dl class="b-404">
    <dt>403</dt>
    <dd class="b-404_dsc">We can't seem to find the domain <br> you are looking for.</dd>
</dl>

</body>
</html>