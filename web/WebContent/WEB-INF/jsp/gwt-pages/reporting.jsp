<%--
  Created by IntelliJ IDEA.
  User: Virus
  Date: 2/10/12
  Time: 2:54 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<tiles:insertDefinition name="mainLayout">
    <tiles:putAttribute name="loading_message">
        <fmt:message key="main.loading"/>
    </tiles:putAttribute>
    <tiles:putAttribute name="script">
        <script language='javascript' src='reportingsystem/reportingsystem.nocache.js'></script>
    </tiles:putAttribute>
</tiles:insertDefinition>