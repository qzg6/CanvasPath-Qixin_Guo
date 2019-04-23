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
@SequenceGenerator(name = "Department", sequenceName = "Department_seq")
public class Department {
    /**
     * 系标识
     */
    @Id
    @GeneratedValue(generator = "Department", strategy = GenerationType.AUTO)
    private int id;
    /**
     * 系名称
     */
    @Column(name = "dep_name")
    private String depName;
    /**
     * 创建时间
     */
    @Column(columnDefinition = "timestamp")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss" )
    private LocalDateTime ctm;

}
