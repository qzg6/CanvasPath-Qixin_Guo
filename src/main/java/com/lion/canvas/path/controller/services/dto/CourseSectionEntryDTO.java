package com.lion.canvas.path.controller.services.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author
 * @date 2019/4/18 10:52
 */
@Data
public class CourseSectionEntryDTO {
    private long id;
    private String title;
    private long courseId;
    private long courseSectionId;
    private String category;
    private Double avg;
    private Double max;
    private Double min;
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss" )
    private LocalDateTime ctm;
}
