var URLS = {};
var TCH = {};
$(function(){
    //获取所有资源地址
    setTimeout(function(){
        parent.requestData("/js/page/data.json", "get", {}, function(data){
            URLS = data;
            init();
        });
    }, 1000);
});
function init(){
     //部门切换事件
     $("#cou_dpt").change(function(){
        pullCourseOptionList({"dep_id": $("#cou_dpt").val(), "sch_str": $("#cou_sch_str").val()}, "cou_cou", function(cou_data){});
    });
    //搜索
    $("#cou_sch").click(function(){
        pullSecctionList({"cou_id": $("#cou_cou").val(), "sch_str":  $("#cou_sch_str").val()});
    });
    //选课
    $("#section_list").on("click", ".cou_select", function(){
        var that = this;
        if($(".select").length >= 1 && !$(this).hasClass("select")){
            //取消之前选择的课
            parent.sweetTip("warning", "Just select one secction, Please cancel the last one.");
        }else{
            //判断当前是否被选中
            var sec_id = $(this).attr("data-id"); 
            if($(this).hasClass("select")){
                pushUserSelect({"sec_id": sec_id}, function(data){
                    $(".cou_select").removeClass("select");
                    parent.toast("info", "section unsubscribe success!");
                });
            }else{
                pushUserSelect({"sec_id": sec_id}, function(data){
                    if (data != sec_id){
                        parent.sweetTip("warning", "the select course does not exceed the course limit");
                    }else{
                        $(".cou_select").removeClass("select");
                        $(that).addClass("select");
                        parent.toast("info", "section subscribe succss!");
                    }
                });
            }
        }
    });
    //初始化加载
    pullDepartmentOptionList("cou_dpt", function(dpt_data){
        pullCourseOptionList({"dep_id": dpt_data[0].id, "sch_str": $("#cou_sch_str").val()}, "cou_cou", function(cou_data){
            pullTecherOptionList(null, function(tch_data){
                TCH = tch_data;
                pullSecctionList({"cou_id": cou_data[0].id, "sch_str": ""});
            });
        })
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
    return strs ? strs.substring(0, strs.length -2) : "";
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
/**
 * 加载课程列表数据
 */
function pullCourseOptionList(param, targetSelect, callback){
    parent.requestData(URLS.root + URLS.manager.course.list, "get", param, function(result){
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
/**
 * secction列表
 */
function pullSecctionList(param){
    parent.requestData(URLS.root + URLS.select.list, "get", param, function(result){
        var data = result.data; 
        $("#section_list").empty();
        var trs = "";
        for(var i in data){
            var tchs = matchTeacherNames(TCH, JSON.parse(data[i].teachers_id));
            var tchdesc = tchs.length > 10 ? tchs.substring(0, 10)+"..." : tchs;
            var seleced = data[i].select ? "select" : "";
            trs += '<tr>'+
                    '<td>'+ data[i].id +'</td>'+
                    '<td>'+ data[i].sectionName +'</td>'+
                    '<td title="'+ tchdesc +'">'+tchs +'</td>'+
                    '<td>'+ data[i].days_time +'</td>'+
                    '<td>'+ data[i].meeting_dates +'</td>'+
                    '<td>'+ data[i].room +'</td>'+
                    '<td>'+ data[i].number +'</td>'+
                    '<td><i class="fa fa-circle '+ (data[i].status == "open" ? "primary" : "") +'"></i></td>'+
                    '<td>'+ data[i].score +'</td>'+
                    '<td>'+
                    '<button class="btn '+ seleced +' btn-circle cou_select" type="button" data-id="'+ data[i].id +'"><i class="fa fa-check"></i></button></td>'+
                    '</td>'+
                    '</tr>';
        }
        $("#section_list").append(trs); 
    });
}
/**
 * 选课
 */
function pushUserSelect(param, callback){
    parent.requestData(URLS.root + URLS.select.select, "get", param, function(result){
        var data = result.data;
         callback(data);
    });
}