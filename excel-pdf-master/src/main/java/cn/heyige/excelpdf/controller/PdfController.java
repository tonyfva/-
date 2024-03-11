package cn.heyige.excelpdf.controller;

import cn.heyige.excelpdf.domain.ResumeAddDTO;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;

@Slf4j
@RestController
@RequestMapping("pdf")
public class PdfController {

    /**
     * 导出pdf
     *
     * @param dto      PDF数据
     * @param response 响应流
     * @throws UnsupportedEncodingException 不支持编码异常
     */
    @PostMapping("exportPDF")
    public void exportPDF(@RequestBody ResumeAddDTO dto, HttpServletResponse response) throws UnsupportedEncodingException {
        // 指定解析器
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
                "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
        // 指定使用哪套PDF模板文件
        String path = "D:/pdf";
        String fileName = "template1.pdf";
        switch (dto.getTemplateId()) {
            case 1:
                fileName = "template1.pdf";
                break;
            case 2:
                fileName = "template2.pdf";
                break;
        }
        // 导出格式
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment;fileName=" + dto.getName() + ".pdf"
                + URLEncoder.encode(fileName, "UTF-8"));
        OutputStream os = null;
        PdfStamper ps = null;
        PdfReader reader = null;
        try {
            os = response.getOutputStream();
            // 2 读入pdf表单
            reader = new PdfReader(path + "/" + fileName);
            // 3 根据表单生成一个新的pdf
            ps = new PdfStamper(reader, os);
            // 4 获取pdf表单
            AcroFields form = ps.getAcroFields();
            // 5给表单添加中文字体 这里采用系统字体。不设置的话，中文可能无法显示
            BaseFont bf = BaseFont.createFont("C:/WINDOWS/Fonts/SIMSUN.TTC,1",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            form.addSubstitutionFont(bf);
            // 6遍历dto 给pdf表单表格赋值
            form.setField("name", dto.getName());
            form.setField("age", String.valueOf(dto.getAge()));
            form.setField("phone", dto.getPhone());
            form.setField("workyear", String.valueOf(dto.getWorkYear()));
            form.setField("position", dto.getPosition());
            form.setField("education", dto.getEducation());
            form.setField("email", dto.getEmail());
            form.setField("salary", dto.getSalary());
            form.setField("professional", dto.getProfessional());
            form.setField("sex", dto.getSex());
            form.setField("school", dto.getSchool());
            form.setField("skill", dto.getSkill());
            form.setField("address", dto.getAddress());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            form.setField("beginDate", sdf.format(dto.getBeginDate()));
            form.setField("endDate", sdf.format(dto.getEndDate()));

            ps.setFormFlattening(true);
            log.info("*******************PDF导出成功***********************");
        } catch (Exception e) {
            log.error("*******************PDF导出失败***********************");
            e.printStackTrace();
        } finally {
            try {
                ps.close();
                reader.close();
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
