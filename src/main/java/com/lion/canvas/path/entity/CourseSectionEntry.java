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
@SequenceGenerator(name = "CourseSectionEntry", sequenceName = "CourseSectionEntry_seq")
public class CourseSectionEntry {
    /**
     * 章节条目标识
     */
    @Id
    @GeneratedValue(generator = "CourseSectionEntry", strategy = GenerationType.AUTO)
    private long id;
    /**
     * 条目标题
     */
    private String title;
    /**
     * 课程id
     */
    @Column(name = "course_id")
    private long courseId;
    /**
     * 章节id
     */
    @Column(name = "course_section_id")
    private long courseSectionId;
    /**
     * 类型 capstone general
     */
    private String category;
    /**
     * 创建时间
     */
    @Column(columnDefinition = "timestamp")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss" )
    private LocalDateTime ctm;

}
