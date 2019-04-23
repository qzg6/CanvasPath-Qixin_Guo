package com.lion.canvas.path.repository;

import com.lion.canvas.path.entity.StaffTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author
 * @date 2019/3/12
 */
@Repository
public interface StaffTableRepository extends JpaRepository<StaffTable, Long> {
    /**
     * 按类别查找用户
     * @param category
     * @return
     */
    List<StaffTable> findAllByCategory(@Param(value = "category") Integer category);

    /**
     * 登陆
     * @param userAccount
     * @param userPassword
     * @return
     */
    StaffTable findFirstByUserAccountAndUserPassword(@Param(value = "userAccount") String userAccount, @Param(value = "userPassword") String userPassword);
}
