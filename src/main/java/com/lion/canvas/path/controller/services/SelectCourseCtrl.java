package com.lion.canvas.path.controller.services;

import com.lion.canvas.path.common.ResultBean;
import com.lion.canvas.path.controller.services.dto.SlcSecDTO;
import com.lion.canvas.path.entity.Course;
import com.lion.canvas.path.entity.CourseSection;
import com.lion.canvas.path.entity.StaffTable;
import com.lion.canvas.path.repository.CourseRepository;
import com.lion.canvas.path.repository.CourseSectionEntryRepository;
import com.lion.canvas.path.repository.CourseSectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NegativeOrZero;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author
 * @date 2019/3/29 17:38
 */
@RestController
@CrossOrigin
@RequestMapping(path = "/api/slc")
public class SelectCourseCtrl {
    @Autowired
    private CourseSectionRepository sectionRep;
    @Autowired
    private CourseRepository courseRep;

    @RequestMapping(path = "/sec/list", params = {"cou_id", "sch_str"})
    public ResultBean pullCourseSectionList(HttpSession session,  @Valid @NegativeOrZero Long cou_id, String sch_str){
        StaffTable user = (StaffTable)session.getAttribute("user");
        sch_str = sch_str == null ? "" : sch_str;
        List<CourseSection> resList = sectionRep.findAllByCourseIdAndSectionNameLike(cou_id, "%".concat(sch_str).concat("%"));
        List<SlcSecDTO> viewList = new ArrayList<>();
        resList.stream().forEach(e ->{
            SlcSecDTO ssd = new SlcSecDTO();
            ssd.setId(e.getId());
            ssd.setCourseId(e.getCourseId());
            ssd.setDays_time(e.getDays_time());
            ssd.setTeachers_id(e.getTeachersId());
            ssd.setMeeting_dates(e.getMeeting_dates());
            ssd.setRoom(e.getRoom());
            ssd.setSectionName(e.getSectionName());
            int selNum = 0;
            if (!StringUtils.isEmpty(e.getStudentsId())){
                selNum = e.getStudentsId().trim().split(",").length;
            }
            ssd.setNumber(selNum + "/" + e.getNumber());
            ssd.setStatus(selNum < e.getNumber() ? "open" : "close");
            String students = e.getStudentsId() == null ? "" : e.getStudentsId();
            if (students.contains(user.getId() + ",")){
                ssd.setSelect("selected");
            }
            //获取学分
            Optional<Course> courseOpt = courseRep.findById(e.getCourseId());
            if (courseOpt.isPresent()){
                ssd.setScore(courseOpt.get().getScore());
            }
            viewList.add(ssd);
        });
        return ResultBean.success(viewList);
    }

    @RequestMapping(path = "/select", params = {"sec_id"})
    public ResultBean pushCourseSectionSelect(HttpSession session, Long sec_id){
        StaffTable user = (StaffTable)session.getAttribute("user");
        Optional<CourseSection> sectionOpt = sectionRep.findById(sec_id);
        if (sectionOpt.isPresent()){
            CourseSection section = sectionOpt.get();
            //判断当前课程是否选满了
            boolean flag = section.getStudentsId()== null || section.getStudentsId().equals("") ?  false : true;
            long size =  flag ? Stream.of(section.getStudentsId().split(",")).filter(e-> e!=null && !e.equals("")).count() : 0;
            if (size < section.getNumber()){
                List<CourseSection> sectionList = sectionRep.findAllBySectionNameLikeAndStudentsIdLike("%%", "%"+ user.getId()+",%");
                List<Long> idList = sectionList.stream().map(CourseSection::getCourseId).collect(Collectors.toList());
                List<Course> courseList = courseRep.findAllById(idList);
                //计算总学分
                Integer sumScore = courseList.stream().collect(Collectors.summingInt(Course::getScore));
                Optional<Course> courseOpt = courseRep.findById(section.getCourseId());
                if (courseOpt.isPresent()){
                    //判断是否超过19个学分
                    if (sumScore + courseOpt.get().getScore() <= 19){
                        String ids = section.getStudentsId() == null ? "" : section.getStudentsId();
                        if (ids != null && ids.contains(user.getId() + ",")){
                            ids = ids.replace(user.getId() + ",", "");
                        }else{
                            ids = ids.concat(user.getId() + ",");
                        }
                        section.setStudentsId(ids);
                        sectionRep.save(section);
                        return ResultBean.success(sec_id);
                    }
                }
            }
            return ResultBean.success(-1);
        }else{
            return ResultBean.error(new Exception("section not exist"));
        }
    }
}
