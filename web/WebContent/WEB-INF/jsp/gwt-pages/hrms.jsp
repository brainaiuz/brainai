<%--
  Created by IntelliJ IDEA.
  User: Sherali
  Date: 14-oct-2009
  Time: 19:13:16
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<tiles:insertDefinition name="mainLayout">
    <tiles:putAttribute name="loading_message">
        <fmt:message key="main.loading"/>
    </tiles:putAttribute>
    <tiles:putAttribute name="script">
        <script language='javascript' src='hrms/hrms.nocache.js'></script>
    </tiles:putAttribute>
</tiles:insertDefinition>
