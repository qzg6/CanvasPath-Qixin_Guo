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
@SequenceGenerator(name = "CourseSectionEntryScore", sequenceName = "CourseSectionEntryScore_seq")
public class CourseSectionEntryScore {
    /**
     * 主键标识
     */
    @Id
    @GeneratedValue(generator = "CourseSectionEntryScore", strategy = GenerationType.AUTO)
    private long id;
    /**
     * 章节名称
     */
    private String title;
    /**
     * 章节标识
     */
    @Column(name = "course_section_entry_id")
    private long courseSectionEntryId;
    /**
     * 支持信息
     */
    private String sponsor_info;
    /**
     * 学员id
     */
    @Column(name = "students_id")
    private String studentsId;
    /**
     * 得分
     */
    private double score;

    /**
     * 创建时间
     */
    @Column(columnDefinition = "timestamp")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss" )
    private LocalDateTime ctm;
}
