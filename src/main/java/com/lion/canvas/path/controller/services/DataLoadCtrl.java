package com.lion.canvas.path.controller.services;

import com.lion.canvas.path.common.ResultBean;
import com.lion.canvas.path.entity.*;
import com.lion.canvas.path.repository.*;
import lombok.Data;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @date 2019/4/19 10:11
 */
@RestController
@CrossOrigin
public class DataLoadCtrl {
    @Autowired
    private DepartmentRepository depRep;
    @Autowired
    private CourseRepository couRep;
    @Autowired
    private StaffTableRepository stRep;
    @Autowired
    private StaffTableInfoRepository stInfoRep;
    @Autowired
    private CourseSectionRepository secRep;

    @RequestMapping(path = "/load/data")
    public ResultBean loadCsvData(@RequestParam MultipartFile file, @RequestParam String type) throws IOException {
        if ("student".equals(type)){
            List<Student> sList = loadStudentCSVData(file);
            return ResultBean.success(loadStudentData(sList));
        }else if ("professors".equals(type)){
            List<Professors> pList = loadProfessorsCSVData(file);
            return ResultBean.success(loadTeacherData(pList));
        }
        return ResultBean.success(0);
    }

    private int loadTeacherData(List<Professors> dataList){
        for (Professors p : dataList){
            StaffTable st = new StaffTable();
            st.setUserAccount(p.getEmail());
            st.setUserPassword(p.getPassword());
            st.setName(p.getName());
            //teacher
            st.setCategory(2);
            st.setCtm(LocalDateTime.now());
            st = stRep.save(st);
            StaffTableInfo stInfo = new StaffTableInfo();
            //Computer Science
            int depId = isExistDep(p.getDep());
            stInfo.setDep_id("[\"" + depId + "\"]");
            stInfo.setAge(p.getAge());
            stInfo.setEmail_address(p.getEmail());
            stInfo.setGender(p.getGender());
            stInfo.setHome_address("");
            stInfo.setJob_title(p.getTitle());
            stInfo.setOffice_location(p.getOffice());
            stInfo.setPersonal_id("");
            stInfo.setPostal_code("");
            stInfo.setId(st.getId());
            stInfoRep.save(stInfo);
        }
        return dataList.size();
    }
    private int loadStudentData(List<Student> dataList){
        for (Student p : dataList){
            StaffTable st = new StaffTable();
            st.setUserAccount(p.getEmail());
            st.setUserPassword(p.getPassword());
            st.setName(p.getName());
            //student
            st.setCategory(3);
            st.setCtm(LocalDateTime.now());
            st = stRep.save(st);
            StaffTableInfo stInfo = new StaffTableInfo();
            int depId = isExistDep(p.getMajor());
            stInfo.setDep_id("[\"" + depId + "\"]");
            stInfo.setAge(p.getAge());
            stInfo.setEmail_address(p.getEmail());
            stInfo.setGender(p.getGender());
            stInfo.setHome_address(p.getCity()+","+p.getState()+","+p.getStreet());
            stInfo.setJob_title("");
            stInfo.setOffice_location("");
            stInfo.setPersonal_id(p.getPhone());
            stInfo.setPostal_code(p.getZip());
            stInfo.setId(st.getId());
            stInfoRep.save(stInfo);
        }
        return dataList.size();
    }
    private int isExistDep(String depName){
       Department dep = depRep.findFirstByDepName(depName);
       if (dep != null){
           return dep.getId();
       }else{
           dep = new Department();
           dep.setDepName(depName);
           dep.setCtm(LocalDateTime.now());
           dep = depRep.save(dep);
           return dep.getId();
       }
    }

    private long isExistCourse(int depId, String courseName){
        Course course = couRep.findFirstByDepIdAndName(depId, courseName);
        if (course != null){
            return course.getId();
        }else{
            course = new Course();
            course.setDepId(depId);
            course.setName(courseName);
            course.setCtm(LocalDateTime.now());
            course.setScore(3);
            course = couRep.save(course);
            return course.getId();
        }
    }

    private List<Professors> loadProfessorsCSVData(MultipartFile file) throws IOException {
        Reader reader = new InputStreamReader(file.getInputStream());
        CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL.withHeader());
        List<Professors> resList = new ArrayList<>();
        try {
            for (CSVRecord record : parser){
                Professors p = new Professors();
                p.setName(record.get("Name"));
                p.setEmail(record.get("Email"));
                p.setPassword(record.get("Password"));
                p.setOffice(record.get("Office"));
                p.setTeaching(record.get("Teaching"));
                p.setTitle(record.get("Title"));
                p.setGender(record.get("Gender").equals("F") ? "female": "male");
                p.setDep(record.get("Department"));
                p.setDepName(record.get("Department Name"));
                p.setAge(Integer.parseInt(record.get("Age")));
                resList.add(p);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            parser.close();
            reader.close();
        }
        return resList;
    }

    private List<Student> loadStudentCSVData(MultipartFile file) throws IOException {
        Reader reader = new InputStreamReader(file.getInputStream());
        CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL.withHeader());
        List<Student> resList = new ArrayList<>();
        try {
            for (CSVRecord record : parser){
                Student p = new Student();
                p.setName(record.get("Full Name"));
                p.setEmail(record.get("Email"));
                p.setPassword(record.get("Password"));
                p.setGender(record.get("Gender").equals("F") ? "female": "male");
                p.setAge(Integer.parseInt(record.get("Age")));
                p.setCity(record.get("City"));
                p.setState(record.get("State"));
                p.setStreet(record.get("Street"));
                p.setMajor(record.get("Major"));
                p.setPhone(record.get("Phone"));
                p.setZip(record.get("Zip"));
                resList.add(p);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            parser.close();
            reader.close();
        }
        return resList;
    }
    @Data
    class Professors{
        // Name	Email	Password	Age	Gender	Department	Office	Department Name	Title	Team ID	Teaching
        private String name;
        private String email;
        private String password;
        private Integer age;
        private String gender;
        private String dep;
        private String depName;
        private String office;
        private String title;
        private String teaching;

    }

    @Data
    class Student{
//      Full Name	Email	Age	Zip	Phone	Gender	City	State	Password	Street	Major
        private String name;
        private String email;
        private Integer age;
        private String zip;
        private String phone;
        private String gender;
        private String city;
        private String state;
        private String password;
        private String street;
        private String major;

    }
}
