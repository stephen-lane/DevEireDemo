function commentLogout(ev) {
    ev.preventDefault();
    $.ajax({
        url : "/logout",
        type : "POST",
        success : function(response) {
            $('.comment-box').addClass("signIn");
            $('.comment-box').html(response);
            $('.bsp-comments .bsp-flag').remove();
            $('.bsp-comments .bsp-reply').remove();
            $('.bsp-comments .commentReply').remove();
        }
    });

}
function commentSubmit() {
    var commentText = $("#comment").val();
    var usr = $("#user").val();
    var stry = $("#id").val();
    var capt = $("#captchaResponse").val();
    $.ajax({
        url : "/comment",
        data : {
            "comment" : commentText,
            "id" : stry,
            "user" : usr,
            "action" : "submit",
            "captcha" : capt
        },
        type : "POST",
        success : function(response) {
            $("#commentSubmit").text(response);
        }
    });
}

function commentLogin(ev) {
    ev.preventDefault();
    var serviceId = "";
    if (typeof ($(ev.target).attr("data")) != 'undefined') {
        serviceId = $(ev.target).attr("data");
    } else {
        serviceId = $(ev.target).find('i').attr("data");
    }
    var $url = "/social/accountLink?userCreator=SocialJordanSpiethUserCreator&serviceId=" + serviceId;
    var width = 500;
    var height = 500;
    var left = (screen.width / 2) - (width / 2);
    var top = (screen.height / 2) - (height / 2);

    window.open($url, 'share', 'width=' + width + ',height=' + height + ',top=' + top + ', left=' + left
            + ' toolbar=1,resizable=0');
    return false;
}
function commentFlag(ev) {
    ev.preventDefault();
    var commentId = $(ev.target).attr("commentId");
    var userId = $(ev.target).attr("userId");
    $.ajax({
        url : "/comment",
        data : {
            "id" : commentId,
            "user" : userId,
            "action" : "flag"
        },
        type : "POST",
        success : function(response) {

            $(ev.target).replaceWith(
                    '<a style="pointer-event:none; cursor:default;font-size: 12px; color: #ccc" class="bsp-flag" >'
                            + response + '</a>');
        }
    });
}
function commentReplyShow(ev) {
    ev.preventDefault();
    var theDiv = $(ev.target).parents(".bsp-comment-txt").children(".commentReply")
    if (theDiv.attr("isEmpty") == "true") {
        $(".commentReply[isEmpty='false']").html("");
        $(".commentReply[isEmpty='false']").attr("isEmpty", "true");
        theDiv.attr("isEmpty", "false");
        var replyLink = "/render/common/commentReplyForm.jsp";
        theDiv.load(replyLink);
        // need to set the timeout so that the div has time to load before we
        // try to stuff it full of info
        setTimeout(function() {
            theDiv.children("#id").attr("value", $(ev.target).attr("storyId"));
            theDiv.children("#parentId").attr("value", $(ev.target).attr("commentId"));
            theDiv.children("#user").attr("value", $(ev.target).attr("userId"));
        }, 300);
    } else {
        theDiv.attr("isEmpty", "true");
        theDiv.html("");
    }

}
function commentReply(ev) {
    ev.preventDefault();
    var theDiv = $(ev.target).parents(".commentReply");
    var commentText = theDiv.children("#comment").val();
    var parentId = theDiv.children("#parentId").val();
    var storyId = theDiv.children("#id").val();
    var userId = theDiv.children("#user").val();

    $.ajax({
        url : "/comment",
        data : {
            "comment" : commentText,
            "id" : storyId,
            "user" : userId,
            "parent" : parentId,
            "action" : "submit"
        },
        type : "POST",
        success : function(response) {
            $(".commentReply[isEmpty='false']").html(
                    "Your reply has been submitted for review, hit Reply to submit another reply.");
            $(".commentReply[isEmpty='false']").attr("isEmpty", "true");
        }
    });
}
function commentToggleChildren(ev) {
    ev.preventDefault();
    var theDiv = $(ev.target).parents(".bsp-comments-container").children(".childDiv");
    if (theDiv.attr("style") == "display: none; width: 75%;") {
        theDiv.attr("style", "display: inline; width: 75%;");
        $(ev.target).html("Hide Replies");
    } else {
        theDiv.attr("style", "display: none; width: 75%;");
        $(ev.target).html("Show Replies");
    }
}
function noCaptcha() {
    alert("Please complete the reCAPTCHA");
}
function enableSubmit(response) {
    $(".submitComment").attr('onclick', "commentSubmit()");
    $("#captchaResponse").attr("value", response);
}