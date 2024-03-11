package cn.heyige.excelpdf.controller;

import cn.heyige.excelpdf.anno.TimeLog;
import cn.heyige.excelpdf.domain.Student;
import cn.heyige.excelpdf.repository.StudentRepository;
import cn.heyige.excelpdf.service.StudentService;
import cn.heyige.excelpdf.utils.ExcelUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("excel")
@RequiredArgsConstructor
public class ExcelController {

    final StudentService studentService;

    /**
     * 上传excel文件
     *
     * @param file 文件
     * @return {@link Object}
     * @throws IOException IOException
     */
    @PostMapping("upload")
    @TimeLog
    public Object uploadExcelFile(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        return ExcelUtil.getExcelModelData(inputStream, Student.class);
    }


    /**
     * 文件下载并且失败的时候返回json（默认失败了会返回一个有部分数据的Excel）
     *
     * @since 2.1.1
     */
    @GetMapping("api/download")
    @TimeLog
    public void downloadApi(HttpServletResponse response) throws IOException {
        List<Student> students = studentService.findAll();
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("学生就业信息", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), Student.class).autoCloseStream(Boolean.FALSE).sheet("模板")
                    .doWrite(students);
        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<String, String>();
            map.put("code", "500");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSON.toJSONString(map));
        }
    }

}
