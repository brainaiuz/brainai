<%--
  Created by IntelliJ IDEA.
  User: Sherzod
  Date: 05.05.2011
  Time: 18:16:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
  <head>
      <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

      <link href="/loginpage/cooconnect/style.css" rel="stylesheet" type="text/css">
      <link href="/loginpage/cooconnect/merge.css" rel="stylesheet" type="text/css">

      <title><fmt:message key="successpassword.title"/> </title>

  </head>
  <body><div id="wrapper" class="index-page">
    <jsp:include page="coo-tiles/header.jsp"/>
    <div id="cover" style="height:900px" align="center">
        <div style="width:100%" align="center">
            <h2 style="border-bottom:1px solid #C0C0C0;color:#1F4F8F;font-weight:normal;font-size:20px;margin-left:10px"><fmt:message key="successpassword.passwordReminderSuccess"/> </h2>
            <DIV id="contacts">

                <DIV class="cover"><!--left border-->
                    <div class="cover" style="padding-bottom:15px;font-size:14px;margin-left:15px"><!--right border-->
                        <br/>

                        <h2 style="font-weight:bold;font-size:14px;"><fmt:message key="successpassword.yourLoginDetailsSentToEmail"/> </h2>
                        <br/>
                        <div><br></div>
                        <br>
                        <a href="/index.html"><fmt:message key="successpassword.goBackToSignInPage"/> </a>

                    </DIV>
                </DIV>
            </DIV>

        </div>



    </div>
    <jsp:include page="coo-tiles/footer.jsp"/>
</div></body>
</html>