package cn.heyige.excelpdf;

import cn.heyige.excelpdf.domain.Student;
import cn.heyige.excelpdf.listener.ReadStudentListener;
import cn.heyige.excelpdf.repository.StudentRepository;
import cn.heyige.excelpdf.utils.ExcelUtil;
import com.alibaba.excel.EasyExcel;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

@SpringBootTest
class ExcelPdfApplicationTests {

    @Resource
    StudentRepository studentRepository;

    /**
     * 同步读取 数据读取并封装到实体类中
     */
    @Test
    void syncRead() {
        String fileName = "就业信息.xlsx";
        List<Student> students = EasyExcel.read(fileName).head(Student.class).sheet().doReadSync();
        students.forEach(System.out::println);
    }

    /**
     * 同步读取 数据读取并封装到Map中
     */
    @Test
    void syncReadToMap() {
        String fileName = "就业信息.xlsx";
        List<Map<String, Object>> students = EasyExcel.read(fileName).sheet().doReadSync();
        students.forEach(System.out::println);
    }

    @Test
    void readWithListener() {
        String fileName = "就业信息.xlsx";
        // 参数说明：1文件名 2实体类 3监听器
        EasyExcel.read(fileName, Student.class, new ReadStudentListener(studentRepository)).sheet().doRead();
    }


    @Test
    void writeExcel(){
        String outPutFileName = "学生信息.xlsx";
        // 从数据库查出来的数据
        List<Student> students = studentRepository.findAll();
        EasyExcel.write(outPutFileName,Student.class).sheet("学生就业统计").doWrite(students);
    }

    @Test
    void readExcelWithTools() throws FileNotFoundException {
        File file = new File("就业信息.xlsx");
        FileInputStream inputStream = new FileInputStream(file);
        List<Student> list = ExcelUtil.getExcelModelData(inputStream, Student.class);
        list.forEach(System.out::println);
    }

}
