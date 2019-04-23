package com.lion.canvas.path.controller.services.dto;

import lombok.Data;

import javax.validation.constraints.Min;

/**
 * @author
 * @date 2019/3/14
 * 分页参数
 */
@Data
public class PageHelperDTO {
    @Min(value = 1)
    private Integer size;
    @Min(value = 0)
    private Integer page;
    private String search_str;
}
