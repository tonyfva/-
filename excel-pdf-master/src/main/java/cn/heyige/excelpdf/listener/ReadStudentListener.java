package cn.heyige.excelpdf.listener;

import cn.heyige.excelpdf.domain.Student;
import cn.heyige.excelpdf.repository.StudentRepository;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.ListUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 读取Excel文件的监听器
 * 这个类不要被Spring管理
 *
 * @author Administrator
 * @date 2023/09/22
 */
@Slf4j
public class ReadStudentListener extends AnalysisEventListener<Student> {

    final StudentRepository studentRepository;

    // 批大小
    private static final Integer BATCH = 5;

    // 批量新增时的列表
    private List<Student> list = ListUtils.newArrayListWithExpectedSize(BATCH);

    public ReadStudentListener(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * 此方法 会在Excel文件没读到一行内容就调用一次
     *
     * @param student         大学生
     * @param analysisContext 分析上下文
     */
    @Override
    public void invoke(Student student, AnalysisContext analysisContext) {
        // log.info("读取到是数据是:{}", student);
        list.add(student);
        // 如果集合大于批量大小就到数据库保存一次
        if (list.size() > BATCH) {
            saveStudentList();
            // 清空list
            list = ListUtils.newArrayListWithExpectedSize(BATCH);
        }
    }

    /**
     * 整个文件读完了 只会调用一次这个方法
     *
     * @param analysisContext 分析上下文
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("文件读取完成");
        saveStudentList();
    }

    public void saveStudentList() {
        studentRepository.saveAll(list);
    }
}
