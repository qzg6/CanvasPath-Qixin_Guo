package com.lion.canvas.path.controller.services.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author
 * @date 2019/3/28 17:36
 */
@Data
public class UserDTO {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String user_account;
    @NotBlank
    private String user_password;
    private Integer category;
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
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss" )
    private Date ctm;
}
