<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Home :: DigiX</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <%-- CSS Files --%>
    <link href="<spring:url value="/resources/css/bootstrap.min.css"/>" rel='stylesheet' type='text/css' media="all"/>
    <link href="<spring:url value="/resources/css/dashboard.css"/>" rel="stylesheet"/>
    <link href="<spring:url value="/resources/css/home.css"/>" rel='stylesheet' type='text/css' media="all"/>
</head>

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

<jsp:include page="partial/navbar.jsp"/>

<div class="col-sm-3 col-md-2 sidebar">
    <div class="top-navigation">
        <div class="t-menu">MENU</div>
        <div class="t-img"><img src="<c:url value="/resources/images/home/lines.png"/>" alt=""/></div>
        <div class="clearfix"></div>
    </div>
    <div class="drop-navigation drop-navigation">
        <ul class="nav nav-sidebar">
            <li class="active"><a href="${homeURL}"><span class="glyphicon glyphicon-home" aria-hidden="true"></span>Home</a>
            </li>
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
                <li><a href="${editInfoURL}"><span class="glyphicon glyphicon-edit" aria-hidden="true"></span>Edit
                    Profile</a></li>
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
        <h3 class="contentTitle">News Feed</h3>
        <div id="newsFeed">
            <div class="row">
                <c:forEach items="${contentList}" var="content">
                    <c:if test="${content.typeID =='1'}">
                        <div class="col s4 white-text">
                            <div class="card medium  grey darken-3">
                                <div class="card-image waves-effect waves-block waves-light">
                                    <img class="activator" src="${content.path}">
                                </div>
                                <div class="card-content grey darken-3">
                                    <a href="/profile/userdetails?userID=${content.user.userID}"><span
                                            class="card-title white-text">${content.user.firstName} ${content.user.lastName}</span></a>
                                            <i class="material-icons right activator">more_vert</i>
                                    <p>${content.postedDateString}</p>
                                </div>
                                <div class="card-reveal grey darken-3">
                                    <span class="card-title white-text">Details<i
                                            class="material-icons right">close</i></span>

                                    <c:if test="${content.description.length() > 0 }">
                                        <i class="material-icons">description</i> Description
                                        <br/>  ${content.description} <br/>
                                    </c:if>

                                    <c:if test="${content.location.city.length() > 0}">
                                        <i class="material-icons">my_location</i> Location <br/>
                                        <div class="container">
                                                ${content.location.city} <br/>
                                                ${content.location.country} <br/>
                                                ${content.location.street} <br/>
                                        </div>
                                    </c:if>

                                    <c:if test="${content.tagList.size()>0}">
                                        <i class="material-icons">contacts</i> Tags <br/>
                                        <c:forEach items="${content.tagList}" var="tag">
                                            <div class="chip black-text">
                                                    ${tag.tagName}
                                            </div>
                                        </c:forEach>
                                    </c:if>

                                </div>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${content.typeID =='2'}">
                        <div class="col s4 white-text">
                            <c:if test="${content.provider.equals('facebook')}">
                                <div class="card medium grey darken-3">
                                    <div class="video-container">
                                        <iframe src="https://www.facebook.com/plugins/video.php?href=${content.path}&show_text=0&width=560"
                                                frameborder="0" allowfullscreen="true"></iframe>
                                    </div>
                                    <div class="card-content grey darken-3">
                                        <a href="/profile/userdetails?userID=${content.user.userID}">
                                            <span class='card-title activator white-text'>${content.user.firstName} ${content.user.lastName}</span>
                                        </a>
                                        <p>${content.postedDateString}</p>
                                    </div>
                                </div>
                            </c:if>
                            <c:if test="${content.provider.equals('google')}">
                                <div class="card medium grey darken-3">
                                    <div class="video-container grey darken-3">
                                        <iframe src="${content.path}"
                                                frameborder="0" allowfullscreen="true"></iframe>
                                    </div>
                                    <div class="card-content">
                                        <a href="/profile/userdetails?userID=${content.user.userID}">
                                            <span class='card-title white-text'>${content.user.firstName} ${content.user.lastName}</span>
                                        </a>
                                        <p>${content.postedDateString}</p>
                                    </div>
                                </div>
                            </c:if>
                        </div>
                    </c:if>
                    <c:if test="${content.typeID == '3'}">
                        <div class="col s4 white-text">
                            <div class="card medium grey darken-3">
                                <div class="card-image">
                                    <img src="${content.thumbnailURL}">
                                </div>
                                <div class="card-content grey darken-3">
                                    <a href="/profile/userdetails?userID=${content.user.userID}">
                                        <span class='card-title white-text'>${content.user.firstName} ${content.user.lastName}</span>
                                    </a>
                                    <p>${content.postedDateString}</p>
                                </div>
                                <div class="card-action">
                                    <a href="${content.path}">View document</a>
                                </div>
                            </div>
                        </div>
                    </c:if>
                </c:forEach>
            </div>
        </div>
        <!-- footer -->
        <jsp:include page="partial/footer.jsp"/>
        <div class="clearfix"></div>
    </div>
    <jsp:include page="partial/global.jsp"/>
</body>
</html>