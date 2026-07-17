<%@ page import="com.edatasite.workforce.gwt.core.client.ui.Constants" %><%--
  Created by IntelliJ IDEA.
  User: Aziz
  Date: Oct 02, 2009
  Time: 4:13:22 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<tiles:insertDefinition name="frontEndLayoutNew">
    <tiles:putAttribute name="title">
        <fmt:message key="choosecompany.title"/> ${productName}
    </tiles:putAttribute>
    <tiles:putAttribute name="style">
        <link href="/choosecompany/choose.css" rel="stylesheet" type="text/css"/>
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
    </tiles:putAttribute>
    <tiles:putAttribute name="body">
        <script type="text/javascript">

            setTimeout("redirect()", 10000);

            function getCookie(c_name) {
                var i, x, y, ARRcookies = document.cookie.split(";");
                for (i = 0; i < ARRcookies.length; i++) {
                    x = ARRcookies[i].substr(0, ARRcookies[i].indexOf("="));
                    y = ARRcookies[i].substr(ARRcookies[i].indexOf("=") + 1);
                    x = x.replace(/^\s+|\s+$/g, "");
                    if (x == c_name) {
                        return unescape(y);
                    }
                }
            }

            function redirect() {
                var section = getCookie('SECTION_HTML');
                var path = "<%=Constants.DEFAULT_SECTION%>.html";
                //alert(section);
                if (section != null && section.match("html") != null) {
                    path = section;
                }
                window.location = '/' + path;
            }
            var isInIFrame = (window.location != window.parent.location) ? true : false;
            if (isInIFrame) {
                parent.location = document.location;
            }
        </script>

        <script type="text/javascript">
            if (!window.mstag) mstag = {loadTag:function () {
            }, time:(new Date()).getTime()};

        </script>

        <div id="index-page" style="width:860px; margin: 10px auto;">
            <div class="spiffyfg">
                <h2 class="title" style="text-align:center;font-size:22px;"><fmt:message
                        key="choosecompany.pleaseChooseCompany"/> <a href="javascript:redirect()" class="aclass">redirect </a>
                </h2>

                <div id="grey">
                    <div id="ingrey">
                        <ul>
                            <c:forEach var="companyItem" items="${companyList}">
                                <li>
                                    <a href="${companyItem.clusterURL}&ACCOUNT_TYPE=${ACCOUNT_TYPE}&IS_MULTI_COMPANY=${IS_MULTI_COMPANY}"><img
                                            src="${companyItem.logo}" alt=""></a>
                                    <a href="${companyItem.clusterURL}&ACCOUNT_TYPE=${ACCOUNT_TYPE}&IS_MULTI_COMPANY=${IS_MULTI_COMPANY}"
                                       class="align">${companyItem.companyName}</a>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>

                </div>
            </div>

                <%--<input type="hidden" name="loginType" value="${loginType}"/>--%>
                <%--<input type="hidden" name="action" value="handleChooseCompanySubmit"/>--%>
                <%--</form:form>--%>
        </div>

    </tiles:putAttribute>

</tiles:insertDefinition>