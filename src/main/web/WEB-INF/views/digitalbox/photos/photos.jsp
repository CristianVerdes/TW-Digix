<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="social" uri="http://www.springframework.org/spring-social/social/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Photos :: DigiX</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <%-- CSS Files --%>
    <link href="<spring:url value="/resources/css/bootstrap.min.css"/>" rel='stylesheet' type='text/css' media="all"/>
    <link href="<spring:url value="/resources/css/dashboard.css"/>" rel="stylesheet">
    <link href="<spring:url value="/resources/css/home.css"/>" rel='stylesheet' type='text/css' media="all"/>
</head>

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

<jsp:include page="../../partial/navbar.jsp"/>

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
                <li class="active"><a href="${photosURL}"><span class="glyphicon glyphicon-picture"
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


<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
    <div class="main-grids">
        <h4 class="contentTitle">My Photos</h4>


        <c:if test="${currentPage > 1}">
            <div class="button"><a href="/digitalbox/photos?page=${currentPage - 1}">Previous Page</a></div>
        </c:if>
        <c:if test="${currentPage < counter}">
            <div class="button"><a href="/digitalbox/photos?page=${1 + currentPage}">Next Page</a></div>
        </c:if>


        <c:if test="${photos == null}">
            <div class="contentText">You have no photos yet. Sync your content <a href="/settings/contentsync">here</a>
            </div>
        </c:if>

        <c:forEach items="${photos}" var="photo">
            <div>
                <a href="/digitalbox/photoDetails?contentID=${photo.contentID}"><img class="photo"
                                                                                     src="${photo.path}"/></a>
                <form class="button" action="/digitalbox/editPhoto" method="get">
                    <input type="hidden" name="contentID" value="${photo.contentID}"/>
                    <div class="buttonInput">
                        <input type="submit" value="Edit Photo"/>
                    </div>
                </form>
                <form:form id="deleteForm${photo.contentID}" cssClass="button deleteForm"
                           action="/digitalbox/deletephoto"
                           method="delete"
                           modelAttribute="photoForm">
                    <input type="hidden" name="contentID" value="${photo.contentID}">
                    <div class="buttonInput">
                        <input type="submit" value="Delete Photo"/>
                    </div>

                </form:form>
                <c:if test="${photo.postedDate == null}">
                    <form:form id="postForm${photo.contentID}" cssClass="button photosForm"
                               action="/digitalbox/postphoto" method="post"
                               modelAttribute="photoForm">
                        <input type="hidden" name="contentID" value="${photo.contentID}">>
                        <div class="buttonInput">
                            <input type="submit" value="Post Photo"/>
                        </div>
                    </form:form>
                </c:if>
            </div>
        </c:forEach>
        <c:if test="${currentPage > 1}">
            <div class="button"><a href="/digitalbox/photos?page=${currentPage - 1}">Previous Page</a></div>
        </c:if>
        <c:if test="${currentPage < counter}">
            <div class="button"><a href="/digitalbox/photos?page=${1 + currentPage}">Next Page</a></div>
        </c:if>

    </div>
    <!-- footer -->
    <jsp:include page="../../partial/footer.jsp"/>
    <div class="clearfix"></div>
</div>
<jsp:include page="../../partial/global.jsp"/>
<script>
    $("ul.cl-effect-1").slideToggle(300, function () {
        // Animation complete.
    });
</script>
<script type="text/javascript">
    jQuery(document).ready(function ($) {
        $(function () {
            $('.photosForm').on('submit', function (e) {
                var self = this;
                $.ajax({
                    type: 'post',
                    url: '/digitalbox/postphoto',
                    data: $(this).serialize(),
                    success: function (data) {
                        var element = document.getElementById(self.id);
                        element.parentNode.removeChild(element);
                        Materialize.toast('Photo successfully posted!',2000,'green rounded');
                    },
                    error: function (e) {
                        Materialize.toast('Photo failed to post!',2000,'red rounded');
                    }
                });
                e.preventDefault();
            });
        });
    });
</script>
<script type="text/javascript">
    jQuery(document).ready(function ($) {
        $(function () {
            $('.deleteForm').on('submit', function (e) {
                var self = this;
                $.ajax({
                    type: 'get',
                    url: '/digitalbox/deletephoto',
                    data: $(this).serialize(),
                    success: function (data) {
                        var element = document.getElementById(self.id).parentNode;
                        element.parentNode.removeChild(element);
                        Materialize.toast('Photo successfully deleted!',3000,'yellow rounded');
                    },
                    error: function (e) {
                        Materialize.toast('Photo failed to delete!',3000,'yellow rounded');
                    }
                });
                e.preventDefault();
            });
        });
    });
</script>
</body>
</html>
