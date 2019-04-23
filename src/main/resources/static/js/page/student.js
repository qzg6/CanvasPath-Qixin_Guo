var URLS = {};
$(function(){
    //获取所有资源地址
    setTimeout(function(){
        parent.requestData("/js/page/data.json", "get", {}, function(data){
            URLS = data;
            userInit()
        });
    }, 1000);
});
var TCH = {};
function userInit(){
    //课程搜索
    $("#user_sch").click(function(){
        pullUserCourse({"c_name": $("#user_sch_str").val()});
    });
    //点击课程查看信息
    $("#section_list").on("click", "tr", function(){
        $(this).siblings().css("background-color", "#FFFFFF");
        $(this).css("background-color", "#F5F5F5");
        pullUserProject({"c_id": $(this).attr("data-id")});
        var ids = JSON.parse($(this).attr("data-tid"));
        ids = JSON.parse(ids);
        ids = ids.join(",");
        if(ids){
            pullTeachersInfo({"t_id": ids});
        }
    });
    pullTecherOptionList(null, function(data){
        TCH = data;
        pullUserCourse({"c_name": ""});
    });
}
/**
 * 获取课程列表
 */
function pullUserCourse(param){
    parent.requestData(URLS.root + URLS.student.list, "get", param, function(result){
        var data = result.data;
        var trs = "";
        $("#section_list").empty();
        for(var i in data){
            var nms =  matchTeacherNames(TCH, JSON.parse(data[i].teachersId));
            trs +=  '<tr data-id="'+ data[i].id +'" data-tid=\''+ JSON.stringify(data[i].teachersId) +'\'>' +
                    '<td>'+ data[i].id +'</td>' +
                    '<td>'+ data[i].sectionName +'</td>' +
                    '<td>'+ data[i].days_time +'</td>' +
                    '<td>'+ data[i].room +'</td>' +
                    '<td title = "'+ nms +'">'+ nms +'</td>' +
                    '<td>'+ data[i].meeting_dates +'</td>' +
                    '<td>'+ data[i].score +'</td>' +
                    '</tr>';
        }
        $("#section_list").append(trs);
    });
}
/**
 * 获取项目表
 */
function pullUserProject(param){
    parent.requestData(URLS.root + URLS.student.project, "get", param, function(result){
        var data = result.data;
        $("#project_list").empty();
        var trs = "";
        for(var i in data){
            trs +=  '<tr>' +
                    '<td>'+ data[i].title +'</td>' +
                    '<td>'+ data[i].sponsor_info +'</td>' +
                    '<td>'+ data[i].score +'</td>' +
                    '<td>'+ data[i].avg +'</td>' +
                    '<td>'+ data[i].max +'</td>' +
                    '<td>'+ data[i].min +'</td>' +
                    '<td>'+ data[i].ctm.split(" ")[0] +'</td>' +
                    '</tr>';
        }
        $("#project_list").append(trs);
    });
}
/**
 * 获取教师列表 
 */
function pullTeachersInfo(param){
    parent.requestData(URLS.root + URLS.student.teacher, "get", param, function(result){
        var data = result.data;
        $("#teacher_list").empty();
        var trs = "";
        for(var i in data){
            trs +=  '<tr>' +
                    '<td>'+ data[i].name +'</td>' +
                    '<td>'+ data[i].gender +'</td>' +
                    '<td>'+ data[i].job_title +'</td>' +
                    '<td>'+ data[i].email_address +'</td>' +
                    '<td>'+ data[i].office_location +'</td>' +
                    '</tr>';
        }
        $("#teacher_list").append(trs);
    });
}
/**
 * 匹配老师名称
 */
function matchTeacherNames(tchList, ids){
    var strs = "";
    for(var i in ids){
        for(var j in tchList){
            if(ids[i] == tchList[j].id){
                strs += tchList[j].name + ",";
            }
        }
    }
    return strs ? strs.substring(0, strs.length -1) : "";
}
/**
 * 加载教师列表 
 */
function pullTecherOptionList(targetSelect, callback){
    parent.requestData(URLS.root + URLS.manager.user.list_teacher, "get", {}, function(result){
        var data = result.data;
        if(targetSelect){
            var ops = "";
            for(var i in data){
                ops +=  '<option value="'+ data[i].id +'">'+ data[i].name +'</option>';           
            }
            $("#"+targetSelect).empty();
            $("#"+targetSelect).append(ops);
        } 
        callback(data);
    });
}