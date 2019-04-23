var URLS = {};
requestData("/js/page/data.json", "get", {}, function(data){
    URLS = data;
});

$(function(){
    toastr.options = {
        "closeButton": true,
        "debug": false,
        "progressBar": true,
        "positionClass": "toast-bottom-right",
        "onclick": null,
        "showDuration": "400",
        "hideDuration": "1000",
        "timeOut": "7000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    }
    var user = sessionStorage.getItem("user");
    if(user){
        user = JSON.parse(user);
        $("#name").text(user.name);
        if(user.category == 2){
            $(".student").removeClass("fa-list").addClass("fa-lock");
            $(".admin").removeClass("fa-list").addClass("fa-lock");
            $(".admin").parent().eq(0).attr("href", "");
            $(".student").parent().eq(0).attr("href", "");
            $(".student").parent().eq(1).attr("href", "");
            $("#category").text("teacher");
            $("#ifm").attr("src", "techer_course.html");
        }else if(user.category == 3){
            $(".teacher").removeClass("fa-list").addClass("fa-lock");
            $(".admin").removeClass("fa-list").addClass("fa-lock");
            $(".admin").parent().eq(0).attr("href", "")
            $(".teacher").parent().eq(0).attr("href", "")
            $("#category").text("student");
            $("#ifm").attr("src", "student_course.html");
        }else{
            $("#category").text("admin");
        }
    }else{
        location.href="/login.html";
    }
    $("#loginOut").click(function(){
        sessionStorage.removeItem("user");
        location.href = "/login.html";
    });
});

//公共方法
/**
 * 弹出框
 */
function requestData(ur, tp, dt, callback){
    dt = dt || {};
    dt.timstemp =  new Date().getTime();
    $.ajax({
        url: ur,
        type: tp || "get",
        dataType: "json",
        data: dt,
        success: function(data){
            if(data.code == 0){
                try{
                    callback(data);
                }catch(err){
                    toast("warning", "result analysis error!", data.code);
                    console.log(data, err);
                }
                
            }else{
                toast("warning", "response result formart error!", data.code);
                console.log(data);
            }
        },
        error:function(err){
            toast("error", err.statusText, err.status);
            console.log(ur, tp, dt, err.status, err.statusText)
        }
    });
}
function sweetTip(t, tx){
   sweet(t, tx);
}
function sweetState(t, tx, tp){
    sweet(t, tx, tp);
}
function sweet(t, tx, tp, scb, cfbt, cbt){
    var op = {};
    if(t){
        op.title = t;
    }
    if(tx){
        op.text = tx;
    }
    if(tp){
        op.tp = tp;
    }
    if(scb && cfbt && cbt){
        op.showCancelButton= scb,
        op.confirmButtonColor= "#DD6B55",
        op.confirmButtonText= cfbt,
        op.closeOnConfirm= false,
        op.cancelButtonText= cbt,
        op.closeOnCancel= false
    }
    swal(op, function(isConfirm){
        if(scb && cfbt && cbt){
            if (isConfirm) {
                swal("删除成功！", "信息已删除。", "success");
            } else {
                swal("已取消", "操作已取消！", "error");
            }
        }
    });
}
/**
 * 通知
 */
function toast(tp, msg, title){
    //tp [success,info,warning,error]
    toastr[tp](msg, title || "tip")
}
