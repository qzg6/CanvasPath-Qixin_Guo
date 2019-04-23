package com.lion.canvas.path.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author
 * @date 2019/3/12
 * 学年表
 */
@Entity
@Data
@Accessors(chain = true)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
@SequenceGenerator(name = "SchoolYear", sequenceName = "SchoolYear_seq")
public class SchoolYear {
    @Id
    @GeneratedValue(generator = "SchoolYear", strategy = GenerationType.AUTO)
    private int id;
    private String tag;
    @Column(columnDefinition = "datetime")
    @JsonFormat(pattern = "YYYY-MM-dd" )
    private LocalDate sdt;
    @Column(columnDefinition = "datetime")
    @JsonFormat(pattern = "YYYY-MM-dd" )
    private LocalDate edt;
    @Column(columnDefinition = "timestamp")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss" )
    private LocalDateTime ctm;
}
