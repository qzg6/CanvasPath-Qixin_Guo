package com.lion.canvas.path.controller.services.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author
 * @date 2019/3/29 18:22
 * 选课
 */
@Data
@Accessors(chain = true)
public class SlcSecDTO {
    /**
     * 课程节标识
     */
    private long id;
    /**
     * 课程节名称
     */
    private String sectionName;
    /**
     * 课程id
     */
    private Long courseId;
    /**
     * 教师id
     */
    private String teachers_id;

    /**
     * 数量
     */
    private String number;
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
     * 状态
     */
    private String status;
    /**
     * 是否选中
     */
    private String select;

    /**
     * 学分
     */
    private int score;
}
