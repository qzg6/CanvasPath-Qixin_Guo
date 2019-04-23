package com.lion.canvas.path.controller.services;

import com.lion.canvas.path.common.ResultBean;
import com.lion.canvas.path.controller.services.dto.CourseSectionDTO;
import com.lion.canvas.path.controller.services.dto.CourseSectionEntryDTO;
import com.lion.canvas.path.controller.services.dto.CourseSectionEntryScoreDTO;
import com.lion.canvas.path.entity.*;
import com.lion.canvas.path.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author
 * @date 2019/4/1 14:33
 */
@RestController
@CrossOrigin
@RequestMapping(path = "/api")
public class UserCourseCtrl {

    @Autowired
    private CourseSectionRepository secRep;
    @Autowired
    private CourseSectionEntryScoreRepository scoreRep;
    @Autowired
    private StaffTableInfoRepository staffInfoRep;
    @Autowired
    private CourseSectionEntryRepository cseRep;
    @Autowired
    private CourseRepository courseRep;
    /**
     * 学习课程列表
     * @param c_name
     * @return
     */
    @RequestMapping(path = "/mcou/list", params = {"c_name"})
    public ResultBean pullUserCourseList(HttpSession session, String c_name){
        StaffTable user = (StaffTable)session.getAttribute("user");
        c_name = c_name == null ? "" : c_name;
        List<CourseSection> csList = secRep.findAllBySectionNameLikeAndStudentsIdLike("%".concat(c_name).concat("%"), "%"+ user.getId() + ",%");
        return ResultBean.success(fetchCourseScore(csList));
    }

    @RequestMapping(path = "/mcou/project/list", params = {"c_id"})
    public ResultBean pullUserProjectList(HttpSession session, Long c_id){
        StaffTable user = (StaffTable)session.getAttribute("user");
        List<CourseSectionEntryScoreDTO> csesDtoList = new ArrayList<>();
        //查找得分项目
        List<CourseSectionEntryScore> csesList = scoreRep.findAllByCourseSectionEntryIdEqualsAndStudentsIdLike(c_id, "%" + user.getId() + ",%");
         for (CourseSectionEntryScore cses : csesList){
             //查找项目
             List<CourseSectionEntry> cseList = cseRep.findAllByCourseSectionId(cses.getCourseSectionEntryId());
             for (CourseSectionEntry cse : cseList){
                 CourseSectionEntryScoreDTO csesDto = new CourseSectionEntryScoreDTO();
                 //查找项目的得分人员
                 List<CourseSectionEntryScore> cses2List = scoreRep.findAllByCourseSectionEntryId(cse.getId());
                 //计算平均最大最小
                 Optional<CourseSectionEntryScore> maxVOpt = cses2List.stream().collect(Collectors.maxBy(Comparator.comparingDouble(CourseSectionEntryScore::getScore)));
                 Optional<CourseSectionEntryScore> minVOpt = cses2List.stream().collect(Collectors.minBy(Comparator.comparingDouble(CourseSectionEntryScore::getScore)));
                 Double avgV = cses2List.stream().collect(Collectors.averagingDouble(CourseSectionEntryScore::getScore));
                 csesDto.setAvg(avgV);
                 if (maxVOpt.isPresent()){
                     csesDto.setMax(maxVOpt.get().getScore());
                 }
                 if (minVOpt.isPresent()){
                     csesDto.setMin(minVOpt.get().getScore());
                 }
                 csesDto.setId(cses.getId());
                 csesDto.setCtm(cses.getCtm());
                 csesDto.setScore(cses.getScore());
                 csesDto.setTitle(cses.getTitle());
                 csesDto.setSponsor_info(cses.getSponsor_info());
                 csesDto.setStudentsId(cses.getStudentsId());
                 csesDto.setCourseSectionEntryId(cses.getCourseSectionEntryId());
                 csesDtoList.add(csesDto);
             }
         }
        return ResultBean.success(csesDtoList);
    }

    @RequestMapping(path = "/mcou/teacher/info", params = {"t_id"})
    public ResultBean pullTeacherInfo(String t_id){
        if (t_id != null){
             return ResultBean.success(staffInfoRep.findUserList(Arrays.asList(t_id.split(","))), true);
        }
        return ResultBean.error(new Exception("t_id not empty"));
    }

    /**
     * 授课列表
     * @param c_name
     * @return
     */
    @RequestMapping(path = "/mcou/teacher/list", params = {"c_name"})
    public ResultBean pullTeacherCourseList(HttpSession session, String c_name){
        StaffTable user = (StaffTable)session.getAttribute("user");
        c_name = c_name == null ? "" : c_name;
        List<CourseSection> csList = secRep.findAllBySectionNameLikeAndTeachersIdLike("%".concat(c_name).concat("%"), "%\""+ user.getId() + "\"%");
        return ResultBean.success(fetchCourseScore(csList));
    }

    @RequestMapping(path = "/mcou/student/list", params = {"s_id"})
    public ResultBean pullStudentList(@Valid @NotBlank String s_id){
        return ResultBean.success(
                staffInfoRep.findUserList(Arrays.asList(s_id.split(","))), true
        );
    }

    @RequestMapping(path = "/mcou/teacher/project/list", params = {"c_id"})
    public ResultBean pullTeacherProjectList(Long c_id){
        List<CourseSectionEntryDTO> result = new ArrayList<>();
        List<CourseSectionEntry> cseList = cseRep.findAllByCourseSectionId(c_id);
        for (CourseSectionEntry cse : cseList){
            CourseSectionEntryDTO cseDto = new CourseSectionEntryDTO();
            List<CourseSectionEntryScore> csesList = scoreRep.findAllByCourseSectionEntryId(cse.getId());
            Optional<CourseSectionEntryScore> maxVOpt = csesList.stream().collect(Collectors.maxBy(Comparator.comparingDouble(CourseSectionEntryScore::getScore)));
            Optional<CourseSectionEntryScore> minVOpt = csesList.stream().collect(Collectors.minBy(Comparator.comparingDouble(CourseSectionEntryScore::getScore)));
            Double avgV = csesList.stream().collect(Collectors.averagingDouble(CourseSectionEntryScore::getScore));
            cseDto.setAvg(avgV);
            if (maxVOpt.isPresent()){
                cseDto.setMax(maxVOpt.get().getScore());
            }
            if (minVOpt.isPresent()){
                cseDto.setMin(minVOpt.get().getScore());
            }
            cseDto.setCategory(cse.getCategory());
            cseDto.setCourseId(cse.getCourseId());
            cseDto.setCourseSectionId(cse.getCourseSectionId());
            cseDto.setId(cse.getId());
            cseDto.setTitle(cse.getTitle());
            cseDto.setCtm(cse.getCtm());
            result.add(cseDto);
        }
        return ResultBean.success(result);
    }

    private  List<CourseSectionDTO> fetchCourseScore(List<CourseSection> csList){
        List<CourseSectionDTO> csdTOList = new ArrayList<>();
        for (CourseSection cs : csList){
            CourseSectionDTO csDto = new CourseSectionDTO();
            csDto.setId(cs.getId());
            csDto.setCourseId(cs.getCourseId());
            csDto.setCtm(cs.getCtm());
            csDto.setDays_time(cs.getDays_time());
            csDto.setMeeting_dates(cs.getMeeting_dates());
            csDto.setNumber(cs.getNumber());
            csDto.setRoom(cs.getRoom());
            csDto.setSectionName(cs.getSectionName());
            csDto.setStudentsId(cs.getStudentsId());
            csDto.setTeachersId(cs.getTeachersId());
            //获取学分
            Optional<Course> courseOpt = courseRep.findById(cs.getCourseId());
            if (courseOpt.isPresent()){
                csDto.setScore(courseOpt.get().getScore());
            }
            csdTOList.add(csDto);
        }
        return csdTOList;
    }
}
