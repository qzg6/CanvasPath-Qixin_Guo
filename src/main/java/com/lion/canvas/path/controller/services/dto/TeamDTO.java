package com.lion.canvas.path.controller.services.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author
 * @date 2019/4/15 14:37
 */
@Data
public class TeamDTO {
    @NotBlank
    private String title;
    @NotNull
    private Long courseSectionEntryId;
    private String sponsor_info;
    @NotBlank
    private String studentsId;
}
