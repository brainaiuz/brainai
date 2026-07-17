<%@ page import="com.edatasite.workforce.gwt.core.client.ui.Constants" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<tiles:insertDefinition name="frontEndLayoutNew">
    <tiles:putAttribute name="title">
        <fmt:message key="welcome.title"/> ${productName}
    </tiles:putAttribute>
    <tiles:putAttribute name="style">
        <link rel="shortcut icon" href="/customisation/${productNameLower}/images/favicon.ico" type="image/x-icon"/>
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
                var path = "Settings.html";
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
        <script type="text/javascript"> if (!window.mstag) mstag = {loadTag:function () {
        }, time:(new Date()).getTime()};</script>
        <script
                id="mstag_tops" type="text/javascript"
                src="//flex.atdmt.com/mstag/site/3386f3c4-86c8-40ba-8017-0b8c19c36e07/mstag.js"></script>
        <script type="text/javascript"> mstag.loadTag("conversion",
                {cp:"5050", dedup:"1"})</script>

        <div id="contwrap" align="center">
            <div id="contbody" align="center">
                <div id="main">
                    <div class="thanks">
                        <div align="left" style="padding-left: 20px;">

                            <ul class="welcomeul">
                                <li>
                                    <fmt:message key="successpassword.successPasswordExpiration"/>
                                    <a href="javascript:redirect()" class="aclass">
                                        <fmt:message key="welcome.here"/>
                                    </a>
                                </li>
                            </ul>
                            <br/>
                        </div>
                    </div>
                    <!--/thanks-->
                </div>
                <!--end main--></div>
        </div>

    </tiles:putAttribute>
</tiles:insertDefinition>