package com.lion.canvas.path.repository;

import com.lion.canvas.path.entity.SchoolYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author
 * @date 2019/3/12
 */
@Repository
public interface SchoolYearRepository extends JpaRepository<SchoolYear, Integer> {
}
