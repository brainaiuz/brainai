<%--
  Created by IntelliJ IDEA.
  User: Bobur
  Date: 10/24/2022
  Time: 9:06 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="title" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <link rel="icon" href="/customisation/agroBank/assets/favicon.ico"/>
    <link rel="stylesheet" href="/customisation/agroBank/css/style.css"/>
    <title>Xodimlar bilan ishlash tizimi</title>
</head>
<body>
<div class="main_wrapper">
    <header class="mobile_header">
        <img src="/customisation/agroBank/assets/logo.svg" alt="Agrobank logo"/>
    </header>
    <section class="main_container">
        <div class="left_container">
            <div class="slide"></div>
            <div class="slide"></div>
            <div class="slide"></div>
            <div class="slide"></div>
            <div class="agro_clip_path"></div>
            <h1 class="main_header_title">Xodimlar bilan ishlash tizimi</h1>
            <h2 class="main_header_secondary_title">HRM</h2>
        </div>
        <div class="right_container">
            <img
                    class="agrobank_logo"
                    src="/customisation/agroBank/assets/logo.svg"
                    alt="Agrobank logo"
            />
            <div class="login_wrapper">
                <p class="login_title">Tizimga kirish</p>
                <form class="form_wrapper" action="/mainLogin" method="post">
                    <div class="login_container">
                        <input
                                id="login" name="USER_NAME"
                                class="login_input ${not empty error ? 'error_input' : ''}"
                                placeholder="Login"
                                type="text"
                        />
                        <c:if test="${not empty error}">
                            <p class="error_text" style="display: block">
                                Bunday login yoki parol mavjud emas
                            </p>
                        </c:if>
                    </div>
                    <div class="password_container">
                        <input
                                id="password" name="USER_PASSWORD"
                                class="password_input"
                                placeholder="Parol"
                                type="password"
                        />
                        <img
                                class="password_eye"
                                onclick="toggleInputType()"
                                src="/customisation/agroBank/assets/svgs/eye.svg"
                                alt="eye"
                        />
                    </div>
                    <button type="submit" class="login_button">Kirish</button>
                </form>
            </div>
        </div>
    </section>
</div>

<script src="/customisation/agroBank/js/main.js"></script>
</body>
</html>
