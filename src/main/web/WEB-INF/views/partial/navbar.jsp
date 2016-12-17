<%--
  Created by IntelliJ IDEA.
  User: Daniel Moniry
  Date: 05.06.2016
  Time: 04:37
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<c:url value="/" var="homeURL"/>
<c:url value="/profile/userdetails?userID=${sessionScope.userID}" var="profileURL"/>
<c:url value="/digitalbox/photos?page=1" var="photosURL"/>
<c:url value="/digitalbox/videos?page=1" var="videosURL"/>
<c:url value="/digitalbox/documents?page=1" var="documentsURL"/>
<c:url value="/social/friends" var="friendsURL"/>
<c:url value="/social/family" var="familyURL"/>
<c:url value="/settings/editprofile" var="editInfoURL"/>
<c:url value="/settings/socialsync" var="socialSyncURL"/>
<c:url value="/settings/contentsync" var="contentSyncURL"/>
<c:url value="/search/results" var="searchURL"/>

<!-- Materialize -->
<link href="<spring:url value="/resources/materialize/css/materialize.css"/>" rel='stylesheet' type='text/css' media="all"/>
<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
<!-- /Materialize -->

<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container-fluid">
        <div class="navbar-header"><a class="navbar-bran" href="${homeURL}"><span class="digixLogo"></span></a></div>

        <div id="navbar" class="navbar-collapse collapse">
            <div class="top-search">
                <form action="${searchURL}" method="get" class="navbar-form navbar-right">
                    <input type="text" name="query" class="form-control" placeholder="Search...">
                    <input type="submit" value="">
                </form>
            </div>
            <div class="header-top-right">
                <div class="logout">
                    <form id="logOutForm" action="<c:url value="/logout"/>" method="post">
                        <a href="#" onclick="document.getElementById('logOutForm').submit();">Log Out</a>
                    </form>
                </div>
            </div>
        </div>

    </div>
</nav>



