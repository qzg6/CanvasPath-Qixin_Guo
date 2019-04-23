package com.lion.canvas.path.controller.services.dto;

import lombok.Data;

import javax.validation.constraints.Min;

/**
 * @author
 * @date 2019/3/28 18:48
 */
@Data
public class UserPageDTO extends PageHelperDTO{
    @Min(1)
    private Integer c_id;
}
