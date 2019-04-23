package com.lion.canvas.path.controller.services.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author
 * @date 2019/4/18 11:12
 */
@Data
public class CourseSectionEntryScoreDTO {

    private long id;
    private String title;
    private long courseSectionEntryId;
    private String sponsor_info;
    private String studentsId;
    private double score;
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss" )
    private LocalDateTime ctm;

    private Double avg;
    private Double max;
    private Double min;
}
