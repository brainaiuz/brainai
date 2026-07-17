<%@ page import="com.edatasite.workforce.gwt.core.client.ui.Constants" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%--
  Created by IntelliJ IDEA.
  User: Abdulaziz
  Date: 25.11.2008
  Time: 14:30:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><fmt:message key="thankyou.titlethankYouForTaking"/> </title>
 <link rel="shortcut icon" href="/customisation/${productNameLower}/images/favicon.ico" type="image/x-icon" />
 <link href="/landing/css/thankyou.css" rel="stylesheet" type="text/css">
</head>

<body>
<div style="width:1000px; background:#146593;">
<div id="head">
<table cellspacing="0" cellpadding="0">
<tr>
<td width="331" style="padding-left:100px;">

</td>
<td width="465" align="right" style="color:#FFFFFF"><div id="header"><fmt:message key="thankyou.onlineProjectManagementSoftwareEmployee"/> <br />
<fmt:message key="thankyou.performanceAppraisalToolTimeTrackingandCollaborationTool"/> </div>
</td>
</tr>
</table>
</div>
<div style="height:20px; background:#146593;">
</div>
<div style="width:600px; background:#146593;" align="center">
  <b class="spiffy">
  <b class="spiffy1"><b></b></b>
  <b class="spiffy2"><b></b></b>
  <b class="spiffy3"></b>
  <b class="spiffy4"></b>
  <b class="spiffy5"></b></b>
<div  style="width:1000px;">

    <a id="notice-1" href="<%=Constants.DOMEN%>">
        <br>
        <br>
        <span class="h4"><fmt:message key="thankyou.thankYouForTaking"/> <c:out value="${employee}"/></span>
        <span class="h5"><fmt:message key="thankyou.yourReviewHasBeenSavedasDraft"/> </span>
    	<%--<span class="h5">Your review now has been saved and sent to the initiator, <c:out value="${initiator}"/></span>--%>
    </a> <!--End #notice-1-->
 </div>
 <div id="footer"></div>
</div>

</div>

<!--New Google Analytics script-->
<script type="text/javascript">
    var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
    document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript">
    try {
        var pageTracker = _gat._getTracker("UA-355982-15");
        pageTracker._trackPageview();
    } catch(err) {
    }</script>
<!--New Google Analytics script-->

</body>
</html>
