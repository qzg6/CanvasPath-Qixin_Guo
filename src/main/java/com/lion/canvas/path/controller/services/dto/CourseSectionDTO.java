package com.lion.canvas.path.controller.services.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author
 * @date 2019/4/18 10:52
 */
@Data
public class CourseSectionDTO {
    private long id;
    private String sectionName;
    private Long courseId;
    private String teachersId;
    private String studentsId;
    private int number;
    private String days_time;
    private String meeting_dates;
    private String room;
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss" )
    private LocalDateTime ctm;
    private int score;
}
