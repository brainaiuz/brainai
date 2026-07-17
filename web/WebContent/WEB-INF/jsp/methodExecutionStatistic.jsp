<%--
  Created by IntelliJ IDEA.
  User: Anvar Akramov
  Date: 15.05.2020
  Time: 20:01:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Performence Statistic</title>
    <style type="text/css">
        table {
            margin-top: 20px;
            border-left: 1px solid black;
            border-top: 1px solid black;
            border-right: 1px solid black;
        }

        table th {
            border-bottom: 1px solid black;
        }

        table td {
            border-bottom: 1px solid black;
        }

        table td:nth-child(2) {
            border-left: 1px solid black;
        }
    </style>
</head>
<body>

<h1>Methods execution statistic</h1>

<table cellpadding="5" cellspacing="0">
    <tr>
        <th>Method Name</th>
        <th>Executed time (ms)</th>
    </tr>
    <c:forEach var="instance" items="${stats}">
    <tr>
        <td>${instance.key}</td>
        <td>${instance.value}</td>
    </tr>
    </c:forEach>
</table>

</body>
</html>