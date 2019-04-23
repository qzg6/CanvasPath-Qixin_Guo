package com.lion.canvas.path.repository;

import com.lion.canvas.path.entity.CourseSectionEntryScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author
 * @date 2019/3/12
 */
@Repository
public interface CourseSectionEntryScoreRepository extends JpaRepository<CourseSectionEntryScore, Long> {
    /**
     * 根据项目和学生id查找
     * @param courseSectionEntryId
     * @param studentsId
     * @return
     */
    List<CourseSectionEntryScore> findAllByCourseSectionEntryIdEqualsAndStudentsIdLike(@Param(value = "courseSectionEntryId") Long courseSectionEntryId,@Param(value = "studentsId") String studentsId);

    /**
     * 根据项目id删除
     * @param courseSectionId
     * @return
     */
    void deleteAllByCourseSectionEntryId(@Param(value = "courseSectionId") Long courseSectionId);

    /**
     * 根据项目id查找
     * @param courseSectionId
     * @return
     */
    List<CourseSectionEntryScore> findAllByCourseSectionEntryId(@Param(value = "courseSectionId") Long courseSectionId);
}
