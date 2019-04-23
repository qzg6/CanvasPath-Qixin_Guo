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
@SequenceGenerator(name = "CourseSection", sequenceName = "CourseSection_seq")
public class CourseSection {

    /**
     * 课程节标识
     */
    @Id
    @GeneratedValue(generator = "CourseSection", strategy = GenerationType.AUTO)
    private long id;
    /**
     * 课程节名称
     */
    @Column(name = "section_name")
    private String sectionName;
    /**
     * 课程id
     */
    @Column(name = "course_id")
    private Long courseId;
    /**
     * 教师id
     */
    @Column(name = "teachers_id")
    private String teachersId;
    /**
     * 学生id
     */
    @Column(name = "students_id")
    private String studentsId;
    /**
     * 数量
     */
    private int number;
    /**
     * 上课时间
     */
    private String days_time;
    /**
     * 持续时间
     */
    private String meeting_dates;
    /**
     * 课程房间
     */
    private String room;
    /**
     * 创建时间
     */
    @Column(columnDefinition = "timestamp")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss" )
    private LocalDateTime ctm;

}
