package com.lion.canvas.path.controller.services;

import com.lion.canvas.path.common.ResultBean;
import com.lion.canvas.path.controller.services.dto.UserDTO;
import com.lion.canvas.path.controller.services.dto.UserInfoDTO;
import com.lion.canvas.path.entity.StaffTable;
import com.lion.canvas.path.entity.StaffTableInfo;
import com.lion.canvas.path.repository.StaffTableInfoRepository;
import com.lion.canvas.path.repository.StaffTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

/**
 * @author
 * @date 2019/3/31 17:49
 */
@RestController
@CrossOrigin
@RequestMapping(path = "/api")
public class UserCtrl {

    @Autowired
    private StaffTableRepository staffRep;
    @Autowired
    private StaffTableInfoRepository staffInfoRep;
    @RequestMapping(path = "/user/info")
    public ResultBean pullUserInfo(HttpSession session){
        StaffTable user = (StaffTable)session.getAttribute("user");
        return user == null ? ResultBean.error(new Exception("user not login")) : ResultBean.success( staffRep.findById(user.getId()));
    }
    @RequestMapping(path = "/user/details")
    public ResultBean pullUserDetails(HttpSession session){
        StaffTable user = (StaffTable)session.getAttribute("user");
        UserDTO userDTO = new UserDTO();
        Optional<StaffTable> staffTable = staffRep.findById(user.getId());
        Optional<StaffTableInfo> staffTableInfo = staffInfoRep.findById(user.getId());
        if (staffTable.isPresent()){
            StaffTable staff = staffTable.get();
            userDTO.setId(staff.getId());
            userDTO.setUser_password(staff.getUserPassword());
            userDTO.setName(staff.getName());
            userDTO.setCategory(staff.getCategory());
        }
        if (staffTableInfo.isPresent()){
            StaffTableInfo staffInfo = staffTableInfo.get();
            userDTO.setEmail_address(staffInfo.getEmail_address());
            userDTO.setAge(staffInfo.getAge());
            userDTO.setGender(staffInfo.getGender());
            userDTO.setJob_title(staffInfo.getJob_title());
            userDTO.setHome_address(staffInfo.getHome_address());
            userDTO.setOffice_location(staffInfo.getOffice_location());
            userDTO.setDep_id(staffInfo.getDep_id());
            userDTO.setPostal_code(staffInfo.getPostal_code());
            userDTO.setPersonal_id(staffInfo.getPersonal_id());
        }
        return ResultBean.success(userDTO);
    }

    /**
     * 用户修改
     */
    @RequestMapping(path = "/user/modify")
    public ResultBean pushUserModify(@Valid UserInfoDTO dt){
        if (dt.getId() != null){
            Optional<StaffTable> staffTable = staffRep.findById(dt.getId());
            if (staffTable.isPresent()){
                StaffTable stf = staffTable.get();
                stf.setUserPassword(dt.getUser_password());
                stf.setName(dt.getName());
                staffRep.save(stf);
                StaffTableInfo staffTableInfo = new StaffTableInfo()
                        .setAge(dt.getAge())
                        .setDep_id(dt.getDep_id())
                        .setEmail_address(dt.getEmail_address())
                        .setGender(dt.getGender())
                        .setHome_address(dt.getHome_address())
                        .setOffice_location(dt.getOffice_location())
                        .setJob_title(dt.getJob_title())
                        .setPersonal_id(dt.getPersonal_id())
                        .setPostal_code(dt.getPostal_code())
                        .setId(dt.getId());
                staffInfoRep.save(staffTableInfo);
                return ResultBean.success(dt.getId());
            }
        }
        return ResultBean.error(new Exception("id not exist"));
    }
}
