<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>DigiX :: Log In or Register</title>
    <link rel='stylesheet' type='text/css' href="<spring:url value="/resources/css/login.css"/>"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <script type="application/x-javascript"> addEventListener("load", function () {
        setTimeout(hideURLbar, 0);
    }, false);
    function hideURLbar() {
        window.scrollTo(0, 1);
    } </script>
    <script src="<spring:url value="/resources/js/jquery-1.11.1.min.js"/>"></script>
    <script src="<spring:url value="/resources/js/easyResponsiveTabs.js"/>" type="text/javascript"></script>
    <script type="text/javascript">$(document).ready(function () {
        $('#horizontalTab').easyResponsiveTabs({type: 'default', width: 'auto', fit: true});
    });</script>
</head>

<body>
<a href="<c:url value="/"/>"><h1 class="digixTitle">DigiX</h1></a>

<div class="main-content">
    <div class="sap_tabs">
        <c:if test="${not empty error}">
            <h3 class="message">${error}</h3>
        </c:if>
        <c:if test="${not empty msg}">
            <h3 class="message">${msg}</h3>
        </c:if>
        <div id="horizontalTab" style="display: block; width: 100%; margin: 0;">

            <ul>
                <li class="resp-tab-item" aria-controls="tab_item-0" role="tab"><span>Log In</span></li>
                <li class="resp-tab-item" aria-controls="tab_item-1" role="tab"><span>Register</span></li>
            </ul>

            <div class="tab-1 resp-tab-content" aria-labelledby="tab_item-0">
                <div class="facts">
                    <div class="register">

                        <form:form action="j_spring_security_check" method="post" modelAttribute="user">
                            <form:input id="email" path="email" placeholder="Email Address"/>
                            <form:input id="password" path="password" placeholder="Password" type="password"/>
                            <div class="sign-up">
                                <input type="submit" value="Log In"/>
                            </div>
                        </form:form>
                    </div>
                </div>
            </div>

            <div class="tab-2 resp-tab-content" aria-labelledby="tab_item-1">
                <div class="facts">
                    <%--Sign Up Form--%>
                    <div class="register">
                        <spring:url value="/register" var="registerURL"/>
                        <form:form action="${registerURL}" method="post" modelAttribute="user">
                            <form:input id="email" path="email" placeholder="Email Address"/>
                            <form:input id="firstName" path="firstName" placeholder="First Name"/>
                            <form:input id="lastName" path="lastName" placeholder="Last Name"/>
                            <form:input id="password" path="password" placeholder="Password" type="password"/>
                            <form:input id="confirmedPassword" path="confirmedPassword" placeholder="Confirm Password"
                                        type="password"/>
                            <div class="sign-up">
                                <input type="submit" value="Register"/>
                            </div>
                        </form:form>
                    </div>
                </div>
            </div>

        </div>

    </div>
</div>
<!--start-copyright-->
<div class="copy-right">
    <div class="wrap">
        <p>Copyright © 2016 DigiX. All Rights Reserved</p>
    </div>
</div>
<!--//end-copyright-->

</body>
</html>