<%--
  Created by IntelliJ IDEA.
  User: Daniel Moniry
  Date: 07.06.2016
  Time: 02:37
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="social" uri="http://www.springframework.org/spring-social/social/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit Document :: DigiX</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <%-- CSS Files --%>
    <link href="<spring:url value="/resources/css/bootstrap.min.css"/>" rel='stylesheet' type='text/css' media="all"/>
    <link href="<spring:url value="/resources/css/dashboard.css"/>" rel="stylesheet">
    <link href="<spring:url value="/resources/css/home.css"/>" rel='stylesheet' type='text/css' media="all"/>
</head>

<script src="<spring:url value="/resources/js/jquery-1.11.1.min.js"/>"></script>

<jsp:include page="../../partial/navbar.jsp"/>

<body>


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

<jsp:include page="../../partial/navbar.jsp" />

<div class="col-sm-3 col-md-2 sidebar">
    <div class="top-navigation">
        <div class="t-menu">MENU</div>
        <div class="t-img"><img src="<c:url value="/resources/images/home/lines.png"/>" alt=""/></div>
        <div class="clearfix"></div>
    </div>
    <div class="drop-navigation drop-navigation">
        <ul class="nav nav-sidebar">
            <li><a href="${homeURL}"><span class="glyphicon glyphicon-home" aria-hidden="true"></span>Home</a></li>
            <li><a href="${profileURL}"><span class="glyphicon glyphicon-user" aria-hidden="true"></span>Profile</a>
            </li>


            <li><a href="#" class="contentMenu"><span class="glyphicon glyphicon-inbox" aria-hidden="true"></span>Digital
                Box<span class="glyphicon glyphicon-menu-down" aria-hidden="true"></span></a></li>
            <ul class="cl-effect-1">
                <li><a href="${photosURL}"><span class="glyphicon glyphicon-picture"
                                                                aria-hidden="true"></span>Photos</a></li>
                <li><a href="${videosURL}"><span class="glyphicon glyphicon-film" aria-hidden="true"></span>Videos</a>
                </li>
                <li class="active"><a href="${documentsURL}"><span class="glyphicon glyphicon-paperclip" aria-hidden="true"></span>Documents</a>
                </li>
            </ul>


            <li><a href="#" class="socialMenu"><span class="glyphicon glyphicon-globe"
                                                     aria-hidden="true"></span>Social<span
                    class="glyphicon glyphicon-menu-down" aria-hidden="true"></span></a></li>
            <ul class="cl-effect-3">
                <li><a class="socialMenu" href="${friendsURL}"><span class="glyphicon glyphicon-user"
                                                                     aria-hidden="true"></span>Friends</a></li>
                <li><a class="socialMenu" href="${familyURL}"><span class="glyphicon glyphicon-user"
                                                                    aria-hidden="true"></span>Family</a></li>
            </ul>


            <li><a href="#" class="settingsMenu"><span class="glyphicon glyphicon-cog" aria-hidden="true"></span>Settings<span
                    class="glyphicon glyphicon-menu-down" aria-hidden="true"></span></a></li>
            <ul class="cl-effect-2">
                <li><a href="${editInfoURL}"><span class="glyphicon glyphicon-edit" aria-hidden="true"></span>Edit Info</a>
                </li>
                <li><a href="${contentSyncURL}"><span class="glyphicon glyphicon-cloud-download"
                                                      aria-hidden="true"></span>Content Sync</a></li>
                <li><a href="${socialSyncURL}"><span class="glyphicon glyphicon-cloud" aria-hidden="true"></span>Social
                    Sync</a></li>
            </ul>
        </ul>


        <div class="side-bottom">
            <div class="copyright"><p>Copyright © 2016 DigiX. All Rights Reserved.</p><br></div>
        </div>
    </div>
</div>


<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
    <div class="main-grids">
        <div class="button"><a href="/digitalbox/documents?page=${sessionScope.get("currentPage")}">Back</a></div>
        <h4 class="contentTitle center">Edit Document</h4>

        <style scoped>
            .edit input[type="text"] {
                font-size: 16px;
                font-family: Nunito-Light;
                color: #fff;
                padding: 15px 10px;
                border: none;
                -moz-border-radius: 3px;
                -webkit-border-radius: 3px;
                -khtml-border-radius: 3px;
                border-radius: 3px;
                width: 100%;
                background: rgba(2, 2, 2, 0.60);
            }
        </style>
        <div class="edit">
            <form:form cssClass="row" action="/digitalbox/editdocument" method="post" modelAttribute="content">
                <input type="hidden" name="contentID" value="${content.contentID}"/>

                <ul class="collapsible popout white-text" data-collapsible="accordion">
                    <li>
                        <div class="collapsible-header grey darken-4 active"><i class="material-icons">description</i>Description</div>
                        <div class="collapsible-body row"><p><form:input path="description"
                                                                         placeholder="${content.description}"
                                                                         value="${content.description}" cssClass=""/></p></div>
                    </li>
                    <li>
                        <div class="collapsible-header grey darken-4"><i class="material-icons">translate</i>Country</div>
                        <div class="collapsible-body"><p><form:input path="location.country"
                                                                     placeholder="${content.location.country}"
                                                                     value="${content.location.country}"/></p></div>
                    </li>
                    <li>
                        <div class="collapsible-header grey darken-4"><i class="material-icons">place</i>City</div>
                        <div class="collapsible-body"><p><form:input path="location.city"
                                                                     placeholder="${content.location.city}"
                                                                     value="${content.location.city}"/></p></div>
                    </li>
                    <li>
                        <div class="collapsible-header grey darken-4"><i class="material-icons">person_pin</i>Street</div>
                        <div class="collapsible-body"><p><form:input path="location.street"
                                                                     placeholder="${content.location.street}"
                                                                     value="${content.location.street}"/></p></div>
                    </li>
                </ul>
                <div class="buttonInput container">
                    <input type="submit" value="Save"/>
                </div>
            </form:form>
        </div>

    </div>
    <!-- footer -->
    <jsp:include page="../../partial/footer.jsp"/>
    <div class="clearfix"></div>
</div>
<jsp:include page="../../partial/global.jsp"/>
<script src="<spring:url value="/resources/js/bootstrap-tagsinput.js"/>"></script>
<script>
    $("ul.cl-effect-1").slideToggle(300, function () {
        // Animation complete.
    });
</script>
</body>
</html>

