package com.lion.canvas.path.repository;

import com.lion.canvas.path.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author
 * @date 2019/3/12
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    Department findFirstByDepName(@Param(value = "depName") String depName);
}
