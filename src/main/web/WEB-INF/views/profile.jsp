<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="social" uri="http://www.springframework.org/spring-social/social/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${userProfile.firstName} ${userProfile.lastName} :: DigiX</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <%-- CSS Files --%>
    <link href="<spring:url value="/resources/css/profile.css"/>" rel='stylesheet' type='text/css' media="all"/>
    <link href="//maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet">

    <link href="<spring:url value="/resources/css/bootstrap.min.css"/>" rel='stylesheet' type='text/css' media="all"/>
    <link href="<spring:url value="/resources/css/dashboard.css"/>" rel="stylesheet">
    <link href="<spring:url value="/resources/css/home.css"/>" rel='stylesheet' type='text/css' media="all"/>
    <link href="<spring:url value="/resources/css/socialsync.css"/>" rel='stylesheet' type='text/css' media="all"/>
</head>

<jsp:include page="partial/navbar.jsp" />

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

<jsp:include page="partial/navbar.jsp" />

<div class="col-sm-3 col-md-2 sidebar">
    <div class="top-navigation">
        <div class="t-menu">MENU</div>
        <div class="t-img"><img src="<c:url value="/resources/images/home/lines.png"/>" alt=""/></div>
        <div class="clearfix"></div>
    </div>
    <div class="drop-navigation drop-navigation">
        <ul class="nav nav-sidebar">
            <li><a href="${homeURL}"><span class="glyphicon glyphicon-home" aria-hidden="true"></span>Home</a></li>
            <li class="active"><a href="${profileURL}"><span class="glyphicon glyphicon-user" aria-hidden="true"></span>Profile</a>
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
                <li><a href="${socialSyncURL}"><span class="glyphicon glyphicon-cloud" aria-hidden="true"></span>Social
                    Sync</a></li>
            </ul>
        </ul>


        <div class="side-bottom">
            <div class="copyright"><p>Copyright © 2016 DigiX. All Rights Reserved.</p><br></div>
        </div>
    </div>
</div>


<div class="main s12">
    <div class="container">
        <div class="container">
            <c:if test="${userProfile.birthday!=null}">
            <div class="col-lg-12 col-sm-12">
                <div class="card hovercard">
                    <div class="cardheader">
                    </div>
                    <div class="avatar " >
                        <img alt="" src="${userProfile.avatarPath}">
                    </div>
                    <div class="info">
                        <div class="title">
                            <a>${userProfile.firstName} ${userProfile.lastName}</a>
                        </div>
                        <div class="desc">E-mail : ${userProfile.email}</div>
                        <div class="desc">First Name : ${userProfile.firstName}</div>
                        <div class="desc">Last Name : ${userProfile.lastName}</div>
                        <div class="desc">Join Date: ${userProfile.joinDate}</div>
                        <div class="desc">Birthday: ${userProfile.birthday}</div>
                        <div class="desc">Gender: ${userProfile.gender}</div>
                        <div class="desc">Location: ${userProfile.location}</div>
                        <div class="desc">Status : ${userProfile.status}</div>
                    </div>
                    <div class="bottom">
                        <c:if test="${userProfile.twitterURL.length() > 0}">
                            <a class="btn btn-primary btn-twitter btn-sm" href="${userProfile.twitterURL}">
                                <i class="fa fa-twitter"></i>
                            </a>
                        </c:if>
                        <c:if test="${userProfile.googleURL.length() > 0}">
                            <a class="btn btn-danger btn-sm" rel="publisher"
                               href="${userProfile.googleURL}">
                                <i class="fa fa-google-plus"></i>
                            </a>
                        </c:if>
                        <c:if test="${userProfile.facebookURL.length() > 0}">
                            <a class="btn btn-primary btn-sm" rel="publisher"
                               href="${userProfile.facebookURL}">
                                <i class="fa fa-facebook"></i>
                            </a>
                        </c:if>
                        <c:if test="${userProfile.linkedinURL.length() > 0}">
                            <a class="btn btn-primary btn-sm" rel="publisher"
                               href="${userProfile.linkedinURL}">
                                <i class="fa fa-LinkedIn"></i>
                            </a>
                        </c:if>
                    </div>
                </div>
                </c:if>
                <c:if test="${userProfile.birthday == null and userProfile.userID == sessionScope.get('userID')}">
                    <h4 class="contentTitle">Your account is not synced yet. Synchronized your info  <a
                            href="/settings/contentsync">here</a>.</h4>
                </c:if>
                <c:if test="${userProfile.birthday == null and userProfile.userID != sessionScope.get('userID')}">
                    <h4 class="contentTitle">User account is not synced yet.</h4>
                </c:if>
            </div>
        </div>

    <!-- footer -->
    <jsp:include page="partial/footer.jsp" />
    <div class="clearfix"></div>
</div>
<jsp:include page="partial/global.jsp" />
</body>
</html>
