jQuery(document).ready(function () {
    ContentLoader.init();


});

var ContentLoader = {
    numberPage: 1,
    flag: true,
    init: function () {
        this.bindUIActions();
    },

    bindUIActions: function () {
        var self = this;
        console.log('test');
        $(window).bind('scroll', function () {
            if ($(window).scrollTop() >= $('#newsFeed').offset().top + $('#newsFeed').outerHeight() - window.innerHeight) {
                self.numberPage++;
                if (self.flag) {
                    self.searchViaAjax();
                }
            }
        });
    },

    searchViaAjax: function () {

        var self = this;

        $.ajax({
            type: "GET",
            contentType: "application/json",
            url: "/feed",
            data: {"pageNumber": self.numberPage},
            dataType: 'json',
            success: function (data) {
                console.log("SUCCESS: ", data);
                if (data == null) {
                    self.flag = false;
                }
                self.display(data);

            },
            error: function (e) {
                console.log("ERROR: ", e);
            },
            done: function (e) {
                console.log("DONE: ", e);
                self.enableSearchButton(true);
            }
        });

    },

    enableSearchButton: function (flag) {
        $("#btn-search").prop("disabled", flag);
    },

    display: function (data) {

        var text = "";
        for (var i in data) {
            console.log("data : " + data[i].userID);
            if (data[i].typeID == "1") {
                text += "<div class='col s4 white-text'> <div class='card medium  grey darken-3'>" +
                    "<div class='card-image waves-effect waves-block waves-light'>" +
                    "<img class='activator' src='" + data[i].path + "'> </div>" +
                    "<div class='card-content grey darken-3'>" +
                    "<a href='/profile/userdetails?userID=" + data[i].userID + "'><span class='card-title white-text' >" + data[i].user.firstName + " " + data[i].user.lastName +
                    "</span></a><i class='material-icons activator right'>more_vert</i>" +
                    " <p>" + data[i].postedDateString + "</p>" +
                    "</div> <div class='card-reveal grey darken-3'> <span class='card-title white-text'>Details<i " +
                    "class='material-icons right'>close</i></span>";
                if (data[i].description != null) {
                    text += "<i class='material-icons'>description</i> Description" +
                        "<br/>" + data[i].description + "<br/>";
                }
                if (data[i].location.city != null) {

                    text += "<i class='material-icons'>my_location</i> Location <br/><div class='container' >";
                    text += data[i].location.city + "<br/>";
                    text += data[i].location.country + "<br/>";
                    text += data[i].location.street + "<br/></div>";
                }

                text += "<i class='material-icons'>contacts</i> Tags <br/>";
                for (var j in data[i].tagList) {
                    text += "<div class='chip'>" + data[i].tagList[j].tagName + "</div>";
                }
                text += "</div> </div> </div>";
            }

            if (data[i].typeID == "2") {
                console.log(data[i].provider);
                text += "<div class='col s4 white-text'>";
                if (data[i].provider == "facebook") {
                    text += "<div class='card medium grey darken-3'> <div class='video-container'>" +
                        "<iframe src='https://www.facebook.com/plugins/video.php?href=" + data[i].path +
                        "&show_text='0'&width='560'frameborder='0' allowfullscreen='true'></iframe>" +
                        "</div> <div class='card-content grey darken-3'>" +
                        "<a href='/profile/userdetails?userID=" + data[i].userID + "'><span class='card-title white-text'>" + data[i].user.firstName + " " + data[i].user.lastName + "</span></a>" +
                        "<p>" + data[i].postedDateString + "</p>";
                    text += "</div> </div>";
                }
                if (data[i].provider == "google") {
                    text += "<div class='card medium grey darken-3'> <div class='video-container'>" +
                        "<iframe src='" + data[i].path +
                        "'frameborder='0' allowfullscreen='true'></iframe>" +
                        "</div> <div class='card-content grey darken-3'>" +
                        "<a href='/profile/userdetails?userID=" + data[i].userID + "'><span class='card-title white-text'>" + data[i].user.firstName + " " + data[i].user.lastName + "</span></a>" +
                        "<p>" + data[i].postedDateString + "</p>";
                    text += "</div> </div>";
                }
                text += "</div>";
            }

            if (data[i].typeID == "3") {
                text += "<div class='col s4 white-text'> " +
                    "<div class='card medium grey darken-3'>" +
                    "<div class='card-image'> " +
                    "<img src='" + data[i].thumbnailURL + "'>" +
                    "</div> " +
                    "<div class='card-content grey darken-3'>" +
                    "<a href='/profile/userdetails?userID=" + data[i].userID + "'<span class='card-title white-text'>" + data[i].user.firstName + " " + data[i].user.lastName + "</span></a>" +
                    "<p>" + data[i].postedDateString + "</p>" +
                    "</div> " +
                    "<div class='card-action'>" +
                    "<a href='" + data[i].path + "'>View document</a>" +
                    "</div> </div> </div>";
            }
        }
        $('.row').append(text);
    }

};
