package cn.heyige.excelpdf.domain;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
// 标题行的高度
@HeadRowHeight(30)
// 列的宽度
@ColumnWidth(12)
// 内容行高
@ContentRowHeight(20)
@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ExcelIgnore
    private Integer id;

    @ExcelProperty("姓名")
    @ColumnWidth(10)
    private String name;

    @ExcelProperty("出生日期")
    @DateTimeFormat("yyyy/MM/dd")
    private Date birthday;

    @ExcelProperty("就业城市")
    private String city;

    @ExcelProperty("期望薪资")
    private Double salary;

    // @ExcelIgnore 在excel文件读写的时候忽略那些属性
    // private String password;

}
