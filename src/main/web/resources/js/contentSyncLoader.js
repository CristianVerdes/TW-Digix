jQuery(document).ready(function () {
    ContentLoader.init();


});

var ContentLoader = {
    flag: true,
    until: 0,
    init: function () {
        this.bindUIActions();
    },

    bindUIActions: function () {
        var self = this;
        $(window).bind('scroll', function () {
            if ($(window).scrollTop() >= $('#myPosts').offset().top + $('#myPosts').outerHeight() - window.innerHeight) {
                if (self.flag) {
                    $('#loader').show();
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
            url: "/settings/contentsync/facebook/photos/nextpage",
            data: {"until": self.until},
            dataType: 'json',
            success: function (data) {
                console.log("SUCCESS: ", data);
                if (data == null) {
                    $('#loader').hide();
                    self.flag = false;
                }
                self.display(data);

            },
            error: function (e) {
                $('#loader').hide();
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
            console.log(data[i].path);
            text += "<div>" +
                "<img class='photo' src='" + data[i].path + "'/>" +
                "<form id='command' class='button' action='/settings/contentsync/facebook/photos/posts/store' method='post'>" +
                "<input type='hidden' name='photoURL' value='" + data[i].path + "'>" +
                "<div class='buttonInput'>" +
                "<input type='submit' value='Store Photo'/> </div> </form> </div> ";
        }
        $('#loader').hide();
        $('#myPosts').append(text);
    }

};
