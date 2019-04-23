package com.lion.canvas.path.repository;

import com.lion.canvas.path.entity.CourseSectionEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author
 * @date 2019/3/12
 */
@Repository
public interface CourseSectionEntryRepository extends JpaRepository<CourseSectionEntry, Long> {
    /**
     * 查询所有项目
     * @param courseSectionId
     * @return
     */
    List<CourseSectionEntry> findAllByCourseSectionId(@Param(value = "courseSectionId")Long courseSectionId);
}
