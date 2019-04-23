package com.lion.canvas.path.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * @author
 * @date 2019/3/12
 */
@Entity
@Data
@Accessors(chain = true)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
public class StaffTableInfo {
    /**
     * 标识
     */
    @Id
    private long id;
    /**
     * 身份证
     */
    private String personal_id;
    /**
     * 年龄
     */
    private int age;
    /**
     * 性别
     */
    private String gender;
    /**
     * 邮件地址
     */
    private String email_address;
    /**
     * 家庭住址
     */
    private String home_address;
    /**
     * 办公地址
     */
    private String office_location;
    /**
     * 职称
     */
    private String job_title;
    /**
     * 系
     */
    private String dep_id;
    /**
     * 邮政编码
     */
    private String postal_code;

}
