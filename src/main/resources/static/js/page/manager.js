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
            init();
        });
    }, 1000);
});
var DEP = new Array();
/**
 * 匹配部门名称 
 */
function matchDepName(depList, id){
    for(var i in depList){
        if(depList[i].id == id){
            return depList[i].depName;
        }
    }
}
/**
 * 匹配部门名称表
 */
function matchDepNames(depList, ids){
    var strs = "";
    for(var i in ids){
        for(var j in depList){
            if(ids[i] == depList[j].id){
                strs += depList[j].depName + ",";
            }
        }
    }
    return strs ? strs.substring(0, strs.length -1) : "";
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
function init(){
    initSchoolYear();
    initDepartment();
    initUser();
    initCourse();
    initSecction();
}
function initChose(){
    for (var selector in config) {
        $(selector).chosen(config[selector]);
    }
}

var SYPage = {
    "size": 10,
    "pages": 0,
    "page": 0
};
//学年相关
function initSchoolYear(){
    //增加学年
    $("#sy_add").click(function(){
        $("#school_year_content").hide();
        $("input[name='sy_tag']").attr("data-id", '');
        $("input[name='sy_tag']").val('');
        $("input[name='sy_sdt']").val('');
        $("input[name='sy_edt']").val('');
        $("#school_year").show();
    });
    //学年取消
    $("#sy_cancel").click(function(){
        $("#school_year").hide();
        $("input[name='sy_tag']").attr("data-id", '');
        $("input[name='sy_tag']").val('');
        $("input[name='sy_sdt']").val('');
        $("input[name='sy_edt']").val('');
        $("#school_year_content").show();
    });
    //学年保存
    $("#sy_save").click(function(){
        var sy = {};
        sy.id = $("input[name='sy_tag']").attr("data-id");
        sy.tag = $("input[name='sy_tag']").val();
        sy.sdt = $("input[name='sy_sdt']").val();
        sy.edt = $("input[name='sy_edt']").val();
        if(sy.id){
            pushSchoolYearModify(sy);
        }else{
            pushSchoolYearAdd(sy);
        }
       
    });
    //学年删除
    $("#school_year_list").on("click", ".sy_trash", function(){
        var id = $(this).attr("data-id");
        pushSchoolYearDel({"id": id});
    })
    //学年修改
    $("#school_year_list").on("click", ".sy_wrench", function(){
        $("#school_year_content").hide();
        var sy = $(this).attr("data-data");
        sy = JSON.parse(sy);
        $("input[name='sy_tag']").attr("data-id", sy.id);
        $("input[name='sy_tag']").val(sy.tag);
        $("input[name='sy_sdt']").val(sy.sdt);
        $("input[name='sy_edt']").val(sy.edt);
        $("#school_year").show();
    })
    //学年上一页
    $("#sy_pre_page").click(function(){
        if(SYPage.page - 1 >=0){
            SYPage.page -= 1;
            pullSchoolYearList(SYPage);
        }else{
            parent.toast("info", "No more pages!", "tip")
        }
    });
    //学年下一页
    $("#sy_next_page").click(function(){
        if(SYPage.page + 1 <= SYPage.pages - 1){
            SYPage.page += 1;
            pullSchoolYearList(SYPage);
        }else{
            parent.toast("info", "No more pages!", "tip")
        }
    });
    //学年默认数据加载
    pullSchoolYearList({size: SYPage.size, page: 0});
}

/**
 * 请求学年列表
 */
function pullSchoolYearList(param){
    parent.requestData(URLS.root + URLS.manager.school_year.list, "get", param, function(result){
        $("#sy_cur").text(result.curPage + 1);
        $("#sy_tol").text(result.pages);
        $("#school_year_list").empty();
        SYPage.pages = result.pages;
        var trs = "";
        data = result.data;
        for(var i in data){
            var status = (new Date(data[i].sdt).getTime() <= new Date().getTime() && new Date(data[i].edt).getTime() >= new Date().getTime()) ? "primary" : "";
            trs += '<tr>' +
                    '<td>'+ (i*1 + 1) +'</td>' +
                    '<td>'+ data[i].tag +'</td>' +
                    ' <td>'+ data[i].sdt +'</td>' +
                    '<td>'+ data[i].edt +'</td>' +
                    '<td><i class="fa fa-circle '+ status +'"></i></td>' +
                    '<td>' +
                    '   <button  class="btn btn-danger btn-circle sy_trash" type="button" data-id="'+ data[i].id +'"><i class="fa fa-trash"></i></button>' +
                    '    <button class="btn btn-primary btn-circle sy_wrench" type="button" data-id="'+ data[i].id +'" data-data=\''+ JSON.stringify(data[i]) +'\'><i class="fa fa-wrench"></i></button>' +
                    '</td>'+
                    '</tr>';
        }
        $("#school_year_list").append(trs);
       

    })
}

/**
 * 修改学年
 */
function pushSchoolYearModify(param){
    parent.requestData(URLS.root + URLS.manager.school_year.modify, "post", param, function(result){
        parent.toast("info", "modify data success!", "tip");
        pullSchoolYearList({size: SYPage.size, page: 0});
    })
}

/**
 * 增加学年
 */
function pushSchoolYearAdd(param){
    parent.requestData(URLS.root + URLS.manager.school_year.add, "post", param, function(result){
        parent.toast("info", "insert data success!", "tip");
        pullSchoolYearList({size: SYPage.size, page: 0});
    })
}

/**
 * 删除学年
 */
function pushSchoolYearDel(param){
    parent.requestData(URLS.root + URLS.manager.school_year.del, "get", param, function(result){
        parent.toast("info", "delete data success!", "tip");
        pullSchoolYearList({size: SYPage.size, page: 0});
    })
}

//部门相关
function initDepartment(){
    //部门增加
    $("#dpt_add").click(function(){
        $("#dpt_content").hide();
        $("input[name='dpt_name']").attr("data-id", '');
        $("input[name='dpt_name']").val('');
        $("#dpt").show();
    });
    //部门保存
    $("#dpt_save").click(function(){
        var dpt = {};
        dpt.id = $("input[name='dpt_name']").attr("data-id");
        dpt.name = $("input[name='dpt_name']").val();
        if(dpt.id){
            pushDepartmentModify(dpt);
        }else{
            pushDepartmentAdd(dpt);
        }
    });
    //部门取消
    $("#dpt_cancel").click(function(){
        $("#dpt").hide();
        $("input[name='dpt_name']").attr("data-id", '');
        $("input[name='dpt_name']").val('');
        $("#dpt_content").show();
    });
    //部门删除
    $("#department_list").on("click", ".dpt_trash", function(){
        var id = $(this).attr("data-id");
        pushDepartmentDel({"id": id});
    });
    //部门修改
    $("#department_list").on("click", ".dpt_wrench", function(){
        $("#dpt_content").hide();
        var dpt = $(this).attr("data-data");
        dpt = JSON.parse(dpt);
        $("input[name='dpt_name']").attr("data-id", dpt.id);
        $("input[name='dpt_name']").val(dpt.depName);
        $("#dpt").show();
    });
    //部门默认数据加载
    pullDepartmentList();
}
/**
 * 部门列表
 */
function pullDepartmentList(){
    parent.requestData(URLS.root + URLS.manager.department.list, "get", {}, function(result){
        var data = result.data; 
        $("#department_list").empty();
        var trs = "";
        for(var i in data){
            trs += '<tr>' +
                    '<td>'+ (i*1 + 1) +'</td>' +
                    '<td>'+ data[i].depName +'</td>' +
                    ' <td>'+ data[i].ctm +'</td>' +
                    '<td>' +
                    '   <button  class="btn btn-danger btn-circle dpt_trash" type="button" data-id="'+ data[i].id +'"><i class="fa fa-trash"></i></button>' +
                    '    <button class="btn btn-primary btn-circle dpt_wrench" type="button" data-id="'+ data[i].id +'" data-data=\''+ JSON.stringify(data[i]) +'\'><i class="fa fa-wrench"></i></button>' +
                    '</td>'+
                    '</tr>';
        }
        $("#department_list").append(trs);
        DEP = data; 
    });
}
/**
 * 部门增加
 */
function pushDepartmentAdd(param){
    parent.requestData(URLS.root + URLS.manager.department.add, "post", param, function(result){
        parent.toast("info", "insert data success!", "tip");
        pullDepartmentList();
    })
}
/**
 * 部门删除
 */
function pushDepartmentDel(param){
    parent.requestData(URLS.root + URLS.manager.department.del, "get", param, function(result){
        parent.toast("info", "delete data success!", "tip");
        pullDepartmentList();
    })
}
/**
 * 部门修改
 */
function pushDepartmentModify(param){
    parent.requestData(URLS.root + URLS.manager.department.modify, "post", param, function(result){
        parent.toast("info", "modify data success!", "tip");
        pullDepartmentList();
    })
}
//用户相关
var UPage = {
    "size": 10,
    "pages": 0,
    "page": 0
};
function initUser(){
     //用户-增加
    $("#user_add").click(function(){
        $("#user_content").hide();
        pullDepartmentOptionList("user_dpt", function(data){
            $("#user_dpt").trigger("chosen:updated");
            DEP = data;
        });
        $("#user_name").attr("data-id", "");
        $("#user_acc").val("");
        $("#user_pwd").val("");
        $("#user_persid").val("");
        $("#user_age").val(15);
        $("#user_email").val("");
        $("#user_job").val("");
        $("#user_office").val("");
        $("#user_home").val("");
        $("#user_code").val("");
        $("#user").show();
    });
    //用户-保存
    $("#user_save").click(function(){
        var user = {};
        user.id = $("#user_name").attr("data-id");
        user.name = $("#user_name").val();
        user.user_account = $("#user_acc").val();
        user.user_password  = $("#user_pwd").val();
        user.category = $("#user_cate").val();
        user.personal_id = $("#user_persid").val();
        user.age = $("#user_age").val();
        user.gender = $("input[name='userGender']:checked").val();
        user.email_address = $("#user_email").val();
        user.home_address = $("#user_home").val();
        user.office_location = $("#user_office").val();
        user.job_title = $("#user_job").val();
        user.dep_id = JSON.stringify($("#user_dpt").val());
        user.postal_code = $("#user_code").val();
        user.ctm;
        if(user.id){
            pushUserModify(user);
        }else{
            pushUserAdd(user);
        }
    });
    //用户-取消
    $("#user_cancel").click(function(){
        $("#user").hide()
        $("#user_name").attr("data-id", "");
        $("#user_name").val("");
        $("#user_acc").val("");
        $("#user_pwd").val("");
        $("#user_persid").val("");
        $("#user_age").val(15);
        $("#user_email").val("");
        $("#user_home").val("");
        $("#user_office").val("");
        $("#user_job").val("");
        $("#user_code").val("");
        $("#user_content").show();
    });
    //用户-删除
    $("#user_list").on("click", ".user_trash", function(){
        pushUserDel({"u_id": $(this).attr("data-id")});
    });
    //用户-修改
    $("#user_list").on("click", ".user_wrench", function(){
        $("#user_content").hide();
        var data = $(this).attr("data-data");
        data = JSON.parse(data);
        pullDepartmentOptionList("user_dpt", function(result){
            $("#user_dpt").val(JSON.parse(data.dep_id));
            $("#user_dpt").trigger("chosen:updated");
            DEP = data;
        });
        $("#user_name").attr("data-id", data.id);
        $("#user_name").val(data.name);
        $("#user_acc").val(data.user_account);
        $("#user_pwd").val(data.user_password);
        $("#user_cate").val(data.category);
        $("#user_persid").val(data.personal_id);
        $("#user_age").val(data.age);
        if(data.gender == "male"){
            $("#user_male").attr("checked", "checked");
        }else{
            $("#user_female").attr("checked", "checked");
        }
        $("#user_email").val(data.email_address);
        $("#user_home").val(data.home_address);
        $("#user_office").val(data.office_location);
        $("#user_job").val(data.job_title);
        $("#user_code").val(data.postal_code);
        $("#user").show();
    });
    //用户上一页
    $("#user_pre_page").click(function(){
        if(UPage.page - 1 >=0){
            UPage.page -= 1;
            pullUserList({"size": UPage.size, "page": UPage.page, "search_str": $("#user_sech_str").val(), "c_id": $("#user_type").val()});
        }else{
            parent.toast("info", "No more pages!", "tip")
        }
    });
    //用户下一页
    $("#user_next_page").click(function(){
        if(UPage.page + 1 <= UPage.pages - 1){
            UPage.page += 1;
            pullUserList({"size": UPage.size, "page": UPage.page, "search_str": $("#user_sech_str").val(), "c_id": $("#user_type").val()});
        }else{
            parent.toast("info", "No more pages!", "tip")
        }
    }); 
    //用户-搜索
    $("#user_search").click(function(){
        var c_id = $("#user_type").val();
        var sch_str = $("#user_sech_str").val() || "";
        if(c_id){
            pullUserList({"size": UPage.size, "page": UPage.page, "search_str": sch_str, "c_id":c_id});
        }else{
            parent.toast("warning", "c_id is empty", "tip");
        }
    });
    pullDepartmentOptionList(null, function(data){
        DEP = data;
        pullUserList({"size": UPage.size, "page": 0, "search_str": "", "c_id": 3});
    });
    
}
/**
 * 用户数据列表
 */
function pullUserList(param){
    parent.requestData(URLS.root + URLS.manager.user.list, "get", param, function(result){
        var data = result.data;
        $("#user_cur").text(result.curPage + 1);
        $("#user_tol").text(result.pages); 
        $("#user_list").empty();
        var trs = "";
        for(var i in data){
            var dps = matchDepNames(DEP, JSON.parse(data[i].dep_id));
            var dpdesc = dps.length > 10 ? dps.substring(0, 10)+"..." : dps;
            trs +=   '<tr>'+
                        '<td>'+ data[i].id +'</td>'+
                        '<td>'+ data[i].name +'</td>'+
                        '<td title="'+ dps +'">'+ dpdesc +'</td>'+
                        '<td>'+ data[i].personal_id +'</td>'+
                        '<td>'+ data[i].age +'</td>'+
                        '<td>'+ data[i].gender +'</td>'+
                        '<td>'+ data[i].email_address +'</td>'+
                        '<td>'+ data[i].home_address +' </td>'+
                        '<td>'+ data[i].office_location +'</td>'+
                        '<td>'+ data[i].job_title +'</td>'+
                        '<td>'+ data[i].postal_code +'</td>'+
                        '<td>'+ data[i].ctm.split("T")[0] +'</td>'+
                        '<td>'+
                        '   <button  class="btn btn-danger btn-circle user_trash" type="button" data-id="'+ data[i].id +'"><i class="fa fa-trash"></i></button>' +
                        '    <button class="btn btn-primary btn-circle user_wrench" type="button" data-id="'+ data[i].id +'" data-data=\''+ JSON.stringify(data[i]) +'\'><i class="fa fa-wrench"></i></button>' +
                        '</td>'+
                    '</tr>'
        }
        $("#user_list").append(trs); 
    });
}
/**
 * 增加用户
 */
function pushUserAdd(param){
    parent.requestData(URLS.root + URLS.manager.user.add, "post", param, function(result){
        parent.toast("info", "insert data success!", "tip");
        pullUserList({"size": UPage.size, "page": 0, "search_str": $("#user_sech_str").val(), "c_id": $("#user_type").val()});
    });
}
/**
 * 修改用户
 */
function pushUserModify(param){
    parent.requestData(URLS.root + URLS.manager.user.modify, "post", param, function(result){
        parent.toast("info", "modify data success!", "tip");
        pullUserList({"size": UPage.size, "page": 0, "search_str": $("#user_sech_str").val(), "c_id": $("#user_type").val()});
    });
}
/**
 * 删除用户
 */
function pushUserDel(param){
    parent.requestData(URLS.root + URLS.manager.user.del, "get", param, function(result){
        parent.toast("info", "delete data success!", "tip");
        pullUserList({"size": UPage.size, "page": 0, "search_str": $("#user_sech_str").val(), "c_id": $("#user_type").val()});
    });
}

//课程相关
function initCourse(){
    //课程增加
    $("#cou_add").click(function(){
        $("#cou_content").hide();
        pullDepartmentOptionList("cou_sc_dpt", function(data){
            DEP = data;
        });
        $("#cou_sc_dpt").attr("data-id", '');
        $("#cou_name").val('');
        $("#cou").show();
    });
    //课程保存
    $("#cou_save").click(function(){
        var cou = {};
        cou.id = $("#cou_sc_dpt").attr("data-id");
        cou.dep_id = $("#cou_sc_dpt").val();
        cou.name =  $("#cou_name").val();
        cou.score = $("#cou_score").val();
        if(cou.id){
            pushCourseModify(cou);
        }else{
            pushCourseAdd(cou);
        }
    });
    //课程取消
    $("#cou_cancel").click(function(){
        $("#cou").hide();
        $("#cou_sc_dpt").attr("data-id", '');
        $("#cou_name").val('');
        $("#cou_content").show();
    });
    //课程删除
    $("#course_list").on("click", ".cou_trash", function(){
        var id = $(this).attr("data-id");
        pushCourseDel({dep_id: id})
        
    });
    //课程修改
    $("#course_list").on("click", ".cou_wrench", function(){
        $("#cou_content").hide();
        var cou = $(this).attr("data-data");
        cou = JSON.parse(cou);
        pullDepartmentOptionList("cou_sc_dpt", function(data){
            $("#cou_sc_dpt").val(cou.depId);
            DEP = data;
        });
        $("#cou_sc_dpt").attr("data-id", cou.id);
        $("#cou_name").val(cou.name);
        $("#cou").show();
    });
    //课程搜索
    $("#cou_search").click(function(){
        pullCourseList({dep_id: $("#cou_sech_dpt").val(), sch_str: $("#cou_sech_str").val()});
    });
    //点击选项卡
    $(".m_tb_sou").click(function(){
        pullDepartmentOptionList("cou_sech_dpt", function(data){
            DEP = data;
        });
    });
    //课程默认数据加载
    pullDepartmentOptionList("cou_sech_dpt", function(data){
        DEP = data;
        pullCourseList({dep_id: $("#cou_sech_dpt").val(), sch_str: $("#cou_sech_str").val()});
    })
}
/**
 * 课程列表
 */
function pullCourseList(param){
    parent.requestData(URLS.root + URLS.manager.course.list, "get", param, function(result){
        var data = result.data; 
        $("#course_list").empty();
        var trs = "";
        for(var i in data){
            trs += '<tr>' +
                    '<td>'+ (i*1 + 1) +'</td>' +
                    // '<td>'+ matchingDepName(DEP, data[i].depId) +'</td>' +
                    ' <td>'+ data[i].name +'</td>' +
                    ' <td>'+ data[i].score +'</td>' +
                    ' <td>'+ data[i].ctm +'</td>' +
                    '<td>' +
                    '   <button  class="btn btn-danger btn-circle cou_trash" type="button" data-id="'+ data[i].id +'"><i class="fa fa-trash"></i></button>' +
                    '    <button class="btn btn-primary btn-circle cou_wrench" type="button" data-id="'+ data[i].id +'" data-data=\''+ JSON.stringify(data[i]) +'\'><i class="fa fa-wrench"></i></button>' +
                    '</td>'+
                    '</tr>';
        }
        $("#course_list").append(trs); 
    });
}
/**
 * 课程增加
 */
function pushCourseAdd(param){
    parent.requestData(URLS.root + URLS.manager.course.add, "post", param, function(result){
        parent.toast("info", "insert data success!", "tip");
        pullCourseList({dep_id: $("#cou_sech_dpt").val(), sch_str: $("#cou_sech_str").val()});
    })
}
/**
 * 课程删除
 */
function pushCourseDel(param){
    parent.requestData(URLS.root + URLS.manager.course.del, "get", param, function(result){
        parent.toast("info", "delete data success!", "tip");
        pullCourseList({dep_id: $("#cou_sech_dpt").val(), sch_str: $("#cou_sech_str").val()});
    })
}
/**
 * 课程修改
 */
function pushCourseModify(param){
    parent.requestData(URLS.root + URLS.manager.course.modify, "post", param, function(result){
        parent.toast("info", "modify data success!", "tip");
        pullCourseList({dep_id: $("#cou_sech_dpt").val(), sch_str: $("#cou_sech_str").val()});
    })
}
var TCH = {};
//课程-节
function initSecction(){
     //课程-节-增加
    $("#sec_add").click(function(){
        //部门数据默认加载
        $("#sec_content").hide();
        pullDepartmentOptionList("sec_sc_dpt", function(data){
            pullCourseOptionList({"dep_id": data[0].id, "sch_str": ""}, "sec_sc_cou", function(){});
        });
        pullTecherOptionList("sec_instructor", function(data){
            TCH = data;
            $("#sec_instructor").trigger("chosen:updated");
        });
        $("#sec_name").attr("data-id", "");
        $("#sec_name").val("");
        $("#sec_number").val(1);
        $("#sec_days").val("");
        $("#sec_meeting").val("");
        $("#sec_room").val("");
        $("#sec").show();
    });
    //课程-节-保存
    $("#sec_save").click(function(){
        var sec = {};
        sec.id = $("#sec_name").attr("data-id");
        sec.course_id = $("#sec_sc_cou").val();
        sec.section_name = $("#sec_name").val();
        sec.instructors = JSON.stringify($("#sec_instructor").val());
        sec.number = $("#sec_number").val();
        sec.days_times = $("#sec_days").val();
        sec.meeting_dates = $("#sec_meeting").val();
        sec.room = $("#sec_room").val();
        if(sec.id){
            pushSecctionModify(sec);
        }else{
            pushSecctionAdd(sec);
        }
    });
    //课程-节-取消
    $("#sec_cancel").click(function(){
        $("#sec").hide();
        $("#sec_name").val("");
        $("#sec_name").attr("data-id", "");
        $("#sec_number").val(1);
        $("#sec_days").val("");
        $("#sec_meeting").val("");
        $("#sec_room").val("");
        $("#sec_content").show();
    });
    //课程-节-删除
    $("#secction_list").on("click", ".sec_trash", function(){
        pushSecctionDel({"sec_id": $(this).attr("data-id")})
    });
    //课程-节-修改
    $("#secction_list").on("click", ".sec_wrench", function(){
        $("#sec_content").hide();
        var sec = $(this).attr("data-data");
        sec = JSON.parse(sec);
        pullDepartmentOptionList("sec_sc_dpt", function(data){
            pullCourseOptionList({"dep_id": data[0].id, "sch_str": ""}, "sec_sc_cou", function(){});
        });
        pullTecherOptionList("sec_instructor", function(data){
            $("#sec_instructor").val(JSON.parse(sec.teachersId));
            $("#sec_instructor").trigger("chosen:updated");
            TCH = data;
        });
        $("#sec_name").attr("data-id", sec.id);
        $("#sec_name").val(sec.sectionName);
        $("#sec_number").val(sec.number);
        $("#sec_days").val(sec.days_time);
        $("#sec_meeting").val(sec.meeting_dates);
        $("#sec_room").val(sec.room);
        $("#sec").show();
    });
    //部门切换事件
    $("#sec_sc_dpt").change(function(){
        pullCourseOptionList({"dep_id": $(this).val(), "sch_str": ""}, "sec_sc_cou", function(){});
    });
    $("#sec_sch_dpt").change(function(){
        pullCourseOptionList({"dep_id": $(this).val(), "sch_str": ""}, "sec_sch_cou", function(){});
    });
    //课程-节-搜索
    $("#sec_search").click(function(){
        var cou_id = $("#sec_sch_cou").val();
        var sch_str = $("#sec_sch_str").val() || "";
        if(cou_id){
           pullSecctionList({"cou_id": cou_id, "sch_str": sch_str});
        }else{
           parent.toast("warning", "course id not empty", "tip");
        }
    });
    //点击选项卡
    $(".m_tb_sec").click(function(){
        pullDepartmentOptionList("sec_sch_dpt", function(data){
            DEP = data;
            pullCourseOptionList({"dep_id": data[0].id, "sch_str": ""}, "sec_sch_cou", function(){
                pullTecherOptionList(null, function(data2){
                    TCH = data2;
                });
            });
        });
    });
    //部门数据默认加载
    pullDepartmentOptionList("sec_sch_dpt", function(data){
        DEP = data;
        pullCourseOptionList({"dep_id": data[0].id, "sch_str": ""}, "sec_sch_cou", function(course_data){
            pullTecherOptionList(null, function(tch_data){
                TCH = tch_data;
                pullSecctionList({"cou_id": course_data[0].id, "sch_str": ""});
            });
        });
    });
 
}
/**
 * secction列表
 */
function pullSecctionList(param){
    parent.requestData(URLS.root + URLS.manager.secction.list, "get", param, function(result){
        var data = result.data; 
        $("#secction_list").empty();
        var trs = "";
        for(var i in data){
            var tchs = matchTeacherNames(TCH, JSON.parse(data[i].teachersId));
            var tchdesc = tchs.length > 10 ? tchs.substring(0, 10)+"..." : tchs;
            trs += '<tr>'+
                    '<td>'+ data[i].id +'</td>'+
                    '<td>'+ data[i].sectionName +'</td>'+
                    '<td title="'+ tchdesc +'">'+tchs +'</td>'+
                    '<td>'+ data[i].days_time +'</td>'+
                    '<td>'+ data[i].meeting_dates +'</td>'+
                    '<td>'+ data[i].room +'</td>'+
                    '<td>'+ data[i].number +'</td>'+
                    '<td>'+ data[i].ctm.split(" ")[0] +'</td>'+
                    '<td>'+
                    '   <button  class="btn btn-danger btn-circle sec_trash" type="button" data-id="'+ data[i].id +'"><i class="fa fa-trash"></i></button>' +
                    '    <button class="btn btn-primary btn-circle sec_wrench" type="button" data-id="'+ data[i].id +'" data-data=\''+ JSON.stringify(data[i]) +'\'><i class="fa fa-wrench"></i></button>' +
                    '</td>'+
                    '</tr>';
        }
        $("#secction_list").append(trs); 
    });
}
/**
 * 增加secction
 */
function pushSecctionAdd(param){
    parent.requestData(URLS.root + URLS.manager.secction.add, "post", param, function(result){
        parent.toast("info", "insert data success!", "tip");
        pullSecctionList({"cou_id": $("#sec_sch_cou").val(), "sch_str": ""});
    });
}
/**
 * 删除secction
 */
function pushSecctionDel(param){
    parent.requestData(URLS.root + URLS.manager.secction.del, "get", param, function(result){
        parent.toast("info", "delete data success!", "tip");
        pullSecctionList({"cou_id": $("#sec_sch_cou").val(), "sch_str": ""});
    });
}
/**
 * 修改secction信息
 */
function pushSecctionModify(param){
    parent.requestData(URLS.root + URLS.manager.course.modify, "post", param, function(result){
        parent.toast("info", "modify data success!", "tip");
        pullSecctionList({"cou_id": $("#sec_sch_cou").val(), "sch_str": ""});
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