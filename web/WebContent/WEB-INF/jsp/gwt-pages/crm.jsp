<%--
  Created by IntelliJ IDEA.
  User: Sherali
  Date: 07-Jul-2009
  Time: 15:44:16
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<tiles:insertDefinition name="mainLayout">
    <tiles:putAttribute name="loading_message">
        <fmt:message key="main.loading"/>
    </tiles:putAttribute>
    <tiles:putAttribute name="script">
        <script language='javascript' src='crm/crm.nocache.js'></script>
        <script type="text/javascript">
            var showContextMenu = true;
            document.oncontextmenu = function() {
                temp = showContextMenu;
                showContextMenu = true;
                return temp;
            }
            var setShowContextMenu = function(bool) {
                showContextMenu = bool;
            }
        </script>
    </tiles:putAttribute>
</tiles:insertDefinition>