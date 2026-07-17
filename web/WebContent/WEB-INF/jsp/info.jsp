<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 25.11.2010
  Time: 20:01:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<tiles:insertDefinition name="frontEndLayoutNew">

    <tiles:putAttribute name="title">
        Google Talk Error
    </tiles:putAttribute>
    <tiles:putAttribute name="style">

        <link href="/customisation/preprod.kpi.com/style.css" type="text/css" rel="stylesheet">
        <script src="/customisation/preprod.kpi.com/scripts/jquery.js?ver=1.7.2" type="text/javascript"></script>
        <%--<script src="//kpi.com/sites/all/themes/wft/jquery.cookie.js"></script>--%>
        <SCRIPT type=text/javascript src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.4/jquery.min.js"></SCRIPT>
        <SCRIPT type=text/javascript src="/customisation/kpi.com/jquery.placeholder.min.js"></SCRIPT>

        <SCRIPT type=text/javascript>
            jQuery(function () {

                jQuery('input[placeholder], textarea[placeholder]').placeholder();


            });
        </SCRIPT>

        <meta name="google-site-verification" content="jjw1ZnV5AryaQZs-KB64fCH3EpvkT94GKJAoFvLFzbU"/>
        <meta name="msvalidate.01" content="358DD608AB06D6ED2E842AF53CAFFB9E"/>
        <META name="y_key" content="d766be7156d8deef">

    </tiles:putAttribute>
    <tiles:putAttribute name="body">

        <%----------------------%>
        <%--- BEGIN content page---%>
        <div id="contentPlace" class="clear-block">

            <!-- Start MAIN -->
            <div id="main">
                <h2>Google Talk Error</h2><br>

                <p>
                    Error occured while signing in to Google Talk, please contact ${email} for issue resolution.
                </p>

            </div>
            <br><br><br>

            <div class="clearBox"></div>
            <!--End #main -->


        </div>
        <%----END content page----%>
        <%----------------------%>


    </tiles:putAttribute>
    <tiles:putAttribute name="script">

        <%--End of Drupal Content--%>
    </tiles:putAttribute>
</tiles:insertDefinition>