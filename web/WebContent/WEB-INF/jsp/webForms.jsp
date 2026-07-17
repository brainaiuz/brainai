<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.edatasite.workforce.appContext.SpringPropertiesUtil" %>
<%@ page import="com.edatasite.workforce.gwt.core.client.rpc.module.WfmModuleSettingConstants" %>
<%@ page import="com.edatasite.workforce.gwt.core.client.ui.Constants" %>
<%@ page import="com.edatasite.workforce.gwt.core.client.ui.UiSettings" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    String showHeaderParam = request.getParameter(WfmModuleSettingConstants.SHOWHEADER.getName());
    if (showHeaderParam == null) {
        showHeaderParam = "true";
    }
    Boolean isShowHeader = Boolean.valueOf(showHeaderParam);
    Boolean showGoogleTalkChat = Boolean.valueOf(String.valueOf(request.getAttribute(Constants.SHOW_GOOGLE_TALK_CHAT)));
    String parentStyle = request.getParameter(WfmModuleSettingConstants.STYLE.getName());
    if (parentStyle == null || parentStyle.equals("null")) {
        parentStyle = (String) request.getAttribute(Constants.THEME_FOR_SYSTEM);
    }
    if ("".equals(parentStyle) || parentStyle == null || parentStyle.equals("null")) {
        parentStyle = UiSettings.BLUE_THEME;
    }
    String hostLanguageForUser = (String) request.getAttribute(Constants.LANGUAGE_FOR_USER);
    String activeMenu = (String) request.getAttribute(Constants.ACTIVE_MENU);
    String faviconPath = (String) request.getAttribute("productName");
    String hostName = (String) request.getAttribute("hostName");
    faviconPath = faviconPath != null ? faviconPath.toLowerCase() : "kpi";

    boolean isArabic = "ar".equals(request.getAttribute("LANGUAGE_FOR_USER")) || "he".equals(request.getAttribute("LANGUAGE_FOR_USER"));
    String version = SpringPropertiesUtil.getProperty("cssVersion");%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" type="text/css" href="/mainStyles/custom-webform.css"/>
    <script type="text/javascript" language="javascript">
        var parentRedirect = function (url) {
            parent.location.href = url;
        }
    </script>
    <link rel="shortcut icon" href="/customisation/<%=faviconPath%>/images/favicon.ico" type="image/x-icon"/>
    <link rel="P3Pv1" href="/w3c/p3p.xml">
    <link rel="apple-touch-icon" href="/mobile_sources/images/apple-touch-icon.png" type="image/x-icon">
    <script type="text/javascript" src="/mainStyles/js/jquery-3.2.1.min.js"></script>
    <script src="/mainStyles/bootstrap/js/transition.js" type="application/javascript"></script>
    <script src="/mainStyles/bootstrap/js/tab.js" type="application/javascript"></script>
    <script src="/mainStyles/bootstrap/js/collapse.js" type="application/javascript"></script>

    <script src="/mainStyles/bootstrap/js/switch_stage.js" type="application/javascript"></script>
    <script src="/mainStyles/bootstrap/js/anti_scroll_x.js" type="application/javascript"></script>

    <script src="/mainStyles/bootstrap/js/btn_toggle.js" type="application/javascript"></script>


    <script src="/mainStyles/bootstrap/js/scrollArea_pageControls.js" type="application/javascript"></script>
    <script src="/mainStyles/bootstrap/js/dropdown.js" type="application/javascript"></script>
    <script src="/mainStyles/bootstrap/js/jquery.vibrate.min.js" type="application/javascript"></script>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js" type="application/javascript"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js" type="application/javascript"></script>
<%--    <link rel="stylesheet" href="/mainStyles/new-ui/css/materialize.css?v=<%=version%>">--%>
    <%--<link rel="stylesheet" href="/mainStyles/new-ui/css/kpitables.css?v=<%=version%>">--%>
<%--
    <link rel="stylesheet" href="/mainStyles/new-ui/css/temp.css?v=<%=version%>">
--%>
<%--
    <link rel="stylesheet" href="/mainStyles/new-ui/css/theme.css?v=<%=version%>">
--%>
<%--
    <link rel="stylesheet" href="/mainStyles/new-ui/css/invoice.css?v=<%=version%>">
--%>
<%--
    <link rel="stylesheet" href="/mainStyles/new-ui/css/dynamictable.css?v=<%=version%>">
--%>
<%--
    <link rel="stylesheet" href="/mainStyles/reporting/reporting.css" type="text/css"/>
--%>
<%--
    <link rel="stylesheet" href="/mainStyles/new-ui/css/transition.css">
--%>
    <%--<link rel="stylesheet" href="/mainStyles/new-ui/css/jquery.scrollbar.css">--%>
<%--
    <link rel="stylesheet" href="/mainStyles/new-ui/css/dashboard.grid.css?v=<%=version%>">
--%>
</head>
<body style="overflow-y: scroll;" class="cm-webform__wrapper">
<%--<script type="text/javascript" src="/mainStyles/new-ui/js/jquery.scrollbar.min.js"></script>--%>
<script language="javascript" src="webforms/webforms.nocache.js"></script>
<iframe id="__gwt_historyFrame" style="position: absolute; left: -1000px; top: 0; right: 0; bottom: 0;"></iframe>

<%--Unsubscribe form--%>
<%--<div class="cm-webform unsubscribe">
    <div class="cm-webform__header">
        <svg class="kpi-logo" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" width="55.2" height="68.4"
             viewBox="0 0 46 57">
            <defs>
                <polygon id="new_logo-a" points=".002 .67 45.633 .67 45.633 56.692 .002 56.692"></polygon>
            </defs>
            <g fill="none" fill-rule="evenodd" transform="translate(0 -1)">
                <path fill="#354049"
                      d="M40.197,57.2101 L10.269,50.0971 C8.235,49.6141 6.798,47.8021 6.798,45.7171 L6.798,21.4441 C6.798,19.3591 8.235,17.5471 10.269,17.0641 L39.972,10.0051 C41.436,9.6571 43.032,9.9331 44.151,10.9351 C45.129,11.8111 45.633,13.0111 45.633,14.2291 L45.633,52.5991 C45.633,54.5461 44.523,56.4391 42.681,57.0841 C41.871,57.3661 41.01,57.4021 40.197,57.2101"
                      opacity=".2"></path>
                <path fill="#354049" fill-opacity=".2"
                      d="M10.269,8.2327 C8.235,8.7157 6.798,10.5277 6.798,12.6127 L6.798,36.8887 C6.798,38.9707 8.235,40.7827 10.269,41.2657 L39.972,48.3247 C41.436,48.6727 43.032,48.3997 44.151,47.3947 C45.129,46.5187 45.633,45.3187 45.633,44.1007 L45.633,5.7307 C45.633,3.7837 44.523,1.8907 42.681,1.2457 C41.871,0.9637 41.01,0.9277 40.197,1.1197 L10.269,8.2327 Z"></path>
                <g transform="translate(0 .33)">
                    <mask id="new_logo-b" fill="#fff">
                        <use xlink:href="#new_logo-a"></use>
                    </mask>
                    <path fill="#354049"
                          d="M6.3885,0.8109 L41.8275,9.2649 C44.0595,9.7959 45.6345,11.7909 45.6345,14.0859 L45.6345,43.2639 C45.6345,45.5589 44.0595,47.5539 41.8275,48.0849 L6.4155,56.5299 C5.2335,56.8119 3.9645,56.7459 2.8905,56.1759 C0.9945,55.1739 0.0015,53.3469 0.0015,51.4929 L0.0015,5.9829 C0.0015,4.6179 0.4455,3.2469 1.4055,2.2779 C2.8035,0.8679 4.6875,0.4059 6.3885,0.8109"
                          mask="url(#new_logo-b)"></path>
                </g>
                <path fill="#FFF"
                      d="M18.0999 35.5921C17.7879 35.9011 17.3499 36.0841 16.9389 36.0841 16.5489 36.0841 16.1889 35.9551 15.8799 35.6701L10.6089 30.8131 10.6089 34.6621C10.6089 35.5411 9.9129 36.2371 9.0369 36.2371 8.1579 36.2371 7.4589 35.5411 7.4589 34.6621L7.4589 18.2131C7.4589 17.3341 8.1579 16.6351 9.0369 16.6351 9.9129 16.6351 10.6089 17.3341 10.6089 18.2131L10.6089 26.9401 15.6969 23.4301C16.4199 22.9381 17.4039 23.0911 17.8929 23.8141 18.4119 24.5131 18.2289 25.4941 17.5329 26.0101L13.2189 28.9291 18.0219 33.3451C18.6429 33.9391 18.6939 34.9201 18.0999 35.5921M30.0561 29.704C30.0561 27.82 28.5051 26.296 26.6481 26.296 24.7611 26.296 23.2401 27.82 23.2401 29.704 23.2401 31.588 24.7611 33.112 26.6481 33.112 28.5051 33.112 30.0561 31.588 30.0561 29.704M33.1821 29.704C33.1821 33.319 30.2391 36.238 26.6481 36.238 25.3041 36.238 24.0651 35.824 23.0061 35.152L23.0061 41.197C23.0061 42.073 22.3071 42.772 21.4551 42.772 20.5791 42.772 19.8801 42.073 19.8801 41.197L19.8801 24.772C19.8801 23.92 20.5791 23.221 21.4551 23.221 22.1541 23.221 22.7481 23.686 22.9551 24.307 23.9871 23.584 25.2801 23.17 26.6481 23.17 30.2391 23.17 33.1821 26.089 33.1821 29.704M36.5979 20.9749C35.6949 20.9749 34.9719 20.2519 34.9719 19.3999L34.9719 19.2709C34.9719 18.3679 35.6949 17.6689 36.5979 17.6689 37.4499 17.6689 38.1729 18.3679 38.1729 19.2709L38.1729 19.3999C38.1729 20.2519 37.4499 20.9749 36.5979 20.9749M36.5979 36.1339C35.6949 36.1339 34.9719 35.4109 34.9719 34.5319L34.9719 24.5659C34.9719 23.6869 35.6949 22.9879 36.5979 22.9879 37.4499 22.9879 38.1729 23.6869 38.1729 24.5659L38.1729 34.5319C38.1729 35.4109 37.4499 36.1339 36.5979 36.1339"></path>
            </g>
        </svg>

        <hgroup>
&lt;%&ndash;            <h1>Request cancelled, you can now close this window</h1>&ndash;%&gt;
            <h1>Unsubscribe from our mailing list?</h1>
            <h2>Do you really want to unsubscribe the following email address?</h2>
        </hgroup>
    </div>

    <div class="cm-webform__body">
        <div class="form-group">
            <label class="form-group__label">Email:</label>
            <div class="form-group__content">
                <input class="form-control default-width" type="text" maxlength="255" autocomplete="off" />
            </div>
        </div>
    </div>

    <div class="cm-webform__footer">
        <div class="stack-x">
            <button class="btn btn--primary"><span>Yes, unsubscribe</span></button>
            <button class="btn btn--default"><span>No, cancel</span></button>
        </div>
    </div>
</div>--%>

<%--Candidates form--%>
<%--<div class="cm-webform">
    <div class="cm-webform__header">
        <svg class="kpi-logo" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" width="55.2" height="68.4"
             viewBox="0 0 46 57">
            <defs>
                <polygon id="new_logo-a" points=".002 .67 45.633 .67 45.633 56.692 .002 56.692"></polygon>
            </defs>
            <g fill="none" fill-rule="evenodd" transform="translate(0 -1)">
                <path fill="#354049"
                      d="M40.197,57.2101 L10.269,50.0971 C8.235,49.6141 6.798,47.8021 6.798,45.7171 L6.798,21.4441 C6.798,19.3591 8.235,17.5471 10.269,17.0641 L39.972,10.0051 C41.436,9.6571 43.032,9.9331 44.151,10.9351 C45.129,11.8111 45.633,13.0111 45.633,14.2291 L45.633,52.5991 C45.633,54.5461 44.523,56.4391 42.681,57.0841 C41.871,57.3661 41.01,57.4021 40.197,57.2101"
                      opacity=".2"></path>
                <path fill="#354049" fill-opacity=".2"
                      d="M10.269,8.2327 C8.235,8.7157 6.798,10.5277 6.798,12.6127 L6.798,36.8887 C6.798,38.9707 8.235,40.7827 10.269,41.2657 L39.972,48.3247 C41.436,48.6727 43.032,48.3997 44.151,47.3947 C45.129,46.5187 45.633,45.3187 45.633,44.1007 L45.633,5.7307 C45.633,3.7837 44.523,1.8907 42.681,1.2457 C41.871,0.9637 41.01,0.9277 40.197,1.1197 L10.269,8.2327 Z"></path>
                <g transform="translate(0 .33)">
                    <mask id="new_logo-b" fill="#fff">
                        <use xlink:href="#new_logo-a"></use>
                    </mask>
                    <path fill="#354049"
                          d="M6.3885,0.8109 L41.8275,9.2649 C44.0595,9.7959 45.6345,11.7909 45.6345,14.0859 L45.6345,43.2639 C45.6345,45.5589 44.0595,47.5539 41.8275,48.0849 L6.4155,56.5299 C5.2335,56.8119 3.9645,56.7459 2.8905,56.1759 C0.9945,55.1739 0.0015,53.3469 0.0015,51.4929 L0.0015,5.9829 C0.0015,4.6179 0.4455,3.2469 1.4055,2.2779 C2.8035,0.8679 4.6875,0.4059 6.3885,0.8109"
                          mask="url(#new_logo-b)"></path>
                </g>
                <path fill="#FFF"
                      d="M18.0999 35.5921C17.7879 35.9011 17.3499 36.0841 16.9389 36.0841 16.5489 36.0841 16.1889 35.9551 15.8799 35.6701L10.6089 30.8131 10.6089 34.6621C10.6089 35.5411 9.9129 36.2371 9.0369 36.2371 8.1579 36.2371 7.4589 35.5411 7.4589 34.6621L7.4589 18.2131C7.4589 17.3341 8.1579 16.6351 9.0369 16.6351 9.9129 16.6351 10.6089 17.3341 10.6089 18.2131L10.6089 26.9401 15.6969 23.4301C16.4199 22.9381 17.4039 23.0911 17.8929 23.8141 18.4119 24.5131 18.2289 25.4941 17.5329 26.0101L13.2189 28.9291 18.0219 33.3451C18.6429 33.9391 18.6939 34.9201 18.0999 35.5921M30.0561 29.704C30.0561 27.82 28.5051 26.296 26.6481 26.296 24.7611 26.296 23.2401 27.82 23.2401 29.704 23.2401 31.588 24.7611 33.112 26.6481 33.112 28.5051 33.112 30.0561 31.588 30.0561 29.704M33.1821 29.704C33.1821 33.319 30.2391 36.238 26.6481 36.238 25.3041 36.238 24.0651 35.824 23.0061 35.152L23.0061 41.197C23.0061 42.073 22.3071 42.772 21.4551 42.772 20.5791 42.772 19.8801 42.073 19.8801 41.197L19.8801 24.772C19.8801 23.92 20.5791 23.221 21.4551 23.221 22.1541 23.221 22.7481 23.686 22.9551 24.307 23.9871 23.584 25.2801 23.17 26.6481 23.17 30.2391 23.17 33.1821 26.089 33.1821 29.704M36.5979 20.9749C35.6949 20.9749 34.9719 20.2519 34.9719 19.3999L34.9719 19.2709C34.9719 18.3679 35.6949 17.6689 36.5979 17.6689 37.4499 17.6689 38.1729 18.3679 38.1729 19.2709L38.1729 19.3999C38.1729 20.2519 37.4499 20.9749 36.5979 20.9749M36.5979 36.1339C35.6949 36.1339 34.9719 35.4109 34.9719 34.5319L34.9719 24.5659C34.9719 23.6869 35.6949 22.9879 36.5979 22.9879 37.4499 22.9879 38.1729 23.6869 38.1729 24.5659L38.1729 34.5319C38.1729 35.4109 37.4499 36.1339 36.5979 36.1339"></path>
            </g>
        </svg>

        <h1 class="sup-title">
            Электронная форма для кандидатов
        </h1>

        <hgroup>
            <h1>Для Вашего удобства, предлагаем заполнить электронную анкету.</h1>
            <h2>Пожалуйста, заполните все необходимые поля внимательно, это позволит нам <span class="nobr">быстрее обработать анкету</span> и связаться с Вами для дальнейшего собеседования.</h2>
        </hgroup>
    </div>

    <div class="cm-webform__body">
        <div class="row">
        <div class="col">
            <div class="form-group">
                <label class="form-group__label">Имя <span class="req">*</span></label>
                <div class="form-group__content">
                    <input class="form-control" type="text" />
                </div>
            </div>
            <div class="form-group">
                <label class="form-group__label">Фамилия <span class="req">*</span></label>
                <div class="form-group__content">
                    <input class="form-control" type="text" />
                </div>
            </div>
            <div class="form-group">
                <label class="form-group__label">Год рождения:</label>
                <div class="form-group__content">
                    <div class="form-row">
                        <div class="col-2">
                            <input class="form-control" type="text" />
                        </div>
                        <div class="col-auto">
                            <input class="form-control" type="text" />
                        </div>
                        <div class="col-3">
                            <input class="form-control" type="text" />
                        </div>
                    </div>
                </div>
            </div>

            <div class="form-group">
                <div class="form-group__label">НА ДОЛЖНОСТЬ <span class="req">*</span></div>
                <div class="form-group__content checkbox-stack">
                    <div class="checkbox">
                        <input type="checkbox" id="checkbox_1">
                        <label for="checkbox_1">Разработчик - стажировка</label>
                    </div>
                    <div class="checkbox">
                        <input type="checkbox" id="checkbox_2">
                        <label for="checkbox_2">Разработчик - Junior</label>
                    </div>
                    <div class="checkbox">
                        <input type="checkbox" id="checkbox_3">
                        <label for="checkbox_3">Разработчик - Senior</label>
                    </div>
                    <div class="checkbox">
                        <input type="checkbox" id="checkbox_4">
                        <label for="checkbox_4">Разработчик - Middle Level</label>
                    </div>
                    <div class="checkbox">
                        <input type="checkbox" id="checkbox_5">
                        <label for="checkbox_5">SMM - Senior Manager</label>
                    </div>
                    <div class="checkbox">
                        <input type="checkbox" id="checkbox_6">
                        <label for="checkbox_6">Менеджер по продажам</label>
                    </div>
                    <div class="checkbox">
                        <input type="checkbox" id="checkbox_7">
                        <label for="checkbox_7">Верстальщик</label>
                    </div>
                </div>
            </div>
        </div>

        <div class="col">
            <div class="form-group">
                <label class="form-group__label">Опыт работы</label>
                <div class="form-group__content">
                    <input class="form-control" type="text" />
                </div>
            </div>

            <div class="form-group">
                <label class="form-group__label">Нынешний Работодатель</label>
                <div class="form-group__content">
                    <input class="form-control" type="text" />
                </div>
            </div>

            <div class="form-group">
                <label class="form-group__label">Ожидаемая зарплата</label>
                <div class="form-group__content">
                    <input class="form-control" type="text" />
                </div>
            </div>

            <div class="form-group">
                <label class="form-group__label">Навыки</label>
                <div class="form-group__content">
                    <div class="textarea-group">
                        <div class="textarea-group__sup">
                            1000 characters
                        </div>
                        <textarea name="" id="" cols="30" rows="10"></textarea>
                        <div class="textarea-group__sub">
                            Резюме (.DOC, *PDF) <span class="req">*</span>
                        </div>
                    </div>
                    <input class="upload-control" type="file" name="" id="" />
                </div>
            </div>
        </div>
    </div>
    </div>


    <div class="cm-webform__footer">
        <button class="btn btn--primary"><span>Отправить анкету</span></button>
    </div>
</div>--%>

<script type="text/javascript">
    // setTimeout("showRefreshMessage()", 60000);

    function showRefreshMessage() {
        // var pageLoadVar = document.getElementById("pageload");
        // var loaderAnim = document.getElementById("loaderAnim");

        // if (pageLoadVar != undefined)
            // pageLoadVar.style.display = 'block';
        // if (loaderAnim != undefined)
            // loaderAnim.style.display = 'none;';
    }

    function contentScroll(contentStyleName, parentStyle) {
        $(parentStyle + ' ' + contentStyleName).scrollbar({
            "autoUpdate": true,
            "autoScrollSize": true,
            "scrollx": $(parentStyle + ' .external-scroll_x'),
            "scrolly": $(parentStyle + ' .external-scroll_y')
        });
    }

</script>
</body>
</html>