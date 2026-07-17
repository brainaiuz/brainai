<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<tiles:insertDefinition name="frontEndLayoutNew">
    <tiles:putAttribute name="title">
        <fmt:message key="event.calendarInvitationTitle"/> ${helpHost}
    </tiles:putAttribute>
    <tiles:putAttribute name="style">
        <script type="text/javascript">

            var _gaq = _gaq || [];
            _gaq.push(['_setAccount', 'UA-355982-15']);
            _gaq.push(['_trackPageview']);

            (function() {
                var ga = document.createElement('script');
                ga.type = 'text/javascript';
                ga.async = true;
                ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
                var s = document.getElementsByTagName('script')[0];
                s.parentNode.insertBefore(ga, s);
            })();

        </script>
    </tiles:putAttribute>
    <tiles:putAttribute name="body">
        <div id="index-page" style="width:860px;">
            <h2 class="title">
                <fmt:message key="event.calendarInvitation"/>
            </h2>

            <p>
                <b>
                <fmt:message key="event.yourResponsehasbeensuccessfullysaved"/>
                </b>
            </p>

            <p>&nbsp;</p>

            <p><strong> <fmt:message key="pagenotfound.email"/>
                 </strong> <a href="mailto:${email}"> ${email} </a></p>

            <p><strong> <fmt:message key="pagenotfound.skype"/> </strong> ${skype} </p>

            <p><strong> <fmt:message key="pagenotfound.phone"/> </strong> ${supportPhone} </p>

        </div>

    </tiles:putAttribute>

</tiles:insertDefinition>