function register(){
    $("#main").css("display","none");
    $("#register").css("display","inline");
}

function isRepeat() {
    var username = $("#check_username").val();
    $.get({
        url: "/user/" + username,
        success: function (data) {
            var message;
            console.log("后端传入数据为" + data);
            if (data.toString() == 'none') {
                message = "正确";
                $("#check_tip").css("color", "green");
                $("#check_tip").html(message);
                $("#check_btn").attr("type", "submit");
            } else {
                message = "用户名已存在，请重新输入";
                $("#check_tip").css("color", "red");
                $("#check_tip").html(message);
                $("#check_btn").attr("type", "hidden");
            }
        }
    })
}

function changePwd() {
    $("#main").css("display","none");
    $("#changePwd").css("display","inline");
}

function isNameCorrect() {
    var username = $("#change_username").val();
    $.get({
        url: "/user/" + username,
        success: function (data) {
            var message;
            console.log("后端传入数据为" + data);
            if (data.toString() == 'exist') {
                message = "正确";
                $("#change_tip").css("color", "green");
                $("#change_tip").html(message);
            } else {
                message = "用户名错误，请重新输入";
                $("#change_tip").css("color", "red");
                $("#change_tip").html(message);
            }
        }
    })
}

function isPwdCorrect() {
    var oldPwd = $("#change_oldPassword").val();
    $.post({
        url: "/password",
        data: {
            "username": $("#change_username").val(),
            "oldPwd": oldPwd
        },
        success: function (data) {
            var message;
            console.log(data);
            if (data == 'ok') {
                message = "正确";
                $("#change_tip1").css("color", "green");
                $("#change_tip1").html(message);
            } else {
                message = "密码输入有误！";
                $("#change_tip1").css("color", "red");
                $("#change_tip1").html(message);
            }
        }
    })
}

function isSame() {
    var first = $("#change_first").val();
    var second = $("#change_second").val();
    var message;
    if(first == second) {
        message = "正确";
        $("#change_tip2").css("color", "green");
        $("#change_tip2").html(message);
        $("#change_btn").attr("type", "submit");
    } else {
        message = "两次输入不一致！";
        $("#change_tip2").css("color", "red");
        $("#change_tip2").html(message);
        $("#change_btn").attr("type", "hidden");
    }
}