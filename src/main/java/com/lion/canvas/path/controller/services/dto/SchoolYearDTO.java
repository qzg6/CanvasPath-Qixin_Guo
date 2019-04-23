package com.lion.canvas.path.controller.services.dto;

import com.lion.canvas.path.controller.services.valide.DateCheck;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author
 * @date 2019/3/14
 */
@Data
public class SchoolYearDTO {

    private Integer id;
    @NotBlank
    private String tag;
    @DateCheck(message = "sdt date error")
    private String sdt;
    @DateCheck(message = "edt date error")
    private String edt;

}
