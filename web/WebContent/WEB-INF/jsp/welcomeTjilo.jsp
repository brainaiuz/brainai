<%@ page import="com.edatasite.workforce.gwt.core.client.ui.Constants" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%--
  Created by IntelliJ IDEA.
  User: Ilhombek
  Date: 5/13/11
  Time: 7:08 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<tiles:insertDefinition name="frontEndLayoutNew">
<tiles:putAttribute name="title">
    <fmt:message key="welcome.title"/> ${productName}
</tiles:putAttribute>
<tiles:putAttribute name="style">
    <link href="/landing/css/welcomepage.css" rel="stylesheet" type="text/css">
    <link rel="shortcut icon" href="/customisation/${productNameLower}/images/favicon.ico" type="image/x-icon"/>

</tiles:putAttribute>
<tiles:putAttribute name="body">
    <script type="text/javascript">

        setTimeout("redirect()", 10000);

        function getCookie(c_name) {
            var i,x,y,ARRcookies = document.cookie.split(";");
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

    <div id="contwrap" align="center">
    <div id="contbody" align="center">
        <div id="main">
            <%--<div style="float: left; width: 559px; height: 162px; margin: 7px 0 0;--%>
            <%--background: #fff url(/customisation/${productNameLower}/images/cot-banner.png) no-repeat">--%>
            <%--</div>--%>

            <%--<div style="float: right; height: 147px; width: 242px; margin: 16px 0 0 0;--%>
            <%--background: url(/customisation/${productNameLower}/images/about-map.png) no-repeat left top">--%>
            <%--</div>--%>


            <div class="thanks">
                <h2>
                 <fmt:message key="welcomepage.thankYoForRegisteringAt"/> ${helpHost} !
                </h2>

                <div align="left" style="padding-left: 20px;">

                    <ul class="welcomeul">
                        <li>
                            <fmt:message key="welcomepage.youWillBeRedirectedIntoYour"/>
                            <fmt:message key="welcomepage.accountIn10SecondsOrPleaseClick"/>
                                <a href="javascript:redirect()" class="aclass">
                                    <fmt:message key="welcome.here"/>
                                </a>
                            <fmt:message key="welcome.youWillBeRedirected"/>
                        </li>
                        <li><fmt:message key="welcome.pleaseFollowTheLink"/>
                        </li>
                        <li><fmt:message key="welcome.checkyourjunkorspamfolder"/>
                        </li>
                    </ul>
                    <br/>

                </div>
            </div>
            <!--/thanks-->

            <DIV id="contacts">

                <DIV class="cover"><!--left border-->
                    <div class="cover"><!--right border-->
                        <h3 class="h3class"><fmt:message key="welcome.questions"/></h3>
                        <TABLE>
                            <TBODY>
                            <TR>
                                <TD style="width: 80px; height: 25px;"><STRONG><fmt:message key="pagenotfound.email"/> </STRONG></TD>
                                <TD><A href="mailto:${email}" class="aclass">
                                ${email}
                                </A>
                                </TD>
                            </TR>
                            <%--<TR>--%>
                                <%--<TD style="width: 80px; height: 25px;"><STRONG><fmt:message key="pagenotfound.skype"/> </STRONG></TD>--%>
                                <%--<TD> ${skype}--%>
                                <%--</TD>--%>
                            <%--</TR>--%>
                            <TR>
                                <TD style="width: 80px; height: 25px;"><STRONG><fmt:message key="pagenotfound.phone"/> </STRONG></TD>
                                <TD> ${supportPhone}
                                </TD>
                            </TR>

                            </TBODY>
                        </TABLE>






                    </DIV>
                </DIV>
            </DIV>

        </div>
        <!--end main-->
    </div>
</div>

</tiles:putAttribute>

<tiles:putAttribute name="script">

    <script type="text/javascript">

    </script>

</tiles:putAttribute>


</tiles:insertDefinition>