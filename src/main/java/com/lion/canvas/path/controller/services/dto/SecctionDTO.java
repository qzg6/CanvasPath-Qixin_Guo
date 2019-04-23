package com.lion.canvas.path.controller.services.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * @author
 * @date 2019/3/28 11:26
 */
@Data
public class SecctionDTO {
    @Min(value = 1)
    private Long id;
    @Min(value = 1)
    private long course_id;
    @NotBlank
    private String section_name;
    @NotBlank
    private String instructors;
    @Min(value = 1)
    private int number;
    @NotBlank
    private String days_times;
    @NotBlank
    private String meeting_dates;
    @NotBlank
    private String room;
}
