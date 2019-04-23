package com.lion.canvas.path.repository;

import com.lion.canvas.path.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author
 * @date 2019/3/12
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    /**
     * 模糊查询课程
     * @param depId
     * @param name
     * @return
     */
    List<Course> findAllByDepIdAndNameLike(@Param(value = "depId") Integer depId, @Param(value = "name") String name);

    /**
     * 查询指定课程
     * @param depId
     * @param name
     * @return
     */
    Course findFirstByDepIdAndName(@Param(value = "depId") Integer depId, @Param(value = "name") String name);
}
