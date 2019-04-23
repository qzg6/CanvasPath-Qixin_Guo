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
var section = {};
var projectId;//当前点击项目
var students = [];
$(function(){
    initChose();
    //获取所有资源地址
    setTimeout(function(){
        parent.requestData("/js/page/data.json", "get", {}, function(data){
            URLS = data;
            teacherInit()
        });
    }, 1000);
});
function initChose(){
    for (var selector in config) {
        $(selector).chosen(config[selector]);
    }
}
var TCH = {};
function teacherInit(){
    //课程搜索
    $("#teacher_sch").click(function(){
        pullTeacherCourse({"c_name": $("#teacher_sch_str").val()});
    });
    //点击课程查看信息
    $("#section_list").on("click", "tr", function(){
        $(this).siblings().css("background-color", "#FFFFFF");
        $(this).css("background-color", "#F5F5F5");
        if($(this).attr("data-sid") != 'null'){
            //课程章节编号
            section.id = $(this).attr("data-id");
            //课程编号
            section.cid = $(this).attr("data-cid");
            pullStudentList({"s_id": $(this).attr("data-sid")});
            pullProjectList({"c_id": section.id});
        }
    });
    //增加项目
    $("#pro_add").click(function(){
        var tempStr ='<form class="form-horizontal">' +
                        '<div class="form-group">' +
                            '<label class="col-sm-3 control-label">Title:</label>' +
                            '<div class="col-sm-8">' +
                                '<input type="text" placeholder="project title" class="form-control" id="pro_title">' +
                            '</div>' +
                        '</div>' +
                        '<div class="form-group">' +
                            '<label class="col-sm-3 control-label">Category:</label>' +
                            '<div class="col-sm-8">' +
                                '<select class="form-control" id="pro_cate">' +
                                    '<option value="regular" selected>Regular</option>' +
                                    '<option value="capstone">Capstone</option>' +
                                '</select>' +
                            '</div>' +
                        '</div>' +
                    '</form>'
        layer.open({
            title: "",
            area:[ "400px", "auto"],
            content: tempStr,
            yes: function (index, layero) {
                var title = $("#pro_title").val();
                var cate = $("#pro_cate").val();
                if (title && cate && section.id) {
                   pushPorjectAdd({"title": title, "category": cate, "courseId": section.cid, "courseSectionId": section.id});
                }
                layer.close(index); //如果设定了yes回调，需进行手工关闭
            }
        });
    });
    //删除项目
    $("#pro_list").on("click", ".pro_del", function(){
        var e_id = $(this).attr("data-id");
        if(e_id){
            pushProjectDel({"e_id": e_id});
        }
    });
    //点击项目
    $("#pro_list").on("click", "tr", function(){
        $(this).siblings().css("background-color", "#FFFFFF");
        $(this).css("background-color", "#F5F5F5");
        var e_id = $(this).attr("data-id");
        if(e_id){
            if($(this).children().eq(1).text() == 'general'){
                $("#team_add").attr("disabled", "disabled");
            }else{
                $("#team_add").removeAttr("disabled");
            }
            projectId = e_id;
            pullStudentTeamList({"e_id": e_id});
        }
    });
    //增加组
    $("#team_add").click(function(){
        var tempStr ='<form class="form-horizontal">' +
                    '<div class="form-group">' +
                        '<label class="col-sm-3 control-label">Group Name:</label>' +
                        '<div class="col-sm-8">' +
                            '<input type="text" placeholder="group name" class="form-control" id="team_name">' +
                        '</div>' +
                    '</div>' +
                    '<div class="form-group">' +
                        '<label class="col-sm-3 control-label">Support:</label>' +
                        '<div class="col-sm-8">' +
                            '<input type="text" placeholder="support info" class="form-control" id="team_support">' +
                        '</div>' +
                    '</div>' +
                    '<div class="form-group">' +
                        '<label class="col-sm-3 control-label">Member:</label>' +
                        '<div class="col-sm-8">' +
                        '<select data-placeholder="" class="chosen-select" multiple id="team_student">'+
                        '</select>' +
                        '</div>' +
                    '</div>' +
                '</form>';
            layer.open({
            title: "",
            area:[ "500px", "auto"],
            content: tempStr,
            yes: function (index, layero) {
                var name = $("#team_name").val();
                var member = $("#team_student").val();
                var support = $("#team_support").val();
                member = member.join(',');
                if(name && member && support){
                    pushStudentTeamAdd({"title": name, "courseSectionEntryId": projectId, "sponsor_info": support, "studentsId": member});
                }else{
                    parent.toast("warning", "please improve the information", "tip");
                }
                layer.close(index); //如果设定了yes回调，需进行手工关闭
                }
            });
            setTimeout(function(){
                initChose();
                $(".chosen-container").css("width", "100%");
                fetchUserOption();
            }, 50);
           
    });
    //删除组
    $("#team_list").on("click", ".team_del", function(){
        var t_id = $(this).attr("data-id");
        if(t_id){
            pushStudentTeamDel({"t_id": t_id});
        }
    });
    //修改分数
    $("#team_list").on("blur", "input", function(){
        var score = $(this).val();
        var tid = $(this).attr("data-id");
        //判断是否位数组
        if(!isNaN(score)){
            pushStudentTeamMod({"t_id": tid, "score": score});
        }else{
            parent.toast("warning", "must be number", "tip")
        }
    });
    //颜色定位
    pullTeacherCourse({"c_name": ""});
}
/**
 * 获取课程列表
 */
function pullTeacherCourse(param){
    parent.requestData(URLS.root + URLS.teacher.list, "get", param, function(result){
        var data = result.data;
        var trs = "";
        $("#section_list").empty();
        for(var i in data){
            trs +=  '<tr data-id="'+ data[i].id +'" data-cid="'+ data[i].courseId +'" data-sid='+ data[i].studentsId +'>' +
                    '<td>'+ data[i].id +'</td>' +
                    '<td>'+ data[i].sectionName +'</td>' +
                    '<td>'+ data[i].days_time +'</td>' +
                    '<td>'+ data[i].room +'</td>' +
                    '<td>'+ data[i].meeting_dates +'</td>' +
                    '<td>'+ data[i].number +'</td>' +
                    '<td>'+ data[i].score +'</td>' +
                    '</tr>';
        }
        $("#section_list").append(trs);
    });
}
/**
 * 获取学生列表 
 */
function pullStudentList(param){
    parent.requestData(URLS.root + URLS.teacher.student, "get", param, function(result){
        var data = result.data;
        students = data;
        var trs = "";
        $("#student_list").empty();
        for(var i in data){
            trs +=  '<tr data-id="'+ data[i].id +'">' +
                    '<td>'+ data[i].id +'</td>' +
                    '<td>'+ data[i].name +'</td>' +
                    '<td>'+ data[i].personal_id +'</td>' +
                    '<td>'+ data[i].gender +'</td>' +
                    '<td>'+ data[i].age +'</td>' +
                    '<td>'+ data[i].email_address +'</td>' +
                    '<td>'+ data[i].postal_code +'</td>' +
                    '</tr>';
        }
        $("#student_list").append(trs);
    });
}
/**
 * 获取项目表
 */
function pullProjectList(param){
    parent.requestData(URLS.root + URLS.teacher.project, "get", param, function(result){
        var data = result.data;
        $("#pro_list").empty();
        var trs = "";
        for(var i in data){
            trs +=  '<tr data-id="'+ data[i].id +'">' +
                    '<td>'+ data[i].title +'</td>' +
                    '<td>'+ data[i].category +'</td>' +
                    '<td>'+ (data[i].avg || '0') +'</td>' +
                    '<td>'+ (data[i].max || '0') +'</td>' +
                    '<td>'+ (data[i].min || '0') +'</td>' +
                    '<td>'+ data[i].ctm.split(" ")[0] +'</td>' +
                    '<td><button class="btn btn-danger btn-circle pro_del" type="button" data-id="'+ data[i].id+'"><i class="fa fa-trash"></i></button></td>' +
                    '</tr>';
        }
        $("#pro_list").append(trs);
    });
}
 
/**
 * 增加项目
 */
function pushPorjectAdd(param){
    parent.requestData(URLS.root + URLS.teacher.addPro, "post", param, function(result){
        var data = result.data;
        if(data){
            parent.toast("success", "create success!", "tip");
        }
        pullProjectList({"c_id": section.id});
    });
}
/**
 * 删除项目
 */
function pushProjectDel(param){
    parent.requestData(URLS.root + URLS.teacher.delPro, "post", param, function(result){
        var data = result.data;
        if(data){
            parent.toast("success", "delete success!", "tip");
        }
        pullProjectList({"c_id": section.id});
    });
}
/**
 * 获取组
 */
function pullStudentTeamList(param){
    parent.requestData(URLS.root + URLS.teacher.teamList, "get", param, function(result){
        var data = result.data;
        $("#team_list").empty();
        var trs = "";
        for(var i in data){
            trs +=  '<tr>' +
                    '<td>'+ data[i].title +'</td>' +
                    '<td>'+ fetchUserLabel(data[i].studentsId) +'</td>' +
                    '<td><input type="text" style="border: none; width: 60px; text-align: center;" value="'+ data[i].score +'" data-id="'+ data[i].id +'"></td>' +
                    '<td title="'+ data[i].sponsor_info +'">'+ data[i].sponsor_info +'</td>' +
                    '<td><button class="btn btn-danger btn-circle team_del" type="button" data-id="'+ data[i].id+'"><i class="fa fa-trash"></i></button></td>' +
                    '</tr>';
        }
        $("#team_list").append(trs);
    });
}
/**
 * 创建分组
 */
function pushStudentTeamAdd(param){
    parent.requestData(URLS.root + URLS.teacher.addTeam, "get", param, function(result){
        var data = result.data;
        if(data){
            parent.toast("success", "crate success!", "tip");
        }
        pullStudentTeamList({"e_id": projectId});
    });
}
/**
 * 删除分组
 */
function pushStudentTeamDel(param){
    parent.requestData(URLS.root + URLS.teacher.delTeam, "get", param, function(result){
        var data = result.data;
        if(data){
            parent.toast("success", "delete success!", "tip");
        }
        pullStudentTeamList({"e_id": projectId});
    });
}
/**
 * 修改分数
 */
function pushStudentTeamMod(param){
    parent.requestData(URLS.root + URLS.teacher.modTeam, "get", param, function(result){
        var data = result.data;
    });
}

/**
 * 获取学员名称
 * @param {*} usersIds 
 */
function fetchUserLabel(usersIds){
    var lables = ""
    var ids = usersIds.split(",");
    for(var i in ids){
        for(var j in students){
            if(ids[i] == students[j].id){
                lables += '<span class="label label-info">'+ students[j].name +'</span> '
            }
        }
    }
    return lables;
}

function fetchUserOption(){
    var opts = "";
    for(var i in students){
        opts += '<option value="'+ students[i].id +'">'+ students[i].name +'</option>';
    }
    $("#team_student").append(opts);
    $("#team_student").trigger("chosen:updated");
}