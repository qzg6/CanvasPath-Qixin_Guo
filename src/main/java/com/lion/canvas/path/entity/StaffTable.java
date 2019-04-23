package com.lion.canvas.path.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author
 * @date 2019/3/12
 */
@Entity
@Data
@Accessors(chain = true)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
@SequenceGenerator(name = "StaffTable", sequenceName = "StaffTable_seq")
public class StaffTable {
    /**
     * 人员标识
     */
    @Id
    @GeneratedValue(generator = "StaffTable", strategy = GenerationType.AUTO)
    private Long id;
    /**
     * 完整姓名
     */
    private String name;
    /**
     * 账号
     */
    @Column(name = "user_account")
    private String userAccount;
    /**
     * 密码
     */
    @Column(name = "user_password")
    private String userPassword;
    /**
     * 人员类别
     * 1管理员 2教师 3学生
     */
    private Integer category;
    /**
     * 创建时间
     */
    @Column(columnDefinition = "timestamp")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss" )
    private LocalDateTime ctm;

}
