<%--
  Created by IntelliJ IDEA.
  User: Bilol
  Date: 11/19/2022
  Time: 11:43 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <p id="message">${response}</p>
<script>
    setTimeout(function () {
        window.close();
    },500);
</script>
</body>
</html>
