package com.lion.canvas.path.repository;

import com.lion.canvas.path.entity.CourseSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author
 * @date 2019/3/12
 */
@Repository
public interface CourseSectionRepository extends JpaRepository<CourseSection, Long> {
    /**
     * 模糊查询课程节
     * @param courseId
     * @param sectionName
     * @return
     */
    List<CourseSection> findAllByCourseIdAndSectionNameLike(@Param(value = "courseId") Long courseId, @Param(value = "sectionName") String sectionName);

      /**
     * 获取课程列表
     * @param sectionName
     * @param students_id
     * @return
     */
    List<CourseSection> findAllBySectionNameLikeAndStudentsIdLike(@Param(value = "sectionName") String sectionName, @Param(value = "studentsId") String students_id);

    /**
     * 获取课程列表
     * @param sectionName
     * @param teachers_id
     * @return
     */
    List<CourseSection> findAllBySectionNameLikeAndTeachersIdLike(@Param(value = "sectionName") String sectionName, @Param(value = "teachersId") String teachers_id);
}
