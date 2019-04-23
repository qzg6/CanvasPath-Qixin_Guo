package com.lion.canvas.path.controller;

import com.lion.canvas.path.common.ResultBean;
import com.lion.canvas.path.entity.StaffTable;
import com.lion.canvas.path.repository.StaffTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;


/**
 * @author
 * @date 2019/3/12
 */
@Controller
@CrossOrigin
public class PageCtrl {
    @Autowired
    private StaffTableRepository staffRep;

    @RequestMapping(path = "/login", params = {"account", "password"})
    @ResponseBody
    public ResultBean loginPage(HttpSession session, String account, String password){
        StaffTable staffTable = staffRep.findFirstByUserAccountAndUserPassword(account, password);
        if (staffTable != null){
            session.setAttribute("user", staffTable);
            return ResultBean.success(staffTable);
        }else{
            return ResultBean.error(new Exception("landing failed！"));
        }
    }
}
