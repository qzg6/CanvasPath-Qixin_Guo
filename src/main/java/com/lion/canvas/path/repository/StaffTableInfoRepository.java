package com.lion.canvas.path.repository;

import com.lion.canvas.path.entity.StaffTableInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author
 * @date 2019/3/12
 */
@Repository
public interface StaffTableInfoRepository extends JpaRepository<StaffTableInfo, Long> {

    /**
     * 检索用户完整信息
     * @param category
     * @param userName
     * @param pageable
     * @return
     */
    @Query(value = "select ctm, ta.id, ta.name, ta.user_account, ta.user_password, ta.category," +
            " b.personal_id, b.age, b.gender, b.email_address, b.home_address, b.office_location," +
            " b.job_title, b.dep_id, b.postal_code from staff_table as ta inner join staff_table_info b on ta.id = b.id where ta.category = :category and ta.name like :userName",
            countQuery = "select count(*) from staff_table where category = :category and name like :userName", nativeQuery = true)
    Page<Map<String, Object>> findUserInfo(@Param("category") Integer category, @Param("userName") String userName, Pageable pageable);

    /**
     * 检索指定用户
     * @param ids
     * @return
     */
    @Query(value = "select ctm, ta.id, ta.name, ta.category, b.age, b.gender, b.email_address, b.office_location," +
            " b.job_title, b.dep_id, b.personal_id, b.postal_code from staff_table as ta inner join staff_table_info b on ta.id = b.id where ta.id in ( :ids )", nativeQuery = true)
    List<Map<String, Object>> findUserList(@Param("ids")List<String> ids);
}
