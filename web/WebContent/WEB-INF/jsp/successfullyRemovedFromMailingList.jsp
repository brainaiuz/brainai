<%@ page import="com.edatasite.workforce.utils.EdsContextParams" %>
<%--
  Created by IntelliJ IDEA.
  User: Aziz
  Date: Oct 02, 2009
  Time: 4:13:22 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<% String hostName = (request.getAttribute("hostName") != null) ? request.getAttribute("hostName").toString() : request.getServerName();
    boolean isCompanyDomain = EdsContextParams.getCompanyDomain(request.getServerName());

    if (hostName != null && hostName.contains("ezyadmin")) { %>

<c:set var="analytics_code" value="UA-59981695-14"/>

<%} else if (hostName != null && hostName.contains("smebu")) { %>

<c:set var="analytics_code" value="UA-59981695-3"/>

<%} else if (hostName != null && hostName.contains("tjilo")) { %>

<c:set var="analytics_code" value="UA-59981695-19"/>

<%} else if (hostName != null && hostName.contains("omandatapark.com")) { %>

<c:set var="analytics_code" value="UA-59981695-9"/>

<%} else if (hostName != null && hostName.contains("preprod.kpi.com")) { %>

<c:set var="analytics_code" value="UA-355982-15"/>

<%} else if (hostName != null && hostName.contains("kpi.com.ru")) { %>

<c:set var="analytics_code" value="UA-59981695-6"/>

<%} else if (hostName != null && hostName.contains("iisholding.com")) { %>

<c:set var="analytics_code" value="UA-59981695-15"/>

<%} else if (hostName != null && hostName.contains("appiev.com")) { %>

<c:set var="analytics_code" value="UA-59981695-13"/>

<%} else if (hostName != null && hostName.contains("kah.sa")) { %>

<c:set var="analytics_code" value="UA-59981695-4"/>

<%} else if (hostName != null && hostName.contains("unisyserp.com")) { %>

<c:set var="analytics_code" value="UA-59981695-17"/>

<%} else if (hostName != null && hostName.contains("goodsystems.com.au")) { %>

<c:set var="analytics_code" value="UA-59981695-22"/>

<%} else if (hostName != null && (hostName.contains("upshott.com") || hostName.contains("erp.com") || hostName.contains("erp.ae"))) { %>

<c:set var="analytics_code" value="UA-59981695-16"/>

<%} else if (hostName != null && hostName.contains("talibro")) { %>

<c:set var="analytics_code" value="UA-43168248-1"/>

<%} else if (isCompanyDomain) { %>

<c:set var="analytics_code" value="UA-355982-15"/>

<%} else if (hostName != null && hostName.contains("financialit")) { %>

<c:set var="analytics_code" value="UA-355982-15"/>

<%} else if (hostName != null && hostName.contains("technosolutions")) { %>

<c:set var="analytics_code" value="UA-59981695-5"/>

<%} else if (hostName != null && hostName.contains("zairzaidi")) { %>

<c:set var="analytics_code" value="UA-59981695-10"/>

<%} else if (hostName != null && hostName.contains("basilurarabia")) { %>

<c:set var="analytics_code" value="UA-59981695-8"/>

<%} else if (hostName != null && hostName.contains("mynfra")) { %>

<c:set var="analytics_code" value="UA-59981695-12"/>

<%} else if (hostName != null && hostName.contains("fairtradeengergy")) { %>

<c:set var="analytics_code" value="UA-355982-15"/>

<%} else if (hostName != null && hostName.contains("stefanodesigns")) { %>

<c:set var="analytics_code" value="UA-59981695-11"/>

<%} else if (hostName != null && hostName.contains("b2xcg.com")) { %>

<c:set var="analytics_code" value="UA-59981695-7"/>

<%} else if (hostName != null && hostName.contains("vipworkspace")) { %>

<c:set var="analytics_code" value="UA-59981695-18"/>

<%} else if (hostName != null && hostName.contains("mykidstale")) { %>

<c:set var="analytics_code" value="UA-355982-15"/>

<%} else if (hostName != null && hostName.contains("enfion.com")) { %>

<c:set var="analytics_code" value="UA-59981695-21"/>

<%} else if (hostName != null && hostName.contains("1erp")) { %>

<c:set var="analytics_code" value="UA-355982-15"/>

<%} else if (hostName != null && hostName.contains("idaaerpservices")) { %>

<c:set var="analytics_code" value="UA-59981695-20"/>

<%} else if (hostName != null && hostName.contains("kg.kpi")) { %>

<c:set var="analytics_code" value="UA-59981695-23"/>

<%} else { %>

<c:set var="analytics_code" value="UA-355982-15"/>

<% } %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title><fmt:message key="removedfrommailinglist.title"/> </title>
    <link href="/choosecompany/css.css" rel="stylesheet" type="text/css"/>


    <script type="text/javascript">
        function setCheked(id) {
            form = document.getElementById('ddd');
            data = document.createElement('input');
            data.type = 'hidden';
            data.name = 'id';
            data.value = id;
            form.appendChild(data);
            form.submit();
        }
    </script>

</head>
<body>
<div id="wrapper">
    <b class="spiffy">
        <b class="spiffy1"><b></b></b><b class="spiffy2"><b></b></b><b class="spiffy3"></b>
        <b class="spiffy4"></b>
        <b class="spiffy5"></b></b>
    <form:form method="post" action="/check" id="ddd">
        <div class="spiffyfg">
            <h3><fmt:message key="removedfrommailinglist.youHaveBeenSuccessfullyRemovedFromMailingList"/> </h3>
        </div>



    </form:form>

    <b class="spiffy">
        <b class="spiffy5"></b>

        <b class="spiffy4"></b>
        <b class="spiffy3"></b>
        <b class="spiffy2"><b></b></b>
        <b class="spiffy1"><b></b></b></b>

    <%--<div id="footer">
        <ul>
            <li><a href="<%=Constants.DRUPAL_DOMEN%>content/privacy">Privacy Policy |</a></li>
            <li><a href="<%=Constants.DRUPAL_DOMEN%>content/terms-of-use">Terms of Use |</a></li>
            <li><a href="<%=Constants.DRUPAL_DOMEN%>content/about">About Us |</a></li>
            <li><a href="http://workforcetrack.blogspot.com/">Contact Us</a></li>
        </ul>
        <p>Copyright &copy; 2007-
           <script type="text/javascript">
               <!--
               var currentTime = new Date()
               var year = currentTime.getFullYear()
               document.write(year)
                   //-->
           </script> Finnet Limited.</p>
    </div>--%>
</div>
<!--New Google Analytics script-->
<script type="text/javascript">
    (function (i, s, o, g, r, a, m) {
        i['GoogleAnalyticsObject'] = r;
        i[r] = i[r] || function () {
            (i[r].q = i[r].q || []).push(arguments)
        }, i[r].l = 1 * new Date();
        a = s.createElement(o), m = s.getElementsByTagName(o)[0];
        a.async = 1;
        a.src = g;
        m.parentNode.insertBefore(a, m)
    })(window, document, 'script', '//www.google-analytics.com/analytics.js', 'ga');
    ga('create', '<c:out value="${analytics_code}"/>', 'auto');
    ga('send', 'pageview');
</script>
<!--New Google Analytics script-->


</body>
</html>
