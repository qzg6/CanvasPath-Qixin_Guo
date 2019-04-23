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
 * 课程表
 */
@Entity
@Data
@Accessors(chain = true)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
@SequenceGenerator(name = "Course", sequenceName = "Course_seq")
public class Course {
    /**
     * 主键标识
     */
    @Id
    @GeneratedValue(generator = "Course", strategy = GenerationType.AUTO)
    private long id;
    /**
     * 课程名称
     */
    private String name;
    /**
     * 课程所属系
     */
    @Column(name = "dep_id")
    private int depId;

    /**
     * 学分
     */
    private Integer score;
    /**
     * 创建时间
     */
    @Column(columnDefinition = "timestamp")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss" )
    private LocalDateTime ctm;

}
