package com.lion.canvas.path.controller.services.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * @author
 * @date 2019/3/15
 */
@Data
public class CourseDTO{
    private Long id;
    @Min(1)
    private Integer dep_id;
    @NotBlank
    private String name;
    @Min(1)
    private Integer score;
}
