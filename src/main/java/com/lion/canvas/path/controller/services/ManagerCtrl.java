package com.lion.canvas.path.controller.services;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lion.canvas.path.common.PageResultBean;
import com.lion.canvas.path.common.ResultBean;
import com.lion.canvas.path.controller.services.dto.*;
import com.lion.canvas.path.controller.services.dto.CourseDTO;
import com.lion.canvas.path.entity.*;
import com.lion.canvas.path.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NegativeOrZero;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


/**
 * @author
 * @date 2019/3/12
 * 管理类
 */
@RestController
@CrossOrigin
@RequestMapping(path = "/api/mng")
public class ManagerCtrl {
    @Autowired
    private SchoolYearRepository schoolYearRep;
    @Autowired
    private DepartmentRepository departmentRep;
    @Autowired
    private CourseRepository courseRep;
    @Autowired
    private CourseSectionRepository sectionRep;
    @Autowired
    private StaffTableRepository staffRep;
    @Autowired
    private StaffTableInfoRepository staffInfoRep;

    /**
     * 学年
     */
    @RequestMapping(path = "/schyer/list")
    public ResultBean pullSchoolYearList(@Valid PageHelperDTO phv){
        return PageResultBean.success(
                schoolYearRep.findAll(
                        PageRequest.of(phv.getPage(), phv.getSize(), Sort.by(Sort.Direction.DESC,"sdt"))
                )
        );
    }

    @RequestMapping(path = "/schyer/add")
    public ResultBean pushSchoolYearAdd(@Valid SchoolYearDTO dt){
        SchoolYear sy = new SchoolYear()
                .setCtm(LocalDateTime.now())
                .setEdt(LocalDate.parse(dt.getEdt()))
                .setSdt(LocalDate.parse(dt.getSdt()))
                .setTag(dt.getTag());
        sy = schoolYearRep.save(sy);
        return ResultBean.success(sy.getId());
    }

    @RequestMapping(path = "/schyer/modify")
    public ResultBean pushSchoolYearModify(@Valid SchoolYearDTO dt){
        Optional<SchoolYear> syOpt = schoolYearRep.findById(dt.getId());
        SchoolYear sy = null;
        if (syOpt.isPresent()){
            sy = syOpt.get();
            sy.setEdt(LocalDate.parse(dt.getEdt()))
                    .setSdt(LocalDate.parse(dt.getSdt()))
                    .setTag(dt.getTag());
            schoolYearRep.save(sy);
        }
        return ResultBean.success(sy);
    }

    @RequestMapping(path = "/schyer/del")
    public ResultBean pushSchoolYearDel(@Valid @NegativeOrZero Integer id){
        Optional<SchoolYear> syOpt = schoolYearRep.findById(id);
        SchoolYear sy = null;
        if (syOpt.isPresent()){
            schoolYearRep.deleteById(id);
            sy = syOpt.get();
        }
        return ResultBean.success(sy);
    }

    /**
     * 部门
     */
    @RequestMapping(path = "/dpt/list")
    public ResultBean pullDepartmentList(){
        return ResultBean.success(
                departmentRep.findAll(Sort.by(Sort.Direction.DESC, "ctm"))
        );
    }

    @RequestMapping(path = "/dpt/add", params = {"name"})
    public ResultBean pushDepartmentAdd(@Valid @NotBlank String name){
        Department dpt = new Department()
                .setDepName(name)
                .setCtm(LocalDateTime.now());
        dpt = departmentRep.save(dpt);
        return ResultBean.success(dpt);
    }

    @RequestMapping(path = "/dpt/del", params = {"id"})
    public ResultBean pushDepartmentDel(@Valid @NegativeOrZero Integer id){
        Optional<Department> syOpt = departmentRep.findById(id);
        Department dpt = null;
        if (syOpt.isPresent()){
            departmentRep.deleteById(id);
            dpt = syOpt.get();
        }
        return ResultBean.success(dpt);
    }

    @RequestMapping(path = "/dpt/modify", params = {"id", "name"})
    public ResultBean pushDepartmentModify(@Valid @NegativeOrZero Integer id, @Valid @NotBlank String name){
        Optional<Department> syOpt = departmentRep.findById(id);
        Department dpt = null;
        if (syOpt.isPresent()){
            dpt = syOpt.get();
            dpt.setDepName(name);
            departmentRep.save(dpt);
        }
        return ResultBean.success(dpt);
    }

    /**
     * 课程
     */
    @RequestMapping(path = "/cou/list", params = {"dep_id", "sch_str"})
    public ResultBean pullCourseList(@Valid @NegativeOrZero Integer dep_id, String sch_str){
        sch_str = sch_str == null ? "" : sch_str;
        return ResultBean.success(courseRep.findAllByDepIdAndNameLike(dep_id, "%".concat(sch_str).concat("%")));
    }

    @RequestMapping(path = "/cou/add")
    public ResultBean pushCourseAdd(@Valid CourseDTO dt){
        Course course = new Course()
                .setName(dt.getName())
                .setScore(dt.getScore())
                .setCtm(LocalDateTime.now())
                .setDepId(dt.getDep_id());
        course = courseRep.save(course);
        return ResultBean.success(course);
    }

    @RequestMapping(path = "/cou/del", params = {"dep_id"})
    public ResultBean pushCourseDel(@Valid @NegativeOrZero Long dep_id){
        Optional<Course> couOpt = courseRep.findById(dep_id);
        Course course = null;
        if (couOpt.isPresent()){
            course = couOpt.get();
            courseRep.deleteById(dep_id);
        }
        return ResultBean.success(course);
    }

    @RequestMapping(path = "/cou/modify")
    public ResultBean pushCourseModify(@Valid CourseDTO dt){
        Optional<Course> couOpt = courseRep.findById(dt.getId());
        Course course = null;
        if (couOpt.isPresent()){
            course = couOpt.get();
            course.setName(dt.getName())
                    .setDepId(dt.getDep_id())
                    .setScore(dt.getScore());
            courseRep.save(course);
        }
        return ResultBean.success(course);
    }

    /**
     * 用户
     */
    @RequestMapping(path = "/user/list")
    public ResultBean pullUserList(@Valid UserPageDTO dt) {
        Page<Map<String, Object>> pageList =  staffInfoRep.findUserInfo(
                dt.getC_id(), "%".concat(dt.getSearch_str()).concat("%"),
                PageRequest.of(dt.getPage(), dt.getSize()));
        return PageResultBean.success(pageList, true);
    }

    @RequestMapping(path = "/user/teacher/list")
    public ResultBean pullTeacherList(){
        //2教师
        List<StaffTable> staffTableList = staffRep.findAllByCategory(2);
        for (StaffTable staffTable : staffTableList){
            staffTable.setUserAccount(null);
            staffTable.setUserPassword(null);
            staffTable.setCategory(null);
            staffTable.setCtm(null);
        }
        return ResultBean.success(staffTableList);
    }

    @RequestMapping(path = "/user/add")
    public ResultBean pushUserAdd(@Valid UserDTO dt){
        StaffTable staffTable = new StaffTable()
                .setCtm(LocalDateTime.now())
                .setUserAccount(dt.getUser_account())
                .setUserPassword(dt.getUser_password())
                .setName(dt.getName())
                .setCategory(dt.getCategory());
        staffTable = staffRep.save(staffTable);
        if (staffTable.getId() != null){
            StaffTableInfo staffTableInfo = new StaffTableInfo()
                    .setAge(dt.getAge())
                    .setDep_id(dt.getDep_id())
                    .setEmail_address(dt.getEmail_address())
                    .setGender(dt.getGender())
                    .setHome_address(dt.getHome_address())
                    .setOffice_location(dt.getOffice_location())
                    .setJob_title(dt.getJob_title())
                    .setPersonal_id(dt.getPersonal_id())
                    .setPostal_code(dt.getPostal_code())
                    .setId(staffTable.getId());
            staffTableInfo = staffInfoRep.save(staffTableInfo);
            return ResultBean.success(staffTableInfo.getId());
        }else{
            return ResultBean.error(new Exception("add failed"));
        }
    }

    @RequestMapping(path = "/user/del")
    public ResultBean pushUserDel(@Valid @NegativeOrZero Long u_id){
        Optional<StaffTable> staffTable = staffRep.findById(u_id);
        if (staffTable.isPresent()){
            staffRep.deleteById(u_id);
            staffInfoRep.deleteById(u_id);
        }
        return ResultBean.success(u_id);
    }
    @RequestMapping(path = "/user/modify")
    public ResultBean pushUserModify(@Valid UserDTO dt){
        if (dt.getId() != null){
            Optional<StaffTable> staffTable = staffRep.findById(dt.getId());
            if (staffTable.isPresent()){
                StaffTable stf = staffTable.get();
                stf.setCategory(dt.getCategory());
                stf.setUserPassword(dt.getUser_password());
                stf.setUserAccount(dt.getUser_account());
                stf.setName(dt.getName());
                staffRep.save(stf);
                StaffTableInfo staffTableInfo = new StaffTableInfo()
                        .setAge(dt.getAge())
                        .setDep_id(dt.getDep_id())
                        .setEmail_address(dt.getEmail_address())
                        .setGender(dt.getGender())
                        .setHome_address(dt.getHome_address())
                        .setOffice_location(dt.getOffice_location())
                        .setJob_title(dt.getJob_title())
                        .setPersonal_id(dt.getPersonal_id())
                        .setPostal_code(dt.getPostal_code())
                        .setId(dt.getId());
                staffInfoRep.save(staffTableInfo);
                return ResultBean.success(dt.getId());
            }
        }
        return ResultBean.error(new Exception("id not exist"));
    }

    /**
     * 节
     */
    @RequestMapping(path = "/sec/list", params = {"cou_id", "sch_str"})
    public ResultBean pullCourseSectionList(@Valid @NegativeOrZero Long cou_id, String sch_str){
        sch_str = sch_str == null ? "" : sch_str;
        return ResultBean.success(sectionRep.findAllByCourseIdAndSectionNameLike(cou_id, "%".concat(sch_str).concat("%")));
    }

    @RequestMapping(path = "/sec/add")
    public ResultBean pushCourseSectionAdd(@Valid SecctionDTO dt){
        CourseSection cs = new CourseSection()
                .setCourseId(dt.getCourse_id())
                .setSectionName(dt.getSection_name())
                .setTeachersId(dt.getInstructors())
                .setNumber(dt.getNumber())
                .setDays_time(dt.getDays_times())
                .setMeeting_dates(dt.getMeeting_dates())
                .setRoom(dt.getRoom())
                .setCtm(LocalDateTime.now());
        cs = sectionRep.save(cs);
        return ResultBean.success(cs.getId());
    }

    @RequestMapping(path = "/sec/modify")
    public ResultBean pushCourseSectionModify(@Valid SecctionDTO dt){
        CourseSection cs = new CourseSection()
                .setId(dt.getId())
                .setCourseId(dt.getCourse_id())
                .setSectionName(dt.getSection_name())
                .setTeachersId(dt.getInstructors())
                .setNumber(dt.getNumber())
                .setDays_time(dt.getDays_times())
                .setMeeting_dates(dt.getMeeting_dates())
                .setRoom(dt.getRoom())
                .setCtm(LocalDateTime.now());
        cs = sectionRep.save(cs);
        return ResultBean.success(cs);
    }

    @RequestMapping(path = "/sec/del", params = {"sec_id"})
    public ResultBean pushCourseSectionDel(@Valid @NegativeOrZero Long sec_id){
        Optional<CourseSection> couOpt = sectionRep.findById(sec_id);
        CourseSection section = null;
        if (couOpt.isPresent()){
            section = couOpt.get();
            sectionRep.deleteById(sec_id);
        }
        return ResultBean.success(section);
    }
}
