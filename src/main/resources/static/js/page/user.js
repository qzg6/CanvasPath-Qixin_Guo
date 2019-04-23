var URLS = {};
var config = {
    '.chosen-select': {},
    '.chosen-select-deselect': {
        allow_single_deselect: true
    },
    '.chosen-select-no-single': {
        disable_search_threshold: 10
    },
    '.chosen-select-no-results': {
        no_results_text: 'Oops, nothing found!'
    },
    '.chosen-select-width': {
        width: "95%"
    }
}
$(function(){
    initChose();
    $(".chosen-container").css("width", "100%");
    //获取所有资源地址
    setTimeout(function(){
        parent.requestData("/js/page/data.json", "get", {}, function(data){
            URLS = data;
            userInit()
        });
    }, 1000);
});
function initChose(){
    for (var selector in config) {
        $(selector).chosen(config[selector]);
    }
}
function userInit(){
    //用户-修改
    $("#user_save").click(function(){
        var user = {};
        user.id = $("#user_name").attr("data-id");
        user.name = $("#user_name").val();
        user.user_password  = $("#user_pwd").val();
        user.personal_id = $("#user_persid").val();
        user.age = $("#user_age").val();
        user.gender = $("input[name='userGender']:checked").val();
        user.email_address = $("#user_email").val();
        user.home_address = $("#user_home").val();
        user.office_location = $("#user_office").val();
        user.job_title = $("#user_job").val();
        user.dep_id = JSON.stringify($("#user_dpt").val());
        user.postal_code = $("#user_code").val();
        pushUserModify(user);
    });

    pullDepartmentOptionList("user_dpt", function(dpt_data){
        $("#user_dpt").trigger("chosen:updated");
        pullUserDetails();
    });
}

/**
 * 修改用户
 */
function pushUserModify(param){
    parent.requestData(URLS.root + URLS.user.modify, "post", param, function(result){
        parent.toast("info", "modify data success!", "tip");
    });
}
/**
 * 获取用户信息
 */
function pullUserDetails(){
    parent.requestData(URLS.root + URLS.user.details, "get", {}, function(result){
        var data = result.data;
        if(data){
            $("#user_name").attr("data-id", data.id);
            $("#user_name").val(data.name);
            $("#user_pwd").val(data.user_password);
            $("#user_persid").val(data.personal_id);
            $("#user_age").val(data.age);
            if(data.gender == "male"){
                $("#user_male").attr("checked", "checked");
            }else{
                $("#user_female").attr("checked", "checked");
            }
            if(data.category == 3){
                $("#user_office").attr("disabled", "disabled");
            }else{
                $("#user_office").val(data.office_location);
            }
            $("#user_email").val(data.email_address);
            $("#user_home").val(data.home_address);
            $("#user_job").val(data.job_title);
            $("#user_dpt").val(JSON.parse(data.dep_id));
            $("#user_code").val(data.postal_code);
            $("#user_dpt").trigger("chosen:updated");
        }
    });
}
/**
 * 加载部门数据
 */
function pullDepartmentOptionList(targetSelect, callback){
    parent.requestData(URLS.root + URLS.manager.department.list, "get", {}, function(result){
        var data = result.data;
        if(targetSelect){
            var ops = "";
            for(var i in data){
                ops +=  '<option value="'+ data[i].id +'">'+ data[i].depName +'</option>';           
            }
            $("#"+targetSelect).empty();
            $("#"+targetSelect).append(ops);
        } 
        callback(data);
    });
}