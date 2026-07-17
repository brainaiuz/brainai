<%@ page import="com.edatasite.workforce.gwt.core.server.app.BrowserSupportUtils" %>
<%@ page import="org.springframework.web.servlet.support.RequestContext" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<% String hostName = (request.getAttribute("hostName") != null) ? request.getAttribute("hostName").toString() : request.getServerName();
    RequestContext rc = new RequestContext(request);
    boolean isRussian = rc != null && rc.getLocale() != null && "ru".equals(rc.getLocale().getLanguage());
    boolean isRussianHost = request.getHeader("host") != null && request.getHeader("host").toLowerCase().contains(".ru") || request.getParameterMap().get("locale") != null && request.getParameter("locale").toLowerCase().contains("ru");
    boolean isSupportedIE8 = BrowserSupportUtils.isSupportedIE8(request.getHeader("user-agent"));
    String facebookAppID = EdsContextParams.getFacebookAppID(request.getServerName());
    boolean isCompanyDomain = EdsContextParams.getCompanyDomain(request.getServerName());

    if (hostName != null && hostName.contains("ezyadmin")) { %>

<%@include file="templateEzyadmin.jsp" %>

<%} else if (hostName != null && hostName.contains("smebu")) { %>

<%@include file="templateSmebu.jsp" %>

<%} else if (hostName != null && hostName.contains("tjilo")) { %>

<%@include file="templateTjilo.jsp" %>

<%} else if (hostName != null && hostName.contains("omandatapark.com")) { %>

<%@include file="templateODP.jsp" %>

<%} else if (hostName != null && hostName.contains("preprod.kpi.com")) { %>

<%@include file="templatePreprodKpiCom.jsp" %>

<%} else if (hostName != null && hostName.contains("kpi.com.ru")) { %>

<%@include file="templateWorkforceRu.jsp" %>

<%} else if (hostName != null && hostName.contains("iisholding.com")) { %>

<%@include file="templateIISholding.jsp" %>

<%} else if (hostName != null && hostName.contains("appiev.com")) { %>

<%@include file="templateAppiev.jsp" %>

<%} else if (hostName != null && hostName.contains("kah.sa")) { %>

<%@include file="templateKah.jsp" %>

<%} else if (hostName != null && hostName.contains("unisyserp.com")) { %>

<%@include file="templateUnisyserp.jsp" %>

<%} else if (hostName != null && hostName.contains("activira.com")) { %>

<%@include file="templateActivira.jsp" %>

<%} else if (hostName != null && hostName.contains("goodsystems.com.au")) { %>

<%@include file="templateGoodSystems.jsp" %>

<%} else if (hostName != null && (hostName.contains("upshott.com") || hostName.contains("erp.com") || hostName.contains("erp.ae")) && !hostName.contains("passionerp.com")) { %>

<%@include file="templateUpshot.jsp" %>

<%} else if (hostName != null && hostName.contains("talibro")) { %>

<%@include file="templateTalibro.jsp" %>

<%} else if (isCompanyDomain) { %>

<%@include file="templateCustomPages.jsp" %>

<%} else if (hostName != null && hostName.contains("financialit")) { %>

<%@include file="templateFIT.jsp" %>

<%} else if (hostName != null && (hostName.contains("technosolutions") || hostName.contains("zairzaidi") || hostName.contains("basilurarabia"))) { %>

<%@include file="templateTechnoso.jsp" %>

<%} else if (hostName != null && hostName.contains("mynfra")) { %>

<%@include file="templateMynfra.jsp" %>

<%} else if (hostName != null && hostName.contains("fairtradeengergy")) { %>

<%@include file="templateFairTradeEngergy.jsp" %>

<%} else if (hostName != null && hostName.contains("stefanodesigns")) { %>

<%@include file="templateStefano.jsp" %>

<%}
else if (hostName != null && hostName.contains("almasaood")) { %>

<%@include file="templateAlmasaood.jsp" %>

<%}
else if (hostName != null && hostName.contains("b2xcg.com")) { %>

<%@include file="templateBXERP.jsp" %>

<%} else if (hostName != null && hostName.contains("vipworkspace")) { %>

<%@include file="templateVipworkspace.jsp" %>

<%} else if (hostName != null && hostName.contains("mykidstale")) { %>

<%@include file="templateMykidstale.jsp" %>

<%} else if (hostName != null && hostName.contains("enfion.com")) { %>

<%@include file="templateEnfion.jsp" %>

<%} else if (hostName != null && hostName.contains("1erp")) { %>

<%@include file="template1ErpSa.jsp" %>

<%} else if (hostName != null && hostName.contains("idaaerpservices")) { %>

<%@include file="templateIdaa.jsp" %>

<%} else if (hostName != null && hostName.contains("new.kpi")) { %>

<%@include file="templateNewKpi.jsp" %>

<%} else if (hostName != null && hostName.contains("alkawader.com")) { %>

<%@include file="templateAlKawader.jsp" %>

<%} else if (hostName != null && hostName.contains("alfursanrecruitment.ae")) { %>

<%@include file="templateAlfursanrecruitment.jsp" %>

<%} else if (hostName != null && hostName.contains("genesis-gifts")) { %>

<%@include file="templateGenesisGifts.jsp" %>

<%} else if (hostName != null && hostName.contains("passionerp.com")) { %>

<%@include file="templatePassionerp.jsp" %>

<%} else if (hostName != null && hostName.contains("ebmconsultant.com")) { %>

<%@include file="templateEmbc.jsp" %>

<%} else if (hostName != null && hostName.contains("kpi.developmentlogix.com")) { %>

<%@include file="templateDevelopmentlogix.jsp" %>

<%} else if (hostName != null && hostName.contains("gh.kpi")) { %>

<%@include file="templateGh.jsp" %>

<%} else if (hostName != null && hostName.contains("namangan.kpi")) { %>

<%@include file="templateNamangan.jsp" %>

<%} else if (hostName != null && hostName.contains("texnopark.kpi")) { %>

<%@include file="templateArtel.jsp" %>
<%} else if (hostName != null && hostName.contains("yuborexpress.kpi")) { %>

<%@include file="templateYuborUz.jsp" %>
<%} else if (hostName != null && hostName.contains("akfa.kpi")) { %>

<%@include file="templateArtel.jsp" %>
<%} else if (hostName != null && hostName.contains("orient.kpi")) { %>

<%@include file="templateArtel.jsp" %>
<%} else if (hostName != null && hostName.contains("nestle.kpi")) { %>

<%@include file="templateNestle.jsp" %>

<%} else if (hostName != null && hostName.contains("apps.folioone")) { %>

<%@include file="templateFolioone.jsp" %>

<%} else if (hostName != null && (hostName.contains("gym.kpi") || hostName.contains("praaktisgo"))) { %>

<%@include file="templateGym.jsp" %>
<%} else if (hostName != null && (hostName.contains("brainbm"))) { %>

<%@include file="templateBrain.jsp" %>
<%} else if (hostName != null && (hostName.contains("hotel"))) { %>

<%@include file="templateHotel.jsp" %>
<%} else if (hostName != null && (hostName.contains("tendergpt"))) { %>

<%@include file="tendergpt.jsp" %>
<%} else if (hostName != null && hostName.contains("zeta")) { %>

<%@include file="templateZpw.jsp" %>
<%} else if (hostName != null && hostName.contains("agrobank.uz")) { %>

<%@include file="templateAgroBank.jsp" %>
<%} else { %>

<%@include file="templateKpi.jsp" %>

<% } %>

<%if (request.getRequestURI().endsWith("index.jsp")) {%>

<%--Facebook Script Begin--%>
<div id="fb-root"></div>
<script>(function (d, s, id) {
    var js, fjs = d.getElementsByTagName(s)[0];
    if (d.getElementById(id)) return;
    js = d.createElement(s);
    js.id = id;
    js.src = "//connect.facebook.net/en_US/all.js#xfbml=1&appId=<%=facebookAppID%>";
    fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));</script>

<% } %>
