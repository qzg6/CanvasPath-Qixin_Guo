package com.lion.canvas.path.controller.services.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author
 * @date 2019/4/15 11:49
 */
@Data
public class EntryDto {
    @NotBlank
    private String title;
    @NotBlank
    private String category;
    @NotNull
    private Long courseId;
    @NotNull
    private Long courseSectionId;
}
