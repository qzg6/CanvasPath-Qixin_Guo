package com.lion.canvas.path.controller.services;

import com.lion.canvas.path.common.ResultBean;
import com.lion.canvas.path.controller.services.dto.EntryDto;
import com.lion.canvas.path.controller.services.dto.TeamDTO;
import com.lion.canvas.path.entity.CourseSection;
import com.lion.canvas.path.entity.CourseSectionEntry;
import com.lion.canvas.path.entity.CourseSectionEntryScore;
import com.lion.canvas.path.repository.CourseSectionEntryRepository;
import com.lion.canvas.path.repository.CourseSectionEntryScoreRepository;
import com.lion.canvas.path.repository.CourseSectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author
 * @date 2019/4/15 11:47
 */
@RestController
@CrossOrigin
@RequestMapping(path = "/api")
public class SectionEntryCtrl {
    @Autowired
    private CourseSectionEntryRepository entryRep;
    @Autowired
    private CourseSectionEntryScoreRepository entryScoreRep;
    @Autowired
    private CourseSectionRepository courseSectionRep;

    @RequestMapping(path = "/entry/team/list", params = {"e_id"})
    public ResultBean pullEntryTeamList(Long e_id){
        return ResultBean.success(entryScoreRep.findAllByCourseSectionEntryId(e_id));
    }

    @RequestMapping(path = "/entry/add")
    public ResultBean pushEntryAdd(@Valid EntryDto entryDto){
        CourseSectionEntry sectionEntry = new CourseSectionEntry();
        sectionEntry.setCourseId(entryDto.getCourseId());
        sectionEntry.setCourseSectionId(entryDto.getCourseSectionId());
        sectionEntry.setCtm(LocalDateTime.now());
        sectionEntry.setTitle(entryDto.getTitle());
        sectionEntry.setCategory(entryDto.getCategory());
        sectionEntry = entryRep.save(sectionEntry);
        if (!"capstone".equalsIgnoreCase(sectionEntry.getCategory())){
            //查找选择的人员
           Optional<CourseSection> sectionOpt = courseSectionRep.findById(sectionEntry.getCourseSectionId());
           if (sectionOpt.isPresent()){
               CourseSection section = sectionOpt.get();
               String stuIds = section.getStudentsId();
               if (stuIds != null){
                   List<String> ids = Stream.of(stuIds.split(","))
                               .filter(e -> e != null && !e.equals(""))
                               .collect(Collectors.toList());
                   List<CourseSectionEntryScore> csesList= new ArrayList<>();
                   for (String id : ids){
                       CourseSectionEntryScore cses = new CourseSectionEntryScore();
                       cses.setCourseSectionEntryId(sectionEntry.getId());
                       cses.setTitle("");
                       cses.setSponsor_info("");
                       cses.setStudentsId(id);
                       cses.setCtm(LocalDateTime.now());
                       cses.setScore(0);
                       csesList.add(cses);
                   }
                   entryScoreRep.saveAll(csesList);
               }
           }
        }
        return ResultBean.success(sectionEntry);
    }

    @RequestMapping(path = "/entry/del", params = {"e_id"})
    public ResultBean pushEntryDel(long e_id){
        entryRep.deleteById(e_id);
        entryScoreRep.deleteAllByCourseSectionEntryId(e_id);
        return ResultBean.success(e_id);
    }

    @RequestMapping(path = "/entry/team/add")
    public ResultBean pushEntryTeamAdd(@Valid TeamDTO teamDTO){
        CourseSectionEntryScore cses = new CourseSectionEntryScore();
        cses.setCtm(LocalDateTime.now());
        cses.setCourseSectionEntryId(teamDTO.getCourseSectionEntryId());
        cses.setTitle(teamDTO.getTitle());
        cses.setSponsor_info(teamDTO.getSponsor_info());
        cses.setStudentsId(teamDTO.getStudentsId());
        cses.setScore(0);
        cses = entryScoreRep.save(cses);
        return ResultBean.success(cses);
    }

    @RequestMapping(path = "/entry/team/del", params = {"t_id"})
    public ResultBean pushEntryTeamDel(long t_id){
        entryScoreRep.deleteById(t_id);
        return ResultBean.success(t_id);
    }

    @RequestMapping(path = "/entry/team/mod", params = {"t_id", "score"})
    public ResultBean pushEntryTeamScore(long t_id, Double score){
        Optional<CourseSectionEntryScore> csesOpt = entryScoreRep.findById(t_id);
        if (csesOpt.isPresent()){
            CourseSectionEntryScore cses = csesOpt.get();
            cses.setScore(score);
            entryScoreRep.save(cses);
        }
        return ResultBean.success(t_id);
    }

}
