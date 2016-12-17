<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="social" uri="http://www.springframework.org/spring-social/social/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Social Sync :: DigiX</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <%-- CSS Files --%>
    <link href="<spring:url value="/resources/css/bootstrap.min.css"/>" rel='stylesheet' type='text/css' media="all"/>
    <link href="<spring:url value="/resources/css/dashboard.css"/>" rel="stylesheet">
    <link href="<spring:url value="/resources/css/home.css"/>" rel='stylesheet' type='text/css' media="all"/>
</head>
<!-- Materialize -->
<link href="<spring:url value="/resources/materialize/css/materialize.css"/>" rel='stylesheet' type='text/css' media="all"/>
<script src="<spring:url value="/resources/materialize/js/materialize.min.js"/>"></script>
<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">

<!-- /Materialize -->
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

<jsp:include page="../partial/navbar.jsp" />

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
                <li><a href="${documentsURL}"><span class="glyphicon glyphicon-paperclip" aria-hidden="true"></span>Documents</a>
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
                <li class="active"><a href="${socialSyncURL}"><span class="glyphicon glyphicon-cloud"
                                                                    aria-hidden="true"></span>Social Sync</a></li>
            </ul>
        </ul>


        <div class="side-bottom">
            <div class="copyright"><p>Copyright © 2016 DigiX. All Rights Reserved.</p><br></div>
        </div>
    </div>
</div>


<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
    <div class="main-grids">
        <h4 class="contentTitle">Social Sync</h4>
        <div class="contentText">Sync your photos, videos, documents and many more.</div>

        <%-- Facebook --%>
        <social:connected provider="facebook">
            <form action="<c:url value="/connect/facebook"/>" method="post">
                <input class="connectButton socialButton" type="image"
                       src="<c:url value="/resources/images/social/facebook/disconnect.png"/>"/>
                <input type="hidden" name="_method" value="delete"/>
            </form>
        </social:connected>
        <social:notConnected provider="facebook">
            <form:form action="/connect/facebook" method="post">
                <input class="socialButton" type="image" name="scope" value="user_posts,user_photos,user_relationships,user_birthday,user_location,user_about_me"
                       src="<c:url value="/resources/images/social/facebook/connect.png"/>"/>
            </form:form>
        </social:notConnected>

        <%-- LinkedIn --%>
        <social:connected provider="linkedin">
            <form action="<c:url value="/connect/linkedin"/>" method="post">
                <input class="connectButton socialButton" type="image"
                       src="<c:url value="/resources/images/social/linkedin/disconnect.png"/>"/>
                <input type="hidden" name="_method" value="delete"/>
            </form>
        </social:connected>
        <social:notConnected provider="linkedin">
            <form:form action="/connect/linkedin" method="post">
                <input class="socialButton" type="image" name="scope"
                       src="<c:url value="/resources/images/social/linkedin/connect.png"/>"/>
            </form:form>
        </social:notConnected>

        <%-- Twitter --%>
        <social:connected provider="twitter">
            <form action="<c:url value="/connect/twitter"/>" method="post">
                <input class="connectButton socialButton" type="image"
                       src="<c:url value="/resources/images/social/twitter/disconnect.png"/>"/>
                <input type="hidden" name="_method" value="delete"/>
            </form>
        </social:connected>
        <social:notConnected provider="twitter">
            <form:form action="/connect/twitter" method="post">
                <input class="socialButton" type="image" name="scope" value="user_posts,user_photos"
                       src="<c:url value="/resources/images/social/twitter/connect.png"/>"/>
            </form:form>
        </social:notConnected>
        <social:notConnected provider="google">
            <form:form action="/connect/google/" method="post">
                <input type="hidden" name="access_type" value="offline"/>
                <input class="socialButton" type="image" name="scope"
                       value="https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo#email https://www.googleapis.com/auth/plus.me https://www.googleapis.com/auth/tasks https://www-opensocial.googleusercontent.com/api/people https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/drive"
                       src="<c:url value="/resources/images/social/google/connect.png"/>"/>
            </form:form>
        </social:notConnected>
        <social:connected provider="google">
            <form action="<c:url value="/connect/google"/>" method="post">
                <input class="connectButton socialButton" type="image"
                       src="<c:url value="/resources/images/social/google/disconnect.png"/>"/>
                <input type="hidden" name="_method" value="delete"/>
            </form>
        </social:connected>
    </div>
    <!-- footer -->
    <jsp:include page="../partial/footer.jsp" />
    <div class="clearfix"></div>
</div>
<jsp:include page="../partial/global.jsp" />
<script>
    $("ul.cl-effect-2").slideToggle(300, function () {
        // Animation complete.
    });
</script>
<script type="text/javascript">
    if (window.location.hash == '#_=_') {
        history.replaceState
                ? history.replaceState(null, null, window.location.href.split('#')[0])
                : window.location.hash = '';
    }
</script>
</body>
</html>
