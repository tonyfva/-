package cn.heyige.excelpdf.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class ResumeAddDTO implements Serializable {

    private String name;

    private String sex;

    private String education;

    private Integer age;

    private String phone;

    private Integer workYear;

    private String email;

    private String address;

    private String position;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    private String salary;

    private String school;

    private String professional;

    private String skill;

    /**
     * 模板id
     */
    private Integer templateId;
}
