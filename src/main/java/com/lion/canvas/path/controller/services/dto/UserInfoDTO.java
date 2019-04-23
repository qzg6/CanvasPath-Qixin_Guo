package com.lion.canvas.path.controller.services.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * @author
 * @date 2019/4/1 11:51
 */
@Data
public class UserInfoDTO {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String user_password;
    @NotBlank
    private String personal_id;
    @Min(10)
    private Integer age;
    @NotBlank
    private String gender;
    @NotBlank
    private String email_address;
    @NotBlank
    private String home_address;
    private String office_location;
    private String job_title;
    @NotBlank
    private String dep_id;
    private String postal_code;
}
